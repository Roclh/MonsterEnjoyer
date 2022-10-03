package com.GaysFromITMO.core.engine;

import com.GaysFromITMO.core.window.WindowManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class Engine {
    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;

    private static int fps;
    private static float frametime = 1.0f / FRAMERATE;

    private boolean isRunning;

    private WindowManager window;
    private GLFWErrorCallback errorCallback;

    private void init() throws Exception{
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = new WindowManager("Monster Enjoyer", 1600, 900, false);
        window.init();
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

            //input

            while(unprocessedTime > frametime){
                render = true;
                unprocessedTime -= frametime;

                if(window.shouldClose()){
                    stop();
                }

                if(frameCounter >= NANOSECOND){
                    setFps(frames);
                    window.setTitle("Monster Enjoyer "+ getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render){
                update();
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

    }

    private void render(){

    }

    private void update(){
        window.update();

    }

    private void cleanup(){
        window.cleanup();
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