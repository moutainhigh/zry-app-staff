package com.zhongmei.bty.snack.offline;

import android.content.Context;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.net.RequestManager;

//import com.zhongmei.bty.push.PushPacket;

/**
 * Created by demo on 2018/12/15
 */
public class SnackImpl implements ISnack {
    private static final String TAG = Snack.class.getSimpleName() + "--->";

    //是否是异步订单
    @Override
    public boolean isOfflineTrade(TradeVo tradeVo) {
        return tradeVo != null && isOfflineTrade(tradeVo.getTrade());
    }

    @Override
    public boolean isOfflineTrade(Trade trade) {
        return trade != null
                && trade.getSourceChild() == SourceChild.OFFLINE;
    }

    /**
     * pos server 离线是否可用开关&&不是大客户
     *
     * @return
     */
    @Override
    public boolean isOfflineEnable() {
        return false;
    }

    @Override
    public boolean isSnackBusiness(TradeVo tradeVo) {
        return tradeVo != null
                && tradeVo.getTrade() != null
                && isSnackBusiness(tradeVo.getTrade().getBusinessType());

    }

    @Override
    public boolean isSnackBusiness(BusinessType businessType) {
        return false;
    }

    @Override
    public boolean isSnackBusiness(Trade trade) {
        return isSnackBusiness(trade.getBusinessType());
    }

    @Override
    public boolean netWorkAvailable() {
        return true; /*ServerHeartbeat.getInstance().getNetworkState()
                == ServerHeartbeat.NetworkState.NetworkAvailable;*/
    }

    @Override
    public boolean netWorkUnavailable() {
        return false; /*ServerHeartbeat.getInstance().getNetworkState()
                == ServerHeartbeat.NetworkState.NetworkUnavailable;*/
    }

    public SnackImpl(Context context) {
        /*Offline.init(context, new BizImpl());
        QSBackup.init(new QSBackupImpl(context, ServerAddressUtil.getInstance().getDataUploadUrl()));
        RequestManager.init(context, new RequestManager.RequestFilter() {
            @Override
            public boolean filter(Context context, Request<?> request, Object tag) {
                return isOfflineEnable()
                        && request.getInterceptEnable()
                        && Offline.intercept(new VolleyRequestHolder(request));
            }
        });*/

        RequestManager.init(context);
    }


    public void release() {
        //Offline.release();
    }

//    public  boolean handle(PushPacket pushPacket) {
//        return false; //Offline.intercept(new PushRequestHolder(pushPacket), 0L);
//    }

}
