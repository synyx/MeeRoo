package de.synyx.android.meeroo.util;

/**
 * @author  Julia Dasch - dasch@synyx.de
 */
public interface TimeService {

    /**
     * @return  the current timestamp in milliseconds.
     */
    long getCurrentTimestamp();


    /**
     * @return  the current time in format yyyy-MM-dd'T'HH:mm:ss.SSSXXX (e.g. 2017-08-30T11:32:58.920+02:00)
     */
    String getFormattedCurrentTime();
}
