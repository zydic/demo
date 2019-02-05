package com.example.foldergallery.data;

import android.text.TextUtils;

public class ImageData {
	public String imagePath, imageAlbum;
	public String folderName;
	public int imageCount = 0;
	public long id;
	public boolean isSupported = true;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImageAlbum() {
		return imageAlbum;
	}

	public void setImageAlbum(String imageAlbum) {
		this.imageAlbum = imageAlbum;
	}

	public int getImageCount() {
		return imageCount;
	}

	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		if (!TextUtils.isEmpty(imagePath)) {
			return "ImageData { " + "imagePath=" + imagePath + ",folderName="
					+ folderName + ",imageCount=" + imageCount + " }";
		}
		return super.toString();
	}

}
