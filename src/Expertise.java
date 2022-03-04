import java.util.Hashtable;

public class Expertise {
    private int ID;
    private String name;
    private int[] availableWeaponType;
    // monster id| damage multiplier
    private Hashtable<Integer,Float> advantage;

    public Expertise(int id, String name, int[] availableWeaponType, Hashtable<Integer,Float> advantage){
        this.ID = id;
        this.name = name;
        this.availableWeaponType = availableWeaponType;
        this.advantage = advantage;
    }

    public boolean validateWeaponAdaptable(Weapon weapon){
        for (int typeID:availableWeaponType) {
            if(weapon.getWeaponType() == typeID)
                return true;
        }
        return false;
    }

    public float getAttackEffectiveness(int id){
        float damageMultiplier = 1f;
        if(advantage.containsKey(id)){
            damageMultiplier = advantage.get(id);
        }
        return damageMultiplier;
    }

    public int getID(){
        return ID;
    }

    public String getName(){
        return name;
    };

    public int[] getAvailableWeaponType(){ return availableWeaponType; }
}