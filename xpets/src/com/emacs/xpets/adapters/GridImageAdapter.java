package com.emacs.xpets.adapters;

import java.util.LinkedList;

import com.emacs.models.Pet;
import com.emacs.xpets.utils.Utils;
import com.generpoint.xpets.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridImageAdapter extends BaseAdapter {

	private LinkedList<Pet> mListItems;
	private ImageLoader mImageLoader;

	private LayoutInflater inflater;

	public GridImageAdapter(Context context, LinkedList<Pet> pets) {
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mListItems = pets;
		this.mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return mListItems.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ImageView imageView;
		if (convertView == null) {
			imageView = (ImageView) inflater.inflate(R.layout.item_grid_image,
					parent, false);
		} else {
			imageView = (ImageView) convertView;
		}

		mImageLoader.displayImage(mListItems.get(position).getThumbnail(),
				imageView, Utils.getImageDisplayOptions());

		return imageView;
	}

}
