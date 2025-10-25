package me.kegantu.boombox.utils;

import com.github.felipeucelli.javatube.StreamQuery;
import com.github.felipeucelli.javatube.Youtube;
import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.entity.BoomBoxEntity;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;
import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Executable;
import java.nio.ShortBuffer;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class AudioDownloader {

    private static final AudioDownloaderExecutor AUDIO_DOWNLOADER_EXECUTOR = new AudioDownloaderExecutor("Audio Downloader Executor");

    public static Path download(String youtubeURL) {
        //Path savePath = world.getServer().getSavePath(WorldSavePath.ROOT);
        //StringBuilder sb = new StringBuilder(savePath.toString());
        //File saveDirectory = new File(sb.deleteCharAt(savePath.toString().length() - 1) + "music");
        File saveDirectory = new File(FabricLoader.getInstance().getGameDir() + "\\music");

        if (!saveDirectory.exists()){
            saveDirectory.mkdirs();
        }

        /*FFmpegFrameGrabber frameGrabber = null;
        FFmpegFrameRecorder frameRecorder = null;*/

        try {
            Youtube youtubeVideo = new Youtube(youtubeURL);

            youtubeVideo.streams().filter(StreamQuery.Filter.builder().type("audio").build()).getFirst().download(saveDirectory + "\\", "musicD");

            String[] command = {FFmpegDownloader.FFMPEG_LOCATION, "-y", "-i", saveDirectory + "\\musicD.mp4", "-ac 1", saveDirectory + "\\output.ogg"};
            ProcessBuilder ffmpegBuilder = new ProcessBuilder(command);
            ffmpegBuilder.redirectErrorStream(true);
            Process ffmpeg = ffmpegBuilder.start();

            Thread outputReaderThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(ffmpeg.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        BoomBox.LOGGER.info("FFmpeg Output: {}", line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputReaderThread.start();

            ffmpeg.waitFor(120, TimeUnit.SECONDS);
            outputReaderThread.join();

            //ffmpeg.getErrorStream().transferTo(System.out);

           /* AUDIO_DOWNLOADER_EXECUTOR.execute(() -> {
                try {

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
*/
            /*frameGrabber = new FFmpegFrameGrabber(saveDirectory + "\\musicD.mp4");
            //frameGrabber.setAudioChannels(1);
            frameGrabber.start();

            frameRecorder = new FFmpegFrameRecorder(saveDirectory + "\\output.ogg", frameGrabber.getAudioChannels());
            frameRecorder.setAudioCodec(avcodec.AV_CODEC_ID_VORBIS);
            frameRecorder.setAudioBitrate(frameGrabber.getAudioBitrate());
            //frameRecorder.setAudioQuality(frameGrabber.audioq());
            frameRecorder.setSampleRate(frameGrabber.getSampleRate());
            frameRecorder.setFormat("ogg");
            frameRecorder.start();

            Frame frame;
            while ((frame = frameGrabber.grab()) != null) {
                //frame.audioChannels = 1;
                frameRecorder.record(frame);
            }*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        } /*finally {
            if (frameRecorder != null) {
                try {
                    frameRecorder.stop();
                } catch (FFmpegFrameRecorder.Exception e) {
                    // Log error but continue cleanup
                } finally {
                    try {
                        frameRecorder.release();
                    } catch (FFmpegFrameRecorder.Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (frameGrabber != null) {
                try {
                    frameGrabber.release();
                } catch (FFmpegFrameGrabber.Exception e) {
                    // Log error but continue cleanup
                }
            }
        }*/

        return Path.of(saveDirectory + "\\output.ogg");
    }
}
