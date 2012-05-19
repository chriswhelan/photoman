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

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.chriswhelan.photoman.model.android.bitmap.BitmapLoader;
import com.github.chriswhelan.photoman.view.ThumbnailQuery;

@RunWith(MockitoJUnitRunner.class)
public class DefaultThumbnailLoaderUnitTest {

	@Mock
	private BitmapLoader bitmapLoader;

	private ThumbnailLoader loader;

	@Before
	public void init() {
		loader = new DefaultThumbnailLoader(bitmapLoader);
	}

	@Test
	public void doesNotShrinkImageWithinTargetSize() {
		when(bitmapLoader.loadDimensions(anyString())).thenReturn(new PhotoDimension(200));

		loader.loadThumbnail(new ThumbnailQuery("", 200));

		shouldScaleImageBy(1);
	}

	@Test
	public void doesNotShrinkImageBelowTargetSize() {
		when(bitmapLoader.loadDimensions(anyString())).thenReturn(new PhotoDimension(399));

		loader.loadThumbnail(new ThumbnailQuery("", 200));

		shouldScaleImageBy(1);
	}

	@Test
	public void onlyShrinkImageByMinimumScaleNeeded() {
		when(bitmapLoader.loadDimensions(anyString())).thenReturn(new PhotoDimension(400));

		loader.loadThumbnail(new ThumbnailQuery("", 200));

		shouldScaleImageBy(2);
	}

	@Test
	public void onlyShrinkToSmallestEdgeSize() {
		when(bitmapLoader.loadDimensions(anyString())).thenReturn(new PhotoDimension(2000, 400));

		loader.loadThumbnail(new ThumbnailQuery("", 200));

		shouldScaleImageBy(2);
	}

	private void shouldScaleImageBy(final int expectedScale) {
		verify(bitmapLoader).load(anyString(), eq(expectedScale));
	}
}
