package com.GaysFromITMO;


import com.GaysFromITMO.core.engine.Engine;
import com.GaysFromITMO.core.rendering.RenderManager;
import com.GaysFromITMO.core.window.WindowManager;
import com.GaysFromITMO.test.TestLauncher;

public class Main {
    protected static WindowManager window;
    protected static RenderManager renderer;
    private static TestLauncher game;

    public static void main(String[] args){
        renderer = new RenderManager();
        window = new WindowManager("Monster Enjoyer", 1600, 900, false);
        game = new TestLauncher();
        try {
            new Engine().start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestLauncher getGame() {
        return game;
    }
}
