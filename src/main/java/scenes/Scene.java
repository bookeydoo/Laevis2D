package scenes;

import Laevis.*;
import Renderer.Renderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer Renderer = new Renderer();
    protected Laevis.Camera Camera;
    private boolean IsRunning = false;
    protected List<GameObject> GameObjects = new ArrayList<>();
    protected  GameObject activegameobject=null;
    protected boolean levelloaded=false;
    public Scene() {

    }

    public void InitScene() {

    }

    public void StartScene() {
        for (GameObject gameObject : GameObjects) {
            gameObject.StartGameObjects();
            this.Renderer.AddRenderer(gameObject);
        }
        IsRunning = true;
    }

    public void AddGameObjectToScene(GameObject gameObject) {
        if (!IsRunning) {
            GameObjects.add(gameObject);
        } else {
            GameObjects.add(gameObject);
            gameObject.StartGameObjects();
            this.Renderer.AddRenderer(gameObject);
        }
    }

    public abstract void SceneUpdate(float DeltaTime);

    public void sceneImGui(){
        if(activegameobject !=null){
            ImGui.begin("inspector");
            activegameobject.imgui();
            ImGui.end();
        }
        imgui();
    }
    public void imgui(){

    }

    public Camera Camera() {
        return this.Camera;
    }

    public void saveFile(){
        Gson gson=new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Component.class,new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class,new GameObjectDeserializer())
                .create();

        try {
            FileWriter Writer=new FileWriter("Level.txt");
            Writer.write(gson.toJson(this.GameObjects));
            Writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void load(){
        Gson gson=new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Component.class,new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class,new GameObjectDeserializer())
                .create();
        String infile="";
        try {
            infile=new String(Files.readAllBytes(Paths.get("Level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!infile.equals("")){
            int maxGO_ID=-1;
            int maxCompID=-1;

            GameObject[] gameObjects=gson.fromJson(infile,GameObject[].class);
            for(int i=0;i<gameObjects.length;i++){
                AddGameObjectToScene(gameObjects[i]);

                for(Component c: gameObjects[i].getComponents()){
                    if(c.getU_ID()>maxCompID){
                        maxCompID=c.getU_ID();
                    }
                }
                if(gameObjects[i].getUID() > maxGO_ID){
                    maxGO_ID=gameObjects[i].getUID();
                }
            }

            maxGO_ID++;
            maxCompID++;
            GameObject.init(maxGO_ID);
            Component.init(maxCompID);
            this.levelloaded=true;

        }
    }
}
