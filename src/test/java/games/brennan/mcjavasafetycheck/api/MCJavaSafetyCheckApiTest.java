package games.brennan.mcjavasafetycheck.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MCJavaSafetyCheckApiTest {

    @Test
    @DisplayName("Sender: config wins, then the dev property, then the host mod, then this mod's own name")
    void senderPrecedence() {
        assertEquals("My Pack", MCJavaSafetyCheckApi.resolveSender("My Pack", "Dev", "Dungeon Train"));
        assertEquals("Dev", MCJavaSafetyCheckApi.resolveSender("", "Dev", "Dungeon Train"));
        assertEquals("Dungeon Train", MCJavaSafetyCheckApi.resolveSender("  ", "", "Dungeon Train"));
        assertEquals("MC Java Safety Check", MCJavaSafetyCheckApi.resolveSender(null, null, ""));
        assertEquals("My Pack", MCJavaSafetyCheckApi.resolveSender(" My Pack ", "", ""));
    }

    @Test
    @DisplayName("A host's old dismissal is adopted only when this mod has none")
    void dismissalHandOver() {
        assertEquals("21.0.13+11", MCJavaSafetyCheckApi.mergeDismissal("", "21.0.13+11"));
        assertEquals("21.0.14+7", MCJavaSafetyCheckApi.mergeDismissal("21.0.14+7", "21.0.13+11"));
        assertEquals("", MCJavaSafetyCheckApi.mergeDismissal(null, null));
    }
}
