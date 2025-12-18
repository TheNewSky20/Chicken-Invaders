import java.awt.*;
import java.util.Random;

public class NormalEnemy extends Enemy {

    private static Image[] frames;
    private static final int FRAME_COUNT = 3;

    static {
        frames = new Image[FRAME_COUNT];
        frames[0] = SpriteLoader.load("res/images/chicken.png");
        frames[1] = SpriteLoader.load("res/images/chicken2.png");
        frames[2] = SpriteLoader.load("res/images/chicken3.png");
        if (frames[1] == null) frames[1] = frames[0];
        if (frames[2] == null) frames[2] = frames[0];
    }

    private int moveCounter = 0;
    private int animCounter = 0;
    private int frameIndex = 0;

    private Random rand = new Random();
    private int shootCooldown = 80 + rand.nextInt(80);
    private double waveOffset = rand.nextDouble() * Math.PI;
    private final int startX;
    private final int startY;
    private double preciseY;
    private final double horizAmplitude;
    private final double bobAmplitude;
    private final double horizFreq;

    public NormalEnemy(int x, int y, ChickenType type, int level) {
        super(x, y, type.hp, Math.max(1, 1 + level / 5), type.score);
        this.startX = x;
        this.startY = y;
        this.preciseY = y;
        this.horizAmplitude = 6 + rand.nextDouble() * 8;
        this.bobAmplitude = 3 + rand.nextDouble() * 3;
        this.horizFreq = 0.02 + rand.nextDouble() * 0.02;
    }

    @Override
    public void update() {
        moveCounter++;
        animCounter++;

        double horizontalOffset = Math.sin((moveCounter * horizFreq) + waveOffset) * horizAmplitude;
        this.x = startX + (int) horizontalOffset;

        double verticalBob = Math.cos((moveCounter * 0.02) + waveOffset) * bobAmplitude;
        preciseY = startY + verticalBob + (moveCounter * speed * 0.02);
        this.y = (int) preciseY;

        if (animCounter >= 10) {
            animCounter = 0;
            frameIndex = (frameIndex + 1) % FRAME_COUNT;
        }

        shootCooldown--;
    }

    public boolean canShoot() {
        return shootCooldown <= 0;
    }

    public EnemyBullet shoot() {
        shootCooldown = 80 + rand.nextInt(80);
        return new EnemyBullet(x + 20, y + 40);
    }

    @Override
    public void draw(Graphics g) {
        Image img = frames[frameIndex];
        if (img != null) {
            g.drawImage(img, x, y, 40, 40, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, 40, 40);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x - 4, y - 4, 48, 48);
    }
}
