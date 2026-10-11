import 'dart:math' as math;
import 'dart:ui';

import 'package:flame/events.dart';
import 'package:flutter/foundation.dart' show kIsWeb;
import 'package:flame/game.dart';
import 'package:flutter/painting.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart' show AppLifecycleState, KeyEventResult;

import 'actors.dart';
import 'art.dart';
import 'config.dart';
import 'hero_pack.dart';
import 'level.dart';
import 'session.dart';
import 'sounds.dart';

enum Screen { menu, playing, paused, gameOver, levelComplete }

/// The game: screens, input and drawing. The rules live in [GameSession].
///
/// Everything is drawn in virtual pixels: 640 high, and as wide as the screen
/// shape allows (at least 960), then scaled to the real screen.
class ChakaZuluGame extends FlameGame with TapCallbacks, DragCallbacks, KeyboardEvents {
  ChakaZuluGame({this.heroPackId});

  final String? heroPackId;

  late final Art art;
  late final String _levelCsv;
  final sounds = Sounds();
  HeroPack? heroPack;

  Screen screen = Screen.menu;
  GameSession? session;

  // Virtual screen.
  double viewWidth = Config.designWidth;
  double _scale = 1;
  Offset _origin = Offset.zero;

  // Fixed-step loop.
  double _accumulator = 0;
  double _alpha = 0;
  double _screenTime = 0; // seconds since the current screen was shown

  // Input collected between ticks.
  Swipe? _pendingSwipe;
  bool _pendingShoot = false;
  Vector2 _dragDistance = Vector2.zero();

  final _paint = Paint()..filterQuality = FilterQuality.low;
  final _textCache = <String, TextPainter>{};

  @override
  Color backgroundColor() => const Color(0xFF000000);

  @override
  Future<void> onLoad() async {
    art = await Art.load(images);
    _levelCsv = await rootBundle.loadString('assets/levels/level1.csv');
    if (heroPackId != null) heroPack = await HeroPack.load(heroPackId!);
    await sounds.load();
  }

  // ---------------------------------------------------------------- layout

  @override
  void onGameResize(Vector2 size) {
    super.onGameResize(size);
    final aspect = size.x / size.y;
    if (aspect >= Config.designWidth / Config.viewHeight) {
      viewWidth = Config.viewHeight * aspect;
      _scale = size.y / Config.viewHeight;
      _origin = Offset.zero;
    } else {
      viewWidth = Config.designWidth;
      _scale = size.x / Config.designWidth;
      _origin = Offset(0, (size.y - Config.viewHeight * _scale) / 2);
    }
    session?.viewWidth = viewWidth;
  }

  Offset _toVirtual(Vector2 p) => Offset((p.x - _origin.dx) / _scale, (p.y - _origin.dy) / _scale);

  bool get _portrait => size.y > size.x;

  // ------------------------------------------------------------- screens

  void _show(Screen next) {
    screen = next;
    _screenTime = 0;
  }

  void _startGame() {
    sounds.startMusic();
    session = GameSession(Level.parse(_levelCsv), viewWidth: viewWidth);
    for (final a in [art.heroRun, art.heroJump, art.heroStick]) {
      a.reset();
    }
    _accumulator = 0;
    _pendingSwipe = null;
    _pendingShoot = false;
    _show(Screen.playing);
  }

  Rect get _pauseButton => Rect.fromLTWH(viewWidth - 64, 32, 32, 32);
  Rect get _continueButton => Rect.fromCenter(center: Offset(viewWidth / 2, 250), width: 320, height: 90);
  Rect get _menuButton => Rect.fromCenter(center: Offset(viewWidth / 2, 400), width: 320, height: 90);

  // ---------------------------------------------------------------- input

  @override
  void onTapUp(TapUpEvent event) {
    final p = _toVirtual(event.canvasPosition);
    switch (screen) {
      case Screen.menu:
        if (!_portrait) _startGame();
      case Screen.playing:
        if (_pauseButton.inflate(16).contains(p)) {
          _show(Screen.paused);
        } else {
          _pendingShoot = true;
        }
      case Screen.paused:
        if (_continueButton.contains(p)) _show(Screen.playing);
        if (_menuButton.contains(p)) _show(Screen.menu);
      case Screen.gameOver || Screen.levelComplete:
        if (_screenTime > 0.8) _show(Screen.menu); // avoid skipping by accident
    }
  }

  @override
  void onDragStart(DragStartEvent event) {
    super.onDragStart(event);
    _dragDistance = Vector2.zero();
  }

  @override
  void onDragUpdate(DragUpdateEvent event) {
    _dragDistance += event.localDelta;
  }

  @override
  void onDragEnd(DragEndEvent event) {
    super.onDragEnd(event);
    if (screen != Screen.playing) return;
    final d = _dragDistance.length >= 20 ? _dragDistance : event.velocity;
    if (d.length < 20) return;
    // Same angles as the Android MultiTouchHandler: up is 45..135 degrees.
    final angle = (math.atan2(-d.y, d.x) * 180 / math.pi + 360) % 360;
    _pendingSwipe = angle >= 45 && angle < 135
        ? Swipe.up
        : angle >= 225 && angle < 315
            ? Swipe.down
            : angle >= 135 && angle < 225
                ? Swipe.left
                : Swipe.right;
  }

  @override
  KeyEventResult onKeyEvent(KeyEvent event, Set<LogicalKeyboardKey> keysPressed) {
    if (event is! KeyDownEvent) return KeyEventResult.ignored;
    final key = event.logicalKey;
    if (screen == Screen.playing) {
      if (key == LogicalKeyboardKey.arrowUp || key == LogicalKeyboardKey.keyW) _pendingSwipe = Swipe.up;
      if (key == LogicalKeyboardKey.arrowDown || key == LogicalKeyboardKey.keyS) _pendingSwipe = Swipe.down;
      if (key == LogicalKeyboardKey.arrowRight || key == LogicalKeyboardKey.keyD) _pendingSwipe = Swipe.right;
      if (key == LogicalKeyboardKey.space) _pendingShoot = true;
      if (key == LogicalKeyboardKey.escape || key == LogicalKeyboardKey.keyP) _show(Screen.paused);
      return KeyEventResult.handled;
    }
    if (key == LogicalKeyboardKey.enter || key == LogicalKeyboardKey.space) {
      switch (screen) {
        case Screen.menu:
          _startGame();
        case Screen.paused:
          _show(Screen.playing);
        case Screen.gameOver || Screen.levelComplete:
          if (_screenTime > 0.8) _show(Screen.menu);
        case Screen.playing:
          break;
      }
      return KeyEventResult.handled;
    }
    return KeyEventResult.ignored;
  }

  @override
  void lifecycleStateChange(AppLifecycleState state) {
    super.lifecycleStateChange(state);
    // On the web "inactive" only means the window lost focus: keep playing until the tab is hidden.
    final away = state == AppLifecycleState.paused ||
        state == AppLifecycleState.hidden ||
        (!kIsWeb && state == AppLifecycleState.inactive);
    if (away && screen == Screen.playing) _show(Screen.paused);
  }

  // --------------------------------------------------------------- update

  @override
  void update(double dt) {
    super.update(dt);
    _screenTime += dt;
    _accumulator += dt;
    var ticks = 0;
    while (_accumulator >= Config.tickSeconds) {
      _accumulator -= Config.tickSeconds;
      if (ticks++ < Config.maxTicksPerFrame) _tick();
    }
    _alpha = _accumulator / Config.tickSeconds;
  }

  void _tick() {
    final s = session;
    switch (screen) {
      case Screen.playing when s != null:
        s.step(swipe: _pendingSwipe, shoot: _pendingShoot);
        _pendingSwipe = null;
        _pendingShoot = false;
        s.events.forEach(sounds.play);
        art.tick();
        if (s.outcome == Outcome.gameOver) _show(Screen.gameOver);
        if (s.outcome == Outcome.levelComplete) _show(Screen.levelComplete);
      case Screen.gameOver || Screen.levelComplete:
        art.tickGameOver();
      default:
        break;
    }
  }

  // --------------------------------------------------------------- render

  @override
  void render(Canvas canvas) {
    super.render(canvas);
    if (_portrait) {
      _drawRotateHint(canvas);
      return;
    }
    canvas
      ..save()
      ..translate(_origin.dx, _origin.dy)
      ..scale(_scale)
      ..clipRect(Rect.fromLTWH(0, 0, viewWidth, Config.viewHeight));
    final s = session;
    switch (screen) {
      case Screen.menu:
        _drawMenu(canvas);
      case Screen.playing || Screen.paused when s != null:
        _drawLevel(canvas, s);
        if (screen == Screen.paused) _drawPause(canvas);
      case Screen.gameOver:
        _drawGameOver(canvas);
      case Screen.levelComplete when s != null:
        _drawLevel(canvas, s);
        _drawLevelComplete(canvas, s);
      default:
        break;
    }
    canvas.restore();
  }

  double _lerp(double a, double b) => a + (b - a) * (screen == Screen.playing ? _alpha : 1);

  void _drawImage(Canvas canvas, Image image, Rect dst) => canvas.drawImageRect(
      image, Rect.fromLTWH(0, 0, image.width.toDouble(), image.height.toDouble()), dst, _paint);

  /// Draws [image] to fill the screen without distortion (cropping if needed).
  void _drawCover(Canvas canvas, Image image) {
    final s = math.max(viewWidth / image.width, Config.viewHeight / image.height);
    final w = image.width * s, h = image.height * s;
    _drawImage(canvas, image, Rect.fromLTWH((viewWidth - w) / 2, (Config.viewHeight - h) / 2, w, h));
  }

  void _drawLevel(Canvas canvas, GameSession s) {
    final scroll = _lerp(s.previousScrollX, s.scrollX);
    _drawBackground(canvas, _lerp(s.previousBackgroundX, s.backgroundX));

    for (final p in s.projectiles) {
      final f = art.fire.current;
      _drawImage(canvas, f, Rect.fromLTWH(_lerp(p.previousX, p.x), p.y, Config.fireSize, Config.fireSize));
    }

    _drawTiles(canvas, s.level, scroll);

    final end = s.levelEndX - scroll;
    _drawImage(canvas, art.home, Rect.fromLTWH(end + Config.designWidth / 20, Config.viewHeight * 2 / 3, Config.viewHeight / 3, Config.viewHeight / 3));
    final endRect = Rect.fromLTWH(end, 0, Config.designWidth, Config.viewHeight);
    _drawImage(canvas, art.levelEndBack, endRect);
    _drawHero(canvas, s);
    _drawImage(canvas, art.levelEndFront, endRect);

    for (final e in s.enemies) {
      final x = _lerp(e.previousX, e.x) - scroll, y = _lerp(e.previousY, e.y);
      final dst = Rect.fromCenter(center: Offset(x, y), width: Enemy.width, height: Enemy.height);
      if (dst.right < 0 || dst.left > viewWidth) continue;
      _drawImage(canvas, e.hit ? art.enemyDying.current : art.enemyAlive.current, dst);
    }

    _drawHud(canvas, s);
  }

  void _drawBackground(Canvas canvas, double x) {
    final scale = Config.viewHeight / art.background.first.height;
    var left = x;
    for (final tile in art.background) {
      final w = tile.width * scale;
      if (left + w > 0 && left < viewWidth) _drawImage(canvas, tile, Rect.fromLTWH(left, 0, w + 1, Config.viewHeight));
      left += w;
    }
  }

  void _drawTiles(Canvas canvas, Level level, double scroll) {
    const t = Config.tileSize;
    final first = (scroll / t).floor().clamp(0, level.columns - 1);
    final last = ((scroll + viewWidth) / t).ceil().clamp(0, level.columns - 1);
    final transforms = <RSTransform>[];
    final sources = <Rect>[];
    final bubbles = <Offset>[];
    for (var c = first; c <= last; c++) {
      for (var r = 0; r < level.rows; r++) {
        final id = level.idAt(c, r);
        final kind = tileKindOf(id);
        if (kind == TileKind.none) continue;
        final pos = Offset(c * t - scroll, r * t);
        if (kind == TileKind.bubble) {
          bubbles.add(pos);
          continue;
        }
        final col = id % Config.tilesetColumns, row = id ~/ Config.tilesetColumns;
        // Source inset by 0.5 px and destination grown by 2% to avoid seams when scaled.
        sources.add(Rect.fromLTWH(col * t + 0.5, row * t + 0.5, t - 1, t - 1));
        transforms.add(RSTransform.fromComponents(
            rotation: 0, scale: (t * 1.02) / (t - 1), anchorX: 0, anchorY: 0, translateX: pos.dx, translateY: pos.dy));
      }
    }
    canvas.drawAtlas(art.tileset, transforms, sources, null, null, null, _paint);
    for (final b in bubbles) {
      _drawImage(canvas, art.bubble.current, Rect.fromLTWH(b.dx, b.dy, t, t));
    }
  }

  void _drawHero(Canvas canvas, GameSession s) {
    final hero = s.hero;
    final frame = art.heroAnimation(hero.pose).current;
    final x = _lerp(hero.previousX, hero.x), y = _lerp(hero.previousY, hero.y);
    // Keep the sprite's proportions (the Android version stretched it).
    final h = Panoramic.height;
    final w = h * frame.image.width / frame.image.height;
    final dst = Rect.fromLTWH(x - w / 2, y - h / 2 + Config.heroFeetShading, w, h);
    _drawImage(canvas, frame.image, dst);
    if (hero.onGround) {
      _drawImage(canvas, art.dust.current, Rect.fromLTWH(x - Panoramic.width / 2, y, Panoramic.width, h));
    }
    if (s.shielded) _drawImage(canvas, art.cover.current, dst);

    // The child's face, on top of everything so it is always visible.
    final face = heroPack?.face;
    if (face != null) {
      final center = Offset(dst.left + frame.head.dx * w, dst.top + (frame.head.dy + 0.015) * h);
      _drawFace(canvas, face, center, Config.faceDiameter);
    }
    if (hero.stuck) {
      canvas.drawRect(Rect.fromLTWH(x - Panoramic.width / 2, y, Panoramic.width, h / 10), Paint()..color = const Color(0xFFFFEB3B));
    }
  }

  void _drawFace(Canvas canvas, Image face, Offset center, double diameter) {
    final r = Rect.fromCircle(center: center, radius: diameter / 2);
    canvas
      ..save()
      ..clipPath(Path()..addOval(r));
    _drawImage(canvas, face, r);
    canvas
      ..restore()
      ..drawCircle(
          center,
          diameter / 2,
          Paint()
            ..style = PaintingStyle.stroke
            ..strokeWidth = diameter / 18
            ..color = const Color(0xFF3E2723));
  }

  void _drawHud(Canvas canvas, GameSession s) {
    // Shoot hint (tap anywhere shoots, like the Android version).
    final shoot = Offset(viewWidth * 7 / 8, Config.viewHeight * 3 / 4);
    canvas
      ..drawCircle(shoot, 30, Paint()..color = const Color(0x96FFFFFF))
      ..drawCircle(
          shoot,
          40,
          Paint()
            ..style = PaintingStyle.stroke
            ..strokeWidth = 2
            ..color = const Color(0x96FFFFFF));
    _drawImage(canvas, art.pause, _pauseButton);

    final bob = Rect.fromLTWH(viewWidth - 128, 32, 32, 32);
    _drawImage(canvas, art.redBob, bob);
    _drawText(canvas, '${s.score} x ', Offset(bob.left - 4, bob.center.dy),
        size: 22, color: const Color(0xFFFFFFFF), bold: true, align: Alignment.centerRight, shadow: true);

    _drawImage(canvas, art.lives[(s.lives - 1).clamp(0, 2)], Rect.fromLTWH(Config.designWidth / 15, Config.viewHeight / 15, 92, 32));
  }

  void _drawMenu(Canvas canvas) {
    // Blurred-looking fill for wide screens, then the menu art at its own proportions.
    _drawCover(canvas, art.menu);
    canvas.drawRect(Rect.fromLTWH(0, 0, viewWidth, Config.viewHeight), Paint()..color = const Color(0x66000000));
    final menu = Rect.fromLTWH((viewWidth - Config.designWidth) / 2, 0, Config.designWidth, Config.viewHeight);
    _drawImage(canvas, art.menu, menu);

    final pack = heroPack;
    if (pack != null) {
      final cx = menu.center.dx;
      if (pack.face != null) _drawFace(canvas, pack.face!, Offset(cx, 368), 96);
      _drawText(canvas, 'Starring', Offset(cx, 178), size: 22, color: const Color(0xFF5D4037));
      _drawText(canvas, pack.name.toUpperCase(), Offset(cx, 212), size: 36, color: const Color(0xFF3E2723), bold: true);
    }
  }

  void _drawPause(Canvas canvas) {
    canvas.drawRect(Rect.fromLTWH(0, 0, viewWidth, Config.viewHeight), Paint()..color = const Color(0x9B000000));
    for (final (rect, label) in [(_continueButton, 'Continue'), (_menuButton, 'Menu')]) {
      canvas.drawRRect(RRect.fromRectAndRadius(rect, const Radius.circular(20)), Paint()..color = const Color(0xFFF5E6C8));
      _drawText(canvas, label, rect.center, size: 40, color: const Color(0xFF3E2723), bold: true);
    }
  }

  void _drawGameOver(Canvas canvas) {
    _drawCover(canvas, art.gameOverBackground.current);
    final star = art.gameOverStars.current;
    final w = viewWidth, h = Config.viewHeight;
    for (final p in [Offset(w / 10, h / 10), Offset(8 * w / 10, 0), Offset(w / 10, 8 * h / 10), Offset(7 * w / 10, 9 * h / 10)]) {
      _drawImage(canvas, star, Rect.fromLTWH(p.dx, p.dy, 80, 77));
    }
    _drawImage(canvas, art.rope, Rect.fromLTWH(w - 250 - w / 20, 0, 250, 500));
    _drawText(canvas, 'Tap to continue', Offset(w - 125 - w / 20, 470), size: 22, color: const Color(0xFF3E2723), bold: true);
  }

  void _drawLevelComplete(Canvas canvas, GameSession s) {
    canvas.drawRect(Rect.fromLTWH(0, 0, viewWidth, Config.viewHeight), Paint()..color = const Color(0x99000000));
    final cx = viewWidth / 2;
    final panel = Rect.fromCenter(center: Offset(cx, 340), width: 640, height: 420);
    canvas
      ..drawRRect(RRect.fromRectAndRadius(panel, const Radius.circular(28)), Paint()..color = const Color(0xFFF5E6C8))
      ..drawRRect(
          RRect.fromRectAndRadius(panel, const Radius.circular(28)),
          Paint()
            ..style = PaintingStyle.stroke
            ..strokeWidth = 6
            ..color = const Color(0xFF8D6E63));
    final star = art.gameOverStars.current;
    _drawImage(canvas, star, Rect.fromLTWH(panel.left - 30, panel.top - 30, 80, 77));
    _drawImage(canvas, star, Rect.fromLTWH(panel.right - 50, panel.top - 30, 80, 77));

    final pack = heroPack;
    final face = pack?.face;
    if (face != null) _drawFace(canvas, face, Offset(cx, panel.top + 20), 150);
    _drawText(canvas, pack?.message ?? 'Well done!', Offset(cx, panel.top + 160),
        size: 40, color: const Color(0xFF3E2723), bold: true, maxWidth: panel.width - 60);
    _drawText(canvas, 'Red bobs collected: ${s.score}', Offset(cx, panel.top + 250), size: 26, color: const Color(0xFF5D4037));
    _drawText(canvas, 'Tap to play again', Offset(cx, panel.bottom - 50), size: 24, color: const Color(0xFF8D6E63), bold: true);
  }

  void _drawRotateHint(Canvas canvas) {
    _drawText(canvas, 'Turn your phone sideways to play', Offset(size.x / 2, size.y / 2),
        size: 20, color: const Color(0xFFFFFFFF), bold: true, maxWidth: size.x - 40);
  }

  void _drawText(Canvas canvas, String text, Offset at,
      {required double size,
      required Color color,
      bool bold = false,
      bool shadow = false,
      Alignment align = Alignment.center,
      double? maxWidth}) {
    final key = '$text|$size|${color.toARGB32()}|$bold|$shadow|$maxWidth';
    final painter = _textCache.putIfAbsent(key, () {
      if (_textCache.length > 64) _textCache.clear();
      return TextPainter(
        text: TextSpan(
          text: text,
          style: TextStyle(
            fontSize: size,
            color: color,
            fontWeight: bold ? FontWeight.bold : FontWeight.normal,
            shadows: shadow ? const [Shadow(blurRadius: 3, color: Color(0xCC000000))] : null,
          ),
        ),
        textAlign: TextAlign.center,
        textDirection: TextDirection.ltr,
      )..layout(maxWidth: maxWidth ?? double.infinity);
    });
    final offset = Offset(at.dx - painter.width * (align.x + 1) / 2, at.dy - painter.height * (align.y + 1) / 2);
    painter.paint(canvas, offset);
  }
}
