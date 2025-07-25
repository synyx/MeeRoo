package de.synyx.android.meeroo.data;

import static de.synyx.android.meeroo.util.rx.CursorIterable.closeCursorIfLast;
import static de.synyx.android.meeroo.util.rx.CursorIterable.fromCursor;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import android.util.Log;

import androidx.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;

import java.security.AccessControlException;
import java.util.Date;
import java.util.TimeZone;

import de.synyx.android.meeroo.business.event.EventModel;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;


/**
 * @author Max Dobler - dobler@synyx.de
 */
public class EventAdapterImpl implements EventAdapter {

    private static final String TAG = EventAdapterImpl.class.getSimpleName();
    private static final int TRUE = 1;
    private final ContentResolver contentResolver;

    public EventAdapterImpl(ContentResolver contentResolver) {

        this.contentResolver = contentResolver;
    }

    @Override
    public Observable<EventModel> getEventsForRoom(long roomId) {

        String[] projection = {
                Instances.EVENT_ID, //
                Instances.TITLE, //
                Instances.BEGIN, //
                Instances.END, //
                Instances.DURATION, //
                Instances.STATUS, //
                Instances.RRULE
        };
        String selection = Instances.CALENDAR_ID + " = " + roomId;
        String sortChronological = Instances.BEGIN + " ASC";

        Cursor result = contentResolver.query(constructContentUri(), projection, selection, null, sortChronological);

        return Observable.fromIterable(fromCursor(result)) //
                .doAfterNext(closeCursorIfLast()) //
                .map(toEvent())
                .filter(eventModel -> eventModel.getStatus() != Instances.STATUS_CANCELED);
    }


    @Override
    public Maybe<Long> insertEvent(long calendarId, DateTime start, DateTime end, String title) {

        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, start.getMillis());
        values.put(Events.DTEND, end.getMillis());
        values.put(Events.TITLE, title);
        values.put(Events.CALENDAR_ID, calendarId);
        values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(Events.GUESTS_CAN_MODIFY, TRUE);

        try {
            Uri insert = contentResolver.insert(Events.CONTENT_URI, values);

            return Maybe.just(Long.valueOf(insert.getLastPathSegment()));
        } catch (AccessControlException exception) {
            Log.e(TAG, "insertEvent: ", exception);

            return Maybe.empty();
        }
    }


    @Override
    public boolean updateEvent(long eventId, DateTime start, DateTime end, boolean recurring) {

        if (recurring) {
            return updateRecurringEvent(eventId, start, end);
        }

        return updateSingleEvent(eventId, end);
    }


    private boolean updateRecurringEvent(long eventId, DateTime start, DateTime end) {

        ContentValues values = new ContentValues();
        values.put(Events.ORIGINAL_INSTANCE_TIME, start.getMillis());
        values.put(Events.DURATION, getNewDuration(start, end));

        Uri.Builder eventUriBuilder = Events.CONTENT_EXCEPTION_URI.buildUpon();
        ContentUris.appendId(eventUriBuilder, eventId);

        final Uri resultUri = contentResolver.insert(eventUriBuilder.build(), values);

        return resultUri != null;
    }


    private boolean updateSingleEvent(long eventId, DateTime end) {

        ContentValues values = new ContentValues();
        values.put(Events.DTEND, end.getMillis());

        Uri eventUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventId);
        int rows = contentResolver.update(eventUri, values, null, null);

        return rows == 1;
    }


    @SuppressLint("DefaultLocale")
    @NonNull
    private String getNewDuration(DateTime start, DateTime end) {

        long seconds = new Duration(start, end).getStandardSeconds();

        return String.format("P%dS", seconds);
    }


    private Uri constructContentUri() {

        Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
        long now = new Date().getTime();
        long endOfDay = LocalDateTime.now().withTime(23, 59, 59, 999).toDate().getTime();
        ContentUris.appendId(builder, now);
        ContentUris.appendId(builder, endOfDay);

        return builder.build();
    }


    @NonNull
    private Function<Cursor, EventModel> toEvent() {

        return
                cursor -> {
                    long eventId = cursor.getLong(cursor.getColumnIndexOrThrow(Instances.EVENT_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(Instances.TITLE));
                    long beginMillis = cursor.getLong(cursor.getColumnIndexOrThrow(Instances.BEGIN));
                    long endMillis = cursor.getLong(cursor.getColumnIndexOrThrow(Instances.END));
                    String recurring = cursor.getString(cursor.getColumnIndexOrThrow(Instances.RRULE));
                    int status = cursor.getInt(cursor.getColumnIndexOrThrow(Instances.STATUS));

                    DateTime begin = new DateTime(beginMillis);
                    DateTime end = new DateTime(endMillis);

                    return new EventModel(eventId, title, begin, end, status, recurring != null);
                };
    }
}
