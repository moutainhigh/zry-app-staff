package com.zhongmei.bty.basemodule.erp.util;

import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.Constant;

/**
 * Erp 国籍 常量
 * <p>
 * Created by demo on 2018/12/15
 */
public class ErpConstants {

    public static final String LANGUAGE_ZH_CN = "zh_CN"; // "简体中文(中国)"),

    public static final String LANGUAGE_ZH_TW = "zh_TW"; // "繁体中文(台湾地区)"),

    public static final String LANGUAGE_ZH_HK = "zh_hk"; // "繁体中文(香港)"),

    public static final String LANGUAGE_EN_GB = "en-gb"; // "英语(英国)"),

    public static final String LANGUAGE_JA_JP = "ja-jp"; // "日语(日本)"),

    public static final String LANGUAGE_KO_KR = "ko-kr"; // "韩文(韩国)"),

    public static final String LANGUAGE_RU_RU = "ru-ru"; // "俄语(俄罗斯)"),

    public static final String LANGUAGE_DE_DE = "de-de"; // "德语(德国)"),

    public static final String LANGUAGE_FR_FR = "fr-fr"; // "法语(法国)"),

    public static final String LANGUAGE_ES_ES = "es-es"; // "西班牙语(西班牙)"),

    public static final String LANGUAGE_IT_IT = "it-it"; // "意大利语(意大利)"),

    public static final String LANGUAGE_EL_GR = "el-gr"; // "希腊语(希腊)"),

    public static final String LANGUAGE_HU_HU = "hu-hu"; // "匈牙利语(匈牙利)"),

    public static final String LANGUAGE_CS_CZ = "cs-cz"; // "捷克语(捷克共和国)"),

    public static final String LANGUAGE_PL_PL = "pl-pl"; // "波兰语(波兰)"),

    public static final String LANGUAGE_FI_FI = "fi-fi"; // "芬兰语(芬兰)"),

    public static final String LANGUAGE_DA_DK = "da-dk"; // "丹麦语(丹麦)"),

    public static final String LANGUAGE_NL_NL = "nl-nl"; // "荷兰语(荷兰)"),

    public static final String LANGUAGE_PT_PT = "pt-pt"; // "葡萄牙语(葡萄牙)"),

    public static final String LANGUAGE_NO_NO = "no-no"; // "挪威语(挪威)"),

    public static final String LANGUAGE_TR_TR = "tr-tr"; // "土耳其语(土耳其)"),

    public static final String LANGUAGE_SV_SE = "sv-se"; // "瑞典语(瑞典)");


    //中国大陆区域码
    public static final String COUNTRY_CODE_ZH_INLAND = "86";


    /**
     * 是否是内地
     *
     * @return
     */
    /*public static boolean isInland(){
        String areaCode= SharedPreferenceUtil.getSpUtil().getString(Constant.ERP_ARED_CODE,COUNTRY_CODE_ZH_INLAND);
        if(areaCode.equalsIgnoreCase(COUNTRY_CODE_ZH_INLAND)){
            return true;
        }
        return false;
    }*/

    //用于判断顾客国际化弹窗的选择国籍或手机区号显示语言
    public static boolean isChinese() {
        String language = SharedPreferenceUtil.getSpUtil().getString(Constant.DEFAULTLANGUAGE, ErpConstants.LANGUAGE_ZH_CN);
        boolean isChinese;
        switch (language) {
            case ErpConstants.LANGUAGE_ZH_CN:  //("zh_CN", "简体中文(中国)"),
            case ErpConstants.LANGUAGE_ZH_TW:  //("zh_TW", "繁体中文(台湾地区)"),
            case ErpConstants.LANGUAGE_ZH_HK:  //("zh-hk", "繁体中文(香港)"),
                isChinese = true;
                break;
            case ErpConstants.LANGUAGE_EN_GB:  //("en-gb", "英语(英国)"),
            case ErpConstants.LANGUAGE_JA_JP:  //("ja-jp", "日语(日本)"),
            case ErpConstants.LANGUAGE_KO_KR:  //("ko-kr", "韩文(韩国)"),
            case ErpConstants.LANGUAGE_RU_RU:  //("ru-ru", "俄语(俄罗斯)"),
            case ErpConstants.LANGUAGE_DE_DE:  //("de-de", "德语(德国)"),
            case ErpConstants.LANGUAGE_FR_FR:  //("fr-fr", "法语(法国)"),
            case ErpConstants.LANGUAGE_ES_ES:  //("es-es", "西班牙语(西班牙)"),
            case ErpConstants.LANGUAGE_IT_IT:  //("it-it", "意大利语(意大利)"),
            case ErpConstants.LANGUAGE_EL_GR:  //("el-gr", "希腊语(希腊)"),
            case ErpConstants.LANGUAGE_HU_HU:  //("hu-hu", "匈牙利语(匈牙利)"),
            case ErpConstants.LANGUAGE_CS_CZ:  //("cs-cz", "捷克语(捷克共和国)"),
            case ErpConstants.LANGUAGE_PL_PL:  //("pl-pl", "波兰语(波兰)"),
            case ErpConstants.LANGUAGE_FI_FI:  //("fi-fi", "芬兰语(芬兰)"),
            case ErpConstants.LANGUAGE_DA_DK:  //("da-dk", "丹麦语(丹麦)"),
            case ErpConstants.LANGUAGE_NL_NL:  //("nl-nl", "荷兰语(荷兰)"),
            case ErpConstants.LANGUAGE_PT_PT:  //("pt-pt", "葡萄牙语(葡萄牙)"),
            case ErpConstants.LANGUAGE_NO_NO:  //("no-no", "挪威语(挪威)"),
            case ErpConstants.LANGUAGE_TR_TR:  //("tr-tr", "土耳其语(土耳其)"),
            case ErpConstants.LANGUAGE_SV_SE:  //("sv-se", "瑞典语(瑞典)");
            default:
                isChinese = false;
                break;
        }
        return isChinese;
    }

    /**
     * 判定erp的语言是否是简体中文
     *
     * @return
     */
    public static boolean isSimplifiedChinese() {
        return SharedPreferenceUtil.getSpUtil().getString(Constant.DEFAULTLANGUAGE, ErpConstants.LANGUAGE_ZH_CN).equals(ErpConstants.LANGUAGE_ZH_CN);
    }

    // v8.15.0 登录页面切换语言

    /**
     * 通过Key查Value
     */
    public static String languageDisplay(String key) {
        return "中-简";
    }
}
