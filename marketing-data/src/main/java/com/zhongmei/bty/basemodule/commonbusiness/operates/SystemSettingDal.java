package com.zhongmei.bty.basemodule.commonbusiness.operates;

import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.commonbusiness.entity.SyncDict;
import com.zhongmei.bty.basemodule.commonbusiness.message.CommercialCustomSettingsReq;
import com.zhongmei.bty.basemodule.database.db.CashHandoverConfig;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.bty.basemodule.shopmanager.closing.entity.ClosingAccountRecord;
import com.zhongmei.bty.basemodule.shopmanager.message.OpenAndCloseReq;
import com.zhongmei.bty.basemodule.shopmanager.paymentmanager.entitys.AccountSubjectVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeDealSettingVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSetting;
import com.zhongmei.bty.basemodule.trade.message.TableNumberSettingResp;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.data.MindTransferResp;
import com.zhongmei.bty.commonmodule.database.entity.InitSystem;
import com.zhongmei.bty.commonmodule.database.entity.LydMarketSetting;
import com.zhongmei.bty.commonmodule.database.entity.TableNumberSetting;
import com.zhongmei.bty.commonmodule.database.enums.InitSystemDeviceType;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingBusinessType;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingOperateType;
import com.zhongmei.yunfu.resp.ResponseListener;

import java.util.Date;
import java.util.List;


public interface SystemSettingDal extends IOperates {


    void openAndCloseBusiness(OpenAndCloseReq req, ResponseListener<Object> listener);


    void commercialSettings(List<CommercialCustomSettings> commercialCustomSettings, ResponseListener<List<CommercialCustomSettings>> listener);


    void updateSaveSetting(CommercialCustomSettingsReq req, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener);


    Date getCurrentBizDate();


    TradeDealSettingVo findTradeDealSetting(TradeDealSettingOperateType tradeDealSettingOperateType,
                                            TradeDealSettingBusinessType tradeDealSettingBusinessType) throws Exception;


    List<TradeDealSetting> listDinnerSetting() throws Exception;


    void handoverClean(CashHandoverConfig config, ResponseListener<CashHandoverConfig> listener);


    CashHandoverConfig findCashHandoverConfig(Integer configKey) throws Exception;

    void setTableNumberSetting(List<TableNumberSetting> tableNumberSettings,
                               ResponseListener<TableNumberSettingResp> listener);

    List<TableNumberSetting> listTableNumberSetting(boolean onlyValid) throws Exception;


    List<CashHandoverConfig> listCashConfigs() throws Exception;


    AccountSubjectVo findAccoutSubject() throws Exception;


    CommercialCustomSettings findCommercialCustomSettings(String key) throws Exception;

    ClosingAccountRecord findClosingAccountRecord() throws Exception;


    ElectronicInvoiceVo findElectronicInvoiceVo() throws Exception;


    LydMarketSetting findMarketSetting(String configName) throws Exception;


    Date getBizClosingTime() throws Exception;


    List<PartnerShopBiz> queryPartnerShopBiz(int bizType, boolean onlyEnable) throws Exception;


    List<SyncDict> querySyncDict(int type) throws Exception;


    void setHandoverType(int type, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener);


    void updatePrintSort(int sort, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener);

    List<InitSystem> queryInitSystem(InitSystemDeviceType initSystemDeviceType) throws Exception;

    void updateIsOpenShortName(int isOpen, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener);
}
