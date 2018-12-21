package com.zhongmei.bty.queue.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.bty.basemodule.commonbusiness.listener.SimpleResponseListener;
import com.zhongmei.bty.basemodule.customer.bean.CustomerMobile;
import com.zhongmei.bty.basemodule.customer.bean.CustomerMobileVo;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryGridAdapter;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
import com.zhongmei.bty.basemodule.input.NumberKeyBoardUtils;
import com.zhongmei.bty.common.util.CommonKeyBorad;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.queue.manager.QueueOpManager;
import com.zhongmei.bty.queue.ui.view.KeyboardFragment;
import com.zhongmei.bty.queue.ui.view.KeyboardFragment_;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@EFragment(R.layout.queue_auto_create_takenumber)
public class QueueAutoTakeNumberRightFragment extends BasicFragment implements KeyboardFragment.OnKeyBoradListener {
    private static final String TAG = QueueAutoTakeNumberRightFragment.class.getSimpleName();

    @ViewById(R.id.queue_keyboard_panel)
    LinearLayout mQueueKeyboardPanle;

    @ViewById(R.id.queue_create_count)
    EditText mQueueCreateCount;

    @ViewById(R.id.queue_create_phonenum)
    EditText mQueueCreatePhonenum;

    @ViewById(R.id.queue_create_takenumber_btn)
    LinearLayout submitBtn;

    private int inputtype = 0;

    private Queue queue;

    private CommonKeyBorad mKeyBorad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TVClientService.start(MainApplication.getInstance());
    }

    @AfterViews
    void initView() {
        mErpDal = OperatesFactory.create(ErpCommercialRelationDal.class);
        mKeyBorad = new CommonKeyBorad();
        if (getActivity().getWindow()
                .getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            // 隐藏软键盘
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        KeyboardFragment keyboardFragment = new KeyboardFragment_();
        keyboardFragment.setOnKeyBoradListener(this);

        fragmentTransaction.replace(R.id.queue_keyboard_panel, keyboardFragment);
        fragmentTransaction.commitAllowingStateLoss();
        mQueueCreateCount.setOnFocusChangeListener(focusListener);
        mQueueCreatePhonenum.setOnFocusChangeListener(focusListener);
        NumberKeyBoardUtils.setTouchListener(mQueueCreateCount);
        NumberKeyBoardUtils.setTouchListener(mQueueCreatePhonenum);
        mQueueCreateCount.requestFocus();
        DisplayServiceManager.doUpdateQueue(getActivity().getApplicationContext());
        //registerEventBus();
        initAreaCode();
    }

    @Override
    public void onDestroy() {
        //unregisterEventBus();
        super.onDestroy();
    }


    private final View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                setCurrentInput(null);
                return;
            }
            switch (v.getId()) {
                case R.id.queue_create_count:
                    // setCurrentInput(mQueueCreateCount);
                    inputtype = 1;
                    mKeyBorad.setMax(5);
                    mKeyBorad.setDefaultSuffix(getString(R.string.queue_person));
                    setCurrentInput(mQueueCreateCount);
                    break;
                case R.id.queue_create_phonenum:
                    // setCurrentInput(mQueueCreatePhonenum);
                    inputtype = 2;
                    mKeyBorad.setDefaultSuffix("");
                    setCurrentInput(mQueueCreatePhonenum);
                    break;
            }
        }
    };

    private void printCall(boolean isSetPrint) {
        if (isDetached()) {
            return;
        }

        if (isSetPrint) {
            queue = null;
            mQueueCreateCount.setText("");
            mQueueCreatePhonenum.setText("");
        }
    }

    @Click(R.id.queue_create_count)
    protected void inputPersonCount() {
        inputtype = 1;
        setCurrentInput(mQueueCreateCount);
    }

    @Click(R.id.tvAreaCodes)
    public void clickAreaCode() {
        showCountryDialog(new ArrayList<>(erpCurrencyMap.values()), mErpCurrency);
    }

    @ViewById(R.id.tvAreaCodes)
    TextView mTvCountry;

    /**
     * 当前商户国籍
     */
    private ErpCurrency mErpCurrency;

    private Map<String, ErpCurrency> erpCurrencyMap;
    private ErpCommercialRelationDal mErpDal;

    void initAreaCode() {
        TaskContext.bindExecute(this, new SimpleAsyncTask<Map<String, ErpCurrency>>() {
            @Override
            public Map<String, ErpCurrency> doInBackground(Void... params) {
                if (erpCurrencyMap == null) {
                    erpCurrencyMap = new HashMap<>();
                }
                List<ErpCurrency> mErpCurrencyList = mErpDal.queryErpCurrenctList();
                if (Utils.isNotEmpty(mErpCurrencyList)) {
                    for (ErpCurrency currency : mErpCurrencyList) {
                        erpCurrencyMap.put(currency.getAreaCode(), currency);
                    }
                }
                return erpCurrencyMap;
            }

            @Override
            public void onPostExecute(Map<String, ErpCurrency> erpCurrencyMap) {
                String areaCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
                mErpCurrency = erpCurrencyMap.get(areaCode);
                setupCountryView();
            }
        });
    }

    /**
     * 显示国籍dialog
     *
     * @param erpCurrencyList
     * @param currentErpCurrency
     */
    public void showCountryDialog(List<ErpCurrency> erpCurrencyList, ErpCurrency currentErpCurrency) {
        if (erpCurrencyList == null || erpCurrencyList.size() == 0) {
            ToastUtil.showShortToast(getString(R.string.customer_erpcurrency_empty));
            return;
        }

        CountryDialog dialog = new CountryDialog(getActivity(), erpCurrencyList, currentErpCurrency, new CountryGridAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(Long countryId, String name, ErpCurrency erpCurrency) {
                mErpCurrency = erpCurrency;
                setupCountryView();
            }
        });
        dialog.show();
    }

    void setupCountryView() {
        if (mErpCurrency == null) {
            mTvCountry.setText(ShopInfoCfg.getInstance().getCurrency().getAreaCode());
        }
        if (mErpCurrency != null)
            mTvCountry.setText(mErpCurrency.getCountryAreaCode());
    }

    @Click(R.id.queue_create_takenumber_btn)
    protected void submit() {
        MobclickAgentEvent.onEvent(UserActionCode.PD030001);
        if (ClickManager.getInstance().isClicked()) return;
        String personStr = mQueueCreateCount.getText().toString();
        if (TextUtils.isEmpty(personStr)) {
            ToastUtil.showShortToast(R.string.queue_count_format);
            return;
        }
        personStr = personStr.substring(0, personStr.length() - 1);
        final int personCount = Utils.toInt(personStr);
        if (personCount == 0) {
            ToastUtil.showShortToast(R.string.queue_personcount_codititon);
            return;
        }

        final String mobile = mQueueCreatePhonenum.getText().toString();
        if (!TextUtils.isEmpty(mobile)
                && mErpCurrency != null
                && !TextUtils.isEmpty(mErpCurrency.getPhoneRegulation())
                && !Pattern.matches(mErpCurrency.getPhoneRegulation(), mobile)) {
            ToastUtil.showShortToast(getString(R.string.customer_mobile_regulation_error));
            return;
        }

        submitQueue(personCount, mobile);
    }

    private void submitQueue(final int personCount, final String mobile) {
        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        customerOperates.findCustomerByPhone(mobile, new SimpleResponseListener<CustomerMobileVo>() {

            @Override
            public void onSuccess(ResponseObject<CustomerMobileVo> response) {
                CustomerMobile customer = response.getContent().result;
                final String name = customer.customerName;
                final int sex = customer.getSex();
                submitTake(personCount, mobile, name, Sex.toEnum(sex));
            }

            @Override
            public void onError(VolleyError error) {
                submitTake(personCount, mobile, null, null);
            }
        });
    }

    private void submitTake(final int personCount, final String mobile, final String name, final Sex sex) {
        TaskContext.bindExecute(this, new SimpleAsyncTask<Void>() {
            @Override
            public Void doInBackground(Void... params) {
                queue = QueueOpManager.getInstance().createQueue(personCount, mobile, name, sex);
                if (mErpCurrency != null && queue != null) {
                    queue.setNationalTelCode(mErpCurrency.getAreaCode());
                    queue.country = mErpCurrency.getCountryZh();
                    queue.nation = mErpCurrency.getCountryEn();
                }
                return null;
            }

            @Override
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                submitRequest();
            }
        });
    }

    private void submitRequest() {
        if (queue != null) {
            QueueOpManager.getInstance().createQueueRequest(getActivity(), queue, new SimpleResponseListener<Queue>() {
                @Override
                public void onSuccess(ResponseObject<Queue> response) {
                    try {
                        final Queue respQueue = response.getContent();
                        if (respQueue != null) {
                            NewQueueBeanVo queueBeanVo = new NewQueueBeanVo();
                            queueBeanVo.setQueue(respQueue);
                            queueBeanVo.setQueueExtra(respQueue.queueExtra);
                            //QueueOpManager.getInstance().doPrint(queueBeanVo, new PRTOnSimplePrintListener(PrintTicketTypeEnum.QUEUE));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    super.onError(error);
                }
            });//创建队列并请求发券;

            //更新第二屏
            DisplayServiceManager.doUpdateQueue(getActivity().getApplicationContext());
        }
    }

    @Override
    public void click(String value) {
        switch (inputtype) {
            case 1:
                switch (value) {
                    case "clear":
                        mQueueCreateCount.setText("");
                        break;
                    case "delete":
                        mKeyBorad.delte();
                        break;
                    default:
                        mKeyBorad.insert(value);
                        break;
                }
                break;
            case 2:
                switch (value) {
                    case "clear":
                        mQueueCreatePhonenum.setText("");
                        break;
                    case "delete":
                        mKeyBorad.delte();
                        break;
                    default:
                        mKeyBorad.insert(value);
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置当前编辑栏
     *
     * @param input
     */
    private void setCurrentInput(EditText input) {
        mKeyBorad.setCurrentInput(input);
    }

}
