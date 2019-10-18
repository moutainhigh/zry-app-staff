package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.StatusFlag;


@DatabaseTable(tableName = "new_document_template")
public class NewDocumentTemplate extends IdEntityBase {
    private static final long serialVersionUID = 1L;

    public static int ENABLE_TRUE = 1;

    public static int ENABLE_FALSE = 2;


    public interface $ extends IdEntityBase.$ {

        String url = "url";

        String name = "name";


        String value = "value";


        String documentType = "document_type";


        String baseTemplet = "base_templet";


        String content = "content";


        String defaultTemplet = "default_templet";


        String enableFlag = "enable_flag";


        String statusFlag = "status_flag";


        String brandIdenty = "brand_identy";


        String shopIdenty = "shop_identy";


        String serverCreateTime = "server_create_time";


        String serverUpdateTime = "server_update_time";


        String creatorId = "creator_id";


        String creatorName = "creator_name";


        String updatorId = "updator_id";


        String updatorName = "updator_name";


        String templateVersion = "template_version";
    }

    @DatabaseField(columnName = $.url, canBeNull = false)
    private String url;

    @DatabaseField(columnName = $.name, canBeNull = false)
    private String name;

    @DatabaseField(columnName = $.documentType, canBeNull = false)
    private Integer documentType;

    @DatabaseField(columnName = $.baseTemplet, canBeNull = false)
    private Long baseTemplet;

    @DatabaseField(columnName = $.content, canBeNull = false)
    private String content;

    @DatabaseField(columnName = $.defaultTemplet, canBeNull = false)
    private Integer defaultTemplet;

    @DatabaseField(columnName = $.enableFlag, canBeNull = false)
    private Integer enableFlag;

    @DatabaseField(columnName = $.brandIdenty, canBeNull = false)
    private Long brandIdenty;

    @DatabaseField(columnName = $.shopIdenty, canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = $.serverCreateTime, canBeNull = false)
    private Long serverCreateTime;

    @DatabaseField(columnName = $.serverUpdateTime, canBeNull = false)
    private Long serverUpdateTime;

    @DatabaseField(columnName = $.creatorId)
    private Long creatorId;

    @DatabaseField(columnName = $.creatorName)
    private String creatorName;

    @DatabaseField(columnName = $.updatorId)
    private Long updatorId;

    @DatabaseField(columnName = $.updatorName)
    private String updatorName;

    @DatabaseField(columnName = $.statusFlag, canBeNull = false)
    private Integer statusFlag;

    @DatabaseField(columnName = $.templateVersion)
    private Integer templateVersion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    public Long getBaseTemplet() {
        return baseTemplet;
    }

    public void setBaseTemplet(Long baseTemplet) {
        this.baseTemplet = baseTemplet;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDefaultTemplet() {
        return defaultTemplet;
    }

    public void setDefaultTemplet(Integer defaultTemplet) {
        this.defaultTemplet = defaultTemplet;
    }

    public Integer getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Integer enableFlag) {
        this.enableFlag = enableFlag;
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

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(url, name, documentType, baseTemplet,
                content, defaultTemplet, enableFlag, brandIdenty, shopIdenty, serverCreateTime, serverUpdateTime, statusFlag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        NewDocumentTemplate that = (NewDocumentTemplate) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (documentType != null ? !documentType.equals(that.documentType) : that.documentType != null)
            return false;
        if (baseTemplet != null ? !baseTemplet.equals(that.baseTemplet) : that.baseTemplet != null)
            return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (defaultTemplet != null ? !defaultTemplet.equals(that.defaultTemplet) : that.defaultTemplet != null)
            return false;
        if (enableFlag != null ? !enableFlag.equals(that.enableFlag) : that.enableFlag != null)
            return false;
        if (brandIdenty != null ? !brandIdenty.equals(that.brandIdenty) : that.brandIdenty != null)
            return false;
        if (shopIdenty != null ? !shopIdenty.equals(that.shopIdenty) : that.shopIdenty != null)
            return false;
        if (serverUpdateTime != null ? !serverUpdateTime.equals(that.serverUpdateTime) : that.serverUpdateTime != null)
            return false;
        if (creatorId != null ? !creatorId.equals(that.creatorId) : that.creatorId != null)
            return false;
        if (creatorName != null ? !creatorName.equals(that.creatorName) : that.creatorName != null)
            return false;
        if (updatorId != null ? !updatorId.equals(that.updatorId) : that.updatorId != null)
            return false;
        if (updatorName != null ? !updatorName.equals(that.updatorName) : that.updatorName != null)
            return false;
        return statusFlag != null ? statusFlag.equals(that.statusFlag) : that.statusFlag == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (documentType != null ? documentType.hashCode() : 0);
        result = 31 * result + (baseTemplet != null ? baseTemplet.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (defaultTemplet != null ? defaultTemplet.hashCode() : 0);
        result = 31 * result + (enableFlag != null ? enableFlag.hashCode() : 0);
        result = 31 * result + (brandIdenty != null ? brandIdenty.hashCode() : 0);
        result = 31 * result + (shopIdenty != null ? shopIdenty.hashCode() : 0);
        result = 31 * result + (serverCreateTime != null ? serverCreateTime.hashCode() : 0);
        result = 31 * result + (serverUpdateTime != null ? serverUpdateTime.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        result = 31 * result + (creatorName != null ? creatorName.hashCode() : 0);
        result = 31 * result + (updatorId != null ? updatorId.hashCode() : 0);
        result = 31 * result + (updatorName != null ? updatorName.hashCode() : 0);
        result = 31 * result + (statusFlag != null ? statusFlag.hashCode() : 0);
        return result;
    }
}
