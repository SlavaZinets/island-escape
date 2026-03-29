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
    @Test
    public void Crafting() {
            CraftingTable table = new CraftingTable(0, 0);
            Item[] grid = new Item[4];
            grid[0] = new Item("Wood", "Material");
             grid[1] = new Item("Stone", "Material");
            grid[2] = new Item("Rope", "Material");

            Item result = table.Crafting(grid);
            assertNotNull(result, "fails");
            assertEquals("Axe", result.getName(), "supposwd to be axe");
    }


}
