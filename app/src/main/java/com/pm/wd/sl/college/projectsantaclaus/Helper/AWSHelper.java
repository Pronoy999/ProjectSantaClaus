package com.pm.wd.sl.college.projectsantaclaus.Helper;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

public class AWSHelper {
    private AmazonS3Client s3Client;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private TransferUtility transferUtility;

    /**
     * Method to get the AWS Credential Provider.
     *
     * @param context: The Application Context.
     * @return cognito Credentials.
     */
    private CognitoCachingCredentialsProvider getCredentialsProvider(Context context) {
        if (credentialsProvider == null) {
            credentialsProvider = new CognitoCachingCredentialsProvider(context.getApplicationContext(),
                    Constants.AWS_COGNITO_POOL_ID, Regions.fromName(Constants.AWS_REGION));
        }
        return credentialsProvider;
    }

    /**
     * Method to get the S3 Client.
     *
     * @param context: The Application context.
     * @return s3Client.
     */
    private AmazonS3Client getS3Client(Context context) {
        if (s3Client == null) {
            s3Client = new AmazonS3Client(getCredentialsProvider(context));
            s3Client.setRegion(Region.getRegion(Regions.fromName(Constants.AWS_REGION)));
        }
        return s3Client;
    }

    /**
     * Method to get the Transfer utility client.
     *
     * @param context: The Application Context.
     * @return transfer utility.
     */
    public TransferUtility getTransferUtility(Context context) {
        if (transferUtility == null) {
            transferUtility = TransferUtility.builder()
                    .context(context)
                    .s3Client(getS3Client(context))
                    .defaultBucket(Constants.AWS_BUCKET_NAME)
                    .build();
        }
        return transferUtility;
    }
}
