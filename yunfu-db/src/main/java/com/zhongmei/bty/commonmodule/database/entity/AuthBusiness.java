package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.CommonEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

/**
 * 业务基础表
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "auth_business")
public class AuthBusiness extends CommonEntityBase implements ICreator, IUpdator {

    /**
     * The columns of table "auth_business"
     */
    public interface $ extends CommonEntityBase.$ {

        /**
         * name
         */
        public static final String name = "name";

        /**
         * code
         */
        public static final String code = "code";

        /**
         * description
         */
        public static final String description = "description";

        /**
         * source_flag
         */
        public static final String sourceFlag = "source_flag";

        /**
         * support_version
         */
        public static final String supportVersion = "support_version";

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

    }

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "code")
    private String code;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "support_version")
    private Integer supportVersion;

    @DatabaseField(columnName = "source_flag")
    private Integer sourceFlag;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

}
