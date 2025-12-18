import java.io.*;

public class SettingsManager {

    private int musicVolume = 70;
    private int sfxVolume = 80;
    private String difficulty = "Normal";
    private boolean fullscreen = false;

    private final String FILE = "settings.dat";

    public SettingsManager() {
        load();
    }

    public int getMusicVolume() { return musicVolume; }
    public int getSfxVolume() { return sfxVolume; }
    public String getDifficulty() { return difficulty; }
    public boolean isFullscreen() { return fullscreen; }

    public void setMusicVolume(int v) { musicVolume = v; }
    public void setSfxVolume(int v) { sfxVolume = v; }
    public void setDifficulty(String d) { difficulty = d; }
    public void setFullscreen(boolean f) { fullscreen = f; }

    public void save() {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(FILE))) {
            out.writeInt(musicVolume);
            out.writeInt(sfxVolume);
            out.writeUTF(difficulty);
            out.writeBoolean(fullscreen);
        } catch (IOException e) {
            System.err.println("Cannot save settings");
        }
    }

    public void load() {
        File f = new File(FILE);
        if (!f.exists()) return;

        try (DataInputStream in = new DataInputStream(new FileInputStream(FILE))) {
            musicVolume = in.readInt();
            sfxVolume = in.readInt();
            difficulty = in.readUTF();
            fullscreen = in.readBoolean();
        } catch (IOException e) {
            System.err.println("Cannot load settings");
        }
    }
}
