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
package com.github.chriswhelan.photoman.model.android.media;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.github.chriswhelan.photoman.domain.Photo;
import com.github.chriswhelan.photoman.domain.PhotoAlbum;
import com.github.chriswhelan.photoman.model.PhotoProvider;

import de.akquinet.android.androlog.Log;

public class AndroidMediaStorePhotoProvider implements PhotoProvider {

	private final ContentResolver contentResolver;

	@Inject
	public AndroidMediaStorePhotoProvider(final ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	@Override
	public PhotoAlbum getAllPhotos() {
		final List<Photo> photos = new ArrayList<Photo>();

		final Date startTime = new Date();
		Log.i("PHOTO LOAD START TIME", "" + startTime.getTime());

		// TODO: Needs to be on a background thread
		final Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

		Log.i("LOADING PHOTOS", "*******************************************************************************************************");
		Log.i("internal cursor count", "" + cursor.getCount());

		while (cursor.moveToNext()) {
			final String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

			// TODO: Do we want the content URI to become part of the Photo domain object?
			final long imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID));
			final Uri newUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId);
			Log.i("found bitmap", newUri.getPath() + ": " + data);

			photos.add(new Photo(data));
		}

		cursor.close();

		final Date endTime = new Date();
		Log.i("PHOTO LOAD COMPLETE", "**************************************************************************************************");
		Log.i("PHOTO LOAD END TIME", "" + endTime.getTime());
		Log.i("PHOTO LOAD TOTAL TIME", "" + (endTime.getTime() - startTime.getTime()));

		return new PhotoAlbum(photos);
	}
}
