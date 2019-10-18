package com.zhongmei.bty.common.view.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.bty.base.BusinessMainActivity;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.auth.permission.dialog.VerifyPermissionsDialogFragment;
import com.zhongmei.bty.basemodule.database.enums.EntranceType;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.commonmodule.database.entity.local.CustomerArrivalShop;
import com.zhongmei.bty.customer.customerarrive.CustomerNoticeHelper;
import com.zhongmei.bty.customer.customerarrive.ICustomerNoticeListener;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import de.greenrobot.event.EventBus;


@EViewGroup(R.layout.dinner_left_menu)
public class DinnerLeftMenuView extends LinearLayout implements VerifyPermissionsDialogFragment.PermissionVerify {

    @ViewById(R.id.dinner_left_login_name)
    protected TextView tv_loginName;

        @ViewById(R.id.view_notify_tip)
    protected TextView viewNotifyCenterTip;

        @ViewById(R.id.view_notify_tip_other)
    protected TextView viewNotifyCenterOtherTip;

    @ViewById(R.id.dinner_bar_menu_btn)
    protected ImageView mMenu;

    @ViewById(R.id.rl_notice)
    protected FrameLayout rlNotice;

    @ViewById(R.id.rlDinner_bomb_box)
    protected RelativeLayout mBombBox;

    @ViewById(R.id.rlDinner_queue)
    protected RelativeLayout mQueue;

    @ViewById(R.id.rlDinner_booking)
    protected RelativeLayout mBooking;

    @ViewById(R.id.rlDinner_Dinner)
    protected RelativeLayout mDinner;

    @ViewById(R.id.rlDinner_Group)
    protected RelativeLayout mGroup;

        @ViewById(R.id.ll_customer)
    protected LinearLayout llCustomer;

    @ViewById(R.id.fl_customer)
    protected FrameLayout flCustomer;

    @ViewById(R.id.btn_switch_lang)
    protected ToggleButton mBtnSwitchLanguage;
        private LinearLayout customerAlterView;
    private Animation animation;
    @ViewById(R.id.view_notify_customer)
    protected TextView tvNotifyCustomer;
        protected BusinessMainActivity mBusinessMainActivity;
    private boolean isGoinRemind;
    private EntranceType mEntranceType = EntranceType.DINNER;

    private int mAttrBooking;
    private int mAttrQueue;
    private int mAttrBomb;
    private int mAttrGroup;
    private int mAttrDinner;

    private final int D_VISIBLE = 0;
    private final int D_INVISIBLE = 1;
    private final int D_GONE = 2;

    public DinnerLeftMenuView(Context context) {
        super(context);
    }

    public DinnerLeftMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (!this.isInEditMode()) {
            mBusinessMainActivity = (BusinessMainActivity) context;
            mEntranceType = mBusinessMainActivity.getEntranceType();
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DinnerLeftMenuView);
        mAttrBooking = typedArray.getInt(R.styleable.DinnerLeftMenuView_showBooking, D_VISIBLE);
        mAttrQueue = typedArray.getInt(R.styleable.DinnerLeftMenuView_showQueue, D_VISIBLE);
        mAttrBomb = typedArray.getInt(R.styleable.DinnerLeftMenuView_showBomb, D_VISIBLE);
        mAttrGroup = typedArray.getInt(R.styleable.DinnerLeftMenuView_showGroup, D_GONE);
        mAttrDinner = typedArray.getInt(R.styleable.DinnerLeftMenuView_showDinner, D_GONE);
    }

    public DinnerLeftMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        setLoginName();
    }


    public void setEntranceType(EntranceType mEntranceType) {
        this.mEntranceType = mEntranceType;
    }

    @AfterViews
    public void init() {
        checkViewStatus(mBooking, mAttrBooking);
        checkViewStatus(mBombBox, mAttrBomb);
        checkViewStatus(mQueue, mAttrQueue);
        checkViewStatus(mGroup, mAttrGroup);
        checkViewStatus(mDinner, mAttrDinner);
        if (!this.isInEditMode()) {
            setLoginName();
        }
                        isGoinRemind = SpHelper.getDefault().getBoolean(SpHelper.CUSTOMER_GOIN_REMIND);
        if (isGoinRemind && mEntranceType == EntranceType.DINNER && llCustomer != null) {
            llCustomer.setVisibility(View.VISIBLE);
            llCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        mBtnSwitchLanguage.setChecked(SpHelper.getDefault().getBoolean(SpHelper.DINNER_DISH_LANGUAGE, false));
        mBtnSwitchLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpHelper.getDefault().putBoolean(SpHelper.DINNER_DISH_LANGUAGE, isChecked);
            }
        });
    }

    private void showCustomerArriveDot() {
        boolean isShowDot = SpHelper.getDefault().getBoolean(SpHelper.CUSTOMER_ARRIVE_DOT);
        tvNotifyCustomer.setVisibility(isShowDot ? View.VISIBLE : View.GONE);
    }

    private void hideCustomerArriveDot() {
        SpHelper.getDefault().putBoolean(SpHelper.CUSTOMER_ARRIVE_DOT, false);
        showCustomerArriveDot();
    }

    private void showCustomerAlterView(Context context, final CustomerArrivalShop customer) {
        if (context == null || customer == null) {
            return;
        }

        customerAlterView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dinner_customer_alter_popu_layout, null);

        if (animation == null) {
            animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
            animation.setDuration(699);
        }

        ((TextView) customerAlterView.findViewById(R.id.customer_alter_name)).setText(CustomerNoticeHelper.getCustomerName(getContext(), customer));
        ((TextView) customerAlterView.findViewById(R.id.customer_alter_status)).setText(CustomerNoticeHelper.getCustomerStatus(getContext(), customer.arrivalStatus));
        customerAlterView.setLayoutAnimation(new LayoutAnimationController(animation, 0.19f));
        customerAlterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                                                                            }
        });
        flCustomer.addView(customerAlterView);
    }

    private ICustomerNoticeListener mNoticeListener = new ICustomerNoticeListener() {
        @Override
        public void onCustomerChanged(CustomerArrivalShop customerArrivalShop) {
            hideCustomerAlterView();
            showCustomerAlterView(getContext(), customerArrivalShop);
        }
    };

    public void hideCustomerAlterView() {
        if (customerAlterView != null) {
            flCustomer.removeView(customerAlterView);
            customerAlterView = null;
        }
    }



    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
                if (isGoinRemind) {
            if (mEntranceType == EntranceType.DINNER && CustomerNoticeHelper.isOpenCustomerNotice()) {
                asyncFindData();
            } else {
                showCustomerArriveDot();
            }
        }
    }

    @UiThread(delay = 760)
    protected void asyncFindData() {
        CustomerNoticeHelper.asyncFindCustomer(this.mBusinessMainActivity, new CustomerNoticeHelper.AsyncFindCustomerCallback() {
            @Override
            public void onFindData(List<CustomerArrivalShop> customerArrivalShops) {
                if (Utils.isNotEmpty(customerArrivalShops)) {
                    hideCustomerAlterView();
                    showCustomerAlterView(getContext(), customerArrivalShops.get(customerArrivalShops.size() - 1));
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDetachedFromWindow();
    }

            private void setLoginName() {
        if (Session.getAuthUser() == null) {
            tv_loginName.setText(R.string.unlogin);
            Drawable unLoginDrawable = getResources().getDrawable(R.drawable.dinner_unlogin_icon);
            tv_loginName.setCompoundDrawablesWithIntrinsicBounds(null, unLoginDrawable, null, null);
        } else {
            tv_loginName.setText(Session.getAuthUser().getDisplayName());
            Drawable loginDrawable = getResources().getDrawable(R.drawable.dinner_logined_icon);
            tv_loginName.setCompoundDrawablesWithIntrinsicBounds(null, loginDrawable, null, null);
        }
    }

    private void checkViewStatus(View view, int status) {
        switch (status) {
            case D_VISIBLE:
                view.setVisibility(VISIBLE);
                break;
            case D_INVISIBLE:
                view.setVisibility(INVISIBLE);
                break;
            case D_GONE:
                view.setVisibility(GONE);
                break;
        }
    }

    protected TextView getViewNotifyCenterTip() {
        return viewNotifyCenterOtherTip;
    }

    protected TextView getViewNotifyCenterOtherTip() {
        return viewNotifyCenterTip;
    }

    @Click({R.id.dinner_bar_menu_btn, R.id.rl_notice, R.id.dinner_bomb_box, R.id.dinner_queue, R.id.dinner_booking, R.id.dinner_dinner, R.id.dinner_group, R.id.dinner_left_login_name, R.id.ll_back})
    void onClick(View view) {
        if (mBusinessMainActivity == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_back:
                mBusinessMainActivity.finish();
                break;
            case R.id.dinner_bar_menu_btn:
                mBusinessMainActivity.switchLeftMenu();
                break;
            case R.id.rl_notice:
                mBusinessMainActivity.switchDrawer();
                break;
            case R.id.dinner_bomb_box:
                clickBombBox();
                break;
            case R.id.dinner_queue:
                                break;
            case R.id.dinner_booking:
                                break;
            case R.id.dinner_group:
                                mBusinessMainActivity.finish();
                break;
            case R.id.dinner_dinner:
                                mBusinessMainActivity.finish();
                break;
            case R.id.dinner_left_login_name:
                showQuietDialog();
                break;
        }
    }

    public void clickBombBox() {
        mBusinessMainActivity.closeDrawer();
        VerifyHelper.verifyAlert(mBusinessMainActivity,
                DinnerApplication.PERMISSION_DINNER_MONEYBOX,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                                                                    }
                });
    }


    private void showQuietDialog() {
        CommonDialogFragment.CommonDialogFragmentBuilder builder = new CommonDialogFragment.CommonDialogFragmentBuilder(BaseApplication.sInstance);
        builder.iconType(CommonDialogFragment.ICON_WARNING)
                .title(R.string.login_quiet_confirm)
                .negativeText(R.string.cancel)
                .positiveText(R.string.ok)
                .negativeLisnter(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .positiveLinstner(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLogin();
                    }
                });
        CommonDialogFragment fragment = builder.build();
        fragment.show(mBusinessMainActivity.getSupportFragmentManager(), "quiet_dialog");
    }


    private void showLogin() {

            }

    @Override
    public void verify(String permission, boolean success) {
        if (success) {
                                }
    }
}
