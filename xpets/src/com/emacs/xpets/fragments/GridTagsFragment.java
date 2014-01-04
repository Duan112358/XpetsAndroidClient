package com.emacs.xpets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.emacs.data.DataManager;
import com.emacs.pulltorefresh.PullToRefreshBase;
import com.emacs.pulltorefresh.PullToRefreshBase.Mode;
import com.emacs.pulltorefresh.PullToRefreshGridView;
import com.emacs.xpets.TagDetailsActivity;
import com.emacs.xpets.utils.Constants;
import com.emacs.xpets.utils.DataType;
import com.emacs.xpets.utils.Utils;
import com.generpoint.xpets.R;

public class GridTagsFragment extends GridFragment<String> {
	private ArrayAdapter<String> mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, mListItems);
		isString = true;

		if (!Utils.isNetworkAvailable(getActivity())) {
			mListItems.addAll(DataManager.getOfflineData("tags"));
			mAdapter.notifyDataSetChanged();
		} else {
			loadData(DataType.AllTags);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.image_grid, container, false);
		ptrGridView = (PullToRefreshGridView) rootView
				.findViewById(R.id.image_grid);

		ptrGridView.setAdapter(mAdapter);
		ptrGridView.setOnRefreshListener(this);
		ptrGridView.setMode(Mode.PULL_FROM_END);

		ptrGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String currentTag = mListItems.get(position);
				Intent intent = new Intent(inflater.getContext(),
						TagDetailsActivity.class);
				intent.putExtra("tab", Constants.TAGS);
				intent.putExtra("tag", currentTag);
				startActivity(intent);
			}
		});

		return rootView;
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
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		String label = DateUtils.formatDateTime(this.getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		loadData(DataType.AllTags);
	}

	@Override
	public void onDestroy() {
		if (mListItems.size() > 0) {
			DataManager.saveOfflineData("tags", mListItems);
		}
		
		super.onDestroy();
	}
}
