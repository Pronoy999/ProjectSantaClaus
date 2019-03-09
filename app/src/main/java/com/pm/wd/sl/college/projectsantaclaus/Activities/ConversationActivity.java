package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.pm.wd.sl.college.projectsantaclaus.Objects.ConversationAdapter;
import com.pm.wd.sl.college.projectsantaclaus.Objects.Message;
import com.pm.wd.sl.college.projectsantaclaus.R;

import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends AppCompatActivity {
    List<Message> convMessages = new ArrayList<>();
    ConversationAdapter convAdapter;

    ListView _convMsgView;

    String senderId;

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

    private void initializeViews() {
        FloatingActionButton newMsgConv = findViewById(R.id.newMsgConv);
        newMsgConv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ConversationActivity.this, NewMessageActivity.class).putExtra("receiver_id", senderId), 0x3142); // const
            }
        });

        _convMsgView = findViewById(R.id.converseMessageView);

        convAdapter = new ConversationAdapter(this, R.layout.list_item_converse_message_send, convMessages);
    }

    void fetchMessages() {
        // todo fetch messages where (senderId=senderId and receiver_id=user.id) or (senderId=user.id and receiver_id=senderId)
        // todo on response set the list with new data and notify dataset changed on convAdapter
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0x3142) { // const
            fetchMessages();
        }
    }
}
