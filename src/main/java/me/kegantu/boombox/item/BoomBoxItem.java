package me.kegantu.boombox.item;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.soundsystem.Sound;
import me.kegantu.boombox.utils.AudioDownloader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.nio.file.Path;

public class BoomBoxItem extends Item {
    public BoomBoxItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient()){
            BoomBoxEntity boomBoxEntity = new BoomBoxEntity(world, user.getPos());
            boomBoxEntity.setYaw(user.getYaw());
            world.spawnEntity(boomBoxEntity);
        }

        if (world.isClient()){
            /*Path outputFile = AudioDownloader.download("https://youtu.be/SRWOVU5t6_M?si=gaDLCgr7fV-Yvfm1", world);
            Sound sound = new Sound(outputFile, user.getPos());
            sound.play();*/
        }

        /*MinecraftClient client = MinecraftClient.getInstance();
        double d = client.gameRenderer.getCamera().getPos().squaredDistanceTo(user.getX(), user.getY(), user.getZ());
        PositionedSoundInstance positionedSoundInstance = new PositionedSoundInstance(BoomBox.TEST, SoundCategory.PLAYERS, 1, 1, Random.create(0), user.getX(), user.getY(), user.getZ());
        for (var item : client.getSoundManager().sounds.keySet()) {
            client.player.sendMessage(Text.literal(item.getNamespace() + " " + item.getPath()));
        }
        var soundSet = new WeightedSoundSet(null, null);
        soundSet.add(new Sound("boombox:test", ConstantFloatProvider.create(1f), ConstantFloatProvider.create(1f), 1,
                Sound.RegistrationType.FILE, false, false, 16));
        client.getSoundManager().sounds.put(BoomBox.TEST_IDENTIFIER, soundSet);
        client.getSoundManager().play(positionedSoundInstance);*/
        //world.getServer().getSavePath(WorldSavePath.ROOT);
        return TypedActionResult.success(stack, true);
    }
}
