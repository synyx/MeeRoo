package de.synyx.android.meeroo.domain;

import org.joda.time.DateTime;
import org.joda.time.Duration;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
class TimeSpan {

    public final DateTime begin;
    public final DateTime end;

    TimeSpan(DateTime begin, DateTime end) {

        this.begin = begin;
        this.end = end;
    }

    public static TimeSpan zeroTimeSpanFrom(DateTime begin) {

        return new TimeSpan(begin, begin);
    }


    public DateTime getBegin() {

        return begin;
    }


    public DateTime getEnd() {

        return end;
    }


    public Duration toDuration() {

        return new Duration(begin, end);
    }
}
