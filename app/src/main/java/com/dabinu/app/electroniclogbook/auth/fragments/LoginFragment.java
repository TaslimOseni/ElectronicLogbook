package com.dabinu.app.electroniclogbook.auth.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.dabinu.app.electroniclogbook.landing.LandingActivity;
import com.dabinu.app.electroniclogbook.splash.fragments.WelcomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText email, password;
    TextView signup;
    CardView login;
    ImageButton back;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        progressDialog = new ProgressDialog(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();

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
                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet comnnection", Toast.LENGTH_SHORT).show();
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

                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.cancel();
                                progressDialog.dismiss();
                                startActivity(new Intent(getActivity().getApplicationContext(), LandingActivity.class));
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

}