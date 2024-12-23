package Laevis;

import Renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    private Renderer Renderer;
    protected Camera Camera;
    private boolean IsRunning = false;
    protected List<GameObject> GameObjects = new ArrayList<>();

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

    public Camera Camera() {
        return this.Camera;
    }
}
