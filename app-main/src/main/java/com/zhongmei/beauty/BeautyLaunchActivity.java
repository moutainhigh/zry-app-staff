package com.zhongmei.beauty;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.common.view.ui.widget.RoundImageView;
import com.zhongmei.util.AsyncImage;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.LoginActivity;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
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
        setWaiterInfo(Session.getAuthUser());
        initModele();
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, BeautyLaunchActivity_.class));
    }

    private void setShopInfo(){
        String shopName = ShopInfoManager.getInstance().shopInfo.getShopName();
        tv_shopName.setText(shopName);
        tv_shopAddr.setText(String.format(getString(R.string.beauty_shopinfo), ShopInfoManager.getInstance().shopInfo.getShopAddress()));

        String logoUrl = ShopInfoManager.getInstance().shopInfo.getShopLogo();
        if (!TextUtils.isEmpty(logoUrl)) {
            AsyncImage.showImg(getContext(), iv_shopIcon, logoUrl, R.drawable.shop_icon);
        }
    }



    public void setWaiterInfo(AuthUser authUser) {
        if (authUser != null) {
            iv_waiterQuite.setVisibility(View.VISIBLE);
            iv_waiterHead.setImageResource(R.drawable.lanucher_user_login);
            tv_waiterName.setText(authUser.getName());
        }else{
            iv_waiterQuite.setVisibility(View.GONE);
            iv_waiterHead.setImageResource(R.drawable.launcher_user_default);
            tv_waiterName.setText("账号登录");
        }
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

    @Click(R.id.launcher_user_info)
    public void onClick(View v){
        if (null == Session.getAuthUser()) {
            LoginActivity.start(getContext());
            this.finish();
        } else {
            if (!Session.getAuthUser().isSpecialAccount()) {
                showHintDialog();
            }
        }
    }

    /**
     * 显示注销对话框
     */
    private void showHintDialog() {
        CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance());
        cb.iconType(CommonDialogFragment.ICON_WARNING)
                .title(getResources().getString(R.string.sure_loginout_hint))
                .negativeText(R.string.calm_logout_no)
                .positiveText(R.string.calm_logout_yes)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Session.unbind();
                        setWaiterInfo(null);
                    }

                })
                .build()
                .show(getSupportFragmentManager(), "launcher_login_out_confirm");
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
