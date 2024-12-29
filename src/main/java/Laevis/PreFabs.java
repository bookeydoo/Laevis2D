package Laevis;

import Components.Sprite;
import Components.SpriteRenderer;
import org.joml.Vector2f;

public class PreFabs {
    public static GameObject generateSpriteObject(Sprite sprite,float sizeX,float sizeY){
        GameObject block=new GameObject("Sprite_Object_Gen",
                new Transform(new Vector2f(),new Vector2f(sizeX,sizeY)),0);
        SpriteRenderer renderer=new SpriteRenderer();
        renderer.setSprite(sprite);
        block.AddComponent(renderer);
        System.out.println("object created!!!");
        return block;
    }
}
