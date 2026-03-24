import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChestTest {

    @Test
    public void interact() {


    }
    @Test
    public void open() {

    }
    @Test
    public void takeItem() {
        Chest chest = new Chest(10.0, 10.0);
        double slotX = 1.0;
        double slotY = 1.0;


        assertTrue(chest.takeItem(slotX, slotY) != null, "not null");

    }

}
