package com.HaizStudio.framework.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.HaizStudio.framework.FileIO;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

public class FichierIOAndroid implements FileIO {

	Context context;
	AssetManager assets;
	String cheminDeStockageExterne;

	public FichierIOAndroid (Context context) {
		this.context = context;
		this.assets = context.getAssets();
		this.cheminDeStockageExterne = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;

	}

	@Override
	public InputStream readAsset(String file) throws IOException {
		return assets.open(file);
	}

	@Override
	public InputStream readFile(String file) throws IOException {
		return new FileInputStream(cheminDeStockageExterne + file);
	}

	@Override
	public OutputStream writeFile(String file) throws IOException {
		return new FileOutputStream(cheminDeStockageExterne + file);
	}

	public SharedPreferences getSharedPref() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

}
