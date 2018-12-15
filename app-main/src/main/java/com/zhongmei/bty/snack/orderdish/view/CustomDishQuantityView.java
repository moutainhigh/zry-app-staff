package com.zhongmei.bty.snack.orderdish.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderSetmeal;
import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.SaleType;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;

/**
 * 菜品数量编辑控件
 */
@EViewGroup(R.layout.layout_custom_dish_quantity)
public class CustomDishQuantityView extends LinearLayout {
    private static final String TAG = CustomDishQuantityView.class.getSimpleName();
    @ViewById(R.id.et_quantity)
    protected EditText etQuantity;

    @ViewById(R.id.tv_quantity)
    protected TextView tvQantity;

    @ViewById(R.id.v_change_quantity)
    protected LinearLayout vChangeQuantity;

    @ViewById(R.id.iv_reduce)
    protected ImageView ivReduce;

    @ViewById(R.id.iv_add)
    protected ImageView ivAdd;

    @ViewById(R.id.tv_weighing_tip)
    protected TextView tvWeighingTip;

    private IDishQuantityListener mListener;

    private DishQuantityTextWatcher mTextWatcher;

    private OrderDish mOrderDish;

    private BigDecimal mTradeNmb = BigDecimal.ONE; //用于联台主单

    public void setTradeNmb(BigDecimal mTradeNmb) {
        this.mTradeNmb = mTradeNmb;
    }

    public CustomDishQuantityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomDishQuantityView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDishQuantityView(Context context) {
        super(context);
    }

    @AfterViews
    protected void initViews() {
        setOrientation(VERTICAL);
        mTextWatcher = new DishQuantityTextWatcher();
        etQuantity.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etQuantity.setKeyListener(new DigitsKeyListener(false, true));
    }

    /**
     * 设置数据，若orderdish为null，那么数量控件不允许操作；若orderdish为称重商品，那么数量控件不允许点击加减
     *
     * @param orderDish
     */
    public void setData(OrderDish orderDish, boolean isDinner) {
        mOrderDish = orderDish;
        if (orderDish != null && orderDish.getDishShop() != null) {
            setTextWithoutListener(orderDish.getSingleQty().toString());
            etQuantity.setFilters(new InputFilter[]{new DishQuantityInputFilter(orderDish.getSaleType())});
            etQuantity.setEnabled(true);
            etQuantity.setVisibility(View.VISIBLE);
            tvQantity.setVisibility(View.GONE);
            ivReduce.setEnabled(true);
            ivAdd.setEnabled(true);
            vChangeQuantity.setVisibility(isWeighing(orderDish) ? View.GONE : View.VISIBLE);
            tvWeighingTip.setVisibility(isWeighing(orderDish) ? View.VISIBLE : View.GONE);
            if (isDinner && isWeighing(orderDish)) {
                etQuantity.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mListener != null)
                            mListener.onOnClicked();
                        return true;
                    }
                });
                etQuantity.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        etQuantity.setFocusable(false);
                        etQuantity.clearFocus();
                    }
                });
            }
        } else {
            ivReduce.setEnabled(false);
            ivAdd.setEnabled(false);
            etQuantity.removeTextChangedListener(mTextWatcher);
            etQuantity.setText("");
            etQuantity.setVisibility(View.GONE);
            tvQantity.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 是否称重商品,为true标识是称重商品
     *
     * @param orderDish
     * @return
     */
    private boolean isWeighing(OrderDish orderDish) {
        Checks.verifyNotNull(orderDish, "orderDish");
        return orderDish.getSaleType() == SaleType.WEIGHING;
    }

    /**
     * 在设置文本内容前移除文本变化监听器，设置后在加上文本变化监听器
     *
     * @param text
     */
    public void setTextWithoutListener(String text) {
        etQuantity.removeTextChangedListener(mTextWatcher);
        etQuantity.setText(text);
        etQuantity.setSelection(etQuantity.getText().length());
        etQuantity.addTextChangedListener(mTextWatcher);
    }

    /**
     * 设置数量
     *
     * @param num
     */
    public void setText(String num) {
        etQuantity.setText(num);
        tvQantity.setText(num);
    }

    @Click(R.id.iv_add)
    void add() {
        String text = etQuantity.getText().toString();
        if (!TextUtils.isEmpty(text) && mOrderDish != null && mOrderDish.getDishShop() != null) {
            BigDecimal count = new BigDecimal(text);
            BigDecimal newCount = count.add(genStepNum(mOrderDish.getDishShop()).multiply(mTradeNmb));
            // 不能大于1000
            if (newCount.compareTo(new BigDecimal(100000)) < 0) {
                if (mOrderDish.getSaleType() == SaleType.WEIGHING) {
                    etQuantity.setText(MathDecimal.toTrimThreeZeroString(newCount));
                } else {
                    etQuantity.setText(MathDecimal.toTrimZeroString(newCount));
                }
                etQuantity.setSelection(etQuantity.getText().toString().length());
            }
        }
    }

    @Click(R.id.iv_reduce)
    void reduce() {
        String text = etQuantity.getText().toString();
        if (!TextUtils.isEmpty(text) && mOrderDish != null && mOrderDish.getDishShop() != null) {
            BigDecimal stepNum = genStepNum(mOrderDish.getDishShop()).multiply(mTradeNmb);
            // 起卖份数
            BigDecimal increaseUnit = mOrderDish.getDishShop().getDishIncreaseUnit().multiply(mTradeNmb);
            if (mOrderDish instanceof OrderSetmeal) {
                OrderSetmeal orderSetmeal = (OrderSetmeal) mOrderDish;
                increaseUnit = orderSetmeal.getSetmeal().getLeastCellNum().multiply(mTradeNmb);
            }

            BigDecimal count = new BigDecimal(text);
            BigDecimal newCount = count.subtract(stepNum);
            if (newCount.compareTo(increaseUnit) >= 0) {
                if (isWeighing(mOrderDish)) {
                    etQuantity.setText(MathDecimal.toTrimThreeZeroString(newCount));
                } else {
                    etQuantity.setText(MathDecimal.toTrimZeroString(newCount));
                }
                etQuantity.setSelection(etQuantity.getText().toString().length());
            } else {
                ToastUtil.showShortToast(R.string.order_dish_less_than_min);
            }
        }

    }

    /**
     * @Title: getStepNum
     * @Description: 生成每一次添加的步值, 主要是为了容错
     * @Param @param dishShop
     * @Return BigDecimal 返回类型
     */
    private BigDecimal genStepNum(DishShop dishShop) {
        BigDecimal stepNum = dishShop.getStepNum();
        if (stepNum != null && stepNum.compareTo(BigDecimal.ZERO) > 0) {
            return stepNum;
        } else {
            return BigDecimal.ONE;
        }
    }

    public void setListener(final IDishQuantityListener listener) {
        mListener = listener;
    }

    public interface IDishQuantityListener {
        void onSelectedQtyChanged(BigDecimal selectedQty);

        void onOnClicked();
    }

    private class DishQuantityTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = etQuantity.getEditableText().toString();
            if (TextUtils.isEmpty(text)) {
                ToastUtil.showShortToast(R.string.order_dish_count_cannot_empty);
                return;
            }

            BigDecimal selectedQty = null;
            try {
                selectedQty = new BigDecimal(text);
            } catch (Exception e) {
                Log.e(TAG, e + "");
            }

            if (selectedQty == null || mOrderDish == null) {
                return;
            }

            // 选择数量不能小于起卖份数
            // 起卖份数
            BigDecimal increaseUnit = mOrderDish.getDishShop().getDishIncreaseUnit().multiply(mTradeNmb);
            if (mOrderDish instanceof OrderSetmeal) {
                OrderSetmeal orderSetmeal = (OrderSetmeal) mOrderDish;
                increaseUnit = orderSetmeal.getSetmeal().getLeastCellNum();
            }

            if (selectedQty.compareTo(increaseUnit) < 0) {
                ToastUtil.showShortToast(R.string.order_dish_less_than_min);
                return;
            }

            if (mListener != null) {
                mListener.onSelectedQtyChanged(selectedQty);
            }
        }
    }


    /**
     * 控制商品数量
     */
    private class DishQuantityInputFilter implements InputFilter {
        private SaleType mSaleType;

        DishQuantityInputFilter(SaleType saleType) {
            mSaleType = saleType;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.length() == 0) {
                return null;
            }

            // 非称重商品，不能输入小数点
            if (mSaleType != SaleType.WEIGHING && ".".equals(source.toString())) {
                ToastUtil.showShortToast(R.string.order_dish_cannot_input_decimal);
                return "";
            }

            String strDest = null;
            if (dend > dstart) {
                // 去掉dest中选中的部分
                CharSequence csTemp = dest.subSequence(dstart, dend);
                strDest = dest.toString().replace(csTemp.toString(), "");
            } else {
                strDest = dest.toString();
            }

            // 小数点补0
            if (TextUtils.isEmpty(strDest) && (source.equals(".") || source.equals("0"))) {
                return "0.";
            }

            StringBuilder sbDest = new StringBuilder(strDest);
            if (dstart > sbDest.length()) {
                return "";
            }
            sbDest.insert(dstart, source);
            // 把输入的字加进去，查看是否超过两位小数，或者大于999999
            int dotIndex = sbDest.indexOf(".");
            if (dotIndex >= 0) {
                // 小数，大于99999或者多于两位小数，返回空
                if (dotIndex > 5) {
                    ToastUtil.showShortToast(R.string.inputFiveInt);
                    return "";
                } else if (mSaleType != SaleType.WEIGHING && sbDest.length() - (dotIndex + 1) > 3) {
                    ToastUtil.showShortToast(R.string.inputThreeADecimal);
                    return "";
                } else if (isWeighing(mOrderDish) && sbDest.length() - (dotIndex + 1) > 3) {
                    ToastUtil.showShortToast(R.string.inputThreeADecimal);
                    return "";
                }
                // 整数，大于99999，返回空
            } else if (sbDest.length() > 5) {
                ToastUtil.showShortToast(R.string.inputFiveInt);
                return "";
            }

            return null;
        }
    }
}
