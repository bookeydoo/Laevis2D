package scenes;

import Laevis.Window;

public class LevelScene extends Scene {
    public LevelScene() {
        System.out.println("Level Scene");
        Window.Get().r = 1;
        Window.Get().g = 1;
        Window.Get().b = 1;
    }

    @Override
    public void InitScene() {

    }

    @Override
    public void SceneUpdate(float DeltaTime) {

    }
}
