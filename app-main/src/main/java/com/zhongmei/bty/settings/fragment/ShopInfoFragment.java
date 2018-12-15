package com.zhongmei.bty.settings.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.newprt.prt.ShopInfoUI;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.http.CalmImageRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;

@EFragment(R.layout.settings_shop_info_fragment)
public class ShopInfoFragment extends Fragment {
    @ViewById(R.id.shop_name)
    TextView mShopName;

    @ViewById(R.id.shop_phone)
    TextView mShopPhone;

    @ViewById(R.id.shop_info_list)
    ListView mListView;

    @ViewById(R.id.shop_picture)
    ImageView mImage;

    @AfterViews
    public void initAdapter() {
        List<String> shopInfoList = Arrays.asList(
                ShopInfoUI.getUIShopId(getActivity()),
                ShopInfoUI.getUICommercialGroupId(getActivity()),
                ShopInfoUI.getUIcommercialAddress(getActivity()),
                ShopInfoUI.getUISyncUrl(getActivity()));
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.settings_shop_info_list_item,
                R.id.shop_info_item_title, shopInfoList));
    }

    @SuppressLint("NewApi")
    @AfterViews
    public void initContext() {
        String shopLogoUrl = ShopInfoCfg.getInstance().commercialLogo;
        String merchantName = ShopInfoUI.getUICommercialName(MainApplication.sInstance);
        String merchantPhone = ShopInfoUI.getUICommercialPhone(MainApplication.sInstance);
        mShopName.setText(merchantName);
        mShopPhone.setText(merchantPhone);

        if (!TextUtils.isEmpty(shopLogoUrl) && URLUtil.isValidUrl(shopLogoUrl)) {
            CalmImageRequest imageRequest = new CalmImageRequest(shopLogoUrl, new Response.Listener<Bitmap>() {
                @SuppressWarnings("deprecation")
                @Override
                public void onResponse(Bitmap response) {
                    Drawable drawable = new BitmapDrawable(response);
                    mImage.setBackgroundDrawable(drawable);
                }
            }, 0, 0, Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            imageRequest.executeRequest("3", false, "");
        } else {
            ToastUtil.showShortToast(R.string.settings_shop_icon_url_error);
        }
    }
}
