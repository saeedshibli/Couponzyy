package com.example.couponzy.ui.Coupon;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.CouponDao;
import com.example.couponzy.Model.CouponzyLocalDB;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import com.example.couponzy.ui.CouponsLine.CouponsLineViewModel;
import com.squareup.picasso.Picasso;


public class CouponDetailsFragment extends AddCouponFragment {
    Coupon coupon;
    private CouponDetailsViewModel couponDetailsViewModel;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = super.onCreateView( inflater,  container, savedInstanceState);

        name.setEnabled(false);
        description.setEnabled(false);
        price.setEnabled(false);
        priceAfterDiscount.setEnabled(false);
        datepicker.setEnabled(false);
        imageButton.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        cancel.setVisibility(view.INVISIBLE);
        progressBar.setVisibility(view.INVISIBLE);

        couponDetailsViewModel = new ViewModelProvider(this).get(CouponDetailsViewModel.class);



        final String couponId = CouponDetailsFragmentArgs.fromBundle(getArguments()).getCouponId();
        Log.d("couponId:",couponId);

        imageView = (ImageView) view.findViewById(R.id.imageView_add_coupon);

        couponDetailsViewModel.getCoupon(couponId, new model.GetCouponListener() {
            @Override
            public void onComplete(Coupon coupon) {
                name.setText(coupon.getTitle());
                description.setText(coupon.getDescription());
                price.setText(String.valueOf(coupon.getPrice()));
                priceAfterDiscount.setText(String.valueOf(coupon.getDiscountPrice()));
                datepicker.setText(coupon.getExpireDate());
                imageView.setImageResource(R.drawable.ic_launcher_background);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (coupon.getPostImg() != null) {
                        Picasso.get().load(coupon.getPostImg()).placeholder(R.drawable.ic_launcher_background).into(imageView);
                    }
                });
        }
        });

        return view;
    }

}