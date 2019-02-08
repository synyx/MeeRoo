package de.synyx.android.meetingroom.domain;

/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class BookingResult {

    public final String errorMessage;

    private BookingResult(String errorMessage) {

        this.errorMessage = errorMessage;
    }

    public static BookingResult success() {

        return new BookingResult(null);
    }


    public static BookingResult error(String errorMessage) {

        return new BookingResult(errorMessage);
    }


    public boolean hasError() {

        return errorMessage != null;
    }
}
