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

public class customListAdapter extends BaseAdapter {

    private Context mContext;
    private List<customChatadapter> mAdapterList2;

    public customListAdapter(Context mContext, List<customChatadapter> mAdapterList) {
        this.mContext = mContext;
        this.mAdapterList2 = mAdapterList2;
    }

    @Override
    public int getCount() {
        return mAdapterList2.size();
    }

    @Override
    public Object getItem(int i) {
        return mAdapterList2.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View v =  View.inflate(mContext, R.layout.custom_chatlistview,null);
        TextView tvName= (TextView)v.findViewById(R.id.txtMessage);
        ImageView imgUser =(ImageView)v.findViewById(R.id.friendpic);

        v.setTag(mAdapterList2.get(i).getS_name());

        tvName.setText(mAdapterList2.get(i).getS_name());
        Picasso.with(mContext)
                .load(mAdapterList2.get(i).getS_image())
                .resize(100,100)
                .into(imgUser);

        return v;
    }
}
