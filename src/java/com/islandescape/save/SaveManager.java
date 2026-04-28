package com.islandescape.save;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class SaveManager {

    public static final String path = "save.txt";

    private static final Path SAVE_PATH = Paths.get(path);

    private static final String ITEM_DELIM = "|";
    private static final String ITEM_DELIM_REGEX = "\\|";

    private SaveManager() {}

    public static void save(SaveData data) {
        Properties props = new Properties();

        props.setProperty("p1.x", Double.toString(data.getP1x()));
        props.setProperty("p1.y", Double.toString(data.getP1y()));
        props.setProperty("p2.x", Double.toString(data.getP2x()));
        props.setProperty("p2.y", Double.toString(data.getP2y()));

        props.setProperty("p1.hotbar", Integer.toString(data.getP1Hotbar()));
        props.setProperty("p2.hotbar", Integer.toString(data.getP2Hotbar()));

        props.setProperty("p1.hunger", Integer.toString(data.getP1Hunger()));
        props.setProperty("p1.thirst", Integer.toString(data.getP1Thirst()));
        props.setProperty("p2.hunger", Integer.toString(data.getP2Hunger()));
        props.setProperty("p2.thirst", Integer.toString(data.getP2Thirst()));

        writeSlots(props, "p1.slot.", data.getP1Slots());
        writeSlots(props, "p2.slot.", data.getP2Slots());
        writeSlots(props, "boat.slot.", data.getBoatSlots());

        boolean[] disabled = data.getResourceDisabled();
        props.setProperty("resource.disabled.length", Integer.toString(disabled.length));
        for (int i = 0; i < disabled.length; i++) {
            props.setProperty("resource.disabled." + i, Boolean.toString(disabled[i]));
        }

        try (BufferedWriter writer = Files.newBufferedWriter(SAVE_PATH)) {
            props.store(writer, "Island Escape save");
        } catch (IOException e) {
            throw new RuntimeException("Failed to write save file: " + SAVE_PATH, e);
        }
    }

    public static SaveData load() throws IOException {
        if (!Files.exists(SAVE_PATH)) {
            throw new NoSuchFileException(SAVE_PATH.toString());
        }

        Properties props = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(SAVE_PATH)) {
            props.load(reader);
        }

        SaveData data = new SaveData();
        data.setP1x(parseDouble(props, "p1.x"));
        data.setP1y(parseDouble(props, "p1.y"));
        data.setP2x(parseDouble(props, "p2.x"));
        data.setP2y(parseDouble(props, "p2.y"));

        data.setP1Hotbar(parseInt(props, "p1.hotbar"));
        data.setP2Hotbar(parseInt(props, "p2.hotbar"));

        // Survival stats — only override the SaveData defaults when the
        // property is present, so legacy saves load as full-stats.
        applyIntIfPresent(props, "p1.hunger", data::setP1Hunger);
        applyIntIfPresent(props, "p1.thirst", data::setP1Thirst);
        applyIntIfPresent(props, "p2.hunger", data::setP2Hunger);
        applyIntIfPresent(props, "p2.thirst", data::setP2Thirst);

        readSlots(props, "p1.slot.", data.getP1Slots());
        readSlots(props, "p2.slot.", data.getP2Slots());
        readSlots(props, "boat.slot.", data.getBoatSlots());

        int len = parseInt(props, "resource.disabled.length");
        boolean[] disabled = new boolean[len];
        for (int i = 0; i < len; i++) {
            disabled[i] = Boolean.parseBoolean(props.getProperty("resource.disabled." + i, "false"));
        }
        data.setResourceDisabled(disabled);

        return data;
    }

    public static boolean hasSave() {
        return Files.exists(SAVE_PATH);
    }

    public static void deleteSave() {
        try {
            Files.deleteIfExists(SAVE_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete save file: " + SAVE_PATH, e);
        }
    }

    private static void writeSlots(Properties props, String prefix, Item[] slots) {
        for (int i = 0; i < slots.length; i++) {
            Item item = slots[i];
            if (item == null) {
                continue;
            }
            props.setProperty(prefix + i, encodeItem(item));
        }
    }

    private static void readSlots(Properties props, String prefix, Item[] slots) {
        for (int i = 0; i < slots.length; i++) {
            String encoded = props.getProperty(prefix + i);
            slots[i] = (encoded == null) ? null : decodeItem(encoded);
        }
    }

    private static String encodeItem(Item item) {
        return item.getType().name() + ITEM_DELIM
                + item.getCategory().name() + ITEM_DELIM
                + item.getQuantity();
    }

    private static Item decodeItem(String encoded) {
        String[] parts = encoded.split(ITEM_DELIM_REGEX, -1);
        ItemType type = ItemType.valueOf(parts[0]);
        ItemCategory category = ItemCategory.valueOf(parts[1]);
        int qty = Integer.parseInt(parts[2]);
        // Name must lowercase to the InventoryScreen icon key
        String name = type.name().toLowerCase();
        return new Item(type, category, name, "", qty);
    }

    private static double parseDouble(Properties props, String key) {
        String raw = props.getProperty(key);
        return raw == null ? 0.0 : Double.parseDouble(raw);
    }

    private static int parseInt(Properties props, String key) {
        String raw = props.getProperty(key);
        return raw == null ? 0 : Integer.parseInt(raw);
    }

    private static void applyIntIfPresent(Properties props, String key,
                                          java.util.function.IntConsumer setter) {
        String raw = props.getProperty(key);
        if (raw != null) setter.accept(Integer.parseInt(raw));
    }
}
