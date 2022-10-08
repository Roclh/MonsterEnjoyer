package com.GaysFromITMO.core.window.input;

import com.GaysFromITMO.binder.SingletonClassProvider;
import com.GaysFromITMO.core.window.WindowManager;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseInput {
    private final Vector2d previousPos, currentPos;
    private final Vector2f displVec;
    private final WindowManager window = (WindowManager) SingletonClassProvider.getStoredClass(WindowManager.class);

    private boolean inWindow = false, leftButtonPress = false, rightButtonPress = false;

    public MouseInput() {
        this.previousPos = new Vector2d(-1, -1);
        this.currentPos = new Vector2d(0, 0);
        this.displVec = new Vector2f();
        init();
    }

    public void init(){
        GLFW.glfwSetCursorPosCallback(window.getWindow(), (window, xpos, ypos)->{
            currentPos.x = xpos;
            currentPos.y = ypos;
        });

        GLFW.glfwSetCursorEnterCallback(window.getWindow(), (window, entered)->{
            inWindow = entered;
        });

        GLFW.glfwSetMouseButtonCallback(window.getWindow(), (window, button, action, mods)->{
            leftButtonPress = button== GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        });
    }

    public void input(){
        displVec.x = 0;
        displVec.y = 0;
        if(previousPos.x > 0 && previousPos.y > 0 && inWindow){
            double x = currentPos.x - previousPos.x;
            double y = currentPos.y - previousPos.y;
            boolean rorateX = x!=0;
            boolean rotateY = y!=0;
            if(rorateX)
                displVec.y = (float) x;
            if(rotateY)
                displVec.x = (float) y;
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public boolean isLeftButtonPress() {
        return leftButtonPress;
    }

    public boolean isRightButtonPress() {
        return rightButtonPress;
    }
}
