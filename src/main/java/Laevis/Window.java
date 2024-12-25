package Laevis;

import LaevisUtilities.Time;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private int Width;
    private int Height;
    private final String Title;
    private long glfwWindow;
    private boolean FadeToBlack;
    private ImGuiLayer imGuiLayer;
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


    //init window very important for it to work
    public void init(){

        imGuiLayer=new ImGuiLayer();
        imGuiLayer.imguiglfw.init(glfwWindow,true);
        imGuiLayer.implGl3.init("#version 330 core");
    }

    //Run Engine
    public void Run() {
        System.out.println("LWJGL Version: " + Version.getVersion());

        EngineLoop();

        glfwFreeCallbacks(glfwWindow);

        glfwDestroyWindow(glfwWindow);

        glfwTerminate();

        glfwSetErrorCallback(null).free();
    }

    //Initialize Engine
    public void EngineInit() { //init
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() ) {
            System.out.println("Unable to initialize GLFW");
            System.exit(-1);
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindow = glfwCreateWindow(1920, 1080, "My Window", NULL, NULL);

        if (glfwWindow == NULL) {
            System.out.println("Unable to create window");
            System.exit(-1);
        }

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
            if(DeltaTime>=0){
                CurrentScene.SceneUpdate(DeltaTime);
            }
            ImGui.newFrame();                      // Start a new frame for ImGui (sets up the internal state for a new frame)
            imGuiLayer.implGl3.newFrame();          // Prepare OpenGL renderer for the new frame
            imGuiLayer.imguiglfw.newFrame();        // Prepare GLFW for the new frame

            imGuiLayer.ImGui();                    // Render your ImGui UI elements (create windows, buttons, etc.)

            ImGui.endFrame();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();

            ImGui.render();                         // Render the ImGui draw data to be processed by the renderer
            imGuiLayer.implGl3.renderDrawData(ImGui.getDrawData());
            glfwSwapBuffers(glfwWindow);
            glfwPollEvents();


            EndTime = Time.GetTime();
            DeltaTime = EndTime - BeginTime;
            BeginTime = EndTime;
        }

    }
    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene() {
        return get().CurrentScene;
    }

    public static int getWidth() {
        return get().Width;
    }

    public static int getHeight() {
        return get().Height;
    }

    public static void setWidth(int newWidth) {
        get().Width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().Height = newHeight;
    }
}
