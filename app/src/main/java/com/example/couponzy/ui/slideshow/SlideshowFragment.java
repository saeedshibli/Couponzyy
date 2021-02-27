package com.example.couponzy.ui.slideshow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.couponzy.Model.DownloadImageTask;
import com.example.couponzy.Model.FireBaseAuth;
import com.example.couponzy.Model.FireBaseDB;
import com.example.couponzy.Model.FireDataBase;
import com.example.couponzy.Model.User;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import com.example.couponzy.login_Register.Register_form;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    EditText txtemail,txtpassword,txtconfirmpassword,txtphone,txtfirstname,txtlastname,txtid,txtbirthday;
    RadioButton male,female;
    Button save,cancel;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    ImageView imageView;
    ImageButton imageButton;
    String imgPath;
    boolean flagimg=false;
    User user=null;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        view = inflater.inflate(R.layout.fragment_slideshow, container, false);
        txtemail=view.findViewById(R.id.edit_form_email);
        txtphone=view.findViewById(R.id.edit_form_phone);
        txtfirstname=view.findViewById(R.id.edit_form_first_name);
        txtlastname=view.findViewById(R.id.edit_form_last_name);
        txtid=view.findViewById(R.id.edit_form_id);
        txtbirthday=view.findViewById(R.id.edit_form_Birthday);
        male=view.findViewById(R.id.radioButton_male_edit);
        female=view.findViewById(R.id.radioButton_female_edit);
        save=view.findViewById(R.id.edit_form_save_btn);
        cancel=view.findViewById(R.id.edit_form_cancel_btn);
        progressBar=view.findViewById(R.id.edit_form_progressbar);
        imageView=(ImageView)view.findViewById(R.id.imageView_user_edit);
        imageButton=view.findViewById(R.id.userEdit_imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editImage();
            }
        });

        /*Reaplceing all data with the current values */
        String currentUserId= FireBaseAuth.instance.getCurrentUser().getUid();
        model.instance.getUserById(currentUserId, new model.getUserByIdListener() {
            @Override
            public void onComplete(User user1) {
                user=user1;
                String gender="";
                txtfirstname.setText(user.getFirstname());
                txtlastname.setText(user.getLastname());
                txtphone.setText(user.getPhone());
                txtemail.setText(user.getEmail());
                if(user.getGender().equals("Male")){
                    male.setChecked(true);
                }
                else{
                    female.setChecked(true);
                }
                txtid.setText(user.getId());
                txtbirthday.setText(user.getdateOfBirth());
                imageView.setImageResource(R.drawable.ic_baseline_person_24);
                if (user.getImgURL() != null){
                    //Picasso.get().load(user.getImgURL()).placeholder(R.drawable.ic_launcher_background).into(holder.userImage);
                    Picasso.get()
                            .load(user.getImgURL())
                            .resize(imageView.getWidth(),imageView.getHeight())
                            .into(imageView);
                }
            }
        });
    cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Navigation.findNavController(view)
                    .popBackStack(R.id.nav_home, false);
        }
    });
        /*Saving new details*/
    save.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String gender="";
            String level="";
            String email= txtemail.getText().toString().trim();
            String firstname=txtfirstname.getText().toString().trim();
            String lastname=txtlastname.getText().toString().trim();
            String id=txtid.getText().toString().trim();
            String birthday=txtbirthday.getText().toString().trim();
            String RadioMale=male.getText().toString().trim();
            String Radiofemale=female.getText().toString().trim();
            String Phone=txtphone.getText().toString().trim();
            String Imageurl;
            if(male.isChecked()){
                gender="Male";
            }
            else{
                gender="Female";
            }

            if(TextUtils.isEmpty((email))){
                Toast.makeText(getActivity(), "Please Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty((Phone))){
                Toast.makeText(getActivity(), "Please Enter phone", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty((firstname))){
                Toast.makeText(getActivity(), "Please Enter firstName", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty((lastname))){
                Toast.makeText(getActivity(), "Please Enter lastName", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty((id))){
                Toast.makeText(getActivity(), "Please Enter ID", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty((birthday))){
                Toast.makeText(getActivity(), "Please Enter birthday", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty((RadioMale))&&TextUtils.isEmpty((Radiofemale))){
                Toast.makeText(getActivity(), "Please Enter Gender", Toast.LENGTH_SHORT).show();
                return;
            }
            FireDataBase.instance.getReference("User").child(currentUserId).child("email").setValue(email);
            FireDataBase.instance.getReference("User").child(currentUserId).child("firstname").setValue(firstname);
            FireDataBase.instance.getReference("User").child(currentUserId).child("lastname").setValue(lastname);
            FireDataBase.instance.getReference("User").child(currentUserId).child("id").setValue(id);
            FireDataBase.instance.getReference("User").child(currentUserId).child("dateOfBirth").setValue(birthday);
            FireDataBase.instance.getReference("User").child(currentUserId).child("gender").setValue(gender);
            FireDataBase.instance.getReference("User").child(currentUserId).child("phone").setValue(Phone);
            //TODO: add image editing
           /* BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            if(bitmap !=null&&flagimg==true) {
                model.instance.uploadImage(bitmap, id + email, new model.uploadImageListener() {
                    @Override
                    public void onComplete(String ImgUrl) {
                        if (ImgUrl == null) {
                            displayFailedError();
                        }

                        imgPath = ImgUrl;
                    }

                });
                FireDataBase.instance.getReference("User").child(currentUserId).child("imgURL").setValue(imgPath);
            }*/
            //returing back to home
            Navigation.findNavController(view)
                    .popBackStack(R.id.nav_home, false);
        }
    });

        return view;
    }
    private void editImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    //static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageView=view.findViewById(R.id.imageView_add_coupon);
        if(resultCode != RESULT_CANCELED) {
            flagimg=true;
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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