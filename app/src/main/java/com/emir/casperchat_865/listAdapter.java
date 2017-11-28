package com.emir.casperchat_865;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Emir on 20.11.2017.
 */

public class listAdapter extends BaseAdapter {

    private Context mContext;
    private List<adapter> mAdapterList;

    public listAdapter(Context mContext, List<adapter> mAdapterList) {
        this.mContext = mContext;
        this.mAdapterList = mAdapterList;
    }

    @Override
    public int getCount() {
        return mAdapterList.size();
    }

    @Override
    public Object getItem(int i) {
        return mAdapterList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View v =  View.inflate(mContext, R.layout.customlistview_friend,null);
        TextView tvName= (TextView)v.findViewById(R.id.textView2FrienMail);
        ImageView imgUser =(ImageView)v.findViewById(R.id.imageViewFriend);

        v.setTag(mAdapterList.get(i).getS_name());

        tvName.setText(mAdapterList.get(i).getS_name());
        Picasso.with(mContext)
                .load(mAdapterList.get(i).getS_image())
                .resize(100,100)
                .into(imgUser);

        return v;
    }
}
