import java.io.*;
import java.util.Arrays;

public class HighScoreManager {

    private int[] top = new int[5];
    private final String FILE = "highscore.dat";

    public HighScoreManager() { load(); }

    public int[] getTopScores() {
        return Arrays.copyOf(top, top.length);
    }

    public void submitScore(int s) {
        for (int i = 0; i < 5; i++) {
            if (s > top[i]) {
                for (int j = 4; j > i; j--) top[j] = top[j - 1];
                top[i] = s;
                save();
                return;
            }
        }
    }

    private void save() {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(FILE))) {
            for (int i = 0; i < 5; i++) out.writeInt(top[i]);
        } catch (IOException ignored) {}
    }

    private void load() {
        File f = new File(FILE);
        if (!f.exists()) return;

        try (DataInputStream in = new DataInputStream(new FileInputStream(FILE))) {
            for (int i = 0; i < 5; i++) top[i] = in.readInt();
        } catch (IOException ignored) {}
    }
}
