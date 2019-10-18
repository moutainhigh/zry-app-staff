package com.zhongmei.bty.basemodule.devices.mispos.data;

import java.math.BigDecimal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;


@DatabaseTable(tableName = "ec_card_setting_detail")
public class EcCardSettingDetail extends CrmBasicEntityBase
        implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends CrmBasicEntityBase.$ {


        public static final String cardLevelId = "card_level_id";


        public static final String fullValue = "full_value";


        public static final String sendValue = "send_value";


        public static final String sendRate = "send_rate";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";

    }


    @DatabaseField(columnName = "card_level_id")
    private Long cardLevelId;


    @DatabaseField(columnName = "full_value")
    private BigDecimal fullValue;


    @DatabaseField(columnName = "send_value")
    private BigDecimal sendValue;


    @DatabaseField(columnName = "send_rate")
    private BigDecimal sendRate;

    public Long getCardLevelId() {
        return cardLevelId;
    }

    public void setCardLevelId(Long cardLevelId) {
        this.cardLevelId = cardLevelId;
    }

    public BigDecimal getFullValue() {
        return fullValue;
    }

    public void setFullValue(BigDecimal fullValue) {
        this.fullValue = fullValue;
    }

    public BigDecimal getSendValue() {
        return sendValue;
    }

    public void setSendValue(BigDecimal sendValue) {
        this.sendValue = sendValue;
    }

    public BigDecimal getSendRate() {
        return sendRate;
    }

    public void setSendRate(BigDecimal sendRate) {
        this.sendRate = sendRate;
    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

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
