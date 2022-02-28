/*
WIP
Reference: https://youtu.be/88oZT7Aum6s
 */

package Aurelian.listener;

import static org.lwjgl.glfw.GLFW.GLFW_CONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_LAST;

public class JoystickListener {
    public static JoystickListener instance;
    private final boolean[] joystickConnected = new boolean[GLFW_JOYSTICK_LAST];

    private JoystickListener() {

    }

    public static JoystickListener get(){
        if (JoystickListener.instance == null) JoystickListener.instance = new JoystickListener();

        return JoystickListener.instance;
    }

    public static void joystickCallback(int joystickID, int event){
        get().joystickConnected[joystickID] = event == GLFW_CONNECTED;
    }


}
