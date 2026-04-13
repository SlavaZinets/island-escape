package com.islandescape.crafting;

import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingSystem {

    private final Item[] grid = new Item[4];
    private final int[] gridSlotOwner = new int[4];
    private final List<CraftingRecipe> recipes;

    public CraftingSystem() {
        this.recipes = buildRecipes();
    }

    public void placeIn(int slot, Item item, int ownerPlayerId) {
        if (slot < 0 || slot >= 4) return;
        grid[slot] = item;
        gridSlotOwner[slot] = ownerPlayerId;
    }

    public Item getSlot(int slot) {
        if (slot < 0 || slot >= 4) return null;
        return grid[slot];
    }

    public Item takeOut(int slot) {
        if (slot < 0 || slot >= 4) return null;
        Item item = grid[slot];
        grid[slot] = null;
        return item;
    }

    public void clearGrid(Inventory p1Inv, Inventory p2Inv) {
        for (int i = 0; i < 4; i++) {
            if (grid[i] != null) {
                if (gridSlotOwner[i] == 0) {
                    p1Inv.add(grid[i]);
                } else {
                    p2Inv.add(grid[i]);
                }
                grid[i] = null;
            }
        }
    }

    public int firstEmptySlot() {
        for (int i = 0; i < 4; i++) {
            if (grid[i] == null) return i;
        }
        return -1;
    }

    public CraftingRecipe preview() {
        Map<ItemType, Integer> gridMultiset = buildGridMultiset();
        if (gridMultiset.isEmpty()) return null;

        for (CraftingRecipe recipe : recipes) {
            if (recipe.getIngredients().equals(gridMultiset)) {
                return recipe;
            }
        }
        return null;
    }

    public Item craft(Inventory receivingInv) {
        CraftingRecipe recipe = preview();
        if (recipe == null) return null;

        for (int i = 0; i < 4; i++) {
            grid[i] = null;
        }

        return new Item(recipe.getOutput(), categoryFor(recipe.getOutput()),
                recipe.getOutput().name(), "Crafted item", recipe.getOutputQty());
    }

    private Map<ItemType, Integer> buildGridMultiset() {
        Map<ItemType, Integer> multiset = new HashMap<>();
        for (Item item : grid) {
            if (item != null) {
                multiset.merge(item.getType(), item.getQuantity(), Integer::sum);
            }
        }
        return multiset;
    }

    private ItemCategory categoryFor(ItemType type) {
        switch (type) {
            case AXE:
            case PICKAXE:
                return ItemCategory.TOOL;
            case ROPE:
            case PLANKS:
            case FRAME:
            case MAST:
            case RUDDER:
            case FITTINGS:
            case PADDLE:
                return ItemCategory.CRAFTABLE_RESOURCE;
            default:
                return ItemCategory.PRIMARY_RESOURCE;
        }
    }

    private static List<CraftingRecipe> buildRecipes() {
        List<CraftingRecipe> list = new ArrayList<>();

        list.add(recipe(Map.of(ItemType.VINES, 2), ItemType.ROPE, 1));
        list.add(recipe(Map.of(ItemType.STONE, 1, ItemType.WOOD, 1, ItemType.ROPE, 1), ItemType.AXE, 1));
        list.add(recipe(Map.of(ItemType.STONE, 2, ItemType.WOOD, 1, ItemType.ROPE, 1), ItemType.PICKAXE, 1));
        list.add(recipe(Map.of(ItemType.WOOD, 2), ItemType.PLANKS, 3));
        list.add(recipe(Map.of(ItemType.PLANKS, 2), ItemType.PADDLE, 1));
        list.add(recipe(Map.of(ItemType.PLANKS, 2, ItemType.ROPE, 2), ItemType.FRAME, 1));
        list.add(recipe(Map.of(ItemType.WOOD, 3, ItemType.ROPE, 1), ItemType.MAST, 1));
        list.add(recipe(Map.of(ItemType.PLANKS, 2, ItemType.STONE, 1, ItemType.ROPE, 1), ItemType.RUDDER, 1));
        list.add(recipe(Map.of(ItemType.STONE, 2, ItemType.ROPE, 1, ItemType.WOOD, 1), ItemType.FITTINGS, 1));

        return Collections.unmodifiableList(list);
    }

    private static CraftingRecipe recipe(Map<ItemType, Integer> ingredients,
                                         ItemType output, int qty) {
        return new CraftingRecipe(ingredients, output, qty);
    }
}
