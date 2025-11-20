package puppy.code;

/**
 * Singleton para guardar configuración global:  volumen, highscore, acceso centralizado.
 */
public class Settings {
    private static Settings instance;
    private int highScore = 0;
    private float volumen = 1f;

    private Settings() {}

    public static synchronized Settings getInstance() {
        if (instance == null) instance = new Settings();
        return instance;
    }

    public int getHighScore() {
        return highScore;
    }
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public float getVolumen() {
        return volumen;
    }
    public void setVolumen(float volumen) {
        this.volumen = Math.max(0f, Math.min(1f, volumen));
    }
}
