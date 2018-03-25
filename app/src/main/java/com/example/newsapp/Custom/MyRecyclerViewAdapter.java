package com.example.newsapp.Custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsapp.Json.StoriesItemOne;
import com.example.newsapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by 晨阳大帅比 on 2018/3/10.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>{

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<StoriesItemOne> mDatas = new ArrayList<>();

    private View mHeaderView;

    private OnItemClickListener mListener;
    private OnAutoChangeListener mlistener;
    private Context mContext;

    public void setOnItemClickListener(OnItemClickListener li){
        mListener = li;
    }

    public void setOnAutoChangeListener(OnAutoChangeListener li){
        mlistener = li;
    }


    public void setHeaderView(View headerView){
        mHeaderView = headerView;
        //notifyItemInserted(0);
    }

    public void addDatas(List<StoriesItemOne> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView!=null&&viewType==TYPE_HEADER)
            return new MyViewHolder(mHeaderView);
        mContext = parent.getContext();
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        return new MyViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_HEADER)
            return;
        final int pos = getRealPosition(holder);
        if(mDatas!=null&&mDatas.size()!=0) {
            final StoriesItemOne data = mDatas.get(pos);
            holder.title.setText(data.getTitle());
            holder.des.setText("ZhiHu");
            //holder.img.setImageResource(R.drawable.timg);
            RequestOptions options = new RequestOptions().placeholder(R.drawable.load);//.override(140, 120);
            Glide.with(mContext).asDrawable().load(data.getImgs()).apply(options).into(holder.img);
            if (mListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(data.getId());
                    }
                });
            }
        }
        if(position==mDatas.size()-4){
            mlistener.onAutoChange();
        }
    }

    private int getRealPosition(MyViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null?position:position-1;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null)
            return TYPE_NORMAL;
        if(position == 0)
            return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return mHeaderView==null?mDatas.size():mDatas.size()+1;
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        super.onViewRecycled(holder);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView des;
        ImageView img;
        private MyViewHolder(View itemView){
            super(itemView);
            if(itemView == mHeaderView)
                return;
            title = itemView.findViewById(R.id.title_text);
            img = itemView.findViewById(R.id.title_pic);
            des = itemView.findViewById(R.id.descr_text);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int id);
    }

    public interface OnAutoChangeListener{
        void onAutoChange();
    }

}
