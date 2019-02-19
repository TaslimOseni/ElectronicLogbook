package com.dabinu.app.electroniclogbook.auth.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.dabinu.app.electroniclogbook.models.User;
import com.dabinu.app.electroniclogbook.student.StudentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupNextFragment extends Fragment implements AuthActivity.IOnBackPressed{

    ImageButton image;
    EditText password1, password2;
    CardView signup;
    boolean hasPicture = false;
    Uri uri;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    String photo_url;
    StorageReference storageReference2;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    String user_type;
    String stud_name, stud_email, stud_matric, stud_level, stud_faculty, stud_dept;
    String ind_name, ind_email, ind_phone, ind_rank, ind_staffID;
    String dept_name, dept_email, dept_faculty, dept_dept, dept_staffID;
    String final_email;
    User final_user;

    public SignupNextFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_next, container, false);

        sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);

        user_type = sharedPreferences.getString("type_of_user", "");

        stud_name = sharedPreferences.getString("stud_name", "");
        stud_email = sharedPreferences.getString("stud_email", "");
        stud_matric = sharedPreferences.getString("stud_matric", "");
        stud_level = sharedPreferences.getString("stud_level", "");
        stud_faculty = sharedPreferences.getString("stud_faculty", "");
        stud_dept = sharedPreferences.getString("stud_dept", "");


        ind_name = sharedPreferences.getString("ind_name", "");
        ind_email = sharedPreferences.getString("ind_email", "");
        ind_phone = sharedPreferences.getString("ind_phone", "");
        ind_rank = sharedPreferences.getString("ind_rank", "");
        ind_staffID = sharedPreferences.getString("ind_staffID", "");


        dept_name = sharedPreferences.getString("dept_name", "");
        dept_email = sharedPreferences.getString("dept_email", "");
        dept_faculty = sharedPreferences.getString("dept_faculty", "");
        dept_dept = sharedPreferences.getString("dept_dept", "");
        dept_staffID = sharedPreferences.getString("dept_staffID", "");


        switch(user_type){
            case "Student":
                final_email = stud_email;
                final_user = new User(user_type, stud_name, stud_email, stud_matric, stud_level, stud_faculty, stud_dept, "false", photo_url);
                break;
            case "Department supervisor":
                final_email = dept_email;
                final_user = new User(user_type, dept_name, dept_email, dept_faculty, dept_dept, dept_staffID, photo_url);
                break;
            case "Industrial supervisor":
                final_email = ind_email;
                final_user = new User(user_type, ind_name, ind_email, ind_phone, ind_rank, ind_staffID, "some", "extraSome", "extraTwo", photo_url);
                break;
        }


        storageReference2 = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getActivity());

        image = view.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        password1 = view.findViewById(R.id.password1);
        password2 = view.findViewById(R.id.password2);

        signup = view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!isNetworkAvailable(getActivity().getApplicationContext())){
                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!hasPicture){
                        Toast.makeText(getActivity().getApplicationContext(), "You must select a picture", Toast.LENGTH_SHORT).show();
                    }
                    else if((password1.getText().toString().trim().equals(password2.getText().toString().trim())) && password1.getText().toString().trim().length() > 5){
                        progressDialog.setMessage("Uploading photo...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        final StorageReference ref = storageReference2.child("photos").child(Long.toString(System.currentTimeMillis()));
                        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){

                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        photo_url = uri.toString();
                                    }
                                })

                                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task){

                                                if(task.isSuccessful()){
                                                    progressDialog.setMessage("Creating account...");
                                                    progressDialog.setCancelable(false);
                                                    progressDialog.show();

                                                    mAuth.createUserWithEmailAndPassword(final_email, password1.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if(task.isSuccessful()){
                                                                progressDialog.setMessage("Uploading data...");
                                                                databaseReference.child("users").child(mAuth.getUid()).setValue(final_user);
                                                                try{
                                                                    Thread.sleep(2000);
                                                                }
                                                                catch(Exception e){

                                                                }
                                                                progressDialog.cancel();
                                                                progressDialog.dismiss();

                                                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                                                editor.putString("login", "true");
                                                                editor.apply();

                                                                startActivity(new Intent(getActivity().getApplicationContext(), StudentActivity.class)); }

                                                            else{
                                                                progressDialog.cancel();
                                                                progressDialog.dismiss();
                                                                try {
                                                                    throw task.getException();
                                                                }
                                                                catch(FirebaseAuthUserCollisionException e){
                                                                    new AlertDialog.Builder(getActivity())
                                                                            .setMessage("Email already in use")
                                                                            .setCancelable(true)
                                                                            .setPositiveButton("Okay", null)
                                                                            .show();
                                                                }
                                                                catch(Exception e) {
                                                                    new AlertDialog.Builder(getActivity())
                                                                            .setMessage("Failed, try again")
                                                                            .setCancelable(true)
                                                                            .setPositiveButton("Okay", null)
                                                                            .show();
                                                                }
                                                            }
                                                        }
                                                    });

                                                }
                                                else{
                                                    progressDialog.dismiss();
                                                    progressDialog.cancel();
                                                    Toast.makeText(getActivity().getApplicationContext(), "Failed, try again", Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });
                            }
                        });
                    }


                    else if(password1.getText().toString().trim().length() > 0 && password1.getText().toString().trim().length() < 6){
                        Toast.makeText(getActivity().getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try{
            switch (requestCode){

                case 1:
                    if(resultCode == Activity.RESULT_OK){

                        hasPicture = true;

                        uri = data.getData();
                        bitmap = null;

                        try{
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                            image.setImageBitmap(bitmap);
                        }
                        catch (Exception e){
                            Toast.makeText(getActivity().getApplicationContext(), "Image type not supported", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                    else if(resultCode == Activity.RESULT_CANCELED){

                    }
                    break;
            }
        }
        catch (Exception e){

        }
    }


    private boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    @Override
    public boolean onBackPressed(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new SignUpInitialFragment());
        fragmentTransaction.commit();
        return true;
    }
}