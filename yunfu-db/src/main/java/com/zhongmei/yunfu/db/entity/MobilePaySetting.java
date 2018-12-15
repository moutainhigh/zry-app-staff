package com.zhongmei.yunfu.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;

/**
 * Created by demo on 2018/12/15
 * wallet开通移动支付设置信息
 */
@DatabaseTable(tableName = "mobile_pay_setting")
public class MobilePaySetting extends IdEntityBase {

    public interface $ extends IdEntityBase.$ {
        String brandId = "brand_id";
        String shopId = "shop_id";
        String enumValue = "enum_value";
        String payOrgCode = "pay_org_code";
        String payModeCode = "pay_mode_code";
        String isSupportOneCodePay = "is_support_onecodepay";
        String enable = "enable";
        String statusFlag = "status_flag";

    }

    @DatabaseField(columnName = $.brandId)
    private Long brandId;

    @DatabaseField(columnName = $.shopId)
    private Long shopId;

    @DatabaseField(columnName = $.enumValue, canBeNull = false)
    private Long enumValue;

    @DatabaseField(columnName = $.payOrgCode)
    private String payOrgCode;

    @DatabaseField(columnName = "pay_org_name")
    private String payOrgName;

    @DatabaseField(columnName = $.payModeCode, canBeNull = false)
    private String payModeCode;

    @DatabaseField(columnName = "pay_mode_name")
    private String payModeName;

    @DatabaseField(columnName = $.isSupportOneCodePay)
    private int isSupportOneCodePay;

    @DatabaseField(columnName = $.enable, canBeNull = false)
    private int enable;

    @DatabaseField(columnName = $.statusFlag, canBeNull = false)
    protected Integer statusFlag;

    @Override
    public boolean isValid() {
        return statusFlag == 1;
    }

    @Override
    public Long verValue() {//每次同步都覆盖
        return getId();
    }

    public Long getBrandId() {
        return brandId;
    }

    public Long getShopId() {
        return shopId;
    }

    public String getPayOrgCode() {
        return payOrgCode;
    }

    public String getPayOrgName() {
        return payOrgName;
    }

    public String getPayModeCode() {
        return payModeCode;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public boolean isSupportOneCodePay() {
        return isSupportOneCodePay == 1;
    }

    public PayModeId getPayModeId() {
        return ValueEnums.toEnum(PayModeId.class, enumValue);
    }
}
