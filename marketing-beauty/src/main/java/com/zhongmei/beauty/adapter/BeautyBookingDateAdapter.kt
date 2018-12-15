package com.zhongmei.beauty.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.beauty.entity.BookingDateVo
import kotlinx.android.synthetic.main.beauty_booking_date_item.view.*

/**
 * 预定到店时间日期

 * @date 2018/7/20
 */
class BeautyBookingDateAdapter(val mContext: Context, val mData: MutableList<BookingDateVo>) : RecyclerView.Adapter<BeautyBookingDateAdapter.ViewHolder>() {

    private lateinit var mOnDateListener: OnDateItemListener

    interface OnDateItemListener {
        fun onDateListener(position: Int, vo: BookingDateVo)
    }

    fun setOnBookingDateListener(listener: OnDateItemListener) {
        this.mOnDateListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.beauty_booking_date_item, null)
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
            var dateVo = this@BeautyBookingDateAdapter.mData.get(position)
            this.tv_date.text = dateVo.date
            if (dateVo.isCheck) {
                this.tv_date.setTextColor(mContext.resources.getColor(R.color.beauty_color_FF2283))
                this.tv_date.setBackgroundColor(mContext.resources.getColor(R.color.beauty_color_FFEFF6))
            } else {
                this.tv_date.setTextColor(mContext.resources.getColor(R.color.beauty_color_666666))
                this.tv_date.setBackgroundColor(0)
            }
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mOnDateListener?.onDateListener(position, dateVo)
                }
            })
        }
    }
}