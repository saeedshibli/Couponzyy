package com.example.couponzy.ui.home;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.couponzy.Adapter.MyAdapter;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView postslist;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Coupon> data=new ArrayList<Coupon>();
    ProgressBar progressBar;
    FloatingActionButton addPost;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        postslist=view.findViewById(R.id.main_recycler_v);
        postslist.setHasFixedSize(true);
        String currentTime = ServerValue.TIMESTAMP.toString();
        String randomcode= Integer.toString(ThreadLocalRandom.current().nextInt(9000, 9999 + 1));
        /*model.instance.addPost(new HomeData("Saeed",FireBaseAuth.instance.getUid().toString(),currentTime,"","",currentTime,randomcode,"Bla bla","title",150,100), new model.AddPostsListener() {
           @Override
          public void onComplete() {

            }
        });*/
        model.instance.getAllPosts(new model.GetAllPostsListener() {
            @Override
            public void onComplete(List<Coupon> mydata) {
                data = mydata;


                postslist = view.findViewById(R.id.main_recycler_v);
                postslist.setHasFixedSize(true);

                mLayoutManager = new LinearLayoutManager(getContext());
                mAdapter = new MyAdapter(data);

                postslist.setLayoutManager(mLayoutManager);
                postslist.setAdapter(mAdapter);
                List<Coupon> finalData = data;
               /* mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {


                        StudentListDirections.ActionStudentListToStudentDetails action = StudentListDirections.actionStudentListToStudentDetails(finalData.get(position).getID());
                        Navigation.findNavController(view)
                                .navigate(action);

                    }
                });*/


                addPost = view.findViewById(R.id.postslist_add_button);
                addPost.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   Navigation.findNavController(view)
                                                           .navigate(R.id.action_nav_home_to_addCouponFragment);
                                               }
                                           });
                         progressBar = view.findViewById(R.id.PostsList_progressBar);
                         progressBar.setVisibility(View.INVISIBLE);
                        //  imageView =view.findViewById(R.id.imageView);




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
        addPost.setEnabled(false);
        model.instance.getAllPosts(new model.GetAllPostsListener() {
            @Override
            public void onComplete(List<Coupon> data) {
                mAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.INVISIBLE);
                addPost.setEnabled(true);
            }
        });
    }
}