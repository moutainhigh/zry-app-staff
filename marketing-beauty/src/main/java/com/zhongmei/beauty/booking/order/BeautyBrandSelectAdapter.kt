package com.zhongmei.beauty.booking.order

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.beauty.booking.bean.DishBrandTypeVo
import kotlinx.android.synthetic.main.beauty_select_brand_view.view.*

/**

 */
class BeautyBrandSelectAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private var mContext: Context
    private var mListener: BeautyBrandSelectListener
    private var mBrandTypeList: ArrayList<DishBrandTypeVo>? = ArrayList<DishBrandTypeVo>()

    constructor(context: Context, listener: BeautyBrandSelectListener) {
        mContext = context
        mListener = listener
    }

    public fun setDatas(brandTypeList: List<DishBrandTypeVo>) {
        mBrandTypeList!!.clear()
        mBrandTypeList!!.addAll(brandTypeList)
        notifyDataSetChanged()
    }

    class MyHolder : RecyclerView.ViewHolder {
        constructor(itemView: View) : super(itemView) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var holder: MyHolder = MyHolder(LayoutInflater.from(mContext).inflate(R.layout.beauty_select_brand_view, parent, false))
        return holder
    }

    override fun getItemCount(): Int {
        return mBrandTypeList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        var brandType = mBrandTypeList!![position]
        holder!!.itemView.brand_type_name.text = brandType.getName()
        holder!!.itemView.setOnClickListener {
            if (mListener != null) {
                mListener.onBrandTypeChange(brandType)
            }
            updateSelected(brandType)
            notifyDataSetChanged()
        }
        if (brandType.isSelected) {
            holder!!.itemView.setBackgroundColor(mContext.resources.getColor(R.color.beauty_color_FFEFF6))
        } else {
            holder!!.itemView.setBackgroundColor(mContext.resources.getColor(R.color.transparent))
        }
    }

    fun updateSelected(selectBrandType: DishBrandTypeVo) {
        mBrandTypeList!!.forEach() { brandType ->
            if (selectBrandType.isAll) {
                if (!brandType.isAll) {
                    brandType.isSelected = false
                } else {
                    brandType.isSelected = true
                }
            } else {
                if (brandType.brandType != null && selectBrandType.brandType != null && brandType.brandType == selectBrandType.brandType) {
                    brandType.isSelected = true
                } else {
                    brandType.isSelected = false
                }
            }
        }
    }

    fun clearSelected() {
        mBrandTypeList!!.forEach() { brandType ->
            brandType.isSelected = false
        }
        notifyDataSetChanged()
    }

}