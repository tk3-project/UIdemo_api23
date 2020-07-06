package com.g15.demo;

import android.content.SharedPreferences;
import android.util.Log;

public class Scenarios {

    public enum Scenario {
        SCENARIO_MUSIC,
        SCENARIO_WARNING,
        SCENARIO_HOME
    }

    private static String LOG_TAG = "scenarios";
    public static String SHARED_PREFERENCES_KEY = "scenarios-shared-preferences";

    private SharedPreferences sharedPreferences;

    public Scenarios(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean isScenarioActivated(Scenario scenario) {
        String scenarioName = "scenario" + scenario + "_activated";
        Boolean isActive = sharedPreferences.getBoolean(scenarioName, false);
        Log.d(LOG_TAG, "Scenario " + scenario + (isActive ? " is" : " is not") + " activated.");
        return isActive;
    }

    public void enableScenario(Scenario scenario) {
        Log.i(LOG_TAG, "Enabling scenario " + scenario);
        String scenarioName = "scenario" + scenario + "_activated";
        sharedPreferences.edit()
                .putBoolean(scenarioName, true)
                .commit();
    }

    public void disableScenario(Scenario scenario) {
        Log.i(LOG_TAG, "Disabling scenario " + scenario);
        String scenarioName = "scenario" + scenario + "_activated";
        sharedPreferences.edit()
                .putBoolean(scenarioName, false)
                .commit();
    }

    public void setScenarioEnabled(Scenario scenario, boolean enabled) {
        if (enabled) {
            this.enableScenario(scenario);
        } else {
            this.disableScenario(scenario);
        }
    }

    public boolean isAnyScenarioEnabled() {
        return isScenarioActivated(Scenario.SCENARIO_MUSIC)
                || isScenarioActivated(Scenario.SCENARIO_WARNING)
                || isScenarioActivated(Scenario.SCENARIO_HOME);
    }

}
