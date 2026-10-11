import 'package:flame_audio/flame_audio.dart';

import 'session.dart';

/// Music and sound effects. Failures are ignored: the game must keep running
/// without sound (e.g. a browser that blocks audio).
class Sounds {
  AudioPool? _shoot;
  AudioPool? _hit;
  bool _musicStarted = false;

  Future<void> load() async {
    try {
      FlameAudio.bgm.initialize(); // pauses the music when the app goes to background
      _shoot = await FlameAudio.createPool('projectile.mp3', maxPlayers: 3);
      _hit = await FlameAudio.createPool('enemy.mp3', maxPlayers: 3);
    } catch (_) {}
  }

  /// Browsers only allow audio after a user gesture, so this is called on the
  /// first tap.
  void startMusic() {
    if (_musicStarted) return;
    _musicStarted = true;
    FlameAudio.bgm.play('chaka.mp3', volume: 0.5).catchError((_) {});
  }

  void play(GameEvent event) {
    final pool = switch (event) {
      GameEvent.shoot => _shoot,
      GameEvent.hit => _hit,
    };
    pool?.start(volume: 0.6).catchError((_) => () async {});
  }
}
