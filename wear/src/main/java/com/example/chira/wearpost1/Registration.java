package com.example.chira.wearpost1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import static android.hardware.Sensor.TYPE_GYROSCOPE;
import static android.hardware.Sensor.TYPE_HEART_RATE;

public class Registration extends WearableActivity implements SensorEventListener{

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);
    public int i=4;
    public String lhr,ldb,laccx,laccy,laccz,lgx,lgy,lgz,lrw;
    TextView textView;
    TextView heart;
    private SensorEventListener listener;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mSensorType;
    private static final String REGISTER_URL = "http://www.cs.odu.edu/~snagendra/AGRESSION_DETECTION/getsensorread.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        textView = (TextView)findViewById(R.id.number);
        heart = (TextView)findViewById(R.id.heart);
        setAmbientEnabled();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(mSensorType);
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(TYPE_HEART_RATE),SensorManager.SENSOR_DELAY_GAME);
      //  registerUser();
      /*  final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (i <= 15) {
                    i++;

                    handler.postDelayed(this, 5000);
                }
            }
        }, 0);*/
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor sensor = event.sensor;
        // System.out.println("in sensor changed"+sensor.getType());
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            laccx = Float.toString(event.values[0]);
            laccy = Float.toString(event.values[1]);
            laccz = Float.toString(event.values[2]);

        }else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            lgx = Float.toString(event.values[0]);
            lgy = Float.toString(event.values[1]);
            lgz = Float.toString(event.values[2]);
        }
        else if(sensor.getType()== Sensor.TYPE_HEART_RATE){
            lhr = Float.toString(event.values[0]);
            System.out.println("Heart" + lhr);
        }

        String hr = lhr;
        String db = "45";
        String accx = laccx;
        String accy = laccy;
        String accz = laccz;
        String gx = lgx;
        String gy = lgy;
        String gz = lgz;
        String rw = "66";
        register(hr, db, accx, accy, accz, gx, gy, gz, rw);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

  /*  private void registerUser() {

        String hr = lhr;
        String db = "45";
        String accx = laccx;
        String accy = laccy;
        String accz = laccz;
        String gx = lgx;
        String gy = lgy;
        String gz = lgz;
        String rw = "66";
        System.out.println("blaaaaaaaaaaaaa" + hr + accx + accy);

        register(hr, db, accx, accy, accz, gx, gy, gz, rw);
    }*/

    private void register(String hr, String db, String accx, String accy, String accz,String gx, String gy, String gz, String rw) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            // ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();
                data.put("hr",params[0]);
                data.put("db",params[1]);
                data.put("accx",params[2]);
                data.put("accy",params[3]);
                data.put("accz",params[4]);
                data.put("gx",params[5]);
                data.put("gy",params[6]);
                data.put("gz",params[7]);
                data.put("rw",params[8]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);
                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(hr, db, accx, accy, accz, gx, gy, gz, rw);

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
    }
}
