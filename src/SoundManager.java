import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class SoundManager {

    public static void playSound(String path) {
        try {
            File soundFile = new File(path);
            if (!soundFile.exists()) return;
            AudioInputStream in = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static Clip loopBackground(String path) {
        try {
            File soundFile = new File(path);
            if (!soundFile.exists()) return null;
            AudioInputStream in = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
