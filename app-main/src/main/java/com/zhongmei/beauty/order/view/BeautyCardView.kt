package com.zhongmei.beauty.order.view

import android.content.Context
import android.graphics.Rect
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.zhongmei.yunfu.R
import com.zhongmei.bty.basemodule.beauty.BeautyCardManager
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceAccount
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceInfo
import com.zhongmei.yunfu.bean.req.CustomerResp
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache
import com.zhongmei.yunfu.context.session.Session
import com.zhongmei.yunfu.net.volley.VolleyError
import com.zhongmei.yunfu.context.util.ConvertUtils
import com.zhongmei.yunfu.context.util.DateTimeUtils
import com.zhongmei.beauty.operates.BeautyCustomerOperates
import com.zhongmei.beauty.order.adapter.BeautyCardAdapter
import com.zhongmei.beauty.order.adapter.BeautyCardProjectAdapter
import com.zhongmei.beauty.order.util.IChangeMiddlePageListener
import com.zhongmei.beauty.order.vo.BeautyCardDishshopVo
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory
import com.zhongmei.bty.commonmodule.database.enums.ServerPrivilegeType
import com.zhongmei.yunfu.util.ToastUtil
import com.zhongmei.bty.commonmodule.util.manager.ClickManager
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment
import com.zhongmei.yunfu.bean.YFResponseList
import com.zhongmei.yunfu.context.util.Utils
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal
import com.zhongmei.yunfu.db.entity.dish.DishShop
import com.zhongmei.yunfu.resp.YFResponseListener
import kotlinx.android.synthetic.main.beauty_order_membership_card.view.*
import kotlinx.android.synthetic.main.beauty_order_membership_card_project_title.view.*

/**
 * 订单-会员卡
 *
 * Created by demo on 2018/12/15
 */
class BeautyCardView : LinearLayout, BeautyCardAdapter.OnItemClickListener, BeautyCardProjectAdapter.OnProjectItemClickListener, BeautyCardManager.IBeautyCardListener {

    private var mProjectAdapter: BeautyCardProjectAdapter? = null

    private var mCardAdapter: BeautyCardAdapter?=null

    private var mContext: Context

    private var mIChangeMiddlePageListener: IChangeMiddlePageListener

    private var mDishShopList = ArrayList<DishShop>()

    private var mCardList = ArrayList<BeautyCardServiceInfo>()

    private lateinit var mCustomer: CustomerResp

    private var mFragmentManager: FragmentManager

    private var mDialogFragment: CalmLoadingDialogFragment? = null

    private var mOperates: BeautyCustomerOperates

    private var mCardManager: BeautyCardManager

    constructor(context: Context, iChangeMiddlePageListener: IChangeMiddlePageListener, fragmentManager: FragmentManager) : super(context) {
        this.mContext = context
        this.mIChangeMiddlePageListener = iChangeMiddlePageListener
        this.mFragmentManager = fragmentManager
        this.mOperates = OperatesFactory.create(BeautyCustomerOperates::class.java)
        this.mCardManager = BeautyCardManager.getInstance()
        mCardManager.setIBeautyCardListener(this)
        setupView()
        if (verifyCustomer()) {
            queryCardByCustomerId(mCustomer.customerId)
        }
    }

    /**
     * 验证会员信息 是否有效
     */
    fun verifyCustomer(): Boolean {
        mCustomer = CustomerManager.getInstance().dinnerLoginCustomer
        if (mCustomer != null && mCustomer.customerId != null) {
            return true
        }
        return false
    }

    private fun setupView() {
        //加载View
        LayoutInflater.from(context).inflate(R.layout.beauty_order_membership_card, this, true)
        setupCardRecycelerView()
        setupCardProjectRecycelerView()
        beauty_order_card_list.visibility=View.VISIBLE
        beauty_order_card_project_list.visibility=View.GONE
    }

    /**
     * 卡的view
     */
    private fun setupCardRecycelerView() {
        val linearLayoutManager = LinearLayoutManager(mContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.isSmoothScrollbarEnabled = true
        beauty_order_card_list.layoutManager = linearLayoutManager
        beauty_order_card_list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                when (parent.getChildAdapterPosition(view)) {
                    0 -> {
                        outRect.top = ConvertUtils.dp2px(mContext, 4f)
                        outRect.bottom = ConvertUtils.dp2px(mContext, 9f)
                    }// 头部
                    else -> outRect.bottom = ConvertUtils.dp2px(mContext, 9f)
                }
            }
        })
        mCardAdapter = BeautyCardAdapter(mContext, mCardList)
        mCardAdapter!!.setOnItemClickListener(this)
        beauty_order_card_list.adapter = mCardAdapter
    }

    /**
     * 卡的项目的view
     */
    private fun setupCardProjectRecycelerView() {
        val linearLayoutManager = LinearLayoutManager(mContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.isSmoothScrollbarEnabled = true
        beauty_order_card_project_list.layoutManager = linearLayoutManager
        beauty_order_card_project_list.setHasFixedSize(true)
        beauty_order_card_project_list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                when (parent.getChildAdapterPosition(view)) {
                    0 -> {
                        outRect.top = ConvertUtils.dp2px(mContext, 9f)
                        outRect.bottom = ConvertUtils.dp2px(mContext, 9f)
                    }// 头部
                    else -> outRect.bottom = ConvertUtils.dp2px(mContext, 9f)
                }
            }
        })
        mProjectAdapter = BeautyCardProjectAdapter(mContext, mDishShopList)
        mProjectAdapter!!.setOnItemClickListener(this)
        beauty_order_card_project_list.adapter = mProjectAdapter
    }

    override fun onCardItemClickListener(info: BeautyCardServiceInfo?,position: Int) {
        //展示卡中对应的服务信息，需要从本地数据库中查询


        if(Utils.isEmpty(info!!.listDishShops)){
            ToastUtil.showShortToast("当前服务不可用")
            return
        }

        if(info!!.listDishShops.size==1){
            mCardManager.addDishshopToShopCart(ServerPrivilegeType.COUNT_SERVER, info,info!!.listDishShops!!.get(0), position)
            return
        }

        //展示二级UI

        mDishShopList.clear()

        include_empty_status.visibility = View.GONE
        mDishShopList.addAll(info!!.listDishShops)
        mProjectAdapter?.setCardInfo(info,position)
        mProjectAdapter?.notifyDataSetChanged()

        beauty_order_card_project_list.visibility=View.VISIBLE
        beauty_order_card_list.visibility=View.GONE
    }

    /**
     * 次卡项目点击事件
     * @param dishShop 项目
     */
    override fun onProjectClickListener(vo: BeautyCardServiceInfo?,dishShop:DishShop?, position: Int) {
        if(vo==null || dishShop==null){
            ToastUtil.showShortToast("当前服务不可用")
            return
        }
        mCardManager.addDishshopToShopCart(ServerPrivilegeType.COUNT_SERVER, vo,dishShop, position)
        beauty_order_card_list.visibility=View.VISIBLE
        beauty_order_card_project_list.visibility=View.GONE
    }


    /**
     * 查询卡片数据
     */
    fun queryCardByCustomerId(customerId: Long) {
        // FIXME 通过会员id 查询当前会员下的所有卡片，并过滤会员卡, 查询数据库构建商品数据
        showLoadingProgressDialog()
        mOperates.getCardServiceInfo(Session.getAuthUser().getId(), customerId, object : YFResponseListener<YFResponseList<BeautyCardServiceInfo>> {
            override fun onResponse(response: YFResponseList<BeautyCardServiceInfo>?) {
                mDishShopList.clear()
                if (response == null || response!!.content == null || response!!.content.size == 0) {
                    include_empty_status.visibility = View.VISIBLE
                } else {
                    include_empty_status.visibility = View.GONE
                    beauty_order_card_list.visibility=View.VISIBLE
                    beauty_order_card_project_list.visibility=View.GONE
                    queryCardServerItems(response!!.content)//初始化卡中子项目的服务
                    mCardList.addAll(response!!.content)
                    mCardAdapter?.notifyDataSetChanged()
                }
                dismissLoadingProgressDialog()
            }

            override fun onError(error: VolleyError?) {
                dismissLoadingProgressDialog()
            }
        })
    }

    fun queryCardServerItems(cardInfos:List<BeautyCardServiceInfo>){
        cardInfos!!.forEach(){ cardInfo ->
            var  listDishSetmeal=DishCache.getSetmealHolder().getDishSetmealByDishId(cardInfo!!.serviceId)

            if(Utils.isNotEmpty(listDishSetmeal)){
                listDishSetmeal.forEach(){ dishSetmeal ->
                    var dishShop:DishShop?=DishCache.getDishHolder().get(dishSetmeal.childDishId)
                    if(dishShop!=null){
                        cardInfo.listDishShops.add(dishShop)
                    }
                }
            } else {
                var dishShops = DishCache.getDishHolder().all
                cardInfo.listDishShops.addAll(dishShops)
            }

        }

    }


    fun showLoadingProgressDialog() {
        if (mDialogFragment == null) {
            mDialogFragment = CalmLoadingDialogFragment.showByAllowingStateLoss(mFragmentManager)
            mFragmentManager.executePendingTransactions()
        }
    }

    fun dismissLoadingProgressDialog() {
        if (mDialogFragment != null) {
            CalmLoadingDialogFragment.hide(mDialogFragment)
            mDialogFragment = null
        }
    }


    override fun onRemoveCartCallBack() {
        mCardAdapter!!.notifyDataSetChanged()
    }

    override fun onAddCartCallBack(position: Int) {
        mCardAdapter!!.notifyDataSetChanged()
    }
}


