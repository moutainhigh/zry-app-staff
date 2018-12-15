package com.zhongmei.bty.dinner.orderdish;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.ui.base.MobclickAgentFragment;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.snack.orderdish.PropertySelectListener;
import com.zhongmei.bty.snack.orderdish.view.AddtionView;
import com.zhongmei.bty.snack.orderdish.view.AddtionView.ExtraInfo;
import com.zhongmei.bty.snack.orderdish.view.AddtionView.OnAddtionCLicked;
import com.zhongmei.bty.snack.orderdish.view.OrderDishCountAndMemoView;
import com.zhongmei.bty.snack.orderdish.view.OrderDishCountAndMemoView.ICountAndMemoListener;
import com.zhongmei.bty.snack.orderdish.view.QuantityEditPopupWindow.DataChangeListener;
import com.zhongmei.bty.snack.orderdish.view.RecipeView;
import com.zhongmei.bty.snack.orderdish.view.StandardView;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.dinner.action.ActionDinnerModifyItemProperty;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.bty.entity.event.orderdishevent.EventDishPropertiesNotice;
import com.zhongmei.bty.cashier.orderdishmanager.DishPropertyManager;
import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListener;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListerTag;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertyVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishStandardVo;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderSetmeal;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EFragment(R.layout.dinner_dish_property)
public class DinnerDishPropertyFragment extends MobclickAgentFragment implements PropertySelectListener, OnAddtionCLicked,
        ICountAndMemoListener {
    private static final String TAG = DinnerDishPropertyFragment.class.getSimpleName();

    private ArrayList<OrderProperty> properties;

    private ArrayList<String> mLables;

    private List<DishPropertyVo> mDishMemos;

    /**
     * 规格类属性分组列表
     */
    private List<PropertyGroupVo<DishStandardVo>> standardGroupList;

    /**
     * 非规格类属性分组列表
     */
    public List<PropertyGroupVo<DishPropertyVo>> propertyGroupList;

    /**
     * 加料列表，可能为null
     */
    private List<OrderExtra> extraList;

    private SetmealShopcartItem mSetmealShopcartItem;

    private String mUuid;

    private String mParentUuid;

    private BigDecimal mSelectedQty;

    private String mMemo;

    // 当前是否有菜品加入
    private boolean added;

    // 判读是否是第一次进入 第一次进入就不去add到ShoppingCart
    private boolean isFirst = true;

    private DinnerShoppingCart mShoppingCart;

    private ArrayList<PropertyGroupVo<DishPropertyVo>> tasteGroupVo;

    // private DishPropertyVo taste;
    // private DishPropertyVo taste;
    private ChangePageListener mChangePageListener;

    private DishSetmealManager mDishSetmealManager;

    private DishPropertyManager manager;

    private int mPageNo = ChangePageListener.ORDERDISHLIST;

    public void registerListener(ChangePageListener changePageListener) {

        this.mChangePageListener = changePageListener;
    }

    @ViewById(R.id.tv_dish_name)
    TextView tvDishName;

    @ViewById(R.id.tv_dish_price)
    TextView tvDishPrice;

    @ViewById(R.id.rl_editable)
    RelativeLayout rlEditable;

    @ViewById(R.id.et_editable_content)
    EditText etEditableContent;

    @ViewById(R.id.btn_delete)
    Button btnDelete;

    @ViewById(R.id.v_change_price)
    View vChangePrice;

    @ViewById(R.id.tv_monetary_unit)
    TextView tvMonetaryUnit;

    @ViewById
    AddtionView add_root_view;

    @ViewById
    RecipeView recipe_root_view;

    @ViewById
    OrderDishCountAndMemoView ll_count_and_memo;

    @ViewById
    StandardView taste_root_view;

    // 限售余额LinearLayout
    @ViewById(R.id.ll_residue)
    LinearLayout llResidue;

    // 商品总量
    @ViewById(R.id.tv_residue_sum)
    TextView tvTotal;

    // 限售余额
    @ViewById(R.id.tv_residue)
    TextView tvResidue;

    // 商品总额的单位
    @ViewById(R.id.tv_residue_sum_unit)
    TextView tvTotalUnit;

    // 限售额的单位
    @ViewById(R.id.tv_residue_unit)
    TextView tvResiduceUnit;

    // 赠送按钮
    @ViewById(R.id.ll_dish_give)
    LinearLayout giveLayout;

    @ViewById(R.id.tv_dish_give)
    Button btnGive;
    ShopcartItem item;

    private ShopcartItemBase<?> mRealItemBase;

    private TextWatcher mSelfDishTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s)) {
                tvMonetaryUnit.setVisibility(View.GONE);
            } else {
                tvMonetaryUnit.setVisibility(View.VISIBLE);
            }
            String skuPrice = etEditableContent.getText().toString();
            modifySkuPrice(skuPrice);
        }
    };

    // 控制小数点前八位，小数点后两位
    private InputFilter mInputFilter = new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
        int beforeDecimal = 8, afterDecimal = 2;

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String etText = etEditableContent.getText().toString();
            String temp = etEditableContent.getText() + source.toString();
            if (temp.equals(".")) {
                return "0.";
            } else if (temp.indexOf(".") == -1) {
                // no decimal point placed yet
                if (temp.length() > beforeDecimal) {
                    return "";
                }
            } else {
                int dotPosition;
                if (etText.indexOf(".") == -1) {
                    dotPosition = temp.indexOf(".");
                } else {
                    dotPosition = etText.indexOf(".");
                }
                int cursorPositon = etEditableContent.getSelectionStart();
                if (cursorPositon <= dotPosition) {
                    String beforeDot = etText.substring(0, dotPosition);
                    if (beforeDot.length() < beforeDecimal) {
                        return source;
                    } else {
                        if (source.toString().equalsIgnoreCase(".")) {
                            return source;
                        } else {
                            return "";
                        }

                    }
                } else {
                    temp = temp.substring(temp.indexOf(".") + 1);
                    if (temp.length() > afterDecimal) {
                        return "";
                    }
                }
            }

            return super.filter(source, start, end, dest, dstart, dend);
        }
    };

    @Click(R.id.btn_close)
    void showProperty() {
        if (mChangePageListener != null) {
            if (mRealItemBase instanceof SetmealShopcartItem) {
                Bundle bundle = new Bundle();

                bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mRealItemBase.getParentUuid());
                bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                bundle.putBoolean(Constant.NONEEDCHECK, false);
                if (mShoppingCart.getTempShopItem() != null
                        && mRealItemBase.getParentUuid().equals(mShoppingCart.getTempShopItem().getUuid())) {
                    mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                } else {
                    mChangePageListener.changePage(ChangePageListener.ORDERDISHLIST, bundle);
                }
            } else {
                mChangePageListener.changePage(mPageNo, null);
            }
        }
    }

    @Click(R.id.tv_dish_price)
    protected void showDishPriceEdit() {
        tvDishPrice.setVisibility(View.GONE);
        rlEditable.setVisibility(View.VISIBLE);
        etEditableContent.requestFocus();
        showSoftKeyboard(etEditableContent);
    }

    @Click(R.id.btn_delete)
    protected void deletePrice() {
        etEditableContent.setText("");
    }

    // add 2016.06.13 begin
    @Click(R.id.tv_dish_give)
    protected void giveDish() {
        if (!ClickManager.getInstance().isClicked()) {
            VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_PRIVILEDGE_PRESENT,
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            addPrivilege();
                        }
                    });
        }
    }

    private void addPrivilege() {
        if (item != null && added) {
            if (btnGive.isSelected()) {// 取消赠送
                btnGive.setSelected(false);
                mShoppingCart.removeShopcarItemPrivilege(item);
            } else {// 赠送
                TradePrivilege p = getGivePrivilege();
                item.setPrivilege(p);
                mShoppingCart.addDishToShoppingCart(item, false);
                btnGive.setSelected(true);
            }
        }
    }

    private TradePrivilege getGivePrivilege() {
        TradePrivilege privilege = new TradePrivilege();
        privilege.setPrivilegeName(getString(R.string.give));
        privilege.setPrivilegeType(PrivilegeType.FREE);
        return privilege;
    }

    /*private void updateGiveLayout() {
        if (item != null && item.getPrivilege() != null && item.getPrivilege().getPrivilegeType() == PrivilegeType.FREE) {
            btnGive.setSelected(true);
        } else {
            btnGive.setSelected(false);
        }
    }*/

    // add 2016.06.13 end
    public static DinnerDishPropertyFragment getInstance(Bundle bundle) {
        DinnerDishPropertyFragment fragment = new DinnerDishPropertyFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    protected void init() {

        mLables = new ArrayList<String>();
        extraList = new ArrayList<OrderExtra>();
        properties = new ArrayList<OrderProperty>();
        tasteGroupVo = new ArrayList<PropertyGroupVo<DishPropertyVo>>();
        mPageNo = getArguments().getInt(Constant.EXTRA_LAST_PAGE);
        mUuid = getArguments().getString(Constant.EXTRA_SHOPCART_ITEM_UUID);
        mParentUuid = getArguments().getString(Constant.EXTRA_SHOPCART_ITEM_PARENT_UUID);
        manager = new DishPropertyManager();
        if (!TextUtils.isEmpty(mParentUuid)) {
            item = mShoppingCart.getShopcartItemByUUID(mShoppingCart.getShoppingCartVo(), mParentUuid);
            int size = item.getSetmealItems().size();
            for (int i = 0; i < size; i++) {
                if (item.getSetmealItems().get(i).getUuid().equals(mUuid)) {
                    mSetmealShopcartItem = item.getSetmealItems().get(i);
                    mRealItemBase = mSetmealShopcartItem;
                    break;
                }
            }
        } else {
            item = mShoppingCart.getDinnerShopcartItem(mUuid);
            mRealItemBase = item;
        }
        added = true;
        mMemo = mRealItemBase.getMemo();
        ll_count_and_memo.setMemo(mRealItemBase.getMemo());
        ll_count_and_memo.setOrderDish(mRealItemBase.getOrderDish());
        String skuName = mRealItemBase.getOrderDish().getSkuName();
        if (!TextUtils.isEmpty(skuName)) {
            tvDishName.setText(Utils.getDisplayStr(skuName, 16));
        }
        mUuid = mRealItemBase.getUuid();
        manager.loadData(mRealItemBase, mShoppingCart.getIsSalesReturn(), true);
        mDishSetmealManager = item.getSetmealManager();
        if (mDishSetmealManager != null) {
            mDishSetmealManager.loadData();
        }
        ll_count_and_memo.setListener(this);
        mSelectedQty = mRealItemBase.getSingleQty();

        mShoppingCart.setShowPropertyPageDishUUID(mShoppingCart.getShoppingCartVo(), mUuid);
        mShoppingCart.setTempShopItem(item);

        etEditableContent.setFilters(new InputFilter[]{mInputFilter});
        // 赠送标记,子菜不支持赠送
        giveLayout.setVisibility(View.GONE);
        /*if (mSetmealShopcartItem != null) {
            giveLayout.setVisibility(View.GONE);

        } else {
            giveLayout.setVisibility(View.VISIBLE);
            updateGiveLayout();
        }*/
    }

    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst = true;
        registerEventBus();
        mShoppingCart = DinnerShoppingCart.getInstance();
        mShoppingCart.registerListener(ShoppingCartListerTag.DINNER_DISH_PROPERTY, new ShoppingCartListener() {

            @Override
            public void removeRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo,
                                     IShopcartItem mShopcartItem) {
                if (mShopcartItem != null && mShopcartItem.getUuid().equals(mUuid)) {
                    ll_count_and_memo.setMemo("");
                }
            }

            @Override
            public void removeSetmealRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo,
                                            ISetmealShopcartItem setmeal) {
                if (setmeal != null && setmeal.getUuid().equals(mUuid)) {
                    ll_count_and_memo.setMemo("");
                }
            }

        });
    }

    public void onEventMainThread(EventDishPropertiesNotice eventDishPropertiesNotice) {
        if (eventDishPropertiesNotice.uuid.equals(mUuid)) {
            // 规格
            standardGroupList = eventDishPropertiesNotice.dishPropertiesVo.getStandardGroupList();
            setStandard(standardGroupList, manager);
            setResidue(eventDishPropertiesNotice);
            etEditableContent.removeTextChangedListener(mSelfDishTextWatcher);
            etEditableContent.setFilters(new InputFilter[]{});
            tvMonetaryUnit.setText(ShopInfoCfg.getInstance().getCurrencySymbol());

            OrderDish orderDish = eventDishPropertiesNotice.dishPropertiesVo.getOrderDish();
            if (orderDish != null && orderDish.getDishShop().getClearStatus() == ClearStatus.SALE) {

                // 加料
                extraList = eventDishPropertiesNotice.dishPropertiesVo.getExtraList();
                setAddtion(extraList);

                // 口味做法
                tasteGroupVo.clear();
                if (eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList() != null) {
                    int size = eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList().size();
                    for (int i = 0; i < size; i++) {
                        DishPropertyType type =
                                eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList().get(i).getPropertyType();
                        if (type == null) {
                            mDishMemos =
                                    eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList()
                                            .get(i)
                                            .getPropertyList();
                        } else if (type.getPropertyKind() == PropertyKind.PROPERTY) {
                            tasteGroupVo.add(eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList().get(i));
                        }
                    }
                }
                // 获取标签
                if (mDishMemos != null && mDishMemos.size() > 0) {
                    int size = mDishMemos.size();
                    mLables.clear();
                    for (int i = 0; i < size; i++) {
                        // 只要备注
                        if (mDishMemos.get(i).getProperty().getPropertyKind() == PropertyKind.MEMO) {
                            mLables.add(mDishMemos.get(i).getProperty().getName());
                        }
                    }
                    ll_count_and_memo.setList(mLables);
                }
                setRecipe(tasteGroupVo);

                // 清除老的规格属性,并添加新的规格属性
                ArrayList<OrderProperty> newProperties = new ArrayList<OrderProperty>();
                for (OrderProperty orderProperty : properties) {
                    if (orderProperty.getPropertyKind() != PropertyKind.STANDARD) {
                        newProperties.add(orderProperty);
                    }
                }
                Set<DishProperty> set = orderDish.getStandards();
                if (set != null) {
                    for (DishProperty dishProperty : set) {
                        DishPropertyType dishPropertyType =
                                DishCache.getPropertyTypeHolder().get(dishProperty.getPropertyTypeId());
                        newProperties.add(new OrderProperty(dishPropertyType, dishProperty));
                    }
                }
                properties.clear();
                properties.addAll(newProperties);

                if (isFirst) {
                    isFirst = false;
                } else if (orderDish instanceof OrderSetmeal) {
                    if (!(((OrderSetmeal) orderDish).getSetmealId() == mSetmealShopcartItem.getOrderDish()
                            .getSetmealId())) {
                        OrderSetmeal orderSetmeal = (OrderSetmeal) orderDish;
                        item.getSetmealItems().remove(mSetmealShopcartItem);
                        mSetmealShopcartItem = new SetmealShopcartItem(mUuid, orderSetmeal, item);
                        mSetmealShopcartItem.setProperties(properties);
                        item.getSetmealItems().add(mSetmealShopcartItem);
                        mShoppingCart.addDishToShoppingCart(item, false);
                    }
                } else {
                    // 因为orderDish对象已经变了，所以要创建新的条目
                    item = new ShopcartItem(item.getUuid(), orderDish);
                    added = true;
                    // item.changeQty(mSelectedQty);
                    BigDecimal increaseUnit = MathDecimal.trimZero(orderDish.getDishShop().getDishIncreaseUnit());
                    item.changeQty(increaseUnit);
                    item.setMemo(mMemo);
                    item.setProperties(properties);
                    mRealItemBase = item;

                    // 称重商品
//                    if (item.getDishShop().getSaleType() == SaleType.WEIGHING) {
//                        QuantityEditPopupWindow popupWindow =
//                                new QuantityEditPopupWindow(getActivity(), item, true, dataChangeListener);
//                        popupWindow.showAtLocation(tvDishName, Gravity.NO_GRAVITY, 0, 0);
//                    } else {
//                        mShoppingCart.addDishToShoppingCart(item, false);
//                    }
                    mShoppingCart.addDishToShoppingCart(item, false);
                }

                if (mRealItemBase != null) {
                    ll_count_and_memo.setListener(null);
                    ll_count_and_memo.setOrderDish(mRealItemBase.getOrderDish());
                    ll_count_and_memo.setListener(this);
                }

                if (orderDish.getIsChangePrice() == Bool.YES) {
                    tvDishPrice.setText(Utils.formatPrice(MathDecimal.trimZero(orderDish.getPrice()).doubleValue()));
                    String text = MathDecimal.toTrimZeroString(orderDish.getPrice());
                    etEditableContent.setText(text);
                    etEditableContent.setSelection(text.length());
                    etEditableContent.addTextChangedListener(mSelfDishTextWatcher);
                    etEditableContent.setFilters(new InputFilter[]{mInputFilter});

                    rlEditable.setVisibility(View.GONE);
                    tvDishPrice.setVisibility(View.VISIBLE);
                    vChangePrice.setVisibility(View.VISIBLE);
                } else {
                    vChangePrice.setVisibility(View.GONE);
                }
                //add 2016 06 14 begin
                // updateGiveLayout();
                //add 2016 06 14 end
            } else {
                added = false;
                mShoppingCart.removeDinnerDish(item);
                // btnGive.setSelected(false);//add 2016 06 14
                isFirst = false;
                setAddtion(null);
                setRecipe(null);
                ll_count_and_memo.setList(null);
                vChangePrice.setVisibility(View.GONE);
            }
        }
    }

    private void setResidue(BigDecimal saleTotal, BigDecimal residueTotal, String unit) {

        tvTotal.setText(saleTotal.toString());

        if (residueTotal.compareTo(BigDecimal.ZERO) < 1) {
            tvResidue.setText("0");
        } else {
            tvResidue.setText(residueTotal.toString());
        }

        tvTotalUnit.setText(String.format(getString(R.string.order_residue_property), unit));
        tvResiduceUnit.setText(unit);

    }

    private void setResidue(EventDishPropertiesNotice eventDishPropertiesNotice) {

        OrderDish orderDish = eventDishPropertiesNotice.dishPropertiesVo.getOrderDish();
        if (orderDish != null) {
            llResidue.setVisibility(View.VISIBLE);
            setResidue(orderDish.getDishShop().getSaleTotal(),
                    orderDish.getDishShop().getResidueTotal(),
                    TextUtils.isEmpty(mRealItemBase.getUnitName()) ? "" : mRealItemBase.getUnitName());
        } else {
            llResidue.setVisibility(View.GONE);
        }
    }

    public void onEventMainThread(ActionDinnerModifyItemProperty action) {
        if (TextUtils.equals(action.getUuid(), mUuid)) {
            // 刷新属性界面数量
            ll_count_and_memo.setCount(action.getSelectedQty());
        }
    }

    public void setRecipe(ArrayList<PropertyGroupVo<DishPropertyVo>> recipeList) {
        // 清除之前选中的口味做法
        properties.clear();
        if (recipeList == null || recipeList.size() == 0) {
            recipe_root_view.setVisibility(View.GONE);
            return;
        }
        recipe_root_view.setListener(this);
        recipe_root_view.setVisibility(View.VISIBLE);
        recipe_root_view.setList(recipeList);
    }

    public void setStandard(List<PropertyGroupVo<DishStandardVo>> propertyGroupVo, DishPropertyManager manager) {
        if (propertyGroupVo == null || propertyGroupVo.size() == 0) {
            taste_root_view.setVisibility(View.GONE);
            return;
        }
        taste_root_view.setVisibility(View.VISIBLE);
        // taste_root_view.setListener(this);
        taste_root_view.setList(propertyGroupVo, manager, mShoppingCart);
    }

    public void setAddtion(List<OrderExtra> extraList) {
        if (extraList == null || extraList.size() == 0) {
            add_root_view.setVisibility(View.GONE);
            return;
        }
        add_root_view.setVisibility(View.VISIBLE);
        add_root_view.setListener(this);
        add_root_view.setList(extraList);
    }

    @Override
    public boolean addProperty(OrderProperty property, boolean needAdd) {
        for (int i = 0; i < properties.size(); i++) {
            if (property.getProperty().equals(properties.get(i).getProperty()))
                return true;
        }
        properties.add(property);
        if (mSetmealShopcartItem != null) {
            mSetmealShopcartItem.setProperties(properties);
        } else {
            item.setProperties(properties);
        }

        if (needAdd) {
            // ToastUtil.showShortToast("属性加入");
            mShoppingCart.addDishToShoppingCart(item, false);
        }

        //做法导致菜品金额为负，移除该做法
        if (item.getActualAmount().compareTo(BigDecimal.ZERO) < 0) {
            properties.remove(property);
            DinnerShoppingCart.getInstance().refreshDish();

            ToastUtil.showShortToast(R.string.property_make_dish_price_less_than_0);

            return false;
        }

        return true;
    }

    @Override
    public boolean deleteProperty(OrderProperty property) {
        int index = -1;
        for (int i = 0; i < properties.size(); i++) {
            if (property.getProperty().equals(properties.get(i).getProperty())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            properties.remove(index);
            if (mSetmealShopcartItem != null) {
                mSetmealShopcartItem.setProperties(properties);
            } else {
                item.setProperties(properties);
            }
            mShoppingCart.addDishToShoppingCart(item, false);
        }

        //做法导致菜品金额为负，添加该做法
        if (item.getActualAmount().compareTo(BigDecimal.ZERO) < 0) {
            properties.add(property);
            DinnerShoppingCart.getInstance().refreshDish();

            ToastUtil.showShortToast(R.string.property_make_dish_price_less_than_0);

            return false;
        }

        return true;
    }

    @Override
    public void deleteProperties() {
        if (added) {
            recipe_root_view.clearSelect();
            properties.clear();
        }
    }

    @Override
    public boolean onAddtionClicked(ExtraInfo extraInfo, BigDecimal qty) {
        //修改前的数量，方便修改后进行还原
        BigDecimal oldQty = BigDecimal.ZERO;
        if (mSetmealShopcartItem != null) {
            oldQty = mSetmealShopcartItem.getExtraQty(extraInfo.orderExtra);
            mSetmealShopcartItem.setExtra(extraInfo.orderExtra, qty);
        } else {
            oldQty = item.getExtraQty(extraInfo.orderExtra);
            item.setExtra(extraInfo.orderExtra, qty);
        }

        mShoppingCart.addDishToShoppingCart(item, false);

        //做法导致菜品金额为负，移除该做法
        if (item.getActualAmount().compareTo(BigDecimal.ZERO) < 0) {
            //还原到修改前的金额
            if (mSetmealShopcartItem != null) {
                mSetmealShopcartItem.setExtra(extraInfo.orderExtra, oldQty);
            } else {
                item.setExtra(extraInfo.orderExtra, oldQty);
            }
            DinnerShoppingCart.getInstance().refreshDish();

            ToastUtil.showShortToast(R.string.extra_make_dish_price_less_than_0);

            return false;
        }

        return true;
    }

    @Override
    public void onSelectedQtyChanged(BigDecimal selectedQty) {
        if (mSelectedQty.compareTo(selectedQty) == 0) {
            return;
        }
        if (added) {
            if (mSetmealShopcartItem != null) {
                switch (mDishSetmealManager.modifySetmeal(mSetmealShopcartItem, selectedQty)) {
                    case SUCCESSFUL:
                        mSetmealShopcartItem.changeQty(selectedQty);
                        // mSetmealShopcartItem.setTotalQty(item.getTotalQty().multiply(selectedQty));
                        // mSetmealShopcartItem.setSingleQty(selectedQty);
                        break;
                    case FAILED_MULTI:
                        ToastUtil.showLongToast(R.string.order_dish_setmeal_modify_failed_multi);
                        ll_count_and_memo.setCount(mSelectedQty);
                        return;
                    case FAILED_GREATER_THAN_MAX:
                        ToastUtil.showLongToast(R.string.order_dish_setmeal_count_exceed);
                        ll_count_and_memo.setCount(mSelectedQty);
                        return;
                    default:
                        ToastUtil.showShortToast(R.string.order_dish_setmeal_modify_failed);
                        ll_count_and_memo.setCount(mSelectedQty);
                        return;
                }
            } else {
                item.changeQty(selectedQty);
                // item.setTotalQty(selectedQty);
                // item.setSingleQty(selectedQty);
            }
            // List<ExtraShopcartItem> list = null;
            // if (mSetmealShopcartItem != null) {
            // list = mSetmealShopcartItem.getExtraItems();
            // } else {
            // list = item.getExtraItems();
            // }
            // if (list != null) {
            // int size = list.size();
            // OrderExtra mExtra = null;
            // for (int i = 0; i < size; i++) {
            // mExtra = list.get(i).getOrderDish();
            // if
            // (mExtra.getSingleQty().compareTo(BigDecimal.ZERO)
            // == 1) {
            // if (mSetmealShopcartItem == null) {
            // mExtra.setTotalQty(mExtra.getSingleQty().multiply(item.getTotalQty()));
            // } else {
            // mExtra.setTotalQty(mExtra.getSingleQty().multiply(mSetmealShopcartItem.getTotalQty()));
            // }
            //
            // }
            // }
            // }

            mShoppingCart.addDishToShoppingCart(item, false);
        }
        mSelectedQty = selectedQty;
    }

    @Override
    public void onMemoChanged(String memo) {
        mMemo = memo;
        if (added) {
            if (mSetmealShopcartItem != null) {
                mSetmealShopcartItem.setMemo(memo);
            } else {
                item.setMemo(memo);
            }

            mShoppingCart.addDishToShoppingCart(item, false);
        }
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
        mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.DINNER_DISH_PROPERTY);
        super.onDestroy();
    }

    private void modifySkuPrice(String skuPrice) {
        if (TextUtils.isEmpty(skuPrice) || !Utils.isNum(skuPrice)) {
            return;
        }
        BigDecimal price = new BigDecimal(skuPrice);
        if (mSetmealShopcartItem != null) {
            mSetmealShopcartItem.changePrice(price);
        } else {
            item.changePrice(price);
        }
        mShoppingCart.addDishToShoppingCart(item, false);
    }

    private DataChangeListener dataChangeListener = new DataChangeListener() {

        @Override
        public void dataChange(ShopcartItem shopcartItem, boolean isContainProperties) {
            // TODO Auto-generated method stub
            mShoppingCart.addDishToShoppingCart(item, false);
            ll_count_and_memo.setCount(item.getSingleQty());
        }
    };
}
