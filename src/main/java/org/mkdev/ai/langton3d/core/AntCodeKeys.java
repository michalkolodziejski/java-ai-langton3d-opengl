package org.mkdev.ai.langton3d.core;

/**
 * @author Michał Kołodziejski &lt;<I><A href="mailto:michal.kolodziejski@gmail.com">michal.kolodziejski@gmail.com</A></I>&gt;
 * @version 1.0
 * @license: GPLv3 (http://www.gnu.org/licenses/gpl-3.0.txt)
 * @since: 2014-05-11
 */
public enum AntCodeKeys {
    RIGHT(0),
    LEFT(1),
    UP(2),
    DOWN(3);

    private final int value;

    private AntCodeKeys(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }

}
