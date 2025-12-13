package me.kegantu.boombox.soundsystem;

import me.kegantu.boombox.BoomBox;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.EXTLinearDistance;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Path;
import java.util.UUID;

public class Sound {

    private int soundBufferID;
    private int soundSourceID;
    private float volume;

    public Sound(Path soundPath, Vec3d position, double volume, UUID uuid){
        MusicManager.addMusic(uuid.toString(), this);
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

    public void play(){
        AL10.alSourcePlay(soundSourceID);
    }

    public void stop(){
        AL10.alSourceStop(soundSourceID);
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
}