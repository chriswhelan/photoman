/**
 * Copyright (C) 2012 Chris Whelan
 *
 * This file is part of Photo Man.
 *
 * Photo Man is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Photo Man is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Photo Man.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.chriswhelan.photoman.android;

import javax.inject.Inject;
import javax.inject.Singleton;

import android.app.ActivityManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

@Singleton
public class AndroidDevice {

	private static final int BYTES_IN_A_MEGABYTE = 1024 * 1024;

	private final WindowManager windowManager;
	private final ActivityManager activityManager;

	@Inject
	public AndroidDevice(final WindowManager windowManager, final ActivityManager activityManager) {
		this.windowManager = windowManager;
		this.activityManager = activityManager;
	}

	// TODO: Separate memory methods to a different interface from display methods
	public int getMaximumMemory() {
		return activityManager.getLargeMemoryClass() * BYTES_IN_A_MEGABYTE;
	}

	public int getDisplayWidthInPixels() {
		return getDisplayMetrics().widthPixels;
	}

	public float getAspectRatio() {
		final DisplayMetrics metrics = getDisplayMetrics();
		final int width = metrics.widthPixels;
		final int height = metrics.heightPixels;
		final float larger = Math.max(width, height);
		final float smaller = Math.min(width, height);
		return larger / smaller;
	}

	public boolean isPortrait() {
		final DisplayMetrics metrics = getDisplayMetrics();
		final int width = metrics.widthPixels;
		final int height = metrics.heightPixels;
		return height > width;
	}

	public boolean isLandscape() {
		return !isPortrait();
	}

	private DisplayMetrics getDisplayMetrics() {
		final DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}
}
