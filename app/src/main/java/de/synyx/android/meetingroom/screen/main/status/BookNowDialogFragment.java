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

    BookNowOnClickListener bookNowClickListener;
    OnDialogDismissListener onDismissListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity()) //
            .setTitle(R.string.book_now_dialog_title)
            .setMessage(R.string.book_now_dialog_message)
            .setPositiveButton(R.string.book_now_dialog_confirm, (dialog, which) -> bookNowClickListener.bookNow())
            .setNegativeButton(R.string.book_now_dialog_cancel, (dialog, which) -> { })
            .create();
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        try {
            bookNowClickListener = (BookNowOnClickListener) context;
            onDismissListener = (OnDialogDismissListener) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException(getActivity().getClass().getSimpleName()
                + " must implement BookNowOnClickListener");
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
        onDismissListener.onDialogDissmiss();
    }

    public interface BookNowOnClickListener {

        void bookNow();
    }

    public interface OnDialogDismissListener {

        void onDialogDissmiss();
    }
}
