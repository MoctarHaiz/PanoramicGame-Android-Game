import 'dart:typed_data';
import 'dart:ui';

import 'config.dart';

/// What a tile does. Mirrors the tile ids handled in the Android `Tile` class.
enum TileKind {
  /// Not drawn.
  none,

  /// Grass top: the hero and enemies stand on it.
  ground,

  /// Ground body: blocks the scrolling until the hero jumps over it.
  wall,

  /// Water surface: the hero falls through it.
  water,

  /// Red bob: collected by the hero.
  bubble,

  /// Drawn, no effect (trees, signs, mushrooms...).
  decor,
}

TileKind tileKindOf(int id) {
  if (id <= 0 || id > 85) return TileKind.none;
  if (id <= 3 || id == 22 || id == 24 || id == 28 || (id >= 37 && id <= 39) || (id >= 48 && id <= 59)) {
    return TileKind.ground;
  }
  if ((id >= 13 && id <= 15) || (id >= 25 && id <= 27) || (id >= 60 && id <= 83)) {
    return TileKind.wall;
  }
  if (id == 4) return TileKind.water;
  if (id == 84) return TileKind.bubble;
  return TileKind.decor;
}

/// A tile position in the level grid.
typedef TileCell = ({int column, int row});

/// The tile map, in world coordinates (x grows to the right of the level start).
class Level {
  Level._(this.columns, this.rows, this._ids);

  /// Parses the CSV exported from Tiled (same file as the Android `res/raw/level.csv`).
  factory Level.parse(String csv) {
    final lines = csv.split('\n').map((l) => l.trim()).where((l) => l.isNotEmpty && !l.startsWith('!')).toList();
    final cells = [for (final line in lines) line.split(',').where((v) => v.isNotEmpty).map(int.parse).toList()];
    final rows = cells.length;
    final columns = cells.fold<int>(0, (m, r) => r.length > m ? r.length : m);
    final ids = Int16List(columns * rows);
    for (var r = 0; r < rows; r++) {
      for (var c = 0; c < cells[r].length; c++) {
        ids[r * columns + c] = cells[r][c] < 0 ? 0 : cells[r][c];
      }
    }
    return Level._(columns, rows, ids);
  }

  final int columns;
  final int rows;
  final Int16List _ids;

  double get width => columns * Config.tileSize;
  double get height => rows * Config.tileSize;

  int idAt(int column, int row) => _ids[row * columns + column];
  TileKind kindAt(int column, int row) => tileKindOf(idAt(column, row));

  void clear(TileCell cell) => _ids[cell.row * columns + cell.column] = 0;

  Rect tileRect(int column, int row) =>
      Rect.fromLTWH(column * Config.tileSize, row * Config.tileSize, Config.tileSize, Config.tileSize);

  Rect _wallRect(int column, int row) {
    final r = tileRect(column, row);
    return Rect.fromLTRB(r.left, r.top, r.right - Config.wallInset, r.bottom - Config.wallInset);
  }

  /// The ground tile [area] touches, or null. Like the Android version, when
  /// several match, the right-most then lowest one wins.
  Rect? groundTouching(Rect area) {
    final cell = _lastTouching(area, TileKind.ground, tileRect);
    return cell == null ? null : tileRect(cell.column, cell.row);
  }

  /// The (slightly smaller) wall tile [area] touches, or null.
  Rect? wallTouching(Rect area) {
    final cell = _lastTouching(area, TileKind.wall, _wallRect);
    return cell == null ? null : _wallRect(cell.column, cell.row);
  }

  TileCell? bubbleTouching(Rect area) => _lastTouching(area, TileKind.bubble, tileRect);

  TileCell? _lastTouching(Rect area, TileKind kind, Rect Function(int, int) rectOf) {
    final c0 = (area.left / Config.tileSize).floor().clamp(0, columns - 1);
    final c1 = (area.right / Config.tileSize).floor().clamp(0, columns - 1);
    final r0 = (area.top / Config.tileSize).floor().clamp(0, rows - 1);
    final r1 = (area.bottom / Config.tileSize).floor().clamp(0, rows - 1);
    TileCell? found;
    for (var c = c0; c <= c1; c++) {
      for (var r = r0; r <= r1; r++) {
        if (kindAt(c, r) == kind && rectOf(c, r).overlaps(area)) found = (column: c, row: r);
      }
    }
    return found;
  }
}
