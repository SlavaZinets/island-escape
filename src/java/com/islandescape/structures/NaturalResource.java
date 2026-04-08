package java.com.islandescape.structures;
import  java.com.islandescape.player.Player;


//this will be abstract class from parent class worldstrucure for creating resources such as stone, tree, etc
public abstract class NaturalResource extends WorldStructure {
    protected String itemName;
    protected int health;

    public NaturalResource(int x, int y, String itemName, int health) {
        super(x, y);
        this.itemName = itemName;//here will be the material thst will be mined from specific structure(stone for example)
        this.health = health;//attribure health will be used as the counter of how many times is the structure has to be mined in order to get resource for player
    }
    @Override
    public abstract void interact(Player player);//main method that will base for intercating with player and overriden by each structure

    public int getHealth() {
        return health; //getter to get current health value
    }

}
