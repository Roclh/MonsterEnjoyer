package com.GaysFromITMO.core;

import com.GaysFromITMO.core.figures.FigureVertexArrayObject;
import static org.lwjgl.opengl.GL46C.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Renderer {
    public static void prepare(){
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(0.5f, 0.75f, 1.0f, 1);
    }

    public static void renderVertexArray(FigureVertexArrayObject arrayObject){
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = storeDataInDoubleBuffer(arrayObject.getVertexArrayObject());
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0,3, GL_FLOAT, false, 0, 0);
        MemoryUtil.memFree(buffer);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLES, 0, arrayObject.count());
        glDisableVertexAttribArray(0);
        glBindVertexArray(vaoId);
    }

    private static FloatBuffer storeDataInDoubleBuffer(float[] data){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
