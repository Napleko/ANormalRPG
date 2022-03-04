import java.util.Hashtable;

public class TerrestrialMonster extends Monster{
    public TerrestrialMonster(int id, String name, float hp,
                              float atk, float spd, Hashtable<Integer,Float> protection) {
        super(id, name, hp, atk, spd);
        this.protection = protection;
    }
}
