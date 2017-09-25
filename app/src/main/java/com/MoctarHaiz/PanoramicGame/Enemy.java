package com.MoctarHaiz.PanoramicGame;

import android.graphics.Rect;

public class Enemy {

    public static final int largeur = 120;
    public static final int longueur = 149;
    public int sante = 2;

    private int puissance, speedX,speedY, centerX, centerY;
    private Background bg = GameScreen.getBg1();

    private Robot robot = GameScreen.getPanoramic();

    private int vitesseDeMouvement;
    public Rect r = new Rect(0, 0, 0, 0);


    public boolean estTouche = false;

    public void update() {
        suivre();
        centerX += speedX;
        speedX = bg.getSpeedX() * 5 + vitesseDeMouvement;




        r.set(centerX - 60, centerY - 75, centerX + 60, centerY + 75);
        // Enemy pesanteur
        centerY += speedY;
        speedY += 10;


        if (Rect.intersects(r, Robot.rectGlobal)) {
            checkCollisionWithRobot();
        }
    }

    private void checkCollisionWithRobot() {
        if (Rect.intersects(r, Robot.rectHautCorps) || Rect.intersects(r, Robot.rectBasCorps)) {
            GameScreen.clickEnemy.play(5);
        }
    }

    public void suivre() {


        if (centerX < -95 || centerX > 810) {
            vitesseDeMouvement = 0;
        }

        //19/O5 Modified
        //else if (Math.abs(robot.getCenterX() - centerX) < 5) {
        //vitesseDeMouvement = 0;
        //}

        else {

            if (robot.getCenterX() >= centerX) {
                //vitesseDeMouvement = 4;

            } else {
                vitesseDeMouvement = -4;
            }
        }

    }

    public void mourir() {

    }

    public void attaquer() {

    }

    public int getPuissance() {
        return puissance;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public int getCenterY() {
        return centerY;
    }

    public Background getBg() {
        return bg;
    }

    public void setPuissance(int puissance) {
        this.puissance = puissance;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setBg(Background bg) {
        this.bg = bg;
    }

}
