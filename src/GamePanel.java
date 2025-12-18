import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Thread gameThread;
    private boolean running = false;

    private GameState gameState = GameState.MENU;

    private Player player;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<Item> items;
    private List<EnemyBullet> enemyBullets;

    private LevelManager levelManager;
    private EnemyFactory enemyFactory;
    private SettingsManager settingsManager;
    private HighScoreManager highScoreManager;

    // control
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    // player status
    private int hp = 100;
    private int shield = 0;
    private WeaponMode weaponMode = WeaponMode.NORMAL;
    private long weaponExpireTime = 0;

    // upgrade
    private int extraDamage = 0;
    private boolean pierce = false;
    private int multiShot = 1; // 1,2,3

    private int shootCooldown = 0;
    private int score = 0;

    private Clip bgmClip;
    private BossEnemy currentBoss = null;

    // starfield
    private static class Star { int x, y, speed; }
    private List<Star> stars = new ArrayList<>();

    // menu stars
    private List<Star> menuStars1 = new ArrayList<>();
    private List<Star> menuStars2 = new ArrayList<>();
    private float nebulaOffset = 0f;
    private Color nebulaColor = new Color(90, 0, 140, 80);

    // menu anim
    private float menuAlpha = 0f;
    private int titleWave = 0;

    // settings
    private int settingIndex = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocus();
        addKeyListener(this);

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        items = new ArrayList<>();
        enemyBullets = new ArrayList<>();

        enemyFactory = new EnemyFactory();
        levelManager = new LevelManager(new InfiniteLevelSpawner(enemyFactory));
        settingsManager = new SettingsManager();
        highScoreManager = new HighScoreManager();

        initStars();
        initMenuStars();

        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void initStars() {
        for (int i = 0; i < 80; i++) {
            Star s = new Star();
            s.x = (int)(Math.random() * WIDTH);
            s.y = (int)(Math.random() * HEIGHT);
            s.speed = 1 + (int)(Math.random() * 3);
            stars.add(s);
        }
    }

    private void updateStars() {
        for (Star s : stars) {
            s.y += s.speed;
            if (s.y > HEIGHT) {
                s.y = 0;
                s.x = (int)(Math.random() * WIDTH);
            }
        }
    }

    private void initMenuStars() {
        for (int i = 0; i < 60; i++) {
            Star s = new Star();
            s.x = (int)(Math.random() * WIDTH);
            s.y = (int)(Math.random() * HEIGHT);
            s.speed = 1;
            menuStars1.add(s);
        }
        for (int i = 0; i < 40; i++) {
            Star s = new Star();
            s.x = (int)(Math.random() * WIDTH);
            s.y = (int)(Math.random() * HEIGHT);
            s.speed = 2;
            menuStars2.add(s);
        }
    }

    private void updateMenuBackground() {
        for (Star s : menuStars1) {
            s.y += s.speed;
            if (s.y > HEIGHT) {
                s.y = 0;
                s.x = (int)(Math.random() * WIDTH);
            }
        }

        for (Star s : menuStars2) {
            s.y += s.speed;
            if (s.y > HEIGHT) {
                s.y = 0;
                s.x = (int)(Math.random() * WIDTH);
            }
        }

        nebulaOffset += 0.005;
        if (nebulaOffset > 1) nebulaOffset = 0;
    }

    private void startGame() {
        player = new Player(WIDTH / 2 - Player.WIDTH / 2, HEIGHT - 120);
        bullets.clear();
        enemies.clear();
        items.clear();
        enemyBullets.clear();

        hp = 100;
        shield = 0;
        score = 0;
        weaponMode = WeaponMode.NORMAL;
        weaponExpireTime = 0;

        extraDamage = 0;
        pierce = false;
        multiShot = 1;

        currentBoss = null;

        levelManager.start(enemies);

        if (bgmClip == null) {
            bgmClip = SoundManager.loopBackground("res/sounds/bgm.wav");
        }

        menuAlpha = 0f;
        titleWave = 0;

        gameState = GameState.PLAYING;
    }

    @Override
    public void run() {
        final int FPS = 60;
        final long frameTime = 1000 / FPS;

        while (running) {
            long start = System.currentTimeMillis();

            updateGame();
            repaint();

            long elapsed = System.currentTimeMillis() - start;
            long sleep = frameTime - elapsed;
            if (sleep < 5) sleep = 5;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        if (gameState == GameState.MENU) {
            updateMenuBackground();
        } else {
            updateStars();
        }

        if (gameState == GameState.PLAYING) {
            updatePlaying();
        }
    }

    private void updatePlaying() {
        handleMovement();
        autoFire();
        updateWeaponTimer();

        if (player != null) player.update();

        // bullets
        Iterator<Bullet> bit = bullets.iterator();
        while (bit.hasNext()) {
            Bullet b = bit.next();
            b.update();
            if (!b.isAlive()) bit.remove();
        }

        // enemies
        Iterator<Enemy> eit = enemies.iterator();
        while (eit.hasNext()) {
            Enemy e = eit.next();
            e.update();

            if (e instanceof BossEnemy be) {
                currentBoss = be;
            }

            if (player != null && e.getBounds().intersects(player.getBounds())) {
                takeDamage(20);
    
            if (e instanceof BossEnemy) {
                e.takeDamage(20); 
                } else {
                        eit.remove();
                    continue;
                }
            }
        
                if (!e.isAlive()) {

                if (e instanceof BossEnemy be) { 
                    currentBoss = be;
                }

                if (e instanceof BossEnemy) {
                    currentBoss = null;
                }

                eit.remove();
                break;
            }

            if (!(e instanceof BossEnemy) && e.getY() > HEIGHT + 60) {
                eit.remove();
                continue;
            }

            if (e instanceof NormalEnemy ne && ne.canShoot()) {
                enemyBullets.add(ne.shoot());
            }

            for (Bullet b : bullets) {
                if (!b.isAlive()) continue;

                if (e.getBounds().intersects(b.getBounds())) {
                    e.takeDamage(b.getDamage());

                    if (!b.isPierce()) {
                        b.setAlive(false);
                    }

                    if (!e.isAlive()) {
                        score += e.getScoreValue();
                        dropItemChance(e.getX(), e.getY());
                        SoundManager.playSound("res/sounds/explosion.wav");

                        if (e instanceof BossEnemy) {
                            currentBoss = null;
                        }
                        eit.remove();
                    }
                    break;
                }
            }
        }

        // enemy bullets
        Iterator<EnemyBullet> ebIt = enemyBullets.iterator();
        while (ebIt.hasNext()) {
            EnemyBullet eb = ebIt.next();
            eb.update();
            if (!eb.isAlive()) {
                ebIt.remove();
                continue;
            }
            if (player != null && eb.getBounds().intersects(player.getBounds())) {
                ebIt.remove();
                takeDamage(10);
            }
        }

        // items
        Iterator<Item> iit = items.iterator();
        while (iit.hasNext()) {
            Item it = iit.next();
            it.update();
            if (!it.isAlive()) {
                iit.remove();
                continue;
            }
            if (player != null && it.getBounds().intersects(player.getBounds())) {
                applyItem(it.getType());
                iit.remove();
            }
        }

        if (enemies.isEmpty()) {
            levelManager.nextLevel(enemies);
        }
    }

    private void handleMovement() {
        if (player == null) return;
        if (leftPressed) player.moveLeft();
        if (rightPressed) player.moveRight(WIDTH);
        if (upPressed) player.moveUp();
        if (downPressed) player.moveDown(HEIGHT);
    }

    private void autoFire() {
        if (player == null) return;

        if (shootCooldown > 0) {
            shootCooldown--;
            return;
        }

        int cooldown;
        switch (weaponMode) {
            case LASER -> cooldown = 8;
            case ROCKET -> cooldown = 14;
            default -> cooldown = 10;
        }

        int shots = multiShot;
        if (shots <= 1) {
            bullets.add(player.shoot(0, weaponMode, extraDamage, pierce));
        } else if (shots == 2) {
            bullets.add(player.shoot(-10, weaponMode, extraDamage, pierce));
            bullets.add(player.shoot(10, weaponMode, extraDamage, pierce));
        } else {
            bullets.add(player.shoot(0, weaponMode, extraDamage, pierce));
            bullets.add(player.shoot(-15, weaponMode, extraDamage, pierce));
            bullets.add(player.shoot(15, weaponMode, extraDamage, pierce));
        }

        SoundManager.playSound("res/sounds/shoot.wav");
        shootCooldown = cooldown;
    }

    private void applyItem(ItemType type) {
        switch (type) {
            case SHIELD -> shield++;
            case HEART -> {
                hp += 25;
                if (hp > 100) hp = 100;
            }
            case WEAPON_LASER -> setWeapon(WeaponMode.LASER);
            case WEAPON_ROCKET -> setWeapon(WeaponMode.ROCKET);
            case DAMAGE_UP -> extraDamage++;
            case DOUBLE_SHOT -> {
                if (multiShot < 2) multiShot = 2;
            }
            case TRIPLE_SHOT -> multiShot = 3;
            case PIERCE_SHOT -> pierce = true;
        }
    }

    private void setWeapon(WeaponMode mode) {
        weaponMode = mode;
        weaponExpireTime = System.currentTimeMillis() + 10000; // 10s
    }

    private void updateWeaponTimer() {
        if (weaponMode != WeaponMode.NORMAL && weaponExpireTime > 0) {
            if (System.currentTimeMillis() > weaponExpireTime) {
                weaponMode = WeaponMode.NORMAL;
                weaponExpireTime = 0;
            }
        }
    }

    private void takeDamage(int dmg) {
        if (shield > 0) {
            shield--;
            return;
        }
        hp -= dmg;
        if (hp <= 0) {
            hp = 0;
            highScoreManager.submitScore(score);
            gameState = GameState.GAME_OVER;
        }
    }

    private void dropItemChance(int x, int y) {
        if (Math.random() < 0.30) {
            double r = Math.random();
            ItemType type;
            if (r < 0.20) type = ItemType.SHIELD;
            else if (r < 0.40) type = ItemType.HEART;
            else if (r < 0.55) type = ItemType.WEAPON_LASER;
            else if (r < 0.70) type = ItemType.WEAPON_ROCKET;
            else if (r < 0.82) type = ItemType.DAMAGE_UP;
            else if (r < 0.92) type = ItemType.DOUBLE_SHOT;
            else if (r < 0.97) type = ItemType.TRIPLE_SHOT;
            else type = ItemType.PIERCE_SHOT;
            items.add(new Item(x, y, type));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameState == GameState.MENU) {
            drawMenu(g);
            return;
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        drawStars(g);

        switch (gameState) {
            case SETTINGS -> drawSettings(g);
            case HIGH_SCORE -> drawHighScore(g);
            case PLAYING -> drawPlaying(g);
            case GAME_OVER -> drawGameOver(g);
        }
    }

    private void drawStars(Graphics g) {
        g.setColor(Color.WHITE);
        for (Star s : stars) {
            g.fillRect(s.x, s.y, 2, 2);
        }
    }

    private void drawMenu(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        g2.setColor(Color.WHITE);
        for (Star s : menuStars1) g2.fillRect(s.x, s.y, 2, 2);
        g2.setColor(Color.LIGHT_GRAY);
        for (Star s : menuStars2) g2.fillRect(s.x, s.y, 3, 3);

        GradientPaint nebula = new GradientPaint(
                0, (int)(HEIGHT * nebulaOffset),
                nebulaColor,
                0, (int)(HEIGHT * (nebulaOffset + 1)),
                new Color(0,0,0,0)
        );
        g2.setPaint(nebula);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        if (menuAlpha < 1f) menuAlpha += 0.01f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, menuAlpha));

        titleWave++;
        int titleY = 150 + (int)(Math.sin(titleWave * 0.05) * 10);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 42));
        g.drawString("CHICKEN INVADERS", 190, titleY);

        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("CUTE EDITION", 300, titleY + 40);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("▶  Press ENTER to Start", 260, 320);
        g.drawString("🔍  High Scores (2)",     260, 360);
        g.drawString("⚙️   Settings (3)",       260, 400);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("ESC to Quit Game", 310, 450);
    }

    private void drawSettings(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("SETTINGS", 300, 130);

        String[] names = {
                "Music Volume",
                "SFX Volume",
                "Difficulty",
                "Fullscreen"
        };

        Object[] values = {
                settingsManager.getMusicVolume(),
                settingsManager.getSfxVolume(),
                settingsManager.getDifficulty(),
                settingsManager.isFullscreen() ? "ON" : "OFF"
        };

        int yStart = 200;

        for (int i = 0; i < names.length; i++) {

            if (i == settingIndex)
                g.setColor(Color.YELLOW);
            else
                g.setColor(Color.WHITE);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString(names[i], 200, yStart + i * 60);

            if (i == 0 || i == 1) {
                int value = (int) values[i];
                int sliderX = 420;
                int sliderY = yStart + i * 60 - 12;

                g.setColor(Color.GRAY);
                g.fillRect(sliderX, sliderY, 200, 6);

                g.setColor(Color.CYAN);
                g.fillRect(sliderX, sliderY, value * 2, 6);

                g.setColor(Color.WHITE);
                g.drawString(value + "%", sliderX + 210, sliderY + 10);
            } else {
                g.setColor(Color.CYAN);
                g.drawString(values[i].toString(), 420, yStart + i * 60);
            }
        }

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("↑↓ chọn | ←→ chỉnh | ESC để lưu và thoát", 250, 520);
    }

    private void drawHighScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 38));
        g.drawString("HIGH SCORES", 260, 120);

        int[] top = highScoreManager.getTopScores();

        g.setFont(new Font("Arial", Font.PLAIN, 26));
        for (int i = 0; i < 5; i++) {
            String line = (i + 1) + ".   " + top[i];
            g.drawString(line, 330, 200 + i * 50);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Press ESC to return", 300, 520);
    }

    private void drawPlaying(Graphics g) {
        if (player != null) player.draw(g);
        for (Bullet b : bullets) b.draw(g);
        for (Enemy e : enemies) e.draw(g);
        for (Item it : items) it.draw(g);
        for (EnemyBullet eb : enemyBullets) eb.draw(g);

        drawHUD(g);

        if (currentBoss != null) {
            drawBossHP(g, currentBoss);
        }
    }

    private void drawHUD(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(10, 10, 160, 16);
        double ratio = hp / 100.0;
        if (ratio > 0.6) g.setColor(Color.GREEN);
        else if (ratio > 0.3) g.setColor(Color.YELLOW);
        else g.setColor(Color.RED);
        g.fillRect(10, 10, (int)(160 * ratio), 16);
        g.setColor(Color.WHITE);
        g.drawRect(10, 10, 160, 16);
        g.drawString("HP: " + hp, 15, 22);

        g.drawString("Score: " + score, 10, 40);
        g.drawString("Level: " + levelManager.getLevel(), 10, 60);
        g.drawString("Shield: " + shield, 10, 80);
        g.drawString("Weapon: " + weaponMode, 10, 100);
        g.drawString("DMG+" + extraDamage + "  Shot:" + multiShot + (pierce ? "  Pierce" : ""), 10, 120);
    }

private void drawBossHP(Graphics g, BossEnemy boss) {
    
        if (boss.hp <= 0) { 
            return; 
        }
    
        int barWidth = 400;
        int barHeight = 18;
        int x = WIDTH / 2 - barWidth / 2;
        int y = 40;

        double ratio = boss.hp / (double) boss.maxHp;

        // 1. Draw the empty (dark gray) bar background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        // 2. Determine color and draw the current HP (the colored part)
        if (ratio > 0.6) g.setColor(Color.GREEN);
        else if (ratio > 0.3) g.setColor(Color.YELLOW);
        else g.setColor(Color.RED);
        g.fillRect(x, y, (int)(barWidth * ratio), barHeight);

        // 3. Draw the border and label
        g.setColor(Color.WHITE);
        g.drawRect(x, y, barWidth, barHeight);
        g.drawString("BOSS HP", x + barWidth / 2 - 30, y - 5);
    }

    private void drawGameOver(Graphics g) {
        drawPlaying(g);

        g.setColor(new Color(0,0,0,180));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("GAME OVER", WIDTH / 2 - 110, HEIGHT / 2 - 20);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 22));
        g.drawString("Score: " + score, WIDTH / 2 - 50, HEIGHT / 2 + 20);

        int[] top = highScoreManager.getTopScores();
        g.drawString("Best: " + top[0], WIDTH / 2 - 50, HEIGHT / 2 + 50);

        g.drawString("Press ENTER to restart", WIDTH / 2 - 140, HEIGHT / 2 + 80);
        g.drawString("Press ESC to menu", WIDTH / 2 - 120, HEIGHT / 2 + 110);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gameState == GameState.MENU) {
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_1) startGame();
            else if (code == KeyEvent.VK_2) gameState = GameState.HIGH_SCORE;
            else if (code == KeyEvent.VK_3) gameState = GameState.SETTINGS;
            else if (code == KeyEvent.VK_ESCAPE) System.exit(0);
            return;
        }

        if (gameState == GameState.HIGH_SCORE) {
            if (code == KeyEvent.VK_ESCAPE) gameState = GameState.MENU;
            return;
        }

        if (gameState == GameState.SETTINGS) {
            if (code == KeyEvent.VK_ESCAPE) {
                settingsManager.save();
                gameState = GameState.MENU;
                return;
            }

            if (code == KeyEvent.VK_UP) settingIndex = (settingIndex + 3) % 4;
            else if (code == KeyEvent.VK_DOWN) settingIndex = (settingIndex + 1) % 4;
            else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT)
                changeSetting(code);

            return;
        }

        if (gameState == GameState.GAME_OVER) {
            if (code == KeyEvent.VK_ENTER) startGame();
            else if (code == KeyEvent.VK_ESCAPE) gameState = GameState.MENU;
            return;
        }

        if (gameState != GameState.PLAYING) return;

        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) upPressed = true;
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) downPressed = true;
    }

    private void changeSetting(int key) {
        boolean right = (key == KeyEvent.VK_RIGHT);
        switch (settingIndex) {
            case 0 -> settingsManager.setMusicVolume(
                    clamp(settingsManager.getMusicVolume() + (right ? 5 : -5), 0, 100)
            );
            case 1 -> settingsManager.setSfxVolume(
                    clamp(settingsManager.getSfxVolume() + (right ? 5 : -5), 0, 100)
            );
            case 2 -> {
                String d = settingsManager.getDifficulty();
                if (right) {
                    if (d.equals("Easy")) settingsManager.setDifficulty("Normal");
                    else if (d.equals("Normal")) settingsManager.setDifficulty("Hard");
                    else settingsManager.setDifficulty("Easy");
                } else {
                    if (d.equals("Hard")) settingsManager.setDifficulty("Normal");
                    else if (d.equals("Normal")) settingsManager.setDifficulty("Easy");
                    else settingsManager.setDifficulty("Hard");
                }
            }
            case 3 -> settingsManager.setFullscreen(!settingsManager.isFullscreen());
        }
    }

    private int clamp(int v, int min, int max) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) downPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
