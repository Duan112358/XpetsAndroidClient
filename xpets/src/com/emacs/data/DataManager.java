package com.emacs.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;

import com.emacs.models.Pet;
import com.emacs.xpets.utils.MLog;

@SuppressLint("UseSparseArrays")
public final class DataManager {
	private static DBHelper helper;
	private static SQLiteDatabase db;
	private static Cursor cursor;
	private static HashMap<Integer, Fragment> fragmentCache = new HashMap<Integer, Fragment>(3);
	private static boolean isInitialized = false;

	private DataManager() {

	}

	public static synchronized void dispose() {
		helper.close();
		helper = null;
		isInitialized = false;
	}

	public static synchronized void initialize(Context context) {
		if (isInitialized) {
			MLog.i("DataManager already initialized!");
			return;
		}

		helper = new DBHelper(context);
		isInitialized = true;
		MLog.i("DataManager initialized successfully!");
	}

	public static synchronized void clearDB() {
		db = helper.getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("delete from tbl_xpets");
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			MLog.error("clear database failed.");
			MLog.error(e.getMessage());
			MLog.error(Arrays.toString(e.getStackTrace()));
		} finally {
			db.endTransaction();
			db.close();
			helper.close();
		}
	}

	public static synchronized LinkedList<Pet> getPetsByIDs(
			LinkedList<String> keys) {
		LinkedList<Pet> pets = new LinkedList<Pet>();

		db = helper.getReadableDatabase();
		cursor = db.rawQuery("select * from tbl_xpets", new String[] {});

		if (!cursor.moveToFirst()) {
			cursor.close();
			db.close();
		} else {
			Pet pet;
			do {
				String key = cursor.getString(cursor.getColumnIndex("key"));
				if (keys.contains(key)) {
					pet = new Pet();
					pet.setKey(key);
					pet.setPhoto(cursor.getString(cursor
							.getColumnIndex("photo")));
					String temp = cursor.getString(cursor
							.getColumnIndex("tags"));
					pet.setTags(temp.substring(1, temp.length() - 2).split(","));
					pet.setThumbnail(cursor.getString(cursor
							.getColumnIndex("thumbnail")));
					pet.setTitle(cursor.getString(cursor
							.getColumnIndex("title")));
					pets.add(pet);
				}
			} while (cursor.moveToNext());
			cursor.close();
			db.close();
		}

		helper.close();

		return pets;
	}

	public static synchronized void saveOfflineData(String key,
			LinkedList<String> value) {

		db = helper.getWritableDatabase();
		db.beginTransaction();
		try {
			cursor = db.rawQuery(
					"select value from tbl_xpets_offline_data where key = ?",
					new String[] { key });
			if (cursor.moveToFirst()) {
				db.execSQL(
						"update tbl_xpets_offline_data set value = ? where key = ?",
						new String[] { key, value.toString() });
			} else {
				db.execSQL("insert into tbl_xpets_offline_data values(?,?)",
						new String[] { key, value.toString() });
			}
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			MLog.error("Offline data saved falied.");
			MLog.error(e.getMessage());
			MLog.error(Arrays.toString(e.getStackTrace()));
		} finally {
			cursor.close();
			db.endTransaction();
			db.close();
			helper.close();
		}
	}

	public static synchronized LinkedList<String> getOfflineData(String key) {
		LinkedList<String> result = new LinkedList<String>();

		db = helper.getReadableDatabase();
		cursor = db.rawQuery(
				"select value from tbl_xpets_offline_data where key = ?",
				new String[] { key });
		if (cursor.moveToFirst()) {
			String temp = cursor.getString(cursor.getColumnIndex("value"));
			String[] temp_result = temp.substring(1, temp.length() - 2).split(
					",");

			for (String s : temp_result) {
				result.add(s);
			}
		} else {
			result = new LinkedList<String>();
		}
		cursor.close();
		db.close();
		helper.close();

		return result;
	}

	public static synchronized void savePets(LinkedList<Pet> pets) {

		if (pets == null || pets.size() == 0) {
			return;
		}

		db = helper.getWritableDatabase();
		db.beginTransaction();

		try {
			LinkedList<String> existsKeys = getAllKeys(db);
			for (Pet p : pets) {
				if (existsKeys.contains(p.getKey()))
					continue;
				db.execSQL(
						"insert into tbl_xpets values(?,?,?,?,?)",
						new String[] { p.getKey(), p.getPhoto(),
								Arrays.toString(p.getTags()), p.getThumbnail(),
								p.getTitle() });
				existsKeys.add(p.getKey());
			}
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			MLog.error("Record data into database falied.");
			MLog.error(e.getMessage());
			MLog.error(Arrays.toString(e.getStackTrace()));
		} finally {
			db.endTransaction();
			db.close();
			helper.close();
		}
	}

	public static synchronized boolean isExist(String key) {
		db = helper.getReadableDatabase();
		cursor = db.rawQuery("select count(*) from tbl_xpets where key = ?",
				new String[] { key });
		boolean result = cursor.moveToFirst();
		cursor.close();
		db.close();
		helper.close();

		return result;
	}

	private static synchronized LinkedList<String> getAllKeys(SQLiteDatabase db) {
		LinkedList<String> keys = new LinkedList<String>();

		if(!db.isOpen()){
			db = helper.getWritableDatabase();
		}
		
		cursor = db.rawQuery("select distinct key from tbl_xpets",
				new String[] {});

		if (cursor.moveToFirst()) {
			do {
				keys.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}

		cursor.close();

		return keys;
	}
	
	public static synchronized void cacheFragment(int tag, Fragment fragment){
		if(!fragmentCache.containsKey(tag)){
			fragmentCache.put(tag, fragment);
		}
	}
	
	public static synchronized boolean isFragmentCached(int key){
		return fragmentCache.containsKey(key);
	}
	
	public static synchronized Fragment getFragment(int key){
		return fragmentCache.get(key);
	}
}
