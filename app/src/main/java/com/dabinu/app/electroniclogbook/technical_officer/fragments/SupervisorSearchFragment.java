package com.dabinu.app.electroniclogbook.technical_officer.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.models.User;
import com.dabinu.app.electroniclogbook.technical_officer.TechnicalOfficerActivity;
import com.dabinu.app.electroniclogbook.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */

public class SupervisorSearchFragment extends Fragment implements TechnicalOfficerActivity.IOnBackPressed{

    ImageButton back;
    EditText staffId;
    Button go;
    TextView name, type, view_profile, title;
    TextView sup_name, sup_phone, sup_email, sup_rank;
    RelativeLayout user_layout, profile, landing;
    ProgressDialog progressDialog;
    CircleImageView stud_image;
    Activity activity;
    DatabaseReference databaseReference;
    boolean bool = false, gothereatall = false;
    int posit = 1;


    public SupervisorSearchFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_supervisor_search, container, false);

        init(view);

        return view;
    }


    public void init(View view){
        activity = getActivity();

        title = view.findViewById(R.id.title);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());

        user_layout = view.findViewById(R.id.user);
        landing = view.findViewById(R.id.landing);
        profile = view.findViewById(R.id.profile);

        name = view.findViewById(R.id.name);
        type = view.findViewById(R.id.type);

        stud_image = view.findViewById(R.id.stud_circle);

        sup_name = view.findViewById(R.id.sup_full_name);
        sup_email = view.findViewById(R.id.sup_email);
        sup_phone = view.findViewById(R.id.sup_phone);
        sup_rank = view.findViewById(R.id.sup_rank);

        view_profile = view.findViewById(R.id.viewProfile);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                switch(posit){
                    case 1:
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, new HomeFragment());
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        title.setText("Supervisor search");
                        landing.setVisibility(View.VISIBLE);
                        profile.setVisibility(View.GONE);
                        posit = 1;
                        break;
                }
            }
        });


        staffId = view.findViewById(R.id.staffID);

        go = view.findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(staffId.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Enter a valid staff ID", Toast.LENGTH_LONG).show();
                }
                else{
                    searchForSupervisor(staffId.getText().toString().trim());
                }
            }
        });



    }


    @Override
    public boolean onBackPressed(){
        switch(posit){
            case 1:
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new HomeFragment());
                fragmentTransaction.commit();
                return true;
            case 2:
                title.setText("Supervisor search");
                landing.setVisibility(View.VISIBLE);
                profile.setVisibility(View.GONE);
                posit = 1;
                return true;
            default:
                return false;
        }
    }


    public void searchForSupervisor(String id){
        user_layout.setVisibility(View.GONE);

        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getStudent(id);
    }


    public void getStudent(final String id){

        bool = false;

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                for(final DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    gothereatall = true;

                    if(!dataSnapshot1.getValue(User.class).getType().equals(Constants.STUDENT) && ((dataSnapshot1.getValue(User.class).getStaffID()).toLowerCase()).equals((id.toLowerCase()))){

                        progressDialog.dismiss();
                        progressDialog.cancel();

                        user_layout.setVisibility(View.VISIBLE);

                        name.setText(String.format("%s", dataSnapshot1.getValue(User.class).getFullname()));
                        type.setText(dataSnapshot1.getValue(User.class).getType());

                        sup_name.setText(dataSnapshot1.getValue(User.class).getFullname());
                        sup_email.setText(dataSnapshot1.getValue(User.class).getEmail());
                        sup_phone.setText(dataSnapshot1.getValue(User.class).getPhone());
                        sup_rank.setText(dataSnapshot1.getValue(User.class).getStaffID());

                        Glide.with(activity.getApplicationContext()).load(dataSnapshot1.getValue(User.class).getPhoto_url()).into(stud_image);

                        view_profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view){
                                title.setText("Profile");
                                landing.setVisibility(View.GONE);
                                profile.setVisibility(View.VISIBLE);
                                posit = 2;
                            }
                        });


                        bool = true;
                        break;
                    }

                }

                if(!bool && gothereatall){
                    progressDialog.cancel();
                    progressDialog.dismiss();
                    new AlertDialog.Builder(getActivity())
                            .setMessage(String.format("No match was found for %s", id.toLowerCase()))
                            .setCancelable(false)
                            .setPositiveButton("Okay", null)
                            .show();
                }

                bool = false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        });
    }


}