package com.pm.wd.sl.college.projectsantaclaus.Helper;

import com.amazonaws.regions.Regions;

public class Constants {
    //public static final String API_URL = "http://ec2-13-127-208-122.ap-south-1.compute.amazonaws.com/api/v1/";
    public static final String API_URL = "http://192.168.0.169:7069/";
    public static String API_TOKEN = "v15xbg5qscb7uy4e";
    public static final char DEFAULT_OTP_CHAR = ' ';
    public static final String SHARED_PREFERENCE_NAME = "fuckBH";
    public static final String IS_LOGGED_IN = "isLoggedIn";

    public static final int OTP_REQUEST_CODE = 1;
    public static final int OTP_VERFIY_CODE = 2;
    public static final int NEW_MESSAGE_ACTIVITY_CODE = 3;
    public static final int NEW_CONVO_MSG_CODE = 4;
    /**
     * JSON Constants.
     */
    public static final String JSON_PHONE_NUMBER = "phoneNumber";
    public static final String JSON_RESPONSE = "res";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String JSON_ID = "id";
    public static final String SENDER_EMAIl = "sender_email";
    public static final String RECEIVER_EMAIL = "receiver_email";
    public static final String MESSAGE = "message";
    public static final String MESSAGE_URL = "url";
    public static final String MESSAGE_DATE = "message_date";
    public static final String MESSAGE_TIME = "message_time";

    /**
     * AWS Constants.
     */
    public static final String AWS_BUCKET_NAME = "santa-claus";
    public static final String AWS_COGNITO_POOL_ID = "ap-south-1:af502823-ceca-4eb0-bea6-62ca289aa19a";
    public static final String AWS_REGION = Regions.AP_SOUTH_1.getName();
    public static final String AWS_STATE_COMPLETED = "COMPLETED";
}
