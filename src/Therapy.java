public class Therapy extends Sorcery{
    private float recovery;

    public Therapy(int id, String name, String description, float manaCost, float effect) {
        super(id, name, description, manaCost);
        this.recovery = effect;
    }

    public void castHealing(Hunter target){
        System.out.println("獵人使出治療魔法 " + name + "!");
        target.takePercentageHealing(recovery);
    }
}
