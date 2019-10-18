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

        public DishMemberPrice queryMemberPrice(long templetPriceId, long dishId);

    public List<EcCardKind> findEcCardKinds();

    public DishShop findDishopByEcCardKindId(Long ecCardKindId);


    public RechargeRuleVo findRechargeRule(Long cardLevelId) throws Exception;


    public RechargeRuleVo findRechargeRule() throws Exception;


    public RechargeRuleVo findRechargeRuleByKind(Long cardKindId) throws Exception;


    public RechargeRuleVo findEcCardRechargeRule(Long cardKindId, Long cardLevelId) throws Exception;


    public EcCardKind findEcCardKindById(Long id) throws Exception;


    public List<EcCardKindCommercialRel> findEcCardKindCommercialRel(Long cardKindId) throws Exception;


    public List<EcCardKindCommercialRel> findEcCardKindCommercialRel(EntityCardType entityCardType) throws Exception;




    public boolean getopenIdRegisterSetting() throws SQLException;


    public boolean getLoginQrCodeSetting() throws SQLException;

    public List<CustomerLevel> getCustomerLevelList() throws SQLException;


    CrmLevelStoreRule findCrmLevelStoreRule() throws SQLException;
}
