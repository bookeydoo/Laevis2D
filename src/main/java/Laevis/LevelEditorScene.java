package Laevis;

import LaevisUtilities.Time;
import Renderer.Shader;
import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class LevelEditorScene extends Scene {
    private String VertexShaderSource = "#version 330 core\n" +
            "layout (location = 0) in vec3 AttributePosition;\n" +
            "layout (location = 1) in vec4 AttributeColor;\n" +
            "out vec4 FragmentColor;\n" +
            "void main() {\n" +
            "    FragmentColor = AttributeColor;\n" +
            "    gl_Position = vec4(AttributePosition, 1.0);\n" +
            "}";

    private String FragmentShaderSource = "#version 330 core\n" +
            "in vec4 FragmentColor;\n" +
            "out vec4 Color;\n" +
            "void main() {\n" +
            "    Color = FragmentColor;\n" +
            "}";

    private int VertexID, FragmentID, ShaderProgram;

    private float[] VertexArray = {
        //Position                //Color                      //UV Coordinates
        //X, Y, Z                 //R, G, B, A
        100f, 0.0f, 0.0f,         1.0f, 0.0f, 0.0f, 1.0f,      1, 1,    // Bottom Right
        0.0f, 100f, 0.0f,         0.0f, 1.0f, 0.0f, 1.0f,      0, 0,    // Top Left
        100f, 100f, 0.0f,         0.0f, 0.0f, 1.0f, 1.0f,      1, 0,    // Top Right
        0.0f, 0.0f, 0.0f,         0.0f, 1.0f, 1.0f, 1.0f,      0, 1     // Bottom Left
    };

    // Coounter ClockWise Order
    private int[] ElementArray = {
        2, 1, 0,   // Top Right Triangle
        0, 1, 3    // Bottom Left Triangle
    };

    private int VAO_ID, VBO_ID, EBO_ID;

    private Shader DefaultShader;
    private Texture TestTexture;

    public LevelEditorScene() {

    }

    @Override
    public void InitScene() {
        Vector2f Position = new Vector2f();
        this.Camera = new Camera(Position);

        DefaultShader = new Shader("Assets/Shaders/default.glsl");
        DefaultShader.CompileShader();

        //this.TestTexture = new Texture("Assets/Images/testImage.jpg");
        this.TestTexture = new Texture("Assets/Images/coolcat.jpg");
        //Generate VAO, VBO, EBO
        VAO_ID = glGenVertexArrays();
        glBindVertexArray(VAO_ID);

        //Create Float Buffer of Vertices
        FloatBuffer VertexBuffer = BufferUtils.createFloatBuffer(VertexArray.length);
        VertexBuffer.put(VertexArray).flip();

        //Create VBO
        VBO_ID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);
        glBufferData(GL_ARRAY_BUFFER, VertexBuffer, GL_STATIC_DRAW);

        //Create the Indices
        IntBuffer ElementBuffer = BufferUtils.createIntBuffer(ElementArray.length);
        ElementBuffer.put(ElementArray).flip();

        EBO_ID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO_ID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ElementBuffer, GL_STATIC_DRAW);

        //Add the Vertex Attribute Pointers
        int PositionsSize = 3;
        int ColorSize = 4;
        int UVSize = 2;
        int VertexSizeBytes = (PositionsSize + ColorSize + UVSize) * Float.BYTES;

        glVertexAttribPointer(0, PositionsSize, GL_FLOAT, false, VertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, ColorSize, GL_FLOAT, false, VertexSizeBytes, PositionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, UVSize, GL_FLOAT, false, VertexSizeBytes, (PositionsSize + ColorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void SceneUpdate(float DeltaTime) {
        Camera.Position.x -= DeltaTime * 45.0f;
        Camera.Position.y -= DeltaTime * 20.0f;

        DefaultShader.UseShader();

        DefaultShader.UploadTexture("TextureSampler", 0);
        glActiveTexture(GL_TEXTURE0);
        TestTexture.BindTexture();

        DefaultShader.UploadMatrix4f("UniformProjectionMatrix", Camera.GetProjectionMatrix());
        DefaultShader.UploadMatrix4f("UniformViewMatrix", Camera.GetViewMatrix());
        DefaultShader.UploadFloat("UniformTime", Time.GetTime());

        //Bind VAO
        glBindVertexArray(VAO_ID);

        //Enable The Vertex Attribute Pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, ElementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind Everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        DefaultShader.DetachShader();
    }
}
