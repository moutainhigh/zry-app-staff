package com.zhongmei.bty.customer.views;

/**
 * Created by demo on 2018/12/15
 */

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.customer_save_dialog)
public class SaveCustomerDailog extends BasicDialogFragment {

//    @ViewById(R.id.progressbar)
//    private ProgressBar progressBar;


    @ViewById(R.id.card_btn)
    Button cardStoreBtn;
    @ViewById(R.id.return_btn)
    Button returnListBtn;
    @ViewById(R.id.entity_card_btn)
    Button entityCardStoreBtn;

    private CustomerResp mCustomer;
    DialogBtnClickListener mlistener;

    public interface DialogBtnClickListener {
        void cardStoreBtnClick();

        void entityCardBtnClick();

        void returnListBtnClick();
    }

    @AfterViews
    void initView() {
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
    }

    @Click({R.id.card_btn, R.id.entity_card_btn, R.id.return_btn})
    void clickListener(View view) {
        if (mlistener == null) {
            return;
        }

        switch (view.getId()) {
            case R.id.return_btn:
                MobclickAgentEvent.onEvent(UserActionCode.GK020008);
                mlistener.returnListBtnClick();
                break;
            case R.id.card_btn:
                MobclickAgentEvent.onEvent(UserActionCode.GK020009);
                mlistener.cardStoreBtnClick();
                break;
            case R.id.entity_card_btn:
                MobclickAgentEvent.onEvent(UserActionCode.GK020010);
                mlistener.entityCardBtnClick();
                break;
        }
        dismiss();
    }

    public void setCustomer(CustomerResp customer) {
        this.mCustomer = customer;
    }

    public void setOnClick(DialogBtnClickListener listener) {
        this.mlistener = listener;
    }
}
