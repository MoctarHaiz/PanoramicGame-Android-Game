"""Copy the art, sound and level data of the legacy Android game into the Flutter project.

Usage (from the flutter/ folder):
    python3 tool/import_android_assets.py

Requires: Pillow, numpy, scipy, and ffmpeg on the PATH.

What it does:
  * copies every image the game uses into assets/images/ (same sub-folders as Android)
  * splits the 16000 px wide background into tiles (GPUs and browsers cap texture size)
  * converts .ogg sounds to .mp3 (iOS and Safari cannot play OGG)
  * copies the level map to assets/levels/level1.csv
  * detects the hero's head in every animation frame and writes
    assets/hero/head_anchors.json (used to draw a personalized "big head")
"""
import json
import shutil
import subprocess
from pathlib import Path

import numpy as np
from PIL import Image
from scipy import ndimage

ROOT = Path(__file__).resolve().parents[1]
ANDROID = ROOT.parent / "Android" / "app" / "src" / "main"
SRC = ANDROID / "assets"
IMAGES = ROOT / "assets" / "images"
AUDIO = ROOT / "assets" / "audio"

BACKGROUND_TILE_WIDTH = 2000

HERO_FRAMES = (
    [f"running/panoramic{i}.png" for i in range(1, 18)]
    + [f"jump/panoramicjump{i}.png" for i in range(1, 16)]
    + [f"stick/stick{i}.png" for i in range(0, 9)]
    + ["panoramicaccroupi.png"]
)

OTHER_IMAGES = (
    [f"bandit/small/bandit{i}.png" for i in range(1, 11)]
    + [f"bandit/small/banditMort{i}.png" for i in range(1, 11)]
    + [f"fire/n{i}.png" for i in range(1, 5)]
    + [f"dust/dust{i}.png" for i in range(1, 5)]
    + [f"cover/cover{i}.png" for i in range(1, 9)]
    + [f"lifePan/lifePan{i}.png" for i in range(1, 4)]
    + [f"gameOver/gameOver{i}.png" for i in range(1, 4)]
    + [f"gameOver/star{i}.png" for i in range(1, 6)]
    + [
        "gameOver/gameOverRope.png",
        "bubble/redbob1.png",
        "bubble/redbob2.png",
        "levelEnd/levelEnd1.png",
        "levelEnd/levelEnd2.png",
        "levelEnd/levelEndHome.png",
        "level1Tileset.png",
        "HaizStudioSplash.png",
        "menu.png",
        "pause.png",
    ]
)


def copy_images():
    for rel in HERO_FRAMES + OTHER_IMAGES:
        dst = IMAGES / rel
        dst.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(SRC / rel, dst)


def split_background():
    bg = Image.open(SRC / "background.png")
    out = IMAGES / "background"
    out.mkdir(parents=True, exist_ok=True)
    count = 0
    for x in range(0, bg.width, BACKGROUND_TILE_WIDTH):
        bg.crop((x, 0, min(x + BACKGROUND_TILE_WIDTH, bg.width), bg.height)).save(out / f"bg{count}.png", optimize=True)
        count += 1
    return {"tiles": count, "tileWidth": BACKGROUND_TILE_WIDTH, "width": bg.width, "height": bg.height}


def convert_audio():
    AUDIO.mkdir(parents=True, exist_ok=True)
    shutil.copyfile(SRC / "audio" / "chaka.mp3", AUDIO / "chaka.mp3")
    for name in ("projectile", "enemy"):
        subprocess.run(
            ["ffmpeg", "-y", "-loglevel", "error", "-i", str(SRC / "audio" / f"{name}.ogg"), "-q:a", "4", str(AUDIO / f"{name}.mp3")],
            check=True,
        )


def copy_level():
    dst = ROOT / "assets" / "levels" / "level1.csv"
    dst.parent.mkdir(parents=True, exist_ok=True)
    shutil.copyfile(ANDROID / "res" / "raw" / "level.csv", dst)


def head_anchors():
    """Find the hero's hair (largest dark, desaturated blob) in each frame.

    Coordinates are normalized to the frame (0..1), so they stay valid
    whatever size the sprite is drawn at.
    """
    anchors = {}
    for rel in HERO_FRAMES:
        im = Image.open(SRC / rel).convert("RGBA").resize((128, 192))
        a = np.asarray(im).astype(int)
        lum = 0.299 * a[..., 0] + 0.587 * a[..., 1] + 0.114 * a[..., 2]
        sat = a[..., :3].max(axis=2) - a[..., :3].min(axis=2)
        dark = (a[..., 3] > 128) & (lum < 90) & (sat < 40)
        labels, n = ndimage.label(dark)
        sizes = ndimage.sum(dark, labels, range(1, n + 1))
        ys, xs = np.where(labels == int(np.argmax(sizes)) + 1)
        anchors[rel] = [round(float(xs.mean()) / 128, 3), round(float(ys.mean()) / 192, 3)]
    dst = ROOT / "assets" / "hero" / "head_anchors.json"
    dst.parent.mkdir(parents=True, exist_ok=True)
    dst.write_text(json.dumps(anchors, indent=1) + "\n")


if __name__ == "__main__":
    copy_images()
    info = split_background()
    convert_audio()
    copy_level()
    head_anchors()
    print("background:", info)
    print("done")
