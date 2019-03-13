package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pm.wd.sl.college.projectsantaclaus.Objects.Message;
import com.pm.wd.sl.college.projectsantaclaus.Adapters.MessageAdapter;
import com.pm.wd.sl.college.projectsantaclaus.Objects.MsgApp;
import com.pm.wd.sl.college.projectsantaclaus.R;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    List<Message> userMsgs = new ArrayList<>();
    MessageAdapter userMsgsAdapter;

    FloatingActionButton _newMsgButton;
    ListView _msgsView;

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

    private void initializeViews() {
        _newMsgButton = findViewById(R.id.newMsgButton);
        _msgsView = findViewById(R.id.messagesView);
        userMsgsAdapter = new MessageAdapter(this, R.layout.list_item_message, userMsgs);

        _newMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MessagesActivity.this, NewMessageActivity.class), 0x3142); // const
            }
        });

        _msgsView.setAdapter(userMsgsAdapter);
        _msgsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message msg = userMsgsAdapter.getItem(position);

                if (msg != null) {
                    String userId = msg.getSendrUid().equalsIgnoreCase(MsgApp.instance().user.getEmail()) ? msg.getRecvrUid() : msg.getSendrUid();

                    Intent intent = new Intent(MessagesActivity.this, ConversationActivity.class).putExtra("senderId", userId); // const

                    startActivity(intent);
                }
            }
        });
    }

    void fetchMessages() {
        // todo fetch latest messages with (unique senderId and receiver_id=user.id) or (unique receiver_id and senderId=user.id)
        // todo on response set the list with new data and notify dataset changed on userMsgsAdapter
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0x3142) { // const
            fetchMessages();
        }
    }
}
