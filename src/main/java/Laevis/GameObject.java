package Laevis;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private static int ID_Counter=0;
    private int UID=-1;

    private String Name;
    private List<Component> Components;
    public Transform Transform;
    private int zIndex;

    public GameObject(String Name) {
        this.Name = Name;
        this.Components = new ArrayList<>();
        this.Transform = new Transform();
        this.zIndex=0;

        this.UID=ID_Counter++;
    }

    public GameObject(String Name, Transform Transform,int zIndex) {
        this.Name = Name;
        this.Components = new ArrayList<>();
        this.Transform = Transform;
        this.zIndex=zIndex;
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
        Component.generateID();
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
    public int getzIndex(){
        return this.zIndex;
    }

    public void imgui(){
        for(Component c: Components){
            c.imgui();
        }
    }

    public static void init(int maxid){
        ID_Counter=maxid;
    }
    public int getUID(){
        return this.UID;
    }

    public List<Component>getComponents(){
        return this.Components;
    }
}
