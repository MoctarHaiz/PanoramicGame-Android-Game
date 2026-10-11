import 'dart:convert';
import 'dart:ui';

import 'package:flame/cache.dart';
import 'package:flutter/services.dart';

/// A personalization: the child's face, name and message.
///
/// Packs live in `assets/hero_packs/<id>/` (`hero.json` + `face.png`) and are
/// made with `tool/make_hero_pack.py`.
class HeroPack {
  const HeroPack({required this.name, required this.message, this.age, this.face});

  final String name;
  final String message;
  final int? age;
  final Image? face;

  static final _validId = RegExp(r'^[a-z0-9_-]{1,40}$');

  static bool isValidId(String id) => _validId.hasMatch(id);

  /// Loads pack [id], or returns null if it is missing or broken (the game
  /// then shows the original hero).
  static Future<HeroPack?> load(String id) async {
    if (!isValidId(id)) return null;
    try {
      final json = jsonDecode(await rootBundle.loadString('assets/hero_packs/$id/hero.json')) as Map<String, dynamic>;
      final faceFile = json['face'] as String?;
      final face = faceFile == null ? null : await Images(prefix: 'assets/hero_packs/$id/').load(faceFile);
      final name = json['name'] as String;
      return HeroPack(
        name: name,
        age: json['age'] as int?,
        message: json['message'] as String? ?? 'Well done, $name!',
        face: face,
      );
    } catch (_) {
      return null;
    }
  }
}
