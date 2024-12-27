package LaevisUtilities;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time {
    public static float GetTime() {
        return (float) glfwGetTime();
    }
}
