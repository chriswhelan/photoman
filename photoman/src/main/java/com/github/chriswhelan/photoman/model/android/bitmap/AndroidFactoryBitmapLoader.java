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
package com.github.chriswhelan.photoman.model.android.bitmap;

import javax.inject.Singleton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.chriswhelan.photoman.model.PhotoDimension;

@Singleton
public class AndroidFactoryBitmapLoader implements BitmapLoader {

	@Override
	public PhotoDimension loadDimensions(final String uri) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// TODO: Why doesn't this throw any exceptions? The mediastore throws IOException and FileNotFoundException, which seems rather
		// sensible...
		BitmapFactory.decodeFile(uri, options);
		return new PhotoDimension(options.outWidth, options.outHeight);
	}

	// TODO: Confirm bitmap configuration is ARGB_8888 - can we remove the alpha channel for photos? Is 8888 the highest res option on
	// android?
	// TODO: Have a choice here to use decodeFile or MediaStore.Images.Media.getBitmap(contentResolver, newUri);
	// decodeFile is a bit more 'raw' and doesn't throw exceptions (!), but MediaStore needs a contentResolver reference
	// would need to work out the @ContextScoped stuff in roboguice properly - assume we just tell it to use Application scope.
	// and do we use a Resolver<ContentResolver> or just a ContentResolver - still no idea what this means...
	@Override
	public Bitmap load(final String uri, final int sampleSize) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = sampleSize;
		return BitmapFactory.decodeFile(uri, options);
	}
}
