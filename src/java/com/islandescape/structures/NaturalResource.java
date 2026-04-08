package com.islandescape.structures;
import  com.islandescape.player.Player;
import  com.islandescape.item.Item;


//this will be abstract class from parent class worldstrucure for creating resources such as stone, tree, etc
public abstract class NaturalResource extends WorldStructure {
    protected String itemName;
    protected int health;

    public NaturalResource(int x, int y, String itemName, int health, String structureName) {
        super(x, y, structureName);
        this.itemName = itemName;//here will be the material thst will be mined from specific structure(stone for example)
        this.health = health;//attribure health will be used as the counter of how many times is the structure has to be mined in order to get resource for player
    }

    @Override
    public abstract void interact(double playerx, double playery);//main method that will base for intercating with player and overriden by each structure

    public int getHealth() {
        return health; //getter to get current health value
    }

}
