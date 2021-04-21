package com.example.couponzy.ui.Coupon;

import androidx.lifecycle.ViewModel;

import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;
import com.google.common.io.CountingOutputStream;

public class EditCouponViewModel extends ViewModel {
    private Coupon coupon;

//    public CouponDetailsViewModel() {
//    }
    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void getCoupon(String id, model.GetCouponListener listener) {
        model.instance.getCoupon(id, listener);
        // return coupon;
    }

}
