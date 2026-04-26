public class Hero {

    // Private attributes
    private String name;
    private String heroClass;
    private int powerLevel;
    private int hiringCost;
    private boolean isAvailable;

    public Hero(String name, String heroClass, int powerLevel, int hiringCost) {
        this.name = name;
        this.heroClass = heroClass;
        this.powerLevel = powerLevel;
        this.hiringCost = hiringCost;
        this.isAvailable = true;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for heroClass
    public String getHeroClass() {
        return heroClass;
    }

    public void setHeroClass(String heroClass) {
        this.heroClass = heroClass;
    }

    // Getter and setter for powerLevel
    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    // Getter and setter for hiringCost
    public int getHiringCost() {
        return hiringCost;
    }

    public void setHiringCost(int hiringCost) {
        this.hiringCost = hiringCost;
    }

    // Getter and setter for isAvailable
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Makes the hero available again after resting.
     */
    public void rest() {
        isAvailable = true;
    }
}