package me.kegantu.boombox.client.screen;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.entity.BoomBoxEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class BoomboxScreen extends Screen {
    private ButtonWidget confirm;
    private TextFieldWidget musicLink;
    private SimpleOption<Double> boomboxVolume;

    private String youtubeLink;
    private double volume = 1d;
    private BoomBoxEntity entity;

    public BoomboxScreen(Text title, BoomBoxEntity entity) {
        super(title);
        this.entity = entity;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        confirm = ButtonWidget.builder(Text.literal("Confirm"), button -> {
            entity.downloadMusic(youtubeLink, volume);
            this.client.player.sendMessage(Text.literal("Downloading audio..."), true);
            this.client.setScreen(null);
        }).dimensions(this.width / 2 - 75, 240, 300, 20).build();
        musicLink = new TextFieldWidget(this.textRenderer, this.width / 2 - 75, 160, 300, 20, Text.literal("diddy"));
        musicLink.setChangedListener(s -> {
            youtubeLink = s;
        });
        boomboxVolume = new SimpleOption<Double>("options.boombox.volume", SimpleOption.emptyTooltip(),
                (optionText, value) -> value == 0.0 ? this.client.options.getGenericValueText(optionText, ScreenTexts.OFF) : getPercentValueText(optionText, value),
                SimpleOption.DoubleSliderCallbacks.INSTANCE, 1d, aFloat -> volume = aFloat);


        addDrawableChild(confirm);
        addDrawableChild(musicLink);
        addDrawableChild(boomboxVolume.createWidget(this.client.options, this.width / 2 - 75, 80, 300));
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (i == GLFW.GLFW_KEY_ESCAPE)
            MinecraftClient.getInstance().setScreen(null);
        return super.keyPressed(i, j, k);
    }

    private static Text getPercentValueText(Text prefix, double value) {
        return Text.translatable("options.percent_value", prefix, (int)(value * 100.0));
    }
}
