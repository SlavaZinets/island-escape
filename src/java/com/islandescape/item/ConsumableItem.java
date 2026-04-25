package com.islandescape.item;

public class ConsumableItem extends Item{

    private double hungerEffect;
    private double thirstEffect;


    public ConsumableItem(ItemType type, ItemCategory category, String name, String description) {
        super(type, category, name, description);
    }

    public ConsumableItem(ItemType type, ItemCategory category, String name, String description, int quantity, double hungerEffect, double thirstEffect) {
        super(type, category, name, description, quantity);
        this.hungerEffect = hungerEffect;
        this.thirstEffect = thirstEffect;
    }

    public double getHungerEffect() {
        return hungerEffect;
    }
    public double getThirstEffect() {
        return thirstEffect;
    }


}
