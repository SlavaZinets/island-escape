package com.islandescape.save;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class SaveManager {

    public static final String path = "save.txt";

    private SaveManager() {}

    public static void save(SaveData data) {
    }

    public static SaveData load() throws IOException {
        return null;
    }

    public static boolean hasSave() {
        return false;
    }

    public static void deleteSave() {
    }
}
