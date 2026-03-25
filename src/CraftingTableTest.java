import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class CraftingTableTest {
    @Test   public void interact() {

    }
@Test
    public void isPlayerNearby() {


    }


    @Test
    public void isPlayerInRange() {
        CraftingTable table = new CraftingTable(100, 100);
        boolean result = table.isPlayerInRange(100, 100);
          assertTrue(result, "Fail: player at table");

    }
}
