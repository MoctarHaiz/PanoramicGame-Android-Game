package com.MoctarHaiz.PanoramicGame;

import android.util.Log;

import com.MoctarHaiz.framework.Screen;
import com.MoctarHaiz.framework.implementation.JeuAndroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DebutJeu extends JeuAndroid {


    public static String map;  //Our Level sheet
    boolean PremiereCreation = true;
    public static boolean _DEBUG = true;

    @Override
    public Screen getInitScreen() {

        if (PremiereCreation) {
            Assets.load(this);
            PremiereCreation = false;
        }

        InputStream is = getResources().openRawResource(R.raw.level1);
        map = convertStreamToString(is);

        return new SplashLoadingScreen(this);

    }

    @Override
    public void onBackPressed() {
        getCurrentScreen().backButton();
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            Log.w("LOG", e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.w("LOG", e.getMessage());
            }
        }
        return sb.toString();
    }

    @Override
    public void onResume() {
        super.onResume();

        Assets.theme.play();

    }

    @Override
    public void onPause() {
        super.onPause();
        Assets.theme.pause();

    }

}
