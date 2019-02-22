package com.zhongmei.beauty.utils;

import android.content.Context;
import android.util.LongSparseArray;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.enums.TradeUserType;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.beauty.db.BeautyDaoImpl;
import com.zhongmei.beauty.entity.UserVo;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 处理tradeUser与tradeItemUser的工具类
 * Created by demo on 2018/12/15
 */

public class TradeUserUtil {

    /**
     * @param user
     * @return
     */
    public static TradeUser createTradeUser(User user, Trade trade) {
        TradeUser tradeUser = new TradeUser();
        tradeUser.setUuid(SystemUtils.genOnlyIdentifier());
        tradeUser.setUserId(user.getId());
        tradeUser.setUserName(user.getName());
        tradeUser.setTradeId(trade.getId());
        tradeUser.setTradeUuid(trade.getUuid());
        tradeUser.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        tradeUser.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        tradeUser.setStatusFlag(StatusFlag.VALID);
        tradeUser.setChanged(true);
        tradeUser.setRoleId(user.getRoleId());
        tradeUser.setRoleName(user.getRoleName());
        tradeUser.setType(1);
        return tradeUser;
    }

    /**
     * 添加整单的销售员、技师等
     *
     * @param tradeUserList
     * @param user
     */
    public static void addTradeUser(List<TradeUser> tradeUserList, User user, Trade trade) {
        if (tradeUserList == null) {
            return;
        }
        boolean isFind = false;
        Iterator tradeUserIterator = tradeUserList.iterator();
        while (tradeUserIterator.hasNext()) {
            TradeUser tradeUser = (TradeUser) tradeUserIterator.next();
            if (tradeUser.getId() == null) {
                tradeUserIterator.remove();
            } else if (MathDecimal.isLongEqual(user.getId(), tradeUser.getUserId())) {
                if (tradeUser.getStatusFlag() == StatusFlag.INVALID) {
                    tradeUser.setStatusFlag(StatusFlag.VALID);
                    tradeUser.setChanged(true);
                }
                isFind = true;
            } else {
                tradeUser.setStatusFlag(StatusFlag.INVALID);
                tradeUser.setChanged(true);
            }
        }
        if (!isFind) {
            TradeUser tradeUser = createTradeUser(user, trade);
            tradeUserList.add(tradeUser);
        }
    }

    /**
     * 移除整单的销售员、技师等
     *
     * @param tradeUserList
     * @param user
     * @param identity
     */
    public static void removeTradeUser(List<TradeUser> tradeUserList, User user, int identity) {
        if (Utils.isEmpty(tradeUserList)) {
            return;
        }
        Iterator tradeUserIterator = tradeUserList.iterator();
        while (tradeUserIterator.hasNext()) {
            TradeUser tradeUser = (TradeUser) tradeUserIterator.next();
//            if (tradeUser.getUserType() != identity) {
//                continue;
//            }
            if (user != null && !MathDecimal.isLongEqual(tradeUser.getUserId(), user.getId())) {
                continue;
            }
            if (tradeUser.getId() == null) {
                tradeUserIterator.remove();
            } else {
                tradeUser.setStatusFlag(StatusFlag.INVALID);
                tradeUser.setChanged(true);
            }
        }
    }

    public static void removeTradeUser(List<TradeUser> tradeUserList, TradeUser tradeUser) {
        if (Utils.isEmpty(tradeUserList)) {
            return;
        }
        Iterator tradeUserIterator = tradeUserList.iterator();
        while (tradeUserIterator.hasNext()) {
            TradeUser mTradeUser = (TradeUser) tradeUserIterator.next();
            if (mTradeUser.getId() == null && mTradeUser.getUuid() != null && mTradeUser.getUuid().equals(tradeUser.getUuid())) {
                tradeUserIterator.remove();
            } else if (mTradeUser.getId() != null && mTradeUser.getId() == tradeUser.getId()) {
                tradeUser.setStatusFlag(StatusFlag.INVALID);
                tradeUser.setChanged(true);
            }
        }
    }


    /**
     * 构建tradeItemUser
     *
     * @param user
     * @param isAssigin
     * @return
     */
    public static TradeUser createTradeItemUser(User user, IShopcartItemBase iShopcartItemBase, boolean isAssigin) {
        TradeUser tradeItemUser = new TradeUser();
        tradeItemUser.setUuid(SystemUtils.genOnlyIdentifier());
        tradeItemUser.setIsAssign(isAssigin);
        tradeItemUser.setTradeItemId(iShopcartItemBase.getId());
        tradeItemUser.setTradeItemUuid(iShopcartItemBase.getUuid());
        tradeItemUser.setUserId(user.getId());
        tradeItemUser.setUserName(user.getName());
//        tradeItemUser.setUserType(identity);、
        tradeItemUser.setRoleId(user.getRoleId());
        tradeItemUser.setRoleName(user.getRoleName());
        tradeItemUser.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        tradeItemUser.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        tradeItemUser.setStatusFlag(StatusFlag.VALID);
        tradeItemUser.setChanged(true);
        tradeItemUser.setType(1);
        return tradeItemUser;
    }


    /**
     * 添加user
     *
     * @param user
     * @param iShopcartItemBase
     * @param identity
     * @param isAssigin
     */
    public static void addTradeItemUsers(User user, IShopcartItemBase iShopcartItemBase, boolean isAssigin) {
        List<TradeUser> tradeItemUserList = iShopcartItemBase.getTradeItemUserList();
        if (tradeItemUserList == null) {
            tradeItemUserList = new ArrayList<>();
            iShopcartItemBase.setTradeItemUserList(tradeItemUserList);
        }
        boolean isFind = false;
        Iterator iterator = tradeItemUserList.iterator();
        while (iterator.hasNext()) {
            TradeUser tradeItemUser = (TradeUser) iterator.next();
            if (tradeItemUser.getId() == null) {
                iterator.remove();
            } else if (MathDecimal.isLongEqual(user.getId(), tradeItemUser.getUserId())) {
                if (tradeItemUser.getStatusFlag() == StatusFlag.INVALID) {
                    tradeItemUser.setStatusFlag(StatusFlag.VALID);
                    tradeItemUser.setChanged(true);
                }
                isFind = true;
            } else {
                tradeItemUser.setStatusFlag(StatusFlag.INVALID);
                tradeItemUser.setChanged(true);
            }
        }
        if (!isFind) {
            TradeUser newTradeItemUser = createTradeItemUser(user, iShopcartItemBase, isAssigin);
            tradeItemUserList.add(newTradeItemUser);
        }

    }

    /**
     * 更新tradeItemuser的指定状态
     *
     * @param user
     * @param iShopcartItemBase
     * @param identity
     * @param isAssigin
     */
    public static void updateTradeItemUsers(User user, IShopcartItemBase iShopcartItemBase, int identity, boolean isAssigin) {
        List<TradeUser> tradeItemUserList = iShopcartItemBase.getTradeItemUserList();
        if (Utils.isEmpty(tradeItemUserList) && isAssigin) {
            addTradeItemUsers(user, iShopcartItemBase, isAssigin);
            return;
        }
        //以防有无效的数据
        boolean isFind = false;
        for (TradeUser tradeItemUser : tradeItemUserList) {
//            if(tradeItemUser.getUserType()!=identity){
//                continue;
//            }
            if (!MathDecimal.isLongEqual(user.getId(), tradeItemUser.getUserId())) {
                continue;
            }
            isFind = true;
            tradeItemUser.setIsAssign(isAssigin);
        }
        //未匹配到user 新建
        if (!isFind)
            addTradeItemUsers(user, iShopcartItemBase, isAssigin);
    }

    /**
     * 通过用户移除 tradeItemUser
     *
     * @param user
     * @param iShopcartItemBase
     */
    public static void removeTradeItemusers(User user, IShopcartItemBase iShopcartItemBase) {
        List<TradeUser> tradeItemUserList = iShopcartItemBase.getTradeItemUserList();
        if (tradeItemUserList == null) {
            return;
        }
        Iterator iterator = tradeItemUserList.iterator();
        while (iterator.hasNext()) {
            TradeUser tradeItemUser = (TradeUser) iterator.next();
            if (user != null && !MathDecimal.isLongEqual(user.getId(), tradeItemUser.getUserId())) {
                continue;
            }
//            if(tradeItemUser.getUserType()!=identity){
//                continue;
//            }
            if (tradeItemUser.getId() == null) {
                iterator.remove();
            } else {
                tradeItemUser.setStatusFlag(StatusFlag.INVALID);
                tradeItemUser.setChanged(true);
            }
        }
    }

    public static void removeTradeItemUserByUser(TradeUser tradeItemUser, IShopcartItemBase iShopcartItemBase) {
        if (Utils.isEmpty(iShopcartItemBase.getTradeItemUserList())) {
            return;
        }
        List<TradeUser> tradeItemUserList = iShopcartItemBase.getTradeItemUserList();
        if (tradeItemUserList == null) {
            return;
        }
        Iterator iterator = tradeItemUserList.iterator();
        while (iterator.hasNext()) {
            TradeUser itemUser = (TradeUser) iterator.next();
            if (!MathDecimal.isLongEqual(itemUser.getUserId(), tradeItemUser.getUserId())) {
                continue;
            }
            if (itemUser.getId() == null) {
                iterator.remove();
            } else {
                itemUser.setStatusFlag(StatusFlag.INVALID);
                itemUser.setChanged(true);
            }
        }
    }

    public static String getUserName(TradeUser tradeUser) {
//        return getUserName(context,tradeUser.getRoleName(),tradeUser.getUserName());
        String roleName = tradeUser.getRoleName();
        String userName = tradeUser.getUserName();
        return roleName + "-" + userName;
    }

//    public static String getUserName(Context context,String roleName,String name){
//        String namePrefix="";
//        if(userType==TradeUserType.SHOPOWER.value()){
//            namePrefix=context.getResources().getString(R.string.btn_shopowner_select);
//        }else if(userType==TradeUserType.ADVISER.value()){
//            namePrefix=context.getResources().getString(R.string.btn_adviser);
//        }else if(userType==TradeUserType.TECHNICIAN.value()){
//            namePrefix=context.getResources().getString(R.string.btn_cosmetologist);
//        }
//        return namePrefix+":"+name;
//    }


    public static List<UserVo> getUserVos(Long tradeId, boolean isDefine, int identity, List<TradeUser> tradeUserList, IShopcartItemBase shopcartItemBase, ItemType itemType) {
        List<UserVo> listUserVo = new ArrayList<>();
        List<User> userList = Session.getFunc(UserFunc.class).getUserByIdentity(identity);
        if (Utils.isEmpty(userList)) {
            return listUserVo;
        }

        LongSparseArray<Long> selectedUserMap = new LongSparseArray<>();
        LongSparseArray<Long> assginUserMap = new LongSparseArray<>();
        if (isDefine && Utils.isNotEmpty(tradeUserList)) {
            for (TradeUser tradeUser : tradeUserList) {
                if (tradeUser.getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }
//                if(tradeUser.getUserType().intValue()!=identity){
//                    continue;
//                }
                selectedUserMap.put(tradeUser.getUserId(), tradeUser.getUserId());
            }
        } else if (shopcartItemBase != null && Utils.isNotEmpty(shopcartItemBase.getTradeItemUserList())) {
            for (TradeUser tradeItemUser : shopcartItemBase.getTradeItemUserList()) {
                if (tradeItemUser.getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }
                selectedUserMap.put(tradeItemUser.getUserId(), tradeItemUser.getUserId());
                if (tradeItemUser.isAssign()) {
                    assginUserMap.put(tradeItemUser.getUserId(), tradeItemUser.getUserId());
                }
            }
        }

        LongSparseArray<Long> usedUserMap = new LongSparseArray<>();
        LongSparseArray<Long> usedItemUserMap = new LongSparseArray<>();
        if (isDefine) {
            List<TradeUser> userdTradeUserList = BeautyDaoImpl.getTradeUserListNo(BusinessType.BEAUTY);
            for (TradeUser tradeUser : userdTradeUserList) {
                if (MathDecimal.isLongEqual(tradeUser.getTradeId(), tradeId)) {
                    continue;
                }
                usedUserMap.put(tradeUser.getUserId(), tradeUser.getUserId());
            }
        } else {
            List<TradeUser> userItemUserList = BeautyDaoImpl.getTradeItemListByIdentity(TradeUserType.TECHNICIAN.value(), BusinessType.BEAUTY);
            for (TradeUser tradeItemUser : userItemUserList) {
                if (MathDecimal.isLongEqual(tradeItemUser.getTradeId(), tradeId)) {
                    continue;
                }
                usedItemUserMap.put(tradeItemUser.getUserId(), tradeItemUser.getUserId());
            }
        }

        for (User user : userList) {
            UserVo userVo = new UserVo(user);
            if (selectedUserMap.get(user.getId()) != null) {
                userVo.setChecked(true);
                if (assginUserMap.get(user.getId()) != null) {
                    userVo.setOppoint(true);
                }
            }
            if (isDefine) {
                if (usedUserMap.get(user.getId()) != null) {
                    userVo.setFree(false);
                }
            } else {
                if (usedItemUserMap.get(user.getId()) != null) {
                    userVo.setFree(false);
                }

                if (itemType == ItemType.CHILD) {
                    userVo.setChild(true);
                }
            }
            listUserVo.add(userVo);
        }

        return listUserVo;
    }
}
