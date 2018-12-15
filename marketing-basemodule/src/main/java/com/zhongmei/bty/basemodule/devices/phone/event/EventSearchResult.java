package com.zhongmei.bty.basemodule.devices.phone.event;

import java.util.List;

import com.zhongmei.bty.basemodule.devices.phone.bean.CalmPhoneInfo;
import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager.PhoneType;

/**
 * {@link ICalmPhone#search()} {@link ICalmPhone#search(PhoneType)}执行完毕后回调的搜索结果 <br />
 * 暂时没有处理扫描超时的情况. 即默认此事件一定都会正常返回 </br>
 *
 * @date 2014-8-6
 */
public class EventSearchResult {

    private PhoneType type;
    private List<CalmPhoneInfo> calmPhoneInfos;

    public EventSearchResult(PhoneType type,
                             List<CalmPhoneInfo> calmPhoneInfos) {
        this.type = type;
        this.calmPhoneInfos = calmPhoneInfos;
    }

    public PhoneType getType() {
        return this.type;
    }

    public List<CalmPhoneInfo> getCalmPhoneInfos() {
        return calmPhoneInfos;
    }

    public boolean isEmpty() {
        return calmPhoneInfos == null || calmPhoneInfos.size() == 0;
    }

}
