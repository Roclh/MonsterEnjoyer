package com.GaysFromITMO;


import com.GaysFromITMO.core.window.WindowManager;

public class Main {
    public static void main(String[] args){
        WindowManager window = new WindowManager("MonsterEnjoyer", 1600, 900, false);
        window.init();

        while (!window.shouldClose()){
            window.update();
        }

        window.cleanup();
    }

}
