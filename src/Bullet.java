import java.awt.*;

public class Bullet implements Drawable, Movable {

    private int x, y;
    private int width;
    private int height;
    private double speed;
    private boolean alive = true;

    private int damage;
    private WeaponMode mode;
    private int extraDamage;
    private boolean pierce;

    public Bullet(int x, int y, WeaponMode mode, int extraDamage, boolean pierce) {
        this.x = x;
        this.y = y;
        this.mode = mode;
        this.extraDamage = extraDamage / 2; // scale down extra damage for bullets
        this.pierce = pierce;
        configureByMode();
    }

    private void configureByMode() {
        switch (mode) {
            case LASER -> {
                speed = 16;
                damage = 2;
                width = 6;
                height = 24;
            }
            case ROCKET -> {
                speed = 10;
                damage = 4;
                width = 10;
                height = 20;
            }
            case NORMAL -> {
                speed = 14;
                damage = 1;
                width = 6;
                height = 18;
            }
        }
        damage += extraDamage;
        if (damage < 1) damage = 1;
    }

    @Override
    public void update() {
        for (int i = 0; i < 2; i++) {
            y -= speed / 2.0;
        }
        if (y + height < 0) alive = false;
    }

    @Override
    public void draw(Graphics g) {
        switch (mode) {
            case NORMAL -> g.setColor(Color.GREEN);
            case LASER -> g.setColor(Color.CYAN);
            case ROCKET -> g.setColor(Color.ORANGE);
        }
        g.fillRoundRect(x - width / 2, y - height, width, height, 4, 4);
    }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean a) { this.alive = a; }

    public Rectangle getBounds() {
        return new Rectangle(x - width / 2 - 2, y - height - 2,
                             width + 4, height + 4);
    }

    public int getDamage() { return damage; }
    public boolean isPierce() { return pierce; }
}
