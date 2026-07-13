package me.kegantu.boombox.soundsystem;

import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;
import oshi.util.tuples.Triplet;

import java.util.Hashtable;
import java.util.List;

public class ServerMusicManager {
    private static final Hashtable<String, Triplet<String, Vector3f, Float>> CURRENTLY_PLAYING_MUSIC_SERVER = new Hashtable<>();

    public static void addMusicURL(String UUID, Triplet<String, Vector3f, Float> musicServerInfo){
        CURRENTLY_PLAYING_MUSIC_SERVER.put(UUID, musicServerInfo);
    }

    public static void remove(Triplet<String, BlockPos, Float> musicServerInfo){
        CURRENTLY_PLAYING_MUSIC_SERVER.remove(musicServerInfo);
    }

    public static void remove(String key){
        CURRENTLY_PLAYING_MUSIC_SERVER.remove(key);
    }

    public static List<String> getCurrentlyPlayingMusicServerKeys(){
        return CURRENTLY_PLAYING_MUSIC_SERVER.keySet().stream().toList();
    }

    public static Triplet<String, Vector3f, Float> getMusicServerInfo(String key){
        return CURRENTLY_PLAYING_MUSIC_SERVER.get(key);
    }

    public static void clearServerSounds(){
        CURRENTLY_PLAYING_MUSIC_SERVER.clear();
    }
}
