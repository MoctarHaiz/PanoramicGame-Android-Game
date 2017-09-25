package com.MoctarHaiz.PanoramicGame;

import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by almoctar on 18/06/2017.
 */
public class Level {
    private ArrayList<Tile> tilearray = new ArrayList<Tile>();
    private int x, y;
    private int speed;


    public Level(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.tilearray = new ArrayList<Tile>();
    }


    public Tile checkCollisionTop(Rect rectangle) {
        Tile result = null;
        for (int i = 0; i < tilearray.size(); i++) {
            Tile t = (Tile) tilearray.get(i);
            t.getmRectangle().set(t.getTileX(), t.getTileY(), t.getTileX() + t.getTileWidth(), t.getTileY() + t.getTileHeight());
            Tile.Type type = t.getType();
            if (Rect.intersects(rectangle, t.getmRectangle()) &&
                    (type == Tile.Type.tileGrass1
                            || type == Tile.Type.tileGrass2
                            || type == Tile.Type.tileGrass3
                            || type == Tile.Type.tileGrass7
                            || type == Tile.Type.tileGrass11
                            || type == Tile.Type.tileGrass13
                            || type == Tile.Type.tileGrass14
                            || type == Tile.Type.tileGrass15

                    )) {
                result = t;
            }
        }
        return result;
    }


    public Tile checkCollisionSide(Rect rectangle) {
        Tile result = null;
        for (int i = 0; i < tilearray.size(); i++) {
            Tile t = (Tile) tilearray.get(i);
            t.getmRectangle().set(t.getTileX(), t.getTileY(), t.getTileX() + t.getTileWidth(), t.getTileY() + t.getTileHeight());
            Tile.Type type = t.getType();
            if (Rect.intersects(rectangle, t.getmRectangle()) &&
                    (type == Tile.Type.tileGrass1
                            || type == Tile.Type.tileGrass4
                            || type == Tile.Type.tileGrass12)) {
                result = t;
            }
        }

        return result;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        for (int i = 0; i < tilearray.size(); i++) {

            Tile t = (Tile) tilearray.get(i);
           t.setSpeedX(GameScreen.getBg1().getSpeedX() * 5);
            t.setTileX(t.getTileX() + t.getSpeedX());
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ArrayList<Tile> getTilearray() {
        return tilearray;
    }

    public void setTilearray(ArrayList<Tile> tilearray) {
        this.tilearray = tilearray;
    }
}
