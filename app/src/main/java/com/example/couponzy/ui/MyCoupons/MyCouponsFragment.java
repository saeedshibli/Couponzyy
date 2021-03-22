package com.example.couponzy.ui.MyCoupons;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.couponzy.Adapter.MyAdapter;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ServerValue;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MyCouponsFragment extends Fragment {

    private MyCouponsViewModel homeViewModel;
    private RecyclerView postslist;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Coupon> data;
    ProgressBar progressBar;
    FloatingActionButton addPost;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(MyCouponsViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = view.findViewById(R.id.PostsList_progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        postslist = view.findViewById(R.id.main_recycler_v);
        postslist.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        postslist.setLayoutManager(mLayoutManager);

        addPost = view.findViewById(R.id.postslist_add_button);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_home_to_addCouponFragment);
            }
        });

        homeViewModel.getMyCoupons().observe(getViewLifecycleOwner(), new Observer<List<Coupon>>() {
            @Override
            public void onChanged(List<Coupon> coupons) {
                data = homeViewModel.getMyCoupons().getValue();
                mAdapter = new MyAdapter(data);
                postslist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });

        ReloadData();
        return view;
    }

    void ReloadData() {
        progressBar.setVisibility(View.VISIBLE);
        addPost.setEnabled(false);
        model.instance.refreshMyCoupons(new model.getMyCouponsListener() {
            @Override
            public void onComplete(List<Coupon> data) {
                progressBar.setVisibility(View.INVISIBLE);
                addPost.setEnabled(true);
            }
        });
    }
}