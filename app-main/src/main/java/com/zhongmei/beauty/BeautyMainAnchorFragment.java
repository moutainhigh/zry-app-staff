package com.zhongmei.beauty;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zhongmei.beauty.entity.BeautyNotifyEntity;
import com.zhongmei.beauty.interfaces.IBeautyAnchor;
import com.zhongmei.beauty.operates.BeautyNotifyCache;
import com.zhongmei.beauty.widgets.XRadioGroup;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.LoginActivity;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.DialogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.beauty_main_activity_anchor)
public class BeautyMainAnchorFragment extends BasicFragment implements BeautyNotifyCache.BeautyNotifyListener,XRadioGroup.OnCheckedChangeListener{

    @ViewById(R.id.tv_waiter)
    protected TextView tv_waiter;

    @ViewById(R.id.rg_mundle_anchor)
    protected XRadioGroup rg_mundleAnchor;

    @ViewById(R.id.tv_undeal_trade_tip)
    protected  TextView tv_undealBookingTradeTip;

    private IBeautyAnchor mBeautyAnchor;

    private BeautyNotifyCache mNotifyCache;

    public void setBeautyAnchor(IBeautyAnchor iAnchor) {
        this.mBeautyAnchor = iAnchor;
    }

    private int preCheckedResId;//上一次选中的id


    @AfterViews
    public void init() {
        setWaiterInfo(Session.getAuthUser());
        mNotifyCache=BeautyNotifyCache.getInstance();
        mNotifyCache.addNotifyListener(this);
        mNotifyCache.start();
        rg_mundleAnchor.setOnCheckedChangeListener(this);
    }

    /**
     * 设置服务员信息
     *
     * @param authUser
     */
    public void setWaiterInfo(AuthUser authUser) {
        if (authUser != null) {
            tv_waiter.setText(authUser.getName());
        }
    }

    @Click({ R.id.rb_anchor_refresh, R.id.tv_waiter})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_anchor_notifycation:
                if (mBeautyAnchor != null && !ClickManager.getInstance().isClicked()) {
                    mBeautyAnchor.toNotifycationCenter();
                }
                break;
            case R.id.rb_anchor_refresh:
                if (mBeautyAnchor != null && !ClickManager.getInstance().isClicked()) {
                    mBeautyAnchor.toShopSyncRefresh(v);
                }
                break;
            case R.id.tv_waiter:
                if (getActivity() != null) {
                    DialogUtil.showWarnConfirmDialog(this.getSupportFragmentManager(),
                            R.string.sure_loginout_hint,
                            R.string.ok,
                            R.string.cancel,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LoginActivity.logout(getActivity());
                                }
                            }, null, "exitAppDialog");
                }
                break;
        }
    }

    @UiThread
    protected void setChecked(int resid) {
        View view = getActivity().findViewById(resid);
        if (view != null && view instanceof RadioButton) {
        }
        ((RadioButton) view).setChecked(true);
    }


    @UiThread
    @Override
    public void refreshNotifyNumbers(BeautyNotifyEntity notifyEntity) {
        if(tv_undealBookingTradeTip!=null){
            tv_undealBookingTradeTip.setVisibility(notifyEntity.getUnDealReserverNumber()<=0?View.GONE:View.VISIBLE);
            tv_undealBookingTradeTip.setText(notifyEntity.getUnDealReserverNumber()+"");
        }

    }

    @Override
    public void onCheckedChanged(XRadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_anchor_cashier:
                if (mBeautyAnchor != null) {
                    mBeautyAnchor.toCashier();
                }
                break;
            case R.id.rb_anchor_trades:
                if (mBeautyAnchor != null) {
                    mBeautyAnchor.toTradeCenter();
                }
                break;
            case R.id.rb_anchor_report:
                if (mBeautyAnchor == null) {
                    break;
                }

                VerifyHelper.verifyAlert(getActivity(), BeautyApplication.PERMISSION_BEAUTY_REPORT_FORM,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                mBeautyAnchor.toReportCenter();
                            }

                            @Override
                            public void onNegative(String code, Auth.Filter filter) {
                                super.onNegative(code, filter);
                                setChecked(preCheckedResId);
                            }
                        });

                break;
            case R.id.rb_anchor_reserver:
                if (mBeautyAnchor != null) {
                    mBeautyAnchor.toReserverManager();
                }
                break;
            case R.id.rb_anchor_technician:
                if (mBeautyAnchor != null) {
                    mBeautyAnchor.toTechniciaManage();
                }
                break;
            case R.id.btn_anchor_member:
                if (mBeautyAnchor != null) {
                    mBeautyAnchor.toMemberCenter();
                }
                break;
            case R.id.rb_anchor_shop:
                if (mBeautyAnchor == null) {
                    break;
                }

                VerifyHelper.verifyAlert(getActivity(), BeautyApplication.PERMISSION_BEAUTY_SHOP_MANAGE,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                mBeautyAnchor.toShopManagerCenter();
                            }

                            @Override
                            public void onNegative(String code, Auth.Filter filter) {
                                super.onNegative(code, filter);
                                setChecked(preCheckedResId);
                            }
                        });
                break;
        }
        preCheckedResId = checkedId;
    }

    @Override
    public void onDestroy() {
        if (mNotifyCache != null) {
            mNotifyCache.onDestory();
        }
        super.onDestroy();
    }
}
