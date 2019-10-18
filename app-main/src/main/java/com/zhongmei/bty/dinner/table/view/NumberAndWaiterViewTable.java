package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.buffet.table.view.PropertyItemView;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.util.DinnerMobClickAgentEvent;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.dinner.table.manager.ChangeTableManager;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.pay.utils.PayUtils;
import com.zhongmei.bty.splash.login.UserDialog;
import com.zhongmei.bty.splash.login.adapter.UserGridAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@EViewGroup(R.layout.view_number_and_waiter_table)
public class NumberAndWaiterViewTable extends LinearLayout implements UserGridAdapter.OnUserSelectedListener {

    private FragmentActivity mActivity;

    @ViewById(R.id.minus_customer_iv)
    protected ImageView ivMinus;
    @ViewById(R.id.add_customer_iv)
    protected ImageView ivPlus;
    @ViewById(R.id.customer_num_tv)
    protected EditText etCount;
    @ViewById(R.id.piv_waiter)
    protected PropertyItemView piv_waiter;

    @ViewById(R.id.negative_button)
    Button negativeBtn;

    @ViewById(R.id.positive_button)
    Button positiveBtn;

    @ViewById(R.id.piv_salesman)
    protected PropertyItemView piv_saleMan;
    protected BusinessType mBusinessType;

    private UserDialog userDialog;

    private AuthUser mSalesman;

    private AuthUser mWaiter;

    private OnTradeTableChangeListener onTradeTableChangeListener;

    private OnClickListener positiveListener;

    private OnClickListener negetiveListener;

    private DinnertableTradeVo dinnertableTradeVo;

    private TableInfoFragment tableInfoFragment;

    public NumberAndWaiterViewTable(Context context) {
        super(context);
    }

    public NumberAndWaiterViewTable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setActivity(FragmentActivity activity) {
        mActivity = activity;
    }

    public void setOnTradeTableChangeListener(OnTradeTableChangeListener onTradeTableChangeListener) {
        this.onTradeTableChangeListener = onTradeTableChangeListener;
    }

    @AfterViews
    protected void initViews() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                            }
        });
        etCount.addTextChangedListener(textWatcher);

            }

    public void setmBusinessType(BusinessType mBusinessType) {
        this.mBusinessType = mBusinessType;
    }

    public void updateNumberAndWaiter() {
        TradeVo tradeVo = dinnertableTradeVo.getTradeVo();
        String waiterName = "";
        Long waiterId = null;
        if (tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeTableList())) {
            setViewEnable(true);
            TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
            Integer count = tradeTable.getTablePeopleCount();
            if (count != null) {
                etCount.setText(count.toString());
            }
            waiterName = tradeTable.getWaiterName();
            waiterId = tradeTable.getWaiterId();
        } else {            setViewEnable(false);
            etCount.setText("");
            waiterName = Session.getAuthUser().getName();
            waiterId = Session.getAuthUser().getId();

        }

        mWaiter = new AuthUser();
        mWaiter.setName(waiterName);
        mWaiter.setId(waiterId);

        String waiterTitle = String.format(getResources().getString(R.string.buffet_waiter), waiterName != null ? waiterName : "");
        piv_waiter.setTitleText(waiterTitle);

        initSalesman(tradeVo);     }


    private void setViewEnable(boolean enable) {
        etCount.setEnabled(enable);
    }

    private void setSalesmanText(String name) {
        String waiterTitle = String.format(getResources().getString(R.string.buffet_salesman), name);
        piv_saleMan.setTitleText(waiterTitle);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Click({R.id.minus_customer_iv, R.id.add_customer_iv, R.id.piv_waiter, R.id.piv_salesman, R.id.positive_button, R.id.negative_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.minus_customer_iv:
                MobclickAgentEvent.onEvent(mActivity, MobclickAgentEvent.dinnerDetailsCustomerDecrease);

                if (isUnionMainTrade()) {
                    ToastUtil.showShortToast(R.string.dinner_table_union_customer_number_toast);
                    return;
                }

                String countStr = etCount.getText().toString();
                if (TextUtils.isEmpty(countStr)) {
                    ToastUtil.showShortToast(R.string.dinner_table_info_customer_number_toast);
                    return;
                }

                int customerNum = Integer.valueOf(countStr);
                if (customerNum > 1) {
                    customerNum--;
                    etCount.setText(String.valueOf(customerNum));
                    etCount.setSelection(String.valueOf(customerNum).length());
                }

                break;
            case R.id.add_customer_iv:
                MobclickAgentEvent.onEvent(mActivity, MobclickAgentEvent.dinnerDetailsCustomerIncrease);

                if (isUnionMainTrade()) {
                    ToastUtil.showShortToast(R.string.dinner_table_union_customer_number_toast);
                    return;
                }

                countStr = etCount.getText().toString();
                if (TextUtils.isEmpty(countStr)) {
                    ToastUtil.showShortToast(R.string.dinner_table_info_customer_number_toast);
                    return;
                }

                customerNum = Integer.valueOf(countStr);
                if (customerNum < 999) {                    customerNum++;
                    etCount.setText(String.valueOf(customerNum));
                    etCount.setSelection(String.valueOf(customerNum).length());
                }
                break;
            case R.id.piv_waiter:
                MobclickAgentEvent.onEvent(mActivity, DinnerMobClickAgentEvent.tableDetailsModifyCustomerWaiter);

                if (isUnionMainTrade()) {
                    ToastUtil.showShortToast(R.string.dinner_table_union_waiter_toast);
                    return;
                }

                VerifyHelper.verifyAlert(mActivity, DinnerApplication.PERMISSION_SELECT_WAITER, new VerifyHelper.Callback() {

                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        List<User> allUserList = Session.getFunc(UserFunc.class).getUsers();
                        User currentUser = null;

                        TradeVo tradeVo = dinnertableTradeVo.getTradeVo();
                        if (tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeTableList())) {
                            TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
                            Long userId = mWaiter == null ? tradeTable.getWaiterId() : (Long) mWaiter.getId();
                            if (userId != null) {
                                for (User temp : allUserList) {
                                    if (Utils.equals(temp.getId(), userId)) {
                                        currentUser = temp;
                                        break;
                                    }
                                }
                            }
                        }
                        userDialog = new UserDialog(mActivity, R.string.waiterlist_label, allUserList,
                                currentUser, NumberAndWaiterViewTable.this);
                        userDialog.show();
                    }
                });
                break;
            case R.id.positive_button:
                MobclickAgentEvent.onEvent(mActivity, DinnerMobClickAgentEvent.tableDetailsModifyCustomerSure);

                int customerNumber = 0;
                String waiterName = "";
                Long waiterId = null;
                if (etCount.getText() != null && !TextUtils.isEmpty(etCount.getText().toString())) {
                    customerNumber = Integer.valueOf(etCount.getText().toString());
                }


                if (!TextUtils.isEmpty(etCount.getText()) && customerNumber <= 0) {
                    ToastUtil.showShortToast(R.string.hint_buffet_select_customer);
                    return;
                }

                ChangeTableManager manager = new ChangeTableManager(dinnertableTradeVo, customerNumber, mActivity, mBusinessType);
                if (mWaiter != null) {
                    waiterName = mWaiter.getName();
                    waiterId = mWaiter.getId();
                }

                manager.setUser(waiterId, waiterName);
                                TradeVo tradeVo = dinnertableTradeVo.getTradeVo();
                if (mSalesman != null && tradeVo != null) {
                                        if (tradeVo.getTradeUser() != null) {
                        TradeUser tradeUser = tradeVo.getTradeUser();
                        if (tradeUser.getUserId() != null && !tradeUser.getUserId().equals(mSalesman.getId())) {
                            tradeUser.setUserId(mSalesman.getId());
                            tradeUser.setUserName(mSalesman.getName());
                            tradeUser.setChanged(true);
                            manager.setTradeUser(tradeUser);
                        }
                    } else {
                                                manager.setTradeUser(PayUtils.creatTradeUser(tradeVo.getTrade(), mSalesman));
                    }
                }
                                manager.finishChangeTable();
                setVisibility(View.GONE);
                tableInfoFragment.hideNumberAndWaiterView(View.GONE);
                break;
            case R.id.negative_button:
                MobclickAgentEvent.onEvent(mActivity, DinnerMobClickAgentEvent.tableDetailsModifyCustomerCancel);
                setVisibility(View.GONE);
                tableInfoFragment.hideNumberAndWaiterView(View.GONE);
                cancelSelectSalesman();                break;
            case R.id.piv_salesman:                 MobclickAgentEvent.onEvent(mActivity, DinnerMobClickAgentEvent.tableDetailsModifyCustomerTradeuser);
                List<User> allUserList = Session.getFunc(UserFunc.class).getAllSalesman();
                if (Utils.isNotEmpty(allUserList)) {
                    UserDialog userDialog = new UserDialog(mActivity, R.string.salesman_select_hint, allUserList, mSalesman, mItemSelectedListener);
                    userDialog.show();
                } else {
                    ToastUtil.showShortToast(R.string.salesman_nobody_alter_txt);
                }
                break;
            default:

                break;
        }
    }

    UserGridAdapter.OnUserSelectedListener mItemSelectedListener = new UserGridAdapter.OnUserSelectedListener() {

        @Override
        public void onSelected(User item, Long userId, String userName) {
            mSalesman = new AuthUser();
            mSalesman.setId(userId);
            mSalesman.setName(userName);
            setSalesmanText(userName);
        }
    };

    private void cancelSelectSalesman() {
        TradeVo tradeVo = dinnertableTradeVo.getTradeVo();
        if (tradeVo != null) {
            if (tradeVo.getTradeUser() != null) {
                initSalesman(tradeVo);
            } else {
                mSalesman = null;
                setSalesmanText(this.getResources().getString(R.string.salesman_select_hint));
            }
        }
    }

    private void initSalesman(TradeVo tradeVo) {
        if (tradeVo != null && tradeVo.getTradeUser() != null) {
            if (!TextUtils.isEmpty(tradeVo.getTradeUser().getUserName())) {
                mSalesman = new AuthUser();
                mSalesman.setId(tradeVo.getTradeUser().getUserId());
                mSalesman.setName(tradeVo.getTradeUser().getUserName());
                setSalesmanText(mSalesman.getName());
            }
        } else {
            setSalesmanText(this.getResources().getString(R.string.salesman_select_hint));
        }


    }



    @Override
    public void onSelected(User item, Long userId, String userName) {
        mWaiter = new AuthUser();
        mWaiter.setName(userName);
        mWaiter.setId(userId);

        String waiterTitle = String.format(getResources().getString(R.string.buffet_waiter), userName);
        piv_waiter.setTitleText(waiterTitle);
        piv_waiter.setTag(userId);

    }

    public void show(DinnertableTradeVo dinnertableTradeVo, TableInfoFragment tableInfoFragment) {
        this.dinnertableTradeVo = dinnertableTradeVo;
        this.tableInfoFragment = tableInfoFragment;
        updateNumberAndWaiter();
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public interface OnTradeTableChangeListener {
        public void afterTradeTableChanged();
    }

    public void setPositiveListener(OnClickListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public void setNegetiveListener(OnClickListener negetiveListener) {
        this.negetiveListener = negetiveListener;
    }

    private boolean isUnionMainTrade() {
        Trade trade = null;
        if (dinnertableTradeVo != null) {
            trade = dinnertableTradeVo.getTradeVo().getTrade();
        }

        if (trade != null) {
            return trade.getTradeType() == TradeType.UNOIN_TABLE_MAIN;
        }

        return false;
    }
}
