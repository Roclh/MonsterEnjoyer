package com.GaysFromITMO.core.rendering.shaders;

import com.GaysFromITMO.core.rendering.camera.Camera;
import com.GaysFromITMO.core.rendering.entity.Entity;
import com.GaysFromITMO.core.rendering.light.DirectionalLight;
import com.GaysFromITMO.core.rendering.light.PointLight;
import com.GaysFromITMO.core.rendering.light.SpotLight;
import com.GaysFromITMO.core.utils.MemoryUtils;
import com.GaysFromITMO.core.utils.TransformationUtils;
import org.joml.Vector3f;

public class DefaultShaderManager extends ShaderManager {
    public static final Vector3f AMBIENT_LIGHT = new Vector3f(0.7f,0.7f,0.7f);
    public static final float SPECULAR_POWER = 1.0f;

    public DefaultShaderManager() throws Exception{
        super();
    }

    public DefaultShaderManager(String fragShaderPath, String vertShaderPath) throws Exception{
        super();
        createFragmentShader(MemoryUtils.loadResource(fragShaderPath));
        createVertexShader(MemoryUtils.loadResource(vertShaderPath));
        link();
        createUniform("textureSampler");
        createUniform("transformationMatrix");
        createUniform("projectionMatrix");
        createUniform("viewMatrix");
        createUniform("ambientLight");
        createMaterialUniform("material");
        createUniform("specularPower");
        createDirectionalLightUniform("directionalLight");
        createPointLightUniform("pointLight");
        createSpotLightUniform("spotLight");

    }

    public void createMaterialUniform(String uniformName) throws Exception{
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }



    public void createDirectionalLightUniform(String uniformName) throws Exception{
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");

    }

    public void createPointLightUniform(String uniformName) throws Exception{
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".constant");
        createUniform(uniformName + ".linear");
        createUniform(uniformName + ".exponent");
    }

    public void createSpotLightUniform(String uniformName) throws Exception{
        createPointLightUniform(uniformName + ".pl");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }

    public void setUniform(String uniformName, DirectionalLight light){
        setUniform(uniformName + ".colour", light.getColour());
        setUniform(uniformName + ".direction", light.getDirection());
        setUniform(uniformName + ".intensity", light.getIntensity());
    }
    public void setUniform(String uniformName, PointLight pointLight){
        setUniform(uniformName + ".colour", pointLight.getColour());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntencity());
        setUniform(uniformName + ".constant", pointLight.getConstant());
        setUniform(uniformName + ".linear", pointLight.getLinear());
        setUniform(uniformName + ".exponent", pointLight.getExponent());
    }

    public void setUniform(String uniformName, SpotLight spotLight){
        setUniform(uniformName + ".pl", spotLight.getPointLight());
        setUniform(uniformName+".conedir", spotLight.getConeDirection());
        setUniform(uniformName+".cutoff", spotLight.getCutoff());
    }

    public void prepare(Entity entity, Camera camera){
        setUniform("textureSampler", 0);
        setUniform("transformationMatrix", TransformationUtils.createTransformationMatrix(entity));
        setUniform("viewMatrix", TransformationUtils.getViewMatrix(camera));
    }


    public void renderLights(Camera camera, DirectionalLight directionalLight, PointLight pointLight, SpotLight spotLight){
        setUniform("ambientLight",AMBIENT_LIGHT);
        setUniform("specularPower", SPECULAR_POWER);
        setUniform("directionalLight", directionalLight);
        setUniform("pointLight", pointLight);
        setUniform("spotLight", spotLight);
    }
}
