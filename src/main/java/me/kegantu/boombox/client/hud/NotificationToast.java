package me.kegantu.boombox.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kegantu.boombox.BoomBox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3x2fStack;

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
        Matrix3x2fStack stack = context.getMatrices();
        rotation += 0.5f * MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(true);
        if (rotation >= 360f){
            rotation = 0;
        }

        var delta = rotation / 360;
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, NOTIFICATIONS_TEXTURE, 256, 256, 0,0,0,0, 176, 43);
        stack.pushMatrix();
        stack.translate(4.5f, 5f);
        stack.scale(1.6f,1.6f);
        stack.rotateAbout(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(delta, 0f, 360f)).angle(), 10, 10);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, NOTIFICATIONS_TEXTURE, 256, 256, 177,2,0,0, 20, 20);
        stack.popMatrix();

        var textLine = textRenderer.wrapLines(StringVisitable.plain(soundtrackName), 140);
        int y = 7;
        for (int i = 0; i < textLine.size(); i++) {
            context.drawText(textRenderer, textLine.get(i), 40, y, ColorHelper.getArgb(29, 185, 84), false);
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
