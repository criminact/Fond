package com.noobcode.fond.ViewModel;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AskingRuntimePermissions {
    private static final int PERMISSIONS = 1;
    Activity mActivity;

    public AskingRuntimePermissions(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public boolean checkPermission(String permissionName){
        if(ContextCompat.checkSelfPermission(mActivity, permissionName) == PackageManager.PERMISSION_GRANTED){
         return true;
        }
        return false;
    }


    public void askPermissions(String[] permission_to_be_asked){
        ActivityCompat.requestPermissions(mActivity,
                permission_to_be_asked,
                PERMISSIONS);
    }
}
