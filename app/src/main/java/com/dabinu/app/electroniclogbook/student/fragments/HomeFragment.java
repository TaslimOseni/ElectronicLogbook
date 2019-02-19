package com.dabinu.app.electroniclogbook.student.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.dabinu.app.electroniclogbook.exit.ExitActivity;
import com.dabinu.app.electroniclogbook.models.User;
import com.dabinu.app.electroniclogbook.student.StudentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment implements StudentActivity.IOnBackPressed{

    RelativeLayout fill_logbook, placement_details, supervisor, profile, about, signout;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());



        fill_logbook = view.findViewById(R.id.fill_logbook);
        fill_logbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkAvailable(getActivity().getApplicationContext())){
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    if(sharedPreferences.getString("filled_placement", "no").equals("yes")){
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, new FillLogbookFragment());
                        fragmentTransaction.commit();
                    }
                    else{
                        databaseReference.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener(){
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                if(user.getFilledPlacement().equals("false")){
                                    progressDialog.dismiss();
                                    progressDialog.cancel();

                                    new AlertDialog.Builder(getActivity())
                                            .setMessage("You need to fill your placement details first")
                                            .setPositiveButton("Fill details", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                    fragmentTransaction.replace(R.id.container, new PlacementDetailsFragment());
                                                    fragmentTransaction.commit();
                                                }
                                            })
                                            .setNegativeButton("Cancel", null)
                                            .setCancelable(true)
                                            .show();
                                }
                                else{
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("filled_placement", "yes");
                                    editor.apply();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });



        placement_details = view.findViewById(R.id.placement_details);
        placement_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new PlacementDetailsFragment());
                fragmentTransaction.commit();
            }
        });



        supervisor = view.findViewById(R.id.supervisor);
        supervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new SupervisorFragment());
                fragmentTransaction.commit();
            }
        });


        profile = view.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new ProfileFragment());
                fragmentTransaction.commit();
            }
        });


        about = view.findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new AboutFragment());
                fragmentTransaction.commit();
            }
        });


        signout = view.findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
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

                            editor.putString("login", "false");
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




        return view;
    }


    private boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    @Override
    public boolean onBackPressed(){
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to exit?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ExitActivity.exit(getActivity().getApplicationContext());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
        return true;
    }
}