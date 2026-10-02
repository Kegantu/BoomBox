package me.kegantu.boombox.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kegantu.boombox.BoomBox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class NotificationToast implements Toast {

    private final Identifier NOTIFICATIONS_TEXTURE = Identifier.of(BoomBox.MOD_ID, "toast/notifications");
    private final String soundtrackName;
    private float rotation;
    private Toast.Visibility visibility = Toast.Visibility.HIDE;

    public NotificationToast(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    @Override
    public Visibility getVisibility() {
        return this.visibility;
    }

    @Override
    public void update(ToastManager manager, long time) {
        this.visibility = time >= 7500.0 * manager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    @Override
    public void draw(DrawContext context, TextRenderer textRenderer, long startTime) {
        MatrixStack stack = context.getMatrices();
        rotation += 0.5f * MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);
        if (rotation >= 360f){
            rotation = 0;
        }

        var delta = rotation / 360;
        RenderSystem.enableBlend();
        context.drawGuiTexture(RenderLayer::getGuiTextured, NOTIFICATIONS_TEXTURE, 256, 256, 0,0,0,0, 176, 43);
        stack.push();
        stack.translate(4.5f, 5f, 0f);
        stack.scale(1.6f,1.6f,1.6f);
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(delta, 0f, 360f)), 10f, 10f,0f);
        context.drawGuiTexture(RenderLayer::getGuiTextured, NOTIFICATIONS_TEXTURE, 256, 256, 177,2,0,0, 20, 20);
        stack.pop();

        var textLine = textRenderer.wrapLines(StringVisitable.plain(soundtrackName), 140);
        int y = 7;
        for (int i = 0; i < textLine.size(); i++) {
            context.drawText(textRenderer, textLine.get(i), 40, y, 1947988, false);
            y += 10;
        }
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 43;
    }

    @Override
    public int getRequiredSpaceCount() {
        return MathHelper.ceilDiv(this.getHeight(), this.getHeight());
    }
}
