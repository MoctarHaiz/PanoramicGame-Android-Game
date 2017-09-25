/**
 * This Class contains all assets NAMES ( resources ) used further in this game
 */
package com.MoctarHaiz.PanoramicGame;

import com.MoctarHaiz.framework.Image;
import com.MoctarHaiz.framework.Music;
import com.MoctarHaiz.framework.Sound;


public class Assets {

	/*
     * public static Image menu, splash, background, character, character2,
	 * character3, heliboy, heliboy2, heliboy3, heliboy4, heliboy5; public
	 * static Image tiledirt, tilegrassTop, tilegrassBot, tilegrassLeft,
	 * tilegrassRight, characterJump, characterDown;
	 */

    /**
     * Panoramic Assets
     */
    public static Image menu, splash, image, background, personnage1, personnage2, personnage3, personnage4, personnage5,
            personnage6, personnage7, personnage8, personnage10, personnage9, personnage11, personnage12, personnage13, personnage14, personnage15, personnage16, personnage17,
            personnageSaute1, personnageSaute2, personnageSaute3, personnageSaute4, personnageSaute5,
            personnageSaute6, personnageSaute7, personnageSaute8, personnageSaute10, personnageSaute9, personnageSaute11, personnageSaute12, personnageSaute13,
            personnageSaute14, personnageSaute15, personnageColle,
            personnageAccroupi, enemi1, enemi2,
            enemi3, enemi4, enemi5, enemi6, enemi7, enemi8, enemi9, enemi10, enemiMort1, enemiMort2,
            enemiMort3, enemiMort4, enemiMort5, enemiMort6, enemiMort7, enemiMort8, enemiMort9, enemiMort10;

    /**
     * TileSet Assets
     */
    public static Image tileSet,
            tileGrass1, tileGrass2, tileGrass3, tileGrass4, tileGrass5, tileGrass6, tileGrass7, tileGrass8, tileGrass9, tileGrass10, tileGrass11, tileGrass12, tileGrass13, tileGrass14,
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
            containerRed, containerGreen,
            rope1, rope2, roe3,
            stone1, stone2, stone3, stone4, stone5, stone6, stone7, stone8,
            gridGrey1, gridGrey2;


    public static Sound clickEnemy, click;
    public static Music theme;
    public static Image m1, m2, m3, m4;


    public static void load(DebutJeu debutJeu) {
        // TODO Auto-generated method stub
        theme = debutJeu.getAudio().createMusic("chaka.mp3");
        theme.setLooping(true);
        theme.setVolume(0.85f);
        theme.play();

    }

}