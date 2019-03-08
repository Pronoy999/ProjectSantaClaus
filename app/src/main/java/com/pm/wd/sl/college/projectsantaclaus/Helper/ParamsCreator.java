package com.pm.wd.sl.college.projectsantaclaus.Helper;

import org.json.JSONException;
import org.json.JSONObject;

public class ParamsCreator {
    public static String TAG_CLASS = ParamsCreator.class.getSimpleName();

    /**
     * Method to request OTP.
     *
     * @param phoneNumber: The phone Number.
     * @return jsonObject.
     */
    public static JSONObject createParamsForOTPRequest(String phoneNumber) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.JSON_PHONE_NUMBER, phoneNumber);
        } catch (JSONException e) {
            Messages.l(TAG_CLASS, e.toString());
        }
        return jsonObject;
    }

    /**
     * Creating the params for Adding USers.
     *
     * @param firstName: the first name.
     * @param lastName:  the last name.
     * @param email:     the email.
     * @param phone:     the phone.
     * @return jsonObject.
     */
    public static JSONObject createParamsForUSerAdd(String firstName, String lastName, String email, String phone) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FIRST_NAME, firstName);
            jsonObject.put(Constants.LAST_NAME, lastName);
            jsonObject.put(Constants.EMAIL, email);
            jsonObject.put(Constants.PHONE, phone);
        } catch (JSONException e) {
            Messages.l(TAG_CLASS, e.toString());
        }
        return jsonObject;
    }
}
