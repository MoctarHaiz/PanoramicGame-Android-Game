package com.MoctarHaiz.PanoramicGame;

import android.graphics.Rect;

public class Projectile {

    private int x, y, speedX;
    private boolean visible;

    private Rect r;

    public Projectile(int startX, int startY) {
        x = startX;
        y = startY;
        speedX = 20;
        visible = true;
        r = new Rect(0, 0, 0, 0);
        GameScreen.click.play(5);
    }

    public void update() {
        x += speedX;
        r.set(x, y, x + 34, y + 50);


        if (x > 800) {
            visible = false;
            r = null;
        }
        if (x < 800) {
            verifierCollision();
        }

    }

    private void verifierCollision() {

        for (Enemi e : GameScreen.listOfEnemies) {
            if (Rect.intersects(r, e.r)) {

                visible = false;
                GameScreen.clickEnemy.play(5);
                e.estTouche = true;


                if (e.sante > 0) {
                    e.sante -= 1;
                }
                if (e.sante == 0) {
                    e.setCenterX(-100);
                    // GameScreen.score += 5;
                }

            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeedX() {
        return speedX;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
