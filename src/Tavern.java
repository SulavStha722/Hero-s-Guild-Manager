import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Tavern {
    private final ArrayList<Hero> availableHeroes;
    private final Random rng;

    private static final String[] HERO_CLASSES = {"Warrior", "Mage", "Archer"};
    private static final String[] HERO_NAMES = {
            "Arin", "Borin", "Cleo", "Dara", "Eldon", "Faye", "Garen", "Hilda",
            "Ivan", "Jora", "Kael", "Luna", "Mira", "Nolan", "Orin", "Piper"
    };

    public Tavern() {
        availableHeroes = new ArrayList<>();
        rng = new Random();
    }

    public ArrayList<Hero> getAvailableHeroes() {
        return availableHeroes;
    }

    public void generateDailyHeroes() {
        availableHeroes.clear();
        int numberOfHeroes = rng.nextInt(3) + 3;

        List<String> shuffledNames = Arrays.asList(HERO_NAMES);
        Collections.shuffle(shuffledNames, rng);

        for (int i = 0; i < numberOfHeroes; i++) {
            String name = shuffledNames.get(i); 
            String heroClass = HERO_CLASSES[rng.nextInt(HERO_CLASSES.length)];
            int powerLevel = rng.nextInt(61) + 20; 
            int hiringCost = 40 + (powerLevel * 2);

            if (heroClass.equals("Warrior")) availableHeroes.add(new Warrior(name, powerLevel, hiringCost));
            else if (heroClass.equals("Mage")) availableHeroes.add(new Mage(name, powerLevel, hiringCost));
            else availableHeroes.add(new Archer(name, powerLevel, hiringCost));
        }
    }

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
                    i + 1, hero.getName(), hero.getHeroClass(), hero.getPowerLevel(), hero.getHiringCost());
        }
    }

    public Hero hireHero(int index) {
        if (index < 0 || index >= availableHeroes.size()) {
            return null;
        }
        return availableHeroes.remove(index);
    }
}