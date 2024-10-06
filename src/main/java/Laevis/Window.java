package Laevis;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private final int Width;
    private final int Height;
    private final String Title;
    private long glfwWindow;
    private boolean FadeToLightGreen;
    private float r, g, b, a;

    private static Window window = null;

    //Constructor
    private Window() {
        this.Width = 1920;
        this.Height = 1080;
        this.Title = "GravesBullet";
        this.r = 1;
        this.g = 1;
        this.b = 1;
        this.a = 1;
    }

    //Get Window
    public static Window Get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    //Run Engine
    public void Run() {
        System.out.println("LWJGL Version: " + Version.getVersion());

        EngineInit();
        EngineLoop();

        glfwFreeCallbacks(glfwWindow);

        glfwDestroyWindow(glfwWindow);

        glfwTerminate();

        glfwSetErrorCallback(null).free();
    }

    //Initialize Engine
    public void EngineInit() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to Initialize GLFW");
        }

        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.Width, this.Height, this.Title, NULL, NULL); //glfwGetPrimaryMonitor()
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW Window");
        }

        //Event Handlers Callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::MousePositionCallback);

        glfwSetMouseButtonCallback(glfwWindow, MouseListener::MouseButtonCallback);

        glfwSetScrollCallback(glfwWindow, MouseListener::MouseScrollCallback);

        glfwSetKeyCallback(glfwWindow, KeyListener::KeyCallback);


        glfwMakeContextCurrent(glfwWindow);

        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    //Engine Loop
    public void EngineLoop() {
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(r, g, b, a);

            glClear(GL_COLOR_BUFFER_BIT);

            if (FadeToLightGreen) {
                r = Math.max(r - 0.01f, 0);
                g = Math.max(g - 0.01f, 0);
                b = Math.max(b - 0.01f, 0);
                a = Math.max(a - 0.01f, 0);
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
                FadeToLightGreen = true;
            }

            glfwSwapBuffers(glfwWindow);
        }
    }
}
