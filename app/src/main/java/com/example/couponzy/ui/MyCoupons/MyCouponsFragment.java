package com.example.couponzy.ui.MyCoupons;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.couponzy.Adapter.MyAdapter;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MyCouponsFragment extends Fragment {

    private MyCouponsViewModel myCouponsViewModel;
    private RecyclerView postslist;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton addPost;
    SwipeRefreshLayout sref;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myCouponsViewModel = new ViewModelProvider(this).get(MyCouponsViewModel.class);

        View view = inflater.inflate(R.layout.fragment_mycoupons, container, false);

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

        sref = view.findViewById(R.id.mycouponslist_swipe);

        sref.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sref.setRefreshing(true);
                ReloadData();

            }
        });

        myCouponsViewModel.getMyCoupons().observe(getViewLifecycleOwner(), new Observer<List<Coupon>>() {
            @Override
            public void onChanged(List<Coupon> coupons) {
                mAdapter = new MyAdapter(myCouponsViewModel.getMyCoupons().getValue());
                postslist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    void ReloadData() {
        addPost.setEnabled(false);
        model.instance.refreshMyCoupons(new model.getMyCouponsListener() {
            @Override
            public void onComplete() {
                addPost.setEnabled(true);
                sref.setRefreshing(false);
            }
        });
    }
}