package com.zhongmei.bty.queue;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.database.enums.EntranceType;
import com.zhongmei.bty.basemodule.database.enums.TicketTypeEnum;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.notifycenter.event.ActionNotify;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment_;
import com.zhongmei.bty.queue.event.IdleTableCountChangeEvent;
import com.zhongmei.bty.queue.event.QueueShowChooseTableEvent;
import com.zhongmei.bty.queue.event.QueueShowDetailEvent;
import com.zhongmei.bty.queue.event.RefreshInfoEvent;
import com.zhongmei.bty.queue.event.SwictPageEvent;
import com.zhongmei.bty.queue.ui.QueueHomeFragment;
import com.zhongmei.bty.queue.ui.QueueHomeFragment_;
import com.zhongmei.bty.queue.ui.QueueReportCenterFragment;
import com.zhongmei.bty.queue.ui.QueueReportCenterFragment_;
import com.zhongmei.bty.queue.ui.QueueTableSelectFragment;
import com.zhongmei.bty.queue.ui.QueueTableSelectFragment_;
import com.zhongmei.bty.queue.ui.QueueVoiceListFragment;
import com.zhongmei.bty.queue.ui.QueueVoiceListFragment_;
import com.zhongmei.bty.queue.ui.view.FreeTableNotifyBubble;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.yunfu.LoginActivity;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * 排队主页面
 */

@EActivity(R.layout.queue_main_activity)
public class QueueMainActivity extends MainBaseActivity {

    private static final String TAG = QueueMainActivity.class.getSimpleName();

    // 排队
    public static final int PAGE_QUEUE_LIST = 0;

    // 语音播放
    private static final int PAGE_VOICE_LIST = 1;

    // 排队历史
    public static final int PAGE_QUEUE_HISTORY_LIST = 2;

    // 创建排队
    public static final int PAGE_CREATE_QUEUE = 3;

    // 空闲桌台
    public static final int PAGE_IDLE_TABLE = 4;

    //排队搜索
    public static final int PAGE_QUEUE_SEARCH = 5;

    //排队详情
    public static final int PAGE_QUEUE_DETAIL = 6;

    //排队报表中心
    public static final int PAGE_QUEUE_REPORT_CENTER = 7;

    // 完整类名
    private static final String ACTIVITY_NAME = QueueMainActivity_.class.getName();

    private NewQueueBeanVo queueDetailVo;

    @ViewById(R.id.viewSwitcher)
    ViewSwitcher switcher;

    @ViewById(R.id.table_notify)
    FreeTableNotifyBubble tableNotify;

    @ViewById(R.id.tip_img)
    protected ImageView mTipImg;

    // 通知上的订单数
    @ViewById(R.id.view_notify_tip)
    TextView viewNotifyCenterTip;

    // 通知上的其他数
    @ViewById(R.id.view_notify_tip_other)
    TextView viewNotifyCenterOtherTip;

    // 标题栏左边图标
    @ViewById(R.id.iv_title_bar_menu)
    protected ImageView ivTitleBarMenu;

    // 排队列表
    @ViewById(R.id.queue_list)
    protected TextView tvQueueList;

    // 历史记录
    @ViewById(R.id.queue_list_history)
    protected TextView tvQueueListHistory;

    @ViewById(R.id.drawer)
    protected DrawerLayout drawer;

    // 当前登录名称
    @ViewById(R.id.tv_current_name)
    protected TextView tvCurrentName;

    // 左侧菜单
    @ViewById
    protected View left_menu;

    // 当前Id
    @ViewById(R.id.tv_current_id)
    protected TextView tvCurrentId;

    // 主窗体
    @ViewById
    protected LinearLayout main_frame;

    // 遮罩层
    @ViewById(R.id.view_shadow)
    protected View viewShadow;

    @ViewById(R.id.rg_menu_group)
    protected RadioGroup rgMenuGroup;

    // 排对中心
    @ViewById(R.id.rb_queue_list)
    protected RadioButton rbQueueList;
    // 排对中心
    @ViewById(R.id.rb_queue_self_help)
    protected RadioButton rbQueueSelfHelp;

    // 设置
    @ViewById(R.id.rb_queue_setting)
    protected RadioButton rbQueueSetting;

    @ViewById(R.id.rb_queue_report_center)
    protected RadioButton rbQueueReportCenter;

    @ViewById(R.id.queue_title)
    protected LinearLayout title;

    @ViewById(R.id.rl_title_bar_left)
    protected RelativeLayout rlTitleBarLeft;

    @ViewById(R.id.rl_title_bar_right)
    protected RelativeLayout rlTitleBarRight;

    /**
     * 左侧标题
     */
    @ViewById(R.id.queue_voice_list_tv)
    protected TextView voiceTV;

    /**
     * 左侧切换按钮
     */
    @ViewById(R.id.queue_list_switch_Layout)
    protected LinearLayout queueListLayout;

    // 右边title标题
    @ViewById(R.id.tv_title_bar_cname)
    TextView tv_cname;

    // 右边创建排队/空闲桌台 tab
    @ViewById(R.id.right_switch_layout)
    LinearLayout rightSwitchLayout;

    // 创建桌台tab
    @ViewById(R.id.create_queue_label)
    protected TextView tvCreateQueue;

    // 空闲桌台
    @ViewById(R.id.idle_table_list_label)
    protected TextView tvIdleTable;

    @ViewById(R.id.tv_idle_table_count)
    protected TextView tvIdleTableCount;

    @ViewById(R.id.drawerIdleTable)
    protected DrawerLayout drawerIdleTable;

    @ViewById(R.id.iv_search)
    protected ImageView mIvSearch;

    @ViewById(R.id.iv_search_back)
    protected ImageView mIvSearchBack;

    @ViewById(R.id.iv_detail_back)
    protected ImageView mIvDetailBack;

    @ViewById(R.id.queue_print_item_select)
    protected ImageView mIvPrintItem;

    private int pageTag = -1;

    private boolean isLeftShown = false;

    // 排队列表
    private QueueHomeFragment queueHomeFragment;

    // 语音列表
    private QueueVoiceListFragment queueSettingFragment;

    //报表中心
    private QueueReportCenterFragment queueReportCenterFragment;

    private QueueTableSelectFragment idleTableFragment;

    //private TableDataChangeObserver tableObserver;

    //private TablesDal tablesDal;
    //private List<Tables> emptyTables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayServiceManager.doUpdateQueue(getApplicationContext());
        //TVClientService.start(MainApplication.getInstance());
        /*tablesDal = OperatesFactory.create(TablesDal.class);
        try {
            emptyTables = tablesDal.listDinnerEmptyTablesByStatus(TableStatus.EMPTY);b
        } catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }*/
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);
        setIntent(newIntent);
        EventBus.getDefault().post(new RefreshInfoEvent());

        if (drawer != null && drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        }
    }

    @Override
    protected void onPause() {
        tableNotify.pause();

        super.onPause();
    }

    @Override
    protected void onStop() {
        if (drawerIdleTable.isDrawerOpen(Gravity.END)) {
            drawerIdleTable.closeDrawer(Gravity.END);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DisplayServiceManager.doUpdateQueue(getApplicationContext(), true);
        tableNotify.destroy();
        unregisterEventBus();
        //DatabaseHelper.Registry.unregister(tableObserver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 添加titlebar
        replaceTitleLayout();

        tableNotify.resume();
    }

    public void onEventMainThread(IdleTableCountChangeEvent tableCount) {
        Log.d(TAG, "tableCount = " + tableCount.count);
        if (tableCount.count > 0) {
            if (tableCount.count > 9)
                tvIdleTableCount.setText("9+");
            else
                tvIdleTableCount.setText("" + tableCount.count);
            tvIdleTableCount.setVisibility(View.VISIBLE);
        } else {
            tvIdleTableCount.setVisibility(View.GONE);
        }
    }

    /**
     *
     */
    private void replaceTitleLayout() {
        TitleBarFragment titleBarFragment = new TitleBarFragment_();
        replaceFragment(R.id.queue_title_bar_layout, titleBarFragment, titleBarFragment.getClass().getName());
    }

    // 加载完View之后进行处理
    @AfterViews
    void init() {
        //tableObserver = new TableDataChangeObserver();
        //DatabaseHelper.Registry.register(tableObserver);
        initNotifyCenter();

        initLeftMenu();
        if (null != tvCurrentName && Session.getAuthUser() != null) {
            tvCurrentName.setText(Session.getAuthUser().getName());
        }
        //String[] no = MainApplication.getInstance().getShopInfo().getUIString(MainApplication.getInstance(), ShopInfo.TABLET_NUMBER_KEY).split(":");
        tvCurrentId.setText("NO:" + /*no[1]*/ ShopInfoCfg.getInstance().getTabletNumberFormat());

        if (pageTag == -1) {
            showQueueList();
        } else {
            changePage();
        }

        drawerIdleTable.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerIdleTable.setFocusableInTouchMode(false);
        drawerIdleTable.setScrimColor(getResources().getColor(R.color.shadow_bg));

        drawerIdleTable.setDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {

            }

            @Override
            public void onDrawerOpened(View view) {
                //EventBus.getDefault().post(new QueueChooseTableWindowEvent(true));
            }

            @Override
            public void onDrawerClosed(View view) {
                //EventBus.getDefault().post(new QueueChooseTableWindowEvent(false));
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        idleTableFragment = new QueueTableSelectFragment_();
        replaceFragment(R.id.idle_tabl_layout, idleTableFragment, QueueTableSelectFragment.class.getSimpleName());
        idleTableFragment.setOpenType(QueueTableSelectFragment.OPEN_TYPE_SUPERNATANT);
        registerEventBus();
    }

    private void initNotifyCenter() {
        tableNotify.setEntranceType(EntranceType.QUEUE);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.setFocusableInTouchMode(false);
        drawer.setScrimColor(getResources().getColor(R.color.shadow_bg));
        drawer.setDrawerListener(new DrawerListener() {

            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (mTipImg.getVisibility() != View.VISIBLE) {
                    mTipImg.setVisibility(View.VISIBLE);
                }
                mTipImg.setTranslationX((slideOffset - 1));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }
        });
    }

    @Click(R.id.tv_current_name)
    void doLogout() {
        Session.unbind();
        Intent intent = new Intent(QueueMainActivity.this, LoginActivity.class);
        intent.putExtra("activityName", ACTIVITY_NAME);
        startActivity(intent);
        QueueMainActivity.this.finish();
    }

    // 点击遮罩
    @Click(R.id.view_shadow)
    void clickViewShadow() {
        setLeftMenu(false);
    }

    // 点击标题栏菜单按钮
    @Click(R.id.iv_title_bar_menu)
    void clickTitleBarMenu() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        }
        switchLeftMenu();
    }

    private void initLeftMenu() {
        rbQueueList.setChecked(true);
        rgMenuGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbQueueList.setEnabled(false);
                rbQueueSetting.setEnabled(false);
                switch (checkedId) {
                    case R.id.rb_queue_list:
                        MobclickAgentEvent.onEvent(UserActionCode.PD010001);
                        showQueueList();
                        break;
                    case R.id.rb_queue_setting:
                        MobclickAgentEvent.onEvent(UserActionCode.PD010002);
                        showQueueVoiceList();
                        break;
                    case R.id.rb_queue_self_help:
                        MobclickAgentEvent.onEvent(UserActionCode.PD010003);
                        toAutoQueue();
                        break;
                    case R.id.rb_queue_report_center:
                        MobclickAgentEvent.onEvent(UserActionCode.PD010004);
                        showQueueReportCenter();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    /*
     * 跳转到自动排队
     */
    private void toAutoQueue() {
        DialogUtil.showHintConfirmDialog(getSupportFragmentManager(), getString(R.string.setting_queue_auto_queue_quit_hint), R.string.know, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueueAutoTakeNumberActivity_.intent(QueueMainActivity.this).start();
                QueueMainActivity.this.finish();
            }
        }, true, "");
    }

    private void showQueueList() {
        if (pageTag == PAGE_QUEUE_LIST) {
            return;
        }
        pageTag = PAGE_QUEUE_LIST;
        changePage();
        changeTitle();
    }

    private void showQueueVoiceList() {
        if (pageTag == PAGE_VOICE_LIST) {
            return;
        }
        pageTag = PAGE_VOICE_LIST;
        changePage();
        changeTitle();
    }

    private void showQueueReportCenter() {
        if (pageTag == PAGE_QUEUE_REPORT_CENTER) {
            return;
        }
        pageTag = PAGE_QUEUE_REPORT_CENTER;
        changePage();
        changeTitle();
    }

    /**
     * 修改标题
     */
    private void changeTitle() {
        if (pageTag == PAGE_QUEUE_LIST) {
            switchListOrHistoryOrSearch(PAGE_QUEUE_LIST);
            switchCreateOrTableOrDetail(PAGE_CREATE_QUEUE);
            queueListLayout.setVisibility(View.VISIBLE);
            voiceTV.setVisibility(View.GONE);
            //展示排队列表时隐藏右边标题，显示tab
            tv_cname.setVisibility(View.GONE);
            rightSwitchLayout.setVisibility(View.VISIBLE);
            mIvSearch.setVisibility(View.VISIBLE);
            mIvSearchBack.setVisibility(View.GONE);
            mIvDetailBack.setVisibility(View.GONE);
            mIvPrintItem.setVisibility(View.GONE);

            if (rlTitleBarLeft.getLayoutParams() instanceof LinearLayout.LayoutParams && rlTitleBarRight.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) rlTitleBarLeft.getLayoutParams()).weight = 2;
                ((LinearLayout.LayoutParams) rlTitleBarRight.getLayoutParams()).weight = 1;
            }
        } else if (pageTag == PAGE_VOICE_LIST) {
            queueListLayout.setVisibility(View.GONE);
            voiceTV.setVisibility(View.VISIBLE);
            voiceTV.setText(R.string.queue_voice_list_label);
            //展示排队列表时隐藏右边tab，显示标题
            tv_cname.setText(R.string.queue_voice_play);
            tv_cname.setVisibility(View.VISIBLE);
            rightSwitchLayout.setVisibility(View.GONE);
            mIvSearch.setVisibility(View.GONE);
            mIvSearchBack.setVisibility(View.GONE);
            mIvDetailBack.setVisibility(View.GONE);
            mIvPrintItem.setVisibility(View.GONE);

            if (rlTitleBarLeft.getLayoutParams() instanceof LinearLayout.LayoutParams && rlTitleBarRight.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) rlTitleBarLeft.getLayoutParams()).weight = 7;
                ((LinearLayout.LayoutParams) rlTitleBarRight.getLayoutParams()).weight = 5;
            }

        } else if (pageTag == PAGE_QUEUE_REPORT_CENTER) {
            queueListLayout.setVisibility(View.GONE);
            voiceTV.setVisibility(View.VISIBLE);
            voiceTV.setText(R.string.slidemenu_dinner_form_center);
            //展示排队列表时隐藏右边tab，显示标题
            tv_cname.setText(R.string.queue_loss_rate_rank);
            tv_cname.setVisibility(View.VISIBLE);
            rightSwitchLayout.setVisibility(View.GONE);
            mIvSearch.setVisibility(View.GONE);
            mIvSearchBack.setVisibility(View.GONE);
            mIvDetailBack.setVisibility(View.GONE);
            mIvPrintItem.setVisibility(View.GONE);

            if (rlTitleBarLeft.getLayoutParams() instanceof LinearLayout.LayoutParams && rlTitleBarRight.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) rlTitleBarLeft.getLayoutParams()).weight = 1;
                ((LinearLayout.LayoutParams) rlTitleBarRight.getLayoutParams()).weight = 2;
            }
        }
    }

    private void changePage() {
        if (pageTag == PAGE_QUEUE_LIST) {
//            switchListOrHistory(PAGE_QUEUE_LIST);
            queueHomeFragment = new QueueHomeFragment_();
            replaceFragment(R.id.main_frame, queueHomeFragment, QueueHomeFragment.class.getSimpleName());
            setLeftMenu(false);
            main_frame.setTag(PAGE_QUEUE_LIST);
        } else if (pageTag == PAGE_VOICE_LIST) {
            queueSettingFragment = new QueueVoiceListFragment_();
            replaceFragment(R.id.main_frame, queueSettingFragment, QueueVoiceListFragment.class.getSimpleName());
            setLeftMenu(false);
            main_frame.setTag(PAGE_VOICE_LIST);
        } else if (pageTag == PAGE_QUEUE_REPORT_CENTER) {
            queueReportCenterFragment = new QueueReportCenterFragment_();
            replaceFragment(R.id.main_frame, queueReportCenterFragment, QueueReportCenterFragment.class.getSimpleName());
            setLeftMenu(false);
        }

        rbQueueList.setEnabled(true);
        rbQueueSetting.setEnabled(true);
        rbQueueReportCenter.setEnabled(true);
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        }
    }

    /**
     * 显示或隐藏左侧菜单
     */
    private void switchLeftMenu() {
        setLeftMenu(!isLeftShown);
    }

    private void setLeftMenu(boolean visible) {
        if (visible) {
            if (!isLeftShown) {
                left_menu.setVisibility(View.VISIBLE);

                doAnimation(this, main_frame, R.animator.fragment_slide_right_exit);
                doAnimation(this, left_menu, R.animator.fragment_slide_left_enter);
                doAnimation(this, title, R.animator.fragment_slide_right_exit);

                // 展开左侧菜单时，显示遮罩
                viewShadow.setVisibility(View.VISIBLE);
            }
        } else {
            if (isLeftShown) {
                doAnimation(this, main_frame, R.animator.fragment_slide_right_enter);
                doAnimation(this, left_menu, R.animator.fragment_slide_left_exit);
                doAnimation(this, title, R.animator.fragment_slide_right_enter);

                // 关闭左侧菜单时，隐藏遮罩
                viewShadow.setVisibility(View.GONE);
            }
        }
        isLeftShown = visible;
    }

    private void doAnimation(Context context, View view, int id) {

        AnimatorSet objectAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(context, id);
        objectAnimator.setTarget(view);
        objectAnimator.start();

    }

    @Click({R.id.queue_list, R.id.queue_list_history, R.id.iv_queue_notify_center, R.id.create_queue_label, R.id.idle_table_list_label, R.id.iv_search, R.id.iv_search_back, R.id.iv_detail_back, R.id.queue_print_item_select})
    void initListener(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.queue_list:
                MobclickAgentEvent.onEvent(UserActionCode.PD020001);
                switchListOrHistoryOrSearch(PAGE_QUEUE_LIST);
                EventBus.getDefault().post(new SwictPageEvent(PAGE_QUEUE_LIST));
                break;
            case R.id.queue_list_history:
                MobclickAgentEvent.onEvent(UserActionCode.PD020002);
                switchListOrHistoryOrSearch(PAGE_QUEUE_HISTORY_LIST);
                EventBus.getDefault().post(new SwictPageEvent(PAGE_QUEUE_HISTORY_LIST));
                break;
            case R.id.iv_queue_notify_center:
                if (drawer.isDrawerOpen(Gravity.START)) {
                    drawer.closeDrawer(Gravity.START);
                } else {
                    drawer.openDrawer(Gravity.START);
                }
                break;
            case R.id.create_queue_label:
                MobclickAgentEvent.onEvent(UserActionCode.PD020003);
                //点击创建排队Tab
                switchCreateOrTableOrDetail(PAGE_CREATE_QUEUE);
                EventBus.getDefault().post(new SwictPageEvent(PAGE_CREATE_QUEUE));
                break;
            case R.id.idle_table_list_label:
                MobclickAgentEvent.onEvent(UserActionCode.PD020004);
                //点击空闲桌台Tab
                switchCreateOrTableOrDetail(PAGE_IDLE_TABLE);
                EventBus.getDefault().post(new SwictPageEvent(PAGE_IDLE_TABLE));
                break;
            case R.id.iv_search:
                switchListOrHistoryOrSearch(PAGE_QUEUE_SEARCH);
                EventBus.getDefault().post(new SwictPageEvent(PAGE_QUEUE_SEARCH));
                break;
            case R.id.iv_search_back:
                switchListOrHistoryOrSearch(PAGE_QUEUE_LIST);
                EventBus.getDefault().post(new SwictPageEvent(PAGE_QUEUE_LIST));
                break;
            case R.id.iv_detail_back:
                switchCreateOrTableOrDetail(PAGE_CREATE_QUEUE);
                EventBus.getDefault().post(new SwictPageEvent(PAGE_CREATE_QUEUE));
                break;
            case R.id.queue_print_item_select:
                if (queueDetailVo != null && queueDetailVo.getQueue().getQueueStatus() == QueueStatus.QUEUEING) {
                    //QueueOpManager.getInstance().doPrint(queueDetailVo, new PRTOnSimplePrintListener(PrintTicketTypeEnum.QUEUE));
                }
                break;
            default:
                break;
        }
    }

    private void switchListOrHistoryOrSearch(int pageIndex) {
        if (pageIndex == PAGE_QUEUE_LIST) {
            queueListLayout.setVisibility(View.VISIBLE);
            mIvSearch.setVisibility(View.VISIBLE);
            voiceTV.setVisibility(View.GONE);
            mIvSearchBack.setVisibility(View.GONE);
            tvQueueList.setBackgroundResource(R.drawable.text_view_border_left_select);
            tvQueueList.setTextColor(getResources().getColor(R.color.text_blue));
            tvQueueListHistory.setBackgroundResource(R.drawable.text_view_border_right);
            tvQueueListHistory.setTextColor(getResources().getColor(R.color.text_white));
        } else if (pageIndex == PAGE_QUEUE_HISTORY_LIST) {
            queueListLayout.setVisibility(View.VISIBLE);
            mIvSearch.setVisibility(View.VISIBLE);
            voiceTV.setVisibility(View.GONE);
            mIvSearchBack.setVisibility(View.GONE);
            tvQueueList.setBackgroundResource(R.drawable.text_view_border_left);
            tvQueueList.setTextColor(getResources().getColor(R.color.text_white));
            tvQueueListHistory.setBackgroundResource(R.drawable.text_view_border_right_select);
            tvQueueListHistory.setTextColor(getResources().getColor(R.color.text_blue));
        } else if (pageIndex == PAGE_QUEUE_SEARCH) {
            queueListLayout.setVisibility(View.GONE);
            mIvSearch.setVisibility(View.GONE);
            voiceTV.setVisibility(View.VISIBLE);
            mIvSearchBack.setVisibility(View.VISIBLE);
            voiceTV.setText(R.string.queue_home_title_search_title);
        }
    }

    private void switchCreateOrTableOrDetail(int pageIndex) {
        if (pageIndex == PAGE_CREATE_QUEUE) {
            tv_cname.setVisibility(View.GONE);
            rightSwitchLayout.setVisibility(View.VISIBLE);
            mIvDetailBack.setVisibility(View.GONE);
            mIvPrintItem.setVisibility(View.GONE);
            tvCreateQueue.setBackgroundResource(R.drawable.text_view_border_left_select);
            tvCreateQueue.setTextColor(getResources().getColor(R.color.text_blue));
            tvIdleTable.setBackgroundResource(R.drawable.text_view_border_right);
            tvIdleTable.setTextColor(getResources().getColor(R.color.text_white));
        } else if (pageIndex == PAGE_IDLE_TABLE) {
            tv_cname.setVisibility(View.GONE);
            rightSwitchLayout.setVisibility(View.VISIBLE);
            mIvDetailBack.setVisibility(View.GONE);
            mIvPrintItem.setVisibility(View.GONE);
            tvCreateQueue.setBackgroundResource(R.drawable.text_view_border_left);
            tvCreateQueue.setTextColor(getResources().getColor(R.color.text_white));
            tvIdleTable.setBackgroundResource(R.drawable.text_view_border_right_select);
            tvIdleTable.setTextColor(getResources().getColor(R.color.text_blue));
        } else if (pageIndex == PAGE_QUEUE_DETAIL) {
            tv_cname.setVisibility(View.VISIBLE);
            rightSwitchLayout.setVisibility(View.GONE);
            mIvDetailBack.setVisibility(View.VISIBLE);
            mIvPrintItem.setVisibility(View.VISIBLE);
            tv_cname.setText(R.string.queue_home_title_search_detail);
        }
    }

    /*private class TableDataChangeObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(DBHelper.getUri(Tables.class))) {
                try {
                    List<Tables> tmpEmptyTables = tablesDal.listDinnerEmptyTablesByStatus(TableStatus.EMPTY);
                    if(emptyTables != null && !emptyTables.containsAll(tmpEmptyTables)){
                        int freeTableCount = tmpEmptyTables.size();
                        //处理空闲桌台的气泡（当前是子线程）
                    }

                    emptyTables = tmpEmptyTables;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    /**
     * 监听打印失败
     *
     * @param notify
     */
    public void onEventMainThread(final ActionNotify notify) {
        if (TextUtils.equals(notify.getTicketName(), TicketTypeEnum.QUEUE.getName())) {
            ToastUtil.showShortToast(getString(R.string.printer_error));
        }
    }

    private EntranceType mFromPage = null;
    //记录是从通知中心跳到的目的模块，此模块处于前台时值为空
    private EntranceType mToPage;

    /**
     * 点击queueListItem展示详情
     *
     * @param event
     */
    public void onEventMainThread(QueueShowDetailEvent event) {
        queueDetailVo = event.getQueueBeanVo();
        switchCreateOrTableOrDetail(PAGE_QUEUE_DETAIL);
        EventBus.getDefault().post(new SwictPageEvent(QueueMainActivity.PAGE_QUEUE_DETAIL));
    }

    @Click(R.id.idle_table_close_btn)
    public void closeDrawerIdleTable() {
        if (drawerIdleTable.isDrawerOpen(Gravity.END)) {
            drawerIdleTable.closeDrawer(Gravity.END);
        }
    }

    /**
     * 显示/隐藏桌台选择消息
     *
     * @param event
     */
    public void onEventMainThread(QueueShowChooseTableEvent event) {
        if (event.isShow()) {
            //显示桌台选择
            idleTableFragment.resetUI();
            if (idleTableFragment != null) {
                idleTableFragment.setWindowModel(true);
                idleTableFragment.setQueueObj(event.getQueueVo());
                if (!drawerIdleTable.isDrawerOpen(Gravity.END)) {
                    drawerIdleTable.openDrawer(Gravity.END);
                }
            }
        } else {
            //隐藏桌台选择
            if (drawerIdleTable.isDrawerOpen(Gravity.END)) {
                drawerIdleTable.closeDrawer(Gravity.END);
            }
        }
    }

}
