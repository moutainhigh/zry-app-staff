package com.zhongmei.beauty.customer

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.zhongmei.yunfu.R
import com.zhongmei.bty.basemodule.commonbusiness.view.ScanPopupWindow
import com.zhongmei.bty.basemodule.customer.bean.CustomerMobileVo
import com.zhongmei.yunfu.bean.req.CustomerResp
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager
import com.zhongmei.yunfu.bean.req.CustomerLoginResp
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardLoginResp
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType
import com.zhongmei.bty.basemodule.devices.mispos.event.EventReadKeyboard
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager
import com.zhongmei.yunfu.net.volley.VolleyError
import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants
import com.zhongmei.yunfu.ui.base.BasicFragment
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory
import com.zhongmei.yunfu.db.enums.CustomerType
import com.zhongmei.bty.commonmodule.http.*
import com.zhongmei.yunfu.util.ToastUtil
import com.zhongmei.bty.commonmodule.util.manager.ClickManager
import com.zhongmei.bty.dinner.cash.DinnerPriviligeItemsFragment
import com.zhongmei.yunfu.bean.YFResponse
import com.zhongmei.yunfu.data.LoadingYFResponseListener
import com.zhongmei.yunfu.resp.*
import de.greenrobot.event.EventBus
import java.math.BigDecimal

/**
 * 会员搜索基础类
 * Created by demo on 2018/12/15
 */
abstract class BasicCustomerSearchFragment : BasicFragment() {

    /**
     * 输入框
     *
     * @return
     */
    abstract val getEditTextSearch: EditText?

    /**
     * 搜索按钮
     *
     * @return
     */
    abstract val getBtnSearch: View?

    /**
     * 刷卡按钮
     *
     * @return
     */
    abstract val getBtnPosCard: View?

    /**
     * 输入框清除按钮
     *
     * @return
     */
    abstract val getBtnEditClear: View?

    /**
     * 扫码按钮
     *
     * @return
     */
    abstract val getBtnSanCode: View?

    @BeautyCustomerConstants.CustomerSearchType
    private var mSearchType = BeautyCustomerConstants.CustomerSearchType.SEARCHTYPE_NOMAL

    /**
     * 手机号或者姓名
     */
    private var mSearchCondition: String? = null

    lateinit var mCustomerOperates: CustomerOperates

    private lateinit var mScanCodePopupWindow: ScanPopupWindow

    private val mOnBtnClickListener = View.OnClickListener { v ->
        if (ClickManager.getInstance().isClicked) {
            return@OnClickListener
        }
        when (v.id) {
            getBtnSearch?.id -> clickSearch() // 搜索
            getBtnEditClear?.id -> clearSearchEdit() // 清除
            getBtnPosCard?.id -> scanCard() // 刷卡
            getBtnSanCode?.id -> clickScanCode() // 扫码
        }
    }

    /**
     * 查询数据成功回调数据
     *
     * @param customerNew
     */
    abstract fun loginSuccess(customerNew: CustomerResp)


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCustomerOperates = OperatesFactory.create(CustomerOperates::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        getBtnPosCard?.setOnClickListener(mOnBtnClickListener)
        getBtnSanCode?.setOnClickListener(mOnBtnClickListener)
        getBtnEditClear?.setOnClickListener(mOnBtnClickListener)
        getBtnSearch?.setOnClickListener(mOnBtnClickListener)
        setupSearchEditText()
    }

    /**
     * 清除搜索焦点
     */
    private fun clearEditTextFocus() {
        if (getEditTextSearch != null && getEditTextSearch!!.hasFocus()) {
            getEditTextSearch?.isFocusable = false
            getEditTextSearch?.isFocusableInTouchMode = true
        }
    }

    /**
     * 清楚搜索输入框
     */
    private fun clearSearchEdit() {
        clearEditTextFocus()
        getEditTextSearch?.setText("")
    }

    /**
     * 隐藏软键盘
     *
     * @return
     */
    private fun deelInputKeyboard(): Boolean {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(activity.window.decorView.windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }

    /**
     * 刷卡
     */
    private fun scanCard() {
        getEditTextSearch?.setText("")
        clearEditTextFocus()
        mSearchType = BeautyCustomerConstants.CustomerSearchType.SEARCHTYPE_POSTCARD
        postCard()
    }

    /**
     * 组装搜索Edittext
     */
    private fun setupSearchEditText() {
        getEditTextSearch?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER)) {
                val searchCondition = getEditTextSearch?.text.toString()
                if (!TextUtils.isEmpty(searchCondition)) {
                    mSearchCondition = searchCondition
                    mSearchType = BeautyCustomerConstants.CustomerSearchType.SEARCHTYPE_NOMAL
                    deelInputKeyboard()
                    clearEditTextFocus()
                    queryCustomerInfo()
                }
                return@OnKeyListener true
            }
            false
        })
        getEditTextSearch?.addTextChangedListener(object : TextWatcher {
            private var oldKeyWord: String? = null // 改变之前的文字
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO 改变中
                var keyWord = s.toString()
                if (!TextUtils.isEmpty(keyWord)) {
                    if (keyWord.length == 1 && keyWord.contains(" ")) {
                        keyWord = keyWord.replace(" ".toRegex(), "")
                        ToastUtil.showShortToast(getString(R.string.customer_input_empty))
                        getEditTextSearch?.setText(keyWord)
                        getEditTextSearch?.setSelection(start)
                    }
                    if (!TextUtils.isEmpty(oldKeyWord) && oldKeyWord == keyWord) {
                        return
                    }
                    getBtnEditClear?.visibility = View.VISIBLE
                } else {
                    getBtnEditClear?.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                val keyWord = s.toString()
                if (keyWord.contains(" ")) {
                    oldKeyWord = keyWord.replace(" ".toRegex(), "")
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (mSearchType == BeautyCustomerConstants.CustomerSearchType.SEARCHTYPE_SCANCODE) {
                    if (mScanCodePopupWindow != null && mScanCodePopupWindow.isShowing) {
                        mScanCodePopupWindow.dismiss()
                    }
                    return
                }
                val value = getEditTextSearch?.text.toString()
                if (TextUtils.isEmpty(value)) {
                    mSearchCondition = ""
                    if (getBtnSearch is ImageView) {
                        (getBtnSearch as ImageView).setImageResource(R.drawable.beauty_ic_search_off)
                    } else {
                        getBtnSearch?.setBackgroundResource(R.drawable.bg_customer_search_off)
                    }
                } else {
                    if (getBtnSearch is ImageView) {
                        (getBtnSearch as ImageView).setImageResource(R.drawable.beauty_ic_search_on)
                    } else {
                        getBtnSearch?.setBackgroundResource(R.drawable.bg_customer_search_on)
                    }
                }
            }
        })
        getEditTextSearch?.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (activity.isDestroyed) {
                return@OnFocusChangeListener;
            }
            if (hasFocus) {
                if (!TextUtils.isEmpty(getEditTextSearch?.text.toString())) {
                    getBtnEditClear?.visibility = View.VISIBLE
                } else {
                    getBtnEditClear?.visibility = View.GONE
                }
            } else {
                getBtnEditClear?.visibility = View.GONE
            }
        }
    }


    /**
     * 点击搜索
     */
    private fun clickSearch() {
        clearEditTextFocus()
        val condiction = getEditTextSearch?.text.toString()
        if (!TextUtils.isEmpty(condiction)) {
            mSearchCondition = condiction
            queryCustomerInfo()
        }
    }

    /**
     * 扫码
     */
    private fun clickScanCode() {
        DisplayServiceManager.doCancel(activity)
        getEditTextSearch?.setText("")
        clearEditTextFocus()
        scanCode()
    }

    /**
     * 扫码弹框
     */
    private fun scanCode() {
        mScanCodePopupWindow = ScanPopupWindow(activity, getString(R.string.sacn_customer_number_desc))
        mScanCodePopupWindow.showAtLocation(getBtnSanCode, Gravity.NO_GRAVITY, 0, 0)
        mScanCodePopupWindow.setOnDismissListener { mSearchType = BeautyCustomerConstants.CustomerSearchType.SEARCHTYPE_NOMAL }
        mScanCodePopupWindow.setOnScanBarcodeCallback { barcode -> login(barcode, CustomerLoginType.WECHAT_MEMBERCARD_ID) }
    }

    /**
     * 登录
     *
     * @param cardNum 卡号
     * @param loginType
     * @param isRefresh 是否是刷新
     * @param isAuthLogin 是否是自动登录
     */
    private fun login(cardNum: String, loginType: CustomerLoginType, isRefresh: Boolean, isAuthLogin: Boolean) {
        val listener = object : YFResponseListener<YFResponse<CustomerLoginResp>> {

            override fun onResponse(response: YFResponse<CustomerLoginResp>) {
                if (YFResponse.isOk(response)) {
                    val resp = response.content
                    if (resp != null) {
                        when (loginType) {
                            CustomerLoginType.WECHAT_MEMBERCARD_ID -> loginWeChatCallBack(resp)
                            CustomerLoginType.MOBILE -> loginMoblieCallBack(resp, isRefresh, isAuthLogin)
                        }
                    } else {
                        val msg: String
                        if (response.status == 1126) {
                            msg = getString(R.string.order_dish_member_disabled)
                        } else {
                            msg = response.message
                        }
                        ToastUtil.showShortToast(if (loginType == CustomerLoginType.WECHAT_MEMBERCARD_ID) getString(R.string.customer_get_openid_error) else msg)
                    }

                }
            }

            override fun onError(error: VolleyError) {
                ToastUtil.showShortToast(error.message)
            }
        }

        CustomerManager.customerLogin(loginType, cardNum, null, false, true, LoadingYFResponseListener.ensure(listener, getChildFragmentManager()))

    }

    /**
     * 登录
     *
     * @param cardNum
     */
    private fun login(cardNum: String, loginType: CustomerLoginType) {
        login(cardNum, loginType, false, false)
    }

    /**
     * 微信登录
     */
    private fun loginWeChatCallBack(resp: CustomerLoginResp) {
        if (!TextUtils.isEmpty(resp.openId)) {
            mSearchCondition = resp.openId
            mSearchType = BeautyCustomerConstants.CustomerSearchType.SEARCHTYPE_SCANCODE
            queryCustomerInfo()
        } else {
            ToastUtil.showShortToast(getString(R.string.customer_get_openid_empty))
        }
    }

    /**
     * 手机号登录
     *
     * @param MemberLoginResp 会员登录回调
     * @param isRefresh 是否是刷新
     * @param isAuthLogin 是否是自动登录
     */
    private fun loginMoblieCallBack(resp: CustomerLoginResp, isRefresh: Boolean, isAuthLogin: Boolean) {
        if (resp.customerIsDisable()) {
            ToastUtil.showShortToast(R.string.order_dish_member_disabled)
        } else {
            val customerNew = resp.getCustomer()
            customerNew.setInitialValue()
            customerNew.queryLevelRightInfos()
            customerNew.needRefresh = false
//            customerNew.customerLoginType = resp.getCustom
            val tradeCustomer = CustomerManager.getInstance().getTradeCustomer(customerNew)
            if (!customerNew.isMember()) {
                tradeCustomer!!.setCustomerType(CustomerType.CUSTOMER)
            } else if (customerNew.card == null) {
                tradeCustomer!!.setCustomerType(CustomerType.MEMBER)
            } else {
                tradeCustomer!!.setCustomerType(CustomerType.CARD)
                tradeCustomer!!.setEntitycardNum(customerNew.card.getCardNum())
            }
            if (!isRefresh) ToastUtil.showShortToast(R.string.customer_login)
            // 登录成功后第二屏显示用户信息
            /*  val dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, customerNew, customerNew.integral, false, 0.0)
              DisplayServiceManager.updateDisplay(activity, dUserInfo)*/
            EventBus.getDefault().post(EventReadKeyboard(true, ""))// 发送成功到ReadKeyboardDialogFragment
            DinnerPriviligeItemsFragment.showDisplayUserInfo(activity)
            if (isAuthLogin) DinnerCashManager().jumpAfterLoginByAuth("", customerNew, null) else DinnerCashManager().jumpAfterLogin("", customerNew, null)
            DinnerShopManager.getInstance().shoppingCart.setOpenIdenty(resp.getOpenId())
            clearSearchEdit() // 查询成功清除数据
        }
    }

    /**
     * 自动登录
     */
    protected fun authLogin(phone: String) {
        login(phone, CustomerLoginType.MOBILE, false, true)
//        login(phone, CustomerLoginType.MOBILE)
    }

    /**
     * 刷新用户信息
     */
    protected fun refreshCustomerInfoByCustomer(phone: String) {
        login(phone, CustomerLoginType.MOBILE, true, false)
    }

    /**
     * 刷新用户信息 - 卡登录
     */
    protected fun refreshCustomerInfoByCard(cardNum: String) {
        loginByCardNo(cardNum, true)
    }

    /**
     * 自动登录
     */
    protected fun authLoginByCardNum(cardNum: String) {
        loginByCardNo(cardNum)
    }

    /**
     * 查询顾客列表
     */
    private fun queryCustomerInfo() {
        val listener = object : ResponseListener<CustomerMobileVo> {
            override fun onResponse(response: ResponseObject<CustomerMobileVo>) {
                if (response.content.result.customerId != null) {
                    clearSearchEdit() // 查询成功清除数据
                    loginSuccess(response.content.result.customer)
                }
            }

            override fun onError(error: VolleyError) {
                ToastUtil.showLongToast(error.message)
            }
        }
        mCustomerOperates.findCustomerByPhone(mSearchCondition, LoadingResponseListener.ensure(listener, getChildFragmentManager()))
    }

    /**
     * 刷卡登录
     */
    private fun postCard() {
        /*val readCardDialogFragment = ReadCardDialogFragment.UionCardDialogFragmentBuilder()
                .buildReadCardId(ReadCardDialogFragment.UionCardStaus.READ_CARD_ID_SINGLE,
                        object : ReadCardDialogFragment.CardOvereCallback {

                            override fun onSuccess(status: ReadCardDialogFragment.UionCardStaus, number: String) {
                                getEditTextSearch?.setText("")
                                mSearchCondition = number
                                queryCustomerInfo()
                            }

                            override fun onFail(status: ReadCardDialogFragment.UionCardStaus, rejCodeExplain: String) {
                                if (!TextUtils.isEmpty(rejCodeExplain)) {
                                    ToastUtil.showShortToast(rejCodeExplain)
                                }
                                mSearchType = BeautyCustomerConstants.CustomerSearchType.SEARCHTYPE_NOMAL
                            }
                        })
        readCardDialogFragment.show(fragmentManager, "get_cardno")
        readCardDialogFragment.setCloseListener {
            //关闭刷卡dialog
            mSearchType = BeautyCustomerConstants.CustomerSearchType.SEARCHTYPE_NOMAL
        }*/
    }

    /**
     * 判断是否是刷新
     */
    private fun loginByCardNo(inputNo: String, isRefresh: Boolean) {
        val operates = OperatesFactory.create(CustomerOperates::class.java)
        val listener = object : EventResponseListener<CardLoginResp>(UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD) {

            override fun onResponse(response: ResponseObject<CardLoginResp>) {
                if (ResponseObject.isOk(response)) {
                    val resp = response.content
                    // 设置card的名称，从customer中获得
                    val card = resp.getResult().cardInstance
                    if (card.cardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                        ToastUtil.showShortToast(R.string.customer_login_not_member)
                        /*val dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_VALIDATE_USER_FAIL, "", null, 0, true, 0.0) // 副屏显示
                        DisplayServiceManager.updateDisplay(activity, dUserInfo)*/
                    } else {
                        if (card.cardStatus != CardStatus.ACTIVATED) ToastUtil.showShortToast(R.string.card_disable) else setLoginByCardNo(resp, card, isRefresh)
                    }
                } else {
                    /*val dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_ACCOUNT_INPUT, response.message, null, 0, true, 0.0)
                    DisplayServiceManager.updateDisplay(activity, dUserInfo)*/
                }
                UserActionEvent.end(getEventName())
            }

            override fun onError(error: VolleyError) {
                ToastUtil.showShortToast(error.message)
            }
        }
        operates.cardLogin(inputNo, LoadingResponseListener.ensure(listener, childFragmentManager))
    }

    /**
     * 使用会员卡号登录
     *

     * @Title: loginByCardNo
     * @Return void 返回类型
     */
    private fun loginByCardNo(inputNo: String) {
        loginByCardNo(inputNo, false)
    }

    /**
     * 卡登录下行处理
     *
     * @param resp
     * @param card
     * @param isRefresh
     */
    private fun setLoginByCardNo(resp: CardLoginResp, card: EcCard, isRefresh: Boolean) {
        //add v8.2 start 获取当前登录的卡是否开启会员价限制开关
        val cardKind = resp.getResult().cardKind
        if (cardKind != null && cardKind.priceLimit != null) {
            CustomerManager.getInstance().setCurrentCardIsPriceLimit(if (cardKind.priceLimit == 2) true else false)
        }
        //add v8.2 end  获取当前登录的卡是否开启会员价限制开关
        val customerV5 = resp.getResult().customer
        card.name = customerV5.name
        card.cardLevel = resp.getResult().cardLevel
        card.cardLevelSetting = resp.getResult().cardLevelSetting
        card.cardSettingDetails = resp.getResult().cardSettingDetails
        card.integralAccount = resp.getResult().integralAccount
        card.valueCardAccount = resp.getResult().valueCardAccount
        card.cardKind = cardKind // 8.3添加  结账界面 储值屏蔽售卡即激活的卡片 的 充值
        card.customer = customerV5
        val customerNew = resp.customer
        customerNew.queryLevelRightInfos()
        val tradeCustomer = CustomerManager.getInstance().getTradeCustomer(card.customer)
        tradeCustomer!!.customerType = CustomerType.CARD
        tradeCustomer.entitycardNum = card.cardNum
        if (!isRefresh) {
            ToastUtil.showShortToast(R.string.customer_login)
        }
        // 如果有积分抵现
        var integral = BigDecimal.ZERO
        if (card.integralAccount != null && card.integralAccount.integral != null) {
            integral = BigDecimal.valueOf(card.integralAccount.integral!!)
        }
        /*val dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, customerNew, integral.toLong(), false, 0.0)// 登录成功后第二屏显示用户信息
        DisplayServiceManager.updateDisplay(activity, dUserInfo)*/
        DinnerCashManager().jumpAfterLogin("", customerNew, null)
    }
}
