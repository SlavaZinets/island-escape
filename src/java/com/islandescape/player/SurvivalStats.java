package com.islandescape.player;
import com.islandescape.item.Item;
import com.islandescape.item.ItemType;
public class SurvivalStats {
    public double hunger = 100.0;//variable of hunger level
    public double thirst = 100.0;//variable of thirst level
    public boolean isAlive = true;//alive state true or false
    // Timestamp for next hunger/thirst state decrease in one minute after current time (in 60 seconds)
    public long nextDropTime = System.currentTimeMillis() + 60000;

    //update hunger/thirst state after 60 sec or if states are 0 - no alive automaticaly
    public void update() {

    }
//increasing states when eat or dring sonething
    public void consume(Item item) {

    }
}
