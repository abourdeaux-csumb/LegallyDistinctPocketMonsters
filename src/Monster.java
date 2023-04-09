import utilities.Dice;

import java.util.ArrayList;
import java.util.List;

public abstract class Monster {
    /**
     * Title: Monster.java
     * Abstract: Parent class to different pocket monster types, facilitates
     *           logic to simulate battles between different pocket monsters
     * Author: Aaron Bourdeaux
     * Date: 2023/03/15
     */
    public String getName() {
        return this.name;
    }

    public int getAttackMin() {

        return attackMin;
    }

    public void setAttackMin(int attackMin) {

        this.attackMin = attackMin;
    }

    public int getAttackMax() {

        return attackMax;
    }

    public void setAttackMax(int attackMax) {

        this.attackMax = attackMax;
    }

    public int getAttackPoints() {
        return this.attackPoints;
    }

    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    public int getDefenseMax() {
        return defenseMax;
    }

    public void setDefenseMax(int defenseMax) {
        this.defenseMax = defenseMax;
    }

    public int getDefensePoints() {
        return this.defensePoints;
    }

    public void setDefensePoints (int defensePoints) {
        this.defensePoints = defensePoints;
    }

    public int getDefenseMin() {
        return defenseMin;
    }

    public void setDefenseMin(int defenseMin) {
        this.defenseMin = defenseMin;
    }

    // Return value of phrase repeated once
    public String getPhrase() {
        return phrase + " " + phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public double getHealthPoints() {

        return this.healthPoints;
    }

    public void setHealthPoints(double healthPoints) {
        this.healthPoints = healthPoints;
    }

    public boolean isFainted() {

        return this.fainted;
    }

    public void setFainted(boolean fainted) {

        this.fainted = fainted;
    }

    public List<ElementalType> getElements() {

        return this.elements;
    }
   
    protected enum ElementalType {
        ELECTRIC,
        FIRE,
        GRASS,
        WATER,
    }

    // Fields set with default values
    private String name = "";
    private String phrase = "";
    private List<ElementalType> elements = new ArrayList<ElementalType>();
    private double MAX_HP = 20.0;
    private double healthPoints = MAX_HP;
    private boolean fainted = false;
    protected int defensePoints = 10;
    private int defenseMin = 1;
    private int defenseMax = 10;
    protected int attackPoints = 10;
    private int attackMin = 1;
    private int attackMax = 10;

    public Monster(String name, ElementalType type) {
        this.name = name;
        this.elements = new ArrayList<ElementalType>();
        this.elements.add(type);
        this.setPhrase(this);
    }

    // Sets the phrase based on the type of monster passed in
    protected Monster setPhrase(Monster monster) {
        if (monster instanceof ElectricRat) {
            monster.setPhrase("'Lectric!");
        }
        else if (monster instanceof FireLizard) {
            monster.setPhrase("Deal with it");
        }
        else if (monster instanceof FlowerDino) {
            monster.setPhrase("Flowah!");
        }
        else if (monster instanceof WeirdTurtle) {
            monster.setPhrase("'Urtle!");
        }
        else {
            monster.setPhrase("No phrase for me!");
        }
        return monster;
    }

    // Logic for attacking another monster. Prints out the outcome of the attack
    // returning the total damage calculated.
    public double attack (Monster monster) {
        // Check to see if monster is capable of attacking (i.e. it hasn't fainted)
        if (this.isFainted()) {
            System.out.println(this.name + " isn't conscious... it can't attack.");
            return 0.0;
        }
        System.out.println(this.name + " is attacking " + monster.getName());
        System.out.println(this.getPhrase());
        double attackPoints = calculateAttackPoints(this, monster.getElements());
        System.out.println(this.name + " is attacking with " + attackPoints + " attack points!");
        double damage = monster.takeDamage(attackPoints);
        // Check to see if the monster is attacking itself
        if (this.equals(monster) && damage > 0) {
            System.out.println(this.name + " hurt itself in the confusion.");
        }
        return damage;
    }

    // Logic for taking damage from an attack
    public double takeDamage (double attackValue) {
        double defensePoints = calculateDefensePoints(this);
        attackValue -= defensePoints;
        // Set health based on an attack with an attack value > 0
        if (attackValue > 0) {
            System.out.println(this.name + " is hit for " + attackValue + " damage!");
            this.setHealthPoints(this.getHealthPoints() - attackValue);
        }
        // Don't take damage if attackValue is 0
        else if (attackValue == 0) {
            System.out.println(this.name + " is nearly hit!");
        }
        // Don't take damage if the attack value is small relative to defense points
        else if ((attackValue + defensePoints) < (defensePoints / 2)) {
            System.out.println(this.name + " shrugs off the puny attack.");
        }
        double healthPoints = this.getHealthPoints();
        // Faint if health is <= 0
        if (healthPoints <= 0) {
            System.out.println(this.name + " has faint-- passed out. It's passed out.");
            this.setFainted(true);
        }
        // Print out health if new value is > 0
        else {
            System.out.println(this.name + " has " + healthPoints + "/" + this.MAX_HP + " HP remaining");
        }
        return attackValue;
    }

    // Calculates defense points based on range of possible defense values
    protected double calculateDefensePoints (Monster monster) {
        int defenseValue = Dice.roll(monster.getDefenseMin(), monster.getDefenseMax());
        // Defense boost where defense value is even and exceptionally small
        if (defenseValue % 2 == 0 && defenseValue < monster.getDefenseMax() / 2) {
            defenseValue += 1;
            defenseValue *= 2;
            System.out.println(monster.getName() + " finds courage in the desperate situation");
        }
        // Message when defense value is set to the minimum possible value
        else if (defenseValue == this.defenseMin) {
            System.out.println(this.name + " is clearly not paying attention.");
        }
        return defenseValue;
    }

    // Calculate attack points based on range of possible attack values
    public double calculateAttackPoints (Monster monster, List<ElementalType> enemyType) {
        int attackValue = Dice.roll(monster.getAttackMin(), monster.getAttackMax());
        double modifier = 1.0;
        System.out.println(name + " rolls a " + attackValue);
        // Calculate attack value in relation to attackModifier
        for (ElementalType type : enemyType) {
            modifier *= attackModifier(type);
        }
        // Print message indicating exceptionally high attack modifier
        if (modifier >= 2.0) {
            System.out.println("It's su-- *cough* very effective!");
        }
        return attackValue * modifier;
    }

    // Determine attack modifier based on relative type weaknesses and strengths
    protected double attackModifier (ElementalType defending) {
        double modifier = 1.0;
        if (defending == ElementalType.ELECTRIC) {
            if (elements.contains(ElementalType.ELECTRIC)) {
                modifier *= 0.5;
            } else if (elements.contains(ElementalType.FIRE)) {
                modifier *= 1.0;
            } else if (elements.contains(ElementalType.GRASS)) {
                modifier *= 1.0;
            } else if (elements.contains(ElementalType.WATER)) {
                modifier *= 1.0;
            }
        }
        else if (defending == ElementalType.FIRE) {
            if (elements.contains(ElementalType.ELECTRIC)) {
                modifier *= 1.0;
            } else if (elements.contains(ElementalType.FIRE)) {
                modifier *= 0.5;
            } else if (elements.contains(ElementalType.GRASS)) {
                modifier *= 0.5;
            } else if (elements.contains(ElementalType.WATER)) {
                modifier *= 2.0;
            }
        }
        else if (defending == ElementalType.GRASS) {
            if (elements.contains(ElementalType.ELECTRIC)) {
                modifier *= 0.5;
            } else if (elements.contains(ElementalType.FIRE)) {
                modifier *= 2.0;
            } else if (elements.contains(ElementalType.GRASS)) {
                modifier *= 0.5;
            } else if (elements.contains(ElementalType.WATER)) {
                modifier *= 0.5;
            }
        }
        else if (defending == ElementalType.WATER) {
            if (elements.contains(ElementalType.ELECTRIC)) {
                modifier *= 2.0;
            } else if (elements.contains(ElementalType.FIRE)) {
                modifier *= 0.5;
            } else if (elements.contains(ElementalType.GRASS)) {
                modifier *= 2.0;
            } else if (elements.contains(ElementalType.WATER)) {
                modifier *= 0.5;
            }
        }
        return modifier;
    }

    // Print health, status, and type information for the monster
    public String toString() {
        String returnString = "";
        if (!fainted) {
            returnString += name + " has " + healthPoints + "/" + MAX_HP + " hp.\n";
        }
        else {
            returnString += name + " has fainted.\n";
        }
        returnString += "Elemental type: " + elements;
        return returnString;
    }

    // Sets the type of the monster
    public int setType (ElementalType type) {
        // Don't add type to elements if it already is present
        if (this.elements.contains(type)) {
            System.out.println(type + " already set!");
            return 1;
        }
        // Don't add type if it conflicts with a type that is present
        else if (attackModifier(type) > 1.0) {
            System.out.println("Can't have conflicting types!");
            return -1;
        }
        // Otherwise, print message indicating type has been added
        else {
            System.out.println(this.name + " now has " + type);
            elements.add(type);
            return 0;
        }
    }

    // Implemented by child types to set defense points based on range of possible values
    abstract void setDefensePoints();

    // Implemented by child types to set attack points based on range of possible values
    abstract void setAttackPoints();

}
