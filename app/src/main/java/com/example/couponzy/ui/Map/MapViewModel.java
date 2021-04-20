package com.example.couponzy.ui.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.couponzy.Model.User;
import com.example.couponzy.Model.model;

import java.util.List;

public class MapViewModel extends ViewModel {
    private List<User> sellers;

    public MapViewModel(){
        sellers = model.instance.getSellers();
    }

    public List<User> getSellers(){return this.sellers;}
}
