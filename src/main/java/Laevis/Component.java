package Laevis;

public abstract class Component {

    public transient GameObject GameObject = null;

    public void StartComponent() {

    }

    public abstract void UpdateComponent(float DeltaTime);

    public void imgui(){

    }
}
