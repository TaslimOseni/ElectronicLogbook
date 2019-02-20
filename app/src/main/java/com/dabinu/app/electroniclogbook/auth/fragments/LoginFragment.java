package com.dabinu.app.electroniclogbook.auth.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.dabinu.app.electroniclogbook.dept_supervisor.DeptSupervisorActivity;
import com.dabinu.app.electroniclogbook.ind_supervisor.IndSupervisorActivity;
import com.dabinu.app.electroniclogbook.models.User;
import com.dabinu.app.electroniclogbook.student.StudentActivity;
import com.dabinu.app.electroniclogbook.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements AuthActivity.IOnBackPressed{

    EditText email, password;
    TextView signup;
    CardView login;
    ImageButton back;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    public LoginFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        progressDialog = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new WelcomeFragment());
                fragmentTransaction.commit();
            }
        });

        signup = view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new SignUpAsFragment());
                fragmentTransaction.commit();
            }
        });

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);

        login = view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!isNetworkAvailable(getActivity().getApplicationContext())){
                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
                else if(email.getText().toString().trim().equals("")){
                    email.setError("This field is required");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("This field is required");
                }
                else{
                    progressDialog.setMessage("Logging in");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("login", Constants.LOGGED_IN);
                                editor.apply();

                                databaseReference.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                                        progressDialog.cancel();
                                        progressDialog.dismiss();

                                        User user = dataSnapshot.getValue(User.class);

                                        editor.putString("type", user.getType());
                                        editor.apply();

                                        switch(user.getType()){

                                            case Constants.STUDENT:
                                                startActivity(new Intent(getActivity().getApplicationContext(), StudentActivity.class));
                                                break;
                                            case Constants.DEPARTMENTAL_SUPERVISOR:
                                                startActivity(new Intent(getActivity().getApplicationContext(), DeptSupervisorActivity.class));
                                                break;
                                            case Constants.INDUSTRIAL_SUPERVISOR:
                                                startActivity(new Intent(getActivity().getApplicationContext(), IndSupervisorActivity.class));
                                                break;

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        progressDialog.cancel();
                                        progressDialog.dismiss();

                                        new AlertDialog.Builder(getActivity())
                                                .setMessage("Failed, try again")
                                                .setCancelable(true)
                                                .setPositiveButton("Okay", null)
                                                .show();
                                    }
                                });


                            }
                            else{
                                progressDialog.cancel();
                                progressDialog.dismiss();
                                try {
                                    throw task.getException();
                                }
                                catch(FirebaseAuthInvalidUserException e){
                                    new AlertDialog.Builder(getActivity())
                                            .setMessage("Invalid credentials")
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
            }
        });


        return view;
    }


    private boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }



    @Override
    public boolean onBackPressed(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new WelcomeFragment());
        fragmentTransaction.commit();
        return true;
    }

}