package de.synyx.android.meetingroom.util.proxy;

import android.Manifest;

import android.app.Activity;

import android.content.Context;

import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Julia Dasch - dasch@synyx.de
 */
public class PermissionManager {

    private final Context context;
    private List<String> neededPermissions;

    public PermissionManager(Context context) {

        this.context = context;
    }

    public boolean isPermissionGranted(String permission) {

        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public void requestPermission(Fragment fragment, String[] permissionsToRequest, int requestCode) {

        fragment.requestPermissions(permissionsToRequest, requestCode);
    }


    public void requestPermission(Activity activity, String[] permissionToRequest, int requestCode) {

        ActivityCompat.requestPermissions(activity, permissionToRequest, requestCode);
    }


    public boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {

        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }


    public boolean shouldShowRequestPermissionRationale(Fragment fragment, String permission) {

        return fragment.shouldShowRequestPermissionRationale(permission);
    }


    public List<String> getNeededPermissions() {

        neededPermissions = new ArrayList<>();

        if (!isPermissionGranted(Manifest.permission.WRITE_CALENDAR)) {
            neededPermissions.add(Manifest.permission.WRITE_CALENDAR);
        }

        if (!isPermissionGranted(Manifest.permission.READ_CALENDAR)) {
            neededPermissions.add(Manifest.permission.READ_CALENDAR);
        }

        if (!isPermissionGranted(Manifest.permission.GET_ACCOUNTS)) {
            neededPermissions.add(Manifest.permission.GET_ACCOUNTS);
        }

        if (!isPermissionGranted(Manifest.permission.READ_SYNC_SETTINGS)) {
            neededPermissions.add(Manifest.permission.READ_SYNC_SETTINGS);
        }

        if (!isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
            neededPermissions.add(Manifest.permission.READ_CONTACTS);
        }

        return neededPermissions;
    }
}
