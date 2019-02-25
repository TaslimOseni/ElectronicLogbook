package com.dabinu.app.electroniclogbook.dept_supervisor.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.adapters.MyStudentsAdapter;
import com.dabinu.app.electroniclogbook.dept_supervisor.DeptSupervisorActivity;
import com.dabinu.app.electroniclogbook.models.MyStudents;
import com.dabinu.app.electroniclogbook.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentsFragment extends Fragment implements DeptSupervisorActivity.IOnBackPressed{

    ImageButton back;
    TextView add_new, no;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ArrayList<User> allUsers;
    Activity activity;


    public StudentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students, container, false);

        init(view);

        progressDialog.setMessage("Fetching students...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        fetchStudents();

        return view;
    }


    public void init(View view){
        activity = getActivity();

        allUsers = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new HomeFragment());
                fragmentTransaction.commit();
            }
        });

        no = view.findViewById(R.id.no);


        add_new = view.findViewById(R.id.add_new_student);
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new AddNewStudentFragment());
                fragmentTransaction.commit();
            }
        });


        fab = view.findViewById(R.id.fab_add_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_new.performClick();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

    }


    public void fetchStudents(){
        databaseReference.child("offsprings").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                try{
                    MyStudents myStudents = dataSnapshot.getValue(MyStudents.class);

                    if(myStudents.getMystudents().isEmpty()){
                        Log.d("CHECK_POINT", "here");
                        no.setVisibility(View.VISIBLE);
                        progressDialog.cancel();
                        progressDialog.dismiss();
                    }
                    else{
                        Log.d("CHECK_POINT", "here1");
                        for(String id: myStudents.getMystudents()){
                            databaseReference.child("users").child(id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                    try{
                                        Log.d("CHECK_POINT", "here2");
                                        allUsers.add(dataSnapshot.getValue(User.class));
                                        Log.d("CHECK_POINT", "here3");
                                    }
                                    catch(Exception e){

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError){
                                    progressDialog.cancel();
                                    progressDialog.dismiss();

                                    new AlertDialog.Builder(getActivity())
                                            .setMessage("Failed, try again?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                    fragmentTransaction.replace(R.id.container, new StudentsFragment());
                                                    fragmentTransaction.commit();
                                                }
                                            })
                                            .setNegativeButton("Cancel", null)
                                            .setCancelable(false)
                                            .show();
                                }
                            });
                        }

                        new CountDownTimer(10000, 1000){
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                if(allUsers.isEmpty()){
                                    Log.d("CHECK_POINT", "here9");
                                    no.setVisibility(View.VISIBLE);
                                    progressDialog.cancel();
                                    progressDialog.dismiss();
                                }
                                else{
                                    progressDialog.cancel();
                                    progressDialog.dismiss();
                                    Log.d("CHECK_POINT", "here10");
                                    MyStudentsAdapter myStudentsAdapter = new MyStudentsAdapter(activity.getApplicationContext(), allUsers);
                                    myStudentsAdapter.setOnItemClickListener(new MyStudentsAdapter.ClickListener() {
                                        @Override
                                        public void onItemClick(int position, View v) {

                                        }

                                        @Override
                                        public void onItemLongClick(int position, View v) {

                                        }
                                    });
                                    recyclerView.setAdapter(myStudentsAdapter);
                                }
                            }
                        }.start();
                    }
                }
                catch(Exception e){
                    no.setVisibility(View.VISIBLE);
                    progressDialog.cancel();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                progressDialog.cancel();
                progressDialog.dismiss();
                new AlertDialog.Builder(getActivity())
                        .setMessage("Failed, try again?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.container, new StudentsFragment());
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setCancelable(false)
                        .show();
            }
        });
    }


    @Override
    public boolean onBackPressed(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new com.dabinu.app.electroniclogbook.dept_supervisor.fragments.HomeFragment());
        fragmentTransaction.commit();
        return true;
    }

}