package com.zhongmei.bty.queue.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.basemodule.queue.QueueDal;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.ui.base.BaseActivity;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;
import com.zhongmei.bty.data.operates.QueueOperates;
import com.zhongmei.bty.data.operates.message.content.QueueReq;
import com.zhongmei.bty.data.operates.message.content.QueueResp;
import com.zhongmei.bty.queue.adapter.QueueSettingAdapter;
import com.zhongmei.bty.queue.manager.QueueDataManager;
import com.zhongmei.bty.queue.manager.QueueOpManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 排队设置
 */
@EFragment(R.layout.queue_setting_frangment)
public class QueueSettingFragment extends BasicFragment {

    private static final String TAG = QueueSettingFragment.class.getSimpleName();

    @ViewById(R.id.queue_setting_right_layout)
    protected GridView listView;

    @ViewById(R.id.queue_clean_all)
    protected Button cleanAllBtn;

    @ViewById(R.id.empty_view)
    protected FrameLayout emptyView;

    private QueueSettingAdapter adapter;

    private QueueDataManager manager;

    private List<Queue> queueList;

    private List<CommercialQueueLine> queueLineList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initView() {
        adapter = new QueueSettingAdapter(this.getActivity());
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
        manager = new QueueDataManager();
        manager.register(new QueueDataManager.DataChangedListener() {

            @Override
            public void onChanged() {
                lodaView();
            }
        });
        lodaView();
    }

    @Click({R.id.queue_clean_all})
    public void onClick(View v) {
        new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(R.string.queue_list_clean_info)
                .iconType(R.drawable.commonmodule_dialog_icon_warning)
                .negativeText(R.string.common_cancel)
                .negativeLisnter(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {// 取消
                    }
                })
                .positiveText(R.string.common_submit)
                .positiveLinstner(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        resetAllQueueType(getRootActivity(), new ResponseListener<QueueResp>() {
                            @Override
                            public void onResponse(ResponseObject<QueueResp> response) {
                                queueResetAll();
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });
                    }
                })
                .build()
                .show(this.getFragmentManager(), TAG);

    }

    private void queueResetAll() {
        if (queueList != null && queueList.size() > 0) {
            List<Queue> cleanList = new ArrayList<Queue>();
            for (Queue queue : queueList) {
                if (queue.getIsZeroOped().equalsValue(YesOrNo.NO.value())) {
                    cleanList.add(queue);
                }
            }
            try {
                if (cleanList.size() > 0) {
                    QueueOpManager.getInstance().cleanQueueList(cleanList);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch
                // block
                Log.e(TAG, "", e);
            }
        }
    }

    public static void resetQueueType(BaseActivity fragment, Long queueLineId, final ResponseListener<QueueResp> listener) {
        updateQueueType(fragment, QueueReq.Type.RESET, null, queueLineId, listener);
    }

    public static void resetAllQueueType(BaseActivity fragment, final ResponseListener<QueueResp> listener) {
        updateQueueType(fragment, QueueReq.Type.RESET_ALL, null, null, listener);
    }

    private static void updateQueueType(final BaseActivity fragment, int type, String serverId, Long queueLineId, final ResponseListener<QueueResp> listener) {
        fragment.showLoadingProgressDialog();
        QueueOperates operates = OperatesFactory.create(QueueOperates.class);
        operates.changeQueue(type, serverId, queueLineId, null, new ResponseListener<QueueResp>() {

            @Override
            public void onResponse(ResponseObject<QueueResp> response) {
                if (ResponseObject.isOk(response)) {
                    if (listener != null) {
                        listener.onResponse(response);
                    }
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
                fragment.dismissLoadingProgressDialog();
            }

            @Override
            public void onError(VolleyError error) {
                fragment.dismissLoadingProgressDialog();
                ToastUtil.showLongToast(error.getMessage());
            }
        });
    }

    private void lodaView() {
        TaskContext.bindExecute(this, new SimpleAsyncTask<Void>() {

            @Override
            public Void doInBackground(Void... params) {
                try {
                    QueueDal queueDal = OperatesFactory.create(QueueDal.class);

                    queueList = queueDal.findAllDataByDate(new Date(), null, null);
                    queueLineList = queueDal.queryQueueLineList();
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return null;
            }

            public void onPostExecute(Void vod) {
                adapter.setQueueLineList(queueLineList);
                adapter.setQueueList(queueList);
                adapter.notifyDataSetChanged();
                if (Utils.isEmpty(queueLineList)) {
                    cleanAllBtn.setEnabled(false);
                    cleanAllBtn.setTextColor(getContext().getResources().getColor(R.color.color_bcbcbc));
                } else {
                    cleanAllBtn.setEnabled(true);
                    cleanAllBtn.setTextColor(getContext().getResources().getColor(R.color.color_FF8249));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        manager.unregister();
        super.onDestroyView();
    }
}
