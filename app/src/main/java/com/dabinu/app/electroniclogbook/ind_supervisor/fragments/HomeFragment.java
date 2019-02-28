package com.dabinu.app.electroniclogbook.ind_supervisor.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.dabinu.app.electroniclogbook.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    RelativeLayout students, profile, about, signOut;
    ImageButton stud, person, abt, sout;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home_ind_supervisor, container, false);

        init(view);

        return view;
    }


    public void init(View view){
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getActivity());

        students = view.findViewById(R.id.students);
        stud = view.findViewById(R.id.stud);

        profile = view.findViewById(R.id.profile);
        person = view.findViewById(R.id.person);

        about = view.findViewById(R.id.about);
        abt = view.findViewById(R.id.abt);

        signOut = view.findViewById(R.id.signout);
        sout = view.findViewById(R.id.sout);


        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new com.dabinu.app.electroniclogbook.ind_supervisor.fragments.SearchSupervisorFragment());
                fragmentTransaction.commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new com.dabinu.app.electroniclogbook.ind_supervisor.fragments.ProfileFragment());
                fragmentTransaction.commit();
            }
        });


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, new com.dabinu.app.electroniclogbook.ind_supervisor.fragments.AboutFragment());
                        fragmentTransaction.commit();
            }
        });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(isNetworkAvailable(getActivity().getApplicationContext())){

                    progressDialog.setMessage("Signing out...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    mAuth.signOut();

                    new CountDownTimer(3000, 1000){
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("login", Constants.NOT_LOGGED_IN);
                            editor.putString("weeks", Constants.DEFAULT_FILL);
                            editor.putString("filled_placement", Constants.DEFAULT_FILL);
                            editor.putString("statuigba", "no_gba");
                            editor.apply();

                            progressDialog.cancel();
                            progressDialog.dismiss();

                            startActivity(new Intent(getActivity().getApplicationContext(), AuthActivity.class));
                        }
                    }.start();

                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                students.performClick();
            }
        });


        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.performClick();
            }
        });


        abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                about.performClick();
            }
        });


        sout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut.performClick();
            }
        });


    }


    private boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


}