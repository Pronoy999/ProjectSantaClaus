package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.pm.wd.sl.college.projectsantaclaus.Helper.Constants;
import com.pm.wd.sl.college.projectsantaclaus.Helper.HTTPConnector;
import com.pm.wd.sl.college.projectsantaclaus.Helper.Messages;
import com.pm.wd.sl.college.projectsantaclaus.Helper.ParamsCreator;
import com.pm.wd.sl.college.projectsantaclaus.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity implements HTTPConnector.ResponseListener {

    private EditText _fName, _lName, _email, _phone;
    private MaterialButton _registerButton;
    private AppCompatButton _cancelButton;
    private ProgressDialog _progressDialog;
    private String TAG_CLASS = SignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeViews();
        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        _cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Method to initialize Views.
     */
    private void initializeViews() {
        _fName = findViewById(R.id.fName);
        _lName = findViewById(R.id.lName);
        _email = findViewById(R.id.email);
        _phone = findViewById(R.id.contactPhone);
        _registerButton = findViewById(R.id.confirmButton);
        _cancelButton = findViewById(R.id.cancelButton);
        _progressDialog = new ProgressDialog(this);
        _progressDialog.setMessage("Loading...");
        _progressDialog.setCancelable(false);
    }

    /**
     * Method to upload the data to the server.
     */
    private void getData() {
        String firstName = _fName.getText().toString();
        String lastName = _lName.getText().toString();
        String phone = _phone.getText().toString();
        String email = _email.getText().toString();
        if (!isEmpty(firstName) && !isEmpty(lastName) && !isEmpty(email) && !isEmpty(phone)) {
            String url = Constants.API_URL + "users";
            HTTPConnector connector = new HTTPConnector(this, url, this);
            connector.makeQuery(ParamsCreator.createParamsForUSerAdd(firstName, lastName, email, phone));
            _progressDialog.show();
        } else {
            Messages.toast(getApplicationContext(), "Please fill in all the details. ");
        }
    }

    /**
     * Method to check whether the data is empty or not.
     *
     * @param data: The data to be checked.
     * @return true if Empty, else false.
     */
    private boolean isEmpty(String data) {
        return TextUtils.isEmpty(data);
    }

    @Override
    public void onResponse(JSONObject response) {
        _progressDialog.dismiss();
        try {
            if (response.getBoolean(Constants.JSON_RESPONSE)) {
                Messages.toast(getApplicationContext(), "User added Successfully.");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateSharedPreferences();
                    }
                });
                thread.start();
                //TODO: Change to User Home Activity.
                startActivity(new Intent(SignUpActivity.this,
                        LoginActivity.class));
            }
        } catch (JSONException e) {
            Messages.l(TAG_CLASS, e.toString());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        _progressDialog.dismiss();
        Messages.toast(getApplicationContext(), "Something went wrong.");
        Messages.l(TAG_CLASS, error.toString());
    }

    /**
     * Method to updated the shared Preferences.
     */
    private void updateSharedPreferences() {
        SharedPreferences sharedPreference = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN, true);
        editor.apply();
    }
}
