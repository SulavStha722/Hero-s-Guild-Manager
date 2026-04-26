import java.util.ArrayList;

public class Guild {
    
    private String guildName;
    private int gold;
    private int reputation;
    private ArrayList<Hero> roster;

    public Guild(String guildName, int gold) {
        this.guildName = guildName;
        this.gold = gold;
        this.reputation = 0;
        this.roster = new ArrayList<>();
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }
    
    public ArrayList<Hero> getRoster() {
        return roster;
    }

    public void addHero(Hero h) {
        roster.add(h);
    }

    public int calculateTotalPower() {
        int totalPower = 0;
        for (Hero h : roster) {
            if (h.isAvailable()) {
                totalPower += h.getPowerLevel();
            }
        }
        return totalPower;
    }
}