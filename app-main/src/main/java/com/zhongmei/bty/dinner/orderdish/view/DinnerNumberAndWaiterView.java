package com.zhongmei.bty.dinner.orderdish.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.buffet.table.view.PropertyItemView;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.pay.utils.PayUtils;
import com.zhongmei.bty.splash.login.UserDialog;
import com.zhongmei.bty.splash.login.adapter.UserGridAdapter;

import java.util.List;


public class DinnerNumberAndWaiterView extends NumberAndWaiterView implements UserGridAdapter.OnUserSelectedListener, View.OnClickListener {

    protected ImageView ivMinus;
    protected ImageView ivPlus;
    protected EditText etCount;
    protected PropertyItemView piv_waiter;
    protected PropertyItemView piv_saleMan;

    private UserDialog userDialog;

        private AuthUser mSalesman;

    private Integer mGuestCount = null;

    public DinnerNumberAndWaiterView(Context context) {
        super(context);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(mActivity).inflate(R.layout.view_number_and_waiter, this, true);
        ivMinus = (ImageView) findViewById(R.id.minus_customer_iv);
        ivPlus = (ImageView) findViewById(R.id.add_customer_iv);
        etCount = (EditText) findViewById(R.id.customer_num_tv);
        piv_waiter = (PropertyItemView) findViewById(R.id.piv_waiter);
        piv_saleMan = (PropertyItemView) findViewById(R.id.piv_salesman);
        ivMinus.setOnClickListener(this);
        ivPlus.setOnClickListener(this);
        piv_waiter.setOnClickListener(this);
        piv_saleMan.setOnClickListener(this);
        ((View) piv_saleMan.getParent()).setOnClickListener(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                            }
        });
        etCount.addTextChangedListener(textWatcher);

        updateNumberAndWaiter();

    }


    @Override
    public void updateNumberAndWaiter() {

        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        String waiterName = "";
        if (tradeVo != null && tradeVo.getTradeTableList() != null && tradeVo.getTradeTableList().size() > 0) {
            TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
            mGuestCount = tradeTable.getTablePeopleCount();
            if (mGuestCount != null) {
                etCount.setEnabled(true);
                etCount.setText(mGuestCount.toString());
                etCount.setSelection(etCount.getText().length());
            }
            waiterName = tradeTable.getWaiterName();

        } else {
            waiterName = Session.getAuthUser().getName();
            etCount.setText("");
            etCount.setEnabled(false);
        }

        setWaiterName(waiterName);

                if (tradeVo != null && tradeVo.getTradeUser() != null) {
            if (!TextUtils.isEmpty(tradeVo.getTradeUser().getUserName())) {
                setSalesmanText(tradeVo.getTradeUser().getUserName());
                mSalesman = new AuthUser();
                mSalesman.setId(tradeVo.getTradeUser().getUserId());
                mSalesman.setName(tradeVo.getTradeUser().getUserName());
            }
        } else {
            setSalesmanText(this.getResources().getString(R.string.salesman_select_hint));
        }
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
            if (!TextUtils.isEmpty(s.toString()) && Integer.parseInt(s.toString()) > 0) {
                modifyCustomerCount(Integer.parseInt(s.toString()));
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.minus_customer_iv:
                MobclickAgentEvent.onEvent(mActivity, MobclickAgentEvent.dinnerOrderDishNumberMinus);

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
                    modifyCustomerCount(customerNum);
                }

                break;
            case R.id.add_customer_iv:
                MobclickAgentEvent.onEvent(mActivity, MobclickAgentEvent.dinnerOrderDishNumberPlus);

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
                    modifyCustomerCount(customerNum);
                }
                break;
            case R.id.piv_waiter:
                MobclickAgentEvent.onEvent(mActivity, MobclickAgentEvent.dinnerOrderDishModifyWaiter);

                if (isUnionMainTrade()) {
                    ToastUtil.showShortToast(R.string.dinner_table_union_waiter_toast);
                    return;
                }

                VerifyHelper.verifyAlert(mActivity, DinnerApplication.PERMISSION_SELECT_WAITER, new VerifyHelper.Callback() {

                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        List<User> allUserList = Session.getFunc(UserFunc.class).getUsers();
                        User currentUser = null;

                        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
                        if (tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeTableList())) {
                            TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
                            Long userId = tradeTable.getWaiterId();
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
                                currentUser, DinnerNumberAndWaiterView.this);
                        userDialog.show();
                    }
                });
                break;
            case R.id.piv_salesman:                 List<User> allUserList = Session.getFunc(UserFunc.class).getAllSalesman();
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
                        if (DinnerShoppingCart.getInstance().getTradeUser() != null) {
                DinnerShoppingCart.getInstance().getTradeUser().setUserId(userId);
                DinnerShoppingCart.getInstance().getTradeUser().setUserName(userName);
                DinnerShoppingCart.getInstance().getTradeUser().setChanged(true);
            } else {
                                TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
                DinnerShoppingCart.getInstance().setTradeUser(PayUtils.creatTradeUser(tradeVo.getTrade(), mSalesman));
            }
        }
    };


    private void modifyCustomerCount(int customerNum) {
        DinnerShoppingCart.getInstance().modifyCustomerCount(customerNum);
        if (onChangeListener != null) {
            onChangeListener.onDataChanged();
        }
    }


    private void setWaiterName(String waiterName) {
        String waiterTitle = String.format(getResources().getString(R.string.buffet_waiter), waiterName);
        piv_waiter.setTitleText(waiterTitle);
    }


    private void setSalesmanText(String name) {
        String waiterTitle = String.format(getResources().getString(R.string.buffet_salesman), name);
        piv_saleMan.setTitleText(waiterTitle);
    }

    @Override
    public void onSelected(User item, Long userId, String userName) {

        setWaiterName(userName);

        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeTableList())) {
            TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
            tradeTable.setWaiterId(userId);
            tradeTable.setWaiterName(userName);
            tradeTable.setChanged(true);

            if (onChangeListener != null) {
                onChangeListener.onDataChanged();
            }
        }
    }

    private boolean isUnionMainTrade() {
        Trade trade = DinnerShoppingCart.getInstance().getOrder().getTrade();

        if (trade != null) {
            return trade.getTradeType() == TradeType.UNOIN_TABLE_MAIN;
        }

        return false;
    }
}


