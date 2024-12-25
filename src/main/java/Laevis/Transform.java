package Laevis;

import org.joml.Vector2f;

public class Transform {
    public Vector2f Position;
    public Vector2f Scale;

    public Vector2f Empty = new Vector2f();

    public Transform() {
        this.Position = Empty;
        this.Scale = Empty;
    }

    public Transform(Vector2f Position) {
        InitTransform(Position, Empty);
    }

    public Transform(Vector2f Position, Vector2f Scale) {
        InitTransform(Position, Scale);
    }

    public void InitTransform(Vector2f Position, Vector2f Scale) {
        this.Position = Position;
        this.Scale = Scale;
    }
    public Transform copy(){
        return new Transform(new Vector2f(this.Position),new Vector2f(this.Scale));
    }

    public  void copy(Transform to){    //it copies to the  new transform object we pass in
        to.Position.set(this.Position);
        to.Scale.set(this.Scale);

    }

    @Override
    public boolean equals(Object O){

    if(O==null) return false;

    if(!(O instanceof Transform )) return false;

    Transform t=(Transform) O;
    return t.Position.equals(this.Position) && t.Scale.equals(this.Scale);
    }
}
