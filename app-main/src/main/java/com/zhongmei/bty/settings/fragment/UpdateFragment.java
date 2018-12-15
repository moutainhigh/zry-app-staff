package com.zhongmei.bty.settings.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ResetFragment;
import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.context.util.AppUtils;
import com.zhongmei.yunfu.DownloadApkDialog;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.data.VersionInfo;
import com.zhongmei.yunfu.http.CalmStringRequest;
import com.zhongmei.yunfu.context.util.SystemUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;

@EFragment(R.layout.settings_update_fragment)
public class UpdateFragment extends Fragment {

    private static final String TAG = UpdateFragment.class.getSimpleName();

    @ViewById(R.id.tv_current_version)
    TextView tvCurrentVersion;

    @ViewById(R.id.tv_update_time)
    TextView tvUpdateTime;

    @ViewById(R.id.tv_print_update_time)
    TextView tvPrintUpdateTime;

    @ViewById(R.id.tv_print_version)
    TextView tvPrintVersion;

    @ViewById(R.id.print_version_layer)
    View mPrintVersionLayer;

    @ViewById(R.id.do_upgrade)
    View mDoUpgrade;

    @ViewById(R.id.do_print_upgrade)
    View mDoPrintUpgrade;

    private DownloadApkDialog mPrintDownloadApkDialog;

    private PrintInstallReceiver mPrintInstallReceiver;

    @AfterViews
    protected void init() {
        Resources res = getActivity().getResources();
        VersionInfo versionInfo = ShopInfoCfg.getInstance().getAppVersionInfo(); //MainApplication.getInstance().getVersionInfo();
        tvCurrentVersion.setText(res
                .getString(R.string.settings_current_version)
                + "："
                + SystemUtils.getVersionName());


        boolean hasUpdate = versionInfo.hasUpdate();
        if (hasUpdate) {
            mDoUpgrade.setVisibility(View.VISIBLE);
            tvUpdateTime.setVisibility(View.VISIBLE);
            tvUpdateTime.setTextColor(getResources().getColor(R.color.orange));
            tvUpdateTime.setText(getString(R.string.settings_new_version, getUpdateVersionName(versionInfo)));
        } else {
            mDoUpgrade.setVisibility(View.GONE);
            tvUpdateTime.setTextColor(getResources().getColor(R.color.print_text_gray));
            tvUpdateTime.setText(getString(R.string.publish_time, "2018-08-07"));
        }

        VersionInfo printVersionInfo = ShopInfoCfg.getInstance().getPrintVersionInfo(); //MainApplication.getInstance().getPrintVersionInfo();
        if (printVersionInfo.hasUpdate()) {
            mDoPrintUpgrade.setVisibility(View.VISIBLE);
            tvPrintUpdateTime.setVisibility(View.VISIBLE);
            tvPrintUpdateTime.setTextColor(getResources().getColor(R.color.orange));
            tvPrintUpdateTime.setText(getString(R.string.settings_new_version, getUpdateVersionName(printVersionInfo)));
        } else {
            mDoPrintUpgrade.setVisibility(View.GONE);
            tvPrintUpdateTime.setTextColor(getResources().getColor(R.color.print_text_gray));
            tvPrintUpdateTime.setText("");
            requestPrintPublishDate();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPrintInstallReceiver == null) {
            mPrintInstallReceiver = new PrintInstallReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_PACKAGE_ADDED);
            filter.addDataScheme("package");
            getActivity().registerReceiver(mPrintInstallReceiver, filter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //显示打印服务版本号
        String printVersionName = AppUtils.getAppVersionName(getActivity(), Constant.PRINT_SERVICE_PACKAGE_NAME);
        if (printVersionName != null) {
            tvPrintVersion.setVisibility(View.VISIBLE);
            mPrintVersionLayer.setVisibility(View.VISIBLE);
            tvPrintVersion.setText(getString(R.string.settings_print_version, printVersionName));
        } else {
            tvPrintVersion.setVisibility(View.GONE);
            mPrintVersionLayer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPrintInstallReceiver != null) {
            getActivity().unregisterReceiver(mPrintInstallReceiver);
            mPrintInstallReceiver = null;
        }
    }


    @Click({R.id.do_upgrade, R.id.do_print_upgrade})
    protected void click(View v) {
        switch (v.getId()) {
            case R.id.do_upgrade:
                mPrintDownloadApkDialog = DownloadApkDialog.createDownload(getActivity(), ShopInfoCfg.getInstance().getAppVersionInfo()).star();
                break;
            case R.id.do_print_upgrade:
                mPrintDownloadApkDialog = DownloadApkDialog.createDownload(getActivity(), ShopInfoCfg.getInstance().getPrintVersionInfo()).star();
                break;
        }
    }

    @Click(R.id.btn_reset)
    protected void resetClick(View v) {
        ResetFragment dialog = new ResetFragment(getActivity(), ResetFragment.OTHER);
        dialog.show();
    }

    private void requestPrintPublishDate() {
        CalmStringRequest quest1 = new CalmStringRequest(MainApplication.getInstance(), Request.Method.GET,
                getPublishUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (isAdded() && !getActivity().isDestroyed()) {
                            long publishDate = (long) (Float.valueOf(response) * 1000);
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String date = df.format(new Date(publishDate));
                            tvPrintUpdateTime.setText(getString(R.string.publish_time, date));
                        }
                    }
                },
                null);
        quest1.executeRequest("2");
    }

    private String getPublishUrl() {
        String printVersionName = AppUtils.getAppVersionName(getActivity(), Constant.PRINT_SERVICE_PACKAGE_NAME);
        return "https://demo.com/downadmin/api/getreleasedate?app=printer&&version=" + printVersionName;
    }

    /**
     * 获取升级版本名
     *
     * @param versionInfo
     * @return
     */
    private String getUpdateVersionName(VersionInfo versionInfo) {
        String versionName = versionInfo.getVersionName();
        if (TextUtils.isEmpty(versionName)) {
            String versionCode = (versionInfo.getVersionCode());
            long version = 0;
            try {
                version = Long.parseLong(versionCode);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            versionName = convertVersionName(version);
        }
        return versionName;
    }

    /**
     * 根据版本号生成版本名
     *
     * @param versionCode
     * @return
     */
    private String convertVersionName(long versionCode) {
        long baseNo = 2110000000;

        if (versionCode < baseNo) {
            return String.valueOf(versionCode);
        }

        long version = versionCode - baseNo;
        long ver1 = version / 10000;
        version %= 10000;
        long ver2 = version / 100;
        long ver3 = version % 100;

        return ver1 + "." + ver2 + "." + ver3;
    }

    private class PrintInstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //接收安装广播
            if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                String packageName = intent.getDataString();
                if (("package:" + Constant.PRINT_SERVICE_PACKAGE_NAME).equals(packageName)
                        || ("package:" + MainApplication.getInstance().getPackageName()).equals(packageName)) {
                    if (mPrintDownloadApkDialog != null) {
                        mPrintDownloadApkDialog.dismiss();
                    }
                    init();
                }
            }
        }
    }
}
