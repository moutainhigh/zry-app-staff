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
    private TradePrivilege serviceTradePrivilege;     private ExtraCharge serviceExtraCharge;
    private ExtraChargeTool(TradeVo mTradeVo) {
        this.mTradeVo = mTradeVo;
    }

    public static ExtraChargeTool countExtraCharge(TradeVo mTradeVo, List<IShopcartItem> iShopcartItem, BigDecimal privilegeAfaterAmount) {
        ExtraChargeTool extraChargeTool = new ExtraChargeTool(mTradeVo);
        extraChargeTool.mathExtraCharge(mTradeVo, iShopcartItem, privilegeAfaterAmount);
        return extraChargeTool;
    }

    public Map<Integer, BigDecimal> getPrivilegeMap() {
        return privilegeMap;
    }

    public TradePrivilege getServiceTradePrivilege() {
        return serviceTradePrivilege;
    }


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


    public void mathExtraCharge(TradeVo mTradeVo, List<IShopcartItem> iShopcartItem, BigDecimal privilegeAfaterAmount) {

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

                BigDecimal extraChageYes = BigDecimal.ZERO;

                BigDecimal extraChageNo = BigDecimal.ZERO;

        if (listPrivilege != null) {

                        for (int i = listPrivilege.size() - 1; i >= 0; i--) {

                TradePrivilege mTradePrivilege = listPrivilege.get(i);
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
                                        if (mTradePrivilege.getStatusFlag() == StatusFlag.VALID && extraChargeMap.get(mTradePrivilege.getPromoId()) == null) {
                        if (!isServiceCharge(mTradeVo, mTradePrivilege)) {
                            mTradePrivilege.setInValid();
                        }
                    }
                }

            }

        }

                addServiceCharge(mTradeVo, temPrivileges, listPrivilege);

                for (Long key : extraChargeMap.keySet()) {

            ExtraCharge mExtraCharge = extraChargeMap.get(key);

            if (mExtraCharge.getCode() != null && mExtraCharge.getCode().equals(ExtraManager.mealFee)) {
                TradePrivilege oldPrivilege = temPrivileges.get(mExtraCharge.getId());
                TradePrivilege boxTradePrivilege = BuildPrivilegeTool.buildBoxFee(mTradeVo, oldPrivilege, mExtraCharge);
                boxTradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);
                Map<String, IShopcartItem> temp = new HashMap<String, IShopcartItem>();
                for (IShopcartItem item : iShopcartItem) {

                    if (item.getDishShop() != null && item.getDishShop().getBoxQty() != null && item.getDishShop().getBoxQty().compareTo(BigDecimal.ZERO) != 0) {
                        if (temp.get(item.getDishShop().getUuid()) == null) {
                            mathMealFee(mTradeVo, iShopcartItem, boxTradePrivilege, item, mExtraCharge);
                        }
                        temp.put(item.getDishShop().getUuid(), item);
                    }
                }
                                if (mExtraCharge.getDiscountFlag() == Bool.YES) {
                    extraChageYes = extraChageYes.add(boxTradePrivilege.getPrivilegeAmount());
                } else {
                    extraChageNo = extraChageNo.add(boxTradePrivilege.getPrivilegeAmount());
                }
                if (boxTradePrivilege.getId() == null && oldPrivilege == null) {
                    listPrivilege.add(boxTradePrivilege);
                }
            } else {
                                TradePrivilege oldPrivilege = temPrivileges.get(mExtraCharge.getId());
                                if (ExtraManager.isMinConsum(mExtraCharge))
                    continue;
                                TradePrivilege mTradePrivilege = null;
                if (!isServiceCharge(mTradeVo, mExtraCharge)) {
                    mTradePrivilege = BuildPrivilegeTool.buildExtraChargePrivilege(mTradeVo,
                            oldPrivilege,
                            mExtraCharge, privilegeAfaterAmount);
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

                privilegeMap = mathOutTimeExtraCharge(mTradeVo, privilegeMap);

        mTradeVo.setDiscountExtracharge(extraChageYes);
    }

    private void addServiceCharge(TradeVo mTradeVo, Map<Long, TradePrivilege> temPrivileges, List<TradePrivilege> resultPrivilegeList) {
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


    public static boolean isServiceCharge(TradeVo tradeVo, ExtraCharge mExtraCharge) {
        if (mExtraCharge != null && mExtraCharge.getCode() != null && mExtraCharge.getCode().equalsIgnoreCase(ExtraManager.SERVICE_CONSUM) ) {
            return true;
        }
        return false;
    }

    public static boolean isServiceCharge(TradeVo mTradeVo, TradePrivilege mTradePrivilege) {
        ExtraCharge.ServiceCharge serviceCharge = mTradeVo.getTradeInitConfig(TradeInitConfigKeyId.SERVICE_CHARGE_RATE, ExtraCharge.ServiceCharge.class);
        return serviceCharge != null && Utils.equals(serviceCharge.id, mTradePrivilege.getPromoId());
    }


    public static Map<Integer, BigDecimal> mathOutTimeExtraCharge(TradeVo mTradeVo, Map<Integer, BigDecimal> extraChargePrivilegeMap) {
        boolean outTimeEnable = ServerSettingCache.getInstance().getBuffetOutTimeFeeEnable();

        if (!outTimeEnable) {
            return extraChargePrivilegeMap;
        }


        ExtraCharge mOutTimeRule = ServerSettingCache.getInstance().getmOutTimeRule();

                BigDecimal extraChageYes = extraChargePrivilegeMap.get(EXTRACHARGEYES);

                BigDecimal extraChageNo = extraChargePrivilegeMap.get(EXTRACHARGENO);

        List<TradePrivilege> listPrivilege = mTradeVo.getTradePrivileges();

        for (TradePrivilege outTimePrivilege : listPrivilege) {
            if (outTimePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && outTimePrivilege.getPromoId() != null && outTimePrivilege.getPromoId().longValue() == mOutTimeRule.getId().longValue()) {
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


    public static void mathMealFee(TradeVo tradeVo, List<IShopcartItem> iShopcartItem, TradePrivilege mTradePrivilege, IShopcartItem item, ExtraCharge mExtraCharge) {
                BigDecimal totalQty = BigDecimal.ZERO;
        DeliveryType deliveryType = tradeVo.getTrade().getDeliveryType();
        if (iShopcartItem != null) {
            for (IShopcartItem mIShopcartItem : iShopcartItem) {
                if (mIShopcartItem.getSkuUuid().equals(item.getSkuUuid()) && mIShopcartItem.getStatusFlag() == StatusFlag.VALID
                        && (((deliveryType == DeliveryType.HERE || deliveryType == DeliveryType.TAKE) && mIShopcartItem.getPack())
                        || (deliveryType == DeliveryType.CARRY || deliveryType == DeliveryType.SEND || tradeVo.getIsSalesReturn()))) {                    totalQty = totalQty.add(mIShopcartItem.getTotalQty());
                }
            }
        }
                BigDecimal part = BigDecimal.ONE;
                BigDecimal boxQty = item.getDishShop().getBoxQty();
                BigDecimal dishQtyp = item.getDishShop().getDishQty();


                if (totalQty.compareTo(dishQtyp) > 0) {
                        BigDecimal remainder = totalQty.divideAndRemainder(dishQtyp)[1];
                        BigDecimal count = totalQty.divideToIntegralValue(dishQtyp);
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
