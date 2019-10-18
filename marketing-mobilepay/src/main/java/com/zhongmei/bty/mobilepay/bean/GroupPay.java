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


public class GroupPay implements Serializable {
    private static final long serialVersionUID = 1L;
    private PayModelGroup group;
    private Map<Long, PayModelItem> payments;
        private double mMorePayAmount;
        public GroupPay() {
        payments = new LinkedHashMap<Long, PayModelItem>();
    }

        public GroupPay(PayModelGroup payModelGroup) {
        this();
        group = payModelGroup;
    }

    public PayModelGroup getGroup() {
        return group;
    }

        public void addPayModelItem(PayModelItem item) {
        payments.put(item.getPayModelId(), item);
    }

        public void addPayModelItems(List<PayModelItem> items) {
        if (!Utils.isEmpty(items)) {
            for (PayModelItem item : items) {
                this.addPayModelItem(item);
            }
        }
    }

        public void removePayModelItem(PayModelItem item) {
        if (payments.containsKey(item.getPayModelId())) {
            payments.remove(item.getPayModelId());
        }
    }

        public PayModelItem getPayModelItemByPayModeId(PayModeId payModeId) {

        return getPayModelItemByPayModeId(payModeId.value());
    }

        public PayModelItem getPayModelItemByPayModeId(Long payModeId) {

        return payments.get(payModeId);
    }

        public double getGroupAmount() {
        BigDecimal temp = BigDecimal.ZERO;
        for (PayModelItem item : payments.values()) {
            if (item != null) {
                temp = temp.add(item.getUsedValue());
            }
        }
        return temp.doubleValue();
    }

        public double getGroupActualAmount() {
        BigDecimal temp = BigDecimal.ZERO;
        for (PayModelItem item : payments.values()) {
            if (item != null) {
                temp = temp.add(item.getActualValue());
            }
        }
        return temp.doubleValue();
    }


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

        public GroupPay clone() {
        GroupPay groupPay = new GroupPay();
        groupPay.group = this.group;
        groupPay.mMorePayAmount = this.mMorePayAmount;
        if (!this.payments.isEmpty())
            groupPay.payments.putAll(this.payments);
        return groupPay;
    }
}
