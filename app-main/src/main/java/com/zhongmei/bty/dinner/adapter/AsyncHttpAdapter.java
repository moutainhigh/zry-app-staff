package com.zhongmei.bty.dinner.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.async.util.AsyncNetworkUtil;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpType;

import java.util.List;


/**
 * Created by demo on 2018/12/15
 */

public class AsyncHttpAdapter extends BaseAdapter {

    private Context mContext;
    private List<AsyncHttpRecord> mListAsyncHttpRecord;
    private LayoutInflater mInflater;
    private ViewHolder mViewHolder;
    private AsyncControlListener mRequestControlListener;

    public AsyncHttpAdapter(Context context, List<AsyncHttpRecord> data) {
        this.mContext = context;
        this.mListAsyncHttpRecord = data;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setRequestControlListener(AsyncControlListener listener) {
        this.mRequestControlListener = listener;
    }

    @Override
    public int getCount() {
        if (mListAsyncHttpRecord == null) {
            return 0;
        }
        return mListAsyncHttpRecord.size();
    }

    @Override
    public AsyncHttpRecord getItem(int position) {
        if (mListAsyncHttpRecord == null) {
            return null;
        }
        return mListAsyncHttpRecord.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_lv_asynchttprecord, null);
            mViewHolder = new ViewHolder();
            mViewHolder.tv_recordInfo = (TextView) convertView.findViewById(R.id.tv_recordInfo);
            mViewHolder.tv_reTry = (TextView) convertView.findViewById(R.id.tv_retry);
            mViewHolder.tv_cancel = (TextView) convertView.findViewById(R.id.tv_cancel);
            convertView.setTag(mViewHolder);
        }

        mViewHolder = (ViewHolder) convertView.getTag();

        bindData(getItem(position), mViewHolder, position);

        return convertView;
    }


    private void bindData(AsyncHttpRecord record, ViewHolder viewHolder, int position) {
        if (record == null) {
            return;
        }

        String info = String.format(mContext.getString(R.string.async_http_record_item_hint), TextUtils.isEmpty(record.getTableName()) ? "" : record.getTableName(), AsyncNetworkUtil.getSerialNumber(record),
                AsyncNetworkUtil.getType(record.getType()), AsyncNetworkUtil.getStatus(record.getStatus()));
        int iconHintRes = R.drawable.icon_dinner_asynchttp_failed;

        switch (record.getStatus()) {
            case SUCCESS://成功
                viewHolder.tv_cancel.setVisibility(View.GONE);
                viewHolder.tv_reTry.setVisibility(View.GONE);
                iconHintRes = R.drawable.icon_dinner_asynchttp_succeed;
                break;
            case RETRING://重试中
            case EXCUTING://执行中
                viewHolder.tv_cancel.setVisibility(View.VISIBLE);
                viewHolder.tv_reTry.setVisibility(View.GONE);
                iconHintRes = R.drawable.icon_dinner_asynchttp_failed;
                break;
            case FAILED://失败
                viewHolder.tv_cancel.setVisibility(View.VISIBLE);
                viewHolder.tv_reTry.setVisibility(View.VISIBLE);
                iconHintRes = R.drawable.icon_dinner_asynchttp_failed;
                break;
        }

        viewHolder.tv_recordInfo.setText(info);

        Drawable drawable = mContext.getResources().getDrawable(iconHintRes);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        viewHolder.tv_recordInfo.setCompoundDrawables(drawable, null, null, null);//画在右边

        OnClickListener onClickListener = new OnClickListener(record, position);
        viewHolder.tv_cancel.setOnClickListener(onClickListener);
        viewHolder.tv_reTry.setOnClickListener(onClickListener);
    }

    class OnClickListener implements View.OnClickListener {
        private int mPosition;
        private AsyncHttpRecord record;

        public OnClickListener(AsyncHttpRecord record, int position) {
            this.mPosition = position;
            this.record = record;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_retry:
                    if (mRequestControlListener != null) {
                        mRequestControlListener.reTryRequest(record, mPosition);
                    }
                    break;
                case R.id.tv_cancel:
                    if (mRequestControlListener != null) {
                        mRequestControlListener.cancelRequest(record, mPosition);
                    }
                    break;
            }
        }
    }

    class ViewHolder {
        public TextView tv_recordInfo;
        public TextView tv_reTry;
        public TextView tv_cancel;
    }

    public interface AsyncControlListener {
        /**
         * @param record
         * @param position
         * @return true 取消成功 false:取消失败
         */
        boolean cancelRequest(AsyncHttpRecord record, int position);

        /**
         * @param record
         * @param position
         * @return true:重试成功 false:重试失败
         */
        boolean reTryRequest(AsyncHttpRecord record, int position);
    }

}
