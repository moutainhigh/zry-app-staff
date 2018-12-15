package com.zhongmei.bty.data.operates.message.content;

/**
 * 封装正餐清台接口的请求数据
 *
 * @version: 1.0
 * @date 2015年9月23日
 */
public class ClearDinnertableReq {

    private Long tableId;
    private Long updatorId;
    private String updatorName;
    private Long serverUpdateTime;

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

}
