package com.example.couponzy.login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.couponzy.MainActivity;
import com.example.couponzy.Model.FireBaseAuth;
import com.example.couponzy.Model.FireDataBase;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import com.example.couponzy.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class Register_form extends AppCompatActivity {

    EditText txtemail, txtpassword, txtconfirmpassword, txtphone, txtfirstname, txtlastname, txtid, txtbirthday, et_lat, et_long;
    RadioButton male, female, user, shop;
    Button Register;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    ImageView imageView;
    ImageButton imageButton;
    String imgPath;
    Double latNumber, lonNumber;
    boolean flagimg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        txtemail = findViewById(R.id.register_form_email);
        txtpassword = findViewById(R.id.register_form_password);
        txtconfirmpassword = findViewById(R.id.register_form_password_confirm);
        txtphone = findViewById(R.id.register_form_phone);
        txtfirstname = findViewById(R.id.register_form_first_name);
        txtlastname = findViewById(R.id.register_form_last_name);
        txtid = findViewById(R.id.register_form_id);
        txtbirthday = findViewById(R.id.register_form_Birthday);
        male = findViewById(R.id.radioButton_male);
        female = findViewById(R.id.radioButton_female);
        user = findViewById(R.id.radioButton_User);
        shop = findViewById(R.id.radioButton_shop);
        Register = findViewById(R.id.register_form_Register_btn);
        progressBar = findViewById(R.id.register_form_progressbar);
        imageView = (ImageView) findViewById(R.id.imageView_user_register);
        imageButton = (ImageButton) findViewById(R.id.userRigster_imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editImage();
            }
        });
        databaseReference = FireDataBase.instance.getReference("Users");

        et_lat = findViewById(R.id.lat_user);
        et_long = findViewById(R.id.long_user);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender = "";
                String level = "";
                String email = txtemail.getText().toString().trim();
                String password = txtpassword.getText().toString().trim();
                String passwordconfirm = txtconfirmpassword.getText().toString().trim();
                String firstname = txtfirstname.getText().toString().trim();
                String lastname = txtlastname.getText().toString().trim();
                String id = txtid.getText().toString().trim();
                String birthday = txtbirthday.getText().toString().trim();
                String RadioMale = male.getText().toString().trim();
                String Radiofemale = female.getText().toString().trim();
                String RadioUser = user.getText().toString().trim();
                String RadioShop = shop.getText().toString().trim();
                String Phone = txtphone.getText().toString().trim();
                String lat = et_lat.getText().toString().trim();
                String lon = et_long.getText().toString().trim();

                latNumber = Double.parseDouble(lat);
                lonNumber = Double.parseDouble(lon);
                Log.d("TAG", "latNumber = " + latNumber + " | " + "lonNumber = " + lonNumber);

                String Imageurl;
                if (male.isChecked()) {
                    gender = "Male";
                } else {
                    gender = "Female";
                }
                if (user.isChecked()) {
                    level = "User";
                } else {
                    level = "Shop";
                }

                if (TextUtils.isEmpty((email))) {
                    Toast.makeText(Register_form.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((Phone))) {
                    Toast.makeText(Register_form.this, "Please Enter phone", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((password))) {
                    Toast.makeText(Register_form.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((passwordconfirm))) {
                    Toast.makeText(Register_form.this, "Please Enter confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((firstname))) {
                    Toast.makeText(Register_form.this, "Please Enter firstName", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((lastname))) {
                    Toast.makeText(Register_form.this, "Please Enter lastName", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((id))) {
                    Toast.makeText(Register_form.this, "Please Enter ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((birthday))) {
                    Toast.makeText(Register_form.this, "Please Enter birthday", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((lat))) {
                    Toast.makeText(Register_form.this, "Please Enter birthday", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((lon))) {
                    Toast.makeText(Register_form.this, "Please Enter birthday", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((RadioMale)) && TextUtils.isEmpty((Radiofemale))) {
                    Toast.makeText(Register_form.this, "Please Enter Gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((RadioUser)) && TextUtils.isEmpty((RadioShop))) {
                    Toast.makeText(Register_form.this, "Please Enter Gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bitmap bitmap = null;
                if (imageView.getDrawable() != null) {
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    bitmap = drawable.getBitmap();
                    flagimg = true;

                }
                if (flagimg == false) {
                    Toast.makeText(Register_form.this, "Please enter a photo", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                if (password.equals((passwordconfirm))) {

                    String finalGender = gender;
                    Bitmap finalBitmap = bitmap;
                    String finalLevel = level;
                    FireBaseAuth.instance.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Register_form.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        User information;
                                        if (finalBitmap != null) {
                                            GetImagePath(finalBitmap, id, email);
                                        }
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        if (finalLevel.equals("User")) {
                                            latNumber = 0.0;
                                            lonNumber = 0.0;
                                            information = new User(email, firstname, lastname, id, birthday, finalGender, Phone, imgPath, latNumber, lonNumber, false, false, true);
                                        } else {
                                            information = new User(email, firstname, lastname, id, birthday, finalGender, Phone, imgPath, latNumber, lonNumber, false, true, false);
                                        }


                                        FireDataBase.instance.getReference("User").child(FireBaseAuth.instance.getCurrentUser().getUid())
                                                .setValue(information.toMap())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        while (imgPath == null) ;
                                                        FireDataBase.instance.getReference("User").child(FireBaseAuth.instance.getCurrentUser().getUid())
                                                                .child("imgURL")
                                                                .setValue(imgPath.toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        FirebaseAuth.getInstance().signOut();
                                                                        Toast.makeText(Register_form.this, "Registeration Completed", Toast.LENGTH_SHORT).show();
                                                                        startActivity(new Intent(getApplicationContext(), Login_form.class));
                                                                    }


                                                                });
                                                    }
                                                });


                                    } else {
                                        Toast.makeText(Register_form.this, "Authontication failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register_form.this, "Please enter same Password Twice", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    private void editImage() {
        ActivityCompat.requestPermissions(Register_form.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Register_form.this);
        builder.setTitle("Choose your profile picture");
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

    private String GetImagePath(Bitmap bitmap, String id, String email) {
        final String[] ret = new String[1];
        model.instance.uploadImage(bitmap, id + email, new model.uploadImageListener() {
            @Override
            public void onComplete(String ImgUrl) {
                if (ImgUrl == null) {
                    displayFailedError();
                }

                imgPath = ImgUrl;
                ret[0] = ImgUrl;
            }

        });
        return ret[0];
    }
    //static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageView = findViewById(R.id.imageView_user_register);
        if (resultCode != RESULT_CANCELED) {
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
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = Register_form.this.getContentResolver().query(selectedImage,
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do something you want
                } else {
                    // permission denied
                    Toast.makeText(Register_form.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Register_form.this);
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