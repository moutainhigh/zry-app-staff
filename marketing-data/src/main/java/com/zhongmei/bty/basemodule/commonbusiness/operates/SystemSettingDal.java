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

/**
 * @version: 1.0
 * @date 2015年8月10日
 */
public interface SystemSettingDal extends IOperates {

    /**
     * 开始营业与歇业
     *
     * @param req
     * @param listener
     */
    void openAndCloseBusiness(OpenAndCloseReq req, ResponseListener<Object> listener);

    /**
     * 门店设置
     *
     * @param commercialCustomSettings
     * @param listener
     */
    void commercialSettings(List<CommercialCustomSettings> commercialCustomSettings, ResponseListener<List<CommercialCustomSettings>> listener);

    /**
     * 通用的门店设置
     *
     * @param req      req
     * @param listener listener
     */
    void updateSaveSetting(CommercialCustomSettingsReq req, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener);

    /**
     * 查询当前时间所属的营业日期
     *
     * @return
     */
    Date getCurrentBizDate();

    /**
     * 返回指定tradeDealSettingOperateType的交易处理设置记录。此方法会阻塞调用线程
     *
     * @param tradeDealSettingOperateType
     * @return
     * @throws Exception
     */
    TradeDealSettingVo findTradeDealSetting(TradeDealSettingOperateType tradeDealSettingOperateType,
                                            TradeDealSettingBusinessType tradeDealSettingBusinessType) throws Exception;

    /**
     * 返回正餐相关的所有设置项
     *
     * @return
     * @throws Exception
     */
    List<TradeDealSetting> listDinnerSetting() throws Exception;

    /**
     * 班次交接清零设置
     *
     * @param config
     * @param listener
     */
    void handoverClean(CashHandoverConfig config, ResponseListener<CashHandoverConfig> listener);

    /**
     * 查询交接清零设置
     *
     * @return
     */
    CashHandoverConfig findCashHandoverConfig(Integer configKey) throws Exception;

    void setTableNumberSetting(List<TableNumberSetting> tableNumberSettings,
                               ResponseListener<TableNumberSettingResp> listener);

    List<TableNumberSetting> listTableNumberSetting(boolean onlyValid) throws Exception;

    /**
     * @Title: listCashConfigs
     * @Description: 查询所有的服务器设置
     * @Param @return
     * @Param @throws Exception TODO
     * @Return List<CashHandoverConfig> 返回类型
     */
    List<CashHandoverConfig> listCashConfigs() throws Exception;

    /**
     * 查询收支科目项
     */
    AccountSubjectVo findAccoutSubject() throws Exception;

    /**
     * 根据Key值获取CommercialCustomSettings
     *
     * @param key
     * @return
     * @throws Exception
     */
    CommercialCustomSettings findCommercialCustomSettings(String key) throws Exception;

    ClosingAccountRecord findClosingAccountRecord() throws Exception;

    /**
     * 获取电子发票
     *
     * @return
     * @throws Exception
     */
    ElectronicInvoiceVo findElectronicInvoiceVo() throws Exception;

    /**
     * 获取营销设置
     *
     * @return configNmae
     * @throws Exception
     */
    LydMarketSetting findMarketSetting(String configName) throws Exception;

    /**
     * 获取歇业时间
     *
     * @return 返回门店的歇业时间
     * @throws Exception
     */
    Date getBizClosingTime() throws Exception;

    /**
     * 根据业务类型查询商户开通的业务数据；此方法会阻塞调用线程
     *
     * @param bizType    业务类型（1-外卖，2-点餐，3-配送，4-发票）
     * @param onlyEnable 为true时仅包含enableFlag为1的数据
     * @throws Exception
     */
    List<PartnerShopBiz> queryPartnerShopBiz(int bizType, boolean onlyEnable) throws Exception;

    /**
     * 根据字典类型查询订单域的一些数据信息
     *
     * @param type 字典类型,详见sync_dict_type表
     * @throws Exception
     */
    List<SyncDict> querySyncDict(int type) throws Exception;

    /**
     * 上传交接方式
     */
    void setHandoverType(int type, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener);

    /**
     * 保存打印顺序配置项
     *
     * @param sort 打印顺序 1下单时间 2商品中类
     */
    void updatePrintSort(int sort, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener);

    List<InitSystem> queryInitSystem(InitSystemDeviceType initSystemDeviceType) throws Exception;

    void updateIsOpenShortName(int isOpen, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener);
}
