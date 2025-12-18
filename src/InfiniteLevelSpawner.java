import java.util.List;
import java.util.Random;

public class InfiniteLevelSpawner implements EnemySpawner {

    private EnemyFactory factory;
    private Random rand = new Random();

    public InfiniteLevelSpawner(EnemyFactory factory) {
        this.factory = factory;
    }

    @Override
    public void spawnEnemies(List<Enemy> enemies, int level) {
        enemies.clear();

        if (level % 5 == 0) {
            enemies.add(factory.createBoss(level));
            return;
        }

        int count = 8 + rand.nextInt(6); // 8–13
        int startY = 80;
        int cols = 7;
        int gapX = 80;
        int gapY = 90;

        int rows = (count + cols - 1) / cols;
        int remaining = count;
        for (int r = 0; r < rows; r++) {
            int inRow = Math.min(cols, remaining);
            remaining -= inRow;

            int totalWidth = (inRow - 1) * gapX;
            int rowStartX = (GamePanel.WIDTH - totalWidth) / 2; // center the row

            for (int c = 0; c < inRow; c++) {
                int x = rowStartX + c * gapX;
                int y = startY + r * gapY;
                enemies.add(factory.createChicken(x, y, level));
            }
        }
    }
}
