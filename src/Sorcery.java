public class Sorcery {
    private int ID;
    protected String name;
    protected String description = "";
    protected float manaCost;

    public Sorcery(int id, String name, String description, float manaCost){
        this.ID = id;
        this.name = name;
        this.description = description;
        this.manaCost = manaCost;
    }

    public String getName(){
        return name;
    };

    public String getDescription(){
        return description;
    }

    public float getManaCost(){ return manaCost; }
}
