package Components;

import Laevis.Component;
import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private Vector4f Color;
    private Vector2f[] TextureCoordinates;
    private Texture Texture;
    private Sprite Sprite;

    public SpriteRenderer(Vector4f Color) {
        this.Color = Color;
        this.Sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite Sprite) {
        this.Sprite = Sprite;
        this.Color = new Vector4f(1, 1, 1, 1);
    }

    @Override
    public void StartComponent() {

    }

    @Override
    public void UpdateComponent(float DeltaTime) {

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
}
