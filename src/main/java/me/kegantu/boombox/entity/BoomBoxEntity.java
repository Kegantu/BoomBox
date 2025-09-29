package me.kegantu.boombox.entity;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.client.screen.BoomboxScreen;
import me.kegantu.boombox.init.ModEntities;
import me.kegantu.boombox.init.ModItems;
import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.soundsystem.MusicManager;
import me.kegantu.boombox.soundsystem.Sound;
import me.kegantu.boombox.utils.AudioDownloader;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BoomBoxEntity extends Entity {

    //private static final TrackedData<ItemStack> STACK = DataTracker.registerData(ItemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<Optional<UUID>> MUSIC_UUID = DataTracker.registerData(BoomBoxEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static Sound MUSIC;

    public BoomBoxEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public BoomBoxEntity(World world, Vec3d position) {
        super(ModEntities.BOOMBOX_ENTITY, world);
        this.setPosition(position);
        this.prevX = position.x;
        this.prevY = position.y;
        this.prevZ = position.z;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(MUSIC_UUID, Optional.empty());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public void tick() {
        super.tick();

        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();
        Vec3d vec3d = this.getVelocity();
        float f = this.getStandingEyeHeight() - 0.11111111F;
        if (this.isTouchingWater() && this.getFluidHeight(FluidTags.WATER) > f) {
            this.applyWaterBuoyancy();
        } else if (this.isInLava() && this.getFluidHeight(FluidTags.LAVA) > f) {
            this.applyLavaBuoyancy();
        } else if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
        }

        if (this.getWorld().isClient) {
            this.noClip = false;
        } else {
            this.noClip = !this.getWorld().isSpaceEmpty(this, this.getBoundingBox().contract(1.0E-7));
            if (this.noClip) {
                this.pushOutOfBlocks(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
            }
        }

        if (!this.isOnGround() || this.getVelocity().horizontalLengthSquared() > 1.0E-5F || (this.age + this.getId()) % 4 == 0) {
            this.move(MovementType.SELF, this.getVelocity());
            float g = 0.98F;
            if (this.isOnGround()) {
                g = this.getWorld().getBlockState(this.getVelocityAffectingPos()).getBlock().getSlipperiness() * 0.98F;
            }

            this.setVelocity(this.getVelocity().multiply(g, 0.98, g));
            if (this.isOnGround()) {
                Vec3d vec3d2 = this.getVelocity();
                if (vec3d2.y < 0.0) {
                    this.setVelocity(vec3d2.multiply(1.0, -0.5, 1.0));
                }
            }
        }

        this.velocityDirty = this.velocityDirty | this.updateWaterState();
        if (!this.getWorld().isClient) {
            double d = this.getVelocity().subtract(vec3d).lengthSquared();
            if (d > 0.01) {
                this.velocityDirty = true;
            }
        }
    }

    private void applyWaterBuoyancy() {
        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.x * 0.99F, vec3d.y + (vec3d.y < 0.06F ? 5.0E-4F : 0.0F), vec3d.z * 0.99F);
    }

    private void applyLavaBuoyancy() {
        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.x * 0.95F, vec3d.y + (vec3d.y < 0.06F ? 5.0E-4F : 0.0F), vec3d.z * 0.95F);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient()){
            if (player.isSneaking()){
                MinecraftClient.getInstance().setScreen(new BoomboxScreen(Text.literal("Ballin"), this));
                return ActionResult.SUCCESS;
            }
        }

        if (!this.getWorld().isClient()) {
            if (!player.isSneaking()){
                ItemStack itemStack = new ItemStack(ModItems.BOOMBOX);
                Item item = itemStack.getItem();
                int i = itemStack.getCount();
                if (!player.getInventory().insertStack(itemStack)) {
                    return ActionResult.PASS;
                }

                player.sendPickup(this, i);

                player.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), i);
                if (MUSIC != null){
                    MusicManager.getSound(this.dataTracker.get(MUSIC_UUID).get().toString());
                    MusicManager.remove(MUSIC);
                }
                this.discard();
            }
            //player.triggerItemPickedUpByEntityCriteria(this);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    public void downloadMusic(String youtubeLink, double volume){

        if (this.dataTracker.get(MUSIC_UUID).isEmpty()){
            BoomBox.LOGGER.info("MUSIC_UUID IS EMPTY");
        }

        if (this.dataTracker.get(MUSIC_UUID).isPresent()){
            BoomBox.LOGGER.info(this.dataTracker.get(MUSIC_UUID).get() + " downloadMusic id client");

            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(this.dataTracker.get(MUSIC_UUID).get().toString());
            ClientPlayNetworking.send(ModPackets.BOOMBOX_STOP_C2S, buf);
            MusicManager.getSound(this.dataTracker.get(MUSIC_UUID).get().toString()).stop();
            MusicManager.remove(this.dataTracker.get(MUSIC_UUID).get().toString());
        }

        //this.setMusicUUID(UUID.randomUUID());

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(youtubeLink);
        buf.writeFloat((float) volume);
        buf.writeVector3f(new Vector3f((float) this.getX(), (float) this.getY(), (float) this.getZ()));
        buf.writeString(UUID.randomUUID().toString());
        buf.writeInt(this.getId());

        ClientPlayNetworking.send(ModPackets.BOOMBOX_PLAY_C2S, buf);
        CompletableFuture<Path> futureFfmpeg = CompletableFuture.supplyAsync(() -> AudioDownloader.download(youtubeLink));
        futureFfmpeg.thenAccept(path -> this.playMusic(path, (float) volume, this.dataTracker.get(MUSIC_UUID).get()));
    }

    private void playMusic(Path outputFile, float volume, UUID uuid){
        MUSIC = new Sound(outputFile, this.getPos(), volume, uuid);
        MUSIC.play();
    }

    public void setMusicUUID(UUID uuid){
        this.dataTracker.set(MUSIC_UUID, Optional.of(uuid));
    }
}
