package com.imooc.imoocshake;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ShowActivity extends AppCompatActivity {

    private static final String TAG = ShowActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.e(TAG, "show img cccccc");
        setContentView(R.layout.activity_show);
    }

    public void onClick(View v){
        // 设置显示界面退出的动画
        overridePendingTransition(0, R.anim.show_set_out);
        finish();
    }

}
