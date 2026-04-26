package com.islandescape.save;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SaveDataDefaultsTest {

    @Test
    public void coordsDefaultToZero() {
        SaveData data = new SaveData();
        assertEquals(0.0, data.p1x, 0.0);
        assertEquals(0.0, data.p1y, 0.0);
        assertEquals(0.0, data.p2x, 0.0);
        assertEquals(0.0, data.p2y, 0.0);
    }

    @Test
    public void hotbarIndicesDefaultToZero() {
        SaveData data = new SaveData();
        assertEquals(0, data.p1Hotbar);
        assertEquals(0, data.p2Hotbar);
    }

    @Test
    public void playerSlotArraysAreSizedAndAllNull() {
        SaveData data = new SaveData();

        assertNotNull(data.p1Slots, "p1Slots should be initialized");
        assertEquals(20, data.p1Slots.length);
        for (int i = 0; i < data.p1Slots.length; i++) {
            assertNull(data.p1Slots[i], "p1Slots[" + i + "] should be null");
        }

        assertNotNull(data.p2Slots, "p2Slots should be initialized");
        assertEquals(20, data.p2Slots.length);
        for (int i = 0; i < data.p2Slots.length; i++) {
            assertNull(data.p2Slots[i], "p2Slots[" + i + "] should be null");
        }
    }

    @Test
    public void boatSlotsAreSizedAndAllNull() {
        SaveData data = new SaveData();

        assertNotNull(data.boatSlots, "boatSlots should be initialized");
        assertEquals(6, data.boatSlots.length);
        for (int i = 0; i < data.boatSlots.length; i++) {
            assertNull(data.boatSlots[i], "boatSlots[" + i + "] should be null");
        }
    }

    @Test
    public void resourceDisabledIsNonNull() {
        SaveData data = new SaveData();
        assertNotNull(data.resourceDisabled, "resourceDisabled should be initialized");
    }
}
