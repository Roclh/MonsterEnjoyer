package com.GaysFromITMO.core.rendering.renderers;

import com.GaysFromITMO.binder.SingletonClassProvider;
import com.GaysFromITMO.core.rendering.camera.Camera;
import com.GaysFromITMO.core.rendering.entity.Entity;
import com.GaysFromITMO.core.rendering.entity.Model;
import com.GaysFromITMO.core.rendering.light.DirectionalLight;
import com.GaysFromITMO.core.rendering.light.PointLight;
import com.GaysFromITMO.core.rendering.light.SpotLight;
import com.GaysFromITMO.core.rendering.shaders.DefaultShaderManager;
import com.GaysFromITMO.core.window.WindowManager;
import com.GaysFromITMO.logger.Log;
import com.GaysFromITMO.logger.Loggable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class DefaultRenderManager extends RenderManager implements Loggable<DefaultRenderManager> {
    private final WindowManager window = (WindowManager) SingletonClassProvider.getStoredClass(WindowManager.class);
    private DefaultShaderManager shader;
    private Camera camera;
    private  DirectionalLight directionalLight;
    private PointLight pointLight;
    private SpotLight spotLight;
    private boolean isLogged;

    public DefaultRenderManager(){
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void init() throws Exception{
        shader = new DefaultShaderManager("/shaders/fragment.frag","/shaders/vertex.vert");
        Log.info(this.getClass().getName() + " was initiated with shaders \"/shaders/fragment.frag\" and \"/shaders/vertex.vert\"", isLogged);
    }

    public void passParams(Camera camera, DirectionalLight directionalLight, PointLight pointLight, SpotLight spotLight){
        this.camera = camera;
        this.directionalLight = directionalLight;
        this.pointLight = pointLight;
        this.spotLight = spotLight;
    }

    @Override
    public void render(){
        super.render();
        shader.bind();
        shader.setUniform("projectionMatrix", window.getProjectionMatrix());
        shader.renderLights(camera, directionalLight, pointLight, spotLight);
        entities.keySet().forEach(model -> {
            bind(model);
            List<Entity> entityList = entities.get(model);
            entityList.stream().filter(entity -> entity.shouldBeRendered(camera))
                    .forEach((entity)->{
                shader.prepare(entity, camera);
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

    public void bind(Model model){
        GL30.glBindVertexArray(model.getId());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);
        shader.setUniform("material", model.getMaterial());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
    }

    public DefaultShaderManager getShader(){
        return shader;
    }

    @Override
    public boolean isLogged() {
        return isLogged;
    }

    @Override
    public DefaultRenderManager isLogged(boolean isLogged) {
        this.isLogged = isLogged;
        return this;
    }
}
