package me.kegantu.boombox.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kegantu.boombox.BoomBox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class NotificationToast implements Toast {

    private final Identifier NOTIFICATIONS_TEXTURE = new Identifier(BoomBox.MOD_ID, "textures/gui/notifications.png");
    private final String soundtrackName;
    private float rotation;

    public NotificationToast(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    @Override
    public Visibility draw(DrawContext context, ToastManager manager, long startTime) {
        MatrixStack stack = context.getMatrices();
        rotation += 0.5f * MinecraftClient.getInstance().getTickDelta();
        if (rotation >= 360f){
            rotation = 0;
        }

        var delta = rotation / 360;
        RenderSystem.enableBlend();
        context.drawTexture(NOTIFICATIONS_TEXTURE, 0,0,0,0, 176, 43);
        stack.push();
        stack.translate(4.5f, 5f, 0f);
        stack.scale(1.6f,1.6f,1.6f);
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(delta, 0f, 360f)), 10f, 10f,0f);
        context.drawTexture(NOTIFICATIONS_TEXTURE, 0,0,177,2, 20, 20);
        stack.pop();

        var textLine = manager.getClient().textRenderer.wrapLines(StringVisitable.plain(soundtrackName), 140);
        int y = 7;
        for (int i = 0; i < textLine.size(); i++) {
            context.drawText(manager.getClient().textRenderer, textLine.get(i), 40, y, 1947988, false);
            y += 10;
        }

        return startTime >= 7500.0 * manager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
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
