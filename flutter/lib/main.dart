import 'package:flame/flame.dart';
import 'package:flame/game.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

import 'game/chaka_zulu_game.dart';
import 'game/hero_pack.dart';

/// Hero pack baked into a build: `flutter build ... --dart-define=HERO=emma`.
const _heroFromBuild = String.fromEnvironment('HERO');

/// The personalization to use: the build setting, else `?hero=<id>` in the web
/// address. Null plays the original hero.
String? selectedHeroPack() {
  if (_heroFromBuild.isNotEmpty) return _heroFromBuild;
  final fromUrl = kIsWeb ? Uri.base.queryParameters['hero'] : null;
  return fromUrl != null && HeroPack.isValidId(fromUrl) ? fromUrl : null;
}

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  if (!kIsWeb) {
    await Flame.device.fullScreen();
    await Flame.device.setLandscape();
  }
  runApp(
    MaterialApp(
      title: 'Chaka Zulu',
      debugShowCheckedModeBanner: false,
      home: GameWidget(
        game: ChakaZuluGame(heroPackId: selectedHeroPack()),
        loadingBuilder: (_) => const _Splash(),
      ),
    ),
  );
}

/// Haiz Studio splash, shown while the game loads (as in the Android version).
class _Splash extends StatelessWidget {
  const _Splash();

  @override
  Widget build(BuildContext context) => const ColoredBox(
        color: Color(0xFFFA7600),
        child: Center(child: Image(image: AssetImage('assets/images/HaizStudioSplash.png'))),
      );
}
