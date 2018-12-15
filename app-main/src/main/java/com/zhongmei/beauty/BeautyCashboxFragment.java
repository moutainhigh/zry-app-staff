package com.zhongmei.beauty;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.commonmodule.database.entity.PrinterDevice;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.settings_cashbox)
public class BeautyCashboxFragment extends Fragment {
    private static final String TAG = BeautyCashboxFragment.class.getSimpleName();

    @ViewById(R.id.tv_choose_device)
    protected TextView tv_choose_Name;

    @ViewById(R.id.btn_test_cashbox)
    protected Button btnTestCashbox;

    @ViewById(R.id.cash_printer_list)
    protected LinearLayout devicesList;

    private List<PrinterDevice> printerInfo = new ArrayList<>();

    private PrinterDevice choosePrinter;

    @AfterViews
    void init() {
        // 从数据库取得所有的打印机列表信息
        /*try {
            PrintSettingDal dal = OperatesFactory.create(PrintSettingDal.class);
            printerInfo = dal.listPrinter(PrinterKind.RECEIPT);
            Collections.sort(printerInfo, new PrinterComparator());
            CashBoxPrinterListAdapter listAdapter = new CashBoxPrinterListAdapter(getActivity(), printerInfo);
            listAdapter.setSelectPrinter(SpHelper.getDefault().getString(SpHelper.SETTING_MONEY_BOX_IP));
            if (printerInfo != null && printerInfo.size() > 0) {
                listAdapter.setPositionListener(new PositionListener() {

                    @Override
                    public void itemClick(PrinterDevice printer) {
                        choosePrinter = printer;
                        SpHelper.getDefault().putString(SpHelper.SETTING_MONEY_BOX_IP, printer.getAddress());
                        tv_choose_Name.setText(printer.getDeviceName() + "(" + printer.getAddress() + ")");
                    }

                });
                listAdapter.initView(devicesList);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }*/
    }

    @Click({R.id.btn_test_cashbox})
    protected void click(View v) {
        switch (v.getId()) {
            case R.id.btn_test_cashbox:
                if (choosePrinter != null) {

                } else {
                    ToastUtil.showShortToast(R.string.select_printer_info);
                }
                break;
        }
    }

}
