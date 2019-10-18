package com.zhongmei.bty.data.operates.message.content;

import java.util.List;



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
