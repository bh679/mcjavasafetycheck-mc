"""Builds the icon: the Dungeon Train logo with the vanilla shield's front face in the middle.

Run: python3 art/make_icon.py <dungeon-train logo.png> <minecraft client jar>
 -> listing/icon.png and src/main/resources/mcjavasafetycheck_logo.png (512 x 512).
"""
import io
import sys
import zipfile

from PIL import Image

SIZE = 512
SCALE = 13          # 12 x 22 texels -> 156 x 286 px, nearest-neighbour
OUTLINE = 6         # px of dark outline, like the other mods' icons
SHIELD_FACE = (1, 1, 13, 23)  # front face in textures/entity/shield_base_nopattern.png


def main(logo_path, client_jar):
    logo = Image.open(logo_path).convert("RGBA").resize((SIZE, SIZE), Image.LANCZOS)
    with zipfile.ZipFile(client_jar) as jar:
        tex = Image.open(io.BytesIO(jar.read("assets/minecraft/textures/entity/shield_base_nopattern.png")))
    face = tex.convert("RGBA").crop(SHIELD_FACE)
    face = face.resize((face.width * SCALE, face.height * SCALE), Image.NEAREST)
    w, h = face.width + 2 * OUTLINE, face.height + 2 * OUTLINE
    framed = Image.new("RGBA", (w, h), (24, 20, 16, 255))
    framed.paste(face, (OUTLINE, OUTLINE), face)
    logo.alpha_composite(framed, ((SIZE - w) // 2, (SIZE - h) // 2))
    for out in ("listing/icon.png", "src/main/resources/mcjavasafetycheck_logo.png"):
        logo.save(out, optimize=True)


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2])
