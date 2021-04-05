package com.example.couponzy.ui.CouponsLine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;
import java.util.List;

public class CouponsLineViewModel extends ViewModel {

    private LiveData<List<Coupon>> Coupons;

    public CouponsLineViewModel() {
        Coupons =  model.instance.getCoupons();
    }

    public LiveData<List<Coupon>> getCoupons() {
        return Coupons;
    }
}