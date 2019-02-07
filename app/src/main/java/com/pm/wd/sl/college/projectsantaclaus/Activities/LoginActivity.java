package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.pm.wd.sl.college.projectsantaclaus.R;

public class LoginActivity extends AppCompatActivity {
    private EditText _mobileNumber;
    private Button _requestOTP, _confirmButton, _cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
