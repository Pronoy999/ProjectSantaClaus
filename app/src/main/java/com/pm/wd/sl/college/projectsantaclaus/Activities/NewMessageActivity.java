package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.pm.wd.sl.college.projectsantaclaus.Helper.FileUtils;
import com.pm.wd.sl.college.projectsantaclaus.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.zip.CRC32;

public class NewMessageActivity extends AppCompatActivity {
    EditText _toReceiverEdit;
    EditText _newMsgEditText;
    ImageView _newMsgImageView;
    ImageView _newMsgSendButton;

    String imageFileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        initializeViews();
    }

    private void initializeViews() {
        _toReceiverEdit = findViewById(R.id.toReceiverEdit);
        _newMsgEditText = findViewById(R.id.newMsgEditText);
        _newMsgImageView = findViewById(R.id.newMsgImageView);
        _newMsgSendButton = findViewById(R.id.newMsgSendButton);

        String toRecv;
        if (getIntent() != null && (toRecv = getIntent().getStringExtra("to_receiver_id")) != null) {
            _toReceiverEdit.setText(toRecv); // const
        }

        _newMsgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(FileUtils.newOpenImageIntent(false), 0xef54);
            }
        });

        _newMsgSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_newMsgEditText.getText().toString().isEmpty() /*|| todo no image chosen*/) {
                    // todo show error;
                    return;
                }

                // todo encode image and then send image and message
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                final Uri uri = data.getData();
                if (uri != null) {
                    // todo show shitty loading graphics
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String filename = String.format(Locale.getDefault(), "temp_%d", System.currentTimeMillis());
                            try (ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "r")
                            ) {
                                if (pfd != null) {
                                    File outputFile = new File(getFilesDir(), filename);
                                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                                        FileInputStream fis = new FileInputStream(pfd.getFileDescriptor());

                                        CRC32 crc = new CRC32();

                                        int totalLen = fis.available();

                                        int bytesRead, len;

                                        byte[] buffer = new byte[8192]; // const

                                        for (len = 0; (bytesRead = fis.read(buffer, 0, 8192)) != -1; len += bytesRead) { // const
                                            fos.write(buffer, 0, bytesRead);
                                            crc.update(buffer, 0, bytesRead);
                                        }

                                        fos.flush();
                                        imageFileName = String.format(Locale.getDefault(), "%d", crc.getValue());
                                        outputFile.renameTo(new File(outputFile.getParentFile(), imageFileName));
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                // todo log show error
                            }
                        }
                    }).start();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
