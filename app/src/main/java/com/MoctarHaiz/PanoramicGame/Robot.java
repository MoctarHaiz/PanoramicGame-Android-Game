package com.MoctarHaiz.PanoramicGame;

import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

public class Robot {
    public static final int sante = 4;
    ;
    public static int tileAndFeetShading = 15, largeur = Assets.personnage1.getWidth();
    public static int longueur = Assets.personnage1.getHeight(); //the length of the robot in the sprite
    public static Rect rectHautCorps = new Rect(0, 0, 0, 0);
    public static Rect rectBasCorps = new Rect(0, 0, 0, 0);
    public static Rect rectGlobal = new Rect(0, 0, 0, 0);
    private static Background bg1 = GameScreen.getBg1();
    private static Background bg2 = GameScreen.getBg2();
    final int VITESSESAUT = -40;
    final int VITESSESAUTPLUS = -35;
    final int VITESSEMOUVEMENT = 5;
    public boolean Saute = false;
    public boolean bougeAGauche = false;
    public boolean isSticked = false, bougeADroite = false;
    public boolean baisse = false;
    private int centerX = 150;
    private int centerY = 100; // 382
    private boolean pretATire = true;
    private int speedX = 0;
    private int speedY = 0;
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    public void update() {

        // Mouvement du joueur ou de la scene.
        if (speedX < 0) {

            centerX += speedX;

        }
       /* if (speedX == 0 || speedX < 0) {

            bg1.setSpeedX(0);
            bg2.setSpeedX(0);
        }*/

        //block robot at the begining of the screen


        if (speedX > 0 && centerX > 350) {
            System.out.println("SpeedX robot: " + speedX);
            bg1.setSpeedX(-VITESSEMOUVEMENT / 5);
            System.out.println("SpeedX background1" + bg1.getSpeedX());
            bg2.setSpeedX(-VITESSEMOUVEMENT / 5);

        }

        if (centerY <= 0) {
            collerHaut();
        } else {
            centerY += speedY;
            speedY += 3;
        }
        /*if (speedY > 100) { ///30
            Saute = true;
        }*/


        // Ne pas aller a gauche de l'ecran
        if (centerX + speedX <= 60) {
            centerX = 61;

        }
        Log.i("xxx", "" +largeur);
        rectHautCorps.set(centerX - largeur / 2, centerY - longueur / 2, centerX + largeur / 2, centerY);
        rectBasCorps.set(centerX - largeur / 4, rectHautCorps.bottom, centerX + largeur / 4, rectHautCorps.bottom + longueur / 2);
        int marge = 10;
        rectGlobal.set(centerX - largeur / 2 - marge, centerY - longueur / 2 - marge, centerX + largeur / 2 + marge, centerY + longueur / 2 + marge);

    }

    public void allerADroite() {
        if (baisse == false) {
            speedX = +VITESSEMOUVEMENT;
        }
    }

    public boolean iscolle() {
        return isSticked;
    }

    public void setSticked(boolean sticked) {
        this.isSticked = sticked;
    }

    public void collerHaut() {

        setSticked(true);
        //setSaute(false);
        //setBaisse(false);
        centerY = 0;
        speedY = 0;


    }

    public void allerAGauche() {
        if (baisse == false) {
            speedX = -VITESSEMOUVEMENT;
        }

    }

    public void arreterADroite() {
        setBougeADroite(false);
        stopper();

    }

    public void arreterAGauche() {
        setBougeAGauche(false);
        stopper();

    }

    public void stopper() {
        if (isBougeADroite() == false && isBougeAGauche() == false) {
            speedX = 0;
        }
        if (isBougeADroite() == false && isBougeAGauche() == true) {
            allerAGauche();
        }
        if (isBougeADroite() == true && isBougeAGauche() == false) {
            allerADroite();
        }
    }

    public void sauter() {
        if (Saute == false) {
            speedY = VITESSESAUT;
            Saute = true;
            baisse = false;
        }
    }

    // ////////////////////////
    public void sauterPlus() {
        if (Saute == false) {
            speedY = VITESSESAUTPLUS;
            Saute = true;
            baisse = false;
        }
    }

    // //////////////////////////

    public void shooter() {

        if (pretATire) {
            Projectile p = new Projectile(centerX + 40, centerY );
            projectiles.add(p);
        }
    }

    public ArrayList<Projectile> getProjectiles() { // //////mod
        return projectiles;
    }

    // /lES GETTERS
    public int getCenterX() {
        return centerX;
    }

    // /LEs SETTERS
    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public boolean isSaute() {
        return Saute;
    }

    public void setSaute(boolean aSaute) {
        this.Saute = aSaute;
    }

    public boolean isBaisse() {
        return baisse;
    }

    public void setBaisse(boolean baisse) {
        this.baisse = baisse;
    }

    public boolean isBougeADroite() {
        return bougeADroite;
    }

    public void setBougeADroite(boolean bougeADroite) {
        this.bougeADroite = bougeADroite;
    }

    public boolean isBougeAGauche() {
        return bougeAGauche;
    }

    public void setBougeAGauche(boolean bougeAGauche) {
        this.bougeAGauche = bougeAGauche;
    }

    public boolean isPretATire() {
        return pretATire;
    }

    public void setPretATire(boolean pretATire) {
        this.pretATire = pretATire;
    }

}
