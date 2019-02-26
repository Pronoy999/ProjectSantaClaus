package com.pm.wd.sl.college.projectsantaclaus.Helper;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pm.wd.sl.college.projectsantaclaus.Objects.SingleTon;

import org.json.JSONObject;

public class HTTPConnector {
    private String TAG = HTTPConnector.class.getSimpleName();
    private Context context;
    private String queryURL;
    private ResponseListener responseListener;

    public interface ResponseListener {
        void onResponse(JSONObject response);

        void onErrorResponse(VolleyError error);
    }

    public HTTPConnector(Context context, String queryURL, ResponseListener responseListener) {
        this.context = context;
        this.queryURL = queryURL;
        this.responseListener = responseListener;
    }

    /**
     * Method to make the HTTP POST Request.
     */
    public void makeQuery(JSONObject postData) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, queryURL, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (responseListener != null) {
                    responseListener.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (responseListener != null) {
                    responseListener.onErrorResponse(error);
                }
            }
        });
        request.setTag(TAG);
        SingleTon.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * Method to make a HTTP GET Request.
     */
    public void makeQuery() {
        JsonObjectRequest request = new JsonObjectRequest(queryURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (responseListener != null) {
                    responseListener.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (responseListener != null) {
                    responseListener.onErrorResponse(error);
                }
            }
        });
        request.setTag(TAG);
        SingleTon.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * Method to make a PUT Request.
     *
     * @param postData: The post body.
     * @param isPut:    true to make a out request.
     */
    public void makeQuery(JSONObject postData, boolean isPut) {
        if (isPut) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, queryURL, postData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (responseListener != null) {
                        responseListener.onResponse(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (responseListener != null) {
                        responseListener.onErrorResponse(error);
                    }
                }
            });
            SingleTon.getInstance(context).addToRequestQueue(request);
        }
    }
}
