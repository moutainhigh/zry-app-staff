package com.zhongmei.bty.queue.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.auth.application.FastFoodApplication;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.bty.basemodule.commonbusiness.enums.TradeRelatedType;
import com.zhongmei.bty.basemodule.commonbusiness.listener.SimpleResponseListener;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.data.operates.QueueOperates;
import com.zhongmei.bty.data.operates.message.content.QueueReq;
import com.zhongmei.bty.data.operates.message.content.QueueResp;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.queue.adapter.AreaPagerAdapter;
import com.zhongmei.bty.queue.adapter.IdleTableAdapter;
import com.zhongmei.bty.queue.adapter.QueueAreaPagerAdapter;
import com.zhongmei.bty.queue.event.EventIsCanClick;
import com.zhongmei.bty.queue.event.IdleTableCountChangeEvent;
import com.zhongmei.bty.queue.event.QueueShowChooseTableEvent;
import com.zhongmei.bty.queue.event.TableSelectEvent;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.bty.queue.vo.QueueAreaVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Author：LiuYang
 * Date：2016/6/27 15:30
 * E-mail：liuy0
 */

@EFragment(R.layout.idle_table_layout)
public class QueueTableSelectFragment extends BasicFragment {
    private static final String TAG = QueueTableSelectFragment.class.getName();

    public static final int OPEN_TYPE_REPLACE = 2;
    public static final int OPEN_TYPE_SUPERNATANT = 3;
    private Context context;//上下文对象

    @ViewById(R.id.ll_main_layout)
    LinearLayout mMainLayout;

    @ViewById(R.id.gv_table)
    protected GridView gvTable;

    @ViewById(R.id.tv_queue_name)
    protected TextView tvQueueName;

    @ViewById(R.id.arealine_layout)
    protected ViewPager lineLayout;

    @ViewById(R.id.idle_table_buttom_button)
    protected LinearLayout buttomButtonLayout;

    private IdleTableAdapter tableAdapter;

    private QueueAreaPagerAdapter areaAdapter;

    // 当前选择的区域
    private CommercialArea currentArea;

    private List<QueueAreaVo> areaVoList;

    private int currentIndex;

    private TablesDal tablesDal;

    private List<Tables> emptTabls;

    private Tables currentTable;

    private TableSelectEvent currentTableSelect = new TableSelectEvent(0, 0);

    private NewQueueBeanVo queueVo;//关联当前操作的排队对象

    private int openType = OPEN_TYPE_REPLACE;

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tablesDal = OperatesFactory.create(TablesDal.class);
    }

    public void setMainLayout() {
        mMainLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mMainLayout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        mMainLayout.setLayoutParams(params);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    @AfterViews
    protected void loadLayout() {
        if (openType == OPEN_TYPE_SUPERNATANT) {
            setMainLayout();
        }
        setWindowModel(false);
        registerEventBus();
        initGridView();
        lineLayout.setAdapter(areaAdapter);
        areaVoList = new ArrayList<>();
        emptTabls = new ArrayList<>();
        TaskContext.bindExecute(this, new SimpleAsyncTask<Void>() {
            @Override
            public Void doInBackground(Void... params) {
                initData();
                return null;
            }

            @Override
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // 选中第一个
                areaAdapter.setDataSet(areaVoList);
                if (areaVoList != null && areaVoList.size() > 0) {
                    selecedArea(areaVoList.get(0).getArea());
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            lineLayout.setCurrentItem(0); //Where "2" is the position you want to go
                        }
                    });
                }
                areaAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initGridView() {
        ViewGroup.LayoutParams params = gvTable.getLayoutParams();
        params.width = DensityUtil.dip2px(getContext(), 107) * 3 + DensityUtil.dip2px(getContext(), 12) * 2;
        gvTable.setLayoutParams(params);
        tableAdapter = new IdleTableAdapter(getActivity()) {
            @Override
            public void selectTable(boolean isSelected, Tables table) {
                selectedTable(isSelected, table);
            }

            @Override
            public void selectTables(List<Tables> tables) {

            }
        };

        areaAdapter = new QueueAreaPagerAdapter(getActivity()) {
            @Override
            public void selectArea(CommercialArea area) {
                selecedArea(area);
            }
        };

        gvTable.setAdapter(tableAdapter);
    }

    private void initData() {
        try {
            List<Tables> tables = tablesDal.listDinnerEmptyTablesByStatus(TableStatus.EMPTY);
            emptTabls.clear();
            emptTabls.addAll(tables);

            List<CommercialArea> areaList = tablesDal.listDinnerArea();
            areaVoList.clear();
            for (CommercialArea area : areaList) {
                QueueAreaVo vo = new QueueAreaVo();
                vo.setArea(area);
                areaVoList.add(vo);
            }
            initAreaTables(areaVoList, emptTabls);
            //initAreaTables(areaVoList);
            //通知显示空闲的桌台数
            EventBus.getDefault().post(new IdleTableCountChangeEvent(emptTabls == null ? 0 : emptTabls.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
        super.onDestroy();
    }

    public void setQueueObj(NewQueueBeanVo queueVo) {
        this.queueVo = queueVo;
    }

    public void setWindowModel(boolean on) {
        if (on)
            buttomButtonLayout.setVisibility(View.VISIBLE);
        else
            buttomButtonLayout.setVisibility(View.GONE);
    }

    /**
     * 重置UI
     */
    public void resetUI() {
        tableAdapter.reset();
        currentTable = null;
        if (areaVoList != null && areaVoList.size() > 0) {
            selecedArea(areaVoList.get(0).getArea());
        }
    }

    /**
     * 选中桌台
     *
     * @param table
     */
    private void selectedTable(boolean isSelected, Tables table) {
        if (isSelected)
            currentTable = table;
        else
            currentTable = null;
    }

    /**
     * 选中区域
     *
     * @param area
     */
    private void selecedArea(CommercialArea area) {
        if (areaVoList == null) {
            return;
        }
        // 计算当前队列在第几页
        int index = 0;
        for (int i = 0; i < areaVoList.size(); i++) {
            if (area.getId() != null && area.getId().equals(areaVoList.get(i).getArea().getId())) {
                index = i;
                break;
            }
        }
        //currentArea放到这里赋值，防止传入的area不存在
        if (areaVoList.size() > 0) {
            currentArea = areaVoList.get(index).getArea();
            currentIndex = (index) / AreaPagerAdapter.PAGE_SIZE;
            lineLayout.setCurrentItem(currentIndex);
            // 修改队列选中状态
            for (QueueAreaVo vo : areaVoList) {
                if (vo.getArea().getId().equals(currentArea.getId())) {
                    vo.setSelected(true);
                    if (vo.getTablesList() != null) {
                        if (currentTableSelect.maxPersonCount == 0) {
                            tableAdapter.setData(vo.getTablesList());
                            break;
                        } else if (currentTableSelect.maxPersonCount == -1) {
                            List<Tables> tmp = new ArrayList<>();
                            for (Tables tables : vo.getTablesList()) {
                                if (tables.getTablePersonCount() >= currentTableSelect.minPersonCount)
                                    tmp.add(tables);
                            }
                            tableAdapter.setData(tmp);
                        } else {
                            List<Tables> tmp = new ArrayList<>();
                            for (Tables tables : vo.getTablesList()) {
                                if (tables.getTablePersonCount() >= currentTableSelect.minPersonCount
                                        && tables.getTablePersonCount() <= currentTableSelect.maxPersonCount)
                                    tmp.add(tables);
                            }
                            tableAdapter.setData(tmp);
                        }
                    }
                } else {
                    vo.setSelected(false);
                }
            }
            areaAdapter.notifyDataSetChanged();
        }
    }

    private void initAreaTables(List<QueueAreaVo> queueAreaVos) {
        if (queueAreaVos == null)
            return;
        for (QueueAreaVo vo : queueAreaVos) {
            if (vo.getArea() == null)
                continue;

            try {
                vo.setTablesList(tablesDal.listTablesByAreaId(vo.getArea().getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initAreaTables(List<QueueAreaVo> queueAreaVos, List<Tables> tables) {
        if (queueAreaVos == null || tables == null) {
            return;
        }
        for (Tables table : tables) {
            for (QueueAreaVo vo : queueAreaVos) {
                if (vo.getArea() == null)
                    continue;
                if (vo.getArea().getId().equals(table.getAreaId())) {
                    vo.getTablesList().add(table);
                }
            }
        }
    }

    /**
     * 收到桌台状态更新消息
     */
    public void onEventMainThread(final List<Tables> tables) {
        TaskContext.bindExecute(this, new SimpleAsyncTask<Void>() {
            @Override
            public Void doInBackground(Void... params) {
                try {
                    emptTabls.clear();
                    emptTabls.addAll(tables);
                    List<CommercialArea> areaList = tablesDal.listDinnerArea();
                    areaVoList.clear();
                    for (CommercialArea area : areaList) {
                        QueueAreaVo vo = new QueueAreaVo();
                        vo.setArea(area);
                        areaVoList.add(vo);
                    }
                    initAreaTables(areaVoList, emptTabls);
                    //initAreaTables(areaVoList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                areaAdapter.setDataSet(areaVoList);
                selecedArea(currentArea);
            }
        });
    }


    public void onEventMainThread(EventIsCanClick event) {
        if (event != null) {
            areaAdapter.setCanClick(event.getCanClick());
        }
    }

    public void onEventMainThread(TableSelectEvent event) {
        currentTableSelect = event;
        if (areaAdapter != null) {
            areaAdapter.setCurrentTableSelect(currentTableSelect);
        }
        if (currentTableSelect.maxPersonCount == 0)
            tvQueueName.setText(String.format(getString(R.string.queue_empty_table_title_hint), getString(R.string.queue_line_name_all)));
        else
            tvQueueName.setText(String.format(getString(R.string.queue_empty_table_title_hint), event.name));
        selecedArea(currentArea);
    }

    @Click({R.id.idle_table_entry_button, R.id.idle_table_entry_start_button, R.id.queue_area_pre, R.id.queue_area_next})
    public void click(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.idle_table_entry_button:
                MobclickAgentEvent.onEvent(UserActionCode.PD020011);
                if (queueVo != null) {
                    Queue queue = queueVo.getQueue();
                    updateQueueStatus(QueueReq.Type.IN, queue);//入场请求营销发券和实时更新微信排队状态;
                }
                break;
            case R.id.idle_table_entry_start_button:
                MobclickAgentEvent.onEvent(UserActionCode.PD020012);
                boolean dinnerPermission = VerifyHelper.verify(DinnerApplication.PERMISSION_DINNER);
                boolean fastFoodPermission = VerifyHelper.verify(FastFoodApplication.PERMISSION_FASTFOOD);
                if (dinnerPermission) {
                    if (currentTable == null) {
                        ToastUtil.showLongToast(R.string.queue_select_table_first);
                        return;
                    }
                    if (queueVo != null && queueVo.getTradeVo() != null && queueVo.getTradeVo().getTrade() != null && queueVo.getTradeVo().getTrade().getId() != null) {//含有预点菜,调用改单接口
                        try {
                            TradeVo tradeVo = OperatesFactory.create(TradeDal.class).findTradeById(queueVo.getTradeVo().getTrade().getId());
                            if (tradeVo.getTrade() != null && tradeVo.getTrade().getBusinessType() != BusinessType.DINNER) {
                                ToastUtil.showLongToast(R.string.queue_fastfood_unable_opentable);
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //开始请求开台权限
                    VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_CREATE,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    openTable();
                                }
                            });
                } else if (fastFoodPermission) {
                    ToastUtil.showLongToast(R.string.queue_fastfood_unable_opentable);
                } else {
                    ToastUtil.showLongToast(R.string.queue_no_permission);
                }
                break;
            case R.id.queue_area_pre:
                if (currentIndex > 0) {
                    currentIndex--;
                    lineLayout.setCurrentItem(currentIndex);
                }
                break;
            case R.id.queue_area_next:
                if (currentIndex < areaAdapter.getCount() - 1) {
                    currentIndex++;
                    lineLayout.setCurrentItem(currentIndex);
                }
            default:
                break;
        }
    }

    /**
     * 更新队列状态
     */
    private void updateQueueStatus(int type, final Queue queue) {
        updateQueueStatus(type, queue, null);
    }

    private void updateQueueStatus(int type, final Queue queue, final String tradeUuid) {
        DisplayServiceManager.doUpdateQueue(context);//更新第二屏排队队列

        QueueOperates operates = OperatesFactory.create(QueueOperates.class);
        ResponseListener<QueueResp> listener = new ResponseListener<QueueResp>() {

            @Override
            public void onResponse(ResponseObject<QueueResp> response) {
                dismissLoadingProgressDialog();
                if (ResponseObject.isOk(response)) {
                    ToastUtil.showLongToast(R.string.booking_request_success);
                    //入场成功，通知关闭选择桌台界面
                    EventBus.getDefault().post(new QueueShowChooseTableEvent(false, null));
                    if (tradeUuid != null) {
                        startDinnerMainActivity(tradeUuid);
                    }
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                dismissLoadingProgressDialog();
                ToastUtil.showLongToast(error.getMessage());
            }
        };
        operates.updateQueue(type, queue, listener);
    }

    private void startDinnerMainActivity(String tradeUuid) {
        /*IntentOrderUtil.startDinnerMainActivity(getActivity(), tradeUuid, new IntentOrderUtil.OnStartActivityCallback() {
            @Override
            public void onStartActivity(boolean success) {
                dismissLoadingProgressDialog();
            }
        });*/
    }

    //开始进行开台
    private void openTable() {
        TradeVo tradeVo;
        if (queueVo.getTradeVo() != null && queueVo.getTradeVo().getTrade() != null && queueVo.getTradeVo().getTrade().getId() != null) {//含有预点菜,调用改单接口
            try {
                tradeVo = OperatesFactory.create(TradeDal.class).findTradeById(queueVo.getTradeVo().getTrade().getId());
                if (tradeVo.getTrade() != null && tradeVo.getTrade().getBusinessType() != BusinessType.DINNER) {
                    ToastUtil.showLongToast(R.string.queue_fastfood_unable_opentable);
                    return;
                }
                queueVo.setTradeVo(tradeVo);
                changeTable(tradeVo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showLoadingProgressDialog();
            Long customerId = queueVo.getQueue().getCustomerId();
            CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
            customerOperates.getCustomerById(customerId, false, new SimpleResponseListener<com.zhongmei.bty.basemodule.customer.message.CustomerResp>() {

                @Override
                public void onSuccess(ResponseObject<com.zhongmei.bty.basemodule.customer.message.CustomerResp> response) {
                    CustomerResp customer = response.getContent().result;
                    openTable(customer);
                }

                @Override
                public void onError(VolleyError error) {
                    openTable(null);
                }
            });
        }
    }

    private void openTable(CustomerResp customer) {
        TradeVo tradeVo;//不含有预点菜，调用下单接口
        TradeTable tradeTable = new TradeTable();
        tradeTable.setTableId(currentTable.getId());
        tradeTable.setTableName(currentTable.getTableName());
        tradeTable.validateCreate();
        String uuid = SystemUtils.genOnlyIdentifier();
        tradeTable.setUuid(uuid);
        tradeTable.setTablePeopleCount(queueVo.getQueue().getRepastPersonCount());
        tradeTable.setMemo("");
        //需要获取waiterId
        AuthUser user = Session.getAuthUser();
        tradeTable.setWaiterId(user.getId());
        tradeTable.setWaiterName(user.getName());

        DinnerShoppingCart shoppingCart = new DinnerShoppingCart();
        shoppingCart.openTable(tradeTable);
        shoppingCart.setOrderBusinessType(shoppingCart.getShoppingCartVo(), BusinessType.DINNER);
        shoppingCart.setOrderType(shoppingCart.getShoppingCartVo(), DeliveryType.HERE);

        DinnertableModel dinnertableModel = new DinnertableModel();
        dinnertableModel.setId(currentTable.getId());
        tradeVo = shoppingCart.createOrder(shoppingCart.getShoppingCartVo(), false);
        tradeVo.setRelatedType(TradeRelatedType.QUEUE.value());
        tradeVo.setRelatedId(queueVo.getQueue().getId());
        tradeVo.getTrade().setTradeMemo(queueVo.getQueue().getMemo());

        //添加会员信息
        if (customer != null) {
            if (tradeVo.getTradeCustomerList() == null) {
                tradeVo.setTradeCustomerList(new ArrayList<TradeCustomer>());
            }
            //先写预订顾客
            TradeCustomer bookingCustomer = CustomerManager.getInstance().getTradeCustomer(customer);
            bookingCustomer.setUuid(SystemUtils.genOnlyIdentifier());
            bookingCustomer.setTradeUuid(tradeVo.getTrade().getUuid());
            bookingCustomer.setCustomerType(CustomerType.BOOKING);
            tradeVo.getTradeCustomerList().add(bookingCustomer);
            //如果是会员，写上会员信息
            //if (customer.isMember()) {
            TradeCustomer memberCustomer = CustomerManager.getInstance().getTradeCustomer(customer);
            memberCustomer.setUuid(SystemUtils.genOnlyIdentifier());
            memberCustomer.setTradeUuid(tradeVo.getTrade().getUuid());
            memberCustomer.setCustomerType(customer.isMember() ? CustomerType.MEMBER : CustomerType.CUSTOMER);
            tradeVo.getTradeCustomerList().add(memberCustomer);
            //}
        }

        queueVo.setTradeVo(tradeVo);
        openTable(tradeVo, dinnertableModel);
    }

    /**
     * 开台接口
     */
    private void openTable(final TradeVo mTradeVo, IDinnertable dinnertable) {
        TradeOperates mTradeOperates = OperatesFactory.create(TradeOperates.class);
        ResponseListener<TradeResp> listener = new ResponseListener<TradeResp>() {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                if (ResponseObject.isOk(response)) {
                    updateQueueStatus(QueueReq.Type.IN, queueVo.getQueue(), mTradeVo.getTrade().getUuid());//入场实时更新排队状态;
                } else {
                    dismissLoadingProgressDialog();
                    if (response != null && !TextUtils.isEmpty(response.getMessage())) {
                        ToastUtil.showShortToast(response.getMessage());
                    } else {
                        ToastUtil.showShortToast(R.string.diner_submit_failed);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                dismissLoadingProgressDialog();
                ToastUtil.showShortToast(error.getMessage());
            }
        };

        mTradeOperates.insertDinner(mTradeVo, dinnertable, listener);
    }

    /**
     * 修改开台接口
     */
    private void changeTable(final TradeVo mTradeVo) {
        showLoadingProgressDialog();
        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        tradeOperates.dinnerSetTableAndAccept(mTradeVo, currentTable, Bool.NO, new ResponseListener<TradeResp>() {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                if (ResponseObject.isOk(response)) {
                    updateQueueStatus(QueueReq.Type.IN, queueVo.getQueue(), mTradeVo.getTrade().getUuid());//入场实时更新排队状态;
                } else {
                    dismissLoadingProgressDialog();
                    if (response != null && !TextUtils.isEmpty(response.getMessage())) {
                        ToastUtil.showShortToast(response.getMessage());
                    } else {
                        ToastUtil.showShortToast(R.string.diner_submit_failed);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                dismissLoadingProgressDialog();
                ToastUtil.showLongToast(error.getMessage());
            }

        });
    }
}
