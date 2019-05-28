package de.synyx.android.meeroo.screen;

/**
 * @author  Max Dobler - dobler@synyx.de
 */
public enum ScreenSize {

    XSMALL(480),
    SMALL(840),
    MEDIUM(1280),
    LARGE(1440),
    XLARGE(Float.MAX_VALUE);

    private final float maxWidth;

    ScreenSize(float maxWidth) {

        this.maxWidth = maxWidth;
    }

    public float getMaxWidth() {

        return maxWidth;
    }
}
