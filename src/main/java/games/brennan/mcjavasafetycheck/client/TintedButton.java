package games.brennan.mcjavasafetycheck.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * A vanilla button with a coloured tint on its background, label left white. The card uses it
 * in blue for the step that fixes the flaw itself.
 */
@OnlyIn(Dist.CLIENT)
public final class TintedButton extends Button {

    private static final WidgetSprites SPRITES = new WidgetSprites(
            ResourceLocation.withDefaultNamespace("widget/button"),
            ResourceLocation.withDefaultNamespace("widget/button_disabled"),
            ResourceLocation.withDefaultNamespace("widget/button_highlighted")
    );

    private final float tintR;
    private final float tintG;
    private final float tintB;

    /** {@code tint*} multiply the vanilla sprite, so a channel above 1 survives its shading. */
    public TintedButton(int x, int y, int width, int height, Component message, OnPress onPress,
                        float tintR, float tintG, float tintB) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.tintR = tintR;
        this.tintG = tintG;
        this.tintB = tintB;
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        g.setColor(tintR, tintG, tintB, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        g.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()),
                this.getX(), this.getY(), this.getWidth(), this.getHeight());
        g.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int textColor = this.active ? 0xFFFFFF : 0xA0A0A0;
        this.renderString(g, mc.font, textColor | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}
