package com.zhongmei.beauty.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.*
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.beauty.adapter.BeautyBookingDateAdapter
import com.zhongmei.beauty.adapter.BeautyBookingDateTimeAdapter
import com.zhongmei.beauty.entity.BookingDateVo
import com.zhongmei.beauty.entity.BookingTimeVo
import com.zhongmei.yunfu.ShopInfoManager
import com.zhongmei.yunfu.context.util.DateTimeUtils
import com.zhongmei.yunfu.util.DensityUtil
import com.zhongmei.yunfu.util.ToastUtil
import com.zhongmei.yunfu.ui.base.BasicDialogFragment
import kotlinx.android.synthetic.main.beauty_booking_datetime_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * 预定-到店时间选择

 * @date 2018/4/9 17:11
 */
class BeautyBookingDateTimeDialog : BasicDialogFragment(), View.OnClickListener {

    private val TAG: String = "BeautyBookingDateTimeDialog"

    private lateinit var mParentView: View

    private lateinit var mListener: OnBeautyDateTimeListener

    private var mDates: MutableList<BookingDateVo> = ArrayList()

    private var mTimes: MutableList<BookingTimeVo> = ArrayList()

    private var mDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    private var mCalendar: Calendar = Calendar.getInstance()

    private lateinit var mDateAdapter: BeautyBookingDateAdapter

    private lateinit var mTimeAdapter: BeautyBookingDateTimeAdapter

    private var mCurrentDatePosition: Int = 0

    private var mCurrentTimePosition: Int = 0

    /**
     * 开始营业的时间（服务器营业时间）
     */
    private var mStartTime = "9:00"

    /**
     * 结束营业的时间（服务器营业结束时间）
     */
    private var mEndTime = "18:00"

    /**
     * 时间间隔（服务器配置的时间间隔）
     */
    private var mTimeSpace = 30 * 60 * 1000

    /**
     * 上传时减去1s （时间差用于 上传时整数时间-1s）
     */
    private val mTimeOneSecond = 1 * 1000

    /**
     * 日期跨度（30天）
     */
    private val mDaySpace = 30

    /**
     * 上传日期
     */
    private lateinit var mUpdateDate: String

    /**
     * 上传时间
     */
    private var mUpdateTime: String = ""

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    interface OnBeautyDateTimeListener {
        /**
         * 时间点击
         *
         * @param showTime 展示的时间
         * @param uploadStartTime 需要上传的开始时间
         * @param uploadEndTime 需要上传的结束时间
         */
        fun onDateTimeListener(startTime: Long, endTime: Long)

    }

    override fun show(manager: FragmentManager, tag: String) {
        if (manager != null && !manager.isDestroyed) {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        }
    }

    fun setOnBeautyDateTimeListener(listener: OnBeautyDateTimeListener) {
        this.mListener = listener
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mParentView = View.inflate(activity, R.layout.beauty_booking_datetime_dialog, null)
        setupWindow()
        return mParentView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        if (mDates != null && mDates.size == 0) {
            generateDate()
        }
        refreshDateView()
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
        btn_close.setOnClickListener(this)
        btn_ok.setOnClickListener(this)
        setupDateView()
        setupTimeView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_close -> dismissAllowingStateLoss()
            R.id.btn_ok -> submit()
        }
    }

    /**
     * 日期
     */
    private fun setupDateView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.isSmoothScrollbarEnabled = true
        rv_date.setHasFixedSize(true)
        rv_date.layoutManager = linearLayoutManager
        mDateAdapter = BeautyBookingDateAdapter(activity, mDates)
        mDateAdapter.setOnBookingDateListener(object : BeautyBookingDateAdapter.OnDateItemListener {
            override fun onDateListener(position: Int, vo: BookingDateVo) {
                mDates.get(mCurrentDatePosition).timeVos?.forEach {
                    it.isCheck = false // 取消时间选中
                }
                mUpdateTime = "" // 换了日期清除当前保存时间
                mDates.get(mCurrentDatePosition).isCheck = false // 取消上次选中的日期
                mDates.get(position).isCheck = true // 日期选中
                mCurrentDatePosition = position // 更新当前选中
                refreshDateView()
                refreshTimeView(mDates.get(position).timeVos) // 更新时间view
            }
        })
        rv_date.adapter = mDateAdapter
    }


    /**
     * 时间
     */
    private fun setupTimeView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.isSmoothScrollbarEnabled = true
        rv_time.setHasFixedSize(true)
        rv_time.layoutManager = linearLayoutManager
        mTimeAdapter = BeautyBookingDateTimeAdapter(activity, mTimes)
        mTimeAdapter.setOnBookingTimeListener(object : BeautyBookingDateTimeAdapter.OnTimeItemListener {
            override fun onTimeListener(position: Int, vo: BookingTimeVo) {
                mDates.get(mCurrentDatePosition).timeVos?.forEach {
                    if (it.time.equals(vo.time)) {
                        if (vo.isCheck) {
                            mUpdateTime = ""
                            it.isCheck = false
                        } else {
                            mUpdateTime = vo.time + ":00" // 拼接时间
                            it.isCheck = true
                        }
                    } else {
                        it.isCheck = false
                    }
                }
                mCurrentTimePosition = position
                refreshTimeView(mDates.get(mCurrentDatePosition).timeVos)
            }
        })
        rv_time.adapter = mTimeAdapter
    }

    /**
     * 构建日期，当前时间后30天
     */
    private fun generateDate() {
        mStartTime = ShopInfoManager.getInstance().getShopInfo().startTime
        mEndTime = ShopInfoManager.getInstance().getShopInfo().endTime
        var times = generateMealTime(mStartTime, mEndTime) // 构建时间
        var currentDateTime = generateCurrentDateMealTime(mStartTime, mEndTime) // 当前时间段
        mDates.clear()
        mCalendar.set(Calendar.DATE, mCalendar.get(Calendar.DATE))
        for (i in 1..mDaySpace) {
            var vo = BookingDateVo()
            vo.date = mDateFormat.format(mCalendar.time)
            vo.timeVos = ArrayList()
            vo.timeVos.clear()
            if (DateTimeUtils.isSameDay(Date(), mCalendar.time)) { // 判断是否是今天默认选中今天
                vo.isCheck = true
                mCurrentDatePosition = i - 1 // 记录当前选中位置
                vo.timeVos.addAll(currentDateTime!!)
            } else {
                vo.timeVos.addAll(times!!)
            }
            mDates.add(vo)
            mCalendar.add(Calendar.DATE, 1) // 往后+1天
        }
    }

    /**
     * 构造当前Date的时间段
     *
     * 例如 2018--07-24 14:16 之前14：30之前的时间全部需要移除掉
     */
    private fun generateCurrentDateMealTime(startTime: String, endTime: String): List<BookingTimeVo>? {
        var currentDateTimestamp = System.currentTimeMillis()
        var timelist: MutableList<BookingTimeVo> = ArrayList()
        val startDate = DateTimeUtils.resetDate(Date(), startTime)
        val endDate = DateTimeUtils.resetDate(Date(), endTime)
        var tempTime: Long? = startDate.time
        val endTimes = endDate.time
        do {
            val date = Date(tempTime!!)
            var timeVo = BookingTimeVo()
            if (tempTime >= currentDateTimestamp) {
                timeVo.timestamp = date.time
                timeVo.time = timeFormat.format(date)
                timelist.add(timeVo)
            }
            tempTime += mTimeSpace.toLong()
        } while (tempTime!! <= endTimes - mTimeSpace)
        return timelist
    }

    /**
     * 构造时间段
     */
    private fun generateMealTime(startTime: String, endTime: String): List<BookingTimeVo>? {
        var timelist: MutableList<BookingTimeVo> = ArrayList()
        val startDate = DateTimeUtils.resetDate(Date(), startTime)
        val endDate = DateTimeUtils.resetDate(Date(), endTime)
        var tempTime: Long? = startDate.time
        val endTimes = endDate.time
        do {
            val date = Date(tempTime!!)
            var timeVo = BookingTimeVo()
            timeVo.timestamp = date.time
            timeVo.time = timeFormat.format(date)
            timelist.add(timeVo)
            tempTime += mTimeSpace.toLong()
        } while (tempTime!! <= endTimes - mTimeSpace)
        return timelist
    }

    /**
     * 刷新日期
     */
    private fun refreshDateView() {
        mUpdateDate = mDates.get(mCurrentDatePosition).date
        mDateAdapter.notifyDataSetChanged()
        rv_date.scrollToPosition(mCurrentDatePosition) // 滚动到指定位置
        if (mDates != null && mDates.size > 0) {
            refreshTimeView(mDates.get(mCurrentDatePosition).timeVos)
        }
    }

    /**
     * 刷新时间
     */
    private fun refreshTimeView(times: MutableList<BookingTimeVo>) {
        mTimes.clear()
        mTimes.addAll(times)
        mTimeAdapter.notifyDataSetChanged()
        rv_time.scrollToPosition(mCurrentTimePosition)
    }

    private fun submit() {
        if (!TextUtils.isEmpty(mUpdateTime)) {
            val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val newTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val strStartTime = mUpdateDate + " " + mUpdateTime // yyyy-MM-dd HH:mm:ss
            val longStartTime = dateTimeFormat.parse(strStartTime).time
            var dateEndTime = timeFormat.parse(mUpdateTime).time + (mTimeSpace - mTimeOneSecond) // HH:mm:ss 转换时间 + 时间间隔 - 1s
            val strEndTime = mUpdateDate + " " + newTimeFormat.format(dateEndTime)
            val longEndTime = dateTimeFormat.parse(strEndTime).time
//            ToastUtil.showShortToast(strStartTime + "   " + strEndTime)
            Log.i(TAG!!, "开始时间:[ " + strStartTime + "] --->  结束时间:[ " + strEndTime + " ]")
            mListener?.onDateTimeListener(longStartTime, longEndTime)
            dismissAllowingStateLoss()
        } else {
            ToastUtil.showShortToast("您还没有选取需要预定的时间")
        }
    }

}
