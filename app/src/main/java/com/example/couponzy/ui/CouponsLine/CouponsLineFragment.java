package com.example.couponzy.ui.CouponsLine;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.couponzy.Adapter.MyAdapter;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import java.util.List;

public class CouponsLineFragment extends Fragment {

    private CouponsLineViewModel couponsLineViewModel;
    private RecyclerView postslist;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout sref;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        couponsLineViewModel = new ViewModelProvider(this).get(CouponsLineViewModel.class);

        View view = inflater.inflate(R.layout.fragment_couponsline, container, false);

        postslist = (RecyclerView)view.findViewById(R.id.main_recycler_v_gallery);
        postslist.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        postslist.setLayoutManager(mLayoutManager);

        sref = view.findViewById(R.id.couponslist_swipe);

        sref.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sref.setRefreshing(true);
                ReloadData();
            }
        });

        couponsLineViewModel.getCoupons().observe(getViewLifecycleOwner(), new Observer<List<Coupon>>() {
            @Override
            public void onChanged(List<Coupon> coupons) {
                mAdapter = new MyAdapter(couponsLineViewModel.getCoupons().getValue());
                postslist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
    void ReloadData(){
        model.instance.refreshCoupons(new model.GetCouponsListener() {
            @Override
            public void onComplete() {
                sref.setRefreshing(false);
            }
        });
    }
}