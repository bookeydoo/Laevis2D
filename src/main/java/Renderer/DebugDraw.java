package Renderer;

import Laevis.Window;
import LaevisUtilities.AssetPool;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw {
    private static int MAX_LINES=500;


    private static List<Line2D> Lines=new ArrayList<>();

    //6 floats per vertex ,2 vertices per line
    private static float[] VertexArray=new float[MAX_LINES*6*2];
    private static Shader shader= AssetPool.GetShader("assets/shaders/debugLine2d.glsl");

    private static boolean started=false;
    private static int VAO_ID;
    private static int VBO_ID;


    public static void start(){
        //GENERATING VAO
        VAO_ID=glGenVertexArrays();
        glBindVertexArray(VAO_ID);

        //generating vbo
        VBO_ID=glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,VBO_ID);
        glBufferData(GL_ARRAY_BUFFER,VertexArray.length * Float.BYTES,GL_DYNAMIC_DRAW);

        //enable vaa
        glVertexAttribPointer(0,3,GL_FLOAT,false,6*Float.BYTES,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,3,GL_FLOAT,false,6*Float.BYTES,3*Float.BYTES);
        glEnableVertexAttribArray(1);

    }

    public static void BeginFrame(){

        if(!started) {
            start();
            started=true;
        }

        //Remove old unused lines
        for(int i=0;i<Lines.size();i++){
            if(Lines.get(i).BeginFrame()<0){
                Lines.remove(i);
                i--;
            }
        }
    }
    public static void Draw(){

        if(Lines.size()<=0)return;  //empty array of lines


        int index=0;
        for(Line2D line :Lines){
            for(int i=0;i<2;i++){
                Vector2f position= i==0 ? line.getFrom() :line.getFrom();
                Vector3f color=line.getColor();

                //load position
                VertexArray[index]=position.x;
                VertexArray[index+1]=position.y;
                VertexArray[index+2]=-10f;

                //load color
                VertexArray[index+3]=color.x;
                VertexArray[index+3]=color.y;
                VertexArray[index+3]=color.z;
                index+=6;
            }
        }
        glBindBuffer(GL_ARRAY_BUFFER,VBO_ID);
        glBufferSubData(GL_ARRAY_BUFFER,0,Arrays.copyOfRange(VertexArray,0,Lines.size()*6*2));


        //use shader
        shader.UseShader();
        shader.UploadMatrix4f("uProjection", Window.getScene().Camera().GetProjectionMatrix());
        shader.UploadMatrix4f("uView", Window.getScene().Camera().GetViewMatrix());

        //bind VAO

        glBindVertexArray(VAO_ID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //draw batch
        glDrawArrays(GL_LINES,0,Lines.size());

        //disable location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.DetachShader();
    }
    //ADD LINE2d method

    public static void addLine2d(Vector2f from,Vector2f To){
        addLine2d(from,To,new Vector3f(0,1,0),10);
    }
    public static void addLine2d(Vector2f from,Vector2f To,Vector3f color){
        addLine2d(from,To,color,10);
    }

    public static void addLine2d(Vector2f From,Vector2f To,Vector3f color,int lifetime){
        if(Lines.size()>=MAX_LINES) return;
        DebugDraw.Lines.add(new Line2D(From,To,color,lifetime));
    }




}
