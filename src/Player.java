import java.awt.*;

public class Player implements Drawable, Movable {

    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;

    private int x, y;
    private int speed = 7;
    private Image sprite;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        sprite = SpriteLoader.load("res/images/player.png");
    }

    public void moveLeft() {
        x -= speed;
        if (x < 0) x = 0;
    }

    public void moveRight(int panelWidth) {
        x += speed;
        if (x + WIDTH > panelWidth) x = panelWidth - WIDTH;
    }

    public void moveUp() {
        y -= speed;
        if (y < 0) y = 0;
    }

    public void moveDown(int panelHeight) {
        y += speed;
        if (y + HEIGHT > panelHeight) y = panelHeight - HEIGHT;
    }

    @Override
    public void update() {}

    public Bullet shoot(int offsetX, WeaponMode mode, int extraDamage, boolean pierce) {
        return new Bullet(x + WIDTH / 2 + offsetX, y, mode, extraDamage, pierce);
    }

    @Override
    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
