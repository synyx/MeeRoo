package de.synyx.android.meeroo.util.rx;

import android.database.Cursor;

import android.support.annotation.NonNull;

import io.reactivex.functions.Consumer;

import java.util.Iterator;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class CursorIterable implements Iterable<Cursor> {

    private final Cursor cursor;

    private CursorIterable(Cursor cursor) {

        this.cursor = cursor;
    }

    public static CursorIterable fromCursor(Cursor cursor) {

        return new CursorIterable(cursor);
    }


    @NonNull
    public static Consumer<Cursor> closeCursorIfLast() {

        return cursor -> {
            if (cursor.isLast()) {
                cursor.close();
            }
        };
    }


    @Override
    public Iterator iterator() {

        return CursorIterator.from(cursor);
    }

    public static class CursorIterator implements Iterator<Cursor> {

        private Cursor cursor;

        private CursorIterator(Cursor cursor) {

            this.cursor = cursor;
        }

        public static CursorIterator from(Cursor cursor) {

            return new CursorIterator(cursor);
        }


        @Override
        public boolean hasNext() {

            return !cursor.isClosed() && cursor.moveToNext();
        }


        @Override
        @SuppressWarnings("squid:S2272")
        public Cursor next() {

            return cursor;
        }
    }
}
