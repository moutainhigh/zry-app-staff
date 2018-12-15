package com.zhongmei.bty.basemodule.devices.display.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * 对接erp的营销活动图片
 * <p>
 * CREATE TABLE `erp_pos_push_detail` (
 * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '副屏推送ID',
 * `relation_id` bigint(20) NOT NULL COMMENT 'POS副屏推送ID - 表erp_pos_push_list [id] 字段''',
 * `pos_pic` varchar(200) DEFAULT NULL COMMENT 'OnPOS图片url',
 * `pos_mini_pic` varchar(200) DEFAULT NULL COMMENT 'OnPOS mini图片url',
 * `title` varchar(40) DEFAULT NULL COMMENT '标题',
 * `start_time` timestamp NULL DEFAULT NULL COMMENT '展示起始时间',
 * `end_time` timestamp NULL DEFAULT NULL COMMENT '展示截止时间',
 * `target` tinyint(4) DEFAULT NULL COMMENT '目标终端类型 1-OnPOS 2-OnPOS mini 3-全选',
 * `status_flag` tinyint(4) NOT NULL COMMENT '消息状态 1-有效 2-无效',
 * `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
 * `server_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '消息更新时间',
 * `shop_identity` bigint(20) NOT NULL COMMENT '门店标识',
 * `brand_identity` bigint(20) DEFAULT NULL COMMENT '品牌标识',
 * PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='POS副屏推送详情表';
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "erp_pos_push_detail")
public class ErpPosPushDetail extends IdEntityBase {

    public interface $ extends IdEntityBase.$ {
        String relationId = "relation_id";
        String posPic = "pos_pic";
        String posMiniPic = "pos_mini_pic";
        String title = "title";
        String startTime = "start_time";
        String endTime = "end_time";
        String target = "target";
        String statusFlag = "status_flag";
        String serverCreateTime = "server_create_time";
        String serverUpdateTime = "server_update_time";
        String shopIdentity = "shop_identity";
        String brandIdentity = "brand_identity";
    }

    @DatabaseField(columnName = "relation_id")
    Long relationId;
    @DatabaseField(columnName = "pos_pic")
    String posPic;
    @DatabaseField(columnName = "pos_mini_pic")
    String posMiniPic;
    @DatabaseField(columnName = "title")
    String title;
    @DatabaseField(columnName = "start_time")
    Long startTime;
    @DatabaseField(columnName = "end_time")
    Long endTime;
    @DatabaseField(columnName = "target")
    Integer target;
    @DatabaseField(columnName = "status_flag")
    Integer statusFlag;
    @DatabaseField(columnName = "server_create_time")
    Long serverCreateTime;
    @DatabaseField(columnName = "server_update_time")
    Long serverUpdateTime;
    @DatabaseField(columnName = "shop_identity")
    String shopIdentity;
    @DatabaseField(columnName = "brand_identity")
    String brandIdentity;

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public String getPosPic() {
        return posPic;
    }

    public void setPosPic(String posPic) {
        this.posPic = posPic;
    }

    public String getPosMiniPic() {
        return posMiniPic;
    }

    public void setPosMiniPic(String posMiniPic) {
        this.posMiniPic = posMiniPic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
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

    public String getShopIdentity() {
        return shopIdentity;
    }

    public void setShopIdentity(String shopIdentity) {
        this.shopIdentity = shopIdentity;
    }

    public String getBrandIdentity() {
        return brandIdentity;
    }

    public void setBrandIdentity(String brandIdentity) {
        this.brandIdentity = brandIdentity;
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
