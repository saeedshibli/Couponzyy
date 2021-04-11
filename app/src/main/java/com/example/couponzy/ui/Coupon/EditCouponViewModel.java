package com.example.couponzy.ui.Coupon;

import androidx.lifecycle.ViewModel;

import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;

public class EditCouponViewModel extends ViewModel {
    private Coupon coupon;

//    public CouponDetailsViewModel() {
//    }

    public void getCoupon(String id, model.GetCouponListener listener) {
        model.instance.getCoupon(id, listener);
        // return coupon;
    }

}
