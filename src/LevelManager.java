import java.util.List;

public class LevelManager {

    private int level = 1;
    private EnemySpawner spawner;

    public LevelManager(EnemySpawner spawner) {
        this.spawner = spawner;
    }

    public int getLevel() { return level; }

    public void start(List<Enemy> enemies) {
        level = 1;
        spawner.spawnEnemies(enemies, level);
    }

    public void nextLevel(List<Enemy> enemies) {
        level++;
        spawner.spawnEnemies(enemies, level);
    }
}
