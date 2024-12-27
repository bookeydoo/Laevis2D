package Laevis;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f ProjectionMatrix, ViewMatrix,InverseProjection,InverseView;
    public Vector2f Position;

    public Camera(Vector2f Position) {
        this.Position = Position;
        this.ProjectionMatrix = new Matrix4f();
        this.ViewMatrix = new Matrix4f();
        this.InverseProjection=new Matrix4f();
        this.InverseView=new Matrix4f();
        AdjustProjection();
    }

    public void AdjustProjection() {
        ProjectionMatrix.identity();
        ProjectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
        ProjectionMatrix.invert(InverseProjection);

    }

    public Matrix4f GetViewMatrix() {
        Vector3f CameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f CameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f CameraLookAt = new Vector3f(Position.x, Position.y, 20.0f);
        this.ViewMatrix.identity();
        this.ViewMatrix = ViewMatrix.lookAt(CameraLookAt, CameraFront.add(Position.x, Position.y, 0.0f), CameraUp);

        this.ViewMatrix.invert(InverseView);
        return this.ViewMatrix;
    }

    public Matrix4f GetProjectionMatrix() {
        return this.ProjectionMatrix;
    }

    public Matrix4f GetInverseProjection(){
        return this.InverseProjection;

    }

    public Matrix4f GetInverseView(){
        return this.InverseView;
    }
}
