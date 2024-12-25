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
}
