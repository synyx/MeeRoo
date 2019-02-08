package de.synyx.android.meetingroom.business.calendar;

/**
 * @author  Julia Dasch - dasch@synyx.de
 * @author  Max Dobler - dobler@synyx.de
 */
public class RoomCalendarModel {

    private final String name;
    private final long calendarId;
    private final String owner;

    public RoomCalendarModel(long calendarId, String name, String owner) {

        this.calendarId = calendarId;
        this.name = name;
        this.owner = owner;
    }

    public String getName() {

        return name;
    }


    public long getCalendarId() {

        return calendarId;
    }


    public String getOwner() {

        return owner;
    }
}
