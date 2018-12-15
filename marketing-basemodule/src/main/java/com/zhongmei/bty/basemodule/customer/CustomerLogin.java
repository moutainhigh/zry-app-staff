package com.zhongmei.bty.basemodule.customer;

import android.support.v4.app.FragmentActivity;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLDResponse;
import com.zhongmei.bty.basemodule.devices.liandipos.PosConnectManager;
import com.zhongmei.bty.basemodule.devices.mispos.dialog.ReadKeyboardDialogFragment;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.commonmodule.util.MD5;
import com.zhongmei.yunfu.util.ToastUtil;

import java.util.Locale;

public class CustomerLogin {

    public static PasswordDialog dinnerLoginByPhoneNo(final FragmentActivity context, String input, final DinnerLoginListener listener) {
        final PasswordDialog dialog;
        if (ServerSettingManager.isCommercialNeedVerifPassword()) {
            return showMemberPasswordDialog(context, input, listener);
        } else {
            if (listener != null) {
                listener.login(null, CustomerManager.NOT_NEED_PSWD, "");
            }
        }
        return null;
    }

    public static PasswordDialog showMemberPasswordDialog(final FragmentActivity context, String input, final DinnerLoginListener listener) {
        final PasswordDialog dialog;
        /*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_PASSWORD_INPUT,
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

        //名字为空时，显示电话号码
//            if (!TextUtils.isEmpty(input)) {
//                dialog.setMembeName(input);
//            } else {
//                dialog.setMembeName(context.getString(R.string.customer_sex_unknown));
//            }
        dialog.setMembeName(input);
        dialog.setLisetner(new PasswordDialog.PasswordCheckLisetner() {
            @Override
            public void checkPassWord(String password) {
                password = new MD5().getMD5ofStr(password);
                if (listener != null) {
                    listener.login(dialog, CustomerManager.NEED_PSWD, password);
                }
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
                    ToastUtil.showLongToastCenter(context, context.getString(R.string.customer_pos_connection_closed));
                    return;
                }

                final ReadKeyboardDialogFragment dialogFragment =
                        new ReadKeyboardDialogFragment.ReadKeyboardFragmentBuilder().build();
                ReadKeyboardDialogFragment.CardOvereCallback cardOvereCallback = new ReadKeyboardDialogFragment.CardOvereCallback() {

                    @Override
                    public void onSuccess(String keybord) {
                        String password = keybord.toUpperCase(Locale.getDefault());
                        if (listener != null) {
                            listener.login(dialog, CustomerManager.NEED_PSWD, password);
                        }
                    }

                    @Override
                    public void onFail(NewLDResponse ldResponse) {

                    }
                };
                dialogFragment.setPosOvereCallback(cardOvereCallback);
                dialogFragment.show(context.getSupportFragmentManager(), "ReadKeyboardDialog");
            }
        });
        dialog.show();
        return dialog;
    }

    /**
     * 正餐微信扫码登录
     *
     * @param context
     * @param listener
     */
    @Deprecated
    public static void dinnerLoginWxNo(final FragmentActivity context, final DinnerLoginListener listener) {
        if (ServerSettingManager.isCommercialNeedVerifPassword()) {
            /*DisplayUserInfo dUserInfo =
                    DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_PASSWORD_INPUT,
                            "",
                            null,
                            0,
                            true, 0);
            DisplayServiceManager.updateDisplay(context, dUserInfo);*/

            final PasswordDialog dialog = new PasswordDialog(context) {
                @Override
                public void close() {
                    dismiss();
                    DisplayServiceManager.doCancel(context);
                }
            };

            //名字处显示微信扫码登录
            dialog.setMemberContent(context.getString(R.string.customer_login_through_wechat_scan_code));
            dialog.setLisetner(new PasswordDialog.PasswordCheckLisetner() {
                @Override
                public void checkPassWord(String password) {
                    password = new MD5().getMD5ofStr(password);
                    if (listener != null) {
                        listener.login(dialog, CustomerManager.NEED_PSWD, password);
                    }
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
                        ToastUtil.showLongToastCenter(context, context.getString(R.string.customer_pos_connection_closed));
                        return;
                    }

                    final ReadKeyboardDialogFragment dialogFragment =
                            new ReadKeyboardDialogFragment.ReadKeyboardFragmentBuilder().build();
                    ReadKeyboardDialogFragment.CardOvereCallback cardOvereCallback = new ReadKeyboardDialogFragment.CardOvereCallback() {

                        @Override
                        public void onSuccess(String keybord) {
                            String password = keybord.toUpperCase(Locale.getDefault());
                            if (listener != null) {
                                listener.login(dialog, CustomerManager.NEED_PSWD, password);
                            }
                        }

                        @Override
                        public void onFail(NewLDResponse ldResponse) {

                        }
                    };
                    dialogFragment.setPosOvereCallback(cardOvereCallback);
                    dialogFragment.show(context.getSupportFragmentManager(), "ReadKeyboardDialog");
                }
            });
            dialog.show();
        } else {
            if (listener != null) {
                listener.login(null, CustomerManager.NOT_NEED_PSWD, "");
            }
        }
    }

    public interface DinnerLoginListener {
        void login(PasswordDialog dialog, int needPswd, String password);
    }
}
