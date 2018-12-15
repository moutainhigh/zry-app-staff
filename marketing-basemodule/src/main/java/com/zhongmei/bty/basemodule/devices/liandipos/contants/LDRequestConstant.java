package com.zhongmei.bty.basemodule.devices.liandipos.contants;

/**
 * Created by demo on 2018/12/15
 */
public class LDRequestConstant {

    //收银机号
    public static final String POST_NUMBER = "PosNumber";

    //门店号
    public static final String STORE_NUMBER = "StoreNumber";

    //操作员号
    public static final String OPERATOR = "Operator";

    //卡号
    public static final String CARD_NUMBER = "CardNumber";

    //卡种代码（银行卡01，万商通联02……）
    public static final String CARD_TYPE = "CardType";

    //交易类型
    public static final String TRANS_TYPE = "TransType";

    //金额（12位，分为单位，不足左补0）
    public static final String AMOUNT = "Amount";

    //原始流水号（6位，不足左补0）
    public static final String OLD_TRACE_NUMBER = "OldTraceNumber";

    //授权码（6位，不足左补0）
    public static final String AUTH_NUMBER = "AuthNumber";

    //原系统参考号（12位，不足左补0）
    public static final String HOST_SERIAL_NUMBER = "HostserialNumber";

    //原交易日期（4位，MMDD）
    public static final String TRANS_DATE = "TransDate";

    //有效期
    public static final String EXPIRE_DATE = "ExpireDate";

    //其他信息
    public static final String MEMO = "Memo";

    //交易唯一标识
    public static final String TRANS_CHECK = "TransCheck";

    //业务编码，用于多业务模式
    public static final String BUSINESS_ID = "BusinessId";
}
