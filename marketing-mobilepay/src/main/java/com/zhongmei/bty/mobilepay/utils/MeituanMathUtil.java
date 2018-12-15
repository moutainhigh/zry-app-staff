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

/**
 * 美团菜品匹配和计算工具类
 * Created by demo on 2018/12/15
 */

public class MeituanMathUtil {

    public static MeituanDishVo matchAndMath(TradeVo tradeVo, TuanGouCouponDetail groupBuyingCoupon, int usedCount, List<ICouponDishRelate> dishRelateList) {
        MeituanDishVo meituanDishVo = new MeituanDishVo(usedCount);//返回参数
        Map<String, CouponDishLimit> containDishMap = new HashMap<>();
        Map<String, CouponDishLimit> exculeDishMap = new HashMap<>();
        List<MeituanDishItemVo> meituanVoList = new ArrayList<>();
        meituanDishVo.matchDishItemVoList = meituanVoList;
        List<CouponDishLimit> couponDishLimitList = groupBuyingCoupon.getLimitDish();

        BigDecimal tradeAmount = tradeVo.getTrade().getTradeAmount();

        if (Utils.isEmpty(couponDishLimitList)) {//没有菜品限制，返回券面额或订单应收
            BigDecimal couponAmount = getCouponAmount(groupBuyingCoupon, usedCount);//面值
            if (couponAmount.compareTo(tradeAmount) <= 0) {
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
                //同一个菜可能被抵用多个劵
                if (couponDishRelateMap.get(couponDishRelate.getTradeItemUuid()) == null) {
                    couponDishRelateMap.put(couponDishRelate.getTradeItemUuid(), BigDecimal.ZERO);
                }
                BigDecimal tempQuantity = couponDishRelateMap.get(couponDishRelate.getTradeItemUuid());
                tempQuantity = tempQuantity.add(couponDishRelate.getDishNum());
                couponDishRelateMap.put(couponDishRelate.getTradeItemUuid(), tempQuantity);
            }
        }

        //tradeVo中相同的菜品 统计
        Map<String, TradeItemNumVo> tempMap = new HashMap<>();
        //将相同dish的菜品分组
        if (Utils.isNotEmpty(tradeVo.getTradeItemList())) {
            for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                //过滤无效菜品
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
            //临时存放抵扣金额,只是匹配菜品的金额
            BigDecimal tempDebPrice = BigDecimal.ZERO;
            //面值
            BigDecimal debAmount = groupBuyingCoupon.getMarketPrice();

            MeituanDishItemVo meituanDishItemVo = new MeituanDishItemVo();
            List<MeituanDishItem> meituanDishItemList = new ArrayList<>();
            //是否有包含菜
            boolean isHasContainDish = false;
            //是否有排除菜
            boolean isHasExculeDish = false;
            //是否匹配到菜品
            boolean isMatchDish = false;
            //包含菜
            if (containDishMap.size() > 0 && Utils.isNotEmpty(tradeVo.getTradeItemList())) {
                isHasContainDish = true;
                Iterator<String> iterator = containDishMap.keySet().iterator();
                //抵扣金额
                while (iterator.hasNext()) {
                    String dishUuid = iterator.next();
                    CouponDishLimit couponDishLimit = containDishMap.get(dishUuid);
                    BigDecimal limitNum = couponDishLimit.num;
                    TradeItemNumVo matchItemVo = tempMap.get(dishUuid);
                    if (matchItemVo != null) {
                        //刚好匹配菜品或者设定的规则的菜品数量大于购物车中菜品数量
                        BigDecimal totalItemDebAmount = BigDecimal.ZERO;
                        for (TradeItem tradeItem : matchItemVo.tradeItemList) {
                            BigDecimal relateNum = couponDishRelateMap.get(tradeItem.getUuid());
                            //抵扣数量
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
                            //未抵扣菜品，只记录，不计算抵扣金额
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
                            //刚好抵扣完成不再找下一个菜品
                            totalItemDebAmount = totalItemDebAmount.add(debQuantity);
                            if (totalItemDebAmount.compareTo(limitNum) >= 0) {
                                break;
                            }
                        }
                    }
                }

            }
            //只有是排除菜
            if (!isHasContainDish && exculeDishMap.size() > 0 && Utils.isNotEmpty(tradeVo.getTradeItemList())) {
                for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                    TradeItem tradeItem = tradeItemVo.getTradeItem();
                    if (tradeItem.getStatusFlag() != StatusFlag.VALID || tradeItem.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                    //可以抵扣金额
                    BigDecimal restFaceAmout = debAmount.subtract(tempDebPrice);
                    //抵扣金额小于等于0 退出循环
                    if (restFaceAmout.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }

                    if (exculeDishMap.get(tradeItem.getSkuUuid()) == null && isItemCanMath(tradeItem)) {
                        //商品已经抵扣数据 //modify yutang  20180627 begin
                        BigDecimal relateNum = couponDishRelateMap.get(tradeItem.getUuid());
                        //商品平均价
                        BigDecimal priceTemp = tradeItem.getActualAmount().divide(tradeItem.getQuantity(), 4, RoundingMode.HALF_UP);
                        //如果还没有抵扣记录
                        if (relateNum == null) {
                            //商品全部抵扣（商品金额<可以抵扣的面值）
                            if (restFaceAmout.compareTo(tradeItem.getActualAmount()) > 0) {
                                tempDebPrice = tempDebPrice.add(tradeItem.getActualAmount());
                                couponDishRelateMap.put(tradeItem.getUuid(), tradeItem.getQuantity());
                                MeituanDishItem meituanDishItem = createMDishItem(tradeItem, tradeItem.getQuantity(), tradeItem.getActualAmount(), 2);
                                meituanDishItemList.add(meituanDishItem);
                                isHasExculeDish = true;

                            } else {
                                //商品部分抵扣数量
                                BigDecimal relateNumTemp = restFaceAmout.divide(priceTemp, 4, RoundingMode.HALF_UP);
                                tempDebPrice = tempDebPrice.add(restFaceAmout);
                                couponDishRelateMap.put(tradeItem.getUuid(), relateNumTemp);
                                MeituanDishItem meituanDishItem = createMDishItem(tradeItem, relateNumTemp, restFaceAmout, 2);
                                meituanDishItemList.add(meituanDishItem);
                                isHasExculeDish = true;

                                break;
                            }

                        } else {//如果商品已经有抵扣记录

                            if (tradeItem.getQuantity().compareTo(relateNum) > 0) {  //如果商品还没有抵扣完
                                BigDecimal restNum = tradeItem.getQuantity().subtract(relateNum);//剩余数量
                                BigDecimal reatTradeItemAmount = restNum.multiply(priceTemp);//剩余金额
                                //全部抵扣剩余数量（剩余商品金额<=可以抵扣的面值）
                                if (restFaceAmout.compareTo(reatTradeItemAmount) > 0) {
                                    tempDebPrice = tempDebPrice.add(reatTradeItemAmount);
                                    couponDishRelateMap.put(tradeItem.getUuid(), relateNum.add(restNum));
                                    MeituanDishItem meituanDishItem = createMDishItem(tradeItem, restNum, reatTradeItemAmount, 2);
                                    meituanDishItemList.add(meituanDishItem);
                                    isHasExculeDish = true;

                                } else { //抵扣部分剩余数量
                                    //抵扣数量
                                    BigDecimal relateNumTemp = restFaceAmout.divide(priceTemp, 4, RoundingMode.HALF_UP);
                                    tempDebPrice = tempDebPrice.add(restFaceAmout);
                                    couponDishRelateMap.put(tradeItem.getUuid(), relateNum.add(relateNumTemp));
                                    MeituanDishItem meituanDishItem = createMDishItem(tradeItem, relateNumTemp, restFaceAmout, 2);
                                    meituanDishItemList.add(meituanDishItem);
                                    isHasExculeDish = true;

                                    break;
                                }
                            }
                        }
                        //modify yutang 20180627 end
                    }
                }
            }

            //排除菜多张逻辑
            if (isHasExculeDish) {
              /* BigDecimal diffAmount=tempDebPrice.subtract(meituanDishVo.matchAmount);
                if(diffAmount.compareTo(BigDecimal.ZERO)>0){
                    if (diffAmount.compareTo(debAmount) < 0) {
                        debAmount = diffAmount;
                    }
                }else {
                    //以前的排除劵已经抵扣完成，不用再抵用
                    continue;
                }*/
                //匹配菜品价格小于劵面额，抵扣菜品价格
                if (tempDebPrice.compareTo(debAmount) < 0) {
                    debAmount = tempDebPrice;
                }
            } else {
                //匹配菜品价格小于劵面额，抵扣菜品价格
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

    /**
     * 菜品能否参与匹配计算
     *
     * @param tradeItem
     * @return
     */
    private static boolean isItemCanMath(TradeItem tradeItem) {
        //排除子菜
        if ((tradeItem.getType() == DishType.COMBO || tradeItem.getType() == DishType.SINGLE) && TextUtils.isEmpty(tradeItem.getParentUuid())) {
            return true;
        }
        return false;
    }

    private static BigDecimal getCouponAmount(IGroupBuyingCoupon groupBuyingCoupon, int usedCount) {
        return groupBuyingCoupon.getMarketPrice().multiply(new BigDecimal(usedCount));
    }

    //构建抵扣菜品的数量和金额
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
