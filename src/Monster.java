import java.util.Hashtable;

public class Monster {
    private int ID;
    private String name  = "";
    protected float maxHealth;
    protected float health = 100;
    protected float attack = 20;
    protected float speed = 3;
    // weapon type ID | damage multiplier
    protected Hashtable<Integer,Float> protection;

    public Monster(int id, String name, float hp, float atk, float spd){
        this.ID = id;
        this.name = name;
        this.maxHealth = hp;
        this.attack = atk;
        this.speed = spd;

        this.health = this.maxHealth;
    }

    public void startBattle(){
        health = maxHealth;
    }

    public void takeDamage(float damage, int weaponType){
        float damageMultiplier = 1f;
        if(protection.contains(weaponType)){
            damageMultiplier = protection.get(weaponType);
        }
        damage *= damageMultiplier;
        if(damage < 1) damage = 1;
        if(damage > health) damage = health;
        health -= damage;
        System.out.printf("%s 受到 %.1f 點傷害，血量變成 %.1f\n",name,damage,health);
    }

    public void takeDamage(float damage){
        if(damage > health) damage = health;
        health -= damage;
        System.out.println(name + "受到了" + damage + "點傷害，血量變成" + health);
    }

    public void sleep(){
        health += maxHealth * 0.35f;
        if(health > maxHealth) health = maxHealth;
        System.out.println(name + "睡了一覺，血量變成" + health);
    }

    public boolean isAlive(){
        return health > 0;
    }
    // Getters
    public String getName(){
        return name;
    }

    public int getId(){
        return ID;
    }

    public float getSpeed(){ return speed; }

    public float getMaxHealth(){ return maxHealth; }

    public float getCurrentHealth(){ return health; }

    public float getAttack() {
        return attack;
    }

    public boolean isFasterThanHunter(float hunterSpeed){
        return hunterSpeed < speed;
    }
}
