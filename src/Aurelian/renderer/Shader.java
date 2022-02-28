package Aurelian.renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class Shader {

    private int shaderProgramID;

    private final HashMap<String, Integer> shaders = new HashMap<>();
    private int[] ids;

    private final String filepath;

    public Shader(String filepath){
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            int index;
            int eol = 0;
            for (int i = 1; i < splitString.length; i++){
                index = source.indexOf("#type", eol) + 6;
                eol = source.indexOf(System.lineSeparator(), index);
                String shaderType = source.substring(index, eol).trim();

                switch (shaderType){
                    case "vertex":
                        shaders.put(splitString[i], GL_VERTEX_SHADER);
                        break;
                    case "fragment":
                        shaders.put(splitString[i], GL_FRAGMENT_SHADER);
                        break;
                    case "geometry":
                        // This is an example and has not been implemented yet
                        shaders.put(splitString[i], GL_GEOMETRY_SHADER);
                    default:
                        assert false : "Error: Unsupported shader type '" + shaderType + "' in '" + filepath + "'";

                }
            }
        } catch (IOException e){
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }
    }

    public void compile() { // If necessary you can split the two steps into separate methods

        // ===========================================
        // Compile and Link Shaders
        // ===========================================

        ids = new int[shaders.size()];
        int index = 0;
        for (Map.Entry<String, Integer> e : shaders.entrySet()) {
            // First load and compile the shader
            ids[index] = glCreateShader(e.getValue());

            // Pass the shader source to the CPU
            glShaderSource(ids[index], e.getKey());
            glCompileShader(ids[index]);

            // Check for errors in compilation
            int success = glGetShaderi(ids[index], GL_COMPILE_STATUS);
            if (success == GL_FALSE) {
                int len = glGetShaderi(ids[index], GL_INFO_LOG_LENGTH);
                switch (e.getValue()) {
                    case GL_VERTEX_SHADER -> System.out.println("Error: '" + filepath + "' \n\tVertex shader compilation failed");
                    case GL_FRAGMENT_SHADER -> System.out.println("Error: '" + filepath + "' \n\tFragment shader compilation failed");
                    case GL_GEOMETRY_SHADER -> System.out.println("Error: '" + filepath + "' \n\tGeometry shader compilation failed");
                    default -> System.out.println("Error: '" + filepath + "' \n\tUnknown shader compilation failed");
                }
                glGetShaderInfoLog(ids[index], len);
                assert false : "";
            }

            index++;
        }

        // link shaders and check for errors
        shaderProgramID = glCreateProgram();
        for (int i = 0; i < shaders.size(); i++) { glAttachShader(shaderProgramID, ids[i]); }
        glLinkProgram(shaderProgramID);

        // check for linking errors
        int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "' \n\tLinking of shaders failed" );
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use(){
        // Bind shader program
        glUseProgram(shaderProgramID);

    }

    public void detach(){
        // Unbind shader program
        glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer); 
    }
}