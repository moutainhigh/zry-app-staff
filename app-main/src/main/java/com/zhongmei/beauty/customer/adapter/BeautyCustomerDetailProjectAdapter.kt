package com.zhongmei.beauty.customer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zhongmei.yunfu.R
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceAccount
import kotlinx.android.synthetic.main.beauty_customer_detail_card_project_item.view.*

/**
 * 项目
 *

 * @date 2017/3/13 15:09
 */
class BeautyCustomerDetailProjectAdapter(val mContext: Context, private val mData: List<BeautyCardServiceAccount>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val S_HEADER_VIEW = 0x01
    private val S_CONTENT_VIEW = 0x02
    private val S_FOOTER_VIEW = 0x03

    /**
     * 创建view
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.beauty_customer_detail_card_project_item, null)
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = layoutParams
        if (viewType == S_HEADER_VIEW) return HeaderViewHolder(view) else return ContentViewHolder(view)
    }

    /**
     * 赋值数据
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is HeaderViewHolder -> viewHolder.bind()
            is ContentViewHolder -> viewHolder.bind(getTurePosition(position))
        }
    }

    override fun getItemCount(): Int = mData.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) S_HEADER_VIEW else S_CONTENT_VIEW
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() = with(itemView) {
            this.tv_card_project_item_total_count.visibility = View.GONE
            this.vw_card_project_item_divide_line.visibility = View.VISIBLE
            this.tv_card_project_item_name.setTextColor(resources.getColor(R.color.beauty_color_999999))
            this.tv_card_project_item_name.setText(R.string.beauty_customer_detail_project_item_title_name)
            this.tv_card_project_item_remain_count.setTextColor(resources.getColor(R.color.beauty_color_999999))
            this.tv_card_project_item_remain_count.setText(R.string.beauty_customer_detail_project_item_title_remain_count)
        }
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) = with(itemView) {
            // with(timeView) 它是将某对象作为函数的参数，在函数块内可以通过 this 指代该对象 ， 这里this带指itemView
            var data = this@BeautyCustomerDetailProjectAdapter.mData.get(position)
            this.tv_card_project_item_total_count.visibility = View.VISIBLE
            this.vw_card_project_item_divide_line.visibility = if (position - 1 == 7) View.GONE else View.VISIBLE
            this.tv_card_project_item_name.text = data.serviceName
            this.tv_card_project_item_name.setTextColor(resources.getColor(R.color.beauty_color_333333))
            this.tv_card_project_item_remain_count.text = String.format(resources.getString(R.string.beauty_customer_detail_card_project_item_remain_count), data.serviceRemainderTime)
            this.tv_card_project_item_remain_count.setTextColor(resources.getColor(R.color.beauty_color_333333))
            this.tv_card_project_item_total_count.text = String.format(resources.getString(R.string.beauty_customer_detail_card_project_item_total_count), data.serviceTotalTime)
        }
    }

    private fun getTurePosition(position: Int): Int {
        return position - 1
    }
}
