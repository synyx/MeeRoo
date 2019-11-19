package de.synyx.android.meeroo.screen.login.mvvm;

import android.accounts.AccountManager;

import android.content.Intent;

import android.content.pm.PackageManager;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.config.Registry;

import static android.app.Activity.RESULT_OK;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class MVVMLoginFragment extends Fragment {

    static final int PERMISSION_REQUEST_CODE = 1602;
    private static final int REQUEST_ACCOUNT = 139;
    private MVVMLoginViewModel viewModel;

    private TextView step2;
    private TextView stepAccount;
    private TextView step3;
    private TextView stepMode;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        step2 = view.findViewById(R.id.step_2);
        stepAccount = view.findViewById(R.id.step_account);
        step3 = view.findViewById(R.id.step_3);
        stepMode = view.findViewById(R.id.step_mode);

        progressBar = view.findViewById(R.id.login_progress_bar);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this, Registry.get(MVVMLoginViewModelFactory.class))
                .get(MVVMLoginViewModel.class);

        viewModel.getLoginStep().observe(this, this::onLoginStepChanged);
    }


    private void onLoginStepChanged(LoginStep loginStep) {

        if (LoginStep.PERMISSIONS == loginStep) {
            setAccountStepEnabled(false);
            setModeStepEnabled(false);
            viewModel.askForPermissions(this);
        } else if (LoginStep.ACCOUNT == loginStep) {
            setAccountStepEnabled(true);
            setModeStepEnabled(false);
        } else if (LoginStep.MODE == loginStep) {
            setAccountStepEnabled(true);
            setModeStepEnabled(true);
            displayProgress(false);
            displayModeSelection();
        }
    }


    private void displayModeSelection() {
    }


    private void displayProgress(boolean visible) {

        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    private void setModeStepEnabled(boolean enabled) {

        step3.setEnabled(enabled);
        stepMode.setEnabled(enabled);
    }


    private void setAccountStepEnabled(boolean enabled) {

        step2.setEnabled(enabled);
        stepAccount.setEnabled(enabled);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length == 0) {
                // the dialog was cancelled, e.g. orientation change
                return;
            }

            for (int i = 0; i < permissions.length; i++) {
                if (PackageManager.PERMISSION_DENIED == grantResults[i]) {
                    onPermissionDenied(permissions[i]);

                    return;
                }
            }

            onAllPermissionsGranted();
        }
    }


    private void onPermissionDenied(String permission) {

        // TODO implement
    }


    private void onAllPermissionsGranted() {

        viewModel.getLoginStep().postValue(LoginStep.ACCOUNT);

        Intent accountIntent = AccountManager.newChooseAccountIntent(null, null, null, null, null, null, null);
        startActivityForResult(accountIntent, REQUEST_ACCOUNT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_ACCOUNT && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra("authAccount");

            viewModel.saveAccount(accountName);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
