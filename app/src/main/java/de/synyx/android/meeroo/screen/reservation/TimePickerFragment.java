package de.synyx.android.meeroo.screen.reservation;

import android.app.Dialog;
import android.app.TimePickerDialog;

import android.os.Bundle;

import android.support.v4.app.DialogFragment;

import android.widget.TimePicker;

import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import static android.text.format.DateFormat.is24HourFormat;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private Collection<OnTimeSelectionListener> onTimeSelectionListeners = new HashSet<>(1);

    public boolean addOnTimeSelectionListener(OnTimeSelectionListener listener) {

        return this.onTimeSelectionListeners.add(listener);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 1);

        int hour = c.get(Calendar.HOUR_OF_DAY);

        return new TimePickerDialog(getActivity(), this, hour, 0, is24HourFormat(getActivity()));
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        LocalTime time = new LocalTime(hourOfDay, minute);

        for (OnTimeSelectionListener listener : onTimeSelectionListeners) {
            listener.onSelectTime(time);
        }

        dismiss();
    }

    @FunctionalInterface
    interface OnTimeSelectionListener {

        void onSelectTime(LocalTime time);
    }
}
