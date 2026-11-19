# MC Java Safety Check

A NeoForge 1.21.1 mod that checks whether the Java your game runs on has **Java4Shell**
(CVE-2026-114938), the publicly reported flaw in Java's fast Base64 decoder on newer Intel and
AMD processors. Only if your Java is affected, it shows a card over the title screen with the fix
for your setup: update Java, or add one JVM argument (`-XX:-UseBASE64Intrinsics`). A server logs
the same check when it starts.

- Checked on your PC only. Nothing is sent anywhere.
- Warns only when every condition holds: an x86-64 CPU with AVX-512, an affected Java
  (17.0.6+ / 21 / 25 / 27 below 17.0.21.1 / 21.0.13.1 / 25.0.5.1 / 27.0.1.1) and the Base64
  intrinsic on. Apple silicon, older CPUs and fixed Javas never see it.
- Advisory: https://cert.europa.eu/publications/security-advisories/2026-021

Built for [Dungeon Train](https://modrinth.com/mod/dungeon-train), which bundles it, but works
with any modpack. A modpack can name itself on the card with `senderName` in
`config/mcjavasafetycheck-client.toml`.

Licence: PolyForm Shield 1.0.0. Issues: https://dungeon.training/issues#java-safety-check
