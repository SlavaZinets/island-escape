import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class FIrePlaceTest {
    @Test
    public void interact() {

    }
    @Test
    public void light() {
        FirePlace fire = new FirePlace(100, 100);
        boolean success = fire.light(130, 130);
        assertTrue(success, "works if near the fireplace");
    }
    @Test
    public void addFuel() {
        FirePlace fire = new FirePlace(100, 100);
         fire.light(100, 100);
         double startTime = 100.0;
        Item wood = new Item("Wood stick");
        fire.addFuel(wood);
        assertTrue(fire.burnTimer > startTime, "Timer suppose to increase");
    }
    @Test
    public void update() {

    }


    @Test
    public void getWarmthAt() {


    }

}
