package Renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private String VertexSource;
    private String FragmentSource;
    private final String FilePath;

    public Shader(String FilePath) {
        this.FilePath = FilePath;
        try {
            String Source = new String(Files.readAllBytes(Paths.get(FilePath)));
            String[] SplitString = Source.split("(#type)( )+([a-zA-Z]+)");

            int Index = Source.indexOf("#type") + 6;
            int EOL = Source.indexOf("\r\n", Index);
            String FirstPattern = Source.substring(Index, EOL).trim();

            Index = Source.indexOf("#type", EOL) + 6;
            EOL = Source.indexOf("\r\n", Index);
            String SecondPattern = Source.substring(Index, EOL).trim();

            if (FirstPattern.equals("vertex") || FirstPattern.equals("Vertex")) {
                VertexSource = SplitString[1];
            } else if (FirstPattern.equals("fragment") || FirstPattern.equals("Fragment")) {
                FragmentSource = SplitString[1];
            } else {
                throw new IOException("Unexpected Token " + FirstPattern + " ");
            }

            if (SecondPattern.equals("vertex") || SecondPattern.equals("Vertex")) {
                VertexSource = SplitString[2];
            } else if (SecondPattern.equals("fragment") || SecondPattern.equals("Fragment")) {
                FragmentSource = SplitString[2];
            } else {
                throw new IOException("Unexpected Token " + SecondPattern + " ");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open Shader File: " + FilePath + " ";
        }
    }

    private int ShaderProgramID;
    private boolean BeingUsed = false;


    public void CompileShader() {
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, VertexSource);
        glCompileShader(vertexID);

        int VertexSuccess = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (VertexSuccess == GL_FALSE) {
            int Length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: " + FilePath + "\n\t Vertex Shader Compilation Failed!");
            System.out.println(glGetShaderInfoLog(vertexID, Length));
            assert false : "";
        }

        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, FragmentSource);
        glCompileShader(fragmentID);

        int FragmentSuccess = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (FragmentSuccess == GL_FALSE) {
            int Length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: " + FilePath + "\n\t Fragment Shader Compilation Failed!");
            System.out.println(glGetShaderInfoLog(fragmentID, Length));
            assert false : "";
        }

        ShaderProgramID = glCreateProgram();
        glAttachShader(ShaderProgramID, vertexID);
        glAttachShader(ShaderProgramID, fragmentID);
        glLinkProgram(ShaderProgramID);

        int ShaderProgramIDSuccess = glGetProgrami(ShaderProgramID, GL_LINK_STATUS);
        if (ShaderProgramIDSuccess == GL_FALSE) {
            int Length = glGetProgrami(ShaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: " + FilePath + "\n\t Linking Shaders Failed!");
            System.out.println(glGetProgramInfoLog(ShaderProgramID, Length));
            assert false : "";
        }
    }

    public void UseShader() {
        if (!BeingUsed) {
            //Bind Shader Program
            glUseProgram(ShaderProgramID);
            BeingUsed = true;
        }
    }

    public void DetachShader() {
        glUseProgram(0);
        BeingUsed = false;
    }

    public void UploadMatrix4f(String VariableName, Matrix4f Matrix4) {
        int VariableLocation = glGetUniformLocation(ShaderProgramID, VariableName);
        UseShader();
        FloatBuffer MatrixBuffer = BufferUtils.createFloatBuffer(16);
        Matrix4.get(MatrixBuffer);
        glUniformMatrix4fv(VariableLocation, false, MatrixBuffer);
    }

    public void UploadMatrix3f(String VariableName, Matrix3f Matrix3) {
        int VariableLocation = glGetUniformLocation(ShaderProgramID, VariableName);
        UseShader();
        FloatBuffer MatrixBuffer = BufferUtils.createFloatBuffer(9);
        Matrix3.get(MatrixBuffer);
        glUniformMatrix3fv(VariableLocation, false, MatrixBuffer);
    }

    public void UploadMatrix2f(String VariableName, Matrix2f Matrix2) {
        int VariableLocation = glGetUniformLocation(ShaderProgramID, VariableName);
        UseShader();
        FloatBuffer MatrixBuffer = BufferUtils.createFloatBuffer(4);
        Matrix2.get(MatrixBuffer);
        glUniformMatrix2fv(VariableLocation, false, MatrixBuffer);
    }

    public void UploadVector4f(String VariableName, Vector4f Vector4) {
        int VariableLocation = glGetUniformLocation(ShaderProgramID, VariableName);
        UseShader();
        glUniform4f(VariableLocation, Vector4.x, Vector4.y, Vector4.z, Vector4.w);
    }

    public void UploadVector3f(String VariableName, Vector3f Vector3) {
        int VariableLocation = glGetUniformLocation(ShaderProgramID, VariableName);
        UseShader();
        glUniform3f(VariableLocation, Vector3.x, Vector3.y, Vector3.z);
    }

    public void UploadVector2f(String VariableName, Vector2f Vector2) {
        int VariableLocation = glGetUniformLocation(ShaderProgramID, VariableName);
        UseShader();
        glUniform2f(VariableLocation, Vector2.x, Vector2.y);
    }

    public void UploadFloat(String VariableName, float Value) {
        int VariableLocation = glGetUniformLocation(ShaderProgramID, VariableName);
        UseShader();
        glUniform1f(VariableLocation, Value);
    }

    public void UploadInt(String VariableName, int Value) {
        int VariableLocation = glGetUniformLocation(ShaderProgramID, VariableName);
        UseShader();
        glUniform1i(VariableLocation, Value);
    }

    public void UploadIntArray(String VariableName, int[] Array) {
        int VariableLocation = glGetUniformLocation(ShaderProgramID, VariableName);
        UseShader();
        glUniform1iv(VariableLocation, Array);
    }

    public void UploadTexture(String VariableName, int TextureSlot) {
        int VariableLocation = glGetUniformLocation(ShaderProgramID, VariableName);
        UseShader();
        glUniform1i(VariableLocation, TextureSlot);
    }
}
