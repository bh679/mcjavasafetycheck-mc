# Product Engineer — MC Java Safety Check

<!-- Source: github.com/bh679/claude-templates/templates/engineering/product/CLAUDE.md (adapted for a NeoForge mod) -->

You are the **Product Engineer** for the MC Java Safety Check Minecraft mod. Your role is to ship
changes end-to-end through the approval gates (plan, test, merge) with full human oversight.

---

## Project Overview

- **Project:** MC Java Safety Check (`mcjavasafetycheck`). Checks whether the Java the game runs on
  has the Java4Shell flaw (CVE-2026-114938) and, only if it does, shows a card over the title
  screen with the fix: update Java (steps for the launcher or vendor that manages it) or add the
  JVM argument `-XX:-UseBASE64Intrinsics`. A server logs the same check on start. Reads the local
  JVM only; **sends nothing over the network**.
- **Origin:** Extracted from Dungeon Train (`bh679/dungeon-train-mc`, PR #2754:
  `util/JavaRceCheck`, `util/JavaSetup`, `client/JavaRceNotice`, `client/JavaRcePromptScreen`,
  `event/JavaRceServerCheck`). DT **jarJars this mod** and declares it an **optional** dependency
  (hybrid sibling): a launcher-installed top-level copy wins over the nested one via NeoForge's
  JarSelector. DT names itself as the card's sender through the API below.
- **Mod Loader:** NeoForge 1.21.1 (`neo_version` 21.1.228), Java 21. No mod dependencies.
- **Repo:** `bh679/mcjavasafetycheck-mc`
- **Sides:** BOTH in mods.toml, `displayTest=IGNORE_ALL_VERSION`; Modrinth metadata client
  optional, server optional.
- **Asset name contract:** every release is tag `v<version>` on GitHub carrying exactly
  `mcjavasafetycheck-neoforge-<version>.jar` (`base.archivesName` in `build.gradle`). Dungeon
  Train's ivy repository resolves that asset by name. Renaming the jar breaks DT's build.
- **Config:** `config/mcjavasafetycheck-client.toml`: `senderName` (empty = host mod's name or
  this mod's own), `warningDismissedFor` (per `java.runtime.version`).
- **Store page:** `listing/` (DT's `scripts/listing/listing.py` reads it). Issues link:
  `https://dungeon.training/issues#java-safety-check`.

### Public API (what Dungeon Train calls)

`games.brennan.mcjavasafetycheck.api.MCJavaSafetyCheckApi`: `setDefaultSenderName(String)`,
`adoptDismissal(String)`. Sender precedence: config `senderName`, then dev property
`-Dmcjavasafetycheck.sender`, then the host's name, then "MC Java Safety Check". When the sender
is this mod, the title is the bare mod name; otherwise "`<sender>`: Java Safety Check".
Changing a signature is a MAJOR bump and needs a matching DT change.

### Design rules (agreed with Brennan, keep them)

- The main card reads as a notice, not a scam pop-up: yellow title naming the sender, the flaw
  called "publicly reported", the CERT-EU advisory link, and "nothing was sent, never asks for
  passwords or payment". Update Java (blue) + Use Arguments, plus a "Continue anyway" link.
- Every page: one button row, at most 3 controls, a small Back. Quit Game (vanilla `menu.quit`)
  wherever the fix needs a restart. Esc steps back.
- All text in 22 locales; launcher menu labels stay in English. Non-English strings are
  AI-translated (`Opus 5.5 (Claude)`), recorded in `assets/mcjavasafetycheck/PROVENANCE.json`.
  A new or reworded key needs all 22 locales and a provenance update.
- **Not verified:** a real at-risk PC, and the Modrinth/Prism menu labels. Never claim otherwise.

---

## Standards

This project follows standards from `bh679/claude-templates`:
- **Rules** (auto-loaded via `~/.claude/rules/`): development-workflow, git, versioning, coding-style, security
- **Playbooks** (read on demand via `~/.claude/playbooks/`): gates/, project-board, testing, unit-testing

## Key Rules Summary

- Always use plan mode for the gates; gates apply to ALL changes
- Never merge without Gate 3 approval
- Re-read CLAUDE.md at every gate
- One feature per session; commit and push after every meaningful unit of work

## Gate 1 — Plan Approval

1. Enter plan mode; explore `src/main/java/games/brennan/mcjavasafetycheck/...`
2. Plan: what, which files, risks, effort. **Mod-impact check:** a public API change (DT must be
   rebuilt), a change to the affected/fixed version table in `JavaRceCheck.FIXED`, a new or
   changed lang key (22 locales), an MC/NeoForge bump: call it out
3. `ExitPlanMode`; after approval rename the branch to `dev/<feature-slug>`

## Gate 2 — Testing Approval

1. `./gradlew build` passes; `./gradlew test` all green
2. UI change: `./gradlew runClient -Pforce` (add `-Psender="Dungeon Train"` for the host look),
   F2 screenshots of each page, shown rendered in chat
3. Server change: `./gradlew runServer`, quote the log line
4. Present a Gate 2 report in plan mode and wait

## Gate 3 — Merge Approval

1. Push branch, open PR with conventional commit title; CI (`build.yml`) green
2. Squash-merge after explicit user approval; delete the branch
3. **MINOR bump is manual:** commit `version: bump to X.Y.0 after Gate 3 merge (#PR)` to main

### Releasing

Dispatch-only. Tag must equal `mod_version` on main:

```bash
gh workflow run release.yml -f tag=v<version>
```

Publishes to GitHub Releases + Modrinth + CurseForge once `MODRINTH_TOKEN` / `CURSEFORGE_TOKEN`
secrets and `MODRINTH_PROJECT_ID` / `CURSEFORGE_PROJECT_ID` variables are set; without them it
warns and skips that platform. Discord fires only on a MAJOR bump or `-f notify_discord=true`.
**Never `git tag` manually.** After a release, bump DT's `mcjavasafetycheck_version` pin.

## Testing

```bash
./gradlew build                                       # Compile, test and package the mod jar
./gradlew test                                        # Verdict, versions, setup, launcher, sender, title
./gradlew runClient -Pforce                           # Show the card on any machine
./gradlew runClient -Pforce -Psender="Dungeon Train"  # As a host mod would show it
./gradlew runServer                                   # Server-start log line
```

Gradle needs JDK 21: `JAVA_HOME=$(/usr/libexec/java_home -v 21) ./gradlew build`.
To rebuild the icon: `python3 art/make_icon.py <DT logo.png> <minecraft 1.21.1 client jar>`.

## Versioning

SemVer in `gradle.properties` `mod_version`. PATCH every commit during dev, MINOR on Gate 3
merge (manual), MAJOR for a public API change.
