package games.brennan.mcjavasafetycheck.client;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Which launcher runs this game, so the card can say where that launcher keeps its JVM arguments.
 * Best effort, from the game folder: most launchers keep instances under a folder with their name,
 * CurseForge and Modrinth instances carry a signature file, and the official launcher uses the
 * default {@code .minecraft} folder. Anything else is {@link #UNKNOWN}, and the card falls back to
 * launcher-neutral words.
 */
public enum LauncherKind {
    CURSEFORGE, MODRINTH, MINECRAFT_LAUNCHER, PRISM, MULTIMC, ATLAUNCHER, UNKNOWN;

    private static final Logger LOGGER = LogUtils.getLogger();
    private static volatile LauncherKind cached;

    /** The launcher running this game, detected once. Never throws. */
    public static LauncherKind current() {
        LauncherKind l = cached;
        if (l == null) {
            l = detect();
            cached = l;
        }
        return l;
    }

    private static LauncherKind detect() {
        try {
            Path gameDir = FMLPaths.GAMEDIR.get().toAbsolutePath();
            return classify(gameDir.toString(),
                Files.exists(gameDir.resolve("minecraftinstance.json")),
                Files.exists(gameDir.resolve("modrinth.index.json")));
        } catch (Throwable t) {
            LOGGER.debug("Launcher probe failed: {}", t.toString());
            return UNKNOWN;
        }
    }

    /** Pure: brand in the path first, then signature files, then the official default folder. */
    static LauncherKind classify(String gameDirPath, boolean hasCurseForgeSignature, boolean hasModrinthSignature) {
        String p = gameDirPath.replace('\\', '/').toLowerCase(Locale.ROOT);
        while (p.endsWith("/")) p = p.substring(0, p.length() - 1);
        if (p.contains("curseforge")) return CURSEFORGE;
        if (p.contains("modrinth") || p.contains("theseus")) return MODRINTH;
        if (p.contains("prismlauncher")) return PRISM;
        if (p.contains("multimc")) return MULTIMC;
        if (p.contains("atlauncher")) return ATLAUNCHER;
        if (hasCurseForgeSignature) return CURSEFORGE;
        if (hasModrinthSignature) return MODRINTH;
        if (p.endsWith("/.minecraft") || p.endsWith("/application support/minecraft")) {
            return MINECRAFT_LAUNCHER;
        }
        return UNKNOWN;
    }
}
