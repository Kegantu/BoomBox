package me.kegantu.boombox.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import me.kegantu.boombox.BoomBox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2fStack;

@Environment(EnvType.CLIENT)
public class NotificationToast implements Toast {

    private final Identifier NOTIFICATIONS_TEXTURE = Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "toast/notifications");
    private final String soundtrackName;
    private float rotation;
    private Toast.Visibility visibility = Toast.Visibility.HIDE;

    public NotificationToast(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    @Override
    public Visibility getWantedVisibility() {
        return this.visibility;
    }

    @Override
    public void update(ToastManager manager, long time) {
        this.visibility = time >= 7500.0 * manager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    @Override
    public void render(GuiGraphics context, Font textRenderer, long startTime) {
        Matrix3x2fStack stack = context.pose();
        rotation += 0.5f * Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        if (rotation >= 360f){
            rotation = 0;
        }

        var delta = rotation / 360;
        context.blitSprite(RenderPipelines.GUI_TEXTURED, NOTIFICATIONS_TEXTURE, 256, 256, 0,0,0,0, 176, 43);
        stack.pushMatrix();
        stack.translate(4.5f, 5f);
        stack.scale(1.6f,1.6f);
        stack.rotateAbout(Axis.ZP.rotationDegrees(Mth.lerp(delta, 0f, 360f)).angle(), 10, 10);
        context.blitSprite(RenderPipelines.GUI_TEXTURED, NOTIFICATIONS_TEXTURE, 256, 256, 177,2,0,0, 20, 20);
        stack.popMatrix();

        var textLine = textRenderer.split(FormattedText.of(soundtrackName), 140);
        int y = 7;
        for (int i = 0; i < textLine.size(); i++) {
            context.drawString(textRenderer, textLine.get(i), 40, y, ARGB.color(29, 185, 84), false);
            y += 10;
        }
    }

    @Override
    public int width() {
        return 176;
    }

    @Override
    public int height() {
        return 43;
    }

    @Override
    public int occcupiedSlotCount() {
        return Mth.positiveCeilDiv(this.height(), this.height());
    }
}
