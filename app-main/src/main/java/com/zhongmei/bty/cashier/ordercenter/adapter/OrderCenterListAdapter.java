package com.zhongmei.bty.cashier.ordercenter.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.manager.MediaPlayerQueueManager;
import com.zhongmei.util.SettingManager;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.bty.basemodule.trade.bean.DeliveryOrderVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeUnionType;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeExtraSecrecyPhone;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.bty.cashier.ordercenter.OrderCenterListFragment;
import com.zhongmei.bty.cashier.ordercenter.presenter.IOrderCenterListPresenter;
import com.zhongmei.bty.cashier.ordercenter.view.BroadcastFloatFragment;
import com.zhongmei.bty.cashier.ordercenter.view.BroadcastFloatFragment_;
import com.zhongmei.bty.cashier.ordercenter.view.ListItemView;
import com.zhongmei.bty.cashier.ordercenter.view.ListItemView_;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderStatus;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.yunfu.db.enums.DeliveryStatus;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TakeDishStatus;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.data.db.common.OrderNotify;
import com.zhongmei.bty.data.operates.impl.CallDishNotifyOperatesImpl;
import com.zhongmei.bty.db.entity.VerifyKoubeiOrder;
import com.zhongmei.bty.entity.enums.NotifyType;
import com.zhongmei.bty.snack.orderdish.adapter.RecyclerViewBaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings.SERIAL_DESK_MODE;

/**
 * 订单中心列表适配器
 */
@EBean
public class OrderCenterListAdapter extends RecyclerViewBaseAdapter<TradePaymentVo, ListItemView> {
    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private int selectPosition = -1;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private boolean mCheckBoxVisible;
    private boolean mCallNumberLayoutVisible;
    private Map<String, Integer> mOrderWeChatNotifyMap = new HashMap<String, Integer>();

    private Map<String, Integer> mOrderIVRNotifyMap = new HashMap<String, Integer>();

    private SharedPreferenceUtil pressedNumber;

    private Set<String> pressedNumberSet;

    private int checkTab = DbQueryConstant.UNPROCESSED;
    //取号通知开关
    public boolean takeMealNotice = SharedPreferenceUtil.getSpUtil().getBoolean(OrderCenterListFragment.KEY_TAKE_MEAL_NOTICE, true);

    public IPanelItemSettings settings = SettingManager.getSettings(IPanelItemSettings.class);

    private static final String IS_BROADCAST_PRESSED_KEY = "broadcast_is_pressed_key";

    public static final String IVR = "ivr";

    public static final String WEICHAT = "weichat";

    private IOrderCenterListPresenter mPresenter;
    /**
     * 外部选中的子标签
     */
    private int mChildTab;

    private boolean isFromDinner = false;

    private boolean isInBindDeliveryUserMode = false;


    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);

        void onCheckBoxClick();

        void selectBoxChange(List<TradeVo> selectedOrders);
    }

    public void setPresenter(IOrderCenterListPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setItems(List<TradePaymentVo> items) {
        super.setItems(items);
        //加载数据时选择第一项
        selectPosition = 0;
        clearSelectBoxAndNotify();
    }

    private void clearSelectBoxAndNotify() {
        mSelectedPositions.clear();
        if (mOnItemClickListener != null) {
            mOnItemClickListener.selectBoxChange(null);
        }
    }

    public void clear() {
        int itemSize = getItemCount();
        items.clear();
        clearSelectBoxAndNotify();
        notifyItemRangeRemoved(0, itemSize);
//        notifyDataSetChanged();
    }

    private void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
        if (mOnItemClickListener != null) {
            mOnItemClickListener.selectBoxChange(getSelectedOrders());
        }
    }

    public void addItem(List<TradePaymentVo> tradeVos) {
        int startPosition = getItemCount();
        items.addAll(tradeVos);
        notifyItemRangeInserted(startPosition, tradeVos.size());
//        notifyDataSetChanged();
    }

    private boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }

    @RootContext
    Context context;

    @Override
    protected ListItemView onCreateItemView(ViewGroup parent, int viewType) {
        ListItemView view = ListItemView_.build(context);
        RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(LayoutParams);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = (Integer) v.getTag();
                if (isItemChecked(position)) {
                    setItemChecked(position, false);
                } else {
                    setItemChecked(position, true);
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                    selectPosition = position;
                    notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ListItemView> holder, final int position) {
        final ListItemView view = holder.getView();
        TradePaymentVo tradePaymentVo = (TradePaymentVo) getItem(position);
        TradeVo tradeVo = tradePaymentVo.getTradeVo();
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        Trade trade = tradeVo.getTrade();
        if (selectPosition == position) {
            view.getRootLayout().setBackgroundResource(R.drawable.billcenter_bg_rectcorn);
        } else {
            view.getRootLayout().setBackground(null);
        }
        CheckBox checkBox = view.getCheckBox();
        if (mCheckBoxVisible) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }
        if (mCallNumberLayoutVisible) {
            view.getBroadcast().setVisibility(View.VISIBLE);
            view.getMore().setVisibility(View.VISIBLE);
        } else {
            view.getBroadcast().setVisibility(View.GONE);
            view.getMore().setVisibility(View.GONE);
        }
        boolean itemChecked = isItemChecked(position);
        checkBox.setChecked(itemChecked);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItemChecked(position)) {
                    setItemChecked(position, false);
                } else {
                    setItemChecked(position, true);
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onCheckBoxClick();
                }
            }
        });
        view.getBroadcast().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallDishNotifyOperatesImpl.NotifyReq broadcastNotifyReq = (CallDishNotifyOperatesImpl.NotifyReq) v.getTag();
                playBroadcastNotice(broadcastNotifyReq.getSerialNo()); //tradePaymentVo.getTradeVo().getTradeExtra().getNumberPlate();

                v.setSelected(true);
                pressedNumberSet = pressedNumber.getStringSet(IS_BROADCAST_PRESSED_KEY, null);
                if (pressedNumberSet == null) {
                    pressedNumberSet = new HashSet<String>();
                }
                if (!pressedNumber.contains(broadcastNotifyReq.getTradeUuid())) {
                    pressedNumberSet.add(broadcastNotifyReq.getTradeUuid());
                    pressedNumber.putStringSet(IS_BROADCAST_PRESSED_KEY, pressedNumberSet);
                }
            }
        });
        view.getMore().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallDishNotifyOperatesImpl.NotifyReq notifyReq = (CallDishNotifyOperatesImpl.NotifyReq) v.getTag();
                int type = notifyReq.getType();
                if (type == 3) {
                    FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    Fragment fragment = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("more");
                    if (fragment != null) {
                        ft.remove(fragment);
                    }
                    BroadcastFloatFragment floatFragment = new BroadcastFloatFragment_();
                    floatFragment.showAsRight(ft, "more", view);
                    floatFragment.setPresenterAndData(mPresenter, notifyReq, isWeChatNotified(notifyReq.getTradeUuid()),
                            isIVRNotified(notifyReq.getTradeUuid()));
                } else {
                    mPresenter.notifyVoice(type, notifyReq.getTradeUuid(), notifyReq.getMobile(),
                            notifyReq.getSerialNo(), notifyReq.getTradeNo());
                }
            }
        });
        view.setTag(position);
        setCallNumber(view, tradeVo);
        setSerialNumber(view.getOrderNumber(), tradeVo);
        setTableName(view.getTableNumber(), tradeVo);
        setOrderTime(view.getTime(), trade);
        if (checkTab == DbQueryConstant.SALES) {
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            if (tradePayStatus == TradePayStatus.PAID
                    || tradePayStatus == TradePayStatus.REFUNDED) {
                setCashInPayActual(view.getExpectTime(), tradePaymentVo.getPaymentVoList());
            } else {
                setCash(view.getExpectTime(), tradePaymentVo);
            }
        } else {
            setExpectTime(view.getExpectTime(), tradeVo);
        }
        setSourceIcon(view.getIcon(), trade);
        setPhone(view.getPhoneNumber(), tradeExtra, tradeVo.getTradeExtraSecrecyPhone());
        view.getAppointment().setVisibility(tradeVo.isAppointmentOrder() ? View.VISIBLE : View.GONE);
        setAddress(view.getAddress(), tradeExtra);
        setOrderStatus(view.getOrderStatusLayout(), tradePaymentVo);
        setOrderStatusTop(view.getmOrderStatusLayoutTop(), tradePaymentVo);
        setTakeDishStatus(view.getTakeDishStatus(), tradeVo);
        refreshDeliveryStatus(view.getTakeDishStatus(), tradePaymentVo);
        setUnionLabel(view.getUnionLabel(), tradePaymentVo);
    }

    //播放广播通知
    private void playBroadcastNotice(String serialNo) {
        String content = context.getString(R.string.order_center_list_take_meal_notice_broadcast, serialNo);
        BaiduSyntheticSpeech speech = BaiduSyntheticSpeech.create(content, Sex.FEMALE);
        MediaPlayerQueueManager.getInstance().play(MediaPlayerQueueManager.MEDIA_TYPE_BROADCAST_NOTICE, speech);
    }

    //设置是否取餐的状态
    private void setTakeDishStatus(TextView takeDishStatus, TradeVo tradeVo) {
        Trade trade = tradeVo.getTrade();
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        if (trade != null
                && tradeExtra != null
                && (DbQueryConstant.SALES_PAID == mChildTab || DbQueryConstant.SALES_ALL == mChildTab)
                && DeliveryType.TAKE == trade.getDeliveryType()
                && SourceId.POS != trade.getSource()
                && TradePayStatus.PAID == trade.getTradePayStatus()
                && TradeStatus.UNPROCESSED != trade.getTradeStatus()
                && TradeStatus.REFUSED != trade.getTradeStatus()
                && TradeStatus.CANCELLED != trade.getTradeStatus()) {
            if (TakeDishStatus.HAVE_TAKE_DISH == tradeExtra.getCallDishStatus()) {
//                takeDishStatus.setImageResource(R.drawable.have_take_dish);
                takeDishStatus.setBackgroundResource(R.drawable.lable_bg_green_4dd5b7_2px_radius);
                takeDishStatus.setText(R.string.order_center_label_ready_table_food);
            } else if (TakeDishStatus.NOT_TAKE_DISH == tradeExtra.getCallDishStatus()) {
//                takeDishStatus.setImageResource(R.drawable.not_take_dish);
                takeDishStatus.setBackgroundResource(R.drawable.lable_bg_orange_fdaf33_2px_radius);
                takeDishStatus.setText(R.string.order_center_label_not_take_food);
            }
            takeDishStatus.setVisibility(View.VISIBLE);
        } else {
            takeDishStatus.setVisibility(View.GONE);
        }
    }

    private void refreshDeliveryStatus(TextView view, TradePaymentVo tradePaymentVo) {
        Trade trade = tradePaymentVo.getTradeVo().getTrade();
        TradeExtra tradeExtra = tradePaymentVo.getTradeVo().getTradeExtra();
        if (DbQueryConstant.SALES_ALL == mChildTab
                && !isInBindDeliveryUserMode
                && BusinessType.TAKEAWAY == trade.getBusinessType()
                && DeliveryType.SEND == trade.getDeliveryType()
                && (TradeStatus.CONFIRMED == trade.getTradeStatus() || TradeStatus.FINISH == trade.getTradeStatus())
                && tradeExtra != null
                && DeliveryPlatform.MERCHANT == tradeExtra.getDeliveryPlatform()
                && getDeliveryOrderVo(tradePaymentVo.getDeliveryOrderVoList()) == null
                && DeliveryStatus.WAITINT_DELIVERY == tradeExtra.getDeliveryStatus()) {
            String deliveryUserId = tradeExtra.getDeliveryUserId();
            if (TextUtils.isEmpty(deliveryUserId)) {
//                view.setImageResource(R.drawable.ic_order_center_delivery_status_unbind);
                view.setBackgroundResource(R.drawable.lable_bg_orange_border_fdaf33_2px_radius);
                view.setTextColor(Color.parseColor("#FDAF33"));
                view.setText(R.string.order_center_detail_delivery_status_unbind);
            } else {
//                view.setImageResource(R.drawable.ic_order_center_delivery_status_bind);
                view.setTextColor(Color.parseColor("#4DD5B7"));
                view.setBackgroundResource(R.drawable.lable_bg_green_border_4dd5b7_2px_radius);
                view.setText(R.string.order_center_detail_delivery_status_bind);
            }
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置订单状态
     *
     * @param statusLayout   statusLayout
     * @param tradePaymentVo tradePaymentVo
     */
    private void setOrderStatus(LinearLayout statusLayout, TradePaymentVo tradePaymentVo) {
        TradeVo tradeVo = tradePaymentVo.getTradeVo();
        statusLayout.removeAllViews();
        Trade trade = tradeVo.getTrade();
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        if (trade.getBusinessType() == BusinessType.TAKEAWAY
                && trade.getDeliveryType() == DeliveryType.SEND
                && (trade.getTradeStatus() == TradeStatus.CONFIRMED || trade.getTradeStatus() == TradeStatus.FINISH)) {
            DeliveryOrderVo deliveryOrderVo = getDeliveryOrderVo(tradePaymentVo.getDeliveryOrderVoList());
            if (deliveryOrderVo != null) {
                DeliveryOrderStatus deliveryOrderStatus = deliveryOrderVo.getDeliveryOrder().getDeliveryStatus();
                if (DeliveryOrderStatus.WAITING_CREATE == deliveryOrderStatus) {//待下发
//                    createStatusView(statusLayout, R.drawable.ic_order_center_list_waiting_create);
                    createStatusView(statusLayout, R.drawable.lable_bg_orange_fdaf33_2px_radius, R.string.order_center_detail_waiting_create);
                } else if (DeliveryOrderStatus.WAITING_ACCEPT == deliveryOrderStatus) {//待结单
//                    createStatusView(statusLayout, R.drawable.ic_order_center_list_waiting_accept);
                    createStatusView(statusLayout, R.drawable.lable_bg_deeppink_ff5ea8_2px_radius, R.string.order_center_detail_waiting_accept);
                } else if (DeliveryOrderStatus.WAITING_PICK_UP == deliveryOrderStatus) {//待取货
//                    createStatusView(statusLayout, R.drawable.ic_order_center_list_waiting_pick_up);
                    createStatusView(statusLayout, R.drawable.lable_bg_purple_9180f8_2px_radius, R.string.order_center_detail_waiting_pick_up);
                } else if (DeliveryOrderStatus.DELIVERYING == deliveryOrderStatus) {//配送中
//                    createStatusView(statusLayout, R.drawable.ic_order_center_list_deliverying);
                    createStatusView(statusLayout, R.drawable.lable_bg_blue_359fff_2px_radius, R.string.order_center_detail_deliverying);
                } else if (DeliveryOrderStatus.REAL_DELIVERY == deliveryOrderStatus) {//配送完成
//                    createStatusView(statusLayout, R.drawable.ic_order_center_list_real_delivery);
                    createStatusView(statusLayout, R.drawable.lable_bg_green_4dd5b7_2px_radius, R.string.order_center_detail_real_delivery);
                } else if (DeliveryOrderStatus.DELIVERY_CANCEL == deliveryOrderStatus) {//配送取消
//                    createStatusView(statusLayout, R.drawable.ic_order_center_list_delivery_cancel);
                    createStatusView(statusLayout, R.drawable.lable_bg_deeppink_ff5ea8_2px_radius, R.string.order_center_detail_delivery_cancel);
                }
            } else {
                if (tradeExtra != null) {
                    DeliveryStatus deliveryStatus = tradeExtra.getDeliveryStatus();
                    if (DeliveryStatus.WAITINT_DELIVERY == deliveryStatus) {
                        String deliveryUserId = tradeExtra.getDeliveryUserId();
                        if (!TextUtils.isEmpty(deliveryUserId) || DeliveryPlatform.MERCHANT != tradeExtra.getDeliveryPlatform()) {//待取货
//                            createStatusView(statusLayout, R.drawable.ic_order_center_list_waiting_pick_up);
                            createStatusView(statusLayout, R.drawable.lable_bg_purple_9180f8_2px_radius, R.string.order_center_detail_waiting_pick_up);
                        }
                    } else if (DeliveryStatus.DELIVERYING == deliveryStatus) {//配送中
//                        createStatusView(statusLayout, R.drawable.ic_order_center_list_deliverying);
                        createStatusView(statusLayout, R.drawable.lable_bg_blue_359fff_2px_radius, R.string.order_center_detail_deliverying);
                    } else {//配送完成
//                        createStatusView(statusLayout, R.drawable.ic_order_center_list_real_delivery);
                        createStatusView(statusLayout, R.drawable.lable_bg_green_4dd5b7_2px_radius, R.string.order_center_detail_real_delivery);
                    }
                }
            }
        }

        if (trade.getTradeType() == TradeType.SELL_FOR_REPEAT
                || trade.getTradeType() == TradeType.REFUND_FOR_REPEAT) {
//            createStatusView(statusLayout, R.drawable.order_center_status_fjz_icon);
            createStatusView(statusLayout, R.drawable.lable_bg_blue_359fff_2px_radius, R.string.dinner_order_center_repay);
        }

        if (tradeExtra != null
                && tradeExtra.getDeliveryStatus() == DeliveryStatus.REAL_DELIVERY
                && trade.getTradeStatus() != TradeStatus.RETURNED
                && (mChildTab == DbQueryConstant.SALES_PAID || mChildTab == DbQueryConstant.SALES_ALL)
                && isPayInCashAndFromMobile(tradePaymentVo)) {
//            createStatusView(statusLayout, R.drawable.order_center_status_qz_icon);
            createStatusView(statusLayout, R.drawable.lable_bg_purple_border_9180f8_2px_radius, R.string.order_center_stay_closeout, Color.parseColor("#9180f8"));
        }

        if (trade.getTradeStatus() == TradeStatus.SQUAREUP
                && (mChildTab == DbQueryConstant.SALES_PAID || mChildTab == DbQueryConstant.SALES_ALL)) {
//            createStatusView(statusLayout, R.drawable.order_center_status_qz_icon);
            createStatusView(statusLayout, R.drawable.lable_bg_purple_border_9180f8_2px_radius, R.string.order_center_stay_closeout, Color.parseColor("#9180f8"));
        }

        if (trade.getTradeStatus() == TradeStatus.CREDIT) {// 挂账
//            createStatusView(statusLayout, R.drawable.credit);
            createStatusView(statusLayout, R.drawable.lable_bg_purple_9180f8_2px_radius, R.string.order_center_dinner_bill);
        }

        if (trade.getTradePayStatus() == TradePayStatus.REFUNDING) {
//            createStatusView(statusLayout, R.drawable.order_center_status_tkz_icon);
            createStatusView(statusLayout, R.drawable.lable_bg_blue_359fff_2px_radius, R.string.order_center_refunding);
        } else if (trade.getTradePayStatus() == TradePayStatus.REFUND_FAILED) {
//            createStatusView(statusLayout, R.drawable.order_center_status_tsb_icon);
            createStatusView(statusLayout, R.drawable.lable_bg_red_ff513a_2px_radius, R.string.order_center_label_refund_failed);
        }

        //已支付订单才显示押金未退标识
        TradeDeposit tradeDeposit = tradeVo.getTradeDeposit();
        if (trade.getTradePayStatus() == TradePayStatus.PAID
                && tradeDeposit != null && tradeDeposit.getDepositRefund() == null) {
//            createStatusView(statusLayout, R.drawable.deposit_not_refundable);
            createStatusView(statusLayout, R.drawable.lable_bg_orange_fdaf33_2px_radius, R.string.order_center_label_deposit_not_returned);
        }
    }

    /**
     * 显示订单相关状态在流水号后面。相关方法{@link #setOrderStatus(android.widget.LinearLayout, com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo)}
     *
     * @param statusLayout
     * @param tradePaymentVo
     */
    private void setOrderStatusTop(LinearLayout statusLayout, TradePaymentVo tradePaymentVo) {
        statusLayout.removeAllViews();
        TradeVo tradeVo = tradePaymentVo.getTradeVo();
        if (tradeVo.getVerifyKoubeiOrder() != null && tradeVo.getTrade().getTradeStatus() != TradeStatus.UNPROCESSED) {
            if (tradeVo.getVerifyKoubeiOrder().getVerifyStatus() == VerifyKoubeiOrder.VerifyStatus.VERIFY_WAITING) {
                createStatusView(statusLayout, R.drawable.lable_bg_ffece3_2px_radius, R.string.order_detail_verify_waiting, Color.parseColor("#ffff8249"));
            } else if (tradeVo.getVerifyKoubeiOrder().getVerifyStatus() == VerifyKoubeiOrder.VerifyStatus.VERIFY_SUCCESS) {
                createStatusView(statusLayout, R.drawable.lable_bg_e5f9f4_2px_radius, R.string.order_detail_verify_already, Color.parseColor("#ff4dd5b7"));
            }
        }
    }

    private DeliveryOrderVo getDeliveryOrderVo(List<DeliveryOrderVo> deliveryOrderVos) {
        if (Utils.isNotEmpty(deliveryOrderVos)) {
            for (DeliveryOrderVo deliveryOrderVo : deliveryOrderVos) {
                DeliveryOrder deliveryOrder = deliveryOrderVo.getDeliveryOrder();
                if (deliveryOrder.getEnableFlag() == YesOrNo.YES) {
                    return deliveryOrderVo;
                }
            }
        }

        return null;
    }

    /**
     * 待清账的一个判断条件
     *
     * @param tradePaymentVo
     * @return
     */
    private boolean isPayInCashAndFromMobile(TradePaymentVo tradePaymentVo) {
        List<PaymentVo> paymentVoList = tradePaymentVo.getPaymentVoList();
        if (Utils.isEmpty(paymentVoList)) {
            return false;
        }
        for (PaymentVo paymentVo : paymentVoList) {
            List<PaymentItem> itemList = paymentVo.getPaymentItemList();
            if (Utils.isEmpty(itemList)) {
                continue;
            }
            for (PaymentItem paymentItem : itemList) {
                if (paymentItem.getPaySource() == PaySource.ON_MOBILE
                        && PayModeId.CASH.equalsValue(paymentItem.getPayModeId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void createStatusView(ViewGroup parent, int res) {
        ImageView view = new ImageView(context);
        view.setImageResource(res);
        ViewGroup.MarginLayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.leftMargin = DensityUtil.dip2px(MainApplication.getInstance(), 8);
        parent.addView(view, params);
    }

    private void createStatusView(ViewGroup parent, int bgRes, int textRes) {
        createStatusView(parent, bgRes, textRes, Color.parseColor("#ffffff"));
    }

    private void createStatusView(ViewGroup parent, int bgRes, int textRes, int textColorRes) {
        TextView view = new TextView(context);
        view.setBackgroundResource(bgRes);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(textColorRes);
        view.setPadding(DensityUtil.dip2px(context, 3), 0, DensityUtil.dip2px(context, 3), DensityUtil.dip2px(context, 2));
        view.setTextSize(DensityUtil.sp2px(MainApplication.getInstance(), 7));
        view.setText(textRes);
        parent.addView(view);
    }

    /**
     * 设置叫号view是否显示,及显示哪几个
     *
     * @param view
     */
    private void setCallNumber(ListItemView view, TradeVo tradeVo) {
        Trade trade = tradeVo.getTrade();
        String tradeUuid = trade.getUuid();
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        String serialNumber;
        if (trade == null) {
            serialNumber = "";
        } else {
            serialNumber = trade.getSerialNumber();
            if (tradeExtra != null && !TextUtils.isEmpty(tradeExtra.getNumberPlate())) {
                serialNumber = tradeExtra.getNumberPlate();
            }
        }
        String tradeNo = trade.getTradeNo();
        int businessType = trade.getBusinessType().value();
        int sourceId = trade.getSource().value();
        int deliveryType = trade.getDeliveryType().value();
        String customerPhone = getCustomerPhone(tradeVo);
        ImageView broadcast = view.getBroadcast();
        ImageView more = view.getMore();
        if (takeMealNotice) {
            if (mCallNumberLayoutVisible) {
                broadcast.setVisibility(View.VISIBLE);
                pressedNumber = SharedPreferenceUtil.getSpUtil();
                pressedNumberSet = pressedNumber.getStringSet(IS_BROADCAST_PRESSED_KEY, null);
                if (pressedNumberSet != null && pressedNumberSet.contains(tradeUuid)) {
                    broadcast.setSelected(true);
                } else {
                    broadcast.setSelected(false);
                }
                //设置广播的tag
                broadcast.setTag(toNotifyReq(1, tradeUuid, null, serialNumber, tradeNo));
                more.setVisibility(View.VISIBLE);
                List<String> record = new ArrayList<>();
                final String IVR = "ivr";
                final String WEICHAT = "weichat";
                String receiverPhone;
                String openIdenty;
                if (tradeExtra == null) {
                    receiverPhone = "";
                    openIdenty = "";
                } else {
                    receiverPhone = tradeExtra.getReceiverPhone();
                    openIdenty = tradeExtra.getOpenIdenty();
                }
                if (!TextUtils.isEmpty(openIdenty)) {
                    if (sourceId == SourceId.WECHAT.value()) {
                        if (!isFromDinner) {
                            record.add(IVR);
                        }
                    }
                    if (deliveryType == DeliveryType.HERE.value() || deliveryType == DeliveryType.CARRY.value()) {
                        record.add(WEICHAT);
                    }
                    displayIVRAndWEICHAT(tradeUuid, serialNumber, tradeNo, more, record, IVR, WEICHAT, receiverPhone);
                } else if (sourceId == SourceId.WECHAT.value()) {
                    if (!isFromDinner) {
                        record.add(IVR);
                    }
                    if (businessType == BusinessType.SNACK.value()) {
                        record.add(WEICHAT);
                    }
                    displayIVRAndWEICHAT(tradeUuid, serialNumber, tradeNo, more, record, IVR, WEICHAT, receiverPhone);
                } else if (sourceId == SourceId.POS.value() && deliveryType == DeliveryType.TAKE.value()) {
                    record.add(IVR);
                    more.setImageResource(R.drawable.call_number_ivr_selector);
                    more.setSelected(isIVRNotified(tradeUuid));
                    more.setTag(toNotifyReq(2, tradeUuid, receiverPhone, serialNumber, tradeNo));
                } else if (sourceId == SourceId.POS.value() && !TextUtils.isEmpty(customerPhone)) {
                    record.add(IVR);
                    more.setImageResource(R.drawable.call_number_ivr_selector);
                    more.setSelected(isIVRNotified(tradeUuid));
                    more.setTag(toNotifyReq(2, tradeUuid, customerPhone, serialNumber, tradeNo));
                } else {
                    more.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            broadcast.setVisibility(View.GONE);
            more.setVisibility(View.GONE);
        }
        if (isFromDinner) {
            more.setVisibility(View.GONE);
            if (trade.getDeliveryType() == DeliveryType.HERE) {
                broadcast.setVisibility(View.GONE);
            }
        }
    }

    private void displayIVRAndWEICHAT(String tradeUuid, String serialNumber, String tradeNo, ImageView more, List<String> record, String IVR, String WEICHAT, String receiverPhone) {
        if (record.size() == 1) {
            more.setVisibility(View.VISIBLE);
            if (record.contains(IVR)) {
                more.setImageResource(R.drawable.call_number_ivr_selector);
                more.setSelected(isIVRNotified(tradeUuid));
                more.setTag(toNotifyReq(2, tradeUuid, receiverPhone, serialNumber, tradeNo));
            } else if (record.contains(WEICHAT)) {
                more.setImageResource(R.drawable.call_number_wechat_selector);
                more.setSelected(isWeChatNotified(tradeUuid));
                more.setTag(toNotifyReq(1, tradeUuid, receiverPhone, serialNumber, tradeNo));
            }
        } else if (record.size() == 2) {
            more.setVisibility(View.VISIBLE);
            more.setImageResource(R.drawable.order_center_more);
            more.setTag(toNotifyReq(3, tradeUuid, receiverPhone, serialNumber, tradeNo));
        } else {
            more.setVisibility(View.INVISIBLE);
        }
    }

    private String getCustomerPhone(TradeVo tradeVo) {
        List<TradeCustomer> customerList = tradeVo.getTradeCustomerList();
        if (customerList == null) {
            return "";
        }
        for (TradeCustomer customer : customerList) {
            if (CustomerType.MEMBER == customer.getCustomerType()) {
                return customer.getCustomerPhone();
            }
        }
        return "";
    }

    private boolean isWeChatNotified(String orderUUid) {
        Integer value = mOrderWeChatNotifyMap.get(orderUUid);
        return value != null && value > 0;
    }

    private boolean isIVRNotified(String orderUUid) {
        Integer value = mOrderIVRNotifyMap.get(orderUUid);
        return value != null && value > 0;
    }

    /**
     * 设置金额
     *
     * @param expectTime
     * @param tradePaymentVo
     */
    private void setCash(TextView expectTime, TradePaymentVo tradePaymentVo) {
        if (tradePaymentVo.getTradeUnionType() == TradeUnionType.UNION_SUB) {
            expectTime.setText(R.string.check_amount_in_order_detail);
        } else {
            //订单金额
            BigDecimal amount = tradePaymentVo.getTradeVo().getTrade().getTradeAmount();
            expectTime.setText(ShopInfoCfg.formatCurrencySymbol(amount));
        }
    }

    private void setCashInPayActual(TextView expectTime, List<PaymentVo> paymentVos) {
        BigDecimal amount = getFaceOrUsefullAmount(paymentVos);
        expectTime.setText(ShopInfoCfg.formatCurrencySymbol(amount));
    }

    private BigDecimal getFaceOrUsefullAmount(List<PaymentVo> paymentVos) {
        BigDecimal faceAmount = BigDecimal.ZERO;
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL
                    || paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_REFUND) {
                List<PaymentItem> paymentItems = paymentVo.getPaymentItemList();
                for (PaymentItem paymentItem : paymentItems) {
                    if (paymentItem.getPayStatus() == TradePayStatus.PAID || paymentItem.getPayStatus() == TradePayStatus.REFUNDED
                            || paymentItem.getPayStatus() == TradePayStatus.REPEAT_PAID) {
                        if (PayModeId.EARNEST_DEDUCT.value().equals(paymentItem.getPayModeId()))
                            continue;
                        faceAmount = faceAmount.add(paymentItem.getFaceAmount().subtract(paymentItem.getChangeAmount()));
                    }
                }
            }
        }

        return faceAmount.setScale(2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 设置流水号
     *
     * @param view
     * @param tradeVo
     */
    private void setSerialNumber(TextView view, TradeVo tradeVo) {
        Trade trade = tradeVo.getTrade();
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        // TODO: 2018/2/7 已修改
      /*  if (DbQueryConstant.SALES_ALL == mChildTab) {
            view.setText(getDeliveryAddress(tradeVo.getTradeExtra()));
            view.setVisibility(View.VISIBLE);
        } else*/
        {
            if (trade != null) {
                String serialNumber = trade.getSerialNumber();
                if (TextUtils.isEmpty(serialNumber)) {
                    if (trade.getTradeStatus() == TradeStatus.RETURNED && TextUtils.isEmpty(trade.getRelateTradeUuid())) {
                        view.setVisibility(View.VISIBLE);
                        view.setText(context.getString(R.string.salesReturnlable));
                    } else {
                        view.setVisibility(View.GONE);
                    }
                } else {
                    view.setVisibility(View.VISIBLE);
                    // v8.12.0 修改显示流水号
                    String text = context.getString(R.string.dinner_order_center_list_serial_number, serialNumber);
                    String thirdSerialNo = getThirdSerialNo(tradeExtra);
                    if (!TextUtils.isEmpty(thirdSerialNo)) {
                        text += "-" + context.getString(R.string.third_serial, thirdSerialNo);
                    }
                    view.setText(text);
                }
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置联台单标示
     *
     * @param view
     * @param tradePaymentVo
     */
    private void setUnionLabel(TextView view, TradePaymentVo tradePaymentVo) {
        if (tradePaymentVo.getTradeUnionType() == null) {
            view.setVisibility(View.GONE);
        }

        switch (tradePaymentVo.getTradeUnionType()) {
            case UNION_MAIN:
                view.setVisibility(View.VISIBLE);
                view.setText(R.string.join_table_main_trade);
                view.setTextColor(BaseApplication.sInstance.getResources().getColor(R.color.text_white));
                view.setBackgroundResource(R.drawable.order_center_union_main_bg);
                break;
            case UNION_SUB:
                view.setVisibility(View.VISIBLE);
                view.setText(R.string.join_table_trade);
                view.setTextColor(BaseApplication.sInstance.getResources().getColor(R.color.color_FC8355));
                view.setBackgroundResource(R.drawable.order_center_union_sub_bg);
                break;
            default:
                view.setVisibility(View.GONE);
                break;
        }
    }

    //获取配送地址
    private String getDeliveryAddress(TradeExtra tradeExtra) {
        if (tradeExtra != null) {
            String deliveryAddress = tradeExtra.getDeliveryAddress();
            //地址长度大于15位时，截取前15位其余用省略号结尾
            if (!TextUtils.isEmpty(deliveryAddress) && deliveryAddress.length() > 15) {
                deliveryAddress = deliveryAddress.substring(0, 15) + "...";
            }

            return deliveryAddress;
        }

        return "";
    }

    /**
     * 获取第三方流水号
     *
     * @param tradeExtra
     * @return
     */
    public String getThirdSerialNo(TradeExtra tradeExtra) {
        return tradeExtra != null ? tradeExtra.getThirdSerialNo() : "";
    }

    /**
     * 设置桌台
     *
     * @param view
     * @param tradeVo
     */
    private void setTableName(TextView view, TradeVo tradeVo) {
        List<TradeTable> tradeTableList = tradeVo.getTradeTableList();
        if (Utils.isNotEmpty(tradeTableList)) {
            BigDecimal deskCount = tradeVo.getDeskCount();
            //大于一桌只显示桌数
            if (deskCount.compareTo(BigDecimal.ONE) > 0) {
                String deskCountString = String.format(context.getString(R.string.group_order_item_desknum), deskCount.toString());
                view.setText(deskCountString);
            } else {
                TradeTable tradeTable = tradeTableList.get(0);
                String tableName = tradeTable.getTableName();
                view.setText(context.getString(R.string.dinner_order_center_tables, tableName));
            }
            view.setVisibility(View.VISIBLE);
        } else {
            TradeExtra tradeExtra = tradeVo.getTradeExtra();
            //号牌
            String numberPlate = getNumberPlate(tradeExtra);
            if (!TextUtils.isEmpty(numberPlate)) {
                view.setVisibility(View.VISIBLE);
                if (settings != null) {
                    int serialMode = settings.getSerialMode();
                    switch (serialMode) {
                        case SERIAL_DESK_MODE:
                            view.setText(context.getString(getLeftDisplayForTable(tradeVo.getTrade().getSource()), numberPlate));
                            break;
                        default:
                            view.setText(context.getString(getLeftDisplayForNumberPlate(tradeVo.getTrade().getSource()), numberPlate));
                            break;
                    }
                } else {
                    view.setText(context.getString(getLeftDisplayForNumberPlate(tradeVo.getTrade().getSource()), numberPlate));
                }
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * v8.12.0 号牌左边名称显示 POS订单显示号牌 非POS订单显示取餐号
     */
    private int getLeftDisplayForNumberPlate(SourceId sourceId) {
        switch (sourceId) {
            case POS:
                return R.string.dinner_order_center_numberplate;
            default:
                return R.string.order_center_list_take_number;
        }
    }

    /**
     * v8.12.0 桌台左边名称显示 POS订单显示桌台 非POS订单显示取餐号
     */
    private int getLeftDisplayForTable(SourceId sourceId) {
        switch (sourceId) {
            case POS:
                return R.string.dinner_order_center_tables;
            default:
                return R.string.order_center_list_take_number;
        }
    }

    /**
     * 获取交易号牌
     *
     * @param tradeExtra
     * @return
     */
    public String getNumberPlate(TradeExtra tradeExtra) {
        return tradeExtra == null ? "" : tradeExtra.getNumberPlate();
    }

    /**
     * 设置订单时间
     *
     * @param view
     * @param trade
     */
    private void setOrderTime(TextView view, Trade trade) {
        Long serverUpdateTime = trade.getServerUpdateTime();
        view.setText(DateTimeUtils.getDisplayTime(serverUpdateTime));
    }

    /**
     * 设置期望时间
     *
     * @param view
     * @param tradeVo
     */
    private void setExpectTime(TextView view, TradeVo tradeVo) {
        Trade trade = tradeVo.getTrade();
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        DeliveryType deliveryType = trade.getDeliveryType();
        if (deliveryType == DeliveryType.SEND) {
            if (tradeExtra != null) {
                Long expectTime = tradeExtra.getExpectTime();
                if (expectTime != null) {
                    String text = DateTimeUtils.getDisplayTime(expectTime);
                    if (text.length() == 16) {
                        text = text.substring(5);
                    }
                    view.setText(context.getString(R.string.order_send_delivery_time, text));
                } else {
                    String text = context.getString(R.string.order_delivery_now);
                    view.setText(text);
                }
            }
        } else if (deliveryType == DeliveryType.TAKE) {
            if (tradeExtra != null) {
                Long expectTime = tradeExtra.getExpectTime();
                if (expectTime != null) {
                    String text = DateTimeUtils.getDisplayTime(expectTime);
                    if (text.length() == 16) {
                        text = text.substring(5);
                    }
                    view.setText(context.getString(R.string.order_take_time, text));
                } else {
                    view.setText(context.getString(R.string.take_goods_as_fast));
                }
            }
        } else if (deliveryType == DeliveryType.HERE) {
            view.setText("");
        }
    }

    /**
     * 设置订单来源图标
     */
    private void setSourceIcon(ImageView icon, Trade trade) {
        SourceId sourceId = trade.getSource();
        if (sourceId != null) {
            if (sourceId == SourceId.BAIDU_RICE) {
                icon.setImageResource(R.drawable.baidu_rice_icon);
            } else if (sourceId == SourceId.DIANPING) {
                icon.setImageResource(R.drawable.dianping_icon);
            } else if (sourceId == SourceId.XIN_MEI_DA) {
                icon.setImageResource(R.drawable.xinmeida_icon);
            } else if (sourceId == SourceId.ELEME) {
                icon.setImageResource(R.drawable.eleme_icon);
            } else if (sourceId == SourceId.MEITUAN_TAKEOUT) {
                icon.setImageResource(R.drawable.meituan_icon);
            } else if (sourceId == SourceId.WECHAT) {
                icon.setImageResource(R.drawable.beauty_icon_wechat);
            } else if (sourceId == SourceId.POS) {
                icon.setImageResource(R.drawable.beauty_icon_pos);
            } else if (sourceId == SourceId.BAIDU_TAKEOUT) {
                icon.setImageResource(R.drawable.baidu_takeout_icon);
            } else if (sourceId == SourceId.BAIDU_ZHIDA) {
                icon.setImageResource(R.drawable.baidu_zhida_icon);
            } else if (sourceId == SourceId.BAIDU_MAP) {
                icon.setImageResource(R.drawable.baidu_map_icon);
            } else if (sourceId == SourceId.KIOSK || sourceId == SourceId.KIOSK_ANDROID) {
                icon.setImageResource(R.drawable.kioak_icon);
            } else if (sourceId == SourceId.MERCHANT_HOME) {
                icon.setImageResource(R.drawable.businesshome_icon);
            } else if (sourceId == SourceId.CALL_CENTER) {
                icon.setImageResource(R.drawable.callcenter_icon);
            } else if (sourceId == SourceId.ON_MOBILE) {
                icon.setImageResource(R.drawable.onmobile_icon);
            } else if (sourceId == SourceId.FAMILIAR) {
                icon.setImageResource(R.drawable.familiar_icon);
            } else if (sourceId == SourceId.OPEN_PLATFORM) {
                icon.setImageResource(R.drawable.ic_order_center_list_open_platform);
            } else if (sourceId == SourceId.JD_HOME) {
                icon.setImageResource(R.drawable.jd_home_icon);
            } else if (sourceId == SourceId.KOU_BEI) {
                icon.setImageResource(R.drawable.koubei_icon);
            } else {
                icon.setImageResource(R.drawable.defalut_source_icon);
            }
        } else {
            icon.setImageResource(R.drawable.defalut_source_icon);
        }
    }

    /**
     * 设置收货号码
     *
     * @param view
     * @param tradeExtra
     * @param tradeExtraSecrecyPhone
     */
    private void setPhone(TextView view, TradeExtra tradeExtra, TradeExtraSecrecyPhone tradeExtraSecrecyPhone) {
        if (tradeExtra != null) {
            String phone = getReceiverPhone(tradeExtra, tradeExtraSecrecyPhone);
            if (TextUtils.isEmpty(phone)) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
                view.setText(context.getString(R.string.order_center_list_phone, phone));
            }
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private String getReceiverPhone(TradeExtra tradeExtra, TradeExtraSecrecyPhone tradeExtraSecrecyPhone) {
        if (tradeExtraSecrecyPhone != null) {
            return context.getString(R.string.order_center_receiver_secrecy_phone, tradeExtraSecrecyPhone.getVirtualPhone(), tradeExtraSecrecyPhone.getVirtualPhoneExt());
        } else if (tradeExtra != null) {
            return tradeExtra.getReceiverPhone();
        }

        return "";
    }

    /**
     * 设置收货地址
     *
     * @param view
     * @param tradeExtra
     */
    private void setAddress(TextView view, TradeExtra tradeExtra) {
        //当前子标签为销货单-全部时,不展示地址
      /*  if (DbQueryConstant.SALES_ALL == mChildTab && isInBindDeliveryUserMode) {
            view.setVisibility(View.GONE);
        } else */
        {
            if (tradeExtra != null) {
                String address = tradeExtra.getDeliveryAddress();
                if (TextUtils.isEmpty(address)) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.VISIBLE);
                    view.setText(context.getString(R.string.order_center_list_address, address));
                }
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public void selectAll() {
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            setItemChecked(i, true);
        }
        notifyDataSetChanged();
    }

    public void cancelAll() {
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            setItemChecked(i, false);
        }
        notifyDataSetChanged();
    }

    public void setCheckBoxVisibility(boolean visible) {
        mCheckBoxVisible = visible;
        clearSelectBoxAndNotify();
        notifyDataSetChanged();
    }

    public void setTakeMealNoticeVisible(boolean visible) {
        takeMealNotice = visible;
        notifyDataSetChanged();
    }

    private CallDishNotifyOperatesImpl.NotifyReq toNotifyReq(int type, String tradeUuid, String mobile, String serialNo, String tradeNo) {
        CallDishNotifyOperatesImpl.NotifyReq notifyReq = new CallDishNotifyOperatesImpl.NotifyReq();
        notifyReq.setType(type);
        notifyReq.setTradeUuid(tradeUuid);
        notifyReq.setMobile(mobile);
        notifyReq.setSerialNo(serialNo);
        notifyReq.setTradeNo(tradeNo);
        return notifyReq;
    }


    /**
     * 子标签
     *
     * @param tab
     */
    public void setChildTab(int tab) {
        if (tab == DbQueryConstant.SALES_UNPAID || tab == DbQueryConstant.SALES_PAID) {
            mCallNumberLayoutVisible = true;
        } else {
            mCallNumberLayoutVisible = false;
        }
        this.mChildTab = tab;
    }

    /**
     * 大的页面tab标签
     *
     * @param checkTab
     */
    public void setCheckTab(int checkTab) {
        this.checkTab = checkTab;
    }

    public void setOrderNotifyList(List<OrderNotify> orderNotifyList) {
        mOrderWeChatNotifyMap.clear();
        mOrderIVRNotifyMap.clear();
        if (Utils.isNotEmpty(orderNotifyList)) {
            for (OrderNotify orderNotify : orderNotifyList) {
                if (orderNotify.getType() == NotifyType.WECHAT) {
                    mOrderWeChatNotifyMap.put(orderNotify.getOrderUuid(), orderNotify.getNotifyCount());
                } else if (orderNotify.getType() == NotifyType.IVR) {
                    mOrderIVRNotifyMap.put(orderNotify.getOrderUuid(), orderNotify.getNotifyCount());
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 获取已选择的订单列表
     *
     * @return
     */
    public List<TradeVo> getSelectedOrders() {
        List<TradeVo> tradeVoList = new ArrayList<TradeVo>();
        for (int i = 0; i < getItemCount(); i++) {
            if (isItemChecked(i)) {
                tradeVoList.add(items.get(i).getTradeVo());
            }
        }
        return tradeVoList;
    }

    public void setFromDinner(boolean fromDinner) {
        isFromDinner = fromDinner;
    }

    public void setInBindDeliveryUserMode(boolean inBindDeliveryUserMode) {
        isInBindDeliveryUserMode = inBindDeliveryUserMode;
    }

    public static BigDecimal getFaceOrUsefullAmount(List<PaymentVo> paymentVos, PaymentType paymentType) {
        BigDecimal faceAmount = BigDecimal.ZERO;
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment().getPaymentType() == paymentType) {
                List<PaymentItem> paymentItems = paymentVo.getPaymentItemList();
                for (PaymentItem paymentItem : paymentItems) {
                    if (paymentItem.getPayStatus() == TradePayStatus.PAID || paymentItem.getPayStatus() == TradePayStatus.REFUNDED
                            || paymentItem.getPayStatus() == TradePayStatus.REPEAT_PAID) {
                        faceAmount = faceAmount.add(paymentItem.getFaceAmount().subtract(paymentItem.getChangeAmount()));
                    }
                }
            }
        }

        return faceAmount;
    }
}
