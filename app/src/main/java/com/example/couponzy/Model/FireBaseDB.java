package com.example.couponzy.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.util.List;
import java.util.Map;

class CurrentDateTimeExample {
public static Date  GetCurrentDate(){
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();
    return date;
}

public static Date GetCurrentDateFromString(String date)  {
    Date date1= null;
    if(date==null ||date.isEmpty())
    {
        return GetCurrentDate();
    }
    try
    {
        date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
    }
    catch (ParseException e)
    {
        e.printStackTrace();
    }
    return date1;
}
}
    public class FireBaseDB {

    interface GetCouponsListener {
        void onComplete(List<Coupon> list);
    }

    public void getCoupons(Long lastUpdated, GetCouponsListener listener) {
        Query recentPostsQuery = FireDataBase.instance.getReference("Posts").orderByChild("lastUpdated").startAt(lastUpdated);
        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotg) {
                List<Coupon> data = new ArrayList<Coupon>();
                for (DataSnapshot snapshot : dataSnapshotg.getChildren()) {
                    if (!snapshot.getValue(Coupon.class).userId.equals(FireBaseAuth.instance.getCurrentUser().getUid&&
                            CurrentDateTimeExample.GetCurrentDateFromString(snapshot.getValue(Coupon.class).expireDate).after(CurrentDateTimeExample.GetCurrentDate())) {
                        Coupon post = new Coupon();
                        post.fromMap((Map<String, Object>) snapshot.getValue());
                        data.add(post);
                        Log.d("TAG", "getCoupons: " + post.getCouponCode());
                    }
                }
                listener.onComplete(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        });
    }


    interface getMyCouponsListener {
        void onComplete(List<Coupon> list);
    }

    public void getMyCoupons(Long lastUpdated, getMyCouponsListener listener) {
        Query recentPostsQuery = FireDataBase.instance.getReference("Posts").orderByChild("lastUpdated").startAt(lastUpdated);
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Coupon> data = new ArrayList<Coupon>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(Coupon.class).userId.equals(FireBaseAuth.instance.getCurrentUser().getUid())) {
                        Coupon post = new Coupon();
                        post.fromMap((Map<String, Object>) snapshot.getValue());
                        data.add(post);
                        Log.d("TAG", "getMyCoupons: " + post.getCouponCode());
                    }
                }
                listener.onComplete(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        });
    }

    public void getAllPosts(model.GetAllPostsListener listener) {
        FireDataBase.instance.getReference("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Coupon> data = new ArrayList<Coupon>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Coupon post = snapshot.getValue(Coupon.class);
                    data.add(post);
                }
                listener.onComplete(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        });
    }

    public void addPost(Coupon post, model.AddPostsListener listener) {
        FireDataBase.instance.getReference("Posts").child(post.getid())
                .setValue(post.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "post was added successfully");
                listener.onComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "post was not added successfully");
                listener.onComplete();
            }
        });
    }

    public void updatePost(Coupon student, model.AddPostsListener listener) {
        addPost(student, listener);
    }

    public void getPostById(String id, model.getPostByIdListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Posts").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Coupon post = null;
                if (task.isSuccessful()) {
                    post = task.getResult().toObject(Coupon.class);
                }
                listener.onComplete(post);
            }
        });
    }

    public void getUserById(String id, model.getUserByIdListener listener) {
        FireDataBase.instance.getReference("User").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    User user = task.getResult().getValue(User.class);
                    listener.onComplete(user);
                    //Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    public void getUserType(String id, model.getUserListener listener) {
        FireDataBase.instance.getReference("User").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Boolean type = task.getResult().getValue(User.class).isUser;
                    listener.onComplete(type);
                }
            }
        });
    }

    public void deletePostByID(String id, model.deletePostByIdListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Posts").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete();
            }

        });
    }

    public void uploadImage(Bitmap imageBmp, String name, final model.uploadImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }

    public void getImage(Bitmap imageBmp, String name, final model.uploadImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }
}