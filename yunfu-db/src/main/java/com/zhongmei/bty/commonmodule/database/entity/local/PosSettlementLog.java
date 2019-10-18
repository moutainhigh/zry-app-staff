package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;


@DatabaseTable(tableName = "pos_settlement_log")
public class PosSettlementLog extends LocalEntityBase {

    private static final long serialVersionUID = 1L;


    public interface $ extends LocalEntityBase.$ {


        public static final String terminalNumber = "terminal_number";


        public static final String transDate = "trans_date";


        public static final String transTime = "trans_time";

    }

    @DatabaseField(columnName = "terminal_number", canBeNull = false)
    private String terminalNumber;

    @DatabaseField(columnName = "trans_date", canBeNull = false)
    private String transDate;

    @DatabaseField(columnName = "trans_time", canBeNull = false)
    private String transTime;

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && EntityBase.checkNonNull(terminalNumber, transDate, transTime);
    }
}
