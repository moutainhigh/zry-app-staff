package com.zhongmei.bty.dinner.cash;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem.DishCheckStatus;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.bean.ShoppingCartVo;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.event.ActionCloseOrderDishActivity;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListener;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListerTag;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.data.operates.GroupOperates;
import com.zhongmei.bty.dinner.action.ActionDinnerBatchDiscount;
import com.zhongmei.bty.dinner.action.ActionDinnerBatchFree;
import com.zhongmei.bty.dinner.action.ActionDinnerFinished;
import com.zhongmei.bty.dinner.action.ActionRefreshDinnerCustomer;
import com.zhongmei.bty.dinner.action.ActionSaveData;
import com.zhongmei.bty.dinner.action.ActionSeparateDeleteCoupon;
import com.zhongmei.bty.dinner.action.ActionSeparateDeleteIntegral;
import com.zhongmei.bty.dinner.shopcart.adapter.BuffetBanlanceAdapter;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerBanlanceAdapter;
import com.zhongmei.bty.dinner.shopcart.adapter.SuperShopCartAdapter;
import com.zhongmei.bty.mobilepay.event.SeparateEvent;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.snack.event.EventSelectDish;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.base.MobclickAgentFragment;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.zhongmei.yunfu.db.enums.BusinessType.DINNER;


@EFragment(R.layout.fragment_dinner_paymain_left)
public class PayMainLeftFragment extends MobclickAgentFragment {

    private static final String TAG = PayMainLeftFragment.class.getSimpleName();
        private static final int DEFAULT_MODE = 1;
        private static final int SPLIT_MODE = 2;

        private int mCurrentMode = DEFAULT_MODE;

        private final int FLAG_REMOVE_ALLDISCOUNT = 3;

    private final int FLAG_REMOVE_COUPON = 1;
    private final int FLAG_REMOVE_INTEGRAL = 2;
    private final int FLAG_REMOVE_GIFT_COUPON = 4;
    private final int FLAG_REMOVE_WEIXINCODE_COUPON = 5;
    @ViewById(R.id.dinner_balance_back)
    ImageButton btn_back;

    @ViewById(R.id.tv_table_name)
    TextView tvTableName;

        @ViewById(R.id.dinner_balance_price)
    TextView txt_price;

    @ViewById(R.id.buffet_orgin_price)
    TextView tvOrignPrice;


        @ViewById(R.id.dinner_balance_ticket_list)
    SwipeMenuListView mSwipeListView;

    @ViewById(R.id.tv_select_all)
    TextView tvSelectAll;

        Button btn_clean;

    @ViewById(R.id.dinner_balance_main)
    LinearLayout mainLayout;

        @ViewById(R.id.marketing_campaign_operate_ll)
    LinearLayout marketingCampaignOperateLL;

        @ViewById(R.id.dinner_invert_selection_btn)
    Button btn_invert;

        @ViewById(R.id.cancel_choose_dish_btn)
    Button cancelChooseDishBtn;

        @ViewById(R.id.cancel_choose_dish_btn)
    Button okChooseDishBtn;

    @ViewById(R.id.have_no_dish_layout)
    RelativeLayout haveNoDishLayout;

    @ViewById(R.id.haveNoDishImage)
    View haveNoDishImage;

    @ViewById(R.id.goods_total_number)
    TextView allDishCountTV;
    @ViewById(R.id.tv_pay_label)
    TextView tvPayLabel;

    TextView tv_split_text;

    View bottomView;

    private Context mContext;
        private DinnerShoppingCart mShoppingCart;
        private SeparateShoppingCart mSepShoppingCart;
    private List<IShopcartItem> mListOrderDishshopVo = null;

    private ArrayList<IShopcartItemBase> listDishData = new ArrayList<IShopcartItemBase>();

        private TradeVo mTradeVo = null;

    private DinnerBanlanceAdapter selectedDishAdapter;

    private String totalPrice = "";

        private boolean isAllSelect = false;

        private boolean isBatchDiscountMode = false;

        private boolean isRemove = false;


    private TradeOperates mTradeOperates;

    private MarketRuleVo marketRuleVo;
    private View tv_split, tv_print;
    private boolean isDoDelete;


        private boolean isSpliting = false;
        private boolean isBarCodeSaving = false;

    private DinnerCashManager dinnerCashManager;
        private boolean isInit = true;
    private int localPrepayPrintCount = 0;        private boolean isPaying = false;

    private boolean enableSplitTrade = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        if (mTradeOperates == null)
            mTradeOperates = OperatesFactory.create(TradeOperates.class);
        mShoppingCart = DinnerShopManager.getInstance().getShoppingCart();
        mSepShoppingCart = SeparateShoppingCart.getInstance();
        dinnerCashManager = new DinnerCashManager();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void copyExtraCharge() {
        new AsyncTask<Void, Void, List>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                tv_print.setEnabled(false);
            }

            @Override
            protected List doInBackground(Void... params) {
                return dinnerCashManager.copyExtraToDinner();
            }

            @Override
            protected void onPostExecute(List tempList) {
                super.onPostExecute(tempList);
                if (tempList != null) {
                    DinnerShoppingCart.getInstance().addExtraCharge(tempList, true, true);
                } else {
                    updateData(mListOrderDishshopVo, mTradeVo);
                }
                tv_print.setEnabled(true);
                isInit = false;
            }
        }.execute();

    }

    @AfterViews
    void init() {
        initBottom();
        registerOldListener();
        this.registerEventBus();
                                onMInit();
    }

        protected void onMInit() {
        initAdapter();
        setTableName();
        calPrice(mListOrderDishshopVo, mTradeVo);
        isPaying = DinnerShopManager.getInstance().getShoppingCart().isPaying();
        if (!DinnerShopManager.getInstance().isSepartShopCart() && !isPaying) {
            copyExtraCharge();
        } else {
            updateData(mListOrderDishshopVo, mTradeVo);
            isInit = false;
        }
        updateEnableSplit(this.enableSplitTrade);
    }

        public void setEnableSplitTrade(boolean enableSplitTrade) {
        this.enableSplitTrade = enableSplitTrade;

    }

    private void updateEnableSplit(boolean enable) {
        if (enable) {
            tv_split.setVisibility(View.VISIBLE);
        } else {
            tv_split.setVisibility(View.GONE);
        }
    }


    private void setTableName() {
        DinnertableTradeInfo info = mShoppingCart.getDinnertableTradeInfo();
        if (info != null && info.getTradeVo() != null && Utils.isNotEmpty(info.getTradeVo().getTradeTableList())) {
            BigDecimal deskCount = mShoppingCart.getOrder().getDeskCount();
            if (deskCount.compareTo(BigDecimal.ONE) > 0) {
                String deskCountString = String.format(getString(R.string.group_order_item_desknum), deskCount.toString());
                tvTableName.setText(deskCountString);
            } else {
                String tableName = info.getTradeVo().getTradeTableList().get(0).getTableName();
                if (!TextUtils.isEmpty(tableName)) {
                    tvTableName.setText(tableName);
                } else {
                    tvTableName.setVisibility(View.GONE);
                }
            }
        }
    }

    private void initShoppingcartData() {
        mListOrderDishshopVo = mShoppingCart.mergeShopcartItem(mShoppingCart.getShoppingCartVo());
        mTradeVo = mShoppingCart.getOrder();
    }

        private void updateAllDishCount() {
        if (selectedDishAdapter == null) {
            return;
        }
        selectedDishAdapter.updateCountView(getActivity(), allDishCountTV);
    }

        private void gotoListViewBottom() {
        if (isInit) {
            return;
        }
        if (selectedDishAdapter != null)
            mSwipeListView.setSelection(selectedDishAdapter.getCount() - 1);
    }


    private void goDefaultDisplayMode(boolean isInit) {
        mCurrentMode = DEFAULT_MODE;
        isSpliting = false;
        isBatchDiscountMode = false;
        DinnerShopManager.getInstance().setSepartShopCart(false);
        if (tvSelectAll.isShown()) {
            tvSelectAll.setVisibility(View.GONE);
        }
        if (!bottomView.isShown())
            bottomView.setVisibility(View.VISIBLE);
        tv_split_text.setText(R.string.dinner_order_center_tables_split_name);
        btn_invert.setVisibility(View.GONE);

        btn_clean.setVisibility(View.GONE);
        selectedDishAdapter.setDiscountModleNoNotify(isBatchDiscountMode);
        selectedDishAdapter.setShowRightAnchor(false);
        selectedDishAdapter.setIsDiscountAll(false);
        selectedDishAdapter.isShowMemeberDiscount(true);
        selectedDishAdapter.setCanRemoveMarketActivity(true);
        if (marketingCampaignOperateLL.getVisibility() == View.VISIBLE) {
            showMarketingCampaignDishCheckMode(false, null);
        }
        if (!isInit) {
            selectedDishAdapter.notifyDataSetChanged();
        }
        setSelectBtn(false);
    }


    private void goSplitMode() {
        bindMenuListener(true);
        DinnerShopManager.getInstance().setSepartShopCart(true);
        bottomView.setVisibility(View.GONE);
        tvPayLabel.setVisibility(View.VISIBLE);
        btn_invert.setVisibility(View.VISIBLE);
        tvSelectAll.setVisibility(View.GONE);
        showTotalPage(true);
        selectedDishAdapter.setShowRightAnchor(true);
        selectedDishAdapter.isShowMemeberDiscount(true);
        selectedDishAdapter.setDiscountModleNoNotify(false);
        registerListener();
        updateAllDishCount();
                if (mCurrentMode != SPLIT_MODE) {
            dinnerCashManager.copyDinnerToSepart();
        }
        updateData(mSepShoppingCart.getShoppingCartItems(), mSepShoppingCart.getOrder(), true);
        isSpliting = true;
        mCurrentMode = SPLIT_MODE;
    }



    private void goDefaultDiscountMode() {
        if (tvSelectAll.isShown()) {
            tvSelectAll.setVisibility(View.GONE);
        }
        btn_clean.setVisibility(View.GONE);
        isBatchDiscountMode = false;
        selectedDishAdapter.setIsDiscountAll(false);
        if (mCurrentMode == DEFAULT_MODE) {
            selectedDishAdapter.setDishCheckMode(false);
        }
        selectedDishAdapter.setDiscountModle(isBatchDiscountMode);
        setSelectBtn(false);
    }


    private void goBatchDiscountMode() {
        tvSelectAll.setVisibility(View.VISIBLE);
        btn_clean.setVisibility(View.VISIBLE);
        isBatchDiscountMode = true;
        selectedDishAdapter.setIsDiscountAll(false);
        selectedDishAdapter.setDiscountModle(isBatchDiscountMode);
    }


    private void goAllDiscountMode() {
        tvSelectAll.setVisibility(View.GONE);
        btn_clean.setVisibility(View.VISIBLE);
        setSelectBtn(false);
        isBatchDiscountMode = false;
        selectedDishAdapter.setIsDiscountAll(true);
        selectedDishAdapter.setDiscountModle(isBatchDiscountMode);
    }

    private void initBottom() {
        ViewStub viewStub = (ViewStub) getActivity().findViewById(R.id.dinner_balance_topay);
        if (viewStub == null) {
            return;
        }
        bottomView = viewStub.inflate();
        tv_split = bottomView.findViewById(R.id.dinner_balance_tosplit_layout);
        tv_split_text = (TextView) bottomView.findViewById(R.id.dinner_balance_bottom_txt);
        btn_clean = (Button) bottomView.findViewById(R.id.btn_clean);
        tv_print = bottomView.findViewById(R.id.rl_pay_print);
        tv_print.setOnClickListener(onClickListener);

        tv_split.setOnClickListener(onClickListener);
        btn_clean.setOnClickListener(onClickListener);
        btn_invert.setOnClickListener(onClickListener);
        btn_back.setOnClickListener(onClickListener);
        setBottomEnabled();
    }

    private void setBottomEnabled() {
        if (tv_split == null) {
            return;
        }
                if (mTradeVo != null && (mTradeVo.isUnionMainTrade() || mTradeVo.getTradeEarnestMoney() > 0)) {
            tv_split.setVisibility(View.GONE);
            return;
        }

                if (isPaying || mTradeVo != null && mTradeVo.getTrade().getBusinessType() == BusinessType.BUFFET && mTradeVo.getMealShellVo() != null) {
            tv_print.setBackgroundResource(R.drawable.btn_blue_selector);
            tv_split.setBackgroundResource(R.drawable.btn_gray_disabled_shape);
            tv_split.setEnabled(false);
            tv_print.setEnabled(true);
            return;
        }

        if (!DinnerShopManager.getInstance().isHasItems(mTradeVo, mListOrderDishshopVo) && !DinnerShopManager.getInstance().isSepartShopCart()) {
            tv_print.setBackgroundResource(R.drawable.btn_gray_disabled_shape);
            tv_split.setBackgroundResource(R.drawable.btn_gray_disabled_shape);
            tv_split.setEnabled(false);
            tv_print.setEnabled(false);
        } else {
            tv_print.setBackgroundResource(R.drawable.btn_blue_selector);
            tv_split.setBackgroundResource(R.drawable.btn_green_selector);
            tv_split.setEnabled(true);
            tv_print.setEnabled(true);
        }
    }

    private void initAdapter() {
        initShoppingcartData();
        if (mTradeVo != null && mTradeVo.getTrade() != null) {
            switch (mTradeVo.getTrade().getBusinessType()) {
                case BUFFET:
                                        selectedDishAdapter = new BuffetBanlanceAdapter(mContext);
                    selectedDishAdapter.updateOutTimeFeeItem(mTradeVo);
                    selectedDishAdapter.updateMinconsum(mTradeVo);
                    break;
                default:
                    selectedDishAdapter = new DinnerBanlanceAdapter(mContext);
                    if (mTradeVo.getTrade().getBusinessType() == DINNER)
                        selectedDishAdapter.updateMinconsum(mTradeVo);
                    break;
            }
            mSwipeListView.setAdapter(selectedDishAdapter);
            mSwipeListView.setMenuCreator(mSwipeMenuCreator);
            bindItemListener(true);
            bindMenuListener(false);
            goDefaultDisplayMode(true);
        }
    }



    private void bindItemListener(boolean isEnabled) {
        if (!isEnabled) {
            return;
        }
        mSwipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isBatchDiscountMode || selectedDishAdapter.isDishCheckMode()) {
                    DishDataItem dishDataItem = selectedDishAdapter.getItem(position);
                    if (dishDataItem == null || dishDataItem.getBase() == null) {
                        return;
                    }
                                        if (dishDataItem.getBase() instanceof ReadonlySetmealShopcartItem) {
                        return;
                    }
                                        if (TextUtils.isEmpty(dishDataItem.getBase().getBatchNo())
                            && dishDataItem.getBase().getStatusFlag() == StatusFlag.INVALID
                            && dishDataItem.getBase().getInvalidType() != InvalidType.SPLIT) {
                        return;
                    }

                    if (dishDataItem.getType() != ItemType.SINGLE && dishDataItem.getType() != ItemType.COMBO) {
                        return;
                    }
                    if (isBatchDiscountMode) {
                                                selectedDishAdapter.doEditModeItemClick(dishDataItem, position);
                    } else if (selectedDishAdapter.isDishCheckMode()) {
                        if (dishDataItem.getCheckStatus() == DishCheckStatus.CHECKED) {
                            dishDataItem.setCheckStatus(DishCheckStatus.NOT_CHECK);
                            selectedDishAdapter.notifyDataSetChanged();
                        } else if (dishDataItem.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
                            dishDataItem.setCheckStatus(DishCheckStatus.CHECKED);
                            selectedDishAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if (mCurrentMode == SPLIT_MODE && isSpliting)
                        deleteItem(position);
                }
            }
        });
    }


    private void bindMenuListener(boolean isEnabled) {
        mSwipeListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                deleteItem(position);
                return false;
            }
        });
    }


    private void deleteItem(int position) {
        final DishDataItem dishDataItem = selectedDishAdapter.getItem(position);
        if (dishDataItem == null) {
            return;
        }
        if (isPaying) {
            ToastUtil.showLongToast(R.string.dinner_privilege_used);
            return;
        }
        if (dishDataItem.getType() != null) {
            isDoDelete = true;
            switch (dishDataItem.getType()) {
                case EXCISE_TAX:
                    ToastUtil.showLongToast(R.string.tax_cannot_delete);
                    break;
                                case COUPONS:
                    if (dishDataItem.getCouponPrivilegeVo().isUsed()) {
                        ToastUtil.showLongToast(R.string.dinner_privilege_used);
                        return;
                    }
                    DinnerShopManager.getInstance().getShoppingCart().removeCouponPrivilege(dishDataItem.getCouponPrivilegeVo(), true);
                    sendCouponAction(dishDataItem.getCouponPrivilegeVo());
                    break;
                                case INTERGRAL:
                    if (dishDataItem.getIntegralCashPrivilegeVo().isUsed()) {
                        ToastUtil.showLongToast(R.string.dinner_privilege_used);
                        return;
                    }
                    DinnerShopManager.getInstance().getShoppingCart().removeIntegralCash();
                    sendIntegralAction();
                    break;
                                case WECHAT_CARD_COUPONS:
                    if (dishDataItem.getWeiXinCouponsVo() != null
                            && dishDataItem.getWeiXinCouponsVo().getmTradePrivilege() != null) {
                        if (dishDataItem.getWeiXinCouponsVo().isUsed()) {
                            ToastUtil.showLongToast(R.string.dinner_privilege_used);
                            return;
                        }
                        DinnerShopManager.getInstance().getShoppingCart()
                                .removeWeiXinCouponsPrivilege(dishDataItem.getWeiXinCouponsVo().getmTradePrivilege());
                    }
                    break;
                case COMBO_DISCOUNT:
                case SINGLE_DISCOUNT:
                    IShopcartItem shopcartItem = dishDataItem.getItem();
                    if (shopcartItem != null && shopcartItem.getPrivilege() != null) {
                        TradePrivilege tradePrivilege = shopcartItem.getPrivilege();
                        DinnerShopManager.getInstance().getShoppingCart().removeDishPrivilege(shopcartItem);
                        if (tradePrivilege.getPrivilegeType() != PrivilegeType.AUTO_DISCOUNT
                                && tradePrivilege.getPrivilegeType() != PrivilegeType.MEMBER_PRICE
                                && tradePrivilege.getPrivilegeType() != PrivilegeType.MEMBER_REBATE) {
                                                        if (DinnerShopManager.getInstance().getLoginCustomer() != null) {
                                DinnerShopManager.getInstance().getShoppingCart().memberPrivilege(shopcartItem, true, true);
                            }
                        }
                    }
                    break;
                case CHILD_MEMO:
                case SINGLE_MEMO:
                case COMBO_MEMO:
                case ALL_MEMO:
                    break;
                                case ALL_DISCOUNT:
                    DinnerShopManager.getInstance().getShoppingCart().removeOrderPrivilege();
                    break;
                case CHARGE_PRIVILEGE:
                    DinnerShopManager.getInstance().getShoppingCart().removeChargePrivilege(true,true);
                    break;
                case ADDITIONAL:
                    ExtraCharge extraCharge = dishDataItem.getExtraCharge();
                    if (extraCharge != null) {
                        if (extraCharge.getCode().equals(ExtraManager.BUFFET_OOUTTIME_CODE)) {
                            ToastUtil.showShortToast(R.string.buffet_outtimefee_delete);
                        } else if (extraCharge.getCode().equals(ExtraManager.BUFFET_MIN_CONSUM)) {
                                                        DinnerShopManager.getInstance().getShoppingCart().removeMinconsumExtra();
                        } else if (extraCharge.getCode().equals(ExtraManager.SERVICE_CONSUM)) {
                            ToastUtil.showShortToast(R.string.server_consum_delete);
                        } else {
                            DinnerShopManager.getInstance().getShoppingCart().removeExtraCharge(extraCharge.getId());
                        }
                    }
                    break;
                case SERVICE:
                    ToastUtil.showShortToast(R.string.server_consum_delete);
                    break;
                case BANQUET_PRIVILIGE:                    DinnerShopManager.getInstance().getShoppingCart().removeBanquet();
                    break;
                case GIFT_COUPON:
                                        IShopcartItem item = dishDataItem.getItem();
                    if (item != null && item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().getTradePrivilege() != null) {
                        ShoppingCartVo shoppingCartVo = DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo();
                        DinnerShopManager.getInstance().getShoppingCart().removeGiftCouponePrivilege(item.getCouponPrivilegeVo().getTradePrivilege().getPromoId(), shoppingCartVo, true);
                                                if (DinnerShopManager.getInstance().getLoginCustomer() != null) {
                            DinnerShopManager.getInstance().getShoppingCart().memberPrivilege(item, true, true);
                        }
                        ActionSeparateDeleteCoupon coupon = new ActionSeparateDeleteCoupon();
                        coupon.id = item.getCouponPrivilegeVo().getTradePrivilege().getPromoId();
                        EventBus.getDefault().post(coupon);
                    }
                    break;
                case SINGLE:
                case COMBO:
                                                            DinnerShoppingCart.getInstance()
                            .resetSeparateDish(dishDataItem.getItem().getRelateTradeItemUuid());
                    mSepShoppingCart.removeShoppingCart(dishDataItem.getItem());
                    DinnerShopManager.getInstance().checkTradeIsNegative(mSepShoppingCart.getOrder(), dishDataItem.getItem(), getFragmentManager());
                    initShoppingcartData();
                    break;
                case BUFFET_EXTRA_DEPOSIT:
                    if (dishDataItem.isPaid()) {
                        ToastUtil.showShortToast(R.string.buffet_deposit_paid_not_delete);
                    } else {
                        DinnerShopManager.getInstance().getShoppingCart().removeDeposit();                    }

                    break;
            }
        } else {
                        DinnerShoppingCart.getInstance().resetSeparateDish(dishDataItem.getItem().getRelateTradeItemUuid());
            mSepShoppingCart.removeShoppingCart(dishDataItem.getItem());
            DinnerShopManager.getInstance().checkTradeIsNegative(mShoppingCart.getOrder(), dishDataItem.getItem(), getFragmentManager());
            initShoppingcartData();
        }

    }

    private void sendCouponAction(CouponPrivilegeVo couponPrivilegeVo) {
        ActionSeparateDeleteCoupon coupon = new ActionSeparateDeleteCoupon();
        if (couponPrivilegeVo != null) {
            coupon.id = couponPrivilegeVo.getTradePrivilege().getPromoId();
        }
        EventBus.getDefault().post(coupon);
    }

    private void sendIntegralAction() {
        ActionSeparateDeleteIntegral integralAction = new ActionSeparateDeleteIntegral();
        EventBus.getDefault().post(integralAction);
    }

    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dinner_balance_tosplit_layout:
                    if (selectedDishAdapter == null) {
                                                return;
                    }
                                        if (mTradeVo.getTrade().getBusinessType() == BusinessType.GROUP) {
                        ToastUtil.showLongToast(R.string.group_cannot_split);
                        return;
                    }
                    if (mShoppingCart.getOrder().getTrade() == null) {
                        ToastUtil.showLongToast(R.string.dinner_opentable_unfinished);
                        return;
                    }
                                        if (mTradeVo.getTrade().getBusinessType() == BusinessType.BUFFET) {
                        ToastUtil.showLongToast(R.string.buffet_cannot_split);
                        return;
                    }
                    if (mShoppingCart.isReturnCash()) {
                        ToastUtil.showLongToast(R.string.dinner_return_cannot_split);
                        return;
                    }
                    if (mShoppingCart.getOrder().getTrade().getId() == null) {
                        ToastUtil.showLongToast(R.string.dinner_opentable_unfinished);
                        return;
                    }
                    if (mCurrentMode == DEFAULT_MODE) {
                        MobclickAgentEvent.onEvent(UserActionCode.ZC030002);
                    }
                    VerifyHelper.verifyAlert(getActivity(),
                            DinnerApplication.PERMISSION_DINNER_SPLIT,
                            new VerifyHelper.Callback() {

                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                                                        ResponseListener<TradeResp> listener = new ResponseListener<TradeResp>() {

                                        @Override
                                        public void onResponse(ResponseObject<TradeResp> response) {
                                            new AsyncTask<Void, Void, TradeVo>() {

                                                @Override
                                                protected TradeVo doInBackground(Void... params) {
                                                    Log.e(TAG, "改单成功后开始查询定单");
                                                    TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                                                    TradeVo _tradeVo = null;
                                                    try {
                                                        _tradeVo = tradeDal.findTrade(mShoppingCart.getOrder().getTrade().getUuid(), false);
                                                    } catch (Exception e) {
                                                        Log.e(TAG, "", e);
                                                    }
                                                    return _tradeVo;
                                                }

                                                protected void onPostExecute(TradeVo vo) {
                                                    try {
                                                                                                                CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                                                        DinnerShoppingCart.getInstance().updateDataFromTradeVo(vo, true);
                                                        CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                                                                                                                goSplitMode();

                                                        noticeRefreshCustomerUI(getActivity(), false);
                                                    } catch (Exception e) {
                                                        Log.e(TAG, "", e);
                                                    }
                                                }
                                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            ToastUtil.showLongToast(error.getMessage());
                                        }
                                    };
                                                                        if (!DinnerShopManager.getInstance().isSepartShopCart()) {
                                        mTradeOperates.modifyDinner(mShoppingCart.createOrder(), LoadingResponseListener.ensure(listener, PayMainLeftFragment.this.getChildFragmentManager()));
                                    } else {
                                                                                goSplitMode();

                                        noticeRefreshCustomerUI(getActivity(), false);
                                    }
                                                                    }
                            });
                    break;
                case R.id.rl_pay_print:
                    if (selectedDishAdapter == null) {
                        return;
                    }
                    if (!ClickManager.getInstance().isClicked()) {
                        MobclickAgentEvent.onEvent(UserActionCode.ZC030003);
                        UserActionEvent.start(UserActionEvent.DINNER_PAY_ORDER_PRE);
                        final TradeVo tradeVo = DinnerShopManager.getInstance().getShoppingCart().getOrder();
                        int localPrepayPrintCount = 0;
                        if (tradeVo != null && tradeVo.getTrade() != null && !TextUtils.isEmpty(tradeVo.getTrade().getUuid())) {
                            String tradeUuid = tradeVo.getTrade().getUuid();
                            localPrepayPrintCount = DinnerShopManager.getInstance().getLocalPrepayPrintCount(tradeUuid);
                            DinnerShopManager.getInstance().localPrepayPrintCountPlus(tradeUuid);
                        }

                                                boolean allowMultiPrepayticket = SpHelper.getDefault().getBoolean(Constant.ALLOW_MULTI_PREPAY_TICKET, true);
                        if (!allowMultiPrepayticket) {
                            if (localPrepayPrintCount > 0) {
                                ToastUtil.showShortToast(R.string.not_allow_multi_prepay_ticket);
                                return;
                            }
                            if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getId() != null) {

                            }
                        }
                                                if (tradeVo != null && tradeVo.isUnionMainTrade()) {
                            TradeVo printVo = DinnerShopManager.getInstance().getShoppingCart().createOrder();

                                                        return;
                        }
                                                                        if (!DinnerShopManager.getInstance().isSepartShopCart()) {
                            if (tradeVo != null && tradeVo.getTrade() != null) {
                                                                if (tradeVo.getTrade().getId() == null) {
                                    ToastUtil.showShortToast(R.string.open_table_please);
                                    return;
                                }
                                ResponseListener<TradeResp> listener = new ResponseListener<TradeResp>() {

                                    @Override
                                    public void onResponse(ResponseObject<TradeResp> response) {
                                        if (ResponseObject.isOk(response)) {
                                            saveServerPrint(tradeVo);
                                        }
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        ToastUtil.showLongToast(error.getMessage());
                                    }
                                };
                                BusinessType businessType = tradeVo.getTrade().getBusinessType();
                                TradeVo newTradeVo = mShoppingCart.createOrder();
                                if (businessType == BusinessType.GROUP) {
                                    modifygroup(newTradeVo, true);
                                } else if (businessType == BusinessType.BUFFET) {
                                    modifyBuffet(newTradeVo, true);
                                } else {
                                    mTradeOperates.modifyDinner(newTradeVo, LoadingResponseListener.ensure(listener, PayMainLeftFragment.this.getChildFragmentManager()));
                                }
                            }
                        } else {                                                                                                                                         }
                                            }
                    break;
                case R.id.dinner_balance_back:
                    doBack();
                    break;
                case R.id.btn_clean:
                                        if (isPaying) {
                        return;
                    }
                    MobclickAgentEvent.onEvent(UserActionCode.ZC030012);
                    DinnerShopManager.getInstance().getShoppingCart().removeOrderPrivilege();
                    DinnerShopManager.getInstance().getShoppingCart().removeBanquet();
                    DinnerShopManager.getInstance().getShoppingCart().removeAllItemsPrivilege();
                    break;
                case R.id.dinner_invert_selection_btn:
                                        doClearSplit();
                    break;
            }
        }

    };

    private void saveServerPrint(final TradeVo tradeVo) {
        new AsyncTask<Void, Void, TradeVo>() {

            @Override
            protected TradeVo doInBackground(Void... params) {
                Log.e(TAG, "改单成功后开始查询定单");
                TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                TradeVo _tradeVo = null;
                try {
                    _tradeVo = tradeDal.findTrade(tradeVo.getTrade().getUuid(), false);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return _tradeVo;
            }

            protected void onPostExecute(TradeVo vo) {
                try {
                                        CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                    DinnerShoppingCart.getInstance().updateDataFromTradeVo(vo, true);
                    CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                    noticeRefreshCustomerUI(getActivity(), false);
                                                                            } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void noticeRefreshCustomerUI(FragmentActivity activity, boolean needRefreshCustomerData) {
        CustomerResp customerNew = DinnerShopManager.getInstance().getLoginCustomer();
        if (customerNew != null) {
            customerNew.needRefresh = needRefreshCustomerData;
        }

                if (activity != null) {

                EventBus.getDefault().post(new ActionDinnerPrilivige(ActionDinnerPrilivige.DinnerPriviligeType.PRIVILIGE_ITEMS));
                    } else {
            EventBus.getDefault().post(new ActionDinnerPrilivige(ActionDinnerPrilivige.DinnerPriviligeType.PRIVILIGE_ITEMS));
        }
    }


    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo) {
        updateData(dataList, tradeVo, true);
    }

    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isNeedNotify) {
        if (selectedDishAdapter == null) {
            return;
        }
        UserActionEvent.start(UserActionEvent.DINNER_PAY_SHOPCART_DISPLAY);
        mListOrderDishshopVo = dataList;

                if (Utils.isEmpty(dataList) && tradeVo.getMealShellVo() == null) {
            tradeVo.setTradeTaxs(null);
            tradeVo.setTradeInitConfigs(null);
            tradeVo.setExtraChargeMap(null);
            List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
            if (tradePrivileges != null) {
                Iterator<TradePrivilege> it = tradePrivileges.iterator();
                while (it.hasNext()) {
                    TradePrivilege tradePrivilege = it.next();
                    if (tradePrivilege.getPrivilegeType() == PrivilegeType.SERVICE || tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL) {
                        it.remove();
                    }
                }
            }
        }

        List<IShopcartItem> sourceList = DinnerShoppingCart.getInstance().filterDishList(dataList, false);
        if (tradeVo.getTrade().getBusinessType() == BusinessType.GROUP || tradeVo.getTrade().getBusinessType() == BusinessType.BUFFET) {
            selectedDishAdapter.updateGroupData(sourceList, tradeVo, true);
        } else {
            selectedDishAdapter.updateData(sourceList, tradeVo, true);
        }
        if (isBatchDiscountMode) {
            setSelectView(DinnerShopManager.getInstance().getCanDiscountData(mListOrderDishshopVo), DinnerShopManager.getInstance().getAllSelectData(selectedDishAdapter.getAllData()));
        }
        calPrice(dataList, tradeVo);


        if (selectedDishAdapter.getAllData() != null && selectedDishAdapter.getAllData().size() != 0) {
            haveNoDishLayout.setVisibility(View.GONE);

        } else {
            haveNoDishLayout.setVisibility(View.VISIBLE);

        }
        if (isNeedNotify) {
            selectedDishAdapter.notifyDataSetChanged();
        }
        setBottomEnabled();
                updateAllDishCount();
        UserActionEvent.end(UserActionEvent.DINNER_PAY_SHOPCART_DISPLAY);
    }


    private void calPrice(List<IShopcartItem> dataList, TradeVo tradeVo) {
        try {
            if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getTradeAmount() != null) {
                totalPrice = tradeVo.getTrade().getTradeAmount().toString();
                totalPrice = CashInfoManager.formatCashAmount(Double.valueOf(totalPrice));            } else {
                totalPrice = "0.00";
            }
        } catch (Exception e) {
            totalPrice = "0.00";
            Log.e(TAG, "", e);
        }
        txt_price.setText(ShopInfoCfg.formatCurrencySymbol(totalPrice));

        tvOrignPrice.setVisibility(View.GONE);
    }


    public void onEventMainThread(ActionDinnerBatchDiscount action) {
        boolean isMoveToBottom = false;
        selectedDishAdapter.setDiscountModle(action.isEditModle);
        if (action.getIsEditModle() && !action.isAllDiscount) {
            goBatchDiscountMode();
        } else if (!action.getIsEditModle() && action.isAllDiscount) {
            goAllDiscountMode();
            doClearSelected();
            isMoveToBottom = true;
        } else {
            goDefaultDiscountMode();
            doClearSelected();
        }
        selectedDishAdapter.notifyDataSetChanged();
        if (isMoveToBottom)
            gotoListViewBottom();
    }

        public void onEventMainThread(ActionDinnerBatchFree action) {


        boolean oldBatchCoercionModel = selectedDishAdapter.isBatchCoercionModel();
        selectedDishAdapter.setBatchCoercionModel(action.isBatchCoercionModel());
        if (selectedDishAdapter.isDiscountModle()) {
            if (action.isBatchCoercionModel()) {
                setSelectBtn(false);
                tvSelectAll.setVisibility(View.GONE);
            } else {
                tvSelectAll.setVisibility(View.VISIBLE);
                                if (oldBatchCoercionModel) {
                    setSelectBtn(false);
                    selectedDishAdapter.checkCancelAll();
                    selectedDishAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void onEventMainThread(ActionDinnerPrilivige action) {
        switch (action.getType()) {
            case CLOSETOTALPAGE:
                tvPayLabel.setVisibility(View.GONE);
                btn_invert.setVisibility(View.GONE);
                bottomView.setVisibility(View.VISIBLE);
                tv_split_text.setText(R.string.dinner_modify_split_label);
                selectedDishAdapter.setShowRightAnchor(false);
                isSpliting = false;
                break;
        }
    }

        public void onEventMainThread(ActionSaveData action) {
        if (mTradeOperates == null)
            mTradeOperates = OperatesFactory.create(TradeOperates.class);
                TradeVo tradeVo = DinnerShoppingCart.getInstance().createOrder();
        if (tradeVo.getTrade() != null && tradeVo.getTrade().getId() == null) {
            ToastUtil.showShortToast(R.string.open_table_please);
            return;
        }
        if (mTradeVo.getTrade().getBusinessType() != BusinessType.BUFFET && !DinnerShopManager.getInstance().isHasItems(mTradeVo, mListOrderDishshopVo)) {
            ToastUtil.showLongToast(R.string.no_goods_can_save);
            return;
        }
        if (tradeVo.getTrade() != null && tradeVo.getTrade().getTradeAmount().compareTo(BigDecimal.ZERO) == -1) {
            ToastUtil
                    .showLongToast(MainApplication.getInstance().getResources().getString(R.string.dinner_privilege_over));
            return;
        }
        if (!checkCouponAndIntegralCash()) {
            return;
        }
        DinnerCashManager.removeInValidAuthLog(mShoppingCart.getOrder());
        DinnerShoppingCart.getInstance().onlyMath();

        TradeVo target = null;
        if (!DinnerShopManager.getInstance().isSepartShopCart()) {
                        target = mShoppingCart.createOrder();
            if (target.getTrade().getBusinessType() == BusinessType.GROUP) {
                modifygroup(target, false);
            } else if (target.getTrade().getBusinessType() == BusinessType.BUFFET) {
                modifyBuffet(target, false);
            } else {
                mTradeOperates.modifyDinner(target,
                        LoadingResponseListener.ensure(DinnerShopManager.getInstance().getSaveResponseListener(getActivity()), getFragmentManager()));
            }
        } else {
                        target = mSepShoppingCart.createSeparateOrder();
            ShoppingCartVo shoppingCartVo = DinnerShoppingCart.getInstance().getShoppingCartVo();
            TradeVo sourceRef = mShoppingCart.createOrder();
            TradeVo sourceNew = sourceRef.clone();
            MathShoppingCartTool.mathTotalPrice(DinnerShoppingCart.getInstance().mergeShopcartItem(shoppingCartVo),
                    sourceNew);
                        mTradeOperates.tradeSplitDinner(sourceNew,
                    target,
                    LoadingResponseListener.ensure(DinnerShopManager.getInstance().getSaveResponseListener(getActivity()), getFragmentManager()));
        }
    }



    private void modifyBuffet(final TradeVo target, final boolean isPrePrint) {
        final TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        ResponseListener listener = new ResponseListener<Void>() {
            @Override
            public void onResponse(ResponseObject<Void> response) {
                if (isPrePrint) {
                    saveServerPrint(target);
                } else {
                    EventBus.getDefault().post(new ActionCloseOrderDishActivity());
                    getActivity().finish();
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };
        tradeOperates.modifyBuffet(target, LoadingResponseListener.ensure(listener, getActivity().getSupportFragmentManager()));
    }


    private void modifygroup(final TradeVo target, final boolean isPrePrint) {
        final CalmLoadingDialogFragment dialogFragment = CalmLoadingDialogFragment.showByAllowingStateLoss(getActivity().getSupportFragmentManager());
        GroupOperates groupOperates = OperatesFactory.create(GroupOperates.class);
        groupOperates.modifyGroup(target, new CalmResponseListener<ResponseObject<TradeResp>>() {
            @Override
            public void onError(NetError error) {
                ToastUtil.showLongToast(error.getVolleyError().getMessage());
                CalmLoadingDialogFragment.hide(dialogFragment);
            }

            @Override
            public void onSuccess(ResponseObject<TradeResp> data) {
                CalmLoadingDialogFragment.hide(dialogFragment);
                if (ResponseObject.isOk(data)) {
                    if (isPrePrint) {
                        saveServerPrint(target);
                    } else {
                        getActivity().finish();
                    }
                } else {
                    ToastUtil.showLongToast(data.getMessage());
                }
            }
        }, true);
    }


    private void doClearSelected() {
        if (selectedDishAdapter != null) {
            for (DishDataItem dish : selectedDishAdapter.getAllData()) {
                if (dish.getBase() != null) {
                    dish.getBase().setSelected(false);
                }
            }
        }
        DinnerShopManager.getInstance().getShoppingCart().removeAllDinnerListItems();
    }

    public void onEventMainThread(ActionDinnerFinished action) {
        doClearSelected();
        DinnerShopManager.getInstance().getShoppingCart().setDishPrivilege(null, null);
        selectedDishAdapter.notifyDataSetChanged();
    }

    public void doBack() {
        TradeVo tradeVo = DinnerShoppingCart.getInstance().createOrder();
        Trade trade = tradeVo.getTrade();
        if (trade.getBusinessType() == DINNER && trade.getTradePayStatus() == TradePayStatus.PAYING) {
            ToastUtil.showShortToast(R.string.dinner_paying);
            return;
        }

        DinnerShopManager.getInstance().doReset(true);
        DisplayServiceManager.doCancel(getActivity().getApplicationContext());
        getActivity().finish();
    }

    SwipeMenu swipeMenu;
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            swipeMenu = menu;
                                    switch (menu.getViewType()) {
                case SuperShopCartAdapter.SIGLE_PRIVILEGE_TYPE:
                case SuperShopCartAdapter.PRIVILEGE_TYPE:
                case SuperShopCartAdapter.BUFFET_EXTRA:
                    createMenu0(menu, 0);
                    break;
                default:
                    break;
            }
        }

        private void createMenu0(SwipeMenu menu, int type) {
            SwipeMenuItem item = new SwipeMenuItem(getActivity().getApplicationContext());
            item.setBackground(new ColorDrawable(getActivity().getResources().getColor(R.color.bg_red)));
            item.setWidth(DensityUtil.dip2px(getActivity(), 100));

            item.setIcon(getActivity().getResources().getDrawable(R.drawable.cashier_order_swipemenu_delete_icon));
            menu.addMenuItem(item);
        }
    };


    private void setSelectBtn(boolean isSelected) {
        if (!isSelected) {
            tvSelectAll.setText(getResources().getString(R.string.dinner_select_all));
            tvSelectAll.setTextColor(getResources().getColor(R.color.text_blue));
        } else {
            tvSelectAll.setText(getResources().getString(R.string.dinner_select_all_cancel));
            tvSelectAll.setTextColor(getResources().getColor(R.color.text_select_yellow));
        }
    }


    public void onEventMainThread(EventSelectDish select) {
                List<IShopcartItem> list = DinnerShopManager.getInstance().getCanDiscountData(mListOrderDishshopVo);
        List<IShopcartItemBase> selectedList = DinnerShopManager.getInstance().getAllSelectData(selectedDishAdapter.getAllData());
        setSelectView(list, selectedList);

        DinnerShopManager.getInstance().getShoppingCart().batchDishPrivilege(selectedList);

            }

    public void dealSplitPay(SeparateEvent event) {
        switch (event.getStatus()) {
            case SeparateEvent.EVENT_SEPARATE_SAVE:
                isBarCodeSaving = true;
                break;
            case SeparateEvent.EVENT_SEPARATE_PAYED:
                isBarCodeSaving = false;
                goDefaultDisplayMode(false);
                noticeRefreshCustomerUI(getActivity(), true);
                break;
            case SeparateEvent.EVENT_RESOURCE_PAYING:
            case SeparateEvent.EVENT_SEPARATE_PAYING:
                isPaying = true;
                setBottomEnabled();
                break;
        }
    }


    private void setSelectView(List<IShopcartItem> srcList, List<IShopcartItemBase> selectedList) {
        if (srcList != null) {
            if (selectedList != null) {
                if (srcList.size() != selectedList.size() || selectedList.size() == 0) {
                    isAllSelect = false;
                } else {
                    isAllSelect = true;
                }
            } else if (selectedList == null) {
                isAllSelect = false;
            }
            setSelectBtn(isAllSelect);
        }
    }


    @Click(R.id.tv_select_all)
    void doSelectAll() {
        if (!ClickManager.getInstance().isClicked()) {
            if (selectedDishAdapter == null || selectedDishAdapter.getAllData().size() == 0) {
                return;
            }
            if (DinnerShopManager.getInstance().getCanDiscountData(mListOrderDishshopVo).size() <= 0) {
                ToastUtil.showShortToast(R.string.no_discount_dish);
                return;
            }
            isAllSelect = !isAllSelect;
            setSelectBtn(isAllSelect);
            doSelectAll(isAllSelect);
        }
    }


    public void doSelectAll(boolean isSelect) {
        if (selectedDishAdapter == null) {
            return;
        }
        if (!isSelect) {
            for (DishDataItem dish : selectedDishAdapter.getAllData()) {
                if (dish.getType() == ItemType.SINGLE
                        || dish.getType() == ItemType.COMBO) {
                    if (dish.getBase() != null) {
                        dish.getBase().setSelected(false);
                    }
                }
            }
        } else {
            for (DishDataItem dish : selectedDishAdapter.getAllData()) {
                if (dish.getType() == ItemType.SINGLE
                        || dish.getType() == ItemType.COMBO) {
                                        if (dish.getBase() != null) {
                        if (selectedDishAdapter.isDiscountModle()
                                && DinnerCashManager.hasMarketActivity(selectedDishAdapter.getTradeItemPlanActivityMap(), dish.getBase()) || dish.getBase().getEnableWholePrivilege() == Bool.NO) {
                            continue;
                        }
                        BigDecimal currentPrice = dish.getBase().getPrice();
                        TradePrivilege tradePrivilege = DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo().getDishTradePrivilege();
                        if (tradePrivilege != null && tradePrivilege.getPrivilegeType() == PrivilegeType.REBATE) {
                            if (currentPrice.subtract(tradePrivilege.getPrivilegeValue()).floatValue() >= 0) {
                                dish.getBase().setSelected(true);
                            } else {
                                ToastUtil.showLongToast(getResources().getString(R.string.privilegeError));
                                setSelectBtn(false);
                                return;
                            }
                        } else {
                            dish.getBase().setSelected(true);
                        }
                    }
                }
            }
        }
        batchSetDiscount();
        selectedDishAdapter.notifyDataSetChanged();
    }


    public void batchSetDiscount() {
        listDishData.clear();
        for (DishDataItem dish : selectedDishAdapter.getAllData()) {
                        if ((dish.getBase() != null && dish.getBase().isSelected())
                    && (dish.getType() == ItemType.SINGLE
                    || dish.getType() == ItemType.COMBO)) {
                IShopcartItemBase mShopcartItemBase = dish.getBase();
                if (mShopcartItemBase != null) {
                    listDishData.add(mShopcartItemBase);
                }
            }
        }
        DinnerShopManager.getInstance().getShoppingCart().batchDishPrivilege(listDishData);
    }


    private boolean checkCouponAndIntegralCash() {
                boolean isCouponInvalid = DinnerShopManager.getInstance().isCouponInvalid();
        boolean isIntegralCashInvalid = DinnerShopManager.getInstance().isIntegralCashInvalid();
        boolean isGiftCoupon = DinnerShopManager.getInstance().isHasUnActiveGiftCoupon(DinnerShopManager.getInstance().getShoppingCart().getOrder());
        boolean isWeixinCode = DinnerShopManager.getInstance().isHasUnActiveWeixinCode();
        if (isCouponInvalid && isIntegralCashInvalid) {
            checkInvalidDialog(R.string.privilegeisInvalid,
                    R.string.removeDiscount,
                    0,
                    FLAG_REMOVE_ALLDISCOUNT);
            return false;
        } else {
            if (isCouponInvalid) {
                checkInvalidDialog(R.string.couponIsInvalid,
                        R.string.removeCoupon,
                        0,
                        FLAG_REMOVE_COUPON);
                return false;
            } else if (isIntegralCashInvalid) {

                checkInvalidDialog(R.string.intergral_ineffective,
                        R.string.removeIntergral,
                        0,
                        FLAG_REMOVE_INTEGRAL);
                return false;
            } else if (isGiftCoupon) {
                checkInvalidDialog(R.string.giftcoupon_ineffective,
                        R.string.removeGiftCoupon,
                        0,
                        FLAG_REMOVE_GIFT_COUPON);
                return false;
            } else if (isWeixinCode) {
                checkInvalidDialog(R.string.weiChatcode_ineffective,
                        R.string.removeWeChatCode,
                        0,
                        FLAG_REMOVE_WEIXINCODE_COUPON);
                return false;
            }
        }
        return true;
    }


    private Boolean checkInvalidDialog(int title, int positiveText, final int tag, final int removeType) {
        isRemove = false;
        CommonDialogFragmentBuilder cb = new CommonDialogFragmentBuilder(MainApplication.getInstance());
        cb.iconType(CommonDialogFragment.ICON_WARNING)
                .title(title)
                .negativeText(R.string.backsettle)
                .positiveText(positiveText)
                .positiveLinstner(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (removeType == FLAG_REMOVE_COUPON) {
                            DinnerShopManager.getInstance().getShoppingCart().removeAllInvalidCoupon(DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo(), true);

                        } else if (removeType == FLAG_REMOVE_INTEGRAL) {

                            DinnerShopManager.getInstance().getShoppingCart().removeIntegralCash();                            sendIntegralAction();

                        } else if (removeType == FLAG_REMOVE_ALLDISCOUNT) {
                            DinnerShopManager.getInstance().getShoppingCart().removeAllInvalidCoupon(DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo(), true);
                            DinnerShopManager.getInstance().getShoppingCart().removeIntegralCash();                            sendIntegralAction();                        } else if (removeType == FLAG_REMOVE_GIFT_COUPON) {
                            DinnerShopManager.getInstance().getShoppingCart().removeAllInValidGiftCoupon(true);
                        } else if (removeType == FLAG_REMOVE_WEIXINCODE_COUPON) {
                            DinnerShopManager.getInstance().getShoppingCart().removeAllInUnActiveWeixinCode(true);
                        }
                        isRemove = true;
                    }

                })
                .build()
                .show(getFragmentManager(), "checkCouponPrivilegeIsInvalid");

        return isRemove;
    }



    public void showMarketingCampaignDishCheckMode(boolean show, MarketRuleVo marketRuleVo) {
        if (show) {
            selectedDishAdapter.setDishCheckMode(true);
            selectedDishAdapter.setMarketRuleVo(marketRuleVo);
            selectedDishAdapter.setMarketActivityItemsCheckStatus(marketRuleVo, DinnerShopManager.getInstance().getShoppingCart().getOrder().getTradeItemPlanActivityList());
            DinnerShopManager.getInstance().getShoppingCart().doDishActivityIsCheck(selectedDishAdapter.getUnMarketActivityItems(), marketRuleVo);
            selectedDishAdapter.notifyDataSetChanged();

            this.marketRuleVo = marketRuleVo;
            bottomView.setVisibility(View.GONE);
            btn_clean.setVisibility(View.GONE);
            marketingCampaignOperateLL.setVisibility(View.VISIBLE);

        } else {
            selectedDishAdapter.setDishCheckMode(false);
            selectedDishAdapter.setMarketRuleVo(null);
            selectedDishAdapter.notifyDataSetChanged();

            bottomView.setVisibility(View.VISIBLE);
            marketingCampaignOperateLL.setVisibility(View.GONE);
            btn_clean.setVisibility(View.GONE);
        }

    }


    @Click({R.id.cancel_choose_dish_btn, R.id.ok_choose_dish_btn})
    void clickMarketingCampaignButtons(View view) {
        switch (view.getId()) {
            case R.id.cancel_choose_dish_btn:
                showMarketingCampaignDishCheckMode(false, null);
                cancelSelectInMarketActivityFragment();
                break;
            case R.id.ok_choose_dish_btn:
                if (selectedDishAdapter.getCheckedNumber() == 0) {
                    ToastUtil.showShortToast(R.string.dinner_marketing_campaign_addfail_notchoose);
                    return;
                }

                if (DinnerShopManager.getInstance().getShoppingCart().addMarketActivity(marketRuleVo,
                        selectedDishAdapter.getCheckedIShopcartItems())) {
                    showMarketingCampaignDishCheckMode(false, null);
                    cancelSelectInMarketActivityFragment();
                } else {
                    ToastUtil.showShortToast(R.string.dinner_marketing_campaign_addfail);
                }
                break;

            default:
                break;
        }
    }


    private void cancelSelectInMarketActivityFragment() {
        DinnerMarketActivityFragment marketFragment = (DinnerMarketActivityFragment) getFragmentManager()
                .findFragmentByTag(DinnerMarketActivityFragment.class.getSimpleName());
        if (marketFragment != null) {
            marketFragment.cancelSelected();
        }
    }

    private void updateMarketActivityFragment() {
        DinnerMarketActivityFragment marketFragment = (DinnerMarketActivityFragment) getFragmentManager()
                .findFragmentByTag(DinnerMarketActivityFragment.class.getSimpleName());
        if (marketFragment != null) {
            marketFragment.updateAdapterView();
        }
    }


    private void doClearSplit() {
        MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerSettleSplitInvertSelect);
        List<IShopcartItem> tempList = mShoppingCart.getAllValidShopcartItem(mShoppingCart.getShoppingCartDish());
        if ((mSepShoppingCart.getShoppingCartItems() == null || mSepShoppingCart.getShoppingCartItems().size() == 0)
                && (tempList == null || tempList.size() == 0)) {
            ToastUtil.showShortToast(R.string.payOrderIsEmpty);
            return;
        }
        VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_SPLIT,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        DinnerShopManager.getInstance().doReset(true);
                    }
                });
    }


    private void showTotalPage(boolean isShow) {
        if (isShow) {
            ActionDinnerPrilivige actionDinnerPrilivige = new ActionDinnerPrilivige(ActionDinnerPrilivige.DinnerPriviligeType.SHOWTAOTALPAGE);
            EventBus.getDefault().post(actionDinnerPrilivige);
        } else {
            ActionDinnerPrilivige actionDinnerPrilivige = new ActionDinnerPrilivige(ActionDinnerPrilivige.DinnerPriviligeType.CLOSETOTALPAGE);
            EventBus.getDefault().post(actionDinnerPrilivige);
        }
    }


    private void registerOldListener() {
        mShoppingCart.registerListener(ShoppingCartListerTag.DINNER_DISH_BALANCE_SHOW, new ShoppingCartListener() {
            @Override
            public void resetOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                if (!isBarCodeSaving) {
                    updateData(listOrderDishshopVo, mTradeVo);
                }
            }

            @Override
            public void batchPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                doClearSelected();
                updateData(listOrderDishshopVo, mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void removeCustomerPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                updateData(listOrderDishshopVo, mTradeVo);
            }

            @Override
            public void addExtraCharge(TradeVo mTradeVo, Map<Long, ExtraCharge> arrayExtraCharge) {
                updateData(mListOrderDishshopVo, mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void removeExtraCharge(TradeVo mTradeVo, Long extraChargeId) {
                super.removeExtraCharge(mTradeVo, extraChargeId);
                updateData(mListOrderDishshopVo, mTradeVo);
            }

            @Override
            public void removeDeposit(TradeVo mTradeVo) {
                super.removeDeposit(mTradeVo);
                updateData(mListOrderDishshopVo, mTradeVo);
            }

            @Override
            public void addMarketActivity(TradeVo mTradeVo) {
                super.addMarketActivity(mTradeVo);
                updateData(mListOrderDishshopVo, mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void removeMarketActivity(TradeVo mTradeVo) {
                super.removeMarketActivity(mTradeVo);
                updateData(mListOrderDishshopVo, mTradeVo);
                updateMarketActivityFragment();
            }

            @Override
            public void addWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                                updateData(mListOrderDishshopVo, mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void removeWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                                updateData(mListOrderDishshopVo, mTradeVo);
                gotoListViewBottom();
            }


            @Override
            public void doBanquet(TradeVo mTradeVo) {
                updateData(mListOrderDishshopVo, mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void removeBanquet(TradeVo mTradeVo) {
                updateData(mListOrderDishshopVo, mTradeVo);
            }

            @Override
            public void setIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                super.setIntegralCash(listOrderDishshopVo, mTradeVo);
                updateData(listOrderDishshopVo, mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void removeIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                updateData(listOrderDishshopVo, mTradeVo);
            }

            @Override
            public void setCouponPrivi1lege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                updateData(listOrderDishshopVo, mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void removeCouponPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                updateData(listOrderDishshopVo, mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void updateDish(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                updateData(listOrderDishshopVo, mTradeVo);
            }
        });
    }

    private void registerListener() {
        if (mSepShoppingCart == null) {
            return;
        }
        mSepShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.DINNER_BALANCE_SPLIT);
        mSepShoppingCart.registerListener(ShoppingCartListerTag.DINNER_BALANCE_SPLIT, new ShoppingCartListener() {
            @Override
            public void resetOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                super.resetOrder(listOrderDishshopVo, mTradeVo);
                updateData(listOrderDishshopVo, mTradeVo);
            }

            @Override
            public void batchPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                doClearSelected();
                updateData(listOrderDishshopVo, mTradeVo);
            }

            @Override
            public void removeCustomerPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                updateData(listOrderDishshopVo, mTradeVo);
            }

            @Override
            public void separateOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                List<IShopcartItem> oldShopcartItemList = mShoppingCart.getShoppingCartDish();
                if (!dealSplitToOld()) {
                                        if (listOrderDishshopVo != null)
                        Collections.reverse(listOrderDishshopVo);
                    updateData(listOrderDishshopVo, mTradeVo);
                }
            }

            @Override
            public void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem) {
                updateData(listOrderDishshopVo, mTradeVo);
                updateMarketActivityFragment();
                                if (!isBatchDiscountMode && !isDoDelete) {
                    gotoListViewBottom();
                }
                if (isDoDelete) {
                    isDoDelete = false;
                }
            }

            @Override
            public void removeExtraCharge(TradeVo mTradeVo, Long extraChargeId) {
                super.removeExtraCharge(mTradeVo, extraChargeId);
                updateData(mSepShoppingCart.getShoppingCartItems(), mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void addExtraCharge(TradeVo mTradeVo, Map<Long, ExtraCharge> arrayExtraCharge) {
                super.addExtraCharge(mTradeVo, arrayExtraCharge);
                updateData(mSepShoppingCart.getShoppingCartItems(), mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void addMarketActivity(TradeVo mTradeVo) {
                super.addMarketActivity(mTradeVo);
                updateData(mSepShoppingCart.getShoppingCartItems(), mTradeVo);
            }

            @Override
            public void removeMarketActivity(TradeVo mTradeVo) {
                super.removeMarketActivity(mTradeVo);
                updateData(mSepShoppingCart.getShoppingCartItems(), mTradeVo);
                updateMarketActivityFragment();
            }

            @Override
            public void addWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                                updateData(listOrderDishshopVo, mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void removeWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                                updateData(listOrderDishshopVo, mTradeVo);
                gotoListViewBottom();
            }


            @Override
            public void doBanquet(TradeVo mTradeVo) {
                updateData(mSepShoppingCart.getShoppingCartItems(), mTradeVo);
                gotoListViewBottom();
            }

            @Override
            public void removeBanquet(TradeVo mTradeVo) {
                updateData(mSepShoppingCart.getShoppingCartItems(), mTradeVo);
            }

            @Override
            public void setIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                updateData(mSepShoppingCart.getShoppingCartItems(), mTradeVo);
            }

            @Override
            public void removeIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                updateData(mSepShoppingCart.getShoppingCartItems(), mTradeVo);
            }

            @Override
            public void setCouponPrivi1lege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                updateData(listOrderDishshopVo, mTradeVo);
            }

            @Override
            public void removeCouponPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                updateData(listOrderDishshopVo, mTradeVo);
            }

            @Override
            public void updateDish(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
                updateData(listOrderDishshopVo, mTradeVo);
            }

            @Override
            public void clearShoppingCart() {
                super.clearShoppingCart();
            }
        });
    }


    private synchronized boolean dealSplitToOld() {
        boolean isToOld = false;
        List<IShopcartItem> oldShopcartItemList = mShoppingCart.getShoppingCartDish();
        if (mSepShoppingCart.isAllOrder(oldShopcartItemList) || mSepShoppingCart.getShoppingCartItems().isEmpty()) {
                        mSepShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.DINNER_BALANCE_SPLIT);
            showTotalPage(false);
            goDefaultDisplayMode(false);
            DinnerShopManager.getInstance().resetSepShopcart();
            CustomerManager.getInstance().setSeparateLoginCustomer(null);
            oldShopcartItemList = mShoppingCart.filterDishList(oldShopcartItemList, false);
            updateData(oldShopcartItemList, mShoppingCart.getOrder());
                        EventBus.getDefault().post(new ActionRefreshDinnerCustomer());
            isToOld = true;

            noticeRefreshCustomerUI(getActivity(), false);
        }
        updateMarketActivityFragment();
        if (!isBatchDiscountMode && !isDoDelete && isToOld) {
            gotoListViewBottom();
        }

        return isToOld;
    }

    @Override
    public void onPause() {
                if (selectedDishAdapter.isDishCheckMode()) {
            selectedDishAdapter.setDishCheckMode(false);
            selectedDishAdapter.setMarketRuleVo(null);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.DINNER_DISH_BALANCE_SHOW);
        DinnerShopManager.getInstance().setSepartShopCart(false);
        SeparateShoppingCart.getInstance().clearShoppingCart();
        CustomerManager.getInstance().setSeparateLoginCustomer(null);
        selectedDishAdapter.setDishCheckMode(false);
        if (mCurrentMode == SPLIT_MODE) {
            DinnerShopManager.getInstance().doReset(false);
        }
        this.unregisterEventBus();
        super.onDestroy();
    }
}
