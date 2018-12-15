package com.zhongmei.yunfu.context.data;

/**
 * 获取国籍信息
 * Created by demo on 2018/12/15
 */
public interface ICurrency {

    //默认货币符
    String DEFAULT_SYMBOL = "￥";

    /**
     * 国际ID
     *
     * @return
     */
    Long getId();

    /**
     * 获取电话区号
     *
     * @return
     */
    String getAreaCode();

    /**
     * 验证电话规则
     *
     * @return
     */
    String getPhoneRegulation();

    /**
     * 验证手机号是否满足规则
     *
     * @param phone
     * @return
     */
    boolean checkPhone(String phone);

    /**
     * 国家(中文)
     *
     * @return
     */
    String getCountryZh();

    /**
     * 国家(英文)
     *
     * @return
     */
    String getCountryEn();


    /**
     * 根据国籍ID获取国籍货币符
     *
     * @return
     */
    String getCurrencySymbol();
}
