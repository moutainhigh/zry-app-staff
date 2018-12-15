package com.zhongmei.beauty.order

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import com.zhongmei.yunfu.R
import com.zhongmei.bty.basemodule.beauty.BeautyCardManager
import com.zhongmei.yunfu.bean.req.CustomerResp
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart
import com.zhongmei.yunfu.db.entity.trade.TradeTable
import com.zhongmei.bty.basemodule.trade.event.ActionLoginEvent
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager
import com.zhongmei.yunfu.util.DensityUtil
import com.zhongmei.beauty.customer.BasicCustomerSearchFragment
import com.zhongmei.beauty.customer.BeautyCustomerLoginDialogFragment
import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants
import com.zhongmei.beauty.customer.dialog.BeautyCustomerDetailDialogFragment
import com.zhongmei.beauty.events.OrderDishMaskingEvent
import com.zhongmei.beauty.interfaces.ITableChoice
import com.zhongmei.beauty.order.event.BeautyCustmoerEvent
import com.zhongmei.beauty.order.event.BeautyOrderCustomerEvent
import com.zhongmei.beauty.widgets.BeautyTablePopWindow
import com.zhongmei.yunfu.db.entity.trade.Tables
import com.zhongmei.yunfu.db.enums.BusinessType
import com.zhongmei.bty.customer.CustomerActivity
import com.zhongmei.bty.customer.event.EventCreateOrEditCustomer
import com.zhongmei.bty.customer.event.EventRefreshDetail
import com.zhongmei.bty.customer.event.EventSaleCardCharging
import com.zhongmei.bty.customer.util.AppUtil
import com.zhongmei.yunfu.context.util.Utils
import de.greenrobot.event.EventBus
import kotlinx.android.synthetic.main.beauty_order_customer_login.*
import java.math.BigDecimal

class BeautyOrderCustomerLoginFragment : BasicCustomerSearchFragment(), View.OnClickListener, ITableChoice, PopupWindow.OnDismissListener {
    private lateinit var tablePopuwindow: BeautyTablePopWindow

    override val getEditTextSearch: EditText?
        get() = null
    override val getBtnSearch: View?
        get() = null
    override val getBtnPosCard: View?
        get() = null
    override val getBtnEditClear: View?
        get() = null
    override val getBtnSanCode: View?
        get() = null

    private var mCustomer: CustomerResp? = null

    private lateinit var mContext: Context

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        registerEventBus()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(activity, R.layout.beauty_order_customer_login, null)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewListener()
        beauty_order_customer_search_layout.isEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        tablePopuwindow = BeautyTablePopWindow(activity, BusinessType.BEAUTY)
        tablePopuwindow.setiTableChoiceListener(this)
        tablePopuwindow.setOnDismissListener(this)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        unregisterEventBus()
        if (tablePopuwindow != null) {
            tablePopuwindow.onDestoery()
        }
        super.onDestroy()
    }

    private fun bindViewListener() {
        beauty_order_customer_exit.setOnClickListener(this)
        beauty_order_customer_search_login.setOnClickListener(this)
        beauty_order_customer_room.setOnClickListener(this)
        beauty_order_customer_detail_info.setOnClickListener(this)
    }

    /**
     * 接受自动登陆的event
     *
     * 购物车刷新完成
     */
    fun onEventMainThread(event: BeautyCustmoerEvent) {
        if (event == null) {
            beauty_order_customer_search_layout.isEnabled = true
        } else {
            if (event.customerNew?.card != null) {
                authLoginByCardNum(event!!.customerNew!!.card.cardNum)
            } else {
                if (event.customerNew != null && !TextUtils.isEmpty(event!!.customerNew!!.mobile)) {
                    authLogin(event.customerNew!!.mobile)
                }
            }
        }
    }

    fun onEventMainThread(event: EventRefreshDetail) {
        if (mCustomer?.card != null) {
            refreshCustomerInfoByCard(mCustomer!!.card.cardNum)
        } else {
            refreshCustomerInfoByCustomer(mCustomer!!.mobile)
        }
    }

    /**
     * 刷新用户数据
     */
    fun refreshCustomerInfo(customer: CustomerResp, card: EcCard) {
        if (card != null) {
            authLoginByCardNum(card.cardNum)
        } else {
            if (customer != null && !TextUtils.isEmpty(customer.mobile)) {
                authLogin(customer.mobile)
            }
        }
    }

    /**
     * 储值返回
     * @param chargingResult
     */
    fun onEventMainThread(event: EventSaleCardCharging?) {
        if (event != null) {
            var chargeMoney = event.chargeMoney
            var sendMoney = event.sendMoney
            var customer = DinnerShopManager.getInstance().loginCustomer
            if (event.chargingType == EventSaleCardCharging.ChargingType.CARD) {
                var card = customer?.card
                if (card != null && card.cardNum.equals(event.ecCardInfo.cardNum)) {
                    card.valueCardAccount.remainValue = calculateRemainValue(BigDecimal.valueOf(card.valueCardAccount.remainValue), chargeMoney, sendMoney)
                }
            } else {
                customer.remainValue = calculateRemainValue(BigDecimal.valueOf(customer.remainValue), chargeMoney, sendMoney)
            }
            loginSuccess(customer)
        }
    }

    fun onEventMainThread(event: EventCreateOrEditCustomer) {
        if (event.type == CustomerActivity.PARAM_EDIT) {
            if (mCustomer?.card != null) {
                refreshCustomerInfoByCard(mCustomer!!.card.cardNum)
            } else {
                refreshCustomerInfoByCustomer(mCustomer!!.mobile)
            }
        }
    }

    /**
     * 计算余额
     * @param amount 原始金额
     * @param chargeMoney 充值金额
     * @param sendMoney 赠送金额
     */
    fun calculateRemainValue(amount: BigDecimal, chargeMoney: BigDecimal, sendMoney: BigDecimal): Double {
        var remainValue = amount
        if (chargeMoney != null && BigDecimal.ZERO.compareTo(chargeMoney) != 0) {
            remainValue = remainValue.add(chargeMoney)
            if (sendMoney != null && BigDecimal.ZERO.compareTo(sendMoney) != 0) {
                remainValue = remainValue.add(sendMoney)
            }
        } else if (sendMoney != null && BigDecimal.ZERO.compareTo(sendMoney) != 0) {
            remainValue = remainValue.add(sendMoney)
        }
        return remainValue.toDouble()
    }

    /**
     * 接受登陆的event
     */
    fun onEventMainThread(event: ActionLoginEvent) {
        if (DinnerShopManager.getInstance().loginCustomer != null) {
            loginSuccess(DinnerShopManager.getInstance().loginCustomer)
        }
    }

    fun onClickExit() {
        EventBus.getDefault().post(BeautyOrderCustomerEvent(BeautyOrderCustomerEvent.EventFlag.EXIT, null))
        exitDinnerShopCart(true)
        // 退出会员
        mCustomer = null // 清除数据
        BeautyCardManager.getInstance().exitCardManager()
    }

    fun loginOrOut() {
        if (mCustomer == null) {
            BeautyCustomerLoginDialogFragment().show(activity.getSupportFragmentManager(), "DinnerCustomerLoginDialog")
        } else {
            onClickExit();
        }
    }

    /**
     * 退出购物车清除优惠
     *
     * @param isCancelMiniDisplay 是否清空副屏
     */
    fun exitDinnerShopCart(isCancelMiniDisplay: Boolean) {
        if (isCancelMiniDisplay) {
            DisplayServiceManager.doCancel(activity)
        }
        DinnerShopManager.getInstance().loginCustomer = null
        DinnerShopManager.getInstance().shoppingCart.setOpenIdenty(null)

        if (DinnerShopManager.getInstance().isSepartShopCart) {
            SeparateShoppingCart.getInstance().setSeparateCustomer(null)
        } else {
            DinnerShoppingCart.getInstance().setDinnerCustomer(null)
        }
        DinnerShopManager.getInstance().shoppingCart.removeAllPrivilegeForCustomer(true, false)
        beauty_order_customer_search_layout.visibility = View.GONE
        beauty_order_customer_detail_info.visibility = View.GONE
        beauty_order_customer_base_info.visibility = View.GONE
        beauty_order_customer_search_layout.isEnabled = true
        content_parent.visibility = View.GONE;
    }

    /**
     * 刷新数据
     *
     * @param customerNew
     */
    override fun loginSuccess(customerNew: CustomerResp) {
        mCustomer = customerNew
        beauty_order_customer_search_layout.visibility = View.GONE
        showCustomerBaseInfo(customerNew)
        showCustomerDetailInfo(customerNew)
        EventBus.getDefault().post(BeautyOrderCustomerEvent(BeautyOrderCustomerEvent.EventFlag.LOGIN, customerNew))
        // 更新购物车积分
        updataDinnerIntegralCash(customerNew)
    }

    fun updataDinnerIntegralCash(customerNew: CustomerResp) {
        var manager = DinnerCashManager()
        manager.updateIntegralCash(customerNew)
    }

    fun showCustomerBaseInfo(customerNew: CustomerResp) {
        content_parent.visibility = View.VISIBLE;
        beauty_order_customer_base_info.visibility = View.VISIBLE
        beauty_order_customer_info_name.text = if (TextUtils.isEmpty(customerNew.customerName)) activity.getString(R.string.customer_no_name2) else customerNew.customerName
        beauty_order_customer_info_phone.text = if (TextUtils.isEmpty(customerNew.mobile)) "" else AppUtil.getTel(customerNew.mobile)
        beauty_order_customer_info_vip.visibility = if (null != customerNew.levelId) View.VISIBLE else View.GONE
        beauty_order_customer_info_sex.setImageResource(if (customerNew.sex == CustomerResp.SEX_FEMALE) R.drawable.beauty_order_customer_female else R.drawable.beauty_order_customer_male)
        bindCustomerInfo(customerNew)
    }

    fun bindCustomerInfo(customerNew: CustomerResp) {
        var baseStr: String
        if (customerNew.card != null) {
            baseStr = "余额" + if (customerNew.card.valueCardAccount == null) "0" else
                if (customerNew.card.valueCardAccount.remainValue == null) "0" else customerNew.card.valueCardAccount.remainValue.toString()
        } else {
            baseStr = "余额" +
                    if (customerNew.remainValue == null) "0" else customerNew.remainValue.toString() +
                            "元 | 持有卡" +
                            if (customerNew.cardCount == null) "0" else customerNew.cardCount.toString() +
                                    "张 | 优惠券" +
                                    if (customerNew.coupCount == null) "0" else customerNew.coupCount.toString() +
                                            "张 | 积分" +
                                            if (customerNew.integral == null) "0" else customerNew.integral.toString() +
                                                    "分"
        }
        beauty_order_customer_cardinfo_desc.setText(String.format(activity.getString(R.string.beauty_order_customer_cardinfo_desc), baseStr))
        var serviceStr = ""
        beauty_order_customer_lasttime_serve_desc.setText(String.format(activity.getString(R.string.beauty_order_customer_lasttime_serve_desc), serviceStr))
    }

    fun showCustomerDetailInfo(customerNew: CustomerResp) {
        beauty_order_customer_detail_info.visibility = View.VISIBLE
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.beauty_order_customer_exit -> {
                onClickExit()
            }
            R.id.beauty_order_customer_room -> {
                showTablePopuwindow()
            }
            R.id.beauty_order_customer_search_login -> BeautyCustomerLoginDialogFragment().show(activity.getSupportFragmentManager(), "DinnerCustomerLoginDialog")
            R.id.beauty_order_customer_detail_info -> showCustomerDetailDialog(mCustomer!!.customerId!!)
        }
    }

    fun showTablePopuwindow() {
        if (!tablePopuwindow.isShowing) {
            tablePopuwindow.showAsDropDown(beauty_order_customer_room, DensityUtil.dip2px(context, -5f), DensityUtil.dip2px(context, -5f))
            EventBus.getDefault().post(OrderDishMaskingEvent(true))//发送到BeautyOrderActivity显示蒙版
        }
    }


    /**
     * 加载详情
     */
    fun showCustomerDetailDialog(customerId: Long) {
        var fragment = BeautyCustomerDetailDialogFragment()
        var bundle = Bundle()
        bundle.putLong(BeautyCustomerConstants.KEY_CUSTOMER_ID, customerId)
        fragment.arguments = bundle
        fragment.show(childFragmentManager, "BeautyCustomerDetailDialogFragment")
    }

    fun setTables(tradeTables: List<TradeTable>) {
        if (Utils.isEmpty(tradeTables)) {
            return
        }
        var tablesNameTmp = ""
        for (tradeTable in tradeTables) {
            tablesNameTmp += tradeTable.tableName + ","
        }
        var tableName = tablesNameTmp.substring(0, tablesNameTmp.length - 1);
        beauty_order_customer_room.setText(tableName)
        if (tablePopuwindow != null) {
            tablePopuwindow.settables(tradeTables)
        }
    }

    override fun onDismiss() {
        EventBus.getDefault().post(OrderDishMaskingEvent(false))//去掉蒙版效果，发送到BeautyOrderActivity
    }


    /**
     * 桌台选择列表
     * tables 为null 表示没有选择
     */
    override fun choiceTables(tables: MutableList<Tables>?) {
        var tableTmp = ""
        if (tables != null) {
            for (t in tables) {
                tableTmp += t.tableName + ","
            }
            DinnerShoppingCart.getInstance().updateOrCreateTables(tables, true)
            var tableName = tableTmp.substring(0, tableTmp.length - 1)
            beauty_order_customer_room.setText(tableName)
        }
    }
}
