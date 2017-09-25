package com.MoctarHaiz.PanoramicGame;

import com.MoctarHaiz.framework.Graphics;
import com.MoctarHaiz.framework.Graphics.ImageFormat;
import com.MoctarHaiz.framework.Jeu;
import com.MoctarHaiz.framework.Screen;

public class LoadingScreen extends Screen {
    public LoadingScreen(Jeu game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.menu = g.newImage("menu1.png", ImageFormat.RGB565);

        Assets.background = g.newImage("background.jpg", ImageFormat.RGB565);

        Assets.personnage1 = g.newImage("panoramicRunning/panoramic1.png", ImageFormat.ARGB4444);
        Assets.personnage2 = g.newImage("panoramicRunning/panoramic2.png", ImageFormat.ARGB4444);
        Assets.personnage3 = g.newImage("panoramicRunning/panoramic3.png", ImageFormat.ARGB4444);
        Assets.personnage4 = g.newImage("panoramicRunning/panoramic4.png", ImageFormat.ARGB4444);
        Assets.personnage5 = g.newImage("panoramicRunning/panoramic5.png", ImageFormat.ARGB4444);
        Assets.personnage6 = g.newImage("panoramicRunning/panoramic6.png", ImageFormat.ARGB4444);
        Assets.personnage7 = g.newImage("panoramicRunning/panoramic7.png", ImageFormat.ARGB4444);
        Assets.personnage8 = g.newImage("panoramicRunning/panoramic8.png", ImageFormat.ARGB4444);
        Assets.personnage9 = g.newImage("panoramicRunning/panoramic9.png", ImageFormat.ARGB4444);
        Assets.personnage10 = g.newImage("panoramicRunning/panoramic10.png", ImageFormat.ARGB4444);
        Assets.personnage11 = g.newImage("panoramicRunning/panoramic11.png", ImageFormat.ARGB4444);
        Assets.personnage12 = g.newImage("panoramicRunning/panoramic12.png", ImageFormat.ARGB4444);
        Assets.personnage13 = g.newImage("panoramicRunning/panoramic13.png", ImageFormat.ARGB4444);
        Assets.personnage14 = g.newImage("panoramicRunning/panoramic14.png", ImageFormat.ARGB4444);
        Assets.personnage15 = g.newImage("panoramicRunning/panoramic15.png", ImageFormat.ARGB4444);
        Assets.personnage16 = g.newImage("panoramicRunning/panoramic16.png", ImageFormat.ARGB4444);
        Assets.personnage17 = g.newImage("panoramicRunning/panoramic17.png", ImageFormat.ARGB4444);


        Assets.personnageSaute1 = g.newImage("jump/panoramicjump1.png", ImageFormat.ARGB4444);
        Assets.personnageSaute2 = g.newImage("jump/panoramicjump2.png", ImageFormat.ARGB4444);
        Assets.personnageSaute3 = g.newImage("jump/panoramicjump3.png", ImageFormat.ARGB4444);
        Assets.personnageSaute4 = g.newImage("jump/panoramicjump4.png", ImageFormat.ARGB4444);
        Assets.personnageSaute5 = g.newImage("jump/panoramicjump5.png", ImageFormat.ARGB4444);
        Assets.personnageSaute6 = g.newImage("jump/panoramicjump6.png", ImageFormat.ARGB4444);
        Assets.personnageSaute7 = g.newImage("jump/panoramicjump7.png", ImageFormat.ARGB4444);
        Assets.personnageSaute8 = g.newImage("jump/panoramicjump8.png", ImageFormat.ARGB4444);
        Assets.personnageSaute9 = g.newImage("jump/panoramicjump9.png", ImageFormat.ARGB4444);
        Assets.personnageSaute10 = g.newImage("jump/panoramicjump10.png", ImageFormat.ARGB4444);
        Assets.personnageSaute11 = g.newImage("jump/panoramicjump11.png", ImageFormat.ARGB4444);
        Assets.personnageSaute12 = g.newImage("jump/panoramicjump12.png", ImageFormat.ARGB4444);
        Assets.personnageSaute13 = g.newImage("jump/panoramicjump13.png", ImageFormat.ARGB4444);
        Assets.personnageSaute14 = g.newImage("jump/panoramicjump14.png", ImageFormat.ARGB4444);
        Assets.personnageSaute15 = g.newImage("jump/panoramicjump15.png", ImageFormat.ARGB4444);

        Assets.personnageColle = g.newImage("colle.png", ImageFormat.ARGB4444);

        Assets.personnageAccroupi = g.newImage("panoramicaccroupi.png", ImageFormat.ARGB4444);

        Assets.enemi1 = g.newImage("bandit1.png", ImageFormat.ARGB4444);
        Assets.enemi2 = g.newImage("bandit2.png", ImageFormat.ARGB4444);
        Assets.enemi3 = g.newImage("bandit3.png", ImageFormat.ARGB4444);
        Assets.enemi4 = g.newImage("bandit4.png", ImageFormat.ARGB4444);
        Assets.enemi5 = g.newImage("bandit5.png", ImageFormat.ARGB4444);
        Assets.enemi6 = g.newImage("bandit6.png", ImageFormat.ARGB4444);
        Assets.enemi7 = g.newImage("bandit7.png", ImageFormat.ARGB4444);
        Assets.enemi8 = g.newImage("bandit8.png", ImageFormat.ARGB4444);
        Assets.enemi9 = g.newImage("bandit9.png", ImageFormat.ARGB4444);
        Assets.enemi10 = g.newImage("bandit10.png", ImageFormat.ARGB4444);


        Assets.enemiMort1 = g.newImage("banditMort1.png", ImageFormat.ARGB4444);
        Assets.enemiMort2 = g.newImage("banditMort2.png", ImageFormat.ARGB4444);
        Assets.enemiMort3 = g.newImage("banditMort3.png", ImageFormat.ARGB4444);
        Assets.enemiMort4 = g.newImage("banditMort4.png", ImageFormat.ARGB4444);
        Assets.enemiMort5 = g.newImage("banditMort5.png", ImageFormat.ARGB4444);
        Assets.enemiMort6 = g.newImage("banditMort6.png", ImageFormat.ARGB4444);
        Assets.enemiMort7 = g.newImage("banditMort7.png", ImageFormat.ARGB4444);
        Assets.enemiMort8 = g.newImage("banditMort8.png", ImageFormat.ARGB4444);
        Assets.enemiMort9 = g.newImage("banditMort9.png", ImageFormat.ARGB4444);
        Assets.enemiMort10 = g.newImage("banditMort10.png", ImageFormat.ARGB4444) ;

       /* Assets.tiledirt = g.newImage("tiledirt.png", ImageFormat.RGB565);
        Assets.tilegrassTop = g.newImage("tilegrasstop.png", ImageFormat.RGB565);
        Assets.tilegrassBot = g.newImage("tilegrassbot.png", ImageFormat.RGB565);
        Assets.tilegrassLeft = g.newImage("tilegrassleft.png", ImageFormat.RGB565);
        Assets.tilegrassRight = g.newImage("tilegrassright.png", ImageFormat.ARGB4444);
*/

        //Tiles Map2
        Assets.tileSet = g.newImage("level1.png", ImageFormat.RGB565);





        Assets.m1 = g.newImage("n1.png", ImageFormat.ARGB4444);
        Assets.m2 = g.newImage("n2.png", ImageFormat.ARGB4444);
        Assets.m3 = g.newImage("n3.png", ImageFormat.ARGB4444);
        Assets.m4 = g.newImage("n4.png", ImageFormat.ARGB4444);

        // pour creer un son
        Assets.click = game.getAudio().createSound("projectile.ogg");
        Assets.clickEnemy = game.getAudio().createSound("enemy.ogg");



        game.setScreen(new MainMenuScreen(game));

    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawImage(Assets.splash, 0, 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }
}