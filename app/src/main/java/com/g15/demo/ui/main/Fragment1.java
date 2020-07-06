package com.g15.demo.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.g15.demo.R;
import com.g15.demo.Scenarios;

import static com.g15.demo.Scenarios.SHARED_PREFERENCES_KEY;

public class Fragment1 extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private static String LOG_TAG = "fragment1";

    private Switch musicSwitch;
    private Switch warningSwitch;
    private Switch homeSwitch;
    private Scenarios scenarios;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {


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
        if (compoundButton == musicSwitch) {
            Log.i(LOG_TAG, "Music scenario state changed to: " + scenarioActivated);
            scenarios.setScenarioEnabled(Scenarios.Scenario.SCENARIO_MUSIC, scenarioActivated);
        } else if (compoundButton == warningSwitch) {
            Log.i(LOG_TAG, "Warning scenario state changed to: " + scenarioActivated);
            scenarios.setScenarioEnabled(Scenarios.Scenario.SCENARIO_WARNING, scenarioActivated);
        } else if (compoundButton == homeSwitch) {
            Log.i(LOG_TAG, "Home scenario state changed to: " + scenarioActivated);
            scenarios.setScenarioEnabled(Scenarios.Scenario.SCENARIO_HOME, scenarioActivated);
        } else {
            Log.w(LOG_TAG, "Invalid scenario change triggered.");
        }

        if (!scenarios.isAnyScenarioEnabled()) {
            // No scenario enabled. Disable location and activity tracking.
            // TODO
        } else if (!activatedBefore) {
            // The first scenario has been activated. Enable location and activity tracking.
            // TODO
        }
    }
}