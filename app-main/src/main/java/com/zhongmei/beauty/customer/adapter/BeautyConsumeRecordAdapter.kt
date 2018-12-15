package com.zhongmei.beauty.customer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zhongmei.yunfu.R
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceHistory
import com.zhongmei.yunfu.context.util.DateTimeUtils
import kotlinx.android.synthetic.main.beauty_consume_record_item.view.*

/**
 * 消费记录
 *

 * @date 2017/3/13 15:09
 */
class BeautyConsumeRecordAdapter(val mContext: Context, val mData: MutableList<BeautyCardServiceHistory>) : RecyclerView.Adapter<BeautyConsumeRecordAdapter.ViewHolder>() {

    /**
     * 创建view
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.beauty_consume_record_item, null)
        val LayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = LayoutParams
        return ViewHolder(view)
    }

    /**
     * 赋值数据
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(position)
    }

    override fun getItemCount(): Int = mData?.size

    /**

     * @description (ViewHolder 绑定Item点击事件 和 item 长按事件)
     * @time 2015年6月1日
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) = with(itemView) {
            var history = this@BeautyConsumeRecordAdapter.mData.get(position)
            when (this@BeautyConsumeRecordAdapter.mData.size) {
                1 -> {
                    this.iv_consume_record_bottom_line.visibility = View.INVISIBLE
                    this.iv_consume_record_top_line.visibility = View.INVISIBLE
                    this.iv_consume_record_middle_point.setImageResource(R.drawable.beauty_consume_record_stepview_pink_point)
                }
                else -> when (position) {
                    this@BeautyConsumeRecordAdapter.mData.size - 1 -> { // footer
                        this.iv_consume_record_bottom_line.visibility = View.INVISIBLE
                        this.iv_consume_record_top_line.visibility = View.VISIBLE
                        this.iv_consume_record_middle_point.setImageResource(R.drawable.beauty_consume_record_stepview_gray_point)
                    }
                    0 -> { // header
                        this.iv_consume_record_bottom_line.visibility = View.VISIBLE
                        this.iv_consume_record_top_line.visibility = View.INVISIBLE
                        this.iv_consume_record_middle_point.setImageResource(R.drawable.beauty_consume_record_stepview_pink_point)
                    }
                    else -> { // content
                        this.iv_consume_record_bottom_line.visibility = View.VISIBLE
                        this.iv_consume_record_top_line.visibility = View.VISIBLE
                        this.iv_consume_record_middle_point.setImageResource(R.drawable.beauty_consume_record_stepview_gray_point)
                    }
                }
            }
            this.iv_consume_record_time.text = DateTimeUtils.formatDate(history.bizDate)
            this.iv_consume_record_projects.text = history.serviceName
            this.iv_consume_record_remark.text = history.remark
        }
    }
}
