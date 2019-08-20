package com.zhongmei.beauty.order.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.zhongmei.beauty.order.util.BeautyAppletTool
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener
import com.zhongmei.beauty.operates.message.BeautyAcitivityBuyRecordResp
import com.zhongmei.beauty.order.util.IChangeMiddlePageListener
import com.zhongmei.yunfu.context.util.Utils
import kotlinx.android.synthetic.main.beauty_program_detail.*

/**
 * 小程序详情对话框

 */
class BeautyProgramDialog : Dialog {
    private var mChangeListener: ChangePageListener
    private var mChangeMiddlePageListener: IChangeMiddlePageListener;
    private lateinit var mProgram: BeautyAcitivityBuyRecordResp
    private var mContext: Context

    constructor(cotext: Context, changeListener: ChangePageListener, changeMiddlePageListener: IChangeMiddlePageListener) : super(cotext) {
        mContext = context
        mChangeListener = changeListener
        mChangeMiddlePageListener = changeMiddlePageListener
    }

    fun setData(program: BeautyAcitivityBuyRecordResp) {
        mProgram = program
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        var view: View = layoutInflater.inflate(R.layout.beauty_program_detail, null, false)
        setContentView(view)
        tv_original_price.paintFlags = Paint.SUBPIXEL_TEXT_FLAG
        tv_original_price.setText(mContext.resources.getString(R.string.beauty_orginal_price) + Utils.formatPrice(mProgram.costPrice.toDouble()))
        tv_price.setText(Utils.formatPrice(mProgram.activityPrice.toDouble()))
        tv_content_label.setText(BeautyAppletTool.getAppletNameByType(mContext, mProgram.type,mProgram.marketingName))
        tv_content.setText(mProgram.describe)
        bindListener()
    }


    private fun bindListener() {
        btn_close.setOnClickListener {
            this.dismiss()
        }

        btn_use.setOnClickListener {
            addDishToShopcart()
            this.dismiss()
        }
    }

    private fun addDishToShopcart() {
        BeautyAppletTool.addDishToShopcart(mProgram, mChangeListener, mChangeMiddlePageListener, mContext)
    }

}