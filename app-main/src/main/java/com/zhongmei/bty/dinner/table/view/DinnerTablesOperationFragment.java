package com.zhongmei.bty.dinner.table.view;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.notifycenter.event.EventSelectDinnertableNotice;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.message.uniontable.BuffetUnionTradeCancelReq;
import com.zhongmei.bty.buffet.table.view.BuffetUnionCancelVo;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.dinner.table.adapter.SelectedTableListAdapter;
import com.zhongmei.bty.dinner.table.manager.BuffetSplitUnionManager;
import com.zhongmei.bty.dinner.table.manager.DinnerCreateUnionManager;
import com.zhongmei.bty.dinner.table.manager.IUnionTrade;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;
import com.zhongmei.bty.splash.login.UserDialog;
import com.zhongmei.bty.splash.login.adapter.UserGridAdapter;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.dinner_tables_operation_fragment)
public class DinnerTablesOperationFragment extends BasicFragment implements View.OnClickListener {

    public static final String TAG = DinnerTablesOperationFragment.class.getSimpleName();

    @ViewById(R.id.lv_tables_list)
    ListView lvTablesListView;

    @ViewById(R.id.tv_cancel_connect)
    TextView tvCancelConnect;

    @ViewById(R.id.tv_create_connect)
    TextView tvCreateConnect;

    @ViewById(R.id.empty_view)
    LinearLayout emptyView;

    private BusinessType mBusinessType;

    SelectedTableListAdapter adapter;

    List<DinnerConnectTablesVo> tablesVoList;//已经选择的桌台

    DinnerCreateUnionManager manager;
    IUnionTrade unionTrade;

    public void setFreshViewListener(FreshViewListener freshViewListener) {
        this.mFreshViewListener = freshViewListener;
    }

    private FreshViewListener mFreshViewListener;

    @AfterViews
    void init() {
        mBusinessType = ((BatchOperationTableDialogFragment) getParentFragment()).getmBusinessType();
        unionTrade = IUnionTrade.newUnion(this, mBusinessType);
        /*if (mBusinessType == BusinessType.BUFFET) {
            manager = new BuffetCreateUnionManager();
        } else*/
        if (mBusinessType == BusinessType.DINNER) {
            manager = new DinnerCreateUnionManager();
        }
        tvCreateConnect.setOnClickListener(this);
        tvCancelConnect.setOnClickListener(this);
        tablesVoList = new ArrayList<>();
        adapter = new SelectedTableListAdapter(getContext());
        adapter.setDeleteListener(new SelectedTableListAdapter.DeleteSelectedTablesListener() {
            @Override
            public void delete(DinnerConnectTablesVo vo) {
                vo.isSelected = false;
                if (tablesVoList.contains(vo)) tablesVoList.remove(vo);
                adapter.setTablesVoList(tablesVoList);
                if (mFreshViewListener != null) {
                    mFreshViewListener.refreshView();
                }
            }
        });
        lvTablesListView.setAdapter(adapter);
        lvTablesListView.setEmptyView(emptyView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void refreshView(List<DinnerConnectTablesVo> tablesVoList) {
        if (tablesVoList == null) return;
        this.tablesVoList = tablesVoList;
        adapter.setTablesVoList(tablesVoList);
    }

    public void refreshView(DinnerConnectTablesVo tablesVo) {
        if (tablesVo == null) return;
        if (tablesVoList.contains(tablesVo)) {
            tablesVoList.remove(tablesVo);
        } else {
            tablesVoList.add(tablesVo);
        }
        adapter.setTablesVoList(tablesVoList);
    }


    private void notifyUnion() {
        DinnerConnectTablesVo tablesVo = tablesVoList.get(0);
        EventBus.getDefault().post(new EventSelectDinnertableNotice(tablesVo.tables.getAreaId(), tablesVo.tables.getId(), null, false));
        if (mFreshViewListener != null) {
            mFreshViewListener.closeView();
        }
    }

    private void notifyCancelUnion() {
        if (mFreshViewListener != null) {
            mFreshViewListener.freshView(tablesVoList);
        }
        tablesVoList.clear();
        adapter.setTablesVoList(tablesVoList);
    }

    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) return;
        switch (v.getId()) {
            case R.id.tv_create_connect:
                if (Utils.isEmpty(tablesVoList)) {
                    ToastUtil.showShortToast(getString(R.string.dinner_union_trade_no_table));
                    return;
                }
                if (tablesVoList.size() < 2) {
                    ToastUtil.showShortToast(MainApplication.getInstance().getString(R.string.dinner_union_trade_limit_one));
                    return;
                }

                for (DinnerConnectTablesVo vo : tablesVoList) {
                    if (!vo.enableUnion()) {
                        ToastUtil.showShortToast(vo.areaName + MainApplication.getInstance().getString(R.string.dinner_cancel_union_trade_tables)
                                + vo.tables.getTableName()
                                + MainApplication.getInstance().getString(R.string.dinner_union_trade_limit_two));
                        return;
                    }
                }


                List<User> allUserList = Session.getFunc(UserFunc.class).getAllSalesman();
                if (Utils.isNotEmpty(allUserList)) {
                    UserDialog userDialog = new UserDialog(getContext(), R.string.salesman_select_hint, allUserList, null, new UserGridAdapter.OnUserSelectedListener() {
                        @Override
                        public void onSelected(User item, Long userId, String userName) {

                            User mSalesman = new User();
                            mSalesman.setId(userId);
                            mSalesman.setName(userName);
                            manager.unionTrade(DinnerTablesOperationFragment.this, mCreatUnionTradesListener, tablesVoList, mSalesman);
                        }
                    });
                    userDialog.show();
                } else {
                    manager.unionTrade(DinnerTablesOperationFragment.this, mCreatUnionTradesListener, tablesVoList, null);
                }
                break;
            case R.id.tv_cancel_connect:
                if (Utils.isEmpty(tablesVoList)) {
                    ToastUtil.showShortToast(getString(R.string.dinner_union_trade_no_table));
                    return;
                }
                if (tablesVoList.size() > 1) {
                    ToastUtil.showShortToast(getString(R.string.dinner_cancel_union_trade_limit_one));
                    return;
                }
                if (tablesVoList.get(0).tradeMainSubRelationList == null || tablesVoList.get(0).tradeMainSubRelationList.size() != 1) {
                    ToastUtil.showShortToast(getString(R.string.dinner_cancel_union_trade_limit_two));
                    return;
                }

                if (mBusinessType == BusinessType.BUFFET) {
                    final BuffetSplitUnionManager splitTool = new BuffetSplitUnionManager();
                    new AsyncTask<Void, Void, BuffetUnionTradeCancelReq>() {
                        @Override
                        protected BuffetUnionTradeCancelReq doInBackground(Void... params) {
                            return splitTool.createUnionTradeSplitReq(tablesVoList);
                        }

                        protected void onPostExecute(BuffetUnionTradeCancelReq req) {
                            if (req == null) {
                                String errorMsg = tablesVoList.get(0).areaName
                                        + BaseApplication.getInstance().getString(R.string.dinner_cancel_union_trade_tables)
                                        + tablesVoList.get(0).tables.getTableName()
                                        + BaseApplication.getInstance().getString(R.string.dinner_cancel_union_trade_limit_three);
                                ToastUtil.showShortToast(errorMsg);
                                return;
                            }

                            TradeVo mainTradeVo = splitTool.getMainTradeVo();
                            //判断押金是否支持
                            if (mBusinessType == BusinessType.BUFFET && mainTradeVo.isNeedToPayDeposit() && mainTradeVo.isPaidTradeposit()) {
                                ToastUtil.showShortToast(R.string.buffet_union_cancel_error_nocancel);
                                return;
                            }

                            if (mainTradeVo.getMealShellVo() != null) {
                                if (splitTool.getTradeMainSubRelationList().size() > 1) {
                                    /*UnionCancelDialogFragment.show(getActivity(), mainTradeVo, new UnionCancelDialogFragment.OnDialogListener() {
                                        @Override
                                        public void onKnow(BuffetUnionCancelVo unionCancelVo) {
                                            cancelUnionTrade(unionCancelVo);
                                        }
                                    });*/
                                } else {
                                    BuffetUnionCancelVo unionCancelVo = BuffetUnionCancelVo.createBuffetUnionCancelVo(mainTradeVo);
                                    cancelUnionTrade(unionCancelVo);
                                }
                                return;
                            }

                            cancelUnionTrade(null);
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    cancelUnionTrade(null);
                }
                break;
            default:
                break;
        }
    }

    private void cancelUnionTrade(BuffetUnionCancelVo unionCancelVo) {
        unionTrade.cancel(tablesVoList, unionCancelVo, new IUnionTrade.UnionListener() {
            @Override
            public void onResponse(Throwable error, ResponseObject<?> result) {
                if (error != null) {
                    ToastUtil.showShortToast(error.getMessage());
                    return;
                }

                if (ResponseObject.isOk(result)) {
                    notifyCancelUnion();
                    ToastUtil.showLongToast(result.getMessage());
                } else {
                    ToastUtil.showLongToast(result.getMessage());
                }
            }
        });
    }

    public interface FreshViewListener {
        void freshView(List<DinnerConnectTablesVo> vos);

        void closeView();

        void refreshView();
    }

    ResponseListener<TradeResp> mCreatUnionTradesListener = new ResponseListener<TradeResp>() {

        @Override
        public void onResponse(ResponseObject<TradeResp> response) {
            // 下单成功
            try {
                if (ResponseObject.isOk(response)) {
                    notifyUnion();
                    ToastUtil.showLongToast(response.getMessage());
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);

            }
        }

        @Override
        public void onError(VolleyError error) {
            try {
                ToastUtil.showLongToast(error.getMessage());

            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };

}
