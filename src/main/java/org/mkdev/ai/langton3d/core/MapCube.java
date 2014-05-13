package org.mkdev.ai.langton3d.core;

/**
 * @author Michał Kołodziejski &lt;<I><A href="mailto:michal.kolodziejski@gmail.com">michal.kolodziejski@gmail.com</A></I>&gt;
 * @version 1.0
 * @license: GPLv3 (http://www.gnu.org/licenses/gpl-3.0.txt)
 * @since: 2014-05-11
 */
public class MapCube {
    protected static final int W = 50;
    protected static final int H = 50;
    protected static final int D = 50;

    public static float getCubeSize() {
        return CUBE_SIZE;
    }

    private static final float CUBE_SIZE = 3f / MapCube.D;

    static final int SPACE[] = {MapCube.W, MapCube.H, MapCube.D};

    private int map[][][];
    private int neighbourhood[][][];
    private AntCodeKeys[] code;

    public MapCube(AntCodeKeys[] acode) {
        map = new int[W][H][D];
        neighbourhood = new int[W][H][D];

        this.code = acode;
    }

    public final int[][][] getMap() {
        return map;
    }

    public final void setMap(int[][][] amap) {
        this.map = amap;
    }

    public final int[][][] getNeighbourhood() {
        return neighbourhood;
    }

    public final void setNeighbourhood(int[][][] aneibs) {
        this.neighbourhood = aneibs;
    }

    public final AntCodeKeys[] getCode() {
        return code;
    }

    public final void setCode(AntCodeKeys[] acode) {
        this.code = acode;
    }

    public final int getColor(int loc[]) {
        return map[loc[0]][loc[1]][loc[2]];
    }

    public final void setColor(int loc[], int color) {

        if (code[map[loc[0]][loc[1]][loc[2]]] != code[color]) {
            if (code[color].getValue() == 0) {
                updateNeighbours(loc, false);
            }
            if (code[map[loc[0]][loc[1]][loc[2]]].getValue() == 0) {
                updateNeighbours(loc, true);
            }
        }
        map[loc[0]][loc[1]][loc[2]] = color;
    }

    protected final void updateNeighbours(int[] loc, boolean on) {
        if (on) {
            if (loc[0] + 1 < SPACE[0]) {
                neighbourhood[loc[0] + 1][loc[1]][loc[2]] |= CubeFaces.LEFT.getValue();
            }
            if (loc[0] - 1 >= 0) {
                neighbourhood[loc[0] - 1][loc[1]][loc[2]] |= CubeFaces.RIGHT.getValue();
            }
            if (loc[1] + 1 < SPACE[1]) {
                neighbourhood[loc[0]][loc[1] + 1][loc[2]] |= CubeFaces.BOTTOM.getValue();
            }
            if (loc[1] - 1 >= 0) {
                neighbourhood[loc[0]][loc[1] - 1][loc[2]] |= CubeFaces.TOP.getValue();
            }
            if (loc[2] + 1 < SPACE[2]) {
                neighbourhood[loc[0]][loc[1]][loc[2] + 1] |= CubeFaces.BACK.getValue();
            }
            if (loc[2] - 1 >= 0) {
                neighbourhood[loc[0]][loc[1]][loc[2] - 1] |= CubeFaces.FRONT.getValue();
            }
        } else {
            if (loc[0] + 1 < SPACE[0]) {
                neighbourhood[loc[0] + 1][loc[1]][loc[2]] &= ~CubeFaces.LEFT.getValue();
            }
            if (loc[0] - 1 >= 0) {
                neighbourhood[loc[0] - 1][loc[1]][loc[2]] &= ~CubeFaces.RIGHT.getValue();
            }
            if (loc[1] + 1 < SPACE[1]) {
                neighbourhood[loc[0]][loc[1] + 1][loc[2]] &= ~CubeFaces.BOTTOM.getValue();
            }
            if (loc[1] - 1 >= 0) {
                neighbourhood[loc[0]][loc[1] - 1][loc[2]] &= ~CubeFaces.TOP.getValue();
            }
            if (loc[2] + 1 < SPACE[2]) {
                neighbourhood[loc[0]][loc[1]][loc[2] + 1] &= ~CubeFaces.BACK.getValue();
            }
            if (loc[2] - 1 >= 0) {
                neighbourhood[loc[0]][loc[1]][loc[2] - 1] &= ~CubeFaces.FRONT.getValue();
            }
        }
    }

    public final int getElement(int x, int y, int z) {

        return map[x][y][z];
    }

    public final int getNeighbours(int x, int y, int z) {

        return neighbourhood[x][y][z];
    }
}
