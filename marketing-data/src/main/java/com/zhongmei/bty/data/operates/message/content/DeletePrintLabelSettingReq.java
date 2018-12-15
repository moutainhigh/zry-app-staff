package com.zhongmei.bty.data.operates.message.content;

import java.util.List;

/**
 * 删除标签配置
 * Created by demo on 2018/12/15
 */

public class DeletePrintLabelSettingReq implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<Long> ids;


    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

}
