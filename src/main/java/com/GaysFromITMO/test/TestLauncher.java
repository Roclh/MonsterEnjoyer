package com.GaysFromITMO.test;

import com.GaysFromITMO.Main;
import com.GaysFromITMO.core.InterfaceLogic;
import com.GaysFromITMO.core.rendering.ObjectLoader;
import com.GaysFromITMO.core.rendering.RenderManager;
import com.GaysFromITMO.core.rendering.entity.Model;
import com.GaysFromITMO.core.window.WindowManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class TestLauncher implements InterfaceLogic {

    private int direction;
    private float colour = 0.0f;
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private Model model;

    public TestLauncher() {
        renderer = new RenderManager();
        loader = new ObjectLoader();
        window = Main.getWindow();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };

        int[] indices = {
                0,1,3,
                3,1,2
        };

        model = loader.loadModel(vertices, indices);
    }

    @Override
    public void input() {
        if (window.isKeyPressed(GLFW.GLFW_KEY_UP))
            direction = 1;
        else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN))
            direction = -1;
        else
            direction = 0;
    }

    @Override
    public void update() {
        colour += direction * 0.01f;
        if (colour >= 1) {
            colour = 1;
        } else if (colour <= 0) {
            colour = 0;
        }
    }

    @Override
    public void render() {
        if(window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        window.setClearColor(colour, colour, colour, 0.0f);
        renderer.render(model);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
