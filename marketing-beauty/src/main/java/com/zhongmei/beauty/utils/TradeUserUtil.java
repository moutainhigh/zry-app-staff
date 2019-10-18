package com.zhongmei.beauty.utils;

import android.content.Context;
import android.util.LongSparseArray;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.Role;
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



public class TradeUserUtil {


    public static TradeUser createTradeUser(User user, Trade trade) {
        TradeUser tradeUser = new TradeUser();
        tradeUser.setUuid(SystemUtils.genOnlyIdentifier());
        tradeUser.setUserId(user.getId());
        tradeUser.setUserName(user.getName());
        tradeUser.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        tradeUser.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        tradeUser.setStatusFlag(StatusFlag.VALID);
        tradeUser.setChanged(true);
        tradeUser.setRoleId(user.getRoleId());
        tradeUser.setRoleName(user.getRoleName());
        tradeUser.setType(1);
        return tradeUser;
    }


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


    public static void removeTradeUser(List<TradeUser> tradeUserList, User user) {
        if (Utils.isEmpty(tradeUserList)) {
            return;
        }
        Iterator tradeUserIterator = tradeUserList.iterator();
        while (tradeUserIterator.hasNext()) {
            TradeUser tradeUser = (TradeUser) tradeUserIterator.next();
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

    public static void removeTradeUsers(List<TradeUser> tradeUserList, TradeUser tradeUser) {
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



    public static TradeUser createTradeItemUser(User user, IShopcartItemBase iShopcartItemBase, boolean isAssigin) {
        TradeUser tradeItemUser = new TradeUser();
        tradeItemUser.setUuid(SystemUtils.genOnlyIdentifier());
        tradeItemUser.setIsAssign(isAssigin);
        tradeItemUser.setTradeItemId(iShopcartItemBase.getId());
        tradeItemUser.setTradeItemUuid(iShopcartItemBase.getUuid());
        tradeItemUser.setUserId(user.getId());
        tradeItemUser.setUserName(user.getName());
        tradeItemUser.setRoleId(user.getRoleId());
        tradeItemUser.setRoleName(user.getRoleName());
        tradeItemUser.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        tradeItemUser.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        tradeItemUser.setStatusFlag(StatusFlag.VALID);
        tradeItemUser.setChanged(true);
        tradeItemUser.setType(1);
        return tradeItemUser;
    }



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


    public static void updateTradeItemUsers(User user, IShopcartItemBase iShopcartItemBase, boolean isAssigin) {
        List<TradeUser> tradeItemUserList = iShopcartItemBase.getTradeItemUserList();
        if (Utils.isEmpty(tradeItemUserList) && isAssigin) {
            addTradeItemUsers(user, iShopcartItemBase, isAssigin);
            return;
        }
                boolean isFind = false;
        for (TradeUser tradeItemUser : tradeItemUserList) {
            if (!MathDecimal.isLongEqual(user.getId(), tradeItemUser.getUserId())) {
                continue;
            }
            isFind = true;
            tradeItemUser.setIsAssign(isAssigin);
        }
                if (!isFind)
            addTradeItemUsers(user, iShopcartItemBase, isAssigin);
    }


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
        String roleName = tradeUser.getRoleName();
        String userName = tradeUser.getUserName();
        return roleName + "-" + userName;
    }



    public static List<UserVo> getUserVos(Long tradeId, boolean isDefine, List<TradeUser> tradeUserList, IShopcartItemBase shopcartItemBase, ItemType itemType) {
        List<UserVo> listUserVo = new ArrayList<>();
        List<User> userList = Session.getFunc(UserFunc.class).getUsers();
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

        List<Role> listRole=Session.getFunc(UserFunc.class).getRoles();
        LongSparseArray<Role> mapRole=new LongSparseArray<>();
        for (Role role : listRole) {
            mapRole.put(role.getId(),role);
        }


        for (User user : userList) {
            UserVo userVo = new UserVo(user);
            if (selectedUserMap.get(user.getId()) != null) {
                userVo.setChecked(true);
            }
            Role role=mapRole.get(user.getRoleId());
            if(role!=null){
                userVo.getUser().setRoleName(role.getRoleName());
            }
            listUserVo.add(userVo);
        }

        return listUserVo;
    }
}
