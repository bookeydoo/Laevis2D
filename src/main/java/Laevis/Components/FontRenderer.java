package Components;

import Laevis.Component;
import Laevis.GameObject;

public class FontRenderer extends Component {

    @Override
    public void StartComponent() {
        if (GameObject.GetComponent(SpriteRenderer.class) != null) {
            System.out.println("Found Font Renderer!");
        }
    }

    @Override
    public void UpdateComponent(float DeltaTime) {

    }
}
