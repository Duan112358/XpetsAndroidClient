package com.emacs.xpets;

import com.emacs.http.AsyncHttpClient;
import com.emacs.http.AsyncHttpResponseHandler;
import com.emacs.http.RequestParams;
import com.emacs.xpets.adapters.BasePagerAdapter;
import com.emacs.xpets.utils.MLog;
import com.emacs.xpets.utils.Utils;
import com.generpoint.xpets.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.assist.FailReason;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImageDetailsActivity extends Activity {
	private ViewPager mViewPager;
	private DisplayImageOptions option;
	private ImageLoader loader = ImageLoader.getInstance();
	private final AsyncHttpClient client = new AsyncHttpClient();
	private RequestParams params = new RequestParams();
	
	private String[] titles;
	private String[] keys;
	private String[] images;
	private int current;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_viewer);

		Bundle bundle = getIntent().getExtras();
		images = bundle.getStringArray("images");
		keys = bundle.getStringArray("keys");
		titles = bundle.getStringArray("titles");
		
		if (savedInstanceState != null) {
			current = savedInstanceState.getInt("current");
		} else {
			current = bundle.getInt("current");
		}

		option = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo)
				.resetViewBeforeLoading().cacheOnDisc()
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		mViewPager = (ViewPager) findViewById(R.id.imageviewer);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(new ImagePagerAdapter(this));
		mViewPager.setCurrentItem(current);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("current", mViewPager.getCurrentItem());
	}

	private void sendImagesStats(int position) {
		params.put("key", keys[position]);
		params.put("machine", Utils.getMachineKey());
		params.put("from", "android:" + Build.VERSION.RELEASE);
		client.get(Utils.getBaseUrl() + "stats/pet?", params,
				new AsyncHttpResponseHandler());
	}

	private class ImagePagerAdapter extends BasePagerAdapter {

		private LayoutInflater inflater;

		public ImagePagerAdapter(Context context) {
			super(context);
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return keys.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, final int position) {
			View imageLayout = inflater.inflate(R.layout.item_detail, view,
					false);
			ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);
			final TextView textview = (TextView) imageLayout
					.findViewById(R.id.title);

			loader.displayImage(images[position], imageView, option,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
							sendImagesStats(position);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
							}
							MLog.error(message);
							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
							textview.setText(titles[position]);
						}
					});

			((ViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

	}
}
