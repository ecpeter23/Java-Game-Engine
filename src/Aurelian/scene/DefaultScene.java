package Aurelian.scene;

import Aurelian.listener.KeyListener;

import static org.lwjgl.glfw.GLFW.*;

public class DefaultScene extends Scene{
    public DefaultScene(){
        System.out.println("Loaded Default Scene");
    }

    @Override
    public void update(float dt){
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) System.out.println("" + (1.0f / dt) + "FPS");
    }
}
