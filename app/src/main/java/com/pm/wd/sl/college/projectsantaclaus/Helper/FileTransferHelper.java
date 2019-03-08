package com.pm.wd.sl.college.projectsantaclaus.Helper;

import android.content.Context;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;

public class FileTransferHelper implements TransferListener {
    private Context context;
    private String filePath;
    private TransferListener transferListener;
    private String TAG_CLASS = FileTransferHelper.class.getSimpleName();

    /**
     * Interface to be invoked when the transfer Status is changed.
     */
    public interface TransferListener {
        void onTransferComplete();

        void onTransferError(Exception e);
    }

    public FileTransferHelper(Context context, String filePath, TransferListener transferListener) {
        this.context = context;
        this.filePath = filePath;
        this.transferListener = transferListener;
    }

    /**
     * Method to upload a file to the AWS S3 bucket.
     * It will upload the file to the S3 bucket for
     * the region specified.
     *
     * @param fileName: Just the name of the File. The entire path of the file
     *                  will be initialized in the constructor.
     */
    public void upload(final String fileName) {
        Thread uploadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                AWSHelper awsHelper = new AWSHelper();
                TransferUtility transferUtility = awsHelper.getTransferUtility(context);
                TransferObserver transferObserver = transferUtility
                        .upload(Constants.AWS_BUCKET_NAME, fileName, new File(filePath));
                transferObserver.setTransferListener(FileTransferHelper.this);
            }
        });
        uploadThread.start();
    }

    /**
     * Method to download the file from S3 Bucket for the region selected.
     *
     * @param fileName: The Key by which the file is saved in the S3 Bucket.
     */
    public void download(final String fileName) {
        Thread downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                AWSHelper helper = new AWSHelper();
                TransferUtility transferUtility = helper.getTransferUtility(context);
                TransferObserver observer = transferUtility.download(fileName, new File(filePath));
                observer.setTransferListener(FileTransferHelper.this);
            }
        });
        downloadThread.start();
    }

    @Override
    public void onStateChanged(int id, TransferState state) {
        if (state.name().equalsIgnoreCase(Constants.AWS_STATE_COMPLETED)) {
            if (transferListener != null) {
                transferListener.onTransferComplete();
            }
        } else {
            Messages.l(TAG_CLASS, "Current State: " + state.name());
        }
    }

    @Override
    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
        long progress = (bytesCurrent / bytesTotal) * 100;
        Messages.l(TAG_CLASS, progress + "%");
    }

    @Override
    public void onError(int id, Exception ex) {
        if (transferListener != null) {
            transferListener.onTransferError(ex);
        }
        Messages.l(TAG_CLASS, ex.toString());
    }
}
