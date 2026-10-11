/// Frame animation advanced by a fixed amount each game tick.
/// Port of the Android `Animation` class, so the original timings
/// (frame durations and per-tick steps) can be reused as they are.
class TickAnimation<T> {
  final _frames = <(T, int)>[]; // value, end time
  int _total = 0;
  int _time = 0;
  int _index = 0;

  void add(T frame, int duration) {
    _total += duration;
    _frames.add((frame, _total));
  }

  void addAll(Iterable<T> frames, int duration) {
    for (final f in frames) {
      add(f, duration);
    }
  }

  void update(int elapsed) {
    if (_frames.length < 2) return;
    _time += elapsed;
    if (_time >= _total) {
      _time %= _total;
      _index = 0;
    }
    while (_time > _frames[_index].$2) {
      _index++;
    }
  }

  T get current => _frames[_index].$1;

  void reset() {
    _time = 0;
    _index = 0;
  }
}
