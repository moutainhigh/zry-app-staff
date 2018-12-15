package com.zhongmei.bty.basemodule.commonbusiness.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

/**
 * TradeItemProperty is a ORMLite bean type. Corresponds to the database table "reason_setting"
 */
@DatabaseTable(tableName = "reason_setting")
public class ReasonSetting extends BasicEntityBase implements ICreator, IUpdator {


    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "reason_setting"
     */
    public interface $ extends BasicEntityBase.$ {

        /**
         * content
         */
        public static final String content = "content";

        /**
         * sort
         */
        public static final String sort = "sort";

        /**
         * source
         */
        public static final String source = "source";

        /**
         * type
         */
        public static final String type = "type";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * uuid
         */
        public static final String uuid = "uuid";

    }

    @DatabaseField(columnName = "content", canBeNull = false)
    private String content;

    @DatabaseField(columnName = "sort")
    private Integer sort;

    @DatabaseField(columnName = "source")
    private Integer source = ReasonSource.ZHONGMEI.value();

    @DatabaseField(columnName = "type", canBeNull = false)
    private Integer type;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "uuid")
    private String uuid;

    // v8.12.0 口碑拒绝理由码
    private String contentCode;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public ReasonSource getSource() {
        return ValueEnums.toEnum(ReasonSource.class, source);
    }

    public void setSource(ReasonSource source) {
        this.source = ValueEnums.toValue(source);
    }

    public ReasonType getType() {
        return ValueEnums.toEnum(ReasonType.class, type);
    }

    public void setType(ReasonType type) {
        this.type = ValueEnums.toValue(type);
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(content, type);
    }

    public String getContentCode() {
        return contentCode;
    }

    public void setContentCode(String contentCode) {
        this.contentCode = contentCode;
    }
}
