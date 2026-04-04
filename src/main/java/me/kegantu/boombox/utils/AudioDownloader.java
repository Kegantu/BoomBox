package me.kegantu.boombox.utils;

import com.github.felipeucelli.javatube.StreamQuery;
import com.github.felipeucelli.javatube.Youtube;
import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.soundsystem.MusicManager;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AudioDownloader {

    public static final File SAVE_DIRECTORY = new File(FabricLoader.getInstance().getGameDir() + "\\music");
    private static final int MAX_MP4_AMOUNT = 5;

    public static Path download(String youtubeURL, String uuidFileName) {
        String savePath = SAVE_DIRECTORY.toPath() + "\\";

        if (!SAVE_DIRECTORY.exists()){
            SAVE_DIRECTORY.mkdirs();
        }

        checkSaveDirectorySize();

        try {
            Youtube youtubeVideo = new Youtube(youtubeURL);

            youtubeVideo.streams().filter(StreamQuery.Filter.builder().type("audio").build()).getFirst().download(savePath, uuidFileName);

            String[] command = {FFmpegDownloader.FFMPEG_LOCATION, "-y", "-i", savePath + uuidFileName + ".mp4", savePath + uuidFileName + ".ogg"};
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

        return Path.of(savePath + uuidFileName + ".ogg");
    }

    private static void checkSaveDirectorySize(){
        try (Stream<Path> fileWalk = Files.walk(SAVE_DIRECTORY.toPath())){
            List<File> files = fileWalk.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".mp4"))
                    .map(Path::toFile).collect(Collectors.toCollection((ArrayList::new)));

            if (files.size() >= MAX_MP4_AMOUNT){
                cleanSaveDirectory(files);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cleanSaveDirectory(List<File> files){
        do {
            File randomFile = files.get(new Random().nextInt(0, files.size()));
            File randomFileOGG = new File(randomFile.getAbsolutePath().replace(".mp4", ".ogg"));

            String randomFileName = randomFile.getName().substring(0, randomFile.getName().lastIndexOf('.'));

            if (MusicManager.getSound(randomFileName) == null){
                files.remove(randomFile);
                randomFile.delete();
                randomFileOGG.delete();
                continue;
            }

            if (MusicManager.getSound(randomFileName).isPlaying()){
                continue;
            }

            files.remove(randomFile);
            randomFile.delete();
            randomFileOGG.delete();
        }while (files.size() >= MAX_MP4_AMOUNT);
    }

    public static void cleanAllSaveDirectory(){
        try (Stream<Path> fileWalk = Files.walk(SAVE_DIRECTORY.toPath())){
            List<File> files = fileWalk.filter(Files::isRegularFile).map(Path::toFile)
                    .filter(file -> !file.isDirectory()).collect(Collectors.toCollection((ArrayList::new)));

            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                files.remove(file);
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
