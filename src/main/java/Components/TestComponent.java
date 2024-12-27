package Components;

import Laevis.Component;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TestComponent extends Component {
    private int collidertype=0;
    private float friction=0;
    public Vector3f Velcoity=new Vector3f(0,0.5f,0);
    public transient Vector4f tmp=new Vector4f(0,0,0,0);


    @Override
    public void UpdateComponent(float DeltaTime) {

    }
}
