package de.synyx.android.meeroo.screen.login;

import android.accounts.AccountManager;

import android.content.Intent;

import android.content.pm.PackageManager;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.domain.CalendarMode;

import static android.app.Activity.RESULT_OK;
import static de.synyx.android.meeroo.domain.CalendarMode.CALENDAR;
import static de.synyx.android.meeroo.domain.CalendarMode.RESOURCES;


/**
 * @author Julian Heetel - heetel@synyx.de
 */
public class LoginFragment extends Fragment {

    static final int PERMISSION_REQUEST_CODE = 1602;
    static final int REQUEST_ACCOUNT = 139;
    private LoginViewModel viewModel;

    private TextView step2;
    private TextView stepAccount;
    private TextView step3;
    private TextView stepMode;
    private ProgressBar progressBar;
    private TextView errorText;
    private Button errorExitButton;
    private Button errorRetryButton;
    private ViewGroup modeSelectionForm;
    private RadioGroup modeSelectionRadioGroup;
    private Button modeSelectionConfirmButton;

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

        errorText = view.findViewById(R.id.login_error_text);
        errorExitButton = view.findViewById(R.id.login_error_exit_button);
        errorRetryButton = view.findViewById(R.id.login_error_retry_button);
        errorExitButton.setOnClickListener(this::onExitClicked);
        errorRetryButton.setOnClickListener(this::onRetryClicked);

        modeSelectionForm = view.findViewById(R.id.login_select_mode_form);
        modeSelectionRadioGroup = view.findViewById(R.id.login_mode_selection_radio_group);
        modeSelectionRadioGroup.setOnCheckedChangeListener(this::onCalendarModeSelectionChanged);
        modeSelectionConfirmButton = view.findViewById(R.id.login_mode_selection_confirm_button);
        modeSelectionConfirmButton.setOnClickListener(this::onModeConfirmClicked);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity(), Registry.get(LoginViewModelFactory.class)).get(LoginViewModel.class);

        viewModel.getLoginStep().observe(getViewLifecycleOwner(), this::onLoginStepChanged);
    }


    private void startAccountSelection() {

        Intent accountIntent = AccountManager.newChooseAccountIntent(null, null, null, null, null, null, null);
        startActivityForResult(accountIntent, LoginFragment.REQUEST_ACCOUNT);
    }


    private void onRetryClicked(View view) {

        displayProgress(true);
        displayErrorRetry(false);

        // post same value to retry step
        viewModel.getLoginStep().postValue(viewModel.getLoginStep().getValue());
    }


    private void onExitClicked(View view) {

        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void onCalendarModeSelectionChanged(RadioGroup radioGroup, int id) {

        CalendarMode selectedMode = R.id.login_mode_resources_radio_button == id
                ? RESOURCES : CALENDAR;
        viewModel.getSelectedCalendarMode().setValue(selectedMode);
        modeSelectionConfirmButton.setEnabled(true);
    }

    private void onModeConfirmClicked(View view) {

        viewModel.saveCalendarMode();
    }


    private void onLoginStepChanged(LoginStep loginStep) {

        if (LoginStep.PERMISSIONS == loginStep) {
            enableStepAccount(false);
            enableStepMode(false);
            viewModel.askForPermissions(this);
        } else if (LoginStep.ACCOUNT == loginStep) {
            enableStepAccount(true);
            enableStepMode(false);
            viewModel.stepAccount(this::startAccountSelection);
        } else if (LoginStep.MODE == loginStep) {
            enableStepAccount(true);
            enableStepMode(true);
            displayProgress(false);
            viewModel.stepMode();
            displayModeSelection();
        } else if (LoginStep.FINISHED == loginStep) {
            onLoginFinished();
        }
    }


    private void displayModeSelection() {

        modeSelectionForm.setVisibility(View.VISIBLE);
    }


    private void displayProgress(boolean visible) {

        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    private void enableStepAccount(boolean enabled) {

        step2.setEnabled(enabled);
        stepAccount.setEnabled(enabled);
    }


    private void enableStepMode(boolean enabled) {

        step3.setEnabled(enabled);
        stepMode.setEnabled(enabled);
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
                    onPermissionDenied();

                    return;
                }
            }

            onAllPermissionsGranted();
        }
    }


    private void onPermissionDenied() {

        displayProgress(false);
        displayErrorRetry(true);
    }


    private void displayErrorRetry(boolean visible) {

        errorText.setVisibility(visible ? View.VISIBLE : View.GONE);
        errorExitButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        errorRetryButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    private void onAllPermissionsGranted() {

        viewModel.getLoginStep().postValue(LoginStep.ACCOUNT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_ACCOUNT && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra("authAccount");

            viewModel.saveAccount(accountName);
        } else if (requestCode == REQUEST_ACCOUNT) {
            displayProgress(false);
            displayErrorRetry(true);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void onLoginFinished() {

        if (getActivity() != null) {
            ((LoginActivity) getActivity()).startMainActivity();
        }
    }
}
