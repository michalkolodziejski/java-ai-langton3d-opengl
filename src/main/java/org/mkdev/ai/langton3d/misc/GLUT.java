package org.mkdev.ai.langton3d.misc;

import org.lwjgl.opengl.GL11;
import org.mkdev.ai.langton3d.core.CubeFaces;

/**
 * @author Michał Kołodziejski &lt;<I><A href="mailto:michal.kolodziejski@gmail.com">michal.kolodziejski@gmail.com</A></I>&gt;
 * @version 1.0
 * @license: GPLv3 (http://www.gnu.org/licenses/gpl-3.0.txt)
 * @since: 2014-05-11
 */
public final class GLUT {

    private static final float CUBE_NORMALS[/*6*/][/*3*/] = {
        {-1.0f, 0.0f, 0.0f},
        {0.0f, 1.0f, 0.0f},
        {1.0f, 0.0f, 0.0f},
        {0.0f, -1.0f, 0.0f},
        {0.0f, 0.0f, 1.0f},
        {0.0f, 0.0f, -1.0f}
    };

    private static final int CUBE_FACES[/*6*/][/*4*/] = {
        {0, 1, 2, 3},
        {3, 2, 6, 7},
        {7, 6, 5, 4},
        {4, 5, 1, 0},
        {5, 6, 2, 1},
        {7, 4, 0, 3}
    };

    private GLUT() {
        throw new UnsupportedOperationException("Inaccessible constructor");
    }

    public static void glutSolidCube(double size) {
        drawBox(size, GL11.GL_QUADS);
    }

    public static void drawBox(double size, int type) {

        float v[][] = new float[8][3];
        float sz = (float) size;
        int i;

        v[0][0] = v[1][0] = v[2][0] = v[3][0] = -sz / 2.0f;
        v[4][0] = v[5][0] = v[6][0] = v[7][0] = sz / 2.0f;
        v[0][1] = v[1][1] = v[4][1] = v[5][1] = -sz / 2.0f;
        v[2][1] = v[3][1] = v[6][1] = v[7][1] = sz / 2.0f;
        v[0][2] = v[3][2] = v[4][2] = v[7][2] = -sz / 2.0f;
        v[1][2] = v[2][2] = v[5][2] = v[6][2] = sz / 2.0f;

        for (i = 5; i >= 0; i--) {
            GL11.glBegin(type);

            GL11.glNormal3f(CUBE_NORMALS[i][0], CUBE_NORMALS[i][1], CUBE_NORMALS[i][2]);
            GL11.glVertex3f(v[CUBE_FACES[i][0]][0], v[CUBE_FACES[i][0]][1], v[CUBE_FACES[i][0]][2]);
            GL11.glVertex3f(v[CUBE_FACES[i][1]][0], v[CUBE_FACES[i][1]][1], v[CUBE_FACES[i][1]][2]);
            GL11.glVertex3f(v[CUBE_FACES[i][2]][0], v[CUBE_FACES[i][2]][1], v[CUBE_FACES[i][2]][2]);
            GL11.glVertex3f(v[CUBE_FACES[i][3]][0], v[CUBE_FACES[i][3]][1], v[CUBE_FACES[i][3]][2]);

            GL11.glEnd();
        }
    }

    public static void glutWireCube(double size) {

        drawBox(size, GL11.GL_LINE_LOOP);
    }

    public static void drawWholeCube(float d, float x1, float x2, float x3, int i, int j, int k) {
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glVertex3f(-d + x1, -d + x2, d + x3); // v1
        GL11.glVertex3f(d + x1, -d + x2, d + x3); // v2
        GL11.glVertex3f(d + x1, d + x2, d + x3); // v3
        GL11.glVertex3f(-d + x1, d + x2, d + x3); // v4

        GL11.glNormal3f(1.0f, 0.0f, 0.0f);
        GL11.glVertex3f(d + x1, -d + x2, d + x3); // v2
        GL11.glVertex3f(d + x1, -d + x2, -d + x3); // v6
        GL11.glVertex3f(d + x1, d + x2, -d + x3); // v5
        GL11.glVertex3f(d + x1, d + x2, d + x3); // v3

        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glVertex3f(d + x1, d + x2, d + x3); // v3
        GL11.glVertex3f(d + x1, d + x2, -d + x3); // v5
        GL11.glVertex3f(-d + x1, d + x2, -d + x3); // v7
        GL11.glVertex3f(-d + x1, d + x2, d + x3); // v4

        GL11.glNormal3f(0.0f, -1.0f, 0.0f);
        GL11.glVertex3f(-d + x1, -d + x2, d + x3); // v1
        GL11.glVertex3f(-d + x1, -d + x2, -d + x3); // v8
        GL11.glVertex3f(d + x1, -d + x2, -d + x3); // v6
        GL11.glVertex3f(d + x1, -d + x2, d + x3); // v2

        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        GL11.glVertex3f(-d + x1, -d + x2, -d + x3); // v8
        GL11.glVertex3f(-d + x1, d + x2, -d + x3); // v7
        GL11.glVertex3f(d + x1, d + x2, -d + x3); // v5
        GL11.glVertex3f(d + x1, -d + x2, -d + x3); // v6

        GL11.glNormal3f(-1.0f, 0.0f, 0.0f);
        GL11.glVertex3f(-d + x1, -d + x2, -d + x3); // v8
        GL11.glVertex3f(-d + x1, -d + x2, d + x3); // v1
        GL11.glVertex3f(-d + x1, d + x2, d + x3); // v4
        GL11.glVertex3f(-d + x1, d + x2, -d + x3); // v7
    }

    public static void drawCube(float x11, float x21, float x31, float d1, int mapCubeNeighbours) {
        if ((mapCubeNeighbours & CubeFaces.FRONT.getValue()) == 0) {
            GL11.glNormal3f(0.0f, 0.0f, 1.0f);
            GL11.glVertex3f(-d1 + x11, -d1 + x21, d1 + x31); // v1
            GL11.glVertex3f(d1 + x11, -d1 + x21, d1 + x31); // v2
            GL11.glVertex3f(d1 + x11, d1 + x21, d1 + x31); // v3
            GL11.glVertex3f(-d1 + x11, d1 + x21, d1 + x31); // v4
        }
        if ((mapCubeNeighbours & CubeFaces.RIGHT.getValue()) == 0) {
            GL11.glNormal3f(1.0f, 0.0f, 0.0f);
            GL11.glVertex3f(d1 + x11, -d1 + x21, d1 + x31); // v2
            GL11.glVertex3f(d1 + x11, -d1 + x21, -d1 + x31); // v6
            GL11.glVertex3f(d1 + x11, d1 + x21, -d1 + x31); // v5
            GL11.glVertex3f(d1 + x11, d1 + x21, d1 + x31); // v3
        }
        if ((mapCubeNeighbours & CubeFaces.TOP.getValue()) == 0) {
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GL11.glVertex3f(d1 + x11, d1 + x21, d1 + x31); // v3
            GL11.glVertex3f(d1 + x11, d1 + x21, -d1 + x31); // v5
            GL11.glVertex3f(-d1 + x11, d1 + x21, -d1 + x31); // v7
            GL11.glVertex3f(-d1 + x11, d1 + x21, d1 + x31); // v4
        }
        if ((mapCubeNeighbours & CubeFaces.BOTTOM.getValue()) == 0) {
            GL11.glNormal3f(0.0f, -1.0f, 0.0f);
            GL11.glVertex3f(-d1 + x11, -d1 + x21, d1 + x31); // v1
            GL11.glVertex3f(-d1 + x11, -d1 + x21, -d1 + x31); // v8
            GL11.glVertex3f(d1 + x11, -d1 + x21, -d1 + x31); // v6
            GL11.glVertex3f(d1 + x11, -d1 + x21, d1 + x31); // v2
        }
        if ((mapCubeNeighbours & CubeFaces.BACK.getValue()) == 0) {
            GL11.glNormal3f(0.0f, 0.0f, -1.0f);
            GL11.glVertex3f(-d1 + x11, -d1 + x21, -d1 + x31); // v8
            GL11.glVertex3f(-d1 + x11, d1 + x21, -d1 + x31); // v7
            GL11.glVertex3f(d1 + x11, d1 + x21, -d1 + x31); // v5
            GL11.glVertex3f(d1 + x11, -d1 + x21, -d1 + x31); // v6
        }
        if ((mapCubeNeighbours & CubeFaces.LEFT.getValue()) == 0) {
            GL11.glNormal3f(-1.0f, 0.0f, 0.0f);
            GL11.glVertex3f(-d1 + x11, -d1 + x21, -d1 + x31); // v8
            GL11.glVertex3f(-d1 + x11, -d1 + x21, d1 + x31); // v1
            GL11.glVertex3f(-d1 + x11, d1 + x21, d1 + x31); // v4
            GL11.glVertex3f(-d1 + x11, d1 + x21, -d1 + x31); // v7
        }
    }
}