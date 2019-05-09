package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.pm.wd.sl.college.projectsantaclaus.Adapters.ConversationAdapter;
import com.pm.wd.sl.college.projectsantaclaus.Helper.Constants;
import com.pm.wd.sl.college.projectsantaclaus.Helper.HTTPConnector;
import com.pm.wd.sl.college.projectsantaclaus.Helper.Messages;
import com.pm.wd.sl.college.projectsantaclaus.Helper.ParamsCreator;
import com.pm.wd.sl.college.projectsantaclaus.Objects.Message;
import com.pm.wd.sl.college.projectsantaclaus.Objects.MsgApp;
import com.pm.wd.sl.college.projectsantaclaus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends AppCompatActivity implements HTTPConnector.ResponseListener {
    private List<Message> convMessages = new ArrayList<>();
    private ConversationAdapter convAdapter;
    private ListView _convMsgView;
    private ProgressDialog _progressDialog;

    private String senderId, TAG_CLASS = ConversationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeViews();
        if (getIntent() != null) {
            senderId = getIntent().getStringExtra("senderId"); // const

            if (senderId != null) {

                fetchMessages();
            }
        }
    }

    /**
     * Method to initialize Views.
     */
    private void initializeViews() {
        FloatingActionButton newMsgConv = findViewById(R.id.newMsgConv);
        newMsgConv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ConversationActivity.this,
                                NewMessageActivity.class).putExtra("other_person_id", senderId),
                        Constants.NEW_CONVO_MSG_CODE); // const
            }
        });
        _progressDialog = new ProgressDialog(this);
        _progressDialog.setMessage("Loading...");
        _progressDialog.setCancelable(false);
        _convMsgView = findViewById(R.id.converseMessageView);

        convAdapter = new ConversationAdapter(this, R.layout.list_item_converse_message_send, convMessages);
    }

    /**
     * Method to fetch the messages between 2 users.
     */
    private void fetchMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Constants.API_URL + "message/get";
                HTTPConnector connector = new HTTPConnector(ConversationActivity.this, url,
                        ConversationActivity.this);
                connector.makeQuery(ParamsCreator
                        .createParamsForConversation(MsgApp.instance().user.getEmail(), senderId));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _progressDialog.show();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == Constants.NEW_CONVO_MSG_CODE) { // const
            fetchMessages();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        _progressDialog.dismiss();
        try {
            convMessages.clear();
            JSONArray array = response.getJSONArray(Constants.JSON_RESPONSE);
            for (int i = 0; i < array.length(); i++) {
                JSONObject oneMessage = array.getJSONObject(i);
                Message message = new Message(oneMessage.getInt(Constants.JSON_ID),
                        oneMessage.getString(Constants.SENDER_EMAIl),
                        oneMessage.getString(Constants.RECEIVER_EMAIL),
                        oneMessage.getString(Constants.MESSAGE),
                        oneMessage.getString(Constants.MESSAGE_URL),
                        oneMessage.getString(Constants.MESSAGE_DATE),
                        oneMessage.getString(Constants.MESSAGE_TIME));
                convMessages.add(message);
            }
            convAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Messages.toast(this, "Ops, Something went wrong.");
            Messages.l(TAG_CLASS, e.toString());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        _progressDialog.dismiss();
        Messages.toast(this, "Ops, Something went wrong.");
    }
}
