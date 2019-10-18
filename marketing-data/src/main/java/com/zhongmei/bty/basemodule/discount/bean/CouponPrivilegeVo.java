package com.zhongmei.bty.basemodule.discount.bean;

import android.util.Log;

import com.zhongmei.yunfu.db.entity.discount.CoupRule;
import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilegeExtra;
import com.zhongmei.yunfu.context.util.NoProGuard;
import com.zhongmei.yunfu.db.enums.PrivilegeUseStatus;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.Beans;

import java.util.List;


public class CouponPrivilegeVo implements java.io.Serializable, NoProGuard {


    private static final long serialVersionUID = 1L;
    private static final String TAG = CouponPrivilegeVo.class.getSimpleName();

    private TradePrivilege tradePrivilege;


    private TradePrivilegeExtra tradePrivilegeExtra;

    private boolean actived = false;

        private Coupon coupon;

    private transient ShopcartItem shopcartItem;

    private Long couponInfoId;
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

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public ShopcartItem getShopcartItem() {
        return shopcartItem;
    }

    public void setShopcartItem(ShopcartItem shopcartItem) {
        this.shopcartItem = shopcartItem;
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

    public Long getCouponInfoId() {
        return couponInfoId;
    }

    public void setCouponInfoId(Long couponInfoId) {
        this.couponInfoId = couponInfoId;
    }

    @Override
    public CouponPrivilegeVo clone() {
        CouponPrivilegeVo vo = new CouponPrivilegeVo();
        try {
            if (tradePrivilege != null) {
                TradePrivilege newPrivilege = new TradePrivilege();
                Beans.copyProperties(tradePrivilege, newPrivilege);
                vo.setTradePrivilege(newPrivilege);
            }
            vo.setCoupon(coupon);
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
        CouponPrivilegeVo other = (CouponPrivilegeVo) obj;
        if (tradePrivilege == null) {
            if (other.tradePrivilege != null)
                return false;
        } else if (!tradePrivilege.equals(other.tradePrivilege))
            return false;
        return true;
    }

    public static boolean isNotNull(CouponPrivilegeVo vo) {
        return vo != null && vo.tradePrivilege != null;
    }

    public boolean isPrivilegeValid() {
        return tradePrivilege != null && tradePrivilege.isValid();
    }
}
