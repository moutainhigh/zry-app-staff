package com.zhongmei.bty.mobilepay.bean;

import com.zhongmei.yunfu.db.enums.PayModeId;

import java.util.ArrayList;
import java.util.List;



public class MobilePayMenuItem {
    public String menuName;    public PayModeId payModeId;    public int payModeIcon;    private List<MobilePayMenuItem> childMenus;    public boolean isSelected;

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
