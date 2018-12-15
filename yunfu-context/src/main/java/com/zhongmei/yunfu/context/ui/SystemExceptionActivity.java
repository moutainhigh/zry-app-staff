package com.zhongmei.yunfu.context.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.zhongmei.yunfu.context.util.HomeWatcher;
import com.zhongmei.yunfu.context.R;

/**
 * Created by demo on 2018/12/15
 */
public class SystemExceptionActivity extends android.app.Activity implements HomeWatcher.OnHomePressedListener {
    private HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception_handler);

        findViewById(R.id.activity_exception_handler_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.zhongmei.bty", "com.zhongmei.bty.splash.login.LoginInitActivity_"));
                ExceptionHandlerActivity.this.startActivity(intent);*/

                System.exit(1);
            }
        });

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(this);
        mHomeWatcher.startWatch();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mHomeWatcher.setOnHomePressedListener(null);
        mHomeWatcher.stopWatch();
        super.onDestroy();
    }

    @Override
    public void onHomePressed() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    public void onHomeLongPressed() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
