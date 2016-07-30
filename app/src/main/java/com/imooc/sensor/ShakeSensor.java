package com.imooc.sensor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by chengyuan on 16/7/28.
 */
public class ShakeSensor implements SensorEventListener{

    public ShakeSensor(){

    }

    private static final String TAG = ShakeSensor.class.getSimpleName();
    private static final int SPEED_SHRESHOLD = 480;
    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mSensor; //加速度传感器

    private long lastTtime; // 纪录最后一次纪录时间
    private float lastX; //纪录x轴的最后一次值
    private float lastY; //纪录y轴的最后一次值
    private float lastZ; //纪z轴的最后一次值

    private OnShakeListener mOnShakeListener;
    public ShakeSensor(Context context){
        mContext = context;

    }

    /**
     *  初始化传感器
     */
    public void init(){
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // 注册传感器
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     *  取消注册
     */
    public void unRegisterListener(){

        mSensorManager.unregisterListener(this, mSensor);
    }

    public void onSensorChanged(SensorEvent event){
        long curTime = System.currentTimeMillis();

        if(curTime - lastTtime > 10) {
            // 两次摇动的时间间隔
            long timeDistance = curTime - lastTtime;
            lastTtime = curTime;

            // 当前的值
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

//            Log.e(TAG, x + "--" + y + "--" + z);
            // 速度的阀值
            double speed;

            // 当x/y/z达到一定值后进行操作
            double absValue = Math.abs(x + y + z - lastX - lastY - lastZ);

            speed = absValue / timeDistance * 10000;

//            Log.e(TAG, "speed is " + speed);
            if(speed > SPEED_SHRESHOLD){
            Log.e(TAG, "speed > SPEED_SHRESHOLD");
                if(null != mOnShakeListener){
                    Log.e(TAG, "null != mOnShakeListener");
                    mOnShakeListener.onShake();
                }
            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuarcy) {

    }

    public void setOnShakeListener(OnShakeListener onShakeListener){
        mOnShakeListener = onShakeListener;
    }

    public interface OnShakeListener{
        void onShake();
    }

}
