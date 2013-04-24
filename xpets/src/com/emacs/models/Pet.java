package com.emacs.models;

import java.util.Arrays;

public class Pet {
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
}
