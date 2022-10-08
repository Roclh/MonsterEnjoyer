package com.GaysFromITMO.binder;


import com.GaysFromITMO.logger.Log;

import java.util.HashMap;
import java.util.Map;

public class SingletonClassProvider {
    public static Map<Class, Object> classes = new HashMap<>();

    public static void register(Object object){
        classes.put(object.getClass(), object);
        Log.info("Object " + object.getClass().getName() + " was registered as singleton");
    }

    public static Object getStoredClass(Class clas){
        return classes.get(clas);
    }

    public static void clear(Object object){
        classes.remove(object.getClass());
    }

    public static void clear(){
        classes.clear();
    }
}
