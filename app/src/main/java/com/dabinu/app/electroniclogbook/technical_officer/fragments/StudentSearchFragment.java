package com.dabinu.app.electroniclogbook.technical_officer.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.dept_supervisor.fragments.AddNewStudentFragment;
import com.dabinu.app.electroniclogbook.models.Log;
import com.dabinu.app.electroniclogbook.models.MyStudents;
import com.dabinu.app.electroniclogbook.models.User;
import com.dabinu.app.electroniclogbook.technical_officer.TechnicalOfficerActivity;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentSearchFragment extends Fragment implements TechnicalOfficerActivity.IOnBackPressed{


    EditText matric;
    TextView name, department, faculty, viewProfile, viewLogs, title;
    TextView stud_name, stud_email, stud_matric, stud_level, stud_dept_supervisor, stud_dept, stud_faculty;
    CircleImageView stud_image;
    Button go;
    ImageButton back;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    RelativeLayout user_layout, landing, profile;
    LinearLayout sneakPeak;
    Spinner week, day;
    EditText activity, comment;
    boolean bool = false;
    int position = 1;


    public StudentSearchFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_student_search, container, false);

        init(view);

        return view;
    }


    @Override
    public boolean onBackPressed(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch(position){
            case 1:
                fragmentTransaction.replace(R.id.container, new HomeFragment());
                fragmentTransaction.commit();
                return true;
            case 2:
                title.setText("Student search");
                landing.setVisibility(View.VISIBLE);
                profile.setVisibility(View.GONE);
                position = 1;
                return true;
            case 3:
                title.setText("Student search");
                landing.setVisibility(View.VISIBLE);
                sneakPeak.setVisibility(View.GONE);
                position = 1;
                return true;
        }

        return false;
    }


    public void init(View view){


        day = view.findViewById(R.id.day);
        week = view.findViewById(R.id.week);

        title = view.findViewById(R.id.title);

        landing = view.findViewById(R.id.landing);
        profile = view.findViewById(R.id.profile);

        sneakPeak = view.findViewById(R.id.sneakPeek);

        stud_name = view.findViewById(R.id.stud_full_name);
        stud_email = view.findViewById(R.id.stud_email);
        stud_matric = view.findViewById(R.id.stud_matric_number);
        stud_level = view.findViewById(R.id.stud_level);
        stud_dept_supervisor = view.findViewById(R.id.stud_departmental_supervisor);
        stud_dept = view.findViewById(R.id.stud_department);
        stud_faculty = view.findViewById(R.id.stud_faculty);
        stud_image = view.findViewById(R.id.stud_circle);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                switch(position){
                    case 1:
                        fragmentTransaction.replace(R.id.container, new HomeFragment());
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        title.setText("Student search");
                        landing.setVisibility(View.VISIBLE);
                        profile.setVisibility(View.GONE);
                        position = 1;
                        break;
                    case 3:
                        title.setText("Student search");
                        landing.setVisibility(View.VISIBLE);
                        sneakPeak.setVisibility(View.GONE);
                        position = 1;
                        break;
                }
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());

        user_layout = view.findViewById(R.id.user);

        name = view.findViewById(R.id.name);
        department = view.findViewById(R.id.department);
        faculty = view.findViewById(R.id.faculty);


        viewProfile = view.findViewById(R.id.viewProfile);
        viewLogs = view.findViewById(R.id.viewLogs);

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

                for(final DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    if(dataSnapshot1.getValue(User.class).getType().equals(Constants.STUDENT) && ((dataSnapshot1.getValue(User.class).getMatric()).toLowerCase()).equals((matric.getText().toString().trim()).toLowerCase())){

                        progressDialog.dismiss();
                        progressDialog.cancel();

                        user_layout.setVisibility(View.VISIBLE);

                        name.setText(String.format("%s (%s)", dataSnapshot1.getValue(User.class).getFullname(), dataSnapshot1.getValue(User.class).getMatric()));
                        department.setText(dataSnapshot1.getValue(User.class).getDepartment());
                        faculty.setText(dataSnapshot1.getValue(User.class).getFaculty());

                        stud_name.setText(dataSnapshot1.getValue(User.class).getFullname());
                        stud_email.setText(dataSnapshot1.getValue(User.class).getEmail());
                        stud_matric.setText(dataSnapshot1.getValue(User.class).getMatric());
                        stud_level.setText(dataSnapshot1.getValue(User.class).getLevel());
                        stud_dept_supervisor.setText(dataSnapshot1.getValue(User.class).getSupervisor_id());
                        stud_dept.setText(dataSnapshot1.getValue(User.class).getDepartment());
                        stud_faculty.setText(dataSnapshot1.getValue(User.class).getFaculty());

                        Glide.with(getActivity().getApplicationContext()).load(dataSnapshot1.getValue(User.class).getPhoto_url()).into(stud_image);

                        viewProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view){
                                position = 2;
                                title.setText("Profile");
                                landing.setVisibility(View.GONE);
                                profile.setVisibility(View.VISIBLE);


                            }
                        });

                        viewLogs.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){

                                position = 3;
                                title.setText("Logs");

                                landing.setVisibility(View.GONE);
                                sneakPeak.setVisibility(View.VISIBLE);

                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);

                                ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.day, R.layout.short_spinner);
                                day.setAdapter(arrayAdapter);

                                ArrayList<String> numb = new ArrayList<>();
                                numb.add("Week");
                                for(int i = 1; i <= 8; i++){
                                    numb.add(String.format("Week %d", i));
                                }
                                ArrayAdapter arrayAdapter1 = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.short_spinner, numb);
                                week.setAdapter(arrayAdapter1);

                                activity = view.findViewById(R.id.activity);
                                comment = view.findViewById(R.id.comment);


                                day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if(i != 0 && !(((String) week.getSelectedItem()).equals("Week"))){
                                            fillInDetails(dataSnapshot1.getKey());
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });


                                week.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if(i != 0 && !(((String) day.getSelectedItem()).equals("Day"))){
                                            fillInDetails(dataSnapshot1.getKey());
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                        });

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


    public void fillInDetails(String uid){

        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference.child("logs").child(uid).child(((String) week.getSelectedItem()).split(" ")[1]).child(((String) day.getSelectedItem())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    Log log = dataSnapshot.getValue(Log.class);

                    activity.setText(log.getActivity());
                    comment.setText(log.getComment());
                }
                catch(Exception e){
                    progressDialog.dismiss();
                    progressDialog.cancel();

                    new AlertDialog.Builder(getActivity())
                            .setMessage("This student has not filled his log book for this day")
                            .setCancelable(false)
                            .setPositiveButton("Okay", null)
                            .show();
                }

                progressDialog.dismiss();
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                progressDialog.dismiss();
                progressDialog.cancel();
            }
        });
    }

}