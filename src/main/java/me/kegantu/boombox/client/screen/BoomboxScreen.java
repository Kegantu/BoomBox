package me.kegantu.boombox.client.screen;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.init.ModPackets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class BoomboxScreen extends Screen {
    private ButtonWidget confirm;
    private TextFieldWidget musicLink;
    private SimpleOption<Double> boomboxVolume;

    private String youtubeLink = "";
    private double volume = 1d;
    private BoomBoxEntity entity;

    public BoomboxScreen(Text title, BoomBoxEntity entity) {
        super(title);
        this.entity = entity;
    }

    @Override
    protected void init() {
        confirm = ButtonWidget.builder(Text.literal("Confirm"), button -> {
            if (youtubeLink.isEmpty()){
                this.client.player.sendMessage(Text.literal("Please put your youtube link"), true);
                this.client.setScreen(null);
                return;
            }

            entity.downloadMusic(youtubeLink, volume, client.player.getUuid());
            this.client.player.sendMessage(Text.literal("Downloading an audio..."), true);
            this.client.setScreen(null);
        }).dimensions((this.width - 300) / 2, this.height / 4 + 110, 300, 20).build();
        musicLink = new TextFieldWidget(this.textRenderer, (this.width - 300) / 2, this.height / 4 + 60, 300, 20, Text.literal("Paste link here"));
        musicLink.setMaxLength(128);
        musicLink.setChangedListener(s -> {
            youtubeLink = s;
        });
        boomboxVolume = new SimpleOption<Double>("options.boombox.volume", SimpleOption.emptyTooltip(),
                (optionText, value) -> value == 0.0 ? this.client.options.getGenericValueText(optionText, ScreenTexts.OFF) : this.getPercentValueText(optionText, value),
                SimpleOption.DoubleSliderCallbacks.INSTANCE, 1d, aFloat -> volume = aFloat);

        addDrawableChild(confirm);
        addDrawableChild(musicLink);
        addDrawableChild(boomboxVolume.createWidget(this.client.options, (this.width - 300) / 2, this.height / 4 + 10, 300));
        boomboxVolume.setValue(entity.getVolume() != 1.0 ? entity.getVolume() : 1.0d);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (i == GLFW.GLFW_KEY_ESCAPE)
            MinecraftClient.getInstance().setScreen(null);
        return super.keyPressed(i, j, k);
    }

    private Text getPercentValueText(Text prefix, double value) {
        return Text.translatable("options.percent_value", prefix, (int)(value * 100.0));
    }
}
