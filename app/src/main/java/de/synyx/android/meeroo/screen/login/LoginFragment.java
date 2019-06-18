package de.synyx.android.meeroo.screen.login;

import android.Manifest;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.net.Uri;

import android.os.Bundle;

import android.provider.Settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.util.proxy.PermissionManager;

import java.util.List;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class LoginFragment extends Fragment implements LoginContract.LoginView {

    private static final int REQUEST_PERMISSION_SETTING = 1;
    private LoginContract.LoginPresenter presenter;
    private TextView progressText;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        progressText = view.findViewById(R.id.login_progress_text);
        progressBar = view.findViewById(R.id.login_progress_bar);

        return view;
    }


    @Override
    public void onStart() {

        super.onStart();

        presenter.start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (permissions.length == 0) {
                // the dialog was cancelled, e.g. orientation change
                return;
            }

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    onPermissionDenied(permissions[i]);

                    return;
                }
            }

            presenter.onAllPermissionsGranted();
        }
    }


    @Override
    public void askForPermissions(List<String> neededPermissions) {

        String[] permissionsToRequest = new String[neededPermissions.size()];

        Registry.get(PermissionManager.class)
            .requestPermission(this, neededPermissions.toArray(permissionsToRequest), REQUEST_PERMISSION_SETTING);
    }


    @Override
    public void showAccountSelection(String[] accounts) {

        new AlertDialog.Builder(getActivity()).setTitle(R.string.selectAccount)
            .setItems(accounts, (dialog, which) -> presenter.onAccountSelected(accounts[which]))
            .setCancelable(false).create().show();
    }


    @Override
    public void showCalendarModeSelection(String[] calenderModes) {

        new AlertDialog.Builder(getActivity()).setTitle(R.string.selectMode)
            .setItems(calenderModes, (dialog, which) -> presenter.onCalendarModeSelected(calenderModes[which]))
            .setCancelable(false).create().show();
    }


    @Override
    public void showErrorDialog() {

        new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.noCalendarsError))
            .setTitle(getString(R.string.calendarError))
            .setPositiveButton(R.string.close, ((dialog, which) -> presenter.onErrorDialogCloseButtonClicked()))
            .setCancelable(false).create().show();
    }


    @Override
    public void showProgress() {

        progressBar.setVisibility(View.VISIBLE);
        progressText.setVisibility(View.VISIBLE);
        progressText.setText(getString(R.string.calendarPending));
    }


    public void setPresenter(LoginContract.LoginPresenter presenter) {

        this.presenter = presenter;
    }


    private void onPermissionDenied(String permission) {

        if (Registry.get(PermissionManager.class).shouldShowRequestPermissionRationale(this, permission)) {
            showPermissionNeededDialog(permission,
                (dialog, which) -> handlePermissionsNeededRationaleDialogResult(which));
        } else {
            showPermissionNeededDialog(permission, (dialog, which) -> handlePermissionsNeededDialogResult(which));
        }
    }


    private void handlePermissionsNeededDialogResult(int which) {

        if (which == DialogInterface.BUTTON_POSITIVE) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            presenter.onAllPermissionsGranted();
        }
    }


    private void handlePermissionsNeededRationaleDialogResult(int which) {

        if (which == DialogInterface.BUTTON_POSITIVE) {
            presenter.askForPermissionsAgain();
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            presenter.onAllPermissionsGranted();
        }
    }


    private void showPermissionNeededDialog(String permission, DialogInterface.OnClickListener clickListener) {

        if (getActivity() == null) {
            return;
        }

        switch (permission) {
            case Manifest.permission.READ_CALENDAR:
                showChoiceDialog(getString(R.string.dialog_calendar_permission_massage),
                    getString(R.string.dialog_calendar_permission_title),
                    getString(R.string.permission_dialog_positive), getString(R.string.permission_dialog_negative),
                    clickListener);
                break;

            case Manifest.permission.WRITE_CALENDAR:
                showChoiceDialog(getString(R.string.dialog_calendar_permission_massage),
                    getString(R.string.dialog_calendar_permission_title),
                    getString(R.string.permission_dialog_positive), getString(R.string.permission_dialog_negative),
                    clickListener);
                break;

            case Manifest.permission.GET_ACCOUNTS:
                showChoiceDialog(getString(R.string.dialog_accounts_permission_massage),
                    getString(R.string.dialog_accounts_permission_title),
                    getString(R.string.permission_dialog_positive), getString(R.string.permission_dialog_negative),
                    clickListener);
                break;

            case Manifest.permission.READ_SYNC_SETTINGS:
                showChoiceDialog(getString(R.string.dialog_sync_settings_permission_massage),
                    getString(R.string.dialog_sync_settings_permission_title),
                    getString(R.string.permission_dialog_positive), getString(R.string.permission_dialog_negative),
                    clickListener);
                break;

            case Manifest.permission.READ_CONTACTS:
                showChoiceDialog(getString(R.string.dialog_contacts_permission_massage),
                    getString(R.string.dialog_contacts_permission_title),
                    getString(R.string.permission_dialog_positive), getString(R.string.permission_dialog_negative),
                    clickListener);
                break;
        }
    }


    public void showChoiceDialog(String message, String title, String positiveButtonText, String negativeButtonText,
        final DialogInterface.OnClickListener clickListener) {

        new AlertDialog.Builder(getActivity()).setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText, clickListener)
            .setNegativeButton(negativeButtonText, clickListener).setCancelable(false).create().show();
    }
}
