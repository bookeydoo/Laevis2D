package Laevis;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void InitScene() {

    }

    @Override
    public void SceneUpdate(float DeltaTime) {
        for (GameObject gameObject : this.GameObjects) {
            gameObject.UpdateGameObjects(DeltaTime);
        }
    }
}
