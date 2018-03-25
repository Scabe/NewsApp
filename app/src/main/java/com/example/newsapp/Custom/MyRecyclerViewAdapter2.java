package com.example.newsapp.Custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newsapp.R;

import java.util.List;

/**
 *
 * Created by 晨阳大帅比 on 2018/3/22.
 */

public class MyRecyclerViewAdapter2 extends RecyclerView.Adapter<MyRecyclerViewAdapter2.MyViewHolder>{
    private List<String> mdatas;

    public void setData(List<String> data){
        mdatas = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }


    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(position > 0 && position % 8 == 0){
            holder.img.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.GONE);
            holder.desc.setVisibility(View.GONE);
        }
        else{
            holder.img.setVisibility(View.GONE);
            holder.title.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mdatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        MyImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.id_tv_title);
            desc = itemView.findViewById(R.id.id_tv_desc);
            img = itemView.findViewById(R.id.id_tv_ad);
        }
    }
}
