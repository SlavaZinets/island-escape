import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class FIrePlaceTest {
    @Test
    public void interact() {
        FirePlace fire = new FirePlace(50, 50);
        fire.interact(50, 50);
        assertTrue(fire.isLit(), "Fireplace is lit");
    }
    @Test
    public void light() {
        FirePlace fire = new FirePlace(100, 100);
        boolean success = fire.light(130, 130);
        assertTrue(success, "works if near the fireplace");
    }
    @Test
    public void addFuel() {

    }
    @Test
    public void update() {

    }


    @Test
    public void getWarmthAt() {


    }

}
