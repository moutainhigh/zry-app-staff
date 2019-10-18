package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import java.util.List;


public class AnonymousCardStorePayModeResp {
    private List<AnonymousCardStorePayModeVo> tempCardStorePayModeVos;

    public List<AnonymousCardStorePayModeVo> getTempCardStorePayModeVos() {
        return tempCardStorePayModeVos;
    }

    public void setTempCardStorePayModeVos(List<AnonymousCardStorePayModeVo> tempCardStorePayModeVos) {
        this.tempCardStorePayModeVos = tempCardStorePayModeVos;
    }
}
