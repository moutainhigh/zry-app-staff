package com.zhongmei.beauty.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhongmei.bty.mobilepay.event.ActionClose;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.beauty.order.BeautyOrderManager;
import com.zhongmei.beauty.customer.BeautyCustomerActivity;
import com.zhongmei.beauty.customer.BeautyCustomerActivity_;
import com.zhongmei.beauty.customer.BeautyCustomerCardDialogFragment;
import com.zhongmei.beauty.customer.BeautyCustomerEditActivity_;
import com.zhongmei.beauty.customer.BeautyCustomerLoginDialogFragment;
import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants;
import com.zhongmei.beauty.order.event.ActionClearShopcart;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.customer.CustomerActivity;
import com.zhongmei.yunfu.util.ValueEnums;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.beauty_order_left_tar_layout)
public class BeautyOrderLeftTbarFragment extends BasicFragment {
    @ViewById(R.id.beauty_serial_number)
    TextView tv_serial_number;

    @ViewById(R.id.beauty_order_customer_room)
    TextView tv_room;

    @ViewById(R.id.beauty_order_label)
    TextView tv_beautyOrderTitle;

    private BusinessType mBusType = BusinessType.BEAUTY;

    @AfterViews
    public void initView() {
        setRoomName("");
        if (isBuyServer(mBusType)) {
                        tv_room.setVisibility(View.GONE);
            tv_beautyOrderTitle.setText(R.string.beauty_buy_service);
        }
    }


    public void initSerialView() {
        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo != null && tradeVo.getTradeExtra() != null && !TextUtils.isEmpty(tradeVo.getTradeExtra().getSerialNumber())) {
            tv_serial_number.setText(getResources().getString(R.string.beauty_serialnumber,
                    DinnerShoppingCart.getInstance().getOrder().getTradeExtra().getSerialNumber()));
        }
    }

    public void setBusinessType(BusinessType busType) {
        this.mBusType = busType;
    }

    private boolean isBuyServer(BusinessType busType) {
        return ValueEnums.equalsValue(busType, ValueEnums.toValue(BusinessType.CARD_TIME));
    }

    @Click({R.id.beauty_order_back, R.id.btn_clear, R.id.btn_add_customer, R.id.btn_bank_card, R.id.btn_recharge})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.beauty_order_back:
                getActivity().finish();
                break;
            case R.id.btn_clear:
                clearShoppingCart();
                break;
            case R.id.btn_add_customer:
                addCustomer();
                break;
            case R.id.btn_bank_card:
                saleCard();
                break;
            case R.id.btn_recharge:
                                if (CustomerManager.getInstance().getDinnerLoginCustomer() != null) {
                                        showChargingCard();
                } else {
                                        new BeautyCustomerLoginDialogFragment().show(getActivity().getSupportFragmentManager(), "BeautyCustomerLoginDialogFragment");
                }
                break;
        }
    }

    public void setRoomName(String roomName) {
        if (TextUtils.isEmpty(roomName)) {
            roomName = "无";
        }

        String str_roomName = getResources().getString(R.string.beauty_order_room) + roomName;
        tv_room.setText(str_roomName);
    }


    private void showChargingCard() {
        BeautyCustomerCardDialogFragment dialog = new BeautyCustomerCardDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BeautyCustomerConstants.KEY_CUSTOMER_INFO, CustomerManager.getInstance().getDinnerLoginCustomer());
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(), "BeautyCustomerCardDialogFragment");
    }

    private void addCustomer() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BeautyCustomerEditActivity_.class);
        intent.putExtra("type", CustomerActivity.PARAM_ADD);
        startActivity(intent);
    }

    private void saleCard() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BeautyCustomerActivity_.class);
        intent.putExtra(BeautyCustomerActivity.BUNDER_FLAG, BeautyCustomerConstants.CustomerStartFlag.ENTITY_CARD);
        startActivity(intent);
    }

    private void clearShoppingCart() {
        BeautyOrderManager.clearShopcartDish();
        EventBus.getDefault().post(new ActionClearShopcart());
        EventBus.getDefault().post(new ActionClose());
    }
}
