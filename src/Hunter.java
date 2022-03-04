import java.util.LinkedList;

public class Hunter {
    private String name = "";
    private float maxHealth;
    private float health;
    private float maxMana;
    private float mana;
    private float speed;
    private float defense;
    private int level;
    private Weapon weapon;
    private LinkedList<Sorcery> sorceryList;
    private Expertise expertise;
    private Mount mount;

    public Hunter(float hp, float spd, float def, float fp){
        this.maxHealth = hp;
        this.speed = spd;
        this.defense = def;
        this.maxMana = fp;
        this.level = 1;

        sorceryList = new LinkedList<Sorcery>();
        this.health = this.maxHealth;
        this.mana = this.maxMana;

        weapon = new Weapon("徒手","沒有武器，只有雙手",20,1.15f,0);
        mount = new Mount("新手坐騎",1.4f,0.5f);
        sorceryList.add(new Therapy(1,"初級治療術","簡易的治療魔法，+30%血量",30,0.3f));
    }

    /* battle functions */
    public void startBattle(){
        health = maxHealth;
        mana = maxMana;
    }

    public void attackEnemy(Monster target){
        System.out.printf("%s 使用 %s 攻擊 %s\n",name, weapon.getName(),target.getName());
        float expertiseEffectiveness = expertise.getAttackEffectiveness(target.getId());
        float damage = weapon.getAttack() * expertiseEffectiveness;
        if(expertiseEffectiveness != 1f)
            System.out.print("因職業專長，效果卓越！\n");
        target.takeDamage(damage, weapon.getWeaponType());
    }

    public void rideMount(Mount newMount){
        mount = newMount;

        System.out.printf("%s 騎上了坐騎 %s，防禦變為%.1f，速度變為%.1f\n",name,mount.getName(),getTotalDefense(),getTotalSpeed());
    }

    public void takeDamage(float damage){
        float finalDamage = damage - defense;
        if(finalDamage < 1) finalDamage = 1;
        if(finalDamage > health) finalDamage = health;
        health -= finalDamage;
        System.out.printf("%s 受到了 %.1f 點傷害，血量變成 %.1f\n\n",name,finalDamage,health);
    }

    public void takePercentageHealing(float recovery){
        float finalRecovery = Math.min(recovery * maxHealth, (maxHealth - health));
        health += finalRecovery;
        System.out.printf("血量回復了 %.1f，血量變為 %.1f",finalRecovery,health);
    }

    public void grabWeapon(Weapon newWeapon){
        if(expertise.validateWeaponAdaptable(newWeapon)){
            weapon = newWeapon;

            System.out.printf("%s 拿起了武器：%s(%.1f)，速度變為%.1f\n",
                    name,newWeapon.getName(),newWeapon.getAttack(),getTotalSpeed());
        }
        else
            System.out.printf("%s 無法使用武器：%s\n",name,newWeapon.getName());
    }

    public void learnSorcery(Sorcery spell){
        if(!sorceryList.contains(spell)){
            sorceryList.add(spell);
            System.out.printf("✔ %s 學會了魔法：%s\n",name,spell.getName());
        }
    }

    public void rest(){
        float finalRecovery = Math.min(50, (maxHealth - health));
        health += finalRecovery;

        float finalMana = Math.min(maxMana /2, maxMana - mana);
        mana += finalMana;

        System.out.printf("%s 吃了仙丹，睡了一覺，血量回復了 %.1f，血量變為 %.1f； 魔力回復了 %.1f\n",
                name, finalRecovery, health, finalMana);
    }

    public void levelUp(){
        if(level > 20) {
            maxHealth += GameDriver.getRandomFloat(4f, 6f);
            maxMana += GameDriver.getRandomFloat(3f, 4.5f);
            defense += GameDriver.getRandomFloat(0.1f, 0.3f);
        }
        else{
            maxHealth += GameDriver.getRandomFloat(8f, 12f);
            maxMana += GameDriver.getRandomFloat(6f, 9f);
            defense += GameDriver.getRandomFloat(0.2f, 0.5f);
        }
        level++;
        System.out.printf("%s 提升到LV.%d，血量變為%.1f，魔力變為%.1f，基本防禦變為%.1f\n",
                name, level, maxHealth, maxMana, defense);
    }
    // getters
    public boolean isAlive(){
        return health > 0;
    }

    public float getTotalSpeed(){
        float totalSpeed = speed;
        if(mount != null) totalSpeed += mount.getSpeed();
        if(weapon != null) totalSpeed *= weapon.getSpeedMultiplier();

        return totalSpeed;
    }

    public float getMaxHealth(){ return maxHealth; }

    public float getCurrentHealth(){ return health; }

    public float getTotalDefense(){
        float totalDefense = defense;
        if(mount != null){
            totalDefense += mount.getDefense();
        }
        return totalDefense;
    }

    public float getCurrentMana(){ return mana; }

    public float getMaxMana(){ return maxMana; }

    public String getWeaponName(){ return weapon.getName(); }

    public String getMountName(){
        if(mount==null) return "無";
        return mount.getName();
    }

    public String getExpertiseName(){
        return expertise.getName();
    }

    public LinkedList<Sorcery> getSorceryList(){ return sorceryList; }

    public int getSorceryAmount(){
        return sorceryList.size();
    }

    public float getAttack(){ return weapon.getAttack(); }

    public String getName(){ return name; }

    public int getLevel(){ return level; }
    // setter
    public void setName(String name){ this.name = name; }

    public void setExpertise(Expertise exp){ this.expertise = exp; }

    public Expertise getExpertise(){ return expertise; }

    public void costMana(float cost){
        mana -= cost;
    }
}