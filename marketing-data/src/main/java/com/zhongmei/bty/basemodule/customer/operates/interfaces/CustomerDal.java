package com.zhongmei.bty.basemodule.customer.operates.interfaces;

import com.zhongmei.bty.basemodule.customer.bean.DishMemberPrice;
import com.zhongmei.bty.basemodule.customer.bean.RechargeRuleVo;
import com.zhongmei.bty.basemodule.customer.entity.CrmCustomerLevelRightsDish;
import com.zhongmei.yunfu.db.entity.crm.CrmLevelStoreRule;
import com.zhongmei.yunfu.db.entity.crm.CustomerGroupLevel;
import com.zhongmei.yunfu.db.entity.crm.CustomerLevel;
import com.zhongmei.bty.basemodule.customer.entity.EcCardKindCommercialRel;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardKind;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.bty.basemodule.discount.entity.CrmCustomerLevelRights;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDal extends IOperates {

    public CustomerLevel findCustomerLevelByLevelId(Long levelId) throws Exception;

    CustomerGroupLevel findCustomerGroupLevelByLevelId(Long levelId) throws Exception;

    public CrmCustomerLevelRights findCustomerLevelRightsByLevelId(Long levelId) throws Exception;

    public List<CrmCustomerLevelRightsDish> findCustomerLevelRightsDishsByLevelRightId(Long levelId) throws Exception;

    CustomerGroupLevel findCustomerGroupById(Long groupId) throws Exception;

    List<CustomerGroupLevel> findCustomerGroupAll() throws Exception;

    public DishMemberPrice findDishMemberPriceByDishId(long dishId);

    public DishMemberPrice findDishMemberPriceByDishId(long dishId, long levelId);

    //获取会员实体卡对应的会员价模板
    public DishMemberPrice queryMemberPrice(long templetPriceId, long dishId);

    public List<EcCardKind> findEcCardKinds();

    public DishShop findDishopByEcCardKindId(Long ecCardKindId);

    /**
     * 获取充值规则
     *
     * @param cardLevelId 实体卡种id
     * @return
     */
    public RechargeRuleVo findRechargeRule(Long cardLevelId) throws Exception;

    /**
     * 获取充值规则
     *
     * @param cardKindId 实体卡别
     * @return
     */
    public RechargeRuleVo findRechargeRuleByKind(Long cardKindId) throws Exception;

    /**
     * 获取实体卡(会员实体卡、匿名实体卡)充值规则
     *
     * @param cardKindId  卡种Id
     * @param cardLevelId 卡等级Id
     * @return
     * @throws Exception
     */
    public RechargeRuleVo findEcCardRechargeRule(Long cardKindId, Long cardLevelId) throws Exception;

    /**
     * 获取卡种
     *
     * @return
     * @throws Exception
     */
    public EcCardKind findEcCardKindById(Long id) throws Exception;

    /**
     * 根据卡种Id获取权益列表
     *
     * @param cardKindId
     * @return
     * @throws Exception
     */
    public List<EcCardKindCommercialRel> findEcCardKindCommercialRel(Long cardKindId) throws Exception;

    /**
     * 根据卡类型获取权益列表
     *
     * @param entityCardType
     * @return
     * @throws Exception
     */
    public List<EcCardKindCommercialRel> findEcCardKindCommercialRel(EntityCardType entityCardType) throws Exception;


    /**
     * 获取是否需要关注公众号才能通过openID方式注册会员
     */

    public boolean getopenIdRegisterSetting() throws SQLException;

    /**
     * 获取是否开通微信公众号
     */
    public boolean getLoginQrCodeSetting() throws SQLException;

    public List<CustomerLevel> getCustomerLevelList() throws SQLException;

    /**
     * 查询免密支付配置项
     *
     * @return
     * @throws SQLException
     */
    CrmLevelStoreRule findCrmLevelStoreRule() throws SQLException;
}
