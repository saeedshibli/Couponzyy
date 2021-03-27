package com.example.couponzy.ui.MyAccount;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.couponzy.Model.User;
import com.example.couponzy.Model.model;

public class MyAccountViewModel extends ViewModel {

    private MutableLiveData<String> currentUserId;
    private MutableLiveData<User> activeUser;

    public MyAccountViewModel() {
        currentUserId =  model.instance.getCurrentUserId();
        activeUser = model.instance.getUserById();
    }

    public MutableLiveData<String> getCurrentUserId() {
        return currentUserId;
    }

    public MutableLiveData<User> getActivetUser() {
        return activeUser;
    }

}