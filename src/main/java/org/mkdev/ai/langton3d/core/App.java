package org.mkdev.ai.langton3d.core;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.mkdev.ai.langton3d.misc.GLUT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_AMBIENT_AND_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FOG_MODE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glMaterial;
import static org.lwjgl.opengl.GL11.glMaterialf;
import static org.lwjgl.opengl.GL11.glShadeModel;

/**
 * @author Michał Kołodziejski &lt;<I><A href="mailto:michal.kolodziejski@gmail.com">michal.kolodziejski@gmail.com</A></I>&gt;
 * @version 1.0
 * @license: GPLv3 (http://www.gnu.org/licenses/gpl-3.0.txt)
 * @since: 2014-05-11
 */
public class App {

    private final Logger logger = LoggerFactory.getLogger("org.mkdev.ai.langton3d");

    private static final AntCodeKeys[] CODE = {AntCodeKeys.RIGHT, AntCodeKeys.RIGHT, AntCodeKeys.LEFT, AntCodeKeys.DOWN, AntCodeKeys.DOWN, AntCodeKeys.DOWN, AntCodeKeys.UP, AntCodeKeys.LEFT, AntCodeKeys.RIGHT, AntCodeKeys.RIGHT, AntCodeKeys.LEFT, AntCodeKeys.LEFT, AntCodeKeys.LEFT, AntCodeKeys.LEFT};

    private static double[][] colors = createColorsTable();

    private static double[][] createColorsTable() {

        double[][] localColors = new double[][]{{0.5d, 0.5d, 0.5d}, {1d, 0d, 0d}, {0d, 0d, 1d}, {0d, 1d, 0d}};

        if (localColors.length < CODE.length) {
            double[][] colorsNu = new double[CODE.length][];
            for (int i = 0; i < colorsNu.length; i++) {
                colorsNu[i] = localColors[CODE[i].getValue()];
            }
            localColors = colorsNu;
        }

        return localColors;
    }

    private static boolean isRunning = true;

    private static int targetWidth = 800;

    private static int targetHeight = 600;

    private int fps;

    private int drawCubeCount = 0;

    private static final float FOV = 45.0f;

    private float dt = 0.005f;

    private FloatBuffer matSpecular;
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;
    private FloatBuffer lModelAmbient;

    private long lastFrameTime;

    private long lastFPS;

    private Walker walker;

    private MapCube mapCube;

    public App() {
        mapCube = new MapCube(CODE);
        walker = new Walker(CODE, mapCube);
    }

    public static void main(String[] args) {

        App main = new App();

        main.initDisplay();
        main.initGL();

        main.run();
    }

    private void initDisplay() {
        DisplayMode chosenMode = null;

        try {
            DisplayMode[] modes = Display.getAvailableDisplayModes();

            for (DisplayMode mode : modes) {
                if ((mode.getWidth() == targetWidth) && (mode.getHeight() == targetHeight)) {
                    chosenMode = mode;
                    break;
                }
            }
        } catch (LWJGLException e) {
            Sys.alert("Error", "Unable to determine display modes.");
            System.exit(0);
        }

        if (chosenMode == null) {
            Sys.alert("Error", "Unable to find appropriate display mode.");
            System.exit(0);
        }

        try {
            Display.setDisplayMode(chosenMode);
            Display.setFullscreen(false);
            Display.setTitle(" ");
            Display.setVSyncEnabled(true);
            Display.create(new PixelFormat(/*Alpha Bits*/8, /*Depth bits*/ 16, /*Stencil bits*/ 0, /*samples*/8));

            lastFPS = getTime();
        } catch (LWJGLException e) {
            Sys.alert("Error", "Unable to create display.");

            System.exit(0);
        }
    }

    private boolean initGL() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        GLU.gluPerspective(FOV, ((float) targetWidth) / ((float) targetHeight), 0.1f, 100.0f);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glShadeModel(GL11.GL_SMOOTH);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_FASTEST);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        setupLights();
        setupFog(Settings.FOG_DENSITY, Settings.FOG_START, Settings.FOG_END);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        return true;
    }

    private void run() {
        while (isRunning) {
            update();
            render();

            Display.update();
            Display.sync(Settings.FPS);

            processKeyboard();

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GLU.gluPerspective(FOV, ((float) targetWidth) / ((float) targetHeight), 0.1f, 100.0f);

            GL11.glMatrixMode(GL11.GL_MATRIX_MODE);

            if (Display.isCloseRequested()) {
                isRunning = false;
                Display.destroy();

                System.exit(0);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                System.exit(0);
            }
        }
    }

    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private void setupLights() {
        initLightArrays();

        glShadeModel(GL_FOG_MODE);
        glMaterial(GL_FRONT, GL_DIFFUSE, matSpecular);
        glMaterialf(GL_FRONT, GL_SHININESS, 100.0f);

        glLight(GL_LIGHT0, GL_POSITION, lightPosition);

        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);

        glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);

        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
    }

    private void setupFog(float density, float start, float end) {
        GL11.glEnable(GL11.GL_FOG);

        FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
        fogColor.put(Settings.FOG_COLOR_RED).put(Settings.FOG_COLOR_GREEN).put(Settings.FOG_COLOR_BLUE).put(Settings.FOG_COLOR_ALPHA).flip();

        int fogMode = GL11.GL_LINEAR;
        GL11.glFogi(GL11.GL_FOG_MODE, fogMode);
        GL11.glFog(GL11.GL_FOG_COLOR, fogColor);
        GL11.glFogf(GL11.GL_FOG_DENSITY, density);
        GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glFogf(GL11.GL_FOG_START, start);
        GL11.glFogf(GL11.GL_FOG_END, end);
    }

    private void update() {
        updateWindowTitle();

        resetDrawCubeCount();
        walker.stepNext(Settings.ITERATIONS);
    }

    private void resetDrawCubeCount() {
        drawCubeCount = 0;
    }

    private void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        drawBackground();

        GL11.glPushMatrix();

        Camera.applyRotation();

        drawWireframeCube();

        GL11.glTranslatef(-1.5f, -1.5f, -1.5f);

        drawSceneCubes();

        GL11.glPopMatrix();

        dt = getDeltaTime();
    }

    private void drawWireframeCube() {
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glColor3f(0.9f, 0.9f, 0.9f);
        GLUT.glutWireCube(3.1f);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_FOG);
    }

    private void processKeyboard() {

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            Camera.rotateLeft(dt);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            Camera.rotateRight(dt);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            Camera.rotateDown(dt);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            Camera.rotateUp(dt);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            Camera.zoomIn(dt);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            Camera.zoomOut(dt);
        }
    }

    private void initLightArrays() {

        matSpecular = BufferUtils.createFloatBuffer(4);
        matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();

        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();

        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();

        lModelAmbient = BufferUtils.createFloatBuffer(4);
        lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
    }

    private void updateWindowTitle() {
        if (getTime() - lastFPS > 1000) {
            int[] speed = walker.getSpeed();
            Display.setTitle(String.format("FPS: %d speed is <%d, %d, %d> cubes: %d", fps, speed[0], speed[1], speed[2], getDrawCubeCount()));

            fps = 0;
            lastFPS += 1000;
        }

        fps++;
    }

    private int getDrawCubeCount() {
        return drawCubeCount;
    }

    private void drawBackground() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        GL11.glClearColor(Settings.BKG_COLOR_RED, Settings.BKG_COLOR_GREEN, Settings.BKG_COLOR_BLUE, Settings.BKG_COLOR_ALPHA);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glDisable(GL_LIGHTING);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glColor4f(Settings.BKG_COLOR_RED, Settings.BKG_COLOR_GREEN, Settings.BKG_COLOR_BLUE, Settings.BKG_COLOR_ALPHA);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(1.0f, -1.0f);

        GL11.glColor4f(Settings.BKG_COLOR_RED / 2,Settings.BKG_COLOR_GREEN / 2, Settings.BKG_COLOR_BLUE / 2, Settings.BKG_COLOR_ALPHA);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);

        GL11.glEnd();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void drawSceneCubes() {
        GL11.glTranslatef(-(MapCube.getCubeSize() / 2), -(MapCube.getCubeSize() / 2), -(MapCube.getCubeSize() / 2));
        GL11.glBegin(GL11.GL_QUADS);

        int[] planeStep = new int[]{MapCube.D / 2, MapCube.D / 2, MapCube.D / 2};

        float x1;
        float x2;
        float x3;

        x1 = 0f;

        int xlim = Math.min(MapCube.W, planeStep[0] + MapCube.D / 2);

        for (int x = 0; x < xlim; x++) {
            x1 += MapCube.getCubeSize();
            x2 = 0f;
            for (int y = 0; y < MapCube.H; y++) {
                x2 += MapCube.getCubeSize();
                x3 = 0;
                for (int localz = 0; localz < MapCube.D; localz++) {
                    x3 += MapCube.getCubeSize();

                    int cl = mapCube.getElement(x, y, localz);
                    int n = mapCube.getNeighbours(x, y, localz);
                    if (n != 63) {
                        if (cl != 0) {
                            incrementDrawCubeCount();
                            GL11.glColor3f((float) colors[cl][0], (float) colors[cl][1], (float) colors[cl][2]);

                            if (x == xlim - 1) {
                                GLUT.drawWholeCube(MapCube.getCubeSize() / 2, x1, x2, x3, x, y, localz);
                            } else {
                                GLUT.drawCube(x1, x2, x3, MapCube.getCubeSize() / 2, mapCube.getNeighbours(x, y, localz));
                            }
                        }
                    }
                }
            }
        }

        GL11.glEnd();
        GL11.glTranslatef(MapCube.getCubeSize() / 2, MapCube.getCubeSize() / 2, MapCube.getCubeSize() / 2);
    }

    private void incrementDrawCubeCount() {
        drawCubeCount++;
    }

    private int getDeltaTime() {
        long time = getTime();
        int delta = (int) (time - lastFrameTime);
        lastFrameTime = time;

        return delta;
    }
}