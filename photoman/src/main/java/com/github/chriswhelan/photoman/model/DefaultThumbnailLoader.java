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
package com.github.chriswhelan.photoman.model;

import javax.inject.Inject;
import javax.inject.Singleton;

import android.graphics.Bitmap;

import com.github.chriswhelan.photoman.model.android.bitmap.BitmapLoader;
import com.github.chriswhelan.photoman.view.ThumbnailPhotoProjection;
import com.github.chriswhelan.photoman.view.ThumbnailQuery;

@Singleton
public class DefaultThumbnailLoader implements ThumbnailLoader {

	private final BitmapLoader bitmapLoader;

	@Inject
	public DefaultThumbnailLoader(final BitmapLoader bitmapLoader) {
		this.bitmapLoader = bitmapLoader;
	}

	@Override
	public ThumbnailPhotoProjection loadThumbnail(final ThumbnailQuery query) {
		final String uri = query.getUri();
		final PhotoDimension dimensions = bitmapLoader.loadDimensions(uri);
		final int sampleSize = calculateThumbnailSampleSize(dimensions, query.getTargetSize());
		final Bitmap thumbnail = bitmapLoader.load(uri, sampleSize);
		return new ThumbnailPhotoProjection(thumbnail);
	}

	// TODO: Scaling the smallest edge only atm - bad idea for panoramas
	// TODO: Confirm a ratio of x.9 would round to x and not x + 1
	private int calculateThumbnailSampleSize(final PhotoDimension dimensions, final int targetSize) {
		final int photoSize = Math.min(dimensions.getWidth(), dimensions.getHeight());
		return photoSize / targetSize;
	}
}
