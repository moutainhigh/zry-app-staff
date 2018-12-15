package com.zhongmei.bty.basemodule.orderdish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class TableSeatChoiceAdapter extends BaseAdapter {
    private Context mContext;
    private List<TableSeat> mTableSeats;
    private TableSeat mCurTableSeat = null;
    private LayoutInflater mInflater;

    public TableSeatChoiceAdapter(Context context, List<TableSeat> tableSeats, TableSeat tableSeat) {
        this.mContext = context;
        this.mTableSeats = tableSeats;
        this.mCurTableSeat = tableSeat;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setCurTableSeat(TableSeat tableSeat) {
        if (mCurTableSeat != null && tableSeat.getId().longValue() == mCurTableSeat.getId().longValue()) {
            this.mCurTableSeat = null;
        } else {
            this.mCurTableSeat = tableSeat;
        }
    }

    public TableSeat getmCurTableSeat() {
        return mCurTableSeat;
    }

    @Override
    public int getCount() {
        if (Utils.isEmpty(mTableSeats)) {
            return 0;
        }
        return mTableSeats.size();
    }

    @Override
    public TableSeat getItem(int position) {
        if (Utils.isEmpty(mTableSeats)) {
            return null;
        }
        return mTableSeats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int positoin, View contentView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (contentView == null) {
            contentView = mInflater.inflate(R.layout.orderdish_gv_item_choice_seat, null);
            viewHolder = new ViewHolder();
            viewHolder.cb_seat = (CheckBox) contentView.findViewById(R.id.cb_seat);
            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        TableSeat tableSeat = getItem(positoin);

        viewHolder.cb_seat.setText(tableSeat.getSeatName());

        if (mCurTableSeat != null && mCurTableSeat.getId().longValue() == tableSeat.getId().longValue()) {
            viewHolder.cb_seat.setChecked(true);
        } else {
            viewHolder.cb_seat.setChecked(false);
        }

        return contentView;
    }


    private class ViewHolder {
        public CheckBox cb_seat;
    }
}
