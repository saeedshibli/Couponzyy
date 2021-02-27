package com.example.couponzy.ui.gallery;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.couponzy.Adapter.MyAdapter;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private RecyclerView postslist;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Coupon> data=new ArrayList<Coupon>();
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        progressBar=view.findViewById(R.id.postsList_progressBar_gallery);
        postslist=(RecyclerView) view.findViewById(R.id.main_recycler_v_gallery);
        postslist.setHasFixedSize(true);
        String currentTime = ServerValue.TIMESTAMP.toString();
        String randomcode= Integer.toString(ThreadLocalRandom.current().nextInt(9000, 9999 + 1));
        /*model.instance.addPost(new HomeData("Saeed",FireBaseAuth.instance.getUid().toString(),currentTime,"","",currentTime,randomcode,"Bla bla","title",150,100), new model.AddPostsListener() {
           @Override
          public void onComplete() {

            }
        });*/
        model.instance.getCoupons(new model.GetCouponsListener()

            {
            @Override
            public void onComplete(List<Coupon> mydata) {
                data = mydata;


                postslist = (RecyclerView)view.findViewById(R.id.main_recycler_v_gallery);
                postslist.setHasFixedSize(true);

                mLayoutManager = new LinearLayoutManager(getContext());
                mAdapter = new MyAdapter(data);

                postslist.setLayoutManager(mLayoutManager);
                postslist.setAdapter(mAdapter);
                List<Coupon> finalData = data;

                ReloadData();
            }
        });
        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return view;
    }
    void ReloadData(){
        progressBar.setVisibility(View.VISIBLE);
        model.instance.getAllPosts(new model.GetAllPostsListener() {
            @Override
            public void onComplete(List<Coupon> data) {
                mAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}