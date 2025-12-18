import java.awt.*;

public class EnemyBullet implements Movable, Drawable {

    private int x, y;
    private double speed = 3.0;
    private boolean alive = true;

    public EnemyBullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {
        y += speed;
        if (y > GamePanel.HEIGHT + 20) alive = false;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRoundRect(x - 3, y, 6, 14, 4, 4);
    }

    public boolean isAlive() { return alive; }

    public Rectangle getBounds() { return new Rectangle(x - 3, y, 6, 14); }
}
