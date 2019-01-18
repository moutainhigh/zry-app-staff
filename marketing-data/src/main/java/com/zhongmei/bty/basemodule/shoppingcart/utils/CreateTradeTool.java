package com.zhongmei.bty.basemodule.shoppingcart.utils;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool;
import com.zhongmei.bty.basemodule.orderdish.bean.ExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IOrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyOrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.basemodule.trade.bean.TakeOutInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DomainType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date：2015年7月10日 上午11:27:22
 * @Description: 构建trade对象
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class CreateTradeTool {

    private static final String TAG = CreateTradeTool.class.getSimpleName();

    public static void buildMainTradeVo(TradeVo mTradeVo) {

        Trade mTrade = createTrade();

       /* mTrade.setUuid(SystemUtils.genOnlyIdentifier());

        // 所属领域:1:RESTAURANT:餐饮业
        mTrade.setDomainType(DomainType.RESTAURANT);

        mTrade.setBusinessType(BusinessType.SNACK);
        mTrade.setTradePayForm(TradePayForm.OFFLINE);
        mTrade.setStatusFlag(StatusFlag.VALID);*/

        if (Session.getAuthUser() != null) {
            mTrade.setCreatorId(Session.getAuthUser().getId());
            mTrade.setCreatorName(Session.getAuthUser().getName());
        } else {
            return;
        }

        // mTrade.setLocalUpdateWho(localUpdateWho);
        // mTrade.setLocalUpdateTime(localUpdateTime);
        // mTrade.setServerCreateTime(serverCreateTime)
        // mTrade.setServerUpdateTime(serverUpdateTime);

      /*  // 为true表示此记录已经被修改
        mTrade.setChanged(false);
        // 品牌ID
        mTrade.setBrandIdenty(BaseApplication.sInstance.getShopInfo().getLong(ShopInfo.GROUP_ID_KEY));
        // 门店ID
        mTrade.setShopIdenty(BaseApplication.sInstance.getShopInfo().getLong(ShopInfo.ID_KEY));
        // 设备ID
        mTrade.setDeviceIdenty(SystemUtils.getMacAddress());
        // 营业日期，由服务端生成
        // java.util.Date bizDate;

        // 来源。如店内:1、微信等
        // Long sourceId;
        mTrade.setSource(SourceId.POS);
        // 1:ANDROID 2:IPAD 3:官微 4:商微
        mTrade.setSourceChild(SourceChild.ANDROID);

        // 支付状态：1:UNPAID:未支付
        // 2:PAYING:支付中，微信下单选择了在线支付但实际上未完成支付的 3:PAID:已支付
        mTrade.setTradePayStatus(TradePayStatus.UNPAID);

        // 默认订单类型为内用
        // mTrade.setDeliveryType(DeliveryType.HERE);

        // 各种优惠折扣的减免金额，销货时为负数，退货时为正数
        mTrade.setPrivilegeAmount(new BigDecimal(0));

        // 退货所对应的销货单
        // Long relateTradeId;
        // 原单UUID
        // String relateTradeUuid;

        // 销售金额，明细SALE_AMOUNT之和
        mTrade.setSaleAmount(new BigDecimal(0));

        // 单号
        // String seqNo;

        // 交易金额，等于SALE_AMOUNT与PRIVILEGE_AMOUNT之和
        mTrade.setTradeAmount(new BigDecimal(0));

        // 交易状态：1:UNPROCESSED:未处理 2:TEMPORARY:挂单，不需要厨房打印
        // 3:CONFIRMED:已确认 4:FINISH:已完成(全部支付)
        // 5:RETURNED:已退货 6:INVALID:已作废 7:REFUSED:已拒绝
        mTrade.setTradeStatus(TradeStatus.CONFIRMED);

        // 商品种类，默认传0
        mTrade.setSkuKindCount(0);

        // 交易时间
        // java.util.Date tradeTime;
        mTrade.setTradeTime((new Date()).getTime());

        // 交易类型 1:SELL:售货 2:REFUND:退货
        // com.zhongmei.bty.commonmodule.database.enums.TradeType
        // tradeType;
        mTrade.setTradeType(TradeType.SELL);*/ //mofiyf v8.3

        mTrade.validateCreate();
        mTradeVo.setTrade(mTrade);
        mTradeVo.getTrade().setTradeNo(SystemUtils.getBillNumber());
        mTradeVo.setTradeExtra(new TradeExtra());
    }

    public static Trade createTrade() {
        Trade trade = new Trade();
        trade.setUuid(SystemUtils.genOnlyIdentifier());

        // 所属领域:1:RESTAURANT:餐饮业
        trade.setDomainType(DomainType.RESTAURANT);

        trade.setBusinessType(BusinessType.SNACK);
        trade.setTradePayForm(TradePayForm.OFFLINE);
        trade.setStatusFlag(StatusFlag.VALID);

        if (Session.getAuthUser() != null) {
            trade.setCreatorId(Session.getAuthUser().getId());
            trade.setCreatorName(Session.getAuthUser().getName());
        }

        // trade.setLocalUpdateWho(localUpdateWho);
        // trade.setLocalUpdateTime(localUpdateTime);
        // trade.setServerCreateTime(serverCreateTime)
        // trade.setServerUpdateTime(serverUpdateTime);

        // 为true表示此记录已经被修改
        trade.setChanged(false);
        // 品牌ID
        trade.setBrandIdenty(ShopInfoManager.getInstance().getShopInfo().getBrandId());
        // 门店ID
        trade.setShopIdenty(ShopInfoManager.getInstance().getShopInfo().getShopId());
//        Utils.toLong(ShopInfoCfg.getInstance().shopId)
        // 设备ID
        trade.setDeviceIdenty(SystemUtils.getMacAddress());
        // 营业日期，由服务端生成
        // java.util.Date bizDate;

        // 来源。如店内:1、微信等
        // Long sourceId;
        trade.setSource(SourceId.POS);
        // 1:ANDROID 2:IPAD 3:官微 4:商微
        trade.setSourceChild(SourceChild.ANDROID);

        // 支付状态：1:UNPAID:未支付
        // 2:PAYING:支付中，微信下单选择了在线支付但实际上未完成支付的 3:PAID:已支付
        trade.setTradePayStatus(TradePayStatus.UNPAID);

        // 默认订单类型为内用
        // trade.setDeliveryType(DeliveryType.HERE);

        // 各种优惠折扣的减免金额，销货时为负数，退货时为正数
        trade.setPrivilegeAmount(new BigDecimal(0));

        // 退货所对应的销货单
        // Long relateTradeId;
        // 原单UUID
        // String relateTradeUuid;

        // 销售金额，明细SALE_AMOUNT之和
        trade.setSaleAmount(new BigDecimal(0));

        // 单号
        // String seqNo;

        // 交易金额，等于SALE_AMOUNT与PRIVILEGE_AMOUNT之和
        trade.setTradeAmountBefore(new BigDecimal(0));
        trade.setTradeAmount(new BigDecimal(0));

        // 交易状态：1:UNPROCESSED:未处理 2:TEMPORARY:挂单，不需要厨房打印
        // 3:CONFIRMED:已确认 4:FINISH:已完成(全部支付)
        // 5:RETURNED:已退货 6:INVALID:已作废 7:REFUSED:已拒绝
        trade.setTradeStatus(TradeStatus.CONFIRMED);

        // 商品种类，默认传0
        trade.setDishKindCount(0);

        // 交易时间
        // java.util.Date tradeTime;
        trade.setTradeTime((new Date()).getTime());


        trade.setTradePeopleCount(1);

        // 交易类型 1:SELL:售货 2:REFUND:退货
        // com.zhongmei.bty.commonmodule.database.enums.TradeType
        // tradeType;
        trade.setTradeType(TradeType.SELL);
        return trade;
    }

    /**
     * @Title: dishToTradeItem
     * @Description: 将ShopcartItem转化为TradeItemVo
     * @Param @param mShopcartItemBase
     * @Param @param tradeUuid
     * @Param @return TODO
     * @Return TradeItemVo 返回类型
     */
    public static TradeItemVo dishToTradeItem(ShopcartItemBase mShopcartItemBase, TradeVo mTradeVo, BigDecimal deskCount) {

        TradeItemVo mTradeItemVo = new TradeItemVo();

        // 构建TradeItem
        mTradeItemVo.setTradeItem(buildTradeItem(mShopcartItemBase, mTradeVo, deskCount));
        mTradeItemVo.setTradeItemExtraDinner(mShopcartItemBase.getTradeItemExtraDinner());
        List<TradeItemProperty> tradeItemPropertyList = new ArrayList<TradeItemProperty>();

        // 属性信息
        List<IOrderProperty> listOrderProperty = mShopcartItemBase.getProperties();
        if (listOrderProperty != null) {
            for (IOrderProperty property : listOrderProperty) {
                TradeItemProperty mTradeItemProperty = buildTradeItemProperty(mShopcartItemBase, property, deskCount);
                tradeItemPropertyList.add(mTradeItemProperty);
            }
        }

        // 规格信息
        // Set<DishProperty> listProperty =
        // mShopcartItemBase.getOrderDish().getStandards();
        // if (listProperty != null && listProperty.size() >
        // 0) {
        // for (DishProperty mDishPropertyVo : listProperty)
        // {
        // TradeItemProperty mTradeItemProperty =
        // buildTradeItemProperty(mShopcartItemBase,
        // mDishPropertyVo);
        // tradeItemPropertyList.add(mTradeItemProperty);
        // }
        //
        // }

        mTradeItemVo.setTradeItemPropertyList(tradeItemPropertyList);
        TradePrivilege mTradePrivilege =
                BuildPrivilegeTool.buildPrivilege(mShopcartItemBase, mTradeVo.getTrade().getUuid());
        mTradeItemVo.setTradeItemPrivilege(mTradePrivilege);
        //折扣理由
        if (mShopcartItemBase.getDiscountReasonRel() != null) {
            mTradeItemVo.setDiscountReason(mShopcartItemBase.getDiscountReasonRel());
        }

        CouponPrivilegeVo couponPrivilegeVo = mShopcartItemBase.getCouponPrivilegeVo();
        if (couponPrivilegeVo != null)
            couponPrivilegeVo.setShopcartItem(null);
        mTradeItemVo.setCouponPrivilegeVo(couponPrivilegeVo);
        //更新tradeid和tradeuuid，针对拆单时改变
        if (couponPrivilegeVo != null && couponPrivilegeVo.getTradePrivilege() != null) {
            couponPrivilegeVo.getTradePrivilege().setTradeUuid(mTradeVo.getTrade().getUuid());
            couponPrivilegeVo.getTradePrivilege().setTradeId(mTradeVo.getTrade().getId());
        }
        if (mShopcartItemBase instanceof IShopcartItem) {
            //设置菜品操作记录(只针对单菜和套餐外壳)
            IShopcartItem iShopcartItem = (IShopcartItem) mShopcartItemBase;
            mTradeItemVo.setTradeItemOperations(iShopcartItem.getTradeItemOperations());
        } else if (mShopcartItemBase instanceof ISetmealShopcartItem) {
            ISetmealShopcartItem iShopcartItem = (ISetmealShopcartItem) mShopcartItemBase;
            mTradeItemVo.setTradeItemOperations(iShopcartItem.getTradeItemOperations());
        }
        mTradeItemVo.setShopcartItemType(mShopcartItemBase.getShopcartItemType());
        if (mShopcartItemBase.getTradeItemMainBatchRelList() != null) {
            mTradeItemVo.setTradeItemMainBatchRelList(mShopcartItemBase.getTradeItemMainBatchRelList());
        }
        mTradeItemVo.setTradeItemUserList(mShopcartItemBase.getTradeItemUserList());
        mTradeItemVo.setCardServicePrivilegeVo(mShopcartItemBase.getCardServicePrivilgeVo());
        mTradeItemVo.setAppletPrivilegeVo(mShopcartItemBase.getAppletPrivilegeVo());
        mTradeItemVo.setShopcartItemType(mShopcartItemBase.getShopcartItemType());
        return mTradeItemVo;
    }

    /**
     * @Title: buildTradeItem
     * @Description: 构建TradeItem对象
     * @Param @param mShopcartItem
     * @Param @param tradeUuid
     * @Param @return TODO
     * @Return TradeItem 返回类型
     */
    public static TradeItem buildTradeItem(ShopcartItemBase mShopcartItem, TradeVo mTradeVo, BigDecimal deskCount) {
        TradeItem mTradeItem = new TradeItem();
        mTradeItem.validateCreate();
        if (mShopcartItem.creatorId != null) {//设置mShopcartItem带过来的creator
            mTradeItem.setCreatorId(mShopcartItem.creatorId);
            mTradeItem.setCreatorName(mShopcartItem.creatorName);
        }
        mTradeItem.validateUpdate();
        // 本地生成的唯一标示
        mTradeItem.setUuid(mShopcartItem.getUuid());
        // 数量
        mTradeItem.setQuantity(mShopcartItem.getTotalQty());
        // 单价
        mTradeItem.setPrice(mShopcartItem.getPrice());
        // 金额
        mTradeItem.setAmount(mShopcartItem.getAmount());
        // 各种特征的金额合计
        mTradeItem.setPropertyAmount(mShopcartItem.getPropertyAmount());
        mTradeItem.setFeedsAmount(mShopcartItem.getFeedsAmount());
        // 售价
        mTradeItem.setActualAmount(mShopcartItem.getActualAmount());
        // 父记录Id
        // mTradeItem.setParentId(mShopcartItem.getParentId());
        // 父记录UUID
        mTradeItem.setParentUuid(mShopcartItem.getParentUuid());
        // 商品名称
        mTradeItem.setDishName(mShopcartItem.getSkuName());
        // 商品UUID
        mTradeItem.setSkuUuid(mShopcartItem.getSkuUuid());
        mTradeItem.setDishId(mShopcartItem.getSkuId());
        // 是套餐明细时记录下明细分组ID
        if (mShopcartItem instanceof SetmealShopcartItem) {
            SetmealShopcartItem setmealItem = (SetmealShopcartItem) mShopcartItem;
            mTradeItem.setDishSetmealGroupId(setmealItem.getSetmealGroupId());
        }
        // 排序位
        // mTradeItem.setSort();
        // 交易订单ID
        // mTradeItem.setTradeId();
        // 备注
        mTradeItem.setTradeMemo(mShopcartItem.getMemo());
        mTradeItem.setType(mShopcartItem.getType());

        // 单位名称
        mTradeItem.setUnitName(mShopcartItem.getUnitName());
        // 销售类型
        mTradeItem.setSaleType(mShopcartItem.getSaleType());
        mTradeItem.setTradeUuid(mTradeVo.getTrade().getUuid());
        mTradeItem.setTradeId(mTradeVo.getTrade().getId());
        // 打印状态
        mTradeItem.setIssueStatus(mShopcartItem.getIssueStatus());
        boolean isGroupBusiness = mTradeVo.getTrade().getBusinessType() == BusinessType.GROUP;
        // 设置TradeItem 的桌台信息,团餐团垫菜是多tradeTable，不关联到具体菜上
        if (Utils.isNotEmpty(mTradeVo.getTradeTableList()) && !isGroupBusiness) {
            mTradeItem.setTradeTableUuid(mTradeVo.getTradeTableList().get(0).getUuid());
            mTradeItem.setTradeTableId(mTradeVo.getTradeTableList().get(0).getId());
        }
        // 设置菜品是否参与折扣
        mTradeItem.setEnableWholePrivilege(mShopcartItem.getEnableWholePrivilege());
        // 标记是否是自定义菜品
        mTradeItem.setIsChangePrice(mShopcartItem.getIsChangePrice());

        mTradeItem.setRelateTradeItemId(mShopcartItem.getRelateTradeItemId());
        mTradeItem.setRelateTradeItemUuid(mShopcartItem.getRelateTradeItemUuid());
        mTradeItem.setInvalidType(mShopcartItem.getInvalidType());
        mTradeItem.setStatusFlag(mShopcartItem.getStatusFlag());

        mTradeItem.setBatchNo(getServerExtraInfo(mShopcartItem.getDishShop()));
        doRelateShell(mShopcartItem, mTradeVo, mTradeItem, deskCount);
        return mTradeItem;
    }

    private static String getServerExtraInfo(DishShop dishShop){
        JSONObject obj=new JSONObject();
        try {
            obj.put("timeValue",dishShop.getMinNum());
            obj.put("timeType",dishShop.getMaxNum());
            obj.put("serversCount",dishShop.getSaleTotal());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    /**
     * 关联餐标外壳
     *
     * @param mShopcartItem
     * @param mTradeVo
     * @param tradeItem
     */
    private static void doRelateShell(ShopcartItemBase mShopcartItem, TradeVo mTradeVo, TradeItem tradeItem, BigDecimal deskCount) {
        if (!mShopcartItem.isGroupDish() || mTradeVo.getMealShellVo() == null) {
            return;
        }
        if (TextUtils.isEmpty(tradeItem.getParentUuid())) {
            tradeItem.setParentUuid(mTradeVo.getMealShellVo().getUuid());
            tradeItem.setParentId(mTradeVo.getMealShellVo().getId());
        }
        tradeItem.setActualAmount(BigDecimal.ZERO);
        tradeItem.setAmount(BigDecimal.ZERO);
        tradeItem.setPrice(BigDecimal.ZERO);
        tradeItem.setPropertyAmount(BigDecimal.ZERO);
        BigDecimal quntity = mShopcartItem.getTotalQty().multiply(deskCount);
        tradeItem.setQuantity(quntity);
    }

    /**
     * @Title: buildTradeItemProperty
     * @Description: 构建菜品规格
     * @Param @return TODO
     * @Return TradeItemProperty 返回类型
     */
    public static TradeItemProperty buildTradeItemProperty(ShopcartItemBase mShopcartItemBase,
                                                           DishProperty mDishPropertyVo) {

        // DishProperty mDishProperty =
        // mDishPropertyVo.getProperty();
        // DishPropertyType mDishPropertyType =
        // mDishPropertyVo.getPropertyType();

        TradeItemProperty mTradeItemProperty = new TradeItemProperty();
        // 金额，等于 PRICE * QTY (套餐外壳数量 x 套餐数量)
        BigDecimal price = mDishPropertyVo.getReprice();
        BigDecimal qty = mShopcartItemBase.getTotalQty();
        BigDecimal amount = qty.multiply(price);
        mTradeItemProperty.setAmount(amount);
        mTradeItemProperty.setCreatorId(Session.getAuthUser().getId());
        mTradeItemProperty.setCreatorName(Session.getAuthUser().getName());
        // 单价
        mTradeItemProperty.setPrice(price);
        mTradeItemProperty.setPropertyName(mDishPropertyVo.getName());
        // 该字段后期会去掉，目前占用PropertyType.TASTE
        mTradeItemProperty.setPropertyType(mDishPropertyVo.getPropertyKind());
        mTradeItemProperty.setPropertyUuid(mDishPropertyVo.getUuid());
        mTradeItemProperty.setQuantity(qty);
        // mTradeItemProperty.setTradeItemId();
        mTradeItemProperty.setTradeItemUuid(mShopcartItemBase.getUuid());

        // mTradeItemProperty.setUpdatorId();
        // mTradeItemProperty.setUpdatorName();
        mTradeItemProperty.setUuid(SystemUtils.genOnlyIdentifier());
        mTradeItemProperty.setStatusFlag(StatusFlag.VALID);
        mTradeItemProperty.setUuid(SystemUtils.genOnlyIdentifier());
        mTradeItemProperty.validateCreate();

        return mTradeItemProperty;
    }

    /**
     * @Title: buildTradeItemProperty
     * @Description: 构建菜品属性
     * @Param @param mShopcartItemBase
     * @Param @param mDishPropertyVo
     * @Param @return TODO
     * @Return TradeItemProperty 返回类型
     */
    public static TradeItemProperty buildTradeItemProperty(ShopcartItemBase mShopcartItemBase,
                                                           IOrderProperty mOrderProperty, BigDecimal deskCount) {

        TradeItemProperty mTradeItemProperty = new TradeItemProperty();
        // 金额，等于 PRICE * QTY (套餐外壳数量 x 套餐数量)
        BigDecimal price = mOrderProperty.getPropertyPrice();
        BigDecimal qty = mShopcartItemBase.getTotalQty();
        BigDecimal amount = qty.multiply(price);
        mTradeItemProperty.setAmount(amount);
        // 判断SERVER_ID_KEY是否为空，如果是为则不能进行类型转换并赋值给mTradeItemProperty的CreatoerId

        AuthUser authUser = Session.getAuthUser();
        if (authUser != null) {
            mTradeItemProperty.setCreatorId(authUser.getId());
            mTradeItemProperty.setCreatorName(authUser.getName());
        }

        // 单价
        mTradeItemProperty.setPrice(price);
        mTradeItemProperty.setPropertyName(mOrderProperty.getPropertyName());
        // 该字段后期会去掉，目前占用PropertyType.TASTE
        mTradeItemProperty.setPropertyType(mOrderProperty.getPropertyKind());
        mTradeItemProperty.setPropertyUuid(mOrderProperty.getPropertyUuid());
        mTradeItemProperty.setQuantity(qty);
        // mTradeItemProperty.setTradeItemId();
        mTradeItemProperty.setTradeItemUuid(mShopcartItemBase.getUuid());

        // mTradeItemProperty.setUpdatorId();
        // mTradeItemProperty.setUpdatorName();
        mTradeItemProperty.setUuid(SystemUtils.genOnlyIdentifier());
        mTradeItemProperty.setStatusFlag(StatusFlag.VALID);
        mTradeItemProperty.setUuid(SystemUtils.genOnlyIdentifier());
        mTradeItemProperty.validateCreate();
        doChangeProperty(mShopcartItemBase, mTradeItemProperty, deskCount);
        return mTradeItemProperty;
    }


    /**
     * 改变餐标下菜品属性价格
     *
     * @param mShopcartItem
     * @param mTradeItemProperty
     */
    private static void doChangeProperty(ShopcartItemBase mShopcartItem, TradeItemProperty mTradeItemProperty, BigDecimal deskCount) {
        if (!mShopcartItem.isGroupDish()) {
            return;
        }
        mTradeItemProperty.setAmount(BigDecimal.ZERO);
        mTradeItemProperty.setPrice(BigDecimal.ZERO);
        BigDecimal quntity = mShopcartItem.getSingleQty().multiply(deskCount);
        mTradeItemProperty.setQuantity(quntity);
    }

    /**
     * @Title: createTradeVo
     * @Description: 构建tradeVo
     * @Param @param mTradeVo
     * @Param @param mTakeOutInfo TODO
     * @Return void 返回类型
     */
    public static void createTradeVo(List<ShopcartItem> listShopcart, List<IShopcartItem> readonlyItem,
                                     TradeVo mTradeVo, TakeOutInfo mTakeOutInfo) {

        if (mTradeVo.getTradeItemList() == null) {
            List<TradeItemVo> listTradeItem = new ArrayList<TradeItemVo>();
            mTradeVo.setTradeItemList(listTradeItem);
        } else {
            List<TradeItemVo> listItem = mTradeVo.getTradeItemList();
            for (int i = listItem.size() - 1; i >= 0; i--) {
                TradeItemVo itemVo = listItem.get(i);
                // 该出用于避免重复调用创建
                if (itemVo.getTradeItem().getId() == null) {
                    listItem.remove(i);
                }
            }

        }

        List<TradeItemVo> listTradeItem = mTradeVo.getTradeItemList();

        mTradeVo.setTradeItemExtraList(new ArrayList<TradeItemExtra>());
        BigDecimal deskCount = mTradeVo.getDeskCount();

        addReadonlyTradeItemVo(listTradeItem, readonlyItem, mTradeVo.getOldDeskCount(), deskCount);

        for (TradeItemVo tradeItemVo : listTradeItem) {
            if (tradeItemVo.getTradeItemExtra() != null && tradeItemVo.getTradeItemExtra().getId() != null
                    && tradeItemVo.getTradeItemExtra().getIsPack() == Bool.YES) {
                mTradeVo.getTradeItemExtraList().add(tradeItemVo.getTradeItemExtra());
            }
        }

        boolean isAddItemMode = Utils.isNotEmpty(mTradeVo.getTradeItemList());//当订单里有之前点的菜品，认为是加菜模式
        for (int i = 0; i < listShopcart.size(); i++) {
            ShopcartItem mShopcartItem = listShopcart.get(i);
            TradeItemVo tradeItem = dishToTradeItem(mShopcartItem, mTradeVo, deskCount);
            tradeItem.getTradeItem().setSort(listTradeItem.size());
            //如果是加菜模式，添加加菜标示
            if (isAddItemMode) {
                tradeItem.getTradeItem().setItemSource(2);
            }

            createTradeItemExtra(mShopcartItem, tradeItem.getTradeItem(), mTradeVo);

            // 如果菜品有桌台号则构建tradeItem时设置桌台编号
            if (!TextUtils.isEmpty(mShopcartItem.getTradeTableUuid())) {
                tradeItem.getTradeItem().setTradeTableUuid(mShopcartItem.getTradeTableUuid());
            }
            listTradeItem.add(tradeItem);

            // 套餐外壳、单菜加料
            Collection<? extends ExtraShopcartItem> listExtra = mShopcartItem.getExtraItems();
            if (listExtra != null) {
                for (ExtraShopcartItem extra : listExtra) {
                    TradeItemVo extraTrade = dishToTradeItem(extra, mTradeVo, deskCount);
                    extraTrade.getTradeItem().setSort(listTradeItem.size());
                    if (!TextUtils.isEmpty(mShopcartItem.getTradeTableUuid())) {
                        extraTrade.getTradeItem().setTradeTableUuid(mShopcartItem.getTradeTableUuid());
                    }
                    extraTrade.setShopcartItemType(mShopcartItem.getShopcartItemType());
                    listTradeItem.add(extraTrade);
                }
            }

            // 套餐子菜
            List<SetmealShopcartItem> listSetmeal = mShopcartItem.getSetmealItems();
            if (listSetmeal != null) {
                for (SetmealShopcartItem setmeal : listSetmeal) {
                    TradeItemVo setmealTrade = dishToTradeItem(setmeal, mTradeVo, deskCount);
                    setmealTrade.getTradeItem().setSort(listTradeItem.size());
                    if (!TextUtils.isEmpty(mShopcartItem.getTradeTableUuid())) {
                        setmealTrade.getTradeItem().setTradeTableUuid(mShopcartItem.getTradeTableUuid());
                    }
                    //如果是加菜模式，添加加菜标示
                    if (isAddItemMode) {
                        setmealTrade.getTradeItem().setItemSource(2);
                    }
                    setmealTrade.setShopcartItemType(mShopcartItem.getShopcartItemType());
                    listTradeItem.add(setmealTrade);

                    createTradeItemExtra(setmeal, setmealTrade.getTradeItem(), mTradeVo);

                    // 套餐子菜加料
                    Collection<? extends ExtraShopcartItem> childListExtra = setmeal.getExtraItems();
                    if (childListExtra != null) {
                        for (ExtraShopcartItem extraItem : childListExtra) {
                            TradeItemVo extraTrade = dishToTradeItem(extraItem, mTradeVo, deskCount);
                            extraTrade.getTradeItem().setSort(listTradeItem.size());
                            extraTrade.setShopcartItemType(mShopcartItem.getShopcartItemType());
                            if (!TextUtils.isEmpty(mShopcartItem.getTradeTableUuid())) {
                                extraTrade.getTradeItem().setTradeTableUuid(mShopcartItem.getTradeTableUuid());
                            }
                            listTradeItem.add(extraTrade);
                        }
                    }
                }
            }

        }

        // 如果TradeVo中未构建TradeExtra则构建
        if (mTradeVo.getTradeExtra() == null) {
            TradeExtra mTradeExtra = new TradeExtra();
            mTradeVo.setTradeExtra(mTradeExtra);
            mTradeVo.getTradeExtra().validateCreate();
        } else if (mTradeVo.getTradeExtra() != null && !mTradeVo.getTradeExtra().hasValidateCreate()) {
            mTradeVo.getTradeExtra().validateCreate();
        }
        mTradeVo.getTradeExtra().setTradeId(mTradeVo.getTrade().getId());//add v8.5
        mTradeVo.getTradeExtra().setTradeUuid(mTradeVo.getTrade().getUuid());
        if (mTradeVo.getTradeExtra().getUuid() == null) {
            mTradeVo.getTradeExtra().setUuid(SystemUtils.genOnlyIdentifier());
        }

        // 如果没呼入电话，则将外面客户信息加入到呼入电话信息中，用户下单时创建新客户
        if (mTakeOutInfo != null) {
            List<TradeCustomer> listCustomer = mTradeVo.getTradeCustomerList();
            if (listCustomer != null) {
                Boolean isHadBook = false;
                for (TradeCustomer mCustomer : listCustomer) {
                    if (mCustomer.getCustomerType() == CustomerType.BOOKING) {
                        isHadBook = true;
                    }
                }
                if (!isHadBook) {
                    TradeCustomer mTradeCustomer = createCustomer(mTradeVo.getTrade().getUuid(), mTakeOutInfo);
                    if (mTradeCustomer != null) {
                        listCustomer.add(createCustomer(mTradeVo.getTrade().getUuid(), mTakeOutInfo));
                    }

                }
            } else {
                listCustomer = new ArrayList<TradeCustomer>();
                TradeCustomer mTradeCustomer = createCustomer(mTradeVo.getTrade().getUuid(), mTakeOutInfo);
                if (mTradeCustomer != null) {
                    listCustomer.add(createCustomer(mTradeVo.getTrade().getUuid(), mTakeOutInfo));
                }

            }
            mTradeVo.setTradeCustomerList(listCustomer);
        }

        for (TradeItemVo itemVo : mTradeVo.getTradeItemList()) {
            TradeItem tradeItem = itemVo.getTradeItem();
            tradeItem.setTradeId(mTradeVo.getTrade().getId());
            tradeItem.setTradeUuid(mTradeVo.getTrade().getUuid());
        }
    }

    /**
     * @Title: createTradeItemExtra
     * @Description: 转换TradeItemExtra
     * @Return TradeItemExtra 返回类型
     */

    public static void createTradeItemExtra(ShopcartItemBase mShopcartItemBase, TradeItem tradeItem, TradeVo tradeVo) {
        if (mShopcartItemBase.getPack()) {
            TradeItemExtra tradeItemExtra = new TradeItemExtra();
            tradeItemExtra.setStatusFlag(StatusFlag.VALID);
            tradeItemExtra.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
            tradeItemExtra.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
            tradeItemExtra.setDeviceIdenty(BaseApplication.sInstance.getDeviceIdenty());
            tradeItemExtra.setClientCreateTime(System.currentTimeMillis());
            tradeItemExtra.setClientUpdateTime(System.currentTimeMillis());
            tradeItemExtra.setChanged(true);
            tradeItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
            tradeItemExtra.setTradeItemId(tradeItem.getId());
            tradeItemExtra.setTradeItemUuid(tradeItem.getUuid());
            tradeItemExtra.setIsPack(Bool.YES);
            if (Session.getAuthUser() != null) {
                tradeItemExtra.setCreatorId(Session.getAuthUser().getId());
                tradeItemExtra.setCreatorName(Session.getAuthUser().getName());
                tradeItemExtra.setUpdatorId(Session.getAuthUser().getId());
                tradeItemExtra.setUpdatorName(Session.getAuthUser().getName());
            }
            if (tradeVo.getTradeItemExtraList() == null) {
                List<TradeItemExtra> tradeItemExtras = new ArrayList<TradeItemExtra>();
                tradeItemExtras.add(tradeItemExtra);
                tradeVo.setTradeItemExtraList(tradeItemExtras);
            } else {
                tradeVo.getTradeItemExtraList().add(tradeItemExtra);
            }
        }
    }

    /**
     * @Title: addReturnQTYTradeItemVo
     * @Description: 将只读菜品中已经存在的tradeItem并且在tradeVo中不存在的数据添加到tradeItemList中（如：退菜生成的只读中存在的tradeItem但还未添加到tradeVo的数据添加进去）
     * @Param @param listTradeItem
     * @Param @param readonlyItem TODO
     * @Return void 返回类型
     */
    public static void addReadonlyTradeItemVo(List<TradeItemVo> listTradeItem, List<IShopcartItem> readonlyItem, BigDecimal oldDeskCount, BigDecimal newDeskCount) {
        // 将退回商品中得tradeItem添加到trade中
        Map<String, TradeItemVo> tempTradeItem = new HashMap<String, TradeItemVo>();
        for (TradeItemVo mTradeItemVo : listTradeItem) {
            tempTradeItem.put(mTradeItemVo.getTradeItem().getUuid(), mTradeItemVo);
        }
        if (readonlyItem != null) {
            for (IShopcartItem mIShopcartItem : readonlyItem) {

                corretTradeItemQuantity(mIShopcartItem, oldDeskCount, newDeskCount);

                TradeItemVo mTradeItemVo = tempTradeItem.get(mIShopcartItem.getUuid());
                if (mTradeItemVo != null) {
                    //设置菜品理由
                    addReasons2TradeItemVo(mTradeItemVo, mIShopcartItem);
                    mTradeItemVo.setShopcartItemType(mIShopcartItem.getShopcartItemType());
                    mTradeItemVo.getTradeItem().setChanged(mIShopcartItem.isChanged());
                    //子菜
                    if (Utils.isNotEmpty(mIShopcartItem.getSetmealItems())) {
                        for (ISetmealShopcartItem setmealShopcartItem : mIShopcartItem.getSetmealItems()) {
                            TradeItemVo setmealItemVo = tempTradeItem.get(setmealShopcartItem.getUuid());
                            if (setmealItemVo != null) {
                                addReasons2TradeItemVo(setmealItemVo, setmealShopcartItem);
                            }
                        }
                    }
                } else if (mIShopcartItem instanceof ReadonlyShopcartItemBase) {
                    // 将退回菜品生成的新只读菜品转换为tradeItemVo
                    ReadonlyShopcartItemBase mReadonlyShopcartItemBase = (ReadonlyShopcartItemBase) mIShopcartItem;
                    TradeItemVo tradeItemVo = buildReadOnlyTradeItemVo(mReadonlyShopcartItemBase);
                    tradeItemVo.setShopcartItemType(mReadonlyShopcartItemBase.getShopcartItemType());
                    listTradeItem.add(tradeItemVo);

                    // 如果是套餐则需要将子菜对应的tradeItem也添加到tradeVo中
                    List<? extends ISetmealShopcartItem> listSetmeal = mIShopcartItem.getSetmealItems();
                    if (listSetmeal != null) {
                        for (ISetmealShopcartItem item : listSetmeal) {
                            ReadonlyShopcartItemBase readonlySetmeal = (ReadonlyShopcartItemBase) item;
                            if (item instanceof ReadonlyShopcartItemBase) {
                                TradeItemVo setmealItemVo = buildReadOnlyTradeItemVo(readonlySetmeal);
                                //子菜的类型和外壳一致
                                setmealItemVo.setShopcartItemType(mIShopcartItem.getShopcartItemType());
                                listTradeItem.add(setmealItemVo);
                            }

                            // 加料
                            Collection<ReadonlyExtraShopcartItem> listExtra = readonlySetmeal.getExtraItems();
                            if (listExtra != null) {
                                for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                                    TradeItemVo extraItemVo = buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem);
                                    extraItemVo.setShopcartItemType(mIShopcartItem.getShopcartItemType());
                                    listTradeItem.add(extraItemVo);
                                }
                            }

                        }
                    }

                    // 加料
                    Collection<ReadonlyExtraShopcartItem> listExtra = mReadonlyShopcartItemBase.getExtraItems();
                    if (listExtra != null) {
                        for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                            TradeItemVo extraItemVo = buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem);
                            extraItemVo.setShopcartItemType(mReadonlyShopcartItemBase.getShopcartItemType());
                            listTradeItem.add(extraItemVo);
                        }
                    }

                }
            }
        }
    }

    /**
     * 拷贝菜品理由到tradeItemVo中
     *
     * @param tradeItemVo
     * @param iShopcartItem
     */
    private static void addReasons2TradeItemVo(TradeItemVo tradeItemVo, IShopcartItemBase iShopcartItem) {
        // 将退菜理由数据信息添加到原单的tradeItem中
        if (tradeItemVo != null) {
            if (iShopcartItem.getReturnQtyReason() != null) {
                tradeItemVo.setRejectQtyReason(iShopcartItem.getReturnQtyReason());
            }
            if (iShopcartItem.getDiscountReasonRel() != null) {
                TradeReasonRel tradeReasonRel = tradeItemVo.getReason(iShopcartItem.getDiscountReasonRel().getOperateType());
                if (tradeReasonRel != null) {
                    tradeReasonRel.setReasonContent(iShopcartItem.getDiscountReasonRel().getReasonContent());
                    tradeReasonRel.setReasonId(iShopcartItem.getDiscountReasonRel().getReasonId());
                }
            }

            tradeItemVo.setCouponPrivilegeVo(iShopcartItem.getCouponPrivilegeVo());
            if (iShopcartItem.getCouponPrivilegeVo() != null && iShopcartItem.getCouponPrivilegeVo().getTradePrivilege() != null) {
                iShopcartItem.getCouponPrivilegeVo().getTradePrivilege().setTradeUuid(tradeItemVo.getTradeItem().getTradeUuid());
                iShopcartItem.getCouponPrivilegeVo().getTradePrivilege().setTradeId(tradeItemVo.getTradeItem().getId());
            }

            //设置菜品操作记录
            tradeItemVo.setTradeItemOperations(iShopcartItem.getTradeItemOperations());
            tradeItemVo.setTradeItemExtraDinner(iShopcartItem.getTradeItemExtraDinner());
            tradeItemVo.setTradeItemUserList(iShopcartItem.getTradeItemUserList());
            tradeItemVo.setCardServicePrivilegeVo(iShopcartItem.getCardServicePrivilgeVo());
            tradeItemVo.setAppletPrivilegeVo(iShopcartItem.getAppletPrivilegeVo());
        }
    }

    /**
     * 团餐桌台数变化涉及菜品数量加减
     *
     * @param mIShopcartItem
     * @param oldDeskCount   改桌之前的桌数
     * @param newDeskCount   改桌之后的桌数
     */
    private static void corretTradeItemQuantity(IShopcartItem mIShopcartItem, BigDecimal oldDeskCount, BigDecimal newDeskCount) {
        if (mIShopcartItem instanceof ReadonlyShopcartItemBase) {
            ReadonlyShopcartItemBase mReadonlyShopcartItemBase = (ReadonlyShopcartItemBase) mIShopcartItem;
            if (oldDeskCount == null || oldDeskCount.compareTo(newDeskCount) == 0 || !mIShopcartItem.isGroupDish()) {
                return;
            }
            //只有分组的菜品才调整数量
            if (mReadonlyShopcartItemBase.isGroupDish()) {
                corretTradeItemQuantity(mReadonlyShopcartItemBase.tradeItem, oldDeskCount, newDeskCount);
                corretTradeItemPropertyQuantity(mReadonlyShopcartItemBase, oldDeskCount, newDeskCount);
            }
            List<? extends ISetmealShopcartItem> listSetmeal = mIShopcartItem.getSetmealItems();
            if (listSetmeal != null) {
                for (ISetmealShopcartItem item : listSetmeal) {
                    ReadonlyShopcartItemBase readonlySetmeal = (ReadonlyShopcartItemBase) item;
                    if (item instanceof ReadonlyShopcartItemBase) {
                        TradeItemVo setmealItemVo = buildReadOnlyTradeItemVo(readonlySetmeal);
                        if (item.isGroupDish()) {
                            corretTradeItemQuantity(setmealItemVo.getTradeItem(), oldDeskCount, newDeskCount);
                            corretTradeItemPropertyQuantity(readonlySetmeal, oldDeskCount, newDeskCount);
                        }
                    }

                    // 加料
                    Collection<ReadonlyExtraShopcartItem> listExtra = readonlySetmeal.getExtraItems();
                    if (listExtra != null) {
                        for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                            TradeItemVo extraItemVo = buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem);
                            if (mReadonlyExtraShopcartItem.isGroupDish())
                                corretTradeItemQuantity(extraItemVo.getTradeItem(), oldDeskCount, newDeskCount);
                        }
                    }
                }
            }

            // 加料
            Collection<ReadonlyExtraShopcartItem> listExtra = mReadonlyShopcartItemBase.getExtraItems();
            if (listExtra != null) {
                for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                    TradeItemVo extraItemVo = buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem);
                    if (mReadonlyExtraShopcartItem.isGroupDish())
                        corretTradeItemQuantity(extraItemVo.getTradeItem(), oldDeskCount, newDeskCount);
                }
            }
        }
    }

    private static void corretTradeItemQuantity(TradeItem tradeItem, BigDecimal oldDeskCount, BigDecimal newDeskCount) {
        //只纠正有效的数据
        if (tradeItem.getStatusFlag() == StatusFlag.INVALID) {
            return;
        }
        BigDecimal newQuantitiy = newDeskCount.multiply(tradeItem.getQuantity()).divide(oldDeskCount);
        tradeItem.setQuantity(newQuantitiy);
        tradeItem.validateUpdate();
    }

    /**
     * 纠正属性数量
     *
     * @param oldDeskCount
     * @param newDeskCount
     */
    private static void corretTradeItemPropertyQuantity(ReadonlyShopcartItemBase mReadonlyShopcartItemBase, BigDecimal oldDeskCount, BigDecimal newDeskCount) {
        List<ReadonlyOrderProperty> listReadOnlyProperty = mReadonlyShopcartItemBase.getProperties();
        if (Utils.isEmpty(listReadOnlyProperty)) {
            return;
        }
        for (ReadonlyOrderProperty orderProperty : listReadOnlyProperty) {
            BigDecimal newQuantitiy = newDeskCount.multiply(orderProperty.tradeItemProperty.getQuantity()).divide(oldDeskCount);
            orderProperty.tradeItemProperty.setQuantity(newQuantitiy);
            orderProperty.tradeItemProperty.validateUpdate();
        }
    }

    /**
     * @Title: buildReadOnlyTradeItemVo
     * @Description: 退菜生成的只读菜品构建TradeItemVo
     * @Param @param shopcartItem
     * @Param @return TODO
     * @Return TradeItemVo 返回类型
     */
    public static TradeItemVo buildReadOnlyTradeItemVo(ReadonlyShopcartItemBase shopcartItem) {

        TradeItemVo tradeItemVo = new TradeItemVo();
        TradeItem mTradeItem = shopcartItem.tradeItem;
        tradeItemVo.setTradeItem(mTradeItem);
        tradeItemVo.setTradeItemPrivilege(shopcartItem.getPrivilege());
        tradeItemVo.setTradeItemExtra(shopcartItem.getTradeItemExtra());
        tradeItemVo.setCouponPrivilegeVo(shopcartItem.getCouponPrivilegeVo());
        tradeItemVo.setTradeItemExtraDinner(shopcartItem.getTradeItemExtraDinner());

        //拷贝菜品折扣的理由
        if (shopcartItem.getDiscountReasonRel() != null) {
            tradeItemVo.setDiscountReason(shopcartItem.getDiscountReasonRel());
        }

        List<ReadonlyOrderProperty> listReadOnlyProperty = shopcartItem.getProperties();
        List<TradeItemProperty> tradeItemPropertyList = new ArrayList<TradeItemProperty>();
        if (listReadOnlyProperty != null) {
            for (ReadonlyOrderProperty mReadonlyOrderProperty : listReadOnlyProperty) {
                tradeItemPropertyList.add(mReadonlyOrderProperty.tradeItemProperty);
            }
        }

        tradeItemVo.setTradeItemPropertyList(tradeItemPropertyList);
        tradeItemVo.setTradeItemUserList(shopcartItem.getTradeItemUserList());
        tradeItemVo.setCardServicePrivilegeVo(shopcartItem.getCardServicePrivilgeVo());
        tradeItemVo.setAppletPrivilegeVo(shopcartItem.getAppletPrivilegeVo());
        return tradeItemVo;
    }

    public static TradeCustomer createCustomer(String tradeUUid, TakeOutInfo entity) {
        TradeCustomer mCustomer = new TradeCustomer();
        mCustomer.setCustomerType(CustomerType.BOOKING);
        mCustomer.setCustomerId(entity.getCustomerID());
        mCustomer.setCustomerName(entity.getReceiverName());
        mCustomer.setCustomerSex(entity.getReceiverSex());
        mCustomer.setCustomerPhone(entity.getReceiverTel());
        mCustomer.setCreatorId(Session.getAuthUser().getId());
        mCustomer.setCreatorName(Session.getAuthUser().getName());
        mCustomer.setUuid(SystemUtils.genOnlyIdentifier());
        mCustomer.setTradeUuid(tradeUUid);
        mCustomer.validateCreate();
        return mCustomer;

    }

    /**
     * 将 listShopcartItem 中的优惠复制到 mTradeVo中，使用
     * tradeItem.relateTradeItemUuid进行对比
     *
     * @Title: updateTradeItemPrivilge
     * @Description: 将IshopcartItem中的优惠信息赋给TradeVo中的TradeItem
     * @Param @param mTradeVo
     * @Param @param listShopcartItem TODO
     * @Return void 返回类型
     */
    public static void updateTradeItemPrivilgeOfRelate(TradeVo mTradeVo, List<IShopcartItem> listShopcartItem) {

        for (TradeItemVo tradeItemVo : mTradeVo.getTradeItemList()) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            TradePrivilege oldTradePrivilege = tradeItemVo.getTradeItemPrivilege();
            TradeReasonRel oldTradeReasonRel = tradeItemVo.getReasonLast2(); //.getDiscountReason();
            for (IShopcartItem shopcartItem : listShopcartItem) {
                if (shopcartItem.getUuid() != null
                        && shopcartItem.getUuid().equals(tradeItem.getUuid())) {

                    //新单有
                    TradeReasonRel newTradeReasonRel = shopcartItem.getDiscountReasonRel();
                    if (shopcartItem.getInvalidType() == InvalidType.RETURN_QTY) {
                        newTradeReasonRel = shopcartItem.getReturnQtyReason();
                    }
                    if (newTradeReasonRel != null) {
                        if (oldTradeReasonRel != null) {
                            oldTradeReasonRel.setStatusFlag(newTradeReasonRel.getStatusFlag());
                            oldTradeReasonRel.setReasonContent(newTradeReasonRel.getReasonContent());
                            oldTradeReasonRel.setReasonId(newTradeReasonRel.getReasonId());
                            oldTradeReasonRel.validateUpdate();
                        } else {
                            newTradeReasonRel.setRelateId(tradeItem.getId());
                            newTradeReasonRel.setRelateUuid(tradeItem.getUuid());
                            tradeItemVo.setDiscountReason(newTradeReasonRel);
                        }
                    } else {
                        //新单没有，原单有 把原单的置为无效
                        if (oldTradeReasonRel != null) {
                            tradeItemVo.removeTradeReasonRel(oldTradeReasonRel, oldTradeReasonRel.getOperateType());
                        }
                    }

                    //处理礼品劵
//                    if (shopcartItem.getCouponPrivilegeVo() != null && shopcartItem.getCouponPrivilegeVo().getTradePrivilege() != null) {
//                        if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null) {
//                            TradePrivilege newTradePrivilege = shopcartItem.getCouponPrivilegeVo().getTradePrivilege();
//                            TradePrivilege mTradePrivilege = cloneTradePrivilege(newTradePrivilege);
//                            TradePrivilege oldPrivilege = tradeItemVo.getCouponPrivilegeVo().getTradePrivilege();
//                            mTradePrivilege.setCouponId(oldPrivilege.getId());
//                            oldPrivilege.setChanged(true);
//                        } else {
//                            tradeItemVo.setCouponPrivilegeVo(shopcartItem.getCouponPrivilegeVo());
//                        }
//                    } else {
//                        if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null) {
//                            tradeItemVo.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
//                            tradeItemVo.getCouponPrivilegeVo().getTradePrivilege().setChanged(true);
//                        }
//                    }

                    // 新单有
                    if (shopcartItem.getPrivilege() != null) {
                        TradePrivilege mTradePrivilege = cloneTradePrivilege(shopcartItem.getPrivilege());
                        mTradePrivilege.setTradeId(tradeItem.getTradeId());
                        mTradePrivilege.setTradeUuid(tradeItem.getTradeUuid());
                        mTradePrivilege.setTradeItemId(tradeItem.getId());
                        mTradePrivilege.setTradeItemUuid(tradeItem.getUuid());
                        if (oldTradePrivilege != null) {
                            mTradePrivilege.setId(oldTradePrivilege.getId());
                            mTradePrivilege.setUuid(oldTradePrivilege.getUuid());
                            mTradePrivilege.setServerCreateTime(oldTradePrivilege.getServerCreateTime());
                            mTradePrivilege.setServerUpdateTime(oldTradePrivilege.getServerUpdateTime());
                        }
                        mTradePrivilege.setChanged(true);
                        tradeItemVo.setTradeItemPrivilege(mTradePrivilege);
                        break;

                    } else {
                        // 新单沒有原单有
                        if (oldTradePrivilege != null) {
                            if (tradeItem.getId() == null) {
                                tradeItemVo.setTradeItemPrivilege(null);
                                break;
                            }
                            TradePrivilege tradePrivilege = cloneTradePrivilege(oldTradePrivilege);
                            tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                            tradePrivilege.setChanged(true);
                            tradeItemVo.setTradeItemPrivilege(tradePrivilege);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static TradePrivilege cloneTradePrivilege(TradePrivilege theRef) {
        if (theRef == null) {
            return null;
        }
        TradePrivilege theNew = new TradePrivilege();
        try {
            Beans.copyProperties(theRef, theNew);
        } catch (Exception e) {
            Log.e(TAG, "Copy error!", e);
        }
        return theNew;
    }

    /**
     * 创建桌位号对象
     *
     * @param shopcartItem
     * @param tableSeat
     * @return
     */
    public static TradeItemExtraDinner buildTradeItemExtraDinner(IShopcartItem shopcartItem, TableSeat tableSeat) {
        if (tableSeat == null) {
            return null;
        }
        TradeItemExtraDinner tradeItemExtraDinner = new TradeItemExtraDinner();
        tradeItemExtraDinner.setStatusFlag(StatusFlag.VALID);
        tradeItemExtraDinner.setChanged(true);
        tradeItemExtraDinner.setTradeItemId(shopcartItem.getId());
        tradeItemExtraDinner.setTradeItemUuid(shopcartItem.getUuid());
        tradeItemExtraDinner.setSeatId(tableSeat.getId());
        tradeItemExtraDinner.setSeatNumber(tableSeat.getSeatName());
        tradeItemExtraDinner.setClientCreateTime(System.currentTimeMillis());
        tradeItemExtraDinner.setClientUpdateTime(System.currentTimeMillis());
        tradeItemExtraDinner.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        tradeItemExtraDinner.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        tradeItemExtraDinner.setCreatorId(Session.getAuthUser().getId());
        tradeItemExtraDinner.setCreatorName(Session.getAuthUser().getName());
        tradeItemExtraDinner.setUpdatorId(Session.getAuthUser().getId());
        tradeItemExtraDinner.setUpdatorName(Session.getAuthUser().getName());
        return tradeItemExtraDinner;
    }

}
