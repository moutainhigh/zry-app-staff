package com.zhongmei.beauty.order;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.zhongmei.beauty.interfaces.BeautyOrderOperatorListener;
import com.zhongmei.beauty.order.BeautyOrderManager;
import com.zhongmei.beauty.view.NestRadioGroup;
import com.zhongmei.bty.mobilepay.event.ActionClose;
import com.zhongmei.beauty.order.event.BeautyOrderCustomerEvent;
import com.zhongmei.yunfu.R;
import com.zhongmei.beauty.order.event.ActionClearShopcart;
import com.zhongmei.yunfu.ui.base.BasicFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.beauty_order_top_fragment)
public class BeautyOrderTopFragment extends BasicFragment implements NestRadioGroup.OnCheckedChangeListener {

    public static final int TYPE_CARD = 0x01;     public static final int TYPE_INTEGRAL = 0x02;     public static final int TYPE_PARTY = 0x03;     public static final int TYPE_COUPON = 0x04;     public static final int TYPE_DISCOUNT = 0x05;     public static final int TYPE_ACTIVITY = 0x06;     public static final int TYPE_EXTRA = 0x07;     public static final int TYPE_REMARK = 0x08;     public static final int TYPE_TABLE = 0x09;

    @ViewById(R.id.custom_rb)
    protected NestRadioGroup custom_rb;

    @ViewById(R.id.btn_login)
    protected Button btn_login;

    @ViewById(R.id.rb_card)
    protected RadioButton rb_card;
    @ViewById(R.id.rb_integral)
    protected RadioButton rb_integral;

    @ViewById(R.id.rb_party)
    protected RadioButton rb_party;
    @ViewById(R.id.rb_coupon)
    protected RadioButton rb_coupon;
    @ViewById(R.id.rb_discount)
    protected RadioButton rb_discount;
    @ViewById(R.id.rb_activity)
    protected RadioButton rb_activity;

    @ViewById(R.id.rb_extra)
    protected RadioButton rb_extra;
    @ViewById(R.id.rb_remark)
    protected RadioButton rb_remark;
    @ViewById(R.id.rb_table)
    protected RadioButton rb_table;

    private BeautyOrderOperatorListener mOperatorListener;


    public void setOperatorListener(BeautyOrderOperatorListener listener) {
        this.mOperatorListener = listener;
    }

    @AfterViews
    protected void initView() {
        custom_rb.setOnCheckedChangeListener(this);
    }

    @Click({R.id.btn_back, R.id.btn_login, R.id.btn_clearcart})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getActivity().finish();
                break;
            case R.id.btn_login:
                if (mOperatorListener != null) {
                    mOperatorListener.onLoginClick();
                }
                break;
            case R.id.btn_clearcart:
                clearCart();
                break;
        }
    }


    private void clearCart() {
        BeautyOrderManager.clearShopcartDish();
        EventBus.getDefault().post(new ActionClearShopcart());
        EventBus.getDefault().post(new ActionClose());
    }



    public void setCompoundButtonStatus(int type, boolean isCheck) {
        switch (type) {
            case TYPE_CARD:
                setChecked(rb_card, isCheck);
                break;
            case TYPE_INTEGRAL:
                setChecked(rb_integral, isCheck);
                break;
            case TYPE_PARTY:
                setChecked(rb_party, isCheck);
                break;
            case TYPE_COUPON:
                setChecked(rb_coupon, isCheck);
                break;
            case TYPE_DISCOUNT:
                setChecked(rb_discount, isCheck);
                break;
            case TYPE_ACTIVITY:
                setChecked(rb_activity, isCheck);
                break;
            case TYPE_EXTRA:
                setChecked(rb_extra, isCheck);
                break;
            case TYPE_REMARK:
                setChecked(rb_remark, isCheck);
                break;
            case TYPE_TABLE:
                setChecked(rb_table, isCheck);
                break;
        }
    }


    public void setCompoundButtonEnable(int type, boolean enable) {
        switch (type) {
            case TYPE_CARD:
                setEnable(rb_card, enable);
                break;
            case TYPE_INTEGRAL:
                setEnable(rb_integral, enable);
                break;
            case TYPE_PARTY:
                setEnable(rb_party, enable);
                break;
            case TYPE_COUPON:
                setEnable(rb_coupon, enable);
                break;
            case TYPE_DISCOUNT:
                setEnable(rb_discount, enable);
                break;
            case TYPE_ACTIVITY:
                setEnable(rb_activity, enable);
                break;
            case TYPE_EXTRA:
                setEnable(rb_extra, enable);
                break;
            case TYPE_REMARK:
                setEnable(rb_remark, enable);
                break;
            case TYPE_TABLE:
                setEnable(rb_table, enable);
                break;
        }
    }


    public void doLoginCustomer() {
        setEnable(rb_card, !BeautyOrderManager.isBuyServerBus());        setEnable(rb_integral, true);
        setEnable(rb_coupon, true);
        btn_login.setText("登出");
    }


    public void doLoginOutCustomer() {
        setEnable(rb_card, false);
        setEnable(rb_integral, false);
        setEnable(rb_coupon, false);
        btn_login.setText(R.string.login_in);
    }

    public void doClearCheckedStatus() {
        if (custom_rb != null) {
            custom_rb.clearCheck();
        }
    }

    private void setEnable(View view, boolean enable) {
        if (view != null) {
            view.setEnabled(enable);
        }
    }

    private void setChecked(CompoundButton view, boolean isChecked) {
        if (view != null) {
            view.setChecked(isChecked);
        }
    }


    @Override
    public void onCheckedChanged(NestRadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_card:                if (mOperatorListener != null) {
                    mOperatorListener.onCardClick();
                }
                break;
            case R.id.rb_integral:                if (mOperatorListener != null) {
                    mOperatorListener.onIntegralClick();
                }
                break;
            case R.id.rb_party:                if (mOperatorListener != null) {
                    mOperatorListener.onPartyClick();
                }
                break;
            case R.id.rb_coupon:                if (mOperatorListener != null) {
                    mOperatorListener.onCouponClick();
                }
                break;
            case R.id.rb_discount:                if (mOperatorListener != null) {
                    mOperatorListener.onDiscountClick();
                }
                break;
            case R.id.rb_activity:                if (mOperatorListener != null) {
                    mOperatorListener.onActivityClick();
                }
                break;
            case R.id.rb_extra:
                if (mOperatorListener != null) {
                    mOperatorListener.onExtraClick();
                }
                break;
            case R.id.rb_remark:                if (mOperatorListener != null) {
                    mOperatorListener.onRemarkClick();
                }
                break;
            case R.id.rb_table:                if (mOperatorListener != null) {
                    mOperatorListener.onTableClick();
                }
                break;
        }

    }

    public void onEventMainThread(BeautyOrderCustomerEvent event) {
        if (event.mEventFlag == BeautyOrderCustomerEvent.EventFlag.LOGIN) {
                        if (btn_login != null) {
                btn_login.setText(getResources().getString(R.string.login_out));
            }
        } else {
                        if (btn_login != null) {
                btn_login.setText(getResources().getString(R.string.login_in));
            }
        }
    }
}
