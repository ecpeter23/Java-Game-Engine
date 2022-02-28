package Aurelian;

import Aurelian.listener.*;
import Aurelian.scene.DefaultScene;
import Aurelian.scene.LevelEditorScene;
import Aurelian.scene.Scene;
import Aurelian.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;

    private static Window window = null;

    private static Scene currentScene = null;

    private Window(){
        this.width = 1440;
        this.height = 900;
        this.title = "Aurelian";
    }

    public static void changeScene(Scene scene){
        Window.currentScene = scene;
        Window.currentScene.init();
    }

    public static void changeScene(int sceneIndex){
        // Add scene manually

        switch (sceneIndex){
            case 0:
                Window.currentScene = new DefaultScene();
                Window.currentScene.init();
                break;
            case 1:
                Window.currentScene = new LevelEditorScene();
                Window.currentScene.init();
                break;
            default:
                assert false : "Unknown scene '" + sceneIndex + "'";
        }


    }

    public static Window get(){
        if (Window.window == null) Window.window = new Window();

        return Window.window;
    }

    public void run(){
        System.out.println("LWJGL Version: " + Version.getVersion());

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init(){
        // Setup an error callback (where the errors will be printed)
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);

        // Create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) throw new IllegalStateException("Failed to create the GLFW window");

        // Setup callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Window.changeScene(1); //Window.changeScene(0);
    }

    private void loop(){
        float begainTime = Time.getTimeNano();
        float endTime = Time.getTimeNano();
        float dt = -1.0f;


        while (!glfwWindowShouldClose(glfwWindow)){
            // Poll events (Mouse and key events)
            glfwPollEvents();

            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) currentScene.update(dt);

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTimeNano();
            dt = endTime - begainTime;
            begainTime = endTime;
        }

    }
}
