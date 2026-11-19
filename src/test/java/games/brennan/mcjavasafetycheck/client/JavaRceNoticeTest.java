package games.brennan.mcjavasafetycheck.client;

import games.brennan.mcjavasafetycheck.check.JavaRceCheck.Verdict;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaRceNoticeTest {

    @Test
    @DisplayName("At risk and never dismissed: shown")
    void atRisk_shown() {
        assertTrue(JavaRceNotice.shouldShow(Verdict.AT_RISK, "21.0.13+11-LTS", "", false));
        assertTrue(JavaRceNotice.shouldShow(Verdict.AT_RISK, "21.0.13+11-LTS", null, false));
    }

    @Test
    @DisplayName("Dismissed for this Java: hidden; a different Java: shown again")
    void dismissal_isPerRuntime() {
        assertFalse(JavaRceNotice.shouldShow(Verdict.AT_RISK, "21.0.13+11-LTS", "21.0.13+11-LTS", false));
        assertTrue(JavaRceNotice.shouldShow(Verdict.AT_RISK, "21.0.14+7-LTS", "21.0.13+11-LTS", false));
    }

    @Test
    @DisplayName("Not at risk: never shown unless forced")
    void notAtRisk_hidden() {
        for (Verdict v : new Verdict[] {Verdict.SAFE, Verdict.SAFE_WORKAROUND, Verdict.UNKNOWN}) {
            assertFalse(JavaRceNotice.shouldShow(v, "21.0.13", "", false));
        }
        assertTrue(JavaRceNotice.shouldShow(Verdict.SAFE, "21.0.13", "21.0.13", true));
    }

    @Test
    @DisplayName("Every launcher has its own directions key")
    void whereKey_perLauncher() {
        assertEquals("gui.mcjavasafetycheck.where.curseforge", JavaRceNotice.whereKey(LauncherKind.CURSEFORGE));
        for (LauncherKind l : LauncherKind.values()) {
            assertTrue(JavaRceNotice.whereKey(l).startsWith("gui.mcjavasafetycheck.where."));
        }
    }

    @Test
    @DisplayName("Main title: the bare mod name when it speaks for itself, otherwise the sender's name")
    void mainTitle() {
        assertEquals("MC Java Safety Check", JavaRcePromptScreen.mainTitle("MC Java Safety Check").getString());
        net.minecraft.network.chat.contents.TranslatableContents c = (net.minecraft.network.chat.contents.TranslatableContents)
            JavaRcePromptScreen.mainTitle("Dungeon Train").getContents();
        assertEquals("gui.mcjavasafetycheck.main.title", c.getKey());
        assertEquals("Dungeon Train", c.getArgs()[0]);
    }
}
