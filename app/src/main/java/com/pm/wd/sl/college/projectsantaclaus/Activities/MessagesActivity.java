package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.pm.wd.sl.college.projectsantaclaus.Helper.Constants;
import com.pm.wd.sl.college.projectsantaclaus.Helper.HTTPConnector;
import com.pm.wd.sl.college.projectsantaclaus.Helper.Messages;
import com.pm.wd.sl.college.projectsantaclaus.Helper.ParamsCreator;
import com.pm.wd.sl.college.projectsantaclaus.Objects.Message;
import com.pm.wd.sl.college.projectsantaclaus.Adapters.MessageAdapter;
import com.pm.wd.sl.college.projectsantaclaus.Objects.MsgApp;
import com.pm.wd.sl.college.projectsantaclaus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity implements HTTPConnector.ResponseListener {
    private List<Message> userMsgs = new ArrayList<>();
    private MessageAdapter userMsgsAdapter;
    private FloatingActionButton _newMsgButton;
    private ListView _msgsView;
    private ProgressDialog _progressDialog;
    private String TAG_CLASS = MessagesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeViews();
        if (MsgApp.instance().user != null) {
            fetchMessages();
        }
    }

    /**
     * Method to initialize Views.
     */
    private void initializeViews() {
        _newMsgButton = findViewById(R.id.newMsgButton);
        _msgsView = findViewById(R.id.messagesView);
        userMsgsAdapter = new MessageAdapter(this, R.layout.list_item_message, userMsgs);
        _progressDialog = new ProgressDialog(this);
        _progressDialog.setMessage("Loading...");
        _progressDialog.setCancelable(false);
        _newMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MessagesActivity.this,
                        NewMessageActivity.class), Constants.NEW_MESSAGE_ACTIVITY_CODE);
            }
        });

        _msgsView.setAdapter(userMsgsAdapter);
        _msgsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message msg = userMsgsAdapter.getItem(position);
                if (msg != null) {
                    String userId = msg.getSendrUid()
                            .equalsIgnoreCase(MsgApp.instance().user.getEmail()) ?
                            msg.getRecvrUid() : msg.getSendrUid();

                    Intent intent = new Intent(MessagesActivity.this,
                            ConversationActivity.class).putExtra("senderId", userId); // const
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Method to fetch the recent messages.
     */
    private void fetchMessages() {
        String url = Constants.API_URL + "message/recent";
        HTTPConnector connector = new HTTPConnector(getApplicationContext(), url, this);
        connector.makeQuery(ParamsCreator.createParamsForRecentMessages(MsgApp.instance().user.getEmail()));
        _progressDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == Constants.NEW_MESSAGE_ACTIVITY_CODE) {
            fetchMessages();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        _progressDialog.dismiss();
        try {
            userMsgs.clear();
            JSONArray array = response.getJSONArray(Constants.JSON_RESPONSE);
            for (int i = 0; i < array.length(); i++) {
                JSONObject oneMessage = array.getJSONObject(i);
                Message message = new Message(oneMessage.getInt(Constants.JSON_ID), oneMessage.getString(Constants.SENDER_EMAIl),
                        oneMessage.getString(Constants.RECEIVER_EMAIL),
                        oneMessage.getString(Constants.MESSAGE),
                        oneMessage.getString(Constants.MESSAGE_URL),
                        oneMessage.getString(Constants.MESSAGE_DATE),
                        oneMessage.getString(Constants.MESSAGE_TIME));
                userMsgs.add(message);
            }
            userMsgsAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Messages.toast(getApplicationContext(), "Ops, Something went wrong!");
            Messages.l(TAG_CLASS, e.toString());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        _progressDialog.dismiss();
        error.printStackTrace();
        Messages.toast(getApplicationContext(), "Ops, Something went wrong!");
    }
}
