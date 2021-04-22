package com.example.couponzy.ui.Coupon;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.couponzy.MainActivity;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.FireBaseAuth;
import com.example.couponzy.Model.FireDataBase;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import com.example.couponzy.ui.Coupon.AddCouponFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Random;


public class EditCouponFragment extends AddCouponFragment {
    Coupon coupon;
    TextView datepicker;
    String sdate;

    private EditCouponViewModel editCouponViewModel;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView( inflater,  container, savedInstanceState);
        editCouponViewModel = new ViewModelProvider(this).get(EditCouponViewModel.class);



        /*DatePicker*/
        {
            datepicker = view.findViewById(R.id.add_coupon_datepicker);
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            datepicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker2, int year, int month, int dayOfMonth) {
                            month = month + 1;
                            sdate = dayOfMonth + "/" + month + "/" + year;
                            datepicker.setText(sdate);
                        }
                    }, year, month, day
                    );
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                }
            });
        }



        save = view.findViewById(R.id.button_new_coupon_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPost(false);
            }
        });
        delete = view.findViewById(R.id.button_coupon_delete);
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPost(true);
                //deletePost();
            }
        });

        final String couponId = CouponDetailsFragmentArgs.fromBundle(getArguments()).getCouponId();
        Log.d("couponId:",couponId);

        imageView = (ImageView) view.findViewById(R.id.imageView_add_coupon);

        editCouponViewModel.getCoupon(couponId, new model.GetCouponListener() {
            @Override
            public void onComplete(Coupon coupon) {
                editCouponViewModel.setCoupon(coupon);
                name.setText(coupon.getTitle());
                description.setText(coupon.getDescription());
                price.setText(String.valueOf(coupon.getPrice()));
                priceAfterDiscount.setText(String.valueOf(coupon.getDiscountPrice()));
                datepicker.setText(coupon.getExpireDate());
                sdate = coupon.getExpireDate();
                imageView.setImageResource(R.drawable.ic_launcher_background);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (coupon.getPostImg() != null) {
                        Picasso.get().load(coupon.getPostImg()).placeholder(R.drawable.ic_launcher_background).into(imageView);
                    }
                });
            }
        });


        return view;
    }
    private void editPost(boolean ex) {
        final Coupon post = new Coupon();
        final int minc = 90000;
        final int maxc = 99999;
        final int minu = 900000000;
        final int maxu = 999999999;
        final int random = new Random().nextInt((maxc - minc) + 1) + minc;
        final int random2 = new Random().nextInt((maxu - minu) + 1) + minu;
        post.id= CouponDetailsFragmentArgs.fromBundle(getArguments()).getCouponId();
        if (TextUtils.isEmpty((priceAfterDiscount.getText().toString()))) {
            Toast.makeText(getActivity(), "Please Enter Price After Discount", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(price.getText().toString())) {
            Toast.makeText(getActivity(), "Please Enter a Price", Toast.LENGTH_SHORT).show();
            return;
        }
        post.couponCode = editCouponViewModel.getCoupon().couponCode;
        post.setTitle(name.getText().toString());
        post.setDescription(description.getText().toString());
        post.setPrice(Double.parseDouble(price.getText().toString()));
        post.setDiscountPrice(Double.parseDouble(priceAfterDiscount.getText().toString()));
        if(post.price<=post.discountPrice){
            Toast.makeText(getActivity(), "Discount Price Must Be Lower that Normal Price", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Double.parseDouble(price.getText().toString())<Double.parseDouble(priceAfterDiscount.getText().toString())){
            Toast.makeText(getActivity(), "Please Insert PriceAfterDiscount bigger than Normal Price", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Double.parseDouble(price.getText().toString())<Double.parseDouble(priceAfterDiscount.getText().toString())){
            Toast.makeText(getActivity(), "Please Insert PriceAfterDiscount bigger than Normal Price", Toast.LENGTH_SHORT).show();
            return;
        }

        FireDataBase.instance.getReference("User").child(FireBaseAuth.instance.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Firstname = snapshot.child("firstname").getValue().toString();
                String Lastname = snapshot.child("lastname").getValue().toString();
                post.setUserName(Firstname + Lastname);
                String ImgUrl = snapshot.child("imgURL").getValue().toString();
                post.setProfileImg(ImgUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        post.setUserId(FireBaseAuth.instance.getUid());
        post.setTimestamp(sdf.format(resultdate).toString());
        if(ex==true){
            post.setExpireDate("EXPIRED");
        }
        else {
            post.setExpireDate(sdate);
        }
        post.setDistance("0 M");
        if (TextUtils.isEmpty((post.title))) {
            Toast.makeText(getActivity(), "Please Enter a Coupon Title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty((post.description))) {
            Toast.makeText(getActivity(), "Please Enter a Description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty((post.expireDate))) {
            Toast.makeText(getActivity(), "Please Enter an Expire Date", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap = null;
        if(imageView.getDrawable()!=null) {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            bitmap = drawable.getBitmap();

            model.instance.uploadImage(bitmap, post.getid(), new model.uploadImageListener() {
                @Override
                public void onComplete(String ImgUrl) {
                    if (ImgUrl == null) {
                        displayFailedError();
                    }
                    post.setPostImg(ImgUrl);
                    model.instance.addPost(post, new model.AddPostsListener() {
                        @Override
                        public void onComplete() {
                            Navigation.findNavController(view)
                                    .popBackStack(R.id.nav_home, false);
                        }
                    });
                }
            });
        }

    }

    private void deletePost() {
        final Coupon post = new Coupon();

        sdate = "EXPIRED";
        post.id= CouponDetailsFragmentArgs.fromBundle(getArguments()).getCouponId();
        post.couponCode = editCouponViewModel.getCoupon().couponCode;
        post.title = editCouponViewModel.getCoupon().title;
        post.description = editCouponViewModel.getCoupon().description;
        post.price = editCouponViewModel.getCoupon().price;
        post.discountPrice = editCouponViewModel.getCoupon().discountPrice;
        post.setUserId(FireBaseAuth.instance.getUid());
        post.setTimestamp(sdf.format(resultdate).toString());
        post.setExpireDate(sdate);
        post.distance = editCouponViewModel.getCoupon().distance;
        post.setLastUpdated(editCouponViewModel.getCoupon().getLastUpdated()+1);
        Bitmap bitmap = null;
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();

        model.instance.uploadImage(bitmap, post.getid(), new model.uploadImageListener() {
            @Override
            public void onComplete(String ImgUrl) {
                if (ImgUrl == null) {
                    displayFailedError();
                }
                post.setPostImg(ImgUrl);
                model.instance.addPost(post, new model.AddPostsListener() {
                    @Override
                    public void onComplete() {
                        Navigation.findNavController(view)
                                .popBackStack(R.id.nav_home, false);
                    }
                });
            }
        });

    }



    private void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Operation Failed");
        builder.setMessage("Saving image failed, please try again later...");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


}