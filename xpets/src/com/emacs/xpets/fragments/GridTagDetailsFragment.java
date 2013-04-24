package com.emacs.xpets.fragments;

import java.util.Set;

import com.emacs.data.DataManager;
import com.emacs.models.Pet;
import com.emacs.pulltorefresh.PullToRefreshBase;
import com.emacs.pulltorefresh.PullToRefreshBase.Mode;
import com.emacs.pulltorefresh.PullToRefreshGridView;
import com.emacs.xpets.ImageDetailsActivity;
import com.emacs.xpets.adapters.GridImageAdapter;
import com.emacs.xpets.utils.Constants;
import com.emacs.xpets.utils.DataType;
import com.emacs.xpets.utils.MLog;
import com.emacs.xpets.utils.Utils;
import com.generpoint.xpets.R;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class GridTagDetailsFragment extends GridFragment<Pet> {
	private static String currentTag;
	private GridImageAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAdapter = new GridImageAdapter(getActivity(), mListItems);
		currentTag = getArguments().getString("tag");
		MLog.i(currentTag);

		if (!Utils.isNetworkAvailable(getActivity())) {
			Set<String> keys = Utils.getOfflineDataSet(getActivity(),
					currentTag);
			if (keys != null && keys.size() > 0) {
				mListItems.addAll(new DataManager(getActivity())
						.getPetsByIDs(keys));
				mAdapter.notifyDataSetChanged();
			}
		} else {
			loadData(currentTag, "", 0, Constants.PAGE_SIZE);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.image_grid, null);
		ptrGridView = (PullToRefreshGridView) rootView
				.findViewById(R.id.image_grid);

		ptrGridView.setMode(Mode.PULL_FROM_END);
		ptrGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(inflater.getContext(),
						ImageDetailsActivity.class);
				Bundle bundle = Utils.extractPropertiesIntoBundle(mListItems);
				bundle.putInt("current", position);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
		ptrGridView.setOnRefreshListener(this);
		ptrGridView.setAdapter(mAdapter);

		return rootView;
	}

	private void sendTagsStats() {
		params.put("name", currentTag);
		params.put("machine", Utils.getMachineKey(getActivity()));
		params.put("from", "android:" + Build.VERSION.RELEASE);

		client.get(Utils.getBaseUrl(getActivity()) + "stats/tag?", params, null);
	}

	private void loadData(String tag, String key, int index, int pageSize) {
		MLog.i("Loading recommend images begining...");

		addRequestParameter("tag", tag);
		addRequestParameter("key", key);
		addRequestParameter("d", index + "");
		addRequestParameter("n", pageSize + "");

		loadData(DataType.ByTag);
	}

	@Override
	protected void onDataLoadingSuccessed() {
		MLog.i("Loading recommend completed.");
		sendTagsStats();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDataLoadingFinished() {
		ptrGridView.onRefreshComplete();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		int size = mListItems.size();
		loadData(currentTag, size > 0 ? mListItems.getLast().getKey() : "",
				size > 0 ? size - 1 : 0, Constants.PAGE_SIZE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mKeys.size() == 0) {
			return;
		}
		Utils.saveStringSet(getActivity(), currentTag, mKeys);
		new DataManager(getActivity()).savePets(mListItems);
	}

}
