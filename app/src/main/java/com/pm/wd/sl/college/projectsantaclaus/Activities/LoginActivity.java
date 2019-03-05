package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.pm.wd.sl.college.projectsantaclaus.Helper.Constants;
import com.pm.wd.sl.college.projectsantaclaus.Helper.HTTPConnector;
import com.pm.wd.sl.college.projectsantaclaus.Helper.Messages;
import com.pm.wd.sl.college.projectsantaclaus.Helper.ParamsCreator;
import com.pm.wd.sl.college.projectsantaclaus.Objects.DigitText;
import com.pm.wd.sl.college.projectsantaclaus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements HTTPConnector.ResponseListener {
    private EditText _mobileNumber;
    private MaterialButton _confirmButton, _requestOTP;
    private Button _cancelButton, _registerButton;
    private List<DigitText> mDigitViews;
    private String pinNumber;
    private char[] mOTPChars = {Constants.DEFAULT_OTP_CHAR,
            Constants.DEFAULT_OTP_CHAR,
            Constants.DEFAULT_OTP_CHAR, Constants.DEFAULT_OTP_CHAR};
    private ConstraintLayout _otpLayout;
    private HTTPConnector connector;
    private ProgressDialog _progressDialog;
    private int currentCode;
    private String TAG_CLASS = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        _otpLayout.setVisibility(View.GONE);
        for (int i = 0; i < 4; i++) {
            final DigitText digit = mDigitViews.get(i);
            final int q = i;
            digit.addTextChangedListener(new TextWatcher() {
                int lenbef = 0;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    lenbef = s.length();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (lenbef == 0 && s.length() == 1) {
                        onDigitEntered(q);
                    }
                }
            });

            digit.setOnBackspaceListener(new DigitText.OnBackspaceListener() {
                @Override
                public void onBackspace(KeyEvent event) {
                    if (digit.getSelectionStart() == 0 && digit.getSelectionEnd() < 1) {
                        LoginActivity.this.onDigitDeleted(q - 1);
                    } else {
                        LoginActivity.this.onDigitDeleted(q);
                    }
                }
            });
        }
        _requestOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(TextUtils.isEmpty(_mobileNumber.getText().toString())) &&
                        _mobileNumber.getText().toString().length() == 10) {
                    requestOTP(_mobileNumber.getText().toString());
                }
            }
        });
        _confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinNumber = new String(mOTPChars);
                checkPin();
            }
        });
        _cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Finish.
            }
        });
        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    /**
     * Method to initialize the widgets.
     */
    private void initializeViews() {
        mDigitViews = Arrays.asList((DigitText) findViewById(R.id.pin1),
                (DigitText) findViewById(R.id.pin2),
                (DigitText) findViewById(R.id.pin3),
                (DigitText) findViewById(R.id.pin4));
        _mobileNumber = findViewById(R.id.userPhone);
        _requestOTP = findViewById(R.id.requestOTP);
        _confirmButton = findViewById(R.id.confirmOTP);
        _registerButton = findViewById(R.id.registerNew);
        _cancelButton = findViewById(R.id.cancelOTPButton);
        _otpLayout = findViewById(R.id.otpLayout);
        _progressDialog = new ProgressDialog(this);
        _progressDialog.setMessage("Loading...");
        _progressDialog.setCancelable(false);
    }

    /**
     * fired when an otp digit is entered at the specified {@code index}
     *
     * @param index the index of the entered digit
     */
    private void onDigitEntered(int index) {
        if (index > -1 && index < 4) {
            mOTPChars[index] = mDigitViews.get(index).getText().toString().charAt(0);
            if (index < 3) {
                mDigitViews.get(index + 1).requestFocus();
            } else {
                hideKeyboard(this);
                pinNumber = new String(mOTPChars);
                checkPin();
            }
        }
    }

    /**
     * fired when an otp digit is deleted from the specified {@code index}
     *
     * @param index the index of the deleted digit
     */
    private void onDigitDeleted(int index) {
        if (index > -1 && index < 4) {
            mOTPChars[index] = Constants.DEFAULT_OTP_CHAR;
            mDigitViews.get(index).setText("");
            mDigitViews.get(index).requestFocus();
        }
    }

    /**
     * hides the keyboard from the specified {@code activity}
     *
     * @param activity the activity to hide the keyboard from
     */
    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Method to check the OTP.
     */
    private void checkPin() {
        String url = Constants.API_URL + "?key=" + Constants.API_TOKEN +
                "&phoneNumber=%2B91" + _mobileNumber.getText().toString() + "&otp=" + pinNumber;
        connector = new HTTPConnector(this, url, this);
        currentCode = Constants.OTP_VERFIY_CODE;
        _progressDialog.show();
    }

    /**
     * Method to request OTP.
     *
     * @param phoneNumber: The Phone Number where the OTP is to be send.
     */
    private void requestOTP(String phoneNumber) {
        String url = Constants.API_URL + "otp?key=" + Constants.API_TOKEN;
        connector = new HTTPConnector(getApplicationContext(), url, this);
        connector.makeQuery(ParamsCreator.createParamsForOTPRequest(phoneNumber));
        _progressDialog.show();
        currentCode = Constants.OTP_REQUEST_CODE;
    }

    /**
     * Method to change the Activity.
     */
    private void changeActivity() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN, true);
        editor.apply();
        //TODO: Change the Activity after Successful OTP.
    }

    @Override
    public void onResponse(JSONObject response) {
        _progressDialog.dismiss();
        try {
            if (currentCode == Constants.OTP_REQUEST_CODE) {
                String responseText = response.getString(Constants.JSON_RESPONSE);
                if (responseText.equalsIgnoreCase("OTP Send.") ||
                        responseText.equalsIgnoreCase("New OTP Send.")) {
                    Messages.toast(getApplicationContext(), "OTP SEND.");
                } else {
                    Messages.toast(getApplicationContext(), "Something went wrong.");
                }
            } else if (currentCode == Constants.OTP_VERFIY_CODE) {
                if (response.getBoolean(Constants.JSON_RESPONSE)) {
                    changeActivity();
                } else {
                    Messages.toast(getApplicationContext(), "Please enter a valid OTP.");
                }
            }
        } catch (JSONException e) {
            Messages.l(TAG_CLASS, e.getStackTrace().toString());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        _progressDialog.dismiss();
        Messages.toast(getApplicationContext(), "Ops, Something went wrong!");
        Messages.l(TAG_CLASS, error.toString());
    }
}
