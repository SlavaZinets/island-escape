import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class CraftingTableTest {
    @Test   public void interact() {

    }
@Test
    public void isPlayerNearby() {
    CraftingTable table = new CraftingTable(100.0, 100.0);
    boolean result = table.isPlayerNearby(100.0, 100.0);
    assertTrue(result, "true when player is on top of table");

    }



}
