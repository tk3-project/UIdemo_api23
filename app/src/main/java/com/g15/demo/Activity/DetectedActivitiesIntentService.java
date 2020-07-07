package com.g15.demo.Activity;

import android.app.IntentService;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.g15.demo.Constants;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class DetectedActivitiesIntentService extends IntentService{

    protected static final String TAG = DetectedActivitiesIntentService.class.getSimpleName();

    public DetectedActivitiesIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressWarnings("unchecked")
    /** Define an onHandleIntent() method, which will be called
    * whenever an activity detection update is available
    */
    @Override
    protected void onHandleIntent(Intent intent) {
        /** Check whether the Intent contains activity recognition data */
        if (ActivityRecognitionResult.hasResult(intent)) {
            /** If data is available, then extract the
             * ActivityRecognitionResult from the Intent
             */
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            /** Get the list of the probable activities associated
             * with the current state of the device. Each activity
             * is associated with a confidence level, which is an
             * int between 0 and 100.
             */
            ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

            for (DetectedActivity activity : detectedActivities) {
                Log.i(TAG, "Detected activity: " + activity.getType() + ", " + activity.getConfidence());
                broadcastActivity(activity);
            }
        }
    }

    private void broadcastActivity(DetectedActivity activity) {
        Intent intent = new Intent(Constants.BROADCAST_DETECTED_ACTIVITY);
        intent.putExtra("type", activity.getType());
        intent.putExtra("confidence", activity.getConfidence());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
