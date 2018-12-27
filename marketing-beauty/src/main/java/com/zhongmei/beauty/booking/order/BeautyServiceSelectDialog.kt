package com.zhongmei.beauty.booking.order

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.beauty.booking.bean.DishBrandTypeVo
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager
import com.zhongmei.yunfu.ui.view.SearchView
import kotlinx.android.synthetic.main.beauty_service_select_dialog.*
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo
import com.zhongmei.yunfu.context.util.Utils


/**

 */
class BeautyServiceSelectDialog : Dialog, BeautyBrandSelectListener {

    private var mContext: Context
    private lateinit var brandTypeAdapter: BeautyBrandSelectAdapter
    private lateinit var dishAdapter: BeautyDishAdapter
    private lateinit var mManager: BeautySelectManager
    private var mDishManager = DishManager()
    private var mCallback: BeautyServiceCallback? = null

    constructor(context: Context) : super(context) {
        mContext = context
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val uiOptions = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_LAYOUT_FLAGS or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        val decorView = window!!.getDecorView()
        decorView.setSystemUiVisibility(uiOptions)
        setCancelable(false)
        setContentView(R.layout.beauty_service_select_dialog)
        initBrandView()
        initDishView()
        loadData()
        bindListenr()
    }

    fun bindListenr() {
        btn_back.setOnClickListener {
            dismiss()
        }
        btn_ok.setOnClickListener {
            mCallback?.onCallback(ArrayList(mManager!!.dishCache!!.values))
            this.dismiss()
        }
        edit_search.setControlView(
                null,
                object : SearchView.TextChangeCallback {
                    override fun afterTextChanged(s: Editable?) {
                        dishAdapter.setDatas(mManager.doSearch(mDishManager, s.toString()))
                        brandTypeAdapter.clearSelected()
                    }
                }
        )
    }

    /**
     * 注册服务选择事件
     */
    fun registerCallback(callback: BeautyServiceCallback) {
        mCallback = callback
    }

    fun initBrandView() {
        brandTypeAdapter = BeautyBrandSelectAdapter(mContext, this);
        beauty_brand_rview.layoutManager = LinearLayoutManager(context)
        beauty_brand_rview.adapter = brandTypeAdapter
        beauty_brand_rview.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))

    }

    fun initDishView() {
        mManager = BeautySelectManager()
        dishAdapter = BeautyDishAdapter(mContext, mManager)
        beauty_dish_rview.layoutManager = LinearLayoutManager(context)
        beauty_dish_rview.adapter = dishAdapter
        beauty_dish_rview.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
    }

    /**
     * 加载数据
     */
    fun loadData() {
        var typeList = mManager.loadBrandTypeData(mDishManager)
        brandTypeAdapter.setDatas(typeList)
         var listDishVo:List<DishVo> = ArrayList()
        if(Utils.isNotEmpty(typeList)){
            typeList!!.get(0)!!.isSelected=true
            listDishVo=mManager.initDishVoByType(mDishManager, typeList!!.get(0))
        }
        dishAdapter.setDatas(listDishVo)
    }

    override fun onBrandTypeChange(dishBrandType: DishBrandTypeVo) {
        if (dishBrandType.isAll) {
            dishAdapter.setDatas(mManager.initAllDishVo(mDishManager))
        } else {
            dishAdapter.setDatas(mManager.initDishVoByType(mDishManager, dishBrandType))
        }
    }


}