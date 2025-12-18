import java.awt.*;

public class BossEnemy extends Enemy {

    private static Image[] frames;
    private static final int FRAME_COUNT = 3;
    
    public int maxHp;
    int time = 0; // package-visible để GamePanel dùng
    private final int startX;
    private final int startY;

    static {
        frames = new Image[FRAME_COUNT];
        frames[0] = SpriteLoader.load("res/images/boss1.png");
        frames[1] = SpriteLoader.load("res/images/boss2.png");
        frames[2] = SpriteLoader.load("res/images/boss3.png");
        if (frames[1] == null) frames[1] = frames[0];
        if (frames[2] == null) frames[2] = frames[0];
    }

    public BossEnemy(int x, int y, int level) {
        super(x, y, 300 + level * 80, 1, 300);
        this.maxHp = this.hp;
        this.startX = x;
        this.startY = y;
    }

    @Override
    public void update() {
        time++;
        int width = 140;
        double amp = 120;
        double ox = startX + Math.sin(time * 0.02) * amp;
        x = (int) Math.max(0, Math.min(GamePanel.WIDTH - width, ox));

        y = startY + (int)(Math.cos(time * 0.01) * 25);
    }

    @Override
    public void draw(Graphics g) {
        Image img = frames[(time / 10) % FRAME_COUNT];
        if (img != null) {
            g.drawImage(img, x, y, 140, 140, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, 140, 140);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 140, 140);
    }
}
