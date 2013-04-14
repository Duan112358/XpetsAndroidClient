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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class GridRecommendFragment extends GridFragment<Pet> {
	private GridImageAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new GridImageAdapter(getActivity(), mListItems);
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

		if (!Utils.isNetworkAvailable(getActivity())) {
			Set<String> keys = Utils.getOfflineDataSet(getActivity(),
					Constants.GET_RECOMMEND);
			if (keys != null && keys.size() > 0) {
				mListItems.addAll(new DataManager(getActivity())
						.getPetsByIDs(keys));
				mAdapter.notifyDataSetChanged();
			}
		} else {
			loadData(-1, Constants.PAGE_SIZE);
		}
		return rootView;
	}

	private void loadData(int index, int pageSize) {
		MLog.i("Loading recommend images begining...");
		addRequestParameter("i", index + "");
		addRequestParameter("n", pageSize + "");

		loadData(DataType.Recommend);
	}

	@Override
	protected void onDataLoadingSuccessed() {
		MLog.i("Loading recommend completed.");
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
		loadData(mListItems.size() - 1, Constants.PAGE_SIZE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mKeys.size() == 0) {
			return;
		}
		Utils.saveStringSet(getActivity(), Constants.GET_RECOMMEND,
				mKeys);
		new DataManager(getActivity()).savePets(mListItems);
	}

}