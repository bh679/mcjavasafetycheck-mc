This mod is designed specifically for the [Dungeon Train modpack](https://modrinth.com/modpack/bEFyz3ji) / [mod](https://modrinth.com/mod/dungeon-train), but can be used independently.

## What it does

Java4Shell (CVE-2026-114938) is a publicly reported flaw in the fast Base64 decoder of some Java versions on newer Intel and AMD processors. Minecraft decodes Base64 for things like skins and server icons, so a hostile multiplayer server could use it to crash your game or run code on your computer.

MC Java Safety Check looks at the Java your game is running on when you reach the title screen. **Only if that Java is affected**, it shows a card that says what it found, links the [official CERT-EU advisory](https://cert.europa.eu/publications/security-advisories/2026-021), and offers two fixes:

- **Update Java.** Steps for the launcher or Java vendor this game actually uses: restart the Minecraft Launcher, CurseForge or ATLauncher, re-download Java in the Modrinth App or Prism Launcher, or get a newer build from the vendor you installed it from.
- **Use Arguments.** The JVM argument `-XX:-UseBASE64Intrinsics`, with a Copy button and the place your launcher keeps its arguments. It switches off only the faulty part of Java.

Once a fix is in place, the card stops appearing by itself. "Continue anyway" asks once more, and "Don't show me again" is remembered for that Java only.

On a server, it writes the same check to the log when the server starts.

## Who sees the card

Only players for whom every condition holds:

- an x86-64 processor with AVX-512,
- an affected Java: 17.0.6 or newer, 21, 25 or 27, below 17.0.21.1, 21.0.13.1, 25.0.5.1 or 27.0.1.1,
- the Base64 shortcut switched on (the default).

Apple silicon, older processors and fixed Javas never see it.

## What it doesn't do

- It never runs the flaw or tests your PC with an attack. It only reads Java's version and settings.
- It doesn't change Java or your launcher for you. You make the fix yourself, in a minute.
- It never asks for passwords or payment.
- It doesn't send anything over the network. Links open only when you click them.

## Setup

- NeoForge 1.21.1. No other mods needed.
- Install it on the client, the server or both. Each side works on its own.
- A modpack can name itself on the card ("My Pack: Java Safety Check") with `senderName` in `config/mcjavasafetycheck-client.toml`.
- Text in 22 languages. Translations other than English are AI-made.

To check it is running, look for `[MC Java Safety Check]` in `latest.log`.

## Licence

PolyForm Shield 1.0.0. Source: [GitHub](https://github.com/bh679/mcjavasafetycheck-mc).
