package com.example.couponzy.ui.MyAccount;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.couponzy.Model.User;
import com.example.couponzy.Model.model;
import com.example.couponzy.R;
import com.example.couponzy.login_Register.Register_form;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class MyAccountFragment extends Fragment {

    private MyAccountViewModel myAccountViewModel;
    EditText txtemail, txtphone, txtfirstname, txtlastname, txtid, txtbirthday;
    RadioButton male, female;
    Button save, cancel;
    ProgressBar progressBar;
    ImageView imageView;
    ImageButton imageButton;
    boolean flagimg = false;
    DatePickerDialog picker;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myAccountViewModel = new ViewModelProvider(this).get(MyAccountViewModel.class);

        view = inflater.inflate(R.layout.fragment_myaccount, container, false);

        txtemail = view.findViewById(R.id.edit_form_email);
        txtfirstname = view.findViewById(R.id.edit_form_first_name);
        txtlastname = view.findViewById(R.id.edit_form_last_name);
        txtid = view.findViewById(R.id.edit_form_id);
        txtbirthday = view.findViewById(R.id.edit_form_Birthday);
        male = view.findViewById(R.id.radioButton_male_edit);
        female = view.findViewById(R.id.radioButton_female_edit);
        txtphone = view.findViewById(R.id.edit_form_phone);
        txtbirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtbirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        save = view.findViewById(R.id.edit_form_save_btn);
        cancel = view.findViewById(R.id.edit_form_cancel_btn);

        progressBar = view.findViewById(R.id.edit_form_progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        imageView = view.findViewById(R.id.imageView_user_edit);
        imageButton = view.findViewById(R.id.userEdit_imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editImage();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "cancel");
                Navigation.findNavController(view)
                        .popBackStack();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender = "";
                String email = txtemail.getText().toString().trim();
                String firstname = txtfirstname.getText().toString().trim();
                String lastname = txtlastname.getText().toString().trim();
                String id = txtid.getText().toString().trim();
                String birthday = txtbirthday.getText().toString().trim();
                String RadioMale = male.getText().toString().trim();
                String Radiofemale = female.getText().toString().trim();
                String phone = txtphone.getText().toString().trim();
                if (male.isChecked()) {
                    gender = "Male";
                } else {
                    gender = "Female";
                }

                if (TextUtils.isEmpty((email))) {
                    Toast.makeText(getActivity(), "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((firstname))) {
                    Toast.makeText(getActivity(), "Please Enter firstName", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((lastname))) {
                    Toast.makeText(getActivity(), "Please Enter lastName", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((id))) {
                    Toast.makeText(getActivity(), "Please Enter ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((birthday))) {
                    Toast.makeText(getActivity(), "Please Enter birthday", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((RadioMale)) && TextUtils.isEmpty((Radiofemale))) {
                    Toast.makeText(getActivity(), "Please Enter Gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((phone))) {
                    Toast.makeText(getActivity(), "Please Enter phone", Toast.LENGTH_SHORT).show();
                    return;
                }

                myAccountViewModel.getActivetUser().getValue().setEmail(email);
                myAccountViewModel.getActivetUser().getValue().setFirstname(firstname);
                myAccountViewModel.getActivetUser().getValue().setLastname(lastname);
                myAccountViewModel.getActivetUser().getValue().setId(id);
                myAccountViewModel.getActivetUser().getValue().setdateOfBirth(birthday);
                myAccountViewModel.getActivetUser().getValue().setGender(gender);
                myAccountViewModel.getActivetUser().getValue().setPhone(phone);

                Bitmap bitmap = null;

                if (flagimg == true) {
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    bitmap = drawable.getBitmap();
                    model.instance.uploadImage(bitmap, myAccountViewModel.getCurrentUserId().getValue(), new model.uploadImageListener() {
                        @Override
                        public void onComplete(String ImgUrl) {
                            if (ImgUrl == null) {
                                displayFailedError();
                            }
                            if (ImgUrl != null) {
                                myAccountViewModel.getActivetUser().getValue().setImgURL(ImgUrl);
                                onSave();
                            }
                        }

                    });
                } else {
                    onSave();
                }
            }
        });

        myAccountViewModel.getActivetUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {

                txtfirstname.setText(myAccountViewModel.getActivetUser().getValue().firstname);
                txtlastname.setText(myAccountViewModel.getActivetUser().getValue().lastname);
                txtphone.setText(myAccountViewModel.getActivetUser().getValue().phone);
                txtemail.setText(myAccountViewModel.getActivetUser().getValue().email);
                if (myAccountViewModel.getActivetUser().getValue().gender.equals("Male")) {
                    male.setChecked(true);
                } else {
                    female.setChecked(true);
                }
                txtid.setText(myAccountViewModel.getActivetUser().getValue().id);
                txtbirthday.setText(myAccountViewModel.getActivetUser().getValue().dateOfBirth);

                imageView.setImageResource(R.drawable.ic_baseline_person_24);
                if (myAccountViewModel.getActivetUser().getValue().getImgURL() != null) {
                    Picasso.get()
                            .load(user.getImgURL())
                            .fit()
                            .centerInside()
                            .into(imageView);
                }
            }
        });

        retrieveUser();

        return view;
    }

    void retrieveUser() {
        progressBar.setVisibility(View.VISIBLE);
        save.setEnabled(false);
        model.instance.retrieveUser(myAccountViewModel.getCurrentUserId().getValue(), new model.getUserByIdListener() {
            @Override
            public void onComplete(User result) {
                progressBar.setVisibility(View.INVISIBLE);
                save.setEnabled(true);
            }
        });
    }

    void onSave() {

        progressBar.setVisibility(View.VISIBLE);
        model.instance.saveUser(myAccountViewModel.getActivetUser().getValue(), new model.saveUserListener() {
            @Override
            public void onComplete() {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("TAG", "onSuccess Save user");
                Navigation.findNavController(view).popBackStack();
            }
        });
    }


    static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1;

    private void editImage() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
            }
        }
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
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
        imageView = view.findViewById(R.id.imageView_user_edit);
        if (resultCode != RESULT_CANCELED) {
            flagimg = true;
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        if (selectedImage == null) return;
                        imageView.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView = (ImageView) view.findViewById(R.id.imageView_user_edit);
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