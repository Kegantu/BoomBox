package me.kegantu.boombox.entity;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.client.screen.BoomboxScreen;
import me.kegantu.boombox.init.*;
import me.kegantu.boombox.item.components.BoomboxComponentDataType;
import me.kegantu.boombox.networking.payloads.C2S.BoomboxPlayC2SPayload;
import me.kegantu.boombox.networking.payloads.C2S.BoomboxStopC2SPayload;
import me.kegantu.boombox.networking.payloads.C2S.UpdateVolumeC2SPayload;
import me.kegantu.boombox.soundsystem.MusicManager;
import me.kegantu.boombox.soundsystem.Sound;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.UUID;

public class BoomBoxEntity extends Entity {

    private static final EntityDataAccessor<Optional<Component>> MUSIC_UUID = SynchedEntityData.defineId(BoomBoxEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
    private static final EntityDataAccessor<Float> VOLUME = SynchedEntityData.defineId(BoomBoxEntity.class, EntityDataSerializers.FLOAT);
    private static Sound MUSIC;
    private int timePassed;

    public BoomBoxEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    public BoomBoxEntity(Level world, Vec3 position) {
        super(ModEntities.BOOMBOX_ENTITY, world);
        this.setPos(position);
        this.xo = position.x;
        this.yo = position.y;
        this.zo = position.z;
    }

    public BoomBoxEntity(Level world, Vec3 position, UUID musicUUID) {
        super(ModEntities.BOOMBOX_ENTITY, world);
        this.setPos(position);
        this.xo = position.x;
        this.yo = position.y;
        this.zo = position.z;
        this.entityData.set(MUSIC_UUID, Optional.of(Component.translationArg(musicUUID)));
        this.entityData.set(VOLUME, 1f);
    }

    public BoomBoxEntity(Level world, Vec3 position, UUID musicUUID, float volume) {
        super(ModEntities.BOOMBOX_ENTITY, world);
        this.setPos(position);
        this.xo = position.x;
        this.yo = position.y;
        this.zo = position.z;
        this.entityData.set(MUSIC_UUID, Optional.of(Component.translationArg(musicUUID)));
        this.entityData.set(VOLUME, volume);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(MUSIC_UUID, Optional.empty());
        builder.define(VOLUME, 1f);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        this.entityData.set(VOLUME, view.getFloatOr("Volume", 1f));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        view.putFloat("Volume", this.entityData.get(VOLUME));
    }

    @Override
    public void tick() {
        super.tick();
        this.timePassed++;

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        Vec3 vec3d = this.getDeltaMovement();
        float f = this.getEyeHeight() - 0.11111111F;
        if (this.isInWater() && this.getFluidHeight(FluidTags.WATER) > f) {
            this.applyWaterBuoyancy();
        } else if (this.isInLava() && this.getFluidHeight(FluidTags.LAVA) > f) {
            this.applyLavaBuoyancy();
        } else if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }

        if (this.level().isClientSide()) {
            this.noPhysics = false;
        } else {
            this.noPhysics = !this.level().noCollision(this, this.getBoundingBox().deflate(1.0E-7));
            if (this.noPhysics) {
                this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
            }
        }

        if (!this.onGround() || this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
            this.move(MoverType.SELF, this.getDeltaMovement());
            float g = 0.98F;
            if (this.onGround()) {
                g = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
            }

            this.setDeltaMovement(this.getDeltaMovement().multiply(g, 0.98, g));
            if (this.onGround()) {
                Vec3 vec3d2 = this.getDeltaMovement();
                if (vec3d2.y < 0.0) {
                    this.setDeltaMovement(vec3d2.multiply(1.0, -0.5, 1.0));
                }
            }
        }

        this.needsSync = this.needsSync | this.updateInWaterStateAndDoFluidPushing();
        if (!this.level().isClientSide()) {
            double d = this.getDeltaMovement().subtract(vec3d).lengthSqr();
            if (d > 0.01) {
                this.needsSync = true;
            }
        }

        if (!this.isPlaying()){
            return;
        }

        if (this.timePassed < 20){
            return;
        }

        this.timePassed = 0;
        if (this.level() instanceof ServerLevel serverWorld){
            serverWorld.sendParticles(ModParticles.BOOMBOX_NOTE, this.getX(), this.getY() + 0.25f, this.getZ(), 1, 0f,0f,0f, 0f);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        return false;
    }

    private void applyWaterBuoyancy() {
        Vec3 vec3d = this.getDeltaMovement();
        this.setDeltaMovement(vec3d.x * 0.99F, vec3d.y + (vec3d.y < 0.06F ? 5.0E-4F : 0.0F), vec3d.z * 0.99F);
    }

    private void applyLavaBuoyancy() {
        Vec3 vec3d = this.getDeltaMovement();
        this.setDeltaMovement(vec3d.x * 0.95F, vec3d.y + (vec3d.y < 0.06F ? 5.0E-4F : 0.0F), vec3d.z * 0.95F);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.level().isClientSide()){
            if (player.isShiftKeyDown()){
                Minecraft.getInstance().setScreen(new BoomboxScreen(Component.literal("Boombox Screen"), this));
                return InteractionResult.SUCCESS;
            }
        }

        if (!this.level().isClientSide()) {
            if (!player.isShiftKeyDown()){
                if (this.entityData.get(MUSIC_UUID).isPresent() && !this.isPlaying()){
                    return InteractionResult.PASS;
                }
                ItemStack itemStack = new ItemStack(ModItems.BOOMBOX);
                if (this.entityData.get(MUSIC_UUID).isPresent()){
                    itemStack.set(ModComponents.BOOMBOX_COMPONENT, new BoomboxComponentDataType(this.entityData.get(MUSIC_UUID).get().tryCollapseToString(), this.entityData.get(VOLUME)));
                }
                Item item = itemStack.getItem();
                int i = itemStack.getCount();
                if (player.getMainHandItem().isEmpty()){
                    if (!player.getInventory().add(itemStack)) {
                        return InteractionResult.PASS;
                    }
                } else {
                    if (!player.getInventory().add(40, itemStack)) {
                        return InteractionResult.PASS;
                    }
                }

                player.take(this, i);

                player.awardStat(Stats.ITEM_PICKED_UP.get(item), i);
                if (MUSIC != null){
                    MusicManager.getSound(this.entityData.get(MUSIC_UUID).get().tryCollapseToString());
                    MusicManager.remove(MUSIC);
                }
                this.discard();
            }
            //player.triggerItemPickedUpByEntityCriteria(this);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public void downloadMusic(String youtubeLink, double volume, UUID musicOwner){

        if (this.entityData.get(MUSIC_UUID).isPresent()){
            BoomboxStopC2SPayload payload = new BoomboxStopC2SPayload(this.entityData.get(MUSIC_UUID).get().tryCollapseToString());
            ClientPlayNetworking.send(payload);

            if (MusicManager.getSound(this.entityData.get(MUSIC_UUID).get().tryCollapseToString()) == null){
                MusicManager.getSound(this.entityData.get(MUSIC_UUID).get().tryCollapseToString()).stop();
                MusicManager.remove(this.entityData.get(MUSIC_UUID).get().tryCollapseToString());
            }
        }

        BoomboxPlayC2SPayload payload = new BoomboxPlayC2SPayload(youtubeLink, this.entityData.get(VOLUME),
                new Vector3f((float) this.getX(), (float) this.getY(), (float) this.getZ()), UUID.randomUUID().toString(), this.getId(), musicOwner.toString());

        ClientPlayNetworking.send(payload);
    }

    public void setMusicUUID(UUID uuid){
        this.entityData.set(MUSIC_UUID, Optional.of(Component.translationArg(uuid)));
    }

    public void setVolumeClient(float volume){
        this.entityData.set(VOLUME, volume);

        if (this.entityData.get(MUSIC_UUID).isEmpty()){
            return;
        }

        if (MusicManager.getSound(this.entityData.get(MUSIC_UUID).get().tryCollapseToString()) == null){
            return;
        }

        UpdateVolumeC2SPayload payload = new UpdateVolumeC2SPayload(volume, this.getId(), this.entityData.get(MUSIC_UUID).get().tryCollapseToString());
        ClientPlayNetworking.send(payload);
    }

    public void setVolumeServer(float volume){
        this.entityData.set(VOLUME, volume);
    }

    public float getVolume(){
        return this.entityData.get(VOLUME);
    }

    public boolean isPlaying(){
        if (this.entityData.get(MUSIC_UUID).isEmpty()){
            return false;
        }

        if (MusicManager.getSound(this.entityData.get(MUSIC_UUID).get().tryCollapseToString()) == null){
            return false;
        }

        return MusicManager.getSound(this.entityData.get(MUSIC_UUID).get().tryCollapseToString()).isPlaying();
    }
}
