package com.zhongmei.bty.queue.ui.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.bty.booking.adapter.DishAdapter;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.queue.manager.QueueOpManager;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.bty.settings.view.XInnerListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 预点菜菜单展示、打印
 */
@EFragment(R.layout.queue_ex_menu_dialog)
public class ExMenuDetailDialog extends BasicDialogFragment {

    @ViewById(R.id.booking_info)
    TextView info;

    @ViewById(R.id.close)
    ImageView close;

    @ViewById(R.id.list)
    XInnerListView listView;

    @ViewById(R.id.dish_memo_ll)
    LinearLayout dish_memo_ll;

    @ViewById(R.id.dish_memo)
    TextView dish_memo;

    @ViewById(R.id.print)
    TextView print;

    private NewQueueBeanVo mQueueVo;

    private String mQueueNo = "";

    @AfterViews
    void initView() {
        // 必须设置成透明，不然窗口的圆角效果没了
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);


        TaskContext.bindExecute(getActivity(), new SimpleAsyncTask<Void>() {
            @Override
            public Void doInBackground(Void... params) {
                TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                if (mQueueVo != null && mQueueVo.getTradeRelation() != null) {
                    try {
                        mQueueVo.setTradeVo(tradeDal.findTrade(mQueueVo.getTradeRelation().getTradeId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            public void onPostExecute(Void mVoid) {
                super.onPostExecute(mVoid);
                initData();
            }
        });
    }

    private void initData() {
        String titleInfo = getString(R.string.booking_ex_menu_detail_people);
        Queue queue = mQueueVo.getQueue();
        String text = String.format(titleInfo,
                mQueueNo,
                null == queue.getName() ? getString(R.string.customer_no_name2) : queue.getName(),
                DateTimeUtils.formatDateTime(queue.getCreateDateTime()));

        info.setText(text);


        DishAdapter mDish = new DishAdapter(getActivity());
        listView.setAdapter(mDish);
        mDish.setDataSet(mQueueVo.getTradeVo());

        /*
         * 整单备注
         */
        String memo = mQueueVo.getTradeVo().getTrade().getTradeMemo();
        if (TextUtils.isEmpty(memo)) {
            dish_memo_ll.setVisibility(View.GONE);
        } else {
            dish_memo_ll.setVisibility(View.VISIBLE);
            dish_memo.setText(memo);
        }
    }

    /**
     * 通过此方法把数据传给dialog
     *
     * @param queueVo
     */
    public void setQueueVo(NewQueueBeanVo queueVo) {
        this.mQueueVo = queueVo;
    }

    @Click(R.id.print)
    void print() {
        try {
            QueueOpManager.getInstance().doNewPrint(mQueueVo);
        } catch (Exception e) {
            Log.e("ExMenuDetailDialog", "", e);
        }
        this.dismiss();
    }

    @Click(R.id.close)
    void close() {
        this.dismiss();
    }
}
