package com.zhongmei.bty.mobilepay.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.event.AmountEditedEvent;
import com.zhongmei.bty.mobilepay.v1.event.PaymentModeOtherChangeEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.yunfu.util.MathDecimal;

import de.greenrobot.event.EventBus;

/**
 * 自定义收款
 */
public class OtherPayModelAdapter {

    private Context mContext;

    private AmountEditedEvent mAmountEditedEvent = new AmountEditedEvent(PayModelGroup.OTHER, "");

    private List<View> listView = new ArrayList<View>();

    private ScrollView scrollView;

    private ViewHolder lastHolder;

    private List<PayModelItem> listData;

    private LinearLayout mainLayout;

    private boolean isDefaultInput = false;// 是否默认带入

    private PayModelItem mDefaultInputModel;// 默认带入支付类型

    private String mDefaultInputStr;
    //add 20160714 start
    private IPaymentInfo mCashInfoManager;

    private int horizontalSize = 3;//水平行数

    private boolean isSuportMulti = true;//是否支持组合，默认支持

    public OtherPayModelAdapter(Context context, List<PayModelItem> listData, ScrollView scrollView) {
        this.mContext = context;
        this.scrollView = scrollView;
        this.listData = listData;
        init();
    }

    //设置水平列数
    public void setHorizontalSize(int horizontalSize) {
        this.horizontalSize = horizontalSize;
    }

    public void setCashInfoManager(IPaymentInfo cashInfoManager) {
        this.mCashInfoManager = cashInfoManager;
        this.mAmountEditedEvent.setBusinessType(cashInfoManager.getTradeBusinessType());
    }

    public void setSuportMulti(boolean suportMulti) {
        isSuportMulti = suportMulti;
    }

    private void init() {
        mainLayout = new LinearLayout(mContext);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(mainLayout);
        mainLayout.setPadding(0, 0, 20, 0);
    }

    /**
     * 刷新界面
     */
    public void refreshView() {
        mainLayout.removeAllViews();
        if (listView != null)
            listView.clear();
        for (PayModelItem item : listData) {
            listView.add(getPositionView(item));
        }
        //行间距
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        diyWh.setMargins(0, 10, 0, 0);
        //列间距
        /*LinearLayout.LayoutParams diyWh2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120, 1);
        diyWh2.setMargins(0, 0, 0, 0);*/
        int size = 0;
        if (listView != null)
            size = listView.size();
        if (listView != null && size > 0) {
            //modify 20160517 两列改3列begin
            final int mod = size % horizontalSize;
            //modify 20161008自动列数 begin
            final int emptySize = (horizontalSize - mod) % horizontalSize;
            LinearLayout.LayoutParams param =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120, 1);
            for (int i = 0; i < emptySize; i++) {
                View emptyView = new View(mContext);
                emptyView.setLayoutParams(param);
                listView.add(emptyView);
                size++;
            }
            //modify 20160517 两列改3列 end
            //modify 20161008自动列数 end
            LinearLayout linerLayout = null;
            for (int i = 0; i < size; i++) {
                View view = listView.get(i);
                if (i % horizontalSize == 0) {//modify 20161008自动列数
                    //view.setLayoutParams(diyWh2);//第一列不要间距
                    linerLayout = new LinearLayout(mContext);
                    linerLayout.setLayoutParams(diyWh);
                    linerLayout.addView(view);
                    mainLayout.addView(linerLayout);
                } else {
                    linerLayout.addView(view);
                }
            }
        }
    }

    public View getPositionView(PayModelItem mPayModelItem) {
        ViewHolder holder = null;
        LinearLayout convertView =
                (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.payment_mode_other_item, null);
        holder = new ViewHolder();
        //holder.contentView = convertView;
        holder.paymentMode = (ViewGroup) convertView.findViewById(R.id.payment_mode);
        holder.paymentModeAdd = (ImageView) convertView.findViewById(R.id.payment_mode_add);
        holder.paymentModeReduce = (ImageView) convertView.findViewById(R.id.payment_mode_reduece);

        holder.paymentModeFaceValue = (TextView) convertView.findViewById(R.id.payment_mode_face_value);
        holder.paymentModeAmount = (EditText) convertView.findViewById(R.id.payment_mode_amount);
        holder.paymentModeCount = (TextView) convertView.findViewById(R.id.payment_mode_count);
        holder.paymentModeName = (TextView) convertView.findViewById(R.id.payment_mode_name);
        holder.item = mPayModelItem;
        convertView.setTag(holder);
        if (mPayModelItem.getUsedValue() != null && mPayModelItem.getUsedValue().compareTo(BigDecimal.ZERO) > 0) {
            setSelected(true, holder);
        } else {
            setSelected(false, holder);
        }
        holder.paymentModeName.setText(mPayModelItem.getPaymentModeShop().getName());
        convertView.setOnClickListener(new LayoutClickListener());
        holder.paymentModeAdd.setTag(holder);
        holder.paymentModeAdd.setOnClickListener(new AddOrReduceClickListener(mPayModelItem, true));
        holder.paymentModeReduce.setTag(holder);
        holder.paymentModeReduce.setOnClickListener(new AddOrReduceClickListener(mPayModelItem, false));
        Watcher watcher = new Watcher(mPayModelItem, holder.paymentModeAmount);
        holder.paymentModeAmount.setTag(holder);
        holder.paymentModeAmount.addTextChangedListener(watcher);
        int height = holder.paymentMode.getLayoutParams().height > 0 ? holder.paymentMode.getLayoutParams().height : LinearLayout.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams diyWh = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height, 1);
        diyWh.setMargins(20, 0, 0, 0);
        convertView.setLayoutParams(diyWh);
        return convertView;
    }

    /**
     * @Title: clearDefaultInput
     * @Description: 清楚默认输入值
     */
    public void clearDefaultInput() {
        if (isDefaultInput && mDefaultInputModel != null) {
            mDefaultInputModel.setUsedValue(BigDecimal.ZERO);
            mCashInfoManager.getOtherPay().removePayModelItem(mDefaultInputModel);
            removeDefaultPay();
        }
    }

    public void clearInput() {
        if (mCashInfoManager != null)
            mCashInfoManager.getOtherPay().clear();
        removeDefaultPay();
    }

    private void removeDefaultPay() {
        isDefaultInput = false;
        mDefaultInputModel = null;
        mDefaultInputStr = null;
    }

    private void setDefaultInput(PayModelItem defaultInput) {
        if (defaultInput != null) {
            isDefaultInput = true;
            mDefaultInputModel = defaultInput;
        }
    }

    public class Watcher implements TextWatcher {

        private PayModelItem mPayModelItem;

        private EditText paymentModeAmount;

        public Watcher(PayModelItem mPayModelItem, EditText paymentModeAmount) {
            this.mPayModelItem = mPayModelItem;
            this.paymentModeAmount = paymentModeAmount;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            lastHolder = (ViewHolder) paymentModeAmount.getTag();
            if (!TextUtils.isEmpty(s)) {
                if (s.toString().startsWith(".")) {
                    paymentModeAmount.setText("0" + s.toString());
                    return;
                }
                BigDecimal amount = new BigDecimal(s.toString());
                BigDecimal shouldAmount =
                        BigDecimal.valueOf(mCashInfoManager
                                .getOtherPayNotPayMentByModelId(mPayModelItem.getPayModelId()));

                if (amount.compareTo(shouldAmount) > 0) {
                    ToastUtil.showShortToast(R.string.amount_more_than_should_pay);
                    paymentModeAmount.setText(s.subSequence(0, s.length() - 1));
                    paymentModeAmount.setSelection(paymentModeAmount.getText().length());
                    return;
                } else if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    mPayModelItem.setUsedValue(new BigDecimal(s.toString()));
                    mCashInfoManager.getOtherPay().addPayModelItem(mPayModelItem);
                    // 取消自动带入
                    if (!s.toString().equals(mDefaultInputStr)) {
                        if (isDefaultInput) {
                            removeDefaultPay();
                        }
                    }
                } else if (amount.compareTo(BigDecimal.ZERO) == 0) {
                    mCashInfoManager.getOtherPay().removePayModelItem(mPayModelItem);
                }
                paymentModeAmount.setSelection(s.length());
            } else {
                mPayModelItem.setUsedValue(BigDecimal.ZERO);
                mCashInfoManager.getOtherPay().removePayModelItem(mPayModelItem);
            }
            // 修改付款金额
            mAmountEditedEvent.setAmountValue(mCashInfoManager.getOtherPay().getGroupAmount());
            EventBus.getDefault().post(mAmountEditedEvent);
            EventBus.getDefault().post(new PaymentModeOtherChangeEvent());
        }
    }

    /**
     * 点击
     */
    public class LayoutClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            ViewHolder nowHolder = (ViewHolder) v.getTag();
            cleanLastHolder(lastHolder, nowHolder);
            lastHolder = nowHolder;
            PayModelItem nowItem = nowHolder.item;
            BigDecimal notPayAmount = BigDecimal.valueOf(mCashInfoManager// 当前剩余要付的金额
                    .getOtherPayNotPayMentByModelId(nowItem.getPayModelId()));
            BigDecimal faceValue = nowItem.getPaymentModeShop().getFaceValue();

            if (faceValue != null) {
                /*if (nowItem.getUsedValue() != null && nowItem.getUsedValue().compareTo(BigDecimal.ZERO) > 0) {// 有面值的并且已选中不做处理
                    return;
                }*/
                if (notPayAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    ToastUtil.showShortToast(R.string.amount_is_enough);
                    return;
                } else {// 有面值第一次选中
                    nowItem.setUsedCount(1);
                    nowItem.setUsedValue(nowItem.getPaymentModeShop().getFaceValue());
                    mCashInfoManager.getOtherPay().addPayModelItem(nowItem);
                }
            }
            setSelected(true, nowHolder);
            // 修改付款金额
            mAmountEditedEvent.setAmountValue(mCashInfoManager.getOtherPay().getGroupAmount());
            EventBus.getDefault().post(mAmountEditedEvent);
            EventBus.getDefault().post(new PaymentModeOtherChangeEvent());
            if (faceValue == null) {
                nowHolder.paymentModeAmount.requestFocus();
                String txt = nowHolder.paymentModeAmount.getText().toString();
                if (!TextUtils.isEmpty(txt)) {
                    nowHolder.paymentModeAmount.setSelection(txt.length());
                }
                // ViewUtil.showSoftKeyboard(nowHolder.paymentModeAmount);
            }
        }
    }

    /**
     * 加减
     */
    public class AddOrReduceClickListener implements OnClickListener {
        private PayModelItem mPayModelItem;

        private boolean isAdd;

        public AddOrReduceClickListener(PayModelItem mPayModelItem, boolean isAdd) {
            this.mPayModelItem = mPayModelItem;
            this.isAdd = isAdd;
        }

        @Override
        public void onClick(View v) {
            ViewHolder nowHolder = (ViewHolder) v.getTag();
            cleanLastHolder(lastHolder, nowHolder);
            lastHolder = nowHolder;
            BigDecimal notPayAmount = BigDecimal.valueOf(mCashInfoManager.getNotPayMent());
            BigDecimal faceValue = mPayModelItem.getPaymentModeShop().getFaceValue();
            ViewHolder holder = (ViewHolder) v.getTag();
            if (isAdd) {// 增加
                if (notPayAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    ToastUtil.showShortToast(R.string.amount_is_enough);
                    return;
                } else {
                    if (mPayModelItem.getUsedCount() == 99) {
                        ToastUtil.showShortToast(R.string.has_been_up_limit);
                        return;
                    }
                    mPayModelItem.setUsedCount(mPayModelItem.getUsedCount() + 1);
                    mPayModelItem.setUsedValue(mPayModelItem.getUsedValue().add(faceValue));
                    holder.paymentModeCount.setText(mPayModelItem.getUsedCount() + "");
                    mCashInfoManager.getOtherPay().addPayModelItem(mPayModelItem);
                }
            } else {// 减少
                if (mPayModelItem.getUsedCount() == 1) {
                    mPayModelItem.setUsedCount(0);
                    mPayModelItem.setUsedValue(BigDecimal.ZERO);
                    mCashInfoManager.getOtherPay().removePayModelItem(mPayModelItem);
                    setSelected(false, holder);
                } else {
                    mPayModelItem.setUsedCount(mPayModelItem.getUsedCount() - 1);
                    mPayModelItem.setUsedValue(mPayModelItem.getUsedValue().subtract(faceValue));
                    holder.paymentModeCount.setText(mPayModelItem.getUsedCount() + "");
                    mCashInfoManager.getOtherPay().addPayModelItem(mPayModelItem);
                }
            }
            // 修改付款金额
            mAmountEditedEvent.setAmountValue(mCashInfoManager.getOtherPay().getGroupAmount());
            EventBus.getDefault().post(mAmountEditedEvent);
            EventBus.getDefault().post(new PaymentModeOtherChangeEvent());
        }
    }

    /**
     * 清楚上一个状态,支持组合与非组合
     * add v8.1
     */
    private void cleanLastHolder(ViewHolder lastHolder, ViewHolder currentHolder) {
        if (lastHolder != null) {// 上一个自定义没输入的清除选中状态
            PayModelItem lastItem = lastHolder.item;
            BigDecimal faceValue = lastItem.getPaymentModeShop().getFaceValue();
            if (this.isSuportMulti) {
                if (faceValue == null) {
                    clearDefaultInput();// add 20160511
                    lastHolder.paymentModeAmount.setVisibility(View.VISIBLE);
                    lastHolder.paymentModeFaceValue.setVisibility(View.GONE);
                    if (lastItem.getUsedValue() == null || lastItem.getUsedValue().compareTo(BigDecimal.ZERO) == 0) {
                        setSelected(false, lastHolder);
                    }
                }
            } else {
                if (faceValue == null) {//自定义输入
                    clearInput();
                    lastItem.setUsedValue(BigDecimal.ZERO);
                    setSelected(false, lastHolder);
                } else {//固定面值
                    if (lastHolder != currentHolder) {
                        clearInput();
                        lastItem.setUsedCount(0);
                        setSelected(false, lastHolder);
                    }
                }
            }
        }
    }

    /**
     * 选中或取消
     *
     * @param isSelected
     * @param holder
     */
    private void setSelected(boolean isSelected, ViewHolder holder) {
        PayModelItem payModelItem = holder.item;
        BigDecimal faceValue = payModelItem.getPaymentModeShop().getFaceValue();
        if (isSelected) {
            BigDecimal notPayAmount = BigDecimal.valueOf(mCashInfoManager// 当前剩余要付的金额
                    .getOtherPayNotPayMentByModelId(payModelItem.getPayModelId()));
            holder.paymentMode.setBackgroundResource(R.drawable.payment_mode_other_select_bg);
            //holder.contentView.setBackgroundResource(R.drawable.pay_other_item_selected);
            if (faceValue != null) {
                holder.paymentModeAdd.setVisibility(View.VISIBLE);
                holder.paymentModeReduce.setVisibility(View.VISIBLE);
                holder.paymentModeCount.setVisibility(View.VISIBLE);
                holder.paymentModeCount.setText(payModelItem.getUsedCount() + "");
                holder.paymentModeAmount.setVisibility(View.GONE);
                holder.paymentModeFaceValue.setVisibility(View.VISIBLE);
                //ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.paymentModeFaceValue.getLayoutParams();
                //params.setMargins(DensityUtil.dip2px(10), DensityUtil.dip2px(12),0,0);
                //holder.paymentModeFaceValue.setLayoutParams(params);
            } else {
                holder.paymentModeAmount.setVisibility(View.VISIBLE);
                holder.paymentModeFaceValue.setVisibility(View.GONE);
                holder.paymentModeAdd.setVisibility(View.GONE);
                holder.paymentModeReduce.setVisibility(View.GONE);
                holder.paymentModeCount.setVisibility(View.GONE);
                if (payModelItem.getUsedValue() != null && payModelItem.getUsedValue().compareTo(BigDecimal.ZERO) > 0) {
                    holder.paymentModeAmount.setText(payModelItem.getUsedValue() + "");
                } else {// 自动带入 add 20160511
                    if (notPayAmount != null && notPayAmount.compareTo(BigDecimal.ZERO) > 0) {
                        payModelItem.setUsedValue(notPayAmount);
                        setDefaultInput(payModelItem);
                        mDefaultInputStr = payModelItem.getUsedValue() + "";
                        holder.paymentModeAmount.setText(mDefaultInputStr);
                    } else holder.paymentModeAmount.setText(null);
                }
            }
            holder.paymentModeFaceValue.setTextColor(Color.WHITE);
            holder.paymentModeCount.setTextColor(Color.BLACK);
            holder.paymentModeName.setTextColor(Color.WHITE);
        } else {
            holder.paymentMode.setBackgroundResource(R.drawable.payment_mode_oter_bg);
            holder.paymentModeAdd.setVisibility(View.GONE);
            holder.paymentModeReduce.setVisibility(View.GONE);
            holder.paymentModeCount.setVisibility(View.GONE);
            holder.paymentModeAmount.setVisibility(View.GONE);
            holder.paymentModeFaceValue.setVisibility(View.VISIBLE);
            holder.paymentModeFaceValue.setTextColor(mContext.getResources().getColor(R.color.color_555555));
            holder.paymentModeCount.setTextColor(mContext.getResources().getColor(R.color.color_555555));
            holder.paymentModeName.setTextColor(mContext.getResources().getColor(R.color.color_555555));
            //ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.paymentModeFaceValue.getLayoutParams();
            //params.setMargins(DensityUtil.dip2px(10), DensityUtil.dip2px(37),0,0);
            //holder.paymentModeFaceValue.setLayoutParams(params);
        }
        if (faceValue != null) {
            holder.paymentModeFaceValue.setText(ShopInfoCfg.getInstance().getCurrencySymbol() + MathDecimal.toTrimZeroString(faceValue));
        } else {
            holder.paymentModeFaceValue.setText(R.string.user_define_text);
        }
    }

    static class ViewHolder {
        //View contentView;

        ViewGroup paymentMode;
        ImageView paymentModeAdd;

        ImageView paymentModeReduce;

        TextView paymentModeFaceValue;

        EditText paymentModeAmount;

        TextView paymentModeCount;

        TextView paymentModeName;

        PayModelItem item;

    }
}
