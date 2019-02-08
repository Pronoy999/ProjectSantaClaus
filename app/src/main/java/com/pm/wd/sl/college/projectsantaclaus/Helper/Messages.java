package com.pm.wd.sl.college.projectsantaclaus.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Messages {
    public static void l(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
