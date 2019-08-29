package com.zhongmei.beauty.order;

import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongmei.beauty.dialog.BeautyBookingWaiterDialog;
import com.zhongmei.beauty.entity.UserVo;
import com.zhongmei.beauty.interfaces.BeautyOrderOperatorListener;
import com.zhongmei.beauty.order.event.ActionClearShopcart;
import com.zhongmei.beauty.order.view.BeautySingleDiscountView;
import com.zhongmei.beauty.order.view.BeautySingleDiscountView_;
import com.zhongmei.beauty.order.view.BeautyVerifyCodeView;
import com.zhongmei.beauty.order.view.BeautyVerifyCodeView_;
import com.zhongmei.beauty.utils.TradeUserUtil;
import com.zhongmei.bty.basemodule.discount.enums.DiscountType;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.mobilepay.event.ActionClose;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.enums.TradeUserType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.util.ViewUtil;
import com.zhongmei.beauty.order.event.BeautyUpdateUserInfo;
import com.zhongmei.beauty.order.util.BeautyOrderActionManager;
import com.zhongmei.beauty.order.util.BeautyPropertyUtil;
import com.zhongmei.beauty.order.util.IChangeMiddlePageListener;
import com.zhongmei.beauty.order.util.IOperateListener;
import com.zhongmei.beauty.order.view.BeautyActivityView;
import com.zhongmei.beauty.order.view.BeautyBaseWaiter;
import com.zhongmei.beauty.order.view.BeautyBaseWaiter_;
import com.zhongmei.beauty.order.view.BeautyCardView;
import com.zhongmei.beauty.order.view.BeautyCouponView;
import com.zhongmei.beauty.order.view.BeautyCouponView_;
import com.zhongmei.beauty.order.view.BeautyDiscountView_;
import com.zhongmei.beauty.order.view.BeautyIntegralView_;
import com.zhongmei.beauty.order.view.BeautyActivityView_;
import com.zhongmei.beauty.order.vo.BeautyOpActionVo;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.view.VerticalViewPager;
import com.zhongmei.bty.dinner.action.ActionSeparateDeleteCoupon;
import com.zhongmei.bty.dinner.cash.DinnerWeixinCouponCodeFragment;
import com.zhongmei.bty.dinner.cash.DinnerWeixinCouponCodeFragment_;
import com.zhongmei.bty.snack.event.EventCouponVoResult;
import com.zhongmei.bty.snack.orderdish.adapter.CommonPagerAdapter;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomEmptyView;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomEmptyView_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */

@EFragment(R.layout.beauty_order_middle_layout)
public class BeautyOrderMiddleFragment extends BasicFragment implements IOperateListener {

    @ViewById(R.id.v_content)
    FrameLayout vContent;

    @ViewById(R.id.vActionBar)
    LinearLayout vActionBar;

    /**
     * 当前内容
     */
    private View mCurrentContentView;
    /**
     * 空态页
     */
    private CustomEmptyView mCustomEmptyView;

    /**
     * 会员相关操作
     */
    @ViewById(R.id.layout_member_operate)
    LinearLayout layout_member_operate;
    @ViewById(R.id.btn_card)
    Button btn_card; //次卡
    @ViewById(R.id.btn_integral)
    Button btn_integral; //积分
    @ViewById(R.id.btn_coupon)
    Button btn_coupon; //优惠卷
    @ViewById(R.id.btn_activity)
    Button btn_activity; //活动

    /**
     * 整单操作
     */
    @ViewById(R.id.layout_trade_operate)
    LinearLayout layout_trade_operate;
    @ViewById(R.id.btn_discount)
    Button btn_discount;//折扣
    @ViewById(R.id.btn_trade_remark)
    Button btn_trade_remark;//备注
    @ViewById(R.id.btn_table)
    Button btn_table;//桌台选择
    @ViewById(R.id.btn_party)
    Button btn_party;//服务员选择
    @ViewById(R.id.btn_verify_code)
    Button btn_verifyCode;//验券
    @ViewById(R.id.btn_clearcart)
    Button btn_clearcart;//清空购物车


    /**
     * 单品操作
     */
    @ViewById(R.id.layout_trade_item_operate)
    LinearLayout layout_trade_item_operate;
    @ViewById(R.id.btn_extra)
    Button btn_extra;
    @ViewById(R.id.btn_trade_item_party)
    Button btn_trade_item_party;
    @ViewById(R.id.btn_trade_item_discount)
    Button btn_trade_item_discount;
    @ViewById(R.id.btn_trade_item_remark)
    Button btn_trade_item_remark;

    //规格
    @ViewById(R.id.btn_standard)
    Button btnStandard;

    @ViewById(R.id.btn_remark)
    Button btnRemark;

    @ViewById(R.id.btn_cosmetologist)
    Button btnCosmetologist;

    @ViewById(R.id.btn_adviser)
    Button btnAdviser;

    @ViewById(R.id.btn_sale)
    Button btnSale;

    @ViewById(R.id.vvp_order_action)
    protected VerticalViewPager vvpOrderAction;

    @ViewById(R.id.iv_switch_page)
    protected ImageView ivSwitchPage;

    protected CommonPagerAdapter mCommonPagerAdapter;

    private IChangeMiddlePageListener mChangeListener;
    //当前选中的购物车条目
    private DishDataItem mDishDataItem;
    BeautyPropertyUtil beautyPropertyUtil = null;

    private BeautyActivityView beautyActivityView;
    private BeautyOrderActionManager mBeautyActionManager;

    private BeautyCouponView beautyCouponView;
    private BeautyBaseWaiter beautyWaiter;
    private ChangePageListener mChangePageListener;

    private BeautyOrderOperatorListener mOperatorListener;

    public void setOperatorListener(BeautyOrderOperatorListener listener) {
        this.mOperatorListener = listener;
    }

    @AfterViews
    protected void initView() {
        initBtnSelected();
        initManager();
        initPropertyUtil();
        registerEventBus();
    }

    private void initManager() {

        mBeautyActionManager = new BeautyOrderActionManager(getActivity(), new BeautyOrderActionManager.IOrderActionListener() {
            @Override
            public void onDataChanged() {
                if (BeautyOrderMiddleFragment.this.isDestroyView()) {
                    return;
                }
                initVvpOrderAction();
                ivSwitchPage.setVisibility(mBeautyActionManager.showSwitchPage() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onActionClicked(View view, BeautyOpActionVo orderActionVo) {
                if (orderActionVo != null) {
                    int type = orderActionVo.getType();
                    showOrderActionView(view, type);
                }
            }
        });
        mBeautyActionManager.loadData();

    }

    private void initVvpOrderAction() {
        mCommonPagerAdapter = new CommonPagerAdapter(mBeautyActionManager.inflateView());
        vvpOrderAction.setAdapter(mCommonPagerAdapter);
        vvpOrderAction.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //目前只有两页
                if (i > 0) {
                    ivSwitchPage.setImageResource(R.drawable.ic_switch_page_pre);
                } else {
                    ivSwitchPage.setImageResource(R.drawable.ic_switch_page_next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        vvpOrderAction.setVisibility(View.VISIBLE);
        mBeautyActionManager.refreshCustomer(false);
    }

    private void initPropertyUtil() {
        beautyPropertyUtil = new BeautyPropertyUtil(getActivity(), mChangePageListener, this, mChangeListener);
        beautyPropertyUtil.initView(vActionBar, btn_extra, btnRemark,btn_trade_item_remark);
    }

    private void initBtnSelected() {
        btnStandard.setEnabled(false);
        btnStandard.setAlpha(0.4f);
    }


    public void doSelect(DishDataItem item) {
        mDishDataItem = item;
        switchOperate(false);
        showStandard();
    }

    private void switchOperate(boolean isTrade){
        if(isTrade){
            layout_trade_operate.setVisibility(View.VISIBLE);
            layout_trade_item_operate.setVisibility(View.GONE);
            layout_member_operate.setVisibility(CustomerManager.getInstance().getDinnerLoginCustomer()==null?View.GONE:View.VISIBLE);
        }else{
            layout_trade_operate.setVisibility(View.GONE);
            layout_trade_item_operate.setVisibility(View.VISIBLE);
            layout_member_operate.setVisibility(View.GONE);
        }
    }


    @Click({R.id.btn_card,R.id.btn_integral,R.id.btn_coupon,R.id.btn_activity,
            R.id.btn_discount,R.id.btn_trade_remark,R.id.btn_table,R.id.btn_party,R.id.btn_verify_code,R.id.btn_clearcart,
            R.id.btn_extra,R.id.btn_trade_item_discount,R.id.btn_trade_item_party,R.id.btn_trade_item_remark,
            R.id.btn_cosmetologist, R.id.btn_adviser, R.id.btn_sale})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_card: //会员次卡
                showCustomerCard();
                break;
            case R.id.btn_integral://会员积分
                showIntegral();
                break;
            case R.id.btn_coupon: //会员优惠卷
                showCoupon();
                break;
            case R.id.btn_activity: //会员活动
                showMarketActivity();
                break;
            case R.id.btn_discount: //整单折扣
                showDiscount(DiscountType.ALLDISCOUNT);
                break;
            case R.id.btn_trade_remark://整单备注
                showRemark();
                ViewUtil.setButtonSelected(vActionBar, btn_trade_remark);
                break;
            case R.id.btn_table://选择工作台
                if(mOperatorListener!=null){
                    mOperatorListener.onTableClick();
                }
                break;
            case R.id.btn_party://整单销售员
                showShopowner();
                break;
            case R.id.btn_verify_code:
                showVerifyCode();
                break;
            case R.id.btn_clearcart://清空购物车
                clearCart();
                break;
            case R.id.btn_extra: //项目
                showExtra();
                break;
            case R.id.btn_trade_item_discount:
                showSingleDiscount();
                break;
            case R.id.btn_trade_item_party://单品服务员
                showWaiter(R.string.beauty_choice_trade_item_wiater,false);
                ViewUtil.setButtonSelected(vActionBar, btn_trade_item_party);
                break;
            case R.id.btn_trade_item_remark://单品备注
                showRemark();
                ViewUtil.setButtonSelected(vActionBar, btn_trade_item_remark);
                break;

            case R.id.btn_cosmetologist:
                showCosmetologist();
                break;
            case R.id.btn_adviser:
                showAdviser();
                break;
            case R.id.btn_sale:
                showShopowner();
                break;
        }
    }


    /**
     * 清空购物车
     */
    private void clearCart() {
        BeautyOrderManager.clearShopcartDish();
        EventBus.getDefault().post(new ActionClearShopcart());
        EventBus.getDefault().post(new ActionClose());
    }

    //目前只有两页，在第二页则返回第一页，在第一页则前往第二页
    @Click(R.id.iv_switch_page)
    protected void clickSwitchPageButton() {
        if (vvpOrderAction.getCurrentItem() > 0) {
            vvpOrderAction.setCurrentItem(0);
        } else {
            vvpOrderAction.setCurrentItem(1);
        }
    }

    private void showStandard() {
        if (beautyPropertyUtil != null) {
            beautyPropertyUtil.stopAsyncTask();
        }
        beautyPropertyUtil.initData(mDishDataItem);
    }


    /**
     * 显示技师
     */
    private void showCosmetologist() {
        //技师只处理单品
        if (mDishDataItem == null) {
            ToastUtil.showLongToast(R.string.beauty_consmetologist_unselected);
            return;
        }
        mBeautyActionManager.clearSelectedView();
        ViewUtil.setButtonSelected(vActionBar, btnCosmetologist);
        boolean isHasPointView = true;

        showWaiter( R.string.beauty_choice_technician, isHasPointView);
        mChangeListener.changePage(IChangeMiddlePageListener.DEFAULT_PAGE, mDishDataItem.getBase().getUuid());
    }


    private void showWaiter(int resId, boolean isHasPointView) {
        beautyWaiter = BeautyBaseWaiter_.build(getActivity(), resId, isHasPointView);
        beautyWaiter.setOnUserCheckedListener(checkedListener);
        if (mDishDataItem != null) {
            beautyWaiter.initItemData(mDishDataItem.getBase(), mDishDataItem.getType());
        } else {
            if (DinnerShoppingCart.getInstance().getOrder().getTradeUsers() == null) {
                DinnerShoppingCart.getInstance().getOrder().setTradeUsers(new ArrayList<TradeUser>());
            }
            beautyWaiter.initTradeuserData(DinnerShoppingCart.getInstance().getOrder().getTradeUsers());
        }
        beautyWaiter.refreshView(DinnerShoppingCart.getInstance().getOrder().getTrade().getId());
        showCustomContentView(beautyWaiter);
    }

    /**
     * 顾问
     */
    private void showAdviser() {
        clearSingleItem();
        mBeautyActionManager.clearSelectedView();
        ViewUtil.setButtonSelected(vActionBar, btnAdviser);
        showWaiter(R.string.beauty_choice_adviser, false);
        mChangeListener.changePage(IChangeMiddlePageListener.COMMON_DEFINE_PAGE, null);
    }

    /**
     * 店长
     */
    private void showShopowner() {
        clearSingleItem();
        mBeautyActionManager.clearSelectedView();
        ViewUtil.setButtonSelected(vActionBar, btn_party);
        showWaiter( R.string.beauty_choice_trade_wiater, false);
        mChangeListener.changePage(IChangeMiddlePageListener.COMMON_DEFINE_PAGE, null);
    }

    /**
     * 展示验券的UI
     */
    private void showVerifyCode(){
        clearSingleItem();
        clearButtonSelected();
        ViewUtil.setButtonSelected(vActionBar, btn_verifyCode);
        BeautyVerifyCodeView beautyVerifyCodeView = BeautyVerifyCodeView_.build(getActivity(), mChangePageListener, mChangeListener,getFragmentManager());
        showCustomContentView(beautyVerifyCodeView);
//        mBeautyActionManager.setSelectedView(selectedView);
        mChangeListener.changePage(IChangeMiddlePageListener.VERIFY_CODE, null);
    }

    BeautyBaseWaiter.OnUserCheckedListener checkedListener = new BeautyBaseWaiter.OnUserCheckedListener() {
        @Override
        public void onUserCheckedChange(boolean isDefine) {
            //通知购物车更新
            DinnerShoppingCart.getInstance().updateUserInfo();
        }
    };

    private void showOrderActionView(View selectedView, int type) {
        switch (type) {
            case BeautyOpActionVo.TYPE_CUSTOMER:
//                showCustomerCard(selectedView);
                break;
            case BeautyOpActionVo.TYPE_INTEGRAL:
//                showIntegral(selectedView);
                break;
            case BeautyOpActionVo.TYPE_COUPONS:
//                showCoupon(selectedView);
                break;
            case BeautyOpActionVo.TYPE_DISCOUNT:
//                showDiscount(selectedView);
                break;
            case BeautyOpActionVo.TYPE_ACTIVITY:
//                showMarketActivity(selectedView);
                break;
            case BeautyOpActionVo.TYPE_WEIXINCODE:
                showWeixinCode(selectedView);
                break;
            default:
                throw new IllegalArgumentException("Illegal Argument" + type);
        }
    }

    /**
     * 会员卡
     */
    public void showCustomerCard() {
        clearSingleItem();
        clearButtonSelected();
        ViewUtil.setButtonSelected(vActionBar, btn_card);
        showCustomContentView(new BeautyCardView(getActivity(), mChangeListener, getChildFragmentManager()));
        mChangeListener.changePage(IChangeMiddlePageListener.DEFAULT_PAGE, null);
    }

    /**
     * 积分
     */
    public void showIntegral() {
        clearSingleItem();
        clearButtonSelected();
        ViewUtil.setButtonSelected(vActionBar, btn_integral);
        showCustomContentView(BeautyIntegralView_.build(getActivity()));
//        mBeautyActionManager.setSelectedView(selectedView);
        mChangeListener.changePage(IChangeMiddlePageListener.DEFAULT_PAGE, null);
    }


    /**
     * 参与者
     */
    public void showParty(final TradeVo tradeVo) {

        List<UserVo> mUserVos = new ArrayList<>();

        if (mDishDataItem != null) {//获取当前选中商品的服务技师
            mUserVos = getDishItemUserVos(mDishDataItem.getItem().getTradeItemUserList());
        } else {//查询当前订单销售员
            mUserVos = getTradeUserVos(tradeVo);
        }


        BeautyBookingWaiterDialog dialog = new BeautyBookingWaiterDialog();
        dialog.setOnBeautyWaiterListener(new BeautyBookingWaiterDialog.OnBeautyWaiterListener() {
            @Override
            public void onChoiceUserListener(@Nullable List<? extends UserVo> userVos) {
                //当前选中的tradeUser
                if (mDishDataItem != null) {//tradeItemUser
                    //处理DishItemUser
                    UserVo userVo = null;
                    if (userVos != null && userVos.size() > 0) {
                        userVo = userVos.get(0);
                    }
                    updateDishItemPary(mDishDataItem, userVo);
                } else {
                    UserVo userVo = null;
                    if (userVos != null && userVos.size() > 0) {
                        userVo = userVos.get(0);
                    }
                    //处理整单销售员
                    updateTradeUser(tradeVo, userVo);
                }
            }
        });
        dialog.setDishDataItem(mDishDataItem);
        dialog.setSelectUsers(mUserVos); // 已选择的
        dialog.setIsNotFreeUsers(new ArrayList<Long>());//被占用的，
        dialog.show(getSupportFragmentManager(), "BeautyBookingWaiterDialog");
    }

    private void updateDishItemPary(DishDataItem dishItem, UserVo userVo) {
        if (userVo != null) {//作废之前的
            TradeUserUtil.addTradeItemUsers(userVo.getUser(), mDishDataItem.getBase(), userVo.isOppoint());
        } else {//新增一个
            TradeUserUtil.removeTradeItemusers(null, mDishDataItem.getBase());
        }

        DinnerShoppingCart.getInstance().updateUserInfo();

    }

    private void updateTradeUser(TradeVo tradeVo, UserVo userVo) {
        if (userVo == null) {
            TradeUserUtil.removeTradeUser(tradeVo.getTradeUsers(), null);
        } else {
            if (tradeVo.getTradeUsers() == null) {
                tradeVo.setTradeUsers(new ArrayList<TradeUser>());
            }

            TradeUserUtil.addTradeUser(tradeVo.getTradeUsers(), userVo.getUser(), tradeVo.getTrade());

        }
        DinnerShoppingCart.getInstance().updateUserInfo();
    }

    /**
     * 优惠劵
     */
    public void showCoupon() {
        clearSingleItem();
        clearButtonSelected();
        ViewUtil.setButtonSelected(vActionBar, btn_coupon);
        beautyCouponView = BeautyCouponView_.build(getActivity());
        showCustomContentView(beautyCouponView);
//        mBeautyActionManager.setSelectedView(selectedView);
        mChangeListener.changePage(IChangeMiddlePageListener.DEFAULT_PAGE, null);
    }

    /**
     * 折扣
     */
    public void showDiscount(DiscountType type) {
        clearSingleItem();
        clearButtonSelected();
        ViewUtil.setButtonSelected(vActionBar, btn_discount);
        showCustomContentView(BeautyDiscountView_.build(getActivity(),type));
//        mBeautyActionManager.setSelectedView(selectedView);
        mChangeListener.changePage(IChangeMiddlePageListener.DEFINE_DISCOUNT_PAGE, null);
    }

    /**
     * 单品打折
     */
    public void showSingleDiscount(){
        if (mDishDataItem == null) {
            ToastUtil.showLongToast(R.string.beauty_consmetologist_unselected);
            return;
        }
        ViewUtil.setButtonSelected(vActionBar, btn_trade_item_discount);
        showCustomContentView(BeautySingleDiscountView_.build(getActivity(),mDishDataItem));
//        mBeautyActionManager.setSelectedView(selectedView);
        mChangeListener.changePage(IChangeMiddlePageListener.DEFAULT_PAGE, mDishDataItem.getBase().getUuid());
    }

    /**
     * 显示加项
     */
    public void showExtra() {
        if (beautyPropertyUtil != null) {
            beautyPropertyUtil.showExtra();
            ViewUtil.setButtonSelected(vActionBar, btn_extra);
            Log.e("MiddleFragment", "showExtra.....>");
        }
    }

    /**
     * 显示整单备注
     */
    public void showRemark() {
        if (beautyPropertyUtil != null) {
            beautyPropertyUtil.showRemarkView();
            Log.e("MiddleFragment", "showRemark.....>");
        }
    }

    /**
     * 营销活动
     */
    public void showMarketActivity() {
        clearSingleItem();
        clearButtonSelected();
        ViewUtil.setButtonSelected(vActionBar, btn_activity);
        beautyActivityView = BeautyActivityView_.build(getActivity(), mChangePageListener, mChangeListener);
        showCustomContentView(beautyActivityView);
//        mBeautyActionManager.setSelectedView(selectedView);
        mChangeListener.changePage(IChangeMiddlePageListener.MARKET_ACTIVITY_PAGE, null);
    }

    /**
     * 微信卡劵
     */
    private void showWeixinCode(View selectedView) {
        clearSingleItem();
        clearButtonSelected();
        DinnerWeixinCouponCodeFragment weixinCouponCodeFragment = new DinnerWeixinCouponCodeFragment_();
        weixinCouponCodeFragment.show(getFragmentManager(), weixinCouponCodeFragment.getClass().getSimpleName());
        mChangeListener.closePage(null);
        doCancel();
    }

    /**
     * 清楚单品数据
     */
    private void clearSingleItem() {
        mDishDataItem = null;
        switchOperate(true);
        if (beautyPropertyUtil != null) {
            beautyPropertyUtil.initData(null);
        }
    }

    /**
     * 取消添加营销活动
     */
    public void cancelMarketView() {
        if (beautyActivityView != null) {
            beautyActivityView.cancelSelected();
        }
    }


    /**
     * @param customContentView
     */
    public void showCustomContentView(View customContentView) {
        vContent.removeAllViews();
        vContent.setVisibility(View.VISIBLE);
        if (mBeautyActionManager != null) {
            mBeautyActionManager.clearSelectedView();
        }
        if (customContentView == null) {
            if (mCustomEmptyView == null) {
                mCustomEmptyView = CustomEmptyView_.build(getActivity());
            }
            vContent.addView(mCustomEmptyView);
            setCurrentContentView(mCustomEmptyView);
        } else {
            vContent.addView(customContentView);
            setCurrentContentView(customContentView);
        }
    }

    public void setCurrentContentView(View currentContentView) {
        mCurrentContentView = currentContentView;
    }

    public void showContent() {
        if (vContent != null)
            vContent.setVisibility(View.VISIBLE);
    }

    public void hideContent() {
        mDishDataItem = null;
        if (vContent != null) {
            Log.e("MiddleFragment", "removeAllViews.....>");
            vContent.removeAllViews();
            vContent.setVisibility(View.GONE);
        }

        if (beautyPropertyUtil != null) {
            beautyPropertyUtil.stopAsyncTask();
        }
        beautyActivityView = null;
        beautyCouponView = null;
        beautyWaiter = null;
        switchOperate(true);
        Log.e("MiddleFragment", "FaceRoundView.....>");
    }

    /**
     * 推出fragment显示
     */
    public void doCancel() {
        hideContent();
        clearSingleItem();
        clearButtonSelected();
    }

    private void clearButtonSelected() {
        if (vActionBar == null) {
            return;
        }
        ViewUtil.setButtonSelected(vActionBar, null);
        if (mBeautyActionManager != null) {
            mBeautyActionManager.clearSelectedView();
        }

        if(mOperatorListener!=null){
            mOperatorListener.onClearSelected();
        }
    }


    public void doLoginCustomer(CustomerResp customerNew) {
        mBeautyActionManager.refreshCustomer(true);
        CustomerManager.getInstance().setDinnerLoginCustomer(customerNew);
        layout_member_operate.setVisibility(View.VISIBLE);
    }

    public void doExitCustomer() {
        mBeautyActionManager.refreshCustomer(false);
        CustomerManager.getInstance().setDinnerLoginCustomer(null);
        layout_member_operate.setVisibility(View.GONE);
        doCancel();
    }

    public void registerListener(ChangePageListener changePageListener, IChangeMiddlePageListener listener) {
        mChangePageListener = changePageListener;
        mChangeListener = listener;
    }

    public void onEventMainThread(EventCouponVoResult eventdata) {
        if (beautyCouponView != null) {
            beautyCouponView.onEventMainThread(eventdata);
        }
    }

    public void onEventMainThread(ActionSeparateDeleteCoupon event) {
        if (beautyCouponView != null) {
            beautyCouponView.onEventMainThread(event);
        }
    }

    public void onEventMainThread(BeautyUpdateUserInfo updateUserInfo) {
        if (beautyWaiter == null || beautyWaiter.getVisibility() != View.VISIBLE) {
            return;
        }
        if (mDishDataItem != null) {
            beautyWaiter.initItemData(mDishDataItem.getBase(), mDishDataItem.getType());
        } else {
            beautyWaiter.initTradeuserData(DinnerShoppingCart.getInstance().getOrder().getTradeUsers());
        }
        beautyWaiter.refreshView(DinnerShoppingCart.getInstance().getOrder().getTrade().getId());
    }


    /**
     * 获取TradeUser
     */
    private List<UserVo> getTradeUserVos(TradeVo tradeVo) {
        List<UserVo> userVos = new ArrayList<>();
        if (tradeVo != null && tradeVo.getTradeUser() != null) {

            TradeUser tradeUser = tradeVo.getTradeUser();
            userVos.add(buildUserVo(tradeUser.getUserId(), tradeUser.getUserName()));

        }
        return userVos;
    }

    private List<UserVo> getDishItemUserVos(List<TradeUser> tradeItemUser) {
        List<UserVo> userVos = new ArrayList<>();
        if (tradeItemUser != null && tradeItemUser.size() > 0) {
            userVos.add(buildUserVo(tradeItemUser.get(0).getUserId(), tradeItemUser.get(0).getUserName()));
        }
        return userVos;
    }


    private UserVo buildUserVo(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return new UserVo(user);
    }


    @Override
    public void onDestroy() {
        unregisterEventBus();
        super.onDestroy();
    }

}
