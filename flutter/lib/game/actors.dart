import 'dart:ui';

import 'config.dart';

enum HeroPose { run, jump, stick, crouch }

/// The hero ("Panoramic"). Port of the Android `Actor` class.
///
/// Positions are screen coordinates: the hero stays near the left of the
/// screen while the level scrolls under him.
class Panoramic {
  double x = Config.heroStartX;
  double y = Config.heroStartY;
  double previousX = Config.heroStartX;
  double previousY = Config.heroStartY;
  double speedY = 0;

  bool jumping = false;
  bool crouching = false;
  bool stuck = false; // hanging from the top of the screen
  bool onGround = false;

  static const double width = Config.heroWidth;
  static const double height = Config.heroHeight;

  Rect get body => Rect.fromCenter(center: Offset(x, y), width: width, height: height);
  Rect get upperBody => Rect.fromLTRB(x - width / 4, y - height / 2, x + width / 4, y);
  Rect get lowerBody => Rect.fromLTRB(x - width / 4, y, x + width / 4, y + height / 2);

  HeroPose get pose {
    if (jumping) return HeroPose.jump;
    if (stuck) return HeroPose.stick;
    if (crouching) return HeroPose.crouch;
    return HeroPose.run;
  }

  bool get fallen => upperBody.top > Config.viewHeight;

  void update() {
    if (y <= 0) {
      _stickToTop();
    } else {
      y += speedY;
      speedY += Config.heroGravity;
    }
    if (x <= Config.heroMinX) x = Config.heroMinX + 1;
  }

  void jump() {
    if (jumping) return;
    speedY = Config.heroJumpSpeed;
    jumping = true;
    crouching = false;
  }

  /// Swipe down: let go of the top of the screen, or crouch.
  void down() {
    if (stuck) {
      stuck = false;
      y += 1;
    } else {
      crouching = true;
    }
  }

  void nudgeRight() {
    if (!crouching) x += Config.heroNudge;
  }

  void landOn(double groundTop) {
    jumping = false;
    speedY = 0;
    y = groundTop - height / 2;
  }

  void _stickToTop() {
    stuck = true;
    jumping = false;
    y = 0;
    speedY = 0;
  }

  /// Same rule as the Android version: the upper body always counts, the lower
  /// body only when the enemy's centre is above and to the left of the hero's.
  bool isHitBy(Rect enemy) {
    if (!Config.enemiesHurtOnlyFromAbove) return enemy.overlaps(body);
    return enemy.overlaps(upperBody) ||
        (enemy.overlaps(lowerBody) && x >= enemy.center.dx && y >= enemy.center.dy);
  }
}

/// A bandit. Port of the Android `Enemy` class. World coordinates.
class Enemy {
  Enemy(this.x, this.y)
      : previousX = x,
        previousY = y;

  double x;
  double y;
  double previousX;
  double previousY;
  double speedY = 0;
  int health = Config.enemyHealth;
  bool hit = false; // shows the "dying" animation
  bool dead = false;
  bool touchedHero = false;

  static const double width = Config.enemyWidth;
  static const double height = Config.enemyHeight;

  Rect get rect => Rect.fromCenter(center: Offset(x, y), width: width, height: height);

  void update() {
    x -= Config.enemyRunSpeed;
    y += speedY;
    speedY += Config.enemyGravity;
  }

  void takeHit() {
    hit = true;
    if (health > 0) health--;
    if (health == 0) dead = true;
  }
}

/// The blue ring the hero throws. Screen coordinates.
class Projectile {
  Projectile(this.x, this.y) : previousX = x;

  double x;
  final double y;
  double previousX;
  bool visible = true;

  Rect get rect => Rect.fromLTWH(x, y, Config.projectileWidth, Config.projectileHeight);
}
