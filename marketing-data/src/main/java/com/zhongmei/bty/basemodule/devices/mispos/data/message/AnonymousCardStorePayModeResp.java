package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import java.util.List;

/**
 * 查询匿名卡储值支付方式相应结果
 */
public class AnonymousCardStorePayModeResp {
    private List<AnonymousCardStorePayModeVo> tempCardStorePayModeVos;

    public List<AnonymousCardStorePayModeVo> getTempCardStorePayModeVos() {
        return tempCardStorePayModeVos;
    }

    public void setTempCardStorePayModeVos(List<AnonymousCardStorePayModeVo> tempCardStorePayModeVos) {
        this.tempCardStorePayModeVos = tempCardStorePayModeVos;
    }
}
