import java.awt.*;

public class Item implements Drawable, Movable {

    private int x, y;
    private int speed = 3;
    private ItemType type;
    private Image sprite;
    private int size = 30;
    private boolean alive = true;

    public Item(int x, int y, ItemType type) {
        this.x = x;
        this.y = y;
        this.type = type;

        switch (type) {
            case SHIELD -> sprite = SpriteLoader.load("res/images/item_shield.png");
            case HEART -> sprite = SpriteLoader.load("res/images/item_heart.png");
            case WEAPON_LASER -> sprite = SpriteLoader.load("res/images/item_laser.png");
            case WEAPON_ROCKET -> sprite = SpriteLoader.load("res/images/item_rocket.png");
            case DAMAGE_UP -> sprite = SpriteLoader.load("res/images/item_damage.png");
            case DOUBLE_SHOT -> sprite = SpriteLoader.load("res/images/item_double.png");
            case TRIPLE_SHOT -> sprite = SpriteLoader.load("res/images/item_triple.png");
            case PIERCE_SHOT -> sprite = SpriteLoader.load("res/images/item_pierce.png");
        }
    }

    @Override
    public void update() {
        y += speed;
        if (y > GamePanel.HEIGHT + 40) alive = false;
    }

    @Override
    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x - size / 2, y - size / 2, size, size, null);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval(x - size / 2, y - size / 2, size, size);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x - size / 2, y - size / 2, size, size);
    }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean a) { alive = a; }

    public ItemType getType() { return type; }
}
