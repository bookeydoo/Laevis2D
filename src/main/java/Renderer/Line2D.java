package Renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D {

    private Vector2f From;
    private Vector2f To;
    private Vector3f Color;
    private int Lifetime;



    public Line2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        From = from;
        To = to;
        Color = color;
        Lifetime = lifetime;
    }

    public int BeginFrame(){
        this.Lifetime--;
        return this.Lifetime;
    }

    public Vector2f getFrom() {
        return From;
    }

    public Vector2f getTo() {
        return To;
    }

    public Vector3f getColor() {
        return Color;
    }


}
