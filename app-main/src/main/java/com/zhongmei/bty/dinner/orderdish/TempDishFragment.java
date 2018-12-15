package com.zhongmei.bty.dinner.orderdish;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.input.NumberKeyBoardUtils;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateItemTool;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.input.NumberKeyBoard;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

@EFragment(R.layout.fragment_orderdish_temp_dish)
public class TempDishFragment extends BasicDialogFragment {

    @ViewById(R.id.btn_close)
    protected Button btn_close;

    @ViewById(R.id.layout_temp_dish_name)
    protected LinearLayout layout_showTempDishName;//临时商品名称显示
    @ViewById(R.id.tv_temp_dish_name)
    protected TextView tv_tempDishName;
    @ViewById(R.id.ib_edit)
    protected ImageButton ib_edit;

    @ViewById(R.id.layout_edit_temp_dish_name)
    protected LinearLayout layout_editTempDishName;//临时商品名称编辑
    @ViewById(R.id.et_dish_name)
    protected EditText et_tempDishName;
    @ViewById(R.id.ib_clear_input)
    protected ImageButton ib_clean;

    @ViewById(R.id.tv_total_fee)
    protected TextView tv_totalFee;//商品总价


    @ViewById(R.id.et_tempDishPrice)
    protected EditText et_dishPrice;//商品价格

    @ViewById(R.id.et_temp_dish_weigh)
    protected EditText et_tempDishweigh;//商品重量(数量)


    @ViewById(R.id.btn_add_to_cart)
    protected Button btn_addToCart;

    @ViewById(R.id.keyboard)
    protected NumberKeyBoard mKeyBorad;

    private DishVo mDishVo;
    //货币符号
    String symbol = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = dialog.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        return dialog;
    }

    private void setDialogWidthAndHeight(View view) {
        Window window = getDialog().getWindow();
        view.measure(0, 0);
        Resources resources = getActivity().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int desiredWidth = metrics.widthPixels;
        int desiredHeight = metrics.heightPixels;
        window.setLayout(desiredWidth, desiredHeight);
        window.getAttributes().y = 0;
    }

    public void setDishVo(DishVo dishVo) {
        mDishVo = dishVo;
    }

    @AfterViews
    protected void initView() {

        setDialogWidthAndHeight(getView());
        symbol = ShopInfoCfg.getInstance().getCurrencySymbol();
        et_dishPrice.setText(symbol);
        et_dishPrice.setSelection(et_dishPrice.getText().length());
        et_dishPrice.setOnFocusChangeListener(onFocusChangeListener);
        et_tempDishweigh.setOnFocusChangeListener(onFocusChangeListener);
        et_dishPrice.addTextChangedListener(textWatcher);
        et_tempDishweigh.addTextChangedListener(textWeighWatcher);
        NumberKeyBoardUtils.setTouchListener(et_dishPrice);
        NumberKeyBoardUtils.setTouchListener(et_tempDishweigh);
        setTotalPrice("0");
    }


    TextWatcher textWatcher = new TextWatcher() {
        String textBeforeTmp = "";

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            textBeforeTmp = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            ([0-9]*)?+(.[0-9]{0,2})?$
//            decimal(10,2)
            String matchStr = "^" + symbol + "\\d{0,8}+(\\.\\d{0,2})?$";
            if (matchStr.startsWith("^$")) {
                matchStr = "^\\$\\d{0,8}+(\\.\\d{0,2})?$";
            }
            String etText = s.toString();

            if (TextUtils.isEmpty(etText) || !etText.matches(matchStr)) {
                et_dishPrice.setText(textBeforeTmp);
                et_dishPrice.setSelection(et_dishPrice.getText().length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            mathTotalPrice();
        }
    };

    TextWatcher textWeighWatcher = new TextWatcher() {
        String textBeforeTmp = "";

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            textBeforeTmp = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String matchStr = "([0-9]*)?$";
            String etText = s.toString();

            if (!TextUtils.isEmpty(etText) && !etText.matches(matchStr)) {
                et_tempDishweigh.setText(textBeforeTmp);
                et_tempDishweigh.setSelection(et_tempDishweigh.getText().length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            mathTotalPrice();
        }
    };

    private void mathTotalPrice() {
        String price = et_dishPrice.getText().toString();
        String count = et_tempDishweigh.getText().toString();
        if (TextUtils.isEmpty(price) || symbol.equals(price)) {
            price = "0";
        }

        if (TextUtils.isEmpty(count)) {
            count = "0";
        }
        String totalPrice = String.valueOf(Double.parseDouble(price.replace(symbol, "0")) * Double.parseDouble(count));
        setTotalPrice(totalPrice);
    }

    private void setTotalPrice(String totalPrice) {
        totalPrice = String.format(getResources().getString(R.string.dish_total_fee), ShopInfoCfg.formatCurrencySymbol(totalPrice));
        tv_totalFee.setText(totalPrice);
    }


    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.et_tempDishPrice:
                    mKeyBorad.setPointClickable(true);
                    mKeyBorad.setEditView(et_dishPrice);
                    break;
                case R.id.et_temp_dish_weigh:
                    mKeyBorad.setPointClickable(false);
                    mKeyBorad.setEditView(et_tempDishweigh);
                    break;
            }
        }
    };


    @Click({R.id.btn_close, R.id.ib_edit, R.id.ib_clear_input, R.id.btn_add_to_cart})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismissAllowingStateLoss();
                break;
            case R.id.ib_edit:
                layout_showTempDishName.setVisibility(View.GONE);
                layout_editTempDishName.setVisibility(View.VISIBLE);
                et_tempDishName.setSelection(et_tempDishName.getText().toString().trim().length());
                et_tempDishName.requestFocus();
                NumberKeyBoardUtils.showSoftKeyboard(et_tempDishName);
                break;
            case R.id.ib_clear_input:
                et_tempDishName.setText("");
                break;
            case R.id.btn_add_to_cart:
                boolean addSuccess = addTempDishToShopcart();
                if (addSuccess) {
                    ToastUtil.showLongToast(R.string.temp_dish_ticket_config_tip);
                    dismiss();
                }
                break;
        }
    }

    /**
     * 添加临时菜到购物车
     */
    private boolean addTempDishToShopcart() {
        if (TextUtils.isEmpty(et_tempDishName.getText()) || TextUtils.isEmpty(et_dishPrice.getText())
                || TextUtils.isEmpty(et_tempDishweigh.getText())) {
            ToastUtil.showLongToast(R.string.dish_temp_input_error_hint);
            return false;
        }
        String dishName = et_tempDishName.getText().toString().trim();
        //价格去人民币符号
        String price = et_dishPrice.getText().toString();
        price = price.replace(symbol, "0");
        String weight = et_tempDishweigh.getText().toString();
        BigDecimal weightDecimal = new BigDecimal(weight);
        BigDecimal priceDecimal = new BigDecimal(price);
        if (BigDecimal.ZERO.compareTo(priceDecimal) >= 0) {
            ToastUtil.showLongToast(R.string.dish_temp_price_error_hint);
            return false;
        }
        if (BigDecimal.ZERO.compareTo(weightDecimal) >= 0) {
            ToastUtil.showLongToast(R.string.dish_temp_count_error_hint);
            return false;
        }
        ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(mDishVo);
        shopcartItem.changeQty(weightDecimal);
        shopcartItem.changePrice(priceDecimal);
        shopcartItem.changeName(dishName);
        DinnerShoppingCart.getInstance().addDishToShoppingCart(shopcartItem, true);

        return true;
    }

}
