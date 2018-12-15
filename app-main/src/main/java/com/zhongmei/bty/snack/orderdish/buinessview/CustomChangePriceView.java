package com.zhongmei.bty.snack.orderdish.buinessview;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryInfo;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.util.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.List;

/**
 * 菜品规格控件
 */
@EViewGroup(R.layout.layout_custom_changeprice)
public class CustomChangePriceView extends LinearLayout implements TextWatcher {


    @ViewById(R.id.tv_goodsName)
    protected TextView tv_goodsName;

    @ViewById(R.id.tv_goodsPrice)
    protected TextView tv_goodsPrice;

    @ViewById(R.id.tv_goodsRestore)
    protected TextView tv_goodsRestore;//余额

    @ViewById(R.id.tv_dailyLimit)
    protected TextView tv_dailyLimit;//每日限量

    @ViewById(R.id.layout_changePrice)
    protected LinearLayout layout_changePrice;

    @ViewById(R.id.et_changePrice)
    protected EditText et_changePrice;//变价

    private OnPriceChangeListener mPriceChangeListener;

    private OrderDish mOrderDish;


    public CustomChangePriceView(Context context) {
        super(context);
    }

    public CustomChangePriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomChangePriceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnPriceChangeListener(OnPriceChangeListener listener) {
        this.mPriceChangeListener = listener;
    }

    private InventoryCacheUtil.InventoryDataChangeListener inventoryDataChangeListener = new InventoryCacheUtil.InventoryDataChangeListener() {
        @Override
        public void dataChange(List<InventoryInfo> data) {
            setShowInventory(mOrderDish);
            /*InventoryInfo inventoryInfo = InventoryCacheUtil.getInstance().getInventoryNumByDishUuid(mOrderDish.getSkuUuid());
            if(inventoryInfo!=null) {
                String unitName = mOrderDish.getUnitName();
                if(TextUtils.isEmpty(unitName)){
                    unitName="";
                }
                tv_goodsRestore.setText(String.format(getResources().getString(R.string.order_inventory_item), inventoryInfo.getInventoryQty().toString() + unitName));
                tv_dailyLimit.setText("");
            }else{
                tv_goodsRestore.setText("");
                tv_dailyLimit.setText("");
            }*/
        }
    };

    @AfterViews
    protected void initViews() {
        setOrientation(VERTICAL);
        ViewGroup.LayoutParams lp =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
        et_changePrice.addTextChangedListener(this);
        et_changePrice.setOnTouchListener(onTouchListener);
        et_changePrice.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                initChangePriceEdit();
                return true;
            }
        });
        et_changePrice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                initChangePriceEdit();
                return;
            }
        });
    }

    /**
     * 设置数据
     *
     * @param orderDish
     */
    public void setData(OrderDish orderDish) {
        mOrderDish = orderDish;
        InventoryCacheUtil.getInstance().registerListener(ChangePageListener.PAGE_CHANGE_PRICE, inventoryDataChangeListener);
        refreshView(orderDish);
    }

    private void refreshView(OrderDish orderDish) {
        tv_goodsName.setText(orderDish.getSkuName());
        tv_goodsPrice.setText(Utils.formatPrice(MathDecimal.trimZero(orderDish.getPrice())
                .doubleValue()));
        setShowInventory(orderDish);
        if (orderDish.getIsChangePrice() == Bool.YES) {
            //显示改价
            layout_changePrice.setVisibility(View.VISIBLE);
            et_changePrice.setText(Utils.formatPrice(MathDecimal.trimZero(orderDish.getPrice())
                    .doubleValue()));
        } else {
            //隐藏改价
            layout_changePrice.setVisibility(View.GONE);
        }
    }

    private void setShowInventory(OrderDish orderDish) {
        String unitName = orderDish.getUnitName();
        if (TextUtils.isEmpty(unitName)) {
            unitName = "";
        }
        if (InventoryCacheUtil.getInstance().getSaleSwitch()) {//展示库存
            InventoryInfo inventoryInfo = InventoryCacheUtil.getInstance().getInventoryNumByDishUuid(orderDish.getSkuUuid());
            if (inventoryInfo != null) {
                tv_goodsRestore.setText(String.format(getResources().getString(R.string.order_inventory_item), inventoryInfo.getInventoryQty().toString() + unitName));
                tv_dailyLimit.setText("");
            } else {
                tv_goodsRestore.setText("");
                tv_dailyLimit.setText("");
            }
        } else {//没有库存数据
            tv_goodsRestore.setText(String.format(getResources().getString(R.string.hint_restore), orderDish.getDishShop().getResidueTotal() + unitName));
            tv_dailyLimit.setText(String.format(getResources().getString(R.string.hint_daily_limit_sale), orderDish.getDishShop().getSaleTotal() + unitName));
        }

        if (!InventoryCacheUtil.getInstance().getSaleNumOpenSwitch()) {
            tv_goodsRestore.setVisibility(GONE);
            tv_dailyLimit.setVisibility(GONE);
        } else {
            tv_goodsRestore.setVisibility(VISIBLE);
            tv_dailyLimit.setVisibility(VISIBLE);
        }
    }

    private void setChangedPrice(String price) {
        //如果字符串仅包含货币符号，那么不做处理
        String symbol = ShopInfoCfg.getInstance().getCurrencySymbol();
        if (!symbol.equals(price)) {
            tv_goodsPrice.setText(price);

            if (mPriceChangeListener != null) {
                try {
                    mPriceChangeListener.onPriceChanged(BigDecimal.valueOf(Double.parseDouble(price.replace(symbol, "0"))));
                } catch (Exception e) {

                }
            }
        }
    }

    private String textBeforeTmp = "";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        textBeforeTmp = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String matchStr = "^" + ShopInfoCfg.getInstance().getCurrencySymbol() + "\\d{0,8}+(\\.\\d{0,2})?$";
        if (matchStr.startsWith("^$")) {
            matchStr = "^\\$\\d{0,8}+(\\.\\d{0,2})?$";
        }
        String etText = s.toString();

        if (TextUtils.isEmpty(etText) || !etText.matches(matchStr)) {
            etText = textBeforeTmp;
            setChangedPrice(etText);
            et_changePrice.setText(textBeforeTmp);
            et_changePrice.setSelection(et_changePrice.getText().length());
        } else {
            setChangedPrice(etText);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    public interface OnPriceChangeListener {
        void onPriceChanged(BigDecimal price);
    }

    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            initChangePriceEdit();
            return false;
        }
    };

    private void initChangePriceEdit() {
        et_changePrice.setText(ShopInfoCfg.getInstance().getCurrencySymbol());
        et_changePrice.requestFocus();
        et_changePrice.setSelection(et_changePrice.getText().length());
    }
}
