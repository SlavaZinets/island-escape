package com.islandescape.save;

import com.islandescape.item.Item;

public class SaveData {

    double p1x;
    double p1y;
    double p2x;
    double p2y;

    int p1Hotbar;
    int p2Hotbar;

    Item[] p1Slots = new Item[20];
    Item[] p2Slots = new Item[20];

    Item[] boatSlots = new Item[6];

    boolean[] resourceDisabled = new boolean[0];
}
