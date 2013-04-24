package com.emacs.xpets;

import com.emacs.xpets.fragments.GridTagDetailsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class TagDetailsActivity extends FragmentActivity {
	private static final String TAG = "TagDetails";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String tag = getIntent().getStringExtra("tag");
		setTitle(tag);

		if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			Fragment f = new GridTagDetailsFragment();
			Bundle bundle = new Bundle();
			bundle.putString("tag", tag);
			f.setArguments(bundle);
			ft.add(android.R.id.content, f, TAG);
			ft.commit();
		}
	}
	
}
