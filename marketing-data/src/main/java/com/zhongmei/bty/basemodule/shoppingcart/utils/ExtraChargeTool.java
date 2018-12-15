package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.discount.enums.ExtraChargeCalcWay;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.commonmodule.database.enums.TradeInitConfigKeyId;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtraChargeTool {

    private static int EXTRACHARGEYES = 1;
    private static int EXTRACHARGENO = 2;
    private static int EXTRACHARGESERVICE = 3;

    private TradeVo mTradeVo;
    private Map<Integer, BigDecimal> privilegeMap = new HashMap<Integer, BigDecimal>();
    private TradePrivilege serviceTradePrivilege; //服务费
    private ExtraCharge serviceExtraCharge; //服务费规则

    private ExtraChargeTool(TradeVo mTradeVo) {
        this.mTradeVo = mTradeVo;
    }

    public static ExtraChargeTool countExtraCharge(TradeVo mTradeVo, List<IShopcartItem> iShopcartItem, BigDecimal privilageAfaterAmount) {
        ExtraChargeTool extraChargeTool = new ExtraChargeTool(mTradeVo);
        extraChargeTool.mathExtraCharge(mTradeVo, iShopcartItem, privilageAfaterAmount);
        return extraChargeTool;
    }

    public Map<Integer, BigDecimal> getPrivilegeMap() {
        return privilegeMap;
    }

    public TradePrivilege getServiceTradePrivilege() {
        return serviceTradePrivilege;
    }

    /**
     * 计算服务费
     *
     * @param saleAmount          折扣前金额
     * @param discountAfterAmount 折扣后金额
     * @return
     */
    public BigDecimal chargeService(BigDecimal saleAmount, BigDecimal discountAfterAmount) {
        if (serviceTradePrivilege != null) {
            TradePrivilege mTradePrivilege;
            if (serviceExtraCharge.isDiscountAfter()) {
                mTradePrivilege = BuildPrivilegeTool.buildExtraChargePrivilege(mTradeVo,
                        serviceTradePrivilege,
                        serviceExtraCharge, discountAfterAmount);
                privilegeMap.put(EXTRACHARGESERVICE, mTradePrivilege.getPrivilegeAmount());
            } else {
                mTradePrivilege = BuildPrivilegeTool.buildExtraChargePrivilege(mTradeVo,
                        serviceTradePrivilege,
                        serviceExtraCharge, saleAmount);
            }

            privilegeMap.put(EXTRACHARGESERVICE, mTradePrivilege.getPrivilegeAmount());
        }
        return getChargeService();
    }

    public BigDecimal getChargeService() {
        return getChargePrivilege(EXTRACHARGESERVICE);
    }

    public BigDecimal getExtraChargeYes() {
        return getChargePrivilege(EXTRACHARGEYES);
    }

    public BigDecimal getExtraChargeNo() {
        return getChargePrivilege(EXTRACHARGENO);
    }

    public BigDecimal getChargePrivilege(int key) {
        BigDecimal result = privilegeMap.get(key);
        return result == null ? BigDecimal.ZERO : result;
    }

    /**
     * @Title: mathExtraCharge
     * @Description: 计算附加费
     * @Param mTradeVo
     * @Param @return 返回参与整单折扣的附加费金额
     * @Return BigDecimal 返回类型
     */
    public void mathExtraCharge(TradeVo mTradeVo, List<IShopcartItem> iShopcartItem, BigDecimal privilageAfaterAmount) {

        List<TradePrivilege> listPrivilege = mTradeVo.getTradePrivileges();

        Map<Long, TradePrivilege> temPrivileges = new HashMap<Long, TradePrivilege>();

        Map<Long, ExtraCharge> extraChargeMap = mTradeVo.getExtraChargeMap();

        if (extraChargeMap == null) {
            extraChargeMap = new HashMap<Long, ExtraCharge>();
        }

        if (listPrivilege == null) {
            mTradeVo.setTradePrivileges(new ArrayList<TradePrivilege>());
            listPrivilege = mTradeVo.getTradePrivileges();
        }
        //end

        // 参与整单折扣的附加费总金额
        BigDecimal extraChageYes = BigDecimal.ZERO;

        // 附加费总金额
        BigDecimal extraChageNo = BigDecimal.ZERO;

        if (listPrivilege != null) {

            // 先移除所有附加费
            for (int i = listPrivilege.size() - 1; i >= 0; i--) {

                TradePrivilege mTradePrivilege = listPrivilege.get(i);
                // 该折扣信息是附加费折扣信息
                if ((mTradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL
                        || mTradePrivilege.getPrivilegeType() == PrivilegeType.SERVICE)
                        && mTradePrivilege.getStatusFlag() == StatusFlag.VALID) {
                    temPrivileges.put(mTradePrivilege.getPromoId(), mTradePrivilege);
                    if (mTradeVo.getMinconExtraCharge() != null && MathDecimal.isLongEqual(mTradePrivilege.getPromoId(), mTradeVo.getMinconExtraCharge().getId())) {
                        continue;
                    }
                    boolean outTimeFeeEnable = ServerSettingCache.getInstance().getBuffetOutTimeFeeEnable();
                    ExtraCharge outFee = ServerSettingCache.getInstance().getmOutTimeRule();
                    if (outTimeFeeEnable && outFee != null && MathDecimal.isLongEqual(mTradePrivilege.getPromoId(), outFee.getId())) {
                        continue;
                    }
                    //服务端附加费被作废,作废tradePrivilege记录
                    if (mTradePrivilege.getStatusFlag() == StatusFlag.VALID && extraChargeMap.get(mTradePrivilege.getPromoId()) == null) {
                        if (!isServiceCharge(mTradeVo, mTradePrivilege)) {
                            mTradePrivilege.setInValid();
                        }
                    }
                }

            }

        }

        //添加服务费
        addServiceCharge(mTradeVo, temPrivileges, listPrivilege);

        // 添加所有附加费(服务费除外)，其中temPrivileges记录已加入过的附加费，这种只需修改附加费金额
        for (Long key : extraChargeMap.keySet()) {

            ExtraCharge mExtraCharge = extraChargeMap.get(key);

            if (mExtraCharge.getCode() != null && mExtraCharge.getCode().equals(ExtraManager.mealFee)) {
                TradePrivilege oldPrivilege = temPrivileges.get(mExtraCharge.getId());
                TradePrivilege boxTradePrivilege = BuildPrivilegeTool.buildBoxFee(mTradeVo, oldPrivilege, mExtraCharge);
                boxTradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);
                Map<String, IShopcartItem> temp = new HashMap<String, IShopcartItem>();
                for (IShopcartItem item : iShopcartItem) {
                    /**
                     * 如果item.getDishShop()为null表示下单后对已经参与附加费的菜品在后台进行了删除操作，然后在重新加入到购物车中是拿不到dishshop数据的。但这类商品是不需要重新计算餐盒费的，直接已经计算过了
                     */
                    if (item.getDishShop() != null && item.getDishShop().getBoxQty() != null && item.getDishShop().getBoxQty().compareTo(BigDecimal.ZERO) != 0) {
                        if (temp.get(item.getDishShop().getUuid()) == null) {
                            mathMealFee(mTradeVo, iShopcartItem, boxTradePrivilege, item, mExtraCharge);
                        }
                        temp.put(item.getDishShop().getUuid(), item);
                    }
                }
                //是否参与折扣 1：是 2：否
                if (mExtraCharge.getDiscountFlag() == Bool.YES) {
                    extraChageYes = extraChageYes.add(boxTradePrivilege.getPrivilegeAmount());
                } else {
                    extraChageNo = extraChageNo.add(boxTradePrivilege.getPrivilegeAmount());
                }
                if (boxTradePrivilege.getId() == null && oldPrivilege == null) {
                    listPrivilege.add(boxTradePrivilege);
                }
            } else {
                // 其中temPrivileges.get(mExtraCharge.getId())也可能返回null，返回null表示该种附加费是新增附加费
                TradePrivilege oldPrivilege = temPrivileges.get(mExtraCharge.getId());
                //这儿不处理最低消费
                if (ExtraManager.isMinConsum(mExtraCharge))
                    continue;
                //服务费，服务费不参与整单折扣，只计算商品金额
                TradePrivilege mTradePrivilege = null;
                if (!isServiceCharge(mTradeVo, mExtraCharge)) {
                    mTradePrivilege = BuildPrivilegeTool.buildExtraChargePrivilege(mTradeVo,
                            oldPrivilege,
                            mExtraCharge, privilageAfaterAmount);
                    // 是否参与折扣 1：是 2：否
                    if (mExtraCharge.getDiscountFlag() == Bool.YES) {

                        extraChageYes = extraChageYes.add(mTradePrivilege.getPrivilegeAmount());

                    } else {

                        extraChageNo = extraChageNo.add(mTradePrivilege.getPrivilegeAmount());

                    }
                }

                if (mTradePrivilege != null && mTradePrivilege.getId() == null && oldPrivilege == null) {
                    listPrivilege.add(mTradePrivilege);
                }
            }

        }


        privilegeMap.put(EXTRACHARGEYES, extraChageYes);

        privilegeMap.put(EXTRACHARGENO, extraChageNo);

        //计算超时费
        privilegeMap = mathOutTimeExtraCharge(mTradeVo, privilegeMap);

        mTradeVo.setDiscountExtracharge(extraChageYes);
    }

    private void addServiceCharge(TradeVo mTradeVo, Map<Long, TradePrivilege> temPrivileges, List<TradePrivilege> resultPrivilegeList) {
        //快餐不使用服务费
        if (mTradeVo.getTrade().getBusinessType() == BusinessType.SNACK || mTradeVo.getTrade().getBusinessType() == BusinessType.TAKEAWAY) {
            return;
        }

        ExtraCharge.ServiceCharge serviceCharge = mTradeVo.getTradeInitConfig(TradeInitConfigKeyId.SERVICE_CHARGE_RATE, ExtraCharge.ServiceCharge.class);
        if (serviceCharge != null) {
            ExtraCharge mExtraCharge = new ExtraCharge();
            mExtraCharge.setId(serviceCharge.id);
            mExtraCharge.setName(serviceCharge.name);
            mExtraCharge.setCalcWay(ValueEnums.toEnum(ExtraChargeCalcWay.class, serviceCharge.calcWay));
            mExtraCharge.setContent(serviceCharge.feeRate);
            mExtraCharge.setPrivilegeFlag(serviceCharge.getPrivilegeFlag());

            TradePrivilege oldPrivilege = temPrivileges.get(mExtraCharge.getId());

            TradePrivilege mTradePrivilege = BuildPrivilegeTool.buildExtraChargePrivilege(mTradeVo,
                    oldPrivilege,
                    mExtraCharge, mTradeVo.getTrade().getSaleAmount());
            mTradePrivilege.setPrivilegeType(PrivilegeType.SERVICE);
            privilegeMap.put(EXTRACHARGESERVICE, mTradePrivilege.getPrivilegeAmount());
            serviceTradePrivilege = mTradePrivilege;
            serviceExtraCharge = mExtraCharge;

            if (mTradePrivilege.getId() == null && oldPrivilege == null) {
                resultPrivilegeList.add(mTradePrivilege);
            }
        }
    }

    /**
     * 是否是服务费,快餐和外卖不使用服务费
     *
     * @param mExtraCharge
     * @return
     */
    public static boolean isServiceCharge(TradeVo tradeVo, ExtraCharge mExtraCharge) {
        if (mExtraCharge != null && mExtraCharge.getCode() != null && mExtraCharge.getCode().equalsIgnoreCase(ExtraManager.SERVICE_CONSUM) /*&& mExtraCharge.isAutoJoinTrade()*/) {
            return true;
        }
        return false;
    }

    public static boolean isServiceCharge(TradeVo mTradeVo, TradePrivilege mTradePrivilege) {
        ExtraCharge.ServiceCharge serviceCharge = mTradeVo.getTradeInitConfig(TradeInitConfigKeyId.SERVICE_CHARGE_RATE, ExtraCharge.ServiceCharge.class);
        return serviceCharge != null && Utils.equals(serviceCharge.id, mTradePrivilege.getPromoId());
    }

    /**
     * 超时费计算
     *
     * @param mTradeVo
     * @param extraChargePrivilegeMap
     * @return
     */
    public static Map<Integer, BigDecimal> mathOutTimeExtraCharge(TradeVo mTradeVo, Map<Integer, BigDecimal> extraChargePrivilegeMap) {
        boolean outTimeEnable = ServerSettingCache.getInstance().getBuffetOutTimeFeeEnable();

        if (!outTimeEnable) {
            return extraChargePrivilegeMap;
        }


        ExtraCharge mOutTimeRule = ServerSettingCache.getInstance().getmOutTimeRule();

        // 参与整单折扣的附加费总金额
        BigDecimal extraChageYes = extraChargePrivilegeMap.get(EXTRACHARGEYES);

        // 附加费总金额
        BigDecimal extraChageNo = extraChargePrivilegeMap.get(EXTRACHARGENO);

        List<TradePrivilege> listPrivilege = mTradeVo.getTradePrivileges();

        for (TradePrivilege outTimePrivilege : listPrivilege) {
            if (outTimePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && outTimePrivilege.getPromoId() != null && outTimePrivilege.getPromoId().longValue() == mOutTimeRule.getId().longValue()) {
                // 是否参与折扣 1：是 2：否
                if (mOutTimeRule.getDiscountFlag() == Bool.YES) {
                    extraChageYes = extraChageYes.add(outTimePrivilege.getPrivilegeAmount());
                } else {
                    extraChageNo = extraChageNo.add(outTimePrivilege.getPrivilegeAmount());
                }
            }
        }

        extraChargePrivilegeMap.put(EXTRACHARGENO, extraChageNo);
        extraChargePrivilegeMap.put(EXTRACHARGEYES, extraChageYes);

        return extraChargePrivilegeMap;
    }

    /**
     * @Title: mathMealFee
     * @Description: 计算餐盒费
     * @Param mTradeVo
     * @Param mTradePrivilege
     * @Param item TODO
     * @Return void 返回类型
     */
    public static void mathMealFee(TradeVo tradeVo, List<IShopcartItem> iShopcartItem, TradePrivilege mTradePrivilege, IShopcartItem item, ExtraCharge mExtraCharge) {
        //一个点了多少个菜
        BigDecimal totalQty = BigDecimal.ZERO;
        DeliveryType deliveryType = tradeVo.getTrade().getDeliveryType();
        if (iShopcartItem != null) {
            for (IShopcartItem mIShopcartItem : iShopcartItem) {
                if (mIShopcartItem.getSkuUuid().equals(item.getSkuUuid()) && mIShopcartItem.getStatusFlag() == StatusFlag.VALID
                        && (((deliveryType == DeliveryType.HERE || deliveryType == DeliveryType.TAKE) && mIShopcartItem.getPack())
                        || (deliveryType == DeliveryType.CARRY || deliveryType == DeliveryType.SEND || tradeVo.getIsSalesReturn()))) {//内用、自取有打包标记才计算、外带外送无单退直接计算
                    totalQty = totalQty.add(mIShopcartItem.getTotalQty());
                }
            }
        }
        //需要餐盒的份数
        BigDecimal part = BigDecimal.ONE;
        //一组需配置的餐盒数量
        BigDecimal boxQty = item.getDishShop().getBoxQty();
        //多少个菜配置一个餐盒
        BigDecimal dishQtyp = item.getDishShop().getDishQty();


        //单菜数量大于配置一组餐盒费要求的菜品数据
        if (totalQty.compareTo(dishQtyp) > 0) {
            //取余
            BigDecimal remainder = totalQty.divideAndRemainder(dishQtyp)[1];
            //整除
            BigDecimal count = totalQty.divideToIntegralValue(dishQtyp);
            //如count>0表示当前所点菜品数量满足搭配多次次餐盒,
            if (remainder.compareTo(BigDecimal.ZERO) == 0) {
                part = count;
            } else {
                part = count.add(BigDecimal.ONE);
            }
        }
        if (totalQty.compareTo(BigDecimal.ZERO) == 0) {
            part = BigDecimal.ZERO;
        }
        boxQty = boxQty.multiply(part);

        if (mExtraCharge != null) {
            BigDecimal amount = mTradePrivilege.getPrivilegeAmount();
            BigDecimal boxfee = boxQty.multiply(new BigDecimal(mExtraCharge.getContent()));
            amount = amount.add(boxfee);
            mTradePrivilege.setPrivilegeAmount(amount);
        }

    }
}
