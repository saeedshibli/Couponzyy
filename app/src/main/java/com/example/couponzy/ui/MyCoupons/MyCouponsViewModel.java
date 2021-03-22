package com.example.couponzy.ui.MyCoupons;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;

import java.util.List;

public class MyCouponsViewModel extends ViewModel {

    private MutableLiveData<List<Coupon>> myCoupons;

    public MyCouponsViewModel() {
        myCoupons =  model.instance.getMyCoupons();
    }

    public MutableLiveData<List<Coupon>> getMyCoupons() {
        return myCoupons;
    }
}