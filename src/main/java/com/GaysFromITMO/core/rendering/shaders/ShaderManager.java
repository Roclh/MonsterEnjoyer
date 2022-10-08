package com.GaysFromITMO.core.rendering.shaders;

import com.GaysFromITMO.core.rendering.entity.Material;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

public abstract class ShaderManager {
    private final int programId;
    private int vertexShaderId, fragmentShaderId;
    private final Map<String, Integer> uniforms;

    protected ShaderManager() throws Exception{
        programId = GL20.glCreateProgram();
        if(programId == 0)
            throw new Exception("Could not create shader");
        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName) throws Exception{
        int uniformLocation = GL20.glGetUniformLocation(programId, uniformName);
        if(uniformLocation<0){
            throw new Exception("Could not find uniform "+ uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }
    public void setUniform(String uniformName, Material material){
        setUniform(uniformName + ".ambient", material.getAmbientColour());
        setUniform(uniformName + ".diffuse", material.getDiffuseColour());
        setUniform(uniformName + ".specular", material.getSpecularColour());
        setUniform(uniformName + ".hasTexture", material.hasTexture());
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }

    public void setUniform(String uniform, Vector4f value) {
        GL20.glUniform4f(uniforms.get(uniform), value.x, value.y, value.z, value.w);
    }
    public void setUniform(String uniform, Vector3f value) {
        GL20.glUniform3f(uniforms.get(uniform), value.x, value.y, value.z);
    }

    public void setUniform(String uniform, boolean value){
        GL20.glUniform1f(uniforms.get(uniform), value?1:0);
    }

    public void setUniform(String uniform, Matrix4f value){
        try(MemoryStack stack = MemoryStack.stackPush()){
            GL20.glUniformMatrix4fv(uniforms.get(uniform), false,
                    value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniform, int value){
        GL20.glUniform1i(uniforms.get(uniform), value);
    }

    public void setUniform(String uniform, float value){
        GL20.glUniform1f(uniforms.get(uniform), value);
    }

    public void createVertexShader(String shaderCode) throws Exception{
        vertexShaderId = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception{
        vertexShaderId = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int shaderType) throws Exception{
        int shaderID = GL20.glCreateShader(shaderType);
        if(shaderID == 0){
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0)
            throw new Exception("Error compiling shader code: TYPE: "+ shaderType
                    + " Info: "+GL20.glGetShaderInfoLog(shaderID, 1024));

        GL20.glAttachShader(programId, shaderID);

        return shaderID;
    }

    public void link() throws Exception{
        GL20.glLinkProgram(programId);
        if(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) ==0)
            throw new Exception("Error linking shader code "
                    + " info " + GL20.glGetProgramInfoLog(programId, 1024));
        if(vertexShaderId != 0){
            GL20.glDetachShader(programId, vertexShaderId);
        }
        if(fragmentShaderId != 0){
            GL20.glDetachShader(programId, fragmentShaderId);
        }
        GL20.glValidateProgram(programId);
        if(GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == 0){
            throw new Exception("Unable to validate shader code: "+GL20.glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind(){
        GL20.glUseProgram(programId);
    }

    public void unbind(){
        GL20.glUseProgram(0);
    }

    public void cleanup(){
        unbind();
        if(programId!=0){
            GL20.glDeleteProgram(programId);
        }
    }
}
