package Laevis;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener KeyInstance;
    private boolean KeyPressed[] = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener Get() {
        if (KeyListener.KeyInstance == null) {
            KeyListener.KeyInstance = new KeyListener();
        }
        return KeyListener.KeyInstance;
    }

    public static void KeyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            Get().KeyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            Get().KeyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int KeyCode) {
        return Get().KeyPressed[KeyCode];
    }
}
