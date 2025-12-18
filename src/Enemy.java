import java.awt.*;

public abstract class Enemy implements Drawable, Movable, Damageable {

    protected int x, y;
    protected int hp;
    protected int speed;
    protected int scoreValue;

    public Enemy(int x, int y, int hp, int speed, int scoreValue) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.speed = speed;
        this.scoreValue = scoreValue;
    }

    @Override
    public void takeDamage(int dmg) { hp -= dmg; }

    @Override
    public boolean isAlive() { return hp > 0; }

    public int getScoreValue() { return scoreValue; }

    public abstract Rectangle getBounds();

    public int getX() { return x; }
    public int getY() { return y; }
}
