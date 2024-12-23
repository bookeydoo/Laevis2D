package Laevis;

import Components.SpriteRenderer;
import LaevisUtilities.AssetPool;
import Renderer.Renderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {
    private Vector2f Empty = new Vector2f();

    public LevelEditorScene() {

    }

    @Override
    public void InitScene() {
        this.Camera = new Camera(Empty);

        int xOffset = 10;
        int yOffset = 10;

        float TotalWidth = (float) (600 - xOffset * 2);
        float TotalHeight = (float) (300 - yOffset * 2);
        float SizeX = TotalWidth / 100.0f;
        float SizeY = TotalHeight / 100.0f;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPosition = xOffset + (x * SizeX);
                float yPosition = yOffset + (y * SizeY);

                GameObject gameObject = new GameObject("Object" + x + " " + y, new Transform(new Vector2f(xPosition, yPosition), new Vector2f(SizeX, SizeY)));
                gameObject.AddComponent(new SpriteRenderer(new Vector4f(xPosition / TotalWidth, yPosition / TotalHeight, 1, 1)));
                this.AddGameObjectToScene(gameObject);
            }
        }

        LoadResources();
    }

    private void LoadResources() {
        AssetPool.GetShader("Assets/Shaders/default.glsl");
    }

    @Override
    public void SceneUpdate(float DeltaTime) {
        System.out.println("FPS: " + (1.0f / DeltaTime));

        for (GameObject gameObject : this.GameObjects) {
            gameObject.UpdateGameObjects(DeltaTime);
        }
        this.Renderer.Render();
    }
}
