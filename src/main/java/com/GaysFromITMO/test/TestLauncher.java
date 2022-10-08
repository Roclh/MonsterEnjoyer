package com.GaysFromITMO.test;

import com.GaysFromITMO.core.InterfaceLogic;
import com.GaysFromITMO.core.rendering.camera.Camera;
import com.GaysFromITMO.core.rendering.ObjectLoader;
import com.GaysFromITMO.core.rendering.renderers.DefaultRenderManager;
import com.GaysFromITMO.core.rendering.entity.Entity;
import com.GaysFromITMO.core.rendering.entity.Model;
import com.GaysFromITMO.core.rendering.entity.Texture;
import com.GaysFromITMO.core.rendering.light.DirectionalLight;
import com.GaysFromITMO.core.rendering.light.PointLight;
import com.GaysFromITMO.core.rendering.light.SpotLight;
import com.GaysFromITMO.binder.SingletonClassProvider;
import com.GaysFromITMO.core.window.WindowManager;
import com.GaysFromITMO.core.window.input.MouseInput;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestLauncher implements InterfaceLogic {
    private static float CAMERA_MOVE_SPEED = 0.05f;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private final DefaultRenderManager renderer = (DefaultRenderManager)SingletonClassProvider.getStoredClass(DefaultRenderManager.class);
    private final ObjectLoader loader = (ObjectLoader)SingletonClassProvider.getStoredClass(ObjectLoader.class);
    private final WindowManager window = (WindowManager) SingletonClassProvider.getStoredClass(WindowManager.class);
    private List<Entity> entities;
    private Camera camera;
    private float lightAngle;
    private DirectionalLight directionalLight;
    private PointLight pointLight;
    private SpotLight spotLight;
    private boolean isLinked = false;

    Vector3f cameraInc;

    public TestLauncher() throws Exception {
        camera = new Camera();
        camera.setVerticalRotationLimits(-40f, 40f);
        cameraInc = new Vector3f(0, 0, 0);
        lightAngle = -90;
        init();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadOBJModel("/models/human.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/blue.png")), 1f);
        entities = new ArrayList<>();
        Random random = new Random();
        for(int i =0; i<1000; i++){
            float x = random.nextFloat()*10000 - 5000;
            float y = random.nextFloat()*10000 - 5000;
            float z = random.nextFloat()*-300;
            entities.add(new Entity(model, new Vector3f(x, y, z), new Vector3f(random.nextFloat()*180, random.nextFloat()*180, random.nextFloat()*180), 1f));
        }
        entities.add(new Entity(model, new Vector3f(0, 0, -10), new Vector3f(0, 0, 0), 1f));

        float lightIntensity = 0.5f;
        Vector3f lightPosition = new Vector3f(0,0,-3.2f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity, 0, 0, 1);

        Vector3f conedir = new Vector3f(0, 0, 1);
        float cutoff = (float) Math.cos(Math.toRadians(60));
        spotLight = new SpotLight(new PointLight(lightColour, new Vector3f(0, 0, 1),1f, 0, 0, 1),
                conedir, cutoff);

        lightPosition = new Vector3f(-100, -100, 0);
        lightColour = new Vector3f(0.5f, 0.5f, 0.5f);
        directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_Z))
            cameraInc.y = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_X))
            cameraInc.y = 1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_KP_ADD))
            CAMERA_MOVE_SPEED += 0.1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_KP_SUBTRACT))
            CAMERA_MOVE_SPEED -= 0.1;
        if (window.isKeyReleased(GLFW.GLFW_KEY_F)){
            System.out.println("Light switched");
            isLinked = !isLinked;
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED, cameraInc.y * CAMERA_MOVE_SPEED, cameraInc.z * CAMERA_MOVE_SPEED);
        if(mouseInput.isRightButtonPress()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x*MOUSE_SENSITIVITY, rotVec.y*MOUSE_SENSITIVITY, 0);
        }

        lightAngle += 0.5f;
        if(lightAngle > 90){
            directionalLight.setIntensity(0);
            if(lightAngle >= 360){
                lightAngle = -90;
            }
        } else if(lightAngle <= -80 || lightAngle >= 80){
            float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColour().y = Math.max(factor, 0.9f);
            directionalLight.getColour().z = Math.max(factor, 0.5f);
        }else {
            directionalLight.setIntensity(1);
            directionalLight.getColour().x = 1;
            directionalLight.getColour().y = 1;
            directionalLight.getColour().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
        if(isLinked){
            System.out.println(camera.getRotation().toString());
            spotLight.setConeDirection(camera.getRotation());
            spotLight.getPointLight().setPosition(camera.getPosition());
        }

        entities.forEach(renderer::processEntity);
    }

    @Override
    public void render() {
        renderer.passParams(camera, directionalLight, pointLight, spotLight);
        renderer.render();
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
