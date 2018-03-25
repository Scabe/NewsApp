package com.example.newsapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsapp.Custom.MyImageView;
import com.example.newsapp.Custom.MyRecyclerViewAdapter2;
import com.example.newsapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by 晨阳大帅比 on 2018/3/8.
 */

public class ContactsFragment extends Fragment{

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MyRecyclerViewAdapter2 adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<String> list = new ArrayList<>();
        for (int i=0;i<40;i++)
            list.add(i+"");
        adapter = new MyRecyclerViewAdapter2();
        adapter.setData(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int fpos = linearLayoutManager.findFirstVisibleItemPosition();
                int lpos = linearLayoutManager.findLastVisibleItemPosition();
                for(int i = fpos;i <= lpos;i++){
                    View v = linearLayoutManager.findViewByPosition(i);
                    MyImageView myImageView = v.findViewById(R.id.id_tv_ad);
                    if(myImageView.getVisibility()==View.VISIBLE)
                        myImageView.setDy(linearLayoutManager.getHeight()-v.getTop(),linearLayoutManager.getHeight());
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }


}
