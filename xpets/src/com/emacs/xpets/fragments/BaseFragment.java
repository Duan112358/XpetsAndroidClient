package com.emacs.xpets.fragments;

import com.emacs.pulltorefresh.PullToRefreshGridView;
import com.generpoint.xpets.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseFragment extends Fragment {
	protected ImageLoader mImageLoader = ImageLoader.getInstance();
	protected PullToRefreshGridView ptrGridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		applyScrollListener();
	}

	private void applyScrollListener() {
		ptrGridView.setOnScrollListener(new PauseOnScrollListener(mImageLoader,
				false, true));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_clear_memory_cache:
			mImageLoader.clearMemoryCache();
			return true;
		case R.id.item_clear_disc_cache:
			mImageLoader.clearDiscCache();
			return true;
		default:
			return false;
		}
	}
}
