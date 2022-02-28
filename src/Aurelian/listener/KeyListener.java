package Aurelian.listener;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class KeyListener {
    private static KeyListener instance;
    private final boolean[] keyPressed = new boolean[GLFW_KEY_LAST];

    private KeyListener(){

    }

    public static KeyListener get(){
        if (KeyListener.instance == null) KeyListener.instance = new KeyListener();

        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        get().keyPressed[key] = action == GLFW_PRESS;
    }

    public static boolean isKeyPressed(int keyCode){
        return get().keyPressed[keyCode];
    }
}
