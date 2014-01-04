package com.emacs.xpets.adapters;

import com.emacs.data.DataManager;
import com.emacs.xpets.fragments.GridLatestFragment;
import com.emacs.xpets.fragments.GridRecommendFragment;
import com.emacs.xpets.fragments.GridTagsFragment;
import com.emacs.xpets.utils.Constants;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class GridPagerFragmentAdapter extends FragmentStatePagerAdapter {

	public GridPagerFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment;
		switch (arg0) {
		case Constants.LATEST:
			if(DataManager.isFragmentCached(Constants.LATEST)){
				return DataManager.getFragment(Constants.LATEST);
			}else{
				fragment = new GridLatestFragment();
				DataManager.cacheFragment(Constants.LATEST, fragment);
				return fragment;
			}
		case Constants.RECOMMEND:
			if(DataManager.isFragmentCached(Constants.RECOMMEND)){
				return DataManager.getFragment(Constants.RECOMMEND);
			}else{
				fragment = new GridRecommendFragment();
				DataManager.cacheFragment(Constants.RECOMMEND, fragment);
				return fragment;
			}
		case Constants.TAGS:
			if(DataManager.isFragmentCached(Constants.TAGS)){
				return DataManager.getFragment(Constants.TAGS);
			}else{
				fragment = new GridTagsFragment();
				DataManager.cacheFragment(Constants.TAGS, fragment);
				return fragment;
			}
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

}
