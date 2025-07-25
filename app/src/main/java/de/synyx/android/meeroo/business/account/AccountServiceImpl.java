package de.synyx.android.meeroo.business.account;

import android.accounts.Account;
import android.accounts.AccountManager;

import android.content.Context;

import android.os.Bundle;

import de.synyx.android.meeroo.preferences.PreferencesService;
import io.reactivex.rxjava3.core.Observable;

import static android.content.ContentResolver.requestSync;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class AccountServiceImpl implements AccountService {

    private static final String CALENDAR_SYNC_AUTHORITY = "com.android.calendar";

    private final PreferencesService preferencesService;
    private final Context context;

    public AccountServiceImpl(PreferencesService preferencesService, Context context) {

        this.preferencesService = preferencesService;
        this.context = context;
    }

    @Override
    public String[] getAccountNames() {

        Account[] accounts = AccountManager.get(context).getAccounts();

        return Observable.fromArray(accounts)
            .map(account -> account.name)
            .toList(accounts.length)
            .blockingGet()
            .toArray(new String[accounts.length]);
    }


    @Override
    public String getUserAccountName() {

        return preferencesService.getUserAccountName();
    }


    @Override
    public String getUserAccountType() {

        return preferencesService.getUserAccountType();
    }


    @Override
    public void syncCalendar() {

        String userAccountName = getUserAccountName();
        Account[] accounts = AccountManager.get(context).getAccounts();

        Observable.fromArray(accounts)
            .filter(account -> account.name.equals(userAccountName))
            .firstElement()
            .subscribe(account -> requestSync(account, CALENDAR_SYNC_AUTHORITY, new Bundle()));
    }
}
