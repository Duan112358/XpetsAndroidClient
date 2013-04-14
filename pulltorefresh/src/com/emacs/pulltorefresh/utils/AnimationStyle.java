package com.emacs.pulltorefresh.utils;

import com.emacs.pulltorefresh.PullToRefreshBase.Mode;
import com.emacs.pulltorefresh.PullToRefreshBase.Orientation;

import android.content.Context;
import android.content.res.TypedArray;

public enum AnimationStyle {
	/**
	 * This is the default for Android-PullToRefresh. Allows you to use any
	 * drawable, which is automatically rotated and used as a Progress Bar.
	 */
	ROTATE,

	/**
	 * This is the old default, and what is commonly used on iOS. Uses an
	 * arrow image which flips depending on where the user has scrolled.
	 */
	FLIP;

	static AnimationStyle getDefault() {
		return ROTATE;
	}

	/**
	 * Maps an int to a specific mode. This is needed when saving state, or
	 * inflating the view from XML where the mode is given through a attr
	 * int.
	 * 
	 * @param modeInt - int to map a Mode to
	 * @return Mode that modeInt maps to, or ROTATE by default.
	 */
	static AnimationStyle mapIntToValue(int modeInt) {
		switch (modeInt) {
			case 0x0:
			default:
				return ROTATE;
			case 0x1:
				return FLIP;
		}
	}

	LoadingLayout createLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		switch (this) {
			case ROTATE:
			default:
				return new RotateLoadingLayout(context, mode, scrollDirection, attrs);
			case FLIP:
				return new FlipLoadingLayout(context, mode, scrollDirection, attrs);
		}
	}
}
