package com.zhongmei.beauty.booking.util

import com.zhongmei.beauty.booking.bean.BeautyBookingVo
import com.zhongmei.beauty.entity.BookingTradeItemUser
import com.zhongmei.beauty.entity.UserVo
import com.zhongmei.bty.basemodule.booking.bean.BookingTradeItemVo
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty
import com.zhongmei.bty.basemodule.orderdish.bean.*
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager
import com.zhongmei.yunfu.context.session.Session
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateItemTool
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo
import com.zhongmei.bty.basemodule.trade.enums.TradeUserType
import com.zhongmei.yunfu.db.entity.trade.TradeItem
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty
import com.zhongmei.yunfu.db.enums.BusinessType
import com.zhongmei.yunfu.db.enums.DeliveryType
import com.zhongmei.yunfu.db.enums.StatusFlag
import com.zhongmei.yunfu.context.base.BaseApplication
import com.zhongmei.yunfu.context.util.SystemUtils
import com.zhongmei.yunfu.context.util.Utils
import com.zhongmei.yunfu.db.entity.trade.TradeUser
import java.math.BigDecimal

/**
 * 预定购物车构建工具

 */
class BeautyBookingShopTool {
    companion object {

        /**
         * dishVo 转换为bookingTradeItemVo
         */
        fun dishToBookingItem(dishVo: DishVo, bookingUuid: String, bookingId: Long?): BookingTradeItemVo {
            var shopcartItem: ShopcartItem = CreateItemTool.createShopcartItem(dishVo)
            var bookingTradeItem = buildBookingTradeItem(shopcartItem, bookingUuid, bookingId)
            var bProperties = ArrayList<BookingTradeItemProperty>()
            shopcartItem.properties?.forEach() { property ->
                var bookingItemProperty = buildTradeItemProperty(shopcartItem, property, bookingId)
                bProperties.add(bookingItemProperty)
            }
            var bookingTradeItemVo = BookingTradeItemVo()
            bookingTradeItemVo.tradeItem = bookingTradeItem
            bookingTradeItemVo.tradeItemPropertyList = bProperties
            return bookingTradeItemVo
        }


        fun buildBookingTradeItem(mShopcartItem: ShopcartItem, bookingUuid: String, bookingId: Long?): BookingTradeItem {
            val mBookingTradeItem = BookingTradeItem()
            mBookingTradeItem.validateCreate()
            if (mShopcartItem.creatorId != null) {//设置mShopcartItem带过来的creator
                mBookingTradeItem.creatorId = mShopcartItem.creatorId
                mBookingTradeItem.creatorName = mShopcartItem.creatorName
            }
            mBookingTradeItem.validateUpdate()
            // 本地生成的唯一标示
            mBookingTradeItem.uuid = mShopcartItem.getUuid()
            // 数量
            mBookingTradeItem.quantity = mShopcartItem.getTotalQty()
            // 单价
            mBookingTradeItem.price = mShopcartItem.getPrice()
            // 金额
            mBookingTradeItem.amount = mShopcartItem.getAmount()
            // 各种特征的金额合计
            mBookingTradeItem.propertyAmount = mShopcartItem.getPropertyAmount()
            mBookingTradeItem.feedsAmount = mShopcartItem.getFeedsAmount()
            // 售价
            mBookingTradeItem.actualAmount = mShopcartItem.getActualAmount()
            mBookingTradeItem.parentUuid = mShopcartItem.getParentUuid()
            // 商品名称
            mBookingTradeItem.dishName = mShopcartItem.getSkuName()
            // 商品UUID
            mBookingTradeItem.dishUuid = mShopcartItem.getSkuUuid()
            mBookingTradeItem.dishId = mShopcartItem.getSkuId()
            // 是套餐明细时记录下明细分组ID
            if (mShopcartItem is SetmealShopcartItem) {
                val setmealItem = mShopcartItem as SetmealShopcartItem
                mBookingTradeItem.dishSetmealGroupId = setmealItem.setmealGroupId
            }
            mBookingTradeItem.type = mShopcartItem.getType()

            // 单位名称
            mBookingTradeItem.unitName = mShopcartItem.getUnitName()
            // 销售类型
            mBookingTradeItem.saleType = mShopcartItem.getSaleType()
            mBookingTradeItem.bookingUuid = bookingUuid
            mBookingTradeItem.bookingId = bookingId
            // 设置菜品是否参与折扣
            mBookingTradeItem.enableWholePrivilege = mShopcartItem.getEnableWholePrivilege()
            // 标记是否是自定义菜品
            mBookingTradeItem.isChangePrice = mShopcartItem.getIsChangePrice()

            mBookingTradeItem.relateTradeItemId = mShopcartItem.getRelateTradeItemId()
            mBookingTradeItem.relateTradeItemUuid = mShopcartItem.getRelateTradeItemUuid()
            mBookingTradeItem.invalidType = mShopcartItem.getInvalidType()
            mBookingTradeItem.unitName = mShopcartItem.unitName
            mBookingTradeItem.statusFlag = mShopcartItem.getStatusFlag()
            mBookingTradeItem.deviceIdenty = BaseApplication.getInstance().deviceIdenty
            mBookingTradeItem.sort = 1;
            return mBookingTradeItem
        }

        fun buildTradeItemProperty(mShopcartItemBase: ShopcartItemBase<*>,
                                   mOrderProperty: OrderProperty, bookingId: Long?): BookingTradeItemProperty {
            val mTradeItemProperty = BookingTradeItemProperty()
            // 金额，等于 PRICE * QTY (套餐外壳数量 x 套餐数量)
            val price = mOrderProperty.getPropertyPrice()
            val qty = mShopcartItemBase.totalQty
            val amount = qty.multiply(price)
            mTradeItemProperty.amount = amount
            // 判断SERVER_ID_KEY是否为空，如果是为则不能进行类型转换并赋值给mTradeItemProperty的CreatoerId

            if (Session.getAuthUser() != null && Session.getAuthUser()!!.id != null) {
                val serverId = Session.getAuthUser()!!.id!!
                mTradeItemProperty.creatorId = serverId
            }

            mTradeItemProperty.creatorName = Session.getAuthUser()!!.name
            mTradeItemProperty.bookingId = bookingId
            // 单价
            mTradeItemProperty.price = price
            mTradeItemProperty.propertyName = mOrderProperty.getPropertyName()
            // 该字段后期会去掉，目前占用PropertyType.TASTE
            mTradeItemProperty.propertyType = mOrderProperty.getPropertyKind()
            mTradeItemProperty.propertyUuid = mOrderProperty.getPropertyUuid()
            mTradeItemProperty.quantity = qty
            mTradeItemProperty.bookingTradeItemUuid = mShopcartItemBase.uuid

            mTradeItemProperty.uuid = SystemUtils.genOnlyIdentifier()
            mTradeItemProperty.statusFlag = StatusFlag.VALID
            mTradeItemProperty.uuid = SystemUtils.genOnlyIdentifier()
            mTradeItemProperty.validateCreate()

            return mTradeItemProperty
        }


        /**
         * 将技师转换为预定技师对象
         */
        fun buildBookingItemUser(userVo: UserVo, bookingTradeItemUuid: String, bookingId: Long?): BookingTradeItemUser {
            var tradeItemUser = BookingTradeItemUser()
            tradeItemUser.bookingTradeItemUuid = bookingTradeItemUuid
            tradeItemUser.isAssign = if (userVo.isOppoint) {
                1
            } else {
                2
            }
            tradeItemUser.statusFlag = StatusFlag.VALID
            tradeItemUser.userType = TradeUserType.TECHNICIAN.value()
            tradeItemUser.bookingId = bookingId
            tradeItemUser.userId = userVo.user.id
            tradeItemUser.userName = userVo.user.name
            tradeItemUser.roleId = userVo.user.roleId
            tradeItemUser.roleName = userVo.user.roleName
            return tradeItemUser
        }

        fun covertBookToTradeItem(bookingVo: BeautyBookingVo): List<TradeItemVo>? {
            val bookingTradeItemList = bookingVo.bookingTradeItemVos
            if (Utils.isEmpty(bookingTradeItemList)) {
                return null
            }

            val tradeItemVoList = java.util.ArrayList<TradeItemVo>()
            for (bookingTradeItemVo in bookingTradeItemList) {
                val tradeItemVo = TradeItemVo()
                val tradeItem = convertBookingToTradeItem(bookingTradeItemVo.tradeItem)
                if (tradeItem != null) {
                    tradeItemVo.tradeItem = tradeItem
                    tradeItemVo.tradeItemUserList = convertBookTradeItemUserToTradeUser(bookingTradeItemVo.bookingTradeItemUsers, tradeItem);
                    covertTradeItemProperty(bookingTradeItemVo.tradeItemPropertyList, tradeItemVo)
                    tradeItemVoList.add(tradeItemVo)
                }
            }
            return tradeItemVoList
        }


        fun convertBookTradeItemUserToTradeUser(bookingTradeItemUsers: List<BookingTradeItemUser>?, tradeItem: TradeItem): List<TradeUser> {
            var tradeItemUsers = ArrayList<TradeUser>()
            bookingTradeItemUsers?.forEach { tradeItemUser ->
                tradeItemUsers.add(buildTradeUserByBookingItemUser(tradeItemUser, tradeItem))
            }
            return tradeItemUsers
        }

        fun buildTradeUserByBookingItemUser(bookingItemUser: BookingTradeItemUser, tradeItem: TradeItem): TradeUser {
            var tradeItemUser = TradeUser();
            tradeItemUser.setIsAssign(bookingItemUser.isAssign == 1);
            tradeItemUser.setType(1)
            tradeItemUser.roleId = bookingItemUser.roleId
            tradeItemUser.roleName = bookingItemUser.roleName
//            tradeItemUser.tradeItemId=tradeItem.id
            tradeItemUser.tradeItemUuid = tradeItem.uuid
            tradeItemUser.userId = bookingItemUser.userId
            tradeItemUser.userName = bookingItemUser.userName
            tradeItemUser.uuid = SystemUtils.genOnlyIdentifier();
            return tradeItemUser;
        }

        /**
         * @return 过期商品
         */
        fun copyBookingDish(bookingVo: BeautyBookingVo, dinnerShoppingCart: DinnerShoppingCart): List<IShopcartItem>? {
            val tradeItemVoList = covertBookToTradeItem(bookingVo)
            val tradeVo = dinnerShoppingCart.createOrder()
            tradeVo.trade.deliveryType = DeliveryType.HERE
            tradeVo.trade.businessType = BusinessType.BEAUTY
            tradeVo.tradeItemList = tradeItemVoList
            if (Utils.isEmpty(tradeItemVoList)) {
                return null
            }
            //美业预定未选桌台 写1
            tradeVo.oldDeskCount = BigDecimal.ONE
            val shopcartItemList = DinnertableTradeInfo.buildShopcartItem(tradeVo)
            if (Utils.isEmpty(shopcartItemList)) {
                return null
            }
            //重新构建tradeItem
            tradeVo.tradeItemList.clear()
            val overShopcartItemList = java.util.ArrayList<IShopcartItem>()
            for (iShopcartItem in shopcartItemList) {
                val dishShop = DishCache.getDishHolder().get(iShopcartItem.skuId)
                if (dishShop == null) {
                    overShopcartItemList.add(iShopcartItem)
                    continue
                }
                dinnerShoppingCart.addReadOnlyShippingToCart(dinnerShoppingCart.shoppingCartVo, iShopcartItem, false)
            }
            MathShoppingCartTool.mathTotalPrice(dinnerShoppingCart.shoppingCartDish, tradeVo)
            dinnerShoppingCart.createOrder()

            tradeVo.oldDeskCount = BigDecimal.ONE
            return overShopcartItemList
        }

        fun convertBookingToTradeItem(bookingTradeItem: BookingTradeItem): TradeItem? {
            //需要构建一个IshopCartItem来获取一些参数
            var dishVo = DishManager().getDishVoByBrandDishId(bookingTradeItem.dishId)

            if (dishVo == null) {
                return null
            }

            var shopcartItem: ShopcartItem = CreateItemTool.createShopcartItem(dishVo)

            val tradeItem = TradeItem()
//            tradeItem.id = bookingTradeItem.id
            tradeItem.uuid = bookingTradeItem.uuid
//            tradeItem.tradeId = bookingTradeItem.bookingId
            tradeItem.tradeUuid = bookingTradeItem.bookingUuid
            tradeItem.statusFlag = bookingTradeItem.statusFlag
            tradeItem.serverUpdateTime = bookingTradeItem.serverUpdateTime
            tradeItem.serverCreateTime = bookingTradeItem.serverCreateTime
            tradeItem.shopIdenty = bookingTradeItem.shopIdenty
            tradeItem.deviceIdenty = bookingTradeItem.deviceIdenty
            tradeItem.dishName = bookingTradeItem.dishName
            tradeItem.skuUuid = bookingTradeItem.dishUuid
            tradeItem.dishId = bookingTradeItem.dishId
            tradeItem.brandIdenty = bookingTradeItem.brandIdenty
            tradeItem.actualAmount = shopcartItem.actualAmount
            tradeItem.amount = shopcartItem.amount
            tradeItem.price = shopcartItem.price
            tradeItem.propertyAmount = shopcartItem.propertyAmount
//            tradeItem.dishSetmealGroupId = bookingTradeItem.dishSetmealGroupId
            tradeItem.creatorId = bookingTradeItem.creatorId
            tradeItem.creatorName = bookingTradeItem.creatorName
            tradeItem.updatorId = bookingTradeItem.updatorId
            tradeItem.updatorName = bookingTradeItem.updatorName
            tradeItem.clientCreateTime = System.currentTimeMillis()
            tradeItem.clientUpdateTime = System.currentTimeMillis()
            tradeItem.enableWholePrivilege = shopcartItem.enableWholePrivilege
            tradeItem.feedsAmount = shopcartItem.feedsAmount
            tradeItem.invalidType = shopcartItem.invalidType
            tradeItem.isChangePrice = shopcartItem.isChangePrice
//            tradeItem.parentId = bookingTradeItem.parentId
//            tradeItem.parentUuid = shopcartItem.parentUuid
            tradeItem.relateTradeItemId = shopcartItem.relateTradeItemId
            tradeItem.relateTradeItemUuid = shopcartItem.relateTradeItemUuid
//            tradeItem.returnQuantity = bookingTradeItem.returnQuantity
            tradeItem.saleType = shopcartItem.saleType
            tradeItem.sort = bookingTradeItem.sort
            tradeItem.tradeTableId = shopcartItem.tradeTableId
            tradeItem.tradeTableUuid = shopcartItem.tradeTableUuid
            tradeItem.tradeMemo = shopcartItem.memo
            tradeItem.type = shopcartItem.type
            tradeItem.unitName = shopcartItem.unitName
            tradeItem.quantity = shopcartItem.singleQty
            tradeItem.isChanged = true;
            tradeItem.deviceIdenty = bookingTradeItem.deviceIdenty;
            return tradeItem
        }


        private fun covertTradeItemProperty(itemPropertyList: List<BookingTradeItemProperty>?, tradeItemVo: TradeItemVo) {
            if (Utils.isEmpty(itemPropertyList)) {
                return
            }
            val tradeItemPropertyList = java.util.ArrayList<TradeItemProperty>()
            for (bookingTradeItemProperty in itemPropertyList!!) {
                val tradeItemProperty = TradeItemProperty()
                tradeItemProperty.id = bookingTradeItemProperty.id
                tradeItemProperty.uuid = bookingTradeItemProperty.uuid
                tradeItemProperty.shopIdenty = bookingTradeItemProperty.shopIdenty
                tradeItemProperty.deviceIdenty = bookingTradeItemProperty.deviceIdenty
                tradeItemProperty.brandIdenty = bookingTradeItemProperty.brandIdenty
                tradeItemProperty.statusFlag = bookingTradeItemProperty.statusFlag
                tradeItemProperty.amount = bookingTradeItemProperty.amount
                tradeItemProperty.price = bookingTradeItemProperty.price
                tradeItemProperty.tradeItemId = bookingTradeItemProperty.bookingTradeItemId
                tradeItemProperty.tradeItemUuid = bookingTradeItemProperty.bookingTradeItemUuid
                tradeItemProperty.clientCreateTime = bookingTradeItemProperty.clientCreateTime
                tradeItemProperty.clientUpdateTime = bookingTradeItemProperty.clientUpdateTime
                tradeItemProperty.serverCreateTime = bookingTradeItemProperty.serverCreateTime
                tradeItemProperty.serverUpdateTime = bookingTradeItemProperty.serverUpdateTime
                tradeItemProperty.creatorId = bookingTradeItemProperty.creatorId
                tradeItemProperty.creatorName = bookingTradeItemProperty.creatorName
                tradeItemProperty.updatorId = bookingTradeItemProperty.updatorId
                tradeItemProperty.updatorName = bookingTradeItemProperty.updatorName
                tradeItemProperty.isChanged = bookingTradeItemProperty.isChanged
                tradeItemProperty.quantity = bookingTradeItemProperty.quantity
                tradeItemProperty.propertyName = bookingTradeItemProperty.propertyName
                tradeItemProperty.propertyType = bookingTradeItemProperty.propertyType
                tradeItemProperty.propertyUuid = bookingTradeItemProperty.propertyUuid
                tradeItemProperty.isChanged = bookingTradeItemProperty.isChanged
                tradeItemPropertyList.add(tradeItemProperty)
            }
            tradeItemVo.tradeItemPropertyList = tradeItemPropertyList
        }
    }
}