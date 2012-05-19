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
package com.github.chriswhelan.photoman.view.android.adapter;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.github.chriswhelan.photoman.R;
import com.github.chriswhelan.photoman.view.PhotoGridViewModel;
import com.github.chriswhelan.photoman.view.ThumbnailPhotoProjection;
import com.github.chriswhelan.photoman.view.background.BackgroundTaskResultHandler;

public class PhotoGridAdapter extends BaseAdapter {

	private final LayoutInflater layoutInflater;
	private final PhotoGridViewModel viewModel;

	// TODO: Confirm a new instance is injected every time on configuration change
	@Inject
	public PhotoGridAdapter(final LayoutInflater layoutInflater, final PhotoGridViewModel viewModel) {
		this.layoutInflater = layoutInflater;
		this.viewModel = viewModel;
	}

	@Override
	public int getCount() {
		return viewModel.getPhotoCount();
	}

	@Override
	public Object getItem(final int arg0) {
		return null;
	}

	@Override
	public long getItemId(final int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final ImageView imageView = recycleView(parent, convertView);

		final int previousPosition = getPreviousInProgressPosition(imageView);
		if (position == previousPosition)
			return imageView;

		if (previousPosition != -1)
			viewModel.cancelLoadThumbnail(previousPosition);

		imageView.setImageDrawable(new InProgressDrawable(position));
		viewModel.loadThumbnail(position, new AsyncThumbnailHandler(position, imageView));
		return imageView;
	}

	// TODO: Replace -1 with Option pattern
	private int getPreviousInProgressPosition(final ImageView imageView) {
		final Drawable drawable = imageView.getDrawable();
		if (!(drawable instanceof InProgressDrawable))
			return -1;

		return ((InProgressDrawable) drawable).getPosition();
	}

	// TODO: Are you trying to reuse the same LayoutParams object across all rows?
	// That could be your problem. – CommonsWare Mar 23 '10 at 0:10
	// Aha, yes, that was it. I assumed that when layout parameters were set using a LayoutParams object, the View copied the values out of
	// the LayoutParams, leaving it free to be reused. That was not the case, and the object remained somehow tied to the View. Creating a
	// new LayoutParams for each row did the trick and everything now works properly. Thanks!
	private ImageView recycleView(final ViewGroup parent, final View convertView) {
		if (convertView instanceof ImageView)
			return (ImageView) convertView;
		return createView(parent);
	}

	private ImageView createView(final ViewGroup parent) {
		final ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.thumbnail, parent, false);
		imageView.setLayoutParams(layoutParams(viewModel.getImageSize()));
		return imageView;
	}

	private GridView.LayoutParams layoutParams(final int size) {
		return new GridView.LayoutParams(size, size);
	}

	// TODO: Change to use BitmapDrawable and pass in a Bitmap for in progress (need to eventually layer progressbar on top of this)
	// TODO: Why not create a subclass of ImageView instead of Drawable, then we can ask the ImageView for it's position without ugly
	// casting
	// Could store the position as an ImageView member
	// Could also make the ImageView implement BTRH<PTV> itself so we don't even create an extra object - but would then need to use a Task
	// reference for concurrency check and BTRH would have to use a WeakReference to it's handler again which seems odd
	private static class InProgressDrawable extends ColorDrawable {

		private final int position;

		public InProgressDrawable(final int position) {
			super(Color.BLACK);
			this.position = position;
		}

		public int getPosition() {
			return position;
		}
	}

	private static class AsyncThumbnailHandler implements BackgroundTaskResultHandler<ThumbnailPhotoProjection> {

		private final int position;
		private final WeakReference<ImageView> imageViewReference;

		public AsyncThumbnailHandler(final int position, final ImageView imageView) {
			this.position = position;
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		public void handleResult(final ThumbnailPhotoProjection result) {
			if (imageViewReference == null || result == null)
				return;

			final ImageView imageView = imageViewReference.get();
			if (position == getPosition(imageView))
				imageView.setImageBitmap(result.getThumbnail());
		}

		private int getPosition(final ImageView imageView) {
			if (imageView == null)
				return -1;

			return getPosition(imageView.getDrawable());
		}

		private int getPosition(final Drawable drawable) {
			if (!(drawable instanceof InProgressDrawable))
				return -1;

			final InProgressDrawable inProgressDrawable = (InProgressDrawable) drawable;
			return inProgressDrawable.getPosition();
		}
	}
}
