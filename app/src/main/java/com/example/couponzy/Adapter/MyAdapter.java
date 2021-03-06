package com.example.couponzy.Adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.couponzy.Model.Coupon;
import com.example.couponzy.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ExampleViewHolder> {
    public List<Coupon> list = new ArrayList<>();
    public HashMap<String,String>CheckImg=new HashMap<String, String>();
    LayoutInflater inflater;
    private MyOnItemClickListener listener;

    public interface MyOnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price, priceDiscount, description, expireDate, distance, couponCode;
        public ImageView userImage, postImage;

        public ExampleViewHolder(View itemView, MyOnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_main_title);
            price = itemView.findViewById(R.id.textView_main_price);
            priceDiscount = itemView.findViewById(R.id.textView_main_price_afterDiscount);
            description = itemView.findViewById(R.id.textView_main_description);
            expireDate = itemView.findViewById(R.id.textView_main_valid_till);
            distance = itemView.findViewById(R.id.textView_main_distance);
            couponCode = itemView.findViewById(R.id.textView_main_coupon_code);
            userImage = itemView.findViewById(R.id.imageView_post_userImg);
            postImage = itemView.findViewById(R.id.imageView_post_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int postion = getAdapterPosition();
                        if (postion != RecyclerView.NO_POSITION) {
                            listener.onItemClick(postion);
                        }
                    }
                }
            });

        }
    }

    public MyAdapter(List<Coupon> exampleList) {
        list = exampleList;
    }

    public MyAdapter(LayoutInflater layoutInflater) {
        inflater = layoutInflater;
    }

    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_list_row2, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, listener);
        return evh;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        DecimalFormat decim = new DecimalFormat("#,###.00");
        Coupon coupon = list.get(position);
        holder.title.setText(coupon.getTitle());
        holder.price.setText("₪ " + String.valueOf(decim.format(coupon.price)));
        holder.priceDiscount.setText("₪ " + String.valueOf(decim.format(coupon.discountPrice)));
        holder.description.setText(coupon.getDescription());
        holder.expireDate.setText(coupon.getExpireDate());
        holder.distance.setText(coupon.getDistance());
        holder.couponCode.setText(coupon.getCouponCode());
        holder.userImage.setImageResource(R.drawable.ic_baseline_person_24);
        holder.postImage.setImageResource(R.drawable.ic_launcher_background);
        if (coupon.getPostImg() != null) {
            Picasso.get().load(coupon.getPostImg()).placeholder(R.drawable.ic_launcher_background).into(holder.postImage);
        }
        //TODO:Check if this is needed , this is used for the bug in recycler view were pictures are changed !!
        if(CheckImg.get(coupon.getid())==null) {
            CheckImg.put(coupon.getid(), coupon.getProfileImg());
        }
            Picasso.get().load(CheckImg.get(coupon.getid())).placeholder(R.drawable.ic_baseline_person_24).into(holder.userImage);

//            if (coupon.getProfileImg() != null ) {
//                Picasso.get().load(coupon.getProfileImg()).placeholder(R.drawable.ic_baseline_person_24).into(holder.userImage);
//            }

    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {

        if (list == null) {
            return 0;
        }
        return list.size();


    }


}
