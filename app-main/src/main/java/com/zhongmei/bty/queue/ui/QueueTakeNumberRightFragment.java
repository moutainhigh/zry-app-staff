package com.zhongmei.bty.queue.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueOrderSource;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
import com.zhongmei.bty.basemodule.input.NumberKeyBoardUtils;
import com.zhongmei.bty.common.util.CommonKeyBorad;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.queue.event.RefreshInfoEvent;
import com.zhongmei.bty.queue.event.UpdatePersonEvent;
import com.zhongmei.bty.queue.manager.QueueOpManager;
import com.zhongmei.bty.queue.ui.view.KeyboardFragment;
import com.zhongmei.bty.queue.ui.view.KeyboardFragment_;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ICurrency;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.util.ValueEnums;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.queue_create_takenumber)
public class QueueTakeNumberRightFragment extends BasicFragment implements KeyboardFragment.OnKeyBoradListener {
    private static final String TAG = QueueTakeNumberRightFragment.class.getSimpleName();

    @ViewById(R.id.queue_keyboard_panel)
    LinearLayout mQueueKeyboardPanle;

    @ViewById(R.id.queue_name_sex_panel)
    LinearLayout mQueueNameAndSexPanle;

    @ViewById(R.id.queue_male)
    TextView ivMale;

    @ViewById(R.id.queue_female)
    TextView ivFeMale;

    @ViewById(R.id.queue_create_count)
    EditText mQueueCreateCount;

    @ViewById(R.id.queue_create_phonenum)
    EditText mQueueCreatePhonenum;

    @ViewById(R.id.queue_create_name)
    EditText mQueueCreateName;

    @ViewById(R.id.rlAreaCode)
    LinearLayout mCountryLayout;

    @ViewById(R.id.tvAreaCodes)
    TextView mTvCountry;

    @ViewById(R.id.queue_create_memo)
    EditText mEtQueueMemo;

    private int inputtype = 0;

    private Sex mSex = Sex.MALE;

    private boolean isAutoTakenum = false;

    private boolean isFromHistroyFlag = false;

    private CustomerManager customerManager;

    private CommonKeyBorad mKeyBorad;

    private QueueOrderSource orderSource;

    private final String Suffix = MainApplication.getInstance().getString(R.string.queue_person);
    private Long levelId; //	等级Id

    /**
     * 当前商户国籍
     */
    private ErpCurrency mErpCurrency;

    private Map<String, ErpCurrency> erpCurrencyMap;

    private ErpCommercialRelationDal mErpDal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        registerEventBus();
        mErpDal = OperatesFactory.create(ErpCommercialRelationDal.class);
        mKeyBorad = new CommonKeyBorad();
    }

    @AfterViews
    void initView() {

        if (getActivity().getWindow()
                .getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            // 隐藏软键盘
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        KeyboardFragment keyboardFragment = new KeyboardFragment_();
        keyboardFragment.setOnKeyBoradListener(this);
        customerManager = CustomerManager.getInstance();
        fragmentTransaction.replace(R.id.queue_keyboard_panel, keyboardFragment);
        fragmentTransaction.commitAllowingStateLoss();
        mQueueCreateCount.setOnFocusChangeListener(focusListener);
        mQueueCreatePhonenum.setOnFocusChangeListener(focusListener);
        mQueueCreateName.setOnFocusChangeListener(focusListener);
        if (isAutoTakenum) {
            mQueueNameAndSexPanle.setVisibility(View.GONE);
        }
        mQueueCreateName.setFilters(new InputFilter[]{filter});
        NumberKeyBoardUtils.setTouchListener(mQueueCreateCount);
        NumberKeyBoardUtils.setTouchListener(mQueueCreatePhonenum);
        mQueueCreateCount.requestFocus();
        initAreaCode();
    }

    void initAreaCode() {
        TaskContext.bindExecute(getActivity(), new SimpleAsyncTask<Map<String, ErpCurrency>>() {
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
                String areaCode = "";
                ICurrency currency = ShopInfoCfg.getInstance().getCurrency();
                if (currency != null) {
                    areaCode = currency.getAreaCode();
                }
                if (!TextUtils.isEmpty(areaCode)) {
                    mErpCurrency = erpCurrencyMap.get(areaCode);
                }
                setupCountryView();
                refreshInfo();
            }
        });
    }

    void setupCountryView() {
        if (mErpCurrency != null) {
            mTvCountry.setText(mErpCurrency.getCountryAreaCode());
        }
    }

    /**
     * 刷新界面
     */
    private void refreshInfo() {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            String type = intent.getStringExtra("type");
            if (!TextUtils.isEmpty(type)) {
                if ("2".equals(type)) {
                    String customerName = intent.getStringExtra("name");
                    String phoneNO = intent.getStringExtra("phoneNO");
                    String nationalTelCode = intent.getStringExtra("nationalTelCode");
                    String countryEN = intent.getStringExtra("countryEN");
                    String countryZH = intent.getStringExtra("countryZH");
                    if (!TextUtils.isEmpty(nationalTelCode)) {
                        mErpCurrency = erpCurrencyMap.get(nationalTelCode);
                    }
                    setupCountryView();
                    String sex = intent.getStringExtra("sex");
                    String queueOrderSouce = intent.getStringExtra("orderSource");
                    mQueueCreatePhonenum.setText(phoneNO);
                    //booking.setNationalTelCode(nationalTelCode);
                    mQueueCreateName.setText(customerName);
                    if (sex != null && sex.equals("0")) {
                        mSex = Sex.FEMALE;
                        ivMale.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                        ivFeMale.setTextColor(getContext().getResources().getColor(R.color.color_FF5363));
                    } else {
                        mSex = Sex.MALE;
                        ivFeMale.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                        ivMale.setTextColor(getContext().getResources().getColor(R.color.color_FF5363));
                    }
                    orderSource = queueOrderSouce != null ? ValueEnums.toEnum(QueueOrderSource.class, queueOrderSouce) : QueueOrderSource.DianHua;
                }
            }
        }
    }

    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            // TODO Auto-generated method stub
            if (isDestroyView()) {
                return;
            }
            if (!hasFocus) {
                setCurrentInput(null);
                if (v.getId() == R.id.queue_create_phonenum) {
                    setCustomerNameTextView();
                }
                return;
            }
            switch (v.getId()) {
                case R.id.queue_create_count:
                    mQueueCreateName.clearFocus();
                    inputtype = 1;
                    setPersonText();
                    break;
                case R.id.queue_create_phonenum:
                    mQueueCreateName.clearFocus();
                    inputtype = 2;
                    mKeyBorad.setDefaultSuffix("");
                    setCurrentInput(mQueueCreatePhonenum);
                    break;

                case R.id.queue_create_name:
                    inputtype = 0;
                    break;

            }
        }
    };

    @Click({R.id.queue_male, R.id.queue_female, R.id.tvAreaCodes})
    void click(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.queue_male:
                mSex = Sex.MALE;
                ivMale.setTextColor(getContext().getResources().getColor(R.color.color_FF5363));
                ivFeMale.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                break;
            case R.id.queue_female:
                mSex = Sex.FEMALE;
                ivMale.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                ivFeMale.setTextColor(getContext().getResources().getColor(R.color.color_FF5363));
                break;
            case R.id.tvAreaCodes:
                showCountryDialog(new ArrayList<>(erpCurrencyMap.values()), mErpCurrency);
            default:
                break;
        }
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

    private void setPersonText() {
        mKeyBorad.setMax(5);
        mKeyBorad.setDefaultSuffix(getString(R.string.queue_person));
        setCurrentInput(mQueueCreateCount);
    }

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
            int dindex = 0;
            int count = 0;

            int maxLen = 30;
            while (count <= maxLen && dindex < dest.length()) {
                char c = dest.charAt(dindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                return dest.subSequence(0, dindex - 1);
            }

            int sindex = 0;
            while (count <= maxLen && sindex < src.length()) {
                char c = src.charAt(sindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                sindex--;
            }

            return src.subSequence(0, sindex);
        }
    };

    @Click(R.id.queue_create_count)
    protected void inputPersonCount() {
        inputtype = 1;
        mQueueCreateName.clearFocus();
        setCurrentInput(mQueueCreateCount);
    }

    @AfterTextChange({R.id.queue_create_count})
    void afterTextChangedTextView(TextView tv) {
        if (!isAdded()) {
            return;
        }
        // Something Here
        String personStr = tv.getText().toString();
        if (TextUtils.isEmpty(personStr)) {
            personStr = String.valueOf(0);
        }
        if (personStr.endsWith(getString(R.string.queue_person))) {
            personStr = personStr.substring(0, personStr.length() - Suffix.length());
        }
        int personCount = Utils.toInt(personStr);
        if (personCount > 0) {
            EventBus.getDefault().post(new UpdatePersonEvent(personCount));
        }
    }

    private void setCustomerNameTextView() {
        if (mQueueCreatePhonenum.getText().toString().length() < 7) {
            return;
        }
        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        customerOperates.findCustomerByPhone(mQueueCreatePhonenum.getText().toString(), new SimpleResponseListener<CustomerMobileVo>() {

            @Override
            public void onSuccess(ResponseObject<CustomerMobileVo> response) {
                if (TextUtils.isEmpty(mQueueCreatePhonenum.getText())) {
                    return;
                }

                CustomerMobile customer = response.getContent().result;
                levelId = customer.levelId;
                String name = customer.customerName;
                if (TextUtils.isEmpty(mQueueCreateName.getText())) {
                    mQueueCreateName.setText(name);
                    Integer sex = customer.getSex();
                    if (sex == CustomerResp.SEX_MALE) {
                        ivMale.callOnClick();
                    } else if (sex == CustomerResp.SEX_FEMALE) {
                        ivFeMale.callOnClick();
                    }
                }
                if (!TextUtils.isEmpty(customer.nationalTelCode)) {
                    mErpCurrency = erpCurrencyMap.get(customer.nationalTelCode);
                    setupCountryView();
                }
            }

            @Override
            public void onError(VolleyError error) {
                levelId = null;
            }
        });
    }

    @Click(R.id.queue_create_takenumber_btn)
    protected void submit(final View v) {
        MobclickAgentEvent.onEvent(UserActionCode.PD020006);
        v.setEnabled(false);
        String personStr = mQueueCreateCount.getText().toString();
        if (TextUtils.isEmpty(personStr)) {
            ToastUtil.showShortToast(R.string.queue_count_format);
            v.setEnabled(true);
            return;
        }
        int personCount;
        if (personStr.contains(Suffix)) {
            CharSequence countString = personStr.subSequence(0, personStr.length() - Suffix.length());
            personCount = Utils.toInt(countString.toString());
        } else {
            personCount = Utils.toInt(personStr);
        }

        if (personCount == 0) {
            ToastUtil.showShortToast(R.string.queue_personcount_codititon);
            v.setEnabled(true);
            return;
        }

        final String mobile = mQueueCreatePhonenum.getText().toString();
        if (!TextUtils.isEmpty(mobile) && mErpCurrency != null && !TextUtils.isEmpty(mErpCurrency.getPhoneRegulation()) && !Pattern.matches(mErpCurrency.getPhoneRegulation(), mobile)) {
            ToastUtil.showShortToast(getString(R.string.customer_mobile_regulation_error));
            v.setEnabled(true);
            return;
        }

        final int _personCount = personCount;
        TaskContext.bindExecute(this, new SimpleAsyncTask<Queue>() {
            @Override
            public Queue doInBackground(Void... params) {
                return createQueue(_personCount, mobile);
            }

            @Override
            public void onPostExecute(Queue queue) {
                super.onPostExecute(queue);
                if (queue != null) {
                    submitCreateQueue(v, queue);
                } else {
                    ToastUtil.showShortToast(R.string.queue_line_empty);
                }
            }
        });
    }

    private Queue createQueue(int personCount, String mobile) {
        String name = mQueueCreateName.getText().toString();
        final Queue queue = QueueOpManager.getInstance().createQueue(personCount, mobile, name, mSex);
        if (queue != null) {
            queue.levelId = levelId;
            if (orderSource != null) {
                queue.setQueueSource(orderSource);
            }
            if (mErpCurrency != null) {
                queue.setNationalTelCode(mErpCurrency.getAreaCode());
                queue.country = mErpCurrency.getCountryZh();
                queue.nation = mErpCurrency.getCountryEn();
                queue.setMemo(mEtQueueMemo.getText().toString().trim());
            }
        }
        return queue;
    }

    private void submitCreateQueue(final View v, final Queue queue) {
        QueueOpManager.getInstance().createQueueRequest(getActivity(), queue, new SimpleResponseListener<Queue>() {
            @Override
            public void onSuccess(ResponseObject<Queue> response) {
                try {
                    final Queue respQueue = response.getContent();
                    if (respQueue != null) {
                        NewQueueBeanVo queueBeanVo = new NewQueueBeanVo();
                        queueBeanVo.setQueue(respQueue);
                        queueBeanVo.setQueueExtra(respQueue.queueExtra);
                        //  QueueOpManager.getInstance().doPrint(queueBeanVo, new PRTOnSimplePrintListener(PrintTicketTypeEnum.QUEUE));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }

                restInput();
                v.setEnabled(true);
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                v.setEnabled(true);
            }
        });//创建队列并请求发券;
        DisplayServiceManager.doUpdateQueue(getActivity());
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
                        mQueueCreateName.clearFocus();
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
                        mQueueCreateName.clearFocus();
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

    private void restInput() {
        orderSource = null;
        String areaCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
        if (!TextUtils.isEmpty(areaCode)) {
            mErpCurrency = erpCurrencyMap.get(areaCode);
            setupCountryView();
        }
        mQueueCreatePhonenum.setText("");
        mQueueCreateCount.setText("");
        mQueueCreateName.setText("");
        mEtQueueMemo.setText("");
        ivMale.callOnClick();
    }

    /**
     * 刷新
     *
     * @param aciton
     */
    public void onEventMainThread(RefreshInfoEvent aciton) {
        initAreaCode();
    }

}

