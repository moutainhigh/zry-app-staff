package com.zhongmei.bty.basemodule.print.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.ServerEntityBase;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.PrintStatus;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "print_operation")
public class PrintOperation extends ServerEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends ServerEntityBase.$ {



        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String opType = "op_type";


        public static final String sourceTradeId = "source_trade_id";


        public static final String sourceTableId = "source_table_id";


        public static final String targetTradeId = "target_trade_id";


        public static final String targetTableId = "target_table_id";


        public static final String sourceSerialNumber = "source_serial_number";


        public static final String targetSerialNumber = "target_serial_number";


        public static final String printStatus = "print_status";


        public static final String cashPoints = "cash_points";


        public static final String extendsStr = "extends_str";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "op_type", canBeNull = false)
    private Integer opType;

    @DatabaseField(columnName = "source_trade_id", canBeNull = false, index = true)
    private Long sourceTradeId;

    @DatabaseField(columnName = "target_trade_id")
    private Long targetTradeId;

    @DatabaseField(columnName = "source_table_id", canBeNull = false)
    private Long sourceTableId;

    @DatabaseField(columnName = "target_table_id")
    private Long targetTableId;

    @DatabaseField(columnName = "source_serial_number")
    private String sourceSerialNumber;

    @DatabaseField(columnName = "target_serial_number")
    private String targetSerialNumber;

    @DatabaseField(columnName = "print_status", canBeNull = false)
    private Integer printStatus;


    @DatabaseField(columnName = "extends_str")
    private String extendsStr;


    @DatabaseField(columnName = "cash_points")
    private String cashPoints;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public Integer localPrintStatus;
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

    public PrintOperationOpType getOpType() {
        return ValueEnums.toEnum(PrintOperationOpType.class, opType);
    }

    public void setOpType(PrintOperationOpType opType) {
        this.opType = ValueEnums.toValue(opType);
    }

    public Long getSourceTradeId() {
        return sourceTradeId;
    }

    public void setSourceTradeId(Long sourceTradeId) {
        this.sourceTradeId = sourceTradeId;
    }

    public Long getTargetTradeId() {
        return targetTradeId;
    }

    public void setTargetTradeId(Long targetTradeId) {
        this.targetTradeId = targetTradeId;
    }

    public Long getSourceTableId() {
        return sourceTableId;
    }

    public void setSourceTableId(Long sourceTableId) {
        this.sourceTableId = sourceTableId;
    }

    public Long getTargetTableId() {
        return targetTableId;
    }

    public void setTargetTableId(Long targetTableId) {
        this.targetTableId = targetTableId;
    }

    public String getSourceSerialNumber() {
        return sourceSerialNumber;
    }

    public void setSourceSerialNumber(String sourceSerialNumber) {
        this.sourceSerialNumber = sourceSerialNumber;
    }

    public String getTargetSerialNumber() {
        return targetSerialNumber;
    }

    public void setTargetSerialNumber(String targetSerialNumber) {
        this.targetSerialNumber = targetSerialNumber;
    }

    public PrintStatus getPrintStatus() {
        return ValueEnums.toEnum(PrintStatus.class, printStatus);
    }

    public void setPrintStatus(PrintStatus printStatus) {
        this.printStatus = ValueEnums.toValue(printStatus);
    }

    public String getCashPoints() {
        return cashPoints;
    }

    public void setCashPoints(String cashPoints) {
        this.cashPoints = cashPoints;
    }

    public String getExtendsStr() {
        return extendsStr;
    }

    public void setExtendsStr(String extendsStr) {
        this.extendsStr = extendsStr;
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

    public PrintStatus getLocalPrintStatus() {
        return ValueEnums.toEnum(PrintStatus.class, localPrintStatus);
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(opType, sourceTradeId, sourceTableId, printStatus);
    }
}
