package com.emacs.data;

import com.emacs.xpets.utils.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DBHelper(Context context) {
		this(context, Constants.DB, null, Constants.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists tbl_xpets(key varchar(300) primary key , "
				+ "photo varchar(300) not null,"
				+ "tags varchar(300) not null,"
				+ "thumbnail varchar(300) not null,"
				+ "title varvhar(500) not null);");
		db.execSQL("create table if not exists tbl_xpets_offline_data(key varchar(100) primary key," +
				"value varchar(2000);)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists tbl_xpets;drop table if exists tbl_xpets_offline_data");
		db.execSQL("create table if not exists tbl_xpets(key varchar(300) primary key ,"
				+ "photo varchar(300) not null,"
				+ "tags varchar(300) not null,"
				+ "thumbnail varchar(300) not null,"
				+ "title varvhar(500) not null);");
		db.execSQL("create table if not exists tbl_xpets_offline_data(key varchar(100) primary key," +
				"value varchar(2000);)");
	}

}
