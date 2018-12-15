package com.zhongmei.bty.basemodule.orderdish.adapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.orderdish.bean.DishQuantityBean;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.fragment.ChoiceSeatDialogFragment;
import com.zhongmei.bty.basemodule.trade.enums.OperateType;
import com.zhongmei.bty.basemodule.trade.utils.DinnerUtils;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public class DishQuantityAdapter extends BaseAdapter {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private LayoutInflater mInflater;
    private ViewHold holdView;
    private List<DishQuantityBean> moveDishItems;
    private OnQuantityChangeListener mQuantityChangeListener;
    private List<TableSeat> mTableSeats;
    private OperateType mOperateType;
    private boolean isAllowQuantity = true;//是否允许操作数量，默认允许

    public DishQuantityAdapter(Context mContext, FragmentManager fm, List<DishQuantityBean> moveShopcartItems) {
        this.mContext = mContext;
        this.mFragmentManager = fm;
        this.moveDishItems = moveShopcartItems;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.mQuantityChangeListener = listener;
    }

    public void setData(List<DishQuantityBean> moveDishItems) {
        this.moveDishItems = moveDishItems;
        notifyDataSetChanged();
    }

    public void setAllowQuantity(boolean allowQuantity) {
        isAllowQuantity = allowQuantity;
    }

    public void setmOperateType(OperateType mOperateType) {
        this.mOperateType = mOperateType;
        setAllowQuantity(this.mOperateType == OperateType.MOVE_DISH);
    }

    public void setTableSeats(List<TableSeat> tableSeats) {
        this.mTableSeats = tableSeats;
    }

    @Override
    public int getCount() {
        return this.moveDishItems == null ? 0 : this.moveDishItems.size();
    }

    @Override
    public DishQuantityBean getItem(int position) {
        if (this.moveDishItems != null) {
            return this.moveDishItems.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.orderdish_item_movedish_quantiay, null);
            holdView = new ViewHold();
            holdView.tv_dishName = (TextView) convertView.findViewById(R.id.tv_dishName);
            holdView.ib_decrease = (ImageButton) convertView.findViewById(R.id.ib_decrease);
            holdView.ib_increase = (ImageButton) convertView.findViewById(R.id.ib_increase);
            holdView.tv_dishQuantity = (TextView) convertView.findViewById(R.id.tv_dishQuantity);
            holdView.rl_seatNo = (RelativeLayout) convertView.findViewById(R.id.rl_seat_no);
            holdView.tv_seatNoHint = (TextView) convertView.findViewById(R.id.tv_seat_hint);
            holdView.tv_seatNo = (TextView) convertView.findViewById(R.id.tv_seat_no);
            convertView.setTag(holdView);
        } else {
            holdView = (ViewHold) convertView.getTag();
        }

        DishQuantityBean shopcartItem = getItem(position);
        holdView.tv_dishName.setText(shopcartItem.shopcartItem.getSkuName());

        holdView.tv_dishQuantity.setText(MathDecimal.trimZero(shopcartItem.quantity).toString());

//        if(shopcartItem.shopcartItem.getSaleType().value() == SaleType.WEIGHING.value()){
//            holdView.tv_dishQuantity.setText(String.valueOf(shopcartItem.quantity));
//        }else {
//            holdView.tv_dishQuantity.setText(String.valueOf(shopcartItem.quantity.intValue()));
//        }

        if (Utils.isEmpty(mTableSeats) || !DinnerUtils.isWestStyle() || shopcartItem.shopcartItem.getType() != DishType.SINGLE) {
            holdView.rl_seatNo.setVisibility(View.GONE);
        } else {
            holdView.tv_seatNoHint.setText(mContext.getString(R.string.dinner_table_seat_hint, mOperateType.desc()));
            holdView.tv_seatNo.setText(shopcartItem.tableSeat == null ? "" : shopcartItem.tableSeat.getSeatName());
            holdView.rl_seatNo.setVisibility(View.VISIBLE);
        }


        OnQuantityClickListener quantityClickListener = new OnQuantityClickListener(position);
        holdView.ib_increase.setOnClickListener(quantityClickListener);
        holdView.ib_decrease.setOnClickListener(quantityClickListener);
        holdView.rl_seatNo.setOnClickListener(quantityClickListener);

        //设置button的状态
        setBtnStatus(shopcartItem, holdView);
        return convertView;
    }


    /**
     * 控制数量操作后Button的状态
     *
     * @param shopcartItem
     * @param holdView
     */
    public void setBtnStatus(DishQuantityBean shopcartItem, ViewHold holdView) {
        ReadonlyShopcartItemBase readonlyShopcartItemBase = (ReadonlyShopcartItemBase) shopcartItem.shopcartItem;
        if (!isAllowQuantity) {
            holdView.ib_increase.setVisibility(View.GONE);
            holdView.ib_decrease.setVisibility(View.GONE);
            return;
        }

        holdView.ib_increase.setVisibility(View.VISIBLE);
        holdView.ib_decrease.setVisibility(View.VISIBLE);

        //称重商品或者商品数量为1
        if (readonlyShopcartItemBase.getSaleType().value() == SaleType.WEIGHING.value() || shopcartItem.shopcartItem.getSingleQty().intValue() == 1) {
            holdView.ib_increase.setEnabled(false);
            holdView.ib_decrease.setEnabled(false);
            return;
        }

        //如果商品数量达到最大值
        if (shopcartItem.quantity.compareTo(shopcartItem.shopcartItem.getSingleQty()) == 0) {
            holdView.ib_increase.setEnabled(false);
            holdView.ib_decrease.setEnabled(true);
            return;
        }

        //如果商品数量达到下限1
        if (shopcartItem.quantity.compareTo(BigDecimal.ONE) <= 0) {
            holdView.ib_increase.setEnabled(true);
            holdView.ib_decrease.setEnabled(false);
            return;
        }


        holdView.ib_increase.setEnabled(true);
        holdView.ib_decrease.setEnabled(true);
    }

    class OnQuantityClickListener implements View.OnClickListener {
        private int mPosition;

        public OnQuantityClickListener(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            DishQuantityBean dishBean = getItem(mPosition);
            boolean isWeighing = dishBean.shopcartItem.getSaleType().value() == SaleType.WEIGHING.value() ? true : false;
            if (v.getId() == R.id.ib_decrease) {
                if (mQuantityChangeListener != null && dishBean.quantity.compareTo(BigDecimal.ONE) > 0) {
                    dishBean.quantity = dishBean.quantity.subtract(BigDecimal.ONE);
                    mQuantityChangeListener.onQuantityChange(mPosition, dishBean.quantity, isWeighing);
                }
            } else if (v.getId() == R.id.ib_increase) {
                if (mQuantityChangeListener != null && dishBean.quantity.compareTo(dishBean.shopcartItem.getSingleQty()) < 0) {
                    dishBean.quantity = dishBean.quantity.add(BigDecimal.ONE);
                    mQuantityChangeListener.onQuantityChange(mPosition, dishBean.quantity, isWeighing);
                }
            } else if (v.getId() == R.id.rl_seat_no) {
                //跳转到座位号选择
                showChoiceSeatDialog(mPosition, dishBean);
            }
        }
    }

    private void showChoiceSeatDialog(final int position, final DishQuantityBean dishBean) {
        CommonDialogFragment.CommonDialogFragmentBuilder builder = new ChoiceSeatDialogFragment.ChoiceSeatDialogFragmentBuilder(BaseApplication.sInstance);
        DialogFragment fragment = builder.headTitle(dishBean.shopcartItem.getSkuName()).iconType(R.drawable.commonmodule_dialog_icon_warning)
                .positiveText(R.string.orderdish_dinner_ok)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(final View arg0) {// 确定
                        //返回一个座位号
                        Object obj = arg0.getTag();
                        if (mQuantityChangeListener != null) {
                            String seatName = null;
                            TableSeat tableSeat = null;
                            if (obj != null) {
                                tableSeat = (TableSeat) arg0.getTag();
                                seatName = tableSeat.getSeatName();
                            }
                            dishBean.tableSeat = tableSeat;
                            mQuantityChangeListener.onChoiceSeats(position, seatName);
                        }
                    }
                }).negativeText(R.string.orderdish_dinner_cancel)
                .negativeLisnter(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {// 取消
                    }
                })
                .build();

        ((ChoiceSeatDialogFragment) fragment).setData(mTableSeats, dishBean);
        ((ChoiceSeatDialogFragment) fragment).setmOperateType(mOperateType);

        fragment.show(mFragmentManager, "ChoiceTableSeats");
    }

    public class ViewHold {
        public TextView tv_dishName;
        public ImageButton ib_decrease;
        public ImageButton ib_increase;
        public TextView tv_dishQuantity;
        public RelativeLayout rl_seatNo;
        public TextView tv_seatNoHint;
        public TextView tv_seatNo;
    }

    /**
     * 移菜分数改变的回调
     */
    public interface OnQuantityChangeListener {
        void onQuantityChange(int position, BigDecimal quantity, boolean isWeighing);

        void onChoiceSeats(int position, String seatName);
    }
}
