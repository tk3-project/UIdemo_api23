# Readme

## Change to Dependencies

```diff
+ implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
+ implementation 'com.google.android.material:material:1.1.0'
```

## Change to AndroidManifest.xml

```diff
<activity
+    android:name=".MainActivity"
+    android:label="@string/app_name"
+    android:theme="@style/AppTheme.NoActionBar">
    ...
</activity>
```

## Changes Summary
```
|-- app
    |-- manifests
        |-- AndroidManifest.xml
    |-- java
        |-- ui.main
            |-- Fragment1.java
            |-- Fragment2.java
            |-- SectionsPagerAdapter.java
        |-- MainActivity.java
    |-- res
        |-- drawable
            |-- ic_lancher_background_xml
            |-- selected.xml
        |-- layout
            |-- activity_main.xml
            |-- fragment1.xml
            |-- fragment2.xml
        |-- values
            |-- colors.xml
            |-- dimens.xml
            |-- strings.xml
            |-- styles.xml
|-- Gradle Scripts
    |-- build.gradle (:app)
```                       
