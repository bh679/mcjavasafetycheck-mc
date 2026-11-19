package games.brennan.mcjavasafetycheck;

import games.brennan.mcjavasafetycheck.config.SafetyCheckConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

/**
 * MC Java Safety Check: warns a player whose Java has the Java4Shell flaw (CVE-2026-114938) and
 * shows how to fix it ({@code client.JavaRceNotice}), and logs the same check when a server starts
 * ({@code server.JavaRceServerCheck}). Reads the local JVM only; sends nothing anywhere.
 *
 * <p>A host mod or modpack can name itself as the card's sender: see
 * {@link games.brennan.mcjavasafetycheck.api.MCJavaSafetyCheckApi}.</p>
 */
@Mod(MCJavaSafetyCheck.MOD_ID)
public final class MCJavaSafetyCheck {

    public static final String MOD_ID = "mcjavasafetycheck";

    /** The mod's own name: the card's sender unless a host mod or the config names another. */
    public static final String MOD_NAME = "MC Java Safety Check";

    public MCJavaSafetyCheck(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, SafetyCheckConfig.SPEC);
    }
}
