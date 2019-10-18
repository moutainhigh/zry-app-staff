package com.zhongmei.bty.basemodule.orderdish.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.orderdish.adapter.DishQuantityAdapter;
import com.zhongmei.bty.basemodule.orderdish.bean.DishQuantityBean;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModelMoveDish;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTradeMoveDish;
import com.zhongmei.bty.basemodule.trade.enums.OperateType;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EFragment(resName = "orderdish_fragment_move_dish_quantity_dialog")
public class DishQuantityAndSeatEditDialogFragment extends CommonDialogFragment implements DishQuantityAdapter.OnQuantityChangeListener {
    @ViewById(resName = "tv_head_title")
    public TextView tv_headTitle;


    @ViewById(resName = "ll_dish_quantity")
    public LinearLayout ll_dishQuantity;

    @ViewById(resName = "lv_dishQuantity")
    public ListView lv_dishQuantity;

    @ViewById(resName = "ll_property")
    public LinearLayout ll_property;

    @ViewById(resName = "print_kitchen_cb")
    public CheckBox printKitchenCb;



    public List<DishQuantityBean> mDishQuantityBeans = new ArrayList<>();

    private DishQuantityAdapter mDishQuantityAdapter;

    private OperateType mOperateType;

    private List<TableSeat> mTableSeats;

    @AfterViews
    protected void initView() {
        super.initView();
        super.mNegativeButton.setText("");
                mDishQuantityAdapter = new DishQuantityAdapter(getActivity(), getFragmentManager(), mDishQuantityBeans);
        mDishQuantityAdapter.setOnQuantityChangeListener(this);
        mDishQuantityAdapter.setTableSeats(mTableSeats);
        mDishQuantityAdapter.setmOperateType(mOperateType);
        lv_dishQuantity.setAdapter(mDishQuantityAdapter);
        showProperty();
        showHeaddTitle();
            }

    public void setmOperateType(OperateType mOperateType) {
        this.mOperateType = mOperateType;
    }


    private void showProperty() {
        if (mOperateType != null && mOperateType == OperateType.COPY_DISH) {
            ll_property.setVisibility(View.VISIBLE);
            ll_dishQuantity.setVisibility(View.GONE);
        } else {
            ll_property.setVisibility(View.GONE);
        }


    }

    private void showHeaddTitle() {
        if (tv_headTitle != null && !TextUtils.isEmpty(headTitle)) {
            tv_headTitle.setText(headTitle);
        }
    }

    public void setData(IDinnertableTradeMoveDish orginal, List<TableSeat> tableSeats) {
        if (orginal == null) {
            return;
        }

        DinnertableTradeModelMoveDish modelMoveDish = (DinnertableTradeModelMoveDish) orginal;

        setData(modelMoveDish.getItems(), tableSeats);
    }

    public void setData(List<IShopcartItem> iShopcartItems, List<TableSeat> tableSeats) {
        this.mTableSeats = tableSeats;

        for (IShopcartItem shopartItem : iShopcartItems) {
            if (shopartItem.getSingleQty().compareTo(BigDecimal.ZERO) > 0 && shopartItem.getStatusFlag() == StatusFlag.VALID) {
                mDishQuantityBeans.add(new DishQuantityBean(shopartItem));
            }
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
                ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        if (listAdapter.getCount() < 5) {
            for (int i = 0; i < listAdapter.getCount(); i++) {                 View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);                 totalHeight += listItem.getMeasuredHeight();             }
        } else {
            for (int i = 0; i < 5; i++) {                 View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);                 totalHeight += listItem.getMeasuredHeight();             }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
                        listView.setLayoutParams(params);
    }

    @Override
    public void onQuantityChange(int position, BigDecimal quantity, boolean isWeighing) {
        View contentView = (View) lv_dishQuantity.getChildAt(position - lv_dishQuantity.getFirstVisiblePosition());
        DishQuantityAdapter.ViewHold viewHold = (DishQuantityAdapter.ViewHold) contentView.getTag();
        if (isWeighing) {
            viewHold.tv_dishQuantity.setText(String.valueOf(MathDecimal.trimZero(quantity)));
        } else {
            viewHold.tv_dishQuantity.setText(String.valueOf(MathDecimal.trimZero(quantity)));
        }
        mDishQuantityAdapter.setBtnStatus(mDishQuantityAdapter.getItem(position), viewHold);

    }


    @Override
    public void onChoiceSeats(int position, String seatName) {
        View contentView = (View) lv_dishQuantity.getChildAt(position - lv_dishQuantity.getFirstVisiblePosition());
        DishQuantityAdapter.ViewHold viewHold = (DishQuantityAdapter.ViewHold) contentView.getTag();
        viewHold.tv_seatNo.setText(TextUtils.isEmpty(seatName) ? getString(R.string.dinner_table_seat_default) : seatName);
    }


    public static class MoveDishQuantityDialogFragmentBuilder extends CommonDialogFragmentBuilder {
        public MoveDishQuantityDialogFragmentBuilder(Context context) {
            super(context);
        }

        @Override
        public CommonDialogFragment build() {
            DishQuantityAndSeatEditDialogFragment fragment = new DishQuantityAndSeatEditDialogFragment_();
            fragment.setArguments(mBundle);
            if (mNegativeListener != null) {
                fragment.setNegativeListener(mNegativeListener);
            }
            if (mpositiveLinstner != null) {
                fragment.setpositiveListener(mpositiveLinstner);
            }
            return fragment;
        }

    }

    public void setPositiveListener(View.OnClickListener listener) {
        super.setpositiveListener(listener);
    }

    public void setNegativeListener(View.OnClickListener listener) {
        super.setNegativeListener(listener);
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);
    }
}
