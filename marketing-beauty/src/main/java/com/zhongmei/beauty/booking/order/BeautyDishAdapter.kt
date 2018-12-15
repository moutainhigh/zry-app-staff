package com.zhongmei.beauty.booking.order

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo
import com.zhongmei.yunfu.context.data.ShopInfoCfg
import kotlinx.android.synthetic.main.beauty_select_dish_item.view.*

/**

 */
class BeautyDishAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private var mContext: Context
    private var mDishVoList: ArrayList<DishVo>? = ArrayList()
    private var mManager: BeautySelectManager

    constructor(context: Context, manager: BeautySelectManager) {
        mContext = context
        mManager = manager
    }


    public fun setDatas(dishVoList: List<DishVo>) {
        mDishVoList!!.clear()
        mDishVoList!!.addAll(dishVoList)
        notifyDataSetChanged()
    }

    class MyHolder : RecyclerView.ViewHolder {
        constructor(itemView: View) : super(itemView) {
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var dishVo = mDishVoList!![position]
        holder.itemView.setOnClickListener {
            mManager.changeDishCache(dishVo)
            notifyDataSetChanged()
        }
        holder.itemView.checkView.isChecked = dishVo.isSelected
        holder.itemView.checkView.text = dishVo.name
        holder.itemView.price.text = ShopInfoCfg.formatCurrencySymbol(dishVo.price)
        if (dishVo.isSelected) {
            holder.itemView.setBackgroundColor(mContext.resources.getColor(R.color.beauty_color_FFEFF6))
        } else {
            holder.itemView.setBackgroundColor(mContext.resources.getColor(android.R.color.transparent))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var holder: MyHolder = MyHolder(LayoutInflater.from(mContext).inflate(R.layout.beauty_select_dish_item, parent, false))
        return holder
    }

    override fun getItemCount(): Int {
        return mDishVoList!!.size
    }
}