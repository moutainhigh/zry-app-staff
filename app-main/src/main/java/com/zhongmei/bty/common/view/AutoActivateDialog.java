package com.zhongmei.bty.common.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.EncodingHandler;
import com.zhongmei.bty.common.view.ui.AutoActivateEcpainDialog;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.json.JSONObject;

/**
 * 自助激活设备
 *
 * @created 2017/7/3
 */
@SuppressLint("ValidFragment")
public class AutoActivateDialog extends BasicDialogFragment {

    TextView tvTitle;
    TextView tvSubTitle;
    TextView tvExplain;
    ImageView ivClose;
    ImageView ivQrCode;
    Button btnCancel;
    Button btnOk;

    View.OnClickListener listenerOk;
    View.OnClickListener listenerCancel;

    public static AutoActivateDialog show(FragmentActivity activity, View.OnClickListener listenerOk, View.OnClickListener listenerCancel) {
        AutoActivateDialog autoActivateDialog = (AutoActivateDialog) AutoActivateDialog.instantiate(activity, AutoActivateDialog.class.getName());
        autoActivateDialog.listenerOk = listenerOk;
        autoActivateDialog.listenerCancel = listenerCancel;
        autoActivateDialog.show(activity.getSupportFragmentManager(), null);
        return autoActivateDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_auto_activate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        tvTitle = findViewById(R.id.tvTitle);
        tvSubTitle = findViewById(R.id.tvSubTitle);
        ivClose = findViewById(R.id.ivClose);
        ivQrCode = findViewById(R.id.ivQrCode);
        btnCancel = findViewById(R.id.btnCancel);
        btnOk = findViewById(R.id.btnOk);
        tvExplain = findViewById(R.id.tv_explain);

        setQrCode();
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                if (listenerOk != null) {
                    listenerOk.onClick(v);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                if (listenerCancel != null) {
                    listenerCancel.onClick(v);
                }
            }
        });
        tvExplain.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tvExplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickManager.getInstance().isClicked()) {
                    return;
                }
                new AutoActivateEcpainDialog().show(getFragmentManager(), "AutoActivateEcpainDialog");
            }
        });
    }

    private void setQrCode() {
        Bitmap mBitmap = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("applicationType", "5"); //On OS
            jsonObject.put("mac", SystemUtils.getMacAddress());
            mBitmap = EncodingHandler.createQRCode(jsonObject.toString(), Math.max(ivQrCode.getLayoutParams().height, ivQrCode.getLayoutParams().width));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ivQrCode.setImageBitmap(mBitmap);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ivQrCode.setImageDrawable(null);
    }
}
