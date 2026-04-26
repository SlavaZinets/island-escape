package com.islandescape.window;

import com.islandescape.boat.BoatRepairSystem;
import com.islandescape.boat.BoatWreck;
import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.map.MapLoader;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;
import com.islandescape.player.Player;
import com.islandescape.resources.ResourceNode;
import com.islandescape.ui.MainMenu;
import com.islandescape.ui.MainMenu.MenuOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GamePanelNewGameResetTest {

    private static final String TMX_PATH = "src/resources/maps/IslandMap.tmx";

    private TileMap map;
    private Player p1;
    private Player p2;
    private GamePanel panel;
    private BoatWreck wreck;

    @BeforeEach
    public void setUp() throws Exception {
        map = MapLoader.load(TMX_PATH);
        MapRenderer renderer = new MapRenderer();

        p1 = new Player("Player1", 1, 320, 192);
        p2 = new Player("Player2", 2, 352, 192);

        panel = new GamePanel(map, renderer, p1, p2);
        panel.setQuitAction(() -> {});

        wreck = new BoatWreck(130, 130, "Boat");
        panel.setBoatWreck(wreck);
    }

    @Test
    public void resetToFresh_returnsPlayersToSpawnPositions() {
        p1.setPosition(100, 100);
        p2.setPosition(500, 500);

        panel.resetToFresh();

        assertEquals(320.0, p1.getX(), 0.001);
        assertEquals(192.0, p1.getY(), 0.001);
        assertEquals(352.0, p2.getX(), 0.001);
        assertEquals(192.0, p2.getY(), 0.001);
    }

    @Test
    public void resetToFresh_clearsBothInventories() {
        Inventory inv1 = p1.getInventory();
        Inventory inv2 = p2.getInventory();
        inv1.addItem(new Item(ItemType.WOOD, ItemCategory.PRIMARY_RESOURCE, "Wood", "", 5));
        inv1.addItem(new Item(ItemType.STONE, ItemCategory.PRIMARY_RESOURCE, "Stone", "", 3));
        inv2.addItem(new Item(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, "Plank", "", 4));

        panel.resetToFresh();

        assertTrue(inv1.snapshot().isEmpty(), "P1 inventory should be empty after reset");
        assertTrue(inv2.snapshot().isEmpty(), "P2 inventory should be empty after reset");
    }

    @Test
    public void resetToFresh_emptiesBoatRepair() {
        BoatRepairSystem repair = wreck.getRepairSystem();
        repair.placeIn(0, new Item(ItemType.PLANK, ItemCategory.CRAFTABLE_RESOURCE, "Plank", "", 2));
        repair.placeIn(1, new Item(ItemType.MAST, ItemCategory.CRAFTABLE_RESOURCE, "Mast", "", 1));

        panel.resetToFresh();

        for (int i = 0; i < BoatRepairSystem.TOTAL_SLOTS; i++) {
            assertNull(repair.getSlot(i), "boat slot " + i + " should be empty");
        }
        assertFalse(repair.isFullyRepaired());
        assertEquals(0.0, repair.getProgress(), 0.001);
    }

    @Test
    public void resetToFresh_reEnablesAllResourceNodes() {
        List<ResourceNode> nodes = panel.getResourceNodes();
        assertFalse(nodes.isEmpty(), "fixture map should produce at least one resource node");

        for (ResourceNode node : nodes) {
            node.disable();
        }

        panel.resetToFresh();

        for (ResourceNode node : panel.getResourceNodes()) {
            assertFalse(node.isDisabled(), "resource node should be re-enabled after reset");
        }
    }
}
