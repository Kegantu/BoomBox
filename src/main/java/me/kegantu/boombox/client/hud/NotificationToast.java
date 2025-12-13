package me.kegantu.boombox.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kegantu.boombox.BoomBox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

    private final Identifier NOTIFICATIONS_TEXTURE = new Identifier(BoomBox.MOD_ID, "textures/gui/notifications40v3.png");
    //private final Identifier MUSIC_DISC_TEXTURE = new Identifier(BoomBox.MOD_ID, "textures/gui/musicdisc.png");
    private final String soundtrackName;

    public NotificationToast(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    @Override
    public Visibility draw(DrawContext context, ToastManager manager, long startTime) {
        MatrixStack stack = context.getMatrices();
        RenderSystem.enableBlend();
        context.drawTexture(NOTIFICATIONS_TEXTURE, 0,0,0,0, this.getWidth(), this.getHeight());
        stack.push();
        stack.translate(10,20,0f);
        stack.scale(1.6f,1.6f,1.6f);
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(0.5f, 0f, 180f)));
        context.drawTexture(NOTIFICATIONS_TEXTURE, 0,0,177,2, 20, 20);
        stack.pop();

        var textLine = manager.getClient().textRenderer.wrapLines(StringVisitable.plain(soundtrackName), 145);
        int y = 7;
        for (int i = 0; i < textLine.size(); i++) {
            context.drawText(manager.getClient().textRenderer, textLine.get(i), 34, y, 1947988, false);
            y += 10;
        }
        //context.drawTexture(MUSIC_DISC_TEXTURE, 34, 40, 0, 0, 18, 18);

        return startTime >= 7500.0 * manager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 42;
    }
}
