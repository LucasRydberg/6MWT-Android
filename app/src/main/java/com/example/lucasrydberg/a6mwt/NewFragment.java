package com.example.lucasrydberg.a6mwt;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by lucasrydberg on 10/2/17.
 */

public class NewFragment extends Fragment implements SensorEventListener, StepListener {

    MainActivity mainActivity;
    View myFragmentView;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;

    private ProgressBar mProgressBar;
    private CountDownTimer mCountDownTimer;

    private TextView timeText;

    public NewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.fragment_new, container, false);

        mainActivity = ((MainActivity)getActivity());

        // get an instance of the SensorManager
        sensorManager = (SensorManager) mainActivity.getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        // initiate view items
        Button BtnStart = (Button)myFragmentView.findViewById(R.id.btn_start);
        Button BtnStop = (Button)myFragmentView.findViewById(R.id.btn_stop);
        mProgressBar=(ProgressBar)myFragmentView.findViewById(R.id.timeProgress);
        timeText = (TextView)myFragmentView.findViewById(R.id.timeText);
        timeText.setText("06:00");

        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                numSteps = 0;
                setListener();

                mCountDownTimer = new CountDownTimer(360000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        Log.i("seconds remaining ", millisUntilFinished / 1000 + "");
                        mProgressBar.setProgress((int)millisUntilFinished/1000);
                        String time = secondsToString((int)millisUntilFinished/1000);
                        timeText.setText(time);
                    }

                    public void onFinish() {
                        Log.i("Finished! ", "Number of steps: " + numSteps);
                        Toast.makeText(getActivity(), "Nice Job! Test complete. Check the history page for results", Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        String sharedPrefString = sharedPref.getString("numSteps", "");
                        String newNumSteps = sharedPrefString + "/:/ " + numSteps;
                        sharedPref.edit().putString("numSteps", newNumSteps).apply();
                        mProgressBar.setProgress(360);
                        timeText.setText("06:00");
                    }
                }.start();

            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                unregisterListener();
                Log.i("Number of steps", numSteps + "");
            }
        });

        // Inflate the layout for this fragment
        return myFragmentView;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }

    private void setListener(){
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unregisterListener(){
        sensorManager.unregisterListener(this);
    }
}