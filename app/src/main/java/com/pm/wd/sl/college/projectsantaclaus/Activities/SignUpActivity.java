package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pm.wd.sl.college.projectsantaclaus.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.fName)
    EditText fName;
    @BindView(R.id.lName)
    EditText lName;
    @BindView(R.id.contactEmail)
    EditText email;
    @BindView(R.id.contactPhone)
    EditText phone;
    @BindView(R.id.userName)
    EditText username;
    @BindView(R.id.register_btn)
    Button register_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
