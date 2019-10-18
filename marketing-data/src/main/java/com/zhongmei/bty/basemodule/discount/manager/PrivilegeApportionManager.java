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


    public static class ItemApportion implements Serializable {

        public String uuid;
        public String skuUuid;
        public String skuName;
        public String shopcartUUID;

        public BigDecimal privilegeAmount = BigDecimal.ZERO;

        public BigDecimal saleAmount = BigDecimal.ZERO;

        public int num;

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


    public void startMath() {
        apportionList = new ArrayList<>();
    }


    public void finishMath() {
        apportionList = null;
    }

    public List<ItemApportion> getApportionList() {
        return apportionList;
    }


    public void updateSingleCouponPrivilege(IShopcartItem item) {
        apportionList.add(obtainItemApportionByShopcartItem(item));
    }


    public void updateSingleDiscount(IShopcartItem item) {
        apportionList.add(obtainItemApportionByShopcartItem(item));
    }


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


    public void updateWholePrivilege(List<IShopcartItem> items,
                                     BigDecimal privilege,
                                     String ruleName,
                                     BigDecimal totalAmount) {

        if (items == null || items.size() == 0 || privilege == null || totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            RLog.e(TAG, "updateWholePrivilege error items == null || items.size() == 0 || privilege == null " +
                    "|| totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) == 0");
            return;
        }

                List<IShopcartItem> wholePrivilegeItems = new ArrayList<>();
        for (IShopcartItem shopcartItem : items) {
            if (shopcartItem.getStatusFlag() == StatusFlag.VALID && shopcartItem.getEnableWholePrivilege() == Bool.YES) {
                wholePrivilegeItems.add(shopcartItem);
            }
        }

        RLog.d(TAG, "\nupdateWholePrivilege"
                + "\nprivilege " + privilege.toPlainString()
                + "\ntotalAmount " + totalAmount.toPlainString() + "\n");

                matchWholePrivilege(wholePrivilegeItems, privilege, -5L, ruleName, totalAmount);
    }


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


    private void matchWholePrivilege(List<IShopcartItem> items,
                                     BigDecimal privilegeAmount,
                                     Long ruleId,
                                     String ruleName,
                                     BigDecimal totalAmount) {

        for (IShopcartItem shopcartItem : items) {

                        BigDecimal actualAmount = null;

            for (ItemApportion apportion : apportionList) {
                if (shopcartItem.getUuid().equals(apportion.shopcartUUID)) {
                                        actualAmount = shopcartItem.getActualAmount().subtract(apportion.privilegeAmount);
                    RLog.d(TAG, "matchWholePrivilege 正常优惠之前已有优惠金额 " + apportion.privilegeAmount
                            + "\n优惠名称 " + apportion.ruleName
                            + "\n优惠id " + apportion.ruleId);
                    break;
                }
            }

                        if (actualAmount == null) {
                actualAmount = shopcartItem.getActualAmount();
            }

            ItemApportion apportion = obtainItemApportionByShopcartItem(shopcartItem);
            BigDecimal tempPrivilegeAmount = privilegeAmount.multiply(actualAmount.divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP));             apportion.privilegeAmount = MathDecimal.round(tempPrivilegeAmount, 2).abs();             apportion.ruleId = ruleId;
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

                if (shopcartItem.getPrivilege() != null && shopcartItem.getPrivilege().getPrivilegeAmount() != null) {

            apportion.privilegeAmount = shopcartItem.getPrivilege().getPrivilegeAmount().abs();
            if (shopcartItem.getPrivilege().getPromoId() != null) {                 apportion.ruleId = shopcartItem.getPrivilege().getPromoId();
            }
            apportion.ruleName = shopcartItem.getPrivilege().getPrivilegeName();
        } else if (shopcartItem.getCouponPrivilegeVo() != null
                && shopcartItem.getCouponPrivilegeVo().getTradePrivilege() != null
                && shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount() != null) {
            apportion.privilegeAmount = shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount().abs();
            if (shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPromoId() != null) {                 apportion.ruleId = shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPromoId();
            }
            apportion.ruleName = shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeName();
        }

        return apportion;
    }
}
