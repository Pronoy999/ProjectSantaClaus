package com.pm.wd.sl.college.projectsantaclaus.Helper;

import com.pm.wd.sl.college.projectsantaclaus.Objects.Message;

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
    public static JSONObject createParamsForUserAdd(String firstName, String lastName, String email, String phone) {
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

    /**
     * Method to make the Params for fetching the recent Messages.
     *
     * @param emailID: The Email Id of the owner.
     * @return jsonObject.
     */
    public static JSONObject createParamsForRecentMessages(String emailID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.EMAIL, emailID);
        } catch (JSONException e) {
            e.printStackTrace();
            Messages.l(TAG_CLASS, e.toString());
        }
        return jsonObject;
    }

    /**
     * Method to create params for New Message.
     *
     * @param msg:           The Message that is to be send.
     * @param receiverEmail: The email of the receiver.
     * @param senderEmail:   The Sender Email.
     * @param url:           The URL of the Media.
     * @return jsonObject.
     */
    public static JSONObject createParamsForNewMessage(String msg, String receiverEmail, String senderEmail, String url) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.MESSAGE, msg);
            jsonObject.put(Constants.RECEIVER_EMAIL, receiverEmail);
            jsonObject.put(Constants.SENDER_EMAIl, senderEmail);
            jsonObject.put(Constants.MESSAGE_URL, url);
        } catch (JSONException e) {
            e.printStackTrace();
            Messages.l(TAG_CLASS, e.toString());
        }
        return jsonObject;
    }

    /**
     * Method to create params for getting conversation between 2 user.
     *
     * @param senderEmail:   The Sender Email.
     * @param receiverEmail: The receiver Email.
     * @return jsonObject.
     */
    public static JSONObject createParamsForConversation(String senderEmail, String receiverEmail) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.SENDER_EMAIl, senderEmail);
            jsonObject.put(Constants.RECEIVER_EMAIL, receiverEmail);
        } catch (JSONException e) {
            Messages.l(TAG_CLASS, e.toString());
        }
        return jsonObject;
    }
}
