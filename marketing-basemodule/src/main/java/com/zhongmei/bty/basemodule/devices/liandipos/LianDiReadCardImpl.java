package com.zhongmei.bty.basemodule.devices.liandipos;

import android.content.Context;

import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLiandiposManager.OnTransListener;
import com.zhongmei.bty.basemodule.devices.mispos.watchdata.readcard.IReadCard;
import com.zhongmei.bty.basemodule.devices.mispos.watchdata.readcard.ReadCardResponseListener;

/**
 * 联迪读卡器读卡实现
 */

public class LianDiReadCardImpl implements IReadCard {
    @Override
    public void connectDevice(Context context, final ReadCardResponseListener listener, boolean isDelay
    ) {
        NewLiandiposManager.OnTransListener onTransListener = new OnTransListener() {
            @Override
            public void onActive() {
                if (listener != null) {
                    listener.onActive();
                }
            }

            @Override
            public void onStart() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            public void onConfirm(PosTransLog log) {
                if (listener != null) {
                    listener.onResponse(log.getCardNumber());
                }
            }

            @Override
            public void onFailure(NewLDResponse ldResponse) {
                if (listener != null) {
                    String errorCode = ldResponse != null ? ldResponse.getRejCode() : "";
                    String errorCodeExplain = ldResponse != null ? ldResponse.getRejCodeExplain() : "";
                    listener.onError(errorCode, errorCodeExplain);
                }
            }
        };

        NewLiandiposManager.getInstance().startReadCardID(context, onTransListener, isDelay);
    }

    @Override
    public void disconnectDevice() {
        NewLiandiposManager.getInstance().cancelConnect();
    }
}
