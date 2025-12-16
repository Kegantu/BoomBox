package me.kegantu.boombox.utils;

import com.github.felipeucelli.javatube.StreamQuery;
import com.github.felipeucelli.javatube.Youtube;
import me.kegantu.boombox.BoomBox;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class AudioDownloader {

    public static Path download(String youtubeURL) {
        File saveDirectory = new File(FabricLoader.getInstance().getGameDir() + "\\music");

        if (!saveDirectory.exists()){
            saveDirectory.mkdirs();
        }

        try {
            Youtube youtubeVideo = new Youtube(youtubeURL);

            youtubeVideo.streams().filter(StreamQuery.Filter.builder().type("audio").build()).getFirst().download(saveDirectory + "\\", "youtube");

            String[] command = {FFmpegDownloader.FFMPEG_LOCATION, "-y", "-i", saveDirectory + "\\youtube.mp4", saveDirectory + "\\output.ogg"};
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Path.of(saveDirectory + "\\output.ogg");
    }
}
