package com.zhongmei.bty.snack.orderdish.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.orderdish.bean.DishPageInfo;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 */

public class DishTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<DishPageInfo> dishBrandTypes;
    private DishPageInfo curPageInfo;

    public DishTypeAdapter(Context context,List<DishPageInfo> dishBrandTypes) {
        this.mContext = context;
        this.dishBrandTypes = dishBrandTypes;
        if(dishBrandTypes.size() > 0){
            curPageInfo=dishBrandTypes.get(0);
        }
    }

    public void setCurPageInfo(DishPageInfo curPageInfo){
        this.curPageInfo=curPageInfo;
    }

    public void setData(List<DishPageInfo> listDishPageInfo){
        this.dishBrandTypes.clear();
        if(Utils.isNotEmpty(listDishPageInfo)){
            this.dishBrandTypes.addAll(listDishPageInfo);
            setCurPageInfo(listDishPageInfo.get(0));
        }
    }

    @Override
    public void notifyDataSetChanged() {
        reSort();
        super.notifyDataSetChanged();
    }

    public void reSort(){//排序
        int position=curPageInfo==null?0:curPageInfo.position;
        Collections.sort(dishBrandTypes, new DishTypeComparator());

        Iterator it=dishBrandTypes.iterator();
        List<DishPageInfo> listPageInfo=new ArrayList<>();
        while (it.hasNext()){
            DishPageInfo dishTypeInfo= (DishPageInfo) it.next();
            if(dishTypeInfo.position<position){
                listPageInfo.add(dishTypeInfo);
                it.remove();
            }else{
                break;
            }
        }

        if(Utils.isNotEmpty(listPageInfo)){
            dishBrandTypes.addAll(listPageInfo);
        }
    }

    @Override
    public int getCount() {
        if(dishBrandTypes!=null){
            return dishBrandTypes.size();
        }
        return 0;
    }

    @Override
    public DishPageInfo getItem(int position) {
        return dishBrandTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.item_dish_type, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DishPageInfo dishBrandType = dishBrandTypes.get(position);
        viewHolder.fillData(dishBrandType,curPageInfo);
        return convertView;
    }



    public class ViewHolder{
        public View background;
        public TextView name;
        public ImageView arrow;

        public ViewHolder(View root) {
            background = root;
            name = (TextView) root.findViewById(R.id.name);
            arrow = (ImageView) root.findViewById(R.id.arrow);
            ViewGroup.LayoutParams params = name.getLayoutParams();
            params.width = (int) name.getPaint().measureText("测试五个字");
            name.setLayoutParams(params);
        }

        public void fillData(DishPageInfo type,DishPageInfo curType){
            if(curType!=null && curType.dishBrand.getId()==type.dishBrand.getId()){
                background.setBackgroundResource(R.drawable.dish_type_sel);
                arrow.setVisibility(View.VISIBLE);
            }else{
                background.setBackgroundResource(R.drawable.dish_type_nor);
                arrow.setVisibility(View.GONE);
            }
            name.setText(type.dishBrand.getName());
        }
    }

    public class DishTypeComparator implements Comparator<DishPageInfo>{

        @Override
        public int compare(DishPageInfo lhs, DishPageInfo rhs) {
            return lhs.position>rhs.position?1:-1;
        }
    }
}
