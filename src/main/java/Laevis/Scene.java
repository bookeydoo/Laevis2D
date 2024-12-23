package Laevis;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
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
        }
        IsRunning = true;
    }

    public void AddGameObjectToScene(GameObject gameObject) {
        if (!IsRunning) {
            GameObjects.add(gameObject);
        } else {
            GameObjects.add(gameObject);
            gameObject.StartGameObjects();
        }
    }

    public abstract void SceneUpdate(float DeltaTime);
}
