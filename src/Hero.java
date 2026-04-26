public abstract class Hero {
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

    // Abstract method to demonstrate Polymorphism
    public abstract void attack();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getHeroClass() { return heroClass; }
    public void setHeroClass(String heroClass) { this.heroClass = heroClass; }
    
    public int getPowerLevel() { return powerLevel; }
    public void setPowerLevel(int powerLevel) { this.powerLevel = powerLevel; }
    
    public int getHiringCost() { return hiringCost; }
    public void setHiringCost(int hiringCost) { this.hiringCost = hiringCost; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public void rest() { isAvailable = true; }
}


class Warrior extends Hero {
    public Warrior(String name, int powerLevel, int hiringCost) {
        super(name, "Warrior", powerLevel, hiringCost);
    }

    @Override
    public void attack() {
        System.out.println(getName() + " fights with a heavy sword!");
    }
}

class Mage extends Hero {
    public Mage(String name, int powerLevel, int hiringCost) {
        super(name, "Mage", powerLevel, hiringCost);
    }

    @Override
    public void attack() {
        System.out.println(getName() + " casts a powerful elemental spell!");
    }
}

class Archer extends Hero {
    public Archer(String name, int powerLevel, int hiringCost) {
        super(name, "Archer", powerLevel, hiringCost);
    }

    @Override
    public void attack() {
        System.out.println(getName() + " attacks with precision using a bow and arrows!");
    }
}