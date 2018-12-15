package com.zhongmei.bty.data.operates.message.content;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class CheckVersionsAreaResp {
    private String maxAvaliableVersion;

    private List<String> otherAvaliableZone;

    public String getMaxAvaliableVersion() {
        return maxAvaliableVersion;
    }

    public List<String> getOtherAvaliableZone() {
        return otherAvaliableZone;
    }
}
