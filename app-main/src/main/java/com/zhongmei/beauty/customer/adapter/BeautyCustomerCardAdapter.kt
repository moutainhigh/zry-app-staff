package com.zhongmei.beauty.customer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zhongmei.yunfu.R
import com.zhongmei.yunfu.bean.req.CustomerResp
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerCardItem
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo
import com.zhongmei.bty.commonmodule.util.manager.ClickManager
import kotlinx.android.synthetic.main.beauty_customer_card_item.view.*

/**
 * 卡选择
 *

 * @date 2017/3/13 15:09
 */
class BeautyCustomerCardAdapter(val mContext: Context, private val mData: List<CustomerCardItem>, private val mCustomer: CustomerResp) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val S_HEADER_VIEW = 0x01
    private val S_CONTENT_VIEW = 0x02
    private val S_FOOTER_VIEW = 0x03

    private lateinit var mListener: OnCardItemClickListener

    interface OnCardItemClickListener {
        fun onCardItemListener(customerNew: CustomerResp, item: EcCardInfo)
        fun onCustomerListener(customerNew: CustomerResp)
    }

    fun setOnCardItemClickListener(listener: OnCardItemClickListener) {
        this.mListener = listener
    }

    /**
     * 创建view
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.beauty_customer_card_item, null)
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
            this.beauty_card_root.setBackgroundResource(R.drawable.beauty_card_virtual_bg)
            this.beauty_card_name.text = "虚拟卡"
            this.beauty_card_no.text = this@BeautyCustomerCardAdapter.mCustomer.mobile
            this.beauty_card_root.setOnClickListener {
                if (ClickManager.getInstance().isClicked) {
                    return@setOnClickListener
                }
                mListener?.onCustomerListener(this@BeautyCustomerCardAdapter.mCustomer)
            }
        }
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) = with(itemView) {
            // with(timeView) 它是将某对象作为函数的参数，在函数块内可以通过 this 指代该对象 ， 这里this带指itemView
            var data = this@BeautyCustomerCardAdapter.mData.get(position)
            this.beauty_card_root.setBackgroundResource(R.drawable.beauty_card_entity_bg)
            this.beauty_card_name.text = data.cardKindName
            this.beauty_card_no.text = data.cardNum
            this.beauty_card_root.setOnClickListener {
                if (ClickManager.getInstance().isClicked) {
                    return@setOnClickListener
                }
                mListener?.onCardItemListener(this@BeautyCustomerCardAdapter.mCustomer, data.ecCardInfo)
            }
        }
    }

    private fun getTurePosition(position: Int): Int {
        return position - 1
    }
}
