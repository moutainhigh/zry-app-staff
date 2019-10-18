package com.zhongmei.bty.basemodule.customer.operates.impls;

import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.customer.bean.DishMemberPrice;
import com.zhongmei.bty.basemodule.customer.bean.RechargeRuleVo;
import com.zhongmei.bty.basemodule.customer.bean.RechargeRuleVo.RechargeRuleDetailVo;
import com.zhongmei.bty.basemodule.customer.entity.CrmCustomerLevelRightsDish;
import com.zhongmei.bty.basemodule.discount.entity.CustomerDishPrivilege;
import com.zhongmei.bty.commonmodule.database.enums.SendType;
import com.zhongmei.yunfu.db.entity.crm.CrmCustomerThreshold;
import com.zhongmei.yunfu.db.entity.crm.CrmLevelStoreRule;
import com.zhongmei.yunfu.db.entity.crm.CrmLevelStoreRuleDetail;
import com.zhongmei.yunfu.db.entity.crm.CustomerGroupLevel;
import com.zhongmei.yunfu.db.entity.crm.CustomerLevel;
import com.zhongmei.bty.basemodule.customer.entity.EcCardKindCommercialRel;
import com.zhongmei.bty.basemodule.customer.entity.MemberPriceTemplet;
import com.zhongmei.bty.basemodule.customer.entity.MemberPriceTempletDetail;
import com.zhongmei.bty.basemodule.customer.enums.FullSend;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.customer.util.CrmEcCardStoreRuleDetailCompartor;
import com.zhongmei.bty.basemodule.customer.util.CrmLevelStoreRuleDetailCompartor;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardKind;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevel;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevelSetting;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardSettingDetail;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.bty.basemodule.discount.entity.CrmCustomerLevelRights;
import com.zhongmei.yunfu.db.entity.crm.CustomerSaveRule;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.Status;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.Brand;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerDalImpl extends AbstractOpeartesImpl implements CustomerDal {
    private static final String TAG = CustomerDalImpl.class.getSimpleName();

    public CustomerDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public CustomerLevel findCustomerLevelByLevelId(Long levelId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<CustomerLevel, Long> dao = helper.getDao(CustomerLevel.class);
            return dao.queryForId(levelId);

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public CustomerGroupLevel findCustomerGroupLevelByLevelId(Long levelId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<CustomerGroupLevel, Long> dao = helper.getDao(CustomerGroupLevel.class);
            return dao.queryForId(levelId);

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public CrmCustomerLevelRights findCustomerLevelRightsByLevelId(Long levelId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return null;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return null;
    }

    @Override
    public List<CrmCustomerLevelRightsDish> findCustomerLevelRightsDishsByLevelRightId(
            Long levelRightId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CrmCustomerLevelRightsDish, String> ruledao = helper.getDao(CrmCustomerLevelRightsDish.class);

            QueryBuilder<CrmCustomerLevelRightsDish, String> qb = ruledao.queryBuilder();

            qb.where().eq(CrmCustomerLevelRightsDish.$.customerLevelRightsId, levelRightId).and().eq(
                    CrmCustomerLevelRightsDish.$.statusFlag, StatusFlag.VALID);

            return qb.orderBy(CrmCustomerLevelRightsDish.$.id, true).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public CustomerGroupLevel findCustomerGroupById(Long groupId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<CustomerGroupLevel, Long> dao = helper.getDao(CustomerGroupLevel.class);
            return dao.queryForId(groupId);

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<CustomerGroupLevel> findCustomerGroupAll() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<CustomerGroupLevel, Long> dao = helper.getDao(CustomerGroupLevel.class);
            return dao.queryForAll();

        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }


    public DishMemberPrice findDishMemberPriceByDishId(long dishId) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<MemberPriceTempletDetail, Long> dao = helper.getDao(MemberPriceTempletDetail.class);
            QueryBuilder<MemberPriceTempletDetail, Long> builder = dao.queryBuilder();
            builder.where().eq(MemberPriceTempletDetail.$.brandDishId, dishId);
            List<MemberPriceTempletDetail> details = builder.query();
            if (details != null && details.size() > 0) {
                Dao<MemberPriceTemplet, Long> dao2 = helper.getDao(MemberPriceTemplet.class);
                MemberPriceTemplet memberPriceTemplet = dao2.queryForId(details.get(0).getPriceTempletId());

                DishMemberPrice dishMemberPrice = new DishMemberPrice();
                dishMemberPrice.setDiscount(details.get(0).getDiscount());
                dishMemberPrice.setMemberPrice(details.get(0).getMemberPrice());
                dishMemberPrice.setPriceName(memberPriceTemplet.getName());
                dishMemberPrice.setPriceType(memberPriceTemplet.getPriceType());
                dishMemberPrice.setPeriodStart(memberPriceTemplet.getPeriodStart());
                dishMemberPrice.setPeriodEnd(memberPriceTemplet.getPeriodEnd());

                return dishMemberPrice;
            }

        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }


    public DishMemberPrice findDishMemberPriceByDishId(long dishId, long levelId) {
        DishMemberPrice dishMemberPrice = null;
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<CustomerDishPrivilege, Long> dao = helper.getDao(CustomerDishPrivilege.class);
            List<CustomerDishPrivilege> customerDishPrivileges =
                    dao.queryBuilder()
                            .selectColumns(CustomerDishPrivilege.$.privilegeType, CustomerDishPrivilege.$.privilegeValue)
                            .where().eq(CustomerDishPrivilege.$.dishId, dishId).
                            and().eq(CustomerDishPrivilege.$.levelId, levelId).
                            and().eq(CustomerDishPrivilege.$.statusFlag, StatusFlag.VALID).query();

            if (Utils.isNotEmpty(customerDishPrivileges)) {
                CustomerDishPrivilege customerDishPrivilege = customerDishPrivileges.get(0);

                    if (customerDishPrivilege != null) {
                        dishMemberPrice = new DishMemberPrice();
                        dishMemberPrice.setDiscount(customerDishPrivilege.getPrivilegeValue().doubleValue());
                        dishMemberPrice.setMemberPrice(customerDishPrivilege.getPrivilegeValue().doubleValue());
                        dishMemberPrice.setPriceName("会员优惠名称");
                        dishMemberPrice.setPriceType(customerDishPrivilege.getPrivilegeType().value());
                        dishMemberPrice.setPeriodStart("00:00");
                        dishMemberPrice.setPeriodEnd("00:00");
                    }

            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return dishMemberPrice;
    }

    @Override
    public List<EcCardKind> findEcCardKinds() {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<EcCardKind> kinds = null;
        try {
            Dao<EcCardKind, Long> dao = helper.getDao(EcCardKind.class);
            QueryBuilder<EcCardKind, Long> builder = dao.queryBuilder();
            builder.where().eq(EcCardKind.$.statusFlag, StatusFlag.VALID);
            kinds = builder.query();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return kinds;
    }

    @Override
    public DishShop findDishopByEcCardKindId(Long cardKindId) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        DishShop dishShop = null;
        try {
            Dao<EcCardKind, Long> dao = helper.getDao(EcCardKind.class);
            QueryBuilder<EcCardKind, Long> builder = dao.queryBuilder();
            builder.where().eq(EcCardKind.$.id, cardKindId).and().eq(EcCardKind.$.statusFlag, StatusFlag.VALID);
            EcCardKind kind = builder.queryForFirst();
            if (kind != null) {
                Dao<DishShop, Long> dishShopDao = helper.getDao(DishShop.class);
                QueryBuilder<DishShop, Long> dsBuilder = dishShopDao.queryBuilder();
                dsBuilder.where().eq(DishShop.$.brandDishId, kind.getDishBrandId()).and().eq(DishShop.$.statusFlag,
                        StatusFlag.VALID);
                dishShop = dsBuilder.queryForFirst();
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return dishShop;
    }

    @Override
    public RechargeRuleVo findRechargeRule(Long cardLevelId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        RechargeRuleVo vo = new RechargeRuleVo();
        vo.setRuleDetailList(new ArrayList<RechargeRuleDetailVo>());
        try {
            if (cardLevelId != null) {                Dao<EcCardLevelSetting, Long> ecCardLevelSettingDao = helper.getDao(EcCardLevelSetting.class);
                QueryBuilder<EcCardLevelSetting, Long> build = ecCardLevelSettingDao.queryBuilder();
                build.where().eq(EcCardLevelSetting.$.cardLevelId, cardLevelId);
                EcCardLevelSetting ecCardLevelSetting = build.queryForFirst();
                if (ecCardLevelSetting != null) {
                    vo.setIsFullSend(FullSend.YES);
                    vo.setSendType(ecCardLevelSetting.getValueCardSendType());
                    Dao<EcCardSettingDetail, Long> ecCardLevelSettingDetailDao = helper.getDao(EcCardSettingDetail.class);
                    QueryBuilder<EcCardSettingDetail, Long> qb = ecCardLevelSettingDetailDao.queryBuilder();
                    qb.where().eq(EcCardSettingDetail.$.cardLevelId, cardLevelId);
                    List<EcCardSettingDetail> ecCardSettingDetailList = qb.query();
                    if (ecCardSettingDetailList != null && ecCardSettingDetailList.size() > 0) {
                        Collections.sort(ecCardSettingDetailList, new CrmEcCardStoreRuleDetailCompartor());
                        for (EcCardSettingDetail detail : ecCardSettingDetailList) {
                            RechargeRuleDetailVo detailVo = new RechargeRuleDetailVo();
                            detailVo.setRule(detail);
                            vo.getRuleDetailList().add(detailVo);
                        }
                    }
                }
            } else {                Dao<CrmLevelStoreRule, Long> crmLevelStoreRuleDao = helper.getDao(CrmLevelStoreRule.class);
                List<CrmLevelStoreRule> crmLevelStoreRuleList = crmLevelStoreRuleDao.queryForAll();
                if (crmLevelStoreRuleList != null && crmLevelStoreRuleList.size() > 0) {
                    CrmLevelStoreRule crmLevelStoreRule = crmLevelStoreRuleList.get(0);
                    vo.setIsFullSend(crmLevelStoreRule.getIsFullSend());
                    vo.setSendType(crmLevelStoreRule.getSendType());
                    Dao<CrmLevelStoreRuleDetail, Long> crmLevelStoreRuleDetailDao = helper.getDao(CrmLevelStoreRuleDetail.class);
                    QueryBuilder<CrmLevelStoreRuleDetail, Long> qb = crmLevelStoreRuleDetailDao.queryBuilder();
                    qb.where().eq(CrmLevelStoreRuleDetail.$.levelStoreRuleId, crmLevelStoreRule.getId());
                    List<CrmLevelStoreRuleDetail> crmLevelStoreRuleDetailList = qb.query();
                    if (crmLevelStoreRuleDetailList != null && crmLevelStoreRuleDetailList.size() > 0) {
                        Collections.sort(crmLevelStoreRuleDetailList, new CrmLevelStoreRuleDetailCompartor());
                        for (CrmLevelStoreRuleDetail detail : crmLevelStoreRuleDetailList) {
                            RechargeRuleDetailVo detailVo = new RechargeRuleDetailVo();
                            detailVo.setRule(detail);
                            vo.getRuleDetailList().add(detailVo);
                        }
                    }
                }
            }
            return vo;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }


    @Override
    public RechargeRuleVo findRechargeRule() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        RechargeRuleVo vo = new RechargeRuleVo();
        vo.setRuleDetailList(new ArrayList<RechargeRuleDetailVo>());
        try {
            Dao<CustomerSaveRule, Long> customerSaveRuleDao = helper.getDao(CustomerSaveRule.class);
            List<CustomerSaveRule> customerSaveRules = customerSaveRuleDao.queryForAll();
            if (Utils.isNotEmpty(customerSaveRules)) {
                vo.setIsFullSend(FullSend.YES);                vo.setSendType(SendType.FIXED);                for (CustomerSaveRule customerSaveRule : customerSaveRules) {
                    RechargeRuleDetailVo detailVo = new RechargeRuleDetailVo();
                    detailVo.setFullValue(customerSaveRule.getStoredValue());
                    detailVo.setSendValue(customerSaveRule.getGiveValue());
                    vo.getRuleDetailList().add(detailVo);
                }
            }
            return vo;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public RechargeRuleVo findRechargeRuleByKind(Long cardKindId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        Dao<EcCardLevel, Long> ecCardLevelDao = helper.getDao(EcCardLevel.class);
        QueryBuilder<EcCardLevel, Long> builder = ecCardLevelDao.queryBuilder();
        builder.where().eq(EcCardLevel.$.cardKindId, cardKindId);
        EcCardLevel level = builder.queryForFirst();
        return findRechargeRule(level.getId());
    }

    @Override
    public RechargeRuleVo findEcCardRechargeRule(Long cardKindId, Long cardLevelId) throws Exception {
        Checks.verifyNotNull(cardKindId, "cardKindId");
        Checks.verifyNotNull(cardLevelId, "cardLevelId");
        DatabaseHelper helper = DBHelperManager.getHelper();
        RechargeRuleVo vo = new RechargeRuleVo();
        vo.setRuleDetailList(new ArrayList<RechargeRuleDetailVo>());
        try {
            EcCardKind ecCardKind = findEcCardKindById(cardKindId);
                        if (ecCardKind != null && ecCardKind.getIsValueCard() == Bool.YES) {
                                boolean isSend = ecCardKind.getIsSend() == Bool.YES;
                vo.setIsFullSend(isSend ? FullSend.YES : FullSend.NO);

                                if (vo.getIsFullSend() == FullSend.YES) {
                    Dao<EcCardLevelSetting, Long> ecCardLevelSettingDao = helper.getDao(EcCardLevelSetting.class);
                    QueryBuilder<EcCardLevelSetting, Long> build = ecCardLevelSettingDao.queryBuilder();
                    build.where().eq(EcCardLevelSetting.$.cardLevelId, cardLevelId);
                    EcCardLevelSetting ecCardLevelSetting = build.queryForFirst();
                    if (ecCardLevelSetting != null) {
                        vo.setSendType(ecCardLevelSetting.getValueCardSendType());
                    }
                }

                                Dao<EcCardSettingDetail, Long> ecCardLevelSettingDetailDao = helper.getDao(EcCardSettingDetail.class);
                QueryBuilder<EcCardSettingDetail, Long> qb = ecCardLevelSettingDetailDao.queryBuilder();
                qb.where().eq(EcCardSettingDetail.$.cardLevelId, cardLevelId);
                List<EcCardSettingDetail> ecCardSettingDetailList = qb.query();
                if (ecCardSettingDetailList != null && ecCardSettingDetailList.size() > 0) {
                    for (EcCardSettingDetail detail : ecCardSettingDetailList) {
                        RechargeRuleDetailVo detailVo = new RechargeRuleDetailVo();
                        detailVo.setRule(detail);
                        vo.getRuleDetailList().add(detailVo);
                    }
                }
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return vo;
    }

    @Override
    public EcCardKind findEcCardKindById(Long id) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<EcCardKind, Long> dao = helper.getDao(EcCardKind.class);
            return dao.queryForId(id);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<EcCardKindCommercialRel> findEcCardKindCommercialRel(Long cardKindId) throws Exception {
        Checks.verifyNotNull(cardKindId, "cardKindId");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<EcCardKindCommercialRel, Long> dao = helper.getDao(EcCardKindCommercialRel.class);
            QueryBuilder<EcCardKindCommercialRel, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(EcCardKindCommercialRel.$.cardKindId, cardKindId)
                    .and().eq(EcCardKindCommercialRel.$.statusFlag, StatusFlag.VALID);
            return queryBuilder.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<EcCardKindCommercialRel> findEcCardKindCommercialRel(EntityCardType entityCardType) throws Exception {
        Checks.verifyNotNull(entityCardType, "entityCardType");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<EcCardKind, Long> dao = helper.getDao(EcCardKind.class);
            QueryBuilder<EcCardKind, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.selectColumns(EcCardKind.$.id);
            queryBuilder.where().eq(EcCardKind.$.cardType, entityCardType)
                    .and().eq(EcCardKindCommercialRel.$.statusFlag, StatusFlag.VALID);
            List<EcCardKind> ecCardKinds = queryBuilder.query();
            if (Utils.isNotEmpty(ecCardKinds)) {
                List<EcCardKindCommercialRel> ecCardKindCommercialRels = new ArrayList<EcCardKindCommercialRel>();
                for (EcCardKind ecCardKind : ecCardKinds) {
                    List<EcCardKindCommercialRel> childEcCardKindCommercialRels = findEcCardKindCommercialRel(ecCardKind.getId());
                    if (Utils.isNotEmpty(childEcCardKindCommercialRels)) {
                        ecCardKindCommercialRels.addAll(childEcCardKindCommercialRels);
                    }
                }

                return ecCardKindCommercialRels;
            }

            return Collections.emptyList();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public DishMemberPrice queryMemberPrice(long templetPriceId, long dishId) {

        DishMemberPrice dishMemberPrice = null;
        DatabaseHelper helper = DBHelperManager.getHelper(DBHelperManager.CALM_DATABSE_HELPER);

        try {
            Dao<MemberPriceTemplet, Long> templetDao = helper.getDao(MemberPriceTemplet.class);
            MemberPriceTemplet templet = templetDao.queryBuilder()
                    .selectColumns(MemberPriceTemplet.$.name,
                            MemberPriceTemplet.$.priceType,
                            MemberPriceTemplet.$.periodStart,
                            MemberPriceTemplet.$.periodEnd)
                    .where().eq(MemberPriceTemplet.$.id, templetPriceId).queryForFirst();

            Dao<MemberPriceTempletDetail, Long> templetDetailDao = helper.getDao(MemberPriceTempletDetail.class);
            Where<MemberPriceTempletDetail, Long> where = templetDetailDao.queryBuilder()
                    .selectColumns(MemberPriceTempletDetail.$.memberPrice,
                            MemberPriceTempletDetail.$.discount).where();
            where.and(where.eq(MemberPriceTempletDetail.$.priceTempletId, templetPriceId),
                    where.eq(MemberPriceTempletDetail.$.brandDishId, dishId));
            List<MemberPriceTempletDetail> templeteDetails = where.query();

            if (templeteDetails != null && templeteDetails.size() > 0) {
                dishMemberPrice = new DishMemberPrice();
                dishMemberPrice.setDiscount(templeteDetails.get(0).getDiscount());
                dishMemberPrice.setMemberPrice(templeteDetails.get(0).getMemberPrice());
                dishMemberPrice.setPriceName(templet.getName());
                dishMemberPrice.setPriceType(templet.getPriceType());
                dishMemberPrice.setPeriodStart(templet.getPeriodStart());
                dishMemberPrice.setPeriodEnd(templet.getPeriodEnd());
            }

        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return dishMemberPrice;

    }

    @Override
    public boolean getopenIdRegisterSetting() throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<CrmCustomerThreshold, Long> dao = helper.getDao(CrmCustomerThreshold.class);
            CrmCustomerThreshold crmCustomerThreshold = dao.queryBuilder().where()
                    .eq(CrmCustomerThreshold.$.customerThreSholdType, 1)
                    .and()
                    .eq(CrmCustomerThreshold.$.statusFlag, StatusFlag.VALID).queryForFirst();
            if (crmCustomerThreshold != null) {
                if (crmCustomerThreshold.getIsOpen() == Bool.YES) {
                    return true;
                } else {
                    return false;
                }
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return false;
    }

    @Override
    public boolean getLoginQrCodeSetting() throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<Brand, Long> dao = helper.getDao(Brand.class);
            Brand brand = dao.queryBuilder().where()
                    .eq(Brand._statusFlag, 0).queryForFirst();
            if (brand != null) {

                return false;
                            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return false;
    }

    @Override
    public List<CustomerLevel> getCustomerLevelList() throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CustomerLevel, Long> dao = helper.getDao(CustomerLevel.class);
            return dao.queryBuilder()
                    .where().eq(CustomerLevel.$.status, Status.VALID).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    public CrmLevelStoreRule findCrmLevelStoreRule() throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CrmLevelStoreRule, Long> dao = helper.getDao(CrmLevelStoreRule.class);
            CrmLevelStoreRule crmLevelStoreRule = dao.queryBuilder().queryForFirst();

            return crmLevelStoreRule;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

}
