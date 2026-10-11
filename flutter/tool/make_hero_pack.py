"""Turn a customer's photo into a hero pack (face + name + message) for the game.

Usage (from the flutter/ folder):
    python3 tool/make_hero_pack.py photo.jpg --id emma --name Emma --age 7
    python3 tool/make_hero_pack.py photo.jpg --id emma --name Emma --box 120,80,300,300

  --box x,y,w,h   square around the face in the photo (pixels). Without it the
                  centre of the photo is used, so crop the photo to the face first.
  --message       text shown when the level is completed
                  (default: "Happy <age>th birthday, <name>!")

Output: assets/hero_packs/<id>/face.png and hero.json.
Play it with:  flutter run -d chrome --web-browser-flag=... then open ?hero=<id>
           or build a dedicated app with --dart-define=HERO=<id>

Requires: Pillow.
"""
import argparse
import json
import re
from pathlib import Path

from PIL import Image, ImageDraw, ImageOps

ROOT = Path(__file__).resolve().parents[1]
FACE_SIZE = 256


def ordinal(n: int) -> str:
    if 10 <= n % 100 <= 20:
        return f"{n}th"
    return f"{n}{ {1: 'st', 2: 'nd', 3: 'rd'}.get(n % 10, 'th') }"


def circle_face(photo: Path, box: str | None) -> Image.Image:
    im = ImageOps.exif_transpose(Image.open(photo)).convert("RGBA")
    if box:
        x, y, w, h = (int(v) for v in box.split(","))
    else:
        side = min(im.size)
        x, y, w, h = (im.width - side) // 2, (im.height - side) // 2, side, side
    face = im.crop((x, y, x + w, y + h)).resize((FACE_SIZE, FACE_SIZE), Image.LANCZOS)
    mask = Image.new("L", (FACE_SIZE * 4, FACE_SIZE * 4), 0)
    ImageDraw.Draw(mask).ellipse((0, 0, FACE_SIZE * 4 - 1, FACE_SIZE * 4 - 1), fill=255)
    face.putalpha(mask.resize((FACE_SIZE, FACE_SIZE), Image.LANCZOS))
    return face


def main():
    p = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    p.add_argument("photo", type=Path)
    p.add_argument("--id", required=True, help="pack id: lowercase letters, digits, - or _")
    p.add_argument("--name", required=True)
    p.add_argument("--age", type=int)
    p.add_argument("--message")
    p.add_argument("--box")
    a = p.parse_args()

    if not re.fullmatch(r"[a-z0-9_-]{1,40}", a.id):
        p.error("--id must be lowercase letters, digits, - or _")
    message = a.message or (f"Happy {ordinal(a.age)} birthday, {a.name}!" if a.age else f"Well done, {a.name}!")

    out = ROOT / "assets" / "hero_packs" / a.id
    out.mkdir(parents=True, exist_ok=True)
    circle_face(a.photo, a.box).save(out / "face.png", optimize=True)
    (out / "hero.json").write_text(json.dumps({"name": a.name, "age": a.age, "message": message, "face": "face.png"}, indent=2) + "\n")
    print(f"Hero pack written to {out.relative_to(ROOT)}")
    print(f"Add '- assets/hero_packs/{a.id}/' under flutter: assets: in pubspec.yaml")


if __name__ == "__main__":
    main()
