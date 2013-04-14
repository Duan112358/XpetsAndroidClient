package com.emacs.models;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

public class Pet implements Parcelable {
	private String key;
	private String[] tags;
	private String title;
	private String photo;
	private String thumbnail;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Override
	public String toString() {
		return "Pet [key=" + key + ", tags=" + Arrays.toString(tags)
				+ ", title=" + title + ", photo=" + photo + ", thumbnail="
				+ thumbnail + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Pet) {
			Pet p = (Pet) o;
			return p.getKey().equalsIgnoreCase(this.key);
		}
		return false;
	}

	public Pet(Parcel in) {
		super();
		readFromParcel(in);
	}

	public Pet() {
	}

	public static final Parcelable.Creator<Pet> CREATOR = new Parcelable.Creator<Pet>() {
		public Pet createFromParcel(Parcel in) {
			return new Pet(in);
		}

		public Pet[] newArray(int size) {

			return new Pet[size];
		}

	};

	public void readFromParcel(Parcel in) {
		key = in.readString();
		title = in.readString();
		in.readStringArray(tags);
		thumbnail = in.readString();
		photo = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(key);
		dest.writeString(thumbnail);
		dest.writeString(photo);
		dest.writeStringArray(tags);
	}

}
