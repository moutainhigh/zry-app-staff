package com.zhongmei.bty.basemodule.pay.operates;

import com.zhongmei.yunfu.db.entity.MobilePaySetting;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;
import java.util.Set;

import com.zhongmei.yunfu.db.entity.trade.PaymentModeScene;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeShop;
import com.zhongmei.yunfu.db.enums.PayModelGroup;


public interface PaymentModeDal extends IOperates {


    List<PaymentModeShop> findAllPaymentMode() throws Exception;


    List<PaymentModeScene> findAllPaymentModeScene();


    Set<Integer> findAllPaymentModeSceneCode();


    List<PaymentModeShop> findPaymentModeByGroup(PayModelGroup group) throws Exception;



    String findPaymentModeNameByErpModeId(Long erpModeId);


    List<MobilePaySetting> findAllMobilePaySettings();
}
