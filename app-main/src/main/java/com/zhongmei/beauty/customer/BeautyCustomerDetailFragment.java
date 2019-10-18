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
import com.zhongmei.beauty.customer.dialog.BeautyCustomerBindCardDialog;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
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
import com.zhongmei.yunfu.context.util.Utils;
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




    @ViewById(R.id.detail_base)
    LinearLayout baseDetailLl;

    @ViewById(R.id.layout_card)
    LinearLayout layout_card;

    @ViewById(R.id.tv_card_no)
    TextView tv_cardNO;


    @ViewById(R.id.tv_total_consume_content)
    TextView mTvTotalConsumeContent;

    @ViewById(R.id.tv_total_consume_count_content)
    TextView mTvTotalConsumeCountContent;


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

    @ViewById(R.id.hobby_layout)
    LinearLayout mHobbyLayout;

    @ViewById(R.id.hobby)
    TextView mTvHobby;




    @ViewById(R.id.detail_member_card_inf)
    LinearLayout cardDetailLl;

    @ViewById(R.id.card_no)
    protected TextView mCardNoTv;

    @ViewById(R.id.card_no_layout)
    protected LinearLayout cardNoLayout;


    @ViewById(R.id.card_money)
    protected TextView mCardMoneyTv;

    @ViewById(R.id.card_money_layout)
    protected LinearLayout cardMoneLayout;


    @ViewById(R.id.card_point)
    protected TextView mCardPoint;

    @ViewById(R.id.card_point_layout)
    protected LinearLayout cardPointLayout;

    @ViewById(R.id.customer_recharge)
    protected DrawableLeftCenterTextView mRecharge;
    @ViewById(R.id.create_order)
    protected DrawableLeftCenterTextView mCreateOrder;
    @ViewById(R.id.tv_kind_cardkind)
    protected TextView mCardKind;
    @ViewById(R.id.tv_kind_cardkind_time)
    protected TextView mCardKindTime;
    @ViewById(R.id.ll_project)
    protected LinearLayout mCardProjectLl;
    @ViewById(R.id.rv_project_list)
    protected RecyclerView mCardProjectList;
    @ViewById(R.id.rl_last_consume)
    protected RelativeLayout mCardLastConsumeRl;
    @ViewById(R.id.rl_last_consume_root)
    protected RelativeLayout mCardLastConsumeRoot;
    @ViewById(R.id.tv_last_consume_project)
    protected TextView mCardLastConsumeProject;
    @ViewById(R.id.rl_last_consume_remark_root)
    protected RelativeLayout mCardLastConsumeRemarkRoot;
    @ViewById(R.id.tv_last_consume_remark)
    protected TextView mCardLastConsumeRemark;
    @ViewById(R.id.tv_last_consume_remark_more)
    protected TextView mCardLastConsumeRemarkMore;


    private EcCardInfo ecCard = null;

    private String face_id;

    private String people_id;


    private int checkType = -1;
    private CustomerOperates mCustomerOperates;

    private String mFaceCode;

    private CustomerFragmentReplaceListener replaceListener;


    private List<EcCardInfo> dataList = new ArrayList<EcCardInfo>();

    private int mFrom = BeautyCustomerConstants.CustomerDetailFrom.OTHER;


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
        EventBus.getDefault().register(this);
        setupProjectRecyclerView();
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


    public void reSetData(CustomerResp customer) {
        this.mCustomer = customer;
        initData();
    }


    private void initData() {
        initUI();
        initTitleInfo();
        initBaseInfo();
        initCardNo();
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


    private void initUI() {
                switchTitleSelectStatus(0);
                        if (mCustomer.isMember()) {
            if (TextUtils.isEmpty(mCustomer.mobile)) {

                initWechatMemberInfoUI();
                            } else {
                initMemberUI();
            }
        } else {
            initMemberNotInfoUI();
        }
    }


    private void setupProjectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        mCardProjectList.setHasFixedSize(true);
        mCardProjectList.setLayoutManager(linearLayoutManager);
        mCardProjectAdapter = new BeautyCustomerDetailProjectAdapter(getActivity(), mCardProjects);
        mCardProjectList.setAdapter(mCardProjectAdapter);
    }


    private void initMemberNotInfoUI() {
        disableMemberView();
        mBindLayout.setVisibility(View.VISIBLE);
        mRecharge.setVisibility(View.GONE);
        mCreateOrder.setVisibility(View.GONE);
        mTitleLayout.setVisibility(View.GONE);
        mTakePhotoLayout.setVisibility(View.GONE);
    }


    private void initWechatMemberInfoUI() {
        faceBtnChooseUI();
        showMemberView();
        initDisableMemberUI();
        mBindLayout.setVisibility(View.GONE);
        mLevelUpLayout.setVisibility(View.GONE);
        mCallLayout.setVisibility(View.GONE);
        mEditLayout.setVisibility(View.VISIBLE);         mRecharge.setVisibility(View.GONE);
        mCreateOrder.setVisibility(View.GONE);
        mTitleLayout.setVisibility(View.GONE);

        memberPwdTitle.setTextColor(getResources().getColor(R.color.member_disable_text_color));
        memberPwdImg.setImageResource(R.drawable.customer_info_item_img_un);
        mTakePhotoLayout.setVisibility(View.GONE);
    }



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


    private void faceBtnChooseUI() {
        if (!mCustomer.hasFaceCode()) {
            mTvTakePhoto.setText(getString(R.string.customer_face_collected));
        } else {
            mTvTakePhoto.setText(getString(R.string.customer_face_update));
        }
    }


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
        mBindLayout.setVisibility(View.VISIBLE);
    }


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


    private void initDisableMemberUI() {
        if (mCustomer.isDisabled()) {
            memberPwdTitle.setTextColor(getResources().getColor(R.color.member_disable_text_color));
            memberPwdImg.setImageResource(R.drawable.customer_info_item_img_un);
            memberSendCoupon.setTextColor(getResources().getColor(R.color.member_disable_text_color));
            memberSendCouponImg.setImageResource(R.drawable.customer_info_item_img_un);
            mRecharge.setEnabled(false);
            mBindLayout.setVisibility(View.GONE);
        } else {
            mRecharge.setEnabled(true);
            mBindLayout.setVisibility(View.GONE);
        }
    }


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
                if (TextUtils.isEmpty(mCustomer.customerName)) {
            mName.setText(getString(R.string.customer_no_name2));
        } else {
            if (TextUtils.isEmpty(sex)) {
                mName.setText(mCustomer.customerName);
            } else {
                mName.setText(String.format(sex, mCustomer.customerName));
            }
        }
                if (mCustomer.isMember()) {
            memberLevel.setVisibility(View.VISIBLE);
            memberLevel.setText(getString(R.string.customer_level) + mCustomer.levelName);
        } else {
            memberLevel.setVisibility(View.GONE);
        }
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


    private void initBaseInfo() {
                if (!TextUtils.isEmpty(mCustomer.groupName)) {
            mGroupLayout.setVisibility(View.VISIBLE);
            mGroup.setText(mCustomer.groupName);
        } else {
            mGroupLayout.setVisibility(View.GONE);
        }
                if (!TextUtils.isEmpty(mCustomer.birthday)) {
            mBirthLayout.setVisibility(View.VISIBLE);
            mBirthday.setText(mCustomer.birthday);
        } else {
            mBirthLayout.setVisibility(View.GONE);
        }
                if (!TextUtils.isEmpty(mCustomer.memo)) {
            mCommentLayout.setVisibility(View.VISIBLE);
            mComment.setText(mCustomer.memo);
        } else {
            mCommentLayout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mCustomer.hobby)) {
            mHobbyLayout.setVisibility(View.VISIBLE);
            mTvHobby.setText(mCustomer.hobby);
        } else {
            mHobbyLayout.setVisibility(View.GONE);
        }

    }


    private void initCardNo(){
        if(Utils.isEmpty(mCustomer.entityCards)){
            layout_card.setVisibility(View.GONE);
        }else{
            layout_card.setVisibility(View.VISIBLE);
            tv_cardNO.setText(mCustomer.getCardNos());
        }
    }



    private void initMemberInfo() {
        memberYue.setText(ShopInfoCfg.getInstance().getCurrencySymbol() + (mCustomer.remainValue == null ? "0" : mCustomer.remainValue + ""));
        if (mCustomer.integral == null) {
            memberJiFen.setText(String.format(getString(R.string.customer_cent_str), 0 + ""));
        } else {
            memberJiFen.setText(String.format(getString(R.string.customer_cent_str), mCustomer.integral + ""));
        }
        memberQuan.setText(String.format(getString(R.string.customer_zhang_str), mCustomer.coupCount + ""));
    }


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



    private void switchTitleSelectStatus(int status) {
        memberTitleBase.setTextColor(getResources().getColor(R.color.beauty_color_666666));
        memberTitleCard.setTextColor(getResources().getColor(R.color.beauty_color_666666));
        memberTitleBaseSelected.setVisibility(View.INVISIBLE);
        memberTitleCardSelected.setVisibility(View.INVISIBLE);
        cardDetailLl.setVisibility(View.GONE);
        baseDetailLl.setVisibility(View.GONE);
        switch (status) {
            case 0:                 mCustomerTitleCardRoot.setClickable(true);
                mCustomerTitleBaseRoot.setClickable(false);
                showBaseInfoBtn();
                memberTitleBase.setTextColor(getResources().getColor(R.color.beauty_color_FFB729));
                memberTitleBaseSelected.setVisibility(View.VISIBLE);
                baseDetailLl.setVisibility(View.VISIBLE);
                break;
            case 2:
                mCustomerTitleCardRoot.setClickable(false);
                mCustomerTitleBaseRoot.setClickable(true);
                showCardInfoBtn();
                memberTitleCard.setTextColor(getResources().getColor(R.color.beauty_color_FFB729));
                memberTitleCardSelected.setVisibility(View.VISIBLE);
                cardDetailLl.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }


    private void showBaseInfoBtn() {
        if (mCustomer.isDisabled()) {
            mRecharge.setEnabled(false);
        } else {
            mRecharge.setEnabled(true);
        }
        mCreateOrder.setVisibility(View.GONE);
        mCreateOrder.setBackgroundResource(R.drawable.beauty_customer_detail_pink_order_btn1);
    }


    private void showCardInfoBtn() {
        mRecharge.setEnabled(true);
        mCreateOrder.setVisibility(View.GONE);
        mRecharge.setBackgroundResource(R.drawable.beauty_customer_detail_pink_recharge_btn);
        mCreateOrder.setBackgroundResource(R.drawable.beauty_customer_detail_pink_order_btn);
    }


    private void loadCardInfo(final String cardNo) {
        UserActionEvent.start(UserActionEvent.CUSTOMER_QUERY_EC_CARD);
        mCustomerOperates.cancelGetMemberCards();
                dataList.clear();
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


    private void clearCardInfo() {
        ecCard = null;
        mCardNoTv.setText("-");
        mCardKind.setText("-");
        mCardMoneyTv.setText("-");
        mCardPoint.setText("-");
    }


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



    private void showCardListDialog() {

    }


    private void showPasswordDialog(final String mobile, final String inputNo, final Long customerId) {
        CustomerLogin.showMemberPasswordDialog(getActivity(), inputNo, new CustomerLogin.DinnerLoginListener() {
            @Override
            public void login(PasswordDialog dialog, int needPswd, String password) {
                doVerifypassword(dialog, mobile, customerId + "", password);
            }
        });
    }


    private void doVerifypassword(final PasswordDialog dialog, String mobile, String customerId, final String password) {
        if (customerId != null) {
            CustomerLoginReq loginReq = new CustomerLoginReq();
            loginReq.setLoginId(customerId);
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


    private void inputFace() {

    }


    private void bindFace(String faceCode) {
        if (mCustomer == null) {
            return;
        }
        mFaceCode = faceCode;
        saveCustomerFaceCode(mCustomer.customerId, mFaceCode);
    }




    private void checkPermiss(ChargingType type) {
        if (type == ChargingType.LIST) {
            showCharingBalance(true);
        } else {
            showreChargeDialog(true);
        }
    }


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


    private void showCharingBalance(boolean isMember) {
        if (isMember) {            Bundle args = new Bundle();
            args.putInt("type", checkType);
            args.putInt("whereFrom", CustomerChargingDialogFragment.FROM_MEMBER_CUSTOMER);            args.putString("customerId", String.valueOf(mCustomer.customerId));
            args.putString("balance", mCustomer.remainValue + "");
            args.putString("integral", mCustomer.integral + "");
            args.putSerializable(BeautyCustomerConstants.KEY_CUSTOMER, mCustomer);
            Intent intent = new Intent();
            intent.setClass(getActivity(), BeautyCustomerChargingBalanceActivity_.class);
            intent.putExtras(args);
            startActivity(intent);
        } else {            if (ecCard != null) {

            } else {
                ToastUtil.showLongToast(R.string.customer_get_card_info_fail);
            }
        }
    }


    private void showreChargeDialog(boolean isMember) {
        if (isMember) {            showChargingDialog(mCustomer, null, mCustomer.remainValue + "");
        } else {            if (ecCard != null) {
                showChargingDialog(mCustomer, ecCard, null);
            } else {
                ToastUtil.showLongToast(R.string.customer_get_card_info_fail);
            }
        }
    }


    private void showChargingDialog(CustomerResp customer, EcCardInfo ecCard, String balance) {
        CustomerChargingDialogFragment dialogFragment = new CustomerChargingDialogFragment_();
        Bundle args = new Bundle();
        args.putInt(CustomerChargingDialogFragment.KEY_FROM, CustomerChargingDialogFragment.FROM_MEMBER_CUSTOMER);        args.putSerializable(CustomerChargingDialogFragment.KEY_CUSTOMER, customer);
        if (ecCard != null) {
            args.putSerializable(CustomerChargingDialogFragment.KEY_ECCARD, ecCard);
            args.putString(CustomerChargingDialogFragment.KEY_BALANCE, ecCard.getRemainValue() != null ? ecCard.getRemainValue().toPlainString() : "0");
        } else {
            args.putString(CustomerChargingDialogFragment.KEY_BALANCE, balance);
        }
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "ecCardCharging");
    }


    private void toBookPage() {


    }


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



    private void showEditUser() {
        BeautyCustomerEditActivity_.IntentBuilder_ senderOrder = BeautyCustomerEditActivity_.intent(getActivity());
        senderOrder.get().putExtra(CustomerContants.KEY_CUSTOMER_EDIT, mCustomer);
        senderOrder.get().putExtra("type", CustomerActivity.PARAM_EDIT);
        if (mFrom == BeautyCustomerConstants.CustomerDetailFrom.DIALOG_TO_DETAIL) {
            senderOrder.get().putExtra(BeautyCustomerConstants.KEY_CUSTOMER_EDIT_PAGE, BeautyCustomerConstants.CustomerEditPage.DETAIL_DIALOG);
        }
        senderOrder.start();
    }



    private boolean checkPhoneSetting() {
        if (!SpHelper.getDefault().getBoolean(SpHelper.SP_FIXEDPHONE_SERVICE_SWITCH, true)
                && !SpHelper.getDefault().getBoolean(SpHelper.SP_BLUETOOTH_SERVICE_SWITCH, false)) {
            return false;
        }
        return true;
    }



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



    public void onEventMainThread(EventRefreshBalance event) {
        if (event.cardNo != null) {
            loadCardInfo(event.cardNo);
        } else {
            queryCustomerById(mCustomer.customerId);
        }
    }


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



    @Click({R.id.customer_takephoto, R.id.customer_bindcard, R.id.level_up, R.id.call, R.id.customer_edit})
    public void onClickTitleItem(View v) {
                if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.customer_bindcard:                 MobclickAgentEvent.onEvent(UserActionCode.GK010005);
                if (mCustomer != null) {
                    BeautyCustomerBindCardDialog dialog = new BeautyCustomerBindCardDialog();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BeautyCustomerConstants.KEY_CUSTOMER, mCustomer);
                    dialog.setArguments(bundle);
                    dialog.show(getChildFragmentManager(), "BeautyCustomerBindCardFragment");
                }
                break;
            case R.id.level_up:                 MobclickAgentEvent.onEvent(UserActionCode.GK010006);
                VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_UPDATE,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                CustomerLevelUpDialogFragment.show(mCustomer, getFragmentManager());
                            }
                        });
                break;
            case R.id.call:                 MobclickAgentEvent.onEvent(UserActionCode.GK010007);
                if (!checkPhoneSetting()) {
                    ToastUtil.showShortToast(getString(R.string.check_phone));
                    return;
                }


                break;
            case R.id.customer_edit:                 MobclickAgentEvent.onEvent(UserActionCode.GK010008);
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
            case R.id.customer_takephoto:                 MobclickAgentEvent.onEvent(UserActionCode.GK010004);
                if (!TextUtils.isEmpty(mCustomer.mobile)) {
                    showPasswordDialog(mCustomer.mobile, mCustomer.customerName, mCustomer.customerId);
                }
                break;
        }
    }


    @Click({R.id.customer_menber_title_base_root, R.id.customer_menber_title_card_root})
    public void onClickSwitch(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.customer_menber_title_base_root:                 MobclickAgentEvent.onEvent(UserActionCode.GK010009);
                switchTitleSelectStatus(0);
                break;
            case R.id.customer_menber_title_card_root:                 MobclickAgentEvent.onEvent(UserActionCode.GK010010);
                                switchTitleSelectStatus(2);
                if (ecCard != null) {
                    setCardInfo(ecCard);
                } else {
                    loadCardInfo(null);
                }
                break;
        }
    }


    @Click({R.id.customer_recharge, R.id.create_order})
    public void
    onClickBaseButton(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {             case R.id.customer_recharge:
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
            case R.id.create_order:                                 break;
        }
    }



    @Click({R.id.cus_jifen_ll, R.id.cus_yue_ll, R.id.cus_pwd_ll, R.id.cus_quan_ll,
            R.id.cus_card_time_ll, R.id.cus_wxapp_ll, R.id.cus_expense_ll,R.id.cus_doc_ll})
    protected void onClickBaseInfo(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.cus_pwd_ll:                 MobclickAgentEvent.onEvent(UserActionCode.GK010014);
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
            case R.id.cus_quan_ll:                 MobclickAgentEvent.onEvent(UserActionCode.GK010013);
                checkType = CustomerContants.TYPE_COUPON;
                checkPermiss(ChargingType.LIST);
                break;
            case R.id.cus_card_time_ll:                 checkType = CustomerContants.TYPE_CARD_TIME;
                checkPermiss(ChargingType.LIST);
                break;
            case R.id.cus_wxapp_ll:
                checkType = CustomerContants.TYPE_WX_APP;
                checkPermiss(ChargingType.LIST);
                break;
            case R.id.cus_doc_ll:
                checkType = CustomerContants.TYPE_DOCMENT;
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



    @Click({R.id.card_no_layout, R.id.card_money_layout, R.id.card_point_layout, R.id.tv_last_consume_remark_more})
    public void onCardInfoClick(View v) {
                if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.card_no_layout:                 showCardListDialog();
                break;
            case R.id.card_money_layout:                MobclickAgentEvent.onEvent(UserActionCode.GK010017);
                checkType = CustomerContants.TYPE_BALANCE;
                showCharingBalance(false);
                break;
            case R.id.card_point_layout:                MobclickAgentEvent.onEvent(UserActionCode.GK010018);
                checkType = CustomerContants.TYPE_INTEGRAL;
                showCharingBalance(false);
                break;
            case R.id.tv_last_consume_remark_more:                showConsumeRecord();
                break;
            default:
                break;
        }
    }
}