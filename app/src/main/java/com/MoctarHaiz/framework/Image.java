package com.MoctarHaiz.framework;

import com.MoctarHaiz.framework.Graphics.ImageFormat;

public interface Image {
	public int getWidth();

	public int getHeight();

	public ImageFormat getFormat();

	public void dispose();
}
