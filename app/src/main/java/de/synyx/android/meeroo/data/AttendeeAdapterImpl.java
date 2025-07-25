package de.synyx.android.meeroo.data;

import android.content.ContentResolver;
import android.content.ContentValues;

import android.database.Cursor;

import android.provider.CalendarContract.Attendees;

import de.synyx.android.meeroo.domain.Attendee;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;

import static de.synyx.android.meeroo.util.rx.CursorIterable.closeCursorIfLast;
import static de.synyx.android.meeroo.util.rx.CursorIterable.fromCursor;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class AttendeeAdapterImpl implements AttendeeAdapter {

    private final ContentResolver contentResolver;

    public AttendeeAdapterImpl(ContentResolver contentResolver) {

        this.contentResolver = contentResolver;
    }

    @Override
    public Observable<Attendee> getAttendeesForEvent(long eventId) {

        String[] mProjection = {
            Attendees.ATTENDEE_NAME, //
            Attendees.ATTENDEE_STATUS
        };
        String isEventAttendee = Attendees.EVENT_ID + " = " + eventId;
        String isResource = Attendees.ATTENDEE_TYPE + " = " + Attendees.TYPE_RESOURCE;
        String selection = isEventAttendee + " AND " + isResource;

        String[] mSelectionArgs = {};

        Cursor cursor = contentResolver.query(Attendees.CONTENT_URI, mProjection, selection, mSelectionArgs, null);

        return Observable.fromIterable(fromCursor(cursor)) //
            .doAfterNext(closeCursorIfLast()) //
            .map(toAttendee());
    }


    @Override
    public void insertAttendeeForEvent(long eventId, String attendeeName) {

        ContentValues values = new ContentValues();
        values.put(Attendees.EVENT_ID, eventId);
        values.put(Attendees.ATTENDEE_NAME, attendeeName);
        values.put(Attendees.ATTENDEE_RELATIONSHIP, Attendees.RELATIONSHIP_ATTENDEE);
        values.put(Attendees.ATTENDEE_TYPE, Attendees.TYPE_OPTIONAL);
        values.put(Attendees.ATTENDEE_STATUS, Attendees.ATTENDEE_STATUS_ACCEPTED);

        contentResolver.insert(Attendees.CONTENT_URI, values);
    }


    private Function<Cursor, Attendee> toAttendee() {

        return
            cursor -> {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Attendees.ATTENDEE_NAME));
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(Attendees.ATTENDEE_STATUS));

            return new Attendee(name, status);
        };
    }
}
