package com.g15.demo.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.g15.demo.R;
import com.g15.demo.Scenarios;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.g15.demo.Scenarios.SHARED_PREFERENCES_KEY;

public class Fragment1 extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private static String LOG_TAG = "settings-fragment";

    private Switch musicSwitch;
    private Switch warningSwitch;
    private Switch homeSwitch;
    private Scenarios scenarios;
    private Scenarios.Scenario currentTargetScenario;
    private CompoundButton currentTargetSwitch;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        musicSwitch = view.findViewById(R.id.switch1);
        warningSwitch = view.findViewById(R.id.switch2);
        homeSwitch = view.findViewById(R.id.switch3);

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        scenarios = new Scenarios(sharedPreferences);

        initializeScenarioActivated();

        musicSwitch.setOnCheckedChangeListener(this);
        warningSwitch.setOnCheckedChangeListener(this);
        homeSwitch.setOnCheckedChangeListener(this);
    }

    /**
     * Checks which of the necessary permissions are granted.
     * @return A list of permission identifiers that are not granted, but needed.
     */
    private String[] identifyNeededPermissions() {
        List<String> neededPermissions = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            neededPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            neededPermissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            neededPermissions.add(Manifest.permission.ACTIVITY_RECOGNITION);
        }

        return neededPermissions.toArray(new String[neededPermissions.size()]);
    }

    /**
     * Checks if all needed permissions are granted.
     * @return true if all permissions are granted, otherwise false.
     */
    private boolean checkPermissions() {
        final String[] neededPermissions = identifyNeededPermissions();

        return neededPermissions.length == 0;
    }

    /**
     * Displays a descriptive dialog that prompts the user to grant the necessary permissions.
     */
    private void showPermissionRequestDialog() {
        final String[] neededPermissions = identifyNeededPermissions();

        if (neededPermissions.length == 0) {
            Log.d(LOG_TAG, "All permissions already granted.");
        }

        Log.d(LOG_TAG, "Showing dialog to grant permissions.");
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.required_permissions_title))
                .setMessage(getString(R.string.required_permissions_message))
                .setNeutralButton(getString(R.string.required_permissions_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(getView(), getString(R.string.permission_dialog_canceled), Snackbar.LENGTH_LONG)
                                .show();
                        Log.i(LOG_TAG, "The user canceled the grant permission process.");
                    }
                })
                .setPositiveButton(getString(R.string.required_permissions_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(LOG_TAG, "User starts grant permission process.");
                        grantPermissions(neededPermissions);
                    }
                })
                .show();
    }

    /**
     * Request the user to grant the specified permissions.
     * @param permissions The permissions that should be granted.
     */
    private void grantPermissions(String[] permissions) {
        if (permissions.length > 0) {
            Log.d(LOG_TAG, "Requesting missing permissions.");
            requestPermissions(permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Boolean allPermissionsGranted = true;
        for (int i = 0; i < grantResults.length; i++) {
            allPermissionsGranted = allPermissionsGranted && grantResults[i] == PackageManager.PERMISSION_GRANTED;
        }

        if (allPermissionsGranted) {
            Log.i(LOG_TAG, "All needed permissions granted.");
            if (currentTargetScenario != null) {
                Log.i(LOG_TAG, "Activating pending scenario.");
                boolean activatedBefore = scenarios.isAnyScenarioEnabled();
                setScenarioEnabled(currentTargetScenario, true);
                currentTargetSwitch.setChecked(true);

                if (!activatedBefore) {
                    // The first scenario has been activated. Enable location and activity tracking.
                    enableLocationAndActivityTracking();
                }
                currentTargetScenario = null;
            }
        } else {
            Snackbar.make(getView(), getString(R.string.permission_dialog_canceled), Snackbar.LENGTH_LONG)
                    .show();
            for (int i = 0; i < permissions.length && i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    Log.w(LOG_TAG, "Necessary permission not granted: " + permissions[i]);
                }
            }
        }
    }

    /**
     * This method restores the state of the switches to match the stored activation state.
     */
    private void initializeScenarioActivated() {
        if (scenarios.isScenarioActivated(Scenarios.Scenario.SCENARIO_MUSIC)) {
            musicSwitch.setChecked(true);
        }
        if (scenarios.isScenarioActivated(Scenarios.Scenario.SCENARIO_WARNING)) {
            warningSwitch.setChecked(true);
        }
        if (scenarios.isScenarioActivated(Scenarios.Scenario.SCENARIO_HOME)) {
            homeSwitch.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean scenarioActivated) {
        boolean activatedBefore = scenarios.isAnyScenarioEnabled();
        Scenarios.Scenario currentScenario;
        if (compoundButton == musicSwitch) {
            Log.i(LOG_TAG, "Music scenario state changed to: " + scenarioActivated);
            currentScenario = Scenarios.Scenario.SCENARIO_MUSIC;
        } else if (compoundButton == warningSwitch) {
            Log.i(LOG_TAG, "Warning scenario state changed to: " + scenarioActivated);
            currentScenario = Scenarios.Scenario.SCENARIO_WARNING;
        } else if (compoundButton == homeSwitch) {
            Log.i(LOG_TAG, "Home scenario state changed to: " + scenarioActivated);
            currentScenario = Scenarios.Scenario.SCENARIO_HOME;
        } else {
            Log.w(LOG_TAG, "Invalid scenario change triggered.");
            return;
        }

        boolean hasPermissions = checkPermissions();
        Log.d(LOG_TAG, hasPermissions ? "All permissions granted." : "Additional permissions needed.");
        if (scenarioActivated && !hasPermissions) {
            currentTargetScenario = currentScenario;
            currentTargetSwitch = compoundButton;
            currentTargetSwitch.setChecked(false);
            showPermissionRequestDialog();
            return;
        }

        setScenarioEnabled(currentScenario, scenarioActivated);

        if (!scenarios.isAnyScenarioEnabled()) {
            // No scenario enabled. Disable location and activity tracking.
            disableLocationAndActivityTracking();
        } else if (!activatedBefore) {
            // The first scenario has been activated. Enable location and activity tracking.
            enableLocationAndActivityTracking();
        }
    }

    /**
     * Enable and initialize the tracking of location and activity of the user.
     */
    private void enableLocationAndActivityTracking() {
        // TODO
    }

    /**
     * Disable the tracking of location and activity of the user.
     */
    private void disableLocationAndActivityTracking() {
        // TODO
    }

    /**
     * This method changes the activation state of a scenario.
     * @param scenario The scenario to change.
     * @param scenarioActivated If the scenario should be enabled or disabled.
     */
    private void setScenarioEnabled(Scenarios.Scenario scenario, boolean scenarioActivated) {
        scenarios.setScenarioEnabled(scenario, scenarioActivated);
    }
}