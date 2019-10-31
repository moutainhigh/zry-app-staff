package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.beauty.entity.AppContent;
import com.zhongmei.yunfu.R;

import java.util.List;

public class ModuleAdapter extends BaseAdapter {
    private List<AppContent> mListApps;
    private Context mContext;
    private LayoutInflater mInflater;

    public ModuleAdapter(Context context, List<AppContent> datas){
        this.mContext=context;
        this.mListApps=datas;
        this.mInflater=LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        if(mListApps!=null){
            return mListApps.size();
        }
        return 0;
    }

    @Override
    public AppContent getItem(int position) {
        if(mListApps!=null && mListApps.size()>position){
            return mListApps.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.item_apps_launcher,null);
            holdView=new HoldView();

            holdView.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
            holdView.tv_name=(TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(holdView);
        }else{
            holdView= (HoldView) convertView.getTag();
        }

        convertView.setBackgroundColor(position%2==0?mContext.getResources().getColor(R.color.blue):mContext.getResources().getColor(R.color.beauty_color_btn_pink_nomal));

        AppContent app=getItem(position);
        holdView.iv_icon.setBackgroundResource(app.getModuleIcon());
        holdView.tv_name.setText(app.getModuleName());

        return convertView;
    }


    class HoldView{
        public ImageView iv_icon;
        public TextView tv_name;
        public TextView tv_tip;//暂时不用
    }
}
