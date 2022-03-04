import java.util.Hashtable;

public class AquaticMonster extends Monster{
    public AquaticMonster(int id, String name, float hp,
                          float atk, float spd, Hashtable<Integer,Float> protection) {
        super(id, name, hp, atk, spd);
        this.protection = protection;
    }
}
