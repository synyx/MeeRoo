package de.synyx.android.meetingroom.util;

import java.util.Date;


/**
 * @author  Julia Dasch - dasch@synyx.de
 */
public class TimeServiceImpl implements TimeService {

    private final DateFormatter dateFormatter = new DateFormatter();

    @Override
    public long getCurrentTimestamp() {

        return System.currentTimeMillis();
    }


    @Override
    public String getFormattedCurrentTime() {

        return dateFormatter.formatToISOTime(new Date());
    }
}
