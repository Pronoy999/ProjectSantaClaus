package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pm.wd.sl.college.projectsantaclaus.R;

public class SignUpActivity extends AppCompatActivity {

    EditText fName;

    EditText lName;

    EditText email;

    EditText phone;

    EditText username;

    Button register_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeViews();
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeViews() {
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        email = findViewById(R.id.contactEmail);
        phone = findViewById(R.id.contactPhone);
        username = findViewById(R.id.userName);
        register_btn = findViewById(R.id.register_btn);
    }
}
