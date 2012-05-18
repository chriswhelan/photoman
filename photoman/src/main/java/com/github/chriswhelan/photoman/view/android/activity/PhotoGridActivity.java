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
package com.github.chriswhelan.photoman.view.android.activity;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.github.chriswhelan.photoman.R;
import com.github.chriswhelan.photoman.controller.PhotomanController;
import com.github.chriswhelan.photoman.view.PhotoGridViewModel;
import com.github.chriswhelan.photoman.view.android.adapter.PhotoGridAdapter;

@ContentView(R.layout.main)
public class PhotoGridActivity extends RoboActivity {

	// TODO: does this mean guice will only actually instantiate the tree for this intent/viewmodel?
	// or does it instantiate everything at the start?
	// where is my 'configuretheapp' central container config?
	// Need tests to confirm that a given intent only boots up the necessary sub-tree of the system
	@Inject
	private PhotomanController controller;

	@InjectView(R.id.gridView1)
	private GridView gridView;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO: how do we ensure that only the necessary components needed for this activity are created here and not the entire tree?
		final PhotoGridViewModel viewModel = controller.handleIntent(getIntent());

		// TODO: make sure the activity isn't being leaked on orientation change
		gridView.setNumColumns(viewModel.getNumberOfColumns());
		gridView.setAdapter(getGridViewAdapter(viewModel));

		// final Loader<Cursor> cursorLoader = new CursorLoader(this);
	}

	// TODO: could shrink the cache here. Is there an application level equivalent? - this is just a hack though, better to avoid entirely
	// Also lookinto onTrimMemory
	// @Override
	// public void onLowMemory() {
	// // TODO Auto-generated method stub
	// super.onLowMemory();
	// }

	// LoaderCallbacks
	// @Override
	// public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
	// return null;
	// }
	//
	// @Override
	// public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
	// }
	//
	// @Override
	// public void onLoaderReset(final Loader<Cursor> loader) {
	// }

	private ListAdapter getGridViewAdapter(final PhotoGridViewModel viewModel) {
		return new PhotoGridAdapter(this, viewModel);

		// TODO: FLAG_REGISTER_CONTENT_OBSERVER If set the adapter will register a content observer on the cursor and will call
		// onContentChanged() when a notification comes in.
		// return new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null,
		// new String[] { MediaStore.Images.Thumbnails.IMAGE_ID }, new int[] { android.R.id.text1, android.R.id.text2 }, 0);
	}

	// TODO: some crap from a dodgy codebase. Does it make any sense?
	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	//
	// unbindDrawables(layout);
	// makeBitmapNull();
	// System.gc();
	// }
	//
	// private void makeBitmapNull() {
	// for (int i = 0; i < bitmap1.length; i++) {
	//
	// if (bitmap1[i] != null) {
	// bitmap1[i].recycle();
	// }
	//
	// }
	//
	// }
	//
	// private void unbindDrawables(final View view) {
	// if (view.getBackground() != null) {
	// view.getBackground().setCallback(null);
	// }
	// if (view instanceof ViewGroup) {
	// Log.v("DEST", view.getClass() + "");
	// for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
	// unbindDrawables(((ViewGroup) view).getChildAt(i));
	// }
	// if (!(view instanceof GridView)) {
	// ((ViewGroup) view).removeAllViews();
	// } else {
	// gv.removeAllViewsInLayout();
	// }
	// }
	// }
}
