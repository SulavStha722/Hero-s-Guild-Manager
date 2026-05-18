import org.junit.jupiter.api.*;          // Imports @Test, @BeforeEach, etc.
import org.junit.jupiter.api.assertions.*; // Imports all assertions (assertEquals, assertNull, etc.) using wildcard

import java.util.ArrayList;
import java.util.List;

public class RosterAlgorithmsTest {

    private List<Hero> testDatabase;

    @BeforeEach
    public void setUp() {
        // This runs before every single test to give us a fresh, clean list of dummy heroes
        testDatabase = new ArrayList<>();
        testDatabase.add(new Warrior("Garen", 50, 140));
        testDatabase.add(new Mage("Luna", 80, 200));
        testDatabase.add(new Archer("Piper", 35, 110));
        testDatabase.add(new Warrior("Arin", 65, 170));
    }

    @Test
    public void testMergeSortByPower() {
        // Act: Perform our custom Merge Sort
        RosterAlgorithms.sortDatabaseByPower(testDatabase);

        // Assert: Verify that the list is sorted from HIGHEST power to LOWEST power
        Assertions.assertEquals(80, testDatabase.get(0).getPowerLevel(), "Highest power should be index 0 (Luna)");
        Assertions.assertEquals(65, testDatabase.get(1).getPowerLevel(), "Second highest should be index 1 (Arin)");
        Assertions.assertEquals(50, testDatabase.get(2).getPowerLevel(), "Third highest should be index 2 (Garen)");
        Assertions.assertEquals(35, testDatabase.get(3).getPowerLevel(), "Lowest power should be index 3 (Piper)");
    }

    @Test
    public void testBinarySearchHeroFound() {
        // Act: Search for a hero that absolutely exists in our setup
        Hero result = RosterAlgorithms.searchHeroByName(testDatabase, "Luna");

        // Assert: Ensure the search did not return null and found the correct stats
        Assertions.assertNotNull(result, "Hero should be found.");
        Assertions.assertEquals("Luna", result.getName());
        Assertions.assertEquals("Mage", result.getHeroClass());
    }

    @Test
    public void testBinarySearchHeroNotFound() {
        // Act: Search for a fake hero name
        Hero result = RosterAlgorithms.searchHeroByName(testDatabase, "DemonKingSpier");

        // Assert: Ensure the algorithm correctly handles a missing record by returning null
        Assertions.assertNull(result, "Search should return null for a non-existent hero.");
    }

    @Test
    public void testEdgeCaseEmptyList() {
        // Edge Case: What if the database is completely empty? Will it crash?
        List<Hero> emptyList = new ArrayList<>();
        
        // Assertions: Ensure our code handles this gracefully without throwing errors
        Assertions.assertDoesNotThrow(() -> RosterAlgorithms.sortDatabaseByPower(emptyList), 
            "Sorting an empty list should not throw an exception.");
            
        Assertions.assertNull(RosterAlgorithms.searchHeroByName(emptyList, "Garen"), 
            "Searching an empty list should safely return null.");
    }
}