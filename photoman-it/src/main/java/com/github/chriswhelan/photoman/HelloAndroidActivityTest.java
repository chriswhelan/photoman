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
package com.github.chriswhelan.photoman;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.GridView;
import android.widget.ImageView;

import com.github.chriswhelan.photoman.view.android.activity.PhotoGridActivity;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<PhotoGridActivity> {

	private PhotoGridActivity activity;

	public HelloAndroidActivityTest() {
		super(PhotoGridActivity.class);
	}

	@Override
	protected void setUp() throws Exception {

	}

	public void testActivity() {
		activity = getActivity();
		final GridView gridView = findViewById(R.id.gridView1);
		final ImageView actualImage = (ImageView) gridView.getChildAt(0);
		assertNotNull("Expected an image in the main grid", actualImage);
	}

	@SuppressWarnings("unchecked")
	private <T> T findViewById(final int id) {
		return (T) activity.findViewById(id);
	}
}
