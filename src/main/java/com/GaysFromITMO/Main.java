package com.GaysFromITMO;


import com.GaysFromITMO.core.engine.Engine;
import com.GaysFromITMO.test.TestBinder;

public class Main {

    public static void main(String[] args){
        try {
            new TestBinder().bind();
            new Engine().start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
