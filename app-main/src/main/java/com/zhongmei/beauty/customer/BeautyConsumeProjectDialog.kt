package com.zhongmei.beauty.customer

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.*
import com.zhongmei.yunfu.R
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceHistory
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo
import com.zhongmei.yunfu.context.session.Session
import com.zhongmei.yunfu.net.volley.VolleyError
import com.zhongmei.yunfu.util.DensityUtil
import com.zhongmei.beauty.customer.adapter.BeautyConsumeRecordAdapter
import com.zhongmei.beauty.operates.BeautyCustomerOperates
import com.zhongmei.beauty.operates.message.BeautyCardServiceHistoryResp
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory
import com.zhongmei.yunfu.resp.ResponseListener
import com.zhongmei.yunfu.resp.ResponseObject
import com.zhongmei.yunfu.ui.base.BasicDialogFragment
import kotlinx.android.synthetic.main.beauty_consume_record_dialog.*
import kotlinx.android.synthetic.main.beauty_consume_record_header.*
import java.util.*

/**
 * 消费记录

 * @date 2018/4/9 17:11
 */
class BeautyConsumeProjectDialog : BasicDialogFragment(), View.OnClickListener {

    val TAG = "BeautyConsumeProjectDialog"

    private val mConsumeRecords = ArrayList<BeautyCardServiceHistory>()

    private var mRecordAdapter: BeautyConsumeRecordAdapter? = null

    private lateinit var mOperates: BeautyCustomerOperates

    private lateinit var mEcCardInfo: EcCardInfo

    override fun show(manager: FragmentManager, tag: String) {
        if (manager != null && !manager.isDestroyed) {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mOperates = OperatesFactory.create(BeautyCustomerOperates::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupWindow()
        return View.inflate(activity, R.layout.beauty_consume_record_dialog, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgmentsData()
        initView()
        getConsumeProjectData()
    }

    private fun getArgmentsData() {
        mEcCardInfo = arguments.getSerializable(BeautyConsumeProjectDialog.KEY_CARD_INFO) as EcCardInfo
    }

    private fun setupWindow() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)// 设置点击屏幕Dialog不消失
        val window = dialog.window
        if (window != null) {
            //设置宽高
//            val attributes = window.attributes
//            attributes.width = DensityUtil.dip2px(activity, 460f)
//            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
//            window.attributes = attributes
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    /**组装卡项目的RecyclerView
     *
     */
    private fun setupRecordView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.isSmoothScrollbarEnabled = true
        rv_consume_record.setHasFixedSize(true)
        rv_consume_record.layoutManager = linearLayoutManager
        rv_consume_record.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                when (parent.getChildAdapterPosition(view)) {
                    0 -> outRect.top = DensityUtil.px2dip(context, 30f) // 头部
                    mConsumeRecords.size - 1 -> DensityUtil.px2dip(context, 30f)
                }
            }
        })
        mRecordAdapter = BeautyConsumeRecordAdapter(activity, mConsumeRecords)
        rv_consume_record.adapter = mRecordAdapter
    }

    private fun initView() {
        btn_close.setOnClickListener(this)
        tv_consume_record_title_cardkindnum.setText(mEcCardInfo?.cardKindName + mEcCardInfo?.cardNum)
        setupRecordView()
    }

    /**
     * 获取消费项目数据
     */
    fun getConsumeProjectData() {
        if (mEcCardInfo == null || TextUtils.isEmpty(mEcCardInfo.cardNum)) {
            return
        }
        showLoadingProgressDialog()
        mOperates.queryConsumeProject(Session.getAuthUser().getId(), mEcCardInfo.cardNum, object : ResponseListener<BeautyCardServiceHistoryResp> {
            override fun onError(error: VolleyError?) {
                dismissLoadingProgressDialog()
            }

            override fun onResponse(response: ResponseObject<BeautyCardServiceHistoryResp>?) {
                mConsumeRecords.clear()
                if (response != null && response.content != null) {
                    var data = response.content.result
                    if (data != null && data.size > 0) {
                        mConsumeRecords.addAll(data)
                    }
                }
                mRecordAdapter?.notifyDataSetChanged()
                dismissLoadingProgressDialog()
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_close -> dismiss()
        }
    }

    /**
     * 静态属性/方法
     */
    companion object {
        val KEY_CARD_INFO = "card_info"
    }

}
