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
        FirePlace fire = new FirePlace(100, 100);
        fire.light(100, 100);
        fire.update(10.0);
        assertEquals(40.0, fire.burnTimer, 0.1);
    }


    @Test
    public void getWarmthAt() {
        FirePlace fire = new FirePlace(100, 100);
        fire.light(100, 100);
        double warmth = fire.getWarmthAt(240, 100);
        assertTrue(warmth > 0, " warmer near the fireplace");

    }
    @Test
    public void cook() {
        FirePlace fire = new FirePlace(100, 100);
        Item rawFood = new Item("Raw Fish");
        fire.light(100, 100);
        Item result = fire.cook(rawFood); //cookin
        //if result null or no match for nsme- falls
        assert result != null && result.getName().equals("Cooked");
    }

}
