import 'dart:ui';

import 'actors.dart';
import 'config.dart';
import 'level.dart';

enum Swipe { up, down, left, right }

enum Outcome { playing, gameOver, levelComplete }

enum GameEvent { shoot, hit }

/// One play-through of the level: all the game rules, no drawing.
/// Port of the update part of the Android `GameScreen`.
///
/// Call [step] 40 times per second.
class GameSession {
  GameSession(this.level, {required this.viewWidth}) {
    final spacing = level.width / Config.enemyCount;
    for (var i = 1; i <= Config.enemyCount; i++) {
      enemies.add(Enemy(i * spacing, Config.enemySpawnY));
    }
  }

  final Level level;

  /// Visible width in virtual pixels (the height is always [Config.viewHeight]).
  double viewWidth;

  final hero = Panoramic();
  final enemies = <Enemy>[];
  final projectiles = <Projectile>[];

  /// How far the level has scrolled (world x of the left edge of the screen).
  double scrollX = 0;
  double previousScrollX = 0;
  double backgroundX = 0;
  double previousBackgroundX = 0;

  int score = 0; // red bobs collected
  int lives = Config.lives;
  bool reachedEnd = false;
  Outcome outcome = Outcome.playing;

  /// Sounds to play for the last step.
  final events = <GameEvent>[];

  int _ticksSinceBubble = Config.shieldTicks;
  double _backgroundSpeed = 0;

  bool get shielded => _ticksSinceBubble < Config.shieldTicks;

  /// Where the level-end decoration (hollow tree) starts, in world x.
  double get levelEndX => level.width - Config.designWidth;

  Rect get _screen => Rect.fromLTWH(0, 0, viewWidth, Config.viewHeight);
  Offset get _toWorld => Offset(scrollX, 0);

  void step({Swipe? swipe, bool shoot = false}) {
    events.clear();
    if (outcome != Outcome.playing) return;
    _rememberPositions();

    switch (swipe) {
      case Swipe.up:
        hero.jump();
      case Swipe.down:
        hero.down();
      case Swipe.right:
        hero.nudgeRight();
      case Swipe.left || null:
        break;
    }
    if (shoot) {
      projectiles.add(Projectile(hero.x + Config.projectileSpawnOffsetX, hero.y));
      events.add(GameEvent.shoot);
    }

    _updateProjectiles();
    hero.update();
    if (hero.fallen) {
      outcome = Outcome.gameOver;
      return;
    }
    backgroundX -= _backgroundSpeed;
    _updateEnemies();
    if (outcome != Outcome.playing) return;
    _checkCollisions();
  }

  void _rememberPositions() {
    previousScrollX = scrollX;
    previousBackgroundX = backgroundX;
    hero
      ..previousX = hero.x
      ..previousY = hero.y;
    for (final e in enemies) {
      e
        ..previousX = e.x
        ..previousY = e.y;
    }
    for (final p in projectiles) {
      p.previousX = p.x;
    }
  }

  void _updateProjectiles() {
    for (final p in projectiles) {
      p.x += Config.projectileSpeed;
      if (p.x > viewWidth) {
        p.visible = false;
        continue;
      }
      for (final e in enemies) {
        if (!e.dead && p.rect.overlaps(e.rect.shift(-_toWorld))) {
          p.visible = false;
          e.takeHit();
          events.add(GameEvent.hit);
        }
      }
    }
    projectiles.removeWhere((p) => !p.visible);
  }

  void _updateEnemies() {
    for (final e in enemies) {
      if (e.rect.shift(-_toWorld).right < 0) e.dead = true; // left behind
      if (e.dead || !e.rect.shift(-_toWorld).overlaps(_screen)) continue; // waits off screen

      final ground = level.groundTouching(e.rect);
      if (ground != null) {
        e.speedY = 0;
        e.y = ground.top - Enemy.height / 2;
      }
      final wall = level.wallTouching(e.rect);
      if (wall != null) {
        e.x = wall.right + Enemy.width;
        e.speedY = Config.enemyJumpSpeed;
      }

      final onScreen = e.rect.shift(-_toWorld);
      if (onScreen.right < 0 || onScreen.top > Config.viewHeight) {
        e.dead = true;
        continue;
      }
      e.update();

      if (!e.touchedHero && hero.isHitBy(e.rect.shift(-_toWorld))) {
        e.touchedHero = true;
        events.add(GameEvent.hit);
        lives--;
        if (lives == 0) outcome = Outcome.gameOver;
      }
    }
    enemies.removeWhere((e) => e.dead);
  }

  void _checkCollisions() {
    // Ground. The probe is 1 px taller than the body so a hero standing still
    // keeps touching the ground (the Android version flickered here).
    final body = hero.body.shift(_toWorld);
    final ground = level.groundTouching(Rect.fromLTRB(body.left, body.top, body.right, body.bottom + 1));
    hero.onGround = ground != null;
    if (ground != null) hero.landOn(ground.top);

    // Walls stop the scrolling; the end of the level too, then the hero walks
    // into the hollow tree.
    final wall = level.wallTouching(hero.upperBody.shift(_toWorld)) ??
        level.wallTouching(hero.lowerBody.shift(_toWorld));
    final maxScroll = level.width - viewWidth;
    if (wall != null) {
      _backgroundSpeed = 0;
    } else if (scrollX >= maxScroll) {
      _backgroundSpeed = 0;
      reachedEnd = true;
      hero.nudgeRight();
      if (hero.x + scrollX >= levelEndX + Config.designWidth * 0.62) outcome = Outcome.levelComplete;
    } else {
      _backgroundSpeed = Config.backgroundSpeed;
      scrollX = (scrollX + Config.levelSpeed).clamp(0, maxScroll);
    }

    // Red bobs.
    final bubble = level.bubbleTouching(hero.body.shift(_toWorld));
    if (bubble != null) {
      level.clear(bubble);
      score++;
      _ticksSinceBubble = 0;
    } else {
      _ticksSinceBubble++;
    }

    // Rings stop at walls.
    projectiles.removeWhere((p) => level.wallTouching(p.rect.shift(_toWorld)) != null);
  }
}
