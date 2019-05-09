package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.pm.wd.sl.college.projectsantaclaus.Helper.Constants;
import com.pm.wd.sl.college.projectsantaclaus.R;

public class SplashScreenActivity extends AppCompatActivity {
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences sharedPreferences =
                getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false);
        new Handler().postDelayed(() -> changeActivity(), 1000);

    }

    /**
     * Method to change the Activity.
     */
    private void changeActivity() {
        if (!isLoggedIn) {
            startActivity(new Intent(SplashScreenActivity.this, SignUpActivity.class));
        } else {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        }
        finish();
    }
}
