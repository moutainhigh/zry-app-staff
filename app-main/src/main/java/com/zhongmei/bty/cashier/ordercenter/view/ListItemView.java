package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.order_center_list_item_layout)
public class ListItemView extends RelativeLayout {
    @ViewById(R.id.order_center_list_root_layout)
    RelativeLayout mRootLayout;
    @ViewById(R.id.order_center_item_select_check_box)
    CheckBox mCheckBox;
    @ViewById(R.id.order_center_list_item_icon)
    ImageView mIcon;
    @ViewById(R.id.order_center_list_order_number)
    TextView mOrderNumber;
    @ViewById(R.id.order_status_layout)
    LinearLayout mOrderStatusLayout;
    @ViewById(R.id.order_center_list_table_number)
    TextView mTableNumber;
    @ViewById(R.id.order_center_list_phone_number)
    TextView mPhoneNumber;
    @ViewById(R.id.order_center_list_appointment)
    ImageView mAppointment;
    @ViewById(R.id.order_center_list_address)
    TextView mAddress;
    @ViewById(R.id.order_center_list_time)
    TextView mTime;
    @ViewById(R.id.order_center_list_expected_time)
    TextView mExpectTime;
    @ViewById(R.id.order_center_list_item_broadcast)
    ImageView mBroadcast;
    @ViewById(R.id.order_center_list_item_more)
    ImageView mMore;
    @ViewById(R.id.order_center_list_take_dish_status)
    TextView mTakeDishStatus;
    @ViewById(R.id.order_status_layout_top)
    LinearLayout mOrderStatusLayoutTop;
    @ViewById(R.id.order_center_list_union_trade)
    TextView mUnionLabel;//联台单标示

    public ListItemView(Context context) {
        super(context);
    }

    public RelativeLayout getRootLayout() {
        return mRootLayout;
    }

    public CheckBox getCheckBox() {
        return mCheckBox;
    }

    public ImageView getIcon() {
        return mIcon;
    }

    public LinearLayout getOrderStatusLayout() {
        return mOrderStatusLayout;
    }

    public LinearLayout getmOrderStatusLayoutTop() {
        return mOrderStatusLayoutTop;
    }

    public TextView getOrderNumber() {
        return mOrderNumber;
    }

    public TextView getTableNumber() {
        return mTableNumber;
    }

    public TextView getPhoneNumber() {
        return mPhoneNumber;
    }

    public ImageView getAppointment() {
        return mAppointment;
    }

    public TextView getAddress() {
        return mAddress;
    }

    public TextView getTime() {
        return mTime;
    }

    public TextView getExpectTime() {
        return mExpectTime;
    }

    public ImageView getBroadcast() {
        return mBroadcast;
    }

    public ImageView getMore() {
        return mMore;
    }

    public TextView getTakeDishStatus() {
        return mTakeDishStatus;
    }

    public TextView getUnionLabel() {
        return mUnionLabel;
    }
}
