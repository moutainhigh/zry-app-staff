package com.zhongmei.bty.base;

import android.support.v4.app.FragmentActivity;

/**
 * 主activity的父类，登录和自动配置等除外
 */
public class ProcessActivity extends FragmentActivity {

    @Override
    protected void onResume() {
        super.onResume();
        /*if (Session.getAuthUser() == null) {
            new CommonDialogFragmentBuilder(this).title(getResources().getString(R.string.invalidLogin))
                    .iconType(R.drawable.commonmodule_dialog_icon_warning)
                    .negativeText(R.string.reLogin)
                    .negativeLisnter(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent();
                            intent.setClassName("com.zhongmei.bty", "com.zhongmei.bty.splash.login.LoginActivity_");
                            startActivity(intent);
                            finish();
                        }
                    })
                    .build()
                    .show(this.getSupportFragmentManager(), TAG);

        }*/
    }
}
