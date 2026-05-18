import java.util.Random;

public class Quest {

    private String questName;
    private int difficultyLevel; // 1–100
    private int goldReward;
    private int reputationReward;

    public Quest(String questName, int difficultyLevel, int goldReward, int reputationReward) {
        this.questName = questName;
        this.difficultyLevel = difficultyLevel;
        this.goldReward = goldReward;
        this.reputationReward = reputationReward;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        if (difficultyLevel >= 1 && difficultyLevel <= 100) {
            this.difficultyLevel = difficultyLevel;
        }
    }

    public int getGoldReward() {
        return goldReward;
    }

    public void setGoldReward(int goldReward) {
        this.goldReward = goldReward;
    }

    public int getReputationReward() {
        return reputationReward;
    }

    public void setReputationReward(int reputationReward) {
        this.reputationReward = reputationReward;
    }

    // UPDATED: Now includes the 50/50 RNG luck mechanic for underpowered guilds
    public boolean attemptQuest(int teamPower) {
        if (teamPower >= difficultyLevel) {
            // Guaranteed Win: Your guild is strong enough
            return true;
        } else {
            // Underpowered: Flip a 50/50 coin for a lucky victory!
            System.out.println("Your guild is underpowered! Attempting a risky maneuver... (50/50 chance)");
            Random rng = new Random();
            return rng.nextBoolean(); // Returns true 50% of the time, false 50% of the time
        }
    }

    @Override
    public String toString() {
        return questName +
               " | Difficulty: " + difficultyLevel +
               " | Gold: " + goldReward +
               " | Reputation: " + reputationReward;
    }
}