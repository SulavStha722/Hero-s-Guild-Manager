import java.util.ArrayList;
import java.util.Random;

public class WorldBoard {

    private Random rng;
    private static final String[] QUEST_PREFIXES = {"Defeat the", "Find the", "Explore the", "Escort the"};
    private static final String[] QUEST_TARGETS = {"Goblin King", "Lost Relic", "Dark Cavern", "Merchant Caravan"};

    public WorldBoard() {
        rng = new Random();
    }

    public ArrayList<Quest> generateDailyQuests() {
        ArrayList<Quest> dailyQuests = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String name = QUEST_PREFIXES[rng.nextInt(QUEST_PREFIXES.length)] + " " + 
                          QUEST_TARGETS[rng.nextInt(QUEST_TARGETS.length)];
            
            // Reputation is strictly between 15 and 40
            int repReward = rng.nextInt(26) + 15; 
            
            // Difficulty scales with the reputation reward (High Rep = High Risk)
            int difficulty = (repReward * 2) + rng.nextInt(21); 
            
            int goldReward = difficulty * 2 + rng.nextInt(50);
            
            dailyQuests.add(new Quest(name, difficulty, goldReward, repReward));
        }
        return dailyQuests;
    }

    public void simulateRivalGuilds(ArrayList<Guild> rivals) {
        System.out.println("\n--- Rival Guild Activity ---");
        for (Guild rival : rivals) {
            if (rng.nextInt(100) < 30) {
                if (rival.getGold() >= 50) {
                    rival.setGold(rival.getGold() - 50);
                    rival.addHero(new Hero("Recruit", "Novice", rng.nextInt(20) + 10, 50));
                    System.out.println(rival.getGuildName() + " hired a new recruit!");
                }
            }
            if (rng.nextInt(100) < 40) {
                int goldGain = rng.nextInt(21) + 10;
                int repGain = rng.nextInt(11) + 5;
                rival.setGold(rival.getGold() + goldGain);
                rival.setReputation(rival.getReputation() + repGain);
                System.out.println(rival.getGuildName() + " completed a quest!");
            }
        }
    }

    public void printLeaderboard(Guild playerGuild, ArrayList<Guild> rivals) {
        ArrayList<Guild> allGuilds = new ArrayList<>();
        allGuilds.add(playerGuild);
        allGuilds.addAll(rivals);

        allGuilds.sort((g1, g2) -> Integer.compare(g2.getReputation(), g1.getReputation()));

        System.out.println("\n--- Current Standings ---");
        System.out.printf("%-5s %-20s %-12s %-10s %-10s%n", "Rank", "Guild Name", "Reputation", "Gold", "Roster");
        System.out.println("---------------------------------------------------------------");
        
        int rank = 1;
        for (Guild g : allGuilds) {
             System.out.printf("%-5d %-20s %-12d %-10d %-10d%n", 
                               rank,
                               g.getGuildName(), 
                               g.getReputation(), 
                               g.getGold(), 
                               g.getRoster().size());
             rank++;
        }
    }
}