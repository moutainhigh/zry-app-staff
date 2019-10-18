package com.zhongmei.bty.customer.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;

@EFragment(R.layout.customer_charging_dialogfragment)
public class ListConfirmDialogFragment extends BasicDialogFragment {

    public static final String TITLE = "title";

    protected String[] mItemStrings;

    @FragmentArg(TITLE)
    protected String mTitleString;

    @ViewById(R.id.confirm_title)
    TextView mTitle;

    @ViewById(R.id.count)
    TextView mCount;

    @ViewById(R.id.phone)
    TextView mPhone;

    @ViewById(R.id.amount)
    TextView mAmount;

    @ViewById(R.id.type)
    TextView mType;

    private OnClickListener mListener = null;

    private String account;
    public void setAccount(String account) {
        this.account = account;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setChargeAmonut(BigDecimal chargeAmonut) {
        this.chargeAmonut = chargeAmonut;
    }

    private String phoneNumber;
    private BigDecimal chargeAmonut;
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    @AfterViews
    protected void initView() {
        getDialog().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mTitle.setText(getString(R.string.customer_confirm));                if (account != null) {
            mCount.setText(account);
        } else {
            mCount.setText("");
        }

                if (phoneNumber != null) {
            mPhone.setText(phoneNumber);
        } else {
            mPhone.setText("");
        }
        String amountString = "";
                if (chargeAmonut != null) {
            amountString = ShopInfoCfg.formatCurrencySymbol(chargeAmonut.toString());
            mAmount.setText(amountString);
        }
            }

    protected void setItemList(String[] items) {
        mItemStrings = items;
    }

    @Click({R.id.ok, R.id.back, R.id.close})
    protected void onClick(View v) {
        switch (v.getId()) {

            case R.id.ok:
                if (!ClickManager.getInstance().isClicked(R.id.ok, 600)) {
                                        if (mListener != null) {
                        mListener.onClick(v);
                    }
                }

                break;
            case R.id.back:
            case R.id.close:
                dismiss();
                DisplayServiceManager.doCancel(getActivity());
                break;
            default:
                break;
        }
    }

    public void setOnConfirmClick(OnClickListener listener) {
        mListener = listener;
    }



    static class ViewHolder {
        TextView infoTextView;
    }

    public static class ListItem {
        int drawable = 0;

        String text = "";

        public ListItem(int resID, String string) {
            drawable = resID;
            text = string;
        }
    }
}
