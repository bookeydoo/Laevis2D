package Laevis;

public abstract class Component {

    public GameObject GameObject = null;

    public void StartComponent() {

    }

    public abstract void UpdateComponent(float DeltaTime);

    public void imgui(){

    }
}
