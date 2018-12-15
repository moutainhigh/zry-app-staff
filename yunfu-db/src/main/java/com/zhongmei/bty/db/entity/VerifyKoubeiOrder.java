package com.zhongmei.bty.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;

@DatabaseTable(tableName = "verify_koubei_order")
public class VerifyKoubeiOrder extends ShopEntityBase {

    public interface $ extends BasicEntityBase.$ {
        String tradeId = "tradeId";
        String tradeUuid = "tradeUuid";
        String verifyStatus = "verifyStatus";
    }

    @DatabaseField(columnName = $.tradeId)
    private Long tradeId; //订单ID |是

    @DatabaseField(columnName = $.tradeUuid)
    private String tradeUuid; //订单UUID |是

    @DatabaseField(columnName = $.verifyStatus)
    private Integer verifyStatus; //核销状态(1-待核销,2-核销成功) |是

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

    public VerifyStatus getVerifyStatus() {
        return ValueEnums.toEnum(VerifyStatus.class, verifyStatus);
    }

    public void setVerifyStatus(VerifyStatus verifyStatus) {
        this.verifyStatus = ValueEnums.toValue(verifyStatus);
    }

    public enum VerifyStatus implements ValueEnum<Integer> {
        /**
         * 待核销
         */
        VERIFY_WAITING(1),
        /**
         * 核销成功
         */
        VERIFY_SUCCESS(2),

        /**
         * 未知的值
         *
         * @deprecated 为了避免转为enum出错而设置，不应直接使用
         */
        @Deprecated
        __UNKNOWN__;

        private final Helper<Integer> helper;

        private VerifyStatus(Integer value) {
            helper = Helper.valueHelper(value);
        }

        private VerifyStatus() {
            helper = Helper.unknownHelper();
        }

        @Override
        public Integer value() {
            return helper.value();
        }

        @Override
        public boolean equalsValue(Integer value) {
            return helper.equalsValue(this, value);
        }

        @Override
        public boolean isUnknownEnum() {
            return helper.isUnknownEnum();
        }

        @Override
        public void setUnknownValue(Integer value) {
            helper.setUnknownValue(value);
        }

        @Override
        public String toString() {
            return "" + value();
        }

    }
}
