package com.emacs.xpets.fragments;

import java.util.Arrays;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.emacs.data.DataManager;
import com.emacs.http.AsyncHttpClient;
import com.emacs.http.AsyncHttpResponseHandler;
import com.emacs.http.JsonHttpResponseHandler;
import com.emacs.http.RequestParams;
import com.emacs.models.Pet;
import com.emacs.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.emacs.xpets.utils.DataType;
import com.emacs.xpets.utils.MLog;
import com.emacs.xpets.utils.Utils;
import com.generpoint.xpets.R;
import android.os.Build;
import android.widget.GridView;
import android.widget.Toast;

public abstract class GridFragment<T> extends BaseFragment implements
		OnRefreshListener2<GridView> {
	//"http://www.xpets.net/api/ping/GetApiSite?"
	private static final String REQUEST_URL = "http://www.xpets.net/api/ping/GetApiSite?";
	private static String BASE_URL = "";

	protected static AsyncHttpClient client = new AsyncHttpClient();
	protected static RequestParams params = new RequestParams();
	protected boolean isRefreshing = false;
	protected boolean isString = false;
	private boolean isloading = false;
	protected LinkedList<String> mKeys = new LinkedList<String>();
	protected LinkedList<T> mListItems = new LinkedList<T>();
	protected DataManager db = new DataManager(this.getActivity());

	protected abstract void onDataLoadingSuccessed();

	protected abstract void onDataLoadingFinished();

	protected void addRequestParameter(String key, String value) {
		params.put(key, value);
	}

	protected void loadData(DataType url) {
		if (isloading) {
			return;
		}

		if (!Utils.isNetworkAvailable(getActivity())) {
			Toast.makeText(getActivity(), R.string.network_error_content,
					Toast.LENGTH_SHORT).show();

			onDataLoadingFinished();

		} else {
			isloading = true;

			params.put("machine", Utils.getMachineKey(getActivity()));
			params.put("from", "android:" + Build.VERSION.RELEASE);

			if (BASE_URL == "") {
				loadBaseUrl(url);
			} else {
				beginDataLoading(url);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void praseJsonData(JSONArray content) {
		try {
			if (isString) {
				mKeys.clear();
				mListItems.clear();
				for (int i = 0; i < content.length(); i++) {
					String tag = content.getString(i);
					if (mKeys.contains(tag))
						continue;
					mListItems.add((T) tag);
					mKeys.add(tag);
				}
				MLog.i("Tags refreshing completed.");
			} else {
				Pet pet;
				for (int i = 0; i < content.length(); i++) {
					JSONObject obj = content.getJSONObject(i);

					String key = obj.getString("key");
					if (mKeys.contains(key)) {
						continue;
					}

					pet = new Pet();
					pet.setKey(key);
					pet.setPhoto(obj.getString("photo"));
					pet.setTags(Utils.parseJsonArrayToStringArray(obj
							.getJSONArray("tags")));
					pet.setThumbnail(obj.getString("thumbnail"));
					pet.setTitle(obj.getString("title"));

					mKeys.add(key);
					if (isRefreshing) {
						mListItems.addFirst((T) pet);
					} else {
						mListItems.add((T) pet);
					}
				}
				MLog.i("Pets loaded completed. count : " + content.length());
			}
		} catch (JSONException e) {
			MLog.error(e.getMessage());
			MLog.error(Arrays.toString(e.getStackTrace()));
		}
		onDataLoadingSuccessed();
	}

	private void loadBaseUrl(final DataType url) {
		if (client == null) {
			client = new AsyncHttpClient();
		}
		client.get(REQUEST_URL, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				MLog.error(content);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				String baseUrl = content.substring(1, content.length() - 1);
				
				BASE_URL = baseUrl;
				
				MLog.i(baseUrl);
				String original = Utils.getBaseUrl(getActivity());
				
				if (original == null || !original.equalsIgnoreCase(baseUrl)) {
					Utils.saveBaseUrl(getActivity(), baseUrl);
				} else {
					if (original != content) {
						Utils.saveBaseUrl(getActivity(), baseUrl);
					}
				}
				beginDataLoading(url);
			}

		});
	}

	private void beginDataLoading(DataType url) {
		if (client == null) {
			client = new AsyncHttpClient();
		}

		client.get(Utils.getBaseUrl(getActivity()) + url.getUrl(), params,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONArray response) {
						praseJsonData(response);
						MLog.i("Data loading completed. Retrieve data size : "
								+ response.length());
					}

					@Override
					public void onFailure(Throwable e, JSONArray errorResponse) {
						MLog.error(e.getMessage());
						MLog.error(errorResponse.toString());
						Toast.makeText(getActivity(), R.string.network_error,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						onDataLoadingFinished();
						isloading = false;
					}
				});
		MLog.i(AsyncHttpClient.getUrlWithQueryString(url.getUrl(), params));
	}

}
