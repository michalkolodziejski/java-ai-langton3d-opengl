package org.mkdev.ai.langton3d.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michał Kołodziejski &lt;<I><A href="mailto:michal.kolodziejski@gmail.com">michal.kolodziejski@gmail.com</A></I>&gt;
 * @version 1.0
 * @license: GPLv3 (http://www.gnu.org/licenses/gpl-3.0.txt)
 * @since: 2014-05-11
 */
public class Walker extends Thread {

    private final Logger logger = LoggerFactory.getLogger("org.mkdev.ai.langton");

    private static final int THREAD_SLEEP_TIME = 250;

    private static final int VALUES_COUNT = 3;

    protected final int xp[] = new int[]{ 1,  0,  0};
    protected final int xm[] = new int[]{-1,  0,  0};
    protected final int yp[] = new int[]{ 0,  1,  0};
    protected final int ym[] = new int[]{ 0, -1,  0};
    protected final int zp[] = new int[]{ 0,  0,  1};
    protected final int zm[] = new int[]{ 0,  0, -1};

    private final AntCodeKeys[] code;

    int loc[] = new int[VALUES_COUNT];

    int codeIndex = 0;
    boolean alive = true;
    long maxIterations = 0;
    long iteration = 0;

    Thread runner;

    MapCube mapCube;

    int face[], right[], left[], up[], down[], back[], tmp[];

    public Walker(AntCodeKeys[] acode, MapCube amapCube) {
        super();

        loc[0] = MapCube.W / 2;
        loc[1] = MapCube.H / 2;
        loc[2] = MapCube.D / 2;

        face = xp;
        back = xm;
        right = zp;
        left = zm;
        up = yp;
        down = ym;

        this.code = acode;
        this.mapCube = amapCube;
    }

    public final void stepNext(long iterations) {
        maxIterations = iterations;
        iteration = 0;
        if (runner == null) {
            runner = new Thread() {
                public void run() {
                    work();

                    try {
                        Thread.yield();
                        Thread.sleep(THREAD_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        logger.error("Walker thread interrupted: ", e);
                    }
                }
            };
            alive = true;
            runner.start();
        }
    }

    private void work() {
        while (alive) {
            if (++iteration > maxIterations) {
                alive = false;
            } else {
                advance();
            }
        }
        runner = null;
    }

    private void advance() {
        for (int i = 0; i < VALUES_COUNT; i++) {
            loc[i] += face[i];
            if (loc[i] < 0) {
                loc[i] += MapCube.SPACE[i];
            }

            loc[i] %= MapCube.SPACE[i];
        }

        int color = mapCube.getColor(loc);
        turn(code[color]);
        mapCube.setColor(loc, advanceCode(color));
    }

    private void turn(AntCodeKeys cd) {
        switch (cd) {
            case RIGHT:
                tmp = face;
                face = right;
                right = back;
                back = left;
                left = tmp;
                break;
            case LEFT:
                tmp = face;
                face = left;
                left = back;
                back = right;
                right = tmp;
                break;
            case UP:
                tmp = face;
                face = up;
                up = back;
                back = down;
                down = tmp;
                break;
            case DOWN:
                tmp = face;
                face = down;
                down = back;
                back = up;
                up = tmp;
                break;
        }
    }

    private int advanceCode(int cod) {
        codeIndex = ((cod + 1) % code.length);

        return codeIndex;
    }

    public final int[] getSpeed() {

        return new int[]{face[0], face[1], face[2]};
    }
}
