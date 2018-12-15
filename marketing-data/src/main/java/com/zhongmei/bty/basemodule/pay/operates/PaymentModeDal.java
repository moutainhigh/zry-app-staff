package com.zhongmei.bty.basemodule.pay.operates;

import com.zhongmei.yunfu.db.entity.MobilePaySetting;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;
import java.util.Set;

import com.zhongmei.yunfu.db.entity.trade.PaymentModeScene;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeShop;
import com.zhongmei.yunfu.db.enums.PayModelGroup;

/**
 * 付款方式接口
 */
public interface PaymentModeDal extends IOperates {

    /**
     * 查询所有可用付款方式
     *
     * @param date
     * @return
     * @throws Exception
     */
    List<PaymentModeShop> findAllPaymentMode() throws Exception;

    /**
     * 查询所有可用付款方式场景
     */
    List<PaymentModeScene> findAllPaymentModeScene();

    /**
     * 查询所有可用付款方式场景类别
     */
    Set<Integer> findAllPaymentModeSceneCode();

    /**
     * 通过类别查询付款
     *
     * @param date
     * @return
     * @throws Exception
     */
    List<PaymentModeShop> findPaymentModeByGroup(PayModelGroup group) throws Exception;


    /**
     * @Title: findPaymentModeNameByErpModeId
     * @Description: 根据支付类型值查看其支付类型显示名称
     * @Param @return
     * @Param @throws Exception TODO
     * @Return String 返回类型
     */
    String findPaymentModeNameByErpModeId(Long erpModeId);

    /**
     * 查询所有移动支付配置（wallet开通的支付渠道）
     */
    List<MobilePaySetting> findAllMobilePaySettings();
}
