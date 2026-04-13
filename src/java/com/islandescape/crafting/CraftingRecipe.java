package com.islandescape.crafting;

import com.islandescape.item.ItemType;

import java.util.Collections;
import java.util.Map;

public class CraftingRecipe {

    private final Map<ItemType, Integer> ingredients;
    private final ItemType output;
    private final int outputQty;

    public CraftingRecipe(Map<ItemType, Integer> ingredients, ItemType output, int outputQty) {
        this.ingredients = Collections.unmodifiableMap(ingredients);
        this.output = output;
        this.outputQty = outputQty;
    }

    public Map<ItemType, Integer> getIngredients() {
        return ingredients;
    }

    public ItemType getOutput() {
        return output;
    }

    public int getOutputQty() {
        return outputQty;
    }
}
