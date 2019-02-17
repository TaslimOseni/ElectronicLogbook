package com.dabinu.app.electroniclogbook.auth.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.dabinu.app.electroniclogbook.auth.fragments.LoginFragment;
import com.dabinu.app.electroniclogbook.auth.fragments.SignUpAsFragment;
import com.dabinu.app.electroniclogbook.exit.ExitActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment implements AuthActivity.IOnBackPressed{

    CardView loginCard, signupCard;

    public WelcomeFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        loginCard = view.findViewById(R.id.loginCard);
        loginCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new LoginFragment());
                fragmentTransaction.commit();
            }
        });

        signupCard = view.findViewById(R.id.signupCard);
        signupCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new SignUpAsFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }


    @Override
    public boolean onBackPressed(){
        new AlertDialog.Builder(getActivity())
                .setMessage("Exit?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ExitActivity.exit(getActivity().getApplicationContext());
                    }
                })
                .setNegativeButton("No", null)
                .show();
        return true;

    }
}