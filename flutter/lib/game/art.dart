import 'dart:convert';
import 'dart:ui';

import 'package:flame/cache.dart';
import 'package:flutter/services.dart';

import 'actors.dart';
import 'tick_animation.dart';

/// A hero animation frame, with where the head is (0..1 inside the frame).
class HeroFrame {
  const HeroFrame(this.image, this.head);
  final Image image;
  final Offset head;
}

/// Every image and animation of the game. Port of the Android `Assets` /
/// `LoadingScreen` setup, with the same frame timings.
class Art {
  Art._(this._images, this._anchors);

  final Images _images;
  final Map<String, Offset> _anchors;

  late final heroRun = TickAnimation<HeroFrame>()..addAll(_heroFrames([for (var i = 1; i <= 17; i++) 'running/panoramic$i.png']), 10);
  late final heroJump = TickAnimation<HeroFrame>()..addAll(_heroFrames([for (var i = 1; i <= 15; i++) 'jump/panoramicjump$i.png']), 10);
  late final heroStick = TickAnimation<HeroFrame>()
    ..addAll(_heroFrames([for (var i = 0; i <= 7; i++) 'stick/stick$i.png']), 50)
    ..addAll(_heroFrames([for (var i = 7; i >= 1; i--) 'stick/stick$i.png']), 50);
  late final heroCrouch = TickAnimation<HeroFrame>()..addAll(_heroFrames(['panoramicaccroupi.png']), 10);

  late final enemyAlive = TickAnimation<Image>()..addAll(_all([for (var i = 1; i <= 10; i++) 'bandit/small/bandit$i.png']), 20);
  late final enemyDying = TickAnimation<Image>()..addAll(_all([for (var i = 1; i <= 10; i++) 'bandit/small/banditMort$i.png']), 20);
  late final fire = TickAnimation<Image>()..addAll(_all([for (var i = 1; i <= 4; i++) 'fire/n$i.png']), 100);
  late final bubble = TickAnimation<Image>()..addAll(_all(['bubble/redbob1.png', 'bubble/redbob2.png']), 100);
  late final dust = TickAnimation<Image>()..addAll(_all([for (final i in [1, 2, 3, 4, 3, 2]) 'dust/dust$i.png']), 100);
  late final cover = TickAnimation<Image>()
    ..addAll(_all([for (final i in [1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2]) 'cover/cover$i.png']), 50);
  late final gameOverStars = TickAnimation<Image>()..addAll(_all([for (var i = 1; i <= 5; i++) 'gameOver/star$i.png']), 100);
  late final gameOverBackground = TickAnimation<Image>()..addAll(_all([for (var i = 1; i <= 3; i++) 'gameOver/gameOver$i.png']), 100);

  late final background = _all([for (var i = 0; i < backgroundTiles; i++) 'background/bg$i.png']);
  late final tileset = _get('level1Tileset.png');
  late final lives = _all([for (var i = 1; i <= 3; i++) 'lifePan/lifePan$i.png']);
  late final pause = _get('pause.png');
  late final redBob = _get('bubble/redbob1.png');
  late final menu = _get('menu.png');
  late final levelEndBack = _get('levelEnd/levelEnd1.png');
  late final levelEndFront = _get('levelEnd/levelEnd2.png');
  late final home = _get('levelEnd/levelEndHome.png');
  late final rope = _get('gameOver/gameOverRope.png');

  static const backgroundTiles = 8;

  static Future<Art> load(Images images) async {
    final anchors = (jsonDecode(await rootBundle.loadString('assets/hero/head_anchors.json')) as Map<String, dynamic>)
        .map((k, v) => MapEntry(k, Offset((v[0] as num).toDouble(), (v[1] as num).toDouble())));
    await images.loadAll([
      ...anchors.keys,
      for (var i = 1; i <= 10; i++) ...['bandit/small/bandit$i.png', 'bandit/small/banditMort$i.png'],
      for (var i = 1; i <= 4; i++) ...['fire/n$i.png', 'dust/dust$i.png'],
      for (var i = 1; i <= 8; i++) 'cover/cover$i.png',
      for (var i = 1; i <= 5; i++) 'gameOver/star$i.png',
      for (var i = 1; i <= 3; i++) ...['gameOver/gameOver$i.png', 'lifePan/lifePan$i.png'],
      for (var i = 0; i < backgroundTiles; i++) 'background/bg$i.png',
      'bubble/redbob1.png',
      'bubble/redbob2.png',
      'level1Tileset.png',
      'pause.png',
      'menu.png',
      'levelEnd/levelEnd1.png',
      'levelEnd/levelEnd2.png',
      'levelEnd/levelEndHome.png',
      'gameOver/gameOverRope.png',
    ]);
    return Art._(images, anchors);
  }

  TickAnimation<HeroFrame> heroAnimation(HeroPose pose) => switch (pose) {
        HeroPose.run => heroRun,
        HeroPose.jump => heroJump,
        HeroPose.stick => heroStick,
        HeroPose.crouch => heroCrouch,
      };

  /// Advances every in-game animation by one tick (Android `GameScreen.animate`).
  void tick() {
    heroRun.update(10);
    heroJump.update(10);
    heroStick.update(10);
    enemyAlive.update(50);
    enemyDying.update(50);
    bubble.update(10);
    dust.update(150);
    cover.update(50);
    fire.update(50);
  }

  void tickGameOver() {
    gameOverStars.update(15);
    gameOverBackground.update(15);
  }

  Image _get(String path) => _images.fromCache(path);
  List<Image> _all(List<String> paths) => [for (final p in paths) _get(p)];
  List<HeroFrame> _heroFrames(List<String> paths) => [for (final p in paths) HeroFrame(_get(p), _anchors[p]!)];
}
