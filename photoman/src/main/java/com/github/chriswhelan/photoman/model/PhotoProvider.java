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

import com.github.chriswhelan.photoman.domain.PhotoAlbum;

public interface PhotoProvider {

	PhotoAlbum getAllPhotos();
}
