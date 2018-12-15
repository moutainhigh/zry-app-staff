package com.zhongmei.beauty.order.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.zhongmei.yunfu.R
import com.zhongmei.yunfu.db.entity.dish.DishBrandType
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager
import com.zhongmei.yunfu.db.enums.Bool
import com.zhongmei.bty.dinner.Listener.BrandTypeListener
import com.zhongmei.bty.snack.orderdish.adapter.DishTypeInflate
import kotlinx.android.synthetic.main.beauty_view_brandtype_select.view.*

/**

 */
class BeautyBrandTypeView : LinearLayout, DishTypeInflate.ChangeTypeListener, View.OnClickListener {


    private var mContext: Context? = null
    private val mDishManager: DishManager
    private lateinit var mDishTypeInflate: DishTypeInflate
    private var mListerer: BrandTypeListener

    constructor(context: Context, dishManager: DishManager, listener: BrandTypeListener) : super(context) {
        mDishManager = dishManager
        mListerer = listener
        initView(context)
    }

    private fun assignViews() {
        arrow_up.setBackgroundResource(R.drawable.beauty_arrow_left)
        arrow_down.setBackgroundResource(R.drawable.beauty_arrow_right)
    }

    fun initView(context: Context) {
        val inflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.beauty_view_brandtype_select, this, true)
        assignViews()
        mContext = context
        arrow_down.setOnClickListener(this)
        arrow_up.setOnClickListener(this)
        btn_search_goods.setOnClickListener(this)
        mDishTypeInflate = DishTypeInflate(context, this)
        mDishTypeInflate.setPageSize(7)
        mDishTypeInflate.setItemBg(R.drawable.beauty_dish_type_item_bg)
        mDishTypeInflate.setItemTextSize(16f)
        mDishTypeInflate.setItemTextColor(R.drawable.beauty_type_text_selector)

        val dishBrandTypes = mDishManager.loadData().dishTypeList
        val iterator = dishBrandTypes.iterator()    //过滤不显示中类
        while (iterator.hasNext()) {
            val type = iterator.next()
            if (type.isShow !== Bool.YES.value())
                iterator.remove()
        }
        mDishTypeInflate.setData(dishBrandTypes)
        mDishTypeInflate.inflateView(viewPager_dish_type)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.arrow_up ->
                mDishTypeInflate.scrollToPreviousPage()
            R.id.arrow_down ->
                mDishTypeInflate.scrollToNextPage()
            R.id.btn_search_goods ->
                mListerer.onSearchClick()
        }

    }

    override fun onChangeTypeListener(dishBrandType: DishBrandType?) {
        mListerer.onBrandTypeChange(dishBrandType, null, false)
    }
}