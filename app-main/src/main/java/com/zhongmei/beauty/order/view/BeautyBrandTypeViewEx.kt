package com.zhongmei.beauty.order.view

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import com.zhongmei.yunfu.R
import com.zhongmei.bty.basemodule.orderdish.bean.DishBrandTypes
import com.zhongmei.yunfu.db.entity.dish.DishBrandType
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager
import com.zhongmei.yunfu.util.DensityUtil
import com.zhongmei.yunfu.util.MobclickAgentEvent
import com.zhongmei.yunfu.context.util.Utils
import com.zhongmei.bty.dinner.Listener.BrandTypeListener
import com.zhongmei.bty.dinner.adapter.DishTwoTypeAdapter
import com.zhongmei.bty.snack.orderdish.adapter.DishTypeInflateEx
import kotlinx.android.synthetic.main.beauty_view_brandtype_select.view.*
import java.util.ArrayList
import java.util.HashMap

/**

 */
class BeautyBrandTypeViewEx : LinearLayout, DishTypeInflateEx.ChangeTypeListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private lateinit var mPopupView: View
    private var mContext: Context? = null
    private lateinit var mDishManager: DishManager
    private lateinit var mDishTypeInflate: DishTypeInflateEx
    private lateinit var mListerer: BrandTypeListener
    private lateinit var mTwoTypeMap: HashMap<Long, List<DishBrandType>>
    private lateinit var mTypeTask: LoadDishTwoType
    private lateinit var mLastDishType: DishBrandType
    private lateinit var mDishTwoTypeAdapter: DishTwoTypeAdapter
    private val mTwoTypeList = ArrayList<DishBrandType>()
    private var mPopupWindow: PopupWindow? = null
    private lateinit var mLvTwoTypes: ListView
    private lateinit var mHandler: Handler

    constructor(context: Context, dishManager: DishManager, listener: BrandTypeListener) : super(context) {
        mDishManager = dishManager
        mListerer = listener
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.beauty_view_brandtype_select, this, true)

        mPopupView = inflater.inflate(R.layout.beauty_popuwindow_brandtype_viewex, null)
        mLvTwoTypes = mPopupView.findViewById(R.id.lv_types) as ListView
        mDishTwoTypeAdapter = DishTwoTypeAdapter(mTwoTypeList, context)
        mDishTwoTypeAdapter.setItemLayoutRes(R.layout.beauty_listitem_dish_two_type)
//        mDishTwoTypeAdapter.setItemBgResId(R.drawable.beauty_dish_type_item_bg)
        mLvTwoTypes.setAdapter(mDishTwoTypeAdapter)
        mLvTwoTypes.setItemChecked(0, true)
        mLvTwoTypes.setOnItemClickListener(this)

        mContext = context
        arrow_up.setOnClickListener(this)
        arrow_down.setOnClickListener(this)
        btn_search_goods.setOnClickListener(this)

        val dishBrandTypes = mDishManager.supperDishTypes
        if (dishBrandTypes == null || Utils.isEmpty(dishBrandTypes.dishTypeList)) {
            return
        }

        mDishTypeInflate = DishTypeInflateEx(context, this)
        mDishTypeInflate.setPageSize(9)
        mDishTypeInflate.setLayoutResId(R.layout.beauty_item_dish_type_ex)
        mDishTypeInflate.setItemBg(R.drawable.beauty_dish_type_item_bg)
        mDishTypeInflate.setMoreBg(R.drawable.beauty_more_icon)
        mDishTypeInflate.setmIvArrBg(R.drawable.beauty_iv_arr_bg)
        mDishTypeInflate.setItemTextSize(16f)
        mDishTypeInflate.setItemTextColor(R.drawable.beauty_type_text_selector)

        mDishTypeInflate.setData(dishBrandTypes.dishTypeList)
        mDishTypeInflate.inflateView(viewPager_dish_type)
        mDishTypeInflate.hideSelectedViewArr()
        mHandler = Handler()
        mHandler.post(Runnable {
            mTypeTask = LoadDishTwoType(dishBrandTypes.dishTypeList[0], false)
            mTypeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            mTwoTypeMap = HashMap()
            var dishBrandTypes1: DishBrandTypes? = null
            for (type in dishBrandTypes.dishTypeList) {
                dishBrandTypes1 = mDishManager.getSecondDishTypes(type)
                if (dishBrandTypes1 == null)
                    continue
                mTwoTypeMap.put(type.id, dishBrandTypes1.dishTypeList)
            }
        })

    }

    override fun onChangeTypeListener(dishBrandType: DishBrandType, sw: Boolean) {
        if (mTypeTask != null)
            mTypeTask.cancel(true)
        if (sw) {
            mLvTwoTypes.setItemChecked(0, true)
            if (mTwoTypeMap == null)
                return
            val dishBrandTypes = mTwoTypeMap[dishBrandType.id]
            if (Utils.isNotEmpty(dishBrandTypes)) {
                mLastDishType = dishBrandTypes!!.get(0)
                mTwoTypeList.clear()
                mTwoTypeList.addAll(dishBrandTypes)

                showWindow()
                mDishTwoTypeAdapter.notifyDataSetChanged()

                mListerer?.onBrandTypeChange(mLastDishType, null, false)
            }
        } else {
            if (Utils.isNotEmpty(mTwoTypeList)) {
                showWindow()
            }
        }
    }

    internal inner class LoadDishTwoType(var dishBrandType: DishBrandType, show: Boolean) : AsyncTask<Void, Void, DishBrandTypes>() {
        var showView = true

        init {
            showView = show
        }

        override fun doInBackground(vararg params: Void): DishBrandTypes {
            return mDishManager.getSecondDishTypes(dishBrandType)
        }

        override fun onPostExecute(dishBrandTypes: DishBrandTypes?) {
            if (mDishTypeInflate.getLastType().id!!.toLong() != dishBrandType.id!!.toLong())
                return  //防点击错乱

            if (dishBrandTypes != null && mDishTwoTypeAdapter != null
                    && dishBrandTypes.dishTypeList != null && Utils.isNotEmpty(dishBrandTypes.dishTypeList)) {
                mLastDishType = dishBrandTypes.dishTypeList[0]
                mTwoTypeList.clear()
                mTwoTypeList.addAll(dishBrandTypes.dishTypeList)
                if (showView)
                    showWindow()
                else
                    dismissWindow()
                mDishTwoTypeAdapter.notifyDataSetChanged()

                mListerer?.onBrandTypeChange(mLastDishType, null, false)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.arrow_up -> {
                if (mDishTypeInflate != null)
                    mDishTypeInflate.scrollToPreviousPage()
                MobclickAgentEvent.onEvent(mContext, MobclickAgentEvent.dinnerOrderDishTypePageUp)
            }
            R.id.arrow_down -> {
                if (mDishTypeInflate != null)
                    mDishTypeInflate.scrollToNextPage()
                MobclickAgentEvent.onEvent(mContext, MobclickAgentEvent.dinnerOrderDishTypePageDown)
            }
            R.id.btn_search_goods -> mListerer?.onSearchClick()
            else -> {
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        mLastDishType = mTwoTypeList[position]
        mListerer?.onBrandTypeChange(mLastDishType, null, false)
        dismissWindow()
        mDishTypeInflate.setSelectTxt(mTwoTypeList[position].name)
        mDishTypeInflate.hideSelectedViewArr()
    }

    private fun showWindow() {
        if (mPopupWindow == null) {
            mPopupWindow = PopupWindow(mPopupView, LinearLayout.LayoutParams.WRAP_CONTENT, height)
        }
        if (mPopupWindow!!.isShowing())
            return
        mPopupWindow?.setFocusable(true)
        mPopupWindow?.setOutsideTouchable(true)
        mPopupWindow?.setBackgroundDrawable(BitmapDrawable())
        mPopupWindow?.setOnDismissListener(PopupWindow.OnDismissListener { mDishTypeInflate.hideSelectedViewArr() })
        val location = IntArray(2)
        getLocationInWindow(location)
        mPopupWindow?.showAtLocation(this, Gravity.NO_GRAVITY, location[0] - DensityUtil.dip2px(mContext, 162f), location[1])
    }

    private fun dismissWindow() {
        if (mPopupWindow != null && mPopupWindow!!.isShowing())
            mPopupWindow?.dismiss()
    }


}