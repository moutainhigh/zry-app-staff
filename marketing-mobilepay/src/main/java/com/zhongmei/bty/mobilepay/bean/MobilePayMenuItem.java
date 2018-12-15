package com.zhongmei.bty.mobilepay.bean;

import com.zhongmei.yunfu.db.enums.PayModeId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 移动支付二维码菜单数据结构
 */

public class MobilePayMenuItem {
    public String menuName;//支付方式的名称
    public PayModeId payModeId;//支付方式枚举
    public int payModeIcon;//图标资源
    private List<MobilePayMenuItem> childMenus;//移动支付下包含哪些一码支付方式
    public boolean isSelected;

    public List<MobilePayMenuItem> getChildMenus() {
        return childMenus;
    }


    public void addChildMenu(MobilePayMenuItem item) {
        if (childMenus == null) {
            childMenus = new ArrayList<MobilePayMenuItem>(2);
        }
        childMenus.add(item);
    }
}
