package com.pm.wd.sl.college.projectsantaclaus.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.pm.wd.sl.college.projectsantaclaus.Helper.Constants;
import com.pm.wd.sl.college.projectsantaclaus.Helper.FileTransferHelper;
import com.pm.wd.sl.college.projectsantaclaus.Helper.FileUtils;
import com.pm.wd.sl.college.projectsantaclaus.Helper.HTTPConnector;
import com.pm.wd.sl.college.projectsantaclaus.Helper.LSBWatermarkUtils;
import com.pm.wd.sl.college.projectsantaclaus.Helper.Messages;
import com.pm.wd.sl.college.projectsantaclaus.Helper.ParamsCreator;
import com.pm.wd.sl.college.projectsantaclaus.Objects.MsgApp;
import com.pm.wd.sl.college.projectsantaclaus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.zip.CRC32;

public class NewMessageActivity extends AppCompatActivity implements FileTransferHelper.TransferListener, HTTPConnector.ResponseListener {
    private EditText _toReceiverEdit;
    private EditText _newMsgEditText;
    private ImageView _newMsgImageView;
    private ImageView _newMsgSendButton;

    private String imageFileName = "";
    private ProgressDialog _progressDialog;
    private String TAG_CLASS = NewMessageActivity.class.getSimpleName();

    private CircularProgressDrawable cpd;

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
        _progressDialog = new ProgressDialog(this);
        _progressDialog.setMessage("Loading...");
        _progressDialog.setCancelable(false);

        cpd = new CircularProgressDrawable(this);
        cpd.setStrokeWidth(2);
        cpd.setCenterRadius(5.0f);
        cpd.start();

        boolean isReceived = false;

        if (getIntent() != null) {
            String otherPersonId;
            if ((otherPersonId = getIntent().getStringExtra("other_person_id")) != null) {
                _toReceiverEdit.setText(otherPersonId); // const
            }

            isReceived = getIntent().getBooleanExtra("is_received_msg", false);
        }

        if (!isReceived) {
            _newMsgImageView.setOnClickListener(v -> startActivityForResult(FileUtils.newOpenImageIntent(false), 0xef54));

            _newMsgSendButton.setOnClickListener(v -> {
                if (_newMsgEditText.getText().toString().isEmpty() /*|| todo no image chosen*/) {
                    Messages.toast(getApplicationContext(), "Enter a message to continue.");
                    return;
                }
                // todo encode image
                uploadFileAndSendMessage(imageFileName);
                setResult(RESULT_OK);
                finish();
            });
        } else {
            _newMsgSendButton.setVisibility(View.GONE);
            _toReceiverEdit.setHint("From:");
            _toReceiverEdit.setFocusable(false);
            _newMsgEditText.setFocusable(false);


            // todo download and set image to new message using glide.
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                final Uri uri = data.getData();
                if (uri != null) {
                    _progressDialog.show();
                    new Thread(() -> {
                        String filename = String.format(Locale.getDefault(),
                                "temp_%d", System.currentTimeMillis());
                        try (ParcelFileDescriptor pfd = getContentResolver()
                                .openFileDescriptor(uri, "r")) {
                            if (pfd != null) {
                                File outputFile = new File(getFilesDir(), filename);
                                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                                    FileInputStream fis = new FileInputStream(pfd.getFileDescriptor());

                                    CRC32 crc = new CRC32();

                                    int totalLen = fis.available();

                                    int bytesRead, len;

                                    byte[] buffer = new byte[8192]; // const

                                    for (len = 0; (bytesRead = fis.read(buffer, 0, 8192))
                                            != -1; len += bytesRead) { // const
                                        fos.write(buffer, 0, bytesRead);
                                        crc.update(buffer, 0, bytesRead);
                                    }

                                    fos.flush();

                                    imageFileName = String.format(Locale.getDefault(),
                                            "%d", crc.getValue());
                                    final File imageFile = new File(outputFile.getParentFile(),
                                            imageFileName);
                                    outputFile.renameTo(imageFile);

//                                    imageFileName = imageFile.getName();
                                    imageFileName = imageFile.getAbsolutePath();
                                    runOnUiThread(() -> _progressDialog.dismiss());
                                    _newMsgImageView.post(() -> Glide.with(NewMessageActivity.this).load(imageFile)
                                            .placeholder(cpd).into(_newMsgImageView));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> _progressDialog.dismiss());
                            Messages.toast(getApplicationContext(), "Something went wrong!");
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

    /**
     * Method to upload the Image to S3 and send the message.
     *
     * @param filePath: The File Location of the image.
     */
    private void uploadFileAndSendMessage(final String filePath) {
        new Thread(() -> {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);

            bitmap = LSBWatermarkUtils.watermark(bitmap, MsgApp.instance().user.getEmail());

            try (FileOutputStream fos2 = new FileOutputStream(filePath)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileTransferHelper fileTransferHelper = new FileTransferHelper(NewMessageActivity.this,
                    filePath, NewMessageActivity.this);
            String[] fParts = filePath.split(File.separator);
            String fPP = fParts[fParts.length - 1];
            fileTransferHelper.upload(fPP);
            String url = Constants.API_URL + "message/new";
            HTTPConnector connector = new HTTPConnector(NewMessageActivity.this, url,
                    NewMessageActivity.this);
            String receiverEmail = _toReceiverEdit.getText().toString();
            String sender = MsgApp.instance().user.getEmail();
            String msg = _newMsgEditText.getText().toString();
            String msgUrl = String.format(Constants.BUCKET_URL, fPP);
            connector.makeQuery(ParamsCreator.createParamsForNewMessage(msg, receiverEmail,
                    sender, msgUrl));
        }).start();

    }

    @Override
    public void onTransferComplete() {
        runOnUiThread(() -> {
            if (!NewMessageActivity.this.isDestroyed()) {
                _progressDialog.dismiss();
            }
        });
        finish();
        Messages.toast(this, "Media uploaded.");
    }

    @Override
    public void onTransferError(Exception e) {
        if (!NewMessageActivity.this.isDestroyed()) {
            _progressDialog.dismiss();
        }
        Messages.toast(this, "Couldn't upload Media.");
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            if (response.getBoolean(Constants.JSON_RESPONSE)) {
                Messages.toast(this, "Sent.");
            } else {
                Messages.toast(this, "Could not send.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Messages.l(TAG_CLASS, e.toString());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Messages.toast(this, "Ops, Something went wrong.");
    }
}
