package com.dabinu.app.electroniclogbook.student.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.models.User;
import com.dabinu.app.electroniclogbook.student.StudentActivity;
import com.dabinu.app.electroniclogbook.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MySupervisorFragment extends Fragment implements StudentActivity.IOnBackPressed{

    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    ScrollView scrollView;
    TextView fullName, email, staffID, dept, faculty, noSupervisor;
    String photo_url;
    CircleImageView circleImageView;
    ImageButton back;


    public MySupervisorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_my_supervisor, container, false);

        init(view);

        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try{
            getUId();
        }
        catch(Exception e){
            progressDialog.cancel();
            progressDialog.dismiss();
        }

        return view;
    }



    public void init(View view){
        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        scrollView = view.findViewById(R.id.supervisorView);
        noSupervisor = view.findViewById(R.id.noSupervisors);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new com.dabinu.app.electroniclogbook.dept_supervisor.fragments.HomeFragment());
                fragmentTransaction.commit();
            }
        });

        fullName = view.findViewById(R.id.fullName);
        email = view.findViewById(R.id.email);
        staffID = view.findViewById(R.id.staffID);
        dept = view.findViewById(R.id.department);
        faculty = view.findViewById(R.id.faculty);

        circleImageView = view.findViewById(R.id.circle);

    }


    public void fetchDetails(String uid){
        databaseReference.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    scrollView.setVisibility(View.VISIBLE);

                    User user = dataSnapshot.getValue(User.class);

                    fullName.setText(user.getFullname());
                    email.setText(user.getEmail());
                    staffID.setText(user.getStaffID());
                    dept.setText(user.getDepartment());
                    faculty.setText(user.getFaculty());
                    photo_url = user.getPhoto_url();

                    Glide.with(getActivity()).load(photo_url).into(circleImageView);

                }
                catch(Exception e){
                    progressDialog.cancel();
                    progressDialog.dismiss();
                }

                progressDialog.cancel();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
                progressDialog.dismiss();
            }
        });
    }


    public void getUId(){
        databaseReference.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    User user = dataSnapshot.getValue(User.class);
                    if(user.getSupervisor_id().equals(Constants.NOT_FILLED)){
                        progressDialog.cancel();
                        progressDialog.dismiss();

                        noSupervisor.setVisibility(View.VISIBLE);
                    }
                    else{
                        fetchDetails(user.getSupervisor_id());
                    }


                }
                catch(Exception e){
                    progressDialog.cancel();
                    progressDialog.dismiss();

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, new HomeFragment());
                    fragmentTransaction.commit();
                }

                progressDialog.cancel();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public boolean onBackPressed(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new HomeFragment());
        fragmentTransaction.commit();

        return true;
    }


}