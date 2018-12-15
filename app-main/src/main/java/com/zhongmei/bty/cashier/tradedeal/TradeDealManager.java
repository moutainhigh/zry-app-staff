package com.zhongmei.bty.cashier.tradedeal;

import android.content.Context;
import android.util.Log;

import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingBusinessType;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingOperateType;
import com.zhongmei.bty.basemodule.trade.bean.TradeDealSettingVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;

import java.util.Collections;
import java.util.List;

/**
 * 订单处理管理类,如:外卖自动接单、外卖自动拒单
 */
public class TradeDealManager {
    private static final String TAG = TradeDealManager.class.getSimpleName();

    private Context mContext;

    public TradeDealManager(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 查找一笔自动拒绝的记录
     *
     * @param startTime 开始时间戳
     * @param endTime   结束时间戳
     * @param maxRows   最大行数
     * @return TradeVo
     * @throws Exception
     */
    public List<TradeVo> findAutoRejectRecord(long startTime, long endTime, long maxRows) {
        try {
            TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
            return tradeDal.findAutoRejectRecord(startTime, endTime, maxRows);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }

        return Collections.emptyList();
    }

    /**
     * 查询订单自动接受或自动拒单设置项
     *
     * @param tradeDealSettingOperateType 操作类型
     * @return
     */
    public TradeDealSettingVo findTradeDealSetting(TradeDealSettingOperateType tradeDealSettingOperateType) {
        return ServerSettingManager.getTradeDealSettingVo(TradeDealSettingBusinessType.TAKEAWAY, tradeDealSettingOperateType);
    }
}
