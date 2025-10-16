package me.kegantu.boombox.utils;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;

public class FFmpegDownloader {

    public static final Path FFMPEG_LOCATION = Path.of(FabricLoader.getInstance().getGameDir() + "\\music\\ffmpeg\\ffmpeg.exe");
    private static final String REPOSITORY = "https://github.com/Tyrrrz/FFmpegBin/releases/download/7.1.1/";
    private static final Path OUTPUT_ZIP = Path.of(FabricLoader.getInstance().getGameDir() + "\\music\\ffmpeg\\ffmpeg-windows-x64.zip");

    public static void download(){
        if (Files.exists(FFMPEG_LOCATION, LinkOption.NOFOLLOW_LINKS)){
            return;
        }

        try {
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(REPOSITORY + "ffmpeg-windows-x64.zip"))
                    .header("Accept", "application/octet-stream")
                    .GET()
                    .build();

            HttpResponse<byte[]> response = client.send(request,
                    HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                try (FileOutputStream fos = new FileOutputStream(OUTPUT_ZIP.toString())) {
                    fos.write(response.body());
                }
            } else {
                throw new IOException("Failed to download: HTTP " + response.statusCode());
            }

            try (FileSystem fileSystem = FileSystems.newFileSystem(OUTPUT_ZIP)) {
                Path fileToExtract = fileSystem.getPath("ffmpeg.exe");
                Files.copy(fileToExtract, FFMPEG_LOCATION);
            }

            client.close();
            Files.delete(OUTPUT_ZIP);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /*private static boolean checkIfExist(){
        return ;
    }*/
}
