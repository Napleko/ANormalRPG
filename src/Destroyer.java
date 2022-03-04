public class Destroyer extends Sorcery{
    private float magicAttack;

    public Destroyer(int id, String name, String description, float manaCost, float atk) {
        super(id, name, description, manaCost);
        this.magicAttack = atk;
    }

    public void doDamage(Monster target){
        System.out.println("獵人使出破壞魔法 " + name + "!");
        target.takeDamage(magicAttack);
    }
}
