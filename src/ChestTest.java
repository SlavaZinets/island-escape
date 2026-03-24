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
        chest.open(10.0, 10.0);
        assertNotNull(chest.takeItem(5.0, 5.0));

    }

}
