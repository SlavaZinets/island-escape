package com.islandescape.save;

import com.islandescape.player.SurvivalStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SaveDataDefaultsTest {

    @Test
    public void coordsDefaultToZero() {
        SaveData data = new SaveData();
        assertEquals(0.0, data.getP1x(), 0.0);
        assertEquals(0.0, data.getP1y(), 0.0);
        assertEquals(0.0, data.getP2x(), 0.0);
        assertEquals(0.0, data.getP2y(), 0.0);
    }

    @Test
    public void hotbarIndicesDefaultToZero() {
        SaveData data = new SaveData();
        assertEquals(0, data.getP1Hotbar());
        assertEquals(0, data.getP2Hotbar());
    }

    @Test
    public void playerSlotArraysAreSizedAndAllNull() {
        SaveData data = new SaveData();

        assertNotNull(data.getP1Slots());
        assertEquals(20, data.getP1Slots().length);
        for (int i = 0; i < data.getP1Slots().length; i++) {
            assertNull(data.getP1Slots()[i]);
        }

        assertNotNull(data.getP2Slots());
        assertEquals(20, data.getP2Slots().length);
        for (int i = 0; i < data.getP2Slots().length; i++) {
            assertNull(data.getP2Slots()[i]);
        }
    }

    @Test
    public void boatSlotsAreSizedAndAllNull() {
        SaveData data = new SaveData();

        assertNotNull(data.getBoatSlots());
        assertEquals(6, data.getBoatSlots().length);
        for (int i = 0; i < data.getBoatSlots().length; i++) {
            assertNull(data.getBoatSlots()[i]);
        }
    }

    @Test
    public void resourceDisabledIsNonNull() {
        SaveData data = new SaveData();
        assertNotNull(data.getResourceDisabled());
    }

    @Test
    public void hungerAndThirstDefaultToMaxSoLegacySavesLoadFull() {
        SaveData data = new SaveData();
        assertEquals(SurvivalStats.MAX, data.getP1Hunger());
        assertEquals(SurvivalStats.MAX, data.getP1Thirst());
        assertEquals(SurvivalStats.MAX, data.getP2Hunger());
        assertEquals(SurvivalStats.MAX, data.getP2Thirst());
    }
}
