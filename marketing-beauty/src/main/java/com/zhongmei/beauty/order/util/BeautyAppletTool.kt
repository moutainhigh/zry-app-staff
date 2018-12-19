package com.zhongmei.beauty.order.util

import android.content.Context
import android.os.Bundle
import com.zhongmei.bty.basemodule.discount.bean.AppletPrivilegeVo
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege
import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateItemTool
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeApplet
import com.zhongmei.yunfu.util.ValueEnums
import com.zhongmei.beauty.operates.message.BeautyAcitivityBuyRecordResp
import com.zhongmei.yunfu.Constant
import com.zhongmei.yunfu.db.entity.trade.Trade
import com.zhongmei.yunfu.db.enums.DishType
import com.zhongmei.yunfu.db.enums.PrivilegeType
import com.zhongmei.yunfu.context.util.SystemUtils
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase
import com.zhongmei.yunfu.db.enums.StatusFlag
import com.zhongmei.yunfu.context.util.Utils
import com.zhongmei.yunfu.util.ToastUtil
import java.math.BigDecimal
import java.util.*
import kotlin.collections.HashMap

/**
 * 小程序处理工具类

 */
class BeautyAppletTool {

    companion object {
        //添加小程序到购物车
        fun addDishToShopcart(applet: BeautyAcitivityBuyRecordResp, mChangePageLisener: ChangePageListener, changeMiddlePageListener: IChangeMiddlePageListener, context: Context) {
            applet.isUsed = true
            //临时添加
            var shopcarItem = CreateItemTool.createShopcartItem(applet.dishId)

            if (shopcarItem == null) {
                ToastUtil.showShortToast("没有查询到对应的商品信息")
                return;
            }

            shopcarItem.changeQty(BigDecimal(applet.goodsNum))
            doReleteApplet(shopcarItem, applet, context)
            when (shopcarItem.type) {
                DishType.COMBO -> {
                    DinnerShoppingCart.getInstance().addDishToShoppingCart(shopcarItem, false)
                    modifyCombo(mChangePageLisener, shopcarItem.uuid)
                    changeMiddlePageListener.showCombo(shopcarItem)
                }
                DishType.SINGLE ->
                    DinnerShoppingCart.getInstance().addDishToShoppingCart(shopcarItem, false)

            }
        }


        /**
         * 移除小程序
         */
        fun removeApplet(app: BeautyAcitivityBuyRecordResp) {
            app.isUsed = false
            removeAppletById(app.id.toString())
        }

        /**
         * 购物车移除小程序时，更新显示
         */
        fun doAppletRemove(shopcartItem: IShopcartItemBase, mProgramList: ArrayList<BeautyAcitivityBuyRecordResp>?): Boolean {
            if (shopcartItem.appletPrivilegeVo == null || !shopcartItem.appletPrivilegeVo.isPrivilegeValid) {
                return false
            }
            for (ba in mProgramList!!) {
                if (shopcartItem.appletPrivilegeVo.activityId.equals(ba.id)) {
                    ba.isUsed = false
                    return true;
                }
            }
            return false
        }

        /**
         * 通过活动id移除小程序
         */
        fun removeAppletById(activityId: String) {
            var shopcartItemList = DinnerShoppingCart.getInstance().shoppingCartDish
            for (item in shopcartItemList) {
                if (item.statusFlag == StatusFlag.INVALID || item.appletPrivilegeVo == null || !item.appletPrivilegeVo.isPrivilegeValid) {
                    continue
                }
                if (item.appletPrivilegeVo.activityId.toString().equals(activityId)) {
                    DinnerShoppingCart.getInstance().removeAppletItem(item)
                    break
                }
            }

        }


        /**
         * 跳转到套餐页面
         */
        private fun modifyCombo(mChangePageLisener: ChangePageListener, uuid: String) {
            if (mChangePageLisener != null) {
                val bundle = Bundle()
                bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, uuid)
                bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST)
                bundle.putBoolean(Constant.NONEEDCHECK, false)
                mChangePageLisener.changePage(ChangePageListener.DISHCOMBO, bundle)
            }
        }


        /**
         * 将活动关联shopcartItem
         */
        fun doReleteApplet(shopcartItem: ShopcartItem, applet: BeautyAcitivityBuyRecordResp, context: Context) {

            var appletPrivigeVo: AppletPrivilegeVo = AppletPrivilegeVo()
            var trade: Trade = DinnerShoppingCart.getInstance().order.trade
            var tradePrivilege: TradePrivilege = BuildPrivilegeTool.buildPrivige(applet.id, trade.uuid, shopcartItem, getAppletPrivilegeType(applet.type), getAppletNameByType(context, applet.type))
            appletPrivigeVo.tradePrivilege = tradePrivilege
            shopcartItem.appletPrivilegeVo = appletPrivigeVo
        }

        fun buildAppletPrivilege(shopcartItem: ShopcartItem, applet: BeautyAcitivityBuyRecordResp, trade: Trade, tradePrivilege: TradePrivilege): TradePrivilegeApplet {
            var tradePrivilegeApplet: TradePrivilegeApplet = TradePrivilegeApplet()
            tradePrivilegeApplet.uuid = SystemUtils.genOnlyIdentifier()
            tradePrivilegeApplet.brandDishId = applet.dishId
//            tradePrivilegeApplet.activityId=applet.id
            tradePrivilegeApplet.brandDishName = shopcartItem.skuName
            tradePrivilegeApplet.brandDishNum = shopcartItem.totalQty
            tradePrivilegeApplet.tradeId = trade.id
            tradePrivilegeApplet.tradeUuid = trade.uuid
            tradePrivilegeApplet.tradeItemId = shopcartItem.id
            tradePrivilegeApplet.tradeItemUuid = shopcartItem.uuid
            tradePrivilegeApplet.tradePrivilegeId = tradePrivilege.id
            tradePrivilegeApplet.tradePrivilegeUuid = tradePrivilege.uuid
            tradePrivilegeApplet.customerId = CustomerManager.getInstance().dinnerLoginCustomer.customerId
            return tradePrivilegeApplet
        }

        /**
         * 初始化小程序选中状态
         */
        fun initAppletStatus(list: List<BeautyAcitivityBuyRecordResp>): ArrayList<BeautyAcitivityBuyRecordResp> {
            var shopcartItemList = DinnerShoppingCart.getInstance().shoppingCartDish;
            if (Utils.isEmpty(shopcartItemList)) {
                return ArrayList(list)
            }
            var map = HashMap<String, String>()
            shopcartItemList.forEach continuing@{ item ->
                if (item.appletPrivilegeVo == null || !item.appletPrivilegeVo.isPrivilegeValid) {
                    return@continuing
                }
                map.put(item.appletPrivilegeVo.activityId.toString(), item.appletPrivilegeVo.activityId.toString())
            }
            var resultList = ArrayList<BeautyAcitivityBuyRecordResp>()
            list.forEach { it ->
                if (map[it.id.toString()] != null) {
                    it.isUsed = true
                } else {
                    it.isUsed = false
                }
                resultList.add(it)
            }
            return resultList
        }


        fun getAppletPrivilegeType(type: Int): PrivilegeType {
            when (type) {
                1 ->
                    return PrivilegeType.COLLAGE
                2 ->
                    return PrivilegeType.BARGAIN
                3 ->
                    return PrivilegeType.SECKILL
                else ->
                    return PrivilegeType.__UNKNOWN__
            }
        }

        /**
         * 根据活动类型返回字符串
         */
        fun getAppletNameByType(context: Context, type: Int): String {
            when (type) {
                PrivilegeType.COLLAGE.value() ->
                    return context.resources.getString(R.string.beauty_applet_collage)
                PrivilegeType.BARGAIN.value() ->
                    return context.resources.getString(R.string.beauty_applet_bargain)
                PrivilegeType.SECKILL.value() ->
                    return context.resources.getString(R.string.beauty_applet_seckill)
                else ->
                    return ""

            }
        }


    }

}