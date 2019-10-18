package com.zhongmei.bty.snack.orderdish;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.bty.basemodule.customer.message.MemberLoginVoResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerV5;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLDResponse;
import com.zhongmei.bty.basemodule.devices.liandipos.PosConnectManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardLoginResp;
import com.zhongmei.bty.basemodule.devices.mispos.dialog.ReadKeyboardDialogFragment;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.bty.common.adpter.ViewHolder;
import com.zhongmei.bty.common.adpter.abslistview.CommonAdapter;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.commonmodule.util.MD5;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;


public class CustomerSelectDialog extends BasicDialogFragment implements View.OnClickListener {

    private static final String TAG = CustomerSelectDialog.class.getSimpleName();

    private ListView mListView;

    private List<String> mAccounts;

    private CommonAdapter<String> mAdapter;
    private boolean hasLogined = false;     private ResponseListener<MemberLoginVoResp> mPhoneLoginListener;
    private ResponseListener<CardLoginResp> mCardLoginListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccounts = CustomerManager.getInstance().getAccounts();
    }

    public void setResponseListener(ResponseListener<MemberLoginVoResp> phoneListener, ResponseListener<CardLoginResp> cardListener) {
        mPhoneLoginListener = phoneListener;
        mCardLoginListener = cardListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View view = inflater.inflate(R.layout.customer_select_dialog_layout, container);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        mListView = (ListView) view.findViewById(R.id.lv_cards);
        initList();
        return view;
    }

    public void setHasLogined(boolean hasLogined) {
        this.hasLogined = hasLogined;
    }

    private void initList() {
        mAdapter = new CommonAdapter<String>(getActivity(), R.layout.custom_select_item_layout, mAccounts) {
            @Override
            public void convert(ViewHolder holder, String s) {
                try {
                    final JSONObject jsonObject = new JSONObject(s);
                    holder.setText(R.id.tv_card_name, jsonObject.getString("name"));
                    holder.setText(R.id.tv_card_no, jsonObject.getString("num"));
                    holder.setOnClickListener(R.id.card_layout_item, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                int type = jsonObject.getInt("type");
                                String num = jsonObject.getString("num");
                                String mobile = jsonObject.optString("mobile");
                                if (type == 1) {
                                    loginByPhoneNo(num, 2, null, null);
                                } else {
                                    if (hasLogined) {
                                        loginByCardNoWithoutPass(mobile, num);
                                    } else {
                                        loginByCardNoCheckPwd(mobile, num, getActivity());
                                    }
                                }

                            } catch (JSONException e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                    });
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                }
            }
        };
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onDestroy() {
        if (CustomerManager.getInstance().getLoginCustomer() == null) {
            DisplayServiceManager.doCancel(getActivity());
        }
        super.onDestroy();
    }


    private void loginByPhoneNo(final String inputNo, final int needPswd, final String pswd, final PasswordDialog dialog) {
        if (mPhoneLoginListener == null) {
            mPhoneLoginListener = new PhotoLoginListener(inputNo, needPswd, pswd, dialog);
        }
        CustomerManager.getInstance().customerLogin(CustomerLoginType.MOBILE, inputNo, pswd, needPswd == 1, false, true, LoadingResponseListener.ensure(mPhoneLoginListener, getFragmentManager()));
    }


    private void loginByCardNo(String inputNo) {
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        if (mCardLoginListener == null) {
            mCardLoginListener = new CardLoginListener();
        }
        ResponseListener<CardLoginResp> listener = LoadingResponseListener.ensure(mCardLoginListener, getFragmentManager());
        operates.cardLogin(inputNo, listener);
    }


    private void loginCheckpwd(final PasswordDialog pwDlg, final String mobile, final String inputNo, String pwd) {
        ResponseListener<MemberLoginVoResp> listener = new ResponseListener<MemberLoginVoResp>() {
            @Override
            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                try {
                    if (ResponseObject.isOk(response) && MemberLoginVoResp.isOk(response.getContent())) {
                        if (pwDlg != null && pwDlg.isShowing()) {
                            pwDlg.dismiss();
                        }
                        final CustomerLoginResp resp = response.getContent().getResult();
                        if (resp.customerIsDisable()) {                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                            return;
                        }
                        loginByCardNo(inputNo);
                    } else {
                        if (pwDlg != null && pwDlg.isShowing()) {
                            pwDlg.clean();
                        }

                        String msg;
                        if (response.getContent() != null) {
                            msg = response.getContent().getErrorMessage();
                        } else {
                            msg = getString(R.string.display_login_error);
                        }
                        ToastUtil.showLongToast(msg);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };
        CustomerManager.getInstance().customerLogin(CustomerLoginType.MOBILE,
                mobile, pwd, true, false, true, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    private void loginByCardNoWithoutPass(String mobile, final String num) {
        ResponseListener<MemberLoginVoResp> listener = new ResponseListener<MemberLoginVoResp>() {
            @Override
            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                try {
                    if (ResponseObject.isOk(response) && MemberLoginVoResp.isOk(response.getContent())) {
                        final CustomerLoginResp resp = response.getContent().getResult();
                        if (resp.customerIsDisable()) {                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                            return;
                        }
                        loginByCardNo(num);
                    } else {

                        String msg;
                        if (response.getContent() != null) {
                            msg = response.getContent().getErrorMessage();
                        } else {
                            msg = getString(R.string.display_login_error);
                        }
                        ToastUtil.showLongToast(msg);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };
        CustomerManager.getInstance().customerLogin(CustomerLoginType.MOBILE,
                mobile, null, false, false, true, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    private void loginByCardNoCheckPwd(final String mobile, final String inputNo, final Context context) {
                final PasswordDialog dialog;

        dialog = new PasswordDialog(context) {
            @Override
            public void close() {
                dismiss();
                DisplayServiceManager.doCancel(context);
            }
        };

        dialog.setMembeName(inputNo);
        dialog.setLisetner(new PasswordDialog.PasswordCheckLisetner() {
            @Override
            public void checkPassWord(String password) {
                password = new MD5().getMD5ofStr(password);
                loginCheckpwd(dialog, mobile, inputNo, password);
            }

            @Override
            public void showPassWord(String password) {

            }

            @Override
            public void showReadKeyBord() {
                if (!PosConnectManager.isPosConnected()) {
                    ToastUtil.showLongToastCenter(context, context.getString(R.string.pay_pos_connection_closed));
                    return;
                }

                final ReadKeyboardDialogFragment dialogFragment =
                        new ReadKeyboardDialogFragment.ReadKeyboardFragmentBuilder().build();
                ReadKeyboardDialogFragment.CardOvereCallback cardOvereCallback = new ReadKeyboardDialogFragment.CardOvereCallback() {

                    @Override
                    public void onSuccess(String keybord) {
                        String password = keybord.toUpperCase(Locale.getDefault());
                        dialogFragment.dismiss();
                        loginCheckpwd(dialog, mobile, inputNo, password);
                    }

                    @Override
                    public void onFail(NewLDResponse ldResponse) {

                    }
                };
                dialogFragment.setPosOvereCallback(cardOvereCallback);
                dialogFragment.show(getFragmentManager(), "ReadKeyboardDialog");
            }
        });
        dialog.show();
    }

    private class PhotoLoginListener implements ResponseListener<MemberLoginVoResp> {

        private String inputNo;
        private int needPswd;
        private String pswd;
        private PasswordDialog dialog;

        public PhotoLoginListener(String inputNo, int needPswd, String pswd, PasswordDialog dialog) {
            this.inputNo = inputNo;
            this.needPswd = needPswd;
            this.pswd = pswd;
            this.dialog = dialog;
        }

        @Override
        public void onResponse(ResponseObject<MemberLoginVoResp> response) {
            try {
                if (ResponseObject.isOk(response) && MemberLoginVoResp.isOk(response.getContent())) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    final CustomerLoginResp resp = response.getContent().getResult();
                    if (resp.customerIsDisable()) {                        ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                        return;
                    }
                    new AsyncTask<Void, Void, CustomerResp>() {
                        @Override
                        protected CustomerResp doInBackground(Void... params) {
                            CustomerResp customerNew = resp.getCustomer();
                            customerNew.queryLevelRightInfos();
                            return customerNew;
                        }

                        @Override
                        protected void onPostExecute(CustomerResp customer) {
                            super.onPostExecute(customer);
                            CustomerManager.getInstance().setLoginCustomer(customer);
                            TradeCustomer tradeCustomer = CustomerManager.getInstance()
                                    .getTradeCustomer(customer);
                            if (!customer.isMember()) {
                                tradeCustomer.setCustomerType(CustomerType.CUSTOMER);
                            } else if (customer.card == null) {
                                tradeCustomer.setCustomerType(CustomerType.MEMBER);
                            } else {
                                tradeCustomer.setCustomerType(CustomerType.CARD);
                                tradeCustomer.setEntitycardNum(customer.card.getCardNum());
                            }
                            if (!TextUtils.isEmpty(customer.openId)) {
                                ShoppingCart.getInstance().setOpenIdenty(customer.openId);
                            }
                            customer.integral = resp.getIntegral() == null ? 0 : resp.getIntegral();
                            ShoppingCart.getInstance().setFastFoodCustomer(tradeCustomer);
                            ShoppingCart.getInstance().memberPrivilege();                            ToastUtil.showShortToast(R.string.customer_login);

                            dismiss();
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.clean();
                    }

                    String msg;
                    if (response.getContent() != null) {
                        msg = response.getContent().getErrorMessage();
                    } else {
                        msg = getString(R.string.display_login_error);
                    }
                    ToastUtil.showLongToast(msg);
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }

        @Override
        public void onError(VolleyError error) {
            ToastUtil.showLongToast(error.getMessage());
        }
    }

    private class CardLoginListener implements ResponseListener<CardLoginResp> {

        @Override
        public void onResponse(ResponseObject<CardLoginResp> response) {
            if (ResponseObject.isOk(response)) {
                final CardLoginResp resp = response.getContent();
                                final EcCard card = resp.getResult().getCardInstance();
                if (card.getCardType() == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                    ToastUtil.showShortToast(R.string.customer_login_not_member);


                } else {
                    final CustomerV5 customerV5 = resp.getResult().getCustomer();
                    card.setName(customerV5.getName());
                    card.setCardLevel(resp.getResult().getCardLevel());
                    card.setCardLevelSetting(resp.getResult().getCardLevelSetting());
                    card.setCardSettingDetails(resp.getResult().getCardSettingDetails());
                    card.setIntegralAccount(resp.getResult().getIntegralAccount());
                    card.setValueCardAccount(resp.getResult().getValueCardAccount());
                    card.setCustomer(customerV5);

                    new AsyncTask<Void, Void, CustomerResp>() {

                        @Override
                        protected CustomerResp doInBackground(Void... params) {
                            CustomerResp customerNew = resp.getCustomer();
                            customerNew.queryLevelRightInfos();
                            return customerNew;
                        }

                        @Override
                        protected void onPostExecute(CustomerResp customer) {
                            super.onPostExecute(customer);
                            customer.card = card;
                            CustomerManager.getInstance().setLoginCustomer(customer);

                            TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(card.getCustomer());
                            tradeCustomer.setCustomerType(CustomerType.CARD);
                            tradeCustomer.setEntitycardNum(card.getCardNum());
                            ShoppingCart.getInstance().setFastFoodCustomer(tradeCustomer);
                            ShoppingCart.getInstance().memberPrivilege();

                            ToastUtil.showShortToast(R.string.customer_login);
                                                        BigDecimal integral = BigDecimal.ZERO;
                            if (card.getIntegralAccount() != null && card.getIntegralAccount().getIntegral() != null) {
                                integral = BigDecimal.valueOf(card.getIntegralAccount().getIntegral());
                            }

                            dismiss();
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            } else {
                String message = response.getMessage();
                ToastUtil.showShortToast(message);


            }
        }

        @Override
        public void onError(VolleyError error) {
            ToastUtil.showShortToast(error.getMessage());
        }
    }
}
