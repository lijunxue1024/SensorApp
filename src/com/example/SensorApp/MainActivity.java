package com.example.SensorApp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private TextView _sensor_light_text ;

    private TextView _sensor_accelerate_text;
    private TextView _sensor_magnetic_text;

    private SensorManager sensorManager;
    private ImageView _compass_img;


    private SensorEventListener listener = new SensorEventListener() {

        float lightValues  ;
        float [] accelerValues = new float[3];
        float [] magneticValues = new float[3];

        private float lastRotateDegree;
        @Override
        //数据变化时
        public void onSensorChanged(SensorEvent event) {


            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                lightValues = event.values[0];
                _sensor_light_text.setText("当前手机亮度为：" + lightValues);
            }else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                accelerValues = event.values.clone();
                float xValue = accelerValues[0];
                float yValue = accelerValues[1];
                float zValue = accelerValues[2];
                _sensor_accelerate_text.setText(
                        "手机向左加速度为x："+xValue+"\n"+
                                "手机向后加速度为y："+yValue+"\n"+
                                "手机向下加速度为z："+zValue);
                if (xValue>15||yValue>15||zValue>15)
                {
                    Toast.makeText(MainActivity.this,"你正在左右摇晃",Toast.LENGTH_SHORT).show();
                }
            }else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            {
                magneticValues = event.values.clone();
                _sensor_magnetic_text.setText("手机磁感："+magneticValues[0]);
            }

            float [] R = new float[9];
            float [] values = new float[3];


            SensorManager.getRotationMatrix(R,null,accelerValues,
                    magneticValues);
            SensorManager.getOrientation(R,values);

           double degrees = Math.toDegrees(values[0]);
//            Log.d("----------degrees"," "+degrees);
           float rotateDegrees = -(float)Math.toDegrees(values[0]);
            if (Math.abs(rotateDegrees - lastRotateDegree)>5)
            {
                RotateAnimation animation = new RotateAnimation(
                        lastRotateDegree,rotateDegrees, Animation.RELATIVE_TO_SELF,0.5f,
                        Animation.RELATIVE_TO_SELF,0.5f);
                animation.setFillAfter(true);


                _compass_img.startAnimation(animation);
                lastRotateDegree = rotateDegrees;


            }


        }

        @Override//精度变化时
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        _sensor_light_text = (TextView)findViewById(R.id.sensor_light_text);
        _sensor_accelerate_text = (TextView)findViewById(R.id.sensor_accelerate_text);
        _sensor_magnetic_text = (TextView)findViewById(R.id.sensor_magnetic_text);
        _compass_img = (ImageView)findViewById(R.id.compass_img);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);



        Sensor sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor sensorAcce = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);



        // SENSOR_DELAY_NORMAL为更新速率
        sensorManager.registerListener(listener,sensorLight,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener,sensorAcce,SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(listener,sensorMagnetic,SensorManager.SENSOR_DELAY_UI);





    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (sensorManager != null)
        {
            //注销释放资源
            sensorManager.unregisterListener(listener);

        }
    }




}
