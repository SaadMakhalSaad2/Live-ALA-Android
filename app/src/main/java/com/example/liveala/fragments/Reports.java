package com.example.liveala.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.liveala.R;

public class Reports extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reports, container, false);

        linkUi(root);
        return root;

    }

    private void linkUi(View root) {
        LinearLayout gridView = root.findViewById(R.id.grid_view);
        Animation fade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
        gridView.startAnimation(fade);

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        root.findViewById(R.id.titans).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("titans");
            action.setHallName("titans");
            navController.navigate(action);
        });
        root.findViewById(R.id.athens).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("athena");
            action.setHallName("athena");
            navController.navigate(action);
        });

        root.findViewById(R.id.clasified).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("classified");
            action.setHallName("classified");
            navController.navigate(action);
        });

        root.findViewById(R.id.gaga).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("gaga");
            action.setHallName("gaga");
            navController.navigate(action);
        });

        root.findViewById(R.id.jeshi).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("jeshi");
            action.setHallName("jeshi");
            navController.navigate(action);
        });
        root.findViewById(R.id.keza).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("keza");
            action.setHallName("keza");
            navController.navigate(action);
        });
        root.findViewById(R.id.kushinga).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("kushinga");
            action.setHallName("kushinga");
            navController.navigate(action);
        });
        root.findViewById(R.id.malaika).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("malaika");
            action.setHallName("malaika");
            navController.navigate(action);
        });
        root.findViewById(R.id.office).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("office");
            action.setHallName("office");
            navController.navigate(action);
        });
        root.findViewById(R.id.olympus).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("olympus");
            action.setHallName("olympus");
            navController.navigate(action);
        });
        root.findViewById(R.id.twawana).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("twawana");
            action.setHallName("twawana");
            navController.navigate(action);
        });
        root.findViewById(R.id.valkyrie).setOnClickListener(v -> {
            ReportsDirections.ActionReportsToReportsDetailed action = ReportsDirections.actionReportsToReportsDetailed("valkyrie");
            action.setHallName("valkyrie");
            navController.navigate(action);
        });


    }

}