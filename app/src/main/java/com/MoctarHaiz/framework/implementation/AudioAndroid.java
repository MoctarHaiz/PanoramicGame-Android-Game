package com.MoctarHaiz.framework.implementation;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.MoctarHaiz.framework.Audio;
import com.MoctarHaiz.framework.Music;
import com.MoctarHaiz.framework.Sound;

public class AudioAndroid implements Audio {

	AssetManager assets;
	SoundPool soundPool;

	public AudioAndroid(Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}

	@Override
	public Music createMusic(String filename) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			return new MusicAndroid(assetDescriptor);
		} catch (IOException e) {
			throw new RuntimeException("Ne peut pas charger la Musique'" + filename + "'");
		}
	}

	@Override
	public Sound createSound(String filename) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new SoundAndroid(soundPool, soundId);
		} catch (IOException e) {
			throw new RuntimeException("Ne peut pas charger le son '" + filename + "'");
		}
	}
}
