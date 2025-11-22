package me.kegantu.boombox.client.hud;

import me.kegantu.boombox.BoomBox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class NotificationToast implements Toast {

    private final Identifier NOTIFICATIONS_TEXTURE = new Identifier(BoomBox.MOD_ID, "textures/gui/notifications40v2.png");
    private final String soundtrackName;

    public NotificationToast(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    @Override
    public Visibility draw(DrawContext context, ToastManager manager, long startTime) {
        MatrixStack stack = context.getMatrices();
        context.drawTexture(NOTIFICATIONS_TEXTURE, 0,0,0,0, this.getWidth(), this.getHeight());

        //stack.push();
        //stack.translate(30f,7f, 0f);
        //stack.scale(0.75f, 0.75f, 0.75f);
        var textLine = manager.getClient().textRenderer.wrapLines(StringVisitable.plain(soundtrackName), 145);
        int y = 7;
        for (int i = 0; i < textLine.size(); i++) {
            context.drawText(manager.getClient().textRenderer, textLine.get(i), 34, y, 1947988, false);
            y += 10;
        }
        //stack.pop();

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
