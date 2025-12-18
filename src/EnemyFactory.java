public class EnemyFactory {

    public NormalEnemy createChicken(int x, int y, int level) {
        double r = Math.random();
        ChickenType type;
        if (r < 0.5) type = ChickenType.SMALL;
        else if (r < 0.8) type = ChickenType.MEDIUM;
        else type = ChickenType.BIG;

        return new NormalEnemy(x, y, type, level);
    }

    public BossEnemy createBoss(int level) {
        return new BossEnemy(GamePanel.WIDTH / 2 - 70, 60, level);
    }
}
