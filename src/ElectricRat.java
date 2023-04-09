import utilities.Dice;

public class ElectricRat extends Monster {
    /**
     * Title: ElectricRat.java
     * Abstract: One type of Monster, has different stats and phrase
     *           compared to other Monsters
     * Author: Aaron Bourdeaux
     * Date: 2023/03/15
     */

    // Set ranges for possible defense and attack values
    private static final int DEFENSE_MIN = 5;
    private static final int DEFENSE_MAX = 8;
    private static final int ATTACK_MIN = 5;
    private static final int ATTACK_MAX = 8;

    public ElectricRat(String name) {
        super(name, ElementalType.ELECTRIC);
        setDefenseMin(DEFENSE_MIN);
        setDefenseMax(DEFENSE_MAX);
        setAttackMax(ATTACK_MAX);
        setAttackMin(ATTACK_MIN);
        this.setAttackPoints();
        this.setDefensePoints();
    }

    // Sets defense points to a random value between DEFENSE_MIN and DEFENSE_MAX
    public void setDefensePoints() {
        this.defensePoints = Dice.roll(DEFENSE_MIN, DEFENSE_MAX);
    }

    // Sets attack points to a random value between ATTACK_MIN and ATTACK_MAX
    public void setAttackPoints() {
        this.attackPoints = Dice.roll(ATTACK_MIN, ATTACK_MAX);
    }



}
