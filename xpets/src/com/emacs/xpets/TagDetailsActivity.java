package com.emacs.xpets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

import com.emacs.data.DataManager;
import com.emacs.xpets.fragments.GridTagDetailsFragment;
import com.emacs.xpets.utils.Constants;

public class TagDetailsActivity extends FragmentActivity {
	private static final String TAG = "TagDetails";
	private static int previousTabIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent parentIntent = getIntent();
		String tag = parentIntent.getStringExtra("tag");
		previousTabIndex = parentIntent.getIntExtra("tab", 0);
		setTitle(tag);

		if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			Fragment f ;
			if(DataManager.isFragmentCached(Constants.By_TAG)){
				f = DataManager.getFragment(Constants.By_TAG);
			}else{
				f = new GridTagDetailsFragment();
				DataManager.cacheFragment(Constants.By_TAG, f);
			}
			Bundle bundle = new Bundle();
			bundle.putString("tag", tag);
			f.setArguments(bundle);
			ft.add(android.R.id.content, f, TAG);
			ft.commit();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			navigateBackUp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void navigateBackUp(){
		Intent upIntent = NavUtils.getParentActivityIntent(this);
		upIntent.putExtra("tab", previousTabIndex);
		//This activity is NOT part of this app's task, so create
		// a new task when navigating up, with a synthesized back stack.
		if(NavUtils.shouldUpRecreateTask(this, upIntent)){
			//Add all of this activity's parents to the back stack and navigate to the closest parent
			TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
		}else{//This activity is part of this app's task, so simply navigate up to the logical parent
			NavUtils.navigateUpTo(this, upIntent);
		}
	}
}
