package com.dabinu.app.electroniclogbook.student.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.models.PlacementObject;
import com.dabinu.app.electroniclogbook.student.StudentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class PlacementDetailsFragment extends Fragment implements StudentActivity.IOnBackPressed{

    EditText companyName, companyAddress, companyEmail, supervisorName, supervisorEmail;
    Spinner numWeeks;
    ImageButton back;
    TextView done;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;


    public PlacementDetailsFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_placement_details, container, false);

        init(view);

        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try{
            getPlacementDetails();
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

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new HomeFragment());
                fragmentTransaction.commit();
            }
        });


        companyName = view.findViewById(R.id.companyName);
        companyAddress = view.findViewById(R.id.companyAddress);
        companyEmail = view.findViewById(R.id.companyEmail);
        supervisorName = view.findViewById(R.id.supervisorName);
        supervisorEmail = view.findViewById(R.id.supervisorEmail);

        numWeeks = view.findViewById(R.id.numberOfWeeks);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.period, R.layout.short_spinner);
        numWeeks.setAdapter(arrayAdapter);

        done = view.findViewById(R.id.submit);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(companyName.getText().toString().trim().equals("")){
                    companyName.setError("This field is required");
                }
                else if(companyAddress.getText().toString().trim().equals("")){
                    companyAddress.setError("This field is required");
                }
                else if(companyEmail.getText().toString().trim().equals("")){
                    companyEmail.setError("This field is required");
                }
                else if(supervisorName.getText().toString().trim().equals("")){
                    supervisorName.setError("This field is required");
                }
                else if(supervisorEmail.getText().toString().trim().equals("")){
                    supervisorEmail.setError("This field is required");
                }
                else if(((String) numWeeks.getSelectedItem()).equals("Number of weeks")){
                    Toast.makeText(getActivity().getApplicationContext(), "Select a valid number of weeks", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.setMessage("Uploading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    databaseReference.child("placements").child(mAuth.getUid()).setValue(new PlacementObject(companyName.getText().toString().trim(), companyAddress.getText().toString().trim(), companyEmail.getText().toString().trim(), supervisorName.getText().toString().trim(), supervisorEmail.getText().toString().trim(), numWeeks.getSelectedItem().toString().split(" ")[0])).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task){
                            if(task.isSuccessful()){
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("filled_placement", "yes");
                                editor.putString("number_of_weeks", ((String) numWeeks.getSelectedItem()).split(" ")[0]);
                                editor.apply();

                                databaseReference.child("users").child(mAuth.getUid()).child("filledPlacement").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task){
                                        progressDialog.cancel();
                                        progressDialog.dismiss();

                                        if(task.isSuccessful()){
                                            Toast.makeText(getActivity().getApplicationContext(), "Placement details successfully updated", Toast.LENGTH_SHORT).show();

                                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                            fragmentTransaction.replace(R.id.container, new HomeFragment());
                                            fragmentTransaction.commit();
                                        }
                                        else{
                                            new AlertDialog.Builder(getActivity())
                                                    .setMessage("Failed, try again")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Okay", null)
                                                    .show();
                                        }
                                    }
                                });


                            }
                            else{
                                progressDialog.dismiss();
                                progressDialog.cancel();

                            }
                        }
                    });
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

    void getPlacementDetails(){
        databaseReference.child("placements").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    PlacementObject placementObject = dataSnapshot.getValue(PlacementObject.class);
                    companyName.setText(placementObject.getCompanyName());
                    companyAddress.setText(placementObject.getCompanyAddress());
                    companyEmail.setText(placementObject.getCompanyEmail());
                    supervisorName.setText(placementObject.getSupervisorName());
                    supervisorEmail.setText(placementObject.getSupervisorEmail());
                    List<String> myOptions = Arrays.asList((getResources().getStringArray(R.array.period)));
                    String nj = placementObject.getNumberOfWeeks();
                    nj += " week";
                    if(nj.split(" ")[0] != "1"){
                        nj += "s";
                    }
                    numWeeks.setSelection(myOptions.indexOf(nj));
                }
                catch(Exception e){

                }

                progressDialog.cancel();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}