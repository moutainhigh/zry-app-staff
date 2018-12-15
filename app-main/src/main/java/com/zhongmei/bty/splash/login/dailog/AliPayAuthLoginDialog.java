package com.zhongmei.bty.splash.login.dailog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.yunfu.context.session.Callback;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.yunfu.context.AppBuildConfig;
import com.zhongmei.yunfu.util.EmptyUtils;
import com.zhongmei.yunfu.context.util.EncodingHandler;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.entity.event.AliPayLoginEvent;
import com.zhongmei.bty.entity.event.AliPayLoginEvent.AliPayLogin;
import com.zhongmei.bty.thirdplatform.openplatform.ShopUserInfo;
import com.zhongmei.bty.thirdplatform.openplatform.ShopUserUtil;

import java.net.URLEncoder;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * @since 2018.06.12.
 */
@EFragment(R.layout.dialog_alipay_auth_login)
public class AliPayAuthLoginDialog extends BasicDialogFragment {

    @ViewById(R.id.iv_ali_auth_qr_code)
    ImageView mQrCode;

    private Bitmap mBitmap;

    private OnLoginSuccess onLoginSuccess;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        return dialog;
    }


    @AfterViews
    protected void initialize() {
        registerEventBus();
        ThreadUtils.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                // 拼装Gateway地址
                String brandId = ShopInfoCfg.getInstance().commercialGroupId;
                String deviceID = ShopInfoCfg.getInstance().deviceID;
                String commercialId = ShopInfoCfg.getInstance().shopId;
                StringBuilder gateBuiler = new StringBuilder();
                gateBuiler.append(getRedirectUri())
                        .append("?brandId=").append(brandId)
                        .append("&deviceID=").append(deviceID)
                        .append("&commercialId=").append(commercialId);
                Log.d("GatewayUrl", String.valueOf(gateBuiler));
                String gateway = URLEncoder.encode(String.valueOf(gateBuiler));
                // 拼装支付宝地址
                String url = "http://demo.com/oauth2/publicAppAuthorize.htm";
                StringBuilder builder = new StringBuilder(url);
                builder.append("?app_id=").append(getAPPID())
                        .append("&scope=auth_user")
                        .append("&redirect_uri=")
                        .append(gateway)
                        .append("&state=init");
                Log.d("AliPayUrl", String.valueOf(builder));
                try {
                    mBitmap = EncodingHandler.createQRCode(String.valueOf(builder), 200);
                    Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.pay_online_alipay);
                    mBitmap = EncodingHandler.addQRCodeLogo(mBitmap, logo);
                    handler.sendEmptyMessage(0);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
        DisplayServiceManager.doCancel(getActivity());
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mBitmap != null) {
                mQrCode.setImageBitmap(mBitmap);
            }
        }
    };

    @Click(R.id.iv_back)
    public void onBack() {
        dismiss();
    }

    public void show(FragmentManager fm) {
        show(fm, "AliPayAuthLoginDialog");
    }

    /**
     * 支付宝登录成功
     *
     * @see com.zhongmei.bty.sync.PushMessageReceiver /on_os/koubei/alipay_login
     */
    public void onEventMainThread(AliPayLoginEvent event) {
        if (event.result == 0 && event.data != null) {
            AliPayLogin login = event.data;
            if (EmptyUtils.isEmpty(login) || EmptyUtils.isEmpty(login.accountId)) {
                return;
            }
            final Long userId = login.accountId;
            // 成功
            ThreadUtils.runOnWorkThread(new Runnable() {
                @Override
                public void run() {
                    List<User> userList = Session.getFunc(UserFunc.class).getUsers();
                    for (User u : userList) {
                        if (u.getAccountId() != null && userId.compareTo(u.getAccountId()) == 0) {
                            // 登录
                            submit(u);
                            break;
                        }
                    }
                }
            });
        } else {
            // 失败
            ToastUtil.showShortToast(event.msg);
        }
    }

    /**
     * 登录
     */
    private void submit(User user) {
        Session.bind(user, new Callback() {
            @Override
            public void onBindSuccess() {
                saveUser(Session.getAuthUser());//add 8.5
                if (onLoginSuccess != null) {
                    onLoginSuccess.onLoginSUccess();
                }
                dismiss();
            }

            @Override
            public void onBindError(String message) {
                ToastUtil.showShortToast(message);
            }
        });
    }

    //保存登录用户及商户信息，与开放平台共享 add v8.4
    private void saveUser(User user) {
        if (user != null) {
            final ShopUserInfo shopUserInfo = new ShopUserInfo();
            shopUserInfo.setBrandId(MainApplication.getInstance().getBrandIdenty() + "");
            shopUserInfo.setBrandName(MainApplication.getInstance().getBrandName());
            shopUserInfo.setShopId(MainApplication.getInstance().getShopIdenty() + "");
            shopUserInfo.setUserId(user.getId() + "");
            shopUserInfo.setUserName(user.getName());
            ThreadUtils.runOnWorkThread(new Runnable() {
                @Override
                public void run() {
                    ShopUserUtil.saveShopUserInfo(getContext(), shopUserInfo);
                }
            });
        }
    }

    /**
     * 获取支付宝AppId
     */
    private String getAPPID() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1: //开发环境
                return "2018022402264734";
            case 2: //测试环境
                return "";
            case 3: //灰度环境
                return "2018022402264734";
            case 4: //CI环境
                return "2018022402264734";
            default:
                // 添加各个环境的AppId
                return "2018022402264734";
        }
    }

    /**
     * 获取回调地址
     */
    private String getRedirectUri() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1: //开发环境
                return "http://demo.com/api/koubei/down/auth/callback";
            case 2: //测试环境
                return "";
            case 3: //灰度环境
                return "http://demo.com/api/koubei/down/auth/callback";
            case 4: //CI环境
                return "http://demo.com/api/koubei/down/auth/callback";
            default:
                // 添加各个环境的回调地址
                return "http://demo.com/api/koubei/down/auth/callback";
        }
    }

    public void setOnLoginSuccess(OnLoginSuccess loginSuccess) {
        this.onLoginSuccess = loginSuccess;
    }

    public interface OnLoginSuccess {
        void onLoginSUccess();
    }

}
