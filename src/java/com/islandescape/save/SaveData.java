package com.islandescape.save;

import com.islandescape.boat.BoatWreck;
import com.islandescape.item.Item;
import com.islandescape.player.Player;
import com.islandescape.resources.ResourceNode;

import java.util.List;

public class SaveData {

    public static SaveData capture(Player p1, Player p2, BoatWreck wreck, List<ResourceNode> nodes) {
        return new SaveData();
    }

    public static void apply(SaveData data, Player p1, Player p2, BoatWreck wreck, List<ResourceNode> nodes) {
    }


    private double p1x;
    private double p1y;
    private double p2x;
    private double p2y;

    private int p1Hotbar;
    private int p2Hotbar;

    private Item[] p1Slots = new Item[20];
    private Item[] p2Slots = new Item[20];

    private Item[] boatSlots = new Item[6];

    private boolean[] resourceDisabled = new boolean[0];

    public double getP1x() { return p1x; }
    public void setP1x(double p1x) { this.p1x = p1x; }

    public double getP1y() { return p1y; }
    public void setP1y(double p1y) { this.p1y = p1y; }

    public double getP2x() { return p2x; }
    public void setP2x(double p2x) { this.p2x = p2x; }

    public double getP2y() { return p2y; }
    public void setP2y(double p2y) { this.p2y = p2y; }

    public int getP1Hotbar() { return p1Hotbar; }
    public void setP1Hotbar(int p1Hotbar) { this.p1Hotbar = p1Hotbar; }

    public int getP2Hotbar() { return p2Hotbar; }
    public void setP2Hotbar(int p2Hotbar) { this.p2Hotbar = p2Hotbar; }

    public Item[] getP1Slots() { return p1Slots; }
    public void setP1Slots(Item[] p1Slots) { this.p1Slots = p1Slots; }

    public Item[] getP2Slots() { return p2Slots; }
    public void setP2Slots(Item[] p2Slots) { this.p2Slots = p2Slots; }

    public Item[] getBoatSlots() { return boatSlots; }
    public void setBoatSlots(Item[] boatSlots) { this.boatSlots = boatSlots; }

    public boolean[] getResourceDisabled() { return resourceDisabled; }
    public void setResourceDisabled(boolean[] resourceDisabled) { this.resourceDisabled = resourceDisabled; }
}
