package de.synyx.android.meetingroom.screen.main.status;

import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;

import android.support.v7.app.AlertDialog;

import de.synyx.android.meetingroom.R;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class BookNowDialogFragment extends DialogFragment {

    BookNowDialogListener bookNowDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity()) //
            .setTitle(R.string.book_now_dialog_title)
            .setMessage(R.string.book_now_dialog_message)
            .setPositiveButton(R.string.book_now_dialog_confirm, (dialog, which) -> bookNowDialogListener.bookNow())
            .setNegativeButton(R.string.book_now_dialog_cancel, (dialog, which) -> { })
            .create();
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        try {
            bookNowDialogListener = (BookNowDialogListener) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException(getActivity().getClass().getSimpleName()
                + " must implement BookNowDialogListener");
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
        bookNowDialogListener.onBookNowDialogDismiss();
    }

    public interface BookNowDialogListener {

        void bookNow();


        void onBookNowDialogDismiss();
    }
}
