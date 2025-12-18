import javax.swing.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Chicken Invaders - Cute Java Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel();
        add(panel);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
