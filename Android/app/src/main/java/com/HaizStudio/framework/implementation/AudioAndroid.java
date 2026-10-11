package com.HaizStudio.framework.implementation;

import java.io.IOException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.HaizStudio.framework.Audio;
import com.HaizStudio.framework.Music;
import com.HaizStudio.framework.Sound;

public class AudioAndroid implements Audio {

	AssetManager assets;
	SoundPool soundPool;

	public AudioAndroid(Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			createNewSoundPool();
		} else {
			createOldSoundPool();
		}
	}



	protected void createSoundPool() {

	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	protected void createNewSoundPool(){
		AudioAttributes attributes = new AudioAttributes.Builder()
				.setUsage(AudioAttributes.USAGE_GAME)
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.build();
		this.soundPool = new SoundPool.Builder()
				.setMaxStreams(2)
				.setAudioAttributes(attributes)
				.build();
	}

	@SuppressWarnings("deprecation")
	protected void createOldSoundPool(){
		this.soundPool= new SoundPool(2,AudioManager.STREAM_MUSIC,0);
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
			int soundId = soundPool.load(assetDescriptor, 1);
			return new SoundAndroid(soundPool, soundId);
		} catch (IOException e) {
			throw new RuntimeException("Ne peut pas charger le son '" + filename + "'");
		}
	}
}
