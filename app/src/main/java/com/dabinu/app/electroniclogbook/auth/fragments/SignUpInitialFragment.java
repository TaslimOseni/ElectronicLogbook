package com.dabinu.app.electroniclogbook.auth.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpInitialFragment extends Fragment implements AuthActivity.IOnBackPressed{

    TextView header;
    ImageButton back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText stud_fullname, stud_email, stud_matric, stud_level, stud_faculty, stud_department;
    CardView stud_next;
    EditText ind_name, ind_email, ind_phone, ind_rank, ind_staffID;
    CardView ind_next;
    EditText dept_name, dept_email, dept_faculty, dept_dept, dept_staffID;
    CardView dept_next;
    FrameLayout student, ind_supervisor, dept_supervisor;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;


    public SignUpInitialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_initial, container, false);

        progressDialog = new ProgressDialog(getActivity());

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new SignUpAsFragment());
                fragmentTransaction.commit();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        header = view.findViewById(R.id.header);
        header.setText(sharedPreferences.getString("type_of_user", ""));

        student = view.findViewById(R.id.student);
        ind_supervisor = view.findViewById(R.id.ind_supervisor);
        dept_supervisor = view.findViewById(R.id.dept_supervisor);


        switch(sharedPreferences.getString("type_of_user", "")){
            case "Student":
                student.setVisibility(View.VISIBLE);
                ind_supervisor.setVisibility(View.GONE);
                dept_supervisor.setVisibility(View.GONE);
                break;
            case "Department supervisor":
                student.setVisibility(View.GONE);
                ind_supervisor.setVisibility(View.VISIBLE);
                dept_supervisor.setVisibility(View.GONE);
                break;
            case "Industrial supervisor":
                student.setVisibility(View.GONE);
                ind_supervisor.setVisibility(View.GONE);
                dept_supervisor.setVisibility(View.VISIBLE);
                break;
            default:

        }

        stud_fullname = view.findViewById(R.id.stud_fullname);
        stud_email = view.findViewById(R.id.stud_email);
        stud_matric = view.findViewById(R.id.stud_matricnumber);
        stud_level = view.findViewById(R.id.stud_level);
        stud_faculty = view.findViewById(R.id.stud_faculty);
        stud_department = view.findViewById(R.id.stud_dept);
        stud_next = view.findViewById(R.id.stud_next);

        stud_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stud_fullname.getText().toString().trim().equals("")){
                    stud_fullname.setError("This field is required");
                }
                else if(stud_email.getText().toString().trim().equals("")){
                    stud_email.setError("This field is required");
                }
                else if(stud_matric.getText().toString().trim().equals("")){
                    stud_matric.setError("This field is required");
                }
                else if(stud_level.getText().toString().trim().equals("")){
                    stud_level.setError("This field is required");
                }
                else if(stud_faculty.getText().toString().trim().equals("")){
                    stud_faculty.setError("This field is required");
                }
                else if(stud_department.getText().toString().trim().equals("")){
                    stud_department.setError("This field is required");
                }
                else{
                    editor.putString("stud_name", stud_fullname.getText().toString().trim());
                    editor.putString("stud_email", stud_email.getText().toString().trim());
                    editor.putString("stud_matric", stud_matric.getText().toString().trim());
                    editor.putString("stud_level", stud_level.getText().toString().trim());
                    editor.putString("stud_faculty", stud_faculty.getText().toString().trim());
                    editor.putString("stud_dept", stud_department.getText().toString().trim());

                    editor.apply();

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, new SignupNextFragment());
                    fragmentTransaction.commit();

                }
            }
        });





        ind_name = view.findViewById(R.id.ind_name);
        ind_email = view.findViewById(R.id.ind_email);
        ind_phone = view.findViewById(R.id.ind_phone);
        ind_rank = view.findViewById(R.id.ind_rank);
        ind_staffID = view.findViewById(R.id.ind_staffID);
        ind_next = view.findViewById(R.id.ind_next);


        ind_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ind_name.getText().toString().trim().equals("")){
                    ind_name.setError("This field is required");
                }
                else if(ind_email.getText().toString().trim().equals("")){
                    ind_email.setError("This field is required");
                }
                else if(ind_phone.getText().toString().trim().equals("")){
                    ind_phone.setError("This field is required");
                }
                else if(ind_rank.getText().toString().trim().equals("")){
                    ind_rank.setError("This field is required");
                }
                else if(ind_staffID.getText().toString().trim().equals("")){
                    ind_staffID.setError("This field is required");
                }
                else{
                    editor.putString("ind_name", ind_name.getText().toString().trim());
                    editor.putString("ind_email", ind_email.getText().toString().trim());
                    editor.putString("ind_phone", ind_phone.getText().toString().trim());
                    editor.putString("ind_rank", ind_rank.getText().toString().trim());
                    editor.putString("ind_staffID", ind_staffID.getText().toString().trim());

                    editor.apply();

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, new SignupNextFragment());
                    fragmentTransaction.commit();

                }
            }
        });




        dept_name = view.findViewById(R.id.dept_name);
        dept_email = view.findViewById(R.id.dept_email);
        dept_faculty = view.findViewById(R.id.dept_faculty);
        dept_dept = view.findViewById(R.id.dept_dept);
        dept_staffID = view.findViewById(R.id.dept_staffID);
        dept_next = view.findViewById(R.id.dept_next);


        dept_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dept_name.getText().toString().trim().equals("")){
                    dept_name.setError("This field is required");
                }
                else if(dept_email.getText().toString().trim().equals("")){
                    dept_email.setError("This field is required");
                }
                else if(dept_faculty.getText().toString().trim().equals("")){
                    dept_faculty.setError("This field is required");
                }
                else if(dept_dept.getText().toString().trim().equals("")){
                    dept_dept.setError("This field is required");
                }
                else if(dept_staffID.getText().toString().trim().equals("")){
                    dept_staffID.setError("This field is required");
                }
                else{
                    editor.putString("dept_name", dept_name.getText().toString().trim());
                    editor.putString("dept_email", dept_email.getText().toString().trim());
                    editor.putString("dept_faculty", dept_faculty.getText().toString().trim());
                    editor.putString("dept_dept", dept_dept.getText().toString().trim());
                    editor.putString("dept_staffID", dept_staffID.getText().toString().trim());

                    editor.apply();

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, new SignupNextFragment());
                    fragmentTransaction.commit();

                }
            }
        });



        return view;
    }


    @Override
    public boolean onBackPressed() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new SignUpAsFragment());
        fragmentTransaction.commit();
        return true;
    }

}