package com.MoctarHaiz.framework.implementation;

import android.graphics.Bitmap;

import com.MoctarHaiz.framework.Graphics.ImageFormat;
import com.MoctarHaiz.framework.Image;


	public class ImageAndroid implements Image {
	    Bitmap bitmap;
	    ImageFormat format;
	   
	    public ImageAndroid(Bitmap bitmap, ImageFormat format) {
	        this.bitmap = bitmap;
	        this.format = format;
	    }

	    @Override
	    public int getWidth() {
	        return bitmap.getWidth();
	    }

	    @Override
	    public int getHeight() {
	        return bitmap.getHeight();
	    }

	    @Override
	    public ImageFormat getFormat() {
	        return format;
	    }

	    @Override
	    public void dispose() {
	        bitmap.recycle();
	    }      
	}
	 

