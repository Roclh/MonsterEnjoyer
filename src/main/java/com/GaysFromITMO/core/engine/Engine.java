package com.GaysFromITMO.core.engine;

import com.GaysFromITMO.core.InterfaceLogic;
import com.GaysFromITMO.binder.SingletonClassProvider;
import com.GaysFromITMO.core.window.WindowManager;
import com.GaysFromITMO.core.window.input.MouseInput;
import com.GaysFromITMO.test.TestLauncher;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class Engine{
    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;

    private static int fps;
    private static float frametime = 1.0f / FRAMERATE;

    private boolean isRunning;

    private WindowManager window = (WindowManager) SingletonClassProvider.getStoredClass(WindowManager.class);
    private MouseInput mouseInput = (MouseInput) SingletonClassProvider.getStoredClass(MouseInput.class);
    private GLFWErrorCallback errorCallback;
    private final InterfaceLogic gameLogic = (TestLauncher)SingletonClassProvider.getStoredClass(TestLauncher.class);

    private void init() throws Exception{
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
    }

    public void start() throws Exception{
        init();
        if(isRunning)
            return;
        run();
    }

    public void run(){
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while(isRunning){
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            input();

            while(unprocessedTime > frametime){
                render = true;
                unprocessedTime -= frametime;

                if(window.shouldClose()){
                    stop();
                }

                if(frameCounter >= NANOSECOND){
                    setFps(frames);
                    window.setTitle("Monster Enjoyer "+ getFps());
                    window.releaseButtons();
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render){
                update(frametime);
                render();
                frames++;
            }
        }
    }

    public void stop(){
        if(!isRunning) return;
        isRunning = false;

    }

    private void input(){

        mouseInput.input();
        gameLogic.input();
    }

    private void render(){
        gameLogic.render();
        window.update();
    }

    private void update(float interval){
        gameLogic.update(interval, mouseInput);
    }

    private void cleanup(){
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        Engine.fps = fps;
    }
}
