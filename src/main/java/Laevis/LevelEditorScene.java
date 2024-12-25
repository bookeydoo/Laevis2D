package Laevis;

import Components.Sprite;
import Components.SpriteRenderer;
import Components.SpriteSheet;
import LaevisUtilities.AssetPool;
import Renderer.Renderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {
    private Vector2f CameraOffsetLocal = new Vector2f(-50, 0);

    public LevelEditorScene() {

    }

    @Override
    public void InitScene() {
        LoadResources();

        this.Camera = new Camera(CameraOffsetLocal);

        SpriteSheet Sprites = AssetPool.GetSpriteSheet("Assets/Images/spritesheet.png");

        Vector2f TransformPosition1 = new Vector2f(100, 100);
        Vector2f TransformScale1 = new Vector2f(128, 128);

        Vector2f TransformPosition2 = new Vector2f(220, 100);
        Vector2f TransformScale2 = new Vector2f(128, 128);

        Vector2f TransformPosition3 = new Vector2f(340, 100);
        Vector2f TransformScale3 = new Vector2f(128, 128);

        Vector2f TransformPosition4 = new Vector2f(460, 100);
        Vector2f TransformScale4 = new Vector2f(128, 128);

        GameObject gameObject1 = new GameObject("Object 1", new Transform(TransformPosition1, TransformScale1));
        gameObject1.AddComponent(new SpriteRenderer(Sprites.GetSprite(0)));
        this.AddGameObjectToScene(gameObject1);

        GameObject gameObject2 = new GameObject("Object 2", new Transform(TransformPosition2, TransformScale2));
        gameObject2.AddComponent(new SpriteRenderer(Sprites.GetSprite(1)));
        this.AddGameObjectToScene(gameObject2);


        GameObject gameObject3 = new GameObject("Object 3", new Transform(TransformPosition3, TransformScale3));
        gameObject3.AddComponent(new SpriteRenderer(Sprites.GetSprite(2)));
        this.AddGameObjectToScene(gameObject3);

        GameObject gameObject4 = new GameObject("Object 4", new Transform(TransformPosition4, TransformScale4));
        gameObject4.AddComponent(new SpriteRenderer(Sprites.GetSprite(3)));
        this.AddGameObjectToScene(gameObject4);
    }

    private void LoadResources() {
        AssetPool.GetShader("Assets/Shaders/default.glsl");

        AssetPool.AddSpriteSheet(
            "Assets/Images/spritesheet.png",
            new SpriteSheet(AssetPool.GetTexture("Assets/Images/spritesheet.png"), 16, 16, 26, 0)
        );
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
