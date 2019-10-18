package com.zhongmei.bty.basemodule.commonbusiness.utils;

import com.zhongmei.util.SettingManager;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings_Type1;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings_Type2;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings_Type3;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings_Type4;
import com.zhongmei.bty.commonmodule.component.IPanelSettings;


public class Utils {

    public static IPanelItemSettings getCurrentIpanelItemSettings() {
        IPanelSettings iPanelSettings = SettingManager.getSettings(IPanelSettings.class);
        IPanelItemSettings iPanelItemSettings;
        switch (iPanelSettings.getPanel()) {
            case IPanelSettings.PANEL_TYPE_1:
                iPanelItemSettings = SettingManager.getSettings(IPanelItemSettings_Type1.class);
                break;
            case IPanelSettings.PANEL_TYPE_2:
                iPanelItemSettings = SettingManager.getSettings(IPanelItemSettings_Type2.class);
                break;
            case IPanelSettings.PANEL_TYPE_3:
                iPanelItemSettings = SettingManager.getSettings(IPanelItemSettings_Type3.class);
                break;
            case IPanelSettings.PANEL_TYPE_4:
                iPanelItemSettings = SettingManager.getSettings(IPanelItemSettings_Type4.class);
                break;
            default:
                iPanelItemSettings = SettingManager.getSettings(IPanelItemSettings_Type1.class);
                break;
        }
        return iPanelItemSettings;
    }

    public static int getIndexByCurrentPanel(int panel) {
        switch (panel) {
            case IPanelSettings.PANEL_TYPE_1:
                return 1;
            case IPanelSettings.PANEL_TYPE_2:
                return 2;
            case IPanelSettings.PANEL_TYPE_3:
                return 3;
            case IPanelSettings.PANEL_TYPE_4:
                return 4;
            default:
                return 1;
        }
    }
}
