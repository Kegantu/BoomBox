package me.kegantu.boombox.utils;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;

public class YTDLPDownloader {

    private static String YTDLP_FILE_NAME = "yt-dlp";
    private static String REPOSITORY_FILE = "";
    private static final String REPOSITORY = "https://github.com/yt-dlp/yt-dlp/releases/download/2026.08.19/";
    private static File OUTPUT_EXE;
    public static String YTDLP_LOCATION = "";

    private static void prepare(){
        if (SystemUtils.IS_OS_WINDOWS){
            YTDLP_FILE_NAME += ".exe";
            REPOSITORY_FILE = "yt-dlp.exe";
        } /*else if (SystemUtils.IS_OS_LINUX) {
            REPOSITORY_FILE = "ffmpeg-linux-x64.zip";
        } else if (SystemUtils.IS_OS_MAC) {
            REPOSITORY_FILE = "ffmpeg-osx-x64.zip";
        }*/

        YTDLP_LOCATION = FabricLoader.getInstance().getGameDir() + "\\music\\yt-dlp\\" + YTDLP_FILE_NAME;
        var FFMPEG_CHECK = new File(FabricLoader.getInstance().getGameDir() + "\\music\\yt-dlp\\");

        if (!FFMPEG_CHECK.exists()){
            FFMPEG_CHECK.mkdirs();
        }

        OUTPUT_EXE = new File(FabricLoader.getInstance().getGameDir() + "\\music\\yt-dlp\\" + REPOSITORY_FILE);
    }

    public static void download(){
        prepare();

        if (Files.exists(Path.of(YTDLP_LOCATION), LinkOption.NOFOLLOW_LINKS)){
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
                try (FileOutputStream fos = new FileOutputStream(OUTPUT_EXE.toPath().toString())) {
                    fos.write(response.body());
                }
            } else {
                throw new IOException("Failed to download: HTTP " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
