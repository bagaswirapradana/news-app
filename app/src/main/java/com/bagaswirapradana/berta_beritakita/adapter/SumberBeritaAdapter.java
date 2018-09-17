package com.bagaswirapradana.berta_beritakita.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bagaswirapradana.berta_beritakita.R;
import com.bagaswirapradana.berta_beritakita.penampung.SumberBerita;

import java.util.List;

public class SumberBeritaAdapter extends BaseAdapter {

    private Activity activity;
    private List<SumberBerita> sumberBeritaArrayList;
    private LayoutInflater mInflater;
    private SumberBerita sumberBerita;
    private ViewHolder mViewHolder;

    //public constructor
    public SumberBeritaAdapter(Activity activity, List<SumberBerita> sumberBeritas) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.sumberBeritaArrayList = sumberBeritas;
    }

    @Override
    public int getCount() {
        return sumberBeritaArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return sumberBeritaArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_menu, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.namaSumber = convertView.findViewById(R.id.tv_sumberBerita);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        sumberBerita = sumberBeritaArrayList.get(position);
        mViewHolder.namaSumber.setText(sumberBerita.getName());

        return convertView;
    }

    private static class ViewHolder {
        TextView namaSumber;
    }

}
