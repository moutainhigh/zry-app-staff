package com.zhongmei.bty.basemodule.erp.util;

import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.Constant;


public class ErpConstants {

    public static final String LANGUAGE_ZH_CN = "zh_CN";
    public static final String LANGUAGE_ZH_TW = "zh_TW";
    public static final String LANGUAGE_ZH_HK = "zh_hk";
    public static final String LANGUAGE_EN_GB = "en-gb";
    public static final String LANGUAGE_JA_JP = "ja-jp";
    public static final String LANGUAGE_KO_KR = "ko-kr";
    public static final String LANGUAGE_RU_RU = "ru-ru";
    public static final String LANGUAGE_DE_DE = "de-de";
    public static final String LANGUAGE_FR_FR = "fr-fr";
    public static final String LANGUAGE_ES_ES = "es-es";
    public static final String LANGUAGE_IT_IT = "it-it";
    public static final String LANGUAGE_EL_GR = "el-gr";
    public static final String LANGUAGE_HU_HU = "hu-hu";
    public static final String LANGUAGE_CS_CZ = "cs-cz";
    public static final String LANGUAGE_PL_PL = "pl-pl";
    public static final String LANGUAGE_FI_FI = "fi-fi";
    public static final String LANGUAGE_DA_DK = "da-dk";
    public static final String LANGUAGE_NL_NL = "nl-nl";
    public static final String LANGUAGE_PT_PT = "pt-pt";
    public static final String LANGUAGE_NO_NO = "no-no";
    public static final String LANGUAGE_TR_TR = "tr-tr";
    public static final String LANGUAGE_SV_SE = "sv-se";

        public static final String COUNTRY_CODE_ZH_INLAND = "86";





        public static boolean isChinese() {
        String language = SharedPreferenceUtil.getSpUtil().getString(Constant.DEFAULTLANGUAGE, ErpConstants.LANGUAGE_ZH_CN);
        boolean isChinese;
        switch (language) {
            case ErpConstants.LANGUAGE_ZH_CN:              case ErpConstants.LANGUAGE_ZH_TW:              case ErpConstants.LANGUAGE_ZH_HK:                  isChinese = true;
                break;
            case ErpConstants.LANGUAGE_EN_GB:              case ErpConstants.LANGUAGE_JA_JP:              case ErpConstants.LANGUAGE_KO_KR:              case ErpConstants.LANGUAGE_RU_RU:              case ErpConstants.LANGUAGE_DE_DE:              case ErpConstants.LANGUAGE_FR_FR:              case ErpConstants.LANGUAGE_ES_ES:              case ErpConstants.LANGUAGE_IT_IT:              case ErpConstants.LANGUAGE_EL_GR:              case ErpConstants.LANGUAGE_HU_HU:              case ErpConstants.LANGUAGE_CS_CZ:              case ErpConstants.LANGUAGE_PL_PL:              case ErpConstants.LANGUAGE_FI_FI:              case ErpConstants.LANGUAGE_DA_DK:              case ErpConstants.LANGUAGE_NL_NL:              case ErpConstants.LANGUAGE_PT_PT:              case ErpConstants.LANGUAGE_NO_NO:              case ErpConstants.LANGUAGE_TR_TR:              case ErpConstants.LANGUAGE_SV_SE:              default:
                isChinese = false;
                break;
        }
        return isChinese;
    }


    public static boolean isSimplifiedChinese() {
        return SharedPreferenceUtil.getSpUtil().getString(Constant.DEFAULTLANGUAGE, ErpConstants.LANGUAGE_ZH_CN).equals(ErpConstants.LANGUAGE_ZH_CN);
    }



    public static String languageDisplay(String key) {
        return "中-简";
    }
}
