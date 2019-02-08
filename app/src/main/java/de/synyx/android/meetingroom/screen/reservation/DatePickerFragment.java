package de.synyx.android.meetingroom.screen.reservation;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;

import android.support.annotation.NonNull;

import android.support.v4.app.DialogFragment;

import android.widget.DatePicker;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Collection<OnDateSelectionListener> onDateSelectionListeners = new HashSet<>(1);

    public boolean addOnDateSelectionListener(OnDateSelectionListener listener) {

        return this.onDateSelectionListeners.add(listener);
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        LocalDate date = new LocalDate(year, month + 1, dayOfMonth);

        for (OnDateSelectionListener listener : onDateSelectionListeners) {
            listener.onSelectDate(date);
        }

        dismiss();
    }

    @FunctionalInterface
    interface OnDateSelectionListener {

        void onSelectDate(LocalDate date);
    }
}
