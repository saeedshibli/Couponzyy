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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FireBaseDB {

    public void getCoupons(model.GetCouponsListener listener) {
        FireDataBase.instance.getReference("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Coupon> data = new ArrayList<Coupon>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if(!snapshot.getValue(Coupon.class).userId.equals(FireBaseAuth.instance.getCurrentUser().getUid())) {
                        Coupon post = snapshot.getValue(Coupon.class);
                        data.add(post);
                    }
                }
                listener.onComplete(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getMyCoupons(model.getMyCouponsListener listener) {
        FireDataBase.instance.getReference("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Coupon> data = new ArrayList<Coupon>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if(snapshot.getValue(Coupon.class).userId.equals(FireBaseAuth.instance.getCurrentUser().getUid())) {
                        Coupon post = snapshot.getValue(Coupon.class);
                        data.add(post);
                    }
                }
                listener.onComplete(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getAllPosts(model.GetAllPostsListener listener) {

        FireDataBase.instance.getReference("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Coupon> data = new ArrayList<Coupon>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Coupon post = snapshot.getValue(Coupon.class);
                    data.add(post);
                }
                listener.onComplete(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // This type of listener is not one time, and you need to cancel it to stop
        // receiving updates.

    }

               /* addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<HomeData> data = new ArrayList<HomeData>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                HomeData post = doc.toObject(HomeData.class);
                                data.add(post);
                            }
                        }
                        listener.onComplete(data);
                    }
                });*/


    public void addPost(Coupon post, model.AddPostsListener listener) {

        FireDataBase.instance.getReference("Posts").child(post.getid())
                .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("TAG","post was added successfully");
                listener.onComplete();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","post was not added successfully");
                listener.onComplete();
            }
        });
    }

    public void getPostById(String id, model.getPostByIdListener listener) {

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("Posts").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Coupon post=null;
                if(task.isSuccessful()){
                    post =task.getResult().toObject(Coupon.class);
                }
                listener.onComplete(post);


            }
        });
    }
    public void getUserById(String id, model.getUserByIdListener listener) {
          FireDataBase.instance.getReference("User").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
          {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        User user=task.getResult().getValue(User.class);
                        listener.onComplete(user);
                        //Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    }
                }
            });
    }

    public void deletePostByID(String id, model.deletePostByIdListener listener) {
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("Posts").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete();
            }

        });
    }

    public void updatePost(Coupon student, model.AddPostsListener listener) {
        addPost(student,listener);
    }

    public  void uploadImage(Bitmap imageBmp, String name, final model.uploadImageListener listener){
        FirebaseStorage storage=FirebaseStorage.getInstance();
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
    public  void getImage(Bitmap imageBmp, String name, final model.uploadImageListener listener){
        FirebaseStorage storage=FirebaseStorage.getInstance();
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
