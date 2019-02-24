package com.dabinu.app.electroniclogbook.student.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.dabinu.app.electroniclogbook.models.Log;
import com.dabinu.app.electroniclogbook.student.StudentActivity;
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
public class FillLogbookFragment extends Fragment implements StudentActivity.IOnBackPressed{


    Spinner week, day;
    EditText activity, comment;
    ImageButton back;
    Button submit;
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    boolean shouldSubmit = true;


    public FillLogbookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fill_logbook, container, false);

        init(view);

        return view;
    }


    private boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    public void init(View view){
        sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new HomeFragment());
                fragmentTransaction.commit();
            }
        });

        submit = view.findViewById(R.id.submit);

        day = view.findViewById(R.id.day);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.day, R.layout.short_spinner);
        day.setAdapter(arrayAdapter);

        week = view.findViewById(R.id.week);
        ArrayList<String> numb = new ArrayList<>();
        numb.add("Week");
        for(int i = 1; i <= Integer.parseInt(sharedPreferences.getString("number_of_weeks", "6")); i++){
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
                    fillInDetails();
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
                    fillInDetails();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable(getActivity().getApplicationContext())){
                    if(shouldSubmit){
                        if(comment.getText().toString().trim().equals("")){
                            comment.setError("This field cannot be empty");
                        }
                        else if(activity.getText().toString().trim().equals("")){
                            activity.setError("This field cannot be empty");
                        }
                        else if(((String) week.getSelectedItem()).equals("Week")){
                            Toast.makeText(getActivity().getApplicationContext(), "Select a valid week", Toast.LENGTH_SHORT).show();
                        }
                        else if(((String) day.getSelectedItem()).equals("Day")){
                            Toast.makeText(getActivity().getApplicationContext(), "Select a valid day", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressDialog.setMessage("Uploading..");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            databaseReference.child("logs").child(mAuth.getUid()).child(((String) week.getSelectedItem()).split(" ")[1]).child(((String) day.getSelectedItem())).setValue(new Log(((String) week.getSelectedItem()).split(" ")[0], (String) day.getSelectedItem(), activity.getText().toString().trim(), comment.getText().toString().trim(), true)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.cancel();
                                        progressDialog.dismiss();

                                        Toast.makeText(getActivity().getApplicationContext(), String.format("%s, %s successfully updated", (String) day.getSelectedItem(), (String) week.getSelectedItem()), Toast.LENGTH_LONG).show();

                                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.container, new HomeFragment());
                                        fragmentTransaction.commit();
                                    }
                                    else{

                                    }
                                }
                            });
                        }
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(),"This day has already been filled and cannot be edited", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public boolean onBackPressed() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new HomeFragment());
        fragmentTransaction.commit();
        return true;
    }


    public void fillInDetails(){

        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference.child("logs").child(mAuth.getUid()).child(((String) week.getSelectedItem()).split(" ")[1]).child(((String) day.getSelectedItem())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    Log log = dataSnapshot.getValue(Log.class);
                    if(log == null){
                    }
                    activity.setText(log.getActivity());
                    comment.setText(log.getComment());
                    if(log.isFilledAlready()){
                        shouldSubmit = false;
                    }
                }
                catch(Exception e){
                    activity.setText("");
                    comment.setText("");
                    shouldSubmit = true;
                    progressDialog.dismiss();
                    progressDialog.cancel();
                }


                progressDialog.dismiss();
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                progressDialog.dismiss();
                progressDialog.cancel();
                shouldSubmit = true;
            }
        });
    }

}