package com.islandescape.save;

import com.islandescape.boat.BoatRepairSystem;
import com.islandescape.boat.BoatWreck;
import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.player.Player;
import com.islandescape.resources.ResourceNode;
import com.islandescape.resources.Tree;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SaveDataCaptureTest {

    @Test
    public void capturesPlayerCoordinates() {
        Player p1 = new Player("P1", 1, 100, 200);
        Player p2 = new Player("P2", 2, 300, 400);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        SaveData data = SaveData.capture(p1, p2, wreck, List.of());

        assertEquals(100.0, data.getP1x(), 0.0001);
        assertEquals(200.0, data.getP1y(), 0.0001);
        assertEquals(300.0, data.getP2x(), 0.0001);
        assertEquals(400.0, data.getP2y(), 0.0001);
    }

    @Test
    public void capturesHotbarSelection() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        p1.getInventory().setSelectedHotBarSlot(17);
        p2.getInventory().setSelectedHotBarSlot(19);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        SaveData data = SaveData.capture(p1, p2, wreck, List.of());

        assertEquals(17, data.getP1Hotbar());
        assertEquals(19, data.getP2Hotbar());
    }

    @Test
    public void capturesPlayerInventoriesAcrossAllTwentySlots() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);

        Item wood = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "", 5);
        Item axe = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "", 1);
        Item stone = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "", 3);
        Item plank = new Item(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, "Plank", "", 2);

        // slot 0 is in the main grid; slot 17 is in the hotbar
        p1.getInventory().setSlot(0, wood);
        p1.getInventory().setSlot(17, axe);
        p2.getInventory().setSlot(7, stone);
        p2.getInventory().setSlot(19, plank);

        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        SaveData data = SaveData.capture(p1, p2, wreck, List.of());

        assertEquals(20, data.getP1Slots().length);
        assertEquals(20, data.getP2Slots().length);

        assertItemEquals(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, 5, data.getP1Slots()[0]);
        assertItemEquals(ItemType.AXE, ItemCategory.TOOL, 1, data.getP1Slots()[17]);
        assertItemEquals(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, 3, data.getP2Slots()[7]);
        assertItemEquals(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, 2, data.getP2Slots()[19]);

        // unset slots stay null
        assertNull(data.getP1Slots()[1]);
        assertNull(data.getP1Slots()[14]);
        assertNull(data.getP2Slots()[0]);
        assertNull(data.getP2Slots()[15]);
    }

    @Test
    public void capturesBoatSlotsIncludingIncompleteAndCompleteSlots() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        BoatRepairSystem repair = wreck.getRepairSystem();
        // slot 0: PLANK requires qty 3 — depositing qty 2 leaves it incomplete
        Item plank2 = new Item(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, "Plank", "", 2);
        repair.placeIn(0, plank2);
        // slot 2: FRAME requires qty 1 — depositing qty 1 completes it
        Item frame1 = new Item(ItemType.FRAME, ItemCategory.CRAFTABLE_RESOURCE, "Frame", "", 1);
        repair.placeIn(2, frame1);

        SaveData data = SaveData.capture(p1, p2, wreck, List.of());

        assertEquals(6, data.getBoatSlots().length);
        assertItemEquals(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, 2, data.getBoatSlots()[0]);
        assertNull(data.getBoatSlots()[1]);
        assertItemEquals(ItemType.FRAME, ItemCategory.CRAFTABLE_RESOURCE, 1, data.getBoatSlots()[2]);
        assertNull(data.getBoatSlots()[3]);
        assertNull(data.getBoatSlots()[4]);
        assertNull(data.getBoatSlots()[5]);
    }

    @Test
    public void capturesResourceDisabledFlagsByNodeIndex() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        ResourceNode node0 = new Tree(0, 0);
        ResourceNode node1 = new Tree(50, 50);
        ResourceNode node2 = new Tree(100, 100);
        node1.disable();

        SaveData data = SaveData.capture(p1, p2, wreck, Arrays.asList(node0, node1, node2));

        assertNotNull(data.getResourceDisabled());
        assertEquals(3, data.getResourceDisabled().length);
        assertEquals(false, data.getResourceDisabled()[0]);
        assertEquals(true, data.getResourceDisabled()[1]);
        assertEquals(false, data.getResourceDisabled()[2]);
    }

    @Test
    public void capturesEmptyResourceListAsZeroLengthArray() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        SaveData data = SaveData.capture(p1, p2, wreck, List.of());

        assertNotNull(data.getResourceDisabled());
        assertEquals(0, data.getResourceDisabled().length);
    }

    @Test
    public void capturesHungerAndThirstFromBothPlayers() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        p1.getSurvivalStats().loadFromBackup(73, 64);
        p2.getSurvivalStats().loadFromBackup(12, 45);

        SaveData data = SaveData.capture(p1, p2, wreck, List.of());

        assertEquals(73, data.getP1Hunger());
        assertEquals(64, data.getP1Thirst());
        assertEquals(12, data.getP2Hunger());
        assertEquals(45, data.getP2Thirst());
    }

    private static void assertItemEquals(ItemType type, ItemCategory category, int qty, Item actual) {
        assertNotNull(actual, "expected an item, got null");
        assertEquals(type, actual.getType());
        assertEquals(category, actual.getCategory());
        assertEquals(qty, actual.getQuantity());
    }
}
