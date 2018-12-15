package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.bty.basemodule.commonbusiness.bean.TypeBase;

import java.util.List;

/**
 * @version: 1.0
 * @date 2015年7月27日
 */
public class TablesAreaVo extends TypeBase {

    private final CommercialArea tablesArea;

    private List<TablesVo> tablesVoList;

    // 是否是全部选择
    private boolean isSelectAll = false;

    private boolean isSelected;

    /**
     * 初始 选中桌台个数
     */
    private int initSelectedTableCount;

    public int getInitSelectedTableCount() {
        return initSelectedTableCount;
    }

    public void setInitSelectedTableCount(int initSelectedTableCount) {
        this.initSelectedTableCount = initSelectedTableCount;
    }

    public TablesAreaVo(CommercialArea tablesArea) {
        this.tablesArea = tablesArea;
    }

    public String getAreaName() {
        return tablesArea.getAreaName();
    }

    public CommercialArea getTablesArea() {
        return tablesArea;
    }

    public List<TablesVo> getTablesVoList() {
        return tablesVoList;
    }

    public void setTablesVoList(List<TablesVo> tablesVoList) {
        this.tablesVoList = tablesVoList;
    }

    public boolean isSelectAll() {
        return isSelectAll;
    }

    public void setSelectAll(boolean isSelectAll) {
        this.isSelectAll = isSelectAll;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        int count = 0;
        if (tablesVoList != null) {
            count = tablesVoList.size();
        }
        return getAreaName() + " (" + count + ") ";
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
