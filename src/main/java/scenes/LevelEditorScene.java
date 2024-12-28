package scenes;

import Components.*;
import Laevis.Camera;
import Laevis.GameObject;
import Laevis.PreFabs;
import Laevis.Transform;
import LaevisUtilities.AssetPool;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {

    private static SpriteSheet sprites;
    private GameObject gameObject1;
    private Vector2f CameraOffsetLocal = new Vector2f(-50, 0);

    MouseControls mouseControls=new MouseControls();

    public LevelEditorScene() {

    }

    @Override
    public void InitScene() {
        LoadResources();

        this.Camera = new Camera(CameraOffsetLocal);

        if(!levelloaded){
            this.gameObject1=GameObjects.get(0);
            return;
        }

         sprites = AssetPool.GetSpriteSheet("Assets/Images/spritesheet.png");
        if(sprites==null){
            System.err.println("sprites failed to load ");
            return;
        }

        Vector2f TransformPosition1 = new Vector2f(100, 100);
        Vector2f TransformScale1 = new Vector2f(128, 128);

        Vector2f TransformPosition2 = new Vector2f(220, 100);
        Vector2f TransformScale2 = new Vector2f(128, 128);

        Vector2f TransformPosition3 = new Vector2f(340, 100);
        Vector2f TransformScale3 = new Vector2f(128, 128);

        Vector2f TransformPosition4 = new Vector2f(460, 100);
        Vector2f TransformScale4 = new Vector2f(128, 128);

        //TODO try to fix the missing textures shit and implement proper imgui menus
        //added random z index values
        SpriteRenderer gameObject1Sprite=new SpriteRenderer();
        gameObject1 = new GameObject("Object 1", new Transform(TransformPosition1, TransformScale1),-1);
        gameObject1.AddComponent(gameObject1Sprite);
        gameObject1.AddComponent(new TestComponent());
        gameObject1Sprite.setColor(new Vector4f(1,1,1,1));
        this.AddGameObjectToScene(gameObject1);
        this.activegameobject=gameObject1;

/*
        SpriteRenderer gameObject2SpriteRendrer=new SpriteRenderer();
        Sprite gameObject2Sprite=new Sprite();
        GameObject gameObject2 = new GameObject("Object 2", new Transform(TransformPosition2, TransformScale2),2);
        gameObject2.AddComponent(gameObject2SpriteRendrer);
        gameObject2Sprite.setTexture(AssetPool.GetTexture("x"));
        gameObject2SpriteRendrer.setSprite(gameObject2Sprite);
        gameObject2SpriteRendrer.setColor(new Vector4f(1,1,1,1));
        gameObject2.AddComponent(gameObject2SpriteRendrer);
        this.AddGameObjectToScene(gameObject2);
*/

/*
        GameObject gameObject3 = new GameObject("Object 3", new Transform(TransformPosition3, TransformScale3),2);
        gameObject3.AddComponent(new SpriteRenderer(Sprites.GetSprite(2)));
        this.AddGameObjectToScene(gameObject3);

        GameObject gameObject4 = new GameObject("Object 4", new Transform(TransformPosition4, TransformScale4),-1);
        gameObject4.AddComponent(new SpriteRenderer(Sprites.GetSprite(3)));
        this.AddGameObjectToScene(gameObject4);*/


    }

    private void LoadResources() {
        AssetPool.GetShader("Assets/Shaders/default.glsl");

        AssetPool.AddSpriteSheet(
            "Assets/Images/spritesheet.png",
            new SpriteSheet(AssetPool.GetTexture("Assets/Images/spritesheet.png"), 16, 16, 26, 0)
        );
    }
    private  int spriteindex=0;
    private float spriteFliptime=0.2f;
    private float spriteFliptimeleft=0.0f;

    @Override
    public void SceneUpdate(float DeltaTime) {

        mouseControls.UpdateComponent(DeltaTime);
        System.out.println("FPS: " + (1.0f / DeltaTime));


        for (GameObject gameObject : this.GameObjects) {
            gameObject.UpdateGameObjects(DeltaTime);
        }
        this.Renderer.Render();
    }
    @Override
    public void imgui(){

        ImGui.begin("test window");

        ImVec2 WindowPos=new ImVec2();
        ImGui.getWindowPos(WindowPos);
        ImVec2 WindowSize= new ImVec2();
        ImGui.getWindowSize(WindowSize);
        ImVec2 IconSpacing=new ImVec2();
        ImGui.getStyle().getItemSpacing(IconSpacing);


        //this is the actual window x as in translated to screen coords
        float WindowX2=WindowPos.x+WindowSize.x;

        for(int i=0;i<sprites.size();i++){
            Sprite sprite=sprites.GetSprite(i);

            float spriteWidth=sprite.getWidth()*4;
            float spriteHeight=sprite.getHeight()*4;
            int id=sprite.getTexId();
            Vector2f[] Texcoords=sprite.GetTextureCoordinates();

            ImGui.pushID(i);

            if(ImGui.imageButton(id,spriteWidth,spriteHeight,Texcoords[0].x,Texcoords[0].y,
                    Texcoords[2].x,Texcoords[2].y)){
                GameObject object= PreFabs.generateSpriteObject(sprite,spriteWidth,spriteHeight);
                //attach this to mouse cursor
                mouseControls.pickupObject(object);

            }
            ImGui.popID();
            ImVec2 lastButtonPos= new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2= lastButtonPos.x;
            float nextButtonX2=lastButtonX2+ IconSpacing.x +spriteWidth;
            if(i+1<sprites.size() && nextButtonX2<WindowX2){
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}
