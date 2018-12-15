package com.zhongmei.bty.entity.vo;


// 暂时构建一个票据样式VO,待同步组确定字段后再做修改
public class TicketStyleVo {
    // TODO
    // 暂时按照0表示 票据上不展示菜品 1 表示票据上展示菜品
    // 区分什么单据
    public int mType;
    // 是否打印菜品的flag 票据上不展示菜品 1 表示票据上展示菜品
    public int printDishFlag;
    // 票据名称，合单，转单
    public String mTicketName;

    public TicketStyleVo(int mType, int printDishFlag, String mTicketName) {
        super();
        this.mType = mType;
        this.printDishFlag = printDishFlag;
        this.mTicketName = mTicketName;
    }


}
