package com.zhongmei.bty.basemodule.discount.bean;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilegeExtra;
import com.zhongmei.yunfu.context.util.NoProGuard;
import com.zhongmei.yunfu.db.enums.PrivilegeUseStatus;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * @Date：2016年6月30日 下午6:32:51
 * @Description: 微信卡卷操作对象
 * @Version: 1.0
 */
public class WeiXinCouponsVo implements java.io.Serializable, NoProGuard {

    /**
     * @date：2016年6月30日 下午6:32:48
     * @Description:TODO
     */
    private static final long serialVersionUID = 1L;

    private TradePrivilege mTradePrivilege;

    /**
     * 优惠拓展信息
     */
    private TradePrivilegeExtra tradePrivilegeExtra;

    private WeiXinCouponsInfo mWeiXinCouponsInfo;

    private boolean actived;//是否生效

    public boolean isValid() {
        return mTradePrivilege != null && mTradePrivilege.getStatusFlag() == StatusFlag.VALID;
    }

    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public TradePrivilege getmTradePrivilege() {
        return mTradePrivilege;
    }

    public void setmTradePrivilege(TradePrivilege mTradePrivilege) {
        this.mTradePrivilege = mTradePrivilege;
    }

    public TradePrivilegeExtra getTradePrivilegeExtra() {
        return tradePrivilegeExtra;
    }

    public void setTradePrivilegeExtra(TradePrivilegeExtra tradePrivilegeExtra) {
        this.tradePrivilegeExtra = tradePrivilegeExtra;
    }

    public WeiXinCouponsInfo getmWeiXinCouponsInfo() {
        return mWeiXinCouponsInfo;
    }

    public void setmWeiXinCouponsInfo(WeiXinCouponsInfo mWeiXinCouponsInfo) {
        this.mWeiXinCouponsInfo = mWeiXinCouponsInfo;
    }

    //是否被核销、使用
    public boolean isUsed() {
        return tradePrivilegeExtra != null && tradePrivilegeExtra.getUseStatus() == PrivilegeUseStatus.USED;
    }

}
