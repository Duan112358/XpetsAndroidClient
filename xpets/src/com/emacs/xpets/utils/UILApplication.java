package com.emacs.xpets.utils;

import android.app.Application;
import android.content.Context;

import com.emacs.data.DataManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class UILApplication extends Application {
	@Override
	public void onCreate() {
		/*
		 * if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD){
		 * StrictMode.setThreadPolicy(new
		 * StrictMode.ThreadPolicy.Builder().detectAll
		 * ().penaltyDialog().build()); StrictMode.setVmPolicy(new
		 * StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build()); }
		 */
		super.onCreate();
		initImageLoader(getApplicationContext());
		DataManager.initialize(this);
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		DataManager.dispose();
	}
}
