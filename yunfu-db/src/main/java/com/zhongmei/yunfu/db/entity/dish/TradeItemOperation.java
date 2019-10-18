package com.zhongmei.yunfu.db.entity.dish;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.ServerEntityBase;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.PrintStatus;

@DatabaseTable(tableName = "trade_item_operation")
public class TradeItemOperation extends ServerEntityBase implements ICreator, IUpdator {


    private static final long serialVersionUID = 1L;

    public interface $ extends ServerEntityBase.$ {

        public static final String tradeItemId = "trade_item_id";

        public static final String opType = "op_type";

        public static final String printStatus = "print_status";

        public static final String printOperationId = "print_operation_id";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";
    }

    @DatabaseField(columnName = "trade_item_id", canBeNull = false)
    private Long tradeItemId;

    @DatabaseField(columnName = "op_type", canBeNull = false)
    private Integer opType;

    @DatabaseField(columnName = "print_status", canBeNull = false)
    private Integer printStatus;

    @DatabaseField(columnName = "print_operation_id")
    private Long printOperationId;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    private Long batchId;

    public Long getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(Long tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public PrintOperationOpType getOpType() {
        return ValueEnums.toEnum(PrintOperationOpType.class, opType);
    }

    public void setOpType(PrintOperationOpType opType) {
        this.opType = ValueEnums.toValue(opType);
    }

    public PrintStatus getPrintStatus() {
        return ValueEnums.toEnum(PrintStatus.class, printStatus);
    }

    public void setPrintStatus(PrintStatus printStatus) {
        this.printStatus = ValueEnums.toValue(printStatus);
    }

    public Long getPrintOperationId() {
        return printOperationId;
    }

    public void setPrintOperationId(Long printOperationId) {
        this.printOperationId = printOperationId;
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

    private String tradeItemUuid;

    public String getTradeItemUuid() {
        return tradeItemUuid;
    }

    public void setTradeItemUuid(String tradeItemUuid) {
        this.tradeItemUuid = tradeItemUuid;
    }


    private String skuName;
    private String tableName;
    private String serialNumber;

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(tradeItemId, opType, printStatus);
    }
}
