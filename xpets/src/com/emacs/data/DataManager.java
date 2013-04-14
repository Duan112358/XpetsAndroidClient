package com.emacs.data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;

import com.emacs.models.Pet;
import com.emacs.xpets.utils.MLog;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	private Cursor cursor;

	public DataManager(Context context) {
		helper = new DBHelper(context);
	}

	public void clearDB() {
		db = helper.getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("delete from tbl_xpets");
			db.setTransactionSuccessful();
			MLog.i("tbl_xpets cleared successfully.");
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

	public LinkedList<Pet> getPetsByIDs(Set<String> ids) {
		LinkedList<Pet> pets = new LinkedList<Pet>();
		
		db = helper.getReadableDatabase();
		cursor = db.rawQuery("select * from tbl_xpets",new String[]{});

		if (!cursor.moveToFirst()) {
			cursor.close();
			db.close();
			helper.close();
			MLog.i("no record found in ids : " + ids);
		} else {
			Pet pet;
			do {
				String key = cursor.getString(cursor.getColumnIndex("key"));
				if(ids.contains(key)){
					pet = new Pet();
					pet.setKey(key);
					pet.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
					String temp = cursor.getString(cursor.getColumnIndex("tags"));
					pet.setTags(temp.substring(1, temp.length() - 2).split(","));
					pet.setThumbnail(cursor.getString(cursor
							.getColumnIndex("thumbnail")));
					pet.setTitle(cursor.getString(cursor.getColumnIndex("title")));
					pets.add(pet);
					MLog.i("loaded from db pet :" +pet.toString());
				}
			} while (cursor.moveToNext());
			cursor.close();
			db.close();
			helper.close();
			MLog.i("record querying completed.");
		}

		return pets;
	}

	public void savePets(LinkedList<Pet> pets) {
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
				MLog.i("Inserted record : "+p.toString());
			}
			db.setTransactionSuccessful();
			MLog.i("record inserting completed.");
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

	public boolean isExist(String key) {
		db = helper.getReadableDatabase();
		cursor = db.rawQuery("select count(*) from tbl_xpets where key = ?",
				new String[] { key });
		boolean result = cursor.moveToFirst();
		cursor.close();
		db.close();
		helper.close();
		return result;
	}

	private LinkedList<String> getAllKeys(SQLiteDatabase db) {
		LinkedList<String> keys = new LinkedList<String>();
		cursor = db.rawQuery("select distinct key from tbl_xpets",
				new String[] {});

		if (cursor.moveToFirst()) {
			do {
				keys.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}

		cursor.close();
		MLog.i(Arrays.toString(keys.toArray()));
		return keys;
	}
}
