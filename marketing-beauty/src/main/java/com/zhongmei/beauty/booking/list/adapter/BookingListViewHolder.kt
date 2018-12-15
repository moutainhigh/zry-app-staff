package com.zhongmei.beauty.booking.list.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.beauty.booking.bean.BeautyBookingVo
import com.zhongmei.beauty.booking.constants.BeautyBookingEnum
import com.zhongmei.beauty.booking.list.manager.BeautyBookListManager
import com.zhongmei.beauty.booking.list.manager.BeautyOpenTradeManager
import com.zhongmei.beauty.dialog.BeautyCreateOrEditBookingDialog
import com.zhongmei.bty.basemodule.booking.bean.BookingTradeItemVo
import com.zhongmei.yunfu.db.entity.booking.Booking
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem
import com.zhongmei.beauty.entity.BookingTradeItemUser
import com.zhongmei.beauty.enums.BeautyListType
import com.zhongmei.beauty.operates.message.BeautyBookingResp
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication
import com.zhongmei.bty.basemodule.session.support.VerifyHelper
import com.zhongmei.bty.basemodule.session.support.VerifyHelper.verifyAlert
import com.zhongmei.yunfu.db.enums.BookingOrderSource
import com.zhongmei.yunfu.db.enums.BookingOrderStatus
import com.zhongmei.yunfu.db.enums.Sex
import com.zhongmei.bty.commonmodule.util.CalmClickAgent
import com.zhongmei.bty.commonmodule.util.DateUtil
import com.zhongmei.yunfu.context.base.BaseApplication
import com.zhongmei.yunfu.context.session.core.auth.Auth
import com.zhongmei.yunfu.context.session.core.user.User
import com.zhongmei.yunfu.context.util.Utils
import com.zhongmei.yunfu.ui.view.recycler.OnRecyclerViewScrollListener
import com.zhongmei.yunfu.util.DensityUtil
import kotlinx.android.synthetic.main.beauty_booking_list_item.view.*


/**

 */
class BookingListViewHolder : RecyclerView.ViewHolder {
    var mItemView: View
    var mAdapter: BookingListAdapter
    var beautyListManager: BeautyBookListManager

    constructor(itemView: View, beautyListManager: BeautyBookListManager, adapter: BookingListAdapter) : super(itemView) {
        this.mItemView = itemView
        this.beautyListManager = beautyListManager
        mAdapter = adapter
    }

    fun update(fragment: Fragment, holder: BookingListViewHolder, bookingVo: BeautyBookingVo) {
        initBtn(holder, bookingVo.booking)
        updateUserInfo(holder, bookingVo.booking)
        updateSource(holder, bookingVo.booking)
        var bookingTradeItems = ArrayList<BookingTradeItem>()
        var bookingTradeItemUsers = ArrayList<BookingTradeItemUser>()
        buildShowData(bookingVo.bookingTradeItemVos, bookingTradeItems, bookingTradeItemUsers)
        updateService(fragment.context, holder, bookingTradeItems)
        updateCosmetologist(holder, bookingTradeItemUsers)
        updateCommonBooking(fragment, holder, bookingVo)

    }

    fun buildShowData(tradeItemVos: List<BookingTradeItemVo>?, bookingTradeItems: ArrayList<BookingTradeItem>, bookingTradeItemUsers: ArrayList<BookingTradeItemUser>) {
        tradeItemVos?.forEach { itemVo ->
            bookingTradeItems.add(itemVo.tradeItem)
            if (Utils.isNotEmpty(itemVo.bookingTradeItemUsers))
                bookingTradeItemUsers.addAll(itemVo.bookingTradeItemUsers)
        }
    }

    fun updateUserInfo(holder: BookingListViewHolder, booking: Booking) {
        var headRes = R.drawable.beauty_customer_default
        if (booking?.commercialGender == Sex.FEMALE) {
            headRes = R.drawable.beauty_customer_female
        } else if (booking?.commercialGender == Sex.MALE) {
            headRes = R.drawable.beauty_customer_male
        }
        holder.itemView.iv_member_header.setBackgroundResource(headRes)
        holder.itemView.tv_name.text = booking.commercialName
        holder.itemView.tv_phone.text = booking.commercialPhone
    }

    /**
     * 初始化按钮
     */
    fun initBtn(holder: BookingListViewHolder, booking: Booking) {
        if (booking.orderStatus == BookingOrderStatus.UNPROCESS) {
            holder.itemView.btn_accept.visibility = View.VISIBLE
            holder.itemView.btn_refuse.visibility = View.VISIBLE
            holder.itemView.btn_open.visibility = View.GONE
            holder.itemView.btn_modify.visibility = View.GONE
        } else {
            holder.itemView.btn_accept.visibility = View.GONE
            holder.itemView.btn_refuse.visibility = View.GONE
            holder.itemView.btn_open.visibility = View.VISIBLE
            holder.itemView.btn_modify.visibility = View.VISIBLE
        }
    }

    /**
     * 来源
     */
    fun updateSource(holder: BookingListViewHolder, booking: Booking) {
        var imgRes = R.drawable.beauty_icon_pos
        if (booking.bookingSource == BookingOrderSource.DaoDian) {
            imgRes = R.drawable.beauty_icon_pos
//            holder.itemView.tv_source.setText(R.string.beauty_source_daodian)
        } else if (booking.bookingSource == BookingOrderSource.WECHAT) {
            imgRes = R.drawable.beauty_icon_wechat
//            holder.itemView.tv_source.setCompoundDrawables(drawableLeft,null,null,null);
//            holder.itemView.tv_source.setText(R.string.beauty_source_wechat)
        } else {
//            holder.itemView.tv_source.setCompoundDrawables(null,null,null,null)
        }

        var drawableLeft = BaseApplication.getInstance().resources.getDrawable(imgRes);
        drawableLeft.setBounds(0, 0, DensityUtil.dip2px(BaseApplication.sInstance, 22f), DensityUtil.dip2px(BaseApplication.sInstance, 22f));
        holder.itemView.tv_source.setCompoundDrawables(drawableLeft, null, null, null);
    }

    /**
     * 更新服务
     */
    fun updateService(context: Context, holder: BookingListViewHolder, tradeItemVos: List<BookingTradeItem>?) {
        holder.itemView.tv_servers.text = getTradeItemInfo(context, tradeItemVos)
    }

    /**
     * 技师信息
     */
    fun updateCosmetologist(holder: BookingListViewHolder, tradeItemUsers: List<BookingTradeItemUser>?) {
        if (Utils.isEmpty(tradeItemUsers)) {
            holder.itemView.tv_cosmetologist.setText(R.string.beauty_booking_cosmetologist_unselected)
            return
        }
        holder.itemView.tv_cosmetologist.text = tradeItemUsers!![0].userName
    }

    /**
     * 其它预定信息
     */
    fun updateCommonBooking(fragment: Fragment, holder: BookingListViewHolder, bookingVo: BeautyBookingVo) {
        var booking = bookingVo.booking
        if (booking.orderStatus == BookingOrderStatus.CANCEL) {
            holder.itemView.lv_cancel.visibility = View.VISIBLE
            holder.itemView.lv_operate.visibility = View.GONE
            holder.itemView.tv_cancel_time.text = DateUtil.format(booking.serverUpdateTime)
        } else if (booking.orderStatus == BookingOrderStatus.UNPROCESS) {
            holder.itemView.lv_cancel.visibility = View.GONE
            holder.itemView.lv_operate.visibility = View.VISIBLE
            holder.itemView.btn_accept.setOnClickListener {
                accetpTrade(fragment, bookingVo)
            }
            holder.itemView.btn_refuse.setOnClickListener {
                refuseTrade(fragment, bookingVo)
            }
        } else {
            holder.itemView.lv_cancel.visibility = View.GONE
            holder.itemView.lv_operate.visibility = View.VISIBLE
            holder.itemView.btn_modify.setOnClickListener {
                toModify(fragment, bookingVo)
            }
            holder.itemView.btn_open.setOnClickListener {
                toOpen(fragment, bookingVo)
            }
        }
        holder.itemView.tv_time.text = fragment.getString(R.string.beauty_arrive, DateUtil.format(booking.orderTime))

        if (bookingVo.interfaceType == BeautyListType.OUTLINE) {
            holder.itemView.tv_status.visibility = View.VISIBLE
        } else {
            holder.itemView.tv_status.visibility = View.GONE
        }
    }

    /**
     *修改预定
     */
    fun toModify(fragment: Fragment, bookingVo: BeautyBookingVo) {
        VerifyHelper.verifyAlert(fragment.activity, BeautyApplication.PERMISSION_BEAUTY_MODIFY_RESERVER, object : VerifyHelper.Callback() {
            override fun onPositive(user: User, code: String, filter: Auth.Filter?) {
                super.onPositive(user, code, filter)
                val dialog = BeautyCreateOrEditBookingDialog()
                dialog.setOnBookingListener(object : BeautyCreateOrEditBookingDialog.OnBookingListener {
                    override fun onCreateListener(resp: BeautyBookingResp) {
                    }

                    override fun onEditListener(booking: BeautyBookingVo, resp: BeautyBookingResp) {
                        doEditSuccess(resp, bookingVo)
                    }

                    override fun onCancelListener(booking: Booking) {
                        mAdapter.update(beautyListManager.removeBooking(bookingVo))
                        mAdapter.notifyDataSetChanged()
                    }
                })
                val bundle = Bundle()
                bundle.putInt(BeautyBookingEnum.LAUNCHMODE_BOOKING_DIALOG, BeautyBookingEnum.BookingDialogLaunchMode.EDIT) // 编辑 CREATE创建
                bundle.putSerializable(BeautyBookingEnum.DATA_BOOKING_DIALOG, bookingVo)
                dialog.setArguments(bundle)
                dialog.show(fragment.getChildFragmentManager(), "showCreateBookingDialog")
            }

        })
    }

    /**
     * 编辑成功后处理
     */
    fun doEditSuccess(resp: BeautyBookingResp, bookingVo: BeautyBookingVo) {
        beautyListManager.updateBooking(resp, bookingVo, mAdapter)
    }

    /**
     * 开单
     */
    fun toOpen(fragment: Fragment, bookingVo: BeautyBookingVo) {
        VerifyHelper.verifyAlert(fragment.activity, BeautyApplication.PERMISSION_BEAUTY_CREATE_TRADE, object : VerifyHelper.Callback() {
            override fun onPositive(user: User, code: String, filter: Auth.Filter?) {
                super.onPositive(user, code, filter)
                var manager = BeautyOpenTradeManager()
                manager.openTrade(fragment.activity, bookingVo, object : BeautyOpenTradeManager.OpenTradeCallBack {
                    override fun onOpenTradeSuccess() {
                        mAdapter.update(beautyListManager.removeBooking(bookingVo))
                        mAdapter.notifyDataSetChanged()
                    }
                })
            }

        })
    }

    /**
     * 接受
     */
    fun accetpTrade(fragment: Fragment, bookingVo: BeautyBookingVo) {
        VerifyHelper.verifyAlert(fragment.activity, BeautyApplication.PERMISSION_BEAUTY_ACCEPT_OR_REFUSE_RESERVER, object : VerifyHelper.Callback() {
            override fun onPositive(user: User, code: String, filter: Auth.Filter?) {
                super.onPositive(user, code, filter)
                var manager = BeautyOpenTradeManager()
                manager.acceptOrRefuseBookingTrade(fragment.activity, bookingVo, BookingOrderStatus.UNARRIVED, object : BeautyOpenTradeManager.OpenTradeCallBack {
                    override fun onOpenTradeSuccess() {
                        mAdapter.update(beautyListManager.removeBooking(bookingVo))
                        mAdapter.notifyDataSetChanged()
                    }
                })
            }

        })

    }

    /**
     * 拒绝
     */
    fun refuseTrade(fragment: Fragment, bookingVo: BeautyBookingVo) {
        VerifyHelper.verifyAlert(fragment.activity, BeautyApplication.PERMISSION_BEAUTY_ACCEPT_OR_REFUSE_RESERVER, object : VerifyHelper.Callback() {
            override fun onPositive(user: User, code: String, filter: Auth.Filter?) {
                super.onPositive(user, code, filter)
                var manager = BeautyOpenTradeManager()
                manager.acceptOrRefuseBookingTrade(fragment.activity, bookingVo, BookingOrderStatus.REFUSED, object : BeautyOpenTradeManager.OpenTradeCallBack {
                    override fun onOpenTradeSuccess() {
                        mAdapter.update(beautyListManager.removeBooking(bookingVo))
                        mAdapter.notifyDataSetChanged()
                    }
                })
            }

        })

    }


    private fun getTradeItemInfo(context: Context, tradeItemVos: List<BookingTradeItem>?): String {
        if (Utils.isEmpty(tradeItemVos)) {
            return context.getResources().getString(R.string.beauty_no_service)
        }

        val itemLen = if (tradeItemVos!!.size > 3) 3 else tradeItemVos.size

        val itemNameBuffer = StringBuilder()
        for (i in 0 until itemLen) {
            val vo = tradeItemVos[i]
            itemNameBuffer.append(vo.dishName)
            itemNameBuffer.append("x" + vo.quantity)
            itemNameBuffer.append(",\n")
        }

        return itemNameBuffer.substring(0, itemNameBuffer.length - 2)
    }


}