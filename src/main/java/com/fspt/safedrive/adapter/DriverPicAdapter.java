package com.fspt.safedrive.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fspt.safedrive.R;

/**
 * Create by Spring on 2021/4/21 13:54
 */
public class DriverPicAdapter extends BaseAdapter {
    private int[] resIds;

    public DriverPicAdapter(int[] resids) {
        this.resIds = resids;
    }

    @Override
    public int getCount() {
        return resIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VH vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_item, parent, false);
            vh = new VH(convertView);
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }
        vh.iv.setImageResource(resIds[position]);
        vh.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iOnItemClickListener != null) iOnItemClickListener.onItemClick(position);
            }
        });

        return convertView;


    }


    private IOnItemClickListener iOnItemClickListener;

    public void registerOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
        this.iOnItemClickListener = iOnItemClickListener;
    }

    public interface IOnItemClickListener {
        void onItemClick(int pos);
    }

    static class VH implements View.OnClickListener {
        private ImageView iv;

        VH(View v) {
            iv = v.findViewById(R.id.iv);
            iv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    ;

}
