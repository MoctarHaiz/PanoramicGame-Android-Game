package com.MoctarHaiz.PanoramicGame;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.MoctarHaiz.framework.Graphics;
import com.MoctarHaiz.framework.Image;
import com.MoctarHaiz.framework.Input;
import com.MoctarHaiz.framework.Input.TouchEvent;
import com.MoctarHaiz.framework.Jeu;
import com.MoctarHaiz.framework.Screen;
import com.MoctarHaiz.framework.Sound;
import com.MoctarHaiz.framework.implementation.MultiTouchHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameScreen extends Screen {

    enum EtatDuJeu {
        EnCours, Pret, Pause, GameOver
    }

    EtatDuJeu etat = EtatDuJeu.EnCours;

    private static Robot Panoramic;
    //public static Enemi enemiPresent1, enemiPresent2, enemiPresent3, enemiPresent4, enemiPresent5, enemiPresent6, enemiPresent7, enemiPresent8;
    public static ArrayList<Enemi> listOfEnemies = new ArrayList<Enemi>();
    private Image PanoramicCurrentImage,
            personnage1, personnage2, personnage3,
            personnage4, personnage5, personnage6,
            personnage7, personnage8, personnage9,
            personnage10, personnage11, personnage12,
            personnage13, personnage14, personnage15,
            personnage16, personnage17,
            enemiCurrent,
            enemi1, enemi2, enemi3, enemi4, enemi5,
            enemi6, enemi7, enemi8, enemi9, enemi10,
            m1, m2, m3, m4,
            enemiMort1, enemiMort2, enemiMort3,
            enemiMort4, enemiMort5, enemiMort6,
            enemiMort7, enemiMort8, enemiMort9,
            enemiMort10;


    private Image personnageSauteActuel,
            personnageSaute1, personnageSaute2, personnageSaute3,
            personnageSaute4, personnageSaute5, personnageSaute6,
            personnageSaute7, personnageSaute8, personnageSaute9,
            personnageSaute10, personnageSaute11, personnageSaute12,
            personnageSaute13, personnageSaute14, personnageSaute15,
            personnageSaute16, personnageSaute17;


    private static Background bg1, bg2;
    public static Sound clickEnemy, click;
    private Animation PanoramicAnimationNormal, PanoramicAnimationSticked, PanoramicAnimationJumped, enemiAnimation, enemiAnimationDied, hammerAnimation;

    private Level level1 = new Level(0, 0, 0);

    int livesLeft = 1;
    Paint paint, paint2;

    public GameScreen(Jeu game) {
        super(game);

        // initialisation des objets du jeu
        bg1 = new Background(0, 0);
        bg2 = new Background(1191, 0);
        Panoramic = new Robot();

        //Adding enemies
        listOfEnemies.add(new Enemi(1500, 260));
        listOfEnemies.add(new Enemi(7000, 360));
        listOfEnemies.add(new Enemi(5500, 360));
        listOfEnemies.add(new Enemi(10000, 360));
        listOfEnemies.add(new Enemi(3400, 360));
        listOfEnemies.add(new Enemi(9500, 360));
        listOfEnemies.add(new Enemi(13000, 360));


        // Image Setups
        personnage1 = Assets.personnage1;
        personnage2 = Assets.personnage2;
        personnage3 = Assets.personnage3;
        personnage4 = Assets.personnage4;
        personnage5 = Assets.personnage5;
        personnage6 = Assets.personnage6;
        personnage7 = Assets.personnage7;
        personnage8 = Assets.personnage8;
        personnage9 = Assets.personnage9;
        personnage10 = Assets.personnage10;
        personnage11 = Assets.personnage11;
        personnage12 = Assets.personnage12;
        personnage13 = Assets.personnage13;
        personnage14 = Assets.personnage14;
        personnage15 = Assets.personnage15;
        personnage16 = Assets.personnage16;
        personnage17 = Assets.personnage17;


        enemi1 = Assets.enemi1;
        enemi2 = Assets.enemi2;
        enemi3 = Assets.enemi3;
        enemi4 = Assets.enemi4;
        enemi5 = Assets.enemi5;
        enemi6 = Assets.enemi6;
        enemi7 = Assets.enemi7;
        enemi8 = Assets.enemi8;
        enemi9 = Assets.enemi9;
        enemi10 = Assets.enemi10;

        enemiMort1 = Assets.enemiMort1;
        enemiMort2 = Assets.enemiMort2;
        enemiMort3 = Assets.enemiMort3;
        enemiMort4 = Assets.enemiMort4;
        enemiMort5 = Assets.enemiMort5;
        enemiMort6 = Assets.enemiMort6;
        enemiMort7 = Assets.enemiMort7;
        enemiMort8 = Assets.enemiMort8;
        enemiMort9 = Assets.enemiMort9;
        enemiMort10 = Assets.enemiMort10;

        m1 = Assets.m1;
        m2 = Assets.m2;
        m3 = Assets.m3;
        m4 = Assets.m4;

        //Adding Panoramic normal frames
        PanoramicAnimationNormal = new Animation();
        PanoramicAnimationNormal.addframes(personnage1, 15);
        PanoramicAnimationNormal.addframes(personnage2, 15);
        PanoramicAnimationNormal.addframes(personnage3, 15);
        PanoramicAnimationNormal.addframes(personnage4, 15);
        PanoramicAnimationNormal.addframes(personnage5, 15);
        PanoramicAnimationNormal.addframes(personnage6, 15);
        PanoramicAnimationNormal.addframes(personnage7, 15);
        PanoramicAnimationNormal.addframes(personnage8, 15);
        PanoramicAnimationNormal.addframes(personnage9, 15);
        PanoramicAnimationNormal.addframes(personnage10, 15);
        PanoramicAnimationNormal.addframes(personnage11, 15);
        PanoramicAnimationNormal.addframes(personnage12, 15);
        PanoramicAnimationNormal.addframes(personnage13, 15);
        PanoramicAnimationNormal.addframes(personnage14, 15);
        PanoramicAnimationNormal.addframes(personnage15, 15);
        PanoramicAnimationNormal.addframes(personnage16, 15);
        PanoramicAnimationNormal.addframes(personnage17, 15);

        //Adding Panoramic sticked frames
        PanoramicAnimationSticked = new Animation();
        PanoramicAnimationSticked.addframes(Assets.personnageSaute1, 15);

        //Adding Panoramic Jump frames
        PanoramicAnimationJumped = new Animation();
        PanoramicAnimationJumped.addframes(Assets.personnageSaute1, 15);
       /* PanoramicAnimationJumped.addframes(Assets.personnageSaute2, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute3, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute4, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute5, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute6, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute7, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute8, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute9, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute10, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute11, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute12, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute13, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute14, 15);
        PanoramicAnimationJumped.addframes(Assets.personnageSaute15, 15);*/


        enemiAnimation = new Animation();
        enemiAnimation.addframes(enemi1, 80);
        enemiAnimation.addframes(enemi2, 80);
        enemiAnimation.addframes(enemi3, 80);
        enemiAnimation.addframes(enemi4, 80);
        enemiAnimation.addframes(enemi5, 80);
        enemiAnimation.addframes(enemi6, 80);
        enemiAnimation.addframes(enemi7, 80);
        enemiAnimation.addframes(enemi8, 80);
        enemiAnimation.addframes(enemi9, 80);
        enemiAnimation.addframes(enemi10, 80);

        enemiAnimationDied = new Animation();
        enemiAnimationDied.addframes(enemiMort1, 50);
        enemiAnimationDied.addframes(enemiMort2, 50);
        enemiAnimationDied.addframes(enemiMort3, 50);
        enemiAnimationDied.addframes(enemiMort4, 50);
        enemiAnimationDied.addframes(enemiMort5, 50);
        enemiAnimationDied.addframes(enemiMort6, 50);
        enemiAnimationDied.addframes(enemiMort7, 50);
        enemiAnimationDied.addframes(enemiMort8, 50);
        enemiAnimationDied.addframes(enemiMort9, 50);
        enemiAnimationDied.addframes(enemiMort10, 50);

        PanoramicCurrentImage = PanoramicAnimationNormal.getImage();
        enemiCurrent = enemiAnimation.getImage();

        hammerAnimation = new Animation();
        hammerAnimation.addframes(m1, 100);
        hammerAnimation.addframes(m2, 100);
        hammerAnimation.addframes(m3, 100);
        hammerAnimation.addframes(m4, 100);

        //Getting sound
        click = Assets.click;
        clickEnemy = Assets.clickEnemy;

        //Loading Map
        loadMap(DebutJeu.map);

        // Definition d'un objet paint
        paint = new Paint();
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        paint2 = new Paint();
        paint2.setTextSize(100);
        paint2.setTextAlign(Paint.Align.CENTER);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.WHITE);

    }

    /**
     * Loading the tileset map
     *
     * @param path the path
     */
    private void loadMap(String path) {

        ArrayList<String> lignes = new ArrayList<String>();
        int width = 0;
        int height = 0;

        Scanner scanner = new Scanner(path);
        while (scanner.hasNextLine()) {
            String ligne = scanner.nextLine();
            // no more lines to read
            if (ligne == null) {
                break;
            }
            if (!ligne.startsWith("!")) { ///not a comment
                lignes.add(ligne);
                width = Math.max(width, ligne.length());
            }
        }

        height = lignes.size();

        for (int j = 0; j < height; j++) {
            String ligne = (String) lignes.get(j);
            String[] numbers = ligne.split(",");


            width = Math.max(width, numbers.length);
            for (int i = 0; i < width; i++) {
                if (i < numbers.length) {

                    if (!numbers[i].equals("")) {
                        Tile t = new Tile(i, j, Integer.parseInt(numbers[i]));
                        level1.getTilearray().add(t);
                    }
                }

            }

        }
    }


    /**
     * Loading the tileset map
     *
     * @param path the path
     */
    private void loadMap2(String path) {
        ArrayList<String> lignes = new ArrayList<String>();
        int width = 0;
        int height = 0;

        Scanner scanner = new Scanner(path);
        while (scanner.hasNextLine()) {
            String ligne = scanner.nextLine();
            // no more lines to read
            if (ligne == null) {
                break;
            }
            if (!ligne.startsWith("!")) { ///not a comment
                lignes.add(ligne);
                width = Math.max(width, ligne.length());
            }
        }

        height = lignes.size();

        for (int j = 0; j < height; j++) {
            String ligne = (String) lignes.get(j);

            for (int i = 0; i < width; i++) {
                if (i < ligne.length()) {
                    char ch = ligne.charAt(i);
                    Tile t = new Tile(i, j, Character.getNumericValue(ch));
                    level1.getTilearray().add(t);
                }
            }

        }

    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents(); // ///mod

        // We have four separate update methods .
        // Depending on the state of the game, we call different update methods.
        // update methods.

        if (etat == EtatDuJeu.Pret)
            updateReady(touchEvents);
        if (etat == EtatDuJeu.EnCours)
            updateRunning(game.getInput(), deltaTime);
        if (etat == EtatDuJeu.Pause)
            updatePaused(touchEvents);
        if (etat == EtatDuJeu.GameOver)
            updateGameOver(touchEvents);
    }


    /**
     * The game begin when the user click on the screen
     * Then one  changes the state to EtatDuJeu.EnCours
     *
     * @param touchEvents
     */
    private void updateReady(List<TouchEvent> touchEvents) {
        if (touchEvents.size() > 0)
            etat = EtatDuJeu.EnCours;
    }

    /**
     * The mai function of the game
     *
     * @param input
     * @param deltaTime
     */
    private void updateRunning(Input input, float deltaTime) {

        //Handling the swipe behaviour
        MultiTouchHandler.Direction swipeDirection = input.getSwipeDirection();
        if (swipeDirection != null) {
            switch (swipeDirection) {
                case down:
                    if (Panoramic.isSticked) {
                        //Panoramic descends
                        Panoramic.isSticked = !Panoramic.isSticked;
                        int temp = Panoramic.getCenterY();
                        Panoramic.setCenterY(++temp);
                    } else {
                        PanoramicCurrentImage = Assets.personnageAccroupi;
                        Panoramic.setBaisse(true);
                        //Panoramic.setSpeedX(0);
                    }
                    break;

                case up:
                    Panoramic.sauter();
                    PanoramicCurrentImage = PanoramicAnimationNormal.getImage();
                    break;

                case left:
                    //TODO
                    break;


                case right:
                    Panoramic.allerADroite();
                    break;
                case longUp:
                    // Panoramic.collerHaut();
                    // PanoramicCurrentImage = PanoramicAnimationNormal.getImage();

                    break;
            }
        }

        //Handling Shoot
        if (input.isShootRequested()) {
            Panoramic.shooter();
            // ///////////////////////// Update projectiles///////////////////////
            ArrayList<Projectile> projectiles = Panoramic.getProjectiles();
            for (int i = 0; i < projectiles.size(); i++) {
                Projectile p = (Projectile) projectiles.get(i);
                if (p.isVisible() == true) {
                    p.update();
                } else {
                    projectiles.remove(i);
                }
                Log.d("ok", "P1xx");
            }
        }

        //Handling Life
        if (livesLeft == 0) {
            etat = EtatDuJeu.GameOver;
        }
        //Handling Updates
        Panoramic.update();
        if (Panoramic.isSaute()) {
            PanoramicCurrentImage = PanoramicAnimationJumped.getImage();
        } else if (Panoramic.isSticked) {
            PanoramicCurrentImage = PanoramicAnimationSticked.getImage();
        } else if (Panoramic.isSaute() == false && Panoramic.isBaisse() == false && Panoramic.isSticked == false) {
            PanoramicCurrentImage = PanoramicAnimationNormal.getImage();
        }

        // ///////////////////////// Update() projectiles///////////////////////

        ArrayList<Projectile> projectiles = Panoramic.getProjectiles();
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = (Projectile) projectiles.get(i);
            if (p.isVisible() == true) {
                p.update();

            } else {
                projectiles.remove(i);
            }
            Log.d("ok", "P2");
        }

        updateTiles();
        updateAllEnemies(listOfEnemies);
        bg1.update();
        bg2.update();
        animate();
        animateHammer();


        //Panormaic false on a hole
        if (Panoramic.getCenterY() > 500) {
            etat = EtatDuJeu.GameOver;
        }
    }

    private void updateRunning0(List<TouchEvent> touchEvents, float deltaTime) {
        // Panoramic.allerADroite();
        // 1. toutes les touches sont gerees ici:
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) { //handle gerer toutes les touches de l'user
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {

                if (inBounds(event, 0, 285, 65, 65)) {
                    Panoramic.sauter();
                    PanoramicCurrentImage = PanoramicAnimationNormal.getImage();
                    Panoramic.setBaisse(false);
                }

				/*else if (inBounds(event, 0, 350, 65, 65)) {

					if (Panoramic.isBaisse() == false && Panoramic.isSaute() == false
							&& Panoramic.isPretATire()) {
						Panoramic.shooter();
					}
				}*/

                else if (inBounds(event, 0, 415, 65, 65)
                        && Panoramic.isSaute() == false) {
                    PanoramicCurrentImage = Assets.personnageAccroupi;
                    Panoramic.setBaisse(true);
                    //     Panoramic.setSpeedX(0);

                }

                if (inBounds(event, 700, 300, 100, 103)) {

                    if (Panoramic.isBaisse() == false && Panoramic.isSaute() == false && Panoramic.isPretATire()) {
                        Panoramic.shooter();
                    }
                }

               /* if (event.x > 400) {

                    Panoramic.allerADroite();
                    Panoramic.setBougeADroite(true);

                }*/


            }

            if (event.type == TouchEvent.TOUCH_UP) {

                if (inBounds(event, 0, 415, 65, 65)) {
                    if (Panoramic.isSticked) {
                        Panoramic.isSticked = !Panoramic.isSticked;
                        int temp = Panoramic.getCenterY();
                        Panoramic.setCenterY(++temp);
                    } else {
                        PanoramicCurrentImage = PanoramicAnimationNormal.getImage();
                        Panoramic.setBaisse(false);
                        //Panoramic.arreterADroite();
                        //
                    }
                }

                if (inBounds(event, 0, 0, 65, 65)) {
                    pause();

                }

				/*
                 * if (event.x > 400) { // Move right. Panoramic.arreterADroite(); }
				 */
            }

        }

        // verifier la vie

        if (livesLeft == 0) {
            etat = EtatDuJeu.GameOver;
        }

        // update() methods ici.
        // tous les updates
        Panoramic.update();
        if (Panoramic.isSaute()) {
            PanoramicCurrentImage = PanoramicAnimationJumped.getImage();
        } else if (Panoramic.isSticked) {
            PanoramicCurrentImage = PanoramicAnimationSticked.getImage();
        } else if (Panoramic.isSaute() == false && Panoramic.isBaisse() == false && Panoramic.isSticked == false) {
            PanoramicCurrentImage = PanoramicAnimationNormal.getImage();
        }

        // ///////////////////////// Update() projectiles///////////////////////

        ArrayList<Projectile> projectiles = Panoramic.getProjectiles();
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = (Projectile) projectiles.get(i);
            if (p.isVisible() == true) {
                p.update();

            } else {
                projectiles.remove(i);
            }
        }

        updateTiles();
        updateAllEnemies(listOfEnemies);


        bg1.update();
        bg2.update();
        animate();
        animateHammer();
        if (Panoramic.getCenterY() > 500) {
            etat = EtatDuJeu.GameOver;
        }
    }

    private void updateAllEnemies(ArrayList<Enemi> list) {
        for (Enemi e : list) {
            e.update();
        }
    }


    private boolean inBounds(TouchEvent event, int x, int y, int width,
                             int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    private void updatePaused(List<TouchEvent> touchEvents) { // //mod
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event, 0, 0, 800, 240)) {

                    if (!inBounds(event, 0, 0, 35, 35)) {
                        resume();
                    }
                }

                if (inBounds(event, 0, 240, 800, 240)) {
                    nullify();
                    goToMenu();
                }
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) { // /mod
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (inBounds(event, 0, 0, 800, 480)) {
                    nullify();
                    goToMenu();
                    //game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }

    }

    private void updateTiles() {

        //check if there at least one SIDE collision with  robot
       /* for (int i = 0; i < tilearray.size(); i++) {
            Tile t = (Tile) tilearray.get(i);

            if (t.checkCollisionSide(Panoramic.rectGlobal)) {
                sideCollissionDetected = true;
                Panoramic.setCenterX(t.getTileX() - 45);
                Panoramic.setSpeedX(0);
                break;
            } else {

                t.setSpeedX(GameScreen.getBg1().getSpeedX() * 5);
                t.setTileX(t.getTileX() + t.getSpeedX());
                t.getmRectangle().set(t.getTileX(), t.getTileY(), t.getTileX() + 40, t.getTileY() + 40);
            }
        }*/
       //Checking top collision
        Tile collingTile = level1.checkCollisionTop(Panoramic.rectGlobal);
        if (collingTile != null) { //Collision detected
            Panoramic.setSaute(false);
            Panoramic.setSpeedY(0);
            Panoramic.setCenterY(collingTile.getTileY() - Panoramic.longueur / 2);
        }

         //Checking side collision
        Tile collingTileSideFoot = level1.checkCollisionSide(Panoramic.rectHautCorps);
        Tile collingTileSideChest = level1.checkCollisionSide(Panoramic.rectBasCorps);
        if (collingTileSideChest != null || collingTileSideFoot != null) { //Collision detected
          // Panoramic.setCenterX(collingTileSide.getTileX());
            Panoramic.setSpeedX(0);
            GameScreen.getBg1().setSpeedX(0);
            GameScreen.getBg2().setSpeedX(0);
        } else { //Move Level + bg1 + bg2
            level1.setX(level1.getX() + GameScreen.getBg1().getSpeedX() * 5);
            GameScreen.getBg1().setSpeedX(-5);
            GameScreen.getBg2().setSpeedX(-5);
        }
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawImage(Assets.background, bg1.getBgX(), bg1.getBgY());
        g.drawImage(Assets.background, bg2.getBgX(), bg2.getBgY());
        paintTiles(g);

        ArrayList<Projectile> projectiles = Panoramic.getProjectiles();
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = (Projectile) projectiles.get(i);
            // g.drawRect(p.getX(), p.getY(), 35, 50, Color.YELLOW);
            g.drawImage(hammerAnimation.getImage(), p.getX(), p.getY());
        }

        // //////////////////////////////////POUR LE YELLOW-RED/////////////////////////////////////////
        // g.drawRect(Robot.rectGlobal.left, Robot.rectGlobal.top, Robot.rectGlobal.width(), Robot.rectGlobal.height(), Color.YELLOW);
        // ////////////////////////////////////////////////////////////////////////////////////////////

        if (DebutJeu._DEBUG) {
            g.drawRect(Robot.rectGlobal.left, Robot.rectGlobal.top, Robot.rectGlobal.width(), Robot.rectGlobal.height(), Color.YELLOW);
            g.drawRect(Robot.rectHautCorps.left, Robot.rectHautCorps.top, Robot.rectHautCorps.width(), Robot.rectHautCorps.height(), Color.BLUE);
            g.drawRect(Robot.rectBasCorps.left, Robot.rectBasCorps.top, Robot.rectBasCorps.width(), Robot.rectBasCorps.height(), Color.RED);

        }

        g.drawImage(PanoramicCurrentImage, Panoramic.getCenterX() - Robot.largeur / 2,
                Panoramic.getCenterY() - Robot.longueur / 2 + Robot.tileAndFeetShading);


        if (Panoramic.isSticked)
            //Draw yellow line
            g.drawRect(Panoramic.getCenterX() - 70, Panoramic.getCenterY(), 90, 5, Color.YELLOW);

        //Rectangle of bandit
        if (DebutJeu._DEBUG) {
            g.drawRect(listOfEnemies.get(0).r.left, listOfEnemies.get(0).r.top, listOfEnemies.get(0).r.width(), listOfEnemies.get(0).r.height(), Color.YELLOW);

        }
        //colision tiles & ennemies
       /*for (Rect r : Tile.l                ) {
            g.drawRect(r.left, r.top, r.width(), r.height(), Color.RED);
        }*/

        //colision tiles & Panoramic

        /*for (Rect r : Tile.lrect) {f
            g.drawRect(r.left, r.top, r.width(), r.height(), Color.RED);
            // Tile.lrect.remove(r);
        }*/
        // Tile.lrect.clear();
        //  g.drawRect(enemiPresent1.getCenterX() - 48, enemiPresent1.getCenterY() - 48, Enemi.largeur, Enemi.longueur, Color.RED);
        // ////////////////////////////////////////////////////////////////////////////////////////////

        for (Enemi e : listOfEnemies) {
            if (!e.estTouche) {
                g.drawImage(enemiAnimation.getImage(), e.getCenterX() - e.largeur / 2, e.getCenterY() - e.longueur / 2);
            } else {
                g.drawImage(enemiAnimationDied.getImage(), e.getCenterX() - e.largeur / 2, e.getCenterY() - e.longueur / 2);

            }

        }


        // //////////////////////////////////////////////////////mod

        // Example:
        // g.drawImage(Assets.background, 0, 0);
        // g.drawImage(Assets.character, characterX, characterY);

        // Secondly, draw the UI above the game elements.
        if (etat == EtatDuJeu.Pret)
            drawReadyUI();
        if (etat == EtatDuJeu.EnCours)
            drawRunningUI();
        if (etat == EtatDuJeu.Pause)
            drawPausedUI();
        if (etat == EtatDuJeu.GameOver)
            drawGameOverUI();

    }

    /**
     * Function to draw the tileSet
     *
     * @param g
     */
    private void paintTiles(Graphics g) {
        for (int i = 0; i < level1.getTilearray().size(); i++) {
            Tile t = (Tile) level1.getTilearray().get(i);
            if (t.getType() != Tile.Type.none) {
                {//  g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY());
                    int stepX = t.getId() % 11;
                    int stepY = t.getId() / 11;


                    g.drawImage(Assets.tileSet, t.getTileX(), t.getTileY(),
                            stepX * t.getTileWidth(), stepY * t.getTileHeight(),
                            t.getTileWidth(), t.getTileHeight());
                }
            }
        }
    }

    public void animate() {
        PanoramicAnimationNormal.update(10);
        PanoramicAnimationJumped.update(10);
        PanoramicAnimationSticked.update(10);
        enemiAnimation.update(50);
        enemiAnimationDied.update(50);
        // hammerAnimation.update(50);
    }

    public void animateHammer() {
        hammerAnimation.update(50);
    }

    private void nullify() {

        // Set all variables to null. You will be recreating them in the
        // constructor.
        paint = null;
        paint2 = null;
        bg1 = null;
        bg2 = null;
        Panoramic = null;

        listOfEnemies.clear();


        PanoramicCurrentImage = null;
        enemiCurrent = null;
        personnage1 = null;
        personnage2 = null;
        personnage4 = null;
        personnage5 = null;
        personnage6 = null;
        personnage7 = null;
        personnage8 = null;
        personnage3 = null;

        enemi1 = null;
        enemi2 = null;
        enemi3 = null;
        enemi4 = null;
        enemi5 = null;
        enemi6 = null;
        enemi7 = null;
        enemi8 = null;
        enemi9 = null;
        enemi10 = null;

        enemiMort1 = null;
        enemiMort2 = null;
        enemiMort3 = null;
        enemiMort4 = null;
        enemiMort5 = null;
        enemiMort6 = null;
        enemiMort7 = null;
        enemiMort8 = null;
        enemiMort9 = null;
        enemiMort10 = null;

        m1 = null;
        m2 = null;
        m3 = null;
        m4 = null;


        PanoramicAnimationNormal = null;
        PanoramicAnimationJumped = null;
        PanoramicAnimationSticked = null;
        enemiAnimation = null;
        enemiAnimationDied = null;
        hammerAnimation = null;

        // Call garbage collector to clean up memory.
        System.gc();
        // runfinalizesonexit(true);

    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);
        g.drawString("Click to play", 400, 240, paint);

    }

    private void drawRunningUI() {
        Graphics g = game.getGraphics();
        //g.drawImage(Assets.button, 0, 285, 0, 0, 65, 65); //up button
        //g.drawImage(Assets.button, 0, 350, 0, 65, 65, 65);
        //g.drawImage(Assets.button, 0, 415, 0, 130, 65, 63); //down button
        //g.drawImage(Assets.button, 0, 0, 0, 195, 65, 65); //pause button
        //g.drawImage(Assets.buttonTire, 700, 300);  //fire button

    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        // Darken the entire screen so you can display the Paused screen.
        g.drawARGB(155, 0, 0, 0);
        g.drawString("Continuer...", 400, 165, paint2);
        g.drawString("Menu", 400, 360, paint2);

    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1281, 801, Color.BLACK);
        g.drawString("GAME OVER.", 400, 240, paint2);
        g.drawString("Cliquer pour revenir.", 400, 290, paint);

    }

    @Override
    public void pause() {
        if (etat == EtatDuJeu.EnCours)
            etat = EtatDuJeu.Pause;

    }

    @Override
    public void resume() {
        if (etat == EtatDuJeu.Pause)
            etat = EtatDuJeu.EnCours;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {
        pause();
    }

    private void goToMenu() {
        // TODO Auto-generated method stub
        game.setScreen(new MainMenuScreen(game));

    }

    public static Background getBg1() {
        // TODO Auto-generated method stub
        return bg1;
    }

    public static Background getBg2() {
        // TODO Auto-generated method stub
        return bg2;
    }

    public static Robot getPanoramic() {
        // TODO Auto-generated method stub
        return Panoramic;
    }


}
