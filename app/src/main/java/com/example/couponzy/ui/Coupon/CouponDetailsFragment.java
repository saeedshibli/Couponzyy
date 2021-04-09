package com.example.couponzy.ui.Coupon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;


public class CouponDetailsFragment extends AddCouponFragment {
    Coupon coupon;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = super.onCreateView( inflater,  container, savedInstanceState);

        name.setEnabled(false);
        description.setEnabled(false);
        price.setEnabled(false);
        priceAfterDiscount.setEnabled(false);
        imageButton.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        cancel.setVisibility(view.INVISIBLE);


        final String couponId = CouponDetailsFragmentArgs.fromBundle(getArguments()).getCouponId();
        Log.d("couponId:",couponId);

        model.instance.getPostsById(couponId, (co) ->  {
                coupon = co;
            name.setText(coupon.getUserName());
            description.setText(coupon.getDescription());
            price.setText(String.valueOf(coupon.getPrice()));
            priceAfterDiscount.setText(String.valueOf(coupon.getDiscountPrice()));


//                if (co.getPostImg() != null){
//                    Picasso.get().load(co.getPostImg()).placeholder(R.drawable.avatar).into(avatarImageView);
//                }

        });
        return view;
    }

}