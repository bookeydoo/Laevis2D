package Components;

import Laevis.Component;

public class SpriteRenderer extends Component {
    private boolean FirstTime = false;

    @Override
    public void StartComponent() {
        System.out.println("I am Starting");
    }

    @Override
    public void UpdateComponent(float DeltaTime) {
        if (!FirstTime) {
            System.out.println("I am Updating");
            FirstTime = true;
        }
    }
}
