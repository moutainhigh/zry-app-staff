package com.zhongmei.bty.dinner.cash;



import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.customer_login_save_dialog)
public class DinnerCustomerLoginSaveDialog extends BasicDialogFragment {



    @ViewById(R.id.card_store)
    Button cardStore;
    @ViewById(R.id.entity_card)
    Button entityCard;
    @ViewById(R.id.close)
    Button close;

    private CustomerResp mCustomer;
    OnDialogBtnClickListener mlistener;

    public interface OnDialogBtnClickListener {
        void cardStoreBtnClick();

        void entityCardBtnClick();

        void closeBtnClick();
    }

    @AfterViews
    void initView() {
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
    }

    @Click({R.id.close, R.id.entity_card, R.id.card_store})
    void clickListener(View view) {
        if (mlistener == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.close:
                mlistener.closeBtnClick();
                break;
            case R.id.card_store:
                mlistener.cardStoreBtnClick();
                break;
            case R.id.entity_card:
                mlistener.entityCardBtnClick();
                break;
        }
        dismiss();
    }

    public void setCustomer(CustomerResp customer) {
        this.mCustomer = customer;
    }

    public void setOnClick(OnDialogBtnClickListener listener) {
        this.mlistener = listener;
    }
}
