package Renderer;

import org.joml.Matrix4f;
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
    private String FilePath;

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

        System.out.println(VertexSource);
        System.out.println(FragmentSource);
    }

    private int VertexID, FragmentID, ShaderProgramID;

    public void CompileShader() {
        VertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(VertexID, VertexSource);
        glCompileShader(VertexID);

        int VertexSuccess = glGetShaderi(VertexID, GL_COMPILE_STATUS);
        if (VertexSuccess == GL_FALSE) {
            int Length = glGetShaderi(VertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: " + FilePath + "\n\t Vertex Shader Compilation Failed!");
            System.out.println(glGetShaderInfoLog(VertexID, Length));
            assert false : "";
        }

        FragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(FragmentID, FragmentSource);
        glCompileShader(FragmentID);

        int FragmentSuccess = glGetShaderi(FragmentID, GL_COMPILE_STATUS);
        if (FragmentSuccess == GL_FALSE) {
            int Length = glGetShaderi(FragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: " + FilePath + "\n\t Fragment Shader Compilation Failed!");
            System.out.println(glGetShaderInfoLog(FragmentID, Length));
            assert false : "";
        }

        ShaderProgramID = glCreateProgram();
        glAttachShader(ShaderProgramID, VertexID);
        glAttachShader(ShaderProgramID, FragmentID);
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
        //Bind Shader Program
        glUseProgram(ShaderProgramID);
    }

    public void DetachShader() {
        glUseProgram(0);
    }

    public void UploadMatrix4f(String VariableName, Matrix4f Matrix4) {
        int VariableLocation = glGetUniformLocation(ShaderProgramID, VariableName);
        FloatBuffer MatrixBuffer = BufferUtils.createFloatBuffer(16);
        Matrix4.get(MatrixBuffer);
        glUniformMatrix4fv(VariableLocation, false, MatrixBuffer);
    }
}
