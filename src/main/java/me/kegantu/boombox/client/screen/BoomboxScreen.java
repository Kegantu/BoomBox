package me.kegantu.boombox.client.screen;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.init.ModPackets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class BoomboxScreen extends Screen {
    private Button confirm;
    private Button back;
    private EditBox musicLink;
    private OptionInstance<Double> boomboxVolume;

    private String youtubeLink = "";
    private double volume = 1d;
    private BoomBoxEntity entity;

    public BoomboxScreen(Component title, BoomBoxEntity entity) {
        super(title);
        this.entity = entity;
    }

    @Override
    protected void init() {
        confirm = Button.builder(Component.literal("Update Link"), button -> {
            if (youtubeLink.isEmpty()){
                this.minecraft.player.displayClientMessage(Component.literal("Please put your youtube link"), true);
                this.minecraft.setScreen(null);
                return;
            }

            entity.downloadMusic(youtubeLink, volume, minecraft.player.getUUID());
            this.minecraft.player.displayClientMessage(Component.literal("Downloading an audio..."), true);
            this.minecraft.setScreen(null);
        }).bounds((this.width + 46) / 2, this.height / 4 + 110, 130, 20).build();
        back = Button.builder(Component.literal("Back"), button -> {
                    this.minecraft.setScreen(null);
                }).bounds((this.width - 301) / 2, this.height / 4 + 110, 130, 20).build();

        musicLink = new EditBox(this.font, (this.width - 300) / 2, this.height / 4 + 60, 300, 20, Component.literal("Paste link here"));
        musicLink.setMaxLength(128);
        musicLink.setResponder(s -> {
            youtubeLink = s;
        });
        boomboxVolume = new OptionInstance<Double>("options.boombox.volume", OptionInstance.noTooltip(),
                (optionText, value) -> value == 0.0 ? this.minecraft.options.genericValueLabel(optionText, CommonComponents.OPTION_OFF) : this.getPercentValueText(optionText, value),
                OptionInstance.UnitDouble.INSTANCE, 1d, aFloat -> {
            volume = aFloat;
            entity.setVolumeClient((float) volume);
        });
        boomboxVolume.set(entity.getVolume() != 1.0 ? entity.getVolume() : 1.0d);

        addRenderableWidget(confirm);
        addRenderableWidget(back);
        addRenderableWidget(musicLink);
        addRenderableWidget(boomboxVolume.createButton(this.minecraft.options, (this.width - 300) / 2, this.height / 4 + 10, 300));
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (input.key() == GLFW.GLFW_KEY_ESCAPE)
            Minecraft.getInstance().setScreen(null);
        return super.keyPressed(input);
    }

    private Component getPercentValueText(Component prefix, double value) {
        return Component.translatable("options.percent_value", prefix, (int)(value * 100.0));
    }
}
