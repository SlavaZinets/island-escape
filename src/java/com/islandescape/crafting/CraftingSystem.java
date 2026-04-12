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

    public void place(int slot, Item item, int ownerPlayerId) {
    }

    public Item take(int slot) {
        return null;
    }

    public void clearGrid(Inventory p1Inv, Inventory p2Inv) {
    }

    public CraftingRecipe preview() {
        return null;
    }

    public Item craft(Inventory receivingInv) {

        return null;
    }

    private Map<ItemType, Integer> buildGridMultiset() {
        return new HashMap<>();
    }

    private ItemCategory categoryFor(ItemType type) {
        return ItemCategory.PRIMARY_RESOURCE;
    }

    private static List<CraftingRecipe> buildRecipes() {
        List<CraftingRecipe> list = new ArrayList<>();
        return Collections.unmodifiableList(list);
    }

    private static CraftingRecipe recipe(Map<ItemType, Integer> ingredients,
                                         ItemType output, int qty) {
        return new CraftingRecipe(ingredients, output, qty);
    }
}
