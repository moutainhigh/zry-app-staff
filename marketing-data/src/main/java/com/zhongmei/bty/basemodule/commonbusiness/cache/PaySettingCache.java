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

        public static List<PaymentModeShop> getOthersPaymentModeShops(Integer sceneCode) {
        if (LazySingletonHolder.INSTANCE.mPaymentModeContent != null)
            return LazySingletonHolder.INSTANCE.mPaymentModeContent.getOtherPaymentModeShop(sceneCode);
        else return null;
    }

        public static boolean isContainsPayModes(Integer sceneCode) {
        return LazySingletonHolder.INSTANCE.isContainsPayMode(sceneCode);
    }


    public static boolean isErpModeID(long id) {
        if (LazySingletonHolder.INSTANCE.mPaymentModeContent != null)
            return LazySingletonHolder.INSTANCE.mPaymentModeContent.isContainsPaymentModeId(PayScene.SCENE_CODE_SHOP.value(), id);
        else return false;
    }

        public static PaymentModeShop getPaymentModeShop(long erpModeId) {

        return LazySingletonHolder.INSTANCE.mPaymentModesMap.get(erpModeId);
    }



    public static MobilePaySettingHolder getMobilePaySettingHolder() {
        return LazySingletonHolder.INSTANCE.mMobilePaySettingHolder;
    }


    public static boolean isUnionpay() {
        ErpCommercialRelation erpComRel = LazySingletonHolder.INSTANCE.getmErpCommercialRelation();

        if (erpComRel != null && erpComRel.getIsUnionpay() == 1) {
            return true;
        }
        return false;
    }


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

        public static boolean isContainsPayModes() {
        return LazySingletonHolder.INSTANCE.isContainsPayMode(PayScene.SCENE_CODE_SHOP.value());
    }

    public static boolean isSupportGroupPay() {
        return LazySingletonHolder.INSTANCE.isSupportGroupPay;
    }

    public static void setSupportGroupPay(boolean supportGroupPay) {
        LazySingletonHolder.INSTANCE.isSupportGroupPay = supportGroupPay;
    }


    public static ElectronicInvoiceVo getElectronicInvoiceVo() {
        return LazySingletonHolder.INSTANCE.mElectronicInvoiceVo;
    }


    public static boolean isElectronicInvoiceOpen() {
        if (LazySingletonHolder.INSTANCE.mElectronicInvoiceVo != null)
            return LazySingletonHolder.INSTANCE.mElectronicInvoiceVo.isOpen();
        return false;
    }

    public static boolean isSupportOneCodePay() {        return LazySingletonHolder.INSTANCE.isSupportOneCodePay;
    }

    public static void setSupportOneCodePay(boolean isSuport) {        LazySingletonHolder.INSTANCE.isSupportOneCodePay = isSuport;
    }

    public static boolean isCanPayNoPwd(BigDecimal amount) {
        return LazySingletonHolder.INSTANCE.canPayNoPwd(amount);
    }

    private static final Uri URI_ERP_COMMERCIAL_RELATION = DBHelperManager.getUri(ErpCommercialRelation.class);

    private static final Uri URI_PAYMENT_MODE_ID = DBHelperManager.getUri(PaymentModeShop.class);

        private static final Uri URI_COMMERCIAL_CUSTOMER_SETTINGS = DBHelperManager.getUri(CommercialCustomSettings.class);

        private static final Uri URI_ELECTRONIC_INVOICE = DBHelperManager.getUri(ElectronicInvoice.class);

        private static final Uri URI_INVOICE_TAX_RATE = DBHelperManager.getUri(InvoiceTaxRate.class);

        private static final Uri URI_PAYMENT_MODE_SCENE = DBHelperManager.getUri(PaymentModeScene.class);

        private static final Uri URI_CRM_LEVEL_STORE_RULE = DBHelperManager.getUri(CrmLevelStoreRule.class);

        private static final Uri URI_CRM_MOBILE_PAY_SETTING = DBHelperManager.getUri(MobilePaySetting.class);

    private ErpCommercialRelation mErpCommercialRelation;

    private ElectronicInvoiceVo mElectronicInvoiceVo;

    private Map<Long, String> mPaymentModeNamesMap = new HashMap<Long, String>();
    private Map<Long, PaymentModeShop> mPaymentModesMap = new HashMap<Long, PaymentModeShop>();
    private Map<Long, Integer> mPayMenuOrderMap;
    private boolean isSupportGroupPay;
    private boolean isSupportOneCodePay;
    private PaymentModeContent mPaymentModeContent;
    private CrmLevelStoreRule mCrmLevelStoreRule;
    private MobilePaySettingHolder mMobilePaySettingHolder = new MobilePaySettingHolder();
    private DatabaseHelper.DataChangeObserver mObserver;

    private Context mContext;
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
        if (mPaymentModesMap != null) {            mPaymentModesMap.clear();
        }
    }


    private void backgroundRefresh() {
        ThreadUtils.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                refreshErpCommercialRelation();
                refreshPaymentModes();
                refreshElectronicInvoice();
                refreshPayMenuOrder();
                refreshCrmLevelStoreRule();
                initIsSupportGroupPay();                refreshMobilePaySetting();            }
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

    private void refreshMobilePaySetting() {        PaymentModeDal paymentModeDal = OperatesFactory.create(PaymentModeDal.class);
        List<MobilePaySetting> list = paymentModeDal.findAllMobilePaySettings();
        mMobilePaySettingHolder.updateData(list);
    }


    private void refreshElectronicInvoice() {
        mElectronicInvoiceVo = SystemSettingsManager.findElectronicInvoiceVo();
    }

        private void refreshPaymentModes() {
        try {
            PaymentModeContent _PaymentModeContent = new PaymentModeContent();            Map<Long, String> _PaymentModeNamesMap = new HashMap<Long, String>();            Map<Long, PaymentModeShop> _PaymentModeShopMap = new HashMap<Long, PaymentModeShop>();            boolean isUsedScene = false;            PaymentModeDal paymentModeDal = OperatesFactory.create(PaymentModeDal.class);
                        List<PaymentModeShop> modeList = paymentModeDal.findAllPaymentMode();
                        List<PaymentModeScene> sceneList = paymentModeDal.findAllPaymentModeScene();
                        Set<Integer> sceneCodeSet = paymentModeDal.findAllPaymentModeSceneCode();
                        if (sceneList != null && !sceneList.isEmpty() && sceneCodeSet != null) {
                isUsedScene = true;
            }
            if (modeList != null && !modeList.isEmpty()) {
                for (PaymentModeShop shop : modeList) {
                    if (shop.getErpModeId() != null) {
                        _PaymentModeShopMap.put(shop.getErpModeId(), shop);                                                if (!sceneCodeSet.contains(PayScene.SCENE_CODE_SHOP.value())) {
                            _PaymentModeContent.setPaymentMode(PayScene.SCENE_CODE_SHOP.value(), shop);
                        }
                                                if (!sceneCodeSet.contains(PayScene.SCENE_CODE_CHARGE.value())) {
                            _PaymentModeContent.setPaymentMode(PayScene.SCENE_CODE_CHARGE.value(), shop);
                        }
                                                if (!sceneCodeSet.contains(PayScene.SCENE_CODE_WRITEOFF.value())) {
                            _PaymentModeContent.setPaymentMode(PayScene.SCENE_CODE_WRITEOFF.value(), shop);
                        }
                                                if (!TextUtils.isEmpty(shop.getName())) {
                            _PaymentModeNamesMap.put(shop.getErpModeId(), shop.getName());
                        }
                    }
                }
                this.mPaymentModeNamesMap = _PaymentModeNamesMap;                this.mPaymentModesMap = _PaymentModeShopMap;            }
                        if (isUsedScene) {
                for (PaymentModeScene scene : sceneList) {
                    if (scene.getShopErpModeId() != null && scene.getSceneCode() != null) {
                                                _PaymentModeContent.setPaymentMode(scene.getSceneCode(), _PaymentModeShopMap.get(scene.getShopErpModeId()));
                    }
                }
            }
            this.mPaymentModeContent = _PaymentModeContent;        } catch (Exception e) {
            Log.e(TAG, "refreshPaymentMode error!", e);
        }
    }

    public ErpCommercialRelation getmErpCommercialRelation() {
        return this.mErpCommercialRelation;
    }

            private boolean isContainsPayMode(Integer senceCode) {
        if (LazySingletonHolder.INSTANCE.mPaymentModeContent != null) {
            if (LazySingletonHolder.INSTANCE.mPaymentModeContent.paymentModeShopHolderMap.get(senceCode) != null) {                return true;
            }
            if (LazySingletonHolder.INSTANCE.mPaymentModeContent.mPayModeIdMap.get(senceCode) != null) {                return true;
            }
        }
        return false;
    }


    private String getPayModeName(long paymodeId) {
        String payModeName = this.mPaymentModeNamesMap.get(paymodeId);
                if (TextUtils.isEmpty(payModeName) && mContext != null) {
            String[] paymentModeName = mContext.getResources().getStringArray(R.array.trade_payment_mode);
            switch ((int) (paymodeId + 0)) {
                case 1:
                                        payModeName = paymentModeName[0];
                    break;
                case 2:
                                        payModeName = paymentModeName[1];
                    break;
                case 3:
                                        payModeName = paymentModeName[2];
                    break;
                case 4:
                                        payModeName = paymentModeName[3];
                    break;
                case 5:
                                        payModeName = paymentModeName[4];
                    break;
                case 6:
                                        payModeName = paymentModeName[5];
                    break;
                case 7:
                                        payModeName = paymentModeName[6];
                    break;
                                case -8:
                                        payModeName = paymentModeName[7];
                    break;
                case -9:
                                        payModeName = paymentModeName[8];

                    break;
                case -10:
                                        payModeName = paymentModeName[9];

                    break;
                case -11:
                                        payModeName = paymentModeName[10];
                    break;
                case -12:                     payModeName = paymentModeName[11];
                    break;
                case -13:                     payModeName = paymentModeName[12];
                    break;
                case -14:                     payModeName = paymentModeName[13];
                    break;
                case -15:                     payModeName = paymentModeName[14];
                    break;
                case -16:                     payModeName = paymentModeName[15];
                    break;
                case -17:                     payModeName = paymentModeName[16];
                    break;
                case -18:                     payModeName = paymentModeName[17];
                    break;
                case -19:                     payModeName = paymentModeName[18];
                    break;
                case -20:                     payModeName = paymentModeName[19];
                    break;
                case -21:                     payModeName = paymentModeName[20];
                    break;
                case -22:                     payModeName = paymentModeName[21];
                    break;
                case -23:                     payModeName = paymentModeName[22];
                    break;
                case -24:                     payModeName = paymentModeName[23];
                    break;
                case -25:                    payModeName = paymentModeName[24];
                    break;
                case -26:                    payModeName = paymentModeName[25];
                    break;
                case -29:                    payModeName = paymentModeName[26];
                    break;
                case -30:                    payModeName = paymentModeName[27];
                    break;
                case -34:                    payModeName = paymentModeName[28];
                    break;
                case -36:                    payModeName = paymentModeName[29];
                    break;
                case -37:                    payModeName = paymentModeName[30];
                    break;
                case -38:                    payModeName = paymentModeName[31];
                    break;
                case -40:                     payModeName = paymentModeName[32];
                    break;
                case -42:                     payModeName = paymentModeName[33];
                    break;
                case -101:                     payModeName = paymentModeName[34];
                    break;
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


    public boolean canPayNoPwd(BigDecimal amount) {
        if (mCrmLevelStoreRule != null) {
            if (mCrmLevelStoreRule.getPayNoPwd() == YesOrNo.YES                     && (mCrmLevelStoreRule.getPayNoPwdAmount() == null                     || mCrmLevelStoreRule.getPayNoPwdAmount().compareTo(new BigDecimal(-1)) == 0                     || mCrmLevelStoreRule.getPayNoPwdAmount().compareTo(amount) >= 0)) {
                return true;
            }
        }

        return false;
    }

    private class ErpSettingsObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(URI_ERP_COMMERCIAL_RELATION)) {
                refreshErpCommercialRelation();            }
            if (uris.contains(URI_PAYMENT_MODE_ID) || uris.contains(URI_PAYMENT_MODE_SCENE)) {
                refreshPaymentModes();            }
                        if (uris.contains(URI_COMMERCIAL_CUSTOMER_SETTINGS)
                    || uris.contains(URI_ELECTRONIC_INVOICE)
                    || uris.contains(URI_INVOICE_TAX_RATE)) {
                refreshElectronicInvoice();
            }
                        if (uris.contains(URI_CRM_LEVEL_STORE_RULE)) {
                refreshCrmLevelStoreRule();
            }
                        if (uris.contains(URI_CRM_MOBILE_PAY_SETTING)) {
                refreshMobilePaySetting();
            }
        }
    }

        private static class PaymentModeContent {
        Map<Integer, Set<Long>> mPayModeIdMap;        Map<Integer, PaymentModeShopHolder> paymentModeShopHolderMap;
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


        private static class PaymentModeShopHolder extends BaseDataHolder<Integer, PaymentModeShop> {

        private PaymentModeShopHolder() {
            super();
        }

        protected void addData(PaymentModeShop paymentModeShop) {
            if (paymentModeShop.getPaymentModeType() != null) {                if (this.get(paymentModeShop.getPaymentModeType().value()) == null) {
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

        public class MobilePaySettingHolder extends BaseDataHolder<String, MobilePaySetting> {
        Set<PayModeId> mOneCodeSet = new HashSet<PayModeId>();
        private MobilePaySettingHolder() {
            super();
        }

        private void updateData(List<MobilePaySetting> dataList) {
            this.clear();
            if (dataList != null) {
                mOneCodeSet.clear();
                                for (MobilePaySetting mobilePaySetting : dataList) {
                    if (mobilePaySetting.isSupportOneCodePay()) {
                        mOneCodeSet.add(mobilePaySetting.getPayModeId());
                    }
                }
                                for (MobilePaySetting mobilePaySetting : dataList) {
                    this.addData(mobilePaySetting);
                }
            }
        }

        @Override
        public void addData(MobilePaySetting mobilePaySetting) {
            String payModeCode = mobilePaySetting.getPayModeCode();            if (!TextUtils.isEmpty(payModeCode) && (MobilePayMode.PAY_MODE_SCANCODE.equals(payModeCode) || MobilePayMode.PAY_MODE_SHOWCODE.equals(payModeCode))) {
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
    }
