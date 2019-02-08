package de.synyx.android.meetingroom.util.livedata;

/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class SingleEvent<T> {

    private final T content;
    private boolean hasBeenHandled = false;

    public SingleEvent(T content) {

        this.content = content;
    }

    public T getContentIfNotHandled() {

        if (hasBeenHandled) {
            return null;
        }

        hasBeenHandled = true;

        return content;
    }


    public static <T> SingleEvent<T> withContent(T content) {

        return new SingleEvent<>(content);
    }
}
