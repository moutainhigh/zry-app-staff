package com.zhongmei.bty.dinner.cash;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.CustomerInfoResp.Card;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.bty.basemodule.customer.message.MemberLoginVoResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerV5;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLDResponse;
import com.zhongmei.bty.basemodule.devices.liandipos.PosConnectManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcValueCardAccount;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.NewCardLoginResp;
import com.zhongmei.bty.basemodule.devices.mispos.dialog.ReadKeyboardDialogFragment;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.bty.basemodule.devices.mispos.event.EventReadKeyboard;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige.DinnerPriviligeType;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.data.LoyaltyMindTransferResp;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.commonmodule.util.MD5;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.snack.orderdish.adapter.DinnerSwitchCardAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * 正餐切换实体卡登录界面
 *
 * @Date：2016-5-19 上午10:56:01
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
@EFragment(R.layout.fragment_dinner_switch_card)
public class DinnerSwitchCardFragment extends BasicFragment {

    private static final String TAG = DinnerSwitchCardFragment.class.getSimpleName();

    @ViewById(R.id.lv_cards)
    ListView lvCards;

    private List<Card> cards = new ArrayList<Card>();

    private String source;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            List<Card> temp = (List<Card>) getArguments().getSerializable(DinnerCashManager.CARDS);
            if (!Utils.isEmpty(temp)) {
                for (Card card : temp) {
                    if (card != null && card.getCardType() == EntityCardType.CUSTOMER_ENTITY_CARD) {
                        cards.add(card);
                    }
                }
            }
            source = getArguments().getString(DinnerCashManager.SOURCE);
        }
    }

    @AfterViews
    void init() {
        CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
        DinnerSwitchCardAdapter adapter = new DinnerSwitchCardAdapter(getActivity());


        Card card = new Card();
        card.setCardKindName(getString(R.string.customer_login_virtual_card));
        card.setCardNum("");
        card.setCardNum(customer.mobile);

        cards.add(0, card);

        adapter.setCards(cards);
        lvCards.setAdapter(adapter);
    }

    @Click({R.id.iv_back, R.id.btn_exit_customer})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                // 返回到items界面
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.PRIVILIGE_ITEMS));
                break;
            case R.id.btn_exit_customer:
                DinnerShopManager.getInstance().setLoginCustomer(null);
                if (DinnerShopManager.getInstance().isSepartShopCart()) {
                    SeparateShoppingCart.getInstance().setSeparateCustomer(null);
                } else {
                    DinnerShoppingCart.getInstance().setDinnerCustomer(null);
                }
                DinnerShopManager.getInstance().getShoppingCart().removeAllPrivilegeForCustomer(true, false);

                DisplayServiceManager.doCancel(getActivity());

                // 返回到items界面
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.PRIVILIGE_ITEMS));
                break;
        }
    }

    @ItemClick(R.id.lv_cards)
    void itemClick(int position) {
        //切换虚拟会员
        if (position == 0) {
            CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
            if (customer == null) {
                return;
            }

            //如果当前登录的就是虚拟会员，直接返回结算优惠主界面
            if (customer.card == null) {
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.PRIVILIGE_ITEMS));
                return;
            }

            String inputNo = customer.mobile;
            loginByPhoneNo(inputNo);
            //切换实体卡
        } else {
            String cardNo = cards.get(position).getCardNum();
            CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
            //要切换的实体卡，就是当前登录的实体卡，直接返回
            if (customer != null && customer.card != null && TextUtils.equals(customer.card.getCardNum(), cardNo)) {
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.PRIVILIGE_ITEMS));
            } else {
                //loginByCardNo(cardNo);
                loginByCardNoCheckPwd(customer.mobile, cardNo, getActivity());
            }
        }
    }

    private void loginByCardNoCheckPwd(final String mobile, final String inputNo, final Context context) {
        //密码登陆
        final PasswordDialog dialog;
		/*DisplayUserInfo dUserInfo =
				DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_PASSWORD_INPUT,
						"",
						null,
						0,
						true, 0);
		DisplayServiceManager.updateDisplay(context, dUserInfo);*/

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
				/*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_PASSWORD_INPUT,
						password,
						null,
						0,
						false, 0);
				DisplayServiceManager.updateDisplay(context, dUserInfo);*/
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
                        if (resp.customerIsDisable()) {//当前账号冻结
                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
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

    /**
     * 使用手机号登录
     *
     * @Title: loginByPhoneNo
     * @Return void 返回类型
     */
    private void loginByPhoneNo(final String inputNo) {
        loginByPhoneNo(inputNo, CustomerManager.NOT_NEED_PSWD, null, null);
    }

    private void loginByPhoneNo(final String inputNo, int needPswd, String pswd, final PasswordDialog dialog) {
        ResponseListener<MemberLoginVoResp> listener = new ResponseListener<MemberLoginVoResp>() {

            @Override
            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                try {
                    if (ResponseObject.isOk(response)) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        CustomerLoginResp resp = response.getContent().getResult();

                        if (resp.customerIsDisable()) {//当前账号冻结
                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                            return;
                        }

                        CustomerResp customer = resp.getCustomer();
                        customer.setInitialValue();
                        customer.needRefresh = false;

                        customer.queryLevelRightInfos();
                        ToastUtil.showShortToast(R.string.customer_login);

                        EventBus.getDefault().post(new EventReadKeyboard(true, ""));// 发送成功到ReadKeyboardDialogFragment

                        DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());

                        new DinnerCashManager().jumpAfterLogin(null, customer, null);
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.clean();
                        }

                        String msg;
                        if (response.getStatusCode() == 1126) {
                            msg = getString(R.string.order_dish_member_disabled);
                        } else {
                            msg = response.getMessage();
                        }
                        EventBus.getDefault().post(new EventReadKeyboard(false, msg));// 发送失败到ReadKeyboardDialogFragment

                        loginFail(response.getMessage());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());

                EventBus.getDefault().post(new EventReadKeyboard(false, error.getMessage()));// 发送失败到ReadKeyboardDialogFragment
            }
        };

        CustomerManager customerManager = CustomerManager.getInstance();
        customerManager.customerLogin(CustomerLoginType.MOBILE, inputNo, pswd, needPswd == 1, false, true, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    /**
     * 使用会员卡号登录
     *
     * @Title: loginByCardNo
     * @Return void 返回类型
     */
    private void loginByCardNo(final String inputNo) {
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<LoyaltyMindTransferResp<NewCardLoginResp>> listener =
                LoadingResponseListener.ensure(new ResponseListener<LoyaltyMindTransferResp<NewCardLoginResp>>() {

                                                   @Override
                                                   public void onResponse(ResponseObject<LoyaltyMindTransferResp<NewCardLoginResp>> response) {
                                                       if (ResponseObject.isOk(response)) {
                                                           NewCardLoginResp resp = response.getContent().getData();
                                                           //1表示loyaty登录成功
                                                           if (response.getContent().isOk() && resp != null) {
                                                               // 设置card的名称，从customer中获得
                                                               EcCard card = resp.getCardInstance();
                                                               CustomerV5 customerV5 = resp.getCustomerV5();
                                                               card.setName(customerV5.getName());
                                                               card.queryLevelSettingInfo();
                                                               // 如果valuecard为null时，给一个余额为0的对象，避免切换该界面时总是加载网络获取信息（只有新卡会为null）
                                                               EcValueCardAccount valueCardAccount = card.getValueCardAccount();
                                                               if (valueCardAccount == null) {
                                                                   valueCardAccount = new EcValueCardAccount();
                                                                   valueCardAccount.setRemainValue(0D);
                                                                   card.setValueCardAccount(valueCardAccount);
                                                               }
                                                               card.setCustomer(customerV5);

                                                               CustomerResp customer = resp.getCustomer();
                                                               customer.queryLevelRightInfos();

                                                               DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());
                                                               if (card != null && card.priceLimit != null) {
                                                                   CustomerManager.getInstance().setCurrentCardIsPriceLimit(card.priceLimit == 2 ? true : false);
                                                               }
                                                               ToastUtil.showShortToast(R.string.customer_login);

                                                               new DinnerCashManager().jumpAfterLogin(null, customer, null);
                                                           } else {
                                                               loginFail(response.getContent().getMessage());
                                                           }
                                                       } else {
                                                           loginFail(response.getMessage());
                                                       }
                                                   }

                                                   @Override
                                                   public void onError(VolleyError error) {
                                                       ToastUtil.showShortToast(error.getMessage());
                                                   }
                                               },
                        getFragmentManager());
        operates.cardLoginNew(inputNo, listener);
    }

    private void loginFail(String message) {
        ToastUtil.showShortToast(message);

        // 副屏显示
		/*DisplayUserInfo dUserInfo =
				DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_VALIDATE_USER_FAIL,
						"",
						null,
						0,
						true,0);
		DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
    }

}
