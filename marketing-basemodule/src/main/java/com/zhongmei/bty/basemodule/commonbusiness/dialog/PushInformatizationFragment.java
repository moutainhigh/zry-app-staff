package com.zhongmei.bty.basemodule.commonbusiness.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

public class PushInformatizationFragment extends BasicDialogFragment implements OnClickListener {
    private static final String TAG = PushInformatizationFragment.class.getSimpleName();
    private TextView titleText;
    private ImageView closeImage;
    private Button btnKnow;
    private WebView webView;
    private OnDialogListener listener;
    private String title;
    private String detail;

    public PushInformatizationFragment() {
    }

    public static PushInformatizationFragment newInstance(String title, String detail) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Title", title);
        bundle.putSerializable("Detail", detail);
        PushInformatizationFragment shopManagementMainFragment = new PushInformatizationFragment();
        shopManagementMainFragment.setArguments(bundle);
        return shopManagementMainFragment;
    }

    public static PushInformatizationFragment show(FragmentActivity activity, String title, String detail, OnDialogListener listener) {
        PushInformatizationFragment fragment =
                PushInformatizationFragment.newInstance(title, detail);
        fragment.setDialogListener(listener);
        fragment.show(activity.getSupportFragmentManager(), "system_information");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        title = (String) b.get("Title");
        detail = (String) b.get("Detail");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = dialog.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commonbusiness_push_system_information_fragment, container, false);
        titleText = (TextView) view.findViewById(R.id.push_system_information_title);
        closeImage = (ImageView) view.findViewById(R.id.push_system_information_close);
        btnKnow = (Button) view.findViewById(R.id.btn_know);
        closeImage.setOnClickListener(this);
        btnKnow.setOnClickListener(this);
        webView = (WebView) view.findViewById(R.id.push_system_information_webview);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleText.setText(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (URLUtil.isNetworkUrl(url)) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        webView.loadDataWithBaseURL(null, detail, "text/html", "utf-8", null);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.push_system_information_close) {
            dismiss();
            if (listener != null) {
                listener.onClose();
            }
        } else if (v.getId() == R.id.btn_know) {
            dismiss();
            if (listener != null) {
                listener.onKnow();
            }
        } else {
            Log.d(TAG, "No item clicked");

        }
    }

    public void setDialogListener(OnDialogListener listener) {
        this.listener = listener;
    }

    public interface OnDialogListener {
        void onClose();

        void onKnow();
    }
}
