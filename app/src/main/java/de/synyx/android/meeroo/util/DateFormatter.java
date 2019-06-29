package de.synyx.android.meeroo.util;

import android.content.Context;

import androidx.annotation.NonNull;

import de.synyx.android.meeroo.R;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;


/**
 * @author  Julia Dasch - dasch@synyx.de
 * @author  Max Dobler - dobler@synyx.de
 */
public class DateFormatter {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            Locale.getDefault());

    public String formatToISOTime(Date date) {

        // we have to place the colon ourselves, as SimpleDateFormat only supports timezone without colon
        return dateFormat.format(date).replaceAll("(.*)(\\d\\d)$", "$1:$2");
    }


    public static PeriodFormatter periodFormatter(Context context) {

        return new PeriodFormatterBuilder().appendDays()
            .appendSuffix(addSpaceBefore(context, R.string.time_suffix_day),
                    addSpaceBefore(context, R.string.time_suffix_days))
            .appendSeparator(" ")
            .appendHours()
            .appendSuffix(addSpaceBefore(context, R.string.time_suffix_hour),
                    addSpaceBefore(context, R.string.time_suffix_hours))
            .appendSeparator(" ")
            .appendMinutes()
            .appendSuffix(addSpaceBefore(context, R.string.time_suffix_minute),
                addSpaceBefore(context, R.string.time_suffix_minutes)).toFormatter();
    }


    @NonNull
    private static String addSpaceBefore(Context context, int stringResource) {

        return " " + context.getString(stringResource);
    }


    public static PeriodFormatter smallPeriodFormatter() {

        return new PeriodFormatterBuilder().appendDays()
            .appendSuffix(" d")
            .appendSeparator(" ")
            .appendHours().appendSuffix(" h").appendSeparator(" ").appendMinutes().appendSuffix(" min").toFormatter();
    }
}
