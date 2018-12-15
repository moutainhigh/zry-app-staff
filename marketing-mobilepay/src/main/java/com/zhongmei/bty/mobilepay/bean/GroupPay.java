package com.zhongmei.bty.mobilepay.bean;

import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.context.util.Utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date：2015-9-29 上午10:03:47
 * @Description: 保存一组支付类型
 * @Version: 1.0
 */
public class GroupPay implements Serializable {
    private static final long serialVersionUID = 1L;
    private PayModelGroup group;
    private Map<Long, PayModelItem> payments;
    //溢收金额
    private double mMorePayAmount;//add 20170531

    //构造方法1
    public GroupPay() {
        payments = new LinkedHashMap<Long, PayModelItem>();
    }

    //构造方法2
    public GroupPay(PayModelGroup payModelGroup) {
        this();
        group = payModelGroup;
    }

    public PayModelGroup getGroup() {
        return group;
    }

    //添加一种支付金额
    public void addPayModelItem(PayModelItem item) {
        payments.put(item.getPayModelId(), item);
    }

    //添加多种支付金额
    public void addPayModelItems(List<PayModelItem> items) {
        if (!Utils.isEmpty(items)) {
            for (PayModelItem item : items) {
                this.addPayModelItem(item);
            }
        }
    }

    //取消一种支付金额
    public void removePayModelItem(PayModelItem item) {
        if (payments.containsKey(item.getPayModelId())) {
            payments.remove(item.getPayModelId());
        }
    }

    //获取一种支付金额
    public PayModelItem getPayModelItemByPayModeId(PayModeId payModeId) {

        return getPayModelItemByPayModeId(payModeId.value());
    }

    //根据key获取一种支付金额
    public PayModelItem getPayModelItemByPayModeId(Long payModeId) {

        return payments.get(payModeId);
    }

    //支付面值
    public double getGroupAmount() {
        BigDecimal temp = BigDecimal.ZERO;
        for (PayModelItem item : payments.values()) {
            if (item != null) {
                temp = temp.add(item.getUsedValue());
            }
        }
        return temp.doubleValue();
    }

    //其它支付成本价
    public double getGroupActualAmount() {
        BigDecimal temp = BigDecimal.ZERO;
        for (PayModelItem item : payments.values()) {
            if (item != null) {
                temp = temp.add(item.getActualValue());
            }
        }
        return temp.doubleValue();
    }

    /**
     * @Title: getGroupAmountExceptModel
     * @Description: 获取除payModelId 之外的其它支付之和
     * @Param @param payModelId
     * @Return double 返回类型
     */
    public double getGroupAmountExceptModel(Long payModelId) {
        BigDecimal temp = BigDecimal.ZERO;
        for (Long key : payments.keySet()) {
            if (!payModelId.equals(key)) {
                PayModelItem item = payments.get(key);
                if (item != null) {
                    temp = temp.add(item.getUsedValue());
                }
            }
        }
        return temp.doubleValue();
    }

    public List<PayModelItem> getAllPayModelItems() {
        List<PayModelItem> list = new ArrayList<PayModelItem>();
        list.addAll(payments.values());
        return list;
    }

    public List<PayModelItem> getAutoInputPayModelItems(boolean isAutoInput) {
        List<PayModelItem> list = new ArrayList<PayModelItem>();
        for (PayModelItem item : payments.values()) {
            if (item.isAutoInput() == isAutoInput) {
                list.add(item);
            }
        }
        return list;
    }

    public boolean isContainsPayModel(PayModeId payModeId) {
        if (payModeId == PayModeId.BAINUO_TUANGOU || payModeId == PayModeId.MEITUAN_TUANGOU) {
            for (PayModelItem item : payments.values()) {
                if (item.getPayMode() == payModeId) {
                    return true;
                }
            }
        }
        return payments.containsKey(payModeId.value());
    }

    public boolean isEmpty() {

        return payments.isEmpty();
    }

    public int size() {
        return payments.size();
    }

    public void clear() {
        if (payments != null) {
            payments.clear();
        }
        mMorePayAmount = 0;
    }

    public double getMorePayAmount() {
        return mMorePayAmount;
    }

    public void setMorePayAmount(double morePayAmount) {
        this.mMorePayAmount = morePayAmount;
    }

    ///add v8.16
    public GroupPay clone() {
        GroupPay groupPay = new GroupPay();
        groupPay.group = this.group;
        groupPay.mMorePayAmount = this.mMorePayAmount;
        if (!this.payments.isEmpty())
            groupPay.payments.putAll(this.payments);
        return groupPay;
    }
}
