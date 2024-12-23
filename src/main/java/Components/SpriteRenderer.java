package Components;

import Laevis.Component;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private Vector4f Color;

    public SpriteRenderer(Vector4f Color) {
        this.Color = Color;
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
}
