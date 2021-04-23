package com.example.liveala.fragments;

import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.liveala.R;
import com.google.android.material.snackbar.Snackbar;


public class NewInspection extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_new_inspection, container, false);

        linkUi(root);

        return root;
    }

    private void linkUi(View root) {
        LinearLayout gridView = root.findViewById(R.id.grid_view);
        Animation fade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
        gridView.startAnimation(fade);

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        root.findViewById(R.id.titans).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("titans");
                action.setHallName("titans");
                navController.navigate(action);
            }
        });
        root.findViewById(R.id.athens).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("athens");
                action.setHallName("athena");
                navController.navigate(action);
            }
        });

        root.findViewById(R.id.clasified).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("clasified");
                action.setHallName("classified");
                navController.navigate(action);
            }
        });

        root.findViewById(R.id.gaga).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("gaga");
                action.setHallName("gaga");
                navController.navigate(action);
            }
        });

        root.findViewById(R.id.jeshi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("jeshi");
                action.setHallName("jeshi");
                navController.navigate(action);
            }
        });
        root.findViewById(R.id.keza).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("keza");
                action.setHallName("keza");
                navController.navigate(action);
            }
        });
        root.findViewById(R.id.kushinga).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("kushinga");
                action.setHallName("kushinga");
                navController.navigate(action);
            }
        });
        root.findViewById(R.id.malaika).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("malaika");
                action.setHallName("malaika");
                navController.navigate(action);
            }
        });
        root.findViewById(R.id.office).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("office");
                action.setHallName("office");
                navController.navigate(action);
            }
        });
        root.findViewById(R.id.olympus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("olympus");
                action.setHallName("olympus");
                navController.navigate(action);
            }
        });
        root.findViewById(R.id.twawana).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("twawana");
                action.setHallName("twawana");
                navController.navigate(action);
            }
        });
        root.findViewById(R.id.valkyrie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInspectionDirections.ActionNewInspectionToAddNewInspection action = NewInspectionDirections.actionNewInspectionToAddNewInspection("valkyrie");
                action.setHallName("valkyrie");
                navController.navigate(action);
            }
        });


    }
}