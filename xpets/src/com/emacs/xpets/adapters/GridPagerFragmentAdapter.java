package com.emacs.xpets.adapters;

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
			fragment = new GridLatestFragment();
			return fragment;
		case Constants.RECOMMEND:
			fragment = new GridRecommendFragment();
			return fragment;
		case Constants.TAGS:
			fragment = new GridTagsFragment();
			return fragment;
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

}
