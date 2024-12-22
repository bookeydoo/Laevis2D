package Laevis;

public abstract class Scene {
    protected Camera Camera;

    public Scene() {

    }

    public abstract void InitScene();

    public abstract void SceneUpdate(float DeltaTime);
}
