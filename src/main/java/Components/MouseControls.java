package Components;

import Laevis.Component;
import Laevis.GameObject;
import Laevis.MouseListener;
import Laevis.Window;
import org.joml.Vector2f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;

public class MouseControls extends Component {

    GameObject holdingObject=null;

    public void pickupObject(GameObject GO){
        this.holdingObject=GO;
        Window.getScene().AddGameObjectToScene(GO);
        System.out.println("Picked up object: " );

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

    // Update the position based on the mouse position in your update method
    public void updateMousePosition(float mouseX, float mouseY) {
        if (holdingObject != null) {
            holdingObject.Transform.Position.x=mouseX;

            holdingObject.Transform.Position.y=mouseY;
        }
    }
}
