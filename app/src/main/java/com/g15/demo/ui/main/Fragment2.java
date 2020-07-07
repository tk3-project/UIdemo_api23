package com.g15.demo.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.g15.demo.Activity.BgDetectedActivitiesService;
import com.g15.demo.Constants;
import com.g15.demo.R;
import com.google.android.gms.location.DetectedActivity;

public class Fragment2 extends Fragment {

    private static String LOG_TAG = "show-fragment";
    BroadcastReceiver broadcastReceiver;

    private TextView txtActivity, txtLocation;
    private ImageView imgActivity;
    private Button btnStartTracking, btnStopTracking;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        txtActivity = view.findViewById(R.id.txt_activity);
        //TODO
        txtLocation = view.findViewById(R.id.txt_location);
        imgActivity = view.findViewById(R.id.img_activity);
        btnStartTracking = view.findViewById(R.id.btn_start_tracking);
        btnStopTracking = view.findViewById(R.id.btn_stop_tracking);

        btnStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTracking();
            }
        });

        btnStopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTracking();
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_DETECTED_ACTIVITY)) {
                    int type = intent.getIntExtra("type", -1);
                    int confidence = intent.getIntExtra("confidence", 0);
                    handleUserActivity(type, confidence);
                }
            }
        };
    }

    private void handleUserActivity(int type, int confidence) {
        String label = getString(R.string.activity_unknown);
        int icon = R.drawable.ic_still;

        switch (type) {
            case DetectedActivity.IN_VEHICLE: {
                label = getString(R.string.activity_in_vehicle);
                icon = R.drawable.ic_driving;
                break;
            }
            case DetectedActivity.ON_BICYCLE: {
                label = getString(R.string.activity_on_bicycle);
                icon = R.drawable.ic_on_bicycle;
                break;
            }
            case DetectedActivity.ON_FOOT: {
                label = getString(R.string.activity_on_foot);
                icon = R.drawable.ic_walking;
                break;
            }
            case DetectedActivity.RUNNING: {
                label = getString(R.string.activity_running);
                icon = R.drawable.ic_running;
                break;
            }
            case DetectedActivity.STILL: {
                label = getString(R.string.activity_still);
                break;
            }
            case DetectedActivity.TILTING: {
                label = getString(R.string.activity_tilting);
                icon = R.drawable.ic_tilting;
                break;
            }
            case DetectedActivity.WALKING: {
                label = getString(R.string.activity_walking);
                icon = R.drawable.ic_walking;
                break;
            }
            case DetectedActivity.UNKNOWN: {
                label = getString(R.string.activity_unknown);
                break;
            }
        }

        Log.e(LOG_TAG, "User activity: " + label + ", Confidence: " + confidence);

        if (confidence > Constants.CONFIDENCE) {
            txtActivity.setText(label);
            imgActivity.setImageResource(icon);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(Constants.BROADCAST_DETECTED_ACTIVITY));
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    // Problem
    private void startTracking() {
        Intent intent = new Intent(getActivity(), BgDetectedActivitiesService.class);
        getActivity().startService(intent);
    }
    private void stopTracking() {
        Intent intent = new Intent(getActivity(), BgDetectedActivitiesService.class);
        getActivity().stopService(intent);
    }
}