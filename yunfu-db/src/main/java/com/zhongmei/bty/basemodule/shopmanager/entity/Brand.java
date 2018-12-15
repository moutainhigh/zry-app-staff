package com.zhongmei.bty.basemodule.shopmanager.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;

/**
 * @Date： 2016/11/30
 * @Description:品牌表
 * @Version: 1.0
 */
@DatabaseTable(tableName = "brand")
public class Brand extends IdEntityBase {


    public interface $ {

        /**
         * weixinAppId
         */
        String weixinAppId = "weixinAppId";
        /**
         * server_create_time
         */
        String serverCreateTime = "server_create_time";

        /**
         * server_update_time
         */
        String serverUpdateTime = "server_update_time";

        /**
         * status
         */
        String statusFlag = "status_flag";

    }

    /**
     * 微信AppId
     */
    @DatabaseField(columnName = "weixinAppId")
    private String weixinAppId;

    /**
     * 服务器创建时间
     */
    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    /**
     * 服务器最后修改时间
     */
    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    /**
     * 状态
     */
    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWeixinAppId() {
        return weixinAppId;
    }

    public void setWeixinAppId(String weixinAppId) {
        this.weixinAppId = weixinAppId;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    @Override
    public boolean isValid() {

        return statusFlag == 0 ? true : false;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(statusFlag);
    }
}
