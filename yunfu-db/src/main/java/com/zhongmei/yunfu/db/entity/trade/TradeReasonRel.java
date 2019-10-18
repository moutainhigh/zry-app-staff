package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.UuidEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.StatusFlag;


@DatabaseTable(tableName = "trade_reason_rel")
public class TradeReasonRel extends UuidEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends UuidEntityBase.$ {


        String creatorId = "creator_id";


        String creatorName = "creator_name";


        String updatorId = "updator_id";


        String updatorName = "updator_name";


        String reasonContent = "reason_content";


        String reasonId = "reason_id";


        String operateType = "operate_type";


        String relateId = "relate_id";


        String relateUuid = "relate_uuid";


        String id = "id";


        String statusFlag = "status_flag";


        String serverCreateTime = "server_create_time";


        String serverUpdateTime = "server_update_time";


        String brandIdenty = "brand_identy";


        String shopIdenty = "shop_identy";


        String deviceIdenty = "device_identy";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "reason_content")
    private String reasonContent;

    @DatabaseField(columnName = "reason_id")
    private Long reasonId;

    @DatabaseField(columnName = "operate_type", canBeNull = false)
    private Integer operateType;

    @DatabaseField(columnName = "relate_id")
    private Long relateId;

    @DatabaseField(columnName = "relate_uuid")
    private String relateUuid;

    @DatabaseField(columnName = "id")
    private Long id;

    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;

    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "device_identy")
    private String deviceIdenty;

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public String getReasonContent() {
        return reasonContent;
    }

    public void setReasonContent(String reasonContent) {
        this.reasonContent = reasonContent;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public OperateType getOperateType() {
        return ValueEnums.toEnum(OperateType.class, operateType);
    }

    public void setOperateType(OperateType operateType) {
        this.operateType = ValueEnums.toValue(operateType);
    }

    public Long getRelateId() {
        return relateId;
    }

    public void setRelateId(Long relateId) {
        this.relateId = relateId;
    }

    public String getRelateUuid() {
        return relateUuid;
    }

    public void setRelateUuid(String relateUuid) {
        this.relateUuid = relateUuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        setDeviceIdenty(BaseApplication.sInstance.getDeviceIdenty());
        setChanged(true);
        if (this instanceof ICreator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                ICreator creator = (ICreator) this;
                creator.setCreatorId(user.getId());
                creator.setCreatorName(user.getName());
            }
        }
    }

    public void validateUpdate() {
        setChanged(true);
        if (this instanceof IUpdator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                IUpdator updator = (IUpdator) this;
                updator.setUpdatorId(user.getId());
                updator.setUpdatorName(user.getName());
            }
        }
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(operateType, statusFlag, brandIdenty, shopIdenty);
    }
}
