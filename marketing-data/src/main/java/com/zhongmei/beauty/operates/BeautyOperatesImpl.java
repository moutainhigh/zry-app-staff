package com.zhongmei.beauty.operates;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryUtils;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeApplet;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.http.CalmNetWorkRequest;
import com.zhongmei.yunfu.http.processor.CalmDatabaseProcessor;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.message.RecisionDinnerReq;
import com.zhongmei.bty.basemodule.trade.message.TradeItemReq;
import com.zhongmei.beauty.operates.message.BeautyModifyReq;
import com.zhongmei.beauty.operates.message.BeautyTradeDeleteReq;
import com.zhongmei.beauty.operates.message.BeautyTradeReq;
import com.zhongmei.beauty.operates.message.BeautyTradeResp;
import com.zhongmei.beauty.utils.BeautyServerAddressUtil;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyOperatesImpl extends AbstractOpeartesImpl implements BeautyOperates {

    public BeautyOperatesImpl(IOperates.ImplContext context) {
        super(context);
    }


    @Override
    public void submitBeauty(TradeVo tradeVo, CalmResponseListener<ResponseObject<BeautyTradeResp>> listener, FragmentActivity activity) {
//        BeautyTradeReq beautyTradeReq=toBeaurtyReq(tradeVo,true);
        BeautyModifyReq beautyTradeReq = toBeautyModifyReq(tradeVo);
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(BeautyServerAddressUtil.submit())
                .requestContent(beautyTradeReq)
                .responseClass(BeautyTradeResp.class)
                .responseProcessor(new BeautyRespProcessor())
                .successListener(listener)
                .errorListener(listener)
                .with(activity)
                .tag("submitBeauty")
                .showLoading()
                .create();


    }

    @Override
    public void modifyBeauty(TradeVo tradeVo, CalmResponseListener<ResponseObject<BeautyTradeResp>> listener, FragmentActivity activity) {
        BeautyModifyReq beautyModifyReq = toBeautyModifyReq(tradeVo);
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(BeautyServerAddressUtil.modifyBeauty())
                .requestContent(beautyModifyReq)
                .responseClass(BeautyTradeResp.class)
                .responseProcessor(new BeautyRespProcessor())
                .successListener(listener)
                .errorListener(listener)
                .tag("modifyBeauty")
                .with(activity)
                .showLoading()
                .create();
    }

    @Override
    public void deleteTrade(Long tradeId, Long serverUpdateTime, Reason mReason, List<InventoryItemReq> inventoryItems, CalmResponseListener<ResponseObject<BeautyTradeResp>> listener, Fragment fragment) {
        BeautyTradeDeleteReq deleteReq = toTradeDeleteReq(tradeId, serverUpdateTime, mReason, inventoryItems);
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(BeautyServerAddressUtil.deleteTrade())
                .requestContent(deleteReq)
                .responseClass(BeautyTradeResp.class)
                .responseProcessor(new BeautyRespProcessor())
                .successListener(listener)
                .errorListener(listener)
                .tag("deleteBeauty")
                .with(fragment)
                .showLoading()
                .create();
    }

    private BeautyTradeDeleteReq toTradeDeleteReq(Long tradeId, long serverUpdateTime, Reason mReason, List<InventoryItemReq> inventoryItems) {
        BeautyTradeDeleteReq deleteReq = new BeautyTradeDeleteReq();
        RecisionDinnerReq req = new RecisionDinnerReq();
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        req.setTradeId(tradeId);
        req.setServerUpdateTime(serverUpdateTime);
        if (mReason != null) {
            req.setReasonId(mReason.getId());
            req.setReasonContent(mReason.getContent());
        }

        deleteReq.setObsoleteRequest(req);
        deleteReq.setReturnInventoryItems(inventoryItems);

        return deleteReq;
    }

    public static BeautyModifyReq toBeautyModifyReq(TradeVo tradeVo) {
        BeautyModifyReq modifyReq = new BeautyModifyReq();
        InventoryUtils.setInventoryVoValue(tradeVo);
        InventoryChangeReq inventoryChangeReq = InventoryUtils.makeInventoryChangeReq(tradeVo.inventoryVo);
        BeautyTradeReq beautyTradeReq = toBeaurtyReq(tradeVo, true);
        modifyReq.setTradeRequest(beautyTradeReq);
        modifyReq.setInventoryRequest(inventoryChangeReq);
        return modifyReq;
    }


    private static BeautyTradeReq toBeaurtyReq(TradeVo tradeVo, boolean isFilterChange) {
        BeautyTradeReq req = new BeautyTradeReq();
        copyProperties(tradeVo.getTrade(), req);
        List<TradeItemReq> tradeItems = new ArrayList<TradeItemReq>();
        List<TradePrivilege> tradePrivileges = new ArrayList<TradePrivilege>();
        List<TradeItemProperty> tradeItemProperties = new ArrayList<TradeItemProperty>();
        List<TradeReasonRel> tradeReasonRels = new ArrayList<TradeReasonRel>();
        List<TradeItemOperation> tradeItemOperations = new ArrayList<TradeItemOperation>();
        List<TradePlanActivity> tradePlanActivities = new ArrayList<TradePlanActivity>();
        List<TradeItemPlanActivity> tradeItemPlanActivities = new ArrayList<TradeItemPlanActivity>();
        List<TradeUser> tradeItemUserList = new ArrayList<>();
//        List<TradePrivilegeLimitNumCardSku> tradePrivilegeLimitNumCardSkuList=new ArrayList<>();
        List<TradePrivilegeApplet> appletList = new ArrayList<>();
        if (Utils.isNotEmpty(tradeVo.getTradeReasonRelList())) {
            tradeReasonRels.addAll(tradeVo.getTradeReasonRelList());
        }
        // 营销活动
        if (Utils.isNotEmpty(tradeVo.getTradePlanActivityList())) {
            tradePlanActivities.addAll(tradeVo.getTradePlanActivityList());
        }
        if (Utils.isNotEmpty(tradeVo.getTradeItemPlanActivityList())) {
            tradeItemPlanActivities.addAll(tradeVo.getTradeItemPlanActivityList());
        }

        // 会员
        if (Utils.isNotEmpty(tradeVo.getTradeCustomerList())) {
            // 由于后台统计需要下单顾客(customerType为1的记录)，所以有登录会员时，如果没有下单顾客，就用登录会员补上
            List<TradeCustomer> tradeCustomerList = new ArrayList<TradeCustomer>();
            TradeCustomer bookingTradeCustomer = null;
            TradeCustomer memberTradeCustomer = null;
            for (TradeCustomer tc : tradeVo.getTradeCustomerList()) {
                tc.setDeviceIdenty(BaseApplication.getInstance().getDeviceIdenty());
                tradeCustomerList.add(tc);
                if (tc.isValid()) {
                    switch (tc.getCustomerType()) {
                        case MEMBER:
                            memberTradeCustomer = tc;
                            break;
                        case BOOKING:
                            bookingTradeCustomer = tc;
                            break;
                        default:
                            break;
                    }
                }
            }
            if (memberTradeCustomer != null && bookingTradeCustomer == null) {
                bookingTradeCustomer = new TradeCustomer();
                copyProperties(memberTradeCustomer, bookingTradeCustomer);
                bookingTradeCustomer.setId(null);
                bookingTradeCustomer.setServerUpdateTime(null);
                bookingTradeCustomer.setUuid(SystemUtils.genOnlyIdentifier());
                bookingTradeCustomer.setCustomerType(CustomerType.BOOKING);
                tradeCustomerList.add(bookingTradeCustomer);
            }

            req.setTradeCustomers(tradeCustomerList);
        }

        // 优惠
        if (tradeVo.getTradePrivileges() != null) {
            for (TradePrivilege tp : tradeVo.getTradePrivileges()) {
                tp.setTradeId(tradeVo.getTrade().getId());
                tp.setDeviceIdenty(BaseApplication.getInstance().getDeviceIdenty());
                tradePrivileges.add(tp);
            }
        }
        //优惠劵
        if (tradeVo.getCouponPrivilegeVoList() != null) {
            for (CouponPrivilegeVo couponPrivilegeVo : tradeVo.getCouponPrivilegeVoList()) {
                if (couponPrivilegeVo != null) {
                    TradePrivilege tp = couponPrivilegeVo.getTradePrivilege();
                    if (!TextUtils.isEmpty(tp.getUuid()) || tp.getStatusFlag() == StatusFlag.VALID) {
                        tp.setTradeId(tradeVo.getTrade().getId());
                    }
                    tradePrivileges.add(tp);
                }
            }
        }
        //宴请
        if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege() != null) {
            TradePrivilege tp = tradeVo.getBanquetVo().getTradePrivilege();
            if (tp.getId() != null || tp.getStatusFlag() == StatusFlag.VALID) {
                tp.setTradeId(tradeVo.getTrade().getId());
                tradePrivileges.add(tp);
            }
        }
        if (tradeVo.getIntegralCashPrivilegeVo() != null) {
            TradePrivilege tp = tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege();
            //异步改为使用uuid判断
            if (!TextUtils.isEmpty(tp.getUuid()) || tp.getStatusFlag() == StatusFlag.VALID) {
                tp.setTradeId(tradeVo.getTrade().getId());
                tradePrivileges.add(tp);
            }
        }
        List<WeiXinCouponsVo> listWXC = tradeVo.getmWeiXinCouponsVo();
        if (listWXC != null) {
            for (WeiXinCouponsVo mWeiXinCouponsVo : listWXC) {
                TradePrivilege tp = mWeiXinCouponsVo.getmTradePrivilege();
                if (tp.getId() != null || mWeiXinCouponsVo.isActived()) {
                    tp.setTradeId(tradeVo.getTrade().getId());
                    tradePrivileges.add(tp);
                }
            }

        }


        // 菜品
        if (tradeVo.getTradeItemList() != null) {
            for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                TradeItemReq tradeItemReq = new TradeItemReq();
                copyProperties(tradeItem, tradeItemReq);
                tradeItemReq.setUnitName("套");
                tradeItemReq.setDeviceIdenty(BaseApplication.sInstance.getDeviceIdenty());
                tradeItemReq.setCardSaleInfos(tradeItemVo.getCardSaleInfos());
                tradeItems.add(tradeItemReq);
                boolean invalid = (tradeItem.getStatusFlag() != StatusFlag.VALID);
                if (tradeItemVo.getTradeItemPrivilege() != null) {
                    TradePrivilege tp = tradeItemVo.getTradeItemPrivilege();
                    //add 20161102
                    if (tp.getStatusFlag() == StatusFlag.VALID || tp.getId() != null || !TextUtils.isEmpty(tp.getUuid())) {
                        // 无效的商品对应的优惠也置为无效
                        if (invalid && tp.getStatusFlag() != StatusFlag.INVALID) {
                            tp.setStatusFlag(StatusFlag.INVALID);
                            tp.setChanged(true);
                        }

                        tp.setTradeId(tradeVo.getTrade().getId());
                        tradePrivileges.add(tp);
                    }
                }
                if (tradeItemVo.getTradeItemPropertyList() != null) {
                    List<TradeItemProperty> itemProperties = tradeItemVo.getTradeItemPropertyList();
                    for (TradeItemProperty ip : itemProperties) {
                        // 无效的商品对应的属性也置为无效
                        if (invalid && ip.getStatusFlag() != StatusFlag.INVALID) {
                            ip.setStatusFlag(StatusFlag.INVALID);
                            ip.setChanged(true);
                        }
                    }
                    tradeItemProperties.addAll(itemProperties);
                }
                List<TradeReasonRel> reasonRelList = tradeItemVo.getReasonRelList();
                if (reasonRelList != null) {
                    tradeReasonRels.addAll(reasonRelList);
                }
                if (tradeItemVo.getTradeItemOperations() != null && !tradeItemVo.getTradeItemOperations().isEmpty()) {
                    for (TradeItemOperation tradeItemOperation : tradeItemVo.getTradeItemOperations()) {
                        tradeItemOperation.setTradeItemUuid(tradeItem.getUuid());
                        tradeItemOperations.add(tradeItemOperation);
                    }
                }

                //礼品券
                if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null) {
                    TradePrivilege tp = tradeItemVo.getCouponPrivilegeVo().getTradePrivilege();
                    if (tradeItemVo.getCouponPrivilegeVo().isActived()
                            || tp.getId() != null) {
                        // 无效的商品对应的优惠也置为无效
                        if (invalid && tp.getStatusFlag() != StatusFlag.INVALID) {
                            tp.setStatusFlag(StatusFlag.INVALID);
                            tp.setChanged(true);
                        }
                        tp.setTradeId(tradeVo.getTrade().getId());
                        tp.setTradeUuid(tradeVo.getTrade().getUuid());
                        tradePrivileges.add(tp);
                    }

                }

                if (Utils.isNotEmpty(tradeItemVo.getTradeItemUserList())) {
                    for (TradeUser tradeItemUser : tradeItemVo.getTradeItemUserList()) {
                        tradeItemUser.setTradeId(tradeVo.getTrade().getId());
                        tradeItemUser.setTradeUuid(tradeVo.getTrade().getUuid());
                        tradeItemUserList.add(tradeItemUser);
                    }
                }
                if (tradeItemVo.getCardServicePrivilegeVo() != null) {
                    TradePrivilege cPrivilege = tradeItemVo.getCardServicePrivilegeVo().getTradePrivilege();
                    if (cPrivilege != null) {
                        cPrivilege.setTradeId(tradeVo.getTrade().getId());
                        tradePrivileges.add(cPrivilege);
                    }

//                    TradePrivilegeLimitNumCard  tradePrivilegeLimitNumCard=tradeItemVo.getCardServicePrivilegeVo().getTradePrivilegeLimitNumCard();
//                    if(tradePrivilegeLimitNumCard!=null){
//                        tradePrivilegeLimitNumCard.setTradeId(tradeVo.getTrade().getId());
//                        tradePrivilegeLimitNumCards.add(tradePrivilegeLimitNumCard);
//                    }
//                    TradePrivilegeLimitNumCardSku tradePrivilegeLimitNumCardSku=tradeItemVo.getCardServicePrivilegeVo().getTradePrivilegeLimitNumCardSku();
//                    if(tradePrivilegeLimitNumCardSku!=null){
//                        tradePrivilegeLimitNumCardSku.setTradeId(tradeVo.getTrade().getId());
//                        tradePrivilegeLimitNumCardSkuList.add(tradePrivilegeLimitNumCardSku);
//                    }

                }

                if (tradeItemVo.getAppletPrivilegeVo() != null) {
                    TradePrivilege aPrivilege = tradeItemVo.getAppletPrivilegeVo().getTradePrivilege();
                    if (aPrivilege != null) {
                        aPrivilege.setTradeId(tradeVo.getTrade().getId());
                        tradePrivileges.add(aPrivilege);
                    }
                }
            }
        }


        TradeTable tradeTable = Utils.isNotEmpty(tradeVo.getTradeTableList()) ? tradeVo.getTradeTableList().get(0) : null;

        // 过滤掉没有改变的菜和属性，只保留有改动的
        if (isFilterChange) {
            for (int i = tradeItems.size() - 1; i >= 0; i--) {
                // 未修改的
                if (!tradeItems.get(i).isChanged()) {
                    tradeItems.remove(i);
                } else {
                    if (tradeTable != null) {
                        tradeItems.get(i).setTradeTableId(tradeTable.getId());
                        tradeItems.get(i).setTradeTableUuid(tradeTable.getUuid());
                    }
                }
            }
            for (int i = tradeItemProperties.size() - 1; i >= 0; i--) {
                if (!tradeItemProperties.get(i).isChanged()) {
                    tradeItemProperties.remove(i);
                }
            }
            for (int i = tradeReasonRels.size() - 1; i >= 0; i--) {
                if (!tradeReasonRels.get(i).isChanged()) {
                    tradeReasonRels.remove(i);
                }
            }
            for (int i = tradePlanActivities.size() - 1; i >= 0; i--) {
                if (!tradePlanActivities.get(i).isChanged()) {
                    tradePlanActivities.remove(i);
                }
            }
            for (int i = tradeItemPlanActivities.size() - 1; i >= 0; i--) {
                TradeItemPlanActivity tradeItemPlanActivity = tradeItemPlanActivities.get(i);
                if (!tradeItemPlanActivity.isChanged()) {
                    tradeItemPlanActivities.remove(i);
                } else if (tradeItemPlanActivity.getTradeUuid() == null) {
                    // 处理点菜界面传送后厨时无tradeUuid问题
                    tradeItemPlanActivity.setTradeUuid(tradeVo.getTrade().getUuid());
                }
            }

            req.setTradeItems(tradeItems);
            if (!tradeItemProperties.isEmpty()) {
                req.setTradeItemProperties(tradeItemProperties);
            }
            if (!tradeReasonRels.isEmpty()) {
                req.setTradeReasonRels(tradeReasonRels);
            }
            if (!tradePrivileges.isEmpty()) {
                req.setTradePrivileges(tradePrivileges);
            }
            if (!tradeItemOperations.isEmpty()) {
                req.setTradeItemOperations(tradeItemOperations);
            }
            List<TradeExtra> tradeExtraList = new ArrayList<TradeExtra>();
            if (tradeVo.getTradeExtra() != null) {
                if (isFilterChange) {//modify v8.4 过滤没改变的
                    if (tradeVo.getTradeExtra().isChanged()) {
                        tradeExtraList.add(tradeVo.getTradeExtra());
                        req.setTradeExtra(tradeVo.getTradeExtra());
                    }
                } else {
                    tradeExtraList.add(tradeVo.getTradeExtra());
                    req.setTradeExtra(tradeVo.getTradeExtra());
                }
            }
            req.setTradeExtras(tradeExtraList);
            if (!tradePlanActivities.isEmpty()) {
                req.setTradePlanActivities(tradePlanActivities);
            }
            if (!tradeItemPlanActivities.isEmpty()) {
                req.setTradeItemPlanActivities(tradeItemPlanActivities);
            }
            //菜品打包
            if (tradeVo.getTradeItemExtraList() != null) {
                req.setTradeItemExtras(tradeVo.getTradeItemExtraList());
            }


            List<TradeUser> tradeUsers = new ArrayList<>();
            if (tradeVo.getTradeUsers() != null) {
                for (TradeUser tradeUser : tradeVo.getTradeUsers()) {
                    tradeUser.setTradeId(tradeVo.getTrade().getId());
                    tradeUser.setTradeUuid(tradeVo.getTrade().getUuid());
                    tradeUsers.add(tradeUser);
                }
            }

            if (tradeItemUserList != null) {
                tradeUsers.addAll(tradeItemUserList);
            }

            req.setTradeUsers(tradeUsers);

            if (Utils.isNotEmpty(tradeVo.getTradeTaxs())) {
                req.setTradeTaxs(tradeVo.getTradeTaxs());
            }
            List<TradeTable> tradeTableList = new ArrayList<>();
            if (Utils.isNotEmpty(tradeVo.getTradeTableList())) {
                for (TradeTable table : tradeVo.getTradeTableList()) {
                    if (!table.isChanged()) {
                        continue;
                    }
                    table.setTradeId(tradeVo.getTrade().getId());
                    table.setDeviceIdenty(BaseApplication.getInstance().getDeviceIdenty());
                    tradeTableList.add(table);
                }
            }
            req.setTradeTables(tradeTableList);
//            req.setTradePrivilegeLimitNumCardSkus(tradePrivilegeLimitNumCardSkuList);
            req.setTradePrivilegeApplets(appletList);
            req.setDeviceIdenty(BaseApplication.sInstance.getDeviceIdenty());
        }
        return req;
    }


    public static class BeautyRespProcessor extends CalmDatabaseProcessor<BeautyTradeResp> {
        @Override
        protected void transactionCallable(DatabaseHelper helper, BeautyTradeResp resp) throws Exception {
            saveData(helper, resp);
        }
    }

    public static void saveData(DatabaseHelper helper, BeautyTradeResp resp) throws Exception {
        DBHelperManager.saveEntities(helper, Trade.class, resp.getTrade());
//       DBHelperManager.saveEntities(helper, TradeExtra.class, resp.getTradeExtra());
        DBHelperManager.saveEntities(helper, TradePrivilege.class, resp.getTradePrivileges());
        DBHelperManager.saveEntities(helper, TradeCustomer.class, resp.getTradeCustomers());
        DBHelperManager.saveEntities(helper, TradeItem.class, resp.getTradeItems());
        DBHelperManager.saveEntities(helper, TradeItemProperty.class, resp.getTradeItemProperties());
        DBHelperManager.saveEntities(helper, TradePlanActivity.class, resp.getTradePlanActivities());
        DBHelperManager.saveEntities(helper, TradeItemPlanActivity.class, resp.getTradeItemPlanActivities());
        DBHelperManager.saveEntities(helper, TradeItemExtra.class, resp.getTradeItemExtras());
        DBHelperManager.saveEntities(helper, TradeUser.class, resp.getTradeItemUsers());
        DBHelperManager.saveEntities(helper, TradeReasonRel.class, resp.getTradeReasonRels());
        DBHelperManager.saveEntities(helper, TradeUser.class, resp.getTradeUsers());
        DBHelperManager.saveEntities(helper, TradeTable.class, resp.getTradeTables());
//       DBHelperManager.saveEntities(helper,TradePrivilegeLimitNumCard.class,resp.getTradePrivilegeLimitNumCards());
//       DBHelperManager.saveEntities(helper,TradePrivilegeLimitNumCardSku.class,resp.getTradePrivilegeLimitNumCardSkus());
        DBHelperManager.saveEntities(helper, Tables.class, resp.getTables());
        DBHelperManager.saveEntities(helper, TradePrivilegeApplet.class, resp.getTradePrivilegeApplets());
    }
}
