package Laevis;

import org.joml.Vector4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class MouseListener {
    private static MouseListener MouseInstance;
    private double ScrollX, ScrollY;
    private double XPosition, YPosition, LastX, LastY;
    private boolean MouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        this.ScrollX = 0.0;
        this.ScrollY = 0.0;
        this.XPosition = 0.0;
        this.YPosition = 0.0;
        this.LastX = 0.0;
        this.LastY = 0.0;
    }

    public static MouseListener Get() {
        if (MouseListener.MouseInstance == null) {
            MouseListener.MouseInstance = new MouseListener();
        }
        return MouseListener.MouseInstance;
    }

    public static void MousePositionCallback(long window, double XPosition, double YPosition) {
        Get().LastX = Get().XPosition;
        Get().LastY = Get().YPosition;
        Get().XPosition = XPosition;
        Get().YPosition = YPosition;
        Get().isDragging = Get().MouseButtonPressed[0] || Get().MouseButtonPressed[1] || Get().MouseButtonPressed[2];
    }

    public static void MouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < Get().MouseButtonPressed.length) {
                Get().MouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < Get().MouseButtonPressed.length) {
                Get().MouseButtonPressed[button] = false;
                Get().isDragging = false;
            }
        }
    }

    public static void MouseScrollCallback(long window, double XOffset, double YOffset) {
        Get().ScrollX = XOffset;
        Get().ScrollY = YOffset;
    }

    public static void EndFrame() {
        Get().ScrollX = 0;
        Get().ScrollY = 0;
        Get().LastX = Get().XPosition;
        Get().LastY = Get().YPosition;
    }

    public static float GetX() {
        return (float)Get().XPosition;
    }

    public static float GetY() {
        return (float)Get().YPosition;
    }

    public static float getOrthoX(){
    float currentX=GetX();
    currentX=(currentX/(float)Window.getWidth())*2.0f - 1.0f;

    Vector4f tmp=new Vector4f(0,0,0,1);
    tmp.mul(Window.getScene().Camera().GetInverseProjection().mul(Window.getScene().Camera().GetInverseView()));
    currentX=tmp.x;
    return currentX;
    }

    public static float getOrthoY(){
    float currentY=Window.getHeight()-GetY();
    currentY=(currentY/(float)Window.getHeight())*2.0f -1.0f;
    Vector4f tmp=new Vector4f(0,0,0,1);
    tmp.mul(Window.getScene().Camera().GetInverseProjection().mul(Window.getScene().Camera().GetInverseView()));
    currentY=tmp.y;
    return -1;
    }

    public static float GetDx() {
        return (float)(Get().LastX - Get().XPosition);
    }

    public static float GetDy() {
        return (float)(Get().LastY - Get().YPosition);
    }

    public static float GetScrollX() {
        return (float)Get().ScrollX;
    }

    public static float GetScrollY() {
        return (float)Get().ScrollY;
    }

    public static boolean isDragging() {
        return Get().isDragging;
    }

    public static boolean MouseButtonDown(int button) {
        if (button < Get().MouseButtonPressed.length) {
            return Get().MouseButtonPressed[button];
        } else {
            return false;
        }
    }
}