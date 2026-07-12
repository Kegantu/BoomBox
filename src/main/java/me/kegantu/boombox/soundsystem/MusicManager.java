package me.kegantu.boombox.soundsystem;

import java.util.Hashtable;
import java.util.List;

public class MusicManager {
    private static final Hashtable<String, Sound> CURRENTLY_PLAYING_MUSIC = new Hashtable<>();

    public static void addMusic(String key, Sound music){
        CURRENTLY_PLAYING_MUSIC.put(key, music);
    }

    public static void remove(Sound music){
        CURRENTLY_PLAYING_MUSIC.remove(music);
    }

    public static void remove(String key){
        CURRENTLY_PLAYING_MUSIC.remove(key);
    }

    public static List<Sound> getCurrentlyPlayingMusic(){
        return CURRENTLY_PLAYING_MUSIC.values().stream().toList();
    }

    public static Sound getSound(String key){
        return CURRENTLY_PLAYING_MUSIC.get(key);
    }

    public static void clearSounds(){
        CURRENTLY_PLAYING_MUSIC.clear();
    }
}
