package com.zhongmei.bty.basemodule.discount.bean;

import android.util.Log;

import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevelSetting;
import com.zhongmei.bty.basemodule.discount.entity.CrmCustomerLevelRights;
import com.zhongmei.yunfu.db.entity.discount.CustomerScoreRule;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilegeExtra;
import com.zhongmei.yunfu.context.util.NoProGuard;
import com.zhongmei.bty.commonmodule.database.enums.LimitType;
import com.zhongmei.yunfu.db.enums.PrivilegeUseStatus;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;


public class IntegralCashPrivilegeVo implements java.io.Serializable, NoProGuard {


    private static final long serialVersionUID = 1L;
    private static final String TAG = IntegralCashPrivilegeVo.class.getSimpleName();

    private TradePrivilege tradePrivilege;

    private BigDecimal convertValue;
    private BigDecimal maxInteger;

    private TradePrivilegeExtra tradePrivilegeExtra;


    private BigDecimal integral;

    private boolean actived = false;

    private Long ruleId;

    public TradePrivilege getTradePrivilege() {
        return tradePrivilege;
    }

    public void setTradePrivilege(TradePrivilege tradePrivilege) {
        this.tradePrivilege = tradePrivilege;
    }

    public TradePrivilegeExtra getTradePrivilegeExtra() {
        return tradePrivilegeExtra;
    }

    public void setTradePrivilegeExtra(TradePrivilegeExtra tradePrivilegeExtra) {
        this.tradePrivilegeExtra = tradePrivilegeExtra;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRule(CrmCustomerLevelRights rule) {
        if (rule != null) {

            ruleId = rule.getId();
        }
    }

    public void setMaxInteger(BigDecimal maxInteger){
        if(maxInteger!=null){
            this.maxInteger=maxInteger;
        }
    }

    public BigDecimal getMaxInteger(){
        return maxInteger;
    }

    public void setRule(CustomerScoreRule rule) {
        if (rule != null && rule.getConvertValue() != null) {
            convertValue = new BigDecimal(rule.getConvertValue());
            ruleId = rule.getId();
        }
    }

    public void setRule(EcCardLevelSetting rule) {
        if (rule != null) {

            ruleId = rule.getId();
        }
    }

    public boolean hasRule() {
        return convertValue != null;
    }

    public BigDecimal getIntegral() {
        return integral;
    }


    public BigDecimal getUseInteger(){
        if(maxInteger==null){
            return integral;
        }
        return integral.compareTo(maxInteger)<0?integral:maxInteger;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public BigDecimal getConvertValue() {
        return convertValue;
    }

    public void setConvertValue(BigDecimal convertValue) {
        this.convertValue = convertValue;
    }


    public boolean isActived() {
        return actived;
    }


    public void setActived(boolean actived) {
        this.actived = actived;
    }

        public boolean isUsed() {
        return tradePrivilegeExtra != null && tradePrivilegeExtra.getUseStatus() == PrivilegeUseStatus.USED;
    }


    public boolean isValid() {
        return tradePrivilege != null && tradePrivilege.getStatusFlag() == StatusFlag.VALID;
    }

    @Override
    public IntegralCashPrivilegeVo clone() {
        IntegralCashPrivilegeVo vo = new IntegralCashPrivilegeVo();
        try {
            if (tradePrivilege != null) {
                vo.setTradePrivilege(Beans.copyEntity(tradePrivilege, new TradePrivilege()));
            }
            if (tradePrivilegeExtra != null) {
                vo.setTradePrivilegeExtra(Beans.copyEntity(tradePrivilegeExtra, new TradePrivilegeExtra()));
            }
            vo.setIntegral(integral);
            vo.setActived(actived);
            return vo;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error!", e);
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tradePrivilege == null) ? 0 : tradePrivilege.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IntegralCashPrivilegeVo other = (IntegralCashPrivilegeVo) obj;
        if (tradePrivilege == null) {
            if (other.tradePrivilege != null)
                return false;
        } else if (!tradePrivilege.equals(other.tradePrivilege))
            return false;
        return true;
    }

    public static boolean isNotNull(IntegralCashPrivilegeVo vo) {
        return vo != null && vo.tradePrivilege != null;
    }

    public boolean isPrivilegeValid() {
        return tradePrivilege != null && tradePrivilege.isValid();
    }

}
