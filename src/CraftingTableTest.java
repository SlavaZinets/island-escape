import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class CraftingTableTest {
    @Test   public void interact() {

            CraftingTable table = new CraftingTable(100, 100);
            table.interact(100, 100);
            assertTrue(false); 

    }
@Test
    public void isPlayerNearby() {
    CraftingTable table = new CraftingTable(100.0, 100.0);
    boolean result = table.isPlayerNearby(100.0, 100.0);
    assertTrue(result, "true when player is on top of table");

    }



}
