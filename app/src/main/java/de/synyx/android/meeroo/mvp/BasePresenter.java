package de.synyx.android.meeroo.mvp;

/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public interface BasePresenter {

    /**
     * Notify the presenter that the view is ready to be updated.
     */
    void start();


    /**
     * Notify the presenter that the view is stopped / destroyed and no longer able to receive updates.
     */
    void stop();
}
