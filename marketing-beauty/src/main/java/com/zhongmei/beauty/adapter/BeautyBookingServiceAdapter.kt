package com.zhongmei.beauty.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo
import kotlinx.android.synthetic.main.beauty_booking_service_item.view.*

/**
 * 预定到店时间日期

 * @date 2018/7/20
 */
class BeautyBookingServiceAdapter(val mContext: Context, val mData: MutableList<DishVo>) : RecyclerView.Adapter<BeautyBookingServiceAdapter.ViewHolder>() {

    private lateinit var mListener: OnServiceListener

    interface OnServiceListener {
        fun onClickItemListener(position: Int)
        fun onClickDeleteListener(position: Int)
    }

    fun setOnServiceListener(listener: OnServiceListener) {
        this.mListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.beauty_booking_service_item, null)
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
            var dishShop = this@BeautyBookingServiceAdapter.mData.get(position).dishShop
            this.tv_service.text = dishShop.name + "      x1"
            this.iv_delete.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mListener?.onClickDeleteListener(position)
                }
            })
            this.rl_service.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mListener?.onClickItemListener(position)
                }
            })
        }
    }
}