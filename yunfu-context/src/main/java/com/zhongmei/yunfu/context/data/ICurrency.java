package com.zhongmei.yunfu.context.data;


public interface ICurrency {

        String DEFAULT_SYMBOL = "￥";


    Long getId();


    String getAreaCode();


    String getPhoneRegulation();


    boolean checkPhone(String phone);


    String getCountryZh();


    String getCountryEn();



    String getCurrencySymbol();
}
