package com.zhongmei.beauty.customer;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zhongmei.beauty.customer.adapter.BeautyCustomerListAdapter;
import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.view.ScanPopupWindow;
import com.zhongmei.bty.basemodule.customer.bean.CustomerBaseBean;
import com.zhongmei.bty.basemodule.customer.bean.CustomerGroupBean;
import com.zhongmei.bty.basemodule.customer.bean.CustomerLevelBean;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.customer.CustomerActivity;
import com.zhongmei.bty.customer.adapter.CustomerPopWindowAdapter;
import com.zhongmei.bty.customer.event.CustomerFragmentReplaceListener;
import com.zhongmei.bty.customer.event.DetailRefreshEvent;
import com.zhongmei.bty.customer.event.EventCreateOrEditCustomer;
import com.zhongmei.bty.customer.event.EventRefreshDetail;
import com.zhongmei.bty.customer.views.CustomerPopWindow;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.YFResponseList;
import com.zhongmei.yunfu.bean.req.CustomerListReq;
import com.zhongmei.yunfu.bean.req.CustomerListResp;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.db.entity.crm.CustomerGroupLevel;
import com.zhongmei.yunfu.db.entity.crm.CustomerLevel;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.beauty_customer_list)
public class BeautyCustomerListFragment extends BasicFragment implements LoaderManager.LoaderCallbacks<Cursor>, OnRefreshListener, OnLoadMoreListener, BeautyCustomerListAdapter.OnRecyclerViewListener {
    private static final String TAG = BeautyCustomerListFragment.class.getSimpleName();

    private final int SEARCHTYPE_NOMAL = 0x01;    private final int SEARCHTYPE_POSTCARD = 0x02;     private final int SEARCHTYPE_SCANCODE = 0x03;
    @ViewById(R.id.customer_search_edit)
    protected EditText mSearchEdit;

    @ViewById(R.id.ivDelete_Customer)
    protected ImageView mIvDelete;

    @ViewById(R.id.ib_post_card)
    protected ImageButton ib_postCard;

    @ViewById(R.id.customer_type)
    protected TextView mType;

    @ViewById(R.id.customer_level)
    protected TextView mLevel;

    @ViewById(R.id.customer_group)
    protected TextView mGroup;

    @ViewById(R.id.tvSearch_Customer)
    protected TextView tvSearch;

    @ViewById(R.id.booking_detail_empty_layout)
    protected RelativeLayout mEmpty;

    @ViewById(R.id.swipeToLoadLayout)
    protected SwipeToLoadLayout swipeToLoadLayout;

    @ViewById(R.id.swipe_target)
    protected RecyclerView rvContent;

    private BeautyCustomerListAdapter mCustomerListAdapter;

    private List<CustomerListResp> mCustomerList = new ArrayList<CustomerListResp>();

    Handler initPopWinHandler = null;


    private List<CustomerBaseBean> mCustomerLevelList = new ArrayList<CustomerBaseBean>();


    private List<CustomerBaseBean> mCustomerGroupList = new ArrayList<CustomerBaseBean>();


    private List<CustomerBaseBean> mCustomerTypeList = new ArrayList<CustomerBaseBean>();

    private CustomerPopWindowAdapter mLevelAdatper;

    private CustomerPopWindowAdapter mGroupAdatper;

    private CustomerPopWindowAdapter mTypeAdapter;

    private CustomerPopWindow mLevelPop;

    private CustomerPopWindow mGroupPop;

    private CustomerPopWindow mTypePop;


    private String mSearchCondition = "";

    private String mLevelCondition = "";

    private String mGroupCondition = "";

    private String mTypeCondition = "";


    private String[] mTypeCustomerValue = new String[3];

    private ScanPopupWindow scanPopupWindow;

    private int mSearchType = SEARCHTYPE_NOMAL;

    private int mCurrentPage = 1;

    private Long mQueryTime;


    private boolean mIsRefresh = true;
    private final int PAGESIZE = 40;


    private boolean mIsCache = true;


    private boolean mIsSupportLoadMore = true;


    private CustomerOperates mCustomerOperates;

    private CustomerFragmentReplaceListener replaceListener;

    HandlerThread thread = new HandlerThread("customer_list");

    boolean level, group, type;

    public void setReplaceListener(CustomerFragmentReplaceListener replaceListener) {
        this.replaceListener = replaceListener;
    }

    @Override
    public void onAttach(Activity activity) {
        mCustomerOperates = OperatesFactory.create(CustomerOperates.class);
        super.onAttach(activity);
    }

    @AfterViews
    public void initView() {
                if (getActivity() instanceof MainBaseActivity) {
            ((MainBaseActivity) getActivity()).setIsConsumeTochTransEvent(true);
        }

        initPopWindows();

        initCustomerType();

        initLoaderManager();

        mSearchEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER)) {
                    String searchCondition = mSearchEdit.getText().toString();
                    if (!TextUtils.isEmpty(searchCondition)) {
                        mSearchCondition = searchCondition;
                        mSearchType = SEARCHTYPE_NOMAL;
                        deelInputKeyboard();
                        clearEditTextFocus();
                        refreshData();
                    }
                    return true;
                }
                return false;
            }
        });
        setupEditText();

        setupRecycelerView();

        registerEventBus();
    }


    private boolean deelInputKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private void initLoaderManager() {
        getLoaderManager().initLoader(0, null, this);        getLoaderManager().initLoader(1, null, this);    }

    private void setupRecycelerView() {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rvContent.setHasFixedSize(true);
        rvContent.setLayoutManager(linearLayoutManager);
        mCustomerListAdapter = new BeautyCustomerListAdapter(getActivity(), mCustomerList);
                mCustomerListAdapter.setOnRecyclerViewListener(this);
        rvContent.setAdapter(mCustomerListAdapter);
        rvContent.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!mIsSupportLoadMore) {
                    return;
                }
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == totalItemCount - 1
                        && visibleItemCount > 0 && mCustomerListAdapter.getItemCount() > 0) {
                                        swipeToLoadLayout.setLoadingMore(true);
                }
            }
        });
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
                super.onResume();
    }


    private void initPopWindows() {
        thread.start();

        initPopWinHandler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mLevelPop = new CustomerPopWindow(getActivity(), mLevelAdatper, levelItemSelectListener);
                        level = true;
                        break;
                    case 2:
                        mGroupPop = new CustomerPopWindow(getActivity(), mGroupAdatper, groupItemSelectListener);
                        group = true;
                        break;
                    case 3:
                        mTypePop = new CustomerPopWindow(getActivity(), mTypeAdapter, typeItemSelectListener);
                        type = true;
                        break;
                }
                if (level && group && type) {
                    if (thread.isAlive()) {

                        thread.quit();
                    }
                }
            }
        };
    }


    private OnItemClickListener typeItemSelectListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            if (isRefreshOrLoad()) {
                return;
            }
            String text = mCustomerTypeList.get(position).getName();
            if (TextUtils.isEmpty(text)) {
                return;
            }
            if (mTypeCustomerValue[1].equals(text)) {                mTypeCondition = "2";
                mType.setText(mTypeCustomerValue[1]);
            } else if (mTypeCustomerValue[2].equals(text)) {                mTypeCondition = "1";
                mType.setText(mTypeCustomerValue[2]);
            } else {                mTypeCondition = "";
                mType.setText(mTypeCustomerValue[0]);
            }
                        refreshData();
            mTypePop.dismiss();
        }
    };

    private OnItemClickListener groupItemSelectListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (isRefreshOrLoad()) {
                return;
            }
            mGroupCondition = mGroupAdatper.getId(arg2);
            mGroup.setText(mGroupAdatper.getName(arg2));
                        refreshData();
            mGroupPop.dismiss();
        }

    };

    private OnItemClickListener levelItemSelectListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (isRefreshOrLoad()) {
                return;
            }
            mLevelCondition = mLevelAdatper.getId(arg2);
            mLevel.setText(mLevelAdatper.getName(arg2));
                        refreshData();
            mLevelPop.dismiss();
        }
    };


    private boolean isRefreshOrLoad() {
        return (swipeToLoadLayout.isRefreshing() || swipeToLoadLayout.isLoadingMore()) ? true : false;
    }

    private void initCustomerType() {
        mTypeCustomerValue[0] = getString(R.string.customer_member_default);
        mTypeCustomerValue[1] = getString(R.string.customer_member);
        mTypeCustomerValue[2] = getString(R.string.customer_not_menber);

        CustomerBaseBean b;
        for (int i = 0; i < mTypeCustomerValue.length; i++) {
            b = new CustomerBaseBean();
            b.setId("");
            b.setName(mTypeCustomerValue[i]);
            mCustomerTypeList.add(b);
        }

        mTypeAdapter = new CustomerPopWindowAdapter(getActivity(), mCustomerTypeList);
                initPopWinHandler.sendEmptyMessage(3);
    }


    @Click(R.id.ivDelete_Customer)
    public void clearEditText() {
        clearEditTextFocus();
        mSearchEdit.setText("");

    }


    private void clearEditTextFocus() {
        if (mSearchEdit.hasFocus()) {
            mSearchEdit.setFocusable(false);
            mSearchEdit.setFocusableInTouchMode(true);
        }
    }

    @Click({R.id.customer_group, R.id.customer_level, R.id.customer_type, R.id.ib_scan_code, R.id.ib_post_card, R.id.tvSearch_Customer, R.id.ivCreate_Customer})
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        if (isRefreshOrLoad()) {
            return;
        }
        switch (v.getId()) {
            case R.id.customer_group:
                if (mGroupPop != null && !mGroupPop.isShowing()) {
                    mGroupPop.showAsDropDown(mGroup, (mGroup.getWidth() - 150) / 2, 0);
                }
                break;
            case R.id.customer_level:
                if (mLevelPop != null && !mLevelPop.isShowing()) {
                    mLevelPop.showAsDropDown(mLevel, (mLevel.getWidth() - 150) / 2, 0);
                }
                break;
            case R.id.customer_type:

                break;
            case R.id.ib_scan_code:
                DisplayServiceManager.doCancel(getActivity());
                mSearchEdit.setText("");
                clearEditTextFocus();
                scanCode();
                break;
            case R.id.ib_post_card:
                mSearchEdit.setText("");
                clearEditTextFocus();
                mSearchType = SEARCHTYPE_POSTCARD;
                postCard();
                break;
            case R.id.tvSearch_Customer:
                MobclickAgentEvent.onEvent(UserActionCode.GK010002);
                clearEditTextFocus();
                String condiction = mSearchEdit.getText().toString();
                if (!TextUtils.isEmpty(condiction)) {
                    mSearchCondition = condiction;
                    refreshData();
                }
                break;
            case R.id.ivCreate_Customer:
                MobclickAgentEvent.onEvent(UserActionCode.GK010002);
                VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_CREATE,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                BeautyCustomerEditActivity_.IntentBuilder_ senderOrder = BeautyCustomerEditActivity_.intent(getActivity());
                                senderOrder.start();
                            }
                        });
                break;

        }
    }

    private void loginByOpenid(final String cardNum) {
        YFResponseListener<YFResponse<CustomerLoginResp>> listener = new YFResponseListener<YFResponse<CustomerLoginResp>>() {

            @Override
            public void onResponse(YFResponse<CustomerLoginResp> response) {
                if (YFResponse.isOk(response)) {
                    CustomerLoginResp resp = response.getContent();
                    if (resp != null) {
                        if (!TextUtils.isEmpty(resp.getOpenId())) {
                            mSearchCondition = resp.getOpenId();
                            mSearchType = SEARCHTYPE_SCANCODE;
                            refreshData();
                        } else {
                            ToastUtil.showShortToast(getString(R.string.customer_get_openid_empty));
                        }
                    } else {
                        ToastUtil.showShortToast(getString(R.string.customer_get_openid_error));
                    }
                } else {
                    String msg = response.getContent() == null ? getString(R.string.display_login_error) : response.getMessage();
                    ToastUtil.showShortToast(msg);
                }
                UserActionEvent.end(UserActionEvent.CUSTOMER_QUERY_DATA_BY_LOGIN);
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };

        CustomerManager.getInstance().customerLogin(CustomerLoginType.WECHAT_MEMBERCARD_ID, cardNum, null, false, true, LoadingYFResponseListener.ensure(listener, getFragmentManager()));
    }


    private void scanCode() {
        scanPopupWindow = new ScanPopupWindow(getActivity(), getString(R.string.sacn_customer_number_desc));
        scanPopupWindow.showAtLocation(ib_postCard, Gravity.NO_GRAVITY, 0, 0);
        scanPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mSearchType = SEARCHTYPE_NOMAL;
            }
        });

        scanPopupWindow.setOnScanBarcodeCallback(new ScanPopupWindow.ScanBarcodeCallback() {
            @Override
            public void onScanBarcode(String barcode) {
                loginByOpenid(barcode);
            }
        });
    }


    private void postCard() {

    }


    public void onEventMainThread(EventCreateOrEditCustomer event) {
        if (event.type == CustomerActivity.PARAM_ADD) {             refreshData();
        } else {             refreshByItem(mCustomerListAdapter.getCurrentPosition(), event.bean);
        }
    }


    public void onEventMainThread(DetailRefreshEvent event) {
        if (event.customer != null) {
            refreshByItem(mCustomerListAdapter.getCurrentPosition(), event.customer.getCustomerListBean());
        }
    }


    private void refreshByItem(int position, CustomerListResp bean) {
        CustomerListResp listResp = mCustomerList.get(position);
        if (listResp != null) {
            mCustomerList.get(position).groupId = bean.groupId;
            mCustomerList.get(position).isDisable = bean.isDisable;
            mCustomerList.get(position).levelId = bean.levelId;
            mCustomerList.get(position).mobile = bean.mobile;
            mCustomerList.get(position).name = bean.name;
            if (bean.hasFaceCode != null) {
                mCustomerList.get(position).hasFaceCode = bean.hasFaceCode;
            }
            mCustomerListAdapter.notifyItemChanged(position);
        }
    }


    private void resetTypeCondiction() {
        mTypeCondition = "";
        mGroupCondition = "";
        mLevelCondition = "";
        mType.setText(getString(R.string.beauty_customer_list_item_title_type));
        mGroup.setText(getString(R.string.beauty_customer_list_item_title_group));
        mLevel.setText(getString(R.string.beauty_customer_list_item_title_level));
    }

    private void setupEditText() {
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            private String oldKeyWord;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String keyWord = s.toString();
                if (!TextUtils.isEmpty(keyWord)) {
                    if (keyWord.length() == 1 && keyWord.contains(" ")) {
                        keyWord = keyWord.replaceAll(" ", "");
                        ToastUtil.showShortToast(getString(R.string.customer_input_empty));
                        mSearchEdit.setText(keyWord);
                        mSearchEdit.setSelection(start);
                    }
                    if (TextUtils.isEmpty(keyWord) || !TextUtils.isEmpty(oldKeyWord) && oldKeyWord.equals(keyWord)) {
                        return;
                    }
                    mIvDelete.setVisibility(View.VISIBLE);
                } else {
                    mIvDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                String keyWord = s.toString();
                if (keyWord.contains(" ")) {
                    oldKeyWord = keyWord.replaceAll(" ", "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                                if (mSearchType == SEARCHTYPE_SCANCODE) {
                    if (scanPopupWindow != null && scanPopupWindow.isShowing()) {
                        scanPopupWindow.dismiss();
                    }
                    return;
                }

                String value = mSearchEdit.getText().toString();
                if (TextUtils.isEmpty(value)) {
                    mSearchCondition = "";
                    tvSearch.setBackgroundResource(R.drawable.bg_customer_search_off);
                } else {
                    tvSearch.setBackgroundResource(R.drawable.bg_customer_search_on);
                }
            }
        });
        mSearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!TextUtils.isEmpty(mSearchEdit.getText().toString())) {
                        mIvDelete.setVisibility(View.VISIBLE);
                    } else {
                        mIvDelete.setVisibility(View.GONE);
                    }
                } else {
                    mIvDelete.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 0:
                return queryCustomerLevel();
            case 1:
                return queryCustomerGroup();
        }
        return null;
    }


    private Loader<Cursor> queryCustomerGroup() {
        String orderBy = CustomerGroupLevel.$.id + " ASC";
        return new CursorLoader(getActivity(), DBHelperManager.getUri(CustomerGroupLevel.class), null,
                null, null, orderBy);
    }


    private Loader<Cursor> queryCustomerLevel() {
        return new CursorLoader(getActivity(), DBHelperManager.getUri(CustomerLevel.class), null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        switch (arg0.getId()) {
            case 0:
                levelListAddData(cursor);
                setLevelforAdapter();
                initPopWinHandler.sendEmptyMessage(1);
                break;
            case 1:
                groupListAddData(cursor);
                setGroupForAdapter();
                initPopWinHandler.sendEmptyMessage(2);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    private void setGroupForAdapter() {
        TreeMap<String, String> groupMap = new TreeMap<String, String>();
                for (int i = 1; i < mCustomerGroupList.size(); i++) {
            groupMap.put(mCustomerGroupList.get(i).getId(), mCustomerGroupList.get(i).getName());
        }
        mCustomerListAdapter.setGroupMap(groupMap);
    }


    private void setLevelforAdapter() {
        TreeMap<String, String> levelMap = new TreeMap<String, String>();
                for (int i = 1; i < mCustomerLevelList.size(); i++) {

            levelMap.put(mCustomerLevelList.get(i).getId(), mCustomerLevelList.get(i).getName());
        }
        mCustomerListAdapter.setLevelMap(levelMap);
    }


    private void groupListAddData(Cursor cursor) {
        mCustomerGroupList.clear();
                CustomerGroupBean l = new CustomerGroupBean();
        l.setId("");
        l.setName(getString(R.string.customer_group_default));
        mCustomerGroupList.add(l);
                if (cursor != null) {
            while (cursor.moveToNext()) {
                CustomerGroupBean group = new CustomerGroupBean();
                group.setId(cursor.getString(cursor.getColumnIndex(CustomerGroupLevel.$.id)));
                group.setName(cursor.getString(cursor.getColumnIndex(CustomerGroupLevel.$.groupName)));
                group.setUuid(cursor.getString(cursor.getColumnIndex(CustomerGroupLevel.$.uuid)));

                mCustomerGroupList.add(group);
            }
            cursor.close();
        }
        mGroupAdatper = new CustomerPopWindowAdapter(getActivity(), mCustomerGroupList);
    }


    private void levelListAddData(Cursor cursor) {
        mCustomerLevelList.clear();
                CustomerLevelBean l = new CustomerLevelBean();
        l.setId("");
        l.setName(getString(R.string.customer_level_default));
        mCustomerLevelList.add(l);
                if (cursor != null) {
            while (cursor.moveToNext()) {
                CustomerLevelBean level = new CustomerLevelBean();
                level.setId(cursor.getString(cursor.getColumnIndex(CustomerLevel.$.id)));
                level.setLevel(cursor.getString(cursor.getColumnIndex(CustomerLevel.$.level)));
                level.setName(cursor.getString(cursor.getColumnIndex(CustomerLevel.$.name)));
                level.setUuid(cursor.getString(cursor.getColumnIndex(CustomerLevel.$.uuid)));

                mCustomerLevelList.add(level);
            }
            cursor.close();
        }
        mLevelAdatper = new CustomerPopWindowAdapter(getActivity(), mCustomerLevelList);
    }

    @Override
    public void onStop() {
                super.onStop();
    }

    @Override
    public void onDestroyView() {
        mCustomerOperates.cancelQueryCustomerListByCondition();
        getLoaderManager().destroyLoader(0);
        getLoaderManager().destroyLoader(1);
        initPopWinHandler.removeCallbacksAndMessages(null);
        if (thread.isAlive()) {
            thread.quit();
        }
        super.onDestroyView();
    }

    @Override
    public void onLoadMore() {
        if (!mIsSupportLoadMore) {
            refreshOver();
            ToastUtil.showLongToast(getString(R.string.customer_list_load_more_empty));
            return;
        }
        queryCustomerList();
    }

    @Override
    public void onRefresh() {
        refreshAllData();
    }

    @Override
    public void onItemClickListener(CustomerListResp bean) {
        MobclickAgentEvent.onEvent(UserActionCode.GK010003);
        CustomerResp customerNew = new CustomerResp();
        customerNew.customerId = bean.customerId;
        if (replaceListener != null) {
            replaceListener.getCustomer(customerNew);
        }
    }


    private void sendEventRefresh(Long id) {
        EventBus.getDefault().post(new EventRefreshDetail(id));
    }


    private void initRequest() {
        mCurrentPage = 1;
        mIsRefresh = true;         mIsSupportLoadMore = true;
        mQueryTime = null;
        if (mSearchType == SEARCHTYPE_POSTCARD || mSearchType == SEARCHTYPE_SCANCODE) {
            resetTypeCondiction();
        }
    }


    private void scanCardOrWeChat() {
        mSearchType = SEARCHTYPE_NOMAL;
        mIsSupportLoadMore = false;
        mSearchCondition = "";
    }


    private void refreshData() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }


    private void refreshAllData() {
        initRequest();
        queryCustomerList();
    }


    private void queryCustomerList() {
        mCustomerOperates.cancelQueryCustomerListByCondition();
        YFResponseListener<YFResponseList<CustomerListResp>> responseListener = new YFResponseListener<YFResponseList<CustomerListResp>>() {

            @Override
            public void onResponse(YFResponseList<CustomerListResp> response) {
                mIsCache = false;
                if (YFResponseList.isOk(response)) {
                    mQueryTime = response.getPage().getQueryTime();
                    mCurrentPage++;
                    List<CustomerListResp> list = response.getContent();
                    if (list != null && list.size() > 0) {
                        if (mIsRefresh) {                            mCustomerList.clear();
                            mCustomerList.addAll(list);
                            mCustomerListAdapter.notifyDataAll();
                        } else {                            int positionStart = mCustomerList.size();                             mCustomerList.addAll(list);
                            mCustomerListAdapter.notifyItemRangeInserted(positionStart, list.size());
                        }
                        if (mIsRefresh) {
                            mIsRefresh = false;
                        }
                    } else {
                        if (mIsRefresh) {
                            mCustomerList.clear();
                            mCustomerListAdapter.notifyDataAll();
                            ToastUtil.showLongToast(getString(R.string.customer_list_empty));
                        } else {
                            ToastUtil.showLongToast(getString(R.string.customer_list_load_more_empty));
                        }
                    }
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }

                refreshOver();
                UserActionEvent.end(UserActionEvent.CUSTOMER_QUERY_LIST);
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                refreshOver();
                UserActionEvent.end(UserActionEvent.CUSTOMER_QUERY_LIST);
            }
        };
        if (mSearchType == SEARCHTYPE_SCANCODE) {
                        mCustomerOperates.queryCustomerListByCondition(getCustomerListReq(), responseListener);
            scanCardOrWeChat();
        } else if (mSearchType == SEARCHTYPE_POSTCARD) {
                        mCustomerOperates.queryCustomerListByCondition(getCustomerListReq(), responseListener);
            scanCardOrWeChat();
        } else {
            mCustomerOperates.queryCustomerListByCondition(getCustomerListReq(), responseListener);
        }
    }


    private CustomerListReq getCustomerListReq() {
        CustomerListReq bean = new CustomerListReq(mCurrentPage, PAGESIZE);
        bean.brandId = MainApplication.getInstance().getBrandIdenty();        if (mSearchType == SEARCHTYPE_SCANCODE) {
            bean.openId = mSearchCondition;
        } else if (mSearchType == SEARCHTYPE_POSTCARD) {
            bean.cardNum = mSearchCondition;
        } else {
            bean.nameOrMobile = mSearchCondition;
        }
        if (!TextUtils.isEmpty(mGroupCondition)) {
            bean.groupId = mGroupCondition;
        }
        if (!TextUtils.isEmpty(mTypeCondition)) {
            bean.customerType = mTypeCondition;
        }
        if (!TextUtils.isEmpty(mLevelCondition)) {
            bean.levelId = mLevelCondition;
        }
        if (!mIsCache) {             bean.refresh = "Y";
        }
        bean.queryTime = mQueryTime;
        return bean;
    }


    private void refreshOver() {
        if (mCustomerList.size() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
    }
}
