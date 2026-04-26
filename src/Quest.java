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

    public boolean attemptQuest(int teamPower) {
        return teamPower >= difficultyLevel;
    }

    @Override
    public String toString() {
        return questName +
               " | Difficulty: " + difficultyLevel +
               " | Gold: " + goldReward +
               " | Reputation: " + reputationReward;
    }
}