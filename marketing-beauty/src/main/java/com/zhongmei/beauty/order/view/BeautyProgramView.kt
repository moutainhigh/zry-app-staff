package com.zhongmei.beauty.order.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.zhongmei.yunfu.beauty.R
import kotlinx.android.synthetic.main.beauty_small_program_layout.view.*
import android.graphics.Rect
import com.zhongmei.beauty.order.action.AppletRemoveAction
import com.zhongmei.beauty.order.adapter.BeautyProgramAdapter
import com.zhongmei.beauty.order.util.BeautyAppletTool
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener
import com.zhongmei.yunfu.net.volley.VolleyError
import com.zhongmei.beauty.operates.BeautyCustomerOperates
import com.zhongmei.beauty.operates.message.BeautyAcitivityBuyRecordResp
import com.zhongmei.beauty.order.util.IChangeMiddlePageListener
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory
import com.zhongmei.yunfu.util.ToastUtil
import com.zhongmei.yunfu.bean.YFResponseList
import com.zhongmei.yunfu.context.util.Utils
import com.zhongmei.yunfu.resp.YFResponseListener

/**

 */
class BeautyProgramView : LinearLayout {
    private var mContext: Context
    private var mChangeListener: ChangePageListener
    private var mChangeMiddlePageListener: IChangeMiddlePageListener;
    lateinit var programAdapter: BeautyProgramAdapter
    //小程序list
    private lateinit var appletList: ArrayList<BeautyAcitivityBuyRecordResp>;

    constructor(context: Context, changeListener: ChangePageListener, changeMiddlePageListener: IChangeMiddlePageListener) : super(context) {
        mContext = context
        mChangeListener = changeListener
        mChangeMiddlePageListener = changeMiddlePageListener
        setupView()
    }

    private fun setupView() {
        LayoutInflater.from(context).inflate(R.layout.beauty_small_program_layout, this, true)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        var lManager: LinearLayoutManager = LinearLayoutManager(mContext)
        lManager.orientation = LinearLayoutManager.VERTICAL
        recycler_view.layoutManager = lManager
        programAdapter = BeautyProgramAdapter(mContext, mChangeListener, mChangeMiddlePageListener)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = programAdapter
        recycler_view.addItemDecoration(SpacesItemDecoration(10))
        initData();
    }


    private fun initData() {
        var beautyOperate: BeautyCustomerOperates = OperatesFactory.create(BeautyCustomerOperates::class.java)
        var customerId = CustomerManager.getInstance().dinnerLoginCustomer.customerId
        beautyOperate.getActivityBuyRecord(
                customerId, object : YFResponseListener<YFResponseList<BeautyAcitivityBuyRecordResp>> {
            override fun onResponse(response: YFResponseList<BeautyAcitivityBuyRecordResp>?) {
                var recordList = response!!.content
                if (Utils.isEmpty(recordList)) {
                    showEmptyView()
                } else {
                    appletList = BeautyAppletTool.initAppletStatus(recordList)
                    programAdapter.refreshView(appletList)
                }

            }

            override fun onError(error: VolleyError?) {
                ToastUtil.showLongToast(error?.message)
            }
        })

    }

    fun onEventMainThread(action: AppletRemoveAction) {
        if (action.mShopcartItem == null || programAdapter == null) {
            return
        }
        programAdapter.updateRemove(action.mShopcartItem)
    }

    fun showEmptyView() {
        recycler_view.visibility = View.GONE
        include_empty_status.visibility = View.VISIBLE
    }

    fun showLoadingProgressDialog() {
//        if (mDialogFragment == null) {
//            mDialogFragment = CalmLoadingDialogFragment.showByAllowingStateLoss(mFragmentManager)
//            mFragmentManager.executePendingTransactions()
//        }
    }

    fun dismissLoadingProgressDialog() {
//        if (mDialogFragment != null) {
//            CalmLoadingDialogFragment.hide(mDialogFragment)
//            mDialogFragment = null
//        }
    }

    class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State?) {

            outRect.right = space
            outRect.top = space
        }
    }

}