package com.zhongmei.bty.basemodule.discount.bean.salespromotion;

public class SalesPromotionWeekday {
    //Weekday的编码，星期日为1、星期一为2，注意有一些时区星期日为7、星期一为1
    private int number;
    //Weekday的名称，如星期一、星期二等等
    private String name;
    //是否可用
    private boolean enable;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
