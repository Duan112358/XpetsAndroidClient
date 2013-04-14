/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.emacs.xpets.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;

import com.emacs.models.Pet;
import com.generpoint.xpets.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

/**
 * Class containing some static utility methods.
 */
public class Utils {

	private Utils() {
	};

	public static DisplayImageOptions getImageDisplayOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.empty_photo)
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	public static String getMachineKey() {
		String value = System.getProperty(Constants.MACHINE_KEY);
		if (value == null) {
			value = UUID.randomUUID().toString();
			System.setProperty(Constants.MACHINE_KEY, value);
		}
		return value;
	}

	public static String getBaseUrl() {
		String value = System.getProperty("BASE_URL");
		if (value == null) {
			return "http://www.xpets.net/api/";
		}
		return value;
	}

	public static Set<String> getOfflineDataSet(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				Constants.OFFLINE_DATA, Context.MODE_PRIVATE);
		if (sp.contains(key)) {
			return sp.getStringSet(key, new HashSet<String>());
		}
		return new HashSet<String>();
	}

	public static void saveStringSet(Context context, String key,
			Set<String> tags) {
		SharedPreferences sp = context.getSharedPreferences(
				Constants.OFFLINE_DATA, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		if (sp.contains(key)) {
			editor.remove(key);
		}
		editor.putStringSet(key, tags);
		editor.commit();
	}

	public static LinkedList<String> parseJsonArrayToStringSet(JSONArray array) {
		LinkedList<String> values = new LinkedList<String>();
		for (int i = 0; i < array.length(); i++) {
			try {
				values.add(array.getString(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	public static String[] parseJsonArrayToStringArray(JSONArray array) {
		String[] values = new String[array.length()];
		for (int i = 0; i < array.length(); i++) {
			try {
				values[i] = array.getString(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	public static boolean isNetworkAvailable(Context context) {
		boolean status = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null
					&& netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = true;
			} else {
				netInfo = cm.getNetworkInfo(1);
				if (netInfo != null
						&& netInfo.getState() == NetworkInfo.State.CONNECTED)
					status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return status;
	}
	
	public static Bundle extractPropertiesIntoBundle(List<Pet> pets){
		Bundle bundle = new Bundle();
		int i = 0;
		String[] titles = new String[pets.size()];
		String[] images = new String[pets.size()];
		String[] keys = new String[pets.size()];
		for(Pet p : pets){
			titles[i] = p.getTitle();
			images[i] = p.getPhoto();
			keys[i] = p.getKey();
			i++;
		}
		bundle.putStringArray("keys", keys);
		bundle.putStringArray("titles", titles);
		bundle.putStringArray("images", images);
		return bundle;
	}

}