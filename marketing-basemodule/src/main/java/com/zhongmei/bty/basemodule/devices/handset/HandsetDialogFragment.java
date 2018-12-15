package com.zhongmei.bty.basemodule.devices.handset;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhongmei.util.NetWorkUtil;
import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.devices.handset.constant.HandsetEnumConstant;
import com.zhongmei.bty.basemodule.devices.handset.http.HandsetOperates;
import com.zhongmei.bty.basemodule.devices.handset.http.HandsetOperatesImpl;
import com.zhongmei.bty.basemodule.devices.handset.http.reponse.BindResp;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.TimeoutError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;


public class HandsetDialogFragment extends BasicDialogFragment implements View.OnClickListener {

    public final String TAG = "HandsetDialogFragment";

    public final static String KEY_MOENEY = "money";

    public final static String KEY_LAUNCH_MODE = "launchMode";

    public final static String KEY_BRACELET_ID = "bracelet_id";

    public String mMoney;//金额

    private int mLaunchMode;

    private RelativeLayout mRlContent;

    private ImageView mIvContentSample;

    private TextView mTvContentDesc;

    private Button mBtnRetry;

    private ImageView mBtnClose;

    private TextView mTvMoney;

    private LinearLayout mLlLabel;

    private HandSetStatus mHandsetStatus = HandSetStatus.POS_CONNECT_HS;

    //private PosReceiverUtil mPosBroads;

    private String mLocalHostIP;

    private String mUuid;

    private HandsetOperates mHandsetOperates;

    /**
     * 手环id
     */
    private String mBraceletID;

    //private IpSpUtil mIpSpUtil;

    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) { // 防多次重复连接
            return;
        }
        if (v.getId() == R.id.btnReSearch_HsConnect) {
            onClickRetryButton();
        } else if (v.getId() == R.id.btnClose_HsCommTitle) {
            onClickCloseButton();
        }
    }

    public interface HandsetCallBack {
        void onBraceletInfoCallBack(String braceletId);

        void onPasswordCallBack(String braceletId, String password);
    }

    private HandsetCallBack mHandsetCallBack;

    public void setHandsetCallBack(HandsetCallBack callBack) {
        this.mHandsetCallBack = callBack;
    }

    /**
     * handset连接状态
     */
    public enum HandSetStatus {
        POS_CONNECT_HS, // 连接
        READ, // 手环连接
        PWD // 等待密码
    }

    public void show(FragmentManager manager, String tag) {
        if (manager != null && !manager.isDestroyed()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mLocalHostIP = NetWorkUtil.getLocalIpv4Address(getActivity());
        mHandsetOperates = new HandsetOperatesImpl(getActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBroadCast();
        setupIpSp();
        getBundleDate();
    }

    private void setupIpSp() {
        /*IpSpUtil.getInstance().init(getContext().getApplicationContext());
        mIpSpUtil = IpSpUtil.getInstance();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setupWindow();
        View view = inflater.inflate(R.layout.hs_handset_connect_dialog, container);
        findViewById(view);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if (mIpSpUtil.hasClientIpAddress()){
            Log.i(TAG , "已经存在handset——ip  直接调用连接");
            connect();
        }*/
    }

    private void setupWindow() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        Window window = getDialog().getWindow();
        if (window != null) {
            //设置宽高
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = DensityUtil.dip2px(getActivity(), 460);
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(attributes);
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void setupBroadCast() {
        /*mPosBroads = new PosReceiverUtil(getActivity());
        mPosBroads.regiestPosReceiver();
        mPosBroads.setOnPosReceiverListener(new PosReceiverUtil.OnPosReceiver() {
            @Override
            public void onConnect(String clientAddress) {
                Log.i(TAG, String.format("handset:onConnect -> clientIp : %s", clientAddress));
//                if (TextUtils.isEmpty(IpUtil.getClientIpAddress())){
//                    IpUtil.getClientIpAddress() = clientAddress;
//                }
                sendBindRequest(clientAddress);
            }

            @Override
            public void readBraceletInfo(String json) {
                cancelCountDownTimer();
                Log.i(TAG, String.format("handset:readBraceletInfo -> json : %s", json));
                if (mHandsetCallBack == null){
                    Log.i("handset", "callback is null!");
                    return;
                }
                if (!TextUtils.isEmpty(json)){
                    HandsetReadIdResp resp = praseJson(json , HandsetReadIdResp.class);
                    if (resp.isOk() && mUuid.equals(resp.uid)){
                        mHandsetCallBack.onBraceletInfoCallBack(resp.band_sn);
                        dismiss();
                    } else {
                        Log.i("handset", "handset:readBraceletInfo error -> " + resp.band_read_info);
                        handsetFailure();
                    }
                }
            }

            @Override
            public void readPassword(String json) {
                cancelCountDownTimer();
                Log.i(TAG, String.format("handset:readPassword -> json : %s", json));
                if (mHandsetCallBack == null){
                    Log.i(TAG, "callback is null!");
                    return;
                }
                if (!TextUtils.isEmpty(json)){
                    HandsetPasswordResp resp = praseJson(json , HandsetPasswordResp.class);
                    if (resp.isOk() && mUuid.equals(resp.uid)){
                        mHandsetCallBack.onPasswordCallBack(resp.band_sn , resp.pwd);
                        dismiss();
                    } else {
                        Log.i(TAG, "andset:readPassword error -> " + resp.pwd_cancel_info);
                        handsetFailure();
                    }
                }
            }
        });*/
    }

    private void findViewById(View view) {
        mLlLabel = (LinearLayout) view.findViewById(R.id.llLabel_HsCommTitle);
        mTvMoney = (TextView) view.findViewById(R.id.tvMoney_HsCommTitle);
        mRlContent = (RelativeLayout) view.findViewById(R.id.rlContent_HsConnect);
        mTvContentDesc = (TextView) view.findViewById(R.id.tvDesc_HsConnect);
        mIvContentSample = (ImageView) view.findViewById(R.id.ivSample_HsConnect);
        mBtnRetry = (Button) view.findViewById(R.id.btnReSearch_HsConnect);
        mBtnClose = (ImageView) view.findViewById(R.id.btnClose_HsCommTitle);
        mBtnClose.setOnClickListener(this);
        mBtnRetry.setOnClickListener(this);
    }

    private void initView() {
        attachTitleView();
        initContentView();
    }

    /**
     * 加载 titleview
     */
    private void attachTitleView() {
//        if(isShowMoneyLabel()){
        mLlLabel.setVisibility(View.VISIBLE);
        mTvMoney.setText(String.format(getString(R.string.handset_money_label), mMoney.toString()));
//        } else {
//            mLlLabel.setVisibility(View.GONE);
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroy() {
        cancelAllInterface();
        super.onDestroy();
    }

    public void onClickRetryButton() {
        // TODO 判断目前连接处于那个状态是搜索 还是连接
        if (mHandsetStatus == HandSetStatus.POS_CONNECT_HS) {
            connect();
        } else if (mHandsetStatus == HandSetStatus.READ) {
            readInfo();
        } else if (mHandsetStatus == HandSetStatus.PWD) {
            getPassword();
        }
    }

    private void initContentView() {
        mRlContent.setVisibility(View.VISIBLE);
        mBtnRetry.setVisibility(View.GONE);
        setupContentView(R.drawable.ic_handset_connect_or_input_pwd, getString(R.string.handset_init_connect));

    }

    /**
     * 连接HandSet成功等待手环返回
     */
    private void connectSucces() {
        mBtnRetry.setVisibility(View.GONE);
        if (mLaunchMode == HandsetEnumConstant.HSLaunchMode.GET_BRACELET_INFO) {
            mHandsetStatus = HandSetStatus.READ;
            setupContentView(R.drawable.ic_handset_connect_or_input_pwd, getString(R.string.handset_get_bracelet_info));
            readBraceletInfo();
        } else {
            mHandsetStatus = HandSetStatus.PWD;
            setupContentView(R.drawable.ic_handset_connect_or_input_pwd, getString(R.string.handset_input_pwd));
            readPassword();
        }
    }

    /**
     * handset 超时
     */
    private void handsetTimeOut() {
        mBtnRetry.setVisibility(View.VISIBLE);
        if (mHandsetStatus == HandSetStatus.POS_CONNECT_HS) {
            setupContentView(R.drawable.ic_handset_connect_or_pay_timeout, getString(R.string.handset_connect_timeout));
        } else if (mHandsetStatus == HandSetStatus.READ) {
            setupContentView(R.drawable.ic_handset_connect_or_pay_timeout, getString(R.string.handset_bracelet_timeout));
        }
    }

    /**
     * handset错误
     */
    private void handsetFailure() {
        mBtnRetry.setVisibility(View.VISIBLE);
        if (mHandsetStatus == HandSetStatus.POS_CONNECT_HS) {
            setupContentView(R.drawable.ic_handset_connect_or_pay_fail, getString(R.string.handset_connect_fail));
        } else if (mHandsetStatus == HandSetStatus.READ) {
            setupContentView(R.drawable.ic_handset_connect_or_pay_fail, getString(R.string.handset_bracelet_fail));
        }
    }

    /**
     * 连接设备
     */
    private void connect() {
        mHandsetStatus = HandSetStatus.POS_CONNECT_HS;
        mBtnRetry.setVisibility(View.GONE);
        setupContentView(R.drawable.ic_handset_connect_or_input_pwd, getString(R.string.handset_connect));
        /*if (mIpSpUtil.hasClientIpAddress()){
            sendBindRequest(mIpSpUtil.getClientIpAddress());
        }*/
    }

    /**
     * 等待手环回数据
     */
    private void readInfo() {
        mHandsetStatus = HandSetStatus.READ;
        mBtnRetry.setVisibility(View.GONE);
        setupContentView(R.drawable.ic_handset_connect_or_input_pwd, getString(R.string.handset_bracelet));
        readBraceletInfo();
    }

    /**
     * 获取密码
     */
    private void getPassword() {
        mHandsetStatus = HandSetStatus.PWD;
        mBtnRetry.setVisibility(View.GONE);
        setupContentView(R.drawable.ic_handset_connect_or_input_pwd, getString(R.string.handset_pwd));
        readPassword();
    }

    /**
     * 取消连接
     */
    private void cancelConect() {
        mHandsetOperates.cancelVerify();
    }

    /**
     * 取消手环数据获取
     */
    private void cancelReadBracelet() {
        mHandsetOperates.cancelBracelet();
    }

    /**
     * 取消 密码
     */
    private void cancelPassword() {
        mHandsetOperates.cancelPasswordByBraceletId();
    }

    public void onClickCloseButton() {
        cancelRequest();
        dismiss();
    }

    /**
     * 断开所有接口
     */
    private void cancelAllInterface() {
        /*mPosBroads.setOnPosReceiverListener(null);
        if (mPosBroads != null){
            mPosBroads.unregiestPosReceiver();
        }
        cancelCountDownTimer();
        cancelConect();
        cancelPassword();
        cancelReadBracelet();*/
    }

    private void cancelRequest() {
        /*if (!mIpSpUtil.hasClientIpAddress()){
            return;
        }
        mHandsetOperates.cancel(mIpSpUtil.getClientIpAddress(), mUuid, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mHandsetOperates.cancel();*/
    }

    /**
     * 发送绑定请求
     */
    private void sendBindRequest(String clientAddress) {
        /*if (!mIpSpUtil.equalClientIdAddress(clientAddress)){
            return;
        }*/
        mHandsetOperates.sendVerifyReq(clientAddress, mLocalHostIP, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                if (json != null) {
                    String response = json.toString();
                    if (TextUtils.isEmpty(response)) {
                        handsetFailure();
                    } else {
                        BindResp resp = praseJson(response, BindResp.class);
                        if (resp != null && resp.isOk(mLocalHostIP)) {
                            connectSucces();
                        } else {
                            handsetFailure();
                        }
                    }
                } else {
                    handsetFailure();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    handsetTimeOut();
                } else {
                    handsetFailure();
                }
            }
        });
    }

    /**
     * 读取手环信息
     */
    private void readBraceletInfo() {
        /*if (!mIpSpUtil.hasClientIpAddress()){
            return;
        }*/
        /*mUuid = SystemUtils.genOnlyIdentifier();
        mHandsetOperates.readBraceletReq(mIpSpUtil.getClientIpAddress() , mLocalHostIP, mUuid, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG , "handset send read req success");
                startCountDownTimer();
            }
        } , new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG , "handset send read req failure");
                if (error instanceof TimeoutError){
                    handsetTimeOut();
                } else {
                    handsetFailure();
                }
            }
        });*/
    }

    /**
     * 读取手环信息
     */
    private void readPassword() {
        /*if (!mIpSpUtil.hasClientIpAddress()){
            return;
        }
        mUuid = SystemUtils.genOnlyIdentifier();
        mHandsetOperates.readPasswordByBraceletIdReq(mIpSpUtil.getClientIpAddress() , mLocalHostIP, mUuid , mBraceletID , new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG , "handset send password req success");
                startCountDownTimer();
            }
        } , new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG , "handset send password req failure");
                if (error instanceof TimeoutError){
                    handsetTimeOut();
                } else {
                    handsetFailure();
                }
            }
        });*/
    }

    private void setupContentView(int imageResId, String msg) {
        mIvContentSample.setImageResource(imageResId);
        mTvContentDesc.setText(msg);
    }

    /**
     * 获取手环ID 的启动
     */
    public Bundle readIdLaunch(@Nullable String money) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MOENEY, money);
        bundle.putInt(KEY_LAUNCH_MODE, HandsetEnumConstant.HSLaunchMode.GET_BRACELET_INFO);
        return bundle;
    }

    /**
     * 获取手环密码
     */
    public Bundle passwordLaunch(@Nullable String money, @NotNull String braceletID) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MOENEY, money);
        bundle.putInt(KEY_LAUNCH_MODE, HandsetEnumConstant.HSLaunchMode.INPUT_PASSWORD);
        bundle.putString(KEY_BRACELET_ID, braceletID);
        return bundle;
    }

    private void getBundleDate() {
        mLaunchMode = getArguments().getInt(KEY_LAUNCH_MODE);
        mMoney = getArguments().getString(KEY_MOENEY);
        if (mLaunchMode == HandsetEnumConstant.HSLaunchMode.INPUT_PASSWORD) {
            mBraceletID = getArguments().getString(KEY_BRACELET_ID);
        }
    }

    private <T> T praseJson(String json, Class<T> clz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clz);
    }

    private CountDownTimer mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            handsetTimeOut();
        }
    };

    /**
     * 开始倒计时
     */
    private void startCountDownTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }
    }

    /**
     * 取消倒计时
     */
    private void cancelCountDownTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    /******************* test **********************/
    public static String band_sn = "";

    public static void testHandsetDialog(FragmentManager manager) {
        HandsetDialogFragment dialogFragment = new HandsetDialogFragment();
        dialogFragment.setArguments(dialogFragment.readIdLaunch("1000"));
        dialogFragment.setHandsetCallBack(new HandsetDialogFragment.HandsetCallBack() {
            @Override
            public void onBraceletInfoCallBack(String braceletId) {
                band_sn = braceletId;
                ToastUtil.showShortToast(" 手环id -> " + braceletId);
            }

            @Override
            public void onPasswordCallBack(String braceletId, String password) {
                ToastUtil.showShortToast(" 手环id -> " + braceletId + " : 密码 -> " + password);

            }
        });
        dialogFragment.show(manager, "HandsetDialogFragment");
    }

    public static void testHandsetPasswordDialog(FragmentManager manager) {
        if (TextUtils.isEmpty(band_sn)) {
            return;
        }
        HandsetDialogFragment dialogFragment = new HandsetDialogFragment();
        dialogFragment.setArguments(dialogFragment.passwordLaunch("1000", band_sn));
        dialogFragment.setHandsetCallBack(new HandsetDialogFragment.HandsetCallBack() {
            @Override
            public void onBraceletInfoCallBack(String braceletId) {
                ToastUtil.showShortToast(" 手环id -> " + braceletId);
            }

            @Override
            public void onPasswordCallBack(String braceletId, String password) {
                ToastUtil.showShortToast(" 手环id -> " + braceletId + " : 密码 -> " + password);

            }
        });
        dialogFragment.show(manager, "HandsetDialogFragment");
    }

}
