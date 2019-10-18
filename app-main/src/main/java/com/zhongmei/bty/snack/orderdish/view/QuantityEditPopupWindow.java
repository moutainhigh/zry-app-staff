package com.zhongmei.bty.snack.orderdish.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.devices.ews.ElecScaleManagerWrapper;
import com.zhongmei.bty.basemodule.devices.ews.EwsManager.DataReceivedListener;
import com.zhongmei.bty.basemodule.devices.scaner.DeWoScanCode;
import com.zhongmei.bty.basemodule.orderdish.bean.DishInfo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateItemTool;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ToastUtil;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;


public class QuantityEditPopupWindow extends PopupWindow implements OnClickListener, PopupWindow.OnDismissListener {
    public static final String TAG = QuantityEditPopupWindow.class.getSimpleName();

    private static final int MAX_LENGTH_BEFORE_DOT = 5;

    private static final int MAX_LENGTH_AFTER_DOT = 3;
        private static final int MAX_COUNT = 100000;

    private static final int SCAN_MODE = 10001;


    private static final int NUM_EDIT_MODE = 10002;


    private static final int WEIGHT_MODE = 10003;

    private static final String DOT = ".";

    private View llLoadingMode;

    private View rlQuantityMode;

    private TextView tvDishName;

        private TextView tvQuantity;

    private EditText etBarcode;

    private ImageView ivLoading;

    private LinearLayout mNumLayout;

    private RelativeLayout mWeightLayout;

        private TextView mWeight;

    private ImageView mWeightLoading;

    private Button mOkBtn;

        private TextView mConnectionState;

    private ShopcartItemBase mShopcartItem;

    private Activity mActivity;

        private boolean mContainProperties = false;

    protected DishManager mDishManager;

    private boolean mIsQuantityChanged = false;

    private DataChangeListener mShopcartItemListenter;

    private ElecScaleManagerWrapper mElecScaleManager;

        private int mCurMode = SCAN_MODE;

        private int mMode = SCAN_MODE;

        private float mCurWeight;

    private boolean mIsDinner = false;

    private boolean mIsCustomInput = false;
        private boolean mSelfInstructed = SpHelper.getDefault().getBoolean(Constant.SETTING_SUPPORT_WEIGH_SELF_INSTRUCTED, false);

    private View contentView;


    public QuantityEditPopupWindow(Activity activity, DataChangeListener listenter) {
        this.mCurMode = SCAN_MODE;
        this.mActivity = activity;
        this.mShopcartItemListenter = listenter;
        init();
    }


    public QuantityEditPopupWindow(Activity activity, ShopcartItemBase item, DataChangeListener listenter) {
        this.mCurMode = NUM_EDIT_MODE;
        this.mActivity = activity;
        this.mShopcartItemListenter = listenter;
        this.mShopcartItem = item;
        init();
    }


    public QuantityEditPopupWindow(Activity activity, ShopcartItemBase item, SetmealShopcartItem setamalItem,
                                   boolean containProperties, DataChangeListener listenter) {
        this.mCurMode = WEIGHT_MODE;
        this.mActivity = activity;
        this.mShopcartItemListenter = listenter;
        this.mShopcartItem = item;
        this.mContainProperties = containProperties;
        init();
    }


    public QuantityEditPopupWindow(Activity activity, ShopcartItemBase item, boolean containProperties, DataChangeListener listenter) {
        this(activity, item, null, containProperties, listenter);
    }


    public QuantityEditPopupWindow(Activity activity, ShopcartItemBase item, boolean containProperties, boolean edit, DataChangeListener listenter) {
        this(activity, item, null, containProperties, listenter);
        mIsCustomInput = edit;
    }


    private void init() {
        View contentView = initContentView();
        setContentView(contentView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));
        setOnDismissListener(this);
        initViewByMode();
    }

    private View initContentView() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        contentView = inflater.inflate(R.layout.cashier_order_dish_countitem, null);
        contentView.findViewById(R.id.v_left_zone).setOnClickListener(this);
        contentView.findViewById(R.id.btn_close).setOnClickListener(this);
        contentView.findViewById(R.id.one).setOnClickListener(this);
        contentView.findViewById(R.id.two).setOnClickListener(this);
        contentView.findViewById(R.id.three).setOnClickListener(this);
        contentView.findViewById(R.id.four).setOnClickListener(this);
        contentView.findViewById(R.id.five).setOnClickListener(this);
        contentView.findViewById(R.id.six).setOnClickListener(this);
        contentView.findViewById(R.id.seven).setOnClickListener(this);
        contentView.findViewById(R.id.eight).setOnClickListener(this);
        contentView.findViewById(R.id.nine).setOnClickListener(this);
        contentView.findViewById(R.id.zero).setOnClickListener(this);
        contentView.findViewById(R.id.delete).setOnClickListener(this);
        contentView.findViewById(R.id.tv_dot).setOnClickListener(this);
        contentView.findViewById(R.id.add).setOnClickListener(this);
        contentView.findViewById(R.id.reduce).setOnClickListener(this);
        llLoadingMode = contentView.findViewById(R.id.ll_loading_mode);
        rlQuantityMode = contentView.findViewById(R.id.rl_quantity_mode);
        ivLoading = (ImageView) contentView.findViewById(R.id.iv_loading);
        tvQuantity = (TextView) contentView.findViewById(R.id.count);
        tvDishName = (TextView) contentView.findViewById(R.id.tv_dish_name);
        etBarcode = (EditText) contentView.findViewById(R.id.et_barcode);
        mNumLayout = (LinearLayout) contentView.findViewById(R.id.num_layout);
        mWeightLayout = (RelativeLayout) contentView.findViewById(R.id.weight_num_layout);
        mWeight = (TextView) contentView.findViewById(R.id.weight_num);
        mConnectionState = (TextView) contentView.findViewById(R.id.connection_state);
        mWeightLoading = (ImageView) contentView.findViewById(R.id.weight_loading);
        mOkBtn = (Button) contentView.findViewById(R.id.btn_ok);
        mOkBtn.setOnClickListener(this);
        return contentView;
    }

    public void setLeftZoneWidth(int width) {
        if (width == -1) {
            return;
        }
        View view = contentView.findViewById(R.id.v_left_zone);
        LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }



    private void initViewByMode() {
        if (mCurMode == SCAN_MODE) {
            mDishManager = new DishManager();
            mIsQuantityChanged = false;
            tvQuantity.addTextChangedListener(mCountTextChanged);
            etBarcode.setInputType(InputType.TYPE_NULL);
            etBarcode.setOnKeyListener(scanListener);
            initDeWoScan();
            resetBarcodeView();
            forbiddenSoftKeyboard(etBarcode);
        } else if (mCurMode == NUM_EDIT_MODE) {
            tvQuantity.addTextChangedListener(mCountTextChanged);
            registerKeyboardListener();
            etBarcode.setVisibility(View.GONE);
            if (tvDishName != null && etBarcode != null) {
                tvDishName.setInputType(InputType.TYPE_NULL);
                tvDishName.setFocusable(true);
                tvDishName.setFocusableInTouchMode(true);
                tvDishName.requestFocus();
                etBarcode.setFocusable(false);
            }
        } else if (mCurMode == WEIGHT_MODE) {
            if (mShopcartItem.getDishShop().getWeight() != null) {                if (mElecScaleManager == null) {
                    mElecScaleManager = new ElecScaleManagerWrapper(this.mActivity);
                }
                mElecScaleManager.startGetElecScaleData(dataReceivedListener);
            }

        }
        this.mMode = mCurMode;
        setShopcartItem(mShopcartItem);
        refreshMainContent();

    }

    void initDeWoScan() {
        DeWoScanCode.getInstance().registerReceiveDataListener(new DeWoScanCode.OnReceiveDataListener() {
            @Override
            public void onReceiveData(String data) {
                startLoadingAnimation(ivLoading);
                scanGoods(data);
                resetBarcodeView();
            }
        });
    }

        private BigDecimal doSelfInstructed(BigDecimal inputValue) {
        BigDecimal result = inputValue;
                if (this.mSelfInstructed && ShopInfoCfg.getInstance().isMonitorCode(ShopInfoCfg.ShopMonitorCode.MONITOR_CODE_HNXT)) {
            result = MathDecimal.roundUp(inputValue, 0);
        }
        return result;
    }

    private ElecScaleManagerWrapper.DataReceivedListener dataReceivedListener =
            new ElecScaleManagerWrapper.DataReceivedListener() {

                @Override
                public void onStartLoading() {
                    weightLoadingAnimation(mWeightLoading);
                }

                @Override
                public void onEndLoading() {
                    mWeightLoading.setVisibility(View.GONE);
                }

                @Override
                public void onDeviceConnect(boolean connect) {
                    mConnectionState.setVisibility(connect ? View.GONE : View.VISIBLE);
                }

                @Override
                public void onWeightReceived(float weight) {
                    if (mShopcartItem.getDishShop().getWeight() != null
                            && mShopcartItem.getDishShop().getWeight().floatValue() != 0) {
                        mCurWeight = MathDecimal.div(weight, mShopcartItem.getDishShop().getWeight().floatValue(), 3);
                        mathWeightDishPrice();
                        if (mShopcartItem.getOrderDish().getUnitName() != null) {
                            mWeight.setText(mCurWeight + mShopcartItem.getOrderDish().getUnitName() + "");
                        } else {
                            mWeight.setText(mCurWeight + "");
                        }

                    }
                }
            };

    private DataReceivedListener
            mWeightListener = new DataReceivedListener() {

        @Override
        public void onDeviceConnect(boolean connect) {
            if (connect) {
                mConnectionState.setVisibility(View.GONE);
            } else {
                mConnectionState.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onDataReceivedOver(String data) {
            try {
                                if (mShopcartItem.getDishShop().getWeight() != null
                        && mShopcartItem.getDishShop().getWeight().floatValue() != 0) {
                    mCurWeight = MathDecimal.div(Float.parseFloat(data), mShopcartItem.getDishShop().getWeight().floatValue(), 3);
                    mathWeightDishPrice();
                    if (mShopcartItem.getOrderDish().getUnitName() != null) {
                        mWeight.setText(mCurWeight + mShopcartItem.getOrderDish().getUnitName() + "");
                    } else {
                        mWeight.setText(mCurWeight + "");
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStartLoading() {
            weightLoadingAnimation(mWeightLoading);
        }

        @Override
        public void onEndLoading() {
            mWeightLoading.setVisibility(View.GONE);
        }
    };

    private void scanGoods() {
        String barcode = etBarcode.getText().toString().trim();
        scanGoods(barcode);
    }

    private void scanGoods(String barcode) {
        if (mDishManager != null && !TextUtils.isEmpty(barcode)) {
            DishInfo dishInfo = mDishManager.scan(barcode);
            if (dishInfo != null && Utils.isNotEmpty(dishInfo.dishList) && dishInfo.scanResult) {
                updateScanModeView(dishInfo);
            }  else {
                ToastUtil.showShortToast(R.string.order_dish_not_exist);
            }

        }
    }


    private void updateScanModeView(DishInfo dishInfo) {
        DishVo dishVo = dishInfo.dishList.get(0);
        ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishVo);
        if (shopcartItem.getDishShop().getSaleType() == SaleType.WEIGHING) {            mCurMode = WEIGHT_MODE;
            mContainProperties = dishVo.isContainProperties();
            if (shopcartItem.getDishShop().getWeight() != null) {                if (mElecScaleManager == null) {
                    mElecScaleManager = new ElecScaleManagerWrapper(this.mActivity);
                }
                mElecScaleManager.startGetElecScaleData(dataReceivedListener);
            }

        } else {
            mCurMode = NUM_EDIT_MODE;
            mConnectionState.setVisibility(View.GONE);
        }
        setShopcartItem(shopcartItem);
        refreshMainContent();

    }

    private void resetBarcodeView() {
        if (etBarcode != null) {
            etBarcode.setText("");
            etBarcode.requestFocus();
        }
    }


    private void executeAdd() {
        String val = tvQuantity.getText().toString();
        if (!TextUtils.isEmpty(val) && Utils.isNum(val)) {
            BigDecimal currentQuantity = new BigDecimal(tvQuantity.getText().toString());
            BigDecimal stepNum = mShopcartItem.getOrderDish().getDishShop().getStepNum();
            BigDecimal quantity = currentQuantity.add(stepNum);
            if (isValid(quantity.toString())) {
                updateCountView(MathDecimal.toTrimZeroString(quantity));
            }
        }
    }


    private boolean isValid(String quantity) {
        String[] s = quantity.split("\\" + DOT);

        String textBeforeDot = s[0];
        if (textBeforeDot.length() > MAX_LENGTH_BEFORE_DOT) {
            ToastUtil.showShortToast(R.string.inputFiveInt);
            return false;
        }

        if (s.length >= 2) {
            String textAfterDot = s[1];
            if (textAfterDot.length() > MAX_LENGTH_AFTER_DOT) {
                ToastUtil.showShortToast(R.string.inputThreeADecimal);
                return false;
            }
        }

        return true;
    }


    private void executeSubtract() {
        String val = tvQuantity.getText().toString();
        if (!TextUtils.isEmpty(val) && Utils.isNum(val)) {
            BigDecimal currentQuantity = new BigDecimal(val);
            BigDecimal stepNum = mShopcartItem.getOrderDish().getDishShop().getStepNum();
            BigDecimal increaseUnit = mShopcartItem.getOrderDish().getDishShop().getDishIncreaseUnit();
            BigDecimal quantity = currentQuantity.subtract(stepNum);
            if (quantity.compareTo(increaseUnit) < 0) {
                ToastUtil.showShortToast(R.string.order_dish_less_than_min);
            } else {
                updateCountView(MathDecimal.toTrimZeroString(quantity));
            }
        }
    }


    private void executeAppend(String value) {
        String text = "";
        mIsCustomInput = true;
        if (mCurMode == WEIGHT_MODE) {
            text = mWeight.getText().toString();
            if (!TextUtils.isEmpty(text)
                    && ((mShopcartItem.getOrderDish().getUnitName() != null && text.contains(mShopcartItem.getOrderDish().getUnitName())))) {                if (mShopcartItem != null) {
                    text = text.substring(0, text.length() - mShopcartItem.getOrderDish().getUnitName().length()) + "";
                }
            }
        } else {
            text = tvQuantity.getText().toString();
        }
        StringBuilder sb = new StringBuilder(text);
        if (value.equals(DOT)) {
            if (isUnWeighItem()) {
                ToastUtil.showLongToast(R.string.order_dish_cannot_input_decimal);
                return;
            }
            if (text.contains(DOT)) {
                ToastUtil.showLongToast(R.string.inputADot);
                return;
            } else if (TextUtils.isEmpty(text)) {
                sb.append("0.");
            } else if (!mIsQuantityChanged) {
                sb = new StringBuilder("0.");
            } else {
                sb.append(value);
            }
        } else {
            if (!mIsQuantityChanged) {
                sb = new StringBuilder(value);
            } else {
                sb.append(value);
            }
        }

        if (isValid(sb.toString())) {
            if (mCurMode == WEIGHT_MODE) {
                updateWeightView(sb.toString());
            } else {
                updateCountView(sb.toString());
            }

        }
    }

    private boolean isUnWeighItem() {
        return mShopcartItem.getOrderDish().getDishShop().getSaleType() == SaleType.UNWEIGHING;
    }



    private void notifyShoppingCart(BigDecimal quantity) {
        if (mShopcartItem != null && quantity != null) {
            mShopcartItem.changeQty(quantity);
            mShopcartItemListenter.dataChange(mShopcartItem, mContainProperties);
        }
    }


    public void setShopcartItem(ShopcartItemBase shopcartItem) {
        this.mShopcartItem = shopcartItem;
        updateView();
        mIsQuantityChanged = false;
    }


    private void refreshMainContent() {
        if (mCurMode == SCAN_MODE) {            llLoadingMode.setVisibility(View.VISIBLE);
            rlQuantityMode.setVisibility(View.GONE);
        } else if (mCurMode == NUM_EDIT_MODE) {            llLoadingMode.setVisibility(View.GONE);
            rlQuantityMode.setVisibility(View.VISIBLE);
            mNumLayout.setVisibility(View.VISIBLE);
            mWeightLayout.setVisibility(View.GONE);
            mOkBtn.setVisibility(View.GONE);
        } else {            llLoadingMode.setVisibility(View.GONE);
            rlQuantityMode.setVisibility(View.VISIBLE);
            mNumLayout.setVisibility(View.GONE);
            mWeightLayout.setVisibility(View.VISIBLE);
            mOkBtn.setVisibility(View.VISIBLE);
        }
    }

    private void updateView() {
        if (mShopcartItem != null) {
            updateTitleView();
            if (mCurMode != WEIGHT_MODE) {
                updateCountView(mShopcartItem.getSingleQty().toString());
            }
        }

    }


    private void updateTitleView() {
        String name = mShopcartItem.getSkuName();
        String standardStr = getStandardStr(mShopcartItem.getProperties());
        tvDishName.setText(getTitleText(name, standardStr));
    }

    private String getStandardStr(List<OrderProperty> properties) {
        StringBuilder sb = new StringBuilder();
        if (Utils.isNotEmpty(properties)) {
            int size = properties.size();
            for (int i = 0; i < size; i++) {
                OrderProperty orderProperty = properties.get(i);
                sb.append(orderProperty.getPropertyName());
                if (i < size - 1) {
                    sb.append("、");
                }
            }
        }

        return sb.toString();
    }


    private void updateCountView(String text) {
        tvQuantity.setText(text);
        mIsQuantityChanged = true;
    }


    private void updateWeightView(String text) {
        if (TextUtils.isEmpty(text)) {
            mCurWeight = 0;
            mWeight.setText(text);
            mOkBtn.setText(mActivity.getString(R.string.ok_button));
            mIsQuantityChanged = false;
        } else {
            mCurWeight = Float.valueOf(text);

            mathWeightDishPrice();

            if (mShopcartItem != null && mShopcartItem.getOrderDish().getUnitName() != null) {
                mWeight.setText(text + mShopcartItem.getOrderDish().getUnitName() + "");
            } else if (mShopcartItem != null && mShopcartItem.getOrderDish().getUnitName() == null) {
                mWeight.setText(text + "");
            } else {
                mWeight.setText(mCurWeight + "");
            }
            mIsQuantityChanged = true;
        }

    }


    private void mathWeightDishPrice() {
        BigDecimal price = BigDecimal.ONE;
        BigDecimal weight = new BigDecimal(mCurWeight);
        price = MathDecimal.round(weight.multiply(mShopcartItem.getPrice()), 2);
        mOkBtn.setText(String.format(mActivity.getString(R.string.weight_all_price), ShopInfoCfg.formatCurrencySymbol(price)));
    }

    private String getTitleText(String name, String standardStr) {
        if (!TextUtils.isEmpty(standardStr)) {
            return name + "(" + standardStr + ")";
        }

        return name;
    }

    private void forbiddenSoftKeyboard(EditText editText) {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(editText, false);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }

    }


    public void startLoadingAnimation(ImageView imageView) {
        if (imageView.getAnimation() != null) {
            imageView.clearAnimation();
        }

        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivLoading.setVisibility(View.GONE);
                tvDishName.setVisibility(View.VISIBLE);
            }
        });
        imageView.startAnimation(animation);
        tvDishName.setVisibility(View.INVISIBLE);
        ivLoading.setVisibility(View.VISIBLE);
    }


    public void weightLoadingAnimation(ImageView imageView) {
        if (imageView.getAnimation() != null) {
            imageView.clearAnimation();
        }

        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        imageView.startAnimation(animation);
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.tv_dot:
                executeAppend(DOT);
                break;
            case R.id.delete:
                String temp = mWeight.getText().toString();
                if (mCurMode == WEIGHT_MODE && !TextUtils.isEmpty(mWeight.getText())) {
                    if (((mShopcartItem.getOrderDish().getUnitName() != null && temp.contains(mShopcartItem.getOrderDish().getUnitName())))
                            && temp.length() >= mShopcartItem.getOrderDish().getUnitName().length() + 2) {
                        if (mShopcartItem != null && mShopcartItem.getOrderDish().getUnitName() != null) {
                            temp = temp.substring(0, temp.length() - mShopcartItem.getOrderDish().getUnitName().length() - 1) + "";
                        }

                    } else if ((mShopcartItem.getOrderDish().getUnitName() == null) && temp.length() > 2) {                        temp = temp.substring(0, temp.length() - 1);
                    } else {
                        temp = "";
                    }
                    updateWeightView(temp);
                } else if (!TextUtils.isEmpty(tvQuantity.getText())) {
                    tvQuantity.setText(tvQuantity.getText().toString().subSequence(0, tvQuantity.length() - 1));
                }
                mIsCustomInput = true;
                break;
            case R.id.one:
                executeAppend("1");
                break;
            case R.id.two:
                executeAppend("2");
                break;
            case R.id.three:
                executeAppend("3");
                break;
            case R.id.four:
                executeAppend("4");
                break;
            case R.id.five:
                executeAppend("5");
                break;
            case R.id.six:
                executeAppend("6");
                break;
            case R.id.seven:
                executeAppend("7");
                break;
            case R.id.eight:
                executeAppend("8");
                break;
            case R.id.nine:
                executeAppend("9");
                break;
            case R.id.zero:
                executeAppend("0");
                break;
            case R.id.reduce:
                if (!ClickManager.getInstance().isClicked()) {
                    executeSubtract();
                }
                break;
            case R.id.add:
                if (!ClickManager.getInstance().isClicked()) {
                    executeAdd();
                }
                break;
            case R.id.v_left_zone:
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_ok:
                if (!ClickManager.getInstance().isClicked()) {
                    MobclickAgent.onEvent(mActivity, "fast_food_single_weigh");
                    if (mIsDinner && mIsCustomInput) {
                        MobclickAgent.onEvent(mActivity, "fast_food_single_weigh_input");
                        VerifyHelper.verifyAlert((FragmentActivity) mActivity, DinnerApplication.PERMISSION_DINNER_QUANTITY,
                                new VerifyHelper.Callback() {

                                    @Override
                                    public void onPositive(User user, String code, Auth.Filter filter) {
                                        if (mCurWeight != 0 && mCurWeight < MAX_COUNT) {
                                            BigDecimal increaseUnit = mShopcartItem.getOrderDish().getDishShop().getDishIncreaseUnit();
                                                                                        BigDecimal singleQty = new BigDecimal(("" + mCurWeight));
                                            if (singleQty.compareTo(increaseUnit) < 0) {                                                singleQty = increaseUnit;
                                                ToastUtil.showShortToast(R.string.weight_less_than_min);
                                            }
                                            singleQty = doSelfInstructed(singleQty);                                            mShopcartItem.changeQty(singleQty);
                                            mShopcartItemListenter.dataChange(mShopcartItem, mContainProperties);
                                            if (mMode == SCAN_MODE) {                                                mCurMode = SCAN_MODE;
                                                mCurWeight = 0;
                                                mWeight.setText("");
                                                refreshMainContent();
                                            } else {
                                                dismiss();
                                            }
                                        } else if (mCurWeight >= MAX_COUNT) {
                                            ToastUtil.showLongToast(R.string.inputFiveInt);
                                        } else {
                                            ToastUtil.showLongToast(R.string.input_weight_error);
                                        }
                                    }
                                });
                    } else {

                        if (mCurWeight != 0 && mCurWeight < MAX_COUNT) {
                            BigDecimal increaseUnit = mShopcartItem.getOrderDish().getDishShop().getDishIncreaseUnit();
                                                        BigDecimal singleQty = new BigDecimal(("" + mCurWeight));
                            if (singleQty.compareTo(increaseUnit) < 0) {                                singleQty = increaseUnit;
                                ToastUtil.showShortToast(R.string.weight_less_than_min);
                            }
                            singleQty = doSelfInstructed(singleQty);                            this.mShopcartItem.changeQty(singleQty);
                            this.mShopcartItemListenter.dataChange(mShopcartItem, mContainProperties);
                            if (this.mMode == SCAN_MODE) {                                mCurMode = SCAN_MODE;
                                mCurWeight = 0;
                                mWeight.setText("");
                                refreshMainContent();
                            } else {
                                dismiss();
                            }
                        } else if (mCurWeight >= MAX_COUNT) {
                            ToastUtil.showLongToast(R.string.inputFiveInt);
                        } else {
                            ToastUtil.showLongToast(R.string.input_weight_error);
                        }
                    }
                }

                break;
            default:
                break;
        }
    }

    private TextWatcher mCountTextChanged = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s) && Utils.isNum(s.toString())) {
                BigDecimal increaseUnit = mShopcartItem.getOrderDish().getDishShop().getDishIncreaseUnit();
                BigDecimal quantity = new BigDecimal(s.toString());
                if (quantity.compareTo(increaseUnit) < 0) {
                    ToastUtil.showShortToast(R.string.order_dish_less_than_min);
                } else {
                    notifyShoppingCart(quantity);
                }
            }

        }
    };


    @Override
    public void onDismiss() {
        if (mElecScaleManager != null) {
            mElecScaleManager.stopGetElecScaleData();
        }
    }


    private void registerKeyboardListener() {
        tvDishName.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    switch (keyCode) {
                                                case 156:
                            executeSubtract();
                            return true;
                                                case 157:
                            executeAdd();
                            return true;
                                                case 160:
                        case 66:
                            dismiss();
                            return true;
                                                case 144:
                        case 145:
                        case 146:
                        case 147:
                        case 148:
                        case 149:
                        case 150:
                        case 151:
                        case 152:
                        case 153:
                            executeAppend((char) (keyCode - 144 + '0') + "");
                            return true;
                                                case 158:
                            executeAppend(DOT);
                            return true;
                                                case 67:
                            if (mCurMode == WEIGHT_MODE && !TextUtils.isEmpty(mWeight.getText())) {
                                mWeight.setText(mWeight.getText().toString().subSequence(0, mWeight.length() - 1));
                            } else if (!TextUtils.isEmpty(tvQuantity.getText())) {
                                tvQuantity.setText(tvQuantity.getText().toString().subSequence(0, tvQuantity.length() - 1));
                            }
                            return true;
                    }
                }
                return true;
            }
        });
    }

    private OnKeyListener scanListener = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (ClickManager.getInstance().isClicked()) {
                    return true;
                }
                startLoadingAnimation(ivLoading);
                scanGoods();
                resetBarcodeView();
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                return true;
            }

            return false;
        }
    };

    static public abstract class DataChangeListener {
                public void dataChange(ShopcartItemBase shopcartItem, boolean isContainProperties) {
            if (shopcartItem instanceof ShopcartItem) {
                dataChange((ShopcartItem) shopcartItem, isContainProperties);
            }
        }

        @Deprecated
        public void dataChange(ShopcartItem shopcartItem, boolean isContainProperties) {
        }
    }

    public void setDinner(boolean dinner) {
        this.mIsDinner = dinner;
    }
}
