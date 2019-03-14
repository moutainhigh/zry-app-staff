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

/**
 * @Date：2015年9月15日 下午6:38:57
 * @Description:
 * @Version: 1.0
 */
public class IntegralCashPrivilegeVo implements java.io.Serializable, NoProGuard {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String TAG = IntegralCashPrivilegeVo.class.getSimpleName();

    private TradePrivilege tradePrivilege;

    private BigDecimal convertValue;//抵用规则 ，多少积分1元钱

    private BigDecimal maxInteger;//最高抵用积分限制 null表示无限制

    /**
     * 优惠拓展信息
     */
    private TradePrivilegeExtra tradePrivilegeExtra;

    /**
     * 当前可用积分
     */
    private BigDecimal integral;

    private boolean actived = false;

    private Long ruleId;
    // ***********************************************************
    // *  特别注意！添加属性时要注意修改clone()方法
    // ***********************************************************

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
//			exchangeIntegralValue = rule.getExchangeIntegralValue();//抵消积分
//			exchangeCashValue = rule.getExchangeCashValue(); //抵现金额
//			limitIntegral = rule.getLimitIntegral();//限制可用积分
//			limitType = rule.getLimitType();//抵现规则 1,无上限，2积分个数限制 3 金额百分比限制

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
//			exchangeIntegralValue = rule.getExchangeIntegralValue();
//			exchangeCashValue = rule.getExchangeCashValue();
//			limitIntegral = rule.getLimitIntegral();
//			limitType = rule.getLimitType();

            ruleId = rule.getId();
        }
    }

    public boolean hasRule() {
        return convertValue != null;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    /**
     * 获取可用的积分数量
     * @return
     */
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

    /**
     * 如果此优惠符合规格要求则返回true
     *
     * @return
     */
    public boolean isActived() {
        return actived;
    }

    /**
     * 设置优惠是否是激活的（符合规格要求为已激活状态）
     *
     * @param actived
     */
    public void setActived(boolean actived) {
        this.actived = actived;
    }

    //是否被核销、使用
    public boolean isUsed() {
        return tradePrivilegeExtra != null && tradePrivilegeExtra.getUseStatus() == PrivilegeUseStatus.USED;
    }

    /**
     * tradePrivilege.statusFlag 为 VALID 时返回true
     *
     * @return
     */
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
//			vo.exchangeIntegralValue = exchangeIntegralValue;
//			vo.exchangeCashValue = exchangeCashValue;
//			vo.limitIntegral = limitIntegral;
//			vo.limitType = limitType;
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
