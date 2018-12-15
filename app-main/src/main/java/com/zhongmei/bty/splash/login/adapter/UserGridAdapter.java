package com.zhongmei.bty.splash.login.adapter;

import java.util.List;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.splash.login.UserGridItem;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserGridAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private static final String Tag = "UserGridAdapter";

    private List<UserGridItem> list;

    private LayoutInflater mInflater;

    OnItemClikListener mListener;

    private Context mContext;

    public UserGridAdapter(Context context, List<UserGridItem> list,
                           OnItemClikListener listener) {
        this.list = list;
        mInflater = LayoutInflater.from(context);
        mListener = listener;
        mContext = context;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.user_grid_item, parent, false);
            convertView.setTag(mViewHolder);
            mViewHolder.tvName = (TextView) convertView.findViewById(R.id.user_name);
            mViewHolder.imgAnchor = (ImageView) convertView.findViewById(R.id.user_item_anchor);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final UserGridItem item = list.get(position);
        String userName = item.getUserName();
        mViewHolder.tvName.setText(userName);
        if (item.isSelected()) {
            mViewHolder.tvName.setBackgroundResource(R.drawable.bg_dinner_discount_pressed);
            mViewHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            mViewHolder.imgAnchor.setVisibility(View.VISIBLE);

        } else {
            mViewHolder.tvName.setBackgroundResource(R.drawable.bg_dinner_discount_normal);
            mViewHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.orderdish_text_black));
            mViewHolder.imgAnchor.setVisibility(View.INVISIBLE);
        }

        mViewHolder.tvName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(item);//modify v8.2
                    item.setSelected(!item.isSelected());
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder mHeaderHolder;
        if (convertView == null) {
            mHeaderHolder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.user_grid_header, parent, false);
            mHeaderHolder.mTextView = (TextView) convertView.findViewById(R.id.header);
            convertView.setTag(mHeaderHolder);
        } else {
            mHeaderHolder = (HeaderViewHolder) convertView.getTag();
        }
        String letters = list.get(position).getSortLetters();
        mHeaderHolder.mTextView.setText(letters);
        return convertView;
    }

    public static class ViewHolder {
        public TextView tvName;
        ImageView imgAnchor;
    }

    public static class HeaderViewHolder {
        public TextView mTextView;
    }

    /**
     * 根据gridView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public long getHeaderId(int position) {
        // TODO Auto-generated method stub
        return list.get(position).getSection();
    }

    public interface OnItemClikListener {
        public void onItemClick(UserGridItem item);
    }


    public interface OnUserSelectedListener {
        public void onSelected(User item, Long userId, String userName);
    }

}
