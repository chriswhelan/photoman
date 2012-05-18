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
package com.github.chriswhelan.photoman.view.background;

import android.os.AsyncTask;

import com.github.chriswhelan.photoman.model.PhotoRepository;
import com.github.chriswhelan.photoman.view.ThumbnailPhotoProjection;
import com.github.chriswhelan.photoman.view.ThumbnailQuery;

//TODO: add progress bars
public class PhotoThumbnailViewLoaderBackgroundTask extends AsyncTask<ThumbnailQuery, Void, ThumbnailPhotoProjection> {

	// TODO: We should be the only reference to the handler, so this should not leak. Need a test to confirm.
	private final BackgroundTaskResultHandler<ThumbnailPhotoProjection> handler;
	private final PhotoRepository photoRepository;

	PhotoThumbnailViewLoaderBackgroundTask(final BackgroundTaskResultHandler<ThumbnailPhotoProjection> handler,
			final PhotoRepository photoRepository) {
		this.handler = handler;
		this.photoRepository = photoRepository;
	}

	// TODO: Handle iscancelled, empty weakref, missing param - maybe a sub-class without all this cruft
	// TODO: can photoRepoRef be null here?
	@Override
	protected ThumbnailPhotoProjection doInBackground(final ThumbnailQuery... params) {
		return photoRepository.createThumbnailView(params[0]);
	}

	@Override
	protected void onPostExecute(final ThumbnailPhotoProjection photoThumbnailView) {
		if (photoThumbnailView == null)
			return;

		handler.handleResult(photoThumbnailView);
	}
}
