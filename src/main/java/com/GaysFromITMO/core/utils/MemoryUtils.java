package com.GaysFromITMO.core.utils;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class MemoryUtils {
    public static FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }
}
