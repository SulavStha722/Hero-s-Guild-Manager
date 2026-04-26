import java.util.ArrayList;
import java.util.Random;

/**
 * Tavern.java
 * Generates heroes that can be hired by the player.
 */
public class Tavern {
    
    // Added 'final' here to fix the yellow warnings!
    private final ArrayList<Hero> availableHeroes;
    private final Random rng;

    private static final String[] HERO_CLASSES = {"Warrior", "Mage", "Rogue"};
    private static final String[] HERO_NAMES = {
            "Arin", "Borin", "Cleo", "Dara", "Eldon", "Faye", "Garen", "Hilda",
            "Ivan", "Jora", "Kael", "Luna", "Mira", "Nolan", "Orin", "Piper"
    };

    /**
     * Creates a new Tavern with an empty hero list.
     */
    public Tavern() {
        availableHeroes = new ArrayList<>();
        rng = new Random();
    }

    public ArrayList<Hero> getAvailableHeroes() {
        return availableHeroes;
    }

    /**
     * Clears the current list and generates 3 to 5 new random heroes.
     */
    public void generateDailyHeroes() {
        availableHeroes.clear();

        int numberOfHeroes = rng.nextInt(3) + 3; // 3 to 5 heroes

        for (int i = 0; i < numberOfHeroes; i++) {
            String name = HERO_NAMES[rng.nextInt(HERO_NAMES.length)];
            String heroClass = HERO_CLASSES[rng.nextInt(HERO_CLASSES.length)];
            int powerLevel = rng.nextInt(61) + 20; // 20 to 80 power
            int hiringCost = 40 + (powerLevel * 2); // proportionate cost

            availableHeroes.add(new Hero(name, heroClass, powerLevel, hiringCost));
        }
    }

    /**
     * Displays all heroes currently available in the tavern.
     */
    public void displayHeroes() {
        if (availableHeroes.isEmpty()) {
            System.out.println("The tavern has no heroes available right now.");
            return;
        }

        System.out.println("\nAvailable Tavern Heroes:");
        System.out.printf("%-5s %-12s %-10s %-10s %-10s%n", "No.", "Name", "Class", "Power", "Cost");
        System.out.println("---------------------------------------------------");

        for (int i = 0; i < availableHeroes.size(); i++) {
            Hero hero = availableHeroes.get(i);
            System.out.printf("%-5d %-12s %-10s %-10d %-10d%n",
                    i + 1,
                    hero.getName(),
                    hero.getHeroClass(),
                    hero.getPowerLevel(),
                    hero.getHiringCost());
        }
    }

    /**
     * Removes and returns a hero from the tavern list.
     *
     * @param index the zero-based index of the hero
     * @return the hired Hero object
     */
    public Hero hireHero(int index) {
        if (index < 0 || index >= availableHeroes.size()) {
            return null;
        }

        return availableHeroes.remove(index);
    }
}