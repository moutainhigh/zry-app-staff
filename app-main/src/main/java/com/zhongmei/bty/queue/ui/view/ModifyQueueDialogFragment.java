package com.zhongmei.bty.queue.ui.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.zhongmei.bty.basemodule.database.queue.QueueOrderSource;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.data.operates.QueueOperates;
import com.zhongmei.bty.data.operates.message.content.QueueModifyReq;
import com.zhongmei.bty.data.operates.message.content.QueueReq;
import com.zhongmei.bty.data.operates.message.content.QueueResp;
import com.zhongmei.bty.queue.manager.QueueOpManager;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.queue_modify_dialog_fragment)
public class ModifyQueueDialogFragment extends BasicDialogFragment {

    public static final String TAG = ModifyQueueDialogFragment.class.getSimpleName();

    @ViewById(R.id.queue_count)
    protected EditText mEtCount;

    @ViewById(R.id.queue_phonenum)
    protected EditText mEtPhoneNum;

    @ViewById(R.id.queue_create_name)
    protected EditText mEtName;

    @ViewById(R.id.rlAreaCode)
    protected LinearLayout mLLCountry;

    @ViewById(R.id.tvAreaCodes)
    protected TextView tvAreaCode;

    @ViewById(R.id.queue_male)
    protected TextView mTvMale;

    @ViewById(R.id.queue_female)
    protected TextView mTvFemale;

    @ViewById(R.id.ib_close)
    protected ImageButton mTbClose;

    @ViewById(R.id.btn_ok)
    protected Button mBtOk;

    @ViewById(R.id.queue_create_memo)
    EditText mEtQueueMemo;

    private Queue mQueue;

    private Long levelId;

    private Sex mSex = Sex.MALE;

    /**
     * 当前商户国籍
     */
    private ErpCurrency mErpCurrency;

    private Map<String, ErpCurrency> erpCurrencyMap;

    private ErpCommercialRelationDal mErpDal;

    NewQueueBeanVo queueBeanVo;

    CommercialQueueLine queueLine;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = dialog.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        return dialog;
    }

    public void setQueue(NewQueueBeanVo queueBeanVo) {
        this.queueBeanVo = queueBeanVo;
        mQueue = queueBeanVo.getQueue();
    }

    @AfterViews
    void initView() {
        if (getActivity().getWindow()
                .getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            // 隐藏软键盘
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        mErpDal = OperatesFactory.create(ErpCommercialRelationDal.class);
        initAreaCode();

    }

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
                String areaCode = mQueue != null ? mQueue.getNationalTelCode() : "";
                if (!TextUtils.isEmpty(areaCode)) {
                    mErpCurrency = erpCurrencyMap.get(areaCode);
                }
                setupCountryView();
                refreshInfo();
            }
        });
    }

    void setupCountryView() {
        if (mErpCurrency == null) {
            String areaCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
            mErpCurrency = erpCurrencyMap.get(areaCode);
        }
        if (mErpCurrency != null) {
            tvAreaCode.setText(mErpCurrency.getCountryAreaCode());
        }
    }

    void refreshInfo() {
        if (mQueue != null) {
            mEtCount.setText(mQueue.getRepastPersonCount().toString());
            mEtName.setText(mQueue.getName());
            mEtPhoneNum.setText(mQueue.getMobile());
            mSex = mQueue.getSex();
            if (mSex == Sex.FEMALE) {
                mTvMale.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                mTvFemale.setTextColor(getContext().getResources().getColor(R.color.color_FF5363));
            } else {
                mTvFemale.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                mTvMale.setTextColor(getContext().getResources().getColor(R.color.color_FF5363));
            }
            mEtQueueMemo.setText(mQueue.getMemo() != null ? mQueue.getMemo() : "");
        }
    }

    @FocusChange(R.id.queue_phonenum)
    void onFocusChange(View v, boolean isFocus) {
        if (!isFocus) {
            setCustomerNameTextView();
        }
    }

    private void setCustomerNameTextView() {
        final String mQueuePhoneNum = mEtPhoneNum.getText().toString().trim();
        if (mQueuePhoneNum.length() < 7) {
            return;
        }
        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        customerOperates.findCustomerByPhone(mQueuePhoneNum, new SimpleResponseListener<CustomerMobileVo>() {

            @Override
            public void onSuccess(ResponseObject<CustomerMobileVo> response) {
                if (TextUtils.isEmpty(mQueuePhoneNum)) {
                    return;
                }

                CustomerMobile customer = response.getContent().result;
                levelId = customer.levelId;
                String name = customer.customerName;
                mEtName.setText(name);
                Integer sex = customer.getSex();
                if (sex == CustomerResp.SEX_MALE) {
                    mTvMale.callOnClick();
                } else if (sex == CustomerResp.SEX_FEMALE) {
                    mTvFemale.callOnClick();
                }

                mErpCurrency = erpCurrencyMap.get(customer.nationalTelCode);
                setupCountryView();
            }

            @Override
            public void onError(VolleyError error) {
                levelId = null;
            }
        });
    }

    @Click({R.id.ib_close, R.id.rlAreaCode, R.id.queue_male, R.id.queue_female, R.id.btn_ok})
    void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.ib_close:
                dismiss();
                break;
            case R.id.rlAreaCode:
                showCountryDialog(new ArrayList<>(erpCurrencyMap.values()), mErpCurrency);
                break;
            case R.id.queue_male:
                mSex = Sex.MALE;
                mTvMale.setTextColor(getContext().getResources().getColor(R.color.color_FF5363));
                mTvFemale.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                break;
            case R.id.queue_female:
                mSex = Sex.FEMALE;
                mTvMale.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                mTvFemale.setTextColor(getContext().getResources().getColor(R.color.color_FF5363));
                break;
            case R.id.btn_ok:
                submit();
                break;
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

    void submit() {
        String countStr = mEtCount.getText().toString().trim();
        if (TextUtils.isEmpty(countStr)) {
            ToastUtil.showShortToast(R.string.queue_count_format);
            return;
        }
        final int personCount = Integer.valueOf(countStr);
        if (personCount == 0) {
            ToastUtil.showShortToast(R.string.queue_personcount_codititon);
            return;
        }
        String mobile = mEtPhoneNum.getText().toString().trim();
        if (!TextUtils.isEmpty(mobile) && mErpCurrency != null && !TextUtils.isEmpty(mErpCurrency.getPhoneRegulation()) && !Pattern.matches(mErpCurrency.getPhoneRegulation(), mobile)) {
            ToastUtil.showShortToast(getString(R.string.customer_mobile_regulation_error));
            return;
        }
        TaskContext.bindExecute(getActivity(), new SimpleAsyncTask<CommercialQueueLine>() {
            @Override
            public CommercialQueueLine doInBackground(Void... params) {
                return QueueOpManager.getInstance().getQueueLineByPersonCount(personCount);
            }

            @Override
            public void onPostExecute(CommercialQueueLine commercialQueueLine) {
                super.onPostExecute(commercialQueueLine);

                if (commercialQueueLine != null) {
                    queueLine = commercialQueueLine;
                    if (mQueue.getQueueLineId().longValue() == commercialQueueLine.getId().longValue()) {
                        modifyQueue();
                    } else {
                        showDialogHint();
                    }
                } else {
                    ToastUtil.showShortToast(R.string.queue_line_empty);
                    return;
                }
            }
        });


    }

    void modifyQueue() {
        QueueOperates operates = OperatesFactory.create(QueueOperates.class);
        QueueModifyReq req = new QueueModifyReq();
        req.setName(mEtName.getText().toString().trim());
        req.setMobile(mEtPhoneNum.getText().toString().trim());
        req.setNationalTelCode(mErpCurrency.getAreaCode());
        req.setModifyDateTime(mQueue.getModifyDateTime());
        req.setRepastPersonCount(Integer.valueOf(mEtCount.getText().toString().trim()));
        req.setSynFlag(mQueue.getUuid());
        req.setSex(mSex.value().toString());
        req.setCountry(mErpCurrency.getCountryZh());
        req.setNation(mErpCurrency.getCountryEn());
        req.setMemo(mEtQueueMemo.getText().toString().trim());
        CalmResponseListener<ResponseObject<Queue>> listener = new CalmResponseListener<ResponseObject<Queue>>() {
            @Override
            public void onError(NetError error) {
                ToastUtil.showShortToast(error.getVolleyError().getMessage());
            }

            @Override
            public void onSuccess(ResponseObject<Queue> data) {
                if (data != null && data.getContent() != null) {
                    ToastUtil.showShortToast(R.string.operate_success);
                    try {
                        //QueueOpManager.getInstance().doPrint(queueBeanVo, new PRTOnSimplePrintListener(PrintTicketTypeEnum.QUEUE));
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                    dismiss();
                } else {
                    ToastUtil.showShortToast(data.getMessage());
                }
            }
        };
        operates.queueModify(getActivity(), req, listener);
    }

    void showDialogHint() {
        DialogUtil.showWarnConfirmDialog(getFragmentManager(), getString(R.string.queue_item_modify_hint),
                R.string.common_submit,
                R.string.common_cancel,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reQueue();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, "ShowQueueModifyHint");
    }

    void reQueue() {
        mQueue.setQueueStatus(QueueStatus.INVALID);
        mQueue.setSkipDatetime(new Date().getTime());
        DisplayServiceManager.doUpdateQueue(getContext().getApplicationContext());
        updateQueueStatus(QueueReq.Type.PASS, mQueue);
    }

    /**
     * 更新队列状态
     */
    private void updateQueueStatus(final int type, final Queue queue) {
        QueueOperates operates = OperatesFactory.create(QueueOperates.class);
        ResponseListener<QueueResp> listener = new ResponseListener<QueueResp>() {

            @Override
            public void onResponse(ResponseObject<QueueResp> response) {
                if (ResponseObject.isOk(response)) {
                    createQueue();
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }

        };
        operates.updateQueue(type, queue, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    void createQueue() {
        final int personCount = Integer.valueOf(mEtCount.getText().toString().trim());
        TaskContext.bindExecute(getActivity(), new SimpleAsyncTask<Queue>() {
            @Override
            public Queue doInBackground(Void... params) {
                return createQueue(personCount, mEtPhoneNum.getText().toString().trim());
            }

            @Override
            public void onPostExecute(Queue queue) {
                super.onPostExecute(queue);
                if (queue != null) {
                    submitCreateQueue(queue);
                } else {
                    ToastUtil.showShortToast(R.string.queue_line_empty);
                }
            }
        });
    }

    private Queue createQueue(int personCount, String mobile) {
        String name = mEtName.getText().toString();
        final Queue queue = QueueOpManager.getInstance().createQueue(personCount, mobile, name, mSex);
        if (queue != null) {
            queue.levelId = levelId;
            queue.setQueueSource(QueueOrderSource.DaoDian);
            if (mErpCurrency != null) {
                queue.setNationalTelCode(mErpCurrency.getAreaCode());
                queue.country = mErpCurrency.getCountryZh();
                queue.nation = mErpCurrency.getCountryEn();
            }
            queue.setMemo(mEtQueueMemo.getText().toString().trim());
        }
        return queue;
    }

    private void submitCreateQueue(Queue queue) {
        QueueOpManager.getInstance().createQueueRequest(getActivity(), queue, new SimpleResponseListener<Queue>() {
            @Override
            public void onSuccess(ResponseObject<Queue> response) {
                if (response.getContent() != null) {
                    try {
                        queueBeanVo.setQueue(response.getContent());
                        queueBeanVo.setQueueExtra(response.getContent().queueExtra);
                        queueBeanVo.setQueueLine(queueLine);
                        //QueueOpManager.getInstance().doPrint(queueBeanVo, new PRTOnSimplePrintListener(PrintTicketTypeEnum.QUEUE));
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                    ToastUtil.showShortToast(R.string.operate_success);
                    dismiss();
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }

            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                ToastUtil.showShortToast(error.getMessage());
            }
        });//创建队列并请求发券;
        DisplayServiceManager.doUpdateQueue(getActivity());
    }
}
