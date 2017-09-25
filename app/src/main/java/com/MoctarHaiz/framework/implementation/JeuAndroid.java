package com.MoctarHaiz.framework.implementation;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.MoctarHaiz.framework.Audio;
import com.MoctarHaiz.framework.FileIO;
import com.MoctarHaiz.framework.Graphics;
import com.MoctarHaiz.framework.Input;
import com.MoctarHaiz.framework.Jeu;
import com.MoctarHaiz.framework.Screen;

public abstract class JeuAndroid extends Activity implements Jeu {

    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean estPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int frameBufferWidth = estPortrait ? 480 : 800;
        int frameBufferHeight = estPortrait ? 800 : 480;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);

        /*********************************/
        final int version = android.os.Build.VERSION.SDK_INT; // ///////forum
        final int displayWidth;
        final int displayHeight;
        Display display = getWindowManager().getDefaultDisplay();
        if (version >= 13) {
            Point size = new Point();
            display.getSize(size);
            displayWidth = size.x;
            displayHeight = size.y;
        } else {
            display = getWindowManager().getDefaultDisplay();
            displayWidth = display.getWidth();
            displayHeight = display.getHeight();
        } // //////////////////////////////////////////////////////fin forum
        /**********************************/
        /*
		 * float scaleX = (float) frameBufferWidth /
		 * getWindowManager().getDefaultDisplay().getWidth(); float scaleY =
		 * (float) frameBufferHeight /
		 * getWindowManager().getDefaultDisplay().getHeight();
		 */
        float scaleX = (float) frameBufferWidth / displayWidth;
        float scaleY = (float) frameBufferHeight / displayHeight;

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new GraphiquesAndroid(getAssets(), frameBuffer);
        fileIO = new FichierIOAndroid(this);
        audio = new AudioAndroid(this);
        input = new InputAndroid(this, renderView, scaleX, scaleY);
        screen = getInitScreen();
        context = getApplicationContext();

        setContentView(renderView);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,                "Paanaromic Game");
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    public JeuAndroid() {

    }

    @Override
    public Audio getAudio() {

        return audio;
    }

    @Override
    public Input getInput() {

        return input;
    }

    @Override
    public FileIO getFileIO() {

        return fileIO;
    }

    @Override
    public Graphics getGraphics() {

        return graphics;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen ne doit pas etre null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    public Screen getCurrentScreen() {

        return screen;
    }

    @Override
    public Screen setCurrentScreen() {

        return null;
    }

    @Override
    public Screen setInitScreen() {

        return null;
    }

    @Override
    public Screen getInitScreen() {

        return null;
    }


}
