package com.zhongmei.bty.basemodule.commonbusiness.cache;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.bty.basemodule.commonbusiness.manager.SystemSettingsManager;
import com.zhongmei.yunfu.db.entity.crm.CrmLevelStoreRule;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
import com.zhongmei.yunfu.db.entity.MobilePaySetting;
import com.zhongmei.bty.basemodule.pay.enums.MobilePayMode;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.basemodule.pay.operates.PayMenuOrderdal;
import com.zhongmei.bty.basemodule.pay.operates.PaymentModeDal;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeScene;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeShop;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;
import com.zhongmei.bty.basemodule.pay.entity.ElectronicInvoice;
import com.zhongmei.yunfu.db.entity.ErpCommercialRelation;
import com.zhongmei.bty.basemodule.pay.entity.InvoiceTaxRate;
import com.zhongmei.bty.commonmodule.database.entity.local.PayMenuOrder;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.yunfu.context.util.helper.SpHelper;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Date：2016-2-17 下午12:01:45
 * @Description: 缓存erp设置项(支付方式 ， 支付方式排序 ， 是否组合 ； 电子发票设置 ； 银联开关)
 * @Version: 1.0
 * <p>
 * reserved.
 */
public class PaySettingCache {
    private static final String TAG = PaySettingCache.class.getSimpleName();

    private static class LazySingletonHolder {
        private static final PaySettingCache INSTANCE = new PaySettingCache();
    }

    public static void doInit(Context context) {
        LazySingletonHolder.INSTANCE.init(context);
    }

    public static void doStop() {
        LazySingletonHolder.INSTANCE.stop();
    }

    public static ErpCommercialRelation getmErpComRel() {
        return LazySingletonHolder.INSTANCE.getmErpCommercialRelation();
    }

    public static String getPayModeNameByModeId(long paymodeId) {
        return LazySingletonHolder.INSTANCE.getPayModeName(paymodeId);
    }

    public static boolean isSetPayModeGroup(PayModelGroup payModelGroup) {
        if (LazySingletonHolder.INSTANCE.mPaymentModeContent != null)
            return LazySingletonHolder.INSTANCE.mPaymentModeContent.isContainsPayModelType(PayScene.SCENE_CODE_SHOP.value(), payModelGroup.value());
        else return false;
    }

    // add 20170510 start for paymentmodescene
    public static boolean isSetPayModeGroup(Integer sceneCode, PayModelGroup payModelGroup) {
        if (LazySingletonHolder.INSTANCE.mPaymentModeContent != null)
            return LazySingletonHolder.INSTANCE.mPaymentModeContent.isContainsPayModelType(sceneCode, payModelGroup.value());
        else return false;
    }

    public static boolean isErpModeID(Integer sceneCode, Long id) {
        if (LazySingletonHolder.INSTANCE.mPaymentModeContent != null)
            return LazySingletonHolder.INSTANCE.mPaymentModeContent.isContainsPaymentModeId(sceneCode, id);
        else return false;
    }

    // 根据场景从缓存获取其它支付方式
    public static List<PaymentModeShop> getOthersPaymentModeShops(Integer sceneCode) {
        if (LazySingletonHolder.INSTANCE.mPaymentModeContent != null)
            return LazySingletonHolder.INSTANCE.mPaymentModeContent.getOtherPaymentModeShop(sceneCode);
        else return null;
    }

    //是否有配置支付方式
    public static boolean isContainsPayModes(Integer sceneCode) {
        return LazySingletonHolder.INSTANCE.isContainsPayMode(sceneCode);
    }
    // add 20170510 end


    public static boolean isErpModeID(long id) {
        if (LazySingletonHolder.INSTANCE.mPaymentModeContent != null)
            return LazySingletonHolder.INSTANCE.mPaymentModeContent.isContainsPaymentModeId(PayScene.SCENE_CODE_SHOP.value(), id);
        else return false;
    }

    //add v8.12 start
    public static PaymentModeShop getPaymentModeShop(long erpModeId) {

        return LazySingletonHolder.INSTANCE.mPaymentModesMap.get(erpModeId);
    }

    /*public static List<PaymentModeShop> getPaymentModeShopByType(Integer sceneCode, Integer paymentModeType) {

        if (LazySingletonHolder.INSTANCE.mPaymentModeContent != null)
            return LazySingletonHolder.INSTANCE.mPaymentModeContent.getPaymentModeShopByType(sceneCode, paymentModeType);
        else return null;

    }*/

    public static MobilePaySettingHolder getMobilePaySettingHolder() {
        return LazySingletonHolder.INSTANCE.mMobilePaySettingHolder;
    }
    //add v8.12 end

    /**
     * @Description: erp设置里面判断是否采用银联支付
     * @Return boolean 返回类型
     */
    public static boolean isUnionpay() {
        ErpCommercialRelation erpComRel = LazySingletonHolder.INSTANCE.getmErpCommercialRelation();

        if (erpComRel != null && erpComRel.getIsUnionpay() == 1) {
            return true;
        }
        return false;
    }

    /**
     * @Description: erp设置里面判断是否采用熟客
     * @Return boolean 返回类型
     */
    public static boolean isShukeInErp() {
        ErpCommercialRelation erpComRel = LazySingletonHolder.INSTANCE.getmErpCommercialRelation();

        if (erpComRel != null && erpComRel.getIsShuke() == 1) {
            return true;
        }
        return false;
    }

    public static void refreshPayMenuOrder(List<PayMenuOrder> list) {
        LazySingletonHolder.INSTANCE.updatePayMenuOrder(list);
    }

    public static Integer getPayMenuOrderByMenuId(int MenuId) {
        return LazySingletonHolder.INSTANCE.getMenuOrder(Long.valueOf(MenuId));
    }

    //是否有配置支付方式
    public static boolean isContainsPayModes() {
        return LazySingletonHolder.INSTANCE.isContainsPayMode(PayScene.SCENE_CODE_SHOP.value());
    }

    public static boolean isSupportGroupPay() {
        return LazySingletonHolder.INSTANCE.isSupportGroupPay;
    }

    public static void setSupportGroupPay(boolean supportGroupPay) {
        LazySingletonHolder.INSTANCE.isSupportGroupPay = supportGroupPay;
    }

    /**
     * 获取电子发票设置数据
     */
    public static ElectronicInvoiceVo getElectronicInvoiceVo() {
        return LazySingletonHolder.INSTANCE.mElectronicInvoiceVo;
    }

    /**
     * 获取电子发票开关
     */
    public static boolean isElectronicInvoiceOpen() {
        if (LazySingletonHolder.INSTANCE.mElectronicInvoiceVo != null)
            return LazySingletonHolder.INSTANCE.mElectronicInvoiceVo.isOpen();
        return false;
    }

    public static boolean isSupportOneCodePay() {//add v8.12
        return LazySingletonHolder.INSTANCE.isSupportOneCodePay;
    }

    public static void setSupportOneCodePay(boolean isSuport) {//add v8.12
        LazySingletonHolder.INSTANCE.isSupportOneCodePay = isSuport;
    }

    public static boolean isCanPayNoPwd(BigDecimal amount) {
        return LazySingletonHolder.INSTANCE.canPayNoPwd(amount);
    }

    private static final Uri URI_ERP_COMMERCIAL_RELATION = DBHelperManager.getUri(ErpCommercialRelation.class);

    private static final Uri URI_PAYMENT_MODE_ID = DBHelperManager.getUri(PaymentModeShop.class);

    //门店设置
    private static final Uri URI_COMMERCIAL_CUSTOMER_SETTINGS = DBHelperManager.getUri(CommercialCustomSettings.class);

    //电子发票设置信息表
    private static final Uri URI_ELECTRONIC_INVOICE = DBHelperManager.getUri(ElectronicInvoice.class);

    //电子发票税率表
    private static final Uri URI_INVOICE_TAX_RATE = DBHelperManager.getUri(InvoiceTaxRate.class);

    //支付方式场景表
    private static final Uri URI_PAYMENT_MODE_SCENE = DBHelperManager.getUri(PaymentModeScene.class);

    //免密支付配置
    private static final Uri URI_CRM_LEVEL_STORE_RULE = DBHelperManager.getUri(CrmLevelStoreRule.class);

    //移动支付设置
    private static final Uri URI_CRM_MOBILE_PAY_SETTING = DBHelperManager.getUri(MobilePaySetting.class);

    private ErpCommercialRelation mErpCommercialRelation;

    private ElectronicInvoiceVo mElectronicInvoiceVo;

    private Map<Long, String> mPaymentModeNamesMap = new HashMap<Long, String>();//所有支付方式名称缓存

    private Map<Long, PaymentModeShop> mPaymentModesMap = new HashMap<Long, PaymentModeShop>();//所有支付方式名称缓存 add v8.12

    private Map<Long, Integer> mPayMenuOrderMap;//支付菜单顺序

    private boolean isSupportGroupPay;//add 20170425 是否支持组合支付本地参数

    private boolean isSupportOneCodePay;//add 8.12 是否支持一码支付本地参数

    private PaymentModeContent mPaymentModeContent;//add 20170510 添加支付方式场景缓存

    private CrmLevelStoreRule mCrmLevelStoreRule;//免密支付配置项

    private MobilePaySettingHolder mMobilePaySettingHolder = new MobilePaySettingHolder();//移动支付配置项 add v8.12

    private DatabaseHelper.DataChangeObserver mObserver;

    private Context mContext;//add 20180308

    public void init(Context context) {
        mContext = context;
        if (mObserver == null) {
            mObserver = new ErpSettingsObserver();
            DatabaseHelper.Registry.register(mObserver);
        }
        backgroundRefresh();
    }

    public void stop() {
        if (mObserver != null) {
            DatabaseHelper.Registry.unregister(mObserver);
            mObserver = null;
        }
        if (mPaymentModeNamesMap != null) {
            mPaymentModeNamesMap.clear();
        }
        if (mPaymentModesMap != null) {//add v8.12
            mPaymentModesMap.clear();
        }
    }

    /**
     * @Title: backgroundRefresh
     * @Description: 开启新线程刷新缓存数据
     */
    private void backgroundRefresh() {
        ThreadUtils.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                refreshErpCommercialRelation();
                refreshPaymentModes();
                refreshElectronicInvoice();
                refreshPayMenuOrder();
                refreshCrmLevelStoreRule();
                initIsSupportGroupPay();//add 20170424 是否支持分步开关
                refreshMobilePaySetting();//刷新移动支付设置
            }
        });
    }

    private void initIsSupportGroupPay() {
        this.isSupportGroupPay = SpHelper.getDefault().getBoolean(Constant.SUPPORT_GROUP_PAY, true);
        this.isSupportOneCodePay = SpHelper.getDefault().getBoolean(Constant.SUPPORT_ONECODE_PAY, false);
    }

    private void refreshErpCommercialRelation() {
        try {
            ErpCommercialRelationDal dal = OperatesFactory.create(ErpCommercialRelationDal.class);
            mErpCommercialRelation = dal.findErpCommercialRelation();
        } catch (Exception e) {
            Log.e(TAG, "RefreshErpCommercialRelation  error!", e);
        }
    }

    private void updatePayMenuOrder(List<PayMenuOrder> list) {
        Map<Long, Integer> payMenuOrderMap = new HashMap<Long, Integer>();
        if (list != null && !list.isEmpty()) {
            for (PayMenuOrder menuOrder : list) {
                payMenuOrderMap.put(menuOrder.getPayMenuId(), menuOrder.getOrder());
            }
        }
        this.mPayMenuOrderMap = payMenuOrderMap;
    }

    private Integer getMenuOrder(Long key) {
        if (mPayMenuOrderMap == null) {
            return null;
        } else {
            return mPayMenuOrderMap.get(key);
        }
    }

    private void refreshPayMenuOrder() {
        try {
            PayMenuOrderdal dal = new PayMenuOrderdal();
            List<PayMenuOrder> list = dal.findAllPayMenuOrder();
            updatePayMenuOrder(list);
        } catch (Exception e) {
            Log.e(TAG, "refReshPayMenuOrder  error!", e);
        }
    }

    private void refreshMobilePaySetting() {//刷新移动支付设置 add v8.12
        PaymentModeDal paymentModeDal = OperatesFactory.create(PaymentModeDal.class);
        List<MobilePaySetting> list = paymentModeDal.findAllMobilePaySettings();
        mMobilePaySettingHolder.updateData(list);
    }

    /**
     * 刷新
     */
    private void refreshElectronicInvoice() {
        mElectronicInvoiceVo = SystemSettingsManager.findElectronicInvoiceVo();
    }

    //modify 20170510 start 刷新支付方式及支付场景
    private void refreshPaymentModes() {
        try {
            PaymentModeContent _PaymentModeContent = new PaymentModeContent();//支付方式配置场景缓存容器
            Map<Long, String> _PaymentModeNamesMap = new HashMap<Long, String>();//支付方式名字缓存
            Map<Long, PaymentModeShop> _PaymentModeShopMap = new HashMap<Long, PaymentModeShop>();//modify v8.12 缓存所有支付方式
            boolean isUsedScene = false;//是否开启支付场景设置
            PaymentModeDal paymentModeDal = OperatesFactory.create(PaymentModeDal.class);
            //查询所有支付方式
            List<PaymentModeShop> modeList = paymentModeDal.findAllPaymentMode();
            //查询所有支付方式场景
            List<PaymentModeScene> sceneList = paymentModeDal.findAllPaymentModeScene();
            //查询所有支付方式场景类别
            Set<Integer> sceneCodeSet = paymentModeDal.findAllPaymentModeSceneCode();
            //根据场景数据是否为空作为场景开关
            if (sceneList != null && !sceneList.isEmpty() && sceneCodeSet != null) {
                isUsedScene = true;
            }
            if (modeList != null && !modeList.isEmpty()) {
                for (PaymentModeShop shop : modeList) {
                    if (shop.getErpModeId() != null) {
                        _PaymentModeShopMap.put(shop.getErpModeId(), shop);//add v812 缓存所有支付方式
                        //如果没有消费场景，默认所有支付方式
                        if (!sceneCodeSet.contains(PayScene.SCENE_CODE_SHOP.value())) {
                            _PaymentModeContent.setPaymentMode(PayScene.SCENE_CODE_SHOP.value(), shop);
                        }
                        //如果没有储值场景，默认所有支付方式
                        if (!sceneCodeSet.contains(PayScene.SCENE_CODE_CHARGE.value())) {
                            _PaymentModeContent.setPaymentMode(PayScene.SCENE_CODE_CHARGE.value(), shop);
                        }
                        //如果没有销账场景，默认所有支付方式 add v8.10
                        if (!sceneCodeSet.contains(PayScene.SCENE_CODE_WRITEOFF.value())) {
                            _PaymentModeContent.setPaymentMode(PayScene.SCENE_CODE_WRITEOFF.value(), shop);
                        }
                        //缓存支付方式名字
                        if (!TextUtils.isEmpty(shop.getName())) {
                            _PaymentModeNamesMap.put(shop.getErpModeId(), shop.getName());
                        }
                    }
                }
                this.mPaymentModeNamesMap = _PaymentModeNamesMap;//刷新支付方式名字缓存
                this.mPaymentModesMap = _PaymentModeShopMap;//刷新所有支付方式缓存 add 8.12
            }
            //如果开启场景支持
            if (isUsedScene) {
                for (PaymentModeScene scene : sceneList) {
                    if (scene.getShopErpModeId() != null && scene.getSceneCode() != null) {
                        //添加数据到缓存容器
                        _PaymentModeContent.setPaymentMode(scene.getSceneCode(), _PaymentModeShopMap.get(scene.getShopErpModeId()));
                    }
                }
            }
            this.mPaymentModeContent = _PaymentModeContent;//刷新各场景支付方式缓存
        } catch (Exception e) {
            Log.e(TAG, "refreshPaymentMode error!", e);
        }
    }
    //modify 20170510 end

    public ErpCommercialRelation getmErpCommercialRelation() {
        return this.mErpCommercialRelation;
    }

    //moidfy begin 20170510
    //是否配置有支付方式
    private boolean isContainsPayMode(Integer senceCode) {
        if (LazySingletonHolder.INSTANCE.mPaymentModeContent != null) {
            if (LazySingletonHolder.INSTANCE.mPaymentModeContent.paymentModeShopHolderMap.get(senceCode) != null) {//大的支付类型
                return true;
            }
            if (LazySingletonHolder.INSTANCE.mPaymentModeContent.mPayModeIdMap.get(senceCode) != null) {//具体支付方式
                return true;
            }
        }
        return false;
    }
    //moidfy end 20170510

    /**
     * @Description: 缓存里面获取支付类型名称
     * @Param @param paymodeId
     * @Return String 返回类型
     */
    private String getPayModeName(long paymodeId) {
        String payModeName = this.mPaymentModeNamesMap.get(paymodeId);
        // 如果为空，手动设值
        if (TextUtils.isEmpty(payModeName) && mContext != null) {
            String[] paymentModeName = mContext.getResources().getStringArray(R.array.trade_payment_mode);
            switch ((int) (paymodeId + 0)) {
                case 1:
                    // 会员卡余额
                    payModeName = paymentModeName[0];
                    break;
                case 2:
                    // 现金
                    payModeName = paymentModeName[1];
                    break;
                case 3:
                    // 银行卡
                    payModeName = paymentModeName[2];
                    break;
                case 4:
                    // 微信支付
                    payModeName = paymentModeName[3];
                    break;
                case 5:
                    // 支付宝
                    payModeName = paymentModeName[4];
                    break;
                case 6:
                    // 积分抵现
                    payModeName = paymentModeName[5];
                    break;
                case 7:
                    // 优惠券
                    payModeName = paymentModeName[6];
                    break;
                //------end--------//
                case -8:
                    // 百度直达号
                    payModeName = paymentModeName[7];
                    break;
                case -9:
                    // 积分抵现
                    payModeName = paymentModeName[8];

                    break;
                case -10:
                    // 百度地图
                    payModeName = paymentModeName[9];

                    break;
                case -11:
                    // 银联POS刷卡
                    payModeName = paymentModeName[10];
                    break;
                case -12: //百糯到店付
                    payModeName = paymentModeName[11];
                    break;
                case -13: //百度外卖
                    payModeName = paymentModeName[12];
                    break;
                case -14: //饿了么
                    payModeName = paymentModeName[13];
                    break;
                case -15: //实体卡支付
                    payModeName = paymentModeName[14];
                    break;
                case -16: //大众点评
                    payModeName = paymentModeName[15];
                    break;
                case -17: //美团外卖
                    payModeName = paymentModeName[16];
                    break;
                case -18: //点评团购券
                    payModeName = paymentModeName[17];
                    break;
                case -19: //点评闪惠
                    payModeName = paymentModeName[18];
                    break;
                case -20: //匿名卡支付
                    payModeName = paymentModeName[19];
                    break;
                case -21: //糯米点菜
                    payModeName = paymentModeName[20];
                    break;
                case -22: //第三方C端
                    payModeName = paymentModeName[21];
                    break;
                case -23: //美团闪付
                    payModeName = paymentModeName[22];
                    break;
                case -24: //美团团购
                    payModeName = paymentModeName[23];
                    break;
                case -25://钱包生活
                    payModeName = paymentModeName[24];
                    break;
                case -26://百度糯米券
                    payModeName = paymentModeName[25];
                    break;
                case -29://金诚支付:
                    payModeName = paymentModeName[26];
                    break;
                case -30://金诚储值卡
                    payModeName = paymentModeName[27];
                    break;
                case -34://烽火手环//add v8.7
                    payModeName = paymentModeName[28];
                    break;
                case -36://口碑券//add v8.9
                    payModeName = paymentModeName[29];
                    break;
                case -37://银联云闪付//add v8.11
                    payModeName = paymentModeName[30];
                    break;
                case -38://工商E支付//add v8.11
                    payModeName = paymentModeName[31];
                    break;
                case -40: //移动支付
                    payModeName = paymentModeName[32];
                    break;
                case -42: //订金抵扣
                    payModeName = paymentModeName[33];
                    break;
                case -101: //电信翼支付
                    payModeName = paymentModeName[34];
                    break;
                // 其他
                default:
                    payModeName = mContext.getResources().getString(R.string.erp_unknow);
                    break;
            }
        }
        return payModeName;
    }

    private void refreshCrmLevelStoreRule() {
        CustomerDal customerDal = OperatesFactory.create(CustomerDal.class);
        try {
            mCrmLevelStoreRule = customerDal.findCrmLevelStoreRule();
        } catch (SQLException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    /**
     * 当前金额是否可以进行免密支付（只针对虚拟会员储值支付）
     *
     * @param amount
     * @return
     */
    public boolean canPayNoPwd(BigDecimal amount) {
        if (mCrmLevelStoreRule != null) {
            if (mCrmLevelStoreRule.getPayNoPwd() == YesOrNo.YES //支持免密
                    && (mCrmLevelStoreRule.getPayNoPwdAmount() == null //免密额度为空
                    || mCrmLevelStoreRule.getPayNoPwdAmount().compareTo(new BigDecimal(-1)) == 0 //-1表示免密不限额
                    || mCrmLevelStoreRule.getPayNoPwdAmount().compareTo(amount) >= 0)) {//免密额度大于等于订单金额

                return true;
            }
        }

        return false;
    }

    private class ErpSettingsObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(URI_ERP_COMMERCIAL_RELATION)) {
                refreshErpCommercialRelation();// 刷新erp配置（费率）
            }
            if (uris.contains(URI_PAYMENT_MODE_ID) || uris.contains(URI_PAYMENT_MODE_SCENE)) {
                refreshPaymentModes();// 刷新erp配置（支付类型）
            }
            //刷新电子发票相关表
            if (uris.contains(URI_COMMERCIAL_CUSTOMER_SETTINGS)
                    || uris.contains(URI_ELECTRONIC_INVOICE)
                    || uris.contains(URI_INVOICE_TAX_RATE)) {
                refreshElectronicInvoice();
            }
            //刷新免密支付配置项
            if (uris.contains(URI_CRM_LEVEL_STORE_RULE)) {
                refreshCrmLevelStoreRule();
            }
            //刷新移动支付设置
            if (uris.contains(URI_CRM_MOBILE_PAY_SETTING)) {
                refreshMobilePaySetting();
            }
        }
    }

    //add 20170510 begin  for 支付方式场景
    private static class PaymentModeContent {
        Map<Integer, Set<Long>> mPayModeIdMap;//sceneCode 作为key
        Map<Integer, PaymentModeShopHolder> paymentModeShopHolderMap;//modify v8.12 根据支付类型缓存 sceneCode 作为key

        public PaymentModeContent() {
            mPayModeIdMap = new HashMap<Integer, Set<Long>>();
            paymentModeShopHolderMap = new HashMap<Integer, PaymentModeShopHolder>();
        }

        public void setPaymentMode(Integer sceneCode, PaymentModeShop paymentModeShop) {
            if (sceneCode == null || paymentModeShop == null) {
                return;
            }
            if (paymentModeShop.getPaymentModeType() != null) {
                this.addPayModeShop(sceneCode, paymentModeShop);
            }
            this.addPayModelId(sceneCode, paymentModeShop.getErpModeId());
        }

        private void addPayModeShop(Integer sceneCode, PaymentModeShop paymentModeShop) {
            if (this.paymentModeShopHolderMap.get(sceneCode) == null) {
                PaymentModeShopHolder dataHolder = new PaymentModeShopHolder();
                dataHolder.addData(paymentModeShop);
                this.paymentModeShopHolderMap.put(sceneCode, dataHolder);

            } else {
                this.paymentModeShopHolderMap.get(sceneCode).addData(paymentModeShop);
            }
        }

        private void addPayModelId(Integer sceneCode, Long payModeId) {
            if (this.mPayModeIdMap.get(sceneCode) == null) {
                Set<Long> set = new HashSet<Long>();
                set.add(payModeId);
                this.mPayModeIdMap.put(sceneCode, set);
            } else {
                this.mPayModeIdMap.get(sceneCode).add(payModeId);
            }
        }

        private boolean isContainsPayModelType(Integer sceneCode, Integer paymentModeType) {
            PaymentModeShopHolder dataHolder = paymentModeShopHolderMap.get(sceneCode);
            if (dataHolder == null) {
                if (paymentModeShopHolderMap.get(PayScene.SCENE_CODE_SHOP.value()) != null) {
                    return paymentModeShopHolderMap.get(PayScene.SCENE_CODE_SHOP.value()).isContainsPayModelType(paymentModeType);
                } else {
                    return false;
                }
            } else {
                return dataHolder.isContainsPayModelType(paymentModeType);
            }
        }

        private boolean isContainsPaymentModeId(Integer sceneCode, Long payModeId) {
            if (this.mPayModeIdMap.get(sceneCode) == null) {
                //如果没有缓存的场景默认是消费(目前自助押金取消费)
                if (this.mPayModeIdMap.get(PayScene.SCENE_CODE_SHOP.value()) != null)
                    return this.mPayModeIdMap.get(PayScene.SCENE_CODE_SHOP.value()).contains(payModeId);
                else
                    return false;
            } else {
                return this.mPayModeIdMap.get(sceneCode).contains(payModeId);
            }
        }

        private List<PaymentModeShop> getPaymentModeShopByType(Integer sceneCode, Integer paymentModeType) {
            PaymentModeShopHolder dataHolder = paymentModeShopHolderMap.get(sceneCode);
            if (dataHolder != null) {
                return dataHolder.getPaymentModeShopByType(paymentModeType);
            } else {
                return null;
            }
        }

        private List<PaymentModeShop> getOtherPaymentModeShop(Integer sceneCode) {
            return this.getPaymentModeShopByType(sceneCode, PayModelGroup.OTHER.value());
        }
    }

    //add 20170510 end  for 支付方式场景

    //add v8.12 begin
    private static class PaymentModeShopHolder extends BaseDataHolder<Integer, PaymentModeShop> {

        private PaymentModeShopHolder() {
            super();
        }

        protected void addData(PaymentModeShop paymentModeShop) {
            if (paymentModeShop.getPaymentModeType() != null) {//PaymentModeType作为Key
                if (this.get(paymentModeShop.getPaymentModeType().value()) == null) {
                    List<PaymentModeShop> list = new ArrayList<PaymentModeShop>();
                    list.add(paymentModeShop);
                    this.put(paymentModeShop.getPaymentModeType().value(), list);

                } else {
                    this.get(paymentModeShop.getPaymentModeType().value()).add(paymentModeShop);
                }
            }
        }

        private List<PaymentModeShop> getPaymentModeShopByType(Integer paymentModeType) {

            return this.get(paymentModeType);
        }

        private boolean isContainsPayModelType(Integer paymentModeType) {
            return this.containsKey(paymentModeType);
        }
    }

    //移动支付后台开关
    public class MobilePaySettingHolder extends BaseDataHolder<String, MobilePaySetting> {
        Set<PayModeId> mOneCodeSet = new HashSet<PayModeId>();//缓存一码付的支付方式

        private MobilePaySettingHolder() {
            super();
        }

        private void updateData(List<MobilePaySetting> dataList) {
            this.clear();
            if (dataList != null) {
                mOneCodeSet.clear();
                //先遍历找出支持一码付的支付方式
                for (MobilePaySetting mobilePaySetting : dataList) {
                    if (mobilePaySetting.isSupportOneCodePay()) {
                        mOneCodeSet.add(mobilePaySetting.getPayModeId());
                    }
                }
                //遍历找出主扫和被扫
                for (MobilePaySetting mobilePaySetting : dataList) {
                    this.addData(mobilePaySetting);
                }
            }
        }

        @Override
        public void addData(MobilePaySetting mobilePaySetting) {
            String payModeCode = mobilePaySetting.getPayModeCode();//PayModeCode作为Key
            if (!TextUtils.isEmpty(payModeCode) && (MobilePayMode.PAY_MODE_SCANCODE.equals(payModeCode) || MobilePayMode.PAY_MODE_SHOWCODE.equals(payModeCode))) {
                //如果是被扫，要把支持一码付的单独分开
                if (MobilePayMode.PAY_MODE_SCANCODE.equals(payModeCode) && this.mOneCodeSet.contains(mobilePaySetting.getPayModeId())) {
                    payModeCode = MobilePayMode.PAY_MODE_SCAN_ONE_CODE;
                }
                if (this.get(payModeCode) == null) {
                    List<MobilePaySetting> list = new ArrayList<MobilePaySetting>();
                    list.add(mobilePaySetting);
                    this.put(payModeCode, list);
                } else {
                    this.get(payModeCode).add(mobilePaySetting);
                }
            }
        }

        public List<MobilePaySetting> getMobilePaySettingByMode(String PayModeCode) {
            return this.get(PayModeCode);
        }

        public boolean isContainsPayModeCode(String PayModeCode) {
            return this.containsKey(PayModeCode);
        }
    }


    //容器基类， Map<K, List<V>>类型的容器
    private static abstract class BaseDataHolder<K, V> {
        Map<K, List<V>> mPaymentModeMap;

        BaseDataHolder() {
            init();
        }

        private void init() {
            mPaymentModeMap = new HashMap<K, List<V>>();
        }

        abstract void addData(V value);

        List<V> get(K key) {
            return mPaymentModeMap.get(key);
        }

        void put(K key, List<V> data) {
            mPaymentModeMap.put(key, data);
        }

        void clear() {
            mPaymentModeMap.clear();
        }

        boolean containsKey(K key) {
            return mPaymentModeMap.containsKey(key);
        }
    }
    //add v8.12 end
}
