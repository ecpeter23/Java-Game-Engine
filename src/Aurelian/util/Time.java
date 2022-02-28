package Aurelian.util;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time {
    public static double timeStarted = glfwGetTime();

    public static double getTime() { return (glfwGetTime() - timeStarted) * 1E-9; }

    public static float getTimeNano() { return (float)(glfwGetTime() - timeStarted); }
}
