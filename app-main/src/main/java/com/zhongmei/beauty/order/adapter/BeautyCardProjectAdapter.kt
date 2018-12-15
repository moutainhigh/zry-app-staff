package com.zhongmei.beauty.order.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.zhongmei.yunfu.R
import com.zhongmei.bty.basemodule.beauty.BeautyCardManager
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceAccount
import com.zhongmei.beauty.order.vo.BeautyCardDishshopVo
import com.zhongmei.bty.commonmodule.database.enums.ServerPrivilegeType
import com.zhongmei.bty.commonmodule.util.manager.ClickManager
import kotlinx.android.synthetic.main.beauty_card_project_item.view.*

/**
 * 消费记录
 *

 * @date 2017/3/13 15:09
 */
class BeautyCardProjectAdapter(val mContext: Context, val mData: MutableList<BeautyCardServiceAccount>) : RecyclerView.Adapter<BeautyCardProjectAdapter.ViewHolder>() {

    private lateinit var mOnItemClickListener: OnProjectItemClickListener

    private val mBeautyCardManager: BeautyCardManager

    interface OnProjectItemClickListener {
        fun onProjectClickListener(vo: BeautyCardServiceAccount, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnProjectItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    init {
        mBeautyCardManager = BeautyCardManager.getInstance()
    }


    /**
     * 创建view
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.beauty_card_project_item, null)
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
            var vo = this@BeautyCardProjectAdapter.mData.get(position)
            this.tv_short_name.text = vo.serviceName
            this.tv_all_num.text = String.format(this@BeautyCardProjectAdapter.mContext.resources.getString(R.string.beauty_card_service_all_count), vo.serviceRemainderTime)// 可用次数
            if (!inspectServiceCount(vo)) {
                this.v_main_content.setBackgroundResource(R.drawable.beauty_card_service_bg_disable)
                this.tv_market_num.setTextColor(resources.getColor(R.color.beauty_color_BCBCBC))
                this.tv_all_num.setTextColor(resources.getColor(R.color.beauty_color_BCBCBC))
                this.tv_short_name.setTextColor(resources.getColor(R.color.beauty_color_BCBCBC))
                this.tv_market_num.text = "已用完"
                itemView.isEnabled = false
            } else {
                this.v_main_content.setBackgroundResource(R.drawable.beauty_grid_item_selector)
                this.tv_market_num.setTextColor(resources.getColor(R.color.beauty_color_FF666666))
                this.tv_all_num.setTextColor(resources.getColor(R.color.beauty_color_434343))
                this.tv_short_name.setTextColor(resources.getColor(R.color.beauty_color_434343))
                this.tv_market_num.text = getServiceSurplusCount(vo)
                itemView.isEnabled = true
            }
            itemView.setOnClickListener {
                if (ClickManager.getInstance().isClicked) {
                    return@setOnClickListener
                }
                if (!inspectServiceCount(vo)) Toast.makeText(this@BeautyCardProjectAdapter.mContext, "已经用完了", Toast.LENGTH_SHORT).show() else mOnItemClickListener!!.onProjectClickListener(vo, position)
            }
        }
    }

    fun inspectServiceCount(vo: BeautyCardServiceAccount): Boolean {
        var count = vo.serviceRemainderTime - mBeautyCardManager.getCacheCountById(ServerPrivilegeType.COUNT_SERVER, vo.cardInstanceId)
        return count > 0;
    }

    fun getServiceSurplusCount(vo: BeautyCardServiceAccount): String {
        var remainderCount = vo.serviceRemainderTime - mBeautyCardManager.getCacheCountById(ServerPrivilegeType.COUNT_SERVER, vo.cardInstanceId)
        return String.format(this@BeautyCardProjectAdapter.mContext.resources.getString(R.string.beauty_card_service_surplus_count), remainderCount)
    }
}
