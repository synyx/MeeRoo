package de.synyx.android.meetingroom.business.account;

import android.accounts.Account;
import android.accounts.AccountManager;

import android.content.Context;

import android.os.Bundle;

import de.synyx.android.meetingroom.config.Config;

import io.reactivex.Observable;

import static android.content.ContentResolver.requestSync;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class AccountServiceImpl implements AccountService {

    private final String CALENDAR_SYNC_AUTHORITY = "com.android.calendar";

    private Context context;

    public AccountServiceImpl(Context context) {

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

        return Config.getInstance(context).getPreferencesService().getUserAccountName();
    }


    @Override
    public String getUserAccountType() {

        return Config.getInstance(context).getPreferencesService().getUserAccountType();
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
