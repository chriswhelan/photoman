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
package com.github.chriswhelan.photoman.controller;

import javax.inject.Inject;
import javax.inject.Singleton;

import android.content.Intent;

import com.github.chriswhelan.photoman.domain.PhotoAlbum;
import com.github.chriswhelan.photoman.domain.PhotoManager;
import com.github.chriswhelan.photoman.view.PhotoGridViewModel;

@Singleton
public class PhotomanController {

	private final PhotoManager photoManager;
	private final PhotoGridViewModel photoGridViewModel;

	@Inject
	public PhotomanController(final PhotoManager photoManager, final PhotoGridViewModel photoGridViewModel) {
		this.photoManager = photoManager;
		this.photoGridViewModel = photoGridViewModel;
	}

	public void handleIntent(final Intent intent) {
		// TODO: Use a pattern to discern correct handler
		if (isDefaultLauncherIntent(intent)) {
			handleOpenApplicationIntent();
			return;
		}

		// TODO: Proper logging, an actual exception handling strategy... etc
		throw new UnsupportedOperationException("unrecognized intent");
	}

	private void handleOpenApplicationIntent() {
		final PhotoAlbum photos = photoManager.getAllPhotos();
		photoGridViewModel.setPhotoAlbum(photos);
	}

	private boolean isDefaultLauncherIntent(final Intent intent) {
		// TODO: Hackety hack hack, just lookup DEFAULT/LAUNCHER or whatever from Android intent docs
		return true;
	}
}
