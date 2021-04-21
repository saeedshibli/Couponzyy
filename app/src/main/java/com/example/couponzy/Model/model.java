package com.example.couponzy.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.couponzy.MyApplication;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class model {

    public final static model instance = new model();
    FireBaseDB fireBaseDB = new FireBaseDB();

    private model() {
    }

    public interface Listener<T> {
        void onComplete(T result);
    }

    LiveData<List<Coupon>> CouponsList;

    public LiveData<List<Coupon>> getCoupons() {
        if (CouponsList == null) {
            String userId = FireBaseAuth.instance.getCurrentUser().getUid();
            CouponsList = CouponzyLocalDB.db.couponDao().getCoupons(userId);
            refreshCoupons(null);
        }
        return CouponsList;
    }

    public interface GetCouponsListener {
        void onComplete();
    }

////////////////////////////////////////////////////////////////////////////////////////

    Coupon coupon;

    public void getCoupon(String id, GetCouponListener listener) {
       // if (coupon == null) {
           // String userId = FireBaseAuth.instance.getCurrentUser().getUid();
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                coupon = CouponzyLocalDB.db.couponDao().getCoupon(id);
                listener.onComplete(coupon);
            });

           // refreshCoupons(null);
       // }
    }

    public interface GetCouponListener {
        void onComplete(Coupon coupon);
    }




///////////////////////////////////////////////////////////////////////////////////////////



    public void refreshCoupons(final GetCouponsListener listener) {
        //1. get local last update date
        final SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated", 0);
        //2. get all updated record from firebase from the last update date
        fireBaseDB.getCoupons(lastUpdated, new FireBaseDB.GetCouponsListener() {
            @Override
            public void onComplete(List<Coupon> result) {
                //3. insert the new updates to the local db
                long lastU = 0;
                for (Coupon c : result) {
                    Executor myExecutor = Executors.newSingleThreadExecutor();
                    myExecutor.execute(() -> {
                        CouponzyLocalDB.db.couponDao().insertAll(c);
                    });
                    if (c.getLastUpdated() > lastU) {
                        lastU = c.getLastUpdated();
                    }
                }
                //4. update the local last update date
                sp.edit().putLong("lastUpdated", lastU).commit();
                //5. return the updates data to the listeners
                if (listener != null) {
                    Log.d("TAG", "listener");
                    listener.onComplete();
                }
            }
        });
    }

    LiveData<List<Coupon>> myCouponsList;

    public LiveData<List<Coupon>> getMyCoupons() {
        if (myCouponsList == null) {
            String userId = FireBaseAuth.instance.getCurrentUser().getUid();
            myCouponsList = CouponzyLocalDB.db.couponDao().getMyCoupons(userId);
            refreshMyCoupons(null);
        }
        return myCouponsList;
    }

    public interface getMyCouponsListener {
        void onComplete();
    }

    public void refreshMyCoupons(final getMyCouponsListener listener) {
        //1. get local last update date
        final SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated", 0);
        //2. get all updated record from firebase from the last update date
        fireBaseDB.getMyCoupons(lastUpdated, new FireBaseDB.getMyCouponsListener() {
            @Override
            public void onComplete(List<Coupon> result) {
                //3. insert the new updates to the local db
                long lastU = 0;
                for (Coupon c : result) {
                    Executor myExecutor = Executors.newSingleThreadExecutor();
                    myExecutor.execute(() -> {
                        CouponzyLocalDB.db.couponDao().insertAll(c);
                    });
                    if (c.getLastUpdated() > lastU) {
                        lastU = c.getLastUpdated();
                    }
                }
                //4. update the local last update date
                sp.edit().putLong("lastUpdated", lastU).commit();
                //5. return the updates data to the listeners
                if (listener != null) {
                    listener.onComplete();
                }
            }
        });
    }

    public interface GetAllPostsListener {
        void onComplete(List<Coupon> data);
    }

    public void getAllPosts(final GetAllPostsListener listener) {
        fireBaseDB.getAllPosts(listener);
    }

    public interface getUserByIdListener extends Listener<User> {
    }

    List<User> SellersList;

    public List<User> getSellers() {
        if (SellersList == null) {
            getAllSellers(null);
            SellersList = CouponzyLocalDB.db.userDao().getAllSellers();
        }
        return SellersList;
    }

    public interface getSellersListener{
        void onComplete();

    }

    public void getAllSellers(final getSellersListener listener){
        //1. get local last update date
        final SharedPreferences sp = MyApplication.context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated", 0);
        Log.d("TAG", "lastUpdated = " + lastUpdated);
        //2. get all updated record from firebase from the last update date
        fireBaseDB.getSellers(lastUpdated, new FireBaseDB.getSellersListener(){
            @Override
            public void onComplete(List<User> result) {
                for (User user:result) {
                    Log.d("TAG", "ResultUserId: " + user.id);
                }
                //3. insert the new updates to the local db
                long lastU = 0;
                for (User u : result) {
                    Executor myExecutor = Executors.newSingleThreadExecutor();
                    myExecutor.execute(() -> {
                        CouponzyLocalDB.db.userDao().insertAll(u);
                    });
                    if (u.getLastUpdated() > lastU) {
                        lastU = u.getLastUpdated();
                    }
                }
                //4. update the local last update date
                sp.edit().putLong("lastUpdated", lastU).apply();
                //5. return the updates data to the listeners
                if (listener != null) {
                    listener.onComplete();
                }
            }
        });
    }

    MutableLiveData<User> currentUser = new MutableLiveData<User>();

    public MutableLiveData<User> getUserById() {
        return currentUser;
    }

    public void retrieveUser(String id, Listener listener) {
        fireBaseDB.getUserById(id, new getUserByIdListener() {
            @Override
            public void onComplete(User result) {
                currentUser.setValue(result);
                listener.onComplete(null);
            }
        });
    }

    public interface AddPostsListener {
        void onComplete();
    }

    public void addPost(final Coupon post, final AddPostsListener listener) {
        fireBaseDB.updatePost(post, new AddPostsListener() {
            @Override
            public void onComplete() {
                refreshMyCoupons(new getMyCouponsListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
            }
        });
    }

    public interface GetPostByIdListener {
        void onComplete(Coupon Post);
    }

    public void getPostsById(String id, GetPostByIdListener listener) {
        fireBaseDB.getPostById(id, listener);

    }

    public interface deletePostByIdListener {
        void onComplete();
    }

    public void deletePostById(String id, deletePostByIdListener listener) {
        fireBaseDB.deletePostByID(id, listener);
    }

    public interface uploadImageListener {
        void onComplete(String ImgUrl);
    }

    public void uploadImage(Bitmap imageBmp, String name, final uploadImageListener listener) {
        fireBaseDB.uploadImage(imageBmp, name, listener);
    }

    public interface getUserListener {
        void onComplete(Boolean Type);
    }

    public void getUserType(String id, model.getUserListener listener) {
        fireBaseDB.getUserType(id, listener);
    }

    MutableLiveData<String> currentUserId = new MutableLiveData<String>();

    public MutableLiveData<String> getCurrentUserId() {
        String id = FireBaseAuth.instance.getCurrentUser().getUid();
        currentUserId.setValue(id);
        return currentUserId;
    }
}