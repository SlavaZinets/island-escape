package com.islandescape.save;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class SaveManager {

    public static final Path DEFAULT_SAVE = Paths.get("save.txt");

    private SaveManager() {}

    public static void save(SaveData data, Path path) {
    }

    public static SaveData load(Path path) throws IOException {
        return null;
    }

    public static boolean hasSave(Path path) {
        return false;
    }

    public static void deleteSave(Path path) {
    }
}
