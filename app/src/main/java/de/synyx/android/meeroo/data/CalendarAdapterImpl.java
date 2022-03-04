package de.synyx.android.meeroo.data;

import static de.synyx.android.meeroo.util.rx.CursorIterable.closeCursorIfLast;
import static de.synyx.android.meeroo.util.rx.CursorIterable.fromCursor;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.synyx.android.meeroo.business.account.AccountService;
import de.synyx.android.meeroo.business.calendar.CalendarModeService;
import de.synyx.android.meeroo.business.calendar.RoomCalendarModel;
import de.synyx.android.meeroo.domain.CalendarMode;
import de.synyx.android.meeroo.preferences.PreferencesService;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.functions.Function;


/**
 * @author Max Dobler - dobler@synyx.de
 */
public class CalendarAdapterImpl implements CalendarAdapter {

    private static final String RESSOURCE_SUFFIX = "*@resource.calendar.google.com";

    private final CalendarModeService calendarModeService;
    private final ContentResolver contentResolver;
    private final AccountService accountService;
    private final PreferencesService preferencesService;

    public CalendarAdapterImpl(PreferencesService preferencesService, ContentResolver contentResolver,
                               CalendarModeService calendarModeService, AccountService accountService) {

        this.preferencesService = preferencesService;
        this.contentResolver = contentResolver;
        this.calendarModeService = calendarModeService;
        this.accountService = accountService;
    }

    @Override
    public Observable<RoomCalendarModel> loadAllRooms() {

        return loadRooms(false);
    }


    @Override
    public Observable<RoomCalendarModel> loadVisibleRooms() {

        return loadRooms(true);
    }


    private Observable<RoomCalendarModel> loadRooms(boolean visibleOnly) {

        return
                Observable.fromIterable(fromCursor(loadRoomCalendars(visibleOnly))) //
                        .doAfterNext(closeCursorIfLast()) //
                        .map(toRoomCalendar());
    }


    @Override
    public Maybe<RoomCalendarModel> loadRoom(long id) {

        Cursor cursor = loadRoomCalendarById(id);

        if (!cursor.moveToFirst()) {
            return Maybe.empty();
        }

        return Maybe.just(cursor).map(toRoomCalendar());
    }


    private Cursor loadRoomCalendars(boolean visibleOnly) {

        String[] mProjection = {
                CalendarContract.Calendars._ID, //
                CalendarContract.Calendars.OWNER_ACCOUNT, //
                CalendarContract.Calendars.NAME, //
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };

        List<String> selectionClauses = new ArrayList<>();
        List<String> selectionArgs = new ArrayList<>();

        addOwnerAccountSelection(selectionClauses, selectionArgs);
        addAccountNameSelection(selectionClauses, selectionArgs);

        if (visibleOnly) {
            addHiddenRoomsClause(selectionClauses);
        }

        return queryCalendarProvider(mProjection, selectionClauses, selectionArgs);
    }


    @SuppressLint("MissingPermission")
    private Cursor loadRoomCalendarById(long id) {

        String[] mProjection = {
                CalendarContract.Calendars._ID, //
                CalendarContract.Calendars.OWNER_ACCOUNT, //
                CalendarContract.Calendars.NAME, //
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };

        String selection = CalendarContract.Calendars._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return contentResolver.query(CalendarContract.Calendars.CONTENT_URI, mProjection, selection, selectionArgs,
                null);
    }


    private void addHiddenRoomsClause(List<String> selectionClauses) {

        Set<String> hiddenRoomIds = preferencesService.getHiddenRoomIds();

        String hiddenRooms = TextUtils.join(",", hiddenRoomIds);
        String clause = CalendarContract.Calendars._ID + " NOT IN (" + hiddenRooms + ")";
        selectionClauses.add(clause);
    }


    @NonNull
    private static Function<Cursor, RoomCalendarModel> toRoomCalendar() {

        return
                cursor -> {
                    String name = getNameFrom(cursor).split("\\(")[0]; // Remove id from shared rooms
                    return new RoomCalendarModel(getIdFrom(cursor), name, getOwnerAccountFrom(cursor), 0);
                };
    }


    @SuppressLint("MissingPermission")
    private Cursor queryCalendarProvider(String[] mProjection, List<String> mSelectionClauses,
                                         List<String> selectionArgs) {

        String selection = TextUtils.join(" AND ", mSelectionClauses);

        String[] selectionArgsArray = selectionArgs.toArray(new String[0]);

        return contentResolver.query(CalendarContract.Calendars.CONTENT_URI, mProjection, selection,
                selectionArgsArray, null);
    }


    private void addAccountNameSelection(List<String> mSelectionClauses, List<String> mSelectionArgs) {

        mSelectionClauses.add(CalendarContract.Calendars.ACCOUNT_NAME + " = ?");
        mSelectionArgs.add(accountService.getUserAccountName());
    }


    private void addOwnerAccountSelection(List<String> mSelectionClauses, List<String> mSelectionArgs) {

        CalendarMode calendarMode = calendarModeService.getPrefCalenderMode();

        if (calendarMode == CalendarMode.RESOURCES) {
            mSelectionClauses.add(CalendarContract.Calendars.OWNER_ACCOUNT + " GLOB ?");
            mSelectionArgs.add(RESSOURCE_SUFFIX);
        } else {
            String accountType = accountService.getUserAccountType();
            mSelectionClauses.add(CalendarContract.Calendars.OWNER_ACCOUNT + " LIKE '%" + accountType + "'");
        }
    }


    private static long getIdFrom(Cursor cursor) {

        return cursor.getLong(cursor.getColumnIndex(CalendarContract.Calendars._ID));
    }


    private static String getOwnerAccountFrom(Cursor cursor) {

        return cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.OWNER_ACCOUNT));
    }


    private static String getNameFrom(Cursor cursor) {

        String name = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.NAME));
        String displayName = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));


        return displayName != null //
                ? displayName //
                : name;
    }
}
