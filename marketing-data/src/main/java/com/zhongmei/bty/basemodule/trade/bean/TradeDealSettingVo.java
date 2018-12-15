package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.trade.entity.TradeDealSettingItem;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSetting;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.io.Serializable;
import java.util.List;

public class TradeDealSettingVo implements Serializable {

    /**
     * @date：2015年9月14日 下午2:26:20
     * @Description:TODO
     */
    private static final long serialVersionUID = 1L;


    private TradeDealSetting tradeDealSetting;

    private List<TradeDealSettingItem> tradeDealSettingItems;

    public TradeDealSetting getTradeDealSetting() {
        return tradeDealSetting;
    }

    public void setTradeDealSetting(TradeDealSetting tradeDealSetting) {
        this.tradeDealSetting = tradeDealSetting;
    }

    public List<TradeDealSettingItem> getTradeDealSettingItems() {
        return tradeDealSettingItems;
    }

    public void setTradeDealSettingItems(List<TradeDealSettingItem> tradeDealSettingItems) {
        this.tradeDealSettingItems = tradeDealSettingItems;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tradeDealSetting == null) ? 0 : tradeDealSetting.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TradeDealSettingVo other = (TradeDealSettingVo) obj;
        if (tradeDealSetting == null) {
            if (other.tradeDealSetting != null)
                return false;
        } else if (!tradeDealSetting.equals(other.tradeDealSetting))
            return false;
        return true;
    }

    public boolean isEnabled() {
        return tradeDealSetting != null && tradeDealSetting.getIsEnabled() == YesOrNo.YES;
    }
}
