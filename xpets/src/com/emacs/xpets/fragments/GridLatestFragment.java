package com.emacs.xpets.fragments;

import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.emacs.data.DataManager;
import com.emacs.models.Pet;
import com.emacs.pulltorefresh.PullToRefreshBase;
import com.emacs.pulltorefresh.PullToRefreshBase.Mode;
import com.emacs.pulltorefresh.PullToRefreshGridView;
import com.emacs.xpets.ImageDetailsActivity;
import com.emacs.xpets.adapters.GridImageAdapter;
import com.emacs.xpets.utils.Constants;
import com.emacs.xpets.utils.DataType;
import com.emacs.xpets.utils.Utils;
import com.generpoint.xpets.R;

public class GridLatestFragment extends GridFragment<Pet> {
	private GridImageAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new GridImageAdapter(getActivity(), mListItems);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_grid, null);
		ptrGridView = (PullToRefreshGridView) v.findViewById(R.id.image_grid);
		ptrGridView.setAdapter(mAdapter);
		ptrGridView.setMode(Mode.BOTH);
		ptrGridView.setOnRefreshListener(this);

		ptrGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						ImageDetailsActivity.class);
				Bundle bundle = Utils.extractPropertiesIntoBundle(mListItems);
				bundle.putInt("current", position);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		init();
		return v;
	}

	private void init() {
		if (!Utils.isNetworkAvailable(getActivity())) {
			Set<String> keys = Utils.getOfflineDataSet(getActivity(),
					Constants.GET_LATEST);
			if (keys != null && keys.size() > 0) {
				mListItems.addAll(new DataManager(getActivity())
						.getPetsByIDs(keys));
				mAdapter.notifyDataSetChanged();
			}
		} else {
			loadData("", Constants.PAGE_SIZE, 0);
		}
	}

	private void loadData(String key, int pageSize, int direction) {
		Log.i(Constants.TAG, "Loading latest images begining...");
		addRequestParameter("key", key);
		addRequestParameter("d", direction + "");
		addRequestParameter("n", pageSize + "");

		loadData(DataType.Latest);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		String key = "";
		isRefreshing = true;
		if (mListItems.size() > 0) {
			key = mListItems.getFirst().getKey();
		}
		loadData(key, Constants.PAGE_SIZE, 1);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		String key = "";
		isRefreshing = false;
		if (mListItems.size() > 0) {
			key = mListItems.getLast().getKey();
		}
		loadData(key, Constants.PAGE_SIZE, 0);
	}

	@Override
	protected void onDataLoadingSuccessed() {
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDataLoadingFinished() {
		ptrGridView.onRefreshComplete();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mKeys.size() == 0) {
			return;
		}
		Utils.saveStringSet(getActivity(), Constants.GET_LATEST, mKeys);
		new DataManager(getActivity()).savePets(mListItems);
	}
}
