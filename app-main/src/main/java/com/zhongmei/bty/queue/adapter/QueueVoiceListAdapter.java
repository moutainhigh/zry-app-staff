package com.zhongmei.bty.queue.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.bean.QueueVoiceVo;

/**
 * @date 2015年8月27日下午2:46:06
 */
public class QueueVoiceListAdapter extends BaseAdapter {
    public static final String TAG = QueueVoiceListAdapter.class.getSimpleName();

    private List<QueueVoiceVo> voiceList;

    private Context mContext;

    public QueueVoiceListAdapter(Context context, List<QueueVoiceVo> voiceList) {
        this.mContext = context;
        this.voiceList = voiceList;
    }

    @Override
    public int getCount() {
        if (voiceList != null) {
            return voiceList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return voiceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.queue_voice_list_item, null);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.queue_voice_item_layout);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.queue_voice_item_image);
            viewHolder.name = (TextView) convertView.findViewById(R.id.queue_voice_item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        QueueVoiceVo voice = voiceList.get(position);
        viewHolder.name.setText(voice.getName());
        if (voice.isSelected()) {
            viewHolder.layout.setBackgroundResource(R.drawable.queue_voice_list_background_selected);
            viewHolder.image.setBackgroundResource(R.drawable.queue_voice_list_image_selected);
            viewHolder.name.setTextColor(mContext.getResources().getColor(R.color.text_white));
        } else {
            viewHolder.name.setTextColor(mContext.getResources().getColor(R.color.text_black));
            viewHolder.layout.setBackgroundResource(R.drawable.queue_voice_list_background);
            viewHolder.image.setBackgroundResource(R.drawable.queue_voice_list_image);
        }
        return convertView;
    }


    class ViewHolder {

        LinearLayout layout;

        // 图片
        ImageView image;

        // 名称
        TextView name;

    }

}
