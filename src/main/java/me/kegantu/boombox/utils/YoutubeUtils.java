package me.kegantu.boombox.utils;

import com.github.felipeucelli.javatube.Youtube;

public class YoutubeUtils {

    public static String getTitle(String youtubeLink){
        String title;

        try {
            Youtube youtubeVideo = new Youtube(youtubeLink);
            title = youtubeVideo.getTitle();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return title;
    }
}
