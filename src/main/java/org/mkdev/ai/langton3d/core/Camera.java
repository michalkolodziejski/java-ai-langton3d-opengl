package org.mkdev.ai.langton3d.core;

import org.lwjgl.opengl.GL11;

/**
 * @author Michał Kołodziejski &lt;<I><A href="mailto:michal.kolodziejski@gmail.com">michal.kolodziejski@gmail.com</A></I>&gt;
 * @version 1.0
 * @license: GPLv3 (http://www.gnu.org/licenses/gpl-3.0.txt)
 * @since: 2014-05-11
 */
public class Camera {

    private Camera() {throw new UnsupportedOperationException("Inaccessible constructor");}

    private static float movementSpeed = 0.1f;

    private static float cameraz = 175.0f;
    private static float view_rotx = 29f, view_roty = -126.5f, view_rotz = 0.0f;

    public static void zoomIn(float dt) {
        Camera.cameraz -= (movementSpeed * dt);
    }

    public static void zoomOut(float dt) {
        Camera.cameraz += (movementSpeed * dt);
    }

    public static void rotateUp(float dt) {
        Camera.view_rotx -= (movementSpeed * dt);
    }

    public static void rotateDown(float dt) {
        Camera.view_rotx += (movementSpeed * dt);
    }

    public static void rotateRight(float dt) {
        Camera.view_roty -= (movementSpeed * dt);
    }

    public static void rotateLeft(float dt) {
        Camera.view_roty += (movementSpeed * dt);
    }

    public static float getCameraz() {
        return cameraz;
    }

    public static void setCameraz(float cameraz) {
        Camera.cameraz = cameraz;
    }

    public static float getView_rotx() {
        return view_rotx;
    }

    public static void setView_rotx(float view_rotx) {
        Camera.view_rotx = view_rotx;
    }

    public static float getView_roty() {
        return view_roty;
    }

    public static void setView_roty(float view_roty) {
        Camera.view_roty = view_roty;
    }

    public static float getView_rotz() {
        return view_rotz;
    }

    public static void setView_rotz(float view_rotz) {
        Camera.view_rotz = view_rotz;
    }

    public static void applyRotation() {
        GL11.glTranslatef(0.0f, 0.5f, -1.0f - (Camera.getCameraz() / 32));
        GL11.glRotatef(Camera.getView_rotx(), 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(Camera.getView_roty(), 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(Camera.getView_rotz(), 0.0f, 0.0f, 1.0f);
    }
}
