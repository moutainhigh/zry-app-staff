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
import com.zhongmei.yunfu.db.entity.dish.DishTimeChargingRule;
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




public abstract class SuperShopCartAdapter extends BaseAdapter {
    private final static String TAG = SuperShopCartAdapter.class.getSimpleName();
            public static final int DISH_TYPE = 0;
        public static final int LABLE_TYPE = 1;
        public static final int MEMO_TYPE = 2;
        public static final int SIGLE_PRIVILEGE_TYPE = 3;
        public static final int PRIVILEGE_TYPE = 4;
        public static final int MARKET_TYPE = 5;
        public static final int GRAY_SEPERATOR_TYPE = 6;
        public static final int EXTRA_TYPE = 7;
        public static final int PROPERTIES_TYPE = 8;
        public static final int TITLE_TYPE = 9;
        public static final int BUFFET_PEOPLE_TYPE = 10;
        public static final int BUFFET_EXTRA = 11;
        public static final int TITLE_CATEGORY = 12;
        public static final int DISH_WEST_TYPE = 13;
        public static final int ITEM_USER_TYPE = 14;
        public static final int TRADE_USER_TYPE = 15;
        public static final int CARD_SERVICE_LABEL = 16;
        public static final int CARD_SERVICE_ITEM=17;

    protected Boolean isBatchDiscountModle = false;    protected boolean isBatchCoercionModel = false;
    protected Boolean isDiscountAllMode = false;    protected boolean isShowAllDiscount = false;
    protected boolean isShowFreeDiscount = true;
    protected boolean isHasBanquetOrFree = false;
    protected static boolean isDishCheckMode = false;
    final String STR_SEMICOLON = "：";
    final String STR_COMMA = "，";
    protected int IMAGEMARGINRIGHT = 5;
    protected ArrayList<DishDataItem> data = new ArrayList<DishDataItem>(10);     protected Context context;

    protected String min;
    protected String hour;
    protected String day;
    protected Drawable mChildIcon;    protected Drawable mDeleteIcon, mDishUnSaveIcon, mDishUnPrintedIcon, mDishPrintedIcon, mDishPausedIcon, mDIshPrintFailIcon,
            mDishPrintIngIcon, mDishDiscountAllIcon, mDishNoDiscountIcon, mBuffetPeople, mDeposit, mOutTimeFee, mDrawableCategory;

    protected Drawable mLabelUnSaveIcon, mLabelSaveUnprintedIcon, mLabelSavePrintedIcon,mLabelCardIcon,mLabelVipIcon;
        protected Drawable mSplitPritedIcon, mSplitUnPritedIcon;

    protected BigDecimal mAllDishCount = BigDecimal.ZERO;        protected Map<String, TradeItemPlanActivity> tradeItemPlanActivityMap = new HashMap<String, TradeItemPlanActivity>();
        protected boolean isSlideDish = false;
        protected BigDecimal deskCount = BigDecimal.ONE;

    protected Map<Long, List<IShopcartItem>> typeMap = null;

    Map<Long, List<IShopcartItem>> singleTypeMap = null;


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
        this.mLabelCardIcon=context.getResources().getDrawable(R.drawable.icon_card);
        this.mLabelVipIcon=context.getResources().getDrawable(R.drawable.icon_vip);
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


    public boolean isWeight(IShopcartItemBase shopcartItem) {
        return shopcartItem.getSaleType() != null &&
                shopcartItem.getSaleType() == SaleType.WEIGHING;
    }

    public BigDecimal getAllDishCount() {
        return mAllDishCount;
    }


    public Map<Long, List<IShopcartItem>> getTypeMap() {
        return typeMap;
    }


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
            case SERVER_CHILD_ITEM:
                return CARD_SERVICE_ITEM;
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
        return 18;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position < 0 || position >= data.size()) {
            return false;
        }

        DishDataItem item = data.get(position);
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
        CardServerItemsHolder cardServerItemsHolder=null;
        int type = getItemViewType(position);
        switch (type) {
            case CARD_SERVICE_ITEM:
                if (convertView == null || convertView.getTag(R.id.layout_card_server_item) == null) {
                    cardServerItemsHolder = new CardServerItemsHolder();
                    convertView = initcardServerItemsView(LayoutInflater.from(context),cardServerItemsHolder);
                    convertView.setTag(R.id.layout_card_server_item, cardServerItemsHolder);
                } else {
                    cardServerItemsHolder = (CardServerItemsHolder) convertView.getTag(R.id.layout_card_server_item);
                }
                break;
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
                                if (convertView == null || convertView.getTag(R.id.dishLabelView) == null) {
                    extraHolder = new ExtraHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_shopcart_item_dish_extra, null);
                    extraHolder.dishLabelView = (LinearLayout) convertView.findViewById(R.id.dishLabelView);                    extraHolder.dishLabelName = (TextView) convertView.findViewById(R.id.extraLabelName);
                    convertView.setTag(R.id.dishLabelView, extraHolder);
                } else {
                    extraHolder = (ExtraHolder) convertView.getTag(R.id.dishLabelView);
                }
                break;
            case CARD_SERVICE_LABEL:
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
                                if (convertView == null || convertView.getTag(R.id.view_seperator) == null) {
                    seperatorHolder = new SeperatorHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.dinner_dish_shopcart_item_gray_line, null);
                    seperatorHolder.viewGraySeperator = convertView.findViewById(R.id.view_seperator);                    convertView.setTag(R.id.view_seperator, seperatorHolder);
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
            case CARD_SERVICE_ITEM:
                showCardServerItem(cardServerItemsHolder, item, position);
                setTopLine(cardServerItemsHolder.topLine, item, position);
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
            case CARD_SERVICE_LABEL:
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
            case ITEM_USER_TYPE:
                showUser(userHolder, item, false);
                break;
            case TRADE_USER_TYPE:
                showUser(userHolder, item, true);
                break;
        }
        return convertView;
    }


    protected void showUser(UserHolder userHolder, DishDataItem item, boolean isDefine) {
        if (!isDefine) {
            userHolder.user_info.setLayoutParams(getExtraDiyWh(context, true));
            setDrawableLeft(userHolder.user_info,R.drawable.icon_trade_item_user);
            userHolder.user_info.setTextColor(context.getResources().getColor(R.color.remark_text_color));
        } else {
            userHolder.user_info.setLayoutParams(getExtraDiyWh(context, false));
            setDrawableLeft(userHolder.user_info,R.drawable.icon_trade_user);
            userHolder.user_info.setTextColor(context.getResources().getColor(R.color.beauty_color_FC2584));
        }
        if (item.isNeedTopLine()) {
            userHolder.topLine.setVisibility(View.VISIBLE);
        } else {
            userHolder.topLine.setVisibility(View.GONE);
        }
        userHolder.user_info.setText(item.getName());
    }

    private void setDrawableLeft(TextView tv,int iconResId){
        Drawable drawableLeft = context.getResources().getDrawable(iconResId);
        tv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
        tv.setCompoundDrawablePadding(DensityUtil.dip2px(context,5));
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



    protected abstract View loadDishLayout();


    protected abstract View initDishLayout(ViewHolder holder);

    protected View initcardServerItemsView(LayoutInflater inflater,CardServerItemsHolder holder) {
        View contentView=inflater.inflate(R.layout.dinner_shopcart_item_server_items, null);
        holder.topLine=contentView.findViewById(R.id.topline);
        holder.tv_dishName=(TextView) contentView.findViewById(R.id.tv_dish_name);
        return contentView;
    }

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
                holder.dishView = (RelativeLayout) convertView.findViewById(R.id.dishView);
        holder.dish_name = (TextView) convertView.findViewById(R.id.dish_name);
        holder.dish_printstate = (ImageView) convertView.findViewById(R.id.printstateicon);
        holder.dish_num = (TextView) convertView.findViewById(R.id.dish_num);
        holder.dish_edit_num = (NumberEditText) convertView.findViewById(R.id.edt_number);
        holder.dish_price = (TextView) convertView.findViewById(R.id.dish_price);
        holder.tvWeighFlag = (TextView) convertView.findViewById(R.id.tv_weigh_flag);
        holder.dish_desc = (TextView) convertView.findViewById(R.id.dish_name_desc);

    }

        protected void initSlideLayout(ViewHolder holder, View convertView) {
        holder.slideLayout = (LinearLayout) convertView.findViewById(R.id.slide_layout);
        holder.slideStatusIv = (ImageView) convertView.findViewById(R.id.slide_status_iv);
        holder.slideStatusTv = (TextView) convertView.findViewById(R.id.slide_status_tv);
    }

    protected void initDishOtherLayout(ViewHolder holder, View convertView) {
        holder.tv_remind_dish = (TextView) convertView.findViewById(R.id.tv_remind_dish);
        holder.tv_make_status = (TextView) convertView.findViewById(R.id.tv_make_status);
        holder.tv_dish_bat_serving = (TextView) convertView.findViewById(R.id.tv_dish_bat_serving);

                holder.returnDishQuantityTv = (TextView) convertView.findViewById(R.id.return_dish_quantity_tv);
        holder.returnDishReasonTv = (TextView) convertView.findViewById(R.id.return_dish_reason_tv);
        holder.returnDishLL = (LinearLayout) convertView.findViewById(R.id.return_dish_ll);

                holder.dishOperateTagLL = (LinearLayout) convertView.findViewById(R.id.dish_operate_tag_ll);
        holder.dishPrepareTv = (TextView) convertView.findViewById(R.id.dish_prepare_tv);
        holder.dishMakeTv = (TextView) convertView.findViewById(R.id.dish_make_tv);
        holder.dishPrepareCancelTv = (TextView) convertView.findViewById(R.id.dish_prepare_cancel_tv);
        holder.dishMakeCancelTv = (TextView) convertView.findViewById(R.id.dish_make_cancel_tv);
    }

    protected void initTimeView(ViewHolder holder, View convertView) {
                holder.issueTimeTv = (TextView) convertView.findViewById(R.id.issue_time_tv);
    }


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


    protected void showCardServerItem(CardServerItemsHolder holder, final DishDataItem item, int position){
            holder.tv_dishName.setText(item.getName());
            holder.topLine.setVisibility(item.isNeedTopLine()?View.VISIBLE:View.GONE);
    }


    protected void setDishLayoutValue(DishDataItem item, ViewHolder holder) {
        IShopcartItemBase shopcartItem = item.getBase();
        if (shopcartItem != null) {
            holder.dish_name.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
            holder.dish_name.setText(shopcartItem.getSkuName());             BigDecimal qty = BigDecimal.ZERO;
            if (item.getType() == ItemType.WEST_CHILD) {
                qty = shopcartItem.getTotalQty();
            } else {
                qty = ShopcartItemUtils.getDisplyQty(shopcartItem, deskCount);
            }
            holder.dish_num.setText("×" + MathDecimal.toTrimZeroString(qty));            holder.dish_num.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
                        if (shopcartItem.isGroupDish() || item.getType() == ItemType.WEST_CHILD) {
                holder.dish_price.setText("");            } else {
                holder.dish_price.setText(getDisplayPrice(shopcartItem));                holder.dish_price.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
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


    protected void showPropertys(DishDataItem item, PropertiesHolder holder) {
        holder.propertiesName.setText(item.getProperty().getPropertyName());
        holder.propertiesName.setLayoutParams(getPropertyDiyWh(context, true));
    }


    protected void showStand(DishDataItem item, ViewHolder holder) {
        if (TextUtils.isEmpty(item.getStandText())) {
            return;
        }
        holder.dish_name.setText(item.getBase().getSkuName() + "(" + item.getStandText() + ")");
    }



    protected void showExtra(IShopcartItemBase shopcartItem, ExtraHolder holder, boolean isChild) {
        if (shopcartItem != null && shopcartItem.getExtraItems() != null && shopcartItem.getExtraItems().size() > 0) {
            StringBuilder sb = new StringBuilder(context.getResources().getString(R.string.addExtra));
            int i = 0;
            for (IExtraShopcartItem extra : shopcartItem.getExtraItems()) {
                if (i > 0) {
                    sb.append(STR_COMMA);
                }
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


    protected void updateDishCheck(ViewHolder holder, final DishDataItem item) {
        if (!isDishCheckMode || isSlideDish) {
            return;
        }
        if (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO) {
            Drawable checkDrawable = null;
            if (item.getCheckStatus() == DishDataItem.DishCheckStatus.NOT_CHECK) {                checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_nomal);
            } else if (item.getCheckStatus() == DishDataItem.DishCheckStatus.CHECKED) {                checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_selected);
            } else if (item.getCheckStatus() == DishDataItem.DishCheckStatus.INVALIATE_CHECK) {
                checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_cannot_discount);
            }
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(checkDrawable, null, null, null);
            holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
        }

    }


    protected void resetIcon(IShopcartItemBase shopcartItem, ViewHolder holder) {
        if (isSlideDish) {
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
                if (isBatchDiscountModle || isDiscountAllMode) {
            if (isBatchDiscountModle && shopcartItem.isGroupDish()) {
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
                case PAUSE:                    holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishPausedIcon, null, null, null);
                    break;
                case DIRECTLY:                case DIRECTLY_FROM_CLOUD:
                case ISSUING:                case FAILED:                    if (shopcartItem.getId() == null) {
                        holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishUnSaveIcon, null, null, null);
                    } else {
                        holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishUnPrintedIcon, null, null, null);
                    }
                    break;
                case FINISHED:                    holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishPrintedIcon, null, null, null);
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


    protected void setDeleteIcon(DishDataItem item, ViewHolder holder) {
        if (item == null || item.getBase() == null || item.getBase().getStatusFlag() == null) {
            return;
        }
        if ((item.getBase().getStatusFlag() == StatusFlag.INVALID)
                && (item.getBase().getInvalidType() != InvalidType.SPLIT)) {
                        if (item.getType() != ItemType.CHILD && item.getType()!=ItemType.EXTRA_ITEM) {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDeleteIcon, null, null, null);
            }
            setLayoutGray(holder);
        }
    }


    protected void setSplitIcon(IShopcartItemBase itemBase, ViewHolder holder, Drawable drawable) {
        if (itemBase == null || itemBase.getStatusFlag() == null) {
            return;
        }
        if (itemBase.getInvalidType() == InvalidType.SPLIT) {
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
    }


    protected void setGray(ViewHolder holder, DishDataItem dishItem) {
        if (dishItem == null) {
            return;
        }
        IShopcartItem item = dishItem.getItem();
        if (item != null && item.getInvalidType() == InvalidType.SPLIT) {
            holder.mainLayout.setAlpha(0.5f);
        }
    }


    protected void updatePrintState(IShopcartItemBase shopcartItem, ViewHolder holder) {
        if (shopcartItem.getIssueStatus() == null) {
            holder.dish_printstate.setVisibility(View.GONE);
        } else {
            switch (shopcartItem.getIssueStatus()) {
                case PAUSE:                case DIRECTLY:                case DIRECTLY_FROM_CLOUD:
                case FINISHED:                    holder.dish_printstate.setVisibility(View.GONE);
                    break;
                case ISSUING:                    holder.dish_printstate.setVisibility(View.VISIBLE);
                    holder.dish_printstate.setImageDrawable(mDishPrintIngIcon);
                    break;
                case FAILED:                    holder.dish_printstate.setVisibility(View.VISIBLE);
                    holder.dish_printstate.setImageDrawable(mDIshPrintFailIcon);
                    break;
                default:
                    holder.dish_printstate.setVisibility(View.GONE);
                    break;
            }
        }
    }


    protected void setLayoutGray(ViewHolder holder) {
        holder.dish_name.setTextColor(context.getResources().getColor(R.color.dinner_label_unsave));
        holder.dish_name.setCompoundDrawablePadding(5);
        holder.dish_num.setTextColor(context.getResources().getColor(R.color.dinner_label_unsave));
        holder.dish_price.setTextColor(context.getResources().getColor(R.color.dinner_label_unsave));
    }

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
            case LABEL_UNSAVE:                labelDrawable = this.mLabelUnSaveIcon;
                holder.labelName.setTextAppearance(context, R.style.dinnerLabelUnsave);
                break;
            case LABEL_SAVE_UNPRINTED:                labelDrawable = this.mLabelSaveUnprintedIcon;
                holder.labelName.setTextAppearance(context, R.style.dinnerLabelUnPrinted);
                break;
            case LABEL_SAVE_PRINTED:                labelDrawable = this.mLabelSavePrintedIcon;
                holder.labelName.setTextAppearance(context, R.style.dinnerLabelSavePrinted);
                break;
            case CARD_SERVICE_LABEL:
                labelDrawable = this.mLabelCardIcon;
                holder.labelName.setTextAppearance(context, R.style.dinnerCardLabel);
                break;
            case APPLET_LABEL:
                labelDrawable = this.mLabelVipIcon;
                holder.labelName.setTextAppearance(context, R.style.dinnerCardLabel);
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
            holder.dish_memo.setText(item.getName());         } else {
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
            holder.privilegeValue.setText(Utils.formatPrice(item.getValue()));                        if (!TextUtils.isEmpty(item.getDiscountReason())) {
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


    public RelativeLayout.LayoutParams getIsComboDiyWh(Context context) {
        int left = DensityUtil.dip2px(context, 18);
        int topOrBottom = DensityUtil.dip2px(context, 5);
        RelativeLayout.LayoutParams diyWh =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(left, topOrBottom, 0, topOrBottom);
        return diyWh;
    }


    public RelativeLayout.LayoutParams getNoComboDiyWh(Context context) {
        int top = (int) context.getResources().getDimension(R.dimen.dinner_shoppingCard_dishName_margin);
        int bottom = (int) context.getResources().getDimension(R.dimen.dinner_shoppingCard_dishName_margin);
        RelativeLayout.LayoutParams diyWh =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(0, top, 0, bottom);
        return diyWh;
    }


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



    protected LinearLayout.LayoutParams getExtraDiyWh(Context context, boolean isChild) {
        int left = 0;
        int bottom=0;
        int top=0;
        if (isChild) {
            left = DensityUtil.dip2px(context, 44);
        } else {
            left = DensityUtil.dip2px(context, 10);
            top=DensityUtil.dip2px(context, 10);
            bottom=DensityUtil.dip2px(context, 10);

        }
        return getExtraDiyWh(left, top, 0, bottom);
    }

    protected LinearLayout.LayoutParams getExtraDiyWh(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(left, top, right, bottom);
        return diyWh;
    }



    public abstract void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid);


    public void updateGroupSelectData(List<IShopcartItem> dataList, TradeVo tradeVo) {

    }


    protected void updateGroupSelectData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isGroupModule) {
        initGroupData(dataList, tradeVo);
                buildTypeItem(typeMap, ItemType.TITLE_CATEGORY, true, isGroupModule);
        buildTypeItem(singleTypeMap, ItemType.TITLE_CATEGORY, false, isGroupModule);
    }


    public void updateGroupData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        initGroupData(dataList, tradeVo);
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


    protected void buildTypeItem(Map<Long, List<IShopcartItem>> typeMap, ItemType titleType, boolean isGroup, boolean isShowGroupTitle) {
        Iterator<Long> typeIterator = typeMap.keySet().iterator();
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


    private void setChargingRuleInfo(DishDataItem item,IShopcartItem shopcartItem){
        DishTimeChargingRule rule=DishCache.getDishTimeChargingRuleHolder().getRuleByDishId(shopcartItem.getSkuId());
        if(rule==null){
            return;
        }

        StringBuffer ruleInfo=new StringBuffer();
        ruleInfo.append(String.format(context.getResources().getString(R.string.dish_charging_rule),rule.getStartChargingTimes(),rule.getStartChargingPrice(),rule.getChargingUnit(),rule.getChargingPrice()));

        if(rule.getFullUnit()!=null && rule.getFullUnitCharging()!=null){
            ruleInfo.append(String.format(context.getResources().getString(R.string.dish_charging_limit_rule),rule.getFullUnit(),rule.getFullUnitCharging()));
        }

        if(rule.getNoFullUnit()!=null && rule.getNoFullUnitCharging()!=null){
            ruleInfo.append(String.format(context.getResources().getString(R.string.dish_charging_limit_rule2),rule.getNoFullUnit(),rule.getNoFullUnitCharging()));
        }

        item.setChargingRule(ruleInfo.toString().trim());

        if(shopcartItem.getId()==null){
                        item.setServerTime(context.getResources().getString(R.string.dish_no_time));
        }else{
                        Long currentTime=System.currentTimeMillis();
            Long serverMinute=(currentTime-shopcartItem.getServerCreateTime())/(60*1000);
            item.setServerTime(String.format(context.getResources().getString(R.string.dish_server_time),serverMinute));
        }
    }


    protected List<DishDataItem> createItems(List<IShopcartItem> dataList, ArrayList<DishDataItem> data) {
        if (dataList == null || dataList.size() <= 0) {
            return null;
        }
        List<DishDataItem> comboAndSingleItems = null;
        comboAndSingleItems = new ArrayList<DishDataItem>();
                for (int i = dataList.size() - 1; i >= 0; i--) {
            IShopcartItem shopCartItem = dataList.get(i);
            DishDataItem item = null;
                        if (Utils.isNotEmpty(shopCartItem.getSetmealItems()) || isServerDishShop(shopCartItem)) {
                item = new DishDataItem(ItemType.COMBO);                item.setBase(shopCartItem);
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
                setChargingRuleInfo(item,shopCartItem);
                data.add(item);
                comboAndSingleItems.add(item);

                List<? extends ISetmealShopcartItem> chirldList = shopCartItem.getSetmealItems();
                if (Utils.isNotEmpty(chirldList)) {
                    for (ISetmealShopcartItem chirld : chirldList) {
                                                createChildItem(shopCartItem, chirld, ItemType.CHILD, null, false);
                    }
                }
                if(shopCartItem.getType()==DishType.SERVER_COMBO_ALL){
                                        DishDataItem it = new DishDataItem(ItemType.SERVER_CHILD_ITEM);
                    it.setItem(shopCartItem);
                    it.setName("所有商品可用");
                    it.setDishTypeId(null);
                    it.setNeedTopLine(true);
                    this.data.add(it);
                }
                if(shopCartItem.getType()==DishType.SERVER_COMBO_PART && Utils.isNotEmpty(shopCartItem.getServerItems())){
                                        for (ISetmealShopcartItem serverItems:shopCartItem.getServerItems()){
                                                createServerChildItem(shopCartItem, serverItems, ItemType.SERVER_CHILD_ITEM, null, false);
                    }
                }
                createCardServiceHint(shopCartItem);
                createApplet(shopCartItem);
                createItemUser(shopCartItem,shopCartItem);
                                createMemoItem(shopCartItem,
                        shopCartItem,
                        data,
                        ItemType.COMBO_MEMO,
                        R.string.order_dish_memo_semicolon);
                createComboPrivilege(shopCartItem);

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


    private boolean isServerDishShop(IShopcartItem shopCartItem){
        return shopCartItem.getType()==DishType.SERVER_COMBO_PART || shopCartItem.getType()==DishType.SERVER_COMBO_ALL;
    }


    private void createComboPrivilege(IShopcartItem shopCartItem) {
        if (shopCartItem.getPrivilege() != null && shopCartItem.getPrivilege().getStatusFlag() == StatusFlag.VALID) {
            DishDataItem it = new DishDataItem(ItemType.COMBO_DISCOUNT);            it.setItem(shopCartItem);
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


    private DishDataItem createSigleItem(IShopcartItem shopCartItem, ItemType itemType, Long typeId) {
        DishDataItem item = new DishDataItem(ItemType.SINGLE);        item.setBase(shopCartItem);
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
        setChargingRuleInfo(item,shopCartItem);
        data.add(item);
        createCardServiceHint(shopCartItem);
        createApplet(shopCartItem);
        createPropertiesItem(item);
        createExtraItem(item);
        createItemUser(shopCartItem,shopCartItem);

                createMemoItem(shopCartItem,
                shopCartItem,
                data,
                ItemType.SINGLE_MEMO,
                R.string.order_dish_memo_semicolon);

        createSiglePrivilege(shopCartItem, shopCartItem);
        return item;
    }

    private void createSiglePrivilege(IShopcartItem shopCartItem, IShopcartItemBase shopcartItemBase) {
                if (shopcartItemBase.getPrivilege() != null && shopcartItemBase.getPrivilege().isValid()) {
            DishDataItem it = new DishDataItem(ItemType.SINGLE_DISCOUNT);            it.setBase(shopcartItemBase);
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
                createItemUser(child,null);
        createMemoItem(child,
                shopCartItem,
                data,
                ItemType.CHILD_MEMO,
                R.string.order_dish_memo_semicolon);
    }


    private void createServerChildItem(IShopcartItem shopCartItem, IShopcartItemBase child, ItemType itemType, Long typeId, boolean isNeedTopLine) {
        DishDataItem it = new DishDataItem(itemType);
        it.setItem(shopCartItem);
        it.setName(child.getDishShop().getName());
        it.setDishTypeId(typeId);
        it.setNeedTopLine(isNeedTopLine);
        data.add(it);
    }


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


    private void updateWestByMedium(List<IShopcartItem> dataList, SuperShopCartAdapter adapter) {
        Map<Long, Map<String, List<IShopcartItemBase>>> singleTypeMap = new LinkedHashMap<>();
                Map<String, IShopcartItem> childMap = new HashMap<>();
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
                        if (shopCartItem.getType() != null && shopCartItem.getType() == DishType.COMBO) {
                item = new DishDataItem(ItemType.COMBO);                item.setBase(shopCartItem);
                item.setItem(shopCartItem);
                                item.setCanEditNumber(false);
                data.add(item);
                                createMemoItem(shopCartItem,
                        shopCartItem,
                        data,
                        ItemType.COMBO_MEMO,
                        R.string.order_dish_memo_semicolon);
                createComboPrivilege(shopCartItem);
                isAdd = true;
            }
                        if (Utils.isNotEmpty(setmealShopcartItems)) {
                for (ISetmealShopcartItem setmealShopcartItem : setmealShopcartItems) {
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


    private void createWestItemsByMedium(Map<Long, Map<String, List<IShopcartItemBase>>> singleTypeMap, Map<String, IShopcartItem> childMap, Long typeId) {
        Map<String, List<IShopcartItemBase>> shopcartItemBaseMap = singleTypeMap.get(typeId);
        if (shopcartItemBaseMap == null) {
            return;
        }
        Iterator iterator = shopcartItemBaseMap.values().iterator();
        while (iterator.hasNext()) {
            List<IShopcartItemBase> shopcartItemBaseList = (List<IShopcartItemBase>) iterator.next();
            for (IShopcartItemBase shopcartItemBase : shopcartItemBaseList) {
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



    private void updateWestByServering(List<IShopcartItem> dataList, SuperShopCartAdapter adapter) {
        Map<Long, List<IShopcartItemBase>> singleTypeMap = new LinkedHashMap<>();
                Map<String, IShopcartItem> childMap = new LinkedHashMap<>();
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
                        if (shopCartItem.getType() != null && shopCartItem.getType() == DishType.COMBO) {
                item = new DishDataItem(ItemType.COMBO);                item.setBase(shopCartItem);
                item.setItem(shopCartItem);
                item.setCanEditNumber(false);
                data.add(item);
                                createMemoItem(shopCartItem,
                        shopCartItem,
                        data,
                        ItemType.COMBO_MEMO,
                        R.string.order_dish_memo_semicolon);
                createComboPrivilege(shopCartItem);
                isAdd = true;
            }
            Long sortId = otherId;
                        if (Utils.isNotEmpty(setmealShopcartItems)) {
                for (ISetmealShopcartItem setmealShopcartItem : setmealShopcartItems) {
                                        if (shopCartItem.getStatusFlag() == StatusFlag.VALID && setmealShopcartItem.getStatusFlag() == StatusFlag.INVALID) {
                        continue;
                    }
                    if (setmealShopcartItem.getTradeItemExtraDinner() != null && setmealShopcartItem.getTradeItemExtraDinner().getServingOrder() != null) {
                        sortId = Long.valueOf(setmealShopcartItem.getTradeItemExtraDinner().getServingOrder());
                    }
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
                        if (adapter != null)
                adapter.sumWestAllDishCount(shopCartItem, false);
            if (shopCartItem.getTradeItemExtraDinner() != null && shopCartItem.getTradeItemExtraDinner().getServingOrder() != null) {
                sortId = Long.valueOf(shopCartItem.getTradeItemExtraDinner().getServingOrder());
            }
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


    private void createItemUser(IShopcartItemBase shopCartItemBase,IShopcartItem shopcartItem) {
        if (Utils.isEmpty(shopCartItemBase.getTradeItemUserList())) {
            return;
        }
        for (TradeUser tradeItemUser : shopCartItemBase.getTradeItemUserList()) {
            if (tradeItemUser.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }
            DishDataItem item = new DishDataItem(ItemType.ITEM_USER);
            item.setBase(shopCartItemBase);
            item.setItem(shopcartItem);
            item.setTradeItemUser(tradeItemUser);
            item.setName(TradeUserUtil.getUserName(tradeItemUser));
            item.setNeedTopLine(false);
            data.add(item);
        }
    }


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


    protected void createDishItemNameByPrivilege(DishDataItem it, IShopcartItemBase iShopcartItem, TradePrivilege privilege) {
        if (it == null || privilege == null) {
            return;
        }
        if (privilege.getPrivilegeType() == PrivilegeType.FREE || privilege.getPrivilegeType() == PrivilegeType.GIVE) {
            it.setName(context.getResources().getString(R.string.give));
            if (iShopcartItem.getDiscountReasonRel() != null) {
                String reason = iShopcartItem.getDiscountReasonRel().getReasonContent();
                it.setDiscountReason(context.getResources().getString(R.string.give_reason_label) + reason);
            }
        } else if (privilege.getPrivilegeType() == PrivilegeType.DISCOUNT) {
            it.setName(privilege.getPrivilegeName());
            if (iShopcartItem.getDiscountReasonRel() != null) {
                String reason = iShopcartItem.getDiscountReasonRel().getReasonContent();
                it.setDiscountReason(String.format("%s：%s", context.getResources().getString(R.string.order_discount_reason), reason));
            }

        } else if (privilege.getPrivilegeType() == PrivilegeType.REBATE) {
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
        } else if (privilege.getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT) {            it.setMemberDiscount(true);
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
                        it.setMemberDiscount(true);
            it.setName(context.getResources().getString(R.string.dinner_memeber_price_label));
        }else if (privilege.getPrivilegeType() == PrivilegeType.MEMBER_REBATE) {
                        it.setMemberDiscount(true);
            it.setName(context.getResources().getString(R.string.dinner_memeber_rebate_label));
        }
    }


    protected void createPropertiesItem(DishDataItem item) {
        IShopcartItemBase shopcartItem = item.getBase();
        if (shopcartItem.getProperties() == null || shopcartItem.getProperties().size() <= 0) {
            return;
        }

        StringBuilder sbST = new StringBuilder("");
        for (IOrderProperty property : shopcartItem.getProperties()) {
            if (property.getPropertyKind() == PropertyKind.PROPERTY) {                DishDataItem dishDataItem = new DishDataItem(ItemType.PROPERTIES);
                dishDataItem.setBase(shopcartItem);
                dishDataItem.setProperty(property);
                dishDataItem.setItem(item.getItem());
                data.add(dishDataItem);
            } else if (property.getPropertyKind() == PropertyKind.STANDARD) {                sbST.append(property.getPropertyName()).append(STR_COMMA);
            }
        }
        int len = sbST.length();
                if (len > 0) {
            sbST.setLength(len - 1);
            item.setStandText(sbST.toString());
        }
    }


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


    protected void buildTradeMemo(TradeVo tradeVo) {
        if (!TextUtils.isEmpty(tradeVo.getTrade().getTradeMemo())) {
            DishDataItem item = new DishDataItem(ItemType.ALL_MEMO);
            item.setName(context.getResources().getString(R.string.remarkOrder) + tradeVo.getTrade().getTradeMemo());
            item.setNeedTopLine(true);
            data.add(item);
        }
    }


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

                    case REBATE:                        item.setName(context.getString(R.string.coupon_type_rebate) + STR_SEMICOLON
                                + couponPrivilegeVo.getCoupon().getName());
                        break;

                    case DISCOUNT:                        item.setName(context.getString(R.string.coupon_type_discount) + STR_SEMICOLON
                                + couponPrivilegeVo.getCoupon().getName());
                        break;
                    case GIFT:                        item.setName(context.getString(R.string.coupon_type_gift) + STR_SEMICOLON
                                + couponPrivilegeVo.getCoupon().getName());
                        break;
                    case CASH:                        item.setName(context.getString(R.string.coupon_type_cash) + STR_SEMICOLON
                                + couponPrivilegeVo.getCoupon().getName());
                        break;
                    default:
                        break;
                }
                                if (couponPrivilegeVo.isActived()) {
                    item.setEnabled(true);
                }
                                if (!couponPrivilegeVo.isActived() && isShowUnActive
                        || couponPrivilegeVo.isActived()) {
                    data.add(item);
                }
            }
        }
    }


    protected void buildServiceCharge(TradeVo tradeVo) {
        List<TradePrivilege> tradeprivileges = tradeVo.getTradePrivileges();
        if (tradeprivileges != null) {
            for (TradePrivilege tradePrivilege : tradeprivileges) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.SERVICE && tradePrivilege.isValid()) {
                    DishDataItem item = new DishDataItem(ItemType.SERVICE);

                    item.setName(tradePrivilege.getPrivilegeName());
                    double value = tradePrivilege == null ? 0 : tradePrivilege.getPrivilegeAmount().doubleValue();
                    item.setValue(value);
                                        data.add(item);
                }
            }
        }
    }



    protected void buildExtraCharge(TradeVo tradeVo) {
        List<TradePrivilege> tradeprivileges = tradeVo.getTradePrivileges();
        if (tradeprivileges != null) {
            for (TradePrivilege tradePrivilege : tradeprivileges) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && tradePrivilege.isValid()) {
                    DishDataItem item = new DishDataItem(ItemType.ADDITIONAL);
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


    protected void buildOutTimeCharge(TradeVo tradeVo) {
        List<TradePrivilege> tradeprivileges = tradeVo.getTradePrivileges();
        if (tradeprivileges != null) {
            for (TradePrivilege tradePrivilege : tradeprivileges) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && tradePrivilege.isValid()) {
                    DishDataItem item = new DishDataItem(ItemType.ADDITIONAL);
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


    public void initAllDishCount(BigDecimal count) {
        this.mAllDishCount = count;
    }


    public void sumAllDishCount(IShopcartItem shopCartItem) {
        if (shopCartItem == null) {
            return;
        }
                if (shopCartItem.getStatusFlag() == StatusFlag.INVALID
                && (shopCartItem.getInvalidType() == InvalidType.RETURN_QTY
                || shopCartItem.getInvalidType() == InvalidType.MODIFY_DISH)) {
            return;
        }
        if (Utils.isNotEmpty(shopCartItem.getSetmealItems())) {            this.mAllDishCount = this.mAllDishCount.add(ShopcartItemUtils.getDisplyQty(shopCartItem, deskCount));
        } else {            submitSigleCount(shopCartItem);
        }
    }



    public void sumWestAllDishCount(IShopcartItemBase shopCartItem, boolean isChild) {
        if (shopCartItem == null) {
            return;
        }
                if (shopCartItem.getStatusFlag() == StatusFlag.INVALID
                && (shopCartItem.getInvalidType() == InvalidType.RETURN_QTY
                || shopCartItem.getInvalidType() == InvalidType.MODIFY_DISH)) {
            return;
        }
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
            if (shopCartItem.getSaleType() == SaleType.WEIGHING) {                if (shopCartItem.getSingleQty() != null && shopCartItem.getSingleQty().compareTo(BigDecimal.ZERO) > 0) {
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


    public class ViewHolder {

        View topLine;

        View mainLayout;

        ImageView checkButton;

        public RelativeLayout dishView;
        public TextView dish_name;
                TextView dish_desc;
                TextView tv_remind_dish;

                TextView tv_make_status;

        TextView tv_dish_bat_serving;
        public TextView dish_num;

        public NumberEditText dish_edit_num;
        TextView dish_price;

        ImageView dish_printstate;
        LinearLayout dishTasteView;

        TextView labelValue;

        LinearLayout slideLayout;        ImageView slideStatusIv;
        TextView slideStatusTv;

        LinearLayout returnDishLL;
        TextView returnDishReasonTv;
        TextView returnDishQuantityTv;
        LinearLayout dishOperateTagLL;

        TextView dishPrepareTv;
        TextView dishMakeTv;
        TextView dishPrepareCancelTv;
        TextView dishMakeCancelTv;
        TextView issueTimeTv;        ImageView imgAnchorLeft;        ImageView imgAnchorRight;
        TextView tvWeighFlag;

        public RelativeLayout rl_extraInfo;        public TextView tv_serverTimes;        public TextView tv_deadLines;
        public LinearLayout layout_chargingRule;
        public TextView tv_serverTime;
        public TextView tv_chargingRule;
    }

    class PropertiesHolder {
        TextView propertiesName;    }

    class ExtraHolder {
        LinearLayout dishLabelView;        TextView dishLabelName;    }

    class LabelHolder {
        LinearLayout lableView;        TextView labelName;
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
        ImageView dish_member_img;         TextView dish_memo;
    }

    class PrivilegeHolder {
        RelativeLayout privilegeView;
        View topLine;
        TextView dish_name;
        TextView dish_price;
        TextView dish_memo;
    }

    class MarketHolder {
        LinearLayout llMarketActivity;        TextView tvMarketActivityName;        TextView tvMarketActivityValue;        ImageView ivClose;    }

    class SeperatorHolder {
        View viewGraySeperator;    }

    class TitleHolder {
        View backgrund;
        View topLine;
        TextView tvTitle;        TextView tvProperty;
        View llParent;
    }


    class CategroyHolder {
        View topLine;
        TextView tvCategoryName;        TextView tvCategoryCount;
    }

    class BuffetPeopleHolder {
        View topLine;
        TextView tv_name;        TextView tv_count;
        TextView tv_price;
        ImageView iv_icon;
    }

    class BuffetExtraHolder {
        View topLine;
        TextView tv_name;        TextView tv_property;
        TextView tv_price;
        ImageView iv_icon;
        Button btn_Edit;
    }

    class UserHolder {
                TextView user_info;
        View topLine;
    }

    class CardServerItemsHolder{
        TextView tv_dishName;
        View topLine;
    }
}
