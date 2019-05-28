package de.synyx.android.meeroo.screen.main.status;

import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;

import android.support.v7.app.AlertDialog;

import de.synyx.android.meeroo.R;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class EndNowDialogFragment extends DialogFragment {

    EndNowOnDialogListener endNowDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity()) //
            .setTitle(R.string.end_now_dialog_title)
            .setMessage(R.string.end_now_dialog_message)
            .setPositiveButton(R.string.end_now_dialog_confirm, (dialog, which) -> endNowDialogListener.endNow())
            .setNegativeButton(R.string.end_now_dialog_cancel, (dialog, which) -> { })
            .create();
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        try {
            endNowDialogListener = (EndNowOnDialogListener) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException(getActivity().getClass().getSimpleName()
                + " must implement EndNowOnDialogListener and OnDialogDismissListener");
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
        endNowDialogListener.onEndNowDialogDismiss();
    }

    public interface EndNowOnDialogListener {

        void endNow();


        void onEndNowDialogDismiss();
    }
}
