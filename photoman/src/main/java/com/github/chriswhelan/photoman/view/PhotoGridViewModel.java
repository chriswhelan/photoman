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
package com.github.chriswhelan.photoman.view;

import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.chriswhelan.photoman.android.AndroidDevice;
import com.github.chriswhelan.photoman.domain.Photo;
import com.github.chriswhelan.photoman.domain.PhotoAlbum;
import com.github.chriswhelan.photoman.view.background.BackgroundTaskFactory;
import com.github.chriswhelan.photoman.view.background.BackgroundTaskResultHandler;
import com.github.chriswhelan.photoman.view.background.PhotoThumbnailViewLoaderBackgroundTask;

@Singleton
public class PhotoGridViewModel {

	private static final int DEFAULT_COLUMN_COUNT = 3;

	private final BackgroundTaskFactory taskFactory;
	private final AndroidDevice thisDevice;

	// TODO: Confirm task reference is dropped when completed
	private final Map<GridViewPosition, PhotoThumbnailViewLoaderBackgroundTask> tasks = new WeakHashMap<GridViewPosition, PhotoThumbnailViewLoaderBackgroundTask>();

	private PhotoAlbum photos;

	@Inject
	public PhotoGridViewModel(final BackgroundTaskFactory taskFactory, final AndroidDevice thisDevice) {
		this.taskFactory = taskFactory;
		this.thisDevice = thisDevice;
	}

	// TODO: can we refactor this so it's only accessible to the controller, not the view
	public void setPhotoAlbum(final PhotoAlbum photos) {
		this.photos = photos;
	}

	public int getPhotoCount() {
		return photos.count();
	}

	// TODO: don't need to spawn a background thread if image can be resolved from memory. Push task mgmt further down stack to handle this.
	public void loadThumbnail(final GridViewPosition position, final BackgroundTaskResultHandler<ThumbnailPhotoProjection> handler) {
		final PhotoThumbnailViewLoaderBackgroundTask task = taskFactory.createBackgroundTask(handler);
		tasks.put(position, task);
		final Photo photo = photos.get(position.getPosition());
		final ThumbnailQuery query = new ThumbnailQuery(photo.getUri(), getImageSize());
		task.execute(query);
	}

	public void cancelLoadThumbnail(final GridViewPosition position) {
		final PhotoThumbnailViewLoaderBackgroundTask task = tasks.get(position);
		if (task != null)
			task.cancel(true);
	}

	public int getNumberOfColumns() {
		if (thisDevice.isPortrait())
			return DEFAULT_COLUMN_COUNT;

		return Math.round(thisDevice.getAspectRatio() * DEFAULT_COLUMN_COUNT);
	}

	// TODO: This will break when the gridview is not the root view.
	// Add a test which asserts post layout that actual image size == getImageSize()
	// TODO: Also need to be aware if the current image is a panorama/double size image
	public int getImageSize() {
		return thisDevice.getDisplayWidthInPixels() / getNumberOfColumns();
	}

	public static class GridViewPosition {

		private final int position;

		public GridViewPosition(final int position) {
			this.position = position;
		}

		public int getPosition() {
			return position;
		}
	}
}
