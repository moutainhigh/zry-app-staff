package com.zhongmei.bty.snack.orderdish.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderSetmeal;
import com.zhongmei.yunfu.util.MathDecimal;

/**
 * 备注和折扣
 */
@EViewGroup(R.layout.order_dish_count_and_memo)
public class OrderDishCountAndMemoView extends LinearLayout implements OnClickListener {

    private static final String TAG = OrderDishCountAndMemoView.class.getSimpleName();

    @ViewById
    EditText text_count;

    @ViewById(R.id.tv_residue_total)
    TextView tvResidueTotal;

    @ViewById
    TextView text_add;

    @ViewById
    TextView text_reduce;

    @ViewById
    EditText edit_memo;

    @ViewById
    TextView text_memo_child_arrow;

    @ViewById
    LinearLayout memo_child_root_view;

    @ViewById
    TextView weight_tips;

    @ViewById
    LinearLayout reduce_add_layout;

    private List<String> mList;

    private OrderDish mOrderDish;

    private final int LINE_SIZE = 3;

    private ICountAndMemoListener mListener;

    public void setListener(ICountAndMemoListener listener) {
        mListener = listener;
    }

    public OrderDishCountAndMemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OrderDishCountAndMemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderDishCountAndMemoView(Context context) {
        super(context);
    }

    @AfterViews
    void init() {
        setOrientation(VERTICAL);
        memo_child_root_view.setVisibility(GONE);
    }

    @Click((R.id.text_memo_child_arrow))
    void onArrowClick() {
        Object tag = text_memo_child_arrow.getTag();
        if (tag == null) {
            memo_child_root_view.setVisibility(View.VISIBLE);
            text_memo_child_arrow.setTag(Boolean.TRUE);
            text_memo_child_arrow.setTextColor(getResources().getColor(R.color.orderdish_text_blue));
            text_memo_child_arrow.setBackgroundResource(R.drawable.orderdish_dlg_blue_frame_bg);

            Drawable drawable = getResources().getDrawable(R.drawable.orderdish_dlg_arrow_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            text_memo_child_arrow.setCompoundDrawables(null, drawable, null, null);
            text_memo_child_arrow.setPadding(0, 10, 0, 10);
        } else {
            if ((Boolean) tag) {
                memo_child_root_view.setVisibility(View.GONE);
                text_memo_child_arrow.setTag(Boolean.FALSE);
                text_memo_child_arrow.setTextColor(getResources().getColor(R.color.orderdish_text_black));
                text_memo_child_arrow.setBackgroundResource(R.drawable.orderdish_dlg_item_normal);
                Drawable drawable = getResources().getDrawable(R.drawable.orderdish_dlg_arrow_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                text_memo_child_arrow.setCompoundDrawables(null, drawable, null, null);
                text_memo_child_arrow.setPadding(0, 10, 0, 10);
            } else {
                memo_child_root_view.setVisibility(View.VISIBLE);
                text_memo_child_arrow.setTag(Boolean.TRUE);
                text_memo_child_arrow.setTextColor(getResources().getColor(R.color.orderdish_text_blue));
                text_memo_child_arrow.setBackgroundResource(R.drawable.orderdish_dlg_item_press);

                Drawable drawable = getResources().getDrawable(R.drawable.orderdish_dlg_arrow_up);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                text_memo_child_arrow.setCompoundDrawables(null, drawable, null, null);
                text_memo_child_arrow.setPadding(0, 10, 0, 10);
            }
        }
    }

    public void setList(List<String> list) {
        this.mList = list;
        if (mList != null && mList.size() > 0) {
            memo_child_root_view.removeAllViews();
            inflateItemView();
            text_memo_child_arrow.setVisibility(View.VISIBLE);
            if (text_memo_child_arrow.getTag() == Boolean.TRUE) {
                memo_child_root_view.setVisibility(View.VISIBLE);
            } else {
                memo_child_root_view.setVisibility(View.GONE);
            }
        } else {
            memo_child_root_view.setVisibility(View.GONE);
            text_memo_child_arrow.setVisibility(View.GONE);
        }
    }

    public void setOrderDish(OrderDish orderDish) {
        this.mOrderDish = orderDish;
        text_count.setText(orderDish.getSingleQty().toString());
        text_count.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        text_count.setKeyListener(new DigitsKeyListener(false, true));
        InputFilter lengthfilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.length() == 0) {
                    return null;
                }

                // 非称重商品，不能输入小数点
                if (mOrderDish.getDishShop().getSaleType() != SaleType.WEIGHING && ".".equals(source.toString())) {
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
                sbDest.insert(dstart, source);
                // 把输入的字加进去，查看是否超过两位小数，或者大于999
                int dotIndex = sbDest.indexOf(".");
                if (dotIndex >= 0) {
                    // 小数，大于999或者多于两位小数，返回空
                    if (dotIndex > 3) {
                        ToastUtil.showShortToast(R.string.inputThreeInt);
                        return "";
                    } else if (sbDest.length() - (dotIndex + 1) > 2) {
                        ToastUtil.showShortToast(R.string.inputTwoADecimal);
                        return "";
                    }
                    // 整数，大于999，返回空
                } else if (sbDest.length() > 3) {
                    ToastUtil.showShortToast(R.string.inputThreeInt);
                    return "";
                }

                return null;
            }
        };
        text_count.setFilters(new InputFilter[]{lengthfilter});
    }

    public void setCount(BigDecimal qty) {
        text_count.setText(qty.toString());
        text_count.setSelection(text_count.length());
    }

    public void setMemo(String memo) {
        edit_memo.clearFocus();
        edit_memo.setText(memo);
    }

    /**
     * 显示称重提示
     */
    public void setVisibleWeightTips() {
        weight_tips.setVisibility(View.VISIBLE);
        reduce_add_layout.setVisibility(View.GONE);
    }

    /**
     * 隐藏称重提示
     */
    public void setGoneWeightTips() {
        weight_tips.setVisibility(View.GONE);
        reduce_add_layout.setVisibility(View.VISIBLE);
    }

    @Click(R.id.text_add)
    void add() {
        if (!TextUtils.isEmpty(text_count.getText().toString())) {
            BigDecimal count = new BigDecimal(text_count.getText().toString());
            BigDecimal newCount = count.add(genStepNum(mOrderDish.getDishShop()));
            // 不能大于1000
            if (newCount.compareTo(new BigDecimal(1000)) < 0) {
                text_count.setText(MathDecimal.toTrimZeroString(newCount));
                text_count.setSelection(text_count.getText().toString().length());
            }
        }

    }

    @Click(R.id.text_reduce)
    void reduce() {
        if (!TextUtils.isEmpty(text_count.getText().toString())) {
            BigDecimal stepNum = genStepNum(mOrderDish.getDishShop());
            // 起卖份数
            BigDecimal increaseUnit = mOrderDish.getDishShop().getDishIncreaseUnit();
            if (mOrderDish instanceof OrderSetmeal) {
                OrderSetmeal orderSetmeal = (OrderSetmeal) mOrderDish;
                increaseUnit = orderSetmeal.getSetmeal().getLeastCellNum();
            }

            BigDecimal count = new BigDecimal(text_count.getText().toString());
            BigDecimal newCount = count.subtract(stepNum);
            if (newCount.compareTo(increaseUnit) >= 0) {
                text_count.setText(MathDecimal.toTrimZeroString(newCount));
                text_count.setSelection(text_count.getText().toString().length());
            }
        }

    }

    @AfterTextChange(R.id.text_count)
    void onEidtCount() {
        String count = text_count.getEditableText().toString();
        if (TextUtils.isEmpty(count)) {
            ToastUtil.showShortToast(R.string.order_dish_count_cannot_empty);
            return;
        }
        BigDecimal selectedQty = null;
        try {
            selectedQty = new BigDecimal(count);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        if (selectedQty == null) {
            return;
        }
        // 选择数量不能小于起卖份数
        // 起卖份数
        BigDecimal increaseUnit = mOrderDish.getDishShop().getDishIncreaseUnit();
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

    @AfterTextChange(R.id.edit_memo)
    void editMemoComplete() {
        if (TextUtils.isEmpty(edit_memo.getEditableText().toString())) {
            edit_memo.setBackgroundResource(R.drawable.orderdish_dlg_item_normal);
        } else {
            edit_memo.setBackgroundResource(R.drawable.orderdish_dlg_blue_frame_bg);
        }
        edit_memo.setPadding(10, 10, 10, 10);

        if (mListener != null) {
            mListener.onMemoChanged(edit_memo.getEditableText().toString());
        }
    }

    private void inflateItemView() {
        List<List<String>> holders = new ArrayList<List<String>>();
        for (int i = 0; i < mList.size(); i++) {
            if (i % LINE_SIZE == 0) {
                List<String> list = new ArrayList<String>();
                list.add(mList.get(i));
                holders.add(list);
            } else {
                holders.get(holders.size() - 1).add(mList.get(i));
            }
        }

        for (int i = 0; i < holders.size(); i++) {
            View root_view = null;
            ViewHolder holder = null;
            for (int j = 0; j < holders.get(i).size(); j++) {
                holder = new ViewHolder();
                if (j == 0) {
                    root_view =
                            LayoutInflater.from(getContext()).inflate(R.layout.cashier_order_dish_dlg_memo_holer, null);
                    holder.text_name = (TextView) root_view.findViewById(R.id.text_name_0);

                } else if (j == 1) {
                    holder.text_name = (TextView) root_view.findViewById(R.id.text_name_1);

                } else if (j == 2) {
                    holder.text_name = (TextView) root_view.findViewById(R.id.text_name_2);

                }
                holder.text_name.setText(holders.get(i).get(j));
                holder.text_name.setTag(holders.get(i).get(j));
                holder.text_name.setOnClickListener(this);
                holder.text_name.setVisibility(View.VISIBLE);
            }
            memo_child_root_view.addView(root_view);
        }

    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        String memo = edit_memo.getText().toString();
        if (TextUtils.isEmpty(memo)) {
            edit_memo.setText(tag);
        } else {
            if (memo.endsWith(",")) {
                edit_memo.setText(memo + tag);
            } else {
                edit_memo.setText(memo + "," + tag);
            }
        }
        // 控制光标位置
        edit_memo.setSelection(edit_memo.getText().toString().trim().length());
    }

    /**
     * @Title: getStepNum
     * @Description: 生成每一次添加的步值, 主要是为了容错
     * @Param @param dishShop
     * @Param @return TODO
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

    class ViewHolder {
        TextView text_name;
    }

    public interface ICountAndMemoListener {
        public void onSelectedQtyChanged(BigDecimal selectedQty);

        public void onMemoChanged(String memo);
    }

}
