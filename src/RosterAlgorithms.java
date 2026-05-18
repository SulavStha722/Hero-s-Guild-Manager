import java.util.ArrayList;
import java.util.List;

public class RosterAlgorithms {

    // ==========================================
    // 1. MERGE SORT (By Power Level - Descending)
    // ==========================================
    public static void sortDatabaseByPower(List<Hero> heroes) {
        // Base case: If the list is empty or has 1 item, it's already sorted
        if (heroes == null || heroes.size() <= 1) {
            return;
        }

        int mid = heroes.size() / 2;
        List<Hero> left = new ArrayList<>(heroes.subList(0, mid));
        List<Hero> right = new ArrayList<>(heroes.subList(mid, heroes.size()));

        sortDatabaseByPower(left);
        sortDatabaseByPower(right);
        mergeByPower(heroes, left, right);
    }

    private static void mergeByPower(List<Hero> original, List<Hero> left, List<Hero> right) {
        int i = 0, j = 0, k = 0;
        
        while (i < left.size() && j < right.size()) {
            // Sorts from Highest Power to Lowest Power
            if (left.get(i).getPowerLevel() >= right.get(j).getPowerLevel()) {
                original.set(k++, left.get(i++));
            } else {
                original.set(k++, right.get(j++));
            }
        }
        
        while (i < left.size()) { original.set(k++, left.get(i++)); }
        while (j < right.size()) { original.set(k++, right.get(j++)); }
    }


    // ==========================================
    // 2. BINARY SEARCH (By Hero Name)
    // ==========================================
    // IMPORTANT: Binary search ONLY works if the list is sorted alphabetically first.
    // So we include a quick alphabetical sort method here as well.
    
    public static void sortDatabaseByName(List<Hero> heroes) {
        if (heroes == null || heroes.size() <= 1) return;
        
        int mid = heroes.size() / 2;
        List<Hero> left = new ArrayList<>(heroes.subList(0, mid));
        List<Hero> right = new ArrayList<>(heroes.subList(mid, heroes.size()));

        sortDatabaseByName(left);
        sortDatabaseByName(right);
        mergeByName(heroes, left, right);
    }

    private static void mergeByName(List<Hero> original, List<Hero> left, List<Hero> right) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).getName().compareToIgnoreCase(right.get(j).getName()) <= 0) {
                original.set(k++, left.get(i++));
            } else {
                original.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) { original.set(k++, left.get(i++)); }
        while (j < right.size()) { original.set(k++, right.get(j++)); }
    }

    // The actual search method
    public static Hero searchHeroByName(List<Hero> heroes, String targetName) {
        // 1. Force the list to be sorted alphabetically first!
        sortDatabaseByName(heroes); 
        
        // 2. Perform the search
        int left = 0;
        int right = heroes.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Hero midHero = heroes.get(mid);
            
            int comparison = midHero.getName().compareToIgnoreCase(targetName);

            if (comparison == 0) {
                return midHero; // Found it!
            } else if (comparison < 0) {
                left = mid + 1; // It's in the right half
            } else {
                right = mid - 1; // It's in the left half
            }
        }
        return null; // Not found
    }
}