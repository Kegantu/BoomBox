package me.kegantu.boombox.utils;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;

public class FFmpegDownloader {

    private static String FFMPEG_FILE = "ffmpeg";
    private static String REPOSITORY_FILE = "";
    private static final String REPOSITORY = "https://github.com/Tyrrrz/FFmpegBin/releases/latest/download/";
    private static File OUTPUT_ZIP;
    public static String FFMPEG_LOCATION = "";

    private static void prepare(){
        if (SystemUtils.IS_OS_WINDOWS){
            FFMPEG_FILE += ".exe";
            REPOSITORY_FILE = "ffmpeg-windows-x64.zip";
        } else if (SystemUtils.IS_OS_LINUX) {
            REPOSITORY_FILE = "ffmpeg-linux-x64.zip";
        } else if (SystemUtils.IS_OS_MAC) {
            REPOSITORY_FILE = "ffmpeg-osx-x64.zip";
        }

        FFMPEG_LOCATION = FabricLoader.getInstance().getGameDir() + "\\music\\ffmpeg\\" + FFMPEG_FILE;
        var FFMPEG_CHECK = new File(FabricLoader.getInstance().getGameDir() + "\\music\\ffmpeg\\");

        if (!FFMPEG_CHECK.exists()){
            FFMPEG_CHECK.mkdirs();
        }

        OUTPUT_ZIP = new File(FabricLoader.getInstance().getGameDir() + "\\music\\ffmpeg\\" + REPOSITORY_FILE);
    }

    public static void download(){
        prepare();

        if (Files.exists(Path.of(FFMPEG_LOCATION), LinkOption.NOFOLLOW_LINKS)){
            return;
        }

        try {
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(REPOSITORY + REPOSITORY_FILE))
                    .header("Accept", "application/octet-stream")
                    .GET()
                    .build();

            HttpResponse<byte[]> response = client.send(request,
                    HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                try (FileOutputStream fos = new FileOutputStream(OUTPUT_ZIP.toPath().toString())) {
                    fos.write(response.body());
                }
            } else {
                throw new IOException("Failed to download: HTTP " + response.statusCode());
            }

            try (FileSystem fileSystem = FileSystems.newFileSystem(OUTPUT_ZIP.toPath())) {
                Path fileToExtract = fileSystem.getPath(FFMPEG_FILE);
                Files.copy(fileToExtract, Path.of(FFMPEG_LOCATION));
            }

            //client.close();
            Files.delete(OUTPUT_ZIP.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
