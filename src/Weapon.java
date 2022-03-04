public class Weapon {
    private String name;
    private String description = "";
    private float speedMultiplier = 1f;
    private float attack;
    private int weaponType;

    public Weapon(String name, String description, float atk, float spdMultiplier, int typeID){
        this.name = name;
        this.description = description;
        this.attack = atk;
        this.speedMultiplier = spdMultiplier;
        this.weaponType = typeID;
    }

    public String getName(){
        return name;
    };

    public String getDescription(){
        return description;
    }

    public int getWeaponType(){
        return weaponType;
    }

    public float getAttack(){
        return attack;
    }

    public float getSpeedMultiplier(){
        return  speedMultiplier;
    }
}
