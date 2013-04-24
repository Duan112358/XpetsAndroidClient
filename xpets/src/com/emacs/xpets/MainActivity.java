package com.emacs.xpets;

import com.emacs.xpets.adapters.GridPagerFragmentAdapter;
import com.generpoint.xpets.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {
	ImageLoader mImageLoader = ImageLoader.getInstance();
	GridPagerFragmentAdapter mSectionsAdapter;
	ViewPager mViewPager;

	/* Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSectionsAdapter = new GridPagerFragmentAdapter(
				getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();

		// Specify that we will be displaying tabs in the action bar
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsAdapter);
		mViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		actionBar.addTab(actionBar.newTab().setText(R.string.latest)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.recommend)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tags)
				.setTabListener(this));
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}
	
}
