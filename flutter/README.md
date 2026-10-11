# Chaka Zulu — Flutter + Flame

Android, iOS and web version of the game. See the [main README](../README.md)
for how to run it, the controls and how to make personalized versions.

Code map (`lib/game/`):

| File | Role | Android original |
|---|---|---|
| `config.dart` | speeds, sizes, timings | constants in `Assets`, `Actor`, `Enemy` |
| `level.dart` | tile map and collisions | `Level`, `Tile` |
| `actors.dart` | hero, bandits, rings | `Actor`, `Enemy`, `Projectile` |
| `session.dart` | game rules, one tick at a time | `GameScreen.update*` |
| `tick_animation.dart` | frame animations | `Animation` |
| `art.dart` | images and animations | `Assets`, `LoadingScreen` |
| `chaka_zulu_game.dart` | screens, input, drawing | `GameScreen.paint*`, `MainMenuScreen`, `MultiTouchHandler` |
| `hero_pack.dart` | personalization | (new) |
| `sounds.dart` | music and sound effects | `Assets.load`, `AudioAndroid` |
