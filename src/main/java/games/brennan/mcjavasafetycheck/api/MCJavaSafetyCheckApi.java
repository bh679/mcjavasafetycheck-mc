package games.brennan.mcjavasafetycheck.api;

import games.brennan.mcjavasafetycheck.MCJavaSafetyCheck;

/**
 * What a host mod (one that bundles this mod) may call. Both calls are safe from any thread and at
 * any point of loading; call them during mod setup, before the title screen shows.
 *
 * <p>Changing a signature here is a MAJOR version bump.</p>
 */
public final class MCJavaSafetyCheckApi {

    /** Dev override, {@code -Dmcjavasafetycheck.sender=<name>}: previews a host's name. */
    static final String SENDER_PROPERTY = "mcjavasafetycheck.sender";

    private static volatile String hostSenderName = "";
    private static volatile String pendingDismissal = "";

    private MCJavaSafetyCheckApi() {}

    /**
     * Names the host mod as the card's sender, e.g. "Dungeon Train" shows "Dungeon Train: Java
     * Safety Check" and "Dungeon Train will never ask for passwords or payment". The player's or
     * modpack's {@code senderName} config, when set, still wins.
     */
    public static void setDefaultSenderName(String name) {
        hostSenderName = name == null ? "" : name.strip();
    }

    /**
     * Hands over a dismissal the host recorded before this mod existed: the
     * {@code java.runtime.version} its player chose "Don't show me again" for. Applied once the
     * config is loaded, and only if this mod has no dismissal of its own.
     */
    public static void adoptDismissal(String runtimeVersion) {
        pendingDismissal = runtimeVersion == null ? "" : runtimeVersion.strip();
    }

    /** The dismissal waiting to be adopted, or empty. */
    public static String pendingDismissal() {
        return pendingDismissal;
    }

    /** The sender the card names right now. */
    public static String senderName(String configured) {
        return resolveSender(configured, System.getProperty(SENDER_PROPERTY, ""), hostSenderName);
    }

    /** Pure: config, then the dev property, then the host's name, then this mod's own name. */
    static String resolveSender(String configured, String devProperty, String host) {
        for (String s : new String[] {configured, devProperty, host}) {
            if (s != null && !s.isBlank()) return s.strip();
        }
        return MCJavaSafetyCheck.MOD_NAME;
    }

    /** Pure: the dismissal to keep, adopting the host's only when there is none of our own. */
    static String mergeDismissal(String own, String adopted) {
        if (own != null && !own.isEmpty()) return own;
        return adopted == null ? "" : adopted;
    }

    /** {@link #mergeDismissal} for callers outside this package. */
    public static String dismissalAfterHandOver(String own) {
        return mergeDismissal(own, pendingDismissal);
    }
}
