package com.GaysFromITMO.core.utils;

import com.GaysFromITMO.core.rendering.camera.Camera;
import com.GaysFromITMO.core.rendering.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public class TransformationUtils {
    public static Matrix4f createTransformationMatrix(Entity entity){
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(entity.getPos()).
                rotateX((float) Math.toRadians(entity.getRotation().x)).
                rotateY((float) Math.toRadians(entity.getRotation().y)).
                rotateZ((float) Math.toRadians(entity.getRotation().z)).
                scale(entity.getScale());
        return matrix;
    }

    public static Matrix4f getViewMatrix(Camera camera){
        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix.identity().rotate((float) Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(rot.z), new Vector3f(0, 0, 1));
        matrix.translate(-pos.x, -pos.y, -pos.z);
        return matrix;
    }

    public static Vector3f multiplyMatrix(Vector3f f, Vector3f s){
        return new Vector3f(
                f.y*s.z-f.z*s.y,
                f.z*s.x-f.x*s.z,
                f.x*s.y + f.y*s.x
        );
    }

    public static float[] convertToFloatArray(List<Float> floatList){
        float[] floats = new float[floatList.size()];
        int i =0;
        for (Float f: floatList) {
            floats[i] = f;
            i++;
        }
        return floats;
    }


}
