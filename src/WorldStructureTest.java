import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class WorldStructureTest {
    @Test
    public void interact() {
           //create class here as world structure is abstract one
        class TestStructure extends WorldStructure {
            public TestStructure(double x, double y, String name) {
                super(x, y, name);
            }
        }
        TestStructure structure = new TestStructure(100, 200, "Chest");
            structure.interact(50, 60);
        assertTrue(true);
    }
    @Test
    public void isPlayerInRange() {

    }
}
