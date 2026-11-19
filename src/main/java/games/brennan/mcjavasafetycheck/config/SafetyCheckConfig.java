package games.brennan.mcjavasafetycheck.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * {@code config/mcjavasafetycheck-client.toml}. Client only: the server side has nothing to set.
 */
public final class SafetyCheckConfig {

    public static final ModConfigSpec SPEC;
    /** Who the card says checked the Java. Empty = the host mod's name, or this mod's own. */
    public static final ModConfigSpec.ConfigValue<String> SENDER_NAME;
    /** The {@code java.runtime.version} the player said "Don't show me again" for. Empty = never. */
    public static final ModConfigSpec.ConfigValue<String> WARNING_DISMISSED_FOR;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();
        SENDER_NAME = b
            .comment("The name the warning card gives as the sender (\"<name> checked the Java this game runs on\").",
                     "A modpack can put its own name here. Empty = the mod that bundles this one names itself,",
                     "or \"MC Java Safety Check\".")
            .define("senderName", "");
        WARNING_DISMISSED_FOR = b
            .comment("Internal: the Java version you chose \"Don't show me again\" for. The warning comes back",
                     "if you switch to a different Java. Empty = never dismissed. Clear it to see the warning again.")
            .define("warningDismissedFor", "");
        SPEC = b.build();
    }

    private SafetyCheckConfig() {}

    public static boolean isLoaded() {
        return SPEC.isLoaded();
    }

    public static String senderName() {
        return isLoaded() ? SENDER_NAME.get() : "";
    }

    public static String dismissedFor() {
        return isLoaded() ? WARNING_DISMISSED_FOR.get() : "";
    }

    public static void setDismissedFor(String runtimeVersion) {
        if (!isLoaded()) return;
        WARNING_DISMISSED_FOR.set(runtimeVersion == null ? "" : runtimeVersion);
        WARNING_DISMISSED_FOR.save();
    }
}
