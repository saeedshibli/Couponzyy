package com.example.couponzy.ui.CouponsLine;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.couponzy.Adapter.MyAdapter;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import java.util.ArrayList;
import java.util.List;

public class CouponsLineFragment extends Fragment {

    private CouponsLineViewModel galleryViewModel;
    private RecyclerView postslist;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Coupon> data=new ArrayList<Coupon>();
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel = new ViewModelProvider(this).get(CouponsLineViewModel.class);

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        progressBar=view.findViewById(R.id.postsList_progressBar_gallery);
        progressBar.setVisibility(View.INVISIBLE);

        postslist = (RecyclerView)view.findViewById(R.id.main_recycler_v_gallery);
        postslist.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        postslist.setLayoutManager(mLayoutManager);

        galleryViewModel.getCoupons().observe(getViewLifecycleOwner(), new Observer<List<Coupon>>() {
            @Override
            public void onChanged(List<Coupon> coupons) {
                data = galleryViewModel.getCoupons().getValue();
                mAdapter = new MyAdapter(data);
                postslist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });

        ReloadData();
        return view;
    }
    void ReloadData(){
        progressBar.setVisibility(View.VISIBLE);
        model.instance.refreshCoupons(new model.GetCouponsListener() {
            @Override
            public void onComplete(List<Coupon> data) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}