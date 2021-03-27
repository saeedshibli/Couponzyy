package com.example.couponzy.ui.MyCoupons;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;
import java.util.List;

public class MyCouponsViewModel extends ViewModel {

    private LiveData<List<Coupon>> myCoupons;

    public MyCouponsViewModel() {
        myCoupons =  model.instance.getMyCoupons();
    }

    public LiveData<List<Coupon>> getMyCoupons() {
        return myCoupons;
    }
}