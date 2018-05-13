package edu.arbelkilani.compassview;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import edu.arbelkilani.compass.Compass;
import edu.arbelkilani.compass.CompassListener;

public class MainActivity extends AppCompatActivity implements CompassListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Compass compass = findViewById(R.id.compass_3);
        compass.setListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged : " + event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged : sensor : " + sensor);
        Log.d(TAG, "onAccuracyChanged : accuracy : " + accuracy);
    }
}
