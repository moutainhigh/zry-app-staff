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
    public CheckBox printKitchenCb;//是否传送后厨

    @ViewById(resName = "copy_dish_property_cb")
    public CheckBox copyDishPropertyCb;//是否复制菜品属性

  /*  @ViewById(resName = "ib_close)
    ImageButton ib_close;*/

    public List<DishQuantityBean> mDishQuantityBeans = new ArrayList<>();

    private DishQuantityAdapter mDishQuantityAdapter;

    private OperateType mOperateType;

    private List<TableSeat> mTableSeats;

    @AfterViews
    protected void initView() {
        super.initView();
        super.mNegativeButton.setText("");
        //初始化数据
        mDishQuantityAdapter = new DishQuantityAdapter(getActivity(), getFragmentManager(), mDishQuantityBeans);
        mDishQuantityAdapter.setOnQuantityChangeListener(this);
        mDishQuantityAdapter.setTableSeats(mTableSeats);
        mDishQuantityAdapter.setmOperateType(mOperateType);
        lv_dishQuantity.setAdapter(mDishQuantityAdapter);
//        setListViewHeightBasedOnChildren(lv_dishQuantity);
        showProperty();
        showHeaddTitle();
//        mPositiveButton.setTag(R.id.dinner_table_dish_data,mDishQuantityBeans);
//        mPositiveButton.setTag(R.id.dinner_table_dish_property,new CheckBox[]{printKitchenCb,copyDishPropertyCb});
        //ib_close.setOnClickListener(this);
    }

    public void setmOperateType(OperateType mOperateType) {
        this.mOperateType = mOperateType;
    }

    /**
     * 是否选中后厨，复制菜品属性
     */
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
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        if (listAdapter.getCount() < 5) {
            for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }
        } else {
            for (int i = 0; i < 5; i++) { // listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
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

    /**
     * 设置座位号名称
     *
     * @param position
     * @param seatName
     */
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
        /*switch (view.getId()) {
            case R.id.ib_close:
                dismiss();
                break;
        }*/
        super.onClick(view);
    }
}
