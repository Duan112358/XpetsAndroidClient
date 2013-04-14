package com.emacs.xpets.utils;

public class Constants {
	public static final int LATEST = 0;
	public static final int RECOMMEND = 1;
	public static final int TAGS = 2;
	public static final int By_TAG = 3;
	
	public static final String  GET_LATEST = "pet/GetLatest?";
	public static final String GET_RECOMMEND = "pet/GetRecommend?";
	public static final String GET_TAGS = "pet/GetAllTags?_=-1";
	public static final String GET_BY_TAG = "pet/GetPetsByTag?";
	
	public static final String IMAGE_CACHE_DIR = "images";

	public static final String TAG = "XPets";
	public static final String ERROR = "XPets.Error";
	
	public static final String OFFLINE_DATA = "offline_data";
	public static final String MACHINE_KEY = "machine_key";

	public static final int PAGE_SIZE = 16;
	public static final String DB = "xpets";
	public static final int DB_VERSION = 1;
}
