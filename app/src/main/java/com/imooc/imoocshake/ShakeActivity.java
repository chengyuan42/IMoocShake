package com.imooc.imoocshake;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imooc.sensor.ShakeSensor;

public class ShakeActivity extends AppCompatActivity implements ShakeSensor.OnShakeListener {

    private static final String TAG = ShakeSensor.class.getSimpleName();

    private static final int MSG_COUNT_END = 0X01; // 次数使用结束
    private static final int MSG_COUNT_CONTINUE = 0X02; // 次数没有结束

    private ShakeSensor mShakeSensor; // 传感器
    private MediaPlayer mPlayer; // 音乐效果
    private Vibrator mVibrator; // 震动效果
    private ImageView mImgHandle; // 视图
    private TextView mTxtCount; // 视图
    private static int count = 3; // 纪录次数
    private boolean isStart; // 是否是开始计数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        mImgHandle = (ImageView) this.findViewById(R.id.img_handle);
        mTxtCount = (TextView) this.findViewById(R.id.txt_show_count);

        mShakeSensor = new ShakeSensor(this);

        // 注册回调事件
        mShakeSensor.setOnShakeListener(this);
        mShakeSensor.init();

        mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        // 启动动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.main_img_handle);
        mImgHandle.startAnimation(animation);
    }


    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            Log.e(TAG, "抠门给自己哦亲" + msg.what);

            if(msg.what == MSG_COUNT_END){
                startVibrator();
            }else {
                // 震动 音乐效果
                startAudioWithVibrator();
                Intent show = new Intent(ShakeActivity.this,ShowActivity.class);
                startActivity(show);

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if(count == 0){
            count = 0;
        }else {
            if(isStart)
            count--;
        }
        isStart = true;
        mTxtCount.setText("您还可以摇" + count + "次");
        Log.e(TAG, "您还可以摇" + count + "次");
    }

    @Override
    public void onShake() {
        // 现实操作
        Toast t = Toast.makeText(this, "摇一摇成功", Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();

        Log.e(TAG, "您还可以摇 onshake" + count + "次");
        if(count == 0){
            // 次数已经使用完成
            mHandler.sendEmptyMessage(MSG_COUNT_END);
        }else {
            // 还有次数可以摇一摇
            mHandler.sendEmptyMessage(MSG_COUNT_CONTINUE);
        }

        // 添加跳转时showActivity进入动画
        overridePendingTransition(R.anim.main_set_in, 0);
    }

    /**
     *  启动音乐和震动
     */
    public void startAudioWithVibrator(){
        mPlayer = MediaPlayer.create(this, R.raw.entervoice);
        // 播放
        mPlayer.start();

        long pattern[] = {500, 300, 500, 300}; // 间隔多长时间震动
        mVibrator.vibrate(pattern, -1);
    }

    public void  startVibrator(){
        // 震动效果
        long pattern[] = {500, 300, 500, 300}; // 间隔多长时间震动
        mVibrator.vibrate(pattern, -1);
    }
}
