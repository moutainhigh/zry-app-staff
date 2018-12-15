package com.zhongmei.bty.base;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhongmei.bty.router.RouteIntent;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;

/**
 * 主activity的父类，登录和自动配置等除外
 */
public class MainBaseActivity extends CalmBaseActivity {

    private static final String TAG = MainBaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        //FacePlatformAdapter.init(MainApplication.getInstance());
//        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
//            @Override
//            public boolean queueIdle() {
//                Log.i("IdleHandler","queueIdle");
//                onInit();
//                return false; //false 表示只监听一次IDLE事件,之后就不会再执行这个函数了.
//            }
//        });
    }

//    protected void onInit() {}

    @Override
    protected void onResume() {
        /**
         * 移除友盟 代码 developer:zhaos
         */
        // MobclickAgent.onPageStart(getFlag());
        // MobclickAgentEvent.onResume(this);
        super.onResume();
        if (Session.getAuthUser() == null) {

            new CommonDialogFragmentBuilder(this).title(getResources().getString(R.string.invalidLogin))
                    .iconType(R.drawable.commonmodule_dialog_icon_warning)
                    .negativeText(R.string.reLogin)
                    .negativeLisnter(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            RouteIntent.startLogin(MainBaseActivity.this);
                            finish();

                        }
                    })
                    .build()
                    .show(this.getSupportFragmentManager(), TAG);

        }
    }

    /**
     * 控制遮罩显示隐藏
     *
     * @param isShow
     */
    public void showShadow(boolean isShow) {

    }

}
