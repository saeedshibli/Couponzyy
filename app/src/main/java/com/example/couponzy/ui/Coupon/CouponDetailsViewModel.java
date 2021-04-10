package com.example.couponzy.ui.Coupon;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;

import java.util.List;

public class CouponDetailsViewModel extends ViewModel{


    private Coupon coupon;

//    public CouponDetailsViewModel() {
//    }

    public void getCoupon(String id, model.GetCouponListener listener) {
        model.instance.getCoupon(id, listener);
       // return coupon;
    }
}