package Laevis;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String Name;
    private List<Component> Components;

    public GameObject(String Name) {
        this.Name = Name;
        this.Components = new ArrayList<>();
    }

    public <T extends Component> T GetComponent(Class<T> ComponentClass) {
        for (Component Component : Components) {
            if (ComponentClass.isAssignableFrom(Component.getClass())) {
                try {
                    return ComponentClass.cast(Component);
                }
                catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting Component.";
                }
            }
        }

        return null;
    }

    public <T extends Component> void RemoveComponent(Class<T> ComponentClass) {
        for (int i = 0; i < Components.size(); i++) {
            Component Component = Components.get(i);
            if (ComponentClass.isAssignableFrom(Components.getClass())) {
                Components.remove(i);
                return;
            }
        }
    }

    public void AddComponent(Component Component) {
        this.Components.add(Component);
        Component.GameObject = this;
    }

    public void UpdateGameObjects(float DeltaTime) {
        for (int i = 0; i < Components.size(); i++) {
            Components.get(i).UpdateComponent(DeltaTime);
        }
    }

    public void StartGameObjects() {
        for (int i = 0; i < Components.size(); i++) {
            Components.get(i).StartComponent();
        }
    }
}
