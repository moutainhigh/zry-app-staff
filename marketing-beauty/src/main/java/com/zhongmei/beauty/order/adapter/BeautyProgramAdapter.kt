package com.zhongmei.beauty.order.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener
import com.zhongmei.beauty.operates.message.BeautyAcitivityBuyRecordResp
import com.zhongmei.beauty.order.util.BeautyAppletTool
import com.zhongmei.beauty.order.util.IChangeMiddlePageListener
import com.zhongmei.beauty.order.view.BeautyProgramDialog
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase
import com.zhongmei.bty.commonmodule.util.DateUtil
import kotlinx.android.synthetic.main.beauty_program_item.view.*

/**

 */
class BeautyProgramAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var mContext: Context
    private var mChangeListener: ChangePageListener
    private var mChangeMiddlePageListener: IChangeMiddlePageListener
    private var mProgramList: ArrayList<BeautyAcitivityBuyRecordResp>? = ArrayList<BeautyAcitivityBuyRecordResp>()

    constructor(context: Context, changeListener: ChangePageListener, changeMiddlePageListener: IChangeMiddlePageListener) {
        mContext = context
        mChangeListener = changeListener
        mChangeMiddlePageListener = changeMiddlePageListener
    }

    class MyHolder : RecyclerView.ViewHolder {
        constructor(itemView: View) : super(itemView) {
        }
    }

    public fun refreshView(recordList: ArrayList<BeautyAcitivityBuyRecordResp>) {
        mProgramList!!.clear()
        mProgramList!!.addAll(recordList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        var program: BeautyAcitivityBuyRecordResp = mProgramList!![position]
        holder!!.itemView.tv_title.setText(program.dishName)
        holder!!.itemView.tv_content.setText("有效期：" + DateUtil.formatDate(program.validityPeriod))
        holder!!.itemView.ll_content.setOnClickListener {
            //combo:100615734186551296 sigle:107161226020891648
            if (program.isUsed) {
                BeautyAppletTool.removeApplet(program)
            } else {
                BeautyAppletTool.addDishToShopcart(program, mChangeListener, mChangeMiddlePageListener, mContext)
            }
            notifyDataSetChanged()
        }

        holder!!.itemView.btn_more.setOnClickListener {
            showDetailDialog(program)
        }

        if (program.isUsed) {
            holder!!.itemView.tv_use.text = mContext.resources.getString(R.string.beauty_used)
        } else {
            holder!!.itemView.tv_use.text = mContext.resources.getString(R.string.beauty_use)
        }
    }

    public fun updateRemove(mShopcartItem: IShopcartItemBase) {
        BeautyAppletTool.doAppletRemove(mShopcartItem, mProgramList)
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        var holder: MyHolder = MyHolder(LayoutInflater.from(mContext).inflate(R.layout.beauty_program_item, parent, false))
        return holder
    }

    override fun getItemCount(): Int {
        if (mProgramList == null) {
            return 0
        }
        return mProgramList!!.size
    }

    /**
     * 显示详情
     */
    private fun showDetailDialog(program: BeautyAcitivityBuyRecordResp) {
        var dialog: BeautyProgramDialog = BeautyProgramDialog(mContext, mChangeListener, mChangeMiddlePageListener)
        dialog.setData(program)
        dialog.show()
    }
}