package com.zhongmei.beauty.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.*
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.beauty.adapter.BeautyBookingServiceAdapter
import com.zhongmei.beauty.booking.bean.BeautyBookingVo
import com.zhongmei.beauty.booking.constants.BeautyBookingEnum
import com.zhongmei.beauty.booking.order.BeautyServiceCallback
import com.zhongmei.beauty.booking.order.BeautyServiceSelectDialog
import com.zhongmei.beauty.booking.util.BeautyBookingManager
import com.zhongmei.beauty.booking.util.BeautyBookingShopTool
import com.zhongmei.beauty.entity.UserVo
import com.zhongmei.beauty.utils.BeautyDialogUtils
import com.zhongmei.yunfu.db.entity.booking.Booking
import com.zhongmei.bty.basemodule.booking.entity.BookingPeriod
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache
import com.zhongmei.yunfu.db.entity.dish.DishShop
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager
import com.zhongmei.yunfu.context.session.Session
import com.zhongmei.yunfu.net.volley.VolleyError
import com.zhongmei.yunfu.util.DensityUtil
import com.zhongmei.beauty.entity.BookingTradeItemUser
import com.zhongmei.beauty.operates.BeautyBookingOperates
import com.zhongmei.beauty.operates.message.*
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory
import com.zhongmei.yunfu.resp.ResponseListener
import com.zhongmei.yunfu.resp.ResponseObject
import com.zhongmei.yunfu.util.UserActionCode
import com.zhongmei.yunfu.util.MobclickAgentEvent
import com.zhongmei.yunfu.context.util.SystemUtils
import com.zhongmei.yunfu.util.ToastUtil
import com.zhongmei.yunfu.ui.base.BasicDialogFragment
import com.zhongmei.yunfu.ui.view.CommonDialogFragment
import com.zhongmei.yunfu.ShopInfoManager
import com.zhongmei.yunfu.context.base.BaseApplication
import com.zhongmei.yunfu.context.data.ICurrency
import com.zhongmei.yunfu.db.enums.*
import kotlinx.android.synthetic.main.beauty_create_booking_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * 预定的dialog

 * @date 2018/4/9 17:11
 */
class BeautyCreateOrEditBookingDialog : BasicDialogFragment(), View.OnClickListener {

    private var mUserVos: MutableList<UserVo> = ArrayList()

    private var mDishVos: MutableList<DishVo> = ArrayList()

    private lateinit var mParentView: View

    private var mDateTimeDialog: BeautyBookingDateTimeDialog? = null

    private var mLaunchMode = BeautyBookingEnum.BookingDialogLaunchMode.CREATE

    private lateinit var mErpCurrency: ICurrency

    private lateinit var mServiceAdapter: BeautyBookingServiceAdapter

    private var mSex = Sex.MALE

    private lateinit var mBookingOperates: BeautyBookingOperates

    /**
     * 到店时间
     */
    private var mBookingStartTime: Long = 0L

    private var mBookingEndTime: Long = 0L

    private var mOnBookingListener: OnBookingListener? = null

    private lateinit var mBeautyBookingVo: BeautyBookingVo

    /**
     * 编辑时保存老会员id，用于判断是否有修改过服务员
     */
    private var mOldUserId: Long = 0L

    interface OnBookingListener {
        /**
         * 创建
         */
        fun onCreateListener(resp: BeautyBookingResp)

        /**
         * 编辑
         * @param booking 修改前的booking对象
         * @param resp 修改后的booking 数据
         */
        fun onEditListener(booking: BeautyBookingVo, resp: BeautyBookingResp)

        /**
         * 取消
         */
        fun onCancelListener(booking: Booking)
    }

    fun setOnBookingListener(listener: OnBookingListener) {
        this.mOnBookingListener = listener
    }

    override fun show(manager: FragmentManager, tag: String) {
        if (manager != null && !manager.isDestroyed) {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        }
    }

    private fun getIntentData() {
        mLaunchMode = arguments?.getInt(BeautyBookingEnum.LAUNCHMODE_BOOKING_DIALOG, BeautyBookingEnum.BookingDialogLaunchMode.CREATE)!!
        var bookingVo = arguments?.getSerializable(BeautyBookingEnum.DATA_BOOKING_DIALOG)
        if (bookingVo != null)
            mBeautyBookingVo = bookingVo as BeautyBookingVo
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mBookingOperates = OperatesFactory.create(BeautyBookingOperates::class.java)
        getIntentData()
        getLocalErpCurrery()
    }

    private fun getLocalErpCurrery() {
        mErpCurrency = ShopInfoManager.getInstance().currency
    }

    private fun queryErpCurrery(areaCode: String?): ErpCurrency {
        val erpDal = OperatesFactory.create(ErpCommercialRelationDal::class.java)
        return erpDal.queryErpCurrenctByAreaCode(areaCode)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mParentView = View.inflate(activity, R.layout.beauty_create_booking_dialog, null)
        setupWindow()
        return mParentView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun setupWindow() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)// 设置点击屏幕Dialog不消失
        val window = dialog.window
        if (window != null) {
            //设置宽高
            val attributes = window.attributes
            attributes.width = DensityUtil.dip2px(activity, 460f)
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = attributes
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun initView() {
        setupServiceView()
        if (mLaunchMode == BeautyBookingEnum.BookingDialogLaunchMode.CREATE) {
            ll_booking_customer.visibility = View.VISIBLE
            ll_booking_phone.visibility = View.VISIBLE
            btn_cancel.text = "取消"
            btn_submit.text = "确定"
            tv_title.text = "添加预约"
            ll_customer.visibility = View.GONE
        } else {
            ll_booking_customer.visibility = View.GONE
            ll_booking_phone.visibility = View.GONE
            btn_cancel.text = "取消预约"
            btn_submit.text = "保存"
            tv_title.text = "编辑预约"
            ll_customer.visibility = View.VISIBLE
            generateEditDateAndView()
        }
        tv_time.setOnClickListener(this)
        ll_service.setOnClickListener(this)
        tv_waiter.setOnClickListener(this)
        btn_close.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
        iv_male.setOnClickListener(this)
        iv_female.setOnClickListener(this)
    }

    /**
     * 构建编辑数据 和 view
     */
    private fun generateEditDateAndView() {
        if (mBeautyBookingVo != null) {
            if (mBeautyBookingVo.booking.commercialGender == Sex.MALE) iv_sex.setImageResource(R.drawable.beauty_customer_male) else iv_sex.setImageResource(R.drawable.beauty_customer_female)
            tv_customer_name_phone.text = mBeautyBookingVo.booking.commercialName + "    " + mBeautyBookingVo.booking.commercialPhone
            mDishVos.clear()
            mUserVos.clear()
            var dishShops: MutableList<DishShop> = ArrayList()
            mBeautyBookingVo.bookingTradeItemVos?.forEach {
                var dishShop = DishCache.getDishHolder().get(it.tradeItem.dishId)
                if (dishShop == null) {
                    return@forEach
                }

                dishShops.add(dishShop)
                if (mUserVos.size > 0) {
                    return@forEach
                }
                if (it.bookingTradeItemUsers != null && it.bookingTradeItemUsers.size > 0) {
                    var user: BookingTradeItemUser = it.bookingTradeItemUsers.get(0)
                    mOldUserId = user.id
                    mUserVos.add(BeautyBookingManager().bookingTradeItemTransUserVo(user)) // 目前一个单子只能有一个技师，目前暂时不考虑多技师
                    showWaiterView()
                }
            }
            if (dishShops.size > 0) {
                mDishVos.addAll(DishManager().getDishsVo(dishShops))
                rv_service.visibility = View.VISIBLE
                ll_service.visibility = View.GONE
                mServiceAdapter.notifyDataSetChanged()
            }
            tv_time.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(mBeautyBookingVo.booking.startTime)
            tv_time.setTextColor(activity.resources.getColor(R.color.beauty_color_000000))
            et_remark.setText(mBeautyBookingVo.booking.remark)
        }
    }

    /**
     * 服务的view
     */
    private fun setupServiceView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.isSmoothScrollbarEnabled = true
        rv_service.setHasFixedSize(true)
        rv_service.layoutManager = linearLayoutManager
        rv_service.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = activity.resources.getDimension(R.dimen.beauty_booking_create_item_top).toInt()
                }
            }
        })
        mServiceAdapter = BeautyBookingServiceAdapter(activity, mDishVos)
        mServiceAdapter!!.setOnServiceListener(object : BeautyBookingServiceAdapter.OnServiceListener {
            override fun onClickItemListener(position: Int) {
                showServiceDialog()
            }

            override fun onClickDeleteListener(position: Int) {
                mDishVos.removeAt(position)
                if (mDishVos.size == 0) {
                    rv_service.visibility = View.GONE
                    ll_service.visibility = View.VISIBLE
                }
                mServiceAdapter.notifyDataSetChanged()
            }

        })
        rv_service.adapter = mServiceAdapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_time -> showTimeDialog()
            R.id.tv_waiter -> clickWaiterBtn()
            R.id.ll_service -> showServiceDialog()
            R.id.btn_close -> dismissAllowingStateLoss()
            R.id.btn_cancel -> if (mLaunchMode == BeautyBookingEnum.BookingDialogLaunchMode.CREATE) dismissAllowingStateLoss() else showCancelDialog()
            R.id.btn_submit -> clickSubmitBtn()
            R.id.iv_male -> checkSex(Sex.MALE)
            R.id.iv_female -> checkSex(Sex.FEMALE)
        }
    }

    private fun clickSubmitBtn() {
        if (mLaunchMode == BeautyBookingEnum.BookingDialogLaunchMode.CREATE) {
            createBooking()
        } else {
            editBooking()
        }
    }

    private fun clickWaiterBtn() {
        if (mLaunchMode == BeautyBookingEnum.BookingDialogLaunchMode.EDIT) {
            if (bookingIsTimeOut() && mBookingStartTime == 0L) {
                ToastUtil.showShortToast("当前预约已超时请重新选择预约时间")
                return
            }
            if (mBookingStartTime == 0L) { // 没有超时并且没有重新选择时间
                queryCheckUser(mBeautyBookingVo.booking.startTime, mBeautyBookingVo.booking.endTime)
            } else {
                queryCheckUser(mBookingStartTime, mBookingEndTime)
            }
        } else {
            if (mBookingStartTime == 0L || mBookingEndTime == 0L) {
                ToastUtil.showShortToast("请先选择预约时间")
                return
            }
            queryCheckUser(mBookingStartTime, mBookingEndTime)
        }
    }

    /**
     * 设置选中的 sex
     */
    private fun checkSex(sexValue: Sex) {
        if (sexValue == Sex.MALE) {
            iv_male.setImageResource(R.drawable.beauty_booking_man_selected)
            iv_female.setImageResource(R.drawable.beauty_booking_woman_notselected)
            mSex = Sex.MALE
        } else {
            iv_male.setImageResource(R.drawable.beauty_booking_man_notselected)
            iv_female.setImageResource(R.drawable.beauty_booking_woman_selected)
            mSex = Sex.FEMALE
        }
    }

    /**
     * 预约服务
     */
    private fun showServiceDialog() {
        var serviceDialog = BeautyServiceSelectDialog(activity)
        serviceDialog.registerCallback(object : BeautyServiceCallback {
            override fun onCallback(dishVos: List<DishVo>) {
                if (dishVos.size > 0) {
                    mDishVos.addAll(dishVos)
                    mServiceAdapter.notifyDataSetChanged()
                    rv_service.visibility = View.VISIBLE
                    ll_service.visibility = View.GONE
                }
            }
        })
        serviceDialog.show()
    }

    /**
     * 技师选择
     */
    private fun showWaiterDialog(userIds: List<Long>) {
        var dialog = BeautyBookingWaiterDialog()
        dialog!!.setOnBeautyWaiterListener(object : BeautyBookingWaiterDialog.OnBeautyWaiterListener {
            override fun onChoiceUserListener(userVo: List<UserVo>?) {
                MobclickAgentEvent.onEvent(UserActionCode.GK010022)
                if (userVo!!.size > 0) {
                    mUserVos.clear()
                    mUserVos.addAll(userVo)
                    showWaiterView()
                } else {
                    clearWaiterView()
                }
            }
        })
        dialog!!.setSelectUsers(mUserVos) // 编辑时传入锁定状态
        dialog!!.setIsNotFreeUsers(userIds)
        dialog!!.show(childFragmentManager, "BeautyBookingWaiterDialog")
    }

    /**
     * 加载推销员的view
     */
    private fun showWaiterView() {
        if (mUserVos.size > 0) {
            val buffer = StringBuffer()
            for (i in mUserVos.indices) {
                val vo = mUserVos[i]
                if (i == mUserVos.size - 1) {
                    buffer.append(vo.user.name)
                } else {
                    buffer.append(vo.user.name + ",")
                }
            }
            changeWaiterView(true, buffer.toString())
        }
    }

    private fun changeWaiterView(isShow: Boolean, msg: String) {
        tv_waiter.setText(msg)
        if (isShow) {
            tv_waiter.setTextColor(activity.resources.getColor(R.color.beauty_color_000000))
        } else {
            tv_waiter.setTextColor(activity.resources.getColor(R.color.beauty_color_BCBCBC))
        }
    }

    /**
     * 清除推销员
     */
    private fun clearWaiterView() {
        mUserVos.clear()
        changeWaiterView(false, "请选择技师")
    }

    /**
     * 预定时间
     */
    private fun showTimeDialog() {
        if (mDateTimeDialog == null) {
            mDateTimeDialog = BeautyBookingDateTimeDialog()
            mDateTimeDialog!!.setOnBeautyDateTimeListener(object : BeautyBookingDateTimeDialog.OnBeautyDateTimeListener {
                override fun onDateTimeListener(startTime: Long, endTime: Long) {
                    mBookingStartTime = startTime // 记录开始时间
                    mBookingEndTime = endTime // 记录结束时间
                    tv_time.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(startTime)
                    tv_time.setTextColor(activity.resources.getColor(R.color.beauty_color_000000))
                    // 选择时间后需要重新选择服务员
                    clearWaiterView()
                }
            })
        }
        mDateTimeDialog!!.show(childFragmentManager, "BeautyBookingDateTimeDialog")
    }

    /**
     * 查询被占用技师
     */
    private fun queryCheckUser(startTime: Long, endTime: Long) {
        showWaiterDialog(ArrayList());
//        showLoadingProgressDialog()
//        mBookingOperates.bookingCheckUser(startTime, endTime, TradeUserType.TECHNICIAN, object : ResponseListener<BeautyBookingCheckUserResp> {
//            override fun onResponse(response: ResponseObject<BeautyBookingCheckUserResp>?) {
//                var userIds = arrayListOf<Long>()
//                if (response!!.statusCode == ResponseObject.OK) {
//                    userIds.clear()
//                    userIds.addAll(response.content.userIds)
//                }
//                showWaiterDialog(userIds)
//                dismissLoadingProgressDialog()
//            }
//
//            override fun onError(error: VolleyError?) {
//                dismissLoadingProgressDialog()
//            }
//        })
    }


    /*************************** 创建预定 *******************************/
    /**
     * 提交
     *
     * 规则：1.可以不选技师不选服务
     *       2.可以只选服务不选技师
     *       3.选了技师必须选服务
     *       4.可以技师和服务都不选
     */
    private fun createBooking() {
        var phone = et_phone.text.toString()
        var name = et_customer_name.text.toString()
        if (mErpCurrency == null) {
            ToastUtil.showShortToast("没有当前国籍信息，请先同步国籍信息")
            return
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast("手机号不能为空")
            return
        }
        if (mErpCurrency != null && !mErpCurrency.checkPhone(phone)) {
            ToastUtil.showShortToast(getString(R.string.customer_mobile_regulation_error))
            return
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showShortToast("顾客姓名不能为空")
            return
        }
        if (mBookingStartTime == 0L || mBookingEndTime == 0L) {
            ToastUtil.showShortToast("请选择到店时间")
            return
        }
        if (mUserVos.size == 0) {
            showNoWaiterDialog(phone, name)
        } else {
            createBookingOperates(generateBookingReq(phone, name))
        }
    }

    private fun showNoWaiterDialog(phone: String, name: String) {
        BeautyDialogUtils.showDialog(childFragmentManager, CommonDialogFragment.ICON_WARNING, "技师未选，预约将不在预约看\n板展示，是否继续创建预约？", object : View.OnClickListener {
            override fun onClick(v: View?) {
                createBookingOperates(generateBookingReq(phone, name))
            }
        }, object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialog.dismiss()
            }
        }, "showCancelDialog")
    }

    /**
     * 创建预定请求
     */
    private fun createBookingOperates(bookingReq: BeautyBookingReq) {
        showLoadingProgressDialog()
        mBookingOperates.bookingSubmit(bookingReq, object : ResponseListener<BeautyBookingResp> {
            override fun onResponse(response: ResponseObject<BeautyBookingResp>?) {
                if (response!!.statusCode == ResponseObject.OK) {
                    ToastUtil.showShortToast("创建预定成功")
                    mOnBookingListener?.onCreateListener(response.content)
                    dismissLoadingProgressDialog()
                    dismissAllowingStateLoss()
                } else {
                    dismissLoadingProgressDialog()
                    ToastUtil.showShortToast(response.message)
                }
            }

            override fun onError(error: VolleyError?) {
                dismissLoadingProgressDialog()
            }
        })
    }

    /**
     * 构建预定请求
     */
    private fun generateBookingReq(phone: String, name: String): BeautyBookingReq {
        var bookingReq = BeautyBookingReq()
        bookingReq.commercialGender = mSex.value()
        bookingReq.commercialName = name
        bookingReq.commercialPhone = phone
        bookingReq.orderTime = mBookingStartTime
        bookingReq.orderStatus = BookingOrderStatus.UNARRIVED.value()
        bookingReq.orderSource = BookingOrderSource.DaoDian.value()
        bookingReq.startTime = mBookingStartTime;
        bookingReq.endTime = mBookingEndTime;
        bookingReq.remark = et_remark.text.toString()
        bookingReq.customerNum = 1 // 默认1人
        bookingReq.uuid = SystemUtils.genOnlyIdentifier()
        bookingReq.clientCreateTime = System.currentTimeMillis()
        bookingReq.clientUpdateTime = System.currentTimeMillis()
        bookingReq.bookingType = BookingType.BEAUTY.value()
        bookingReq.creatorId = Session.getAuthUser().id
        bookingReq.creatorName = Session.getAuthUser().name
        bookingReq.updatorId = Session.getAuthUser().id
        bookingReq.updatorName = Session.getAuthUser().name
        bookingReq.bookingTradeItems = arrayListOf()
        bookingReq.bookingTradeItemProperties = arrayListOf()
        bookingReq.bookingSource = 1;
        bookingReq.bookingType = 1;
        bookingReq.confirmed = 2;
        bookingReq.deviceIdenty = BaseApplication.getInstance().deviceIdenty;
        mDishVos?.forEach {
            var vo = BeautyBookingShopTool.dishToBookingItem(it, bookingReq.uuid, null)
            bookingReq.bookingTradeItems.add(vo.tradeItem)
            bookingReq.bookingTradeItemProperties.addAll(vo.tradeItemPropertyList)
        }
        bookingReq.bookingTradeItemUsers = arrayListOf()
        mUserVos?.forEach {
            var userVo = it
            bookingReq.bookingTradeItems.forEach {
                bookingReq.bookingTradeItemUsers.add(BeautyBookingShopTool.buildBookingItemUser(userVo, it.uuid, null))
            }
        }
        return bookingReq
    }

    /**
     * 构建时段
     */
    private fun generateBookingPeriod(): BookingPeriod {
        var period = BookingPeriod()
        period.startTime = mBookingStartTime
        period.endTime = mBookingEndTime
        if (mLaunchMode == BeautyBookingEnum.BookingDialogLaunchMode.EDIT && mBeautyBookingVo != null) {
//            period.id = mBeautyBookingVo.bookingPeriods.get(0).id
            period.bookingId = mBeautyBookingVo.booking.id
        }
        return period
    }
    /*************************** 创建预定（完） *******************************/


    /*************************** 取消预定 *******************************/

    private fun showCancelDialog() {
        BeautyDialogUtils.showDialog(childFragmentManager, CommonDialogFragment.ICON_WARNING, "确认取消此预约？", object : View.OnClickListener {
            override fun onClick(v: View?) {
                cancelBooking()
            }
        }, object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialog.dismiss()
            }
        }, "showCancelDialog")
    }


    private fun cancelBooking() {
        var bookingId = mBeautyBookingVo?.booking.id
        var resaon = ""
        var cancelUserID = Session.getAuthUser().id
        showLoadingProgressDialog()
        mBookingOperates.bookingCancel(bookingId, resaon, cancelUserID, object : ResponseListener<BeautyBookingCancelResp> {
            override fun onResponse(response: ResponseObject<BeautyBookingCancelResp>?) {
                if (response!!.statusCode == ResponseObject.OK) {
                    ToastUtil.showShortToast("取消预定成功")
                    mOnBookingListener?.onCancelListener(response!!.content)
                    dismissLoadingProgressDialog()
                    dismissAllowingStateLoss()
                } else {
                    ToastUtil.showShortToast(response.message)
                    dismissLoadingProgressDialog()
                }
            }

            override fun onError(error: VolleyError?) {
                dismissLoadingProgressDialog()
            }
        })
    }
    /*************************** 取消预定（完） *******************************/

    /*************************** 编辑预定预定 *******************************/
    /**
     * 预约是否超时
     *
     * @return false 没有超时，true 超时
     */
    private fun bookingIsTimeOut(): Boolean {
        if (mLaunchMode == BeautyBookingEnum.BookingDialogLaunchMode.EDIT && mBeautyBookingVo != null) {
            var startTime = mBeautyBookingVo.booking.startTime
            if (startTime < System.currentTimeMillis()) {
                return true
            }
        }
        return false
    }

    /**
     * 编辑预定
     */
    private fun editBooking() {
        if (bookingIsTimeOut() && mBookingStartTime == 0L) { // 服务已经超时并且没有重新选择预约时间
            ToastUtil.showShortToast("当前预约已超时请重新选择预约时间")
            return
        }
        if (mUserVos.size > 0 && mDishVos.size == 0) {
            ToastUtil.showShortToast("请选择服务")
            return
        }
//        var erpCurrency = queryErpCurrery(mBeautyBookingVo.booking.nationalTelCode)
//        if (erpCurrency == null) {
//            ToastUtil.showShortToast("没有获取到当前手机号所属国籍")
//            return
//        }
        updateBookingOperates()
    }

    /**
     * 构建更新预定的请求
     */
    private fun generateUpdateBookingReq(): BeautyBookingUpdateReq {
        var req = BeautyBookingUpdateReq()
        req.convertBookingToBookingUpdateReq(mBeautyBookingVo.booking, Session.getAuthUser(), et_remark.text.toString())
        // 遍历菜品b  看下是否有变化过的菜，先匹配作废菜
        req.bookingTradeItemUsers = arrayListOf()
        req.bookingTradeItems = arrayListOf()
        req.bookingTradeItemProperties = arrayListOf()
        if (mBookingStartTime == 0L) { // 没有修改过时间 , 前面已经判断过是否是过期，如果过期 mBookingStartTime 不可能为0必须重新选择
            req.startTime = mBeautyBookingVo.booking.startTime;
            req.endTime = mBeautyBookingVo.booking.endTime;
        } else {
            req.startTime = mBookingStartTime
            req.endTime = mBookingEndTime;
        }

        req.orderTime = req.startTime;
        invalidOldBookingData(req)
        generateUpdateReq(req)
//        when (mDishVos.size) {
//            0 -> { // 没有服务，移除了全部的服务，这里需要判断是否同样移除了服务员 ， 移除了服务员则可以直接保存，没有移除服务员需要提示选择了服务员必须选择服务
//                if (mUserVos.size == 0) {
//                    // 没有服务员 ，这里需要判断以前booking 有没有菜和itemUser ，有需要全部作废，没有直接调用接口
//                    if (mBeautyBookingVo.bookingTradeItemVos.size > 0) {
//                        mBeautyBookingVo.bookingTradeItemVos.forEach {
//                            it.tradeItem.statusFlag = StatusFlag.INVALID // 菜品无效
//                            req.bookingTradeItems.add(it.tradeItem)
//                            for (user in it.bookingTradeItemUsers) { // 推销员无效
//                                user.statusFlag = StatusFlag.INVALID
//                                req.bookingTradeItemUsers.add(user)
//                            }
//                            if (it.tradeItemPropertyList != null && it.tradeItemPropertyList.size > 0) {// 菜品属性无效
//                                for (property in it.tradeItemPropertyList) {
//                                    property.statusFlag = StatusFlag.INVALID
//                                    req.bookingTradeItemProperties.add(property)
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    ToastUtil.showShortToast("请选择服务")
//                }
//            }
//            else -> { // 有菜品部分菜品被移除,需要匹配新菜,作废老菜
//                if (mBeautyBookingVo.bookingTradeItemVos.size > 0) {
//                    // 全部移除掉，全部重新添加
//                    mBeautyBookingVo.bookingTradeItemVos.forEach {
//                        for (user in it.bookingTradeItemUsers) {
//                            user.statusFlag = StatusFlag.INVALID
//                            req.bookingTradeItemUsers.add(user)
//                        }
//                        if (it.tradeItemPropertyList != null && it.tradeItemPropertyList.size > 0) {
//                            for (property in it.tradeItemPropertyList) {
//                                property.statusFlag = StatusFlag.INVALID
//                                req.bookingTradeItemProperties.add(property)
//                            }
//                        }
//                        it.tradeItem.statusFlag = StatusFlag.INVALID
//                        req.bookingTradeItems.add(it.tradeItem)
//                    }
//                    generateUpdateReq(req)
//                } else { // 以前没有菜，就没有user不用添加user作废，这里需要判断是有有新的user
//                    generateUpdateReq(req)
//                }
//            }
//        }
//        when (mOldUserId) {
//            0L -> { // 以前没有选过推销员
//                generateUpdateTradeItemUser(req)
//            }
//            else -> {
//                if (mOldUserId != mUserVos.get(0).user.id) {
//                    mBeautyBookingVo.bookingTradeItemVos.forEach {
//                        for (user in it.bookingTradeItemUsers) {
//                            user.statusFlag = StatusFlag.INVALID
//                            req.bookingTradeItemUsers.add(user)
//                        }
//                    }
//                    generateUpdateTradeItemUser(req)
//                } else { // 没有改过
//                    mBeautyBookingVo.bookingTradeItemVos.forEach {
//                        req.bookingTradeItemUsers.addAll(it.bookingTradeItemUsers)
//                    }
//                }
//            }
//        }
        return req
    }

    /**
     * 作废全部数据
     */
    private fun invalidOldBookingData(req: BeautyBookingUpdateReq) {
//        if (mBeautyBookingVo.bookingTradeItemVos.size > 0) { // 全部移除
        mBeautyBookingVo.bookingTradeItemVos?.forEach {
            it.tradeItem.statusFlag = StatusFlag.INVALID // 菜品无效
            it.tradeItem.bookingId = mBeautyBookingVo.bookingId
            it.tradeItem.validateUpdate()
            it.tradeItem.updatorName
            req.bookingTradeItems.add(it.tradeItem)
            if (it.bookingTradeItemUsers != null && it.bookingTradeItemUsers.size > 0) {
                for (user in it.bookingTradeItemUsers) { // 推销员无效
                    user.statusFlag = StatusFlag.INVALID
                    user.bookingId = mBeautyBookingVo.bookingId
                    req.bookingTradeItemUsers.add(user)
                }
            }
            if (it.tradeItemPropertyList != null && it.tradeItemPropertyList.size > 0) {
                for (property in it.tradeItemPropertyList) {
                    property.statusFlag = StatusFlag.INVALID
                    property.bookingId = mBeautyBookingVo.bookingId
                    req.bookingTradeItemProperties.add(property)
                }
            }
        }
//        }
    }

    /**
     * 构建上传的user数据
     */
    private fun generateUpdateTradeItemUser(req: BeautyBookingUpdateReq) {
        mUserVos?.forEach {
            var userVo = it
            for (tradeItem in req.bookingTradeItems) {
                req.bookingTradeItemUsers.add(BeautyBookingShopTool.buildBookingItemUser(userVo, tradeItem.uuid, mBeautyBookingVo.bookingId))
            }
        }
    }

    /**
     * 做给传数据
     */
    private fun generateUpdateReq(req: BeautyBookingUpdateReq) {
        mDishVos?.forEach {
            var vo = BeautyBookingShopTool.dishToBookingItem(it, req.uuid, mBeautyBookingVo.bookingId)
            req.bookingTradeItems.add(vo.tradeItem)
            req.bookingTradeItemProperties.addAll(vo.tradeItemPropertyList)
        }
        mUserVos?.forEach {
            var userVo = it
            req.bookingTradeItems.forEach {
                if (it.statusFlag == StatusFlag.VALID) {
                    req.bookingTradeItemUsers.add(BeautyBookingShopTool.buildBookingItemUser(userVo, it.uuid, mBeautyBookingVo.bookingId))
                }
            }
        }
    }

    /**
     * 调用更新预定的接口
     */
    private fun updateBookingOperates() {
        showLoadingProgressDialog()
        mBookingOperates.updateBookingSubmit(generateUpdateBookingReq(), object : ResponseListener<BeautyBookingResp> {
            override fun onResponse(response: ResponseObject<BeautyBookingResp>?) {
                if (response!!.statusCode == ResponseObject.OK) {
                    ToastUtil.showShortToast("编辑预定成功")
                    mOnBookingListener?.onEditListener(mBeautyBookingVo, response.content)
                    dismissLoadingProgressDialog()
                    dismissAllowingStateLoss()
                } else {
                    dismissLoadingProgressDialog()
                    ToastUtil.showShortToast(response.message)
                }
            }

            override fun onError(error: VolleyError?) {
                dismissLoadingProgressDialog()
            }
        })
    }
    /*************************** 编辑预定预定（完） *******************************/

}
