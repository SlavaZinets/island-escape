package com.islandescape.item;


public class ConsumableItem extends Item {

    private final int hungerEffect;
    private final int thirstEffect;

    public ConsumableItem(ItemType type, ItemCategory category, String name, String description) {
        super(type, category, name, description);
        this.hungerEffect = 0;
        this.thirstEffect = 0;
    }

    public ConsumableItem(ItemType type, ItemCategory category, String name, String description,
                          int quantity, int hungerEffect, int thirstEffect) {
        super(type, category, name, description, quantity);
        this.hungerEffect = hungerEffect;
        this.thirstEffect = thirstEffect;
    }

    public int getHungerEffect() {
        return hungerEffect;
    }

    public int getThirstEffect() {
        return thirstEffect;
    }

    public static ConsumableItem coconut() {
        return new ConsumableItem(
                ItemType.COCONUT, ItemCategory.FOOD,
                "Coconut", "Restores hunger and a little thirst",
                1, 15, 8);
    }


    public static ConsumableItem banana() {
        return new ConsumableItem(
                ItemType.BANANA, ItemCategory.FOOD,
                "Banana", "Restores hunger",
                1, 20, 0);
    }


    public static ConsumableItem water() {
        return new ConsumableItem(
                ItemType.WATER, ItemCategory.FOOD,
                "Water", "Restores thirst",
                1, 0, 25);
    }
}
