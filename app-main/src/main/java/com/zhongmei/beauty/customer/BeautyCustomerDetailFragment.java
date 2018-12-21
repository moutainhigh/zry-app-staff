package com.zhongmei.beauty.customer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.beauty.customer.adapter.BeautyCustomerDetailProjectAdapter;
import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.auth.application.PhoneApplication;
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceAccount;
import com.zhongmei.bty.basemodule.customer.CustomerLogin;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;
import com.zhongmei.bty.basemodule.customer.message.BindCustomerFaceCodeResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerCardItem;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.MemberCardResp;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.bty.basemodule.devices.phone.exception.CalmPhoneException;
import com.zhongmei.bty.basemodule.devices.phone.exception.CalmPhoneStateException;
import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.customer.CustomerActivity;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment_;
import com.zhongmei.bty.customer.event.CustomerFragmentReplaceListener;
import com.zhongmei.bty.customer.event.DetailRefreshEvent;
import com.zhongmei.bty.customer.event.EventCreateOrEditCustomer;
import com.zhongmei.bty.customer.event.EventRefreshBalance;
import com.zhongmei.bty.customer.event.EventRefreshDetail;
import com.zhongmei.bty.customer.event.EventShowEditIcon;
import com.zhongmei.bty.customer.util.AppUtil;
import com.zhongmei.bty.customer.util.CustomerContants;
import com.zhongmei.bty.customer.views.CustomerLevelUpDialogFragment;
import com.zhongmei.bty.customer.views.CustomerVerifyCodeDialog_;
import com.zhongmei.bty.customer.views.DrawableLeftCenterTextView;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.req.CustomerLoginReq;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.beauty_customer_detail)
public class BeautyCustomerDetailFragment extends BasicFragment {
    private static final String TAG = BeautyCustomerDetailFragment.class.getSimpleName();

    @FragmentArg(CustomerActivity.PARAMKEY_CUSTOMER)
    protected CustomerResp mCustomer;

    @ViewById(R.id.customer_detail_frame)
    protected LinearLayout mainLayout;

    @ViewById(R.id.booking_detail_empty_layout)
    RelativeLayout mEmptyLayout;

    @ViewById(R.id.customer_empty_hint)
    TextView mEmptyHint;

    /****************** 会员顶部基础控件 *****************/
    @ViewById(R.id.name)
    protected TextView mName;

    @ViewById(R.id.mobileInfo)
    protected TextView mMobile;

    @ViewById(R.id.customer_member_level)
    TextView memberLevel;

    @ViewById(R.id.tv_take_photo)
    protected TextView mTvTakePhoto;

    @ViewById(R.id.customer_takephoto_layout)
    LinearLayout mTakePhotoLayout;

    @ViewById(R.id.customer_bind_layout)
    LinearLayout mBindLayout;

    @ViewById(R.id.level_up_layout)
    LinearLayout mLevelUpLayout;

    @ViewById(R.id.customer_call_layout)
    LinearLayout mCallLayout;

    @ViewById(R.id.customer_edit_layout)
    LinearLayout mEditLayout;

    @ViewById(R.id.customer_menber_title_base)
    TextView mCustomerTitleBase;

    @ViewById(R.id.customer_menber_title_card)
    TextView mCustomerTitleCard;

    @ViewById(R.id.customer_menber_title_base_root)
    RelativeLayout mCustomerTitleBaseRoot;

    @ViewById(R.id.customer_menber_title_card_root)
    RelativeLayout mCustomerTitleCardRoot;

    /****************** 会员顶部基础控件 （完） *****************/

    /******************** 会员基本信息 or 实体卡信息 切换 tab **********************/
    @ViewById(R.id.customer_title_layout)
    LinearLayout mTitleLayout;

    @ViewById(R.id.customer_menber_title_base)
    protected TextView memberTitleBase;

    @ViewById(R.id.customer_menber_title_card)
    protected TextView memberTitleCard;

    @ViewById(R.id.customer_menber_title_base_selected)
    protected View memberTitleBaseSelected;

    @ViewById(R.id.customer_menber_title_card_selected)
    protected View memberTitleCardSelected;

    @ViewById(R.id.customer_menber_title_card_num)
    protected TextView memberTitleCardNum;
    /******************** 会员基本信息 or 实体卡信息 切换 tab （完） **********************/


    /*********************** 会员基本信息 **********************/
    @ViewById(R.id.detail_base)
    LinearLayout baseDetailLl;

    /****** 会员信息 消费总计 ******/
    @ViewById(R.id.tv_total_consume_content)
    TextView mTvTotalConsumeContent;

    @ViewById(R.id.tv_total_consume_count_content)
    TextView mTvTotalConsumeCountContent;

    /****** 会员信息 操作按钮 ******/
    @ViewById(R.id.member_info_layout)
    LinearLayout mMemberLayout;

    @ViewById(R.id.customer_member_quan)
    protected TextView memberQuan;

    @ViewById(R.id.customer_member_quan_title)
    protected TextView memberQuanTitle;

    @ViewById(R.id.customer_member_pwd_title)
    protected TextView memberPwdTitle;

    @ViewById(R.id.customer_quan_img)
    ImageView memberQuanImg;

    @ViewById(R.id.customer_member_pwd_img)
    ImageView memberPwdImg;

    @ViewById(R.id.customer_card_time_title)
    TextView memberSendCoupon;

    @ViewById(R.id.customer_send_quan_img)
    ImageView memberSendCouponImg;

    @ViewById(R.id.customer_member_yue)
    protected TextView memberYue;

    @ViewById(R.id.customer_member_jifen)
    protected TextView memberJiFen;

    @ViewById(R.id.customer_member_yue_title)
    protected TextView memberYueTitle;

    @ViewById(R.id.customer_member_jifen_title)
    protected TextView memberJiFenTitle;

    @ViewById(R.id.customer_yue_img)
    ImageView memberYueImg;

    @ViewById(R.id.customer_jifen_img)
    ImageView memberJiFenImg;

    /****** 会员信息 （描述） ******/
    @ViewById(R.id.group_layout)
    LinearLayout mGroupLayout;

    @ViewById(R.id.group)
    protected TextView mGroup;

    @ViewById(R.id.birthday_layout)
    LinearLayout mBirthLayout;

    @ViewById(R.id.birthday)
    protected TextView mBirthday;

    @ViewById(R.id.comment_layout)
    LinearLayout mCommentLayout;

    @ViewById(R.id.comment)
    protected TextView mComment;

    //@ViewById(R.id.no_customer_info_layout)
    //RelativeLayout noCustomerInfo;
    /*********************** 会员基本信息（完） **********************/


    /*********************** 实体卡信息 **********************/
    @ViewById(R.id.detail_member_card_inf)
    LinearLayout cardDetailLl;

    @ViewById(R.id.card_no)
    protected TextView mCardNoTv;

    @ViewById(R.id.card_no_layout)
    protected LinearLayout cardNoLayout;

    /**
     * 实体卡余额
     */
    @ViewById(R.id.card_money)
    protected TextView mCardMoneyTv;

    @ViewById(R.id.card_money_layout)
    protected LinearLayout cardMoneLayout;

    /**
     * 实体卡积分
     */
    @ViewById(R.id.card_point)
    protected TextView mCardPoint;

    @ViewById(R.id.card_point_layout)
    protected LinearLayout cardPointLayout;

    @ViewById(R.id.customer_recharge)
    protected DrawableLeftCenterTextView mRecharge; // 账户充值

    @ViewById(R.id.create_order)
    protected DrawableLeftCenterTextView mCreateOrder; // 创建订单

    @ViewById(R.id.tv_kind_cardkind)
    protected TextView mCardKind; // 卡别 ， 次卡年卡月卡等

    @ViewById(R.id.tv_kind_cardkind_time)
    protected TextView mCardKindTime; // 有效期

    @ViewById(R.id.ll_project)
    protected LinearLayout mCardProjectLl; // 项目列表

    @ViewById(R.id.rv_project_list)
    protected RecyclerView mCardProjectList; // 项目列表

    @ViewById(R.id.rl_last_consume)
    protected RelativeLayout mCardLastConsumeRl; // 最后消费根节点

    @ViewById(R.id.rl_last_consume_root)
    protected RelativeLayout mCardLastConsumeRoot; // 最后消费根节点

    @ViewById(R.id.tv_last_consume_project)
    protected TextView mCardLastConsumeProject; // 最后消费项目  yyyy-MM-dd 项目

    @ViewById(R.id.rl_last_consume_remark_root)
    protected RelativeLayout mCardLastConsumeRemarkRoot; // 最后消费项目备注根节点

    @ViewById(R.id.tv_last_consume_remark)
    protected TextView mCardLastConsumeRemark; // 最后消费备注

    @ViewById(R.id.tv_last_consume_remark_more)
    protected TextView mCardLastConsumeRemarkMore; // 最后消费项目备注更多
    /************************* 实体卡信息 （完） ************************/

    /**
     * 当前实体卡
     */
    private EcCardInfo ecCard = null;

    private String face_id;

    private String people_id;

    /**
     * 跳转充值界面需要的参数 0充值记录 1积分记录 2优惠券记录
     */
    private int checkType = -1;
    private CustomerOperates mCustomerOperates;

    private String mFaceCode;

    private CustomerFragmentReplaceListener replaceListener;

    /**
     * 当前会员下所有实体卡列表
     */
    private List<EcCardInfo> dataList = new ArrayList<EcCardInfo>();

    private int mFrom = BeautyCustomerConstants.CustomerDetailFrom.OTHER;

    /**
     * 会员卡项目内容
     */
    private List<BeautyCardServiceAccount> mCardProjects = new ArrayList<>();
    private BeautyCustomerDetailProjectAdapter mCardProjectAdapter;

    public void setReplaceListener(CustomerFragmentReplaceListener replaceListener) {
        this.replaceListener = replaceListener;
    }

    @Override
    public void onAttach(Activity activity) {
        mCustomerOperates = OperatesFactory.create(CustomerOperates.class);
        super.onAttach(activity);
    }

    public enum ChargingType {
        LIST, RECHARGE
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        getLoaderManager().destroyLoader(0);
        EventBus.getDefault().post(new EventShowEditIcon(false));
        EventBus.getDefault().unregister(this);
        mCustomerOperates.cancelGetCustomerById();
        mCustomerOperates.cancelGetMemberCards();
        super.onDestroyView();
    }

    @AfterViews
    protected void initView() {
//        CheckAPI.init(MainApplication.getInstance());
        EventBus.getDefault().register(this);
        setupProjectRecyclerView();
//        initPopWindow();
        initBeautyDialog();
    }

    private void initBeautyDialog() {
        if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY && getArguments() != null) {
            Long customerId = getArguments().getLong(BeautyCustomerConstants.KEY_CUSTOMER_ID);
            if (customerId != null) {
                queryCustomerById(getArguments().getLong(BeautyCustomerConstants.KEY_CUSTOMER_ID));
            }
            mFrom = getArguments().getInt(BeautyCustomerConstants.KEY_CUSTOMER_PAGE_FROM, BeautyCustomerConstants.CustomerDetailFrom.OTHER);
            if (mFrom == BeautyCustomerConstants.CustomerDetailFrom.DIALOG_TO_DETAIL) {
                mEmptyLayout.setBackgroundResource(R.drawable.beauty_customer_detail_dialog_detail_bg);
                mainLayout.setBackgroundResource(R.drawable.beauty_customer_detail_dialog_detail_bg);
                mEmptyHint.setText("正在查询顾客信息请稍后...");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCustomer != null) {
            EventBus.getDefault().post(new EventShowEditIcon(true));
        }
    }

    /**
     * 重置数据
     *
     * @param customer
     */
    public void reSetData(CustomerResp customer) {
        this.mCustomer = customer;
        initData();
    }

    /**
     * 填充界面
     */
    private void initData() {
        initUI();
        initTitleInfo();
        initBaseInfo();
        initMemberInfo();
        initSwitchTitle();
    }

    private void initSwitchTitle() {
        if (mCustomer.cardList == null || mCustomer.cardList.size() == 0) {
            memberTitleCardNum.setVisibility(View.GONE);
        } else {
            memberTitleCardNum.setVisibility(View.VISIBLE);
            memberTitleCardNum.setText(mCustomer.cardList.size());
        }
    }

    /*初始化UI*/
    private void initUI() {
        // 选中第一个
        switchTitleSelectStatus(0);
        //initMemberUI();
        // 标题栏是否显示升级、绑定实体卡和实体卡标签
        if (mCustomer.isMember()) {
            if (TextUtils.isEmpty(mCustomer.mobile)) {
                /*if (mCustomer.getLoginType() == CustomerListResp.LoginType.CUSTOMER_ENTITY_CARD) {
                    initAnonymityCardInfoUI();
                } else {*/
                initWechatMemberInfoUI();
                //}
            } else {
                initMemberUI();
            }
        } else {
            initMemberNotInfoUI();
        }
    }

    /**
     * 组装卡项目的RecyclerView
     */
    private void setupProjectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        mCardProjectList.setHasFixedSize(true);
        mCardProjectList.setLayoutManager(linearLayoutManager);
        mCardProjectAdapter = new BeautyCustomerDetailProjectAdapter(getActivity(), mCardProjects);
        mCardProjectList.setAdapter(mCardProjectAdapter);
    }

    /**
     * 非会员展示
     */
    private void initMemberNotInfoUI() {
        disableMemberView();
        if (TextUtils.isEmpty(mCustomer.mobile)) {
            mLevelUpLayout.setVisibility(View.GONE);
            mCallLayout.setVisibility(View.GONE);
            mEditLayout.setVisibility(View.GONE);
        } else {
            mLevelUpLayout.setVisibility(View.GONE);
            mCallLayout.setVisibility(View.GONE);
            mEditLayout.setVisibility(View.VISIBLE);
        }
        mBindLayout.setVisibility(View.GONE);
        mRecharge.setVisibility(View.GONE);
        mCreateOrder.setVisibility(View.GONE);
        mTitleLayout.setVisibility(View.GONE);
        mTakePhotoLayout.setVisibility(View.GONE);
    }

    /**
     * 微信会员
     */
    private void initWechatMemberInfoUI() {
        faceBtnChooseUI();
        showMemberView();
        initDisableMemberUI();
        mBindLayout.setVisibility(View.GONE);
        mLevelUpLayout.setVisibility(View.GONE);
        mCallLayout.setVisibility(View.GONE);
        mEditLayout.setVisibility(View.GONE);
        mRecharge.setVisibility(View.GONE);
        mCreateOrder.setVisibility(View.GONE);
        mTitleLayout.setVisibility(View.GONE);
        /*memberYueTitle.setTextColor(getResources().getColor(R.color.member_disable_text_color));
        memberYue.setTextColor(getResources().getColor(R.color.member_disable_text_color));
        memberYueImg.setImageResource(R.drawable.customer_info_item_img_un);*/
        memberPwdTitle.setTextColor(getResources().getColor(R.color.member_disable_text_color));
        memberPwdImg.setImageResource(R.drawable.customer_info_item_img_un);
        mTakePhotoLayout.setVisibility(View.GONE);
    }


    /**
     * 匿名卡会员
     */
    private void initAnonymityCardInfoUI() {
        faceBtnChooseUI();
        showMemberView();
        initDisableMemberUI();
        mBindLayout.setVisibility(View.GONE);
        mLevelUpLayout.setVisibility(View.GONE);
        mCallLayout.setVisibility(View.GONE);
        mEditLayout.setVisibility(View.GONE);
        mRecharge.setVisibility(View.GONE);
        mCreateOrder.setVisibility(View.GONE);
        mCreateOrder.setBackgroundResource(R.drawable.customer_create_order_btn2);
        mTitleLayout.setVisibility(View.GONE);
        memberPwdTitle.setTextColor(getResources().getColor(R.color.member_disable_text_color));
        memberPwdImg.setImageResource(R.drawable.customer_info_item_img_un);
        mTakePhotoLayout.setVisibility(View.GONE);
        if (mCustomer.cardList != null && mCustomer.cardList.size() > 0) {
            memberTitleCardNum.setVisibility(View.VISIBLE);
            memberTitleCardNum.setText(mCustomer.cardList.size());
        } else {
            memberTitleCardNum.setVisibility(View.GONE);
        }
    }

    /**
     * 人脸按钮切换
     */
    private void faceBtnChooseUI() {
        if (!mCustomer.hasFaceCode()) {
            mTvTakePhoto.setText(getString(R.string.customer_face_collected));
        } else {
            mTvTakePhoto.setText(getString(R.string.customer_face_update));
        }
    }

    /**
     * 会员展示
     */
    private void initMemberUI() {
        if (mCustomer.hasFaceCode != null) {
            faceBtnChooseUI();
        }
        showMemberView();
        initDisableMemberUI();
        if (isHaveEntityCard(mCustomer)) {
            mTitleLayout.setVisibility(View.GONE);
        } else {
            mTitleLayout.setVisibility(View.GONE);
        }
        mLevelUpLayout.setVisibility(View.GONE);
        mCallLayout.setVisibility(View.GONE);
        mEditLayout.setVisibility(View.VISIBLE);
        mRecharge.setVisibility(View.VISIBLE);
        mTakePhotoLayout.setVisibility(View.GONE);
    }

    /**
     * 会员view - 展示
     */
    private void showMemberView() {
        memberYueTitle.setTextColor(getResources().getColor(R.color.customer_text_black));
        memberYue.setTextColor(getResources().getColor(R.color.text_red_2));
        memberYueImg.setImageResource(R.drawable.customer_to_right);
        memberJiFenTitle.setTextColor(getResources().getColor(R.color.customer_text_black));
        memberJiFen.setTextColor(getResources().getColor(R.color.text_red_2));
        memberJiFenImg.setImageResource(R.drawable.customer_to_right);
        memberPwdTitle.setTextColor(getResources().getColor(R.color.customer_text_black));
        memberPwdImg.setImageResource(R.drawable.customer_to_right);
        memberSendCoupon.setTextColor(getResources().getColor(R.color.customer_text_black));
        memberSendCouponImg.setImageResource(R.drawable.customer_to_right);
    }

    /**
     * 会员view - 隐藏
     */
    private void disableMemberView() {
        memberYueTitle.setTextColor(getResources().getColor(R.color.member_disable_text_color));
        memberYue.setTextColor(getResources().getColor(R.color.member_disable_text_color));
        memberYueImg.setImageResource(R.drawable.customer_info_item_img_un);
        memberJiFenTitle.setTextColor(getResources().getColor(R.color.member_disable_text_color));
        memberJiFen.setTextColor(getResources().getColor(R.color.member_disable_text_color));
        memberJiFenImg.setImageResource(R.drawable.customer_info_item_img_un);
        memberPwdTitle.setTextColor(getResources().getColor(R.color.member_disable_text_color));
        memberPwdImg.setImageResource(R.drawable.customer_info_item_img_un);
        memberSendCoupon.setTextColor(getResources().getColor(R.color.member_disable_text_color));
        memberSendCouponImg.setImageResource(R.drawable.customer_info_item_img_un);
    }

    /**
     * 是否是冻结会员的展示
     */
    private void initDisableMemberUI() {
        if (mCustomer.isDisabled()) {
            memberPwdTitle.setTextColor(getResources().getColor(R.color.member_disable_text_color));
            memberPwdImg.setImageResource(R.drawable.customer_info_item_img_un);
            memberSendCoupon.setTextColor(getResources().getColor(R.color.member_disable_text_color));
            memberSendCouponImg.setImageResource(R.drawable.customer_info_item_img_un);
            mRecharge.setEnabled(false);
            mBindLayout.setVisibility(View.GONE);
        } else {
//            memberPwdTitle.setTextColor(getResources().getColor(R.color.customer_text_black));
//            memberPwdImg.setImageResource(R.drawable.customer_to_right);
//            memberSendCoupon.setTextColor(getResources().getColor(R.color.customer_text_black));
//            memberSendCouponImg.setImageResource(R.drawable.customer_to_right);
            mRecharge.setEnabled(true);
            mBindLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 初始话名称、电话 性别信息、等级
     */
    private void initTitleInfo() {
        face_id = TextUtils.isEmpty(mCustomer.faceCode) ? "" : mCustomer.faceCode;
        people_id = TextUtils.isEmpty(mCustomer.peopleId) ? "" : mCustomer.peopleId;
        String sex = "";
        Drawable man = getActivity().getResources().getDrawable(R.drawable.customer_man);
        Drawable woman = getActivity().getResources().getDrawable(R.drawable.cuntomer_woman);
        man.setBounds(0, 0, man.getMinimumWidth(), man.getMinimumHeight());
        woman.setBounds(0, 0, woman.getMinimumWidth(), woman.getMinimumHeight());
        if (mCustomer.sex != null && mCustomer.sex == 0) {
            sex = getString(R.string.customer_detail_sex_women);
            mName.setCompoundDrawables(woman, null, null, null);
        } else if (mCustomer.sex != null && mCustomer.sex == 1) {
            sex = getString(R.string.customer_detail_sex_man);
            mName.setCompoundDrawables(man, null, null, null);
        } else {
            mName.setCompoundDrawables(null, null, null, null);
        }
        //sex_key获取的值有可能为null
        if (TextUtils.isEmpty(mCustomer.customerName)) {
            mName.setText(getString(R.string.customer_no_name2));
        } else {
            if (TextUtils.isEmpty(sex)) {
                mName.setText(mCustomer.customerName);
            } else {
                mName.setText(String.format(sex, mCustomer.customerName));
            }
        }
        // 显示等级
        if (mCustomer.isMember()) {
            memberLevel.setVisibility(View.VISIBLE);
            memberLevel.setText(getString(R.string.customer_level) + mCustomer.levelName);
        } else {
            memberLevel.setVisibility(View.GONE);
        }
        // 显示手机号码：
        if (!TextUtils.isEmpty(mCustomer.mobile)) {
            mCallLayout.setVisibility(View.GONE);
            mMobile.setText(getString(R.string.custoemr_member_phone) + "：" + AppUtil.getTel(mCustomer.mobile));
        } else if (!TextUtils.isEmpty(mCustomer.tel)) {
            mCallLayout.setVisibility(View.GONE);
            mMobile.setText(getString(R.string.custoemr_member_phone) + "：" + AppUtil.getTel(mCustomer.tel));
        } else {
            mCallLayout.setVisibility(View.GONE);
            mMobile.setText(getString(R.string.custoemr_member_phone) + "：" + getString(R.string.customer_not_set));
        }
    }

    /**
     * 会员基本信息
     * <p>
     * 累计消费，优惠券，分组，生日  等
     */
    private void initBaseInfo() {
        // 分组
        if (!TextUtils.isEmpty(mCustomer.groupName)) {
            mGroupLayout.setVisibility(View.VISIBLE);
            mGroup.setText(mCustomer.groupName);
        } else {
            mGroupLayout.setVisibility(View.GONE);
        }
        // 生日
        if (!TextUtils.isEmpty(mCustomer.birthday)) {
            mBirthLayout.setVisibility(View.VISIBLE);
            mBirthday.setText(mCustomer.birthday);
        } else {
            mBirthLayout.setVisibility(View.GONE);
        }
        //	备注
        if (!TextUtils.isEmpty(mCustomer.memo)) {
            mCommentLayout.setVisibility(View.VISIBLE);
            mComment.setText(mCustomer.memo);
        } else {
            mCommentLayout.setVisibility(View.GONE);
        }
        /*if (TextUtils.isEmpty(mCustomer.entityCard) && TextUtils.isEmpty(mCustomer.groupName)
                && TextUtils.isEmpty(mCustomer.birthday) && TextUtils.isEmpty(mCustomer.interest)
                && TextUtils.isEmpty(mCustomer.address) && TextUtils.isEmpty(mCustomer.invoice)
                && TextUtils.isEmpty(mCustomer.invoiceTitle) && TextUtils.isEmpty(mCustomer.memo)) {
            noCustomerInfo.setVisibility(View.VISIBLE);
        } else {
            noCustomerInfo.setVisibility(View.GONE);
        }*/

        // FIXME 添加 会员累计消费 和 消费次数

    }

    /**
     * 初始化会员信息
     * memberYue	memberJiFen		memberQuan
     */
    private void initMemberInfo() {
        memberYue.setText(ShopInfoCfg.getInstance().getCurrencySymbol() + (mCustomer.remainValue == null ? "0" : mCustomer.remainValue + ""));
        if (mCustomer.integral == null) {
            memberJiFen.setText(String.format(getString(R.string.customer_cent_str), 0 + ""));
        } else {
            memberJiFen.setText(String.format(getString(R.string.customer_cent_str), mCustomer.integral + ""));
        }
        memberQuan.setText(String.format(getString(R.string.customer_zhang_str), mCustomer.coupCount + ""));
    }

    /**
     * 返回不扣是否有实体卡
     */
    boolean isHaveEntityCard(CustomerResp mCustomer) {
        List<CustomerCardItem> cardItems = mCustomer.cardList;
        if (mCustomer.cardCount == 1 && cardItems != null && cardItems.size() == 1) {
            return false;
        }
        if (mCustomer.cardCount == 1 && cardItems != null && cardItems.size() == 0) {
            return true;
        }
        if (mCustomer.cardCount >= 2) {
            return true;
        }
        return false;
    }


    /**
     * 切换标题选中状态
     *
     * @param status
     */
    private void switchTitleSelectStatus(int status) {
        memberTitleBase.setTextColor(getResources().getColor(R.color.beauty_color_666666));
        memberTitleCard.setTextColor(getResources().getColor(R.color.beauty_color_666666));
        memberTitleBaseSelected.setVisibility(View.INVISIBLE);
        memberTitleCardSelected.setVisibility(View.INVISIBLE);
        cardDetailLl.setVisibility(View.GONE);
        baseDetailLl.setVisibility(View.GONE);
        switch (status) {
            case 0: // 基本信息
                mCustomerTitleCardRoot.setClickable(true);
                mCustomerTitleBaseRoot.setClickable(false);
                showBaseInfoBtn();
                memberTitleBase.setTextColor(getResources().getColor(R.color.beauty_color_FFB729));
                memberTitleBaseSelected.setVisibility(View.VISIBLE);
                baseDetailLl.setVisibility(View.VISIBLE);
//                mRecharge.setText(getString(R.string.beauty_customer_detail_tab_baseinfo));
                break;
            case 2:
                mCustomerTitleCardRoot.setClickable(false);
                mCustomerTitleBaseRoot.setClickable(true);
                showCardInfoBtn();
                memberTitleCard.setTextColor(getResources().getColor(R.color.beauty_color_FFB729));
                memberTitleCardSelected.setVisibility(View.VISIBLE);
                cardDetailLl.setVisibility(View.VISIBLE);
//                mRecharge.setText(getString(R.string.beauty_customer_detail_tab_card));
                break;
            default:
                break;
        }
    }

    /**
     * 基本信息按钮
     */
    private void showBaseInfoBtn() {
        if (mCustomer.isDisabled()) {
            mRecharge.setEnabled(false);
        } else {
            mRecharge.setEnabled(true);
        }
        mCreateOrder.setVisibility(View.GONE);
        mCreateOrder.setBackgroundResource(R.drawable.beauty_customer_detail_pink_order_btn1);
    }

    /**
     * 实体卡按钮
     */
    private void showCardInfoBtn() {
        mRecharge.setEnabled(true);
        mCreateOrder.setVisibility(View.GONE);
        mRecharge.setBackgroundResource(R.drawable.beauty_customer_detail_pink_recharge_btn);
        mCreateOrder.setBackgroundResource(R.drawable.beauty_customer_detail_pink_order_btn);
    }

    /**
     * 加载实体卡信息
     *
     * @param cardNo 卡号
     */
    private void loadCardInfo(final String cardNo) {
        UserActionEvent.start(UserActionEvent.CUSTOMER_QUERY_EC_CARD);
        mCustomerOperates.cancelGetMemberCards();
        // 清除实体卡列表
        dataList.clear();
        // 清空界面卡信息
        clearCardInfo();
        String mobile = mCustomer.mobile;
        Long customerId = mCustomer.customerId;
        ResponseListener<MemberCardResp> listener = new EventResponseListener<MemberCardResp>(UserActionEvent.CUSTOMER_QUERY_EC_CARD) {

            @Override
            public void onResponse(ResponseObject<MemberCardResp> response) {
                try {
                    clearCardInfo();
                    if (ResponseObject.isOk(response)) {
                        List<EcCardInfo> cardList = response.getContent().getResult().getCardList();
                        if (cardList != null && cardList.size() > 0) {
                            dataList.addAll(cardList);
                            if (cardNo != null) {
                                for (EcCardInfo card : cardList) {
                                    if (cardNo.equals(card.getCardNum())) {
                                        setCardInfo(card);
                                        break;
                                    }
                                }
                            } else {
                                setCardInfo(cardList.get(0));
                            }
                            UserActionEvent.end(getEventName());
                        } else {
                            ToastUtil.showLongToast(getString(R.string.member_does_not_bind_entity_card));
                        }
                    } else {
                        ToastUtil.showShortToast(response.getMessage());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                try {
                    ToastUtil.showShortToast(error.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

        };
        mCustomerOperates.getMemberCards(customerId, mobile, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    /**
     * 清空卡信息
     */
    private void clearCardInfo() {
        ecCard = null;
        mCardNoTv.setText("-");
        mCardKind.setText("-");
        mCardMoneyTv.setText("-");
        mCardPoint.setText("-");
    }

    /**
     * 刷新实体卡信息界面
     */
    private void setCardInfo(EcCardInfo card) {
        ecCard = card;
        if (CardStatus.getStatuS(ecCard.getCardStatus()).equals(getString(R.string.eccard_disabled)) || CardStatus.getStatuS(ecCard.getCardStatus()).equals(getString(R.string.eccard_invalided)) || mCustomer.isDisabled()) {
            mRecharge.setEnabled(false);
        } else {
            mRecharge.setEnabled(true);
        }
        mCardNoTv.setText(card.getCardNum());
        mCardKind.setText(card.getCardKindName());
        if (card.getCardServiceAccountList() == null || card.getCardServiceAccountList().size() == 0) {
            mCardProjects.clear();
            mCardProjectAdapter.notifyDataSetChanged();
            mCardProjectLl.setVisibility(View.GONE);
        } else {
            mCardProjectLl.setVisibility(View.VISIBLE);
            mCardProjects.clear();
            mCardProjects.addAll(card.getCardServiceAccountList());
            mCardProjectAdapter.notifyDataSetChanged();
        }
        if (card.getCardServiceHistoryDto() == null) {
            mCardLastConsumeRl.setVisibility(View.GONE);
        } else {
            mCardLastConsumeRl.setVisibility(View.VISIBLE);
            mCardLastConsumeProject.setText(card.getCardServiceHistoryDto().serviceName);
            mCardLastConsumeRemark.setText(card.getCardServiceHistoryDto().remark);
        }
        String money = card.getRemainValue() != null ? card.getRemainValue().toPlainString() : "0";
        mCardMoneyTv.setText(ShopInfoCfg.getInstance().getCurrencySymbol() + money);
        mCardPoint.setText(String.format(getString(R.string.customer_cent_str), card.getIntegral() != null ? card.getIntegral().toPlainString() : "0"));
    }


    /**
     * 显示实体卡列表弹窗
     */
    private void showCardListDialog() {
        /*if (dataList != null && dataList.size() > 0) {
            MemberCardListDialog dialog = new MemberCardListDialog(this.getActivity()) {
                @Override
                public void itemSelectd(EcCardInfo card) {
                    super.itemSelectd(card);
                    setCardInfo(card);
                }

            };
            dialog.show();
            dialog.setData(dataList);
        }*/
    }

    /**
     * 密码输入框
     *
     * @param inputNo
     * @param customerId
     */
    private void showPasswordDialog(final String mobile, final String inputNo, final Long customerId) {
        CustomerLogin.showMemberPasswordDialog(getActivity(), inputNo, new CustomerLogin.DinnerLoginListener() {
            @Override
            public void login(PasswordDialog dialog, int needPswd, String password) {
                doVerifypassword(dialog, mobile, customerId + "", password);
            }
        });
    }

    /**
     * 验证支付密码
     *
     * @param dialog
     * @param mobile
     * @param customerId
     * @param password
     */
    private void doVerifypassword(final PasswordDialog dialog, String mobile, String customerId, final String password) {
        if (customerId != null) {
            CustomerLoginReq loginReq = new CustomerLoginReq();
            loginReq.setLoginId(customerId);
            //loginReq.setMobile(mobile);
            loginReq.setPassword(password);
            CustomerOperates customerOperate = OperatesFactory.create(CustomerOperates.class);
            customerOperate.login(loginReq, LoadingResponseListener.ensure(new ResponseListener<CustomerLoginResp>() {

                @Override
                public void onResponse(ResponseObject<CustomerLoginResp> response) {
                    if (ResponseObject.isOk(response)) {
                        CustomerLoginResp resp = response.getContent();
                        if (resp.customerIsDisable()) {
                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                        } else {
                            inputFace();
                        }
                        dialog.dismiss();
                    } else {
                        ToastUtil.showShortToast(response.getMessage());
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    ToastUtil.showShortToast(getString(R.string.toast_password_verify_error));
                }

            }, getFragmentManager()));
        } else {
            ToastUtil.showShortToast(R.string.pay_member_login_please);
        }
    }

    /**
     * 录入人脸
     */
    private void inputFace() {
        /*if (mCustomer == null) {
            return;
        }
        boolean available = BaiduFaceRecognition.getInstance().checkFaceServer();
        if (!available) {
            FacecognitionActivity.showFaceServerWarmDialog(getContext(), getChildFragmentManager());
            return;
        }
        startActivityForResult(BaiduFaceRecognition.getInstance().getRegistFaceIntent(), FaceRequestCodeConstant.RC_CUSTOMER_DETAIL_BIND);*/
    }

    /**
     * 绑定人脸
     *
     * @param faceCode
     */
    private void bindFace(String faceCode) {
        if (mCustomer == null) {
            return;
        }
        mFaceCode = faceCode;
        saveCustomerFaceCode(mCustomer.customerId, mFaceCode);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FaceRequestCodeConstant.RC_CUSTOMER_DETAIL_BIND && resultCode == Activity.RESULT_OK) {
            Log.e("CustomerDetail", "request code = " + requestCode + " : resultCode = " + resultCode + " : data = " + data.getStringExtra(BaiduFaceRecognition.KEY_FACE_CODE));
            bindFace(data.getStringExtra(BaiduFaceRecognition.KEY_FACE_CODE));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    /**
     * 会员卡储值check权限
     */
    private void checkPermiss(ChargingType type) {
        if (type == ChargingType.LIST) {
            showCharingBalance(true);
        } else {
            showreChargeDialog(true);
        }
    }

    /**
     * 保存人脸
     *
     * @param customerId
     * @param faceId
     */
    public void saveCustomerFaceCode(Long customerId, final String faceId) {
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        operates.bindCustomerFaceCode(customerId, faceId, new ResponseListener<BindCustomerFaceCodeResp>() {
            @Override
            public void onResponse(ResponseObject<BindCustomerFaceCodeResp> response) {
                if (ResponseObject.isOk(response)) {
                    if (response.getContent() != null) {
                        if (response.getContent().isOk()) {
                            if (response.getContent().getResult() != null && response.getContent().getResult()) {
                                mCustomer.hasFaceCode = 1;
                            }
                            mCustomer.faceCode = faceId;
                            ToastUtil.showShortToast(getString(R.string.customer_bind_face_success));
                            EventBus.getDefault().post(new EventCreateOrEditCustomer(CustomerActivity.CUSTOMER, mCustomer.getCustomerListBean()));
                            faceBtnChooseUI();
                        } else {
                            ToastUtil.showLongToast(response.getContent().getErrorMessage());
                        }
                    } else {
                        ToastUtil.showLongToast(response.getMessage());
                    }
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        });
    }

    /**
     * 跳转充值支付界面
     *
     * @param isMember 会员跳转
     */
    private void showCharingBalance(boolean isMember) {
        if (isMember) {// 会员跳转
            Bundle args = new Bundle();
            args.putInt("type", checkType);
            args.putInt("whereFrom", CustomerChargingDialogFragment.FROM_MEMBER_CUSTOMER);//来自顾客界面
            args.putString("customerId", String.valueOf(mCustomer.customerId));
            args.putString("balance", mCustomer.remainValue + "");
            args.putString("integral", mCustomer.integral + "");
            args.putSerializable(BeautyCustomerConstants.KEY_CUSTOMER, mCustomer);
            Intent intent = new Intent();
            intent.setClass(getActivity(), BeautyCustomerChargingBalanceActivity_.class);
            intent.putExtras(args);
            startActivity(intent);
        } else {// 实体卡跳转
            if (ecCard != null) {
                /*Bundle args = new Bundle();
                args.putInt("type", checkType);
                args.putInt("whereFrom", CustomerChargingDialogFragment.FROM_MEMBER_CUSTOMER);//来自顾客界面
                args.putSerializable(CustomerChargingBalanceActivity.KEY_CUSTOMER, mCustomer);
                args.putSerializable("ecCard", ecCard);
                args.putString("balance", ecCard.getRemainValue() != null ? ecCard.getRemainValue().toPlainString() : "0");
                args.putString("integral", ecCard.getIntegral() != null ? ecCard.getIntegral().toString() : "0");
                Intent intent = new Intent();
                intent.setClass(getActivity(), BeautyCustomerChargingBalanceActivity_.class);
                intent.putExtras(args);
                startActivity(intent);*/
            } else {
                ToastUtil.showLongToast(R.string.customer_get_card_info_fail);
            }
        }
    }

    /**
     * 跳转充值支付界面
     *
     * @param isMember 会员跳转
     */
    private void showreChargeDialog(boolean isMember) {
        if (isMember) {// 会员跳转
            showChargingDialog(mCustomer, null, mCustomer.remainValue + "");
        } else {// 实体卡跳转
            if (ecCard != null) {
                showChargingDialog(mCustomer, ecCard, null);
            } else {
                ToastUtil.showLongToast(R.string.customer_get_card_info_fail);
            }
        }
    }

    /**
     * 实体卡会员充值界面
     *
     * @param customer 顾客信息
     * @param ecCard   实体卡信息 , 会员充值 传 null
     * @param balance  余额，实体卡充值 传 null
     */
    private void showChargingDialog(CustomerResp customer, EcCardInfo ecCard, String balance) {
        CustomerChargingDialogFragment dialogFragment = new CustomerChargingDialogFragment_();
        Bundle args = new Bundle();
        args.putInt(CustomerChargingDialogFragment.KEY_FROM, CustomerChargingDialogFragment.FROM_MEMBER_CUSTOMER);//来自顾客界面
        args.putSerializable(CustomerChargingDialogFragment.KEY_CUSTOMER, customer);
        if (ecCard != null) {
            args.putSerializable(CustomerChargingDialogFragment.KEY_ECCARD, ecCard);
            args.putString(CustomerChargingDialogFragment.KEY_BALANCE, ecCard.getRemainValue() != null ? ecCard.getRemainValue().toPlainString() : "0");
        } else {
            args.putString(CustomerChargingDialogFragment.KEY_BALANCE, balance);
        }
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "ecCardCharging");
    }
//
//    /**
//     * 初始化 订单 popu
//     */
//    private void initPopWindow() {
//        popWindow = new CreateOrderPopWindow(getActivity(), new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                popWindow.dismiss();
//                switch (arg2) {
//                    case 0:// 创建预订
//                        UserActionEvent.eventCount(UserActionCode.GK010026);
//                        toBookPage();
//                        break;
//                }
//            }
//        });
//    }

    /**
     * 跳转到预定页面
     */
    private void toBookPage() {
        /*if (ClickManager.getInstance().isClicked(R.id.btn_booking)) {
            return;
        }*/
        /*VerifyHelper.verifyAlert(getActivity(), BookingApplication.PERMISSION_BOOKING,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        Intent intent = new Intent(getActivity(), BookingCreateActivity_.class);
                        intent.putExtra("orderSource", QueueOrderSource.DaoDian.value());
                        startActivity(bookingSendValue(intent));
                    }
                });*/
    }

    /**
     * 组装预定数据
     *
     * @param intent
     * @return
     */
    private Intent bookingSendValue(Intent intent) {
        intent.putExtra("type", "2");
        if (null != mCustomer) {
            String no = "";
            if (TextUtils.isEmpty(mCustomer.mobile)) {
                if (!TextUtils.isEmpty(mCustomer.tel)) {
                    no = mCustomer.tel;
                }
            } else {
                no = mCustomer.mobile;
            }
            intent.putExtra("phoneNO", no);
            intent.putExtra("name", mCustomer.customerName);
            if (mCustomer.sex != null) {
                try {
                    int sex = mCustomer.sex;
                    intent.putExtra("sex", sex);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        return intent;
    }


    /**
     * 编辑顾客
     */
    private void showEditUser() {
        BeautyCustomerEditActivity_.IntentBuilder_ senderOrder = BeautyCustomerEditActivity_.intent(getActivity());
        senderOrder.get().putExtra(CustomerContants.KEY_CUSTOMER_EDIT, mCustomer);
        senderOrder.get().putExtra("type", CustomerActivity.PARAM_EDIT);
        if (mFrom == BeautyCustomerConstants.CustomerDetailFrom.DIALOG_TO_DETAIL) {
            senderOrder.get().putExtra(BeautyCustomerConstants.KEY_CUSTOMER_EDIT_PAGE, BeautyCustomerConstants.CustomerEditPage.DETAIL_DIALOG);
        }
        senderOrder.start();
    }


    /**
     * 判断固话或蓝牙电话的开关是否开启,两个都关闭表示未开启
     */
    private boolean checkPhoneSetting() {
        if (!SpHelper.getDefault().getBoolean(SpHelper.SP_FIXEDPHONE_SERVICE_SWITCH, true)
                && !SpHelper.getDefault().getBoolean(SpHelper.SP_BLUETOOTH_SERVICE_SWITCH, false)) {
            return false;
        }
        return true;
    }


    /**
     * 查询会员
     *
     * @param customerId
     */
    private void queryCustomerById(Long customerId) {
        UserActionEvent.start(UserActionEvent.CUSTOMER_QUERY_DATA_BY_CUSTOMER_ID);
        mCustomerOperates.cancelGetCustomerById();
        showLoadingProgressDialog();
        YFResponseListener<YFResponse<CustomerResp>> responseListener = new YFResponseListener<YFResponse<CustomerResp>>() {
            @Override
            public void onResponse(YFResponse<CustomerResp> response) {
                try {
                    if (YFResponse.isOk(response)) {
                        CustomerResp customerNew = response.getContent();
                        if (customerNew != null) {
                            reSetData(customerNew);
                            mainLayout.setVisibility(View.VISIBLE);
                            mEmptyLayout.setVisibility(View.INVISIBLE);
                        } else {
                            mainLayout.setVisibility(View.INVISIBLE);
                            mEmptyLayout.setVisibility(View.VISIBLE);
                            mEmptyHint.setText(getString(R.string.no_customer));
                            ToastUtil.showLongToast(response.getMessage());
                        }
                    } else {
                        mainLayout.setVisibility(View.INVISIBLE);
                        mEmptyLayout.setVisibility(View.VISIBLE);
                        mEmptyHint.setText(getString(R.string.request_error));
                        ToastUtil.showLongToast(getString(R.string.connect_server_error) + "!");
                    }
                    dismissLoadingProgressDialog();
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                try {
                    mainLayout.setVisibility(View.INVISIBLE);
                    mEmptyLayout.setVisibility(View.VISIBLE);
                    mEmptyHint.setText(getString(R.string.request_error));
                    ToastUtil.showLongToast(error.getMessage());
                    Log.e("queryCustomerById", error.getMessage());
                    dismissLoadingProgressDialog();
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        };
        mCustomerOperates.getCustomerById(customerId, false, responseListener);
    }

    /************************************************* EventBus *******************************************************/
    /**
     * 刷新账户余额
     */
    public void onEventMainThread(EventRefreshBalance event) {
        if (event.cardNo != null) {
            loadCardInfo(event.cardNo);
        } else {
            queryCustomerById(mCustomer.customerId);
        }
    }

    /**
     * 刷新顾客
     */
    public void onEventMainThread(DetailRefreshEvent event) {
        if (event.customer != null) {
            queryCustomerById(mCustomer.customerId);
        }
    }

    public void onEventMainThread(EventRefreshDetail event) {
        queryCustomerById(event.customerId);
        ecCard = null;
    }

    public void onEventMainThread(EventCreateOrEditCustomer event) {
        if (event.type == CustomerActivity.PARAM_EDIT) {
            if (mCustomer != null) {
                queryCustomerById(mCustomer.customerId);
            }
        }
    }

    /**
     * 消费记录
     */
    private void showConsumeRecord() {
        if (ecCard == null) {
            return;
        }
        BeautyConsumeProjectDialog dialog = new BeautyConsumeProjectDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BeautyConsumeProjectDialog.Companion.getKEY_CARD_INFO(), ecCard);
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(), "BeautyConsumeProjectDialog");
    }

    /************************************************* 会员详情点击事件 *******************************************************/
    /**
     * 基础会员事件
     *
     * @param v
     */
    @Click({R.id.customer_takephoto, R.id.customer_bindcard, R.id.level_up, R.id.call, R.id.customer_edit})
    public void onClickTitleItem(View v) {
        // 防双击
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.customer_bindcard: // 绑卡
                /*MobclickAgentEvent.onEvent(UserActionCode.GK010005);
                if (mCustomer != null) {
                    VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_CARD_ENABLE,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    Intent intent = new Intent(getActivity(), CumtomerSaleCardsActivity_.class);
                                    intent.putExtra("customer", mCustomer);
                                    intent.putExtra("customer_flag", CustomerContants.FLAG_CUSTOMER_BAND);
                                    startActivity(intent);
                                }
                            });
                }*/
                break;
            case R.id.level_up: // 升级
                MobclickAgentEvent.onEvent(UserActionCode.GK010006);
                VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_UPDATE,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                CustomerLevelUpDialogFragment.show(mCustomer, getFragmentManager());
                            }
                        });
                break;
            case R.id.call: // 蓝牙电话
                MobclickAgentEvent.onEvent(UserActionCode.GK010007);
                if (!checkPhoneSetting()) {
                    ToastUtil.showShortToast(getString(R.string.check_phone));
                    return;
                }
                /*if (PhoneCallingActivity.isLuncher || CallingService.isLuncher) {
                    ToastUtil.showLongToast(getString(R.string.tel_is_busy2));
                    return;
                }*/
                VerifyHelper.verifyAlert(getActivity(), PhoneApplication.PERMISSION_PHONE,
                        new VerifyHelper.Callback() {

                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                try {
                                    if (CalmPhoneManager.getDefault().isConnectSuccess()) {
                                        CalmPhoneManager.getDefault().getCalmPhone().dial(mCustomer.mobile);
                                    } else {
                                        ToastUtil.showShortToast(getString(R.string.check_phone));
                                    }

                                } catch (CalmPhoneStateException e) {
                                    Log.e(TAG, "", e);
                                } catch (IllegalArgumentException e) {
                                    Log.e(TAG, "", e);
                                } catch (CalmPhoneException e) {
                                    Log.e(TAG, "", e);
                                }
                            }
                        });
                break;
            case R.id.customer_edit: // 会员编辑
                MobclickAgentEvent.onEvent(UserActionCode.GK010008);
                if (mCustomer != null) {
                    VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_EDIT,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    showEditUser();
                                }
                            });
                }
                break;
            case R.id.customer_takephoto: // 人脸
                MobclickAgentEvent.onEvent(UserActionCode.GK010004);
                if (!TextUtils.isEmpty(mCustomer.mobile)) {
                    showPasswordDialog(mCustomer.mobile, mCustomer.customerName, mCustomer.customerId);
                }
                break;
        }
    }

    /**
     * 标题切换
     *
     * @param v
     */
    @Click({R.id.customer_menber_title_base_root, R.id.customer_menber_title_card_root})
    public void onClickSwitch(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.customer_menber_title_base_root: // 会员基本信息
                MobclickAgentEvent.onEvent(UserActionCode.GK010009);
                switchTitleSelectStatus(0);
                break;
            case R.id.customer_menber_title_card_root: // 实体卡信息
                MobclickAgentEvent.onEvent(UserActionCode.GK010010);
                // 实体卡界面
                switchTitleSelectStatus(2);
                if (ecCard != null) {
                    setCardInfo(ecCard);
                } else {
                    loadCardInfo(null);
                }
                break;
        }
    }

    /**
     * 账户充值 ， 创建订单
     *
     * @param v
     */
    @Click({R.id.customer_recharge, R.id.create_order})
    public void
    onClickBaseButton(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) { // 账户充值
            case R.id.customer_recharge:
                if (memberTitleBaseSelected.getVisibility() == View.VISIBLE) {
                    MobclickAgentEvent.onEvent(UserActionCode.GK010015);
                    checkType = CustomerContants.TYPE_BALANCE;
                    if (mCustomer.isDisabled()) {
                        ToastUtil.showLongToast(getString(R.string.member_disable));
                    } else {
                        checkPermiss(ChargingType.RECHARGE);
                    }
                } else {
                    checkType = CustomerContants.TYPE_BALANCE;
                    if (mCustomer.isDisabled()) {
                        MobclickAgentEvent.onEvent(UserActionCode.GK010019);
                        ToastUtil.showLongToast(getString(R.string.member_disable));
                    } else if (ecCard != null && ecCard.getCardStatus() != CardStatus.ACTIVATED) {
                        ToastUtil.showShortToast(getString(R.string.entity_card) + CardStatus.getStatuS(ecCard.getCardStatus()) + getString(R.string.can_not_execute));
                    } else {
                        showreChargeDialog(false);
                    }
                }
                break;
            case R.id.create_order: // 开单
                // FIXME 开单跳转
//                UserActionEvent.eventCount(UserActionCode.GK010016);
//                if (!popWindow.isShowing()) {
//                    popWindow.setWidth(mCreateOrder.getWidth());
//                    popWindow.showAsDropDown(mCreateOrder, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -220, getResources().getDisplayMetrics()));
//                }
                break;
        }
    }


    /**
     * 会员基本信息 的点击事件 详情  消费明细的点击
     *
     * @param v
     */
    @Click({R.id.cus_jifen_ll, R.id.cus_yue_ll, R.id.cus_pwd_ll, R.id.cus_quan_ll,
            R.id.cus_card_time_ll, R.id.cus_wxapp_ll, R.id.cus_expense_ll})
    protected void onClickBaseInfo(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.cus_pwd_ll: // 密码
                MobclickAgentEvent.onEvent(UserActionCode.GK010014);
                if (!mCustomer.isMember()) {
                    ToastUtil.showShortToast(getString(R.string.customer_not_member));
                    return;
                }
                if (mCustomer.isDisabled()) {
                    ToastUtil.showShortToast(R.string.member_disable);
                    return;
                }
                if (TextUtils.isEmpty(mCustomer.mobile)) {
                    ToastUtil.showShortToast(R.string.customer_bind_pohone);
                    return;
                }

                Bundle args = new Bundle();
                args.putSerializable(CustomerActivity.PARAMKEY_CUSTOMER, mCustomer);
                new CustomerVerifyCodeDialog_().show(getFragmentManager(), args, "customerVerifyCode");
                break;
            case R.id.cus_quan_ll: // 优惠券
                MobclickAgentEvent.onEvent(UserActionCode.GK010013);
                checkType = CustomerContants.TYPE_COUPON;
                checkPermiss(ChargingType.LIST);
                break;
            case R.id.cus_card_time_ll: // 次卡
                checkType = CustomerContants.TYPE_CARD_TIME;
                checkPermiss(ChargingType.LIST);
                break;
            case R.id.cus_wxapp_ll:
                checkType = CustomerContants.TYPE_WX_APP;
                checkPermiss(ChargingType.LIST);
                break;
            case R.id.cus_expense_ll:
                checkType = CustomerContants.TYPE_EXPENSE;
                checkPermiss(ChargingType.LIST);
                break;
            case R.id.cus_jifen_ll:
                MobclickAgentEvent.onEvent(UserActionCode.GK010012);
                if (mCustomer.isMember()) {
                    checkType = CustomerContants.TYPE_INTEGRAL;
                    checkPermiss(ChargingType.LIST);
                } else {
                    ToastUtil.showShortToast(getString(R.string.customer_not_member));
                }
                break;
            case R.id.cus_yue_ll:
                MobclickAgentEvent.onEvent(UserActionCode.GK010011);
                if (mCustomer.isMember()) {
                    checkType = CustomerContants.TYPE_BALANCE;
                    checkPermiss(ChargingType.LIST);
                } else {
                    ToastUtil.showShortToast(getString(R.string.customer_not_member));
                }
                break;
        }
    }


    /**
     * 实体卡界面的点击事件
     *
     * @param v
     */
    @Click({R.id.card_no_layout, R.id.card_money_layout, R.id.card_point_layout, R.id.tv_last_consume_remark_more})
    public void onCardInfoClick(View v) {
        // 防双击
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.card_no_layout: // 实体卡切换
                showCardListDialog();
                break;
            case R.id.card_money_layout:// 实体卡余额充值
                MobclickAgentEvent.onEvent(UserActionCode.GK010017);
                checkType = CustomerContants.TYPE_BALANCE;
                showCharingBalance(false);
                break;
            case R.id.card_point_layout:// 实体卡积分
                MobclickAgentEvent.onEvent(UserActionCode.GK010018);
                checkType = CustomerContants.TYPE_INTEGRAL;
                showCharingBalance(false);
                break;
            case R.id.tv_last_consume_remark_more:// 备注更多
                showConsumeRecord();
                break;
            default:
                break;
        }
    }
}