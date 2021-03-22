package com.example.couponzy.ui.CouponsLine;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;

import java.util.List;

public class CouponsLineViewModel extends ViewModel {

    private MutableLiveData<List<Coupon>> Coupons;

    public CouponsLineViewModel() {
        Coupons =  model.instance.getCoupons();
    }

    public MutableLiveData<List<Coupon>> getCoupons() {
        return Coupons;
    }
}