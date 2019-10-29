package com.zhongmei.beauty;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.common.view.ui.widget.RoundImageView;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_launch)
public class BeautyLaunchActivity extends MainBaseActivity {

    @ViewById(R.id.launcher_user_info)
    protected LinearLayout layout_waiter_info;

    @ViewById(R.id.launcher_user_icon)
    protected ImageView iv_waiterHead;

    @ViewById(R.id.launcher_user_name)
    protected TextView tv_waiterName;

    @ViewById(R.id.launcher_user_login_out)
    protected ImageView iv_waiterQuite;

    @ViewById(R.id.iv_shop_logo)
    protected RoundImageView iv_shopIcon;

    @ViewById(R.id.tv_shop_name)
    protected TextView tv_shopName;

    @ViewById(R.id.tv_shop_address)
    protected TextView tv_shopAddr;


    @AfterViews
    protected void initView(){
        //加载数据
        //设置
        setShopInfo();
        setWaiterInfo();
        initModele();
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, BeautyLaunchActivity_.class));
    }

    private void setShopInfo(){

    }


    private void setWaiterInfo(){

    }


    private void initModele(){
        int mode=SharedPreferenceUtil.getSpUtil().getInt(Constant.SP_USE_SENCE_MODE,1);
        switch (mode){
            case 1: //服务员模式
                loadWaiterMode();
                break;
            case 2://顾客模式
                loadCustomerMode();
                break;
        }

    }

    //加载服务员模式
    private void loadWaiterMode(){
        BeautyWaiterModeLaunchFragment waiterLaunchFragment=new BeautyWaiterModeLaunchFragment_.FragmentBuilder_().build();
        replaceFragment(R.id.fl_operator,waiterLaunchFragment,BeautyWaiterModeLaunchFragment.class.getSimpleName());

    }

    //加载顾客模式
    private void loadCustomerMode(){
        BeautyCustomerModeLaunchFragment customerLaunchFragment=new BeautyCustomerModeLaunchFragment_.FragmentBuilder_().build();
        replaceFragment(R.id.fl_operator,customerLaunchFragment,BeautyCustomerModeLaunchFragment.class.getSimpleName());
    }



}
