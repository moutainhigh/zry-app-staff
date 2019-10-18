package com.zhongmei.bty.basemodule.print.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.db.enums.PrintStatus;


@DatabaseTable(tableName = "print_operation_local_status")
public class PrintOperationLocalStatus extends EntityBase<Long> {

    public interface $ {

        String printOperationId = "print_operation_id";

        String localPrintStatus = "local_print_status";
    }

    @DatabaseField(columnName = "print_operation_id", id = true, canBeNull = false)
    public Long printOperationId;

    @DatabaseField(columnName = "local_print_status")
    public Integer localPrintStatus;

    public void setLocalPrintStatus(PrintStatus localPrintStatus) {
        this.localPrintStatus = ValueEnums.toValue(localPrintStatus);
    }

    public PrintStatus getLocalPrintStatus() {
        return ValueEnums.toEnum(PrintStatus.class, localPrintStatus);
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(localPrintStatus, localPrintStatus);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Long pkValue() {
        return printOperationId;
    }

    @Override
    public Long verValue() {
        return null;
    }
}
