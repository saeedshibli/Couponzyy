package com.example.couponzy.Model;

import android.graphics.Bitmap;

import java.util.List;

public class model {
    public final static model instance = new model();

    private model(){ }


    FireBaseDB fireBaseDB=new FireBaseDB();


    public interface GetAllPostsListener{
        void onComplete(List<Coupon> data);
    }

    public void getAllPosts(final GetAllPostsListener listener) {
        fireBaseDB.getAllPosts(listener);

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

}
