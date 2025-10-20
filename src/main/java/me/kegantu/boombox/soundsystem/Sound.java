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
    //private AudioInputStream inputStream;
    private float volume;
    //private final UUID soundUUID;
    //private ByteBuffer bytePcm;

    public Sound(Path soundPath, Vec3d position, double volume, UUID uuid){
        //soundUUID = uuid;
        MusicManager.addMusic(uuid.toString(), this);

        this.volume = (float) volume;
        //ByteArrayOutputStream pcmByteBuffer = new ByteArrayOutputStream();


        /*int totalFramesRead = 0;
        float[] samples;
        try {
            inputStream = AudioSystem.getAudioInputStream(soundPath.toFile());
            int bytesPerFrame =
                    inputStream.getFormat().getFrameSize();
            int numBytes = 1024 * bytesPerFrame;
            byte[] audioBytes = new byte[numBytes];
            int numBytesRead = 0;
            int numFramesRead = 0;
            samples = new float[audioBytes.length];

            int   bitsPerSample = inputStream.getFormat().getSampleSizeInBits();
            int  bytesPerSample = (int) ceil(bitsPerSample / 8.0);;
            double    fullScale = pow(2.0, bitsPerSample - 1);;

            int s = 0;
            while ((numBytesRead  = inputStream.read(audioBytes)) != -1) {

                if (s == 2048){
                    continue;
                }

                numFramesRead = numBytesRead / bytesPerFrame;
                totalFramesRead += numFramesRead;
                samples[s] = audioBytes[s];
                //pcmByteBuffer.write(audioBytes, 0, numBytesRead);
                s++;
            }
           // pcmByteBuffer.close();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }*/

        //bytePcm = ByteBuffer.wrap(pcmByteBuffer.toByteArray());
        //bytePcm.order(ByteOrder.LITTLE_ENDIAN);

        /*int[] samples = null;
        try {
            //File wavFile = new File(filePath);
            inputStream = AudioSystem.getAudioInputStream(soundPath.toFile());
            AudioFormat format = inputStream.getFormat();

            byte[] audioBytes = inputStream.readAllBytes();

            // Convert bytes to PCM samples (assuming 16-bit signed PCM little endian)

            if (format.getSampleSizeInBits() == 16) {
                int numSamples = audioBytes.length / 2;
                samples = new int[numSamples];

                for (int i = 0, s = 0; i < audioBytes.length; i += 2, s++) {
                    int low = audioBytes[i] & 0xFF;
                    int high = audioBytes[i + 1]; // signed byte
                    samples[s] = (high << 8) | low;
                }
            } else if (format.getSampleSizeInBits() == 8) {
                int numSamples = audioBytes.length;
                samples = new int[numSamples];

                for (int i = 0; i < audioBytes.length; i++) {
                    samples[i] = audioBytes[i];
                }
            } else {
                throw new UnsupportedAudioFileException("Only 8-bit or 16-bit PCM supported in this example.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        System.out.println(1);
        System.out.println(341);
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

        var sampleRate = sampleRateBuffer.get();
        MemoryStack.stackPop();
        MemoryStack.stackPop();
        /*float[] samples;
        try {
            inputStream = AudioSystem.getAudioInputStream(soundPath.toFile());
            int   bitsPerSample = inputStream.getFormat().getSampleSizeInBits();
            int  bytesPerSample = (int) ceil(bitsPerSample / 8.0);;
            double    fullScale = pow(2.0, bitsPerSample - 1);;
            int bytesPerFrame =
                    inputStream.getFormat().getFrameSize();
            int numBytes = 1024 * bytesPerFrame;
            byte[] audioBytes = new byte[numBytes];
            int blen = inputStream.read(audioBytes);
            samples = new float[audioBytes.length];

            int i = 0;
            int s = 0;
            while (i < blen) {
                long temp = (
                        (audioBytes[i    ] & 0xffL)
                                | ((audioBytes[i + 1] & 0xffL) << 8)
                );
                float sample = 0f;

                int bitsToExtend = Long.SIZE - bitsPerSample;
                temp = (temp << bitsToExtend) >> bitsToExtend;
                sample = (float) (temp / fullScale);

                samples[s] = sample;

                i += bytesPerSample;
                s++;
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }*/

        soundBufferID = AL10.alGenBuffers();
        BoomBox.LOGGER.info(String.valueOf(AL10.alGetError()));
        AL10.alBufferData(soundBufferID, AL10.AL_FORMAT_MONO16, pcmShortBuffer, sampleRate);
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

    /*public Sound(short[] pcmMono, int sampleRate, Vec3d position, float volume){
        //this.pcmMono = pcmMono;
        MusicManager.addMusic(UUID.randomUUID().toString(), this);

        soundBufferID = AL10.alGenBuffers();
        AL10.alBufferData(soundBufferID, AL10.AL_FORMAT_MONO16, pcmMono, sampleRate);

        soundSourceID = AL10.alGenSources();
        AL10.alSourcei(soundSourceID, AL10.AL_BUFFER, soundBufferID);

        AL10.alSourcei(soundSourceID, AL10.AL_DISTANCE_MODEL, EXTLinearDistance.AL_LINEAR_DISTANCE);
        AL10.alSourcef(soundSourceID, AL10.AL_MAX_DISTANCE, 16);
        AL10.alSourcef(soundSourceID, AL10.AL_ROLLOFF_FACTOR, 1.0F);
        AL10.alSourcef(soundSourceID, AL10.AL_REFERENCE_DISTANCE, 0.0F);

        AL10.alSource3f(soundSourceID, AL10.AL_POSITION, (float) position.x, (float) position.y, (float) position.z);
        AL10.alSource3f(soundSourceID, AL10.AL_VELOCITY, 0f, 0f, 0f);
        AL10.alSourcef(soundSourceID, AL10.AL_PITCH, 1f);
        AL10.alSourcef(soundSourceID, AL10.AL_GAIN, volume);
    }*/

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

    /*public UUID getSoundUUID() {
        return soundUUID;
    }*/

    /*public int getSampleRate(){
        return sampleRate;
    }

    public int[] getPcmMonoIntData(){
        int[] pcmInt = new int[pcmMono.length];

        for (int i = 0; i < pcmInt.length; i++) {
            pcmInt[i] = pcmMono[i];
        }

        return pcmInt;
    }*/
}
