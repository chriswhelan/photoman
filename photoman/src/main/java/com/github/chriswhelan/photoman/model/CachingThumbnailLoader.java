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
import javax.inject.Named;
import javax.inject.Singleton;

import android.util.LruCache;

import com.github.chriswhelan.photoman.android.AndroidDevice;
import com.github.chriswhelan.photoman.view.ThumbnailPhotoProjection;
import com.github.chriswhelan.photoman.view.ThumbnailQuery;

@Singleton
public class CachingThumbnailLoader implements ThumbnailLoader {

	// TODO: Change key to be ThumbnailQuery so we can handle different sizes separately
	private final LruCache<String, ThumbnailPhotoProjection> thumbnailCache;

	public static final String BASE = "CachingThumbnailLoader.base";

	private final ThumbnailLoader thumbnailLoader;
	private final AndroidDevice androidDevice;

	@Inject
	public CachingThumbnailLoader(@Named(BASE) final ThumbnailLoader thumbnailLoader, final AndroidDevice androidDevice) {
		this.thumbnailLoader = thumbnailLoader;
		this.androidDevice = androidDevice;
		thumbnailCache = getCache();
	}

	@Override
	public ThumbnailPhotoProjection loadThumbnail(final ThumbnailQuery query) {
		final ThumbnailPhotoProjection cachedProjection = thumbnailCache.get(query.getUri());
		if (cachedProjection != null)
			return cachedProjection;

		final ThumbnailPhotoProjection projection = thumbnailLoader.loadThumbnail(query);
		thumbnailCache.put(query.getUri(), projection);
		return projection;
	}

	private LruCache<String, ThumbnailPhotoProjection> getCache() {
		return new LruCache<String, ThumbnailPhotoProjection>(getCacheSize()) {

			@Override
			protected int sizeOf(final String key, final ThumbnailPhotoProjection value) {
				return value.getThumbnail().getByteCount();
			};
		};
	}

	// TODO: Is half of total memory really sensible? Need to profile the app and work out what's really needed
	// TODO: Move "2" to a configuration area
	private int getCacheSize() {
		return androidDevice.getMaximumMemory() / 2;
	}
}
