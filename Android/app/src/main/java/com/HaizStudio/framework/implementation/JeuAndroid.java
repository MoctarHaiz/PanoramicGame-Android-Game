package com.HaizStudio.framework.implementation;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.HaizStudio.ChakaZulu.Assets;
import com.HaizStudio.ChakaZulu.R;
import com.HaizStudio.framework.Audio;
import com.HaizStudio.framework.FileIO;
import com.HaizStudio.framework.Graphics;
import com.HaizStudio.framework.Input;
import com.HaizStudio.framework.Jeu;
import com.HaizStudio.framework.Screen;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import androidx.annotation.NonNull;

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

        //Full screen and No Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Ads view
        //Initialisation of Ad SDK to avoid lag while creating the first Ad
        MobileAds.initialize(this, getResources().getString(R.string.ID_ADMOB_APP_ID));


        //Creating an Interstitial Ad
        Assets.mInterstitialAd = new InterstitialAd(this);
        Assets.mInterstitialAd.setAdUnitId(getResources().getString(R.string.ID_ADMOB_INTERSTITIAL));
        //Assets.mInterstitialAd.setAdUnitId(getResources().getString(R.string.ID_ADMOB_TEST_INTERSTITIAL));
        //Assets.mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //Create a banner ad. The ad size and ad unit ID must be set before calling loadAd.
        Assets.mAdView = new AdView(this);
        Assets.mAdView.setAdSize(AdSize.SMART_BANNER);
        Assets.mAdView.setAdUnitId(getResources().getString(R.string.ID_ADMOB_BANNER));
        //Assets.mAdView.setAdUnitId(getResources().getString(R.string.ID_ADMOB_TEST_BANNER));
        final AdRequest.Builder adRequestBuilder = new AdRequest.Builder();  // Create an ad request.


        // adRequestBuilder.addTestDevice("88357E5441C497ADAB76B801E9D7648F"); // Setting Test Mode
        Assets.mAdView.loadAd(adRequestBuilder.build());        // Start loading the ads.
        Assets.mInterstitialAd.loadAd(adRequestBuilder.build());


        //Creating a rewarded Ad
        Assets.mRewardedAd = createAndLoadRewardedAd(getResources().getString(R.string.ID_ADMOB_REWARDED));
        //Assets.mRewardedAd = createAndLoadRewardedAd(getResources().getString(R.string.ID_ADMOB_TEST_REWARDED));


        // Handling Display SurfaceView and AdView
        final FrameLayout frameLayout = new FrameLayout(this);
        final FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        final FrameLayout.LayoutParams surfaceViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        final FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);


        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        float frameBufferWidth = isPortrait ? Assets.WINDOW_HEIGHT : Assets.WINDOW_WIDTH;
        float frameBufferHeight = isPortrait ? Assets.WINDOW_HEIGHT : Assets.WINDOW_HEIGHT;
        Bitmap frameBuffer = Bitmap.createBitmap((int) frameBufferWidth, (int) frameBufferHeight, Config.RGB_565);
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
        Assets.reScaleGraphics(1.0f / scaleX, 1.0f / scaleY);  //1.0/x to fill the whole screen

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new GraphiquesAndroid(getAssets(), frameBuffer);
        fileIO = new FichierIOAndroid(this);
        audio = new AudioAndroid(this);
        input = new InputAndroid(this, renderView, scaleX, scaleY);
        screen = getInitScreen();
        context = getApplicationContext();


        //Adding the surfaceView and AddView in the FrameLayout

        Assets.mAdView.setAdListener(new AdListener() { //Add it only after the AdView is loaded
            public void onAdLoaded() {
                if (Assets.mAdView.getParent() != null)
                    ((FrameLayout) Assets.mAdView.getParent()).removeView(Assets.mAdView);

                frameLayout.addView(Assets.mAdView, adViewLayoutParams);
            }
        });

        Assets.mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial to save time
                Assets.mInterstitialAd.loadAd(new AdRequest.Builder().build());
                //Resume sound
            }

        });


        frameLayout.addView(renderView, surfaceViewLayoutParams);
        //Adding FrameLayout in the scene View
        this.setContentView(frameLayout, frameLayoutLayoutParams);


        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Panaromic Game");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Assets.mAdView != null)
            Assets.mAdView.destroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (wakeLock != null)
            wakeLock.acquire();

        if (screen != null)
            screen.resume();

        if (renderView != null)
            renderView.resume();

        if (Assets.mAdView != null)
            Assets.mAdView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (wakeLock != null)
            wakeLock.release();

        if (renderView != null)
            renderView.pause();

        if (screen != null)
            screen.pause();

        if (Assets.mAdView != null)
            Assets.mAdView.pause();

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
    public Context getContext() {
        return context;
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

    @Override
    public void showInterstitial() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Assets.mInterstitialAd != null) {
                    if (Assets.mInterstitialAd.isLoaded()) {
                        Assets.mInterstitialAd.show();
                    } else {
                        Assets.mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                }    //Pause sound
                else {
                    //   Log.d("TAG", "The interstitial wasn't loaded yet.");
                    Assets.mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            }
        });
    }

    @Override
    public void showRewarded() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Assets.mInterstitialAd != null) {
                    if (Assets.mRewardedAd.isLoaded()) {

                            //Log.i("Reward Amount ", " "+ Assets.mRewardedAd.getRewardItem().getAmount());
                            //Log.i("Reward Type ", " "+ Assets.mRewardedAd.getRewardItem().getType());

                        RewardedAdCallback adCallback = new RewardedAdCallback() {
                            @Override
                            public void onRewardedAdOpened() {
                                // Ad opened.
                            }

                            @Override
                            public void onRewardedAdClosed() {
                               // Assets.mRewardedAd = createAndLoadRewardedAd(getResources().getString(R.string.ID_ADMOB_TEST_REWARDED));
                                Assets.mRewardedAd = createAndLoadRewardedAd(getResources().getString(R.string.ID_ADMOB_REWARDED));

                            }

                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem reward) {
                                // User earned reward.
                            }

                            @Override
                            public void onRewardedAdFailedToShow(int errorCode) {
                                // Ad failed to display.
                            }
                        };
                        Assets.mRewardedAd.show(JeuAndroid.this, adCallback);
                    } else {
                       // Log.d("TAG", "The rewarded ad wasn't loaded yet.");

                    }    //Pause sound
                } else {
                    //   Log.d("TAG", "The interstitial wasn't loaded yet.");
                    Assets.mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            }
        });
    }


    public RewardedAd createAndLoadRewardedAd(String adUnitId) {
        RewardedAd rewardedAd = new RewardedAd(this, adUnitId);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                //Log.i("onRewardedAdLoaded: ", "Rewarded Ad successfully loaded");

            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
               // Log.i("onRewardedAdFailedToLoad: ", "Failed to load: Error code = " + errorCode);
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        return rewardedAd;
    }

}



