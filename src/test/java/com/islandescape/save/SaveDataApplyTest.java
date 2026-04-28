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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaveDataApplyTest {

    @Test
    public void freshCaptureApplyRoundTripYieldsEqualObservableState() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        SaveData data = SaveData.capture(p1, p2, wreck, List.of());
        SaveData.apply(data, p1, p2, wreck, List.of());

        assertEquals(0.0, p1.getX(), 0.0001);
        assertEquals(0.0, p1.getY(), 0.0001);
        assertEquals(0.0, p2.getX(), 0.0001);
        assertEquals(0.0, p2.getY(), 0.0001);

        for (int i = 0; i < 20; i++) {
            assertNull(p1.getInventory().getSlot(i));
            assertNull(p2.getInventory().getSlot(i));
        }

        BoatRepairSystem repair = wreck.getRepairSystem();
        for (int i = 0; i < BoatRepairSystem.TOTAL_SLOTS; i++) {
            assertNull(repair.getSlot(i));
        }
        assertFalse(wreck.isFullyRepaired());
        assertFalse(wreck.isPlayerBoarded(0));
        assertFalse(wreck.isPlayerBoarded(1));
    }

    @Test
    public void applyMovesPlayersAndReplacesInventories() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        SaveData data = new SaveData();
        data.setP1x(123.5);
        data.setP1y(456.25);
        data.setP2x(789.125);
        data.setP2y(1000.0);

        data.getP1Slots()[0] = new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "", 5);
        data.getP1Slots()[17] = new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "", 1);
        data.getP2Slots()[7] = new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "", 3);
        data.setP1Hotbar(17);
        data.setP2Hotbar(15);

        SaveData.apply(data, p1, p2, wreck, List.of());

        assertEquals(123.5, p1.getX() );
        assertEquals(456.25, p1.getY());
        assertEquals(789.125, p2.getX());
        assertEquals(1000.0, p2.getY());

        Inventory inv1 = p1.getInventory();
        Inventory inv2 = p2.getInventory();
        assertItemEquals(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, 5, inv1.getSlot(0));
        assertItemEquals(ItemType.AXE, ItemCategory.TOOL, 1, inv1.getSlot(17));
        assertItemEquals(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, 3, inv2.getSlot(7));
        assertEquals(17, inv1.getSelectedHotBarSlot());
        assertEquals(15, inv2.getSelectedHotBarSlot());
    }

    @Test
    public void applyingPlankQty3InSlotZeroCompletesItAndUpdatesProgress() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        SaveData data = new SaveData();
        data.getBoatSlots()[0] = new Item(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, "Plank", "", 3);

        SaveData.apply(data, p1, p2, wreck, List.of());

        BoatRepairSystem repair = wreck.getRepairSystem();
        assertTrue(repair.isSlotComplete(0));
        assertEquals(1.0 / BoatRepairSystem.TOTAL_SLOTS, repair.getProgress());
        assertFalse(wreck.isFullyRepaired());
    }

    @Test
    public void applyingFullyFilledBoatLeavesItFullyRepairedAndBoardable() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        SaveData data = new SaveData();
        data.getBoatSlots()[0] = new Item(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, "Plank", "", 3);
        data.getBoatSlots()[1] = new Item(ItemType.MAST, ItemCategory.CRAFTABLE_RESOURCE, "Mast", "", 1);
        data.getBoatSlots()[2] = new Item(ItemType.FRAME, ItemCategory.CRAFTABLE_RESOURCE, "Frame", "", 1);
        data.getBoatSlots()[3] = new Item(ItemType.SAIL, ItemCategory.CRAFTABLE_RESOURCE, "Sail", "", 1);
        data.getBoatSlots()[4] = new Item(ItemType.RUDDER, ItemCategory.CRAFTABLE_RESOURCE, "Rudder", "", 1);
        data.getBoatSlots()[5] = new Item(ItemType.FITTINGS, ItemCategory.CRAFTABLE_RESOURCE, "Fittings", "", 2);

        SaveData.apply(data, p1, p2, wreck, List.of());

        assertTrue(wreck.isFullyRepaired());
        assertEquals(1.0, wreck.getRepairSystem().getProgress(), 0.0001);
        assertTrue(wreck.board(0, p1.getX(), p1.getY()));
        assertTrue(wreck.board(1, p2.getX(), p2.getY()));
        assertTrue(wreck.bothBoarded());
    }

    @Test
    public void applyDoesNotPreserveBoardedPlayers() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        // Pre-fill the wreck to a fully-repaired+boarded state, simulating mid-game state.
        BoatRepairSystem repair = wreck.getRepairSystem();
        repair.forceSetSlot(0, new Item(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, "Plank", "", 3));
        repair.forceSetSlot(1, new Item(ItemType.MAST, ItemCategory.CRAFTABLE_RESOURCE, "Mast", "", 1));
        repair.forceSetSlot(2, new Item(ItemType.FRAME, ItemCategory.CRAFTABLE_RESOURCE, "Frame", "", 1));
        repair.forceSetSlot(3, new Item(ItemType.SAIL, ItemCategory.CRAFTABLE_RESOURCE, "Sail", "", 1));
        repair.forceSetSlot(4, new Item(ItemType.RUDDER, ItemCategory.CRAFTABLE_RESOURCE, "Rudder", "", 1));
        repair.forceSetSlot(5, new Item(ItemType.FITTINGS, ItemCategory.CRAFTABLE_RESOURCE, "Fittings", "", 2));
        wreck.board(0, p1.getX(), p1.getY());
        wreck.board(1, p2.getX(), p2.getY());
        assertTrue(wreck.bothBoarded());

        // Apply an empty save — should clear boarded state.
        SaveData.apply(new SaveData(), p1, p2, wreck, List.of());

        assertFalse(wreck.isPlayerBoarded(0));
        assertFalse(wreck.isPlayerBoarded(1));
        assertFalse(wreck.bothBoarded());
    }

    @Test
    public void applyRestoresHungerAndThirstOnBothPlayers() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        SaveData data = new SaveData();
        data.setP1Hunger(33);
        data.setP1Thirst(44);
        data.setP2Hunger(55);
        data.setP2Thirst(66);

        SaveData.apply(data, p1, p2, wreck, List.of());

        assertEquals(33, p1.getSurvivalStats().getHunger());
        assertEquals(44, p1.getSurvivalStats().getThirst());
        assertEquals(55, p2.getSurvivalStats().getHunger());
        assertEquals(66, p2.getSurvivalStats().getThirst());
    }

    @Test
    public void hungerAndThirstSurviveACaptureApplyRoundTrip() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        p1.getSurvivalStats().loadFromBackup(81, 72);
        p2.getSurvivalStats().loadFromBackup(63, 54);

        SaveData data = SaveData.capture(p1, p2, wreck, List.of());

        // Reset to fresh players, then apply.
        Player loaded1 = new Player("P1", 1, 0, 0);
        Player loaded2 = new Player("P2", 2, 0, 0);
        SaveData.apply(data, loaded1, loaded2, wreck, List.of());

        assertEquals(81, loaded1.getSurvivalStats().getHunger());
        assertEquals(72, loaded1.getSurvivalStats().getThirst());
        assertEquals(63, loaded2.getSurvivalStats().getHunger());
        assertEquals(54, loaded2.getSurvivalStats().getThirst());
    }

    @Test
    public void applyMarksDisabledNodesByIndex() {
        Player p1 = new Player("P1", 1, 0, 0);
        Player p2 = new Player("P2", 2, 0, 0);
        BoatWreck wreck = new BoatWreck(0, 0, "Wreck");

        ResourceNode node0 = new Tree(0, 0);
        ResourceNode node1 = new Tree(50, 50);
        ResourceNode node2 = new Tree(100, 100);
        List<ResourceNode> nodes = Arrays.asList(node0, node1, node2);

        SaveData data = new SaveData();
        data.setResourceDisabled(new boolean[]{false, true, false});

        SaveData.apply(data, p1, p2, wreck, nodes);

        assertFalse(node0.isDisabled());
        assertTrue(node1.isDisabled());
        assertFalse(node2.isDisabled());
    }

    private static void assertItemEquals(ItemType type, ItemCategory category, int qty, Item actual) {
        assertEquals(type, actual.getType());
        assertEquals(category, actual.getCategory());
        assertEquals(qty, actual.getQuantity());
    }
}
