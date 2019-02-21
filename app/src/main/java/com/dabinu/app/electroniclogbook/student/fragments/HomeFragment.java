package com.dabinu.app.electroniclogbook.student.fragments;


import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.dabinu.app.electroniclogbook.exit.ExitActivity;
import com.dabinu.app.electroniclogbook.models.PlacementObject;
import com.dabinu.app.electroniclogbook.models.User;
import com.dabinu.app.electroniclogbook.student.StudentActivity;
import com.dabinu.app.electroniclogbook.utils.Constants;
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
    ImageButton pen, house, eye, person, abt, sout;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    Activity activity;

    public HomeFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_student, container, false);

        sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());

        activity = getActivity();



        fill_logbook = view.findViewById(R.id.fill_logbook);
        fill_logbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable(getActivity().getApplicationContext())){
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    if(sharedPreferences.getString("filled_placement", Constants.DEFAULT_FILL).equals(Constants.FILLED)){
                        progressDialog.cancel();
                        progressDialog.dismiss();

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, new FillLogbookFragment());
                        fragmentTransaction.commit();
                    }

                    else if(sharedPreferences.getString("filled_placement", Constants.DEFAULT_FILL).equals(Constants.NOT_FILLED)){

                        progressDialog.cancel();
                        progressDialog.dismiss();

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
                                .setCancelable(false)
                                .show();
                    }

                    else{
                        databaseReference.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener(){
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                User user = dataSnapshot.getValue(User.class);
                                Log.d("DEBUG_ATTACK", user.getFilledPlacement());

                                if(user.getFilledPlacement().equals("false")){
                                    progressDialog.dismiss();
                                    progressDialog.cancel();

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("filled_placement", Constants.NOT_FILLED);
                                    editor.apply();

                                    new AlertDialog.Builder(getActivity())
                                            .setMessage("You need to fill your placement details first")
                                            .setPositiveButton("Fill details", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("statuigba", "gba");
                                                    editor.apply();

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
                                    if(!(sharedPreferences.getString("statuigba", "no_gba").equals("gba"))){
                                        databaseReference.child("placements").child(mAuth.getUid()).addValueEventListener(new ValueEventListener(){
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                                                progressDialog.cancel();
                                                progressDialog.dismiss();

                                                PlacementObject placementObject = dataSnapshot.getValue(PlacementObject.class);

                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("filled_placement", Constants.FILLED);
                                                editor.putString("number_of_weeks", placementObject.getNumberOfWeeks());
                                                editor.apply();

                                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                fragmentTransaction.replace(R.id.container, new FillLogbookFragment());
                                                fragmentTransaction.commit();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

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
                if(isNetworkAvailable(getActivity().getApplicationContext())){
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, new PlacementDetailsFragment());
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });



        supervisor = view.findViewById(R.id.supervisor);
        supervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Not ready yet", Toast.LENGTH_SHORT).show();
//                if(isNetworkAvailable(getActivity().getApplicationContext())){
//                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.container, new SupervisorFragment());
//                    fragmentTransaction.commit();
//                }
//                else{
//                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
//                }
            }
        });


        profile = view.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable(getActivity().getApplicationContext())){
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, new ProfileFragment());
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
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


        pen = view.findViewById(R.id.pen);
        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fill_logbook.performClick();
            }
        });

        house = view.findViewById(R.id.house);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placement_details.performClick();
            }
        });


        eye = view.findViewById(R.id.eye);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supervisor.performClick();
            }
        });

        person = view.findViewById(R.id.person);
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.performClick();
            }
        });

        abt = view.findViewById(R.id.abt);
        abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                about.performClick();
            }
        });

        sout = view.findViewById(R.id.sout);
        sout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signout.performClick();
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