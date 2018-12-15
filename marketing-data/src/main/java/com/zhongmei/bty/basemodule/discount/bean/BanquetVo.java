package com.zhongmei.bty.basemodule.discount.bean;

import android.util.Log;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.context.util.NoProGuard;

/**
 * Created by demo on 2018/12/15
 * <p>
 * 宴请vo
 */

public class BanquetVo implements java.io.Serializable, NoProGuard {
    private static final String TAG = "BanquetVo";

    private static final long serialVersionUID = 1L;

    private TradePrivilege tradePrivilege;

    public TradePrivilege getTradePrivilege() {
        return tradePrivilege;
    }

    public void setTradePrivilege(TradePrivilege tradePrivilege) {
        this.tradePrivilege = tradePrivilege;
    }

    public static boolean isNotNull(BanquetVo vo) {
        return vo != null && vo.tradePrivilege != null;
    }

    public BanquetVo clone() {
        BanquetVo vo = new BanquetVo();
        try {
            if (tradePrivilege != null) {
                vo.setTradePrivilege(tradePrivilege.clone());
            }
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error!", e);
        }
        return vo;
    }

}
