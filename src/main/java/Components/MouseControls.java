package Components;

import Laevis.Component;
import Laevis.GameObject;
import Laevis.MouseListener;
import Laevis.Window;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;

public class MouseControls extends Component {

    GameObject holdingObject=null;

    public void pickupObject(GameObject GO){
        this.holdingObject=GO;
        Window.getScene().AddGameObjectToScene(GO);

    }

    public void place(){
        this.holdingObject=null;
    }


    @Override
    public void UpdateComponent(float DeltaTime) {
        if(holdingObject != null){
            holdingObject.Transform.Position.x= MouseListener.getOrthoX()-16;
            holdingObject.Transform.Position.y= MouseListener.getOrthoY()-16;

            if(MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
            }
        }

    }
}
