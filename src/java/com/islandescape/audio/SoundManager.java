package com.islandescape.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public final class SoundManager {

    private static final Map<String, Clip> clips = new HashMap<>();

    private SoundManager() {}

    public static void load(String name, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("SoundManager: file not found: " + path);
                return;
            }
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clips.put(name, clip);
        } catch (Exception e) {
            System.err.println("SoundManager: could not load '" + name + "' from " + path
                    + " (" + e.getMessage() + ")");
        }
    }

    public static void play(String name) {
        Clip clip = clips.get(name);
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}
