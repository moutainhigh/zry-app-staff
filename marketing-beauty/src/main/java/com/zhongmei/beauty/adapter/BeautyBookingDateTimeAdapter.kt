package com.zhongmei.beauty.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.beauty.entity.BookingTimeVo
import kotlinx.android.synthetic.main.beauty_booking_date_time_item.view.*

/**
 * 预定到店时间日期

 * @date 2018/7/20
 */
class BeautyBookingDateTimeAdapter(val mContext: Context, val mData: MutableList<BookingTimeVo>) : RecyclerView.Adapter<BeautyBookingDateTimeAdapter.ViewHolder>() {

    private lateinit var mOnTimeListener: OnTimeItemListener

    interface OnTimeItemListener {
        fun onTimeListener(position: Int, vo: BookingTimeVo)
    }

    fun setOnBookingTimeListener(listener: OnTimeItemListener) {
        this.mOnTimeListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.beauty_booking_date_time_item, null)
        val LayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = LayoutParams
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(position)
    }

    override fun getItemCount(): Int = mData?.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) = with(itemView) {
            var timeVo = this@BeautyBookingDateTimeAdapter.mData.get(position)
            this.tv_time.text = timeVo.time
            this.tv_status.visibility = if (timeVo.isLock) View.VISIBLE else View.GONE
            if (timeVo.isCheck) {
                this.iv_check.visibility = View.VISIBLE
                this.tv_time.setTextColor(mContext.resources.getColor(R.color.beauty_color_FF2283))
                this.ll_root.setBackgroundColor(mContext.resources.getColor(R.color.beauty_color_FFEFF6))
            } else {
                this.iv_check.visibility = View.GONE
                this.tv_time.setTextColor(mContext.resources.getColor(R.color.beauty_color_666666))
                this.ll_root.setBackgroundColor(0)
            }
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mOnTimeListener?.onTimeListener(position, timeVo)
                }
            })
        }
    }
}