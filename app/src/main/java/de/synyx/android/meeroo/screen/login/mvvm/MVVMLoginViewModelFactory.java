package de.synyx.android.meeroo.screen.login.mvvm;

import androidx.annotation.NonNull;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class MVVMLoginViewModelFactory implements ViewModelProvider.Factory {

    private final MVVMLoginConfig loginConfig;

    public MVVMLoginViewModelFactory(MVVMLoginConfig loginConfig) {

        this.loginConfig = loginConfig;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return modelClass.cast(new MVVMLoginViewModel(loginConfig));
    }
}
