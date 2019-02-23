package com.dabinu.app.electroniclogbook.dept_supervisor.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.adapters.MyStudentsAdapter;
import com.dabinu.app.electroniclogbook.dept_supervisor.DeptSupervisorActivity;
import com.dabinu.app.electroniclogbook.models.MyStudents;
import com.dabinu.app.electroniclogbook.models.User;
import com.dabinu.app.electroniclogbook.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class AddNewStudentFragment extends Fragment implements DeptSupervisorActivity.IOnBackPressed{

    EditText matric;
    TextView name, department, faculty, claimStud;
    Button go;
    ImageButton back;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ArrayList<User> allStudents;
    ArrayList<String> uselessStudents;
    RelativeLayout user_layout;
    boolean bool = false;

    public AddNewStudentFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_add_new_student, container, false);

        init(view);

        return view;
    }


    public void init(View view){
        allStudents = new ArrayList<>();

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new com.dabinu.app.electroniclogbook.dept_supervisor.fragments.StudentsFragment());
                fragmentTransaction.commit();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());

        user_layout = view.findViewById(R.id.user);

        name = view.findViewById(R.id.name);
        department = view.findViewById(R.id.department);
        faculty = view.findViewById(R.id.faculty);


        claimStud = view.findViewById(R.id.claimStud);

        matric = view.findViewById(R.id.matric);
        go = view.findViewById(R.id.go);

        go.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                user_layout.setVisibility(View.GONE);

                if(matric.getText().toString().trim().length() != 12){
                    matric.setError("Incomplete matric number");
                }
                else{
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    getStudent();
                }
            }
        });
    }


    public void getStudent(){

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    allStudents.add(dataSnapshot1.getValue(User.class));

                    if(dataSnapshot1.getValue(User.class).getType().equals(Constants.STUDENT) && ((dataSnapshot1.getValue(User.class).getMatric()).toLowerCase()).equals((matric.getText().toString().trim()).toLowerCase())){
                        makeUserVisible(dataSnapshot1);
                        bool = true;
                        break;
                    }

                }

                if(!bool){
                    progressDialog.cancel();
                    progressDialog.dismiss();
                    new AlertDialog.Builder(getActivity())
                            .setMessage(String.format("No match was found for %s", matric.getText().toString().trim().toLowerCase()))
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


    public void makeUserVisible(final DataSnapshot dataSnapshot){
        progressDialog.dismiss();
        progressDialog.cancel();

        user_layout.setVisibility(View.VISIBLE);

        name.setText(String.format("%s (%s)", dataSnapshot.getValue(User.class).getFullname(), dataSnapshot.getValue(User.class).getMatric()));
        department.setText(dataSnapshot.getValue(User.class).getDepartment());
        faculty.setText(dataSnapshot.getValue(User.class).getFaculty());

        claimStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                uselessStudents = new ArrayList<>();

                uselessStudents.add(dataSnapshot.getKey());

                fetchStudents();

                databaseReference.child("offsprings").child(mAuth.getUid()).setValue(new MyStudents(uselessStudents)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.cancel();
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressDialog.cancel();
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public void fetchStudents(){
        databaseReference.child("offsprings").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                try{
                    MyStudents myStudents = dataSnapshot.getValue(MyStudents.class);
                    for(String i: myStudents.getMystudents()){
                        uselessStudents.add(i);
                    }
                    }
                catch(Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                progressDialog.cancel();
                progressDialog.dismiss();
                new AlertDialog.Builder(getActivity())
                        .setMessage("Failed")
                        .setPositiveButton("Okay", null)
                        .setCancelable(false)
                        .show();
            }
        });
    }


    @Override
    public boolean onBackPressed(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new com.dabinu.app.electroniclogbook.dept_supervisor.fragments.StudentsFragment());
        fragmentTransaction.commit();
        return true;
    }
}