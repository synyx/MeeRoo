package de.synyx.android.meeroo.mvp;

/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public interface BaseView<T> {

    /**
     * Set the presenter for the view. This should be called before the view is displayed.
     *
     * @param  presenter  to set
     */
    void setPresenter(T presenter);


    /**
     * @return  if the view is currently added to the UI.
     */
    boolean isAdded();
}
