package com.zhongmei.bty.dinner.orderdish;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhongmei.bty.router.RouteIntent;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryInfo;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateItemTool;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.ui.base.MobclickAgentFragment;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.dinner.adapter.DinnerDishSetmealAdapter;
import com.zhongmei.bty.snack.orderdish.view.OrderDishCountAndMemoView;
import com.zhongmei.bty.snack.orderdish.view.OrderDishCountAndMemoView.ICountAndMemoListener;
import com.zhongmei.bty.snack.orderdish.view.QuantityEditPopupWindow.DataChangeListener;
import com.zhongmei.bty.snack.orderdish.view.SetmealButton;
import com.zhongmei.bty.snack.orderdish.view.TouchGridView;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;
import com.zhongmei.bty.dinner.action.ActionDinnerModifyItemProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.entity.dish.DishSetmealGroup;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.bty.entity.event.orderdishevent.EventDishPropertiesNotice;
import com.zhongmei.bty.basemodule.orderdish.event.EventSetmealGroupsNotice;
import com.zhongmei.bty.basemodule.orderdish.event.EventSetmealModifyNotice;
import com.zhongmei.bty.basemodule.orderdish.event.EventSetmealsNotice;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager;
import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager.ModifyResult;
import com.zhongmei.bty.data.operates.DishDal;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.listerner.ModifyShoppingCartListener;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListener;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListerTag;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertyVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date：2015年7月8日 下午4:44:46
 * @Description: 套餐点菜界面
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
@EFragment(R.layout.dinner_dish_setmeal)
public class DinnerDishSetmealFragment extends MobclickAgentFragment implements TouchGridView.ItemTouchListener,
        ICountAndMemoListener {

    private final static String TAG = DinnerDishSetmealFragment.class.getSimpleName();

    @ViewById(R.id.ll_setmeal_group)
    protected LinearLayout llSetmealGroup;

    @ViewById(R.id.gv_setmeal_child_list)
    protected TouchGridView gvSetmealChildList;

    @ViewById(R.id.tv_select_info)
    protected TextView tvSelectInfo;

    @ViewById(R.id.tv_setmeal_name)
    protected TextView tvSetmealName;

    @ViewById(R.id.ll_setmeal_child)
    protected LinearLayout llSetmealChild;

    @ViewById(R.id.ll_setmeal_property)
    protected ScrollView llSetmealProperty;

    @ViewById(R.id.ll_count_and_memo)
    protected OrderDishCountAndMemoView llCountAndMemo;// 备注、分数

//    @ViewById(R.id.btn_other)
//    protected SetmealButton btnOther;

    @ViewById(R.id.btn_finish)
    protected Button btnFinish;

    @ViewById(R.id.btn_close)
    protected ImageButton btnClose;

    private ChangePageListener mChangePageListener;

    private int mPageNo = ChangePageListener.ORDERDISHLIST;

    private SetmealButton mCurrentClickedGroupItem;// 当前View


    private DishSetmealManager mDishSetmealManager;// 套餐管理器

    private InventoryCacheUtil mInventoryCacheUtil;

    // 赠送按钮
    @ViewById(R.id.ll_dish_give)
    protected LinearLayout giveLayout;

    @ViewById(R.id.tv_dish_give)
    protected Button btnGive;
    protected DinnerDishSetmealAdapter mAdapter;

    /**
     * 购物车对象
     */
    private DinnerShoppingCart mShoppingCart;

    private ShopcartItem mShopcartItem;

    private Drawable mDrawable;

    private ModifyShoppingCartListener mShoppingCartListener = new ShoppingCartListener() {
        /**
         * 删除备注
         */
        public void removeRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem shopcartItem) {
            if (shopcartItem != null && shopcartItem.getUuid().equals(mShopcartItem.getUuid())) {
                llCountAndMemo.setMemo("");
            }
        }

        ;

        @Override
        public void exception(String message) {
            new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(getResources().getString(R.string.invalidLogin))
                    .iconType(R.drawable.commonmodule_dialog_icon_warning)
                    .negativeText(R.string.reLogin)
                    .negativeLisnter(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            RouteIntent.startLogin(getActivity());
                        }
                    })
                    .build()
                    .show(getActivity().getSupportFragmentManager(), TAG);
        }

        ;

    };

    public void registerListener(ChangePageListener changePageListener) {
        this.mChangePageListener = changePageListener;
    }

    public static DinnerDishSetmealFragment newInstance(Bundle bundle) {
        DinnerDishSetmealFragment dinnerDishSetmealFragment = new DinnerDishSetmealFragment_();
        dinnerDishSetmealFragment.setArguments(bundle);
        return dinnerDishSetmealFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
        mShoppingCart = DinnerShoppingCart.getInstance();
        mShoppingCart.registerListener(ShoppingCartListerTag.DINNER_DISH_COMBO, mShoppingCartListener);
        mInventoryCacheUtil = InventoryCacheUtil.getInstance();
        mInventoryCacheUtil.registerListener(ChangePageListener.DISHCOMBO, mInventoryListener);
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
        mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.DINNER_DISH_COMBO);
        mInventoryCacheUtil.unRegisterListener(ChangePageListener.DISHCOMBO);
        super.onDestroy();
    }

    @AfterViews
    protected void init() {
        initAdapter();
        gvSetmealChildList.setNumColumns(3);
        gvSetmealChildList.setAdapter(mAdapter);
        gvSetmealChildList.setListener(this);
        setFinishBg();
        if (getArguments() != null) {
            mPageNo = getArguments().getInt(Constant.EXTRA_LAST_PAGE);
            String uuid = getArguments().getString(Constant.EXTRA_SHOPCART_ITEM_UUID);

            mShopcartItem = mShoppingCart.getDinnerShopcartItem(uuid);
            mShoppingCart.setTempShopItem(mShopcartItem);
            mDishSetmealManager = mShopcartItem.getSetmealManager();
            //有可能mDishSetmealManager 为null，还没跟踪原因
            if (mDishSetmealManager != null)
                mDishSetmealManager.loadData();
            // 设置套餐名称
            tvSetmealName.setText(mShopcartItem.getOrderDish().getDishShop().getName());

            // 初始化备注、数量
            llCountAndMemo.setOrderDish(mShopcartItem.getOrderDish());
            llCountAndMemo.setMemo(mShopcartItem.getMemo());
            llCountAndMemo.setListener(this);

            //add 2016 06 14 begin
            giveLayout.setVisibility(View.GONE);
            //  updateGiveLayout();
            //add 2016 06 14 end
        }

    }

    protected void initAdapter() {
        mAdapter = new DinnerDishSetmealAdapter(getActivity(), 3);
    }

    protected void setFinishBg() {
        btnFinish.setBackgroundResource(R.drawable.orderdish_setmeal_submit_bg_selector);
    }

    @Click(R.id.btn_close)
    protected void clickCloseButton() {
        if (mChangePageListener != null) {
            mShoppingCart.setTempShopItem(mShopcartItem);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, null);
            bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
            bundle.putBoolean(Constant.NONEEDCHECK, false);
            mChangePageListener.changePage(mPageNo, bundle);
        }
    }

    @Click(R.id.btn_finish)
    protected void clickFinishButton() {
        mShoppingCart.isCheckAdd(mShoppingCart.getShoppingCartVo(), mShopcartItem, true);
        //主单不允许添加称重套餐子菜
        if (mShoppingCart.getOrder().isUnionMainTrade()) {
            if (Utils.isNotEmpty(mShopcartItem.getSetmealItems())) {
                for (SetmealShopcartItem setmealShopcartItem : mShopcartItem.getSetmealItems()) {
                    if (setmealShopcartItem.getSaleType() == SaleType.WEIGHING) {
                        ToastUtil.showShortToast(R.string.union_main_trade_not_allow_weighing_dish);
                        return;
                    }
                }
            }
        }
        if (mChangePageListener != null) {
            mChangePageListener.changePage(mPageNo, null);
        }
    }

//    @Click(R.id.btn_other)
//    protected void clickOtherButton() {
//        if (!btnOther.isSelected()) {
//            showPropertyView(true);
//            if (mCurrentClickedGroupItem != null) {
//                mCurrentClickedGroupItem.setSelected(false);
//                updateDishSetmealGroup(mCurrentClickedGroupItem, (DishSetmealGroupVo) mCurrentClickedGroupItem.getTag());
//            }
//            btnOther.setSelected(true);
//            mCurrentClickedGroupItem = btnOther;
//        }
//    }

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
        if (mShopcartItem != null) {
            if (btnGive.isSelected()) {// 取消赠送
                btnGive.setSelected(false);
                mShoppingCart.removeShopcarItemPrivilege(mShopcartItem);
            } else {// 赠送
                TradePrivilege p = getGivePrivilege();
                mShopcartItem.setPrivilege(p);
                mShoppingCart.addDishToShoppingCart(mShopcartItem, false);
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
        if (mShopcartItem != null && mShopcartItem.getPrivilege() != null && mShopcartItem.getPrivilege().getPrivilegeType() == PrivilegeType.FREE) {
            btnGive.setSelected(true);
        } else {
            btnGive.setSelected(false);
        }
    }*/

    // add 2016.06.13 end
    private void initDishSetmealGroup(final List<DishSetmealGroupVo> groupVoListList) {
        llSetmealGroup.removeAllViews();

        int size = groupVoListList.size();
        for (int i = 0; i < size; i++) {
            final DishSetmealGroupVo groupVo = groupVoListList.get(i);
            // 创建一个SetmealButton
            SetmealButton setmealButton = new SetmealButton(getActivity());
            setmealButton.setId(i);
            setmealButton.setMainBackground(getBackground(groupVo));
            setmealButton.setTag(groupVo);
            setmealButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        return;
                    }

                    showPropertyView(false);

                    if (mCurrentClickedGroupItem != null) {
                        mCurrentClickedGroupItem.setSelected(false);
                        updateDishSetmealGroup(mCurrentClickedGroupItem,
                                (DishSetmealGroupVo) mCurrentClickedGroupItem.getTag());
                    }

                    SetmealButton s = (SetmealButton) v;
                    s.setActivated(false);
                    s.setSelected(true);
                    // 此时之前点击的那个Item,必须置为之前的状态
                    mCurrentClickedGroupItem = s;
                    // 切换界面
                    mDishSetmealManager.switchGroup((DishSetmealGroupVo) v.getTag());
                }
            });
            llSetmealGroup.addView(setmealButton, getSetmealGroupParams());
            updateDishSetmealGroup(setmealButton, groupVo);
        }
    }

    protected LayoutParams getSetmealGroupParams() {
        int height = DensityUtil.dip2px(MainApplication.getInstance(), 102);
        int width = DensityUtil.dip2px(MainApplication.getInstance(), 170);
        LayoutParams layoutParams = new LayoutParams(width, height, Gravity.CENTER);
        layoutParams.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 20);
        return layoutParams;
    }

    /**
     * @Title: updateDishSetmealGroup
     * @Description: 刷新套餐分组中单个分组
     * @Param @param setmealButton
     * @Param @param groupVo TODO
     * @Return void 返回类型
     */
    private void updateDishSetmealGroup(SetmealButton setmealButton, DishSetmealGroupVo groupVo) {
        if (setmealButton == null || groupVo == null) {
            return;
        }

        BigDecimal selectedQty = groupVo.getSelectedQty();
        if (!setmealButton.isSelected() && selectedQty != null) {
            if (selectedQty.compareTo(BigDecimal.ZERO) > 0) {
                setmealButton.setSelected(false);
                setmealButton.setActivated(true);
            } else if (selectedQty.compareTo(BigDecimal.ZERO) == 0) {
                setmealButton.setSelected(false);
                setmealButton.setActivated(false);
            }
        }

        DishSetmealGroup dishSetmealGroup = groupVo.getSetmealGroup();
        if (dishSetmealGroup != null) {
            // 设置标记是否可见
            if (selectedQty != null && selectedQty.compareTo(dishSetmealGroup.getOrderMin()) >= 0) {
                setmealButton.setTagVisibility(View.VISIBLE);
            } else {
                setmealButton.setTagVisibility(View.INVISIBLE);
            }

            // 设置标题
            setmealButton.setTitleText(dishSetmealGroup.getName());
            setmealButton.setTitleColor(getButtonTextStyle());
            // 描述
            String rangeText =
                    MathDecimal.toTrimZeroString(dishSetmealGroup.getOrderMin()) + "-"
                            + MathDecimal.toTrimZeroString(dishSetmealGroup.getOrderMax());
            if (dishSetmealGroup.getOrderMax().compareTo(dishSetmealGroup.getOrderMin()) == 0) {
                rangeText = dishSetmealGroup.getOrderMax().toString();
            }
            String desc =
                    getActivity().getString(R.string.order_dish_setmeal_group_desc,
                            MathDecimal.toTrimZeroString(selectedQty),
                            rangeText);
            setmealButton.setDescriptionText(desc);
        }
    }

    protected Drawable getBackground(DishSetmealGroupVo groupVo) {
        mDrawable =
                getActivity().getResources().getDrawable(R.drawable.orderdish_setmealgroup_item_bg_selector);
        return mDrawable;
    }

    protected int getButtonTextStyle() {
        return getResources().getColor(R.color.orderdish_setmeal_group_title_selector);
    }


    /**
     * @Title: onEventMainThread
     * @Description: 套餐分组数据
     * @Param @param eventSetmealGroupsNotice TODO
     * @Return void 返回类型
     */
    public void onEventMainThread(EventSetmealGroupsNotice eventSetmealGroupsNotice) {
        if (eventSetmealGroupsNotice != null && eventSetmealGroupsNotice.uuid.equals(mShopcartItem.getUuid())) {
            if (eventSetmealGroupsNotice.groupVoList != null && !eventSetmealGroupsNotice.groupVoList.isEmpty()) {
                initDishSetmealGroup(eventSetmealGroupsNotice.groupVoList);
                // 默认选中第一项
                ((SetmealButton) llSetmealGroup.findViewWithTag(eventSetmealGroupsNotice.groupVoList.get(0))).performClick();

                refreshClosePageButton();
            }
            if (Utils.isNotEmpty(eventSetmealGroupsNotice.selectedList)) {
                addSelectedDish(eventSetmealGroupsNotice.selectedList);
            }
        }
    }

    /**
     * @Title: onEventMainThread
     * @Description: 点击某个套餐分组
     * @Param @param eventSetmealsNotice TODO
     * @Return void 返回类型
     */
    public void onEventMainThread(EventSetmealsNotice eventSetmealsNotice) {
        if (eventSetmealsNotice != null && eventSetmealsNotice.uuid.equals(mShopcartItem.getUuid())) {
            if (mAdapter != null) {
                List<DishSetmealVo> data = eventSetmealsNotice.setmealVoList;
                setDishSetmealInventoryNum(data);
                mAdapter.setDishList(data);
            }

            if (eventSetmealsNotice.setmealGroup != null) {
                refreshSelectedInfo(eventSetmealsNotice.setmealGroup);
            }
        }
    }

    /**
     * @Title: onEventMainThread
     * @Description: 菜品添加
     * @Param @param eventAddedSetmealNotice TODO
     * @Return void 返回类型
     */
    public void onEventMainThread(EventSetmealModifyNotice event) {
        // 操作购物车
        if (event != null && event.modifyResult == ModifyResult.SUCCESSFUL
                && event.uuid.equals(mShopcartItem.getUuid())) {
            // 如果数据改变在当前选择的套餐分组下，则改变选择信息与子菜列表
            if (mCurrentClickedGroupItem != null && mCurrentClickedGroupItem.getTag() != null) {
                DishSetmealGroupVo currentGroupVo = (DishSetmealGroupVo) mCurrentClickedGroupItem.getTag();
                if (currentGroupVo.getSetmealGroup().getId() == event.groupdVo.getSetmealGroup().getId()) {
                    // 刷新子菜列表
                    if (mAdapter != null) {
                        mAdapter.setDish(event.setmealVo);
                    }

                    // 刷新列表提示界面
                    refreshSelectedInfo(event.groupdVo);

                    updateDishSetmealGroup(mCurrentClickedGroupItem, event.groupdVo);

                    mCurrentClickedGroupItem.setTag(event.groupdVo);
                } else {
                    refreshSetmealGroup(event.groupdVo);
                }
            } else {
                refreshSetmealGroup(event.groupdVo);
            }

            refreshClosePageButton();
        }
    }

    public void onEventMainThread(EventDishPropertiesNotice eventDishPropertiesNotice) {
        if (eventDishPropertiesNotice != null && eventDishPropertiesNotice.uuid.equals(mShopcartItem.getUuid())
                && eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList() != null) {
            int size = eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList().size();
            List<DishPropertyVo> dishProperties = null;
            for (int i = 0; i < size; i++) {
                DishPropertyType type =
                        eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList().get(i).getPropertyType();
                if (type == null) {
                    dishProperties =
                            eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList().get(i).getPropertyList();
                }
            }

            List<String> memoList = new ArrayList<String>();
            if (dishProperties != null && dishProperties.size() > 0) {
                for (DishPropertyVo dishPropertyVo : dishProperties) {
                    if (dishPropertyVo.getProperty().getPropertyKind() == PropertyKind.MEMO) {
                        memoList.add(dishPropertyVo.getProperty().getName());
                    }
                }
            }

            llCountAndMemo.setList(memoList);
        }
    }

    public void onEventMainThread(ActionDinnerModifyItemProperty action) {
        if (TextUtils.equals(action.getUuid(), mShopcartItem.getUuid())) {
            // 刷新属性界面数量
            llCountAndMemo.setCount(action.getSelectedQty());
        }
    }

    @Override
    public void onTouch(int postion) {
        if (mAdapter != null) {
            final DishSetmealVo dishSetmealVo = (DishSetmealVo) mAdapter.getItem(postion);
            if (dishSetmealVo.isClear()) {
                return;
            }

            if (!mDishSetmealManager.isRequisiteClearListEmpty()) {
                ToastUtil.showShortToast(R.string.order_dish_setmeal_replace_dish_clear);
            } else if (mDishSetmealManager.isRequisiteDeleted()) {
                ToastUtil.showShortToast(R.string.order_dish_setmeal_replace_dish_deleted);
            }

            dealDishVo(dishSetmealVo);
        }

    }

    /**
     * @Title: dealDish
     * @Description: 处理菜品
     * @Param @param dishVo TODO
     * @Return void 返回类型
     */
    private void dealDishVo(DishSetmealVo dishSetmealVo) {
        // 必须菜不允许编辑
        if (dishSetmealVo.isContainProperties()) {
            DishSetmealGroupVo groupVo = (DishSetmealGroupVo) mCurrentClickedGroupItem.getTag();
            BigDecimal selectedQty = groupVo.getSelectedQty();// 已选数量
            BigDecimal orderMax = groupVo.getSetmealGroup().getOrderMax();// 最大数量
            if (selectedQty != null && orderMax != null && selectedQty.compareTo(orderMax) == 0) {
                ToastUtil.showLongToast(R.string.order_dish_setmeal_count_exceed);
            } else {
                if (mChangePageListener != null) {
                    SetmealShopcartItem setmealShopcartItem = CreateItemTool.createSetmealShopcartItem(mShopcartItem, dishSetmealVo);
                    // mShoppingCart.addShippingToCart(setmealShopcartItem,
                    // false);

                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, setmealShopcartItem.getUuid());
                    bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.DISHCOMBO);
                    bundle.putBoolean(Constant.NONEEDCHECK, true);
                    mChangePageListener.changePage(ChangePageListener.DISHPROPERTY, bundle);
                }
            }
        } else {// 判断是否添加
            // 生成步值
            BigDecimal leastCellNum = MathDecimal.trimZero(dishSetmealVo.getSetmeal().getLeastCellNum());
            BigDecimal selectedQty = leastCellNum;
            final SetmealShopcartItem setmealShopcartItem = CreateItemTool.createSetmealShopcartItem(mShopcartItem, dishSetmealVo);
            switch (mDishSetmealManager.modifySetmeal(setmealShopcartItem, selectedQty)) {
                case SUCCESSFUL:
                    setmealShopcartItem.changeQty(leastCellNum);
//						setmealShopcartItem.setSingleQty(leastCellNum);
//						setmealShopcartItem.setTotalQty(leastCellNum.multiply(mShopcartItem.getTotalQty()));
                    if (mShopcartItem.getSetmealItems() == null) {
                        mShopcartItem.setSetmealItems(new ArrayList<SetmealShopcartItem>());
                    }
                    mShopcartItem.getSetmealItems().add(setmealShopcartItem);
                    mShoppingCart.addDishToShoppingCart(mShopcartItem, true, false);
//                    /*if(setmealShopcartItem.getSaleType().equals(SaleType.UNWEIGHING)){
                       /* List<OrderProperty> propertys = setmealShopcartItem.getProperties();
                        for (OrderProperty property : propertys) {
                            if (property.getPropertyType().getPropertyKind() == PropertyKind.STANDARD) {
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {EventBus.getDefault().post(new OrderDishMes(true,null,setmealShopcartItem));
                                    }
                                });
                            }
                        }*/
//                    }*/
                    // List<SetmealShopcartItem> setmealItems = mShopcartItem.getSetmealItems();


                    break;
                case FAILED_MULTI:
                    ToastUtil.showLongToast(R.string.order_dish_setmeal_modify_failed_multi);
                    break;
                case FAILED_GREATER_THAN_MAX:
                    ToastUtil.showLongToast(R.string.order_dish_setmeal_count_exceed);
                    break;
                default:
                    ToastUtil.showLongToast(R.string.order_dish_setmeal_modify_failed);
                    break;
            }
        }
    }

    public void showPropertyView(boolean show) {
        llSetmealChild.setVisibility(show ? View.GONE : View.VISIBLE);
        llSetmealProperty.setVisibility(show ? View.VISIBLE : View.GONE);
        tvSetmealName.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

    @ItemLongClick(R.id.gv_setmeal_child_list)
    public void myGridItemLongClicked(final DishSetmealVo dishSetmealVo) {
        if (dishSetmealVo.isContainProperties()) {
            // 弹出估清界面
        } else {
            int title = dishSetmealVo.isClear() ? R.string.checkUClearDishStatus : R.string.checkClearDishStatus;
            new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .negativeText(R.string.common_cancel)
                    .positiveText(R.string.common_submit)
                    .positiveLinstner(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestClearStatus(dishSetmealVo, dishSetmealVo.isClear() ? ClearStatus.SALE
                                    : ClearStatus.CLEAR);
                        }
                    })
                    .build()
                    .show(getActivity().getSupportFragmentManager(), "clearDishStatus");
        }
    }

    /**
     * @Title: requestClearStatus
     * @Description: 请求更改估清状态
     * @Param @param dishShop TODO
     * @Return void 返回类型
     */
    private void requestClearStatus(final DishSetmealVo dishSetmealVo, final ClearStatus newValue) {
        List<String> dishUuids = new ArrayList<String>();
        dishUuids.add(dishSetmealVo.getDishShop().getUuid());

        /*
         * 估清请求结果
         */
        ResponseListener<Boolean> listener = new ResponseListener<Boolean>() {

            @Override
            public void onResponse(ResponseObject<Boolean> response) {
                if (ResponseObject.isOk(response) && response.getContent()) {
                    dishSetmealVo.getDishShop().setClearStatus(newValue);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (newValue == ClearStatus.SALE) {
                        ToastUtil.showLongToast(R.string.nClearstatus);
                        mDishSetmealManager.removeRequisiteClearListItem(dishSetmealVo);
                    } else {
                        ToastUtil.showLongToast(R.string.yClearstatus);
                        mDishSetmealManager.addRequisiteClearListItem(dishSetmealVo);
                    }
                } else if (1100 == response.getStatusCode()) {
                    if (newValue == ClearStatus.SALE) {
                        ToastUtil.showLongToast(R.string.nClearstatusNeedSync);
                    } else {
                        ToastUtil.showLongToast(R.string.yClearstatusNeedSync);
                    }
                } else {
                    if (newValue == ClearStatus.SALE) {
                        ToastUtil.showLongToast(R.string.nClearstatusFailure);
                    } else {
                        ToastUtil.showLongToast(R.string.yClearstatusFailure);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                if (newValue == ClearStatus.SALE) {
                    ToastUtil.showLongToast(BaseApplication.sInstance.getString(R.string.nClearstatusFailure) + error.getMessage());
                } else {
                    ToastUtil.showLongToast(BaseApplication.sInstance.getString(R.string.yClearstatusFailure) + error.getMessage());
                }
            }

        };

        DishDal dishDal = OperatesFactory.create(DishDal.class);
        dishDal.clearStatus(newValue, dishUuids, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    /**
     * @Title: updateShopcartItem
     * @Param @param selectedList
     * @Return void 返回类型
     */
    private void addSelectedDish(List<DishSetmealVo> selectedList) {
        // 新生成的购物车子菜列表
        List<SetmealShopcartItem> newSetmealShopcartItems = new ArrayList<SetmealShopcartItem>();
        // 购物车中已存在的子菜列表
        for (int i = 0; i < selectedList.size(); i++) {
            DishSetmealVo dishSetmealVo = selectedList.get(i);

            SetmealShopcartItem setmealShopcartItem = CreateItemTool.createSetmealShopcartItem(mShopcartItem, dishSetmealVo);
            BigDecimal leastCellNum = MathDecimal.trimZero(dishSetmealVo.getSetmeal().getLeastCellNum());
            setmealShopcartItem.changeQty(leastCellNum);
//			setmealShopcartItem.setSingleQty(leastCellNum);
//			setmealShopcartItem.setTotalQty(leastCellNum.multiply(mShopcartItem.getTotalQty()));

            newSetmealShopcartItems.add(setmealShopcartItem);
            mDishSetmealManager.addDefaultSelected(setmealShopcartItem);
        }

        mShopcartItem.setSetmealItems(newSetmealShopcartItems);
        mShoppingCart.addDishToShoppingCart(mShopcartItem, true);
    }

    private void refreshSelectedInfo(DishSetmealGroupVo dishSetmealGroupVo) {
        DishSetmealGroup dishSetmealGroup = dishSetmealGroupVo.getSetmealGroup();
        BigDecimal selectedQty = dishSetmealGroupVo.getSelectedQty();

        if (dishSetmealGroup != null && selectedQty != null) {
            String rangeText =
                    MathDecimal.toTrimZeroString(dishSetmealGroup.getOrderMin()) + "-"
                            + MathDecimal.toTrimZeroString(dishSetmealGroup.getOrderMax());
            if (dishSetmealGroup.getOrderMax().compareTo(dishSetmealGroup.getOrderMin()) == 0) {
                rangeText = dishSetmealGroup.getOrderMax().toString();
            }
            String text =
                    getActivity().getString(R.string.order_dish_setmeal_select_desc,
                            dishSetmealGroup.getName(),
                            MathDecimal.toTrimZeroString(selectedQty),
                            rangeText);
            tvSelectInfo.setText(Html.fromHtml(text));
            tvSelectInfo.setVisibility(View.VISIBLE);
        } else {
            tvSelectInfo.setVisibility(View.INVISIBLE);
        }
    }

    private void refreshClosePageButton() {
        if (mDishSetmealManager.isValid()) {
            btnFinish.setVisibility(View.VISIBLE);
            btnClose.setVisibility(View.GONE);
        } else {
            btnFinish.setVisibility(View.GONE);
            btnClose.setVisibility(View.VISIBLE);
        }
    }

    private void refreshSetmealGroup(DishSetmealGroupVo groupVo) {
        int childCount = llSetmealGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            SetmealButton setmealButton = (SetmealButton) llSetmealGroup.getChildAt(i);
            DishSetmealGroupVo dishSetmealGroupVo = (DishSetmealGroupVo) setmealButton.getTag();
            if (dishSetmealGroupVo.getSetmealGroup().getId() == groupVo.getSetmealGroup().getId()) {
                // 刷新菜品分组
                updateDishSetmealGroup(setmealButton, groupVo);
                setmealButton.setTag(groupVo);
                break;
            }
        }
    }

    @Override
    public void onSelectedQtyChanged(BigDecimal selectedQty) {
        mShopcartItem.changeQty(selectedQty);
//		mShopcartItem.setSingleQty(selectedQty);
//		mShopcartItem.setTotalQty(selectedQty);
//		List<SetmealShopcartItem> setmealShopcartItems = mShopcartItem.getSetmealItems();
//		for (SetmealShopcartItem setmealShopcartItem : setmealShopcartItems) {
//			BigDecimal setmealSelectedQty = setmealShopcartItem.getSingleQty();
//			setmealShopcartItem.setTotalQty(setmealSelectedQty.multiply(selectedQty));
//			
//			// 子菜加料份数修改
//			Collection<ExtraShopcartItem> listExtra = setmealShopcartItem.getExtraItems();
//			if (listExtra != null) {
//				for (ExtraShopcartItem extra : listExtra) {
//					BigDecimal extraQTY = extra.getSingleQty();
//					BigDecimal setExtraQTY = extraQTY.multiply(setmealShopcartItem.getTotalQty());
//					extra.setTotalQty(setExtraQTY);
//				}
//			}
//		}
//		mShopcartItem.setSetmealItems(setmealShopcartItems);
        mShoppingCart.addDishToShoppingCart(mShopcartItem, true);
    }

    @Override
    public void onMemoChanged(String memo) {
        mShopcartItem.setMemo(memo);
        mShoppingCart.addDishToShoppingCart(mShopcartItem, true);
    }

    private DataChangeListener dataChangeListener = new DataChangeListener() {

        @Override
        public void dataChange(ShopcartItem shopcartItem, boolean isContainProperties) {
            // TODO Auto-generated method stub
            mShoppingCart.addDishToShoppingCart(mShopcartItem, true);
        }
    };

    private void setDishSetmealInventoryNum(List<DishSetmealVo> data) {
        if (mInventoryCacheUtil.getSaleSwitch()) {//实时库存开启
            if (data != null && !data.isEmpty()) {
                for (DishSetmealVo dishSetmealVo : data) {
                    if (dishSetmealVo != null && dishSetmealVo.getDishShop() != null) {
                        InventoryInfo inventoryInfo = mInventoryCacheUtil.getInventoryNumByDishUuid(dishSetmealVo.getSkuUuid());
                        if (inventoryInfo != null) {
                            dishSetmealVo.setInventoryNum(inventoryInfo.getInventoryQty());
                        }
                    }
                }
            }
        }
    }

    private InventoryCacheUtil.InventoryDataChangeListener mInventoryListener = new InventoryCacheUtil.InventoryDataChangeListener() {
        @Override
        public void dataChange(List<InventoryInfo> data) {
            if (mAdapter != null) {
                List<DishSetmealVo> dishVos = mAdapter.getDataList();
                if (Utils.isNotEmpty(dishVos)) {
                    int size = dishVos.size();
                    for (int i = 0; i < size; i++) {
                        DishVo dishVo = dishVos.get(i);
                        if (dishVo != null && mInventoryCacheUtil.getAllInventoryData().containsKey(dishVo.getDishShop().getUuid())) {
                            BigDecimal num = mInventoryCacheUtil.getInventoryNumByDishUuid(dishVo.getDishShop().getUuid()).getInventoryQty();
                            dishVo.setInventoryNum(num);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    };
}
