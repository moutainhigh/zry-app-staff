package com.zhongmei.bty.basemodule.customer.dialog.country;

import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;

/**
 * @date:2016年6月7日上午9:24:54
 */
public class CountryGridItem {
    /**
     * 国籍id
     */
    public Long id;
    /**
     * 国家(中文)
     */
    public String countryZh;
    /**
     * 国家(英文)
     */
    public String countryEn;
    //首字母
    public String sortLetters;
    //是否被选中
    public boolean isSelected;
    //首字母的位置
    public int section;

    public String areaCode;

    public ErpCurrency erpCurrency;//add v8.2
}
