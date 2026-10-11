/// Gameplay constants.
///
/// Distances are in virtual pixels: the game is designed for a 960 x 640
/// screen (30 x 20 tiles of 32 px) like the Android version. Speeds are per
/// fixed tick; the game runs 40 ticks per second, like the Android game loop.
///
/// The Android version moved things by raw device pixels, so its speed changed
/// from phone to phone. The values below are the Android ones on a
/// 1920 x 1280 screen (the size the art was drawn for), i.e. device px / 2.
abstract final class Config {
  static const int ticksPerSecond = 40;
  static const double tickSeconds = 1 / ticksPerSecond;
  static const int maxTicksPerFrame = 5;

  // Screen. The height is fixed; wide phones see more of the level instead of
  // getting a stretched picture.
  static const double viewHeight = 640;
  static const double designWidth = 960;

  // Level.
  static const double tileSize = 32;
  static const int tilesetColumns = 12;
  static const double wallInset = 5; // walls are 5 px smaller on right/bottom
  static const double backgroundSpeed = 1;
  static const double levelSpeed = 4; // tiles scroll 4x faster than the background

  // Hero ("Panoramic").
  static const double heroWidth = 64;
  static const double heroHeight = 128;
  static const double heroStartX = 75;
  static const double heroStartY = 50;
  static const double heroMinX = 30;
  static const double heroGravity = 1.5;
  static const double heroJumpSpeed = -32;
  static const double heroNudge = 7.5; // swipe right
  static const double heroFeetShading = 7.5; // sprite is drawn a bit lower than its hit box
  static const int lives = 3;
  static const int shieldTicks = 10; // red bob "cover" effect

  // Enemies (bandits).
  static const int enemyCount = 50;
  static const int enemyHealth = 2;
  static const double enemyWidth = 24;
  static const double enemyHeight = 48;
  static const double enemyRunSpeed = 5;
  static const double enemyGravity = 2;
  static const double enemyJumpSpeed = -5;
  static const double enemySpawnY = viewHeight - tileSize * 3;

  /// Android behaviour: a bandit only hurts the hero when its centre is above
  /// the hero's (falling from a platform), so bandits running on the same
  /// ground pass through him. Set to false to make any contact cost a life
  /// (a harder game).
  static const bool enemiesHurtOnlyFromAbove = true;

  // Projectiles (the blue ring).
  static const double projectileSpeed = 10;
  static const double projectileWidth = 17;
  static const double projectileHeight = 25;
  static const double projectileSpawnOffsetX = 20;
  static const double fireSize = heroWidth / 5;

  // Personalization: size of the "big head" face, relative to the hero height.
  static const double faceDiameter = heroHeight * 0.30;
}
