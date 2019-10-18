package com.zhongmei.bty.mobilepay.popup;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.adapter.ExemptChangeAdapter;
import com.zhongmei.bty.basemodule.commonbusiness.view.PopWindow;
import com.zhongmei.bty.mobilepay.event.ExemptEvent;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class ErasePopupHelper {

    public final static int REMOVE_CENT = 0;
    public final static int REMOVE_DIME = 1;
    public final static int REMOVE_YUAN = 2;
    public final static int REMOVE_AUTO = 3;
    public final static int REMOVE_NOTHING = -1;
    private static ErasePopupHelper sPopupManager = new ErasePopupHelper();

    private List<EraseItem> smallList = new ArrayList<EraseItem>(4);

    private PopWindow mPopupWindow;

    private ErasePopupHelper() {

    }

    public static ErasePopupHelper getInstance() {
        return sPopupManager;
    }

    private void setAllItemUnselected() {
        for (EraseItem item : smallList) {
            if (item.isSelected) {
                item.isSelected = false;
            }
        }
    }


    private void updateAllItem(IPaymentInfo cashInfoManager) {
        for (EraseItem item : smallList) {
            if (cashInfoManager.getEraseType() == item.type) {
                item.isSelected = true;
            } else {
                item.isSelected = false;
            }
        }
    }


    public void initPopwindow(Activity context, Button removeChange, final IPaymentInfo cashInfoManager) {
        updateAllItem(cashInfoManager);
        if (mPopupWindow == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.pay_exempt_change_layout, null);

            ListView listView = (ListView) view.findViewById(R.id.listview);

            ExemptChangeAdapter sma = new ExemptChangeAdapter(context);

            listView.setAdapter(sma);
            if (!smallList.isEmpty()) {
                smallList.clear();
            }

            String[] smallChangeArray = context.getResources().getStringArray(R.array.pay_small_change);
            for (int i = 0; i < smallChangeArray.length; i++) {
                EraseItem item = new EraseItem(i);
                item.name = smallChangeArray[i];
                if (cashInfoManager.getEraseType() == i) {
                    item.isSelected = true;
                }
                smallList.add(item);
            }
            sma.addList(smallList);

            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EraseItem currentItem = smallList.get(position);
                    setAllItemUnselected();
                    if (cashInfoManager.getEraseType() == currentItem.type) {
                        if (REMOVE_AUTO == currentItem.type) {
                            currentItem.isSelected = true;
                            EventBus.getDefault().post(new ExemptEvent(currentItem.type));
                        } else {
                            EventBus.getDefault().post(new ExemptEvent(REMOVE_NOTHING));
                            cashInfoManager.setEraseType(REMOVE_NOTHING);
                        }

                    } else {
                        currentItem.isSelected = true;
                        EventBus.getDefault().post(new ExemptEvent(currentItem.type));
                        if (REMOVE_AUTO != currentItem.type)
                            cashInfoManager.setEraseType(currentItem.type);

                    }
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
            });

            mPopupWindow = new PopWindow(view, 188, 290, context);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.update();
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
                                }
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
                        mPopupWindow.showAsDropDown(removeChange, -94, 5);
        }
    }


    public void disposePopuWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
            smallList.clear();
        }
    }


    public static class EraseItem {
        public int type;

        public String name;

        public boolean isSelected;

        public EraseItem(int Type) {
            type = Type;
        }
    }



        public static void setExemptButtonEraseType(Context context, Button exemptbutton, int eraseType) {
        switch (eraseType) {
            case ErasePopupHelper.REMOVE_CENT:
                setEraseButtonStyle(exemptbutton,
                        context.getResources().getDrawable(R.drawable.pay_erase_item_selector_2),
                        context.getResources().getColorStateList(R.color.commonservice_bule_but_text_color),
                        String.format(context.getResources().getString(R.string.pay_erase_cent)));
                break;
            case ErasePopupHelper.REMOVE_DIME:
                setEraseButtonStyle(exemptbutton,
                        context.getResources().getDrawable(R.drawable.pay_erase_item_selector_2),
                        context.getResources().getColorStateList(R.color.commonservice_bule_but_text_color),
                        String.format(context
                                .getApplicationContext()
                                .getResources()
                                .getString(R.string.pay_erase_dime)));
                break;
            case ErasePopupHelper.REMOVE_YUAN:
                setEraseButtonStyle(exemptbutton,
                        context.getResources().getDrawable(R.drawable.pay_erase_item_selector_2),
                        context.getResources().getColorStateList(R.color.commonservice_bule_but_text_color),
                        String.format(context.getResources().getString(R.string.pay_erase_yuan)));
                break;
            case ErasePopupHelper.REMOVE_AUTO:
                setEraseButtonStyle(exemptbutton,
                        context.getResources().getDrawable(R.drawable.pay_erase_item_selector_2),
                        context.getResources().getColorStateList(R.color.commonservice_bule_but_text_color),
                        String.format(context.getResources().getString(R.string.pay_erase_auto)));
                break;
            default:
                setEraseButtonStyle(exemptbutton,
                        context.getResources().getDrawable(R.drawable.pay_erase_item_selector),
                        context.getResources().getColor(R.color.pay_text_method),
                        String.format(context.getResources().getString(R.string.pay_erase_small_change)));
                break;
        }
    }

    private static void setEraseButtonStyle(Button button, Drawable dra, ColorStateList color, String text) {
        button.setBackgroundDrawable(dra);
        button.setText(text);
        button.setTextColor(color);
        button.requestFocus();
    }

    private static void setEraseButtonStyle(Button button, Drawable dra, int color, String text) {
        button.setBackgroundDrawable(dra);
        button.setText(text);
        button.setPadding(12, 6, 12, 6);
        button.setTextColor(color);
        button.requestFocus();
    }
}
