import 'dart:convert';
import 'dart:io';
import 'dart:ui';

import 'package:chaka_zulu/game/actors.dart';
import 'package:chaka_zulu/game/config.dart';
import 'package:chaka_zulu/game/hero_pack.dart';
import 'package:chaka_zulu/game/level.dart';
import 'package:chaka_zulu/game/session.dart';
import 'package:chaka_zulu/game/tick_animation.dart';
import 'package:flutter_test/flutter_test.dart';

Level loadLevel() => Level.parse(File('assets/levels/level1.csv').readAsStringSync());

const groundTop = 18 * Config.tileSize; // first ground row of the level

/// Runs ticks until [done] or [max] ticks.
int runUntil(GameSession s, bool Function() done, {int max = 4000}) {
  var n = 0;
  while (!done() && n < max) {
    s.step();
    n++;
  }
  return n;
}

void main() {
  group('Level', () {
    test('parses the original 800 x 20 map', () {
      final level = loadLevel();
      expect(level.columns, 800);
      expect(level.rows, 20);
      expect(level.kindAt(0, 18), TileKind.ground);
      expect(level.kindAt(0, 19), TileKind.wall);
      expect(level.kindAt(0, 0), TileKind.none);
    });

    test('tile kinds match the Android Tile class', () {
      expect(tileKindOf(0), TileKind.none);
      expect(tileKindOf(2), TileKind.ground);
      expect(tileKindOf(4), TileKind.water);
      expect(tileKindOf(50), TileKind.ground);
      expect(tileKindOf(65), TileKind.wall);
      expect(tileKindOf(84), TileKind.bubble);
      expect(tileKindOf(85), TileKind.decor);
      expect(tileKindOf(90), TileKind.none);
    });

    test('collision queries', () {
      final level = loadLevel();
      final ground = level.groundTouching(const Rect.fromLTWH(10, groundTop - 10, 20, 20));
      expect(ground?.top, groundTop);
      expect(level.groundTouching(const Rect.fromLTWH(10, 10, 20, 20)), isNull);
    });
  });

  group('Hero', () {
    test('falls and lands on the ground', () {
      final s = GameSession(loadLevel(), viewWidth: 960);
      runUntil(s, () => s.hero.onGround, max: 200);
      expect(s.hero.onGround, isTrue);
      expect(s.hero.y, groundTop - Panoramic.height / 2);
      expect(s.hero.jumping, isFalse);
    });

    test('jumps about 340 px high and lands again', () {
      final s = GameSession(loadLevel(), viewWidth: 960);
      runUntil(s, () => s.hero.onGround, max: 200);
      final start = s.hero.y;
      s.step(swipe: Swipe.up);
      var top = start;
      for (var i = 0; i < 60 && (s.hero.jumping || i == 0); i++) {
        s.step();
        if (s.hero.y < top) top = s.hero.y;
      }
      expect(start - top, closeTo(341, 20));
      expect(s.hero.jumping, isFalse);
    });

    test('swipe down crouches, swipe up stands up', () {
      final s = GameSession(loadLevel(), viewWidth: 960);
      runUntil(s, () => s.hero.onGround, max: 200);
      s.step(swipe: Swipe.down);
      expect(s.hero.pose, HeroPose.crouch);
      s.step(swipe: Swipe.up);
      expect(s.hero.pose, HeroPose.jump);
    });
  });

  group('Session', () {
    test('a wall stops the scrolling until the hero jumps over it', () {
      final s = GameSession(loadLevel(), viewWidth: 960)..enemies.clear();
      var previous = -1.0;
      // The first wall (column 33) is 10 tiles high: scrolling must stop.
      runUntil(s, () {
        final stopped = s.scrollX == previous;
        previous = s.scrollX;
        return stopped && s.hero.onGround;
      });
      expect(s.scrollX, greaterThan(500));
      expect(s.scrollX, lessThan(33 * Config.tileSize));
      expect(s.outcome, Outcome.playing);
    });

    test('shooting an enemy twice kills it', () {
      final s = GameSession(loadLevel(), viewWidth: 960);
      runUntil(s, () => s.hero.onGround, max: 200);
      final target = s.enemies.first;
      for (var i = 0; i < 40 && !target.dead; i++) {
        s.step(shoot: i % 4 == 0);
      }
      expect(target.dead, isTrue);
      expect(s.enemies, isNot(contains(target)));
    });

    test('three enemy hits end the game', () {
      final s = GameSession(loadLevel(), viewWidth: 960)..enemies.clear();
      runUntil(s, () => s.hero.onGround, max: 200);
      for (var hit = 1; hit <= 3; hit++) {
        // A bandit dropping on the hero's head.
        s.enemies.add(Enemy(s.hero.x + s.scrollX, s.hero.y - 40));
        s.step();
        expect(s.lives, 3 - hit);
        s.enemies.clear();
      }
      expect(s.outcome, Outcome.gameOver);
    });

    test('bandits on the same ground pass through the hero (Android behaviour)', () {
      final s = GameSession(loadLevel(), viewWidth: 960);
      runUntil(s, () => s.enemies.first.x - s.scrollX < s.hero.x - 60, max: 200);
      expect(s.lives, Config.enemiesHurtOnlyFromAbove ? 3 : lessThan(3));
    });

    test('red bobs are collected and counted', () {
      final level = loadLevel();
      final s = GameSession(level, viewWidth: 960)..enemies.clear();
      runUntil(s, () => s.hero.onGround, max: 200);
      // Put a red bob just in front of the hero.
      final column = ((s.hero.x + s.scrollX) / Config.tileSize).floor() + 2;
      final row = (s.hero.y / Config.tileSize).floor();
      final csv = File('assets/levels/level1.csv').readAsLinesSync();
      final cells = csv[row].split(',')..[column] = '84';
      csv[row] = cells.join(',');
      final s2 = GameSession(Level.parse(csv.join('\n')), viewWidth: 960)..enemies.clear();
      runUntil(s2, () => s2.score > 0, max: 300);
      expect(s2.score, 1);
      expect(s2.shielded, isTrue);
    });

    test('a wider screen shows more of the level but the level still ends', () {
      final s = GameSession(loadLevel(), viewWidth: 1400)..enemies.clear();
      s.scrollX = s.level.width - 1400 - 8;
      s.hero.y = groundTop - Panoramic.height / 2;
      runUntil(s, () => s.outcome != Outcome.playing, max: 600);
      expect(s.reachedEnd, isTrue);
      expect(s.scrollX, s.level.width - 1400);
      expect(s.outcome, Outcome.levelComplete);
    });
  });

  test('TickAnimation follows the Android timing', () {
    final a = TickAnimation<int>()..addAll([0, 1, 2], 10);
    expect(a.current, 0);
    a.update(10);
    expect(a.current, 0);
    a.update(10);
    expect(a.current, 1);
    a.update(20);
    expect(a.current, 0); // looped
  });

  test('head anchors exist for every hero frame', () {
    final anchors = jsonDecode(File('assets/hero/head_anchors.json').readAsStringSync()) as Map<String, dynamic>;
    expect(anchors.length, 42);
    for (final v in anchors.values) {
      final xy = (v as List).cast<num>();
      expect(xy[0], inInclusiveRange(0, 1));
      expect(xy[1], inInclusiveRange(0, 1));
    }
  });

  test('hero pack ids cannot escape their folder', () {
    expect(HeroPack.isValidId('demo'), isTrue);
    expect(HeroPack.isValidId('emma-2026'), isTrue);
    expect(HeroPack.isValidId('../secret'), isFalse);
    expect(HeroPack.isValidId('Emma'), isFalse);
  });
}
