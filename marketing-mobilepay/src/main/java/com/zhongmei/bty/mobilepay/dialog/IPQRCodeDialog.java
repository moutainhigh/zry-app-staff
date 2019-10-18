package com.zhongmei.bty.mobilepay.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.yunfu.context.util.EncodingHandler;
import com.zhongmei.yunfu.context.util.JsonUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;




public class IPQRCodeDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private ImageView mCloseBT;    private ImageView mQRIV;
    private TextView mIPTV;
    private int barcodeWH = 300;
    public static void start(Activity context) {
        IPQRCodeDialog dialog = new IPQRCodeDialog(context);
        dialog.show();
    }

    private IPQRCodeDialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_ip_qr_code_dialog_layout);
    }

    public IPQRCodeDialog(Activity context) {
        this(context, R.style.custom_alert_dialog);
        this.mContext = context;
        initView();
    }

    private void initView() {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCloseBT = (ImageView) findViewById(R.id.close_button);
        mQRIV = (ImageView) findViewById(R.id.showBarcode);
        mIPTV = (TextView) findViewById(R.id.local_ip_text);
        mCloseBT.setOnClickListener(this);
        showIpQR();
    }

    private void showIpQR() {
        try {
            String ip = getIp(this.getContext());
            mIPTV.setText(getContext().getString(R.string.pay_local_ip_text) + "：" + ip);
            IpQRInfo ipQRInfo = new IpQRInfo(ip);

            String jsonStr = JsonUtil.objectToJson(ipQRInfo);
            Bitmap bitmap = EncodingHandler.createQRCode(jsonStr, barcodeWH);

            mQRIV.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.close_button) {
            this.dismiss();
        }
    }

    private String getIp(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());                return ipAddress;

            } else {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private String intIP2StringIP(int ipAddress) {
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }

    private class IpQRInfo {
        private String hip;
        public IpQRInfo(String ip) {
            this.hip = ip;
        }
    }
}
