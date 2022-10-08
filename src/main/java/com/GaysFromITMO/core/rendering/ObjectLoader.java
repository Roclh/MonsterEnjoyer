package com.GaysFromITMO.core.rendering;

import com.GaysFromITMO.core.rendering.entity.Model;
import com.GaysFromITMO.core.utils.MemoryUtils;
import com.GaysFromITMO.logger.Log;
import com.GaysFromITMO.logger.Loggable;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader implements Loggable<ObjectLoader>{
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();
    private boolean isLogged = false;

    public Model loadOBJModel(String filename){
        List<String> lines = MemoryUtils.readAllLines(filename);
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();

        for (String line : lines){
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v" -> {
                    //vertices
                    Vector3f verticesVec = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(verticesVec);
                }
                case "vt" -> {
                    //vertex textures
                    Vector2f textureVec = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    textures.add(textureVec);
                }
                case "vn" -> {
                    Vector3f normalsVec = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normals.add(normalsVec);
                }
                //vertex normals
                case "f" -> {
                    //faces
                    processFace(tokens[1], faces);
                    processFace(tokens[2], faces);
                    processFace(tokens[3], faces);
                }
                default -> {
                }
            }
        }
        List<Integer> indecies = new ArrayList<>();
        float[] verticesArr = new float[vertices.size()*3];
        int i =0;
        for(Vector3f pos : vertices){
            verticesArr[i*3] = pos.x;
            verticesArr[i*3+1] = pos.y;
            verticesArr[i*3+2] = pos.z;
            i++;
        }

        float[] texCoordArr = new float[vertices.size() *2];
        float[] normalArr = new float[vertices.size()*3];

        for(Vector3i face : faces){
            processVertex(face.x, face.y, face.z, textures, normals, indecies, texCoordArr, normalArr);
        }

        int[] indicesArr = indecies.stream().mapToInt((Integer v)-> v).toArray();

        Log.info(getClass().getName() + " loaded OBJ model with path " + filename, isLogged);
        return loadModel(verticesArr, texCoordArr, normalArr, indicesArr);
    }



    private static void processVertex(int pos, int texCoord, int normal, List<Vector2f> texCoordList,
                                      List<Vector3f> normalList, List<Integer> indicesList,
                                      float[] texCoordArr, float[] normalArr){
        indicesList.add(pos);

        if(texCoord >= 0){
            Vector2f texCoordVec = texCoordList.get(texCoord);
            texCoordArr[pos*2] = texCoordVec.x;
            texCoordArr[pos*2+1] = 1 - texCoordVec.y;
        }
        if(normal == 0){
            Vector3f normalVec = normalList.get(normal);
            normalArr[pos*3] = normalVec.x;
            normalArr[pos*3+1] = normalVec.y;
            normalArr[pos*3+2] = normalVec.z;
        }
    }

    private static void processFace(String token, List<Vector3i> faces){
        String[] lineToken = token.split("/");
        int length = lineToken.length;
        int pos = -1, coords = -1, normal = -1;
        pos = Integer.parseInt(lineToken[0])-1;
        if(length > 1){
            String textCoord = lineToken[1];
            coords = textCoord.length()>0? Integer.parseInt(textCoord) -1 : -1;
            if(length>2){
                normal = Integer.parseInt(lineToken[2]) - 1;
            }
        }
        Vector3i facesVec = new Vector3i(pos, coords, normal);
        faces.add(facesVec);
    }
    public Model loadModel(float[] vertices, float[] textureCoords, float[] normals, int[] indices){
        int id = createVAO();
        storeIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbind();
        return new Model(id, indices.length);
    }

    public int loadTexture(String filepath) throws Exception{
        int width, height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filepath, w, h, c, 4);
            if(buffer == null)
                throw new Exception("Image File "+ filepath + " not loaded " + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();
        }

        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        Log.info(getClass().getName() + " loaded texture with path " + filepath, isLogged);
        return id;
    }

    public int createVAO(){
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeDataInAttributeList(int attributeNumber, int vertexCount, float[] data){
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = MemoryUtils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, vertexCount, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void storeIndicesBuffer(int[] indices){
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = MemoryUtils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

    }

    private void unbind(){
        GL30.glBindVertexArray(0);
    }

    public void cleanup(){
        vaos.forEach(GL30::glDeleteVertexArrays);
        vbos.forEach(GL30::glDeleteBuffers);
        textures.forEach(GL11::glDeleteTextures);
    }

    @Override
    public boolean isLogged() {
        return isLogged;
    }
    @Override
    public ObjectLoader isLogged(boolean isLogged){
        this.isLogged = isLogged;
        return this;
    }
}
