package org.mkdev.ai.langton3d.core;

/**
 * @author Michał Kołodziejski &lt;<I><A href="mailto:michal.kolodziejski@gmail.com">michal.kolodziejski@gmail.com</A></I>&gt;
 * @version 1.0
 * @license: GPLv3 (http://www.gnu.org/licenses/gpl-3.0.txt)
 * @since: 2014-05-11
 */
public class Settings {

    private Settings() {throw new UnsupportedOperationException("Inaccessible constructor");}

    public static final int ITERATIONS = 5;

    public static final int FPS = 60;

    public static final float FOG_DENSITY = 0.25f;
    public static final float FOG_START = 2.5f;
    public static final float FOG_END = 7.5f;

    public static final float FOG_COLOR_RED = 0.07f;
    public static final float FOG_COLOR_GREEN = 0.05f;
    public static final float FOG_COLOR_BLUE = 0.40f;
    public static final float FOG_COLOR_ALPHA = 0.5f;

    public static final float BKG_COLOR_RED = 0.10f;
    public static final float BKG_COLOR_GREEN = 0.09f;
    public static final float BKG_COLOR_BLUE = 0.50f;
    public static final float BKG_COLOR_ALPHA = 1f;
}
