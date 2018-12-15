package com.zhongmei.bty.dinner.orderdish.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.utils.DinnerUtils;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerShopCartAdapter;

import java.util.List;

/**
 * @Description:等叫，起菜，崔菜操作栏
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class DishOperatePopWindow extends PopupWindow implements View.OnTouchListener, View.OnClickListener {
    private View parentView;


    private TextChangedLisener lisener;
    private OperateChangeListener operateChangeListener;
    private TextView dishPrepareTv;//叫菜

    private TextView dishMakeTv;//起菜

    private TextView dishCancelPrepareTv;    //取消叫菜

    private TextView dishCancelMakeTv;        //取消起菜

    private TextView dishUrgeTv;//崔菜

    private TextView tradeMemoTv;//整单备注

    private TextView tvBatchOperation;//批量操作
    private TextView tvDishSequence;
    private View tvPadding;

    private boolean isAllowBatchOperation = false;

    //是否为反结账订单
    public boolean isReturnCash = false;

    private ChangePageListener changePageListener;//整单备注接口回调
    private MainBaseActivity mActivitiy;

    private DinnerShopCartAdapter mAdapter;

    private int contentWidth;
    private boolean isDinner;

    public DishOperatePopWindow(MainBaseActivity activity, OperateChangeListener listener, View parentView,
                                int contentWidth, boolean isAllowBatchOperation, boolean isDinner) {
        super();
        mActivitiy = activity;
        operateChangeListener = listener;
        this.parentView = parentView;
        this.contentWidth = contentWidth;
        this.isAllowBatchOperation = isAllowBatchOperation;
        this.isDinner = isDinner;
        initialView(activity);
    }

    private void initialView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dish_operate_popwindow, null);
        setContentView(view);
        setWidth(contentWidth);
        setHeight(LayoutParams.MATCH_PARENT);
        setTouchable(true);
        setTouchInterceptor(this);
        setBackgroundDrawable(new ColorDrawable(0));

        LinearLayout contentView = (LinearLayout) view.findViewById(R.id.content_ll);
        contentView.getLayoutParams().width = contentWidth;

//		setAnimationStyle(R.style.dinner_table_info_waiterwindow_style);

        dishPrepareTv = (TextView) view.findViewById(R.id.dish_prepare_tv);
        dishMakeTv = (TextView) view.findViewById(R.id.dish_make_tv);
        dishUrgeTv = (TextView) view.findViewById(R.id.dish_urge_tv);
        tradeMemoTv = (TextView) view.findViewById(R.id.trade_memo_tv);
        tvBatchOperation = (TextView) view.findViewById(R.id.tv_batch_operation);
        tvDishSequence = (TextView) view.findViewById(R.id.tv_dish_sequence);

        dishCancelPrepareTv = (TextView) view.findViewById(R.id.dish_cancel_prepare_tv);
        dishCancelMakeTv = (TextView) view.findViewById(R.id.dish_cancel_make_tv);
        tvPadding = view.findViewById(R.id.padding_tv);

        dishPrepareTv.setOnClickListener(this);
        dishMakeTv.setOnClickListener(this);
        dishUrgeTv.setOnClickListener(this);
        tradeMemoTv.setOnClickListener(this);
        dishCancelPrepareTv.setOnClickListener(this);
        dishCancelMakeTv.setOnClickListener(this);
        tvDishSequence.setOnClickListener(this);

        if (isAllowBatchOperation) {
            tvBatchOperation.setVisibility(View.VISIBLE);
            tvBatchOperation.setOnClickListener(this);
        } else {
            tvBatchOperation.setVisibility(View.GONE);
        }

        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo != null && tradeVo.getTrade() != null
                && tradeVo.getTrade().getBusinessType() == BusinessType.BUFFET) {
            dishPrepareTv.setVisibility(View.GONE);
            dishMakeTv.setVisibility(View.GONE);
            dishUrgeTv.setVisibility(View.VISIBLE);
            dishCancelPrepareTv.setVisibility(View.GONE);
            dishCancelMakeTv.setVisibility(View.GONE);
        }
        dishCancelPrepareTv.setVisibility(View.VISIBLE);
    }

    public void show() {
        int all_count = 5;
        int count = 0;

        //反结账订单不允许进行等叫、起菜、取消等叫、取消起菜和催菜操作
        if (isReturnCash) {
            dishPrepareTv.setVisibility(View.GONE);
            dishMakeTv.setVisibility(View.GONE);
            dishUrgeTv.setVisibility(View.GONE);
            dishCancelPrepareTv.setVisibility(View.GONE);
            dishCancelMakeTv.setVisibility(View.GONE);
            tvDishSequence.setVisibility(View.GONE);
        } else {
            if (isDinner)
                tvDishSequence.setVisibility(DinnerUtils.isWestStyle() && DinnerUtils.isServingStyle() ? View.VISIBLE : View.GONE);
            else
                tvDishSequence.setVisibility(View.GONE);
            if (hasOperationDish(PrintOperationOpType.WAKE_UP_CANCEL)) {
                dishCancelPrepareTv.setVisibility(View.VISIBLE);
                count++;
            } else
                dishCancelPrepareTv.setVisibility(View.GONE);
            if (hasOperationDish(PrintOperationOpType.RISE_DISH_CANCEL)) {
                count++;
                dishCancelMakeTv.setVisibility(View.VISIBLE);
            } else
                dishCancelMakeTv.setVisibility(View.GONE);
        }

        if (parentView != null) {
            if (!isShowing()) {
                showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
            }
        }
        if (count > 0) {
            tvPadding.setVisibility(View.VISIBLE);
            tvPadding.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, all_count - count));
        } else
            tvPadding.setVisibility(View.GONE);

    }

    public void hide() {
        if (isShowing()) {
            dismiss();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (inRangeOfView(getContentView(), event)) {
            return false;
        } else {
            hide();
            return true;
        }
    }

    private boolean inRangeOfView(View view, MotionEvent ev) {
        View contentView = getContentView();
        int x = contentView.getPaddingLeft();
        int y = contentView.getPaddingTop();
        View contentView2 = contentView.findViewById(R.id.content_ll);
        int width = contentView2.getWidth();
        int height = contentView2.getHeight();
        if (ev.getX() < x || ev.getX() > (x + width) || ev.getY() < y || ev.getY() > (y + height)) {
            return false;
        }
        return true;
    }

    public void setAdapter(DinnerShopCartAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void setListener(TextChangedLisener lisener) {
        this.lisener = lisener;
    }

    public interface TextChangedLisener {
        void onTextChanged(String content);
    }

    /**
     * @Date 2016年4月25日
     * @Description:
     * @Param
     */
    @Override
    public void onClick(View v) {
        hide();
        // 展示遮罩
        if (v.getId() != R.id.trade_memo_tv && v.getId() != R.id.tv_dish_sequence) {
            if (mActivitiy != null) {
                mActivitiy.showShadow(true);
            }
        }

        switch (v.getId()) {
            case R.id.dish_prepare_tv:// 等叫
                MobclickAgentEvent.onEvent(UserActionCode.ZC020005);
                operateChangeListener.changeDishCheckMode(PrintOperationOpType.WAKE_UP);
                break;
            case R.id.dish_cancel_prepare_tv://取消等叫
                MobclickAgentEvent.onEvent(UserActionCode.ZC020025);
                operateChangeListener.changeDishCheckMode(PrintOperationOpType.WAKE_UP_CANCEL);
                break;
            case R.id.dish_make_tv:// 起菜
                MobclickAgentEvent.onEvent(UserActionCode.ZC020006);
                operateChangeListener.changeDishCheckMode(PrintOperationOpType.RISE_DISH);
                break;
            case R.id.dish_cancel_make_tv:    //取消起菜
                MobclickAgentEvent.onEvent(UserActionCode.ZC020026);
                operateChangeListener.changeDishCheckMode(PrintOperationOpType.RISE_DISH_CANCEL);
                break;
            case R.id.dish_urge_tv:// 崔菜
                MobclickAgentEvent.onEvent(UserActionCode.ZC020007);
                operateChangeListener.changeDishCheckMode(PrintOperationOpType.REMIND_DISH);
                break;
            case R.id.trade_memo_tv:
                MobclickAgentEvent.onEvent(UserActionCode.ZC020008);
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.NONEEDCHECK, false);
                changePageListener.changePage(ChangePageListener.ORDER_COMMENTS, bundle);
                break;
            case R.id.tv_batch_operation:
                MobclickAgentEvent.onEvent(UserActionCode.ZC020009);
                operateChangeListener.changeDishCheckMode(PrintOperationOpType.BATCH_OPERATION);
                break;
            case R.id.tv_dish_sequence:
                operateChangeListener.changeDishCheckMode(PrintOperationOpType.DISH_SEQUENCE);
                break;
            default:
                break;
        }

    }

    public void setChangePageListener(ChangePageListener listener) {
        changePageListener = listener;
    }


    public interface OperateChangeListener {
        public void changeDishCheckMode(PrintOperationOpType type);
    }


    private boolean hasOperationDish(PrintOperationOpType type) {
        //DinnerShoppingCart shoppingCart = DinnerShoppingCart.getInstance();
        //List<IShopcartItem> shopcartItemList =  shoppingCart.getShoppingCartDish();
        List<DishDataItem> dishDataItems = mAdapter.getAllData();
        if (dishDataItems == null || dishDataItems.isEmpty())
            return false;
        for (DishDataItem item : dishDataItems) {
            if (DinnerTradeItemManager.getInstance().getDishCheckStatus(item, type) == DishDataItem.DishCheckStatus.NOT_CHECK)
                return true;
        }
		/*
		if(shopcartItemList == null || shopcartItemList.isEmpty())
			return false;
		for (IShopcartItemBase shopcartItem : shopcartItemList){
			if(type == PrintOperationOpType.WAKE_UP_CANCEL
					&& DinnerTradeItemManager.getInstance().getWakeUpCancelCheckStatus(shopcartItem) == DishDataItem.DishCheckStatus.NOT_CHECK){
				return true;
			}else if(type == PrintOperationOpType.RISE_DISH_CANCEL
					&& DinnerTradeItemManager.getInstance().getRiseUpCancelCheckStatus(shopcartItem) == DishDataItem.DishCheckStatus.NOT_CHECK){
				return true;
			}
		}
		*/
        return false;
    }

    /**
     * 是否存在operationOpType类型s
     *
     * @param tradeItemOperations
     * @param operationOpType
     * @return
     */
    private boolean hasOperationOpType(List<TradeItemOperation> tradeItemOperations, PrintOperationOpType operationOpType) {
        if (Utils.isEmpty(tradeItemOperations))
            return false;
        for (TradeItemOperation operation : tradeItemOperations) {
            if (operation.getOpType() == operationOpType
                    && operation.getStatusFlag() == StatusFlag.VALID) {
                return true;
            }
        }
        return false;
    }

}
