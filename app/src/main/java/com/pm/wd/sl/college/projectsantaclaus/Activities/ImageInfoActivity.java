package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pm.wd.sl.college.projectsantaclaus.Helper.Messages;
import com.pm.wd.sl.college.projectsantaclaus.Objects.Message;
import com.pm.wd.sl.college.projectsantaclaus.R;

import java.text.DecimalFormat;

public class ImageInfoActivity extends AppCompatActivity {
    private TextView checksumValue;
    private TextView senderValue;
    private TextView receiverValue;
    private TextView originalSizeValue;
    private TextView compressedSizeValue;

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_info);
        this.setFinishOnTouchOutside(true);
        this.setTitle("Image Information");
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
        originalSizeValue.setText(readableFileSize(msg.getOriginalSize()));
        compressedSizeValue.setText(readableFileSize(msg.getCompressedSize()));
    }
}
