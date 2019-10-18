package com.zhongmei.beauty;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongmei.bty.commonmodule.event.ActionPrinterServerChanged;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.manager.CashierPointManager;
import com.zhongmei.bty.settings.fragment.BasicSettingFragment_;
import com.zhongmei.bty.settings.fragment.BookSettingSwitchFragment_;
import com.zhongmei.bty.settings.fragment.DinnerAutoOrderFragment_;
import com.zhongmei.bty.settings.fragment.DinnerMenuSettingFragment;
import com.zhongmei.bty.settings.fragment.GroupPaySettingFragment_;
import com.zhongmei.bty.settings.fragment.HandoverTypeSettingFragment_;
import com.zhongmei.bty.settings.fragment.OrderDisplayDeviceSettingFragment_;
import com.zhongmei.bty.settings.fragment.PayModelOrderSettingFragment_;
import com.zhongmei.bty.settings.fragment.PrepaySettingFragment_;
import com.zhongmei.bty.settings.fragment.ScannerFragment_;
import com.zhongmei.bty.settings.fragment.ShopInfoFragment_;
import com.zhongmei.bty.settings.fragment.SupportFragment;
import com.zhongmei.bty.settings.fragment.SupportFragment_;
import com.zhongmei.bty.settings.fragment.TableNumberFragment_;
import com.zhongmei.bty.settings.fragment.TimingPrintFragment_;
import com.zhongmei.bty.settings.fragment.UpdateFragment_;
import com.zhongmei.bty.settings.fragment.WeiXinOrderControlFragment_;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.data.VersionInfo;
import com.zhongmei.yunfu.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.beauty_setting_layout)
public class BeautySettingActivity extends BaseActivity {

    private static final String TAG = BeautySettingActivity.class.getSimpleName();

    public static final String INTENT_TAG = "intent_tag";

    public static final String PHONE = "phone";
    public static final String CASHIERMANAGER = "cashierManager";    public static final String LABEL = "label";    public static final String TICKETPROT = "ticketPort";
        @ViewById(R.id.Seting_printer)
    TextView printer;

        @ViewById(R.id.Seting_moneyBox)
    TextView moneyBox;

        @ViewById(R.id.Seting_scanner)
    TextView scanner;

        @ViewById(R.id.tv_setting_order_display_device)
    TextView tvSettingOrderDisplayDevice;

        @ViewById(R.id.Setting_PayModelOrder)
    TextView payModelTV;
        @ViewById(R.id.Setting_GroupPay)
    TextView groupPaySetTV;

        @ViewById(R.id.Seting_notifity)
    TextView nofify;

        @ViewById(R.id.setting_basic)
    TextView mBasicSetting;

        @ViewById(R.id.Setting_CashierPointManagement)
    TextView mCashierPointManagement;

        @ViewById(R.id.Setting_KitchenManagement)
    TextView mKitchenManagement;

        @ViewById(R.id.Seting_ticketStyle)
    TextView mTicketStyle;

        @ViewById(R.id.Seting_customization)
    TextView mCustomization;

        @ViewById(R.id.Seting_Labelingmachine)
    TextView labelingMachine;

        @ViewById(R.id.Seting_Document)
    TextView settingDocument;

    @ViewById(R.id.Seting_Table_Number)
    TextView settingTableNumber;

    @ViewById(R.id.layout_option)
    TextView mLayoutOption;

    @ViewById(R.id.layout_item_settings)
    TextView mLayoutItemSettings;

    @ViewById(R.id.tv_dinner_layout_option)
    TextView tvDinnerLayoutOption;

    @ViewById(R.id.tv_prepay_setting)
    TextView tvPrepaySetting;

        @ViewById(R.id.Seting_timingPrint)
    TextView timingPrint;

        @ViewById(R.id.v_snack_order_accept)
    View vSnackOrderAccept;

        @ViewById(R.id.tv_snack_order_accept)
    TextView tvSnackOrderAccept;

        @ViewById(R.id.v_dinner_order_accept)
    View vDinnerOrderAccept;

        @ViewById(R.id.tv_dinner_order_accept)
    TextView tvDinnerOrderAccept;
        @ViewById(R.id.v_snack_handover_type)
    View vHandoverType;
        @ViewById(R.id.tv_snack_handover_type)
    TextView tvHandoverType;

    @ViewById(R.id.tv_dinner_menu_setting)
    TextView tvDinnerMenuSetting;

        @ViewById(R.id.Seting_customerInfo)
    TextView customerInfo;

        @ViewById(R.id.Seting_verionInfo)
    TextView verionInfo;

    @ViewById(R.id.iv_update_remind)
    ImageView iv_updateRemind;

        @ViewById(R.id.Seting_systemJournal)
    TextView systemJournal;

    @ViewById(R.id.setting_title)
    TextView title;

        @ViewById(R.id.book_switch)
    TextView bookCommonSwitch;

        @ViewById(R.id.queue_common_switch)
    TextView queueCommonSwitch;

        @ViewById(R.id.queue_manager)
    TextView queueManager;

        @ViewById(R.id.voice_manager)
    TextView voiceManager;

        @ViewById(R.id.Seting_blue_phone)
    TextView phoneManager;

    TextView textView = null;

    FragmentManager mFragmentManager;

    @AfterViews
    void initView() {
        VersionInfo versionInfo = ShopInfoCfg.getInstance().getAppVersionInfo();
        if (versionInfo.hasUpdate()) {
            iv_updateRemind.setVisibility(View.VISIBLE);
        } else {
            iv_updateRemind.setVisibility(View.GONE);
        }

        setprinter();
    }

    @UiThread
    @Click(R.id.Seting_printer)
    public void setprinter() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, printer);
            setCurrentStyle(printer);
                        textView = printer;
        }
    }

    @UiThread
    @Click(R.id.Seting_moneyBox)
    public void setmoneyBox() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, moneyBox);
            setCurrentStyle(moneyBox);
            switchFragment(R.id.right_container1, new BeautyCashboxFragment_(), moneyBox);
            textView = moneyBox;
        }
    }

    @UiThread
    @Click(R.id.Setting_PayModelOrder)
    public void setPayModelOrder() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, payModelTV);
            setCurrentStyle(payModelTV);
            switchFragment(R.id.right_container1, new PayModelOrderSettingFragment_(), payModelTV);
            textView = payModelTV;
        }

    }


    @UiThread
    @Click(R.id.Setting_GroupPay)
    public void setGroupPay() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, groupPaySetTV);
            setCurrentStyle(groupPaySetTV);
            switchFragment(R.id.right_container1, new GroupPaySettingFragment_(), groupPaySetTV);
            textView = groupPaySetTV;
        }
    }

    @UiThread
    @Click(R.id.Seting_scanner)
    public void setscanner() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, scanner);
            setCurrentStyle(scanner);
            switchFragment(R.id.right_container1, new ScannerFragment_(), scanner);
            textView = scanner;
        }
    }

    @UiThread
    @Click(R.id.Seting_blue_phone)
    void callSettingClick() {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion <= 16) {
            Toast.makeText(this, getString(R.string.phone_nonsupport), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, phoneManager);
            setCurrentStyle(phoneManager);
                        textView = phoneManager;
        }
    }

    @UiThread
    @Click(R.id.Seting_notifity)
    public void setNotifity() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, nofify);
            setCurrentStyle(nofify);
                        textView = nofify;
        }
    }

    @UiThread
    @Click(R.id.setting_basic)
    public void setBasicSetting() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, mBasicSetting);
            setCurrentStyle(mBasicSetting);
            switchFragment(R.id.right_container1, new BasicSettingFragment_(), mBasicSetting);
            textView = mBasicSetting;
        }
    }

    @UiThread
    @Click(R.id.Setting_CashierPointManagement)
    public void setCashierPointManagement() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, mCashierPointManagement);
            setCurrentStyle(mCashierPointManagement);
            Bundle bundle = new Bundle();
                                    textView = mCashierPointManagement;
        }
    }

    @UiThread
    @Click(R.id.tv_setting_order_display_device)
    public void orderDisplayDeviceSettingButtonClick() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, tvSettingOrderDisplayDevice);
            setCurrentStyle(tvSettingOrderDisplayDevice);
            switchFragment(R.id.right_container1, new OrderDisplayDeviceSettingFragment_(), tvSettingOrderDisplayDevice);
            textView = tvSettingOrderDisplayDevice;
        }
    }

    @UiThread
    @Click(R.id.Seting_ticketStyle)
    public void setTicketStyle() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, mTicketStyle);
            setCurrentStyle(mTicketStyle);
                        textView = mTicketStyle;
        }
    }

    @UiThread
    @Click(R.id.Seting_customization)
    public void setCustomizationTicket() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, mCustomization);
            setCurrentStyle(mCustomization);
                                                textView = mCustomization;
        }
    }

    @UiThread
    @Click(R.id.Seting_Labelingmachine)
    public void setscanningGun() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, labelingMachine);
            setCurrentStyle(labelingMachine);
                        textView = labelingMachine;
        }
    }

    @UiThread
    @Click(R.id.Seting_Document)
    public void setSettingDocument() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, settingDocument);
            setCurrentStyle(settingDocument);
                        textView = settingDocument;
        }
    }

    @UiThread
    @Click(R.id.Seting_Table_Number)
    public void setSettingTableNumber() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, settingTableNumber);
            setCurrentStyle(settingTableNumber);
            switchFragment(R.id.right_container1, new TableNumberFragment_(), settingTableNumber);
            textView = settingTableNumber;
        }
    }

    @UiThread
    @Click(R.id.Setting_KitchenManagement)
    public void setKitchenManagement() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, mKitchenManagement);
            setCurrentStyle(mKitchenManagement);
            Bundle bundle = new Bundle();
                                    textView = mKitchenManagement;
        }
    }

    @UiThread
    @Click(R.id.layout_option)
    public void setLayoutStylePage() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, mLayoutOption);
            setCurrentStyle(mLayoutOption);
                        textView = mLayoutOption;
        }
    }

    @UiThread
    @Click(R.id.layout_item_settings)
    public void setLayoutItemSettings() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, mLayoutItemSettings);
            setCurrentStyle(mLayoutItemSettings);
                        textView = mLayoutItemSettings;
        }
    }

    @UiThread
    @Click(R.id.Seting_timingPrint)
    public void settimingPrint() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, timingPrint);
            setCurrentStyle(timingPrint);
            switchFragment(R.id.right_container1, new TimingPrintFragment_(), timingPrint);
            textView = timingPrint;
        }
    }

    @UiThread
    @Click(R.id.tv_dinner_layout_option)
    public void dinnerLayoutOption() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, tvDinnerLayoutOption);
            setCurrentStyle(tvDinnerLayoutOption);
                        textView = tvDinnerLayoutOption;
        }
    }

    @UiThread
    @Click(R.id.tv_prepay_setting)
    public void prepaySetting() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, tvPrepaySetting);
            setCurrentStyle(tvPrepaySetting);
            switchFragment(R.id.right_container1, new PrepaySettingFragment_(), tvPrepaySetting);
            textView = tvPrepaySetting;
        }
    }

        @UiThread
    @Click(R.id.tv_snack_order_accept)
    public void snackOrderAcceptClick() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, tvSnackOrderAccept);
            setCurrentStyle(tvSnackOrderAccept);
            switchFragment(R.id.right_container1, new WeiXinOrderControlFragment_(), tvSnackOrderAccept);
            textView = tvSnackOrderAccept;
        }
    }

    @UiThread
    @Click(R.id.tv_snack_handover_type)
    public void snackHandoverTypeClick() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, tvHandoverType);
            setCurrentStyle(tvHandoverType);
            switchFragment(R.id.right_container1, new HandoverTypeSettingFragment_(), tvHandoverType);
            textView = tvHandoverType;
        }
    }

        @UiThread
    @Click(R.id.tv_dinner_order_accept)
    public void dinnerOrderAcceptClick() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, tvDinnerOrderAccept);
            setCurrentStyle(tvDinnerOrderAccept);
            switchFragment(R.id.right_container1, new DinnerAutoOrderFragment_(), tvDinnerOrderAccept);
            textView = tvDinnerOrderAccept;
        }
    }

    @UiThread
    @Click(R.id.tv_dinner_menu_setting)
    public void dinnerMenuSetting() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, tvDinnerMenuSetting);
            setCurrentStyle(tvDinnerMenuSetting);
            switchFragment(R.id.right_container1, new DinnerMenuSettingFragment(), tvDinnerMenuSetting);
            textView = tvDinnerMenuSetting;
        }
    }

    @UiThread
    @Click(R.id.Seting_customerInfo)
    public void setcustomerInfo() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, customerInfo);
            setCurrentStyle(customerInfo);
            switchFragment(R.id.right_container1, new ShopInfoFragment_(), customerInfo);
            textView = customerInfo;
        }
    }

    @UiThread
    @Click(R.id.Seting_verionInfo)
    public void setverionInfo() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, verionInfo);
            setCurrentStyle(verionInfo);
            switchFragment(R.id.right_container1, new UpdateFragment_(), verionInfo);
            textView = verionInfo;
            iv_updateRemind.setVisibility(View.GONE);
        }
    }

    @UiThread
    @Click(R.id.Seting_systemJournal)
    public void setSystemJournal() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, systemJournal);
            setCurrentStyle(systemJournal);
            SupportFragment fragment = new SupportFragment_();
            fragment.setFromType(SupportFragment.FROM_TYPE_RETAIL);
            switchFragment(R.id.right_container1, fragment, systemJournal);
            textView = systemJournal;
        }
    }

    @UiThread
    @Click(R.id.book_switch)
    public void bookCommonSwitch() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, bookCommonSwitch);
            setCurrentStyle(bookCommonSwitch);
            switchFragment(R.id.right_container1, new BookSettingSwitchFragment_(), bookCommonSwitch);

            textView = bookCommonSwitch;
        }
    }

    @UiThread
    @Click(R.id.queue_common_switch)
    public void queueCommonSwitch() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, queueCommonSwitch);
            setCurrentStyle(queueCommonSwitch);

            textView = queueCommonSwitch;
        }
    }

    @UiThread
    @Click(R.id.queue_manager)
    public void queueManager() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, queueManager);
            setCurrentStyle(queueManager);

            textView = queueManager;
        }
    }

    @UiThread
    @Click(R.id.voice_manager)
    public void voiceManager() {
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, voiceManager);
            setCurrentStyle(voiceManager);

            textView = voiceManager;
        }
    }

        private void changeLastStyle(TextView view1, TextView view2) {
        if (view1 != null && view1 != view2) {
            Resources res = getResources();
            view1.setBackgroundColor(res.getColor(R.color.settings_normalback));
            view1.setTextColor(res.getColor(R.color.settings_normalword));
        }
    }

        private void setCurrentStyle(TextView view) {
        if (textView != view) {
            Resources res = getResources();
            view.setBackgroundColor(res.getColor(R.color.settings_biueword));
            view.setTextColor(res.getColor(R.color.color_32ADF6));
            title.setText(view.getTag().toString());
        }
    }

    @AfterViews
    public void init() {
        mFragmentManager = getSupportFragmentManager();
        String tag = getIntent().getStringExtra(INTENT_TAG);
        if (TextUtils.equals(tag, PHONE)) {
            callSettingClick();
        } else if (TextUtils.equals(tag, CASHIERMANAGER)) {            setCashierPointManagement();
        } else if (TextUtils.equals(tag, LABEL)) {            setscanningGun();
        } else if (TextUtils.equals(tag, TICKETPROT)) {            setKitchenManagement();
        }

        mTicketStyle.setVisibility(View.GONE);
        mCustomization.setVisibility(View.VISIBLE);
                CashierPointManager.getInstance().modifyCashierPointPrinterDeviceId(false);
    }


        private void switchFragment(int containerId, Fragment fragment, TextView view) {
        if (textView != view) {
            try {
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(containerId, fragment);
                mFragmentTransaction.commit();
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }

        private void switchFragment(int containerId, Fragment fragment, TextView view, Bundle bundle) {
        fragment.setArguments(bundle);
        switchFragment(containerId, fragment, view);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    public void onEventMainThread(ActionPrinterServerChanged event) {

    }

    @Override
    protected void onDestroy() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.right_container1);
        if (fragment != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
        }
        super.onDestroy();
    }

}
