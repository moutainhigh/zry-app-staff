package com.zhongmei.bty.commonmodule.database.entity.base;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * @date:2016年2月15日下午3:59:44
 */
public abstract class UserEntityBase extends IdEntityBase {

    /**
     * @date：2016年2月15日 下午4:08:47
     * @Description:TODO
     */
    private static final long serialVersionUID = 1L;

    public interface $ extends IdEntityBase.$ {
        public static final String creatorId = "creator_id";

        public static final String creatorName = "creator_name";

        public static final String updatorId = "updator_id";

        public static final String updatorName = "updator_name";

        public static final String statusFlag = "status_flag";

        /**
         * server_create_time
         */
        public static final String serverCreateTime = "server_create_time";

        /**
         * server_update_time
         */
        public static final String serverUpdateTime = "server_update_time";
    }

    /**
     * 创建者id.
     */
    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    /**
     * 创建者名称.
     */
    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    /**
     * 更新者id.
     */
    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    /**
     * 更新人名称.
     */
    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    /**
     * 状态
     */
    @DatabaseField(columnName = "status_flag")
    private Integer statusFlag;

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
     * Set the 创建者id.
     *
     * @param creatorId 创建者id
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * Get the 创建者id.
     *
     * @return 创建者id
     */
    public Long getCreatorId() {
        return this.creatorId;
    }

    /**
     * Set the 创建者名称.
     *
     * @param creatorName 创建者名称
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * Get the 创建者名称.
     *
     * @return 创建者名称
     */
    public String getCreatorName() {
        return this.creatorName;
    }

    /**
     * Set the 更新者id.
     *
     * @param updatorId 更新者id
     */
    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    /**
     * Get the 更新者id.
     *
     * @return 更新者id
     */
    public Long getUpdatorId() {
        return this.updatorId;
    }

    /**
     * Set the 更新人名称.
     *
     * @param updatorName 更新人名称
     */
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    /**
     * Get the 更新人名称.
     *
     * @return 更新人名称
     */
    public String getUpdatorName() {
        return this.updatorName;
    }

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
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

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}
