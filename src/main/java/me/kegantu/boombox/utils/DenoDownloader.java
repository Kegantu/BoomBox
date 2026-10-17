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

public class DenoDownloader {

    private static String DENO_FILE_NAME = "deno";
    private static String REPOSITORY_FILE = "";
    private static final String REPOSITORY = "https://github.com/denoland/deno/releases/latest/download/";
    private static File OUTPUT_ZIP;
    public static String DENO_LOCATION = "";

    private static void prepare(){
        if (SystemUtils.IS_OS_WINDOWS){
            DENO_FILE_NAME += ".exe";
            REPOSITORY_FILE = "deno-x86_64-pc-windows-msvc.zip";
        } else if (SystemUtils.IS_OS_LINUX) {
            REPOSITORY_FILE = "deno-x86_64-unknown-linux-gnu.zip";
        }

        DENO_LOCATION = FabricLoader.getInstance().getGameDir() + "\\music\\deno\\" + DENO_FILE_NAME;
        var FFMPEG_CHECK = new File(FabricLoader.getInstance().getGameDir() + "\\music\\deno\\");

        if (!FFMPEG_CHECK.exists()){
            FFMPEG_CHECK.mkdirs();
        }

        OUTPUT_ZIP = new File(FabricLoader.getInstance().getGameDir() + "\\music\\deno\\" + REPOSITORY_FILE);
    }

    public static void download(){
        prepare();

        if (Files.exists(Path.of(DENO_LOCATION), LinkOption.NOFOLLOW_LINKS)){
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
                Path fileToExtract = fileSystem.getPath(DENO_FILE_NAME);
                Files.copy(fileToExtract, Path.of(DENO_LOCATION));
            }

            //client.close();
            Files.delete(OUTPUT_ZIP.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
