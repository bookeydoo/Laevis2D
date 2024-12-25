package Laevis;

import Renderer.Renderer;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer Renderer = new Renderer();
    protected Camera Camera;
    private boolean IsRunning = false;
    protected List<GameObject> GameObjects = new ArrayList<>();
    protected  GameObject activegameobject=null;
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
    }
    public void imgui(){

    }

    public Camera Camera() {
        return this.Camera;
    }
}
