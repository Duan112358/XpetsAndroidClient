package com.emacs.xpets.utils;

public enum DataType {
	Latest(0), Recommend(1), AllTags(2), ByTag(3);

	public int getValue() {
		return this.value;
	}

	public String getUrl() {
		switch (this.value) {
		case Constants.LATEST:
			return Constants.GET_LATEST;
		case Constants.RECOMMEND:
			return Constants.GET_RECOMMEND;
		case Constants.TAGS:
			return Constants.GET_TAGS;
		default:
			return Constants.GET_BY_TAG;
		}
	}

	DataType(int value) {
		this.value = value;
	}

	public DataType getDefault() {
		return DataType.Latest;
	}

	private int value;
}
