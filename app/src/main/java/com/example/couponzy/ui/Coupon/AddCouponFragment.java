package com.example.couponzy.ui.Coupon;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.couponzy.Model.FireBaseAuth;
import com.example.couponzy.Model.FireDataBase;
import com.example.couponzy.Model.Coupon;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddCouponFragment extends Fragment {
    TextView datepicker;
    ImageView imageView;
    ImageButton imageButton;
    Button save, cancel;
    EditText name, description, price, priceAfterDiscount;
    String sdate;
    SimpleDateFormat sdf;
    Date resultdate;
    View view;
    boolean flagimg = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_coupon, container, false);
        //Constracting Values
        imageView = view.findViewById(R.id.imageView_post_image);
        imageButton = view.findViewById(R.id.addNewCoupon_imageButton);
        name = view.findViewById(R.id.editbox_new_coupon_name);
        description = view.findViewById(R.id.editbox_new_coupon_description);
        price = view.findViewById(R.id.editbox_new_coupon_price);
        priceAfterDiscount = view.findViewById(R.id.editbox_new_coupon_pricediscount);

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

        /*CurrentTime*/
        {
            long yourmilliseconds = System.currentTimeMillis();
            sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            resultdate = new Date(yourmilliseconds);
        }

        cancel = view.findViewById(R.id.button_new_coupon_cancel);
        save = view.findViewById(R.id.button_new_coupon_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .popBackStack(R.id.nav_home, false);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editImage();
            }
        });

        return view;
    }

    private void addPost() {
        final Coupon post = new Coupon();
        final int minc = 90000;
        final int maxc = 99999;
        final int minu = 900000000;
        final int maxu = 999999999;
        final int random = new Random().nextInt((maxc - minc) + 1) + minc;
        final int random2 = new Random().nextInt((maxu - minu) + 1) + minu;

        post.couponCode =Integer.toString(random);
        post.id=Integer.toString(random2);

        post.setTitle(name.getText().toString());
        post.setDescription(description.getText().toString());
        post.setPrice(Double.parseDouble(price.getText().toString()));
        post.setDiscountPrice(Double.parseDouble(priceAfterDiscount.getText().toString()));

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
        post.setExpireDate(sdate);
        post.setDistance("0 M");
        Bitmap bitmap = null;
        if (flagimg == true) {
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
        } else {
            Toast.makeText(getActivity(), "Please enter a photo", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void editImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your Post picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageView = view.findViewById(R.id.imageView_add_coupon);
        if (resultCode != RESULT_CANCELED) {
            flagimg = true;
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        imageView.setImageURI(selectedImage);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
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