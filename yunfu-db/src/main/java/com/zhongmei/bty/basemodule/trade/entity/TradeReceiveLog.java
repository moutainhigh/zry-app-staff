package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.Option;



@DatabaseTable(tableName = "trade_receive")
public class TradeReceiveLog extends DataEntityBase implements Option {

    @Override
    public void onOption() {
        if (getUuid() == null) {
            setUuid(String.valueOf(getId()));
        }
    }


    public interface $ extends DataEntityBase.$ {


        public static final String tradeId = "trade_id";



        public static final String tradeUuid = "trade_uuid";


        public static final String opSourceId = "op_source_id";


        public static final String opType = "op_type";


        public static final String businessType = "business_type";


        public static final String receiverSource = "receive_source";
    }

    @DatabaseField(columnName = "trade_id", canBeNull = false)
    private Long tradeId;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "op_source_id")
    private Integer opSourceId;

    @DatabaseField(columnName = "op_type")
    private Integer opType;

    @DatabaseField(columnName = "business_type", canBeNull = false)
    private Integer businessType;

    @DatabaseField(columnName = "receive_source", canBeNull = false)
    private Integer receiveSource;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public Integer getOpType() {
        return opType;
    }

    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }


    public Integer getOpSourceId() {
        return opSourceId;
    }

    public void setOpSourceId(Integer opSourceId) {
        this.opSourceId = opSourceId;
    }
}
