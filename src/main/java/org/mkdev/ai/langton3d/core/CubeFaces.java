package org.mkdev.ai.langton3d.core;

/**
 * @author Michał Kołodziejski &lt;<I><A href="mailto:michal.kolodziejski@gmail.com">michal.kolodziejski@gmail.com</A></I>&gt;
 * @version 1.0
 * @license: GPLv3 (http://www.gnu.org/licenses/gpl-3.0.txt)
 * @since: 2014-05-11
 */
public enum CubeFaces {
    TOP(1),
    BOTTOM(2),
    FRONT(4),
    BACK(8),
    RIGHT(16),
    LEFT(32);

    private final int value;

    private CubeFaces(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
