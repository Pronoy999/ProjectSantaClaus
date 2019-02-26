package com.pm.wd.sl.college.projectsantaclaus.Helper;

import org.json.JSONException;
import org.json.JSONObject;

public class ParamsCreator {
    public static String TAG_CLASS = ParamsCreator.class.getSimpleName();

    public static JSONObject createParamsForOTPRequest(String phoneNumber) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.JSON_PHONE_NUMBER, phoneNumber);
        } catch (JSONException e) {
            Messages.l(TAG_CLASS, e.toString());
        }
        return jsonObject;
    }
}
