package com.dabinu.app.electroniclogbook.technical_officer.fragments;


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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.dabinu.app.electroniclogbook.exit.ExitActivity;
import com.dabinu.app.electroniclogbook.technical_officer.TechnicalOfficerActivity;
import com.dabinu.app.electroniclogbook.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements TechnicalOfficerActivity.IOnBackPressed{


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        view.findViewById(R.id.stud).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new StudentSearchFragment());
                fragmentTransaction.commit();
            }
        });

        view.findViewById(R.id.sup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.container, new SupervisorSearchFragment());
//                fragmentTransaction.commit();
            }
        });

        view.findViewById(R.id.signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable(getActivity().getApplicationContext())){

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("login", Constants.NOT_LOGGED_IN);
                            editor.putString("weeks", Constants.DEFAULT_FILL);
                            editor.putString("filled_placement", Constants.DEFAULT_FILL);
                            editor.putString("statuigba", "no_gba");
                            editor.apply();

                            startActivity(new Intent(getActivity().getApplicationContext(), AuthActivity.class));

                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
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


    private boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


}