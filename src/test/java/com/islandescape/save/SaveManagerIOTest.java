package com.islandescape.save;

import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaveManagerIOTest {

    File file = new File("save.txt");


    @BeforeEach
    public void cleanBefore() throws IOException {
        file.delete();
    }

    @AfterEach
    public void cleanAfter() throws IOException {
        file.delete();
    }

    @Test
    public void hasSaveFalseInitially() {
        assertFalse(SaveManager.hasSave());
    }

    @Test
    public void hasSaveTrueAfterSave() {
        SaveManager.save(new SaveData());
        assertTrue(SaveManager.hasSave());
        assertTrue(file.exists());
    }

    @Test
    public void deleteSaveRemovesFile() throws IOException {
        SaveManager.save(new SaveData());
        assertTrue(file.exists());

        SaveManager.deleteSave();

        assertFalse(file.exists());
        assertFalse(SaveManager.hasSave());
    }

    @Test
    public void saveDataSavesConcreteData() throws IOException {
        SaveData data = new SaveData();
        data.setP1x(123.5);
        data.setP1y(456.25);
        data.setP2x(789.125);
        data.setP2y(1000.0);

        SaveManager.save(data);
        SaveData loaded = SaveManager.load();

        assertNotNull(loaded);
        assertEquals(123.5, loaded.getP1x());
        assertEquals(456.25, loaded.getP1y());
        assertEquals(789.125, loaded.getP2x());
        assertEquals(1000.0, loaded.getP2y());
    }

    @Test
    public void saveDataSavesPlayersInventory() throws IOException {
        SaveData data = new SaveData();
        data.getP1Slots()[0] = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "", 5);
        data.getP1Slots()[3] = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "", 1);
        data.getP2Slots()[7] = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "", 3);
        data.getP2Slots()[19] = new Item(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, "Plank", "", 2);

        SaveManager.save(data);
        SaveData loaded = SaveManager.load();

        assertNotNull(loaded);
        assertEquals(20, loaded.getP1Slots().length);
        assertEquals(20, loaded.getP2Slots().length);

        assertItemEquals(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, 5, loaded.getP1Slots()[0]);
        assertItemEquals(ItemType.AXE, ItemCategory.TOOL, 1, loaded.getP1Slots()[3]);
        assertItemEquals(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, 3, loaded.getP2Slots()[7]);
        assertItemEquals(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, 2, loaded.getP2Slots()[19]);

        // empty slots stay empty
        assertNull(loaded.getP1Slots()[1]);
        assertNull(loaded.getP2Slots()[0]);
    }

    @Test
    public void saveDataSavesBoatProgressData() throws IOException {
        SaveData data = new SaveData();

        data.getBoatSlots()[0] = new Item(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, "Plank", "", 2);
        data.getBoatSlots()[2] = new Item(ItemType.FRAME, ItemCategory.CRAFTABLE_RESOURCE, "Frame", "", 1);
        data.getBoatSlots()[5] = new Item(ItemType.FITTINGS, ItemCategory.CRAFTABLE_RESOURCE, "Fittings", "", 2);

        SaveManager.save(data);
        SaveData loaded = SaveManager.load();

        assertNotNull(loaded);
        assertEquals(6, loaded.getBoatSlots().length);

        assertItemEquals(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, 2, loaded.getBoatSlots()[0]);
        assertNull(loaded.getBoatSlots()[1]);
        assertItemEquals(ItemType.FRAME, ItemCategory.CRAFTABLE_RESOURCE, 1, loaded.getBoatSlots()[2]);
        assertNull(loaded.getBoatSlots()[3]);
        assertNull(loaded.getBoatSlots()[4]);
        assertItemEquals(ItemType.FITTINGS, ItemCategory.CRAFTABLE_RESOURCE, 2, loaded.getBoatSlots()[5]);
    }

    @Test
    public void saveDataSavesDisabledResourcesInstances() throws IOException {
        SaveData data = new SaveData();
        data.setResourceDisabled(new boolean[]{false, true, true, false, false, true});

        SaveManager.save(data);
        SaveData loaded = SaveManager.load();

        assertNotNull(loaded);
        assertArrayEquals(
                new boolean[]{false, true, true, false, false, true},
                loaded.getResourceDisabled());
    }

    @Test
    public void saveDataSavesEmptyDisabledResourcesInstances() throws IOException {
        SaveData data = new SaveData();
        data.setResourceDisabled(new boolean[0]);

        SaveManager.save(data);
        SaveData loaded = SaveManager.load();

        assertNotNull(loaded);
        assertNotNull(loaded.getResourceDisabled());
        assertEquals(0, loaded.getResourceDisabled().length);
    }

    private static void assertItemEquals(ItemType type, ItemCategory category, int qty, Item actual) {
        assertNotNull(actual, "expected an item, got null");
        assertEquals(type, actual.getType());
        assertEquals(category, actual.getCategory());
        assertEquals(qty, actual.getQuantity());
    }
}
