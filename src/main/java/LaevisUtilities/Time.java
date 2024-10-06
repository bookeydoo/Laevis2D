package LaevisUtilities;

public class Time {
    public static float TimeStarted = System.nanoTime();

    public static float GetTime() {
        return (float) ((System.nanoTime() - TimeStarted) * 1E-9);
    }
}
