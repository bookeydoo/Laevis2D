package Components;

import Laevis.Component;
import Laevis.GameObject;
import Laevis.Transform;
import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private Vector4f Color=new Vector4f(1,1,1,1);
    private Vector2f[] TextureCoordinates;
    private Texture Texture;
    private Sprite Sprite =new Sprite();
    private transient Transform lastTransform;

    private transient boolean isDirty=false;
/*
    public SpriteRenderer(Vector4f Color) {
        this.Color = Color;
        this.Sprite = new Sprite(null);
        this.isDirty=true;
    }*/
//
//    public SpriteRenderer(Sprite Sprite) {
//        this.Sprite = Sprite;
//        this.Color = new Vector4f(1, 1, 1, 1);
//        this.isDirty=true;
//    }

    @Override
    public void StartComponent() {
        this.lastTransform= GameObject.Transform.copy();

    }

    @Override
    public void UpdateComponent(float DeltaTime) {
    if(!this.lastTransform.equals(this.GameObject.Transform)){
        this.GameObject.Transform.copy(this.lastTransform);
        isDirty=true;
    }

    }

    public Vector4f GetColor() {
        return this.Color;
    }

    public Texture GetTexture() {
        return Sprite.GetTexture();
    }

    public Vector2f[] GetTextureCoordinates() {
        return Sprite.GetTextureCoordinates();
    }


    public void setSprite(Sprite sprite){
        this.Sprite=sprite;
        this.isDirty=true;

    }
    public void setColor(Vector4f color){
        if(!this.Color.equals(color)){
            this.isDirty=true;
            this.Color.set(color);
        }

    }
    public boolean isDirty(){
        return this.isDirty;
    }
    public void setClean(){
         this.isDirty=false;
    }

}
