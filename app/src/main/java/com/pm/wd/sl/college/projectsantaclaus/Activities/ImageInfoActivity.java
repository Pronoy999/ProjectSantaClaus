package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pm.wd.sl.college.projectsantaclaus.Helper.Messages;
import com.pm.wd.sl.college.projectsantaclaus.Objects.Message;
import com.pm.wd.sl.college.projectsantaclaus.R;

public class ImageInfoActivity extends AppCompatActivity {
    private TextView checksumValue;
    private TextView senderValue;
    private TextView receiverValue;
    private TextView originalSizeValue;
    private TextView compressedSizeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_info);
        this.setFinishOnTouchOutside(false);
        Message msg;

        if (getIntent() == null ||
                (msg = getIntent().getParcelableExtra("param_message")) == null) {
            Messages.toast(this, "Could not find message!");
            finish();
            return;
        }

        initializeViews(msg);
    }

    private void initializeViews(Message msg) {
        checksumValue = findViewById(R.id.checksumValue);
        senderValue = findViewById(R.id.senderValue);
        receiverValue = findViewById(R.id.receiverValue);
        originalSizeValue = findViewById(R.id.originalSizeValue);
        compressedSizeValue = findViewById(R.id.compressedSizeValue);

        checksumValue.setText(msg.getUrl().substring(msg.getUrl().lastIndexOf("/") + 1));
        senderValue.setText(msg.getSendrUid()); // todo from watermark
        receiverValue.setText(msg.getRecvrUid()); // todo from watermark
        originalSizeValue.setText(msg.getOriginalSize());
        compressedSizeValue.setText(msg.getCompressedSize());
    }
}
