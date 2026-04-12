package com.islandescape.crafting;

import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CraftingSystemTest {

    private CraftingSystem cs;
    private Inventory p1Inv;
    private Inventory p2Inv;

    @BeforeEach
    public void setUp() {
        cs = new CraftingSystem();
        p1Inv = new Inventory(10);
        p2Inv = new Inventory(10);
    }

    private Item item(ItemType type, int qty) {
        return new Item(type, ItemCategory.PRIMARY_RESOURCE, type.name(), "desc", qty);
    }

    private Item item(ItemType type) {
        return item(type, 1);
    }

    // ── Recipe tests ────────────────────────────────────────────────────

    @Test
    public void testRopeRecipe() {
        cs.place(0, item(ItemType.VINES), 0);
        cs.place(1, item(ItemType.VINES), 0);
        CraftingRecipe match = cs.preview();
        assertNotNull(match);
        assertEquals(ItemType.ROPE, match.getOutput());
        assertEquals(1, match.getOutputQty());
    }

    @Test
    public void testAxeRecipe() {
        cs.place(0, item(ItemType.STONE), 0);
        cs.place(1, item(ItemType.WOOD), 0);
        cs.place(2, item(ItemType.ROPE), 0);
        CraftingRecipe match = cs.preview();
        assertNotNull(match);
        assertEquals(ItemType.AXE, match.getOutput());
    }

    @Test
    public void testPickaxeRecipe() {
        cs.place(0, item(ItemType.STONE), 0);
        cs.place(1, item(ItemType.STONE), 0);
        cs.place(2, item(ItemType.WOOD), 0);
        cs.place(3, item(ItemType.ROPE), 0);
        CraftingRecipe match = cs.preview();
        assertNotNull(match);
        assertEquals(ItemType.PICKAXE, match.getOutput());
    }

    @Test
    public void testPlanksRecipe() {
        cs.place(0, item(ItemType.WOOD), 0);
        cs.place(1, item(ItemType.WOOD), 0);
        CraftingRecipe match = cs.preview();
        assertNotNull(match);
        assertEquals(ItemType.PLANKS, match.getOutput());
        assertEquals(3, match.getOutputQty());
    }

    @Test
    public void testPaddleRecipe() {
        cs.place(0, item(ItemType.PLANKS), 0);
        cs.place(1, item(ItemType.PLANKS), 0);
        CraftingRecipe match = cs.preview();
        assertNotNull(match);
        assertEquals(ItemType.PADDLE, match.getOutput());
    }

    @Test
    public void testFrameRecipe() {
        cs.place(0, item(ItemType.PLANKS), 0);
        cs.place(1, item(ItemType.PLANKS), 0);
        cs.place(2, item(ItemType.ROPE), 0);
        cs.place(3, item(ItemType.ROPE), 0);
        CraftingRecipe match = cs.preview();
        assertNotNull(match);
        assertEquals(ItemType.FRAME, match.getOutput());
    }

    @Test
    public void testMastRecipe() {
        cs.place(0, item(ItemType.WOOD), 0);
        cs.place(1, item(ItemType.WOOD), 0);
        cs.place(2, item(ItemType.WOOD), 0);
        cs.place(3, item(ItemType.ROPE), 0);
        CraftingRecipe match = cs.preview();
        assertNotNull(match);
        assertEquals(ItemType.MAST, match.getOutput());
    }

    @Test
    public void testRudderRecipe() {
        cs.place(0, item(ItemType.PLANKS), 0);
        cs.place(1, item(ItemType.PLANKS), 0);
        cs.place(2, item(ItemType.STONE), 0);
        cs.place(3, item(ItemType.ROPE), 0);
        CraftingRecipe match = cs.preview();
        assertNotNull(match);
        assertEquals(ItemType.RUDDER, match.getOutput());
    }

    @Test
    public void testFittingsRecipe() {
        cs.place(0, item(ItemType.STONE), 0);
        cs.place(1, item(ItemType.STONE), 0);
        cs.place(2, item(ItemType.ROPE), 0);
        cs.place(3, item(ItemType.WOOD), 0);
        CraftingRecipe match = cs.preview();
        assertNotNull(match);
        assertEquals(ItemType.FITTINGS, match.getOutput());
    }

    // ── Slot-order independence ─────────────────────────────────────────

    @Test
    public void testSlotOrderDoesNotMatter() {
        cs.place(2, item(ItemType.STONE), 0);
        cs.place(0, item(ItemType.ROPE), 0);
        cs.place(1, item(ItemType.WOOD), 0);
        CraftingRecipe match = cs.preview();
        assertNotNull(match);
        assertEquals(ItemType.AXE, match.getOutput());
    }

    // ── Edge cases ──────────────────────────────────────────────────────

    @Test
    public void testEmptyGridReturnsNull() {
        assertNull(cs.preview());
    }

    @Test
    public void testPartialIngredientsReturnNull() {
        cs.place(0, item(ItemType.STONE), 0);
        cs.place(1, item(ItemType.WOOD), 0);
        assertNull(cs.preview());
    }

    @Test
    public void testExtraIngredientsReturnNull() {
        cs.place(0, item(ItemType.VINES), 0);
        cs.place(1, item(ItemType.VINES), 0);
        cs.place(2, item(ItemType.WOOD), 0);
        assertNull(cs.preview());
    }

    // ── Craft action ────────────────────────────────────────────────────

    @Test
    public void testCraftConsumesGridAndDeliversOutput() {
        cs.place(0, item(ItemType.VINES), 0);
        cs.place(1, item(ItemType.VINES), 0);
        Item result = cs.craft(p1Inv);
        assertNotNull(result);
        assertEquals(ItemType.ROPE, result.getType());
        assertEquals(1, result.getQuantity());
        assertNull(cs.preview());
    }

    @Test
    public void testCraftPlanksYieldsThree() {
        cs.place(0, item(ItemType.WOOD), 0);
        cs.place(1, item(ItemType.WOOD), 0);
        Item result = cs.craft(p1Inv);
        assertNotNull(result);
        assertEquals(ItemType.PLANKS, result.getType());
        assertEquals(3, result.getQuantity());
    }

    @Test
    public void testCraftWithNoMatchReturnsNull() {
        cs.place(0, item(ItemType.STONE), 0);
        assertNull(cs.craft(p1Inv));
        assertNotNull(cs.take(0));
    }

    // ── clearGrid ───────────────────────────────────────────────────────

    @Test
    public void testClearGridReturnsItemsToOriginatingPlayer() {
        cs.place(0, item(ItemType.WOOD), 0);
        cs.place(1, item(ItemType.STONE), 1);
        cs.clearGrid(p1Inv, p2Inv);
        assertEquals(1, p1Inv.countOf(ItemType.WOOD));
        assertEquals(0, p1Inv.countOf(ItemType.STONE));
        assertEquals(1, p2Inv.countOf(ItemType.STONE));
        assertEquals(0, p2Inv.countOf(ItemType.WOOD));
    }

    @Test
    public void testClearGridEmptiesAllSlots() {
        cs.place(0, item(ItemType.WOOD), 0);
        cs.place(1, item(ItemType.VINES), 0);
        cs.clearGrid(p1Inv, p2Inv);
        assertNull(cs.preview());
        assertNull(cs.take(0));
        assertNull(cs.take(1));
    }

    // ── take ────────────────────────────────────────────────────────────

    @Test
    public void testTakeRemovesItemFromSlot() {
        Item wood = item(ItemType.WOOD);
        cs.place(0, wood, 0);
        Item taken = cs.take(0);
        assertNotNull(taken);
        assertEquals(ItemType.WOOD, taken.getType());
        assertNull(cs.take(0));
    }
}
