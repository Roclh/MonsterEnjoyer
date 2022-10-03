package com.GaysFromITMO;


import com.GaysFromITMO.core.engine.Engine;
import com.GaysFromITMO.core.window.WindowManager;

public class Main {
    public static void main(String[] args){
        try {
            new Engine().start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
