package Components;

import Laevis.Component;
import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private Vector4f Color;
    private Vector2f[] TextureCoordinates;
    private Texture Texture;

    public SpriteRenderer(Vector4f Color) {
        this.Texture = null;
        this.Color = Color;
    }

    public SpriteRenderer(Texture Texture) {
        this.Texture = Texture;
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
        return this.Texture;
    }

    public Vector2f[] GetTextureCoordinates() {
        Vector2f[] TextureCoordinates = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
        };
        return TextureCoordinates;
    }
}
