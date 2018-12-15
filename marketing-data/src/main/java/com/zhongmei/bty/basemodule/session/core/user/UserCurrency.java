package com.zhongmei.bty.basemodule.session.core.user;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.context.data.ICurrency;

/**
 * Created by demo on 2018/12/15
 */

public class UserCurrency implements ICurrency {

    private ErpCurrency erpCurrency;

    private static UserCurrency instance = null;

    public UserCurrency(final Long currencyId) {
        erpCurrency = getErpCurrency(currencyId);
        if (erpCurrency == null) {
            erpCurrency = new ErpCurrency();
            erpCurrency.setId(currencyId);
        }
    }

    public static void init(final Long currencyId) {
        instance = new UserCurrency(currencyId);
    }

    public static UserCurrency get() {
        return instance;
    }

    @Override
    public Long getId() {
        return getErpCurrency().getId();
    }

    @Override
    public String getAreaCode() {
        return getErpCurrency().getAreaCode();
    }

    @Override
    public String getPhoneRegulation() {
        return getErpCurrency().getPhoneRegulation();
    }

    @Override
    public boolean checkPhone(String phone) {
        return true;
    }

    @Override
    public String getCountryZh() {
        return getErpCurrency().getCountryZh();
    }

    @Override
    public String getCountryEn() {
        return getErpCurrency().getCountryEn();
    }

    @Override
    public String getCurrencySymbol() {
        String currencySymbol = getErpCurrency().getCurrencySymbol();
        return TextUtils.isEmpty(currencySymbol) ? DEFAULT_SYMBOL : currencySymbol;
    }

    public ErpCurrency getErpCurrency() {
        return erpCurrency;
    }

    private ErpCurrency getErpCurrency(Long currencyId) {
        try {
            ErpCommercialRelationDal erpCommercialRelationDal = OperatesFactory.create(ErpCommercialRelationDal.class);
            return erpCommercialRelationDal.getErpCurrency(currencyId);
        } catch (Exception e) {
            return null;
        }
    }
}
