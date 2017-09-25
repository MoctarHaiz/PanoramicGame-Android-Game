package com.MoctarHaiz.PanoramicGame;

import android.graphics.Rect;

import com.MoctarHaiz.framework.Image;

import java.util.ArrayList;

public class Tile {

    private int tileX, tileY;
    private int speedX;
    private Type type;
    public Image tileImage;
    private int tileWidth = 32, tileHeight = 32;
    private int id;

    private Background bg = GameScreen.getBg1();
    private Robot robot = GameScreen.getPanoramic();
    private Rect mRectangle;

    public Rect getRect() {
        return mRectangle;
    }


    public enum Type {
        tileGrass1, tileGrass2, tileGrass3, tileGrass4, tileGrass5, tileGrass6, tileGrass7, tileGrass8, tileGrass9, tileGrass10, tileGrass11, tileGrass12, tileGrass13, tileGrass14, tileGrass15, tileGrass16,
        tileTreeSmall1, tileTreeSmall2, tileTreeSmall3, tileTreeSmall4,
        tileTreeBig1, tileTreeBig2, tileTreeBig3, tileTreeBig4,
        mushroom1, mushroom2,
        treeTrunk,
        signArrow, signRectangle,
        box,
        grass1, grass2,
        grass3, grass4,
        grass5, grass6,
        grass7, grass8,
        waterTop, waterBottom,
        containerRed, containerGreen,
        rope1, rope2, rope3,
        stone1, stone2, stone3, stone4, stone5, stone6, stone7, stone8,
        gridGrey1, gridGrey2,
        bigStone1, bigStone2, bigStone3, bigStone4, bigStone5, bigStone6, bigStone7, bigStone8,
        none
    }

    public Tile(int x, int y, int id) {
        this.tileX = x * tileWidth;
        this.tileY = y * tileHeight;
        this.id = id;
        this.type = type;
        mRectangle = new Rect();

        if (id == 1) {
            type = Type.tileGrass1;
        } else if (id == 2) {
            type = Type.tileGrass2;
        } else if (id == 3) {
            type = Type.tileGrass3;
        } else if (id == 4) {
            type = Type.waterTop;
        } else if (id == 5) {
            type = Type.tileTreeSmall1;
        } else if (id == 6) {
            type = Type.tileTreeSmall2;
        } else if (id == 7) {
            type = Type.mushroom1;
        } else if (id == 8) {
            type = Type.treeTrunk;
        } else if (id == 9) {
            type = Type.tileGrass3;
        } else if (id == 10) {
            type = Type.tileGrass1;
        } else if (id == 11) {
            type = Type.tileGrass2;
        } else if (id == 12) {
            type = Type.tileGrass4;
        } else if (id == 13) {
            type = Type.tileGrass5;
        } else if (id == 14) {
            type = Type.tileGrass6;
        } else if (id == 15) {
            type = Type.waterBottom;
        } else if (id == 16) {
            type = Type.tileTreeSmall3;
        } else if (id == 17) {
            type = Type.tileTreeSmall4;
        } else if (id == 18) {
            type = Type.mushroom2;
        } else if (id == 19) {
            type = Type.signArrow;
        } else if (id == 20) {
            type = Type.signRectangle;
        } else if (id == 21) {
            type = Type.box;
        } else if (id == 22) {
            type = Type.tileGrass7;
        } else if (id == 23) {
            type = Type.tileGrass8;
        } else if (id == 24) {
            type = Type.tileGrass9;
        } else if (id == 25) {
            type = Type.tileGrass10;
        } else if (id == 26) {
            type = Type.tileGrass11;
        } else if (id == 27) {
            type = Type.tileTreeBig1;
        } else if (id == 28) {
            type = Type.tileTreeBig2;
        } else if (id == 29) {
            type = Type.grass1;
        } else if (id == 30) {
            type = Type.grass2;
        } else if (id == 31) {
            type = Type.grass3;
        } else if (id == 32) {
            type = Type.grass4;
        } else if (id == 33) {
            type = Type.tileGrass12;
        } else if (id == 34) {
            type = Type.tileGrass13;
        } else if (id == 35) {
            type = Type.tileGrass14;
        } else if (id == 36) {
            type = Type.tileGrass15;
        } else if (id == 37) {
            type = Type.tileGrass16;
        } else if (id == 38) {
            type = Type.tileTreeBig3;
        } else if (id == 39) {
            type = Type.tileTreeBig4;
        } else if (id == 40) {
            type = Type.grass5;
        } else if (id == 41) {
            type = Type.grass6;
        } else if (id == 42) {
            type = Type.grass7;
        } else if (id == 43) {
            type = Type.grass8;
        } else if (id == 44) {
            type = Type.tileGrass3;
        } else if (id == 45) {
            type = Type.tileGrass1;
        } else if (id == 46) {
            type = Type.tileGrass2;
        } else if (id == 47) {
            type = Type.tileGrass3;
        } else if (id == 48) {
            type = Type.tileTreeBig3;
        } else if (id == 49) {
            type = Type.tileTreeBig4;
        } else if (id == 50) {
            type = Type.grass5;
        } else if (id == 51) {
            type = Type.grass6;
        } else if (id == 52) {
            type = Type.containerRed;
        } else if (id == 53) {
            type = Type.containerGreen;
        } else if (id == 54) {
            type = Type.tileGrass3;
        } else if (id == 55) {
            type = Type.tileGrass1;
        } else if (id == 56) {
            type = Type.tileGrass2;
        } else if (id == 57) {
            type = Type.tileGrass3;
        } else if (id == 58) {
            type = Type.tileTreeBig3;
        } else if (id == 59) {
            type = Type.tileTreeBig4;
        } else if (id == 104) {
            type = Type.rope1;
        } else if (id == 105) {
            type = Type.rope2;
        } else if (id == 106) {
            type = Type.rope3;
        } else if (id == 110) {
            type = Type.bigStone1;
        } else if (id == 111) {
            type = Type.bigStone2;
        } else if (id == 112) {
            type = Type.bigStone3;
        } else if (id == 113) {
            type = Type.bigStone4;
        } else if (id == 121) {
            type = Type.bigStone5;
        } else if (id == 122) {
            type = Type.bigStone6;
        } else if (id == 123) {
            type = Type.bigStone7;
        } else if (id == 124) {
            type = Type.bigStone8;
        } else if (id == 125) {
            type = Type.rope3;
        } else {
            type = Type.none;
        }

    }

    public void update() {
        speedX = bg.getSpeedX() * 5;
        tileX += speedX;
        this.mRectangle.set(tileX, tileY, tileX + 40, tileY + 40);
        if (Rect.intersects(mRectangle, Robot.rectGlobal) && type != Type.none) {
            checkCollisionVertical(Robot.rectBasCorps);
          if( checkCollisionSide(Robot.rectGlobal)){

          }else{
             //speedX = bg.getSpeedX() * 5;
            //  tileX += speedX;
          }
        }else{


        }

        verifierCollisionVerticaleEnnemies();
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public Image getTileImage() {
        return tileImage;
    }

    public void setTileImage(Image tileImage) {
        this.tileImage = tileImage;
    }

    public boolean checkCollisionVertical(Rect rbas) {
        boolean result = false;
        if (Rect.intersects(rbas, this.mRectangle) &&
                (type == Type.tileGrass1
                        || type == Type.tileGrass2
                        || type == Type.tileGrass3
                        || type == Type.tileGrass7
                        || type == Type.tileGrass11
                        || type == Type.tileGrass13
                        || type == Type.tileGrass14
                        || type == Type.tileGrass15

                )) {
           result = true;
            lrect.add(this.mRectangle);
        }

        return result;
    }

    public boolean checkCollisionSide(Rect rec) {
        boolean result  = false;
        if (type == Type.tileGrass1 || type == Type.tileGrass4 || type == Type.tileGrass12) {
            if (Rect.intersects(rec, this.mRectangle)) {
                result = true;
            }
        }
        return result;
    }


    public void verifierCollisionVerticaleEnnemies() {
        for (Enemi e : GameScreen.listOfEnemies) {
            if (Rect.intersects(e.r, this.mRectangle) &&
                    (type == Type.tileGrass1
                            || type == Type.tileGrass2
                            || type == Type.tileGrass3
                            || type == Type.tileGrass7
                            || type == Type.tileGrass11
                            || type == Type.tileGrass13
                            || type == Type.tileGrass14
                            || type == Type.tileGrass15

                    )) {
                e.setCenterY(tileY - e.longueur / 2);
                e.setSpeedY(0);
                l.add(this.mRectangle);

            }
        }

    }


    public static ArrayList<Rect> lrect = new ArrayList<Rect>();


    public static ArrayList<Rect> l = new ArrayList<Rect>();


    //**Getter and Setters**//

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public Background getBg() {
        return bg;
    }

    public void setBg(Background bg) {
        this.bg = bg;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public Rect getmRectangle() {
        return mRectangle;
    }

    public void setmRectangle(Rect mRectangle) {
        this.mRectangle = mRectangle;
    }
}
