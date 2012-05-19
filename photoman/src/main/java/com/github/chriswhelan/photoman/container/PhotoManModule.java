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
package com.github.chriswhelan.photoman.container;

import com.github.chriswhelan.photoman.model.CachingThumbnailLoader;
import com.github.chriswhelan.photoman.model.DefaultThumbnailLoader;
import com.github.chriswhelan.photoman.model.PhotoProvider;
import com.github.chriswhelan.photoman.model.ThumbnailLoader;
import com.github.chriswhelan.photoman.model.android.bitmap.AndroidFactoryBitmapLoader;
import com.github.chriswhelan.photoman.model.android.bitmap.BitmapLoader;
import com.github.chriswhelan.photoman.model.android.media.AndroidMediaStorePhotoProvider;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class PhotoManModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PhotoProvider.class).to(AndroidMediaStorePhotoProvider.class);
		bind(BitmapLoader.class).to(AndroidFactoryBitmapLoader.class);
		bind(ThumbnailLoader.class).to(CachingThumbnailLoader.class);
		bind(ThumbnailLoader.class).annotatedWith(Names.named(CachingThumbnailLoader.BASE)).to(DefaultThumbnailLoader.class);
	}
}
