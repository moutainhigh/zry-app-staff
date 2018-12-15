package com.zhongmei.bty.basemodule.discount.manager;

import com.zhongmei.bty.basemodule.log.RLog;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 优惠分摊管理类
 */

public class PrivilegeApportionManager {

    private static final String TAG = PrivilegeApportionManager.class.getSimpleName();

    private static volatile PrivilegeApportionManager instance = null;

    private List<ItemApportion> apportionList;

    private PrivilegeApportionManager() {
        apportionList = new ArrayList<>();
    }

    public static PrivilegeApportionManager getInstance() {
        if (instance == null) {
            synchronized (PrivilegeApportionManager.class) {
                if (instance == null) {
                    instance = new PrivilegeApportionManager();
                }
            }
        }

        return instance;
    }

    /**
     * 内部数据结构
     * 一种优惠对应一条数据
     */
    public static class ItemApportion implements Serializable {
        /**
         * 唯一标示，客户端生成，与tradeitem uuid无关
         */
        public String uuid;
        public String skuUuid;
        public String skuName;
        public String shopcartUUID;
        /**
         * 优惠金额
         */
        public BigDecimal privilegeAmount = BigDecimal.ZERO;
        /**
         * 优惠前的金额
         */
        public BigDecimal saleAmount = BigDecimal.ZERO;
        /**
         * 数量
         */
        public int num;
        /**
         * 优惠规则，默认为-5，表示折扣类的优惠，非折扣类的，会在后续填充覆盖该值
         */
        public Long ruleId = -5L;

        public String ruleName;

        @Override
        public boolean equals(Object obj) {
            ItemApportion apportion = (ItemApportion) obj;
            if (apportion != null && apportion.skuUuid.equals(skuUuid)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 开始计算 初始化数据
     */
    public void startMath() {
        apportionList = new ArrayList<>();
    }

    /**
     * 结束计算
     */
    public void finishMath() {
        apportionList = null;
    }

    public List<ItemApportion> getApportionList() {
        return apportionList;
    }

    /**
     * 礼品券
     *
     * @param item
     */
    public void updateSingleCouponPrivilege(IShopcartItem item) {
        apportionList.add(obtainItemApportionByShopcartItem(item));
    }

    /**
     * 更新单品折扣、折让，第一个步骤计算的
     *
     * @param item
     */
    public void updateSingleDiscount(IShopcartItem item) {
        apportionList.add(obtainItemApportionByShopcartItem(item));
    }

    /**
     * 计算促销活动的优惠分摊
     *
     * @param shopcartItems   能参与该活动的商品列表
     * @param totalAmount     参与活动所有商品的总金额
     * @param privilegeAmount 优惠金额
     */
    public void updatePlanActivity(List<IShopcartItem> shopcartItems,
                                   Long ruleId,
                                   String ruleName,
                                   BigDecimal privilegeAmount,
                                   BigDecimal totalAmount) {

        if (shopcartItems == null || shopcartItems.size() == 0 || privilegeAmount == null
                || totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            RLog.e(TAG, "updatePlanActivity error shopcartItem == null " +
                    "|| shopcartItem.size() == 0 || privilegeAmount == null || totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) == 0");
            return;
        }


        for (IShopcartItem shopcartItem : shopcartItems) {

            ItemApportion apportion = obtainItemApportionByShopcartItem(shopcartItem);
            apportion.ruleId = ruleId;
            apportion.ruleName = ruleName;

            BigDecimal tempPrivilegeAmount = privilegeAmount.multiply(shopcartItem.getActualAmount().divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP));
            apportion.privilegeAmount = MathDecimal.round(tempPrivilegeAmount, 2);

            RLog.d(TAG, "updatePlanActivity skuName " + shopcartItem.getSkuName()
                    + "\nprivilegeAmount " + apportion.privilegeAmount.toPlainString()
                    + "\nactualAmount " + shopcartItem.getActualAmount().toPlainString());

            apportionList.add(apportion);
        }
    }

    /**
     * 计算整单折扣
     *
     * @param items     购物车item
     * @param privilege 优惠金额
     */
    public void updateWholePrivilege(List<IShopcartItem> items,
                                     BigDecimal privilege,
                                     String ruleName,
                                     BigDecimal totalAmount) {

        if (items == null || items.size() == 0 || privilege == null || totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            RLog.e(TAG, "updateWholePrivilege error items == null || items.size() == 0 || privilege == null " +
                    "|| totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) == 0");
            return;
        }

        // 筛选出能够参与整单优惠的item
        List<IShopcartItem> wholePrivilegeItems = new ArrayList<>();
        for (IShopcartItem shopcartItem : items) {
            if (shopcartItem.getStatusFlag() == StatusFlag.VALID && shopcartItem.getEnableWholePrivilege() == Bool.YES) {
                wholePrivilegeItems.add(shopcartItem);
            }
        }

        RLog.d(TAG, "\nupdateWholePrivilege"
                + "\nprivilege " + privilege.toPlainString()
                + "\ntotalAmount " + totalAmount.toPlainString() + "\n");

        //默认为-5，表示折扣类的优惠
        matchWholePrivilege(wholePrivilegeItems, privilege, -5L, ruleName, totalAmount);
    }

    /**
     * 计算整单优惠券金额的分摊
     * 注意，目前整单优惠券跟整单折扣、折让是互斥关系
     * 默认所有菜品都能参与
     *
     * @param items
     */
    public void updateWholeCouponPrivilege(List<IShopcartItem> items,
                                           BigDecimal privilegeAmount,
                                           Long ruleId,
                                           String ruleName,
                                           BigDecimal totalAmount) {

        if (items == null || items.size() == 0 || privilegeAmount == null || totalAmount == null
                || totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            RLog.e(TAG, "updateWholeCouponPrivilege data error items == null || items.size() == 0 || privilegeAmount == null " +
                    "|| totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) == 0");
            return;
        }

        RLog.d(TAG, "updateWholeCouponPrivilege privilegeAmount " + privilegeAmount.toPlainString()
                + " totalAmount " + totalAmount.toPlainString());

        matchWholePrivilege(items, privilegeAmount, ruleId, ruleName, totalAmount);
    }

    /**
     * 计算整场的优惠分摊，优惠券、整单折扣、折让...
     *
     * @param items           能够参与整场优惠的数据
     * @param privilegeAmount
     * @param totalAmount
     */
    private void matchWholePrivilege(List<IShopcartItem> items,
                                     BigDecimal privilegeAmount,
                                     Long ruleId,
                                     String ruleName,
                                     BigDecimal totalAmount) {

        for (IShopcartItem shopcartItem : items) {

            //如果该商品已有优惠，那么先要减去之前的优惠，以此为基数才能进一步计算整场的优惠分摊
            BigDecimal actualAmount = null;

            for (ItemApportion apportion : apportionList) {
                if (shopcartItem.getUuid().equals(apportion.shopcartUUID)) {
                    //需要先减去商品维度的优惠金额，因为totalAmount是减去了商品维度优惠的金额
                    actualAmount = shopcartItem.getActualAmount().subtract(apportion.privilegeAmount);
                    RLog.d(TAG, "matchWholePrivilege 正常优惠之前已有优惠金额 " + apportion.privilegeAmount
                            + "\n优惠名称 " + apportion.ruleName
                            + "\n优惠id " + apportion.ruleId);
                    break;
                }
            }

            //如果该商品之前没有优惠，则直接使用actualAmount字段的值
            if (actualAmount == null) {
                actualAmount = shopcartItem.getActualAmount();
            }

            ItemApportion apportion = obtainItemApportionByShopcartItem(shopcartItem);
            BigDecimal tempPrivilegeAmount = privilegeAmount.multiply(actualAmount.divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP)); //除法尽量精确
            apportion.privilegeAmount = MathDecimal.round(tempPrivilegeAmount, 2).abs(); // 保留两位小数
            apportion.ruleId = ruleId;
            apportion.ruleName = ruleName;

            RLog.d(TAG, "matchWholePrivilege 新添加 skuName" + shopcartItem.getSkuName()
                    + "\nactualAmount " + shopcartItem.getActualAmount().toPlainString()
                    + "\n优惠金额 " + tempPrivilegeAmount.toPlainString());

            apportionList.add(apportion);
        }
    }

    private ItemApportion obtainItemApportionByShopcartItem(IShopcartItem shopcartItem) {

        ItemApportion apportion = new ItemApportion();
        apportion.uuid = SystemUtils.genOnlyIdentifier();
        apportion.skuUuid = shopcartItem.getSkuUuid();
        apportion.skuName = shopcartItem.getSkuName();
        apportion.saleAmount = shopcartItem.getActualAmount();
        apportion.num = shopcartItem.getTotalQty().intValue();
        apportion.shopcartUUID = shopcartItem.getUuid();

        // 优先判断单商品折扣
        if (shopcartItem.getPrivilege() != null && shopcartItem.getPrivilege().getPrivilegeAmount() != null) {

            apportion.privilegeAmount = shopcartItem.getPrivilege().getPrivilegeAmount().abs(); //绝对值

            if (shopcartItem.getPrivilege().getPromoId() != null) { //优惠规则id
                apportion.ruleId = shopcartItem.getPrivilege().getPromoId();
            }
            apportion.ruleName = shopcartItem.getPrivilege().getPrivilegeName();
        } else if (shopcartItem.getCouponPrivilegeVo() != null
                && shopcartItem.getCouponPrivilegeVo().getTradePrivilege() != null
                && shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount() != null) { //再判断礼品券

            apportion.privilegeAmount = shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount().abs(); //绝对值

            if (shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPromoId() != null) { //优惠规则id
                apportion.ruleId = shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPromoId();
            }
            apportion.ruleName = shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeName();
        }

        return apportion;
    }
}
