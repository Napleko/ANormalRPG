public class Mount {
    private String name;
    private float defense = 0;
    private float speed = 1;

    public Mount(String name, float def, float spd){
        this.name = name;
        this.defense = def;
        this.speed = spd;
    }

    public String getName(){
        return name;
    };

    public float getDefense(){
        return defense;
    }

    public float getSpeed(){
        return speed;
    }
}
