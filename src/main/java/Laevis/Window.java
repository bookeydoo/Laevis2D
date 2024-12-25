package Laevis;

import LaevisUtilities.Time;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private final int Width;
    private final int Height;
    private final String Title;
    private long glfwWindow;
    private boolean FadeToBlack;
    public float r, g, b, a;

    private static Window window = null;

    private static Scene CurrentScene;

    //Constructor
    private Window() {
        this.Width = 1920;
        this.Height = 1080;
        this.Title = "Game Engine";

        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.a = 0;
    }

    //Change Scene
    public static void ChangeScene(int NewScene) {
        switch (NewScene) {
            case 0:
                CurrentScene = new LevelEditorScene();
                CurrentScene.InitScene();
                CurrentScene.StartScene();
                break;
            case 1:
                CurrentScene = new LevelScene();
                CurrentScene.InitScene();
                CurrentScene.StartScene();
                break;
            default:
                assert false: "Unknown Scene '" + NewScene + "'";
                break;
        }
    }

    //Get Window
    public static Window Get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    //Get Scene
    public static Scene GetScene() {
        return Get().CurrentScene;
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

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE,GL_ONE_MINUS_SRC_ALPHA);

        Window.ChangeScene(0);
    }

    //Engine Loop
    public void EngineLoop() {
        float BeginTime = Time.GetTime();
        float EndTime;
        float DeltaTime = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(r, g, b, a);

            glClear(GL_COLOR_BUFFER_BIT);

            if (DeltaTime >= 0) {
                CurrentScene.SceneUpdate(DeltaTime);
            }
            if (FadeToBlack) {
                r = Math.max(r - 0.01f, 0);
                g = Math.max(g - 0.01f, 0);
                b = Math.max(b - 0.01f, 0);
                a = Math.max(a - 0.01f, 0);
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
                FadeToBlack = true;
            }

            glfwSwapBuffers(glfwWindow);

            EndTime = Time.GetTime();
            DeltaTime = EndTime - BeginTime;
            BeginTime = EndTime;
        }
    }
}
