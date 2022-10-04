package com.GaysFromITMO.core.rendering;

import com.GaysFromITMO.Main;
import com.GaysFromITMO.core.rendering.entity.Entity;
import com.GaysFromITMO.core.rendering.entity.Model;
import com.GaysFromITMO.core.rendering.light.DirectionalLight;
import com.GaysFromITMO.core.rendering.light.PointLight;
import com.GaysFromITMO.core.utils.MemoryUtils;
import com.GaysFromITMO.core.utils.TransformationUtils;
import com.GaysFromITMO.core.window.WindowManager;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderManager {

    public static final Vector3f AMBIENT_LIGHT = new Vector3f(1.3f,1.3f,1.3f);
    public static final float SPECULAR_POWER = 1.0f;
    private Map<Model, List<Entity>> entities = new HashMap<>();
    private final WindowManager window;
    private ShaderManager shader;

    public RenderManager() {
        this.window = Main.getWindow();
    }

    public void init() throws Exception{
        shader = new ShaderManager();
        shader.createVertexShader(MemoryUtils.loadResource("/shaders/vertex.vs"));
        shader.createFragmentShader(MemoryUtils.loadResource("/shaders/fragment.fs"));
        shader.link();
        shader.createUniform("textureSampler");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
        shader.createUniform("ambientLight");
        shader.createMaterialUniform("material");
        shader.createUniform("specularPower");
        shader.createDirectionalLightUniform("directionalLight");
        shader.createPointLightUniform("pointLight");
    }

    public void bind(Model model){
        GL30.glBindVertexArray(model.getId());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);
        shader.setUniform("material", model.getMaterial());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
    }

    public void unbind(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void prepare(Entity entity, Camera camera){
        shader.setUniform("textureSampler", 0);
        shader.setUniform("transformationMatrix", TransformationUtils.createTransformationMatrix(entity));
        shader.setUniform("viewMatrix", TransformationUtils.getViewMatrix(camera));
    }

    public void renderLights(Camera camera, DirectionalLight directionalLight, PointLight pointLight){
        shader.setUniform("ambientLight",AMBIENT_LIGHT);
        shader.setUniform("specularPower", SPECULAR_POWER);
        shader.setUniform("directionalLight", directionalLight);
        shader.setUniform("pointLight", pointLight);
    }

    public void render(Camera camera, DirectionalLight directionalLight, PointLight pointLight){
        clear();
        if (window.isResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        shader.bind();
        shader.setUniform("projectionMatrix", window.getProjectionMatrix());
        renderLights(camera, directionalLight, pointLight);
        entities.keySet().forEach(model -> {
            bind(model);
            List<Entity> entityList = entities.get(model);
            entityList.forEach((entity)->{
                prepare(entity, camera);
                GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            });
            unbind();
        });
        entities.clear();

        shader.unbind();
    }

    public void processEntity(Entity entity){
        List<Entity> entityList = entities.get(entity.getModel());
        if(entityList != null){
            entityList.add(entity);
        }else{
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entities.put(entity.getModel(), newEntityList);
        }
    }

    public void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        shader.cleanup();
    }

}
