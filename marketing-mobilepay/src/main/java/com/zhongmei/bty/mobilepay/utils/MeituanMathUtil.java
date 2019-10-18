package com.zhongmei.bty.mobilepay.utils;

import android.text.TextUtils;

import com.zhongmei.bty.mobilepay.message.TuanGouCouponDetail;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.mobilepay.bean.meituan.CouponDishLimit;
import com.zhongmei.bty.mobilepay.bean.meituan.ICouponDishRelate;
import com.zhongmei.bty.mobilepay.bean.meituan.IGroupBuyingCoupon;
import com.zhongmei.bty.mobilepay.bean.meituan.MeituanDishItem;
import com.zhongmei.bty.mobilepay.bean.meituan.MeituanDishItemVo;
import com.zhongmei.bty.mobilepay.bean.meituan.MeituanDishVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class MeituanMathUtil {

    public static MeituanDishVo matchAndMath(TradeVo tradeVo, TuanGouCouponDetail groupBuyingCoupon, int usedCount, List<ICouponDishRelate> dishRelateList) {
        MeituanDishVo meituanDishVo = new MeituanDishVo(usedCount);        Map<String, CouponDishLimit> containDishMap = new HashMap<>();
        Map<String, CouponDishLimit> exculeDishMap = new HashMap<>();
        List<MeituanDishItemVo> meituanVoList = new ArrayList<>();
        meituanDishVo.matchDishItemVoList = meituanVoList;
        List<CouponDishLimit> couponDishLimitList = groupBuyingCoupon.getLimitDish();

        BigDecimal tradeAmount = tradeVo.getTrade().getTradeAmount();

        if (Utils.isEmpty(couponDishLimitList)) {            BigDecimal couponAmount = getCouponAmount(groupBuyingCoupon, usedCount);            if (couponAmount.compareTo(tradeAmount) <= 0) {
                meituanDishVo.matchAmount = couponAmount;
            } else {
                meituanDishVo.matchAmount = tradeAmount;
            }
            meituanDishVo.isMatchEnable = true;
            return meituanDishVo;
        } else {
            for (CouponDishLimit couponDishLimit : couponDishLimitList) {
                if (couponDishLimit.mappingType == CouponDishLimit.TYPE_CONTAIN) {
                    containDishMap.put(couponDishLimit.dishUuid, couponDishLimit);
                } else {
                    exculeDishMap.put(couponDishLimit.dishUuid, couponDishLimit);
                }
            }
        }
        Map<String, BigDecimal> couponDishRelateMap = new HashMap<>();
        if (Utils.isNotEmpty(dishRelateList)) {
            for (ICouponDishRelate couponDishRelate : dishRelateList) {
                                if (couponDishRelateMap.get(couponDishRelate.getTradeItemUuid()) == null) {
                    couponDishRelateMap.put(couponDishRelate.getTradeItemUuid(), BigDecimal.ZERO);
                }
                BigDecimal tempQuantity = couponDishRelateMap.get(couponDishRelate.getTradeItemUuid());
                tempQuantity = tempQuantity.add(couponDishRelate.getDishNum());
                couponDishRelateMap.put(couponDishRelate.getTradeItemUuid(), tempQuantity);
            }
        }

                Map<String, TradeItemNumVo> tempMap = new HashMap<>();
                if (Utils.isNotEmpty(tradeVo.getTradeItemList())) {
            for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                                if (tradeItem.getStatusFlag() != StatusFlag.VALID || tradeItem.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                if (!isItemCanMath(tradeItem)) {
                    continue;
                }
                TradeItemNumVo tradeItemNumVo = tempMap.get(tradeItem.getSkuUuid());
                if (tradeItemNumVo == null) {
                    tradeItemNumVo = new TradeItemNumVo();
                    tempMap.put(tradeItem.getSkuUuid(), tradeItemNumVo);
                }
                tradeItemNumVo.num = tradeItemNumVo.num.add(tradeItem.getQuantity());
                if (tradeItemNumVo.tradeItemList == null) {
                    tradeItemNumVo.tradeItemList = new ArrayList<>();
                }
                tradeItemNumVo.tradeItemList.add(tradeItem);
            }
        }

        for (int i = 0; i < usedCount; i++) {
                        BigDecimal tempDebPrice = BigDecimal.ZERO;
                        BigDecimal debAmount = groupBuyingCoupon.getMarketPrice();

            MeituanDishItemVo meituanDishItemVo = new MeituanDishItemVo();
            List<MeituanDishItem> meituanDishItemList = new ArrayList<>();
                        boolean isHasContainDish = false;
                        boolean isHasExculeDish = false;
                        boolean isMatchDish = false;
                        if (containDishMap.size() > 0 && Utils.isNotEmpty(tradeVo.getTradeItemList())) {
                isHasContainDish = true;
                Iterator<String> iterator = containDishMap.keySet().iterator();
                                while (iterator.hasNext()) {
                    String dishUuid = iterator.next();
                    CouponDishLimit couponDishLimit = containDishMap.get(dishUuid);
                    BigDecimal limitNum = couponDishLimit.num;
                    TradeItemNumVo matchItemVo = tempMap.get(dishUuid);
                    if (matchItemVo != null) {
                                                BigDecimal totalItemDebAmount = BigDecimal.ZERO;
                        for (TradeItem tradeItem : matchItemVo.tradeItemList) {
                            BigDecimal relateNum = couponDishRelateMap.get(tradeItem.getUuid());
                                                        BigDecimal debQuantity = BigDecimal.ZERO;
                            if (relateNum != null) {
                                if (relateNum.compareTo(tradeItem.getQuantity()) >= 0) {
                                    continue;
                                } else {
                                    debQuantity = tradeItem.getQuantity().subtract(relateNum);
                                }
                            } else {
                                debQuantity = tradeItem.getQuantity();
                            }

                            if (debQuantity.compareTo(limitNum) >= 0) {
                                debQuantity = limitNum;
                            }
                            if (couponDishRelateMap.get(tradeItem.getUuid()) != null) {
                                BigDecimal tempQuantity = couponDishRelateMap.get(tradeItem.getUuid());
                                tempQuantity = tempQuantity.add(debQuantity);
                                couponDishRelateMap.put(tradeItem.getUuid(), tempQuantity);
                            } else {
                                couponDishRelateMap.put(tradeItem.getUuid(), debQuantity);
                            }
                                                        if (debQuantity.compareTo(BigDecimal.ZERO) > 0) {
                                tempDebPrice = tempDebPrice.add(tradeItem.getActualAmount().multiply(debQuantity).divide(tradeItem.getQuantity(), 2));
                            }

                            isMatchDish = true;
                            MeituanDishItem meituanItemVo = new MeituanDishItem();
                            meituanItemVo.skuId = tradeItem.getDishId();
                            meituanItemVo.dishName = tradeItem.getDishName();
                            meituanItemVo.tradeItemUuid = tradeItem.getUuid();
                            meituanItemVo.price = tradeItem.getPrice();
                            meituanItemVo.num = debQuantity;
                            meituanDishItemList.add(meituanItemVo);
                                                        totalItemDebAmount = totalItemDebAmount.add(debQuantity);
                            if (totalItemDebAmount.compareTo(limitNum) >= 0) {
                                break;
                            }
                        }
                    }
                }

            }
                        if (!isHasContainDish && exculeDishMap.size() > 0 && Utils.isNotEmpty(tradeVo.getTradeItemList())) {
                for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                    TradeItem tradeItem = tradeItemVo.getTradeItem();
                    if (tradeItem.getStatusFlag() != StatusFlag.VALID || tradeItem.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                                        BigDecimal restFaceAmout = debAmount.subtract(tempDebPrice);
                                        if (restFaceAmout.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }

                    if (exculeDishMap.get(tradeItem.getSkuUuid()) == null && isItemCanMath(tradeItem)) {
                                                BigDecimal relateNum = couponDishRelateMap.get(tradeItem.getUuid());
                                                BigDecimal priceTemp = tradeItem.getActualAmount().divide(tradeItem.getQuantity(), 4, RoundingMode.HALF_UP);
                                                if (relateNum == null) {
                                                        if (restFaceAmout.compareTo(tradeItem.getActualAmount()) > 0) {
                                tempDebPrice = tempDebPrice.add(tradeItem.getActualAmount());
                                couponDishRelateMap.put(tradeItem.getUuid(), tradeItem.getQuantity());
                                MeituanDishItem meituanDishItem = createMDishItem(tradeItem, tradeItem.getQuantity(), tradeItem.getActualAmount(), 2);
                                meituanDishItemList.add(meituanDishItem);
                                isHasExculeDish = true;

                            } else {
                                                                BigDecimal relateNumTemp = restFaceAmout.divide(priceTemp, 4, RoundingMode.HALF_UP);
                                tempDebPrice = tempDebPrice.add(restFaceAmout);
                                couponDishRelateMap.put(tradeItem.getUuid(), relateNumTemp);
                                MeituanDishItem meituanDishItem = createMDishItem(tradeItem, relateNumTemp, restFaceAmout, 2);
                                meituanDishItemList.add(meituanDishItem);
                                isHasExculeDish = true;

                                break;
                            }

                        } else {
                            if (tradeItem.getQuantity().compareTo(relateNum) > 0) {                                  BigDecimal restNum = tradeItem.getQuantity().subtract(relateNum);                                BigDecimal reatTradeItemAmount = restNum.multiply(priceTemp);                                                                if (restFaceAmout.compareTo(reatTradeItemAmount) > 0) {
                                    tempDebPrice = tempDebPrice.add(reatTradeItemAmount);
                                    couponDishRelateMap.put(tradeItem.getUuid(), relateNum.add(restNum));
                                    MeituanDishItem meituanDishItem = createMDishItem(tradeItem, restNum, reatTradeItemAmount, 2);
                                    meituanDishItemList.add(meituanDishItem);
                                    isHasExculeDish = true;

                                } else {                                                                         BigDecimal relateNumTemp = restFaceAmout.divide(priceTemp, 4, RoundingMode.HALF_UP);
                                    tempDebPrice = tempDebPrice.add(restFaceAmout);
                                    couponDishRelateMap.put(tradeItem.getUuid(), relateNum.add(relateNumTemp));
                                    MeituanDishItem meituanDishItem = createMDishItem(tradeItem, relateNumTemp, restFaceAmout, 2);
                                    meituanDishItemList.add(meituanDishItem);
                                    isHasExculeDish = true;

                                    break;
                                }
                            }
                        }
                                            }
                }
            }

                        if (isHasExculeDish) {

                                if (tempDebPrice.compareTo(debAmount) < 0) {
                    debAmount = tempDebPrice;
                }
            } else {
                                if (tempDebPrice.compareTo(debAmount) < 0) {
                    debAmount = tempDebPrice;
                }
            }
            if (isMatchDish || isHasExculeDish) {
                meituanDishVo.matchCount++;
                meituanDishVo.isMatchEnable = true;

                meituanDishItemVo.setDebAmount(debAmount);
                meituanDishVo.matchAmount = meituanDishVo.matchAmount.add(debAmount);
                meituanDishItemVo.setMeituanItemVoList(meituanDishItemList);
                meituanVoList.add(meituanDishItemVo);
            }
        }
        return meituanDishVo;
    }


    private static boolean isItemCanMath(TradeItem tradeItem) {
                if ((tradeItem.getType() == DishType.COMBO || tradeItem.getType() == DishType.SINGLE) && TextUtils.isEmpty(tradeItem.getParentUuid())) {
            return true;
        }
        return false;
    }

    private static BigDecimal getCouponAmount(IGroupBuyingCoupon groupBuyingCoupon, int usedCount) {
        return groupBuyingCoupon.getMarketPrice().multiply(new BigDecimal(usedCount));
    }

        private static MeituanDishItem createMDishItem(TradeItem tradeItem, BigDecimal realteNum, BigDecimal deductionAmount, int decuctionType) {
        MeituanDishItem meituanItem = new MeituanDishItem();
        meituanItem.skuId = tradeItem.getDishId();
        meituanItem.dishName = tradeItem.getDishName();
        meituanItem.num = realteNum;
        meituanItem.price = tradeItem.getPrice();
        meituanItem.deductionAmount = deductionAmount;
        meituanItem.tradeItemUuid = tradeItem.getUuid();
        meituanItem.deductionType = decuctionType;
        return meituanItem;
    }

    private static class TradeItemNumVo {
        List<TradeItem> tradeItemList;
        BigDecimal num = BigDecimal.ZERO;
    }
}
