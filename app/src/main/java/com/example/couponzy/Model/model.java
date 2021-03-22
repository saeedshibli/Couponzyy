package com.example.couponzy.Model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.List;

public class model {
    public final static model instance = new model();

    private model(){ }


    FireBaseDB fireBaseDB=new FireBaseDB();


    public interface GetAllPostsListener{
        void onComplete(List<Coupon> data);
    }

    public interface Listener<T>{
        void onComplete(T result);
    }

    public void getAllPosts(final GetAllPostsListener listener) {
        fireBaseDB.getAllPosts(listener);

    }
    public interface getUserByIdListener{
        void onComplete(User user);
    }
    public void getUserById(String id, model.getUserByIdListener listener){
        fireBaseDB.getUserById(id,listener);
    }

    public interface GetCouponsListener extends Listener<List<Coupon>>{}
    MutableLiveData<List<Coupon>> CouponsList = new MutableLiveData<List<Coupon>>();
    public MutableLiveData<List<Coupon>> getCoupons() {
        return CouponsList;
    }

    public void refreshCoupons(Listener listener){
        fireBaseDB.getCoupons(new GetCouponsListener() {
            @Override
            public void onComplete(List<Coupon> result) {
                CouponsList.setValue(result);
                listener.onComplete(null);
            }
        });
    }

    public interface getMyCouponsListener extends Listener<List<Coupon>>{}
    MutableLiveData<List<Coupon>> myCouponsList = new MutableLiveData<List<Coupon>>();
    public MutableLiveData<List<Coupon>> getMyCoupons() {
        return myCouponsList;
    }

    public void refreshMyCoupons(Listener listener){
        fireBaseDB.getMyCoupons(new getMyCouponsListener() {
            @Override
            public void onComplete(List<Coupon> result) {
                myCouponsList.setValue(result);
                listener.onComplete(null);
            }
        });
    }

    public interface AddPostsListener{
        void onComplete();
    }
    public void addPost(Coupon post, AddPostsListener listener){
        fireBaseDB.addPost(post,listener);


    }

    public interface getPostByIdListener{
        void onComplete(Coupon Post);
    }
    public void getPostsById(String id,getPostByIdListener listener){
        fireBaseDB.getPostById(id,listener);

    }

    public interface deletePostByIdListener{
        void onComplete();
    }
    public void deletePostById(String id,deletePostByIdListener listener){
        fireBaseDB.deletePostByID(id,listener);

    }
    public interface uploadImageListener{
        void onComplete(String ImgUrl);
    }
    public  void uploadImage(Bitmap imageBmp, String name, final uploadImageListener listener){
        fireBaseDB.uploadImage(imageBmp,name,listener);
    }
    public interface getUserListener{
        void onComplete(Boolean Type);
    }
    public void getUserType(String id, model.getUserListener listener) {
        fireBaseDB.getUserType(id,listener);
    }

}
