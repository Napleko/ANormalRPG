import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

public class GameDriver {
    /*  Weapon Type :
        (0) bare hand (1) sword (2) bow (3) spear (4) hammer (5) axe
    * */
    static Scanner scanner;
    static LinkedList<Weapon> weaponList;
    static LinkedList<Expertise> expertiseList;
    static LinkedList<Monster> monsterList;
    static LinkedList<Mount> mountList;
    static LinkedList<Sorcery> sorceryList;

    static LinkedList<Mount> mountInventory;
    static LinkedList<Weapon> weaponInventory;
    static final String[] weaponTypeName = {"拳","劍","弓","矛","錘","斧"};

    private static Hunter player;
    private static Monster defender;
    private static final int elixirMaxAmount = 3;
    private static int elixirStock;
    private static int coin;
    private static int currentTowerLayer = 0;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        // generate items, mobs and mounts
        generateWeapons();
        generateMonsters();
        generateMount();
        generateSorcery();
        generateExpertises();
        // Init player's status
        initiatePlayerItem();
        initiatePlayerInventory();

        player = new Hunter(getRandomInt(90,110), getRandomFloat(3.5f,4.5f),
                getRandomFloat(0f,2f), getRandomFloat(45f,55f));

        // GUI displacement (no time for GUI, Java Swing is trash)
        showTitle();
    }

    /* init game process*/
    public static void initiatePlayerItem(){
        elixirStock = 1;
        coin = 10;
    }

    public static void initiatePlayerInventory(){
        mountInventory = new LinkedList<>();
        mountInventory.add(mountList.get(0));
        weaponInventory = new LinkedList<>();
        weaponInventory.add(weaponList.get(0));
    }

    public static void showTitle(){
        System.out.print(
                "~歡迎來到普通的RPG~\n" +
                "請輸入獵人名稱："
        );

        String input = scanner.nextLine();
        player.setName(input);
        System.out.print("\n[請選擇職業]\n");
        chooseExpertise();
        showMainMenu();
    }

    public static void chooseExpertise(){
        int index = 0;
        for(Expertise exp: expertiseList){
            System.out.print(++index + "." + exp.getName() + "\n");
        }
        System.out.print("請輸入職業編號：");
        String input = scanner.nextLine();
        int expIndex = 0;
        try{
            expIndex = Integer.parseInt(input);
        }
        catch (NumberFormatException e){
            System.out.print("[無此職業編號！]\n");
            expIndex = -1;
        }
        if(expIndex <= 0 || expIndex > expertiseList.size()) chooseExpertise();
        else player.setExpertise(expertiseList.get(expIndex-1));
    }

    /* main game process */
    public static void showMainMenu(){
        System.out.print(
                "\n------主選單------\n" +
                "1. 查看玩家資料\n" +
                "2. 開啟背包\n" +
                "3. 進入商店\n" +
                "4. 開始戰鬥\n" +
                "X. 結束遊戲\n" +
                "輸入功能碼："
        );

        String input = scanner.nextLine();
        switch (input){
            case "1":
                viewPlayerStatus();
                break;
            case "2":
                viewInventories();
                break;
            case "3":
                viewStore();
                break;
            case "4":
                selectTowerLayer();
                break;
            case "X":
                return;
            default:
                System.out.print("[指令錯誤，請重新輸入！]\n");
                showMainMenu();
        }
    }

    public static void viewPlayerStatus(){
        System.out.printf(
                "\n-----玩家資料-----" +
                "\n稱號：等級%d %s %s" +
                "\n血量：%.1f\t| 防禦：%.1f" +
                "\n速度：%.1f\t| 魔力：%.1f" +
                "\n仙丹數：%d\t| 金幣：%d" +
                "\n武器：%s(%.1f)\t| 坐騎：%s" +
                "\n已習得魔法：%d" +
                "\n\n↪按Enter返回主選單", player.getLevel(),
                player.getExpertiseName(), player.getName(),
                player.getMaxHealth(),player.getTotalDefense(),
                player.getTotalSpeed(),player.getMaxMana(),
                elixirStock, coin, player.getWeaponName(),
                player.getAttack(), player.getMountName(),
                player.getSorceryAmount()
        );
        scanner.nextLine();
        showMainMenu();
    }

    public static void viewInventories(){
        System.out.print(
                "\n-------背包-------\n" +
                        "1. 武器背包\n" +
                        "2. 魔法記憶\n" +
                        "3. 坐騎倉庫\n" +
                        "其他. ↪返回主選單\n" +
                        "輸入功能碼："
        );

        String input = scanner.nextLine();
        switch (input){
            case "1":
                viewWeaponInventory();
                viewInventories();
                break;
            case "2":
                viewSorceryInventory();
                viewInventories();
                break;
            case "3":
                viewMountInventory();
                viewInventories();
                break;
            default:
                showMainMenu();
        }
    }

    public static void viewStore(){
        int elixirPrice = 3 + 7 * player.getLevel();
        int weaponPrice = 72 + 48 * weaponInventory.size();
        int magicPrice = 20 + 10 * player.getSorceryAmount();
        int mountPrice = 20 + 12 * mountInventory.size();
        int upgradePrice = 10 + 5 * player.getLevel();
        System.out.printf(
                "\n-------商店-------\n" +
                "金幣：%d\n" +
                "(1) $%d 1顆仙丹(%d/%d)\n" +
                "(2) $%d 新武器\n" +
                "(3) $%d 學習魔法\n" +
                "(4) $%d 新坐騎\n" +
                "(5) $15 換職業\n" +
                "(6) $%d 玩家升級\n" +
                "(其他) ↪返回主選單\n" +
                "輸入功能碼：",
                coin ,elixirPrice, elixirStock, elixirMaxAmount,
                weaponPrice, magicPrice, mountPrice, upgradePrice
        );

        String input = scanner.nextLine();
        switch (input){
            case "1":
                buyElixir(elixirPrice);
                break;
            case "2":
                buyWeapon(weaponPrice);
                break;
            case "3":
                buySorcery(magicPrice);
                break;
            case "4":
                buyMount(mountPrice);
                break;
            case "5":
                buyExpertise();
                break;
            case "6":
                buyUpgrade(upgradePrice);
                break;
            default:
                showMainMenu();
                return;
        }

        System.out.print("↪ 按Enter返回商店");
        scanner.nextLine();
        viewStore();
    }

    public static void selectTowerLayer(){
        int towerLayerLimit = currentTowerLayer < monsterList.size()
                ? currentTowerLayer + 1 : currentTowerLayer;
        System.out.printf("\n現在能夠到達的層數：%d\n前往層數：", towerLayerLimit);
        String input = scanner.nextLine();
        int towerIndex = 0;
        try{
            towerIndex = Integer.parseInt(input);
            if(towerIndex > towerLayerLimit || towerIndex < 1){
                System.out.print("[輸入層數錯誤]\n");
                scanner.nextLine();
                showMainMenu();
            }
            enterTower(towerIndex);
            return;
        }
        catch (NumberFormatException | IndexOutOfBoundsException ignored){ }
        System.out.print("[輸入層數錯誤]\n");
        scanner.nextLine();
        showMainMenu();
    }

    public static void enterTower(int index){
        defender = monsterList.get(index-1);
        System.out.printf("%s 遭遇 第%d層之怪物 %s！\n[Enter後開始戰鬥]",
                player.getName(), index, defender.getName());
        defender.startBattle();
        player.startBattle();
        scanner.nextLine();
        /* BATTLE */
        int round = 1;
        while(player.isAlive() && defender.isAlive()){
            System.out.printf("------第%d回合------",round++);

            if(defender.isFasterThanHunter(player.getTotalSpeed())){
                System.out.print("\n");
                player.takeDamage(defender.getAttack());
                System.out.print("------Enter-------");
                scanner.nextLine();
                if(!player.isAlive()) break;
                choosePlayerAction();
            }
            else{
                choosePlayerAction();
                System.out.print("------Enter-------");
                scanner.nextLine();
                if(!defender.isAlive()) break;
                player.takeDamage(defender.getAttack());
            }
            System.out.print("------Enter-------");
            scanner.nextLine();
        }
        /* End of BATTLE */
        int baseCoin = 10 + index * 6;
        int minCoin = (int)(baseCoin * 0.7f);
        int maxCoin = (int)(baseCoin * 1.3f);
        if(player.isAlive()){
            int rewardCoin = getRandomInt(minCoin , maxCoin);
            coin += rewardCoin;
            if(index > currentTowerLayer) currentTowerLayer++;
            System.out.printf("%s 擊敗了 %s，獲得 $%d\n",player.getName(),defender.getName(),rewardCoin);
            // chance for player to level up
            if(getRandomInt(0,100) > 99) player.levelUp();
        }
        else{
            int lostCoin = (int)(getRandomInt(minCoin , maxCoin) * 0.5f);
            coin -= lostCoin;
            System.out.printf("%s 被 %s 擊敗了，損失 $%d\n",player.getName(),defender.getName(),lostCoin);
        }
        System.out.print("↪ 按Enter結束戰鬥");
        scanner.nextLine();
        showMainMenu();
    }

    static void choosePlayerAction(){
        System.out.printf("\n-------動作-------\n" +
                        "[%s] 血%.1f/%.1f | 魔%.1f/%.1f | 防%.1f | 速 %.1f\n" +
                        "[%s] 血%.1f/%.1f | 攻%.1f | 速 %.1f\n" +
                        "(1) 攻擊(%.1f)\n" +
                        "(2) 使用法術\n" +
                        "(3) 更換武器\n" +
                        "(4) 更換坐騎\n" +
                        "(5) 休息吃仙丹(%d/%d)\n" +
                        "輸入功能碼：",
                player.getName(), player.getCurrentHealth(),
                player.getMaxHealth(), player.getCurrentMana(),
                player.getMaxMana(), player.getTotalDefense(),
                player.getTotalSpeed(), defender.getName(),
                defender.getCurrentHealth(), defender.getMaxHealth(),
                defender.getAttack(), defender.getSpeed(),
                player.getAttack(), elixirStock, elixirMaxAmount
        );

        String input = scanner.nextLine();
        System.out.print("------------------\n");
        switch (input){
            case "1":
                player.attackEnemy(defender);
                break;
            case "2":
                castSpell();
                break;
            case "3":
                viewWeaponInventoryInBattle();
                choosePlayerAction();
                break;
            case "4":
                viewMountInventoryInBattle();
                choosePlayerAction();
                break;
            case "5":
                useElixir();
                choosePlayerAction();
                break;
            default:
                System.out.print("[輸入格式錯誤，請重新輸入！]\n");
                choosePlayerAction();
        }
    }

    static void useElixir(){
        if(elixirStock < 1){
            System.out.print("[仙丹數量不足，無法休息！\n]");
        }
        else{
            elixirStock--;
            player.rest();
        }
    }

    static void castSpell(){
        readSorceryInventory();
        System.out.print("其他. 返回上一層\n輸入欲使用之法術：");
        String input = scanner.nextLine();
        try {
            int index = Integer.parseInt(input);
            Sorcery sorcery = player.getSorceryList().get(index-1);
            if(player.getCurrentMana() < sorcery.getManaCost()){
                System.out.print("[剩餘魔力不足！]\n");
                choosePlayerAction();
                return;
            }
            player.costMana(sorcery.getManaCost());
            if(sorcery.getClass().getName().equals("Destroyer")){
                Destroyer destroyer = (Destroyer) sorcery;
                destroyer.doDamage(defender);
            } else{
              Therapy therapy = (Therapy) sorcery;
              therapy.castHealing(player);
            }
        }catch (NumberFormatException e){
            choosePlayerAction();
        }catch (IndexOutOfBoundsException e){
            System.out.print("[魔法編號錯誤！]\n");
            choosePlayerAction();
        }
    }

    /* buy any things */
    static void buyElixir(int price){
        if(elixirStock >= elixirMaxAmount){
            System.out.print("[仙丹數量已達上限]\n");
        }
        else if(coin < price){
            System.out.print("[餘額不足]\n");
        }
        else{
            coin -= price;
            elixirStock += 1;
            System.out.print("✔ 仙丹 購買成功\n");
        }
    }

    static void buyWeapon(int price){
        if(weaponInventory.size() == weaponList.size()){
            System.out.print("[武器已售完]\n");
        }
        else if(coin < price){
            System.out.print("[餘額不足]\n");
        }
        else{
            coin -= price;
            if(weaponInventory.size()==1){
                for(int i=0;i<5;i++) {
                    Weapon newWeapon = weaponList.get(weaponInventory.size());
                    weaponInventory.add(newWeapon);
                    System.out.printf("✔ %s 購買成功\n", newWeapon.getName());
                }
            }
            else{
                for(int i=0;i<6;i++) {
                    Weapon newWeapon = weaponList.get(weaponInventory.size());
                    weaponInventory.add(newWeapon);
                    System.out.printf("✔ %s 購買成功\n", newWeapon.getName());
                }
            }
        }
    }

    static void buySorcery(int price){
        if(player.getSorceryAmount() == sorceryList.size()){
            System.out.print("[魔法已售完]\n");
        }
        else if(coin < price){
            System.out.print("[餘額不足]\n");
        }
        else{
            coin -= price;
            Sorcery newMagic = sorceryList.get(player.getSorceryAmount());
            player.learnSorcery(newMagic);
        }
    }

    static void buyMount(int price){
        if(mountInventory.size() == mountList.size()){
            System.out.print("[坐騎已售完]\n");
        }
        else if(coin < price){
            System.out.print("[餘額不足]\n");
        }
        else{
            coin -= price;
            Mount newMount = mountList.get(mountInventory.size());
            mountInventory.add(newMount);
            System.out.printf("✔ %s 購買成功\n",newMount.getName());
        }
    }

    static void buyExpertise(){
        if(coin < 15){
            System.out.print("[餘額不足]\n");
        }
        else{
            coin -= 15;
            chooseExpertise();
            System.out.printf("✔ 職業已更換成：%s\n",player.getExpertiseName());
        }
    }

    static void buyUpgrade(int price){
        if(coin < price){
            System.out.print("[餘額不足]\n");
        }
        else{
            coin -= price;
            player.levelUp();
        }
    }
    /* function of inventories*/

    static void viewWeaponInventory(){
        readWeaponInventory();
        System.out.print("\n[輸入(武器編號)以切換武器，或返回上一層]\n");

        String input = scanner.nextLine();
        int weaponIndex = 0;
        try{
            weaponIndex = Integer.parseInt(input);
            player.grabWeapon(weaponInventory.get(weaponIndex-1));
        }
        catch (NumberFormatException e){
            viewInventories();
            return;
        }
        catch (IndexOutOfBoundsException e){
            System.out.print("[無此武器編號！]\n");
        }
        System.out.print("[按Enter返回上一層]");
        scanner.nextLine();
    }

    static void viewWeaponInventoryInBattle(){
        readWeaponInventory();
        System.out.print("\n[輸入(武器編號)以切換武器，或返回上一層]\n");

        String input = scanner.nextLine();
        int weaponIndex = 0;
        try{
            weaponIndex = Integer.parseInt(input);
            player.grabWeapon(weaponInventory.get(weaponIndex-1));
        }
        catch (NumberFormatException e){
            return;
        }
        catch (IndexOutOfBoundsException e){
            System.out.print("[無此武器編號！]\n");
        }
        System.out.print("[按Enter返回上一層]");
        scanner.nextLine();
    }

    static void readWeaponInventory(){
        System.out.print("\n-----武器背包-----\n");
        System.out.printf("%s可使用的武器類型：",player.getExpertiseName());
        Expertise exp = player.getExpertise();
        for (int typeID : exp.getAvailableWeaponType()){
            System.out.printf("%s | ",weaponTypeName[typeID]);
        }
        System.out.println();
        int index = 0;
        for (Weapon weapon : weaponInventory) {
            System.out.printf("%d. %s | %s | %.1f攻擊力 | %d%% 速度 | %s\n", ++index, weapon.getName(),
                    weaponTypeName[weapon.getWeaponType()], weapon.getAttack(),
                    (int)(weapon.getSpeedMultiplier()*100), weapon.getDescription());
        }
    }

    static void viewSorceryInventory(){
        readSorceryInventory();

        System.out.print("\n↪ 按Enter返回上一層");
        scanner.nextLine();
    }

    static void readSorceryInventory(){
        System.out.print("\n-----魔法記憶-----\n");
        int index = 0;
        for (Sorcery magic : player.getSorceryList()) {
            System.out.printf("%d. %s%s | %.0f魔力 | %s\n", ++index,
                    magic.getClass().getName().equals("Therapy") ? "✙" : "☠",
                    magic.getName(), magic.getManaCost(), magic.getDescription());
        }
    }

    static void viewMountInventory(){
        readMountInventory();

        System.out.print("\n[輸入(坐騎編號)以更換坐騎，或返回上一層]\n");

        String input = scanner.nextLine();
        int mountIndex = 0;
        try{
            mountIndex = Integer.parseInt(input);
            player.rideMount(mountInventory.get(mountIndex-1));
        }
        catch (NumberFormatException e){
            viewInventories();
            return;
        }
        catch (IndexOutOfBoundsException e){
            System.out.print("[無此坐騎編號！]\n");
        }
        System.out.print("[按Enter返回上一層]");
        scanner.nextLine();
    }

    static void viewMountInventoryInBattle(){
        readMountInventory();
        System.out.print("\n[輸入(坐騎編號)以更換坐騎，或返回上一層]\n");

        String input = scanner.nextLine();
        int mountIndex = 0;
        try{
            mountIndex = Integer.parseInt(input);
            player.rideMount(mountInventory.get(mountIndex-1));
        }
        catch (NumberFormatException e){
            return;
        }
        catch (IndexOutOfBoundsException e){
            System.out.print("[無此坐騎編號！]\n");
        }
        System.out.print("[按Enter返回上一層]");
        scanner.nextLine();
    }

    static void readMountInventory(){
        System.out.print("\n-----坐騎倉庫-----\n");
        int index = 0;
        for (Mount mount : mountInventory) {
            System.out.printf("%d. %s | 防禦 %.1f | 速度 %.1f\n",
                    ++index, mount.getName(), mount.getDefense(), mount.getSpeed());
        }
    }

    /*  functions of generating data */
    public static void generateWeapons(){
        weaponList = new LinkedList<Weapon>();
        weaponList.add(new Weapon("徒手","沒有武器，只有雙手",20,1.15f,0));
        weaponList.add(new Weapon("勇者長劍","勇者使用的英勇之劍十分鋒利",28,0.9f,1));
        weaponList.add(new Weapon("精靈長弓","精靈使用的輕盈長弓",24,1.1f,2));
        weaponList.add(new Weapon("穿刺矛","對地面怪物特別有用的長矛",26,0.83f,3));
        weaponList.add(new Weapon("蓋亞巨錘","神秘的失落武器無人知曉",30,0.79f,4));
        weaponList.add(new Weapon("迴旋火焰斧","非常笨重的炙熱斧頭",25,0.76f,5));

        weaponList.add(new Weapon("血之賜","燃燒生命的拳術",30,1.1f,0));
        weaponList.add(new Weapon("雷霆劍","帶著雷電的附魔劍",33,1.0f,1));
        weaponList.add(new Weapon("散華","像櫻花般優雅的弓箭",34,0.82f,2));
        weaponList.add(new Weapon("扺天矛","可以震碎山石的長矛",32,0.9f,3));
        weaponList.add(new Weapon("失落之錘","墮落的黑暗武器",38,0.7f,4));
        weaponList.add(new Weapon("雙手斧","易於揮舞的東方斧頭",29,1.0f,5));

        weaponList.add(new Weapon("太陽指","備受信仰的一種武藝",37,1.05f,0));
        weaponList.add(new Weapon("降生劍","足以斬斷命運的魔劍",38,0.92f,1));
        weaponList.add(new Weapon("漫天星","不明技術製作的分裂箭",35,0.99f,2));
        weaponList.add(new Weapon("封神矛","據傳曾封印古神的矛",41,0.79f,3));
        weaponList.add(new Weapon("墮天子之手","埋藏在古墓的怪異武器",37,1.08f,4));
        weaponList.add(new Weapon("雙生戰斧","同時使用威力極強的斧頭",40,0.81f,5));

        weaponList.add(new Weapon("流星指虎","寶石打造的堅硬指虎",41,1.02f,0));
        weaponList.add(new Weapon("魔舞重劍","嗜血成性的魔劍",45,0.86f,1));
        weaponList.add(new Weapon("穿陽弩","威力驚人的火焰十字弩",46,0.83f,2));
        weaponList.add(new Weapon("海王之矛","出自海底宮殿的長矛",39,1.16f,3));
        weaponList.add(new Weapon("長生明珠","珍珠為材的珍稀錘子",42,0.98f,4));
        weaponList.add(new Weapon("牛頭馬面","寄宿妖怪之力的巨斧",44,1f,5));

        weaponList.add(new Weapon("傳奇手套","競技場冠軍的獎勵",48,0.95f,0));
        weaponList.add(new Weapon("王國之盾","可攻可守的巨劍",52,0.8f,1));
        weaponList.add(new Weapon("奧術","大法師子弟的魔法弓",47,0.96f,2));
        weaponList.add(new Weapon("大洋獵手","知名海盜的武器收藏之一",55,0.76f,3));
        weaponList.add(new Weapon("玻璃公主","透明輕盈的小型錘",45,1.11f,4));
        weaponList.add(new Weapon("馬爾濟斯","犬類骨架磨成的邪斧",50,0.85f,5));

        weaponList.add(new Weapon("玫瑰花園","佈滿尖刺的手套",53,1.13f,0));
        weaponList.add(new Weapon("馬里露納","吟遊詩人的得意武器",56,0.92f,1));
        weaponList.add(new Weapon("潘朵拉","禁忌魔女之物",52,1.14f,2));
        weaponList.add(new Weapon("森林召喚","精靈王子的寶物",58,0.9f,3));
        weaponList.add(new Weapon("賽拉斯翁","傳說中的至上神器",60,0.81f,4));
        weaponList.add(new Weapon("炎虎","帶有灼熱高溫的單手斧",57,0.88f,5));
    }

    public static void generateMonsters(){
        monsterList = new LinkedList<>();
        Hashtable<Integer,Float> groundResistance = new Hashtable<>();
        groundResistance.put(1,1.2f);
        groundResistance.put(2,0.8f);
        groundResistance.put(3,0.9f);
        groundResistance.put(4,1f);
        groundResistance.put(5,1.1f);
        Hashtable<Integer,Float> airResistance = new Hashtable<>();
        airResistance.put(1,1f);
        airResistance.put(2,1.2f);
        airResistance.put(3,1.1f);
        airResistance.put(4,0.8f);
        airResistance.put(5,0.9f);
        Hashtable<Integer,Float> oceanResistance = new Hashtable<>();
        oceanResistance.put(1,0.8f);
        oceanResistance.put(2,0.9f);
        oceanResistance.put(3,1.2f);
        oceanResistance.put(4,1.1f);
        oceanResistance.put(5,1f);

        // L1
        monsterList.add(new TerrestrialMonster(1,"小史萊姆",60,16,2f,groundResistance));
        monsterList.add(new TerrestrialMonster(2,"六角恐龍",76,18,3.2f,groundResistance));
        monsterList.add(new AerialMonster(1,"天史萊姆",66,25,7f,airResistance));
        monsterList.add(new AerialMonster(2,"飛天蠑螈",80,28,5.3f,airResistance));
        monsterList.add(new AquaticMonster(3,"海王穿甲獸",99,34,4.6f,oceanResistance));
        // L2
        monsterList.add(new AerialMonster(3,"暴風龍",120,32,5.1f,airResistance));
        monsterList.add(new AquaticMonster(2,"河中之螈",176,28,9f,oceanResistance));
        monsterList.add(new AquaticMonster(1,"海史萊姆",86,35,3.6f,oceanResistance));
        monsterList.add(new TerrestrialMonster(4,"沙漠土狼",95,38,4.1f,groundResistance));
        monsterList.add(new TerrestrialMonster(3,"深山穿山甲",130,40,4.7F,groundResistance));
        // L3
        monsterList.add(new AquaticMonster(5,"翡翠水蜘蛛",97,40,5.5f,oceanResistance));
        monsterList.add(new AerialMonster(6,"阿格尼斯",131,37,6f,airResistance));
        monsterList.add(new AerialMonster(4,"火山灰狼",140,41,3f,airResistance));
        monsterList.add(new AquaticMonster(4,"冰原白狼",150,44,4.4f,oceanResistance));
        monsterList.add(new TerrestrialMonster(7,"夜鴉",158,49,5.7f,groundResistance));
        // L4
        monsterList.add(new AquaticMonster(6,"奧賽羅提",155,50,5.8f,oceanResistance));
        monsterList.add(new TerrestrialMonster(5,"大地射手",60,78,2.7f,groundResistance));
        monsterList.add(new TerrestrialMonster(6,"亞特諾姆",260,45,4.8f,groundResistance));
        monsterList.add(new AerialMonster(5,"砲彈蜘蛛",333,37,1.5f,airResistance));
        monsterList.add(new AquaticMonster(7,"寒蛇",148,52,6.2f,oceanResistance));
        // L5
        monsterList.add(new AerialMonster(7,"浮鹿",530,38,6.8f,airResistance));
        monsterList.add(new TerrestrialMonster(8,"魔像伊登",400,44,4.4f,groundResistance));
        monsterList.add(new AquaticMonster(8,"魔像亞登",380,52,6.9f,oceanResistance));
        monsterList.add(new AerialMonster(8,"魔像雷登",335,61,7.5f,airResistance));
        // L6
        monsterList.add(new TerrestrialMonster(9,"岩龜王",370,69,5.7f,groundResistance));
        monsterList.add(new AquaticMonster(9,"海龍王",390,67,5.3f,oceanResistance));
        monsterList.add(new AerialMonster(9,"天馬王",400,75,8f,airResistance));
        monsterList.add(new AerialMonster(10,"萬象歸因",600,39,6.2f,airResistance));
    }
    /*  Expertise */
    public static void generateExpertises(){
        expertiseList = new LinkedList<Expertise>();
        setSwordManData();
        setArcherData();
        setWarriorData();
    }

    public static void setSwordManData(){
        int[] availableWeaponType = {0,1,3,5};
        Hashtable<Integer,Float> advantage = new Hashtable<>();
        advantage.put(1,1.3f);
        advantage.put(5,1.2f);
        advantage.put(6,1.4f);
        expertiseList.add(new Expertise(1,"勇者",availableWeaponType,advantage));
    }
    public static void setArcherData(){
        int[] availableWeaponType = {0,1,2,3};
        Hashtable<Integer,Float> advantage = new Hashtable<>();
        advantage.put(2,1.1f);
        advantage.put(4,1.5f);
        advantage.put(8,1.25f);
        expertiseList.add(new Expertise(2,"弓箭手",availableWeaponType,advantage));
    }

    public static void setWarriorData(){
        int[] availableWeaponType = {0,1,3,4};
        Hashtable<Integer,Float> advantage = new Hashtable<>();
        advantage.put(3,1.4f);
        advantage.put(7,1.3f);
        advantage.put(9,1.2f);
        expertiseList.add(new Expertise(3,"戰士",availableWeaponType,advantage));
    }
    /*  Expertise End */

    public static void generateMount(){
        mountList = new LinkedList<>();
        mountList.add(new Mount("新手坐騎",1.4f,0.5f));
        mountList.add(new Mount("百里馬",2.6f,0.6f));
        mountList.add(new Mount("貴族鳳凰",3.5f,0.9f));
        mountList.add(new Mount("重甲烏龜",8f,-3f));
        mountList.add(new Mount("千里馬",1.7f,3.3f));
        mountList.add(new Mount("草原蜥蜴",3.0f,1.6f));
        mountList.add(new Mount("獅鷲獸",3.8f,1.8f));
        mountList.add(new Mount("沼澤毒蛇",7.2f,-2f));
        mountList.add(new Mount("食蟻獸",4.8f,1.7f));
        mountList.add(new Mount("鸚鵡螺",6.2f,-0.2f));
        mountList.add(new Mount("加拉哈德",9f,-1.1f));
        mountList.add(new Mount("彩虹獨角獸",3f,4f));
        mountList.add(new Mount("火山蜥蜴",4f,2.5f));
        mountList.add(new Mount("黃帶蟒蛇",5.5f,2.3f));
        mountList.add(new Mount("王室鳳凰",4.9f,2.6f));
        mountList.add(new Mount("神殿守衛",6.4f,1.7f));
        mountList.add(new Mount("皇家獨角獸",7.5f,1.8f));
        mountList.add(new Mount("究極獅鷲獸",8.4f,2f));
        mountList.add(new Mount("奧古斯坦",7.2f,2.2f));
        mountList.add(new Mount("終焉獅鷲獸",9.2f,1.5f));
    }

    public static void generateSorcery(){
        sorceryList = new LinkedList<>();
        sorceryList.add(new Therapy(1,"初級治療術","簡易的治療魔法，+30%血量",30,0.3f));
        sorceryList.add(new Destroyer(2,"火球術","放出火球燃燒敵人，34傷害",36,34));
        sorceryList.add(new Destroyer(3,"冰爆術","釋放冰霜凍傷敵人，28傷害",27,28));
        sorceryList.add(new Destroyer(4,"滾石術","生成石塊撞擊敵人，47傷害",45,47));
        sorceryList.add(new Destroyer(5,"幻影之刃","以暗影刀鋒割裂敵人，52傷害",51,52));
        sorceryList.add(new Therapy(6,"中級治療術","一般的治癒魔法，+45%血量",43,0.45f));
        sorceryList.add(new Destroyer(7,"烈焰風暴","火焰龍捲風灼燒敵人，46傷害",42,46));
        sorceryList.add(new Destroyer(8,"光之太刀","以光之刃斬擊敵人，55傷害",52,55));
        sorceryList.add(new Destroyer(9,"冰凝境","以極低溫凍結敵人，31傷害",29,31));
        sorceryList.add(new Therapy(10,"高級治療術","強大的治療魔法，+60%血量",56,0.6f));
        sorceryList.add(new Destroyer(11,"炸裂空間","來自虛空的爆炸，99傷害",84,99));
        sorceryList.add(new Destroyer(12,"絕對零度","奪去周圍環境的溫度，76傷害",66,76));
        sorceryList.add(new Destroyer(13,"石之海","數不盡的岩石淹沒敵人，84傷害",72,84));
        sorceryList.add(new Destroyer(14,"風林火山","大幅度改變周圍環境，105傷害",90,105));
        sorceryList.add(new Therapy(15,"神聖治癒曲","發出可以療傷的音調，+66%血量",60,0.66f));
        sorceryList.add(new Destroyer(16,"黑血符陣","失傳的毀滅禁術，120傷害",100,120));
    }

    // other
    public static float getRandomFloat(float min, float max){
        return (float)(Math.random() * (max - min + 1)) + min;
    }

    public static int getRandomInt(int min, int max){
        return (int)(Math.random() * (max - min + 1)) + min;
    }
}