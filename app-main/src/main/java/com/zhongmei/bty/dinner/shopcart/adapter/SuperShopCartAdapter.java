package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.erp.util.ErpConstants;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IOrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.basemodule.orderdish.enums.ExtraItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shoppingcart.utils.GroupShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.utils.DinnerUtils;
import com.zhongmei.beauty.utils.TradeUserUtil;
import com.zhongmei.bty.common.view.NumberEditText;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MathDecimal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 购物车界面通用部分
 * Created by demo on 2018/12/15
 */

public abstract class SuperShopCartAdapter extends BaseAdapter {
    private final static String TAG = SuperShopCartAdapter.class.getSimpleName();
    //布局显示type
    //菜品布局
    public static final int DISH_TYPE = 0;
    //
    public static final int LABLE_TYPE = 1;
    //
    public static final int MEMO_TYPE = 2;
    //单品优惠
    public static final int SIGLE_PRIVILEGE_TYPE = 3;
    //整单优惠相关布局
    public static final int PRIVILEGE_TYPE = 4;
    //营销活动
    public static final int MARKET_TYPE = 5;
    //营销活动分割线
    public static final int GRAY_SEPERATOR_TYPE = 6;
    //加料
    public static final int EXTRA_TYPE = 7;
    //属性(口味做法)
    public static final int PROPERTIES_TYPE = 8;
    //title 类别
    public static final int TITLE_TYPE = 9;
    //自助购物车显示
    public static final int BUFFET_PEOPLE_TYPE = 10;
    //押金
    public static final int BUFFET_EXTRA = 11;
    //等叫、起菜类别
    public static final int TITLE_CATEGORY = 12;
    //西餐菜品布局
    public static final int DISH_WEST_TYPE = 13;
    //技师或者销售
    public static final int ITEM_USER_TYPE = 14;
    //整单技师或者销售
    public static final int TRADE_USER_TYPE = 15;
    //卡服务提示
    public static final int CARD_SERVICE_LABEL = 16;

    protected Boolean isBatchDiscountModle = false;// 是否是批量折扣模式，默认否
    protected boolean isBatchCoercionModel = false; //是否强制批量折扣，不受后台折扣限制

    protected Boolean isDiscountAllMode = false;// 是否整单打折界面
    protected boolean isShowAllDiscount = false;// 是否显示整单折扣信息

    protected boolean isShowFreeDiscount = true;// 是否显示赠送

    protected boolean isHasBanquetOrFree = false;//是否有宴请或者免单

    protected static boolean isDishCheckMode = false;// 是否处于菜品操作mode

    final String STR_SEMICOLON = "：";// 分号

    final String STR_COMMA = "，";// 逗号

    protected int IMAGEMARGINRIGHT = 5;// 图标边距

    protected ArrayList<DishDataItem> data = new ArrayList<DishDataItem>(10); // 保存转换后的数据item
    protected Context context;

    protected String min;
    protected String hour;
    protected String day;
    protected Drawable mChildIcon;// 子菜图标
    protected Drawable mDeleteIcon, mDishUnSaveIcon, mDishUnPrintedIcon, mDishPrintedIcon, mDishPausedIcon, mDIshPrintFailIcon,
            mDishPrintIngIcon, mDishDiscountAllIcon, mDishNoDiscountIcon, mBuffetPeople, mDeposit, mOutTimeFee, mDrawableCategory;

    protected Drawable mLabelUnSaveIcon, mLabelSaveUnprintedIcon, mLabelSavePrintedIcon;
    // 拆单icon 拆单并打印，拆单未打印
    protected Drawable mSplitPritedIcon, mSplitUnPritedIcon;

    protected BigDecimal mAllDishCount = BigDecimal.ZERO;//商品总数
    // 存储菜品uuid和对应的营销活动对象
    protected Map<String, TradeItemPlanActivity> tradeItemPlanActivityMap = new HashMap<String, TradeItemPlanActivity>();
    //是否是配菜
    protected boolean isSlideDish = false;
    //桌台数量
    protected BigDecimal deskCount = BigDecimal.ONE;

    protected Map<Long, List<IShopcartItem>> typeMap = null;

    Map<Long, List<IShopcartItem>> singleTypeMap = null;

    /**
     * 初始化操作
     */
    protected void init(Context context) {
        this.context = context;
        min = context.getString(R.string.min_before);
        hour = context.getString(R.string.hour_before);
        day = context.getString(R.string.day_before);

        this.mDishUnSaveIcon = context.getResources().getDrawable(R.drawable.dinner_dish_unsave);
        this.mDishUnPrintedIcon = context.getResources().getDrawable(R.drawable.dinner_dish_unprinted);
        this.mDishPrintedIcon = context.getResources().getDrawable(R.drawable.dinner_dish_printed);
        this.mLabelUnSaveIcon = context.getResources().getDrawable(R.drawable.dinner_label_unsave);
        this.mLabelSaveUnprintedIcon = context.getResources().getDrawable(R.drawable.dinner_label_unprinted);
        this.mLabelSavePrintedIcon = context.getResources().getDrawable(R.drawable.dinner_label_printed);
        this.mDishPausedIcon = context.getResources().getDrawable(R.drawable.dinner_pause_icon);
        this.mDIshPrintFailIcon = context.getResources().getDrawable(R.drawable.dinner_print_fail_icon);
        this.mDishPrintIngIcon = context.getResources().getDrawable(R.drawable.dinner_print_ing_icon);
        this.mDeleteIcon = context.getResources().getDrawable(R.drawable.dinner_delete_icon);
        this.mSplitPritedIcon = context.getResources().getDrawable(R.drawable.dinner_split_prited_icon);
        this.mSplitUnPritedIcon = context.getResources().getDrawable(R.drawable.dinner_split_unprited_icon);
        this.mDishDiscountAllIcon = context.getResources().getDrawable(R.drawable.checkbox_selected);
        this.mDishNoDiscountIcon = context.getResources().getDrawable(R.drawable.checkbox_cannot_discount);
        this.mChildIcon = context.getResources().getDrawable(R.drawable.dinner_child_icon);
        this.mDeposit = context.getResources().getDrawable(R.drawable.icon_deposit);
        this.mOutTimeFee = context.getResources().getDrawable(R.drawable.icon_outtime_fee);
        this.mBuffetPeople = context.getResources().getDrawable(R.drawable.icon_buffet_people);
        this.mDrawableCategory = context.getResources().getDrawable(R.drawable.middle_category_shape);
    }

    /**
     * @Title: clear
     * @Description: 清空整个列表
     * @Return void 返回类型
     */
    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public static boolean isDishCheckMode() {
        return isDishCheckMode;
    }

    public static void setDishCheckMode(boolean isDishCheck) {
        isDishCheckMode = isDishCheck;
    }

    public void setSlideDish(boolean isShow) {
        this.isSlideDish = isShow;
    }

    /**
     * 是否是称重商品
     *
     * @param shopcartItem
     * @return
     */
    public boolean isWeight(IShopcartItemBase shopcartItem) {
        return shopcartItem.getSaleType() != null &&
                shopcartItem.getSaleType() == SaleType.WEIGHING;
    }

    public BigDecimal getAllDishCount() {
        return mAllDishCount;
    }

    /**
     * 返回菜品分组显示
     *
     * @return
     */
    public Map<Long, List<IShopcartItem>> getTypeMap() {
        return typeMap;
    }

    /**
     * @Description: 返回转换后的显示数据
     * @Return ArrayList<DishDataItem> 返回类型
     */
    public ArrayList<DishDataItem> getAllData() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DishDataItem getItem(int arg0) {
        if (arg0 >= data.size()) {
            return null;
        }
        return data.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.size() == 0) {
            return 0;
        }
        DishDataItem item = data.get(position);
        switch (item.getType()) {
            case SINGLE:
            case COMBO:
            case CHILD:
            case WEST_CHILD:
                return DISH_TYPE;
            case EXTRA_ITEM:
                return EXTRA_TYPE;
            case PROPERTIES:
                return PROPERTIES_TYPE;
            case SINGLE_DISCOUNT:
            case COMBO_DISCOUNT:
            case GIFT_COUPON:
                return SIGLE_PRIVILEGE_TYPE;
            case SINGLE_MEMO:
            case COMBO_MEMO:
            case CHILD_MEMO:
            case ALL_MEMO:
                return MEMO_TYPE;
            case LABEL_UNSAVE:
            case LABEL_SAVE_PRINTED:
            case LABEL_SAVE_UNPRINTED:
                return LABLE_TYPE;
            case MARKET_ACTIVITY:
                return MARKET_TYPE;
            case GRAY_SEPERATOR:
                return GRAY_SEPERATOR_TYPE;
            case TITLE_ITEM:
                return TITLE_TYPE;
            case BUFFET_TRADE_PEOPLE:
                return BUFFET_PEOPLE_TYPE;
            case BUFFET_EXTRA_DEPOSIT:
                return BUFFET_EXTRA;
            case TITLE_CATEGORY:
                return TITLE_CATEGORY;
            case ITEM_USER:
                return ITEM_USER_TYPE;
            case TRADE_USER:
                return TRADE_USER_TYPE;
            case CARD_SERVICE_LABEL:
            case APPLET_LABEL:
                return CARD_SERVICE_LABEL;
            default:
                break;
        }
        return PRIVILEGE_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return 17;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position < 0 || position >= data.size()) {
            return false;
        }

        DishDataItem item = data.get(position);
        // 如果是拆单和不能打折的不能被点击或者勾选,团餐餐标下的菜品
        if (item != null && item.getBase() != null && (item.getBase().getInvalidType() == InvalidType.SPLIT)
                || isDiscountAllMode) {
            return false;
        }
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        PropertiesHolder propertiesHolder = null;
        ExtraHolder extraHolder = null;
        LabelHolder labelHolder = null;
        MemoHolder memoHolder = null;
        SPrivilegeHolder sPrivilegeHolder = null;
        PrivilegeHolder privilegeHolder = null;
        MarketHolder marketHolder = null;
        SeperatorHolder seperatorHolder = null;
        TitleHolder titleHolder = null;
        CategroyHolder categroyHolder = null;
        BuffetPeopleHolder buffetPeopleHolder = null;
        BuffetExtraHolder buffetExtraHolder = null;
        UserHolder userHolder = null;
        int type = getItemViewType(position);
        switch (type) {
            case DISH_TYPE:
                if (convertView == null || convertView.getTag(R.id.dishView) == null) {
                    holder = new ViewHolder();
                    convertView = initDishLayout(holder);
                    convertView.setTag(R.id.dishView, holder);
                } else {
                    holder = (ViewHolder) convertView.getTag(R.id.dishView);
                }
                break;
            case PROPERTIES_TYPE:
                if (convertView == null || convertView.getTag(R.id.propertiesName) == null) {
                    propertiesHolder = new PropertiesHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_shopcart_item_dish_properties, null);
                    propertiesHolder.propertiesName = (TextView) convertView.findViewById(R.id.propertiesName);
                    convertView.setTag(R.id.propertiesName, propertiesHolder);
                } else {
                    propertiesHolder = (PropertiesHolder) convertView.getTag(R.id.propertiesName);
                }
                break;
            case EXTRA_TYPE:
                // 加料
                if (convertView == null || convertView.getTag(R.id.dishLabelView) == null) {
                    extraHolder = new ExtraHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_shopcart_item_dish_extra, null);
                    extraHolder.dishLabelView = (LinearLayout) convertView.findViewById(R.id.dishLabelView);// 加料
                    extraHolder.dishLabelName = (TextView) convertView.findViewById(R.id.extraLabelName);
                    convertView.setTag(R.id.dishLabelView, extraHolder);
                } else {
                    extraHolder = (ExtraHolder) convertView.getTag(R.id.dishLabelView);
                }
                break;
            case LABLE_TYPE:
                if (convertView == null || convertView.getTag(R.id.labelView) == null) {
                    labelHolder = new LabelHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_shopcart_item_dish_label, null);
                    labelHolder.lableView = (LinearLayout) convertView.findViewById(R.id.labelView);
                    labelHolder.labelName = (TextView) convertView.findViewById(R.id.dishLabelName);
                    labelHolder.topLine = (View) convertView.findViewById(R.id.topline);
                    convertView.setTag(R.id.labelView, labelHolder);
                } else {
                    labelHolder = (LabelHolder) convertView.getTag(R.id.labelView);
                }
                break;
            case MEMO_TYPE:
                if (convertView == null || convertView.getTag(R.id.dish_memo) == null) {
                    memoHolder = new MemoHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_shopcart_item_memo, null);
                    // 备注
                    memoHolder.dish_memo = (TextView) convertView.findViewById(R.id.dish_memo);
                    memoHolder.topLine = (View) convertView.findViewById(R.id.topline);
                    convertView.setTag(R.id.dish_memo, memoHolder);
                } else {
                    memoHolder = (MemoHolder) convertView.getTag(R.id.dish_memo);
                }
                break;

            case SIGLE_PRIVILEGE_TYPE:
                if (convertView == null || convertView.getTag(R.id.privilegeName) == null) {
                    sPrivilegeHolder = new SPrivilegeHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_shopcart_item_sigle_discount_layout, null);
                    sPrivilegeHolder.singlePrivilegeView = (LinearLayout) convertView;
                    sPrivilegeHolder.privilegeName = (TextView) convertView.findViewById(R.id.privilegeName);
                    sPrivilegeHolder.privilegeValue = (TextView) convertView.findViewById(R.id.privilegeValue);
                    sPrivilegeHolder.dish_member_img = (ImageView) convertView.findViewById(R.id.dish_memeber_img);
                    sPrivilegeHolder.dish_memo = (TextView) convertView.findViewById(R.id.dish_memo);
                    convertView.setTag(R.id.privilegeName, sPrivilegeHolder);
                } else {
                    sPrivilegeHolder = (SPrivilegeHolder) convertView.getTag(R.id.privilegeName);
                }
                break;

            case PRIVILEGE_TYPE:
                if (convertView == null || convertView.getTag(R.id.privilege_layout) == null) {
                    privilegeHolder = new PrivilegeHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_shopcart_item_privilege_layout, null);
                    privilegeHolder.privilegeView = (RelativeLayout) convertView;
                    privilegeHolder.topLine = (View) convertView.findViewById(R.id.topline);
                    privilegeHolder.dish_name = (TextView) convertView.findViewById(R.id.dish_name);
                    privilegeHolder.dish_price = (TextView) convertView.findViewById(R.id.dish_price);
                    privilegeHolder.dish_memo = (TextView) convertView.findViewById(R.id.dish_memo);
                    convertView.setTag(R.id.privilege_layout, privilegeHolder);
                } else {
                    privilegeHolder = (PrivilegeHolder) convertView.getTag(R.id.privilege_layout);
                }
                break;

            case MARKET_TYPE:
                // 营销活动条目
                if (convertView == null || convertView.getTag(R.id.ll_market_activity) == null) {
                    marketHolder = new MarketHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_dish_shopcart_item_market, null);
                    marketHolder.llMarketActivity = (LinearLayout) convertView.findViewById(R.id.ll_market_activity);
                    marketHolder.tvMarketActivityName = (TextView) convertView.findViewById(R.id.tv_market_activity_name);
                    marketHolder.tvMarketActivityValue = (TextView) convertView.findViewById(R.id.tv_market_activity_value);
                    marketHolder.ivClose = (ImageView) convertView.findViewById(R.id.iv_close);
                    convertView.setTag(R.id.ll_market_activity, marketHolder);
                } else {
                    marketHolder = (MarketHolder) convertView.getTag(R.id.ll_market_activity);
                }
                break;
            case GRAY_SEPERATOR_TYPE:
                // 灰色间隔线
                if (convertView == null || convertView.getTag(R.id.view_seperator) == null) {
                    seperatorHolder = new SeperatorHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_dish_shopcart_item_gray_line, null);
                    seperatorHolder.viewGraySeperator = convertView.findViewById(R.id.view_seperator);// 灰色间隔线
                    convertView.setTag(R.id.view_seperator, seperatorHolder);
                } else {
                    seperatorHolder = (SeperatorHolder) convertView.getTag(R.id.view_seperator);
                }
                break;
            case TITLE_TYPE:
                if (convertView == null || convertView.getTag(R.id.title) == null) {
                    titleHolder = new TitleHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_shopcart_item_title, null);
                    titleHolder.backgrund = convertView.findViewById(R.id.parent);
                    titleHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
                    titleHolder.topLine = (View) convertView.findViewById(R.id.topline);
                    titleHolder.tvProperty = (TextView) convertView.findViewById(R.id.property);
                    titleHolder.llParent = convertView;
                    convertView.setTag(R.id.title, titleHolder);
                } else {
                    titleHolder = (TitleHolder) convertView.getTag(R.id.title);
                }
                break;

            case TITLE_CATEGORY:
                if (convertView == null || convertView.getTag(R.id.category_name) == null) {
                    categroyHolder = new CategroyHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_shopcart_item_title2, null);
                    categroyHolder.tvCategoryName = (TextView) convertView.findViewById(R.id.category_name);
                    categroyHolder.topLine = (View) convertView.findViewById(R.id.topline);
                    categroyHolder.tvCategoryCount = (TextView) convertView.findViewById(R.id.category_count);
                    convertView.setTag(R.id.category_name, titleHolder);
                } else {
                    categroyHolder = (CategroyHolder) convertView.getTag(R.id.category_name);
                }
                break;

            case BUFFET_PEOPLE_TYPE:
                if (convertView == null || convertView.getTag() == null) {
                    buffetPeopleHolder = new BuffetPeopleHolder();
                    convertView = initDishCarteNormView(LayoutInflater.from(context));
                    buffetPeopleHolder.topLine = (View) convertView.findViewById(R.id.topline);
                    buffetPeopleHolder.tv_name = (TextView) convertView.findViewById(R.id.name);
                    buffetPeopleHolder.tv_count = (TextView) convertView.findViewById(R.id.count);
                    buffetPeopleHolder.tv_price = (TextView) convertView.findViewById(R.id.price);
                    buffetPeopleHolder.iv_icon = (ImageView) convertView.findViewById(R.id.icon);
                    convertView.setTag(buffetPeopleHolder);
                } else {
                    buffetPeopleHolder = (BuffetPeopleHolder) convertView.getTag();
                }
                break;
            case BUFFET_EXTRA:
                if (convertView == null || convertView.getTag() == null) {
                    buffetExtraHolder = new BuffetExtraHolder();
                    convertView = initBuffetExtraView(LayoutInflater.from(context));
                    buffetExtraHolder.topLine = (View) convertView.findViewById(R.id.topline);
                    buffetExtraHolder.tv_name = (TextView) convertView.findViewById(R.id.name);
                    buffetExtraHolder.tv_property = (TextView) convertView.findViewById(R.id.property);
                    buffetExtraHolder.tv_price = (TextView) convertView.findViewById(R.id.price);
                    buffetExtraHolder.iv_icon = (ImageView) convertView.findViewById(R.id.icon);
                    buffetExtraHolder.btn_Edit = (Button) convertView.findViewById(R.id.btn_edit_deposit);
                    convertView.setTag(buffetExtraHolder);
                } else {
                    buffetExtraHolder = (BuffetExtraHolder) convertView.getTag();
                }
                break;
            case CARD_SERVICE_LABEL:
            case ITEM_USER_TYPE:
            case TRADE_USER_TYPE:
                if (convertView == null || convertView.getTag() == null) {
                    userHolder = new UserHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_shopcart_item_user, null);
                    userHolder.user_info = (TextView) convertView.findViewById(R.id.user_name);
                    userHolder.topLine = (View) convertView.findViewById(R.id.topline);
                    convertView.setTag(userHolder);
                } else {
                    userHolder = (UserHolder) convertView.getTag();
                }
                break;
        }

        final DishDataItem item = data.get(position);
        switch (type) {
            case DISH_TYPE:
            case DISH_WEST_TYPE:
                showDishLayout(holder, item, position);
                setTopLine(holder.topLine, item, position);
                break;
            case PROPERTIES_TYPE:
                showPropertys(item, propertiesHolder);
                break;
            case EXTRA_TYPE:
                showExtra(item.getBase(), extraHolder, true);
                break;
            case SIGLE_PRIVILEGE_TYPE:
                showSiglePrivilege(sPrivilegeHolder, item);
                break;
            case LABLE_TYPE:
                showLabel(labelHolder, item);
                setTopLine(labelHolder.topLine, item, position);
                break;
            case PRIVILEGE_TYPE:
                showPrivilege(privilegeHolder, item);
                setTopLine(privilegeHolder.topLine, item, position);
                break;
            case MEMO_TYPE:
                showMemo(memoHolder, item);
                setTopLine(memoHolder.topLine, item, position);
                break;
            case MARKET_TYPE:
                showMarketView(marketHolder, item);
                break;
            case GRAY_SEPERATOR_TYPE:
                showSepLine(seperatorHolder);
                break;
            case TITLE_TYPE:
                showTitle(titleHolder, item);
                setTopLine(titleHolder.topLine, item, position);
                setTitleSeletedBg(titleHolder, item);
                break;
            case TITLE_CATEGORY:
                showCategory(categroyHolder, item);
                setTopLine(categroyHolder.topLine, item, position);
                break;
            case BUFFET_PEOPLE_TYPE:
                showBuffetPeople(buffetPeopleHolder, item);
                setTopLine(buffetPeopleHolder.topLine, item, position);
                break;
            case BUFFET_EXTRA:
                showBuffetExtra(buffetExtraHolder, item);
                setTopLine(buffetExtraHolder.topLine, item, position);
                break;
            case CARD_SERVICE_LABEL:
            case ITEM_USER_TYPE:
                showUser(userHolder, item, false);
                break;
            case TRADE_USER_TYPE:
                showUser(userHolder, item, true);
                break;
        }
        return convertView;
    }

    /**
     * 显示单品或者整单的用户或者技师信息
     *
     * @param userHolder
     * @param item
     * @param isDefine   是否是整单
     */
    protected void showUser(UserHolder userHolder, DishDataItem item, boolean isDefine) {
        if (!isDefine) {
            userHolder.user_info.setLayoutParams(getExtraDiyWh(context, true));
        } else {
            userHolder.user_info.setLayoutParams(getExtraDiyWh(context, false));
        }
        if (item.isNeedTopLine()) {
            userHolder.topLine.setVisibility(View.VISIBLE);
        } else {
            userHolder.topLine.setVisibility(View.GONE);
        }
        userHolder.user_info.setText(item.getName());
    }

    protected void showTitle(TitleHolder titleHolder, DishDataItem item) {
        titleHolder.tvTitle.setText(item.getName());
        titleHolder.tvProperty.setText(item.getStandText());
        titleHolder.tvProperty.setVisibility(TextUtils.isEmpty(item.getStandText()) ? View.GONE : View.VISIBLE);
        if (item.isEnabled()) {
            titleHolder.llParent.setBackgroundColor(context.getResources().getColor(R.color.customer_charging_showdow));
            titleHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.dinner_dishname_color));
            titleHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            titleHolder.tvTitle.setGravity(Gravity.CENTER);
        } else {
            titleHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(mDrawableCategory, null, null, null);
            titleHolder.tvTitle.setCompoundDrawablePadding(5);
            titleHolder.tvTitle.setGravity(Gravity.LEFT);
            titleHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.color_FF8552));
            titleHolder.llParent.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }

        showTitleTopLine(titleHolder);
    }

    private void setTitleSeletedBg(TitleHolder titleHolder, DishDataItem item) {
        if (item.isSelected())
            titleHolder.backgrund.setBackgroundResource(R.drawable.order_dish_item_shape);
        else
            titleHolder.backgrund.setBackgroundColor(Color.TRANSPARENT);
    }

    protected void showCategory(CategroyHolder categoryHolder, DishDataItem item) {
        categoryHolder.tvCategoryName.setText(item.getName());
        int dp5 = DensityUtil.dip2px(context, 5);
        categoryHolder.tvCategoryName.setCompoundDrawablePadding(dp5);
        categoryHolder.tvCategoryName.setTextColor(context.getResources().getColor(R.color.color_FF8552));
        if (item.getCheckStatus() == DishDataItem.DishCheckStatus.CHECKED) {
            categoryHolder.tvCategoryName.setCompoundDrawablesWithIntrinsicBounds(mDishDiscountAllIcon, null, null, null);
        } else {
            Drawable checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_nomal);
            categoryHolder.tvCategoryName.setCompoundDrawablesWithIntrinsicBounds(checkDrawable, null, null, null);
        }
        categoryHolder.tvCategoryCount.setText(context.getString(R.string.dinner_orderdish_dishcheck_number,
                String.valueOf(item.getCount())));
    }

    protected void showTitleTopLine(TitleHolder titleHolder) {

    }

    protected void showBuffetPeople(BuffetPeopleHolder buffetPeopleHolder, DishDataItem item) {
        buffetPeopleHolder.iv_icon.setVisibility(View.GONE);
        buffetPeopleHolder.tv_name.setCompoundDrawablesWithIntrinsicBounds(mBuffetPeople, null, null, null);
        buffetPeopleHolder.tv_name.setText(item.getName());
        buffetPeopleHolder.tv_count.setText("x" + item.getCount());
        buffetPeopleHolder.tv_price.setText(Utils.formatPrice(item.getValue()));

    }


    protected void showBuffetExtra(BuffetExtraHolder buffetExtraHolder, DishDataItem item) {
        if (item.getExtraType() == ExtraItemType.DEPOSIT) {
            buffetExtraHolder.iv_icon.setImageDrawable(mDeposit);
        } else if (item.getExtraType() == ExtraItemType.OUTTIME_FEE) {
            buffetExtraHolder.iv_icon.setImageDrawable(mOutTimeFee);
        } else {
            buffetExtraHolder.iv_icon.setImageDrawable(mDishPrintedIcon);
        }

        buffetExtraHolder.tv_name.setText(item.getName());
        buffetExtraHolder.tv_property.setText(item.getStandText());
        buffetExtraHolder.tv_price.setText(Utils.formatPrice(item.getValue()));
    }


    /**
     * 加载显示菜品条目的item
     *
     * @return
     */
    protected abstract View loadDishLayout();

    /**
     * 菜品布局初始化
     *
     * @param holder
     * @return
     */
    protected abstract View initDishLayout(ViewHolder holder);

    protected View initDishCarteNormView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dinner_dish_buffet_people, null);
    }

    protected View initBuffetExtraView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dinner_dish_buffet_extra, null);
    }

    protected void initDishCommon(ViewHolder holder, View convertView) {
        holder.mainLayout = convertView;
        holder.topLine = (View) convertView.findViewById(R.id.topline);
        holder.checkButton = (ImageView) convertView.findViewById(R.id.checkButton);
        // 菜名
        holder.dishView = (RelativeLayout) convertView.findViewById(R.id.dishView);
        holder.dish_name = (TextView) convertView.findViewById(R.id.dish_name);
        holder.dish_printstate = (ImageView) convertView.findViewById(R.id.printstateicon);
        holder.dish_num = (TextView) convertView.findViewById(R.id.dish_num);
        holder.dish_edit_num = (NumberEditText) convertView.findViewById(R.id.edt_number);
        holder.dish_price = (TextView) convertView.findViewById(R.id.dish_price);
        holder.tvWeighFlag = (TextView) convertView.findViewById(R.id.tv_weigh_flag);
        holder.dish_desc = (TextView) convertView.findViewById(R.id.dish_name_desc);
//        holder.dishTasteView = (LinearLayout) convertView.findViewById(R.id.dishTasteView);

    }

    //滑菜标识
    protected void initSlideLayout(ViewHolder holder, View convertView) {
        holder.slideLayout = (LinearLayout) convertView.findViewById(R.id.slide_layout);
        holder.slideStatusIv = (ImageView) convertView.findViewById(R.id.slide_status_iv);
        holder.slideStatusTv = (TextView) convertView.findViewById(R.id.slide_status_tv);
    }

    protected void initDishOtherLayout(ViewHolder holder, View convertView) {
        holder.tv_remind_dish = (TextView) convertView.findViewById(R.id.tv_remind_dish);
        holder.tv_make_status = (TextView) convertView.findViewById(R.id.tv_make_status);
        holder.tv_dish_bat_serving = (TextView) convertView.findViewById(R.id.tv_dish_bat_serving);

        // 退菜/改菜
        holder.returnDishQuantityTv = (TextView) convertView.findViewById(R.id.return_dish_quantity_tv);
        holder.returnDishReasonTv = (TextView) convertView.findViewById(R.id.return_dish_reason_tv);
        holder.returnDishLL = (LinearLayout) convertView.findViewById(R.id.return_dish_ll);

        // 菜品操作
        holder.dishOperateTagLL = (LinearLayout) convertView.findViewById(R.id.dish_operate_tag_ll);
        holder.dishPrepareTv = (TextView) convertView.findViewById(R.id.dish_prepare_tv);
        holder.dishMakeTv = (TextView) convertView.findViewById(R.id.dish_make_tv);
        holder.dishPrepareCancelTv = (TextView) convertView.findViewById(R.id.dish_prepare_cancel_tv);
        holder.dishMakeCancelTv = (TextView) convertView.findViewById(R.id.dish_make_cancel_tv);
    }

    protected void initTimeView(ViewHolder holder, View convertView) {
        //出单时间
        holder.issueTimeTv = (TextView) convertView.findViewById(R.id.issue_time_tv);
    }

    /**
     * 收银界面拆单时菜品左右箭头
     *
     * @param holder
     * @param convertView
     */
    protected void initAnchorLayout(ViewHolder holder, View convertView) {
        holder.imgAnchorLeft = (ImageView) convertView.findViewById(R.id.dinner_anchor_left);
        holder.imgAnchorRight = (ImageView) convertView.findViewById(R.id.dinner_anchor_right);
    }

    protected void itemSelect(DishDataItem item, ViewHolder holder) {
        Drawable checkDrawable = null;
        if (item.getBase().isSelected()) {
            checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_selected);
        } else {
            checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_nomal);
        }
        holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
        holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(checkDrawable, null, null, null);

    }

    protected abstract void showDishLayout(ViewHolder holder, final DishDataItem item, int position);

    /**
     * 设置共同的菜品的值
     *
     * @param item
     * @param holder
     */
    protected void setDishLayoutValue(DishDataItem item, ViewHolder holder) {
        IShopcartItemBase shopcartItem = item.getBase();
        if (shopcartItem != null) {
            holder.dish_name.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
            holder.dish_name.setText(shopcartItem.getSkuName()); // 菜名
            BigDecimal qty = BigDecimal.ZERO;
            if (item.getType() == ItemType.WEST_CHILD) {
                qty = shopcartItem.getTotalQty();
            } else {
                qty = ShopcartItemUtils.getDisplyQty(shopcartItem, deskCount);
            }
            holder.dish_num.setText("×" + MathDecimal.toTrimZeroString(qty));// 数量
            holder.dish_num.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
            //餐标下的菜品和西餐子菜不显示价格
            if (shopcartItem.isGroupDish() || item.getType() == ItemType.WEST_CHILD) {
                holder.dish_price.setText("");// 价格
            } else {
                holder.dish_price.setText(getDisplayPrice(shopcartItem));// 价格
                holder.dish_price.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
            }

            if (isCardService(shopcartItem) || isApplet(shopcartItem)) {
                holder.dish_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.dish_price.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
            }
            if (isWeight(shopcartItem)) {
                holder.tvWeighFlag.setVisibility(View.VISIBLE);
            } else {
                holder.tvWeighFlag.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 是否是次卡服务项
     *
     * @param shopcartItem
     * @return
     */
    protected boolean isCardService(IShopcartItemBase shopcartItem) {
        if (shopcartItem.getCardServicePrivilgeVo() != null && shopcartItem.getCardServicePrivilgeVo().isPrivilegeValid()) {
            return true;
        }
        return false;
    }

    protected boolean isApplet(IShopcartItemBase shopcartItem) {
        if (shopcartItem.getAppletPrivilegeVo() != null && shopcartItem.getAppletPrivilegeVo().isPrivilegeValid()) {
            return true;
        }
        return false;
    }

    private String getDisplayPrice(IShopcartItemBase shopcartItem) {
        if (shopcartItem.isGroupDish()) {
            return Utils.formatPrice(0);
        }
        return Utils.formatPrice(shopcartItem.getActualAmount().doubleValue());
    }

    /**
     * @Title: showPropertys
     * @Description: 规格口味做法展示
     * @Param @param basedish
     * @Param @param holder
     * @Return void 返回类型
     */
    protected void showPropertys(DishDataItem item, PropertiesHolder holder) {
        holder.propertiesName.setText(item.getProperty().getPropertyName());
        holder.propertiesName.setLayoutParams(getPropertyDiyWh(context, true));
    }

    /**
     * 显示规格
     *
     * @param item
     */
    protected void showStand(DishDataItem item, ViewHolder holder) {
        if (TextUtils.isEmpty(item.getStandText())) {
            return;
        }
        holder.dish_name.setText(item.getBase().getSkuName() + "(" + item.getStandText() + ")");
    }


    /**
     * @Description: 设置扩展数据样式
     * @Param @param shopcartItem
     * @Param @param holder
     * @Param @param isChild 是否子菜
     * @Return void 返回类型
     */
    protected void showExtra(IShopcartItemBase shopcartItem, ExtraHolder holder, boolean isChild) {
        if (shopcartItem != null && shopcartItem.getExtraItems() != null && shopcartItem.getExtraItems().size() > 0) {
            StringBuilder sb = new StringBuilder(context.getResources().getString(R.string.addExtra));
            int i = 0;
            for (IExtraShopcartItem extra : shopcartItem.getExtraItems()) {
                if (i > 0) {
                    sb.append(STR_COMMA);
                }
                //称重商品的加料显示的是实际的数量，团餐要除以桌数
                BigDecimal qty = extra.getSingleQty();
                if (shopcartItem.getSaleType() == SaleType.WEIGHING) {
                    qty = GroupShoppingCartTool.getDisplyExtraQty(extra, deskCount);
                }
                sb.append(extra.getSkuName() + " x" + qty.intValue());
                i++;
            }
            if (i > 0) {
                holder.dishLabelView.setVisibility(View.VISIBLE);
                if (isChild)
                    holder.dishLabelName.setLayoutParams(getExtraDiyWh(context, true));
                holder.dishLabelName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                holder.dishLabelName.setTextSize(context.getResources().getDimension(R.dimen.dinner_shoppingCart_dishMemo_textSize));
                holder.dishLabelName.setTextColor(context.getResources().getColor(R.color.remark_text_color));
                holder.dishLabelName.setText(sb.toString());
            } else {

                holder.dishLabelView.setVisibility(View.GONE);
            }
        } else {
            holder.dishLabelView.setVisibility(View.GONE);
        }
    }

    /**
     * @param holder
     * @param item
     * @Date 2016年4月26日
     * @Description: 更新菜品选择checkbox
     * @Return void
     */
    protected void updateDishCheck(ViewHolder holder, final DishDataItem item) {
        if (!isDishCheckMode || isSlideDish) {
            return;
        }
        if (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO) {
            Drawable checkDrawable = null;
            if (item.getCheckStatus() == DishDataItem.DishCheckStatus.NOT_CHECK) {// 可选
                checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_nomal);
            } else if (item.getCheckStatus() == DishDataItem.DishCheckStatus.CHECKED) {// 已选
                checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_selected);
            } else if (item.getCheckStatus() == DishDataItem.DishCheckStatus.INVALIATE_CHECK) {
                checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_cannot_discount);
            }
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(checkDrawable, null, null, null);
            holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
        }

    }

    /**
     * @Title: resetIcon
     * @Description: 根据状态改变icon图标
     * @Param @param shopcartItem
     * @Param @param holder
     * @Return void 返回类型
     */
    protected void resetIcon(IShopcartItemBase shopcartItem, ViewHolder holder) {
        if (isSlideDish) {
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
        // 整单折扣时设置icon图标
        if (isBatchDiscountModle || isDiscountAllMode) {
            if (isBatchDiscountModle && shopcartItem.isGroupDish()) {
                //团餐菜品不允许批量折扣
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishNoDiscountIcon, null, null, null);
                return;
            }
            if (shopcartItem.getEnableWholePrivilege() == Bool.YES || isHasBanquetOrFree) {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishDiscountAllIcon, null, null, null);
            } else {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishNoDiscountIcon, null, null, null);
            }
            return;
        }
        if (shopcartItem.getIssueStatus() == null) {
            if (shopcartItem.getId() == null) {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishUnSaveIcon, null, null, null);
            } else {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishUnPrintedIcon, null, null, null);
            }
            setSplitIcon(shopcartItem, holder, mSplitUnPritedIcon);
        } else {
            switch (shopcartItem.getIssueStatus()) {
                case PAUSE:// 暂停出单
                    holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishPausedIcon, null, null, null);
                    break;
                case DIRECTLY:// 立即出单
                case DIRECTLY_FROM_CLOUD:
                case ISSUING:// 出单中
                case FAILED:// 出单失败
                    if (shopcartItem.getId() == null) {
                        holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishUnSaveIcon, null, null, null);
                    } else {
                        holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishUnPrintedIcon, null, null, null);
                    }
                    break;
                case FINISHED:// 已出单
                    holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishPrintedIcon, null, null, null);
                    setSplitIcon(shopcartItem, holder, mSplitPritedIcon);
                    break;
                default:
                    if (shopcartItem.getId() == null) {
                        holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishUnSaveIcon, null, null, null);
                    } else {
                        holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishUnPrintedIcon, null, null, null);
                    }
                    setSplitIcon(shopcartItem, holder, mSplitUnPritedIcon);
                    break;
            }

            if (shopcartItem.getInvalidType() != null) {
                switch (shopcartItem.getInvalidType()) {
                    case SPLIT:
                        holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mSplitUnPritedIcon, null, null, null);
                        break;
                    case RETURN_QTY:
                        break;
                    case MODIFY_DISH:
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * @Title: setDeleteIcon
     * @Description: 设置删除菜品图标
     * @Param @param item
     * @Param @param holder
     * @Return void 返回类型
     */
    protected void setDeleteIcon(DishDataItem item, ViewHolder holder) {
        if (item == null || item.getBase() == null || item.getBase().getStatusFlag() == null) {
            return;
        }
        if ((item.getBase().getStatusFlag() == StatusFlag.INVALID)
                && (item.getBase().getInvalidType() != InvalidType.SPLIT)) {
            // modify by zhubo 2015-12-23 子菜不合法时，不需要显示删除图标
            if (item.getType() != ItemType.CHILD) {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDeleteIcon, null, null, null);
            }
            setLayoutGray(holder);
        }
    }

    /**
     * 设置拆单icon及文本
     *
     * @Title: setSplitIcon
     * @Param @param item
     * @Return void 返回类型
     */
    protected void setSplitIcon(IShopcartItemBase itemBase, ViewHolder holder, Drawable drawable) {
        if (itemBase == null || itemBase.getStatusFlag() == null) {
            return;
        }
        if (itemBase.getInvalidType() == InvalidType.SPLIT) {
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
    }

    /**
     * @Title: setGray
     * @Description: 设置透明度
     * @Param @param holder
     * @Return void 返回类型
     */
    protected void setGray(ViewHolder holder, DishDataItem dishItem) {
        if (dishItem == null) {
            return;
        }
        IShopcartItem item = dishItem.getItem();
        if (item != null && item.getInvalidType() == InvalidType.SPLIT) {
            holder.mainLayout.setAlpha(0.5f);
        }
    }

    /**
     * @Title: updatePrintState
     * @Description: 更新打印状态图标
     * @Param @param shopcartItem
     * @Param @param holder
     * @Return void 返回类型
     */
    protected void updatePrintState(IShopcartItemBase shopcartItem, ViewHolder holder) {
        if (shopcartItem.getIssueStatus() == null) {
            holder.dish_printstate.setVisibility(View.GONE);
        } else {
            switch (shopcartItem.getIssueStatus()) {
                case PAUSE:// 暂停出单
                case DIRECTLY:// 立即出单
                case DIRECTLY_FROM_CLOUD:
                case FINISHED:// 已出单
                    holder.dish_printstate.setVisibility(View.GONE);
                    break;
                case ISSUING:// 出单中
                    holder.dish_printstate.setVisibility(View.VISIBLE);
                    holder.dish_printstate.setImageDrawable(mDishPrintIngIcon);
                    break;
                case FAILED:// 出单失败
                    holder.dish_printstate.setVisibility(View.VISIBLE);
                    holder.dish_printstate.setImageDrawable(mDIshPrintFailIcon);
                    break;
                default:
                    holder.dish_printstate.setVisibility(View.GONE);
                    break;
            }
        }
    }

    /**
     * 将文本置灰
     *
     * @Title: setLayoutGray
     * @Param @param item
     * @Return void 返回类型
     */
    protected void setLayoutGray(ViewHolder holder) {
        holder.dish_name.setTextColor(context.getResources().getColor(R.color.dinner_label_unsave));
        holder.dish_name.setCompoundDrawablePadding(5);
        holder.dish_num.setTextColor(context.getResources().getColor(R.color.dinner_label_unsave));
        holder.dish_price.setTextColor(context.getResources().getColor(R.color.dinner_label_unsave));
    }

    // 第一条和设置了不需要topline的条目，隐藏topline
    protected void setTopLine(View topLine, DishDataItem item, int position) {
        if (topLine == null) {
            return;
        }
        if (position == 0 || !item.isNeedTopLine()) {
            topLine.setVisibility(View.GONE);
        } else {
            topLine.setVisibility(View.VISIBLE);
        }
    }


    protected void showLabel(LabelHolder holder, DishDataItem item) {
        Drawable labelDrawable = this.mLabelUnSaveIcon;
        switch (item.getType()) {
            case LABEL_UNSAVE:// 未生效标题
                labelDrawable = this.mLabelUnSaveIcon;
                holder.labelName.setTextAppearance(context, R.style.dinnerLabelUnsave);
                break;
            case LABEL_SAVE_UNPRINTED:// 未出单标题
                labelDrawable = this.mLabelSaveUnprintedIcon;
                holder.labelName.setTextAppearance(context, R.style.dinnerLabelUnPrinted);
                break;
            case LABEL_SAVE_PRINTED:// 已出单标题
                labelDrawable = this.mLabelSavePrintedIcon;
                holder.labelName.setTextAppearance(context, R.style.dinnerLabelSavePrinted);
                break;
        }
        if (!TextUtils.isEmpty(item.getName())) {
            holder.labelName.setText(item.getName());
            holder.labelName.setCompoundDrawablesWithIntrinsicBounds(labelDrawable, null, null, null);
        } else {
            holder.labelName.setCompoundDrawablesWithIntrinsicBounds(null,
                    null,
                    null,
                    null);
        }
    }

    protected void showPrivilege(PrivilegeHolder holder, DishDataItem item) {

    }

    protected void showMarketView(MarketHolder marketHolder, final DishDataItem item) {

    }

    protected void showSepLine(SeperatorHolder sepHolder) {

    }

    protected void showMemo(MemoHolder holder, DishDataItem item) {
        if (item.getType() != ItemType.ALL_MEMO) {
            holder.dish_memo.setLayoutParams(getExtraDiyWh(context, true));
        } else {
            holder.dish_memo.setLayoutParams(getExtraDiyWh(context, false));
        }
        holder.dish_memo.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        holder.dish_memo.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
        if (!TextUtils.isEmpty(item.getName())) {
            holder.dish_memo.setText(item.getName()); // 显示数据
        } else {
            holder.dish_memo.setText("");
        }
    }

    protected void showSiglePrivilege(SPrivilegeHolder holder, DishDataItem item) {
        holder.privilegeName.setTextAppearance(context, R.style.dinnerMemoStyle);
        holder.privilegeName.setLayoutParams(getPrivilegeDiyWh(context, holder.privilegeName));
        if (!TextUtils.isEmpty(item.getName())) {
            if (item.isMemberDiscount()) {
                holder.privilegeValue.setTextColor(context.getResources().getColor(R.color.text_red));
                holder.dish_member_img.setVisibility(View.VISIBLE);
            } else {
                holder.privilegeValue.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
                holder.dish_member_img.setVisibility(View.GONE);
            }
            holder.privilegeName.setText(item.getName());
            holder.privilegeValue.setText(Utils.formatPrice(item.getValue()));// 价格
            //设置菜品理由
            if (!TextUtils.isEmpty(item.getDiscountReason())) {
                holder.dish_memo.setLayoutParams(getPrivilegeDiyWh(context, holder.dish_memo));
                holder.dish_memo.setVisibility(View.VISIBLE);
                holder.dish_memo.setText(item.getDiscountReason());
            } else {
                holder.dish_memo.setVisibility(View.GONE);
            }
            if (!item.isEnabled() && item.getItem() != null && item.getItem().getCouponPrivilegeVo() != null
                    && item.getItem().getCouponPrivilegeVo().isPrivilegeValid()) {
                holder.privilegeName.setTextColor(context.getResources()
                        .getColor(R.color.shopcat_item_coupon_unenabled));
            }
        } else {
            holder.dish_member_img.setVisibility(View.GONE);
            holder.privilegeName.setText("");
            holder.privilegeValue.setText("");
            holder.dish_memo.setVisibility(View.GONE);
        }
    }

    /**
     * @Title: getIsComboDiyWh
     * @Description: 设置子菜显示样式
     * @Param @param context
     * @Return LinearLayout.LayoutParams 返回类型
     */
    public RelativeLayout.LayoutParams getIsComboDiyWh(Context context) {
        int left = DensityUtil.dip2px(context, 18);
        int topOrBottom = DensityUtil.dip2px(context, 5);
        RelativeLayout.LayoutParams diyWh =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(left, topOrBottom, 0, topOrBottom);
        return diyWh;
    }

    /**
     * @Title: getNoComboDiyWh
     * @Description: 获取普通菜品边距
     * @Param @param context
     * @Return LinearLayout.LayoutParams 返回类型
     */
    public RelativeLayout.LayoutParams getNoComboDiyWh(Context context) {
        int top = (int) context.getResources().getDimension(R.dimen.dinner_shoppingCard_dishName_margin);
        int bottom = (int) context.getResources().getDimension(R.dimen.dinner_shoppingCard_dishName_margin);
        RelativeLayout.LayoutParams diyWh =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(0, top, 0, bottom);
        return diyWh;
    }

    /**
     * @Title: getPropertyDiyWh
     * @Description: 获取属性显示参数
     * @Param @param context
     * @Param @param isChild 是否子菜
     * @Return LinearLayout.LayoutParams 返回类型
     */
    protected LinearLayout.LayoutParams getPropertyDiyWh(Context context, boolean isChild) {
        int left = 0;
        if (isChild) {
            left = DensityUtil.dip2px(context, 44);
        } else {
            left = DensityUtil.dip2px(context, -4);
        }
        return getPropertyDiyWh(left, 0, 0, 0);
    }

    protected LinearLayout.LayoutParams getPropertyDiyWh(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(left, top, right, bottom);
        return diyWh;
    }


    protected LinearLayout.LayoutParams getPrivilegeDiyWh(Context context, View view) {
        LinearLayout.LayoutParams layoutParams = getPrivilegeDiyWh(context, view, 2);
        return layoutParams;
    }

    protected LinearLayout.LayoutParams getPrivilegeDiyWh(Context context, View view, int leftMargin) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        int left = DensityUtil.dip2px(context, leftMargin);
        layoutParams.setMargins(left, 0, 0, 0);
        return layoutParams;
    }


    /**
     * 获取规格显示参数
     *
     * @Title: getPropertyDiyWh
     * @Return LinearLayout.LayoutParams 返回类型
     */
    protected LinearLayout.LayoutParams getExtraDiyWh(Context context, boolean isChild) {
        int left = 0;
        if (isChild) {
            left = DensityUtil.dip2px(context, 44);
        } else {
            left = DensityUtil.dip2px(context, 10);
        }
        return getExtraDiyWh(left, 0, 0, 0);
    }

    protected LinearLayout.LayoutParams getExtraDiyWh(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(left, top, right, bottom);
        return diyWh;
    }


    /**
     * 构建数据
     *
     * @param dataList
     * @param tradeVo
     * @param isShowInvalid
     */
    public abstract void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid);

    /**
     * 针对等叫起菜时分组显示
     *
     * @param dataList
     * @param tradeVo
     */
    public void updateGroupSelectData(List<IShopcartItem> dataList, TradeVo tradeVo) {

    }

    /**
     * 针对等叫起菜时分组显示
     *
     * @param dataList
     * @param tradeVo
     * @param isGroupModule 是否是团餐 true：是
     */
    protected void updateGroupSelectData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isGroupModule) {
        initGroupData(dataList, tradeVo);
        //根据菜品类型分组
        buildTypeItem(typeMap, ItemType.TITLE_CATEGORY, true, isGroupModule);
        buildTypeItem(singleTypeMap, ItemType.TITLE_CATEGORY, false, isGroupModule);
    }

    /**
     * 更新分组数据
     *
     * @param dataList
     * @param tradeVo
     */
    public void updateGroupData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        initGroupData(dataList, tradeVo);
        //根据菜品类型分组
        buildTypeItem(typeMap, ItemType.TITLE_ITEM, true, true);
        buildTypeItem(singleTypeMap, ItemType.TITLE_ITEM, false, true);
    }

    protected void initCommonData(TradeVo tradeVo) {
        data.clear();
        this.mAllDishCount = BigDecimal.ZERO;
        deskCount = tradeVo.getDeskCount();
    }

    protected void initGroupData(List<IShopcartItem> dataList, TradeVo tradeVo) {
        initCommonData(tradeVo);
        List<IShopcartItem> shopcartItems = GroupShoppingCartTool.buildTypeMap(dataList);
        for (IShopcartItem shopcartItem : shopcartItems) {
            sumAllDishCount(shopcartItem);
        }
        typeMap = GroupShoppingCartTool.getTypeMap();
        singleTypeMap = GroupShoppingCartTool.getSingleTypeMap();
    }

    /**
     * @param typeMap
     * @param titleType
     * @param isGroup          是否是团餐、还是单点
     * @param isShowGroupTitle 是否显示title：团餐、预点菜时有区别
     */
    protected void buildTypeItem(Map<Long, List<IShopcartItem>> typeMap, ItemType titleType, boolean isGroup, boolean isShowGroupTitle) {
        Iterator<Long> typeIterator = typeMap.keySet().iterator();
        //配菜时
        // 不展示这类title
        if (!typeMap.isEmpty() && !isSlideDish && isShowGroupTitle) {
            DishDataItem item = new DishDataItem(ItemType.TITLE_ITEM);
            item.setEnabled(true);
            item.setNeedTopLine(false);
            if (isGroup) {
                item.setName(context.getResources().getString(R.string.group_dish_label));
            } else {
                item.setName(context.getResources().getString(R.string.group_sigle_dish_label));
            }
            data.add(item);
        }
        int i = 0;
        while (typeIterator.hasNext()) {
            Long typeId = typeIterator.next();
            DishBrandType dishType = DishCache.getDishTypeHolder().get(typeId);
            String name = "";
            if (dishType != null) {
                name = dishType.getName();
            } else {
                name = context.getResources().getString(R.string.dialog_other);
            }
            DishDataItem item = new DishDataItem(titleType);
            item.setCategory(true);
            item.setEnabled(false);
            item.setName(name);
            if (dishType != null)
                item.setDishTypeId(dishType.getId());
            if (i == 0) {
                item.setNeedTopLine(false);
            }
            data.add(item);
            createItems(typeMap.get(typeId), data);
            i++;
        }
    }

    protected abstract void updateTrade(TradeVo tradeVo, boolean isShowUnActive);

    /**
     * @return 返回单菜和套餐外壳列表
     * @Description: 根据购物车菜品生成显示item并保存到data数组
     * @Param @param dataList 菜品
     * @Param @param data 根据菜品生成的数据
     * @Return void 返回类型
     */
    protected List<DishDataItem> createItems(List<IShopcartItem> dataList, ArrayList<DishDataItem> data) {
        if (dataList == null || dataList.size() <= 0) {
            return null;
        }
        List<DishDataItem> comboAndSingleItems = null;
        comboAndSingleItems = new ArrayList<DishDataItem>();
        // 倒叙输出
        for (int i = dataList.size() - 1; i >= 0; i--) {
            IShopcartItem shopCartItem = dataList.get(i);
            DishDataItem item = null;
            // 如果子菜不为空，就算套餐外壳
            if (Utils.isNotEmpty(shopCartItem.getSetmealItems())) {
                item = new DishDataItem(ItemType.COMBO);// 套餐外壳
                item.setBase(shopCartItem);
                item.setItem(shopCartItem);
                if (shopCartItem.getId() == null
                        && shopCartItem instanceof ShopcartItemBase
                        && shopCartItem.getSaleType() != SaleType.WEIGHING
                        && shopCartItem.getInvalidType() != InvalidType.RETURN_QTY
                        && shopCartItem.getInvalidType() != InvalidType.DELETE
                        && !isCardService(shopCartItem) && !isApplet(shopCartItem)
                        ) {
                    item.setCanEditNumber(true);
                } else
                    item.setCanEditNumber(false);
                data.add(item);
                comboAndSingleItems.add(item);

                List<? extends ISetmealShopcartItem> chirldList = shopCartItem.getSetmealItems();
                if (Utils.isNotEmpty(chirldList)) {
                    for (ISetmealShopcartItem chirld : chirldList) {
                        // 套餐字菜
                        createChildItem(shopCartItem, chirld, ItemType.CHILD, null, false);
                    }
                }
                createCardServiceHint(shopCartItem);
                createApplet(shopCartItem);
                createItemUser(shopCartItem);
                // 套餐外壳备注
                createMemoItem(shopCartItem,
                        shopCartItem,
                        data,
                        ItemType.COMBO_MEMO,
                        R.string.order_dish_memo_semicolon);
                createComboPrivilege(shopCartItem);

                // 单菜
            } else {
                item = createSigleItem(shopCartItem, ItemType.SINGLE, null);
                comboAndSingleItems.add(item);
            }
            if (isShowAllDiscount) {
                createGiftCoupon(shopCartItem, shopCartItem, data);
            }
        }
        return comboAndSingleItems;
    }

    /**
     * 套餐外壳折扣
     *
     * @param shopCartItem
     */
    private void createComboPrivilege(IShopcartItem shopCartItem) {
        if (shopCartItem.getPrivilege() != null && shopCartItem.getPrivilege().getStatusFlag() == StatusFlag.VALID) {
            DishDataItem it = new DishDataItem(ItemType.COMBO_DISCOUNT);// 套餐外壳折扣
            it.setItem(shopCartItem);
            it.setBase(shopCartItem);
            createDishItemNameByPrivilege(it, shopCartItem, shopCartItem.getPrivilege());
            BigDecimal amount = shopCartItem.getPrivilege().getPrivilegeAmount();
            if (amount != null) {
                it.setValue(amount.doubleValue());
            }
            if (isShowAllDiscount
                    || (isShowFreeDiscount && (shopCartItem.getPrivilege().getPrivilegeType() == PrivilegeType.FREE || shopCartItem.getPrivilege().getPrivilegeType() == PrivilegeType.GIVE)
                    && shopCartItem.getPrivilege().isValid()
            )) {
                data.add(it);
            }
        }
    }

    /**
     * 创建单菜item
     *
     * @param shopCartItem
     * @param itemType
     * @return
     */
    private DishDataItem createSigleItem(IShopcartItem shopCartItem, ItemType itemType, Long typeId) {
        DishDataItem item = new DishDataItem(ItemType.SINGLE);// 单菜
        item.setBase(shopCartItem);
        item.setItem(shopCartItem);
        item.setDishTypeId(typeId);
        if (shopCartItem.getId() == null
                && shopCartItem instanceof ShopcartItemBase
                && shopCartItem.getSaleType() != SaleType.WEIGHING
                && shopCartItem.getInvalidType() != InvalidType.RETURN_QTY
                && shopCartItem.getInvalidType() != InvalidType.DELETE
                && !isCardService(shopCartItem) && !isApplet(shopCartItem)
                ) {
            item.setCanEditNumber(true);
        } else
            item.setCanEditNumber(false);
        data.add(item);
        createCardServiceHint(shopCartItem);
        createApplet(shopCartItem);
        createPropertiesItem(item);
        createExtraItem(item);
        createItemUser(shopCartItem);

        // 单菜备注
        createMemoItem(shopCartItem,
                shopCartItem,
                data,
                ItemType.SINGLE_MEMO,
                R.string.order_dish_memo_semicolon);

        createSiglePrivilege(shopCartItem, shopCartItem);
        return item;
    }

    private void createSiglePrivilege(IShopcartItem shopCartItem, IShopcartItemBase shopcartItemBase) {
        // 单菜折扣
        if (shopcartItemBase.getPrivilege() != null && shopcartItemBase.getPrivilege().isValid()) {
            DishDataItem it = new DishDataItem(ItemType.SINGLE_DISCOUNT);// 单菜折扣
            it.setBase(shopcartItemBase);
            it.setItem(shopCartItem);
            createDishItemNameByPrivilege(it, shopcartItemBase, shopcartItemBase.getPrivilege());
            BigDecimal amount = shopcartItemBase.getPrivilege().getPrivilegeAmount();
            if (amount != null) {
                it.setValue(amount.doubleValue());
            }
            if (isShowAllDiscount
                    || (isShowFreeDiscount && (shopcartItemBase.getPrivilege().getPrivilegeType() == PrivilegeType.FREE || shopcartItemBase.getPrivilege().getPrivilegeType() == PrivilegeType.GIVE)
                    && shopcartItemBase.getPrivilege().isValid()
            )) {
                data.add(it);
            }
        }
    }

    /**
     * 创建子菜显示item
     *
     * @param shopCartItem
     * @param child
     * @param itemType
     */
    private void createChildItem(IShopcartItem shopCartItem, IShopcartItemBase child, ItemType itemType, Long typeId, boolean isNeedTopLine) {
        DishDataItem it = new DishDataItem(itemType);
        it.setItem(shopCartItem);
        it.setBase(child);
        it.setDishTypeId(typeId);
        it.setNeedTopLine(isNeedTopLine);
        data.add(it);
        createPropertiesItem(it);
        createExtraItem(it);
        createSiglePrivilege(shopCartItem, child);
        // 字菜备注
        createMemoItem(child,
                shopCartItem,
                data,
                ItemType.CHILD_MEMO,
                R.string.order_dish_memo_semicolon);
    }

    /**
     * 创建西餐分类
     *
     * @param dataList
     * @param adapter
     */
    public void updateWestData(List<IShopcartItem> dataList, SuperShopCartAdapter adapter) {
        if (dataList == null || dataList.size() <= 0) {
            return;
        }
        if (DinnerUtils.isMediumStyle()) {
            updateWestByMedium(dataList, adapter);
        } else {
            updateWestByServering(dataList, adapter);
        }
    }

    /**
     * 按中类
     */
    private void updateWestByMedium(List<IShopcartItem> dataList, SuperShopCartAdapter adapter) {
        Map<Long, Map<String, List<IShopcartItemBase>>> singleTypeMap = new LinkedHashMap<>();
        //子菜与外壳的关联关系map
        Map<String, IShopcartItem> childMap = new HashMap<>();
        // 遍历购物车中的菜品，对菜品进行分组
        for (int i = 0; i < dataList.size(); i++) {

            IShopcartItem shopCartItem = dataList.get(i);
            DishDataItem item;
            if (shopCartItem.getStatusFlag() == StatusFlag.INVALID
                    && (shopCartItem.getInvalidType() != InvalidType.SPLIT)
                    && (shopCartItem.getInvalidType() != InvalidType.RETURN_QTY)
                    && (shopCartItem.getInvalidType() != InvalidType.MODIFY_DISH)) {
                continue;
            }

            List<? extends ISetmealShopcartItem> setmealShopcartItems = shopCartItem.getSetmealItems();
            boolean isAdd = false;
            //套餐
            if (shopCartItem.getType() != null && shopCartItem.getType() == DishType.COMBO) {
                item = new DishDataItem(ItemType.COMBO);// 套餐外壳
                item.setBase(shopCartItem);
                item.setItem(shopCartItem);
                //西餐不使用快速编辑数量按钮
//                if (shopCartItem.getId() == null
//                        && shopCartItem.getSaleType() != SaleType.WEIGHING
//                        && shopCartItem.getInvalidType() != InvalidType.RETURN_QTY
//                        && shopCartItem.getInvalidType() != InvalidType.DELETE) {
//                    item.setCanEditNumber(true);
//                } else
                item.setCanEditNumber(false);
                data.add(item);
                // 套餐外壳备注
                createMemoItem(shopCartItem,
                        shopCartItem,
                        data,
                        ItemType.COMBO_MEMO,
                        R.string.order_dish_memo_semicolon);
                createComboPrivilege(shopCartItem);
                isAdd = true;
            }
            //套餐子菜
            if (Utils.isNotEmpty(setmealShopcartItems)) {
                for (ISetmealShopcartItem setmealShopcartItem : setmealShopcartItems) {
                    //全退参生新菜的子菜不显示
                    if (shopCartItem.getStatusFlag() == StatusFlag.VALID && setmealShopcartItem.getStatusFlag() == StatusFlag.INVALID) {
                        continue;
                    }
                    Long typeId = null;
                    DishShop dishShop = setmealShopcartItem.getDishShop();
                    if (dishShop == null) {
                        dishShop = DishCache.getDishHolder().get(setmealShopcartItem.getSkuUuid());
                    }
                    if (dishShop != null) {
                        typeId = dishShop.getDishTypeId();
                    }
                    Map<String, List<IShopcartItemBase>> shopcartItemMap = singleTypeMap.get(typeId);
                    if (shopcartItemMap == null) {
                        shopcartItemMap = new LinkedHashMap<>();
                        singleTypeMap.put(typeId, shopcartItemMap);
                    }
                    childMap.put(setmealShopcartItem.getUuid(), shopCartItem);
                    //同一skuuuid菜品list
                    List<IShopcartItemBase> shopcartItemList = shopcartItemMap.get(setmealShopcartItem.getSkuName());
                    if (shopcartItemList == null) {
                        shopcartItemList = new ArrayList<>();
                        shopcartItemMap.put(setmealShopcartItem.getSkuName(), shopcartItemList);
                    }
                    shopcartItemList.add(setmealShopcartItem);
                    if (adapter != null)
                        adapter.sumWestAllDishCount(setmealShopcartItem, true);
                }
                isAdd = true;
            }
            if (isAdd) {
                continue;
            }
            //累计商品数量
            if (adapter != null)
                adapter.sumWestAllDishCount(shopCartItem, false);
            Long typeId = null;
            DishShop dishShop = shopCartItem.getDishShop();
            if (dishShop == null) {
                dishShop = DishCache.getDishHolder().get(shopCartItem.getSkuUuid());
            }
            if (dishShop != null) {
                typeId = dishShop.getDishTypeId();
            }
            Map<String, List<IShopcartItemBase>> shopcartItemMap = singleTypeMap.get(typeId);
            if (shopcartItemMap == null) {
                shopcartItemMap = new LinkedHashMap<>();
                singleTypeMap.put(typeId, shopcartItemMap);
            }
            //同一skuuuid菜品list
            List<IShopcartItemBase> shopcartItemList = shopcartItemMap.get(shopCartItem.getSkuName());
            if (shopcartItemList == null) {
                shopcartItemList = new ArrayList<>();
                shopcartItemMap.put(shopCartItem.getSkuName(), shopcartItemList);
            }
            shopcartItemList.add(shopCartItem);
        }

        Iterator<Long> typeIterator = singleTypeMap.keySet().iterator();
        List<DishBrandType> typeList = new ArrayList<>();
        while (typeIterator.hasNext()) {
            Long typeId = typeIterator.next();
            DishBrandType dishType = DishCache.getDishTypeHolder().get(typeId);
            typeList.add(dishType);
        }
        Collections.sort(typeList, new DishBrandTypeComparator());
        for (DishBrandType dishType : typeList) {
            String name = "";
            if (dishType != null) {
                name = dishType.getName();
            } else {
                name = context.getResources().getString(R.string.dialog_other);
            }
            DishDataItem item = new DishDataItem(ItemType.TITLE_ITEM);
            item.setCategory(true);
            item.setEnabled(false);
            item.setName(name);
            if (dishType != null)
                item.setDishTypeId(dishType.getId());
            data.add(item);
            createWestItemsByMedium(singleTypeMap, childMap, dishType.getId());
        }
    }

    /**
     * 西餐子菜和单菜显示
     *
     * @param singleTypeMap
     * @param childMap
     * @param typeId
     */
    private void createWestItemsByMedium(Map<Long, Map<String, List<IShopcartItemBase>>> singleTypeMap, Map<String, IShopcartItem> childMap, Long typeId) {
        Map<String, List<IShopcartItemBase>> shopcartItemBaseMap = singleTypeMap.get(typeId);
        if (shopcartItemBaseMap == null) {
            return;
        }
        Iterator iterator = shopcartItemBaseMap.values().iterator();
        while (iterator.hasNext()) {
            List<IShopcartItemBase> shopcartItemBaseList = (List<IShopcartItemBase>) iterator.next();
            for (IShopcartItemBase shopcartItemBase : shopcartItemBaseList) {
                //同一skuUuid的菜品分类
                Map<String, List<IShopcartItemBase>> shopcartMap = new LinkedHashMap<>();
                List<IShopcartItemBase> tShopcartList = shopcartMap.get(shopcartItemBase.getSkuUuid());
                if (tShopcartList == null) {
                    tShopcartList = new ArrayList<IShopcartItemBase>();
                    shopcartMap.put(shopcartItemBase.getSkuUuid(), tShopcartList);
                }
                tShopcartList.add(shopcartItemBase);
                createMediumReally(childMap, typeId, shopcartMap);
            }

        }
    }

    private void createMediumReally(Map<String, IShopcartItem> childMap, Long typeId, Map<String, List<IShopcartItemBase>> shopcartMap) {
        Iterator skuUuidIterator = shopcartMap.keySet().iterator();
        while (skuUuidIterator.hasNext()) {
            List<IShopcartItemBase> sameShopcartItemList = shopcartMap.get(skuUuidIterator.next());
            for (IShopcartItemBase shopcartItemBase : sameShopcartItemList) {
                if (shopcartItemBase instanceof ISetmealShopcartItem) {
                    IShopcartItem comboShopcartItem = childMap.get(shopcartItemBase.getUuid());
                    createChildItem(comboShopcartItem, (ISetmealShopcartItem) shopcartItemBase, ItemType.WEST_CHILD, typeId, true);
                } else {
                    createSigleItem((IShopcartItem) shopcartItemBase, ItemType.SINGLE, typeId);
                }
            }
        }
    }


    /**
     * 按上菜顺序
     */
    private void updateWestByServering(List<IShopcartItem> dataList, SuperShopCartAdapter adapter) {
        Map<Long, List<IShopcartItemBase>> singleTypeMap = new LinkedHashMap<>();
        //子菜与外壳的关联关系map
        Map<String, IShopcartItem> childMap = new LinkedHashMap<>();
        // 遍历购物车中的菜品，对菜品进行分组
        final Long otherId = 10000L;
        for (int i = dataList.size() - 1; i >= 0; i--) {

            IShopcartItem shopCartItem = dataList.get(i);
            DishDataItem item;
            if (shopCartItem.getStatusFlag() == StatusFlag.INVALID
                    && (shopCartItem.getInvalidType() != InvalidType.SPLIT)
                    && (shopCartItem.getInvalidType() != InvalidType.RETURN_QTY)
                    && (shopCartItem.getInvalidType() != InvalidType.MODIFY_DISH)) {
                continue;
            }

            List<? extends ISetmealShopcartItem> setmealShopcartItems = shopCartItem.getSetmealItems();
            boolean isAdd = false;
            //套餐
            if (shopCartItem.getType() != null && shopCartItem.getType() == DishType.COMBO) {
                item = new DishDataItem(ItemType.COMBO);// 套餐外壳
                item.setBase(shopCartItem);
                item.setItem(shopCartItem);
                item.setCanEditNumber(false);
                data.add(item);
                // 套餐外壳备注
                createMemoItem(shopCartItem,
                        shopCartItem,
                        data,
                        ItemType.COMBO_MEMO,
                        R.string.order_dish_memo_semicolon);
                createComboPrivilege(shopCartItem);
                isAdd = true;
            }
            Long sortId = otherId;
            //套餐子菜
            if (Utils.isNotEmpty(setmealShopcartItems)) {
                for (ISetmealShopcartItem setmealShopcartItem : setmealShopcartItems) {
                    //全退参生新菜的子菜不显示
                    if (shopCartItem.getStatusFlag() == StatusFlag.VALID && setmealShopcartItem.getStatusFlag() == StatusFlag.INVALID) {
                        continue;
                    }
                    if (setmealShopcartItem.getTradeItemExtraDinner() != null && setmealShopcartItem.getTradeItemExtraDinner().getServingOrder() != null) {
                        sortId = Long.valueOf(setmealShopcartItem.getTradeItemExtraDinner().getServingOrder());
                    }
                    //为0时没有设置上菜顺序
                    if (sortId == null || sortId == 0) {
                        sortId = otherId;
                    }
                    List<IShopcartItemBase> shopcartItemList = singleTypeMap.get(sortId);
                    if (shopcartItemList == null) {
                        shopcartItemList = new ArrayList<>();
                        singleTypeMap.put(sortId, shopcartItemList);
                    }
                    childMap.put(setmealShopcartItem.getUuid(), shopCartItem);
                    shopcartItemList.add(setmealShopcartItem);
                    if (adapter != null)
                        adapter.sumWestAllDishCount(setmealShopcartItem, true);
                }
                isAdd = true;
            }
            if (isAdd) {
                continue;
            }
            //累计商品数量
            if (adapter != null)
                adapter.sumWestAllDishCount(shopCartItem, false);
            if (shopCartItem.getTradeItemExtraDinner() != null && shopCartItem.getTradeItemExtraDinner().getServingOrder() != null) {
                sortId = Long.valueOf(shopCartItem.getTradeItemExtraDinner().getServingOrder());
            }
            //为0时没有设置上菜顺序
            if (sortId == null || sortId == 0) {
                sortId = otherId;
            }
            List<IShopcartItemBase> shopcartItemList = singleTypeMap.get(sortId);
            if (shopcartItemList == null) {
                shopcartItemList = new ArrayList<>();
                singleTypeMap.put(sortId, shopcartItemList);
            }
            shopcartItemList.add(shopCartItem);
        }

        Iterator<Long> typeIterator = singleTypeMap.keySet().iterator();
        List<Long> sortList = new ArrayList<>();
        while (typeIterator.hasNext()) {
            Long typeId = typeIterator.next();
            sortList.add(typeId);
        }
        Collections.sort(sortList);
        int[] sequenceKey = context.getResources().getIntArray(R.array.dish_sequence_key);
        String[] sequenceValue = context.getResources().getStringArray(R.array.dish_sequence_value);
        //只有一条未设置的title时，不显示未设置
        if (sortList.size() == 1 && sortList.get(0) == otherId) {
            createWestItems(singleTypeMap, childMap, sortList.get(0));
            return;
        }
        for (Long sortId : sortList) {
            String name = "";
            if (sortId == otherId) {
                name = context.getResources().getString(R.string.unsetting);
            } else {
                name = sequenceValue[(sortId.intValue() - 1)];
            }
            DishDataItem item = new DishDataItem(ItemType.TITLE_ITEM);
            item.setCategory(true);
            item.setEnabled(false);
            item.setName(name);
            item.setDishTypeId(sortId);
            data.add(item);
            createWestItems(singleTypeMap, childMap, sortId);
        }
    }

    /**
     * 次卡服提示信息
     *
     * @param shopcartItem
     */
    private void createCardServiceHint(IShopcartItem shopcartItem) {
        if (shopcartItem.getCardServicePrivilgeVo() == null || !shopcartItem.getCardServicePrivilgeVo().isPrivilegeValid()) {
            return;
        }
        String name = shopcartItem.getDishShop().getName();
        BigDecimal count = shopcartItem.getSingleQty();

        String label = context.getResources().getString(R.string.beauty_card_service_label, name, count.intValue());
        DishDataItem item = new DishDataItem(ItemType.CARD_SERVICE_LABEL);
        item.setCategory(true);
        item.setEnabled(false);
        item.setName(label);
        item.setNeedTopLine(false);
        data.add(item);
    }

    /**
     * 创建服务关联的技师、销售
     */
    private void createItemUser(IShopcartItem shopcartItem) {
        if (Utils.isEmpty(shopcartItem.getTradeItemUserList())) {
            return;
        }
        for (TradeUser tradeItemUser : shopcartItem.getTradeItemUserList()) {
            if (tradeItemUser.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }
            DishDataItem item = new DishDataItem(ItemType.ITEM_USER);
            item.setBase(shopcartItem);
            item.setItem(shopcartItem);
            item.setTradeItemUser(tradeItemUser);
            item.setName(TradeUserUtil.getUserName(tradeItemUser));
            item.setNeedTopLine(false);
            data.add(item);
        }
    }

    /**
     * 创建小程序优惠显示
     *
     * @param shopcartItem
     */
    private void createApplet(IShopcartItem shopcartItem) {
        if (shopcartItem.getAppletPrivilegeVo() == null || shopcartItem.getAppletPrivilegeVo().getTradePrivilege() == null) {
            return;
        }

        String label = context.getResources().getString(R.string.beauty_wx_service_label, shopcartItem.getDishShop().getName(), shopcartItem.getSingleQty().intValue());
        TradePrivilege tradePrivilege = shopcartItem.getAppletPrivilegeVo().getTradePrivilege();
        DishDataItem item = new DishDataItem(ItemType.APPLET_LABEL);
        item.setBase(shopcartItem);
        item.setItem(shopcartItem);
        item.setName(label);
        item.setNeedTopLine(false);
        data.add(item);
    }

    /**
     * 西餐子菜和单菜显示
     *
     * @param singleTypeMap
     * @param childMap
     * @param typeId
     */
    private void createWestItems(Map<Long, List<IShopcartItemBase>> singleTypeMap, Map<String, IShopcartItem> childMap, Long typeId) {
        List<IShopcartItemBase> shopcartItemBaseList = singleTypeMap.get(typeId);
        if (Utils.isEmpty(shopcartItemBaseList)) {
            return;
        }
        for (IShopcartItemBase shopcartItemBase : shopcartItemBaseList) {
            if (shopcartItemBase instanceof ISetmealShopcartItem) {
                IShopcartItem comboShopcartItem = childMap.get(shopcartItemBase.getUuid());
                createChildItem(comboShopcartItem, (ISetmealShopcartItem) shopcartItemBase, ItemType.WEST_CHILD, typeId, true);
            } else {
                createSigleItem((IShopcartItem) shopcartItemBase, ItemType.SINGLE, typeId);
            }
        }

    }

    /**
     * @Title: createItemNameByPrivilege
     * @Description: 构造菜品折扣展示名称
     * @Param @param it
     * @Param @param privilege 菜品折扣
     * @Return void 返回类型
     */
    protected void createDishItemNameByPrivilege(DishDataItem it, IShopcartItemBase iShopcartItem, TradePrivilege privilege) {
        if (it == null || privilege == null) {
            return;
        }
        if (privilege.getPrivilegeType() == PrivilegeType.FREE || privilege.getPrivilegeType() == PrivilegeType.GIVE) {// 免单(赠送)

            it.setName(context.getResources().getString(R.string.give));
            if (iShopcartItem.getDiscountReasonRel() != null) {
                String reason = iShopcartItem.getDiscountReasonRel().getReasonContent();
                it.setDiscountReason(context.getResources().getString(R.string.give_reason_label) + reason);
            }
        } else if (privilege.getPrivilegeType() == PrivilegeType.DISCOUNT) {// 折扣

            it.setName(privilege.getPrivilegeName());
            if (iShopcartItem.getDiscountReasonRel() != null) {
                String reason = iShopcartItem.getDiscountReasonRel().getReasonContent();
                it.setDiscountReason(String.format("%s：%s", context.getResources().getString(R.string.order_discount_reason), reason));
            }

        } else if (privilege.getPrivilegeType() == PrivilegeType.REBATE) {// 让价

            it.setName(privilege.getPrivilegeName());
            if (iShopcartItem.getDiscountReasonRel() != null) {
                String reason = iShopcartItem.getDiscountReasonRel().getReasonContent();
                it.setDiscountReason(String.format("%s：%s", context.getResources().getString(R.string.order_concession_reason), reason));
            }
        } else if (privilege.getPrivilegeType() == PrivilegeType.PROBLEM) {
            it.setName(privilege.getPrivilegeName());
            if (iShopcartItem.getDiscountReasonRel() != null) {
                String reason = iShopcartItem.getDiscountReasonRel().getReasonContent();
                it.setDiscountReason(String.format("%s：%s", context.getResources().getString(R.string.order_concession_reason), reason));
            }
        } else if (privilege.getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT) {// 会员折扣
            it.setMemberDiscount(true);
            String privilegeStr = "";
            if (ErpConstants.isSimplifiedChinese()) {
                privilegeStr = privilege.getPrivilegeValue().doubleValue() / 10 + "";
            } else {
                privilegeStr = 100 - privilege.getPrivilegeValue().doubleValue() + "";
            }
            it.setName(context.getResources().getString(R.string.print_auto_discount)
                    + privilegeStr
                    + context.getResources().getString(R.string.discount1));
        } else if (privilege.getPrivilegeType() == PrivilegeType.MEMBER_PRICE) {
            // 会员价
            it.setMemberDiscount(true);
            it.setName(context.getResources().getString(R.string.dinner_memeber_price_label));
        }
    }

    /**
     * 口味、做法、规格属性
     *
     * @param item
     */
    protected void createPropertiesItem(DishDataItem item) {
        IShopcartItemBase shopcartItem = item.getBase();
        if (shopcartItem.getProperties() == null || shopcartItem.getProperties().size() <= 0) {
            return;
        }

        StringBuilder sbST = new StringBuilder("");
        for (IOrderProperty property : shopcartItem.getProperties()) {
            if (property.getPropertyKind() == PropertyKind.PROPERTY) {// 如果口味做法
                DishDataItem dishDataItem = new DishDataItem(ItemType.PROPERTIES);
                dishDataItem.setBase(shopcartItem);
                dishDataItem.setProperty(property);
                dishDataItem.setItem(item.getItem());
                data.add(dishDataItem);
            } else if (property.getPropertyKind() == PropertyKind.STANDARD) {// 如果规格
                sbST.append(property.getPropertyName()).append(STR_COMMA);
            }
        }
        int len = sbST.length();
        // 如果有规格
        if (len > 0) {
            sbST.setLength(len - 1);
            item.setStandText(sbST.toString());
        }
    }

    /**
     * 构建加料item
     *
     * @param item
     */
    protected void createExtraItem(DishDataItem item) {
        IShopcartItemBase shopcartItemBase = item.getBase();
        if (shopcartItemBase.getExtraItems() == null || shopcartItemBase.getExtraItems().size() <= 0) {
            return;
        }
        DishDataItem dishDataItem = new DishDataItem(ItemType.EXTRA_ITEM);
        dishDataItem.setBase(shopcartItemBase);
        dishDataItem.setItem(item.getItem());
        dishDataItem.setNeedTopLine(false);
        data.add(dishDataItem);
    }

    /**
     * 礼品劵数据构建
     */
    protected void createGiftCoupon(IShopcartItemBase shopCartItem, IShopcartItem parentItem, ArrayList<DishDataItem> data) {
        if (shopCartItem.getCouponPrivilegeVo() == null || shopCartItem.getCouponPrivilegeVo().getTradePrivilege() == null || !shopCartItem.getCouponPrivilegeVo().getTradePrivilege().isValid()) {
            return;
        }
        DishDataItem it = new DishDataItem(ItemType.GIFT_COUPON);
        it.setBase(shopCartItem);
        it.setItem(parentItem);
        String couponType = context.getString(R.string.coupon_type_gift);
        it.setName(couponType + ":" + shopCartItem.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeName());

        it.setValue(shopCartItem.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount().doubleValue());

        if (shopCartItem.getCouponPrivilegeVo().isActived()) {
            it.setEnabled(true);
        } else {
            it.setEnabled(false);
        }
        data.add(it);
    }

    /**
     * 显示网络订单的备注
     *
     * @Title: createMemoItem
     * @Param @param shopCartItem
     * @Param @param data
     * @Param @param itemType
     * @Return void 返回类型
     */
    private void createMemoItem(IShopcartItemBase shopCartItem, IShopcartItem parentItem, ArrayList<DishDataItem> data,
                                ItemType itemType, int resId) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(shopCartItem.getMemo())) {
            sb.append(shopCartItem.getMemo());
        }
        List<IOrderProperty> memoList = filterTradeItemProperty(shopCartItem, PropertyKind.MEMO);
        if (memoList != null && !memoList.isEmpty()) {
            int memoSize = memoList.size();
            for (int j = 0; j < memoSize; j++) {
                IOrderProperty orderProperty = memoList.get(j);
                if (j == 0 && !TextUtils.isEmpty(orderProperty.getPropertyName())) {
                    sb.append(",");
                }
                sb.append(orderProperty.getPropertyName());
                if (j < memoSize - 1) {
                    sb.append(",");
                }
            }
        }
        if (!TextUtils.isEmpty(sb.toString())) {
            DishDataItem it2 = new DishDataItem(itemType);
            it2.setBase(shopCartItem);
            it2.setItem(parentItem);
            it2.setName(context.getResources().getString(resId) + sb.toString());
            it2.setNeedTopLine(false);
            data.add(it2);
        }
    }

    /**
     * 过滤指定类型的数据
     *
     * @Title: filterTradeItemProperty
     * @Param @param shopcartItem
     * @Param @param propertyKind 属性类型
     * @Return List<IOrderProperty> 返回类型
     */
    public List<IOrderProperty> filterTradeItemProperty(IShopcartItemBase shopcartItem, PropertyKind propertyKind) {
        List<IOrderProperty> orderProperties = new ArrayList<IOrderProperty>();

        @SuppressWarnings("unchecked")
        List<IOrderProperty> tradeItemPropertyList = (List<IOrderProperty>) shopcartItem.getProperties();
        if (tradeItemPropertyList != null) {
            for (IOrderProperty orderProperty : tradeItemPropertyList) {
                if (orderProperty.getPropertyKind() == propertyKind) {
                    orderProperties.add(orderProperty);
                }
            }
        }

        return orderProperties;
    }

    /**
     * 构建整单备注条目
     *
     * @param tradeVo
     */
    protected void buildTradeMemo(TradeVo tradeVo) {
        if (!TextUtils.isEmpty(tradeVo.getTrade().getTradeMemo())) {
            DishDataItem item = new DishDataItem(ItemType.ALL_MEMO);
            item.setName(context.getResources().getString(R.string.remarkOrder) + tradeVo.getTrade().getTradeMemo());
            item.setNeedTopLine(true);
            data.add(item);
        }
    }

    /**
     * 消费税
     *
     * @param tradeVo
     */
    protected void buildTax(TradeVo tradeVo) {
        if (Utils.isEmpty(tradeVo.getTradeTaxs())) {
            return;
        }

        for (TradeTax tradeTax : tradeVo.getTradeTaxs()) {
            if (!tradeTax.isValid()) {
                continue;
            }
            DishDataItem item = new DishDataItem(ItemType.EXCISE_TAX);
            item.setName(tradeTax.getTaxTypeName());
            if (tradeTax.getTaxAmount() != null)
                item.setValue(tradeTax.getTaxAmount().doubleValue());
            data.add(item);
        }

    }


    /**
     * 构建优惠劵的显示对象
     *
     * @param tradeVo
     * @param isShowUnActive
     */
    protected void buildCoupon(TradeVo tradeVo, boolean isShowUnActive) {
        if (Utils.isEmpty(tradeVo.getCouponPrivilegeVoList())) {
            return;
        }
        for (CouponPrivilegeVo couponPrivilegeVo : tradeVo.getCouponPrivilegeVoList()) {
            if (couponPrivilegeVo != null && couponPrivilegeVo.isValid() && couponPrivilegeVo.getCoupon() != null) {
                DishDataItem item = new DishDataItem(ItemType.COUPONS);
                item.setCouponPrivilegeVo(couponPrivilegeVo);
                if (couponPrivilegeVo.getTradePrivilege().getPrivilegeAmount() != null)
                    item.setValue(couponPrivilegeVo
                            .getTradePrivilege()
                            .getPrivilegeAmount()
                            .doubleValue());

                switch (couponPrivilegeVo.getCoupon().getCouponType()) {

                    case REBATE:// 满减券
                        item.setName(context.getString(R.string.coupon_type_rebate) + STR_SEMICOLON
                                + couponPrivilegeVo.getCoupon().getName());
                        break;

                    case DISCOUNT:// 折扣券
                        item.setName(context.getString(R.string.coupon_type_discount) + STR_SEMICOLON
                                + couponPrivilegeVo.getCoupon().getName());
                        break;
                    case GIFT:// 礼品券
                        item.setName(context.getString(R.string.coupon_type_gift) + STR_SEMICOLON
                                + couponPrivilegeVo.getCoupon().getName());
                        break;
                    case CASH:// 代金券
                        item.setName(context.getString(R.string.coupon_type_cash) + STR_SEMICOLON
                                + couponPrivilegeVo.getCoupon().getName());
                        break;
                    default:
                        break;
                }
                // 优惠劵是否生效
                if (couponPrivilegeVo.isActived()) {
                    item.setEnabled(true);
                }
                // 如果优惠劵有效或者允许显示无效的，加入显示列表
                if (!couponPrivilegeVo.isActived() && isShowUnActive
                        || couponPrivilegeVo.isActived()) {
                    data.add(item);
                }
            }
        }
    }

    /**
     * @Title: buildExtraCharge
     * @Description: 构建服务费
     * @Return void 返回类型
     */
    protected void buildServiceCharge(TradeVo tradeVo) {
        List<TradePrivilege> tradeprivileges = tradeVo.getTradePrivileges();
        if (tradeprivileges != null) {
            for (TradePrivilege tradePrivilege : tradeprivileges) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.SERVICE && tradePrivilege.isValid()) {
                    DishDataItem item = new DishDataItem(ItemType.SERVICE);
                    /*
                    ExtraCharge extraCharge = ExtraManager.getExtraChargeById(tradeVo, tradePrivilege.getPromoId());
                    if (extraCharge == null || extraCharge.getStatusFlag() == StatusFlag.INVALID) {
                        continue;
                    }
                    item.setName(extraCharge.getName());
                    */
                    item.setName(tradePrivilege.getPrivilegeName());
                    double value = tradePrivilege == null ? 0 : tradePrivilege.getPrivilegeAmount().doubleValue();
                    item.setValue(value);
                    //item.setExtraCharge(extraCharge);
                    data.add(item);
                }
            }
        }
    }


    /**
     * @Title: buildExtraCharge
     * @Description: 构建附加费
     * @Return void 返回类型
     */
    protected void buildExtraCharge(TradeVo tradeVo) {
        List<TradePrivilege> tradeprivileges = tradeVo.getTradePrivileges();
        if (tradeprivileges != null) {
            for (TradePrivilege tradePrivilege : tradeprivileges) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && tradePrivilege.isValid()) {
                    DishDataItem item = new DishDataItem(ItemType.ADDITIONAL);
                    // 下个迭代使用这个name
                    // item.setName(tradePrivilege.getSurchargeName());
                    ExtraCharge extraCharge = ExtraManager.getExtraChargeById(tradeVo, tradePrivilege.getPromoId());
                    if (extraCharge == null || extraCharge.getStatusFlag() == StatusFlag.INVALID) {
                        continue;
                    }
                    item.setName(extraCharge.getName());
                    double value = tradePrivilege == null ? 0 : tradePrivilege.getPrivilegeAmount().doubleValue();
                    item.setValue(value);
                    item.setExtraCharge(extraCharge);
                    data.add(item);
                }
            }
        }
    }

    /**
     * 计算超时费
     *
     * @param tradeVo
     */
    protected void buildOutTimeCharge(TradeVo tradeVo) {
        List<TradePrivilege> tradeprivileges = tradeVo.getTradePrivileges();
        if (tradeprivileges != null) {
            for (TradePrivilege tradePrivilege : tradeprivileges) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && tradePrivilege.isValid()) {
                    DishDataItem item = new DishDataItem(ItemType.ADDITIONAL);
                    // 下个迭代使用这个name
                    // item.setName(tradePrivilege.getSurchargeName());
                    ExtraCharge outTimeExtraCharge = ServerSettingCache.getInstance().getmOutTimeRule();
                    if (outTimeExtraCharge == null || outTimeExtraCharge.getStatusFlag() == StatusFlag.INVALID || outTimeExtraCharge.getId().longValue() != tradePrivilege.getPromoId().longValue()) {
                        continue;
                    }
                    item.setName(outTimeExtraCharge.getName());
                    double value = tradePrivilege == null ? 0 : tradePrivilege.getPrivilegeAmount().doubleValue();
                    item.setValue(value);
                    item.setExtraCharge(outTimeExtraCharge);
                    item.setTradePlanActivityUuid(tradePrivilege.getUuid());
                    data.add(item);
                }
            }
        }

    }

    /**
     * 商品数量的view控制显示、隐藏
     */
    public void updateCountView(Context context, TextView allDishCountTV) {
        if (getCount() > 0 && getAllDishCount() != null) {
            String count = MathDecimal.trimZero(getAllDishCount()).toString();
            String allCount = context.getResources().getString(R.string.dinner_order_center_goods_total_amount) + count;
            if (allCount.contains(":")) {
                SpannableStringBuilder builder =
                        new SpannableStringBuilder(allCount);
                builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.text_blue)),
                        allCount.indexOf(":") + 1,
                        allCount.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                allDishCountTV.setVisibility(View.VISIBLE);
                allDishCountTV.setText(builder);
            } else {
                allDishCountTV.setVisibility(View.VISIBLE);
                allDishCountTV.setText(allCount);
            }
        } else {
            allDishCountTV.setText(context.getResources().getString(R.string.dinner_order_center_goods_total_amount) + "0");
        }
    }

    /**
     * @return
     * @Date 2016年4月26日
     * @Description: 获取选中数量
     * @Return boolean
     */
    public int getCheckedNumber() {
        if (data == null || data.size() == 0)
            return 0;
        int number = 0;
        for (DishDataItem item : data) {
            if (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO) {
                if (item.getCheckStatus() == DishDataItem.DishCheckStatus.CHECKED) {
                    number++;
                }
            }

        }
        return number;
    }

    public List<IShopcartItem> getCheckedIShopcartItems() {
        List<IShopcartItem> selectedItems = new ArrayList<IShopcartItem>();
        if (data != null && !data.isEmpty()) {
            for (DishDataItem item : data) {
                if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO)
                        && item.getCheckStatus() == DishDataItem.DishCheckStatus.CHECKED) {
                    selectedItems.add(item.getItem());
                }
            }
        }

        return selectedItems;
    }


    public List<IShopcartItem> getAllIShopcartItems() {
        List<IShopcartItem> shopItems = new ArrayList<IShopcartItem>();
        if (data != null && !data.isEmpty()) {
            for (DishDataItem item : data) {
                if (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO) {
                    shopItems.add(item.getItem());
                }
            }
        }
        return shopItems;
    }

    /**
     * 累计商品数量初始化值
     */
    public void initAllDishCount(BigDecimal count) {
        this.mAllDishCount = count;
    }

    /**
     * 累计商品数量
     */
    public void sumAllDishCount(IShopcartItem shopCartItem) {
        if (shopCartItem == null) {
            return;
        }
        //被退菜不计算数量
        if (shopCartItem.getStatusFlag() == StatusFlag.INVALID
                && (shopCartItem.getInvalidType() == InvalidType.RETURN_QTY
                || shopCartItem.getInvalidType() == InvalidType.MODIFY_DISH)) {
            return;
        }
        if (Utils.isNotEmpty(shopCartItem.getSetmealItems())) {//套餐外壳份数
            this.mAllDishCount = this.mAllDishCount.add(ShopcartItemUtils.getDisplyQty(shopCartItem, deskCount));
        } else {//单菜
            submitSigleCount(shopCartItem);
        }
    }


    /**
     * 累计西餐商品数量
     */
    public void sumWestAllDishCount(IShopcartItemBase shopCartItem, boolean isChild) {
        if (shopCartItem == null) {
            return;
        }
        //被退菜不计算数量
        if (shopCartItem.getStatusFlag() == StatusFlag.INVALID
                && (shopCartItem.getInvalidType() == InvalidType.RETURN_QTY
                || shopCartItem.getInvalidType() == InvalidType.MODIFY_DISH)) {
            return;
        }
        //西餐子菜统计实际的数量
        if (isChild) {
            if (shopCartItem.getSaleType() == SaleType.WEIGHING) {
                this.mAllDishCount = this.mAllDishCount.add(BigDecimal.ONE);
            } else {
                this.mAllDishCount = this.mAllDishCount.add(shopCartItem.getTotalQty());
            }
        } else {
            submitSigleCount(shopCartItem);
        }
    }

    private void submitSigleCount(IShopcartItemBase shopCartItem) {
        if (shopCartItem.getSaleType() != null) {
            if (shopCartItem.getSaleType() == SaleType.WEIGHING) {//称重商品数量不为0算一份
                if (shopCartItem.getSingleQty() != null && shopCartItem.getSingleQty().compareTo(BigDecimal.ZERO) > 0) {
                    this.mAllDishCount = this.mAllDishCount.add(BigDecimal.ONE);
                }
            } else {
                this.mAllDishCount = this.mAllDishCount.add(ShopcartItemUtils.getDisplyQty(shopCartItem, deskCount));
            }
        } else {
            this.mAllDishCount = this.mAllDishCount.add(BigDecimal.ONE);
        }
    }

    public int getItemOfShopchartUuid(String shopcartUuid) {
        for (int i = 0; i < data.size(); i++) {
            DishDataItem item = data.get(i);
            if (item.getBase() != null && item.getBase().getUuid().equals(shopcartUuid)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 获取起卖份数
     *
     * @param shopcartItem
     * @return
     */
    public BigDecimal getIncreaseUnit(IShopcartItemBase shopcartItem) {
        DishShop dishShop = shopcartItem.getDishShop();
        if (dishShop == null) {
            return BigDecimal.ONE;
        }

        BigDecimal increaseUnit = dishShop.getDishIncreaseUnit();
        if (increaseUnit != null && increaseUnit.compareTo(BigDecimal.ZERO) > 0) {
            return increaseUnit;
        } else {
            return BigDecimal.ONE;
        }
    }

    public BigDecimal genStepNum(IShopcartItemBase shopcartItem) {
        DishShop dishShop = shopcartItem.getDishShop();
        if (dishShop == null) {
            return BigDecimal.ONE;
        }

        BigDecimal stepNum = dishShop.getStepNum();
        if (stepNum != null && stepNum.compareTo(BigDecimal.ZERO) > 0) {
            return stepNum;
        } else {
            return BigDecimal.ONE;
        }
    }

    /**
     * @Description: item view holder
     * @Version: 1.0
     * <p>
     * rights reserved.
     */
    public class ViewHolder {

        View topLine;

        View mainLayout;

        ImageView checkButton;

        public RelativeLayout dishView;
        public TextView dish_name;
        //桌号，套餐名
        TextView dish_desc;
        // 催菜提醒
        TextView tv_remind_dish;

        //kds菜品制作状态
        TextView tv_make_status;

        TextView tv_dish_bat_serving; //kds划菜

        public TextView dish_num;

        public NumberEditText dish_edit_num;   //数量编辑器

        TextView dish_price;

        ImageView dish_printstate;// 打印状态

        LinearLayout dishTasteView;// 口味做法


        TextView labelValue;

        LinearLayout slideLayout;//
        ImageView slideStatusIv;// 滑动标识

        TextView slideStatusTv;

        LinearLayout returnDishLL;// 退菜区域

        TextView returnDishReasonTv;// 退菜原因

        TextView returnDishQuantityTv;// 退菜数量

        LinearLayout dishOperateTagLL;

        TextView dishPrepareTv;// 等叫

        TextView dishMakeTv;// 起菜

        TextView dishPrepareCancelTv; //取消等叫

        TextView dishMakeCancelTv;  //取消起菜

        TextView issueTimeTv;//出单时间
        ImageView imgAnchorLeft;//左边箭头
        ImageView imgAnchorRight;//右边箭头

        TextView tvWeighFlag; //称重标志
    }

    class PropertiesHolder {
        TextView propertiesName;//属性名字
    }

    class ExtraHolder {
        LinearLayout dishLabelView;// 加料
        TextView dishLabelName;//加料名字
    }

    class LabelHolder {
        LinearLayout lableView;//title
        TextView labelName;
        View topLine;
    }

    class MemoHolder {
        TextView dish_memo;
        View topLine;
    }

    class SPrivilegeHolder {
        LinearLayout singlePrivilegeView;
        TextView privilegeName;
        TextView privilegeValue;
        ImageView dish_member_img; // 会员价图标
        TextView dish_memo;
    }

    class PrivilegeHolder {
        RelativeLayout privilegeView;
        View topLine;
        TextView dish_name;
        TextView dish_price;
        TextView dish_memo;
    }

    class MarketHolder {
        LinearLayout llMarketActivity;// 营销活动
        TextView tvMarketActivityName;// 营销活动名称
        TextView tvMarketActivityValue;// 营销活动优惠金额
        ImageView ivClose;// 营销活动关闭按钮
    }

    class SeperatorHolder {
        View viewGraySeperator;// 灰色间隔线
    }

    class TitleHolder {
        View backgrund;
        View topLine;
        TextView tvTitle;//title
        TextView tvProperty;
        View llParent;
    }


    class CategroyHolder {
        View topLine;
        TextView tvCategoryName;//title
        TextView tvCategoryCount;
    }

    class BuffetPeopleHolder {
        View topLine;
        TextView tv_name;//title
        TextView tv_count;
        TextView tv_price;
        ImageView iv_icon;
    }

    class BuffetExtraHolder {
        View topLine;
        TextView tv_name;//title
        TextView tv_property;
        TextView tv_price;
        ImageView iv_icon;
        Button btn_Edit;
    }

    class UserHolder {
        //用户信息
        TextView user_info;
        View topLine;
    }
}
