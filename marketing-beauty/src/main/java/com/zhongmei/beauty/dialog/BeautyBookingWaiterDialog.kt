package com.zhongmei.beauty.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.*
import com.zhongmei.beauty.adapter.RoleAdapter
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.bty.basemodule.trade.enums.TradeUserType
import com.zhongmei.yunfu.util.DensityUtil
import com.zhongmei.beauty.entity.UserVo
import com.zhongmei.beauty.event.EventOrderCleanRBChecked
import com.zhongmei.beauty.view.BeautyBookingWaiterView
import com.zhongmei.beauty.view.RoleItemView.OnRoleCheckedListener
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem
import com.zhongmei.bty.basemodule.session.core.user.UserFunc
import com.zhongmei.yunfu.ui.view.recycler.RecyclerLinearLayoutManager
import com.zhongmei.yunfu.ui.base.BasicDialogFragment
import com.zhongmei.yunfu.context.session.Session
import com.zhongmei.yunfu.context.session.core.user.Role
import com.zhongmei.yunfu.util.ToastUtil
import de.greenrobot.event.EventBus
import kotlinx.android.synthetic.main.beauty_booking_waiter_dialog.*
import kotlinx.android.synthetic.main.beauty_waiter_dialog.*

/**
 * 服务技师选择

 * @date 2018/4/9 17:11
 */
class BeautyBookingWaiterDialog : BasicDialogFragment(), View.OnClickListener, OnRoleCheckedListener {

    private var mUserVos: MutableList<UserVo> = ArrayList()

    private var mUserMaps: HashMap<Long, UserVo> = HashMap() // 缓存选中数据

    private lateinit var mOnBeautyWaiterListener: OnBeautyWaiterListener

    private lateinit var mBeautyWaiterView: BeautyBookingWaiterView

    private lateinit var mParentView: View

    private lateinit var mIsNotFreeUsers: List<Long> // 被占用的技师

    private lateinit var mRoleAdapter: RoleAdapter;

    private var mDishDataItem: DishDataItem? = null

    interface OnBeautyWaiterListener {
        fun onChoiceUserListener(userVo: List<UserVo>?)
    }

    fun setOnBeautyWaiterListener(onBeautyWaiterListener: OnBeautyWaiterListener) {
        this.mOnBeautyWaiterListener = onBeautyWaiterListener
    }

    fun setIsNotFreeUsers(isNotFreeUsers: List<Long>) {
        this.mIsNotFreeUsers = isNotFreeUsers
    }

    fun setSelectUsers(selectUsers: List<UserVo>) {
        if (selectUsers != null && selectUsers.size > 0) {
            mUserMaps.put(selectUsers.get(0).user.id, selectUsers.get(0))
        }
    }

    fun setDishDataItem(item: DishDataItem?) {
        this.mDishDataItem = item
    }

    override fun show(manager: FragmentManager, tag: String) {
        if (manager != null && !manager.isDestroyed) {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mParentView = View.inflate(activity, R.layout.beauty_booking_waiter_dialog, null)
        setupWindow()
        return mParentView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initData() {//初始化数据
        //获取数据
        var mRoles = Session.getFunc(UserFunc::class.java).roles
        mRoleAdapter = RoleAdapter(activity, this);
        mRoleAdapter.items = mRoles;


        val manager = RecyclerLinearLayoutManager(context)
        lv_technicianType.layoutManager = manager
        lv_technicianType.adapter = mRoleAdapter;

        onRoleChecked(mRoleAdapter.getItem(0) as Role?);

        //加载到adapter
        //默认选中第一项
    }

    private fun setupWindow() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)// 设置点击屏幕Dialog不消失
        val window = dialog.window
        if (window != null) {
            //设置宽高
            val attributes = window.attributes
            attributes.width = DensityUtil.dip2px(activity, 460f)
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = attributes
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun initView() {
//        btn_cosmetologist.setOnClickListener(this)
        btn_bookinig_waiter_close.setOnClickListener(this)
        btn_ok.setOnClickListener(this)
        initWaiterView()
    }

    private fun initWaiterView() {
        mBeautyWaiterView = BeautyBookingWaiterView(activity, false)
        mBeautyWaiterView.setOnUserCheckedListener(object : BeautyBookingWaiterView.OnUserCheckedListener {
            override fun onUserCheckData(user: UserVo?) {
                if (user != null) {
                    mUserMaps.clear()//最多只存放一个。其他的都移除
                    if (user.isChecked) {
                        mUserMaps.put(user.user.id, user) // 剔除重复数据
                    } else {
                        mUserMaps.remove(user.user.id)
                    }
                    mUserVos.clear()
                    mUserMaps.mapValues {
                        mUserVos.add(it.value)
                    }

                }
            }
        })
        v_contents.addView(mBeautyWaiterView)
        showCosmetologist()
    }

    override fun onClick(v: View) {
        when (v.id) {
//            R.id.btn_cosmetologist -> showCosmetologist()
//            R.id.btn_adviser -> showAdviser()
//            R.id.btn_shopowner -> showShopOwner()
            R.id.btn_bookinig_waiter_close -> dismissAllowingStateLoss()
            R.id.btn_ok -> sure()
        }
    }

    fun sure() {
        mOnBeautyWaiterListener!!.onChoiceUserListener(mUserVos)
        dismissAllowingStateLoss()
    }

    override fun onRoleChecked(role: Role?) {
        mRoleAdapter.setCheckedRole(role);
        mRoleAdapter.notifyDataSetChanged();
        var userVo: UserVo? = null;
        if (mUserMaps != null && mUserMaps.values != null && mUserMaps.values.size > 0) {
            userVo = mUserMaps.values.first();
        }

        refreshWaiterView(role, userVo)
    }

    /**
     * 技师
     */
    private fun showCosmetologist() {
//        ViewUtil.setButtonSelected(btn_waiter_bar, btn_cosmetologist)
//        refreshWaiterView(mUserMaps.get(TradeUserType.TECHNICIAN.value()))
    }

    /**
     * 刷新 waiter view
     * @param userVo 当前选中的人，可以为null
     */
    private fun refreshWaiterView(role: Role?, userVo: UserVo?) {
        mBeautyWaiterView!!.refreshView(role, userVo, mIsNotFreeUsers)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        EventBus.getDefault().post(EventOrderCleanRBChecked());
    }
}
