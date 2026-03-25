package me.kegantu.boombox.soundsystem;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.init.ModPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTLinearDistance;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Sound {

    protected int soundBufferID;
    protected int soundSourceID;
    protected float volume;
    protected int monoSamplesSize;
    protected int sampleRate;
    protected float length;
    private ItemStack tiedStack;
    private UUID assignUUID;
    private UUID playerUUID;

    public Sound(Path soundPath, Vec3d position, double volume, UUID uuid){
        MusicManager.addMusic(uuid.toString(), this);
        this.assignUUID = uuid;
        this.volume = (float) volume;

        MemoryStack.stackPush();
        IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);
        MemoryStack.stackPush();
        IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);

        ShortBuffer pcmShortBuffer = STBVorbis.stb_vorbis_decode_filename(soundPath.toString(), channelsBuffer, sampleRateBuffer);
        if (pcmShortBuffer == null) {
            System.out.println("could not load sound " + soundPath);
            MemoryStack.stackPop();
            MemoryStack.stackPop();
            return;
        }

        int stereoSamples = pcmShortBuffer.remaining() / 2;
        short[] mono = new short[stereoSamples];

        for (int i = 0; i < stereoSamples; i++) {
            short left  = pcmShortBuffer.get();
            short right = pcmShortBuffer.get();
            mono[i] = (short)((left + right) / 2);
        }

        var sampleRate = sampleRateBuffer.get();
        MemoryStack.stackPop();
        MemoryStack.stackPop();

        monoSamplesSize = mono.length;
        this.sampleRate = sampleRate;
        length = (float) mono.length / sampleRate;

        soundBufferID = AL10.alGenBuffers();
        BoomBox.LOGGER.info(String.valueOf(AL10.alGetError()));
        AL10.alBufferData(soundBufferID, AL10.AL_FORMAT_MONO16, mono, sampleRate);
        BoomBox.LOGGER.info(AL10.alGetError() + " bufferData");

        soundSourceID = AL10.alGenSources();
        BoomBox.LOGGER.info(AL10.alGetError() + " genSources");

        AL10.alSourcei(soundSourceID, AL10.AL_BUFFER, soundBufferID);
        BoomBox.LOGGER.info(AL10.alGetError() + " assignBuffer");

        AL10.alSourcei(soundSourceID, AL10.AL_DISTANCE_MODEL, EXTLinearDistance.AL_LINEAR_DISTANCE);
        AL10.alSourcef(soundSourceID, AL10.AL_MAX_DISTANCE, 16);
        AL10.alSourcef(soundSourceID, AL10.AL_ROLLOFF_FACTOR, 1.0F);
        AL10.alSourcef(soundSourceID, AL10.AL_REFERENCE_DISTANCE, 0.0F);
        BoomBox.LOGGER.info(AL10.alGetError() + " 3d sound");

        AL10.alSource3f(soundSourceID, AL10.AL_POSITION, (float) position.x, (float) position.y, (float) position.z);
        AL10.alSource3f(soundSourceID, AL10.AL_VELOCITY, 0f, 0f, 0f);
        AL10.alSourcef(soundSourceID, AL10.AL_PITCH, 1f);
        AL10.alSourcef(soundSourceID, AL10.AL_GAIN, this.volume);
        BoomBox.LOGGER.info(String.valueOf(volume));
        BoomBox.LOGGER.info(String.valueOf(position));
        BoomBox.LOGGER.info(AL10.alGetError() + " default settings");
    }

    public void setPosition(Vector3f position){
        AL10.alSource3f(soundSourceID, AL10.AL_POSITION, position.x, position.y, position.z);
    }

    public void play(){
        AL10.alSourcePlay(soundSourceID);
    }

    public void stop(){
        AL10.alSourceStop(soundSourceID);

        if (!tiedStack.isEmpty()){
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeItemStack(tiedStack);
            buf.writeString(assignUUID.toString());

            PacketByteBuf boomboxStopBuf = PacketByteBufs.copy(buf);
            buf.writeUuid(playerUUID);

            ClientPlayNetworking.send(ModPackets.CLEAN_ITEMSTACK_C2S, buf);
            ClientPlayNetworking.send(ModPackets.BOOMBOX_STOP_C2S, boomboxStopBuf);
        }
    }

    public void pause(){
        AL10.alSourcePause(soundSourceID);
    }

    public void resume(){
        if (AL10.alGetSourcei(soundSourceID, AL10.AL_SOURCE_STATE) != AL10.AL_PAUSED){
            return;
        }

        AL10.alSourcePlay(soundSourceID);
    }

    public boolean isPlaying(){
        return AL10.alGetSourcei(soundSourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public void setStack(ItemStack stack, UUID playerUUID){
        this.tiedStack = stack;
        this.playerUUID = playerUUID;

        BoomBox.LOGGER.info(String.valueOf(tiedStack));
        float leftPlayback = length - AL11.alGetSourcef(soundSourceID, AL11.AL_SEC_OFFSET);
        ScheduledExecutorService scheduleService = Executors.newSingleThreadScheduledExecutor();
        scheduleService.schedule(this::stop, (long) leftPlayback, TimeUnit.SECONDS);
    }
}